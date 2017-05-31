package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Queries item by id and sends response to display
 */

public class ItemResult extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = SearchResult.isStringEmpty(request.getParameter("id")) ? -1 : Integer.parseInt(request.getParameter("id"));

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		PrintWriter out = response.getWriter();

		try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);


			// query to get item details
			String itemQuery = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice FROM MerchandiseTbl m WHERE m.merchID = ? ";
			statement = connection.prepareStatement(itemQuery);
			statement.setInt(1, id);
			result = statement.executeQuery();
			if(result.next()) {
				int itemId = result.getInt("m.merchID");
				String itemName = result.getString("m.merchName");
				int itemType = result.getInt("m.merchType");
				double itemPrice = result.getDouble("m.merchPrice");
				Item item = new Item(itemId, itemName, itemType, itemPrice);
				request.setAttribute("item", item);
			}

			// query to get stores
			String storeQuery = "SELECT s.storeID, s.storeName " +
							"FROM MerchandiseTbl m JOIN StoreSellsTbl sells ON m.merchID = sells.merchID JOIN StoreTbl s ON sells.storeID = s.storeID " +
							"WHERE m.merchID = ? ";

			statement = connection.prepareStatement(storeQuery);
			statement.setInt(1, id);

			// execute query
			result = statement.executeQuery();

			// create with result of query
			List<Business> businessList = new ArrayList<Business>();
			
			while(result.next()) {
				long storeId = result.getLong("s.storeID");
				String storeName = result.getString("s.storeName");
				Business newBusiness = new Business(storeId, storeName);
				businessList.add(newBusiness);
			}

			// out.println(businessList);

			request.setAttribute("businesses", businessList);
			RequestDispatcher rd = request.getRequestDispatcher("ShowItem.jsp");
			rd.forward(request, response);

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