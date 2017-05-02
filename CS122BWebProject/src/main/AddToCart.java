package main;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddToCart
 */
@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddToCart() {
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
				+ "Add to cart"
				+ "</title>\n"
				+ "</head>\n");

		out.println("<body bgcolor=\"#FDF5E6\">\n");
	    
	    
	    HashMap<ArrayList<String>, Integer> cart = null;
	    ArrayList<String> itemAndStore = new ArrayList<String>(); //[storeid, itemid]
	    itemAndStore.add(request.getParameter("storeid"));
	    itemAndStore.add(request.getParameter("itemid"));
	    
	    if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
	    	if (session.getAttribute("cart") != null) {
		    	cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
		    	if (cart.containsKey(itemAndStore)) {
		    		Integer currQty = (Integer)cart.get(itemAndStore);
		    		
		    		if (request.getParameter("update") != null && request.getParameter("update").equals("true")) {
		    			try {
		    				currQty = Integer.parseInt(request.getParameter("qty"));
		    				out.println("<p>Quantity updated. You will now be redirected. If you are not redirected, click the button. </p>\n");
		    			} catch (NumberFormatException e) {
		    				out.println("<p>Please enter a valid integer for the quantity of the item.</p>\n");
		    			}
		    		} else {
		    			try {
		    				currQty += Integer.parseInt(request.getParameter("qty"));
		    				out.println("<p>Done... added " + Integer.parseInt(request.getParameter("qty")) + " item to cart. "
					    			+ "You will now be redirected. If you are not redirected, click the button. </p>\n");
		    			} catch (NumberFormatException e) {
		    				out.println("<p>Please enter a valid integer for the quantity of the item.</p>\n");
		    			}
		    		}
		    		if (currQty <= 0) {
		    			cart.remove(itemAndStore);
		    		} else {
		    			cart.put(itemAndStore, currQty);
		    		}
		    		session.setAttribute("cart", cart);
		    	} else {
		    		cart.put(itemAndStore, Integer.parseInt(request.getParameter("qty")));
		    	}
		    } else {
		    	try {
		    		cart = new HashMap<ArrayList<String>, Integer>();
			    	cart.put(itemAndStore, Integer.parseInt(request.getParameter("qty")));
			    	session.setAttribute("cart", cart);
			    	out.println("<p>Done... added " + Integer.parseInt(request.getParameter("qty")) + " item to cart. "
			    			+ "You will now be redirected. If you are not redirected, click the button. </p>\n");
    			} catch (NumberFormatException e) {
    				out.println("<p>Please enter a valid integer for the quantity of the item.</p>\n");
    			}
		    }
	    	
	    	out.println("<button onclick=\"goBack()\">Go Back</button>");
	    	out.println("<script language=\"javascript\">\n");
			out.println("function goBack() {window.history.back();}\n");
			out.println("</script>\n");
	    	
	    	out.println("<script language=\"javascript\">\n"
	    			+ "setTimeout(function(){window.history.back();}, 0);\n"
	    			+ "</script>");
	    	
	    	//out.println("<script language=\"javascript\">setTimeOut(goBack(),10000);\n function goBack(){ $(\".button\").click();}\n</script>");
	    	
	    	out.println("</body>\n</html>");
	    } else {
	    	out.println("</body>\n</html>");
	    	response.setHeader("Refresh", "3; URL=./index.html");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
