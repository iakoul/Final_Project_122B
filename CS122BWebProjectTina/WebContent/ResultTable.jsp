<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
		<title>Result Table</title>
		<link href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="css/basicStyle.css">
		<script src="css/bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
		<script src="css/bootstrap-3.3.7-dist/js/bootstrap.min.js" ></script>
		
		<style>		
			#hoverBox {
				z-index: 10000 !important;
				position: absolute;
				border-style: solid;
				bottom: 0px;
				right: 0px;
				width: 400px;
				height: 200px;
				display: none;
				background-color: #4286f4;
				text-align: left;
			}
		</style>
	</head>
	<body>
		<div id="hoverBox" onmouseover="hoverdiv(event, this.id)" onmouseout="hoverleave(event, this.id)"></div>
		<c:choose>
		<c:when test="${loggedIn}">
			<jsp:include page="header.jsp" />
			<div class="mainContent">
				<h3>Results</h3>
				Results per page: 
				<c:forEach begin="1" end="4" var="i">
					<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${i*20}&page=1&ascItem=${param.ascItem}&ascType=${param.ascType}&ascPrice=${param.ascPrice}&start=${param.start}&type=${param.type}">${i*20}</a> 
				</c:forEach>
	
				<table class="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Item
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascItem=1&ascType=${param.ascType}&ascPrice=${param.ascPrice}&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
								</a>
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascItem=0&ascType=${param.ascType}&ascPrice=${param.ascPrice}&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
								</a>
							</th>
							<th>Type
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascType=1&ascPrice=${param.ascPrice}&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
								</a>
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascType=0&ascPrice=${param.ascPrice}&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
								</a>
							</th>
							<th>Price
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascPrice=1&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-top" aria-hidden="true"></span>
								</a>
								<a href="./search?business=${param.business}&city=${param.city}&payment=${param.payment}&item=${param.item}&price=${param.price}&results=${param.results}&page=1&ascPrice=0&start=${param.start}&type=${param.type}">
									<span class="glyphicon glyphicon-triangle-bottom" aria-hidden="true"></span>
								</a>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${items}">
							<tr id="${item.id}" onmouseover="hoverdiv(event, this.id)" onmouseout="hoverleave(event, this.id)">
								<td>${item.id}</td>
								<td><a href="./item?id=${item.id}">${item.name}</a></td>
								<td>${item.type}</td>
								<td>${item.price}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
	
				<c:url value="/search" var="previousURL">
					<c:param name="business" value="${param.business}" />
					<c:param name="city" value="${param.city}" />
					<c:param name="payment" value="${param.payment}" />
					<c:param name="item" value="${param.item}" />
					<c:param name="price" value="${param.price}" />
					<c:param name="results" value="${param.results}" />
					<c:param name="ascType" value="${param.ascType}" />
					<c:param name="ascPrice" value="${param.ascPrice}" />
					<c:param name="ascItem" value="${param.ascItem}" />
					<c:param name="start" value="${param.start}" />
					<c:param name="type" value="${param.type}" />
					<c:choose>
						<c:when test="${empty param.page}">
							<c:param name="page" value="1" />
						</c:when>
						<c:when test="${param.page=='1'}">
							<c:param name="page" value="1" />
						</c:when>
						<c:otherwise>
							<c:param name="page" value="${param.page-1}" />
						</c:otherwise>
					</c:choose>
				</c:url>
	
				<c:url value="/search" var="nextURL">
					<c:param name="business" value="${param.business}" />
					<c:param name="city" value="${param.city}" />
					<c:param name="payment" value="${param.payment}" />
					<c:param name="item" value="${param.item}" />
					<c:param name="price" value="${param.price}" />
					<c:param name="results" value="${param.results}" />
					<c:param name="ascType" value="${param.ascType}" />
					<c:param name="ascPrice" value="${param.ascPrice}" />
					<c:param name="ascItem" value="${param.ascItem}" />
					<c:param name="start" value="${param.start}" />
					<c:param name="type" value="${param.type}" />
					<c:choose>
						<c:when test="${empty param.page}">
							<c:param name="page" value="2" />
						</c:when>
						<c:otherwise>
							<c:param name="page" value="${param.page+1}" />
						</c:otherwise>
					</c:choose>
				</c:url>
	
				<div>
					<div align="left">
						<a href='<c:out value="${previousURL}" />'>Previous</a>
					</div>
					<div align="right">
						<a href='<c:out value="${nextURL}" />'>Next</a>
					</div>
				</div>
	
			</div>
		</c:when>
		<c:otherwise>
			<c:redirect url="./Login.jsp" />
		</c:otherwise>
		</c:choose>
			<script>
				function populateHoverbox(divid){
					var ajaxRequest;  // The variable that makes Ajax possible!
					try{
					
					   // Opera 8.0+, Firefox, Safari
					   ajaxRequest = new XMLHttpRequest();
					}catch (e){
					   
					   // Internet Explorer Browsers
					   try{
					      ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
					   }catch (e) {
					      
					      try{
					         ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
					      }catch (e){
							// Something went wrong
					        alert("Your browser broke!");
					        return false;
							}
						}
					}
				   
					// Create a function that will receive data
					// sent from the server and will update
					// div section in the same page.
					ajaxRequest.onreadystatechange = function(){
						if(ajaxRequest.readyState == 4){
							if (divid != 'hoverBox' && divid != 'hoverBoxBuy'){
								var ajaxDisplay = document.getElementById('hoverBox');
								ajaxDisplay.innerHTML = ajaxRequest.responseText;	
							}
						}
					}
					
					//ajaxRequest.open("GET", "ajax-example.php" + queryString, true);
					ajaxRequest.open("GET", './hoverDiv?merchid=' + divid, true);
					ajaxRequest.send(null);
				}
			
				function hoverdiv(e, divid){
					
					if (divid != 'hoverBox') {
						populateHoverbox(divid);
						var posx = 0;
						var posy = 0;
						if (!e) var e = window.event;
						if (e.pageX || e.pageY)
						{
							posx = e.pageX;
							posy = e.pageY;
						}
						else if (e.clientX || e.clientY)
						{
							posx = e.clientX;
							posy = e.clientY;
						}
						document.getElementById('hoverBox').style.left = posx + 'px';
						document.getElementById('hoverBox').style.top = posy + 'px';
					}
					document.getElementById('hoverBox').style.display = 'block';
				}
				
				function hoverleave(e, divid) {
					if (divid == 'hoverBox') {
						if (e.pageX < parseInt(document.getElementById('hoverBox').style.left) || e.pageX > (parseInt(document.getElementById('hoverBox').style.left) + 400)
								|| e.pageY < parseInt(document.getElementById('hoverBox').style.top) || e.pageY > (parseInt(document.getElementById('hoverBox').style.top) + 200)) {
							document.getElementById(divid).style.display = 'none';
							document.getElementById(divid).innerHTML = "";
						}
					} else {
						document.getElementById('hoverBox').style.display = 'none';
					}
				}
			
			</script>
	</body>
</html>