/**
 * 
 */
var db = require("./db");

// prototype
function coppiaTimbrate(pausa, giornata_id, ingresso_id, uscita_id, id) {
	this.id = id;
	this.pausa = pausa;
	this.giornata_id = giornata_id;
	this.ingresso_id = ingresso_id;
	this.uscita_id = uscita_id;
}


var getById = function(req, callBack) {
	console.log(req);
	var query = db.pool.query('SELECT * FROM COPPIATIMBRATE WHERE ID = ?', 
			[req.id], 
			function(err, rows, fields) {
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
	coppiaTimbrate: coppiaTimbrate,
	getById : getById
};