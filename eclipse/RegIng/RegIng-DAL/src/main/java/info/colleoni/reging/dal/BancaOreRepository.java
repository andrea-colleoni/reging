package info.colleoni.reging.dal;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class BancaOreRepository {

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager( );

	public BancaOre save(BancaOre bancaOre) {
		entityManager.getTransaction().begin();
		entityManager.persist(bancaOre);
		entityManager.getTransaction().commit();
		return bancaOre;
	}	
	
	public List<BancaOre> elenco() {
		return entityManager.createNamedQuery("BancaOre.getAll", BancaOre.class)
				.getResultList();
	}
	
	public BancaOre byId(int id) {
		try {
			return entityManager.createNamedQuery("BancaOre.byId", BancaOre.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}	
	}	
	
	public List<BancaOre> byDipendente(Dipendente dipendente) {
		return entityManager.createNamedQuery("BancaOre.byDipendente", BancaOre.class)
				.setParameter("dipendente", dipendente)
				.getResultList();
	}	
	
	public List<BancaOre> byDate(Date dataAttribuzione) {
		return entityManager.createNamedQuery("BancaOre.byDate", BancaOre.class)
				.setParameter("dataAttribuzione", dataAttribuzione)
				.getResultList();
	}	

	public List<BancaOre> byDateAndDipendente(Date dataAttribuzione, Dipendente dipendente) {
		return entityManager.createNamedQuery("BancaOre.byDateAndDipendente", BancaOre.class)
				.setParameter("dataAttribuzione", dataAttribuzione)
				.setParameter("dipendente", dipendente)
				.getResultList();
	}		
}
