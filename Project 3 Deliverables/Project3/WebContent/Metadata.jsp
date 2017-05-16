<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Metadata</title>
	<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
	<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
	<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
</head>
<body>
	<c:choose>
	<c:when test="${loggedIn}">
		<c:choose>
			<c:when test="${isEmployee}">
				<div class="mainContent">
					<form action="Dashboard.jsp">
						<button type="submit" class="btn btn-primary">Back to dashboard</button>
					</form>
					<c:forEach var="table" items="${tables}">
						<h4>${table.name}</h4>
						<table class="table">
							<thead>
								<th>Column</th>
								<th>Type</th>
							</thead>
							<tbody>
								<c:forEach var="column" items="${table.columns}">
									<tr>
										<td class="col-md-1">${column.key}</td>
										<td class="col-md-1">${column.value}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
				<c:redirect url="./Main.jsp" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:redirect url="./Main.jsp" />
	</c:otherwise>
	</c:choose>
</body>
</html>