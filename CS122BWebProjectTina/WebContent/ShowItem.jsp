<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Item Display</title>
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
			<h3>${item.name}</h3><br/>
			Type: ${item.type}<br/>
			Price: ${item.price}<br/>
			<table class="table">
				<thead>
		    		<tr>
					    <th>Store</th>
					    <th>Quantity</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach var="business" items="${businesses}">
						<tr>
							<td><a href="./store?id=${business.id}">${business.name}</a></td>
							<td>
								<form action="./addToCart">
								<div>
									<div class="form-group" align="left">
										<input class="form-control" type="number" id="quantity" name="qty" value="1">
									</div>
									<input type="hidden" name="storeid" value="${business.id}">
									<input type="hidden" name="itemid" value="${item.id}">
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