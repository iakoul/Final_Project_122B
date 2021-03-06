import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Checks login info for employee
 */

public class EmployeeLogin extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		
		// boolean valid = VerifyUtils.verify(gRecaptchaResponse);
		// if (!valid) {
		// 	request.setAttribute("error", "Invalid reCaptcha. Please try again.");
		// 	RequestDispatcher rd = request.getRequestDispatcher("EmployeeLogin.jsp");
		// 	rd.forward(request, response);
		// 	return;
		// }

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		Boolean isEmployee = (Boolean)session.getAttribute("isEmployee");

		PrintWriter out = response.getWriter();

		if((loggedIn == null || loggedIn == false) && (isEmployee == null || isEmployee == false)) {
			try {
				// Database connection info
				String user = "mytestuser";
	        	String pass = "mypassword";
	        	String url = "jdbc:mysql://localhost:3306/storemarketing";

				// Load driver
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				// Connect to mySQL
				connection = DriverManager.getConnection(url, user, pass);

				String loginQuery = "SELECT email, password FROM Employees WHERE email = ? AND password = ? ";
				statement = connection.prepareStatement(loginQuery);
				statement.setString(1, username);
				statement.setString(2, password);
				result = statement.executeQuery();

				if(result.next()) {
					session.setAttribute("loggedIn", new Boolean(true));
					session.setAttribute("isEmployee", new Boolean(true));
					// redirect to dashboard
					RequestDispatcher rd = request.getRequestDispatcher("Dashboard.jsp");
					rd.forward(request, response);
				}
				else {
					// TODO: change to give error message with ajax
					// response.sendRedirect("EmployeeLogin.jsp");
					request.setAttribute("error", "Invalid username password combination. Please try again.");
					RequestDispatcher rd = request.getRequestDispatcher("EmployeeLogin.jsp");
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
			response.sendRedirect("Dashboard.jsp");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}