import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.*;
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

	    boolean isAndroid = SearchResult.isStringEmpty(request.getParameter("isAndroid")) ? false : Boolean.parseBoolean(request.getParameter("isAndroid"));
	    
		try {
			String terms[] = request.getParameter("query").split("\u0020");
			if (terms.length > 0 && !terms[0].equals("")) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				
				String searchType = request.getParameter("searchtype");
				String table = "";
				String column = "";
				boolean fulltext = false;
				switch (searchType) {
					case "itemSearch":
						table = "MerchandiseTbl";
						column = "merchName";
						fulltext = true;
						break;
					case "item":
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
				if (fulltext) {
					
					//select merchName from merchandisetbl where match(`merchName`) AGAINST ('+qtd a8rh*' IN BOOLEAN MODE) LIMIT 10;
					
					String prepQuery = "SELECT " + column + " FROM " + table + " WHERE MATCH(`" + column + "`) AGAINST (? IN BOOLEAN MODE)";
					if(!isAndroid) {
						prepQuery += " LIMIT 10;";
					}
					
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					
					List<String> termList = Arrays.asList(terms); 
					
					if (termList.size() > 1) {
						for (int i = 0; i < termList.size(); ++i) {
							if (termList.get(i).length() < 3) {
								termList.set(i, "");
							}
						}
					}
					
					Vector<String> newTermList = new Vector<String>();
					
					for (int i = 0; i < termList.size(); ++i) {
						if (!termList.get(i).equals("")) {
							newTermList.add(termList.get(i));
						}
					}
					
					String searchTerms = "";
					
					for (int i = 0; i < newTermList.size(); ++i) {
						if (i != newTermList.size() - 1) {
							searchTerms += "+" + newTermList.get(i) + " ";
						} else {
							if (newTermList.size() == 1) {
								searchTerms += "+";
							}
							searchTerms += newTermList.get(i) + "*";
						}
					}
					
					pstmt.setString(1, searchTerms);
					ResultSet results = pstmt.executeQuery();
					
					if(isAndroid) {
						ArrayList<Item> items = new ArrayList<Item>();
						while(results.next()) {
							String itemName = results.getString(column);
							Item newItem = new Item(itemName);
							items.add(newItem);
						}
						String json = new Gson().toJson(items);
						out.println(json);
					}
					else {
						String output = "[ ";
					
						while(results.next()){
							output += "\"" + results.getString(1) + "\", ";
						}
						if (!output.equals("[ ")) {
							out.println(output.substring(0, output.length() - 2) + " ]");
						} else {
							out.println(output + "]");
						}
					}
					
					
					
				} else {
					String prepQuery = "SELECT " + column + " FROM " + table + " WHERE ";
					
				    
				    for (int i = 0; i < terms.length; ++i){
				    	prepQuery += column + " LIKE ?";
				    	if (i != terms.length - 1){
				    		prepQuery += " AND ";
				    	}
				    }
				    prepQuery += " LIMIT 10;";
					
					PreparedStatement pstmt = connection.prepareStatement(prepQuery);
					
					for (int i = 1; i < terms.length + 1; ++i){
						if (terms.length > 1 && i == terms.length) {
							pstmt.setString(i, terms[i - 1] + "%");
						} else {
							pstmt.setString(i, "%" + terms[i - 1] + "%");
						}
				    }
					ResultSet results = pstmt.executeQuery();
					
					String output = "[ ";
					
					while(results.next()){
						output += "\"" + results.getString(1) + "\", ";
					}
					if (!output.equals("[ ")) {
						out.println(output.substring(0, output.length() - 2) + " ]");
					} else {
						out.println(output + "]");
					}
				}
			} else if (terms[0].equals("")) {
				out.println("[]");
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