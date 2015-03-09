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
        <td>protocol</td>
        <td>connect</td>
    </tr>
    　　 <c:forEach var="cosAccount" items="${cosAccounts}">
        <tr>
        <td>${cosAccount.host}</td>
        <td>${cosAccount.accessKey}</td>
        <td>${cosAccount.secretKey}</td>
        <td>${cosAccount.protocol}</td>
    <td>
        <form id="connect" method="post" action="../entry">
            <input type="hidden" value="${cosAccount.host}" name="host">
            <input type="hidden" value="${cosAccount.accessKey}" name="accessKey"/><br/>
            <input type="hidden" value="${cosAccount.secretKey}" name="secretKey"/><br/>
            <input type="hidden" value="${cosAccount.protocol}" name="protocol"/><br/>
            <input type="submit" value="connect"/><br/>
        </form>
    </td>
    　　</tr>
    　　</c:forEach>
</table>
<a href="addCos">add an cos account</a>
</body>
</html>
