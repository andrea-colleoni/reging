package info.colleoni.reging.dal;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Timbrata.byKey", query = "SELECT t FROM Timbrata t WHERE t.id=:id"),
	@NamedQuery(name = "Timbrata.getAll", query = "SELECT t FROM Timbrata t ORDER BY t.dataOra DESC, t.codiceBadge")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Timbrata implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8519780744410954573L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private TipoTimbrata tipoTimbrata;
	
	private String codiceBadge;

	private String ip;
	
	private boolean fuoriOrarioTurno;
	
	private boolean modificataManualmente;
	
	private String causale;
	
	@Transient
	private boolean necessitaCausale;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)	
	private Date dataOra;
	
	public String getDataConOrario() {
		SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdt.format(dataOra);
	}
	
	
	public String getData() {
		SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
		return sdt.format(dataOra);
	}
	
	public String getOrario() {
		SimpleDateFormat sdt = new SimpleDateFormat("HH:mm:ss");
		return sdt.format(dataOra);
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceBadge() {
		return codiceBadge;
	}

	public void setCodiceBadge(String codiceBadge) {
		this.codiceBadge = codiceBadge;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getDataOra() {
		return dataOra;
	}

	public void setDataOra(Date dataOra) {
		this.dataOra = dataOra;
	}

	public TipoTimbrata getTipoTimbrata() {
		return tipoTimbrata;
	}

	public void setTipoTimbrata(TipoTimbrata tipoTimbrata) {
		this.tipoTimbrata = tipoTimbrata;
	}

	public boolean isFuoriOrarioTurno() {
		return fuoriOrarioTurno;
	}

	public void setFuoriOrarioTurno(boolean fuoriOrarioTurno) {
		this.fuoriOrarioTurno = fuoriOrarioTurno;
	}

	public boolean isModificataManualmente() {
		return modificataManualmente;
	}

	public void setModificataManualmente(boolean modificataManualmente) {
		this.modificataManualmente = modificataManualmente;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public boolean isNecessitaCausale() {
		return necessitaCausale;
	}

	public void setNecessitaCausale(boolean necessitaCausale) {
		this.necessitaCausale = necessitaCausale;
	}
}
