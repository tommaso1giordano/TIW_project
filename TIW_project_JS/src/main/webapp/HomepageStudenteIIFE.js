/**
 * HomepageDocenteIIFE script 
 */

{
	let pageOrchestrator = new PageOrchestrator(); // main controller
	let studente;
	let scaglioni;
		
	// URLs to get student
	let URL_STUDENTE = "GetStudente";
	
	window.addEventListener("load", () =>
	{
		makeCall("GET", URL_STUDENTE, null,
		        // callback function
		        function(req)
		        {
			    	if (req.readyState == XMLHttpRequest.DONE) // == 4
			    	{ 
		        		let message = req.responseText;
			            if (req.status == 200){
			            	studente = JSON.parse(message);
			            	pageOrchestrator.start(); 
			           	}else
				           	window.location.href = "index.html";
			      	}
		        }
      		);
 	}, false);
		  
	function PageOrchestrator()
  	{   
		let appelli;
		let esito;
		
	    this.start = function()
	    {
			alertContainer = document.getElementById("id_alert");
			
			scaglioni = new Scaglioni(alertContainer, document.getElementById("id_table_scaglioni_body"), this);
			scaglioni.addLinks(this);
			
			appelli = new Appelli
			(
				{
			        alert: document.getElementById("no_appelli_alert"),
			        appelli_header: document.getElementById("id_header_appelli"),
			        appelli_table: document.getElementById("id_table_appelli"),
			        appelli_table_body: document.getElementById("id_table_appelli_body"),
			        orchestrator : this,
				}
			);
			appelli.reset();
			
			esito = new Esito
			(
				{
			        alert: document.getElementById("id_alert_esito"),
			        esito_header: document.getElementById("id_esitoTableHead"),
			        esito_table: document.getElementById("id_esitoTable"),
			        esito_table_body: document.getElementById("id_esitoTableBody"),
			        esito_rifiuta_button: document.getElementById("id_rifiutaBtn"),
			        orchestrator : this,
				}
			);
			esito.reset();
			
			//in the scaglioni table select the id_scaglione of the first row
			let defaultScaglione = document.querySelector(".scaglioneNomeTable").getAttribute("id_scaglione");
			appelli.show(defaultScaglione);
	    };
	    
	    this.updateScaglione = function(id_scaglione)
		{
			esito.reset();
			appelli.show(id_scaglione);
		}; 
		
		this.updateAppello = function(id_appello)
		{
			esito.show(id_appello);
		}; 
 	}
	  
	// Component that handles the Scaglioni Table
	function Scaglioni(_alert, scaglioni_table_body, _orchestrator)
	{	
		this.alert = _alert;
	    this.scaglioni_table_body = scaglioni_table_body;
	    this.orchestrator = _orchestrator;

		//add the event to every row of scaglione table
	    this.addLinks = function(orchestrator)
	    {
			//list of every scaglione row
	    	let elems = scaglioni_table_body.getElementsByClassName("scaglioneNomeTable");
	    	
	    	//add click event to every row
	    	for(let i=0; i < elems.length; i++){
				elems[i].addEventListener('click', (event) => {
					//on click: update the page using the selected scaglione
		        	orchestrator.updateScaglione(event.target.getAttribute("id_scaglione"));
	      		});
			}
	    };
    }
    
    function Appelli(options)
    {
	    this.alert = options['alert'];
	    
	    this.appelli_table = options['appelli_table'];
	    this.appelli_table_body = options['appelli_table_body'];
	    this.appelli_header = options['appelli_header'];
	    this.orchestrator = options['orchestrator'];
	    
	    this.appelli_table.style.visibility = "hidden";

	    this.show = function(scaglioneid)
	    {
	    	let self = this;
	    	
	    	let address = "GetAppelli" + '?id_scaglione=' + scaglioneid;
	    	
	    	//get list of appelli from the selected scaglione
	      	makeCall("GET", address, null,
	        	function(req)
	        	{
	          		if (req.readyState == 4)
	          		{
	            		let message = req.responseText;
			            if (req.status == 200)
			            {
				        	let appelliToShow = JSON.parse(req.responseText);
				        	
				        	//check if response is empty
				        	if (req.responseText === "null")
			              	{
							    self.showError("Non ci sono appelli da mostrare");
			              	}
			              	else
			              	{
								//if response is not empty update table with new data
						    	self.update(appelliToShow);
							}
			            }
		                else
		                {
			              self.showError(message);
			            }
	          		}
        		}
	      );
	    };

	    this.reset = function()
	    {
	    	this.appelli_table.style.visibility = "hidden";
	    };

	    this.update = function(appelliArray)
	    {
	      	let row;
	      	let self = this;
	   		this.appelli_table_body.innerHTML = ""; // empty the table body
		    
		    // build updated list
		    appelliArray.forEach(function(appello)
		    { // self visible here, not this
		        row = document.createElement("tr");
		        
		        self.createTd(row, appello.date, appello.id);
		        
		        self.appelli_table_body.appendChild(row);
	      	});
	      	this.appelli_table.style.visibility = "visible";
	      	self.alert.style.visibility = "hidden";
	    };
    
	    this.createTd = function(row, date, id)
	    {	
			let self = this;
			
			td = document.createElement("td");
			td.setAttribute("class", "blueTable clickableDate");
			nameCell = document.createElement("a");
	        nameCell.setAttribute("id_appello", id);
	        nameCell.setAttribute("href", "#");
	        nameCell.textContent = date;
	        nameCell.addEventListener('click', (event) => {
					//add click event to every appello
		        	self.orchestrator.updateAppello(event.target.getAttribute("id_appello"));
	      		});
	        td.appendChild(nameCell);
	        row.appendChild(td);
		};
		
		this.showError = function(message)
		{
			this.alert.textContent = message;
			this.appelli_table.style.visibility = "hidden";
			this.alert.style.visibility = "visible";
		}
	}
	
	function Esito(options)
    {
	    this.alert = options['alert'];
	    
	    this.esito_table = options['esito_table'];
	    this.esito_table_body = options['esito_table_body'];
	    this.esito_header = options['esito_header'];
	    this.rifiutaBtn = options['esito_rifiuta_button'];
	    this.orchestrator = options['orchestrator'];
	    this.chosenAppello;
	    
	    let self = this;

	    this.show = function(idAppello)
	    {
			this.chosenAppello = idAppello;
			
			//get esito for given appello
	    	let address = 'GetEsito?id_appello=' + idAppello;
	      	makeCall("GET", address, null,
	        	function(req)
	        	{
	          		if (req.readyState == 4)
	          		{
	            		let message = req.responseText;
			            if (req.status == 200)
			            {
					    	self.update(JSON.parse(message));
			            }
		                else
		                {
							self.showError(message);
			            }
	          		}
        		}
	      	);
	      	
	      	this.rifiutaBtn.addEventListener('click', this.rifiutaAction);
	    };
	    
	    this.showError = function(errorMessage){
			this.alert.textContent = errorMessage;
			this.esito_table.style.visibility = "hidden";
			this.alert.style.visibility = "visible";
			this.rifiutaBtn.style.visibility = "hidden";
		};

	    this.reset = function()
	    {
			this.alert.style.visibility = "hidden";
	    	this.esito_table.style.visibility = "hidden";
	    	this.rifiutaBtn.style.visibility = "hidden";
	    };
	    
	    //function called when rifiuta button is clicked
	    this.rifiutaAction = function(){
			makeCall("GET", "Rifiuta?id_appello="+self.chosenAppello, null,
	        	function(req)
	        	{
	          		if (req.readyState == 4)
	          		{
	            		let message = req.responseText;
			            if (req.status == 200)
			            {
							self.orchestrator.updateAppello(self.chosenAppello);
			            }
		                else
		                {
							self.showError(message);
			            }
	          		}
        		}
	      	);
		};

	    this.update = function(esito)
	    {
	      	let row;
	      	this.reset();
	   		this.esito_table_body.innerHTML = ""; // empty the table body
		    
	        row = document.createElement("tr");
	        
	        this.createTd(row, esito.nomeInsegnamento);
	        this.createTd(row, esito.nomeDocente);
	        this.createTd(row, esito.data);
	        this.createTd(row, esito.voto);
	        this.createTd(row, esito.stato);
	        
	        this.esito_table_body.appendChild(row);
	        
	        this.esito_table.style.visibility = "visible";
	        this.alert.style.visibility = "hidden";
	        if(esito.rifiutabile)
	        	this.rifiutaBtn.style.visibility = "visible";
        	else
        		this.rifiutaBtn.style.visibility = "hidden";
	        	
	    };
    
	    this.createTd = function(row, content)
	    {	
			nameCell = document.createElement("td");
	        nameCell.setAttribute("class", "blueTable");
	        nameCell.textContent = content;
	        row.appendChild(nameCell);
		};
	}
}