/**
 * 
 */
var db = require("./db");

// prototype
function giornata(codiceBadge, cognome, nome, ruolo_id, turno_id, id) {
	this.id = id;
	this.dataRiferimento = dataRiferimento;
	this.note = note;
	this.statusGiornata = statusGiornata;
	this.dipendente_id = dipendente_id;
}


var getByDateAndDipendente = function(req, callBack) {
	console.log(req);
	var query = db.pool.query('SELECT * FROM GIORNATA WHERE dataRiferimento = ? AND dipendente_id = ?', 
			[req.dataRiferimento, req.dipendente_id], 
			function(err, rows, fields) {
				if (err) {
					if (callBack) { callBack(err); }
				} else {
					callBack(null, rows); 
				}
			});
};

module.exports = {
	giornata: giornata,
	getByDateAndDipendente : getByDateAndDipendente
};