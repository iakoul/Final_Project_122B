<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Plaza Details</title>
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
			<h3>${plaza.name}</h3><br/>
			City:  <a href="./city?id=${plaza.city.id}">${plaza.city.name}</a><br/>

			<table class="table">
				<thead>
		    		<tr>
					    <th>Stores</th>
					    <th>Contact</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach var="business" items="${businesses}">
						<tr>
							<td><a href="./store?id=${business.id}">${business.name}</a></td>
							<td>
								${business.address}<br/>
								${business.phoneNumber}
							</td>
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