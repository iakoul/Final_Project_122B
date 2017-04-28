package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;

/**
 * Servlet implementation class Browse
 */
@WebServlet("/Browse")
public class Browse extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Connection connection = null;
    public Browse() {
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
	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    try {
	    	if ((Boolean)session.getAttribute("loggedIn")) { //throws null pointer exception
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
						Integer storeCount = 0;
						String countQuery = "SELECT DISTINCT "
								+ "COUNT(s.storeID) "
								+ "FROM "
								+ "`AcceptsPaymentTbl` ap, "
								+ "`LanguageTbl` l1, "
								+ "`LanguageTbl` l2, "
								+ "`CityTbl` c, "
								+ "`StoreTbl` s, "
								+ "`PlazaTbl` p, "
								+ "`OwnerTbl` o "
								+ "WHERE ";
								if (request.getParameter("letter").equals("others")) {
									countQuery += "s.storeName NOT REGEXP ? ";
								} else {
									countQuery += "s.storeName REGEXP ? ";
								}
								countQuery += "AND s.storeID = ap.storeID "
								+ "AND s.plazaID = p.plazaID "
								+ "AND p.cityID = c.cityID "
								+ "AND s.ownerID = o.ownerID "
								+ "AND o.primaryLangID = l1.langID "
								+ "AND o.secondaryLangID = l2.langID;";
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
							storeCount = resultCount.getInt(1);
						}
						
						Integer limit = 1; //results per page
						if (request.getParameter("show") != null) {
							limit = Integer.parseInt(request.getParameter("show"));
						}
						
						out.println("<h1 align=\"center\">Browsing stores starting with letter " + request.getParameter("letter") + "</h1>\n");
						out.println("<h1 align=\"center\">There are a total of " + storeCount.toString() + " starting with that letter</h1>\n");
						
						out.println("<div style=\"text-align: center;\">\n");
						out.println("<a href=\"./mainPage\">Main Page</a>\n");
						out.println("</div>\n");
						
						out.println("<div>\n");
						out.println("Results ");
						out.println("<a href=\"./browse?letter=" + request.getParameter("letter") + "&pg=1&show=1\">1</a> ");
						out.println("<a href=\"./browse?letter=" + request.getParameter("letter") + "&pg=1&show=10\">10</a> ");
						out.println("<a href=\"./browse?letter=" + request.getParameter("letter") + "&pg=1&show=100\">100</a>\n");
						out.println("</div>\n");
						
						out.println("<div>");
						out.println("<table style=\"text-align: left; width: 100%; border: 1px solid black;\">\n");
						out.println("<tr>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Business Name <a href=\"./browse?letter="
						+ request.getParameter("letter") + "&pg=1&show=" + limit.toString() + "&orderin=asc&orderby=name\">Asc</a> <a href=\"./browse?letter="
						+ request.getParameter("letter") + "&pg=1&show=" + limit.toString() + "&orderin=desc&orderby=name\">Desc</a> </th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">City  <a href=\"./browse?letter="
						+ request.getParameter("letter") + "&pg=1&show=" + limit.toString() + "&orderin=asc&orderby=city\">Asc</a> <a href=\"./browse?letter="
						+ request.getParameter("letter") + "&pg=1&show=" + limit.toString() + "&orderin=desc&orderby=city\">Desc</a> </th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Visa</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">MasterCard</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Discover</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">AmEx</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Paypal</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Venmo</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Primary Language</th>\n");
						out.println("<th style=\"border: 1px solid black; text-align: left;\">Secondary Language</th>\n");
						out.println("</tr>");
						
						
						
						if (storeCount > 0) {
							String prepQuery = "SELECT DISTINCT "
									+ "s.storeName, "
									+ "c.cityName, "
									+ "ap.acceptsVisa, "
									+ "ap.acceptsMasterCard, "
									+ "ap.acceptsDiscover, "
									+ "ap.acceptsAmEx, "
									+ "ap.acceptsPaypal, "
									+ "ap.acceptsVenmo, "
									+ "l1.languageSpoken AS primaryLang, "
									+ "l2.languageSpoken AS secondaryLang, "
									+ "s.storeID, "
									+ "c.cityID "
									+ "FROM "
									+ "`AcceptsPaymentTbl` ap, "
									+ "`LanguageTbl` l1, "
									+ "`LanguageTbl` l2, "
									+ "`CityTbl` c, "
									+ "`StoreTbl` s, "
									+ "`PlazaTbl` p, "
									+ "`OwnerTbl` o "
									+ "WHERE ";
							if (request.getParameter("letter").equals("others")) {
								prepQuery += "s.storeName NOT REGEXP ? ";
							} else {
								prepQuery += "s.storeName REGEXP ? ";
							}
							prepQuery += "AND s.storeID = ap.storeID "
									+ "AND s.plazaID = p.plazaID "
									+ "AND p.cityID = c.cityID "
									+ "AND s.ownerID = o.ownerID "
									+ "AND o.primaryLangID = l1.langID "
									+ "AND o.secondaryLangID = l2.langID ";
							if (request.getParameter("orderby") == null || request.getParameter("orderby").equals("name")) {
								prepQuery += "ORDER BY s.storeName";
							} else {
								prepQuery += "ORDER BY c.cityName";
							}
							if (request.getParameter("orderin") == null || request.getParameter("orderin").equals("asc")) {
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
								out.println("<td>" + "<a href=\"./detailBiz?bizid=" + results.getString(11) + "\">" + results.getString(1) + "</a></td>\n"); //business name
								out.println("<td><a href=\"./detailCity?cityid=" + results.getString(12) + "\">" + results.getString(2) + "</a></td>\n"); //business city
								out.println("<td>" + results.getBoolean(3) + "</td>\n"); //visa
								out.println("<td>" + results.getBoolean(4) + "</td>\n"); //master card
								out.println("<td>" + results.getBoolean(5) + "</td>\n"); //discover
								out.println("<td>" + results.getBoolean(6) + "</td>\n"); //american express
								out.println("<td>" + results.getBoolean(7) + "</td>\n"); //paypal
								out.println("<td>" + results.getBoolean(8) + "</td>\n"); //venmo
								out.println("<td>" + results.getString(9) + "</td>\n"); //primary language
								out.println("<td>" + results.getString(10) + "</td>\n"); //secondary language
								out.println("</tr>\n");
							}
							out.println("</div>\n");
							if (Integer.parseInt(request.getParameter("pg")) > 1) {
								out.println("<div style=\"text-align: left;\">\n");
								out.println("<a href=\"./Browse?letter=" + request.getParameter("letter") + "&pg=" + (Integer.parseInt(request.getParameter("pg")) - 1) + "\">Back</a>\n");
								out.println("</div>\n");
							}
							Integer pageCount = 1;
							if (storeCount % limit > 0) {
								pageCount = storeCount/limit + 1;
							} else {
								pageCount = storeCount/limit;
							}
							if (Integer.parseInt(request.getParameter("pg")) < pageCount) {
								out.println("<div style=\"text-align: right;\">\n");
								out.println("<a href=\"./Browse?letter=" + request.getParameter("letter") + "&pg=" + (Integer.parseInt(request.getParameter("pg")) + 1) + "\">Next</a>\n");
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
