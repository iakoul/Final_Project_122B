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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPage() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.println("<!DOCTYPE HTML>"
	    		+ "<html>"
	    		+ "<head>"
	    		+ "<title>"
	    		+ "Log in"
	    		+ "</title>"
	    		+ "</head>");

	    out.println("<body bgcolor=\"#FDF5E6\">\n");
	    if ((Boolean)session.getAttribute("loggedIn")) {
			out.println("<h1 align=\"center\">Welcome, " + session.getAttribute("username") + " to StoreMarketing Search</h1>");
			out.println("<br>Search for specific terms" /* Link to search servlet */);
			out.println("<br>Browse alphabetically by store name");
			out.println("<ul>");
			out.println("<li><a href=\"./browse?letter=a\">A</a></li>");
			out.println("<li><a href=\"./browse?letter=b\">B</a></li>");
			out.println("<li><a href=\"./browse?letter=c\">C</a></li>");
			out.println("<li><a href=\"./browse?letter=d\">D</a></li>");
			out.println("<li><a href=\"./browse?letter=e\">E</a></li>");
			out.println("<li><a href=\"./browse?letter=f\">F</a></li>");
			out.println("<li><a href=\"./browse?letter=g\">G</a></li>");
			out.println("<li><a href=\"./browse?letter=h\">H</a></li>");
			out.println("<li><a href=\"./browse?letter=i\">I</a></li>");
			out.println("<li><a href=\"./browse?letter=j\">J</a></li>");
			out.println("<li><a href=\"./browse?letter=k\">K</a></li>");
			out.println("<li><a href=\"./browse?letter=l\">L</a></li>");
			out.println("<li><a href=\"./browse?letter=m\">M</a></li>");
			out.println("<li><a href=\"./browse?letter=n\">N</a></li>");
			out.println("<li><a href=\"./browse?letter=o\">O</a></li>");
			out.println("<li><a href=\"./browse?letter=p\">P</a></li>");
			out.println("<li><a href=\"./browse?letter=q\">Q</a></li>");
			out.println("<li><a href=\"./browse?letter=r\">R</a></li>");
			out.println("<li><a href=\"./browse?letter=s\">S</a></li>");
			out.println("<li><a href=\"./browse?letter=t\">T</a></li>");
			out.println("<li><a href=\"./browse?letter=u\">U</a></li>");
			out.println("<li><a href=\"./browse?letter=v\">V</a></li>");
			out.println("<li><a href=\"./browse?letter=w\">W</a></li>");
			out.println("<li><a href=\"./browse?letter=x\">X</a></li>");
			out.println("<li><a href=\"./browse?letter=y\">Y</a></li>");
			out.println("<li><a href=\"./browse?letter=z\">Z</a></li>");
			out.println("<li><a href=\"./browse?letter=numbers\">Numbers</a></li>");
			out.println("<li><a href=\"./browse?letter=others\">Others</a></li>");
			out.println("</ul>");
	    } else {
	    	out.println("<h1>Please log in</h1>");
	    	response.setHeader("Refresh", "3; URL=./index.html");
	    }
	    
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
