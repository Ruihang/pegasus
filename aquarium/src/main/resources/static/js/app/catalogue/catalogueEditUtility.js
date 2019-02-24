$(document).ready( function () {
	$("#issueDate").datepicker({autoclose: true});

    $("#catalogSubtypeCode").autocomplete({    	
    	minLength: 1,
    	delay: 500,
    	
    	//define callback to format results 
        source: function (request, response) {
            $.getJSON("/suggestCatalogueSubtype", request, function(result) {                
                response($.map(result, function(item) {                	
                    return {
                        // following property gets displayed in drop down
                        label: item.code,
                        // following property gets entered in the textbox
                        valueName: item.name,
                        valueId: item.id,
                        valueCode: item.code
                    }
                	
                }));
        	}); 
    	},
        
    	//define select handler
    	select : function(event, ui) {
            if (ui.item) {       
            	//alert('Name: ' + ui.item.value + '. Id: ' + ui.item.valueId + ". Code: " + ui.item.valueCode);
            	event.preventDefault();
                $("#catalogSubtypeId").val(ui.item.valueId);
                $("#catalogSubtypeCode").val(ui.item.valueCode);
                $("#catalogSubtypeName").val(ui.item.valueName);
                $("#catalogSubtypeCode").blur();
                return false; 
            }
    	}
    	  	
    });
	
    $("#catalogSubtypeName").autocomplete({    	
    	minLength: 3,
    	delay: 200,
    	
    	//define callback to format results 
        source: function (request, response) {
            $.getJSON("/suggestCatalogueSubtype", request, function(result) {                
                response($.map(result, function(item) {                	
                    return {
                        // following property gets displayed in drop down
                        label: item.name,
                        // following property gets entered in the textbox
                        valueName: item.name,
                        valueId: item.id,
                        valueCode: item.code
                    }
                	
                }));
        	}); 
    	},
        
    	//define select handler
    	select : function(event, ui) {
            if (ui.item) {       
            	//alert('Name: ' + ui.item.value + '. Id: ' + ui.item.valueId + ". Code: " + ui.item.valueCode);
            	event.preventDefault();
                $("#catalogSubtypeId").val(ui.item.valueId);
                $("#catalogSubtypeCode").val(ui.item.valueCode);
                $("#catalogSubtypeName").val(ui.item.valueName);
                $("#catalogSubtypeCode").blur();
                return false; 
            }
    	}
    	  	
    });
});