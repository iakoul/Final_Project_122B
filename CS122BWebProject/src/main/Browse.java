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
	    out.println("<!DOCTYPE HTML>"
	    		+ "<html>"
	    		+ "<head>"
	    		+ "<title>"
	    		+ "Search Results "
	    		+ request.getAttribute("letter")
	    		+ "</title>"
	    		+ "</head>");
		if ((Boolean)session.getAttribute("loggedIn")) {
			//Incorporate mySQL driver
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
