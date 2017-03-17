$(function() {	
	refreshTables();
	
	$('#btnCloseDialogGiornata').click(function(evt) {
		evt.preventDefault();
		$('#dialogGiornata').modal('hide');
	});
	
	$('#dialogGiornata').on('shown.bs.modal', function() {
		$('#tabCoppieTimbrate').DataTable().columns.adjust().draw();
    })	
});

var regColumns = [ 
{ //0
	data : "data",
	title : "Data",
	width : "5%",
	visible: true
}, { //1
	data : "nominativo",
	title : "Nominativo",
	width : "10%",
	visible: true
}, { //2
	data: "giornata.classPresenteFuoriOrario",
	width : "3%",
	title: "O",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }
}, { //3
	data: "giornata.classModificataManualmente",
	width : "3%",
	title: "M",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }
}, { //4
	data: "giornata.classPresentePranzo",
	width : "3%",
	title: "P",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }	
}, { //5
	data: "giornata.classPresenteInterruzione",
	width : "3%",
	title: "I",
	render: function ( data, type, row ) {
        return '<span class="' + data + '">&nbsp;</span>';
    }	
}, { //6
	data : "giornata.statusGiornata",
	title : "Status",
	width : "20%",
	visible: false
}, { //7
	data : "e1",
	title : "E1",
	width : "4%",
	visible: true
}, { //8
	data : "u1",
	title : "U1",
	width : "4%",
	visible: true
}, { //9
	data : "e2",
	title : "E2",
	width : "4%",
	visible: true
}, { //10
	data : "u2",
	title : "U2",
	width : "4%",
	visible: true
}, { //11
	data : "oreTurno",
	title : "Ore turno",
	width : "7%",
	visible: true
}, { //12
	data : "oreLavorate",
	title : "Ore lavorate",
	width : "7%",
	visible: true
}, { //13
	data : "assenza",
	title : "Ore assenza",
	width : "7%",
	visible: true
}, { //14
	data : "straordinario",
	title : "Ore straord.",
	width : "7%",
	visible: true
}, { //15
	data : "giornata.note",
	title : "Note",
	width : "25%",
	visible: true
}, { //16
	data: "giornata.id",
	title : "",
	width : "5%",
	visible: true
}
];

var logsColumns = [
{ //0
	data : "ingresso.tipoTimbrata",
	title : "Tipo ingresso",
	width : "10%",
	visible: true
},{ //1
	data : "ingresso.orario",
	title : "Ora ingresso",
	width : "5%",
	visible: true
},{ //2
	data : "ingresso.causale",
	title : "Causale ingresso",
	width : "20%",
	visible: true
},{ //3
	data : "uscita",
	title : "Tipo uscita",
	width : "10%",
	visible: true
},{ //4
	data : "uscita",
	title : "Ora uscita",
	width : "5%",
	visible: true
},{ //5
	data : "uscita",
	title : "Causale uscita",
	width : "20%",
	visible: true
}                 
];

var reloadRegistrazioni = function(targetTableId, url, callback) {
	$.ajax({
		type : "GET",
		url : url
	}).done(function(data) {
		console.log(data);
		$('.editMonitRow').unbind('click'); 
		
		var dt = $('#' + targetTableId).DataTable({
			scrollY:        "400px",
	        scrollCollapse: true,
	        paging:         false,		
			retrieve: true,
			language: italianDT,				
			data : data,
			order: [[ 0, "desc" ]],
			columns : regColumns,
			columnDefs: [ {
	            targets: 16,
	            "render": function ( data, type, row ) {
                    return '<button class="btn btn-default glyphicon glyphicon-pencil editMonitRow" data-rowid="' + data + '"></button>';
                }
	        } ],			
			drawCallback: function( settings ) {
		        var api = this.api();
		        api.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
		        	// colorazione righe regolari/irregolari
		        	if($(this.cell(rowIdx, 6).node()).text() == 'Irregolare') {
		        		$(this.cell(rowIdx, 6).node()).addClass('danger');
		        	} else if($(this.cell(rowIdx, 6).node()).text() == 'Regolare') {
		        		$(this.cell(rowIdx, 6).node()).addClass('success');
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
    	
    	$('.editMonitRow').click(editMonitRow);    	
    	
		if (callback)
			callback(dt);
	});
};

var editMonitRow = function(evt) {
	evt.preventDefault();
	$.get(wsHome + '/giornata/' + $(this).data('rowid'))
	.done(function(data) {
		console.log(data);
		$('#dataGiornata').html(data.dataRiferimento);
		$('#statusGiornata').html(data.statusGiornata);
		$('#nominativoDipendente').html(data.badgeCognome + ' ' + data.badgeNome);
		$('#oreLavorate').html(data.oreLavorate);
		if(data.fuoriAPranzo) {
			$('#fuoriAPranzo').show();
		} else {
			$('#fuoriAPranzo').hide();
		}
		if(data.presenteFuoriOrario) {
			$('#fuoriOrario').show();
		} else {
			$('#fuoriOrario').hide();
		}
		var dt = $('#tabCoppieTimbrate').DataTable({
			scrollY:        "400px",
	        scrollCollapse: true,
	        paging:         false,		
			retrieve: true,
			language: italianDT,				
			data : data.coppieTimbrate,
			columns : logsColumns,
			columnDefs: [{
                "render": function ( data, type, row ) {
                	if(data) {
                		return data.tipoTimbrata;
                	} else {
                		return '';
                	}
                },
                "targets": 3
            },{
                "render": function ( data, type, row ) {
                	if(data) {
                		return data.orario;
                	} else {
                		return '';
                	}
                },
                "targets": 4
            },{
                "render": function ( data, type, row ) {
                	if(data) {
                		return data.causale;
                	} else {
                		return '';
                	}
                },
                "targets": 5
            }]	    
		});		
		$('#dialogGiornata').modal('show');
	});
};

var refreshTables = function() {
	reloadRegistrazioni('tabTutteTimbrate', wsHome + '/giornata');
	reloadRegistrazioni('tabOggiTimbrate', wsHome + '/giornata?today=true');	
}
