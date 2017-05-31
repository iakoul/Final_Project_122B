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
 * Queries plaza by id and sends response to display
 */

public class CityResult extends HttpServlet {
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

			// query to get city info
			String plazaQuery = "SELECT c.cityName " +
								"FROM CityTbl c " +
								"WHERE c.cityID = ? ";

			statement = connection.prepareStatement(plazaQuery);
			statement.setInt(1, id);
			result = statement.executeQuery();

			if(result.next()) {
				String cityName = result.getString("c.cityName");
				City city = new City(id, cityName);
				request.setAttribute("city", city);
			}
			
			// query to get plazas in city
			String storeQuery = "SELECT p.plazaID, p.plazaName " +
							"FROM PlazaTbl p " +
							"WHERE p.cityID = ? ";

			statement = connection.prepareStatement(storeQuery);
			statement.setDouble(1, id);

			// execute query
			result = statement.executeQuery();

			// create with result of query
			List<Plaza> plazaList = new ArrayList<Plaza>();
			
			while(result.next()) {
				double plazaId = result.getDouble("p.plazaID");
				String plazaName = result.getString("p.plazaName");
				Plaza plaza = new Plaza(plazaId, plazaName);
				plazaList.add(plaza);
			}

			request.setAttribute("plazas", plazaList);
			RequestDispatcher rd = request.getRequestDispatcher("ShowCity.jsp");
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