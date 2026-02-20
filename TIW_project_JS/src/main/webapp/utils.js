/**
 * AJAX call management
 */

	function makeCall(method, url, formElement, cback, reset = true) {
	    var req = new XMLHttpRequest(); // visible by closure
	    req.onreadystatechange = function() {
	      cback(req)
	    }; // closure
	    req.open(method, url);
	    if (formElement == null) {
	      req.send();
	    } else {
	      req.send(new FormData(formElement));
	    }
	    if (formElement !== null && reset === true) {
	      formElement.reset();
	    }
	  }

	function createTD(row, content)
    {	
		var nameCell;
		
		nameCell = document.createElement("td");
        nameCell.setAttribute("class", "blueTable");
        nameCell.textContent = content;
        row.appendChild(nameCell);
	};
	
	function createLinkTD(row, content, key, showable)
	{
		var linkcell, anchor, linkText;
		
		linkcell = document.createElement("td");
		linkcell.setAttribute("class", "blueTable");
    	anchor = document.createElement("a");
        linkcell.appendChild(anchor);
        linkText = document.createTextNode(content);
        anchor.appendChild(linkText);
        anchor.setAttribute("attribute", key); // set a custom HTML attribute
        anchor.addEventListener("click", (event) =>
        {
      		showable.show(event.target.getAttribute("attribute")); // the list must know the details container
    	}, false);
    	anchor.href = "#";
    	row.appendChild(linkcell);	
	};
	
	function createButtonTD(row, key, showable)
	{
		var linkcell, buttonCell;
		
		linkcell = document.createElement("td");
		linkcell.setAttribute("class", "blueTable");
		
		buttonCell = document.createElement("button");
		buttonCell.innerHTML = "Modifica";
		
		linkcell.appendChild(buttonCell);
		
		buttonCell.setAttribute("attribute", JSON.stringify(key));
		buttonCell.addEventListener("click", (event) =>
		{
			showable.show(event.target.getAttribute("attribute"));
		}, false);
		row.appendChild(linkcell);	
	};
	
	function createCheckboxTD(row, key, actionWhenChecked, actionWhenFreed)
	{
		var linkcell, checkboxCell;
		
		linkcell = document.createElement("td");
		linkcell.setAttribute("class", "blueTable");
		
		checkboxCell = document.createElement("input");
		checkboxCell.setAttribute("type", "checkbox");
		
		linkcell.appendChild(checkboxCell);
		
		checkboxCell.setAttribute("attribute", key);
		
		checkboxCell.addEventListener("click", (event) =>
		{
		  	if (checkboxCell.checked)
		  	{
				actionWhenChecked(event.target.getAttribute("attribute"));	  
			}
			else
			{
				actionWhenFreed(event.target.getAttribute("attribute"));
			}
		}, false);
		row.appendChild(linkcell);
	};
	
	function createDetailTR(table, headerName, data)
	{
		var row, headerCell, dataCell;
		
		row = document.createElement("tr");
		row.setAttribute("class", "blueTable");
		
		headerCell = document.createElement("th");
		headerCell.innerHTML = headerName;
		headerCell.setAttribute("class", "grayTable");
		
		dataCell = document.createElement("td");
		dataCell.innerHTML = data;
		dataCell.setAttribute("class", "grayTable");
		
		row.appendChild(headerCell);
		row.appendChild(dataCell);
		
		table.appendChild(row);	
	};
	
	function emptyElementContent(tableBody)
	{
		tableBody.innerHTML = ""; // Empty the table body
	}