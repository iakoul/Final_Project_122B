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
 * Queries business by id and sends response to display
 */

public class StoreResult extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long id = SearchResult.isStringEmpty(request.getParameter("id")) ? -1 : Long.parseLong(request.getParameter("id"));

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		PrintWriter out = response.getWriter();

		try {
			// Database connection info
			String user = "root";
        	String pass = "mysqlpass";
        	String url = "jdbc:mysql://localhost:3306/storemarketing";

			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(url, user, pass);

			// query to get store info
			String storeQuery = "SELECT s.storeID, s.storeName, s.address, s.phoneNum, s.yearOpened, s.typeID, s.plazaID, p.plazaName " +
								"FROM StoreTbl s JOIN PlazaTbl p ON s.plazaID = p.plazaID " +
								"WHERE s.storeID = ? ";

			statement = connection.prepareStatement(storeQuery);
			statement.setLong(1, id);
			result = statement.executeQuery();
			if(result.next()) {
				long storeId = result.getLong("s.storeID");
				String storeName = result.getString("s.storeName");
				String storeAddress = result.getString("s.address");
				String phoneNumber = result.getString("s.phoneNum");
				int yearOpened = result.getInt("s.yearOpened");
				int typeId = result.getInt("s.typeId");
				double plazaId = result.getDouble("s.plazaID");
				String plazaName = result.getString("p.plazaName");
				Business business = new Business(storeId, storeName, storeAddress, phoneNumber, yearOpened, typeId, new Plaza(plazaId, plazaName));
				request.setAttribute("business", business);
			}

			// query to get items at store
			String itemQuery = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice " +
							"FROM StoreTbl s JOIN StoreSellsTbl sells ON s.storeID = sells.storeID JOIN MerchandiseTbl m ON sells.merchID = m.merchID " +
							"WHERE s.storeID = ? ";

			statement = connection.prepareStatement(itemQuery);
			statement.setLong(1, id);

			// execute query
			result = statement.executeQuery();

			// create with result of query
			List<Item> itemList = new ArrayList<Item>();
			
			while(result.next()) {
				int itemId = result.getInt("m.merchID");
				String itemName = result.getString("m.merchName");
				int itemType = result.getInt("m.merchType");
				double itemPrice = result.getDouble("m.merchPrice");
				Item item = new Item(itemId, itemName, itemType, itemPrice);
				itemList.add(item);
			}

			request.setAttribute("items", itemList);
			RequestDispatcher rd = request.getRequestDispatcher("ShowStore.jsp");
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