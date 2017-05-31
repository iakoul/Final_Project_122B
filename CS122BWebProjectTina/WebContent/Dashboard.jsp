<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>Dashboard</title>
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
				<jsp:include page="header.jsp" />
				<div class="mainContent">
					<h3>Dashboard</h3>
					<!-- add item form -->
					<form action="./adminUpdate">
						<h4>Add an item</h4>
						<div class="form-group">
							<label>Item ID</label>
					    	<input class="form-control" name="itemid" placeholder="Enter item id">
						</div>
						<div class="form-group">
							<label>Item name</label>
					    	<input class="form-control" name="item-name" placeholder="Enter item name">
						</div>
						<div class="form-group">
							<label>Item type</label>
					    	<input class="form-control" name="item-type" placeholder="Enter item type">
						</div>
						<div class="form-group">
							<label>Item price</label>
							<input class="form-control" type="number" name="item-price" step="any">
						</div>
						<div class="form-group">
							<label>Item picture URL</label>
					    	<input class="form-control" name="item-pic" placeholder="Enter item picture url">
						</div>
						<div class="form-group">
							<label>Store ID</label>
					    	<input class="form-control" name="storeid" placeholder="Enter store id">
						</div>
						<input type="hidden" name="add-item" value="true">
						<button type="submit" class="btn btn-primary">Add</button>
					</form>

					<!-- add store form -->
					<form action="./adminUpdate">
						<h4>Add a store</h4>
						<p>Adding a Store
						<ul>
						<li>Fill out each of the store fields.</li>
						<li>Store Type: If your new store has a store type that is not in the database, then fill out the Store Type ID and Name fields. Otherwise, just put in the Store Type ID of the desired type.</li>
						<li>Plaza: If your new store is in a plaza that is not in the database, then fill out the Plaza ID and Name fields. Otherwise, just put in the Plaza ID.</li>
						<li>City: If your new store is in a city that is not in the database, then fill out the City ID and Name fields. Otherwise, just put in the City ID.</li>
						<li>Item: If your new store sells an item that is not in the databse, then fill out the Item fields. Otherwise, just put the Item ID.</li>
						</ul>
						<div class="form-group">
							<label>Store ID</label>
					    	<input class="form-control" name="storeid" placeholder="Enter store id">
						</div>
						<div class="form-group">
							<label>Store name</label>
					    	<input class="form-control" name="store-name" placeholder="Enter store name">
						</div>
						<div class="form-group">
							<label>Address</label>
					    	<input class="form-control" name="address" placeholder="Enter address">
						</div>
						<div class="form-group">
							<label>Phone number</label>
							<input class="form-control" name="store-phone" placeholder="Enter phone number">
						</div>
						<div class="form-group">
							<label>Year established</label>
					    	<input class="form-control" name="store-year" placeholder="Enter year established">
						</div>
						<div class="form-group">
							<label>Store Type ID</label>
					    	<input class="form-control" name="store-typeid" placeholder="Enter store type id">
						</div>
						<div class="form-group">
							<label>Store Type Name</label>
					    	<input class="form-control" name="store-type-name" placeholder="Enter store type name">
						</div>
						<div class="form-group">
							<label>Plaza ID</label>
					    	<input class="form-control" name="plazaid" placeholder="Enter plaza id">
						</div>
						<div class="form-group">
							<label>Plaza name</label>
					    	<input class="form-control" name="plaza-name" placeholder="Enter plaza name">
						</div>
						<div class="form-group">
							<label>City ID</label>
							<input class="form-control" name="cityid" placeholder="Enter city id">
						</div>
						<div class="form-group">
							<label>City name</label>
					    	<input class="form-control" name="city-name" placeholder="Enter city name">
						</div>
						<div class="form-group">
							<label>Item ID</label>
					    	<input class="form-control" name="itemid" placeholder="Enter item id">
						</div>
						<div class="form-group">
							<label>Item name</label>
					    	<input class="form-control" name="item-name" placeholder="Enter item name">
						</div>
						<div class="form-group">
							<label>Item type ID</label>
					    	<input class="form-control" name="item-typeid" placeholder="Enter item type id">
						</div>
						<div class="form-group">
							<label>Item type name</label>
					    	<input class="form-control" name="item-type-name" placeholder="Enter item type name">
						</div>
						<div class="form-group">
							<label>Item price</label>
							<input class="form-control" type="number" name="item-price" step="any">
						</div>
						<div class="form-group">
							<label>Item picture URL</label>
					    	<input class="form-control" name="item-pic" placeholder="Enter item picture url">
						</div>
						<input type="hidden" name="add-store" value="true">
						<button type="submit" class="btn btn-primary">Add</button>
					</form>

					<!-- metadata button -->
					<form action="./metadata">
						<h3>Metadata</h3>
						<button type="submit" class="btn btn-primary">Get metadata</button>
					</form>

					<!-- show metadata -->
					<c:forEach var="table" items="${tables}">
						<h4>${table.name}</h4>
						<table class="table">
							<thead>
								<tr>
									<th>Column</th>
									<th>Type</th>
								</tr>
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