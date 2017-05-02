<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>City Details</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<h3>${city.name}</h3><br/>

	<table class="table">
		<thead>
    		<tr>
			    <th>Plazas</th>
    		</tr>
		</thead>
		<tbody>
			<c:forEach var="plaza" items="${plazas}">
				<tr>
					<td><a href="/Project2/plaza?id=${plaza.id}">${plaza.name}</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>