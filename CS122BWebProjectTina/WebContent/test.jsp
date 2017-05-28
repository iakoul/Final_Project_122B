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
				text-align: center;
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
		</style>
	</head>
	
	<title>Testing Page</title>
	
	<body>
		
		<div id="parent" style="border-style: solid;">
			 <table style="width:100%">
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
			 <form action="./autosuggest" >
			  	Enter query: <input type="text" oninput="OnInput(event)" onpropertychange="OnPropChanged(event)" name="query" value="test field" />
			  	<input type="submit" value="Submit">
			</form> 
		</div>
		
		
		<script>
		
		    // Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
	        function OnInput(event) {
	            alert ("The new content: " + event.target.value);
	        }
	        // Internet Explorer
	        function OnPropChanged(event) {
	            if (event.propertyName.toLowerCase () == "value") {
	                alert ("The new content: " + event.srcElement.value);
	            }
	        }
		
		
			var posx = 0;
			var posy = 0;
		
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
						var ajaxDisplay = document.getElementById('hoverBox');
						ajaxDisplay.innerHTML = ajaxRequest.responseText;
					}
				}
				
				//ajaxRequest.open("GET", "ajax-example.php" + queryString, true);
				ajaxRequest.open("GET", './hoverDiv?merchid=' + divid, true);
				ajaxRequest.send(null); 
			}
		
			function hoverdiv(e, divid){
				
				if (divid != 'hoverBox'){
					populateHoverbox(divid);
				} else {
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
					document.getElementById('hoverBox').innerHTML = 'Mouse position is: X=' + posx + ' Y=' + posy + ' <img id="pic" onmouseover="hoverdiv(event,  this.id)" src="test.jpg">';
					document.getElementById('hoverBox').style.left = posx + 'px';
					document.getElementById('hoverBox').style.top = posy + 'px';
				}
				document.getElementById('hoverBox').style.display='block';
			}
			
			function hoverleave(e, divid) {
				if (!e) var e = window.event;
				if (divid == 'hoverBox') {
					if ((e.pageX <= (parseInt(document.getElementById('hoverBox').style.left)) || e.pageX >= (parseInt(document.getElementById('hoverBox').style.left) + 400))
							|| e.pageY <= parseInt((document.getElementById('hoverBox').style.top)) || e.pageY >= (parseInt(document.getElementById('hoverBox').style.top) + 200)) {
						document.getElementById(divid).style.display = 'none';
					}
				} else {
					document.getElementById('hoverBox').style.display = 'none';
				}
			}
		
		</script>
		
	</body>

</html>