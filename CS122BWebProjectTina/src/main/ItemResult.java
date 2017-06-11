package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
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

		long startTimeTS = System.nanoTime();
		
		//Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		
		FileWriter filewriter = null;
		BufferedWriter bufferwriter = null;
		
		PrintWriter out = response.getWriter();

		try {
			long startTimeTJ = System.nanoTime();
			//Load driver
			//Class.forName("com.mysql.jdbc.Driver").newInstance();

			//Connect to mySQL
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
			
			// query to get item details
			String itemQuery = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice FROM MerchandiseTbl m WHERE m.merchID = ? ";
			//statement = connection.prepareStatement(itemQuery);
			statement = dbcon.prepareStatement(itemQuery);
			statement.setInt(1, id);
			result = statement.executeQuery();
            
            /*
            //non-prepared statements
            String itemQuery = "SELECT m.merchID, m.merchName, m.merchType, m.merchPrice FROM MerchandiseTbl m WHERE m.merchID = " + id;
            Statement statement = dbcon.createStatement();
            result = statement.executeQuery(itemQuery);
            */
            
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

			//statement = connection.prepareStatement(storeQuery);
			statement = dbcon.prepareStatement(storeQuery);
			statement.setInt(1, id);

			// execute query
			result = statement.executeQuery();
			
			/*
			//non-prepared statements
			String storeQuery = "SELECT s.storeID, s.storeName " +
					"FROM MerchandiseTbl m JOIN StoreSellsTbl sells ON m.merchID = sells.merchID JOIN StoreTbl s ON sells.storeID = s.storeID " +
					"WHERE m.merchID = " + id;
			statement = dbcon.createStatement();
            result = statement.executeQuery(storeQuery);
			
			*/
			
			long endTimeTJ = System.nanoTime();
			long elapsedTimeTJ = endTimeTJ - startTimeTJ;
			
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
			dbcon.close();
			
			long endTimeTS = System.nanoTime();
			long elapsedTimeTS = endTimeTS - startTimeTS;
			
			
			File file = new File("timing.log");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			filewriter = new FileWriter(file, true);
			bufferwriter = new BufferedWriter(filewriter);
			
			bufferwriter.write("Total search servlet time (TS) in ns is: " + elapsedTimeTS + " : Total search JDBC time (TJ) in ns is: " + elapsedTimeTJ + "\n");
			bufferwriter.flush();
			System.out.println("Log is located at " + file.getAbsolutePath());
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}