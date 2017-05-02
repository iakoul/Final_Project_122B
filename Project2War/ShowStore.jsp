<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Store Details</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<h3>${business.name}</h3><br/>
	Address: ${business.address}<br/>
	City:  <a href="/Project2/city?id=${business.plaza.city.id}">${business.plaza.city.name}</a><br/>
	Plaza: <a href="/Project2/plaza?id=${business.plaza.id}">${business.plaza.name}</a><br/>
	Phone Number: ${business.phoneNumber}<br/>
	Year Opened: ${business.yearOpened}<br/>

	<table class="table">
		<thead>
    		<tr>
			    <th>Item</th>
			    <th>Price</th>
			    <th>Quantity</th>
    		</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${items}">
				<tr>
					<td><a href="/Project2/item?id=${item.id}">${item.name}</a></td>
					<td>${item.price}</td>
					<td>
						<form action="/Project2/addToCart">
						<div>
							<div class="form-group" align="left">
								<input class="form-control" type="number" id="quantity" name="quantity" value="1">
							</div>
							<input type="hidden" name="business" value="${business.id}">
							<input type="hidden" name="item" value="${item.id}">
							<div align="right">
								<button type="submit" class="btn btn-primary">Add to cart</button>
							</div>
						</div>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>