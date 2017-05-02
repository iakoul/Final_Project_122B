<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Item Display</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
</head>
<body>
	<div style="text-align: right;">
		<a href="./mainPage">Main Page</a>
		<br>
		<a href="./shoppingCart">Cart</a>
	</div>
	<h3>${item.name}</h3><br/>
	Type: ${item.type}<br/>
	Price: ${item.price}<br/>
	<table class="table">
		<thead>
    		<tr>
			    <th>Store</th>
			    <th>Quantity</th>
    		</tr>
		</thead>
		<tbody>
			<c:forEach var="business" items="${businesses}">
				<tr>
					<td><a href="./store?id=${business.id}">${business.name}</a></td>
					<td>
						<form action="./addToCart">
						<div>
							<div class="form-group" align="left">
								<input class="form-control" type="number" id="quantity" name="qty" value="1">
							</div>
							<input type="hidden" name="storeid" value="${business.id}">
							<input type="hidden" name="itemid" value="${item.id}">
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