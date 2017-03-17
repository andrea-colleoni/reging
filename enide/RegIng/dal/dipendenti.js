/**
 * 
 */
var db = require("./db");

// prototype
function dipendente(codiceBadge, cognome, nome, ruolo_id, turno_id, id) {
	this.id = id;
	this.codiceBadge = codiceBadge;
	this.cognome = cognome;
	this.nome = nome;
	this.ruolo_id = ruolo_id;
	this.turno_id = turno_id;
}


var getDipendenteByCodiceBadge = function(req, callBack) {
	console.log('req => codiceBadge: ' + req.codiceBadge);
	var query = db.pool.query('SELECT * FROM DIPENDENTE WHERE CODICEBADGE = ?', [req.codiceBadge], function(err, rows, fields) {
		if (err) {
			if (callBack) { callBack(err); }
		} else {
			if (rows.length === 1) {
				if (callBack) { callBack(null, rows[0]); }
			} else {
				if (callBack) { callBack(null, null); }
			}
		}
	});
};

module.exports = {
	dipendente: dipendente,
	getDipendenteByCodiceBadge : getDipendenteByCodiceBadge
};