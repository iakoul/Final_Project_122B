package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

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
	
	Connection connection = null;
    public Login() {
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
		response.setHeader("Refresh", "3; URL=./index.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>\n"
	    		+ "<html>\n"
	    		+ "<head>\n"
	    		+ "<title>"
	    		+ "Login"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		
		if (loggedIn == null || loggedIn == false) {
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
							loggedIn = true;
							if (results.getBoolean(2)) {
								out.println("(A)");
								session.setAttribute("isAdmin", true);
							}
							out.println("</h1>\n");
							response.setHeader("Refresh", "3; URL=./mainPage");
							out.println("</body>\n</html>");
						} else {
							out.println("<h1>Incorrect username or password</h1>\n");
							loggedIn = false;
							out.println("</body>\n</html>");
							response.setHeader("Refresh", "3; URL=./index.html");
						}
					} else {
						out.println("<h1>Incorrect username or password</h1>\n");
						loggedIn = false;
						out.println("</body>\n</html>");
						response.setHeader("Refresh", "3; URL=./index.html");
					}
				} catch (SQLException e) {
					out.println("Select statement failed with code " + e.getMessage());
				}
			    session.setAttribute("loggedIn", loggedIn);
			    session.setAttribute("username", username);
			} catch (SQLException e) {
				out.println("Connection failed " + e.getMessage());
			}
		} else {
			out.println("<h1 align=\"center\">Welcome back " + session.getAttribute("username") + "</h1>\n");
			response.setHeader("Refresh", "3; URL=./mainPage");
			out.println("</body>\n</html>");
		}
	}
	
}
