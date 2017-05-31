<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>City Details</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<c:choose>
	<c:when test="${loggedIn}">
		<jsp:include page="header.jsp" />
		<div class="mainContent">
			<h3>${city.name}</h3><br/>

			<table class="table">
				<thead>
		    		<tr>
					    <th>Plazas</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach var="plaza" items="${plazas}">
						<tr>
							<td><a href="./plaza?id=${plaza.id}">${plaza.name}</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:when>
	<c:otherwise>
		<c:redirect url="./Login.jsp" />
	</c:otherwise>
	</c:choose>
</body>
</html>