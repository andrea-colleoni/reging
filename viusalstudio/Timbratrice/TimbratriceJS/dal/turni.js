/**
 * 
 */
var db = require("./db");

// prototype
function turno(descrizione, durataOre, feriale, festivo, finePausa, ingressoMax, ingressoMin, inizioPausa, 
		nome, preFestivo, minutiArrotondamentoDifetto, minutiArrotondamentoEccesso, id) {
	this.id = id;
	this.descrizione = descrizione;
	this.durataOre = durataOre;
	this.feriale = feriale;
	this.festivo = festivo;
	this.finePausa = finePausa;
	this.ingressoMax = ingressoMax;
	this.ingressoMin = ingressoMin;
	this.inizioPausa = inizioPausa;	
	this.nome = nome;
	this.preFestivo = preFestivo;
	this.minutiArrotondamentoDifetto = minutiArrotondamentoDifetto;
	this.minutiArrotondamentoEccesso = minutiArrotondamentoEccesso;
}


var getById = function(req, callBack) {
	console.log(req);
	var query = db.pool.query('SELECT * FROM TURNO WHERE ID = ?', 
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
	turno: turno,
	getById : getById
};