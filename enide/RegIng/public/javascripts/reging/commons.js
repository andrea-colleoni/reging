/**
 * New node file
 */
var wsHome = 'http://localhost:8080/reging';

var italianDT = {
		"sEmptyTable":     "Nessun dato presente nella tabella",
		"sInfo":           "Vista da _START_ a _END_ di _TOTAL_ elementi",
		"sInfoEmpty":      "Vista da 0 a 0 di 0 elementi",
		"sInfoFiltered":   "(filtrati da _MAX_ elementi totali)",
		"sInfoPostFix":    "",
		"sInfoThousands":  ",",
		"sLengthMenu":     "Visualizza _MENU_ elementi",
		"sLoadingRecords": "Caricamento...",
		"sProcessing":     "Elaborazione...",
		"sSearch":         "Cerca:",
		"sZeroRecords":    "La ricerca non ha portato alcun risultato.",
		"oPaginate": {
			"sFirst":      "Inizio",
			"sPrevious":   "Precedente",
			"sNext":       "Successivo",
			"sLast":       "Fine"
		},
		"oAria": {
			"sSortAscending":  ": attiva per ordinare la colonna in ordine crescente",
			"sSortDescending": ": attiva per ordinare la colonna in ordine decrescente"
		}
};

var showEsito = function(code, message, callback) {
	$('#codiceEsito').html(code);
	$('#messaggioEsito').html(message);
	$('#dialogEsito').modal('show');
	
	$('#dialogEsito').on('hidden.bs.modal', function () {
		if (callback)
			callback();
	});
	
	setTimeout(function() {
		$('#dialogEsito').modal('hide')
	}, 5000);		
}

var showError = function(code, message, callback) {
	$('#codiceErrore').html(code);
	$('#messaggioErrore').html(message);
	$('#dialogErrors').modal('show');
	
	$('#dialogErrors').on('hidden.bs.modal', function () {
		if (callback)
			callback();
	});

	setTimeout(function() {
		$('#dialogErrors').modal('hide')
	}, 5000);			
}

var bindKeypress = function(elementId, callBack) {
	$(elementId).val('');
	
	$(document).keypress(function( event ) {
		if ( event.which == 13 ) {
			event.preventDefault();
			$(document).unbind('keypress');
			callBack($(elementId).val());
		} else {
			$(elementId).val($(elementId).val() + String.fromCharCode(event.which));
		}
	});	
}
