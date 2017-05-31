import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Checks login info
 */

public class Login extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		
		boolean valid = VerifyUtils.verify(gRecaptchaResponse);
		if (!valid) {
			request.setAttribute("error", "Invalid reCaptcha. Please try again.");
			RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
			rd.forward(request, response);
			return;
		}

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");

		PrintWriter out = response.getWriter();

		if(loggedIn == null || loggedIn == false) {
			try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);

			String loginQuery = "SELECT u.firstName, u.hashPW FROM UsersTbl u WHERE u.username = ?";
			statement = connection.prepareStatement(loginQuery);
			statement.setString(1, username);
			result = statement.executeQuery();

			if(result.next() && BCrypt.checkpw(password, result.getString("u.hashPW"))) {
				String firstName = result.getString("u.firstName");
				session.setAttribute("name", firstName);
				session.setAttribute("loggedIn", new Boolean(true));
				session.setAttribute("username", username);
				// redirect to main page
				RequestDispatcher rd = request.getRequestDispatcher("Main.jsp");
				rd.forward(request, response);
			}
			else {
				// TODO: change to give error message with ajax
				// response.sendRedirect("Login.jsp");
				request.setAttribute("error", "Invalid username password combination. Please try again.");
				RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
				rd.forward(request, response);
			}

			}
			catch(Exception e) {
				out.println(e.getMessage());
			}
			finally {
				try {
					result.close();
				}
				catch(Exception e) {
					// ignore
				}
				try {
					statement.close();
				}
				catch(Exception e) {
					// ignore
				}
				try {
					connection.close();
				}
				catch(Exception e) {
					// ignore
				}
			}
		}
		else {
			response.sendRedirect("Main.jsp");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}