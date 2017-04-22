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
 * Servlet implementation class MainPage
 */
@WebServlet("/MainPage")
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MainPage() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>"
	    		+ "<html>"
	    		+ "<head>"
	    		+ "<title>"
	    		+ "Welcome"
	    		+ "</title>"
	    		+ "</head>");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    if ((Boolean)session.getAttribute("loggedIn")) {
			out.println("<h1 align=\"center\">Welcome, " + session.getAttribute("username") + " to StoreMarketing Search</h1>");
			out.println("<br>Search for specific terms" /* Link to search servlet */);
			out.println("<br>Browse alphabetically by store name");
			out.println("<ul>");
			out.println("<li><a href=\"./browse?letter=a&pg=1\">A</a></li>");
			out.println("<li><a href=\"./browse?letter=b&pg=1\">B</a></li>");
			out.println("<li><a href=\"./browse?letter=c&pg=1\">C</a></li>");
			out.println("<li><a href=\"./browse?letter=d&pg=1\">D</a></li>");
			out.println("<li><a href=\"./browse?letter=e&pg=1\">E</a></li>");
			out.println("<li><a href=\"./browse?letter=f&pg=1\">F</a></li>");
			out.println("<li><a href=\"./browse?letter=g&pg=1\">G</a></li>");
			out.println("<li><a href=\"./browse?letter=h&pg=1\">H</a></li>");
			out.println("<li><a href=\"./browse?letter=i&pg=1\">I</a></li>");
			out.println("<li><a href=\"./browse?letter=j&pg=1\">J</a></li>");
			out.println("<li><a href=\"./browse?letter=k&pg=1\">K</a></li>");
			out.println("<li><a href=\"./browse?letter=l&pg=1\">L</a></li>");
			out.println("<li><a href=\"./browse?letter=m&pg=1\">M</a></li>");
			out.println("<li><a href=\"./browse?letter=n&pg=1\">N</a></li>");
			out.println("<li><a href=\"./browse?letter=o&pg=1\">O</a></li>");
			out.println("<li><a href=\"./browse?letter=p&pg=1\">P</a></li>");
			out.println("<li><a href=\"./browse?letter=q&pg=1\">Q</a></li>");
			out.println("<li><a href=\"./browse?letter=r&pg=1\">R</a></li>");
			out.println("<li><a href=\"./browse?letter=s&pg=1\">S</a></li>");
			out.println("<li><a href=\"./browse?letter=t&pg=1\">T</a></li>");
			out.println("<li><a href=\"./browse?letter=u&pg=1\">U</a></li>");
			out.println("<li><a href=\"./browse?letter=v&pg=1\">V</a></li>");
			out.println("<li><a href=\"./browse?letter=w&pg=1\">W</a></li>");
			out.println("<li><a href=\"./browse?letter=x&pg=1\">X</a></li>");
			out.println("<li><a href=\"./browse?letter=y&pg=1\">Y</a></li>");
			out.println("<li><a href=\"./browse?letter=z&pg=1\">Z</a></li>");
			out.println("<li><a href=\"./browse?letter=numbers&pg=1\">Numbers</a></li>");
			out.println("<li><a href=\"./browse?letter=others&pg=1\">Others</a></li>");
			out.println("</ul>");
	    } else {
	    	out.println("<h1>Please log in</h1>");
	    	response.setHeader("Refresh", "3; URL=./index.html");
	    }
	    
		out.println("</body></html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
