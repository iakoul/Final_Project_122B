package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class test
 */
@WebServlet("/HoverDiv")
public class HoverDiv extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection = null;

    public HoverDiv() {
        super();
    }

    public void destroy() {
    	try {
    		connection.close();
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (final Exception e) {
			out.println("mySQL driver was not loaded");
			out.println(e.getMessage());
		}
		
		try {
			connection = DriverManager.getConnection(MyConstants.DB_ADDRESS, MyConstants.DB_USERNAME, MyConstants.DB_PASSWORD);
			
			String prepQuery = "SELECT m.merchID, m.merchName, m.merchPrice, m.merchPic, st.storeType, s.storeID"
					+ " FROM MerchandiseTbl m, StoreTypeTbl st, StoreSellsTbl s"
					+ " WHERE m.merchID = ? AND m.merchType = st.TypeID AND m.merchID = s.merchID"
					+ " LIMIT 1;";
			PreparedStatement pstmt = connection.prepareStatement(prepQuery);
			pstmt.setString(1, request.getParameter("merchid"));
			ResultSet results = pstmt.executeQuery();
			
			while(results.next()) {
				out.print("Merch Pic = <img style=\"pointer-events:none; max-width: 50px; height: auto;\" src=\"" + results.getString(4) + "\"><br>"
						+ "Merch ID = " + results.getString(1) + "<br>"
						+ "Merch Name = " + results.getString(2) + "<br>"
						+ "Merch Price = $" + results.getString(3) + "<br>"
						+ "Merch Type = " + results.getString(5) + "<br>"
						+ "Store ID = " + results.getString(6) + "<br>"
						+ "<form action=\"./addToCart\" style=\"pointer-events: fill; visibility: visible;\">\n"
						+ "		<input type=\"hidden\" name=\"qty\" value=\"1\">\n"
						+ "		<input type=\"hidden\" name=\"storeid\" value=\"" + results.getString(6) + "\">\n"
						+ "		<input type=\"hidden\" name=\"itemid\" value=\"" + results.getString(1) + "\">\n"
						+ "		<button type=\"submit\" class=\"btn btn-primary\">Purchase from Top Seller</button>\n"
						+ "</form>");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
