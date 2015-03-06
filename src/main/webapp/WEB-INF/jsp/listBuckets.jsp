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
        <td>name</td>
        <td>create_date</td>
        <td>owner</td>
    </tr>
    　　 <c:forEach var="bucket" items="${buckets}">
    　　<tr>
    　　<td>${bucket.name}</td>
    　　<td>${bucket.creationDate}</td>
    　　<td>${bucket.owner}</td>
    　　</tr>
    　　</c:forEach>
</table>
</body>
</html>
