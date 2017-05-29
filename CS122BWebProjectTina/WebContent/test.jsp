<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<style>
			body {
				background-color: #45f78c;
			}
			
			#hoverBox {
				position: absolute;
				border-style: solid;
				width: 400px;
				height: 200px;
				display: none;
				background-color: #4286f4;
				text-align: left;
				z-index: 2;
			}
			
			#mouseMe {
				background-color: #c9a05a;
				text-align: center;
				width: 80%;
				height: 50%;
				border-style: solid;
				margin: 5% 0% 10% 10%;
			}
			
			#pic {
				pointer-events:none;
			}
			
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
			
			#intheway {
				width: 500px;
				height: 300px;
				background-color: #9932cc;
				border-style: solid;
			}
			
			#formDiv {
				border-style: solid;
			}
			
			#parent {
				border-style: solid;
			}
			
			#testTable {
				width:100%;
			}
		</style>
	</head>
	
	<title>Testing Page</title>
	
	<body> <!--  onclick="detectClicks(event)">  -->
		<script src="testscripts.js"></script>
		<div id="parent">
			 <table id="testTable">
			 	<tr>
				    <th>Firstname</th>
				    <th>Lastname</th>
				    <th>Age</th>
			    </tr>
				    <tr id="8000" onmouseover="hoverdiv(event, this.id)" onmouseout="hoverleave(event, this.id)">
				    <td>Jill</td>
				    <td>Smith</td>
				    <td>50</td>
			    </tr>
			    <tr id="8001" onmouseover="hoverdiv(event, this.id)" onmouseout="hoverleave(event, this.id)">
				    <td>Eve</td>
				    <td>Jackson</td>
				    <td>94</td>
			    </tr>
			 </table>
		</div>
		
		<div id="hoverBox" onmouseover="hoverdiv(event, this.id)" onmouseout="hoverleave(event, this.id)"></div>
		
		<div id="mouseMe" onmouseover="hoverdiv(event, 'hoverBox')" onmouseout="hoverleave(event, this.id)">Hover over me</div>
		
		<div id="formDiv">
			 <form action="./autosuggest">
			  	Enter query: <input id="inputBox" type="text" oninput="OnInput(event)" onpropertychange="OnPropChanged(event)" name="query" value="test field" />
			  	<input type="submit" value="Submit">
			</form>
		</div>
		
		<div id="intheway">I am just getting in the way</div>
		
		<div id="suggestDiv"></div>
	</body>
</html>