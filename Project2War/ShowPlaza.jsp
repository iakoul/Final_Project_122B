<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Plaza Details</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<h3>${plaza.name}</h3><br/>
	City:  <a href="/Project2/city?id=${plaza.city.id}">${plaza.city.name}</a><br/>

	<table class="table">
		<thead>
    		<tr>
			    <th>Stores</th>
			    <th>Contact</th>
    		</tr>
		</thead>
		<tbody>
			<c:forEach var="business" items="${businesses}">
				<tr>
					<td><a href="/Project2/store?id=${business.id}">${business.name}</a></td>
					<td>
						${business.address}<br/>
						${business.phoneNumber}
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>