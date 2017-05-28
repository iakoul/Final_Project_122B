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
	    out.println("<!DOCTYPE HTML>\n"
	    		+ "<html>\n"
	    		+ "<head>\n"
	    		+ "<title>"
	    		+ "Performing Updates"
	    		+ "</title>\n"
	    		+ "</head>\n");
	    
	    out.println("<body>");
	    out.println(request.getParameter("query") + "<br>");
	    
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql:///storemarketing?autoReconnect=true&useSSL=false","root","mysqlpass");
			
			String prepQuery = "SELECT merchName FROM MerchandiseTbl WHERE ";
			
			String terms[] = request.getParameter("query").split("\u0020");
		    
		    for (int i = 0; i < terms.length; ++i){
		    	prepQuery += "merchName LIKE ?";
		    	if (i != terms.length - 1){
		    		prepQuery += " AND ";
		    	}
		    }
		    prepQuery += ";";
			
			PreparedStatement pstmt = connection.prepareStatement(prepQuery);
			
			for (int i = 1; i < terms.length + 1; ++i){
		    	pstmt.setString(i, "%" + terms[i - 1] + "%");
		    }
			out.println(pstmt.toString() + "<br>");
			ResultSet results = pstmt.executeQuery();
			
			while(results.next()){
				out.println(results.getString(1) + "<br>");
			}
			
			
			out.println("</body></html>");
			
		} catch (final Exception e) {
			out.println("mySQL driver was not loaded");
			out.println(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
