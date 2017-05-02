
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
 * Servlet implementation class ShoppingCart
 */

@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public ShoppingCart() {
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
	    		+ "Shopping Cart"
	    		+ "</title>\n"
	    		+ "<style>\n"
	    		+ ".clearfix:before,\n"
	    		+ ".clearfix:after {\n"
	    		+ "\tcontent: '\0020';\n"
	    		+ "\tdisplay: block;\n"
	    		+ "\toverflow: hidden;\n"
	    		+ "\tvisibility: hidden;\n"
	    		+ "\twidth:0;\n"
	    		+ "\theight:0;\n"
	    		+ "}\n"
	    		+ ".clearfix:after {\n"
	    		+ "\tclear:both;\n"
	    		+ "}\n"
	    		+ ".clearfix {\n"
	    		+ "\tzoom:1;\n"
	    		+ "}\n"
	    		+ "form {\n"
	    		+ "\tdisplay: inline-block;\n"
	    		+ "\twidth:200px;\n"
	    		+ "\tmargin-right:30px;\n"
	    		+ "\ttext-align:right;\n"
	    		+ "}\n"
	    		+ "</style>\n"
	    		+ "</head>\n");
	    
	    out.println("<script language=\"javascript\">\n");
		out.println("window.onpageshow = function(event) {if (event.persisted) {window.location.reload() }};");
		out.println("</script>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
			//Incorporate mySQL driver
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			
			try {
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				
				out.println("<div id=\"container\" style=\"margin: 0 auto;\">"); //container
				out.println("<div id=\"header\" style=\"text-align: center;\">"); //header
				out.println("<b>Cart contents: </b><br>");
			    if (session.getAttribute("cart") != null) {
			    	HashMap<ArrayList<String>, Integer> cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
			    	Double totalCost = 0.0;
			    	out.println("<p>There are " + cart.size() + " items in your cart.</p>\n");
			    	out.println("</div>"); //header
			    	
			    	out.println("<div id=\"table\" class=\"clearfix\">"); //table
			    	out.println("<table style=\"text-align: left; width: 100%; border: 1px solid black;\">\n");
					out.println("<tr>\n");
					out.println("<th style=\"border: 1px solid black; text-align: left;\">Store Name</th>");
					out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Name</th>");
					out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Quantity</th>");
					out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Price</th>");
					out.println("<th style=\"border: 1px solid black; text-align: left;\"></th>");
					out.println("</tr>");
			    	
			    	for (ArrayList<String> key: cart.keySet()) {
			    		
			    		String prepQuery = "SELECT "
			    				+ "s.storeName, "
			    				+ "m.merchName, "
			    				+ "m.merchPrice "
								+ "FROM "
								+ "MerchandiseTbl m, "
								+ "StoreTbl s "
								+ "WHERE "
								+ "s.storeID = ? "
								+ "AND m.merchID = ?;";
						PreparedStatement pstmt = connection.prepareStatement(prepQuery);
						pstmt.setString(1, key.get(0).toString()); //storeid
						pstmt.setString(2, key.get(1).toString()); //itemid
						ResultSet results = pstmt.executeQuery();
						
			    		if (results.next()) {
			    			out.println("<tr>");
				    		out.println("<td>" + results.getString(1) + "</td>");
				    		out.println("<td>" + results.getString(2) + "</td>");
				    		out.println("<td>" + cart.get(key).toString() + "</td>");
				    		out.println("<td>$" + results.getString(3) + "</td>");
				    		
				    		
				    		out.println("<td><form id=\"form\" action=\"./addToCart\" method=\"get\">"
									+ "<input type=\"hidden\" name=\"storeid\" value=\"" + key.get(0).toString() + "\"/>\n"
									+ "<input type=\"hidden\" name=\"itemid\" value=\"" + key.get(1).toString() + "\"/>\n"
									+ "<input type=\"hidden\" name=\"update\" value=\"true\"/>\n"
									+ "<input type=\"text\" name=\"qty\">" + "<input type=\"submit\" value=\"Update\">\n" + "</form></td>");
				    		
				    		
				    		out.println("</tr>");
				    		totalCost += results.getDouble(3) * Integer.parseInt(cart.get(key).toString());
				    		
			    		} else {
			    			out.println("Item or store not found in database");
			    		}
			    	}
			    	out.println("<tr>");
			    	//out.println("<td></td><td></td><td></td><td><b>Total Is $" + totalCost + "</b></td>");
			    	out.println("<td></td><td></td><td></td><td><b>Total Is $");
			    	out.format("%.2f", totalCost);
			    	out.println("</b></td>");
			    	out.println("</table>\n");
			    	out.println("</div>\n"); //end table
			    	out.println("<br>");
				    //out.println("<div id=\"total\">"); //total
				    //out.println("Total is $" + totalCost);
				    
				    out.println("<form id=\"form\" action=\"./checkout\" method=\"post\">\n"
				    		+ "First name: "
				    		+ "<input type=\"text\" name=\"firstname\"><br>\n"
				    		+ "Last name: "
				    		+ "<input type=\"text\" name=\"lastname\"><br>\n"
				    		+ "Credit Card number: "
				    		+ "<input type=\"text\" name=\"ccnum\"><br><br>\n"
				    		+ "Expiration date (YYYY-MM-DD): "
				    		+ "<input type=\"text\" name=\"expdate\"><br><br>\n"
				    		+ "<input type=\"submit\" value=\"Submit\">\n"
				    		+ "</form>");
				    out.println("</div>"); //end total
				    
			    }
			    out.println("<div id=\"mainlink\">"); //main page link
			    out.println("<a href=\"./mainPage\">Main Page</a>\n");
			    out.println("</div>"); //end main page link
			    out.println("</div>"); //end container
			    out.println("</body>\n</html>");
			} catch (SQLException e) {
				e.printStackTrace();
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
