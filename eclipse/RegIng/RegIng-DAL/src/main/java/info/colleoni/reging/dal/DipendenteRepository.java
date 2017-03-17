package info.colleoni.reging.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DipendenteRepository {
	
	final static Logger logger = LoggerFactory.getLogger(DipendenteRepository.class);

    EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager( );

	public Dipendente save(Dipendente registrazione) {
		entityManager.getTransaction().begin();
		entityManager.persist(registrazione);
		entityManager.getTransaction().commit();
		return registrazione;
	}	
	
	public List<Dipendente> elenco() {
		logger.debug(String.format("elenco"));
		try {		
			return entityManager.createNamedQuery("Dipendente.getAll", Dipendente.class)
					.getResultList();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}		
	}	
	
	public Dipendente byCodiceBadge(String codiceBadge) {
		logger.debug(String.format("byCodiceBadge => %1$s", codiceBadge));
		try {
			return entityManager.createNamedQuery("Dipendente.byCodiceBadge", Dipendente.class)
					.setParameter("codiceBadge", codiceBadge)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}
	}

	public Dipendente byId(int id) {
		logger.debug(String.format("byId => %1$d", id));
		try {
			return entityManager.createNamedQuery("Dipendente.byId", Dipendente.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}
	}
	
	public Dipendente byUsername(String username) {
		logger.debug(String.format("byUsername => %1$s", username));
		try {
			return entityManager.createNamedQuery("Dipendente.byUsername", Dipendente.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}
	}	
	
	public boolean login(String username, String password) {
		logger.debug(String.format("login => %1$s", username));
		try {
			entityManager.createNamedQuery("Dipendente.login", Dipendente.class)
					.setParameter("username", username)
					.setParameter("password", password)
					.getSingleResult();
			return true;
		} catch (NoResultException e) {
			logger.info("login failed");
			return false;
		}
	}	
}
