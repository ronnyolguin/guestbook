<!DOCTYPE html>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="es.um.guestbook.Greeting"%>
<%@ page import="es.um.guestbook.Guestbook"%>
<%@ page import="com.googlecode.objectify.Key"%>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <link href='/stylesheet/main.css' rel='stylesheet' type= 'text/css'>
    <title>Guestbook-class</title>
</head>
<body>
    <%
    UserService userService=UserServiceFactory.getUserService();
    User user= userService.getCurrentUser();

if(user != null){
    pageContext.setAttribute("user", user);
    %>


<p>Hello, ${fn:escapeXml(user.nickname)}! (You can <a href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign out</a>.)</p>
<%
} else{
%>
<p>Hello! <a href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign in</a>to include your name with greeting you post.</p>
<%
}
%>

<%--//[START datastore]--%>
<%
	String guestbookName = request.getParameter("guestbookName");
	if (guestbookName == null){
		guestbookName = "default";
	}
	pageContext.setAttribute("guestbookName", guestbookName);
	Key<Guestbook> theBook =Key.create(Guestbook.class, guestbookName);
	List<Greeting> greetings = ObjectifyService.ofy()
			.load()
			.type(Greeting.class)	//We want ony Greetings
			.ancestor(theBook)		//Anyone in this book
			.order("-date")			//Most recent first - date is indexed
			.limit(5)				//Only show 5 of them.
			.list();
	if (greetings.isEmpty()){
		%>
		
	<p>Guestbook'${fn:escapeXml(guestbookName)}' has no messages.</p>
	<%
	} else {
	%>
	<p>Messages in Guestbook'${fn:escapeXml(guestbookName)}'.</p>
	<%
			for (Greeting greeting : greetings){
				pageContext.setAttribute("greeting_content, greeting.content");
				String author;
				if (greeting.author_email==null){
					author = "An anonymous person";
				} else{
					author = greeting.author_email;
					String author_id = greeting.author_id;
					if (user != null && user.getUserId().equals(author_id)) {
						author += " (You) ";
					}
				}
				pageContext.setAttribute("greeting_user", author);
	%>
	<p><b>${fn:escapeXml(greeting_user)}</b> wrote:</p>
	<blockquote>${fn:escapeXml(greeting_content)}</blockquote>
	<%  
			}
				}
	%>
	<%--//[END datastore]--%>

<form action="/sign" method="post">
<div><textarea name="content" rows="3" cols="60"></textarea></div>
<div><input type="submit" value="Post Greeting" /></div>
<input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}" />
</form>
<form action="/guestbook.jps" method="get">
<div><input type="text" name="guestbookName" value="${fn:escapeXml(guestbookName)}" /></div>
<div><input type="submit" value="Switch Guestbook" /></div>
</form>
</body>
</html>