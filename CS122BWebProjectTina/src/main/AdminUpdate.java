package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminUpdate
 */
@WebServlet("/AdminUpdate")
public class AdminUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	Connection connection = null;
    public AdminUpdate() {
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
	    out.println("<!DOCTYPE HTML>\n"
	    		+ "<html>\n"
	    		+ "<head>\n"
	    		+ "<title>"
	    		+ "Performing Updates"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
		if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn") && session.getAttribute("isEmployee") != null && (Boolean)session.getAttribute("isEmployee")) {
			//Incorporate mySQL driver
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (final Exception e) {
				out.println("mySQL driver was not loaded");
				out.println(e.getMessage());
			}
			try {
				connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
				
				if (request.getParameter("add-store") != null && request.getParameter("add-store").equals("true")) {
					/*
					 * add_store(IN sID BIGINT, IN sName CHAR(100), IN sAddr CHAR(100), IN sPhone CHAR(20), IN sYear INTEGER, IN sTypeID INTEGER, 
					 * IN sType CHAR(100), IN sPlazaID BIGINT, IN sPlazaName CHAR(100), IN sCityID INTEGER,
					 * IN sCityName CHAR(100), IN mIDNum INTEGER, IN mName CHAR(100), IN mTypeID INTEGER, IN mType CHAR(100), IN mPrice DECIMAL(10,2), IN mPic VARCHAR(100))
					 */
					
					String prepCall = "{CALL add_store(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
					CallableStatement cs = connection.prepareCall(prepCall);
					
					if (request.getParameter("storeid").equals("")) {
						cs.setString(1, "1");
					} else {
						cs.setString(1, request.getParameter("storeid"));
					}
					cs.setString(1, request.getParameter("storeid"));
					cs.setString(2, request.getParameter("store-name"));
					cs.setString(3, request.getParameter("address"));
					cs.setString(4, request.getParameter("store-phone"));
					if (request.getParameter("store-year").equals("")) {
						cs.setString(5, "1999");
					} else {
						cs.setString(5, request.getParameter("store-year"));
					}
					if (request.getParameter("store-typeid").equals("")) {
						cs.setString(6, "1");
					} else {
						cs.setString(6, request.getParameter("store-typeid"));
					}
					cs.setString(7, request.getParameter("store-type-name"));
					if (request.getParameter("plazaid").equals("")) {
						cs.setString(8, "1");
					} else {
						cs.setString(8, request.getParameter("plazaid"));
					}
					cs.setString(9, request.getParameter("plaza-name"));
					if (request.getParameter("cityid").equals("")) {
						cs.setString(10, "1");
					} else {
						cs.setString(10, request.getParameter("cityid"));
					}
					cs.setString(11, request.getParameter("city-name"));
					if (request.getParameter("itemid").equals("")) {
						cs.setString(12, "1");
					} else {
						cs.setString(12, request.getParameter("itemid"));
					}
					cs.setString(13, request.getParameter("item-name"));
					if (request.getParameter("item-typeid").equals("")) {
						cs.setString(14, "1");
					} else {
						cs.setString(14, request.getParameter("item-typeid"));
					}
					cs.setString(15, request.getParameter("item-type-name"));
					if (request.getParameter("item-price").equals("")) {
						cs.setString(16, "1");
					} else {
						cs.setString(16, request.getParameter("item-price"));
					}
					cs.setString(17, request.getParameter("item-pic"));
					
					cs.execute();
					Integer updates = cs.getUpdateCount();
					
					if (updates >= 0) {
						out.println("<p>There were " + updates + " records updated.</p>\n");
					} else {
						out.println("<p>No records were updated.</p>\n");
					}
				} else if (request.getParameter("add-item") != null && request.getParameter("add-item").equals("true")) {
					String prepMerchQuery = "INSERT INTO MerchandiseTbl VALUES (?, ?, ?, ?, ?);";
					
					PreparedStatement pstmt = connection.prepareStatement(prepMerchQuery);
					pstmt.setString(1, request.getParameter("itemid"));
					pstmt.setString(2, request.getParameter("item-name"));
					if (request.getParameter("item-type").equals("")) {
						pstmt.setNull(3, Types.INTEGER);
					} else {
						pstmt.setString(3, request.getParameter("item-type"));
					}
					pstmt.setString(4, request.getParameter("item-price"));
					if (request.getParameter("item-pic").equals("")) { 
						pstmt.setNull(5, Types.VARCHAR);
					} else {
						pstmt.setString(5, request.getParameter("item-pic"));
					}
					
					
					Integer itemInsert = pstmt.executeUpdate();
					
					out.println("<p>" + itemInsert + " item(s) added to item database.</p>\n");
					
					String prepSellsQuery = "INSERT INTO StoreSellsTbl VALUES (?, ?);";
					
					pstmt = connection.prepareStatement(prepSellsQuery);
					pstmt.setString(1, request.getParameter("storeid"));
					pstmt.setString(2, request.getParameter("itemid"));
					
					Integer sellsInsert = pstmt.executeUpdate();
					
					out.println("<p>" + sellsInsert + " item(s) added to storesells database.</p>\n");
				} else {
					out.println("<p>You are not supposed to be here. You will be redirected.</p>\n");
				}
			} catch (Exception e){
				out.println("SQL Error: " + e.getMessage());
			}	
			out.println("</body>\n</html>");
			response.setHeader("Refresh", "3; URL=Dashboard.jsp");
		} else {
			out.println("</body>\n</html>");
			response.setHeader("Refresh", "3; URL=Main.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
