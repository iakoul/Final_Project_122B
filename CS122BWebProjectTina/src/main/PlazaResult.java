package main;

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
 * Queries plaza by id and sends response to display
 */

public class PlazaResult extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double id = SearchResult.isStringEmpty(request.getParameter("id")) ? -1 : Double.parseDouble(request.getParameter("id"));

		//Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		PrintWriter out = response.getWriter();

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
			
			// query to get plaza info
			String plazaQuery = "SELECT p.plazaName, c.cityID, c.cityName " +
								"FROM PlazaTbl p JOIN CityTbl c ON p.cityID = c.cityID " +
								"WHERE p.plazaID = ? ";

			//statement = connection.prepareStatement(plazaQuery);
			statement = dbcon.prepareStatement(plazaQuery);
			statement.setDouble(1, id);
			result = statement.executeQuery();
			if(result.next()) {
				String plazaName = result.getString("p.plazaName");
				int cityId = result.getInt("c.cityID");
				String cityName = result.getString("c.cityName");
				Plaza plaza = new Plaza(id, plazaName, new City(cityId, cityName));
				request.setAttribute("plaza", plaza);
			}

			// query to get stores at plaza
			String storeQuery = "SELECT s.storeID, s.storeName, s.address, s.phoneNum " +
							"FROM StoreTbl s " +
							"WHERE s.plazaID = ? ";

			//statement = connection.prepareStatement(storeQuery);
			statement = dbcon.prepareStatement(storeQuery);
			statement.setDouble(1, id);

			// execute query
			result = statement.executeQuery();

			// create with result of query
			List<Business> businessList = new ArrayList<Business>();
			
			while(result.next()) {
				long storeId = result.getLong("s.storeID");
				String storeName = result.getString("s.storeName");
				String address = result.getString("s.address");
				String phoneNumber = result.getString("s.phoneNum");
				Business business = new Business(storeId, storeName, address, phoneNumber);
				businessList.add(business);
			}

			request.setAttribute("businesses", businessList);
			RequestDispatcher rd = request.getRequestDispatcher("ShowPlaza.jsp");
			rd.forward(request, response);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}