package info.colleoni.reging.bl;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.colleoni.reging.dal.BancaOre;
import info.colleoni.reging.dal.BancaOreRepository;
import info.colleoni.reging.dal.CoppiaTimbrate;
import info.colleoni.reging.dal.CoppiaTimbrateRepository;
import info.colleoni.reging.dal.Dipendente;
import info.colleoni.reging.dal.DipendenteRepository;
import info.colleoni.reging.dal.Giornata;
import info.colleoni.reging.dal.GiornataRepository;
import info.colleoni.reging.dal.Timbrata;
import info.colleoni.reging.dal.TipoOra;
import info.colleoni.reging.dal.TipoTimbrata;

public class RulesTimbrature {

	final static Logger logger = LoggerFactory.getLogger(RulesTimbrature.class);

	GiornataRepository gr = new GiornataRepository();
	DipendenteRepository dr = new DipendenteRepository();
	CoppiaTimbrateRepository ctr = new CoppiaTimbrateRepository();
	BancaOreRepository bor = new BancaOreRepository();

	public TipoTimbrata getTipoTimbrataBasataSuOrari(Dipendente d, Timbrata t) {
		DateTime dataRif = new DateTime(t.getDataOra());
		DateTime start = new DateTime(t.getDataOra());
		DateTime end = new DateTime(t.getDataOra());

		// entrata
		DateTime limInf = new DateTime(d.getTurno().getIngressoMin())
				.minusMinutes(d.getTurno().getMinutiArrotondamentoDifetto());
		DateTime limSup = new DateTime(d.getTurno().getIngressoMax())
				.plusMinutes(d.getTurno().getMinutiArrotondamentoEccesso());
		start = start.withHourOfDay(limInf.getHourOfDay()).withMinuteOfHour(limInf.getMinuteOfHour())
				.withSecondOfMinute(0);
		end = end.withHourOfDay(limSup.getHourOfDay()).withMinuteOfHour(limSup.getMinuteOfHour()).withSecondOfMinute(0);
		Interval interval;
		if (start.isBefore(end)) {
			interval = new Interval(start, end);
			if (interval.contains(dataRif)) {
				return TipoTimbrata.Entrata;
			}
		}
		// inizio pausa pranzo
		limInf = new DateTime(d.getTurno().getInizioPausa())
				.minusMinutes(d.getTurno().getMinutiArrotondamentoDifetto());
		limSup = new DateTime(d.getTurno().getInizioPausa()).plusMinutes(d.getTurno().getMinutiArrotondamentoEccesso());
		start = start.withHourOfDay(limInf.getHourOfDay()).withMinuteOfHour(limInf.getMinuteOfHour())
				.withSecondOfMinute(0);
		end = end.withHourOfDay(limSup.getHourOfDay()).withMinuteOfHour(limSup.getMinuteOfHour()).withSecondOfMinute(0);
		if (start.isBefore(end)) {
			interval = new Interval(start, end);
			if (interval.contains(dataRif)) {
				return TipoTimbrata.InizioPranzo;
			}
		}
		
		// fine pausa pranzo
		limInf = new DateTime(d.getTurno().getFinePausa()).minusMinutes(d.getTurno().getMinutiArrotondamentoDifetto());
		limSup = new DateTime(d.getTurno().getFinePausa()).plusMinutes(d.getTurno().getMinutiArrotondamentoEccesso());
		start = start.withHourOfDay(limInf.getHourOfDay()).withMinuteOfHour(limInf.getMinuteOfHour())
				.withSecondOfMinute(0);
		end = end.withHourOfDay(limSup.getHourOfDay()).withMinuteOfHour(limSup.getMinuteOfHour()).withSecondOfMinute(0);
		if (start.isBefore(end)) {
			interval = new Interval(start, end);
			if (interval.contains(dataRif)) {
				return TipoTimbrata.FinePranzo;
			}
		}
		return TipoTimbrata.NonIdentificata;
	}

	public TipoTimbrata getTipoTimbrataDaCoppia(CoppiaTimbrate ct, Timbrata t) {
		float oreLavorate = ct.getGiornata().getOreLavorate();

		boolean turnoPrevedePranzo = ct.getGiornata().getDipendente().getTurno().getInizioPausa() != null;
		TipoTimbrata ttOrario = getTipoTimbrataBasataSuOrari(ct.getGiornata().getDipendente(), t);
		
		if (ct.isAperta()) {
			if (ct.getIngresso().getTipoTimbrata() == TipoTimbrata.Entrata) {
				if (turnoPrevedePranzo && !ct.getGiornata().isPresentePranzo()) {
					if (ttOrario == TipoTimbrata.InizioPranzo) {
						return TipoTimbrata.InizioPranzo;
					} else {
						return TipoTimbrata.Interruzione;
					}
				} else {
					if (oreLavorate >= ct.getGiornata().getDipendente().getTurno().getDurataOre()) {
						return TipoTimbrata.Uscita;
					} else {
						return TipoTimbrata.Interruzione;
					}
				}
			} else if (ct.getIngresso().getTipoTimbrata() == TipoTimbrata.Ripresa ||
					ct.getIngresso().getTipoTimbrata() == TipoTimbrata.FinePranzo) {
				if (oreLavorate >= ct.getGiornata().getDipendente().getTurno().getDurataOre()) {
					return TipoTimbrata.Uscita;
				} else {
					return TipoTimbrata.Interruzione;
				}
			}
		}
		if (ct.isVuota()) {
			if (oreLavorate <= 0 || !turnoPrevedePranzo) {
				return TipoTimbrata.Entrata;
			} else {
				if (ct.getGiornata().isFuoriAPranzo()) {
					return TipoTimbrata.FinePranzo;
				} else {
					return TipoTimbrata.Ripresa;
				}
			}
		}

		return TipoTimbrata.NonIdentificata;
	}

