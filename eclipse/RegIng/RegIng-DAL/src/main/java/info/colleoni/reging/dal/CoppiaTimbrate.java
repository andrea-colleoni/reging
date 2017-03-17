package info.colleoni.reging.dal;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "CoppiaTimbrate.getAll", query = "SELECT ct FROM CoppiaTimbrate ct ORDER BY ct.ingresso.dataOra DESC"),
	@NamedQuery(name = "CoppiaTimbrate.byId", query = "SELECT ct FROM CoppiaTimbrate ct WHERE ct.id=:id")
})
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CoppiaTimbrate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2758788454715295974L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private int sequenza;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ingresso_id")
	private Timbrata ingresso;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uscita_id")	
	private Timbrata uscita;

	public boolean isVuota() {
		return (ingresso == null && uscita == null);
	}

	public boolean isAperta() {
		return (ingresso != null && uscita == null);
	}

	public boolean isChiusa() {
		return (ingresso != null && uscita != null);
	}

	public boolean isAnomala() {
		boolean anomala = 
				ingresso == null && uscita != null || 
				ingresso != null && ingresso.getTipoTimbrata() == TipoTimbrata.Anomala ||
				ingresso != null && ingresso.getTipoTimbrata() == TipoTimbrata.Uscita ||
				ingresso != null && ingresso.getTipoTimbrata() == TipoTimbrata.InizioPranzo ||
				uscita != null && uscita.getTipoTimbrata() == TipoTimbrata.Anomala ||
				uscita != null && uscita.getTipoTimbrata() == TipoTimbrata.Entrata ||
				uscita != null && uscita.getTipoTimbrata() == TipoTimbrata.FinePranzo
				;
		if (!anomala && ingresso != null && uscita != null) {
			DateTime start = new DateTime(ingresso.getDataOra());
			DateTime end = new DateTime(uscita.getDataOra());
			anomala |= end.isBefore(start);
		}
		return anomala;
	}
	
	public boolean isContieneModificheManuali() {
		return (
				ingresso != null && ingresso.isModificataManualmente() ||
				uscita != null && uscita.isModificataManualmente()
				);
	}
	
	public boolean isContieneFuoriOrario() {
		return (
				ingresso != null && ingresso.isFuoriOrarioTurno() ||
				uscita != null && uscita.isFuoriOrarioTurno()
				);
	}
	
	public boolean isContieneInterruzione() {
		return (
				ingresso != null && ingresso.getTipoTimbrata() == TipoTimbrata.Ripresa ||
				uscita != null && uscita.getTipoTimbrata() == TipoTimbrata.Interruzione
				);
	}	

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Giornata giornata;
	
	public long getMinutiCoppia() {
		if (ingresso != null && uscita != null) {
			DateTime start = new DateTime(ingresso.getDataOra());
			DateTime end = new DateTime(uscita.getDataOra());
			return new Duration(start, end).getStandardMinutes();
		}
		return 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timbrata getIngresso() {
		return ingresso;
	}

	public void setIngresso(Timbrata ingresso) {
		this.ingresso = ingresso;
	}

	public Timbrata getUscita() {
		return uscita;
	}

	public void setUscita(Timbrata uscita) {
		this.uscita = uscita;
	}

	public Giornata getGiornata() {
		return giornata;
	}

	public void setGiornata(Giornata giornata) {
		this.giornata = giornata;
	}

	public int getSequenza() {
		return sequenza;
	}

	public void setSequenza(int sequenza) {
		this.sequenza = sequenza;
	}
}
