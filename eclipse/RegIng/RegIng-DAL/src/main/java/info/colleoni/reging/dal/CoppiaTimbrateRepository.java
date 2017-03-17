package info.colleoni.reging.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoppiaTimbrateRepository {

	final static Logger logger = LoggerFactory.getLogger(CoppiaTimbrateRepository.class);

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager();

	public CoppiaTimbrate save(CoppiaTimbrate coppiaTimbrate) {
		entityManager.getTransaction().begin();
		entityManager.persist(coppiaTimbrate);
		entityManager.getTransaction().commit();
		return coppiaTimbrate;
	}

	public List<CoppiaTimbrate> elenco() {
		return entityManager.createNamedQuery("CoppiaTimbrate.getAll", CoppiaTimbrate.class)
				.getResultList();
	}

	public CoppiaTimbrate byId(long id) {
		try {
			return entityManager.createNamedQuery("CoppiaTimbrate.byId", CoppiaTimbrate.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (PersistenceException ex) {
			logger.error("exception", ex);
			return null;
		}
	}
}
