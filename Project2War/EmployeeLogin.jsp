<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Employee Login</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
	<script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body>
	<div class="mainContent">
		<h3>Employee Login</h3>
		<form action="./employeeLogin" method="post">
			<div class="form-group">
	    		<label>Username</label>
	    		<input class="form-control" id="username" name="username" placeholder="Enter username">
			</div>
			<div class="form-group">
	    		<label>Password</label>
	    		<input type="password" class="form-control" id="password" name="password" placeholder="Password">
			</div>
			<div class="g-recaptcha" data-sitekey="6LfkZSEUAAAAAJ1eB5CQ1XWc-FrXt59rM-gDOIZa"></div>
			<button type="submit" class="btn btn-primary">Submit</button>
		</form>
		${error}
	</div>
</body>
</html>