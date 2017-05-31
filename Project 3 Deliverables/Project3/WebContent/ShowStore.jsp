<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Store Details</title>
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
			<h3>${business.name}</h3><br/>
			Address: ${business.address}<br/>
			City:  <a href="./city?id=${business.plaza.city.id}">${business.plaza.city.name}</a><br/>
			Plaza: <a href="./plaza?id=${business.plaza.id}">${business.plaza.name}</a><br/>
			Phone Number: ${business.phoneNumber}<br/>
			Year Opened: ${business.yearOpened}<br/>

			<table class="table">
				<thead>
		    		<tr>
					    <th>Item</th>
					    <th>Price</th>
					    <th>Quantity</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${items}">
						<tr>
							<td><a href="./item?id=${item.id}">${item.name}</a></td>
							<td>${item.price}</td>
							<td>
								<form action="./addToCart">
								<div>
									<div class="form-group" align="left">
										<input class="form-control" type="number" id="quantity" name="quantity" value="1">
									</div>
									<input type="hidden" name="business" value="${business.id}">
									<input type="hidden" name="item" value="${item.id}">
									<div align="right">
										<button type="submit" class="btn btn-primary">Add to cart</button>
									</div>
								</div>
								</form>
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