package main;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Checks login info for employee
 */

public class EmployeeLogin extends HttpServlet {
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
			RequestDispatcher rd = request.getRequestDispatcher("EmployeeLogin.jsp");
			rd.forward(request, response);
			return;
		}

		//Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		Boolean isEmployee = (Boolean)session.getAttribute("isEmployee");

		PrintWriter out = response.getWriter();

		if((loggedIn == null || loggedIn == false) && (isEmployee == null || isEmployee == false)) {
			try {
				// Load driver
				//Class.forName("com.mysql.jdbc.Driver").newInstance();

				// Connect to mySQL
				//connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);

				Context initCtx = new InitialContext();
	            if (initCtx == null)
	                out.println("initCtx is NULL");

	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            if (envCtx == null)
	                out.println("envCtx is NULL");

	            // Look up our data source
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/storemarketing");

	            if (ds == null)
	                out.println("ds is null.");

	            Connection dbcon = ds.getConnection();
	            if (dbcon == null)
	                out.println("dbcon is null.");
				
				
				String loginQuery = "SELECT email, password FROM Employees WHERE email = ? AND password = ? ";
				//statement = connection.prepareStatement(loginQuery);
				statement = dbcon.prepareStatement(loginQuery);
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
				dbcon.close();
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
					//connection.close();
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