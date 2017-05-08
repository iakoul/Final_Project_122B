package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminConsole
 */
@WebServlet("/AdminConsole")
public class AdminConsole extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public AdminConsole() {
        super();
    }
    
    public void destroy() {
    	try {
    		connection.close();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>\n"
	    		+ "<html>\n"
	    		+ "<head>\n"
	    		+ "<title>"
	    		+ "Admin Console"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
		    Integer itemsInCart = 0;
		    if ((HashMap<ArrayList<String>, Integer>)session.getAttribute("cart") != null) {
		    	HashMap<ArrayList<String>, Integer> cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
		    	itemsInCart = cart.size();
		    }
		    out.println("<div align=\"right\"><a href=\"./shoppingCart\">Cart(" + itemsInCart + ")</a></div>");
		    out.println("<div align=\"right\"><a href=\"./mainPage\">Main Page</a></div>");
			//Incorporate mySQL driver
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			
			out.println("<div style=\"text-align:center;\">\n");
			out.println("<h3>Welcome, " + session.getAttribute("firstname") + " " + session.getAttribute("lastname") + " to the administration control panel.</h3>\n");
			out.println("</div>\n");
			
			out.println("<div style=\"float:left;\">\n");
			out.println("<p style=\"text-align:center;\"><b>Table metadata</b></p>\n");
			
			try {
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				String prepQuery = "SELECT i.table_name, i.column_name, i.data_type FROM information_schema.COLUMNS i where table_schema = 'storemarketing';";
				PreparedStatement pstmt = connection.prepareStatement(prepQuery);
				ResultSet results = pstmt.executeQuery();
				
				String tableName = null;
				out.println("<table style=\"text-align: left; width: 100%; border: 1px solid black;\">\n");
				while (results.next()) {
					
					if (!results.getString(1).equals(tableName)) {
						tableName = results.getString(1);
						out.println("<tr>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">" + tableName + "</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Column Name</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Column Type</th>\n");
						out.println("</tr>\n");
					}
					
					out.println("<tr>\n");
					out.println("<td></td><td>" + results.getString(2) + "</td><td>" + results.getString(3) + "</td>\n");
				}
				out.println("</table>");
			} catch (SQLException e) {
				System.out.println("Failed to retrieve metadata with error " + e.getMessage());
			}
			out.println("</div>\n");
			
			out.println("<div style=\"float:right; border-style:dashed;\">");
			out.println("<p style=\"text-align:center;\"><b>Add New Item or Update Existing Item</b></p>\n");
			out.println("<form id=\"form\" action=\"./adminUpdate\" method=\"get\">\n"
					+ "Item Name\n"
					+ "<br><input type=\"text\" name=\"itemname\" value=\"\"/><br>\n"
					+ "Item ID\n"
					+ "<br><input type=\"text\" name=\"itemid\" value=\"\"/><br>\n"
					+ "Price\n"
					+ "<br><input type=\"text\" name=\"price\" value=\"\"/><br>\n"
					+ "Type\n"
					+ "<br><input type=\"text\" name=\"type\" value=\"\"/><br>\n"
					+ "Store\n"
					+ "<br><input type=\"text\" name=\"store\" value=\"\"/><br>\n"
					+ "<br><input type=\"radio\" name=\"addorrem\" value=\"add\" checked/>Add to Store<br>\n"
					+ "<br><input type=\"radio\" name=\"addorrem\" value=\"remove\"/>Remove From Store<br>\n"
					+ "<br><input type=\"submit\" value=\"Add or Update\"><br>\n"
					+ "</form>");
			out.println("</div>");
			
			out.println("</body>\n</html>");
		} else {
			out.println("</body>\n</html>");
			response.setHeader("Refresh", "3; URL=./index.html");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
