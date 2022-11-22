<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- ===========header=============== -->
<c:import url="../common/header.jsp"></c:import>
<!-- ================================ -->
<h1>wholeSale JSP</h1>
<table class="table table-striped">
<thead><th>Name</th><th>small</th></thead>
<tbody>
<c:forEach items="${vo}" var="vos">
<tr>
<td>${vos.largeName}</td>
<td>${vos.small}</td>
</tr>
</c:forEach>
</tbody>
</table>


<!-- ===========footer=============== -->
<c:import url="../common/footer.jsp"></c:import>
<!-- ================================ -->
</body>
</html>