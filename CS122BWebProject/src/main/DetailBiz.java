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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DetailBiz
 */
@WebServlet("/DetailBiz")
public class DetailBiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Connection connection = null;
    public DetailBiz() {
        super();
        // TODO Auto-generated constructor stub
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
	    		+ "Business Detail"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    Integer itemsInCart = 0;
	    if ((HashMap<ArrayList<String>, Integer>)session.getAttribute("cart") != null) {
	    	HashMap<ArrayList<String>, Integer> cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
	    	itemsInCart = cart.size();
	    }
	    out.println("<div align=\"right\"><a href=\"./shoppingCart\">Cart(" + itemsInCart + ")</a></div>");
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
			//Incorporate mySQL driver
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			
			//connect to the database
			try {
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				try {
					String prepQuery = "SELECT "
							+ "s.storeName, "
							+ "s.address, "
							+ "c.cityName, "
							+ "p.plazaName, "
							+ "s.phoneNum, "
							+ "s.yearOpened, "
							+ "t.storeType, "
							+ "o.name, "
							+ "p.plazaID, "
							+ "c.cityID, "
							+ "m.merchID, "
							+ "m.merchName "
							+ "FROM "
							+ "StoreTbl s, "
							+ "OwnerTbl o, "
							+ "StoreTypeTbl t, "
							+ "PlazaTbl p, "
							+ "CityTbl c, "
							+ "StoreSellsTbl se, "
							+ "MerchandiseTbl m "
							+ "WHERE "
							+ "s.storeID = ? "
							+ "AND s.ownerID = o.ownerID "
							+ "AND s.typeID = t.typeID "
							+ "AND s.plazaID = p.plazaID "
							+ "AND p.cityID = c.cityID "
							+ "AND s.storeID = se.storeID "
							+ "AND se.merchID = m.merchID;";
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					pstmt.setString(1, request.getParameter("bizid"));
					ResultSet results = pstmt.executeQuery();
					out.println("<div style=\"padding-left: 5%; padding-top: 5%;\">\n");
					if (!results.next()) {
						out.println("<p>Business data is missing from database.</p>\n");
					} else {
						out.println("<h1>" + results.getString(1) + "</h1>\n");
						out.println("<p> <b>Address: </b>" + results.getString(2) + "</p>\n");
						out.println("<p> <b>City: </b><a href=\"./detailCity?cityid=" + results.getString(10) + "\">" + results.getString(3) + "</a></p>\n");
						out.println("<p> <b>Plaza: </b><a href=\"./detailPlaza?plazaid=" + results.getString(9) + "\">" + results.getString(4) + "</a></p>\n");
						out.println("<p> <b>Phone Number: </b>" + results.getString(5) + "</p>\n");
						out.println("<p> <b>Since: </b>" + results.getString(6) + "</p>\n");
						out.println("<p> <b>Business Type: </b>" + results.getString(7) + "</p>\n");
						out.println("<p> <b>Owner: </b>" + results.getString(8) + "</p>\n");
						out.println("<p> <b>Merchandise available at this store: </b></p>\n");
						out.println("<ul>\n");
						out.println("<li><a href=\"./detailItem?itemid=" + results.getString(11) + "\">" + results.getString(12) + "</a></li>\n");
						while(results.next()) {
							out.println("<li><a href=\"./detailItem?itemid=" + results.getString(11) + "\">" + results.getString(12) + "</a></li>\n");
						}
						out.println("</ul>\n");
					}
					out.println("<button onclick=\"goBack()\">Go Back</button>");
					out.println("</div>");
					
					
					out.println("<script language=\"javascript\">\n");
					out.println("function goBack() {window.history.back();}\n");
					out.println("</script>\n");
					out.println("<a href=\"./mainPage\">Main Page</a>\n");
					out.println("</body>\n</html>");
				} catch (Exception e) {
					out.println(e.getMessage());
				}
			} catch (SQLException e) {
				out.println(e.getMessage());
			}
		} else {
			out.println("</body>\n</html>");
			response.setHeader("Refresh", "3; URL=./index.html");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
