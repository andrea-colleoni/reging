package info.colleoni.reging.dal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Turno.getAll", query = "SELECT t FROM Turno t"),
	@NamedQuery(name = "Turno.byKey", query = "SELECT t FROM Turno t WHERE t.id=:id"),
	@NamedQuery(name = "Turno.byNome", query = "SELECT t FROM Turno t WHERE t.nome=:nome") 
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Turno implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6290305197869583095L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String nome;
	
	private String descrizione;	

	private boolean feriale;
	private boolean preFestivo;
	private boolean festivo;
	
	private int discretizzazioneMinuti;
	
	@Temporal(TemporalType.TIME)
	@Column(nullable=false)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")	
	private Date ingressoMin;
	@Temporal(TemporalType.TIME)
	@Column(nullable=false)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")		
	private Date ingressoMax;
	
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")		
	private Date inizioPausa;
	@Temporal(TemporalType.TIME)
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")		
	private Date finePausa;
	
	private float durataOre;
	
	@OneToMany(mappedBy="turno", fetch=FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval=false)
	@OrderBy("cognome, nome")
	@JsonBackReference
	private List<Dipendente> dipendenti;
	
	private int minutiArrotondamentoEccesso;
	
	private int minutiArrotondamentoDifetto;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isFeriale() {
		return feriale;
	}

	public void setFeriale(boolean feriale) {
		this.feriale = feriale;
	}

	public boolean isPreFestivo() {
		return preFestivo;
	}

	public void setPreFestivo(boolean preFestivo) {
		this.preFestivo = preFestivo;
	}

	public boolean isFestivo() {
		return festivo;
	}

	public void setFestivo(boolean festivo) {
		this.festivo = festivo;
	}

	public Date getIngressoMin() {
		return ingressoMin;
	}

	public void setIngressoMin(Date ingressoMin) {
		this.ingressoMin = ingressoMin;
	}

	public Date getIngressoMax() {
		return ingressoMax;
	}

	public void setIngressoMax(Date ingressoMax) {
		this.ingressoMax = ingressoMax;
	}

	public Date getInizioPausa() {
		return inizioPausa;
	}

	public void setInizioPausa(Date inizioPaus) {
		this.inizioPausa = inizioPaus;
	}

	public Date getFinePausa() {
		return finePausa;
	}

	public void setFinePausa(Date finePausa) {
		this.finePausa = finePausa;
	}

	public float getDurataOre() {
		return durataOre;
	}

	public void setDurataOre(float durataOre) {
		this.durataOre = durataOre;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Dipendente> getDipendenti() {
		return dipendenti;
	}

	public void setDipendenti(List<Dipendente> dipendenti) {
		this.dipendenti = dipendenti;
	}

	public int getMinutiArrotondamentoEccesso() {
		return minutiArrotondamentoEccesso;
	}

	public void setMinutiArrotondamentoEccesso(int minutiArrotondamentoEccesso) {
		this.minutiArrotondamentoEccesso = minutiArrotondamentoEccesso;
	}

	public int getMinutiArrotondamentoDifetto() {
		return minutiArrotondamentoDifetto;
	}

	public void setMinutiArrotondamentoDifetto(int minutiArrotondamentoDifetto) {
		this.minutiArrotondamentoDifetto = minutiArrotondamentoDifetto;
	}

	public int getDiscretizzazioneMinuti() {
		return discretizzazioneMinuti;
	}

	public void setDiscretizzazioneMinuti(int discretizzazioneMinuti) {
		this.discretizzazioneMinuti = discretizzazioneMinuti;
	}
}
