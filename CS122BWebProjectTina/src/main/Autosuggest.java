package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Autosuggest
 */
@WebServlet("/Autosuggest")
public class Autosuggest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;
	
    public Autosuggest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    
		try {
			String terms[] = request.getParameter("query").split("\u0020");
			if (terms.length > 0 && !terms[0].equals("")) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				
				String searchType = request.getParameter("searchtype");
				String table = "";
				String column = "";
				switch (searchType) {
					case "item":
						table = "MerchandiseTbl";
						column = "merchName";
						break;
					case "itemSearch":
						table = "MerchandiseTbl";
						column = "merchName";
						break;
					case "price":
						table = "MerchandiseTbl";
						column = "merchPrice";
						break;
					case "business":
						table = "StoreTbl";
						column = "storeName";
						break;
					case "city":
						table = "CityTbl";
						column = "cityName";
						break;
					default:
						table = "MerchandiseTbl";
						column = "merchName";
						break;
				}
				
				String prepQuery = "SELECT " + column + " FROM " + table + " WHERE ";
				
			    
			    for (int i = 0; i < terms.length; ++i){
			    	prepQuery += column + " LIKE ?";
			    	if (i != terms.length - 1){
			    		prepQuery += " AND ";
			    	}
			    }
			    prepQuery += ";";
				
				PreparedStatement pstmt = connection.prepareStatement(prepQuery);
				
				for (int i = 1; i < terms.length + 1; ++i){
					if (i == terms.length + 1) {
						pstmt.setString(i, terms[i - 1] + "%");
					} else {
						pstmt.setString(i, "%" + terms[i - 1] + "%");
					}
			    }
				ResultSet results = pstmt.executeQuery();
				
				String output = "[ ";
				
				while(results.next()){
					output += "\"" + results.getString(1)+"\", ";
				}
				if (!output.equals("[ ")) {
					out.println(output.substring(0, output.length() - 2) + " ]");
				}
			}
			
		} catch (final Exception e) {
			out.println("mySQL driver was not loaded");
			out.println(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
