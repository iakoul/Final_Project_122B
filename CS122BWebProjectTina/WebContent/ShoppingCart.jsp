<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Shopping Cart</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="mainContent">
		<h3>Your Cart</h3>
		<c:forEach var="item" items="${cartItems}">
			<p>${item.key}</p>
		</c:forEach>
	</div>
</body>
</html>