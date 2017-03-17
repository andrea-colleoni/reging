/**
 * 
 */
package info.colleoni.reging.dal;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Andrea
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "Ruolo.getAll", query = "SELECT r FROM Ruolo r"),
	@NamedQuery(name = "Ruolo.byKey", query = "SELECT r FROM Ruolo r WHERE r.id=:id"),
	@NamedQuery(name = "Ruolo.byNome", query = "SELECT r FROM Ruolo r WHERE r.nome=:nome") 
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ruolo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -927044913711955868L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;	
	
	private String nome;
	
	@OneToMany(mappedBy="ruolo", fetch=FetchType.LAZY, cascade={CascadeType.ALL}, orphanRemoval=false)
	@OrderBy("cognome, nome")
	@JsonManagedReference
	private List<Dipendente> dipendenti;	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setDipendenti(List<Dipendente> badges) {
		this.dipendenti = badges;
	}
	

}
