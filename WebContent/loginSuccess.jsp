<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII" import="org.miniPaypal.test.User.*" import="java.util.List" import="java.util.Iterator" import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login Success</title>
</head>
<body>

<%
//User userInfo = (User) request.getAttribute("user");
User userInfo = (User) request.getSession().getAttribute("user");
%>
Welcome <%= userInfo.getUserName() %>
<br>
Account Balance - <%= userInfo.getBalance() %>

<h4> Perform a transaction </h4>
<form action = "secure/userTransaction" method = "post">
	<br> Destination user id: <input type = "text" name = "destUserId" />
	<br> Amount <input type = "text" name = "amount" />
	<br> <input type = "submit" name = "transfer" />
	<input type = "hidden" name = "viewid" value = "/loginSuccess.jsp">
</form>

<%
request.getSession().setAttribute("user", userInfo);
%>

<h4> Transaction History</h4>
<table style = "width:100%;border: 1px solid black">
	<tr>
		<th> Name </th>
		<th> Date </th>
		<th> Credit </th>
		<th> Debit </th>
	</tr>
	
<%
//List<Transaction> list = (List<Transaction>) request.getAttribute("transactions");
List<Transaction> list = (List<Transaction>) request.getSession().getAttribute("transactions");
Iterator<Transaction> itr = list.iterator();
while (itr.hasNext()) {
	Transaction t = itr.next();
%>
	<tr>
		<td> <%= t.getDestuserid() %> </td>
		<td> <%= new SimpleDateFormat("E yyyy.MM.dd hh:mm").format(t.getDate()) %> </td>
		<td> <%= t.getCredit() %> </td>
		<td> <%= t.getDebit() %> </td>
	</tr>
<% } %>	

</table>	

</body>
</html>