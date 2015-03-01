<%--
  Created by IntelliJ IDEA.
  User: Joseph
  Date: 2015/3/1
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List Cos Account</title>
</head>
<body>
<table>
    <tr>
        <td>host</td>
        <td>access_key</td>
        <td>secret_key</td>
    </tr>
    　　 <c:forEach var="cosAccount" items="${cosList}">
    　　<tr>
    　　<td>${cosAccount.host}</td>
    　　<td>${cosAccount.accessKey}</td>
    <td>${cosAccount.secretKey}</td>
    　　</tr>
    　　</c:forEach>
    　　
</table>
</body>
</html>
