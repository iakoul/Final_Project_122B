import java.io.IOException;
import java.io.PrintWriter;
// import java.math.BigDecimal;
// import java.util.List;
// import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Inserts new item into database
 */

public class InsertItem extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String item = request.getParameter("item");
		int itemType = Integer.parseInt(request.getParameter("itemType"));
		double price = Double.parseDouble(request.getParameter("price"));

		if(SearchResult.isStringEmpty(item)) {
			request.setAttribute("message", "Please input an item.");
			RequestDispatcher rd = request.getRequestDispatcher("Dashboard.jsp");
			rd.forward(request, response);
			return;
		}

		Connection connection = null;
		PreparedStatement statement = null;

		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		Boolean isEmployee = (Boolean)session.getAttribute("isEmployee");

		PrintWriter out = response.getWriter();

		if(loggedIn == true && isEmployee == true) {
			try {
				// Database connection info
				String user = "root";
				String pass = "mysqlpass";
				String url = "jdbc:mysql:///storemarketing";

				// Load driver
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				// Connect to mySQL
				connection = DriverManager.getConnection(url, user, pass);

				String insert = "INSERT INTO MerchandiseTbl(merchName, merchType, merchPrice) VALUES(?,?,?) ";
				statement = connection.prepareStatement(insert);
				statement.setString(1, item);
				statement.setInt(2, itemType);
				statement.setDouble(3, price);
				int count = statement.executeUpdate();

				if(count > 0) {
					request.setAttribute("message", "Item inserted into database.");
					RequestDispatcher rd = request.getRequestDispatcher("Dashboard.jsp");
					rd.forward(request, response);
				}
				else {
					request.setAttribute("message", "Sorry, could not insert item " + item + "into database.");
					RequestDispatcher rd = request.getRequestDispatcher("Dashboard.jsp");
					rd.forward(request, response);
				}

			}
			catch(Exception e) {
				out.println(e.getMessage());
			}
			finally {
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
			response.sendRedirect("EmployeeLogin.jsp");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}