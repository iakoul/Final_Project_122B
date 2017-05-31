<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Main</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<c:choose>
	<c:when test="${loggedIn}">
		<jsp:include page="header.jsp" />
		<c:if test="${not empty name}">
			<h3>Welcome, ${name}!</h3>
		</c:if>
		<div class="mainContent">
			<div>
				<jsp:include page="Browse.jsp" />
		    </div>
	    </div>
	</c:when>
	<c:otherwise>
		<c:redirect url="./Login.jsp"/>
	</c:otherwise>
	</c:choose>
</body>
</html>