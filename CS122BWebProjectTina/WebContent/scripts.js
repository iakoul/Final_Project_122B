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