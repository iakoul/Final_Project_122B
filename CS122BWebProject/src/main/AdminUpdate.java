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
 * Servlet implementation class AdminUpdate
 */
@WebServlet("/AdminUpdate")
public class AdminUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public AdminUpdate() {
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
	    		+ "Performing Updates"
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
			
			out.println("<div style=\"text-align:center;\">\n");
			out.println("<h3>Welcome, " + session.getAttribute("firstname") + " " + session.getAttribute("lastname") + " to the administration control panel.</h3>\n");
			out.println("</div>\n");
			
			out.println("<div style=\"float:left;\">\n");
			out.println("<p style=\"text-align:center;\"><b>Table metadata</b></p>\n");
			
			try {
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				String prepQuery = "";
				PreparedStatement pstmt = connection.prepareStatement(prepQuery);
				ResultSet results = pstmt.executeQuery();
				
				
				
			} catch (Exception e){
				out.println("SQL Error");
			}	
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
