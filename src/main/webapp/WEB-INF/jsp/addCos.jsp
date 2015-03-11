<%--
  Created by IntelliJ IDEA.
  User: Joseph
  Date: 2015/3/1
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>add cos account</title>
</head>
<body>
<form id="addCos" method="post" action="account">
    <input type="hidden" value ="${subscriber.name}" name="subscriberName">
    host: <input type="text" name="host"/><br/>
    access_key: <input type="text" name="accessKey"/><br/>
    secret_key: <input type="text" name="secretKey"/><br/>
    <input type="submit" value="add"/><br/>
</form>
</body>
</html>
