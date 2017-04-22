package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class queryResults
 */
@WebServlet(description = "Makes queries and displays them", urlPatterns = { "/queryResults" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	Connection connection = null;
    public Login() {
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>"
	    		+ "<html>"
	    		+ "<head>"
	    		+ "<title>"
	    		+ "Log in"
	    		+ "</title>"
	    		+ "</head>");

	    
	    out.println("<BODY BGCOLOR=\"#FDF5E6\">\n");
		
		Integer loggedIn = (Integer) request.getAttribute("loggedIn");
		
		if (loggedIn == null || loggedIn == 0) {
			//check if username and password are correct
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			//String salt = BCrypt.gensalt(12); //workload = 12
			//String hashed_password = BCrypt.hashpw(password, salt);

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
					String prepQuery = "SELECT u.username, u.isAdmin, u.hashPW FROM UsersTbl u WHERE u.username = ?";
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					pstmt.setString(1, username);
					ResultSet results = pstmt.executeQuery();
					if (results.next()) {
						if (BCrypt.checkpw(password, results.getString(3))) {
							out.println("<h1 align=\"center\">Welcome, " + results.getString(1));
							loggedIn = 1;
							if (results.getBoolean(2)) {
								out.println("(A)");
							}
							out.println("</h1>\n");
						}
					} else {
						out.println("<h1>Incorrect username or password</h1>\n");
						out.println(password);
						loggedIn = 0;
					}
				} catch (SQLException e) {
					out.println("Select statement failed with code " + e.getMessage());
				}
			    request.setAttribute("loggedIn", loggedIn);
			    request.setAttribute("username", username);
			} catch (SQLException e) {
				out.println("Connection failed " + e.getMessage());
			}
		} else {
			out.println("<h1 align=\"center\">Welcome back " + request.getAttribute("username") + "</h1>\n");
		}
		
		out.println("</BODY></HTML>");
	}

}
