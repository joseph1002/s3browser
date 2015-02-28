<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title> User Login </title>
</head>
<body>
<!-- error hint -->
<span style="color:red;font-weight:bold">
<%
if (request.getAttribute("err") != null)
{
	out.println(request.getAttribute("err") + "<br/>");
}
%>
<%--${err}--%>
</span>
<form id="login" method="post" action="login">
User: <input type="text" name="name"/><br/>
Password: <input type="password" name="password"/><br/>
<input type="submit" value="login"/><br/>
</form>
<a href="register">Register</a>
</body>
</html>