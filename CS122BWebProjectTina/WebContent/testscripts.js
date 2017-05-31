function init() {
	document.body.onclick = function() {detectClicks(event);}
	document.body.setAttribute('onclick', 'detectClicks(event);');
}

window.addEventListener("load", init);

//detect clicking away from suggestion box
function detectClicks(e) {
	if (!e) var e = window.event;
	var target = e.target || e.srcElement;
	if (target.id == 'suggestDiv'){
		//do nothing
	} else if (target.id == 'result0') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result1') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result2') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result3') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result4') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result5') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result6') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result7') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result8') {
		document.getElementById('inputBox').value = target.innerText;
	} else if (target.id == 'result9') {
		document.getElementById('inputBox').value = target.innerText;
	} else {
		document.getElementById('suggestDiv').style.display = 'none';
	}
}


// Firefox, Google Chrome, Opera, Safari, Internet Explorer from version 9
function OnInput(event) {
	if (!event) var event = window.event;
	var target = event.target || event.srcElement;
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
			var ajaxDisplay = document.getElementById('suggestDiv');
			ajaxDisplay.style.display = "block";
			ajaxDisplay.style.top = document.getElementById('formDiv').getBoundingClientRect().bottom + 'px';
			ajaxDisplay.style.left = document.getElementById('formDiv').getBoundingClientRect().left + 'px';
			
			var response = ajaxRequest.responseText
			
			var insideHTML = '';
			
			var items = JSON.parse(response);
			
			var results = items.length;
			
			if (items.length > 10) {
				results = 10;
			}
			
			for(var i = 0; i < results; i++) {
			   insideHTML += '<div id="result' + i + '">' + items[i] + '</div>';
			}
			ajaxDisplay.innerHTML = insideHTML;
		}
	}
	
	ajaxRequest.open("GET", './autosuggest?query=' + target.value, true);
	ajaxRequest.send(null);
}


// Internet Explorer
function OnPropChanged(event) {
	if (!event) var event = window.event;
	var target = event.target || event.srcElement;
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
			var ajaxDisplay = document.getElementById('suggestDiv');
			ajaxDisplay.style.display = "block";
			ajaxDisplay.style.top = document.getElementById('formDiv').getBoundingClientRect().bottom + 'px';
			ajaxDisplay.style.left = document.getElementById('formDiv').getBoundingClientRect().left + 'px';
			
			var response = ajaxRequest.responseText
			
			var insideHTML = '';
			
			var items = JSON.parse(response);
			
			var results = items.length;
			
			if (items.length > 10) {
				results = 10;
			}
			
			for(var i = 0; i < results; i++) {
			   insideHTML += '<div id="result' + i + '">' + items[i] + '</div>';
			}
			ajaxDisplay.innerHTML = insideHTML;
		}
	}
	
	ajaxRequest.open("GET", './autosuggest?query=' + target.value, true);
	ajaxRequest.send(null); 
}


//HoverBox Stuff


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
	var posx = 0;
	var posy = 0;
	if (divid != 'hoverBox'){
		populateHoverbox(divid);
	} else {
		document.getElementById('hoverBox').innerHTML = 'Mouse position is: X=' + posx + ' Y=' + posy + ' <img id="pic" onmouseover="hoverdiv(event,  this.id)" src="sample.jpg">';
	}
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