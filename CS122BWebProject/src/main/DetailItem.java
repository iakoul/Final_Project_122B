package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DetailItem
 */
@WebServlet("/DetailItem")
public class DetailItem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection connection = null;
	public DetailItem() {
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
				+ "Item Detail"
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
							+ "m.merchID, "
							+ "m.merchName, "
							+ "m.merchType, "
							+ "m.merchPrice, "
							+ "s.storeID, "
							+ "s.storeName "
							+ "FROM "
							+ "MerchandiseTbl m, "
							+ "StoreTbl s, "
							+ "StoreSellsTbl se "
							+ "WHERE "
							+ "s.storeID = se.storeID "
							+ "AND m.merchID = se.merchID "
							+ "AND m.merchID = ? "
							+ "ORDER BY s.storeName;";
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					pstmt.setString(1, request.getParameter("itemid"));
					ResultSet results = pstmt.executeQuery();
					out.println("<div style=\"padding-left: 5%; padding-top: 5%;\">\n");
					if (!results.next()) {
						out.println("<p>Item data is missing from database.</p>\n");
					} else {
						out.println("<h1>Item name: " + results.getString(2) + "</h1>\n");
						out.println("<p></b>Item type:</b> " + results.getString(3) + "</p>\n");
						out.println("<p></b>Item price:</b> $" + results.getString(4) + "</p>\n");
						out.println("<p>This item is available for purchase at the following locations: </p>\n");
						out.println("<ul>\n");
						out.println("<li><a href=\"./detailBiz?bizid=" + results.getString(5) + "\">" + results.getString(6) + "</a> "
								+ "<a href=\"./addToCart?storeid=" + results.getString(5) + "&itemid=" + results.getString(1) + "&qty=1\">Add 1 to cart</a></li>\n");
						while(results.next()) {
							out.println("<li><a href=\"./detailBiz?bizid=" + results.getString(5) + "\">" + results.getString(6) + "</a> "
									+ "<a href=\"./addToCart?storeid=" + results.getString(5) + "&itemid=" + results.getString(1) + "&qty=1\">Add 1 to cart</a></li>\n");
						}
						out.println("</ul>\n");
					}
					out.println("<button onclick=\"goBack()\">Go Back</button>");
					out.println("</div>\n");

					out.println("<a href=\"./mainPage\">Main Page</a>\n");
					out.println("<script language=\"javascript\">\n");
					out.println("function goBack() {window.history.back();}\n");
					out.println("</script>\n");
					out.println("</body>\n</html>");
				} catch (Exception e) {
					out.println(e.getMessage());
				}
			} catch (Exception e) {
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
