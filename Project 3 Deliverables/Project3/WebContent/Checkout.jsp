<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Checkout</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="mainContent">
		<jsp:include page="header.jsp" />
		<h3>Checkout</h3>
		<form action="./purchase">
			<div class="form-group">
			    <label>First name</label>
			    <input class="form-control" name="firstName" placeholder="Enter first name">
			</div>
			<div class="form-group">
			    <label>Last name</label>
			    <input class="form-control" name="lastName" placeholder="Enter last name">
			</div>
			<div class="form-group">
			    <label>Card number</label>
			    <input class="form-control" name="cardNumber" placeholder="Enter card number">
			</div>
			<div class="form-group">
				<label>Expiration date</label>
    			<input class="form-control" type="date" value="2017-05-13" name="expDate">
  			</div>
  			<button type="submit" class="btn btn-primary">Submit</button>
  		</form>
	</div>
</body>
</html>