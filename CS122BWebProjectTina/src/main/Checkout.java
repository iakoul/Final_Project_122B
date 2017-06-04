package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//Connection connection = null;
    public Checkout() {
        super();
    }

    public void destroy() {
    	/*
    	try {
    		connection.close();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	*/
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Refresh", "3; URL=./shoppingCart");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>\n"
	    		+ "<html>\n"
	    		+ "<head>\n"
	    		+ "<title>"
	    		+ "Checkout"
	    		+ "</title>\n"
	    		+ "</head>\n");
	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
			if (session.getAttribute("isAdmin") != null && (Boolean)session.getAttribute("isAdmin")) {
	    		out.println("<div align=\"right\"><a href=\"./adminConsole\">Admin</a></div>");
	    	}
			//Incorporate mySQL driver
			try {
				//Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			
			try {
				//connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);
				
				Context initCtx = new InitialContext();
	            if (initCtx == null)
	                out.println("initCtx is NULL");

	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                out.println("envCtx is NULL");

	            // Look up our data source
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/storemarketing");

	            if (ds == null)
	                out.println("ds is null.");

	            Connection dbcon = ds.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
				
				String prepQuery = "SELECT c.CCID, "
						+ "c.firstName, "
						+ "c.lastName, "
						+ "c.expiration "
						+ "FROM "
						+ "CreditCardsTbl c "
						+ "WHERE "
						+ "c.CCID = ?;";
				//PreparedStatement pstmt = connection.prepareStatement(prepQuery);
				PreparedStatement pstmt = dbcon.prepareStatement(prepQuery);
				pstmt.setString(1, request.getParameter("ccnum").toString());
				ResultSet results = pstmt.executeQuery();
				
				if (results.next()) {
					if (results.getString(2).equals(request.getParameter("firstname")) 
							&& results.getString(3).equals(request.getParameter("lastname")) 
							&& results.getString(4).equals(request.getParameter("expdate"))) {
						HashMap<ArrayList<String>, Integer> cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
						Iterator<Entry<ArrayList<String>, Integer>> it = cart.entrySet().iterator();
					    while (it.hasNext()) {
					        HashMap.Entry<ArrayList<String>, Integer> pair = (HashMap.Entry<ArrayList<String>, Integer>)it.next();
					        prepQuery = "INSERT "
									+ "INTO "
									+ "ItemsSoldTbl "
									+ "VALUES ("
									+ "?, " //username
									+ "null, " //date time, defaults to current time
									+ "?, " //ccid
									+ "?, " //merchID
									+ "?, " //storeID
									+ "?" //qty
									+ ");";
							//pstmt = connection.prepareStatement(prepQuery);
					        pstmt = dbcon.prepareStatement(prepQuery);
							pstmt.setString(1, session.getAttribute("username").toString());
							pstmt.setString(2, request.getParameter("ccnum").toString());
							ArrayList<String> key = (ArrayList<String>)pair.getKey();
							pstmt.setString(3, key.get(1).toString());
							pstmt.setString(4, key.get(0).toString());
							pstmt.setString(5, pair.getValue().toString());
							pstmt.executeUpdate();
					        it.remove(); // avoids a ConcurrentModificationException
					    }
						out.println("<p>Purchase complete. Thank you for shopping with us. You will now be redirected. <p>");
						cart.clear();
						out.println("</body>\n</html>");
						response.setHeader("Refresh", "3; URL=./Main.jsp");
					} else {
						out.println("<p>Sorry, your credit card was declined. You will be returned to the shopping cart.</p>");
						out.println("</body>\n</html>");
						response.setHeader("Refresh", "3; URL=./shoppingCart");
					}
				} else {
					out.println("<p>Sorry, your credit card was declined. You will be returned to the shopping cart.</p>");
					out.println("</body>\n</html>");
					response.setHeader("Refresh", "3; URL=./shoppingCart");
				}
				dbcon.close();
				pstmt.close();
			} catch (Exception e) {
				out.println(e.getMessage());
			}
		}
		
	}

}
