<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Result Table</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<div style="text-align: right;">
		<a href="./mainPage">Main Page</a>
		<br>
		<a href="./shoppingCart">Cart</a>
	</div>
	<h3>Results</h3>
	Results per page: 
	<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=1&page=1&ascItem=${param.ascItem}&ascType=${param.ascType}&ascPrice=${param.ascPrice}">1</a>
	<c:forEach begin="1" end="4" var="i">
		<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${i*20}&page=1&ascItem=${param.ascItem}&ascType=${param.ascType}&ascPrice=${param.ascPrice}">${i*20}</a> 
	</c:forEach>

	<table class="table">
		<thead>
			<th>Item
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascItem=1&ascType=${param.ascType}&ascPrice=${param.ascPrice}">
					<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
				</a>
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascItem=0&ascType=${param.ascType}&ascPrice=${param.ascPrice}">
					<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
				</a>
			</th>
			<th>Type
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascType=1&ascPrice=${param.ascPrice}">
					<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
				</a>
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascType=0&ascPrice=${param.ascPrice}">
					<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
				</a>
			</th>
			<th>Price
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascPrice=1">
					<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
				</a>
				<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascPrice=0">
					<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
				</a>
			</th>
		</thead>
		<tbody>
			<c:forEach var="item" items="${items}">
				<tr>
					<td><a href="./item?id=${item.id}">${item.name}</a></td>
					<td>${item.type}</td>
					<td>${item.price}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<c:url value="/search" var="previousURL">
		<c:param name="business" value="${param.business}" />
		<c:param name="city" value="${param.city}" />
		<c:param name="payment" value="${param.payment}" />
		<c:param name="item" value="${param.item}" />
		<c:param name="price" value="${param.price}" />
		<c:param name="results" value="${param.results}" />
		<c:param name="ascType" value="${param.ascType}" />
		<c:param name="ascPrice" value="${param.ascPrice}" />
		<c:param name="ascItem" value="${param.ascItem}" />
		<c:choose>
			<c:when test="${empty param.page}">
				<c:param name="page" value="1" />
			</c:when>
			<c:when test="${param.page=='1'}">
				<c:param name="page" value="1" />
			</c:when>
			<c:otherwise>
				<c:param name="page" value="${param.page-1}" />
			</c:otherwise>
		</c:choose>
	</c:url>

	<c:url value="/search" var="nextURL">
		<c:param name="business" value="${param.business}" />
		<c:param name="city" value="${param.city}" />
		<c:param name="payment" value="${param.payment}" />
		<c:param name="item" value="${param.item}" />
		<c:param name="price" value="${param.price}" />
		<c:param name="results" value="${param.results}" />
		<c:param name="ascType" value="${param.ascType}" />
		<c:param name="ascPrice" value="${param.ascPrice}" />
		<c:param name="ascItem" value="${param.ascItem}" />
		<c:choose>
			<c:when test="${empty param.page}">
				<c:param name="page" value="2" />
			</c:when>
			<c:otherwise>
				<c:param name="page" value="${param.page+1}" />
			</c:otherwise>
		</c:choose>
	</c:url>

	<div>
		<div align="left">
			<a href='<c:out value="${previousURL}" />'>Previous</a>
		</div>
		<div align="right">
			<a href='<c:out value="${nextURL}" />'>Next</a>
		</div>
	</div>

</body>
</html>