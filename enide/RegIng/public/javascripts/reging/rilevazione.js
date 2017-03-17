$(function() {	
	activateKybRead();
	$('#btnAssignTimbrata').click(assignBarcode);
	
	$('#btnCloseDialogCollaboratore').click(function() {
		$("#dialogCollaboratore").modal('hide');
	});
	
	$("#dialogCollaboratore").on('shown.bs.modal', function () {
		$('#tabTimbrateCollaboratore').DataTable().columns.adjust().draw();
	});	
	$("#dialogCollaboratore").on('hidden.bs.modal', function () {
		activateKybRead();
	});	
	$("#tipoTimbrata").change(function() {
		$('#pnlCausale').collapse('show');
	})
	$("#dataOraTimbrata").change(function() {
		$('#pnlCausale').collapse('show');
	})	
	
	$('#dtpkDataOra').datetimepicker({
        locale: 'it'
    });
	$('#dtpkDataOra').on('dp.change', function (e) {
		$('#dataOraTimbrata').val(e.date.format('DD/MM/YYYY HH:mm'));
	});	
});	

var collabColumns = [ 
{ //0
	data : "dataRiferimento",
	title : "Data",
	width : "20%",
	visible: true
}, { //1
	data: "classPresenteFuoriOrario",
	width : "3%",
	title: "O",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }
}, { //2
	data: "classModificataManualmente",
	width : "3%",
	title: "M",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }
}, { //3
	data: "classPresentePranzo",
	width : "3%",
	title: "P",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }	
}, { //4
	data: "classPresenteInterruzione",
	width : "3%",
	title: "I",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }	
}, { //4
	data : "statusGiornata",
	title : "Status",
	width : "20%",
	visible: true
}, { //5
	data : "oreLavorate",
	title : "Ore lavorate",
	width : "20%",
	visible: true
}, { //6
	data : "note",
	title : "Note",
	width : "30%",
	visible: true
}
];

var reloadRegistrazioni = function(targetTableId, url, callback) {
	$.ajax({
		type : "GET",
		url : url
	}).done(function(data) {
		console.log(data);
		var dt = $('#' + targetTableId).DataTable({
			retrieve: true,
			language: italianDT,				
			data : data,
			order: [[ 0, "desc" ]],
			columns : regColumns
		});
		if (callback)
			callback(dt);
	});
};

var reloadCollaboratore = function(targetTableId, url, callback) {
	$.ajax({
		type : "GET",
		url : url
	}).done(function(data) {
		console.log(data);
		var dt = $('#' + targetTableId).DataTable({
			scrollY:        "400px",
	        scrollCollapse: true,
	        paging:         false,		
			retrieve: true,
			language: italianDT,				
			data : data,
			order: [[ 0, "desc" ]],
			columns : collabColumns,
			drawCallback: function( settings ) {
		        var api = this.api();
		        api.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
		        	// colorazione righe regolari/irregolari
		        	if($(this.cell(rowIdx, 5).node()).text() == 'Irregolare') {
		        		$(this.cell(rowIdx, 5).node()).addClass('danger');
		        	} else if($(this.cell(rowIdx, 5).node()).text() == 'Regolare') {
		        		$(this.cell(rowIdx, 5).node()).addClass('success');
		        	};
		        });
		    }
		});
		//icona fuori orario (glyphicon glyphicon-time)
    	$('.presenteFuoriOrario').addClass('glyphicon glyphicon-time');
    	
    	//icona modificata manualmente (glyphicon glyphicon-edit)
    	$('.modificataManualmente').addClass('glyphicon glyphicon-edit');
    	
    	// icona pranzo (glyphicon glyphicon-cutlery)
    	$('.presentePranzo').addClass('glyphicon glyphicon-cutlery');
    	
    	// icona interruzione (glyphicon glyphicon-transfer)
    	$('.presenteInterruzione').addClass('glyphicon glyphicon-transfer');
    	
		if (callback)
			callback(dt);
	});
};

var toggleBarcode = function(barcode, callback) {
	$.ajax({
		type : "POST",
		url : wsHome + '/registrazione/toggle/' + barcode
	}).done(function(data) {
		console.log(data);
		showEsito(data);
		callback;
	});
};

var refreshTables = function() {
	reloadRegistrazioni('tabHistory', wsHome + '/registrazione');
	reloadRegistrazioni('tabToday', wsHome + '/registrazione?today=true');	
}

var activateKybRead = function() {
	$(document).unbind('keypress');
	$('#txtRegBarcode').val('');
	bindKeypress('#txtRegBarcode', processBarcode);	
}
// 1-
var processBarcode = function(strBadge) {
	// log della timbrata
	$.post(wsHome + '/timbrata/log/' + strBadge)
	.done(function(timbrata){
		// processo la timbrata
		processTimbrata(timbrata);
	})
	.fail(function(xhr, status, error) {
		showError('002', 'Errore di rete [' + error + ']; riprovare', function() {
  			activateKybRead();
  		});
	});
};
// 2-
var processTimbrata = function(timbrata) {
	if (timbrata) {
		$('#idTimbrata').val(timbrata.id);
		if(timbrata.necessitaCausale) {
			$('#pnlCausale').collapse('show');
		}
		// verifico se esiste il dipendente
		$.get(wsHome + '/dipendente/byCodiceBadge/' + timbrata.codiceBadge)
		.done(function(data) {
			if (data) {
				// esiste => ricarico la tabella delle giornate del mese
				reloadCollaboratore('tabTimbrateCollaboratore', wsHome + '/giornata/dipendente?badge=' +
						timbrata.codiceBadge + '&month=' + (new Date().getMonth() + 1),
						function(dt) {
					// mostro la schermata di riepilogo del collaboratore
					$('#dialogCollaboratore').modal('show');
				});
				
				// riporto le informazioni sullo stato del collaboratore
				$('#turnoCollaboratore').html(data.turno.descrizione);
				$('#saldoRecuperoOre').html(data.saldiBancaOre.RecuperoFlessibilita);
				$('#nominativoCollaboratore').html(data.nome + ' ' +  data.cognome);
				
				$('#ctldpDataOra').val(timbrata.dataConOrario);
				$('#dataOraTimbrata').val(timbrata.dataConOrario);
				$('#tipoTimbrata').val(timbrata.tipoTimbrata);				
			} else {
				showError('001', 'Badge non riconosciuto; riprovare', activateKybRead);				
			}
		})
		.fail(function(xhr, status, error) {
			showError('002', 'Errore di rete [info: ' + error + ']; riprovare', activateKybRead);
		});	
	} else {
		showError('004', 'Timbrata nulla', activateKybRead);
	}
}

var assignBarcode = function(e) {
	e.preventDefault();
	if ($("#pnlCausale" ).is( ":visible" ) && $('#txtCausale').val() == '') {
		$('#wrngCausale').collapse('show');
	} else {
		$.post(wsHome + '/timbrata/assign/' + $('#idTimbrata').val(), {
	  		tipoTimbrata: $('#tipoTimbrata').val(),
	  		dataOraTimbrata: $('#dataOraTimbrata').val(),
	  		causale: $('#txtCausale').val()
	  	})
	  	.done(function(esito) {
	  		showEsito('OK', 'Timbrata registrata correttamente', function() {
	  			$('#dialogCollaboratore').modal('hide');
	  		});	
	  	})
		.fail(function(xhr, status, error) {
			$('#dialogCollaboratore').modal('hide');
			showError('003', 'Errore di assegnazione della timbrata', function() {
	  			$('#dialogCollaboratore').modal('hide');
	  		});	
		});	
	}
}
	
