function init() {
	document.body.onclick = function() {detectClicks(event);}
	document.body.setAttribute('onclick', 'detectClicks(event);');
}

window.addEventListener("load", init);

// detect clicking away from suggestion box
function detectClicks(e) {
	if (!e) var e = window.event;
	var target = e.target || e.srcElement;
	if (target.id == 'suggestDiv'){
		//do nothing
	} else if (target.id.substring(0, 6) == 'result') {
		document.getElementById(target.id.substring(7)).value = target.innerText;
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
			//ajaxDisplay.style.top = document.getElementById('searchForm').getBoundingClientRect().bottom + 'px';
			ajaxDisplay.style.top = event.target.getBoundingClientRect().bottom + 'px';
			//ajaxDisplay.style.left = document.getElementById('searchForm').getBoundingClientRect().left + 'px';
			ajaxDisplay.style.left = event.target.getBoundingClientRect().left + 'px';
			
			var response = ajaxRequest.responseText
			
			var insideHTML = '';
			
			var items = JSON.parse(response);
			
			var results = items.length;
			
			if (items.length > 10) {
				results = 10;
			}
			
			for(var i = 0; i < results; i++) {
			   insideHTML += '<div id="result' + i + event.target.id + '">' + items[i] + '</div>';
			}
			ajaxDisplay.innerHTML = insideHTML;
		}
	}
	
	ajaxRequest.open("GET", './autosuggest?searchtype=' + event.target.id + '&query=' + target.value, true);
	ajaxRequest.send(null);
}


// Internet Explorer
function OnPropChanged(event) {
	OnInput(event); 
}