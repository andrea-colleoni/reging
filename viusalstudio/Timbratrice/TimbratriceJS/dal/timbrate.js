/**
 * 
 */
var db = require("./db");
var moment = require('moment');

var ttRepo = require("./tipiTimbrata");
var dRepo = require("./dipendenti");
var gRepo = require("./giornate");
var tnRepo = require("./turni");

// prototype
function timbrata(codiceBadge, dataOra, tipoTimbrata, ip, fuoriOrarioTurno,
		modificataManualmente, id) {
	this.id = id;
	this.tipoTimbrata = tipoTimbrata;
	this.codiceBadge = codiceBadge;
	this.ip = ip;
	this.fuoriOrarioTurno = fuoriOrarioTurno;
	this.modificataManualmente = modificataManualmente;
	this.dataOra = dataOra;
}

var logTimbrata = function(req, callBack) {
	console.log('req => codiceBadge: ' + req.codiceBadge + ", clientIp: " + req.clientIp);
	var t = new timbrata(req.codiceBadge, new Date(), ttRepo.tipiTimbtata.NonIdentificata,
			req.clientIp, false, false);
	var query = db.pool.query('INSERT INTO TIMBRATA SET ?', t, function(err,
			result) {
		if (err) {
			callBack(err);
		} else {
			dRepo.getDipendenteByCodiceBadge(req, function(err, d) {
				if (d) {
					gRepo.getByDateAndDipendente({
						dataRiferimento: moment(t.dataOra).format('YYYY-MM-DD'),
						dipendente_id: d.id
					}, function(err, gg){
						if (err) {
							callBack(err);
						} else {
							if (gg && gg.length === 1) {
								var ttimbr = getTipoTimbrataBasataSuOrari(turno, gg[0], t);
								if (ttimbr === ttRepo.tipiTimbtata.NonIdentificata) {
									ttimbr = getTipoTimbrataBasataSuCoppie(turno, gg[0], t);
								}
								tnRepo.getById({id: d.turno_id}, function(err, turno) {
									db.pool.query('UPDATE TIMBRATA SET tipoTimbrata = ? WHERE ID = ?', 
											[ttimbr, t.id], 
											function(err, result) {
										if (err) {
											console.error(err);
										} else {
											consoloe.log(result);
										}
									});
								});
							}
							callBack(null, null);
						}
					});
				} else {
					callBack(null, null);
				}
			});
		}
	});
};

var getTimbrata = function(req, callBack) {
	console.log('req => id: ' + req.id);
	var query = db.pool.query('SELECT * FROM TIMBRATA WHERE ID = ?', [req.id], function(err, rows, fields) {
		if (err) {
			if (callBack) { callBack(err); }
		} else {
			if (rows.length === 1) {
				if (callBack) { callBack(null, rows[0]); }
			} else {
				if (callBack) { callBack(null, 0); }
			}
		}
	});
};

module.exports = {
	timbrata: timbrata,
	logTimbrata : logTimbrata
};

/* privates */
var getTipoTimbrataBasataSuOrari = function(turno, gg, timbr) {
	var dataRif = moment(timbr.dataOra);
	var start = moment(timbr.dataOra);
	var end = moment(timbr.dataOra);

	// entrata
	var limInf = moment(turno.ingressoMin)
			.add(-turno.minutiArrotondamentoDifetto, 'm');
	var limSup = moment(turno.ingressoMax)
			.add(turno.minutiArrotondamentoEccesso, 'm');
	start.hours(limInf.hours()).minutes(limInf.minutes());
	end.hours(limSup.hours()).minutes(limSup.minutes());
	if (dataRif.isBetween(start, end)) {
		return ttRepo.tipiTimbrata.Entrata;
	}

	// inizio pausa pranzo
	limInf = moment(turno.inizioPausa)
		.add(-turno.minutiArrotondamentoDifetto, 'm');
	limSup = moment(turno.inizioPausa)
		.add(turno.minutiArrotondamentoEccesso, 'm');
	start.hours(limInf.hours()).minutes(limInf.minutes());
	end.hours(limSup.hours()).minutes(limSup.minutes());
	if (dataRif.isBetween(start, end)) {
		return ttRepo.tipiTimbrata.InizioPranzo;
	}

	// fine pausa pranzo
	limInf = moment(turno.finePausa)
		.add(-turno.minutiArrotondamentoDifetto, 'm');
	limSup = moment(turno.finePausa)
		.add(turno.minutiArrotondamentoEccesso, 'm');
	start.hours(limInf.hours()).minutes(limInf.minutes());
	end.hours(limSup.hours()).minutes(limSup.minutes());
	if (dataRif.isBetween(start, end)) {
		return ttRepo.tipiTimbrata.FinePranzo;
	}
	return ttRepo.tipiTimbrata.NonIdentificata;	
}

var getTipoTimbrataBasataSuCoppie = function(turno, gg, timbr) {
	/*
	var oreLavorate = g.oreLavorate;

	for (CoppiaTimbrate ct : g.getCoppieTimbrate()) {
		if (ct.isAperta()) {
			DateTime start = new DateTime(ct.getIngresso().getDataOra())
					.minusMinutes(truno.getMinutiArrotondamentoDifetto());
			DateTime end = new DateTime(t.getDataOra()).plusMinutes(truno.getMinutiArrotondamentoEccesso());
			if (oreLavorate + (float) new Duration(start, end).getStandardMinutes() / 60F >= d.getTurno()
					.getDurataOre()) {
				return ttRepo.tipiTimbrata.Uscita;
			}
		}
	}
	*/
	return ttRepo.tipiTimbrata.NonIdentificata;
}