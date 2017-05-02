import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
// import java.util.HashMap;
// import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Queries the advanced search fields and sends response
 */

public class SearchResult extends HttpServlet {
	private static final int DEFAULT_LIMIT = 20;
	private final String JOIN_STORE_TABLE = "JOIN StoreSellsTbl sells ON m.merchID = sells.merchID JOIN StoreTbl s ON sells.storeID = s.storeID ";

	public static boolean isStringEmpty(String s) {
		if(s == null || s.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	private String createQuery(String business, String city, String payment, String item, double price, int results, int page, int ascItem, int ascType, int ascPrice) {
		String query = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice ";
		String from = "FROM MerchandiseTbl m ";
		String where = "";
		String limit = "LIMIT ? ";
		String orderBy = "ORDER BY ";
		String offset = "OFFSET ? ";

		boolean joinedStoreTable = false;
		// check fields to fill out query
		if(!isStringEmpty(item)) {
			where += (isStringEmpty(where) ? "" : "AND") + " m.merchName LIKE ? ";
		}
		if(price >= 0.01) {
			where += (isStringEmpty(where) ? "" : "AND") + " m.merchPrice <= ? ";
		}
		if(!isStringEmpty(business)) {
			from += JOIN_STORE_TABLE;
			where += (isStringEmpty(where) ? "" : "AND") + " s.storeName LIKE ? ";
			joinedStoreTable = true;
		}
		if(!isStringEmpty(city)) {
			if(!joinedStoreTable) {
				from += JOIN_STORE_TABLE;
				joinedStoreTable = true;
			}
			from += "JOIN PlazaTbl p ON s.plazaID = p.plazaID JOIN CityTbl c ON p.cityID = c.cityID ";
			where += (isStringEmpty(where) ? "" : "AND") + " c.cityName LIKE ? ";
		}
		if(!isStringEmpty(payment)) {
			if(!joinedStoreTable) {
				from += JOIN_STORE_TABLE;
				joinedStoreTable = true;
			}
			from += "JOIN AcceptsPaymentTbl a ON s.storeID = a.storeID ";
			where += (isStringEmpty(where) ? "" : "AND");
			switch(payment) {
				case "visa":
					where += " a.acceptsVisa = 1 ";
					break;
				case "mastercard":
					where += " a.acceptsMasterCard = 1 ";
					break;
				case "discover":
					where += " a.acceptsDiscover = 1 ";
					break;
				case "amex":
					where += " a.acceptsAmEx = 1 ";
					break;
				case "paypal":
					where += " a.acceptsPaypal = 1 ";
					break;
				case "venmo":
					where += " a.acceptsVenmo = 1 ";
					break;
			}
		}
		if((ascItem != -1) || (ascType != -1) || (ascPrice != -1)) {
			boolean addedOrderClause = false;
			if(ascItem == 1) {
				orderBy += "m.merchName ASC ";
				addedOrderClause = true;
			}
			else if(ascItem == 0) {
				orderBy += "m.merchName DESC ";
				addedOrderClause = true;
			}
			if(ascType == 1) {
				orderBy += (addedOrderClause ? ", " : "") + "m.merchType ASC ";
				addedOrderClause = true;
			}
			else if(ascType == 0) {
				orderBy += (addedOrderClause ? ", " : "") + "m.merchType DESC ";
				addedOrderClause = true;
			}
			if(ascPrice == 1) {
				orderBy += (addedOrderClause ? ", " : "") + "m.merchPrice ASC ";
			}
			else if(ascPrice == 0) {
				orderBy += (addedOrderClause ? ", " : "") + "m.merchPrice DESC ";
			}
		}

		// put clauses of query together
		query += from;
		if(!isStringEmpty(where)) {
			query += "WHERE";
			query += where;
		}
		if((ascItem != -1) || (ascType != -1) || (ascPrice != -1)) {
			query += orderBy;
		}
		query += limit;
		query += offset;

		return query;
	}

	// handle search request
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// to test
		PrintWriter out = response.getWriter();

		// get parameters
		String business = request.getParameter("business");
		String city = request.getParameter("city");
		String payment = request.getParameter("payment");
		String item = request.getParameter("item");
		double price = isStringEmpty(request.getParameter("price")) ? 0 : Double.parseDouble(request.getParameter("price"));
		int results = isStringEmpty(request.getParameter("results")) ? DEFAULT_LIMIT : Integer.parseInt(request.getParameter("results"));
		int page = isStringEmpty(request.getParameter("page")) ? 1 : Integer.parseInt(request.getParameter("page"));
		int ascItem = isStringEmpty(request.getParameter("ascItem")) ? -1 : Integer.parseInt(request.getParameter("ascItem"));
		int ascType = isStringEmpty(request.getParameter("ascType")) ? -1 : Integer.parseInt(request.getParameter("ascType"));
		int ascPrice = isStringEmpty(request.getParameter("ascPrice")) ? -1 : Integer.parseInt(request.getParameter("ascPrice"));


		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {
			// Database connection info
			String user = "root";
        	String pass = "mysqlpass";
        	String url = "jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false";

			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(url, user, pass);

			String query = createQuery(business, city, payment, item, price, results, page, ascItem, ascType, ascPrice);
			
			
			// prepare sql statement
			statement = connection.prepareStatement(query);
			int index = 1;
			if(!isStringEmpty(item)) {
				statement.setNString(index++, "%"+item+"%");
			}
			if(price >= 0.01) {
				BigDecimal value = new BigDecimal(price);
				statement.setBigDecimal(index++, value);
			}
			if(!isStringEmpty(business)) {
				statement.setNString(index++, "%"+business+"%");
			}
			if(!isStringEmpty(city)) {
				statement.setNString(index++, "%"+city+"%");
			}
			statement.setInt(index++, results);
			statement.setInt(index++, (page-1)*results);


			// execute query
			result = statement.executeQuery();

			// create with result of query
			List<Item> searchResult = new ArrayList<Item>();
			
			while(result.next()) {
				int itemId = result.getInt("m.merchID");
				String itemName = result.getString("m.merchName");
				int itemType = result.getInt("m.merchType");
				double itemPrice = result.getDouble("m.merchPrice");
				Item newItem = new Item(itemId, itemName, itemType, itemPrice);
				searchResult.add(newItem);
			}

			// put result in session
			// HttpSession session = request.getSession();
			// session.setAttribute("items", searchResult);

			// Set<Integer> resultStoreIds = searchResult.keySet();
			request.setAttribute("items", searchResult);

			RequestDispatcher rd = request.getRequestDispatcher("ResultTable.jsp");
			rd.forward(request, response);


		}
		catch(Exception e) {
			out.println(e.getMessage());
			//e.printStackTrace();
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