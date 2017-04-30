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
	    
	    HashMap<ArrayList<String>, Integer> cart = null;
	    ArrayList<String> itemAndStore = new ArrayList<String>();
	    itemAndStore.add(request.getParameter("storeid"));
	    itemAndStore.add(request.getParameter("itemid"));
	    
	    if (session.getAttribute("loggedIn") != null && (Boolean)session.getAttribute("loggedIn")) {
	    	if (session.getAttribute("cart") != null) {
		    	cart = (HashMap<ArrayList<String>, Integer>)session.getAttribute("cart");
		    	if (cart.containsKey(itemAndStore)) {
		    		Integer currQty = cart.get(itemAndStore);
		    		currQty += (Integer)request.getAttribute("qty");
		    		cart.put(itemAndStore, currQty);
		    		session.setAttribute("cart", cart);
		    	}
		    } else {
		    	cart = new HashMap<ArrayList<String>, Integer>();
		    	cart.put(itemAndStore, (Integer)request.getAttribute("qty"));
		    	session.setAttribute("cart", cart);
		    }
	    	//request.getRequestDispatcher(request.getParameter("viewid")).forward(request, response);
	    } else {
	    	response.setHeader("Refresh", "3; URL=./index.html");
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
