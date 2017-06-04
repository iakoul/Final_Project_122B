package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import java.sql.*;

/*
 * Inserts new item into database
 */

public class MetadataResult extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Connection connection = null;
		ResultSet resultTables = null;
		ResultSet resultColumns = null;

		HttpSession session = request.getSession(true);
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		Boolean isEmployee = (Boolean)session.getAttribute("isEmployee");

		PrintWriter out = response.getWriter();

		if(loggedIn == true && isEmployee == true) {
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
				
				List<MetadataTable> tables = new ArrayList<MetadataTable>();

				//DatabaseMetaData dbMetaData = connection.getMetaData();
				DatabaseMetaData dbMetaData = dbcon.getMetaData();
				resultTables = dbMetaData.getTables(null, null, null, null);
				while(resultTables.next()) {
					String tableName = resultTables.getString(3);
					// out.println(tableName);
					MetadataTable table = new MetadataTable(tableName);
					resultColumns = dbMetaData.getColumns(null, null, tableName, null);
					while(resultColumns.next()) {
						String columnName = resultColumns.getString(4);
						int columnType = resultColumns.getInt(5);
						table.insertColumn(columnName, JDBCType.valueOf(columnType).getName());
						// out.println("  " + columnName + ", " + JDBCType.valueOf(columnType).getName());
					}
					tables.add(table);
					// out.println();
				}

				request.setAttribute("tables", tables);
				RequestDispatcher rd = request.getRequestDispatcher("Dashboard.jsp");
				rd.forward(request, response);
				dbcon.close();
			}
			catch(Exception e) {
				out.println(e.getMessage());
			}
			finally {
				try {
					resultTables.close();
					resultColumns.close();
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
		else {
			response.sendRedirect("EmployeeLogin.jsp");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}