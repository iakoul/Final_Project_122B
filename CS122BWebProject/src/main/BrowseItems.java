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
 * Servlet implementation class BrowseItems
 */
@WebServlet("/BrowseItems")
public class BrowseItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public BrowseItems() {
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
	    		+ "Search Results for "
	    		+ request.getParameter("letter")
	    		+ "</title>\n"
	    		+ "</head>\n");
	    out.println("<script language=\"javascript\">\n");
	    out.println("window.onpageshow = function(event) {if (event.persisted) {parent.window.location.reload(true) }};");
		out.println("</script>\n");
	    
	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    
	    try {
	    	if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) { //throws null pointer exception
	    		Integer itemsInCart = 0;
	    	    if ((HashMap<ArrayList<String>, Integer>)session.getAttribute("cart") != null) {
	    	    	HashMap<ArrayList<String>, Integer> cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
	    	    	itemsInCart = cart.size();
	    	    }
	    	    out.println("<div align=\"right\"><a href=\"./shoppingCart\">Cart(" + itemsInCart + ")</a></div>");
	    	    if (session.getAttribute("isAdmin") != null && (Boolean)session.getAttribute("isAdmin")) {
		    		out.println("<div align=\"right\"><a href=\"./adminConsole\">Admin</a></div>");
		    	}
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
						Integer itemCount = 0;
						String countQuery = "SELECT DISTINCT "
								+ "COUNT(m.merchID) "
								+ "FROM "
								+ "`MerchandiseTbl` m "
								+ "WHERE ";
								if (request.getParameter("letter").equals("others")) {
									countQuery += "m.merchName NOT REGEXP ? ";
								} else {
									countQuery += "m.merchName REGEXP ? ";
								}
								countQuery += ";";
						PreparedStatement pstmtCount = connection.prepareStatement(countQuery);
						if (request.getParameter("letter").equals("numbers")) {
							pstmtCount.setString(1, "^[0-9]");
						} else if (request.getParameter("letter").equals("others")) {
							pstmtCount.setString(1, "^[a-zA-Z0-9]");
						} else {
							pstmtCount.setString(1, "^" + request.getParameter("letter"));
						}
						ResultSet resultCount = pstmtCount.executeQuery();
						if (resultCount.next()) {
							itemCount = resultCount.getInt(1);
						}
						
						Integer limit = 20; //results per page
						if (request.getParameter("show") != null) {
							limit = Integer.parseInt(request.getParameter("show"));
						}
						
						String orderBy;
						if (request.getParameter("orderby") != null) {
							orderBy = request.getParameter("orderby");
						} else {
							orderBy = "name";
						}
						
						String orderIn;
						if (request.getParameter("orderin") != null) {
							orderIn = request.getParameter("orderin");
						} else {
							orderIn = "asc";
						}
						
						String pgNum;
						if (request.getParameter("pg") != null) {
							pgNum = (String)request.getParameter("pg");
						} else {
							pgNum = "1";
						}
						
						out.println("<h1 align=\"center\">Browsing items starting with letter " + request.getParameter("letter") + "</h1>\n");
						out.println("<h1 align=\"center\">There are a total of " + itemCount.toString() + " starting with that letter</h1>\n");
						
						out.println("<div style=\"text-align: center;\">\n");
						out.println("<a href=\"./mainPage\">Main Page</a>\n");
						out.println("</div>\n");
						
						out.println("<div>\n");
						out.println("Results ");
						out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=1&show=1\">1</a> ");
						out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=1&show=20\">20</a> ");
						out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=1&show=40\">40</a> ");
						out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=1&show=60\">60</a>\n");
						out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=1&show=80\">80</a>\n");
						out.println("</div>\n");
						
						out.println("<div>");
						out.println("<table style=\"text-align: left; width: 100%; border: 1px solid black;\">\n");
						out.println("<tr>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Name <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=asc&orderby=name\">Asc</a> <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=desc&orderby=name\">Desc</a> </th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Type  <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=asc&orderby=type\">Asc</a> <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=desc&orderby=type\">Desc</a> </th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Item Price  <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=asc&orderby=price\">Asc</a> <a href=\"./browseItems?letter="
								+ request.getParameter("letter") + "&pg=" + pgNum + "&show=" + limit.toString() + "&orderin=desc&orderby=price\">Desc</a> </th>\n");
						out.println("</tr>");
						
						
						
						if (itemCount > 0) {
							String prepQuery = "SELECT DISTINCT "
									+ "m.merchID, "
									+ "m.merchName, "
									+ "s.storeType, "
									+ "m.merchPrice "
									+ "FROM "
									+ "`MerchandiseTbl` m "
									+ "LEFT JOIN "
									+ "StoreTypeTbl AS s ON s.typeID = m.merchType "
									+ "WHERE ";
							if (request.getParameter("letter").equals("others")) {
								prepQuery += "m.merchName NOT REGEXP ? ";
							} else {
								prepQuery += "m.merchName REGEXP ? ";
							}
							//if (request.getParameter("orderby") == null || request.getParameter("orderby").equals("name")) {
							if (orderBy.equals("name")) {
								prepQuery += "ORDER BY m.merchName";
							//} else if (request.getParameter("orderby").equals("type")){
							} else if (orderBy.equals("type")) {
								prepQuery += "ORDER BY s.storeType";
							} else {
								prepQuery += "ORDER BY m.merchPrice";
							}
							//if (request.getParameter("orderin") == null || request.getParameter("orderin").equals("asc")) {
							if (orderIn.equals("asc")) {
								prepQuery += " ASC ";
							} else {
								prepQuery += " DESC ";
							}
							prepQuery += "LIMIT ? OFFSET ?;";
							PreparedStatement pstmt = connection.prepareStatement(prepQuery);
							if (request.getParameter("letter").equals("numbers")) {
								pstmt.setString(1, "^[0-9]");
							} else if (request.getParameter("letter").equals("others")) {
								pstmt.setString(1, "^[a-zA-Z0-9]");
							} else {
								pstmt.setString(1, "^" + request.getParameter("letter"));
							}
							pstmt.setInt(2, limit);
							pstmt.setInt(3, (Integer.parseInt(request.getParameter("pg")) - 1) * limit);
							ResultSet results = pstmt.executeQuery();
							while (results.next()) {
								out.println("<tr>\n");
								out.println("<td>" + "<a href=\"./detailItem?itemid=" + results.getString(1) + "\">" + results.getString(2) + "</a></td>\n"); //item name
								out.println("<td>" + results.getString(3) + "</td>\n"); //item type
								out.println("<td>$" + results.getBigDecimal(4) + "</td>\n"); //item price
								out.println("</tr>\n");
							}
							out.println("</div>\n");
							if (Integer.parseInt(request.getParameter("pg")) > 1) {
								out.println("<div style=\"text-align: left; float:left;\">\n");
								out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=" + (Integer.parseInt(request.getParameter("pg")) - 1)
										+ "&show=" + limit.toString() + "&orderin=" + orderIn + "&orderby=" + orderBy + "\">Prev</a>\n");
								out.println("</div>\n");
							}
							Integer pageCount = 1;
							if (itemCount % limit > 0) {
								pageCount = itemCount/limit + 1;
							} else {
								pageCount = itemCount/limit;
							}
							if (Integer.parseInt(request.getParameter("pg")) < pageCount) {
								out.println("<div style=\"text-align: right; float:right;\">\n");
								out.println("<a href=\"./browseItems?letter=" + request.getParameter("letter") + "&pg=" + (Integer.parseInt(request.getParameter("pg")) + 1)
										+ "&show=" + limit.toString() + "&orderin=" + orderIn + "&orderby=" + orderBy + "\">Next</a>\n");
								out.println("</div>\n");
							}
							if (pageCount == 1) {
								out.println("<div style=\"text-align: center;\">\n");
								out.println("<p></p>\n");
								out.println("</div>\n");
							}
							out.println("</body>\n</html>");
						}
					} catch (SQLException e) {
						out.println("Select statement failed with code " + e.getMessage());
					}
				} catch (SQLException e) {
					out.println("Connection failed " + e.getMessage());
				}
			}
	    } catch (NullPointerException e){
	    	response.setHeader("Refresh", "3; URL=./index.html");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
