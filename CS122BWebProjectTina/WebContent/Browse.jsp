<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<h3>Browse</h3>
    <ul class="list-inline">
    	<c:forTokens items = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,#,Other" delims = "," var = "browseLetter">
    		<li><a href="./search?start=${browseLetter eq '#' ? 'num' : browseLetter}"><c:out value = "${browseLetter}"/></a> |</li>
    	</c:forTokens>
    </ul>
    <!--b>By type</b>
    <ul class="list-unstyled">
    	<c:forTokens items="Bakery,Bar,Barber,Bridal,Cafe,Cleaners,Clothing,Cosmetic,Crafts,Donuts,Education,Flowers,Franchises,Groceries,Gym,Home improve,Jewlry,Juice and Smoothie Bars,Karoke,Mail,Medical,Office supplies,Photography,Printing,Pet supplies,Restaurant,Shoes,Snacks and sweets,Storage,Tailoring,Toys,Trucking,Vet,Zpecialz" delims="," var="browseType">
    		<li><a href="./browse?type=${browseType}"><c:out value="${browseType}"/></a></li>
    	</c:forTokens>
    </ul-->
</body>
</html>