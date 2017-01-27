<%@page import="com.hotelmenu.user.MySqlDbConnection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

	<%
		MySqlDbConnection mydb = new MySqlDbConnection();
		String xx = mydb.connectDB();
		
		out.print(xx);
	%>