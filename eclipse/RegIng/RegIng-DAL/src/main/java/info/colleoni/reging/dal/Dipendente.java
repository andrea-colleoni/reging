/**
 * 
 */
package info.colleoni.reging.dal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Andrea
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "Dipendente.getAll", query = "SELECT d FROM Dipendente d"),
	@NamedQuery(name = "Dipendente.byId", query = "SELECT d FROM Dipendente d WHERE d.id=:id"),
	@NamedQuery(name = "Dipendente.byUsername", query = "SELECT d FROM Dipendente d WHERE d.username=:username"),
	@NamedQuery(name = "Dipendente.login", query = "SELECT d FROM Dipendente d WHERE d.username=:username AND d.password=:password"),
	@NamedQuery(name = "Dipendente.byCodiceBadge", query = "SELECT d FROM Dipendente d WHERE d.codiceBadge=:codiceBadge") 
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dipendente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4245836276481736604L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;	
	
	@Column(unique=true)
	private String codiceBadge;
	
	private String nome;
	
	private String cognome;
	
	@Column(unique=true)
	private String username;
	
	@JsonIgnore
	private String password;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RUOLO_ID")	
	@JsonBackReference
	private Ruolo ruolo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TURNO_ID")	
	@JsonManagedReference
	private Turno turno;	
	
	@OneToMany(mappedBy="dipendente", fetch=FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval=false)
	@OrderBy("dataRiferimento")
	@JsonManagedReference
	private List<Giornata> giornate;
	
	@OneToMany(mappedBy="dipendente", fetch=FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval=false)
	@OrderBy("dataAttribuzione")
	@JsonManagedReference
	private List<BancaOre> bancaOre;
	
	public Map<TipoOra, Float> getSaldiBancaOre() {
		Map<TipoOra, Float> _return = new HashMap<TipoOra, Float>();
		for (TipoOra to : TipoOra.values()) {
			_return.put(to, 0.0F);
		}		
		for(BancaOre bo : bancaOre) {
			_return.put(bo.getTipoOra(), _return.get(bo.getTipoOra()) + bo.getQuantitaMinuti());
		}
		return _return;
	}
	
	public void addRegistrazione(Giornata r) {
		this.giornate.add(r);
		if (r.getDipendente() != this) {
			r.setDipendente(this);
		}
	}
	
	public String getNomeRuolo() {
		return (ruolo == null ? "" : ruolo.getNome());
	}

	public String getCodiceBadge() {
		return codiceBadge;
	}

	public void setCodiceBadge(String badgeCode) {
		this.codiceBadge = badgeCode;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public List<Giornata> getGiornate() {
		return giornate;
	}

	public void setGiornate(List<Giornata> registrazioni) {
		this.giornate = registrazioni;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public List<BancaOre> getBancaOre() {
		return bancaOre;
	}

	public void setBancaOre(List<BancaOre> bancaOre) {
		this.bancaOre = bancaOre;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
