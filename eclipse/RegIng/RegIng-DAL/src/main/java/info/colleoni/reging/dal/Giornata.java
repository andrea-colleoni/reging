/**
 * 
 */
package info.colleoni.reging.dal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Andrea
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "Giornata.getAll", query = "SELECT r FROM Giornata r ORDER BY r.dataRiferimento DESC"),
	@NamedQuery(name = "Giornata.byKey", query = "SELECT r FROM Giornata r WHERE r.id=:id"),
	@NamedQuery(name = "Giornata.byDipendente", query = "SELECT r FROM Giornata r WHERE r.dipendente=:dipendente"),
	@NamedQuery(name = "Giornata.byDate", query = "SELECT r FROM Giornata r WHERE r.dataRiferimento=:dateRef"),
	@NamedQuery(name = "Giornata.byDateAndDipendente", query = "SELECT r FROM Giornata r WHERE r.dataRiferimento=:dateRef AND r.dipendente=:dipendente"),
	@NamedQuery(name = "Giornata.today", query = "SELECT r FROM Giornata r WHERE r.dataRiferimento=CURRENT_DATE"),	
	@NamedQuery(name = "Giornata.byBarcodeAndDateRange", query = "SELECT r FROM Giornata r WHERE r.dipendente.codiceBadge=:badge AND r.dataRiferimento BETWEEN :dataInizio AND :dataFine")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Giornata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4289607853807177856L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	private Dipendente dipendente;
	
	private String note;
	
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm")	
	@Column(nullable=false)	
	private Date dataRiferimento;	
	
	@OneToMany(mappedBy="giornata", fetch=FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval=false)
	@JsonManagedReference	
	private List<CoppiaTimbrate> coppieTimbrate;
	
	public Giornata() {
		coppieTimbrate = new ArrayList<CoppiaTimbrate>();
	}
	
	public float getOreLavorate() {
		float _return = 0;
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.getIngresso() != null && ct.getUscita() != null) {
				_return += (float)ct.getMinutiCoppia()/ 60F;
			}
		}
		return _return;
	}
	
	public StatusGiornata getStatusGiornata() {
		if (coppieTimbrate.size() == 0) {
			return StatusGiornata.Nuova;
		}
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.isAnomala() || ct.isContieneFuoriOrario() || ct.isContieneModificheManuali()) {
				return StatusGiornata.Irregolare;
			}
		}
		return StatusGiornata.Regolare;
	}
	
	public boolean isPresenteInterruzione() {
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.isContieneInterruzione()) {
				return true;
			}
		}
		return false;
	}	
	
	public String getClassPresenteInterruzione() {
		return (isPresenteInterruzione() ? "presenteInterruzione" : "");
	}	
	
	public boolean isPresenteFuoriOrario() {
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.isContieneFuoriOrario()) {
				return true;
			}
		}
		return false;
	}	
	
	public String getClassPresenteFuoriOrario() {
		return (isPresenteFuoriOrario() ? "presenteFuoriOrario" : "");
	}
	
	public boolean isModificataManualmente() {
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.isContieneModificheManuali()) {
				return true;
			}
		}
		return false;
	}	
	
	public String getClassModificataManualmente() {
		return (isModificataManualmente() ? "modificataManualmente" : "");
	}	
	
	public boolean isPresentePranzo() {
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.getIngresso() != null && ct.getIngresso().getTipoTimbrata() == TipoTimbrata.FinePranzo) {
				return true;
			}
			if (ct.getUscita() != null && ct.getUscita().getTipoTimbrata() == TipoTimbrata.InizioPranzo) {
				return true;
			}
		}
		return false;
	}
	
	public String getClassPresentePranzo() {
		return (isPresentePranzo() ? "presentePranzo" : "");
	}		
	
	public boolean isFuoriAPranzo() {
		boolean trovatoInizioPranzo = false;
		boolean trovataFinePranzo = false;
		for(CoppiaTimbrate ct : coppieTimbrate) {
			if (ct.getIngresso() != null && ct.getIngresso().getTipoTimbrata() == TipoTimbrata.InizioPranzo) {
				trovatoInizioPranzo = true;
			}
			if (ct.getUscita() != null && ct.getUscita().getTipoTimbrata() == TipoTimbrata.FinePranzo) {
				trovataFinePranzo = true;
			}
		}
		return (trovatoInizioPranzo && ! trovataFinePranzo);
	}
	
	public void addCoppiaTimbrate(CoppiaTimbrate ct) {
		if (coppieTimbrate == null) {
			coppieTimbrate = new ArrayList<CoppiaTimbrate>();
		}
		coppieTimbrate.add(ct);
		ct.setGiornata(this);
	}	
	
	public String getBadgeCodice() {
		return (dipendente == null ? "" : dipendente.getCodiceBadge());
	}
	
	public String getBadgeNome() {
		return (dipendente == null ? "" : dipendente.getNome());
	}	
	
	public String getBadgeCognome() {
		return (dipendente == null ? "" : dipendente.getCognome());
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Dipendente getDipendente() {
		return dipendente;
	}

	public void setDipendente(Dipendente dipendente) {
		this.dipendente = dipendente;
		if (!dipendente.getGiornate().contains(this)) {
			dipendente.getGiornate().add(this);
        }
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataRiferimento() {
		return dataRiferimento;
	}

	public void setDataRiferimento(Date dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}

	public List<CoppiaTimbrate> getCoppieTimbrate() {
		return coppieTimbrate;
	}

	public void setCoppieTimbrate(List<CoppiaTimbrate> coppieTimbrate) {
		this.coppieTimbrate = coppieTimbrate;
	}
}
