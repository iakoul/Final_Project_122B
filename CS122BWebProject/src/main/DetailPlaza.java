package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DetailCity
 */
@WebServlet("/DetailPlaza")
public class DetailPlaza extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public DetailPlaza() {
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
	    		+ "Plaza Detail"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		
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
							+ "p.plazaName, "
							+ "s.storeID "
							+ "FROM "
							+ "StoreTbl s, "
							+ "PlazaTbl p "
							+ "WHERE "
							+ "p.plazaID = ? "
							+ "AND "
							+ "s.plazaID = p.plazaID "
							+ "ORDER BY s.storeName ASC;";
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					pstmt.setString(1, request.getParameter("plazaid"));
					ResultSet results = pstmt.executeQuery();
					out.println("<div style=\"padding-left: 5%; padding-top: 5%;\">\n");
					if (!results.next()) {
						out.println("<p>Plaza data is missing from database.</p>\n");
					} else {
						out.println("<h1>Businesses present in " + results.getString(2) + "</h1>\n");
						out.println("<ul>");
						out.println("<li><a href=\"./detailBiz?bizid=" + results.getString(3) + "\">" + results.getString(1) + "</a></li>\n");
						while(results.next()) {
							out.println("<li><a href=\"./detailBiz?bizid=" + results.getString(3) + "\">" + results.getString(1) + "</a></li>\n");
						}
						out.println("</ul>");
					}
					out.println("<button onclick=\"goBack()\">Go Back</button>");
					out.println("</div>");
					
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
