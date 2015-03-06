<%@ page import="com.amazonaws.services.s3.model.S3ObjectSummary" %>
<%@ page import="java.util.List" %>
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
    <title>List Bucket Contents</title>
</head>
<body>
<style type="text/css">
    table
    {
        border-collapse: collapse;
        border: none;
        width: 200px;
    }
    td
    {
        border: solid #000 1px;
    }
</style>
<table>
    <tr>
        <td>File</td>
        <%--<td>eTag</td>--%>
        <td>size</td>
        <td>owner</td>
        <td>last modified</td>
    </tr>
<c:forEach var="object" items="${objects}">
<tr>
<td>${object.key}</td>
<%--<td>${object.eTag}</td>--%>
<td>${object.size}</td>
<td>${object.owner}</td>
<td>${object.lastModified}</td>
</tr>
</c:forEach>
</table>
<%
    List<S3ObjectSummary> objectList = (List<S3ObjectSummary>)request.getAttribute("objects");
    String bucketName = objectList.get(0).getBucketName();
    String eTag = objectList.get(0).getETag();
    System.out.print("bucketName:" + bucketName + " eTag:" + eTag + "%n");
%>
<form method="POST" action=object enctype="multipart/form-data">
    <%--<input type="text" name="name" /><br>--%>
    <input type="file" name="file" /><br>
    <%--<input type="file" name="file" /><br>--%>
    <input type="submit" /><br>
</form>
</body>
</html>
