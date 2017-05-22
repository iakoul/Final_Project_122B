<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Advanced Search</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="mainContent">
		<form action="./search">
			<h3>Advanced Search</h3>

			<div class="form-group">
				<label for="item-input">Item</label>
	    		<input class="form-control" id="item" name="item" placeholder="Enter item">
			</div>

			<div class="form-group">
				<label for="max-price-input">Max Price</label>
				<input class="form-control" type="number" id="price" name="price">
			</div>

			<div class="form-group">
				<label for="business-name-input">Business</label>
	    		<input class="form-control" id="business" name="business" placeholder="Enter business name">
			</div>

			<div class="form-group">
				<label for="city-name-input">City</label>
	    		<input class="form-control" id="city" name="city" placeholder="Enter city name">
			</div>

			<div class="form-group">
				<label for="payment-type-input">Type of Payment</label>
	    		<input class="form-control" id="payment" name="payment" placeholder="Enter payment type">
			</div>

			<input type="hidden" name="page" value="1">

			<button type="submit" class="btn btn-primary">Search</button>
		</form>
	</div>
</body>
</html>