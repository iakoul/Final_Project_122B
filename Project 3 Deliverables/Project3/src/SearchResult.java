import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
	public static final int DEFAULT_LIMIT = 20;
	private final String JOIN_STORE_TABLE = "JOIN StoreSellsTbl sells ON m.merchID = sells.merchID JOIN StoreTbl s ON sells.storeID = s.storeID ";

	public static boolean isStringEmpty(String s) {
		if(s == null || s.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	private String createQuery(String business, String city, String payment, String item, double price) {
		String query = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice ";
		String from = "FROM MerchandiseTbl m ";
		String where = "";

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

		// put clauses of query together
		query += from;
		if(!isStringEmpty(where)) {
			query += "WHERE";
			query += where;
		}
		return query;
	}

	private String createOrderByLimitOffset(int ascItem, int ascType, int ascPrice) {
		String query = "";
		String orderBy = "ORDER BY ";

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
			query += orderBy;
		}
		query += "LIMIT ? ";
		query += "OFFSET ? ";

		return query;
	}

	// handle search request
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// to test
		PrintWriter out = response.getWriter();

		// get parameters for search
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

		// get parameters for browse
		String startingLetter = request.getParameter("start");
		String type = request.getParameter("type");


		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {
			// Database connection info
			String user = "root";
			String pass = "mysqlpass";
			String url = "jdbc:mysql:///storemarketing";

			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(url, user, pass);

			String query;
			// create the ORDER BY, LIMIT, and OFFSET clause to add to the query
			String orderByLimitOffsetClause = createOrderByLimitOffset(ascItem, ascType, ascPrice);
			int index = 1;	// index used to add arguments
			// browse by letter
			if(!SearchResult.isStringEmpty(startingLetter)) {
				query = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice FROM MerchandiseTbl m WHERE m.merchName ";
				if(startingLetter.equals("num")) {
					query += "REGEXP '^[0-9].*' ";
				}
				else if(startingLetter.equals("Other")) {
					query += "REGEXP '^[^0-9a-z].*' ";
				}
				else {
					query += "LIKE ? ";
				}

				query += orderByLimitOffsetClause;
				
				statement = connection.prepareStatement(query);

				// add argument for browsing by letter
				if(!startingLetter.equals("num") && !startingLetter.equals("Other")) {
					statement.setNString(index++, startingLetter + "%");
				}
			}
			// browse by item type
			else if(!SearchResult.isStringEmpty(type)) {
				query = "SELECT DISTINCT m.merchID, m.merchName, m.merchType, m.merchPrice FROM StoreTypeTbl st JOIN StoreTbl s ON st.typeID = s.typeID JOIN StoreSellsTbl ss ON ss.storeID = s.storeID JOIN MerchandiseTbl m on ss.merchID = m.merchID WHERE st.storeType = ? ";
				query += orderByLimitOffsetClause;
				
				statement = connection.prepareStatement(query);
				// add argument for browsing by type
				statement.setNString(index++, type);
			}
			// search
			else {
				query = createQuery(business, city, payment, item, price);
				query += orderByLimitOffsetClause;

				statement = connection.prepareStatement(query);

				// add search arguments
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
			}

			// add arguments for number of results
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