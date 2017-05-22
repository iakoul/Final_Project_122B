import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Adds item with quantity to session.
 */

public class AddToCart extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int itemId = Integer.parseInt(request.getParameter("item"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		long businessId = Long.parseLong(request.getParameter("business"));

		HttpSession session = request.getSession();
		// HashMap<businessId, HashMap<itemId, Item>>
		@SuppressWarnings("unchecked")
		HashMap<Long, HashMap<Integer,Item>> cartItems = (HashMap<Long, HashMap<Integer,Item>>)session.getAttribute("cartItems");

		if(cartItems == null && quantity > 0) {
			cartItems = new HashMap<Long,HashMap<Integer,Item>>();
		}

		// get price of item
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		PrintWriter out = response.getWriter();

		try {
			// Load driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to mySQL
			connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);

			String query = "SELECT merchName, merchPrice FROM MerchandiseTbl WHERE merchID = ? ";
			statement = connection.prepareStatement(query);
			statement.setInt(1, itemId);
			result = statement.executeQuery();

			if(result.next()) {
				String itemName = result.getString("merchName");
				double itemPrice = result.getDouble("merchPrice");
				// check hashmap for item
				HashMap<Integer, Item> itemsForStore = cartItems.get(businessId);
				if(itemsForStore == null) {	// if business not in hashmap already, add business and item
					if(quantity > 0) {	// only add if quantity is greater than 0
						HashMap<Integer, Item> itemsInStore = new HashMap<Integer, Item>();
						itemsInStore.put(itemId, new Item(itemId, itemName, itemPrice, quantity));
						cartItems.put(businessId, itemsInStore);
					}
				}
				else {	// check if item exists (to update quantity); add item to cart
					Item item = itemsForStore.get(itemId);
					if(item == null) {	// if new item (not updating quantity)
						if(quantity > 0) {
							Item newItem = new Item(itemId, itemName, itemPrice, quantity);
							itemsForStore.put(itemId, newItem);
						}
					}
					else { // update quantity of item
						// if(quantity <= 0) {	// remove item if quantity is <= 0
						// 	itemsForStore.remove(itemId);
						// 	if(itemsForStore.size() == 0) {	// remove business from hashmap if no more items
						// 		cartItems.remove(businessId);
						// 	}
						// }
						// else {
							item.setQuantity(item.getQuantity()+quantity);
						// }
					}
				}
			}
			session.setAttribute("cartItems", cartItems);
			RequestDispatcher rd = request.getRequestDispatcher("./item?id="+itemId);
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
	}
}