	/**
	 * Ottiene una coppia di timbrate.
	 * 
	 * Se nella data di riferimento non esiste la giornata, la crea e la salva a
	 * DB.
	 * 
	 * Se nella giornata non esiste una coppia adatta, la crea e loa slava a DB.
	 * 
	 * @param d
	 *            Il dipendente da cuidevo estrarre la giornata.
	 * @param dataRif
	 *            la data di riferimento della giornata
	 * @param tt
	 *            il TipoTimbrata che determina la logica di attribuzione
	 *            pausa/no pausa
	 * @return
	 */
	public CoppiaTimbrate getCoppiaDisponibile(Dipendente d, Date dataRif) {
		CoppiaTimbrate ct = null;
		List<Giornata> gg = gr.byDateAndDipendente(dataRif, d);
		Giornata g;
		// questo è il punto dove si genera una nuova giornata
		if (gg.size() <= 0) {
			g = new Giornata();
			g.setDipendente(d);
			g.setDataRiferimento(dataRif);
		} else if (gg.size() == 1) {
			g = gg.get(0);
		} else {
			logger.warn(String.format("Trovate troppe giornate => dip: %1$s, dataRif: %2$tD", d, dataRif));
			g = gg.get(0);
		}

		boolean trovateAperte = false;
		boolean trovateVuote = false;

		for (CoppiaTimbrate cts : g.getCoppieTimbrate()) {
			// se trovo 1 coppia aperta => restituisco quella (caso 1, greedy)
			// se trovo 1 coppia vuota e non trovo coppie aperte => restituisco quella
			// se trovo + coppie aperte => warn e restituisco la prima (caso 1, greedy, no warn)
			// se trovo + coppie vuote e non trovo coppie aperte => warn e restituisco la prima
			// se non trovo coppie aperte o vuote => creo, salvo e restituisco
			
			// caso 1
			if (cts.isAperta()) {
				trovateAperte = true;
				ct = cts;
				break;
			}
			if (cts.isVuota()) {
				if (trovateVuote) {
					logger.warn("Trovate più coppie vuote");
				}
				trovateVuote = true;
				if (ct == null) {
					ct = cts;
				}
			}
		}

		// questo è il punto dove si genera una nuova coppia di timbrate
		if (!trovateAperte && !trovateVuote) {
			ct = new CoppiaTimbrate();
			ct.setSequenza(g.getCoppieTimbrate().size() + 1);
			ct.setGiornata(g);
			g.addCoppiaTimbrate(ct);
			gr.save(g);
		}
		return ct;
	}

	public boolean getFuoriOrario(Timbrata t, Giornata g) {
		switch(t.getTipoTimbrata()) {
		case Entrata:
		case InizioPranzo:
		case FinePranzo:
			return t.getTipoTimbrata() != getTipoTimbrataBasataSuOrari(g.getDipendente(), t);
		case Uscita:
			return g.getOreLavorate() < g.getDipendente().getTurno().getDurataOre();
		default:
			return false;
		}
	}

	public boolean getNecessitaCausale(Giornata g, Timbrata t) {
		switch (t.getTipoTimbrata()) {
		case Interruzione:
			return true;
		default:
			break;
		}
		if (getFuoriOrario(t, g)) {
			return true;
		}
		return false;
	}
	
	public void calcolaEAggiornaRecuperoOreGiornata(Dipendente d, Date dataRecupero) {
		float durataGiornaliera = d.getTurno().getDurataOre();

		List<Giornata> ggs = gr.byDateAndDipendente(dataRecupero, d);
		if (ggs.size() == 1) {
			float recuperoOre = ggs.get(0).getOreLavorate() - durataGiornaliera;
			float recuperoMinuti = Math.round(recuperoOre * 60F / (float)d.getTurno().getMinutiArrotondamentoEccesso()) * (float)d.getTurno().getMinutiArrotondamentoEccesso(); 
			List<BancaOre> bors = bor.byDateAndDipendente(dataRecupero, d);
			boolean trovatoRecupero = false;
			for (BancaOre bo : bors) {
				if (bo.getTipoOra() == TipoOra.RecuperoFlessibilita) {
					trovatoRecupero = true;
					bo.setQuantitaMinuti(recuperoMinuti);
					bor.save(bo);
					break;
				}
			}
			if (!trovatoRecupero) {
				BancaOre bo = new BancaOre();
				bo.setDipendente(d);
				bo.setDataAttribuzione(dataRecupero);
				bo.setTipoOra(TipoOra.RecuperoFlessibilita);
				bo.setQuantitaMinuti(recuperoMinuti);
				bor.save(bo);
			}
		} else {
			logger.warn("trovato numero errato di giornate");
		}
	}	
}
