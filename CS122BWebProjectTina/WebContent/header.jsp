<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	#suggestDiv {
		position: absolute;
		border-style: solid;
		width: 400px;
		height: 200px;
		display: none;
		background-color: #a9a9a9;
		text-align: left;
		z-index: 2;
	}
</style>

<script src="autosuggestscripts.js"></script>
<div id="suggestDiv"></div>
<nav class="navbar navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navlinks" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="./Main.jsp">Übel Mart</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="navlinks">



      <ul class="nav navbar-nav">
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Category <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <!-- <ul class="list-unstyled"> -->
              <c:forTokens items="Bakery,Bar,Barber,Bridal,Cafe,Cleaners,Clothing,Cosmetic,Crafts,Donuts,Education,Flowers,Franchises,Groceries,Gym,Home improve,Jewelry,Juice and Smoothie Bars,Karaoke,Mail,Medical,Office supplies,Photography,Printing,Pet supplies,Restaurant,Shoes,Snacks and sweets,Storage,Tailoring,Toys,Trucking,Vet,Zpecialz" delims="," var="browseType">
                <li><a href="./search?type=${browseType}"><c:out value="${browseType}"/></a></li>
              </c:forTokens>
            <!-- </ul> -->
          </ul>
        </li>
        <li><a href="AdvancedSearch.jsp">Advanced Search</a></li>
      </ul>



      <form id="searchForm" class="navbar-form navbar-left" action="./search">
        <div class="form-group">
          <input id="itemSearch" type="text" class="form-control" name="item" placeholder="Search item" autocomplete="off" oninput="OnInput(event)" onpropertychange="OnPropChanged(event)">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="./logout">Logout</a></li>
        <li><a href="./shoppingCart">Cart</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>