package info.colleoni.reging.dal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "BancaOre.getAll", query = "SELECT bo FROM BancaOre bo ORDER BY bo.dataAttribuzione DESC"),
	@NamedQuery(name = "BancaOre.byId", query = "SELECT bo FROM BancaOre bo WHERE bo.id=:id"),
	@NamedQuery(name = "BancaOre.byDipendente", query = "SELECT bo FROM BancaOre bo WHERE bo.dipendente=:dipendente ORDER BY bo.dataAttribuzione DESC"),
	@NamedQuery(name = "BancaOre.byDate", query = "SELECT bo FROM BancaOre bo WHERE bo.dataAttribuzione=:dataAttribuzione"),
	@NamedQuery(name = "BancaOre.byDateAndDipendente", query = "SELECT bo FROM BancaOre bo WHERE bo.dataAttribuzione=:dataAttribuzione AND bo.dipendente=:dipendente"),
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BancaOre implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 354954397254539102L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonBackReference
	private Dipendente dipendente;	
	
	private float quantitaMinuti;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)	
	private Date dataAttribuzione;
	
	@Enumerated(EnumType.STRING)
	private TipoOra tipoOra;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getQuantitaMinuti() {
		return quantitaMinuti;
	}

	public void setQuantitaMinuti(float quantita) {
		this.quantitaMinuti = quantita;
	}

	public Date getDataAttribuzione() {
		return dataAttribuzione;
	}

	public void setDataAttribuzione(Date dataAttribuzione) {
		this.dataAttribuzione = dataAttribuzione;
	}

	public TipoOra getTipoOra() {
		return tipoOra;
	}

	public void setTipoOra(TipoOra tipoOra) {
		this.tipoOra = tipoOra;
	}

	public Dipendente getDipendente() {
		return dipendente;
	}

	public void setDipendente(Dipendente dipendente) {
		this.dipendente = dipendente;
	}
}
