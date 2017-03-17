package info.colleoni.reging.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimbrataRepository {
	
	final static Logger logger = LoggerFactory.getLogger(TimbrataRepository.class);

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager( );

	public Timbrata save(Timbrata timbrata) {
		entityManager.getTransaction().begin();
		entityManager.persist(timbrata);
		entityManager.getTransaction().commit();
		return timbrata;
	}	
	
	public List<Timbrata> elenco() {
		return entityManager.createNamedQuery("Timbrata.getAll", Timbrata.class)
				.getResultList();
	}

	public Timbrata byId(int idTimbrata) {
		try {
			return entityManager.createNamedQuery("Timbrata.byKey", Timbrata.class)
					.setParameter("id", idTimbrata)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("Exception", e);
			return null;
		}
	}
}
