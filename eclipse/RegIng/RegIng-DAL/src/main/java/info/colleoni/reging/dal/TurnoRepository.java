package info.colleoni.reging.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurnoRepository {
	
	final static Logger logger = LoggerFactory.getLogger(TurnoRepository.class);	

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager();

	public Turno save(Turno turno) {
		entityManager.getTransaction().begin();
		entityManager.persist(turno);
		entityManager.getTransaction().commit();
		return turno;
	}

	public List<Turno> elenco() {
		return entityManager.createNamedQuery("Turno.getAll", Turno.class)
				.getResultList();
	}

	public Turno byKey(int id) {
		try {
			return entityManager.createNamedQuery("Turno.byKey", Turno.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}
	}

	public Turno byNome(String nome) {
		try {
			return entityManager.createNamedQuery("Turno.byNome", Turno.class)
					.setParameter("nome", nome)
					.getSingleResult();
		} catch (NoResultException e) {
			logger.error("exception", e);
			return null;
		}		
	}
}
