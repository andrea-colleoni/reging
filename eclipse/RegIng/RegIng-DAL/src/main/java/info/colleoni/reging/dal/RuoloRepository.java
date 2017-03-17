package info.colleoni.reging.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class RuoloRepository {

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager();

	public Ruolo save(Ruolo ruolo) {
		entityManager.getTransaction().begin();
		entityManager.persist(ruolo);
		entityManager.getTransaction().commit();
		return ruolo;
	}

	public List<Ruolo> elenco() {
		return entityManager.createNamedQuery("Ruolo.getAll", Ruolo.class)
				.getResultList();
	}

	public Ruolo byKey(int id) {
		try {
			return entityManager.createNamedQuery("Ruolo.byKey", Ruolo.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Ruolo> byNome(String nome) {
		return entityManager.createNamedQuery("Ruolo.byNome", Ruolo.class)
				.setParameter("nome", nome)
				.getResultList();
	}
}
