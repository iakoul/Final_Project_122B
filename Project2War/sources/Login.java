import java.io.IOException;
import java.io.PrintWriter;
// import java.math.BigDecimal;
// import java.util.List;
// import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.sql.*;
import com.google.gson.*;

/*
 * Checks login info
 */

public class Login extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		PrintWriter out = response.getWriter();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		boolean isAndroid = SearchResult.isStringEmpty(request.getParameter("isAndroid")) ? false : Boolean.parseBoolean(request.getParameter("isAndroid"));

		// redirect to main page if logged into browser
		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		
		if(!isAndroid) { // check if logged into browser
			String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
			boolean valid = VerifyUtils.verify(gRecaptchaResponse);
			if (!valid) { // verify recaptcha is correct
				request.setAttribute("error", "Invalid reCaptcha. Please try again.");
				RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
				rd.forward(request, response);
				return;
			}

			if(loggedIn != null && loggedIn == true) {
				response.sendRedirect("Main.jsp");
				return;
			}
		}

		try {
		// Database connection info
		String user = "mytestuser";
    	String pass = "mypassword";
    	String url = "jdbc:mysql://localhost:3306/storemarketing";

		// Load driver
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		// Connect to mySQL
		connection = DriverManager.getConnection(url, user, pass);

		String loginQuery = "SELECT u.firstName FROM UsersTbl u WHERE u.username = ? AND u.hashPW = ? ";
		statement = connection.prepareStatement(loginQuery);
		statement.setString(1, username);
		statement.setString(2, password);
		result = statement.executeQuery();

		if(result.next()) {
			String firstName = result.getString("u.firstName");
			if(!isAndroid) {
				session.setAttribute("name", firstName);
				session.setAttribute("loggedIn", new Boolean(true));
				session.setAttribute("username", username);
				// redirect to main page
				RequestDispatcher rd = request.getRequestDispatcher("Main.jsp");
				rd.forward(request, response);
			}
			else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("loggedIn", "true");
				map.put("name", firstName);
				String json = new Gson().toJson(map);
			    response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(json);

				// out.println("true");
			}
		}
		else {
			if(!isAndroid) {
				// TODO: change to give error message with ajax
				// response.sendRedirect("Login.jsp");
				request.setAttribute("error", "Invalid username password combination. Please try again.");
				RequestDispatcher rd = request.getRequestDispatcher("Login.jsp");
				rd.forward(request, response);
			}
			else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("loggedIn", "false");
				String json = new Gson().toJson(map);
			    response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(json);
				// out.println("false");
			}
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}