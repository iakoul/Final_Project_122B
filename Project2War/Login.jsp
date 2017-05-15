<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<c:choose>
	<c:when test="${loggedIn}">
		<c:redirect url="./Main.jsp" />
	</c:when>
	<c:otherwise>
		<h1>Ubel Mart</h1>
		<div class="mainContent">
			<form action="./login" method="post">
				<div class="form-group">
		    		<label>Username</label>
		    		<input class="form-control" id="username" name="username" placeholder="Enter username">
				</div>
				<div class="form-group">
		    		<label>Password</label>
		    		<input type="password" class="form-control" id="password" name="password" placeholder="Password">
				</div>
				<button type="submit" class="btn btn-primary">Submit</button>
			</form>
			${error}
		</div>
	</c:otherwise>
	</c:choose>
</body>
</html>