package es.um.guestbook;

import java.io.IOException;
//import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import es.um.guestbook.Greeting;

//@WebServlet(name = "GuestbookServlet", value = "/sign")
//public class GuestbookServlet extends HttpServlet {
//	private static final long serialVersionUID=1L;
//	private static final Logger Log = Logger.getLogger(GuestbookServlet.class.getName());
	

	/*
	 * @Override public void doGet(HttpServletRequest request, HttpServletResponse
	 * response) throws IOException {
	 * 
	 * UserService userService = UserServiceFactory. getUserService(); User
	 * currentUser = userService.getCurrentUser();
	 * 
	 * if (currentUser!=null){ response.setContentType("text/plain");
	 * response.setCharacterEncoding("UTF-8");
	 * response.getWriter().println("Hello. "+ currentUser.getNickname()); } else {
	 * response.sendRedirect(userService.createLoginURL(request.getRequestURI())); }
	 * 
	 * }
	 * }
	 * }
	 */  

//	@Override 
//	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
//		UserService userService = UserServiceFactory.getUserService();
//		User currentUser =userService.getCurrentUser();
//		
//		String guestbookName = request.getParameter("guestbookName");
//		String content = request.getParameter("content");
//		if (content == null) {
//			content ="(No greetings)";
//		}
//		
//		if (currentUser != null) {
//			Log.info("Greeting posted by user" + currentUser.getNickname() + ": " + content);
//		} else  {
//				Log.info("Greeting posted anonymously:" + content);
//			}
//		
//		response.sendRedirect("/guestbook.jps?guestbookName=" + guestbookName);
//	}
//	}

	@WebServlet(name = "GuestbookServlet", value = "/sign")
	public class GuestbookServlet extends HttpServlet {
	private static final long serialVersionUID= 1L;

	@Override 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Greeting greeting;
	UserService userService = UserServiceFactory.getUserService();
	User currentUser =userService.getCurrentUser();
	
	String guestbookName = request.getParameter("guestbookName");
	String content = request.getParameter("content");
	
	if (currentUser != null) {
	greeting = new Greeting(guestbookName, content, currentUser.getUserId(), currentUser.getEmail());
	} else  {
			greeting = new Greeting(guestbookName, content);
			}
		ObjectifyService.ofy().save().entity(greeting).now();
		response.sendRedirect("/guestbook.jps?guestbookName=" + guestbookName);
	}
		}

