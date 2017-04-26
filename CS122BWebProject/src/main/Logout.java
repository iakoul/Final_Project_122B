package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Logout() {
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
	    		+ "Logout"
	    		+ "</title>\n"
	    		+ "</head>\n");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    out.println("<h1>Logging out</h1>\n");
	    out.println("</body>\n</html>");
	    session.setAttribute("loggedIn", false);
	    session.setAttribute("isAdmin", false);
	    response.setHeader("Refresh", "3; URL=./index.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
