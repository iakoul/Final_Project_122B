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
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn") && session.getAttribute("isAdmin") != null && (Boolean)session.getAttribute("isAdmin")) {
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
			
			out.println("<div style=\"float:right; border-style:dashed; margin-left: 100px;\">");
			out.println("<p style=\"text-align:center;\"><b>Insert New Item</b></p>\n");
			out.println("<form id=\"add_item\" action=\"./adminUpdate\" method=\"get\">\n"
					+ "Item Name\n"
					+ "<br><input type=\"text\" name=\"item-name\" value=\"\"/><br>\n"
					+ "Item ID\n"
					+ "<br><input type=\"text\" name=\"itemid\" value=\"\"/><br>\n"
					+ "Price\n"
					+ "<br><input type=\"text\" name=\"item-price\" value=\"\"/><br>\n"
					+ "Type\n"
					+ "<br><input type=\"text\" name=\"item-type\" value=\"\"/><br>\n"
					+ "Picture URL\n"
					+ "<br><input type=\"text\" name=\"item-pic\" value=\"\"/><br>\n"
					+ "Store ID\n"
					+ "<br><input type=\"text\" name=\"storeid\" value=\"\"/><br>\n"
					+ "<input type=\"hidden\" name=\"add-item\" value=\"true\"/>\n"
					+ "<br><input type=\"submit\" value=\"Insert Item\"><br>\n"
					+ "</form>");
			out.println("</div>");
			
			out.println("<div style=\"float:right; border-style: dashed; margin: auto;\">\n"
					+ "	<p style=\"text-align: center;\"><b>Add Store</b></p>\n"
					+ "	<form id=\"add_store\" action=\"./adminUpdate\" method=\"GET\">\n"
					+ "		<div style=\"float: left; padding: 5px;\">\n"
					+ "			Store ID*\n"
					+ "			<br><input type=\"text\" name=\"storeid\" value=\"\"><br>\n"
					+ "			<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "				<p>Only if adding new store</p>\n"
					+ "				Store Name\n"
					+ "				<br><input type=\"text\" name=\"store-name\" value=\"\"><br>\n"
					+ "				Store Address\n"
					+ "				<br><input type=\"text\" name=\"address\" value=\"\"><br>\n"
					+ "				Store Phone Number\n"
					+ "				<br><input type=\"text\" name=\"store-phone\" value=\"\"><br>\n"
					+ "				Year Opened\n"
					+ "				<br><input type=\"text\" name=\"store-year\" value=\"\"><br>\n"
					+ "				Store Type ID\n"
					+ "				<br><input type=\"text\" name=\"store-typeid\" value=\"\"><br>\n"
					+ "				<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "					<p>Only if adding new store type</p>\n"
					+ "					Store Type Name\n"
					+ "					<br><input type=\"text\" name=\"store-type-name\" value=\"\"><br>\n"
					+ "				</div>\n"
					+ "				Plaza ID\n"
					+ "				<br><input type=\"text\" name=\"plazaid\" value=\"\"><br>\n"
					+ "				<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "					<p>Only if new plaza</p>\n"
					+ "					Plaza Name\n"
					+ "					<br><input type=\"text\" name=\"plaza-name\" value=\"\"><br>\n"
					+ "				</div>\n"
					+ "			</div>\n"
					+ "		</div>\n"
					+ "		<div style=\"float: right; padding: 5px;\">\n"
					+ "			<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "				City ID\n"
					+ "				<br><input type=\"text\" name=\"cityid\" value=\"\"><br>\n"
					+ "				<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "					<p>Only if new city</p>\n"
					+ "					City Name\n"
					+ "					<br><input type=\"text\" name=\"city-name\" value=\"\"><br>\n"
					+ "				</div>\n"
					+ "			</div>\n"
					+ "			Item ID*\n"
					+ "			<br><input type=\"text\" name=\"itemid\" value=\"\"><br>\n"
					+ "			<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "				<p>Only if new item</p>\n"
					+ "				Item Name\n"
					+ "				<br><input type=\"text\" name=\"item-name\" value=\"\"><br>\n"
					+ "				Item Type ID\n"
					+ "				<br><input type=\"text\" name=\"item-typeid\" value=\"\"><br>\n"
					+ "				<div style=\"border-style: solid; padding: 5px;\">\n"
					+ "					<p>Only if new type</p>\n"
					+ "					Item Type Name\n"
					+ "					<br><input type=\"text\" name=\"item-type-name\" value=\"\"><br>\n"
					+ "				</div>\n"
					+ "				Item Price\n"
					+ "				<br><input type=\"text\" name=\"item-price\" value=\"\"><br>\n"
					+ "				Item Pic URL\n"
					+ "				<br><input type=\"text\" name=\"item-pic\" value=\"\"><br>\n"
					+ "			</div>\n"
					+ "		</div>\n"
					+ "		<div style=\"clear: both;\">\n"
					+ "			<input type=\"hidden\" name=\"add-city\" value=\"true\">\n"
					+ "			<br><input type=\"submit\" value=\"Add City\"><br>\n"
					+ "		</div>\n"
					+ "	</form>\n"
					+ "</div>");
			
			
			
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
