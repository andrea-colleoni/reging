/**
 * 
 */
package info.colleoni.reging.bl;

import java.text.SimpleDateFormat;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import info.colleoni.reging.dal.CoppiaTimbrate;
import info.colleoni.reging.dal.Giornata;

/**
 * @author Andrea
 *
 */
public class GiornataDenormalizzata {

	private Giornata giornata;
	private Duration qtaLavorata;
	private Duration qtaTurno;
	
	SimpleDateFormat dates = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat times = new SimpleDateFormat("HH:mm");
	
	public GiornataDenormalizzata(Giornata giornata) {
		this.giornata = giornata;
		this.qtaLavorata = new Duration((long)(giornata.getOreLavorate() * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE *
				DateTimeConstants.MILLIS_PER_SECOND));
		this.qtaTurno = new Duration((long)(giornata.getDipendente().getTurno().getDurataOre() * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE *
				DateTimeConstants.MILLIS_PER_SECOND));
	}
	
	public String getNominativo() {
		return giornata.getDipendente().getCognome() + " " +
					giornata.getDipendente().getNome();
	}
	
	public String getData() {
		return dates.format(giornata.getDataRiferimento());
	}
	
	public String getE1() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 1 && ct.getIngresso() != null) {
				return times.format(ct.getIngresso().getDataOra());
			}
		}
		return "";
	}
	
	public String getU1() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 1 && ct.getUscita() != null) {
				return times.format(ct.getUscita().getDataOra());
			}
		}
		return "";
	}	
	
	public String getE2() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 2 && ct.getIngresso() != null) {
				return times.format(ct.getIngresso().getDataOra());
			}
		}
		return "";
	}	
	
	public String getU2() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 2 && ct.getUscita() != null) {
				return times.format(ct.getUscita().getDataOra());
			}
		}
		return "";
	}	
	
	public String getE3() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 2 && ct.getIngresso() != null) {
				return times.format(ct.getIngresso().getDataOra());
			}
		}
		return "";
	}	
	
	public String getU3() {
		for (CoppiaTimbrate ct : giornata.getCoppieTimbrate()) {
			if (ct.getSequenza() == 2 && ct.getUscita() != null) {
				return times.format(ct.getUscita().getDataOra());
			}
		}
		return "";
	}
	
	public String getOreLavorate() {
		return String.valueOf(qtaLavorata.getStandardHours()) + ":" + String.format("%2d", (qtaLavorata.getStandardMinutes() - qtaLavorata.getStandardHours() * DateTimeConstants.MINUTES_PER_HOUR)).replace(' ', '0');
	}
	
	public String getOreTurno() {
		return String.valueOf(qtaTurno.getStandardHours()) + ":" + String.format("%2d", (qtaTurno.getStandardMinutes() - qtaTurno.getStandardHours() * DateTimeConstants.MINUTES_PER_HOUR)).replace(' ', '0');
	}
	
	public String getAssenza() {
		if (qtaLavorata.isShorterThan(qtaTurno)) {
			Duration assenza = qtaTurno.minus(qtaLavorata);
			return String.valueOf(assenza.getStandardHours()) + ":" + String.format("%2d", (assenza.getStandardMinutes() - assenza.getStandardHours() * DateTimeConstants.MINUTES_PER_HOUR)).replace(' ', '0');
		} else {
			return "";
		}
	}	
	
	public String getStraordinario() {
		if (qtaLavorata.isLongerThan(qtaTurno)) {
			Duration straordinario = qtaLavorata.minus(qtaTurno);
			return String.valueOf(straordinario.getStandardHours()) + ":" + String.format("%2d", (straordinario.getStandardMinutes() - straordinario.getStandardHours() * DateTimeConstants.MINUTES_PER_HOUR)).replace(' ', '0');
		} else {
			return "";
		}
	}		

	public Giornata getGiornata() {
		return giornata;
	}

	public void setGiornata(Giornata giornata) {
		this.giornata = giornata;
	}
	
}
