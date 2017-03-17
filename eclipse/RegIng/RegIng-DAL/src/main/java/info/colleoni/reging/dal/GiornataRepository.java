package info.colleoni.reging.dal;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.joda.time.DateTime;

public class GiornataRepository {

	EntityManager entityManager = EmSingleton.getInstance().getEmFactory().createEntityManager( );

	public Giornata save(Giornata giornata) {
		entityManager.getTransaction().begin();
		entityManager.persist(giornata);
		entityManager.getTransaction().commit();
		return giornata;
	}	
	
	public List<Giornata> elenco() {
		return entityManager.createNamedQuery("Giornata.getAll", Giornata.class)
				.getResultList();
	}
	
	public Giornata byKey(long id) {
		try {
			return entityManager.createNamedQuery("Giornata.byKey", Giornata.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}	
	}	
	
	public List<Giornata> byDipendente(Dipendente dipendente) {
		return entityManager.createNamedQuery("Giornata.byDipendente", Giornata.class)
				.setParameter("dipendente", dipendente)
				.getResultList();
	}	
	
	public List<Giornata> byDate(Date dateRef) {
		return entityManager.createNamedQuery("Giornata.byDate", Giornata.class)
				.setParameter("dateRef", dateRef)
				.getResultList();
	}	
	
	public List<Giornata> today() {
		return entityManager.createNamedQuery("Giornata.today", Giornata.class)
				.getResultList();
	}	
	
//	public List<Giornata> open() {
//		return entityManager.createNamedQuery("Giornata.open", Giornata.class)
//				.getResultList();
//	}
	
//	public List<Giornata> openByBadge(Dipendente badge) {
//		return entityManager.createNamedQuery("Giornata.openByBadge", Giornata.class)
//				.setParameter("badge", badge)
//				.getResultList();
//	}

	public List<Giornata> byBadgeAndMonth(String badge, String month) {
		DateTime dt = new DateTime();
		dt = dt.monthOfYear().setCopy(Integer.valueOf(month));
		DateTime first = dt.dayOfMonth().withMinimumValue()
				.hourOfDay().withMinimumValue()
				.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue()
				.millisOfSecond().withMinimumValue();
		DateTime last = dt.dayOfMonth().withMaximumValue()
				.hourOfDay().withMaximumValue()
				.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue()
				.millisOfSecond().withMinimumValue();		
		return entityManager.createNamedQuery("Giornata.byBarcodeAndDateRange", Giornata.class)
				.setParameter("badge", badge)
				.setParameter("dataInizio", first.toDate())
				.setParameter("dataFine", last.toDate())
				.getResultList();
	}

	public List<Giornata> byDateAndDipendente(Date dataOra, Dipendente dipendente) {
		return entityManager.createNamedQuery("Giornata.byDateAndDipendente", Giornata.class)
				.setParameter("dateRef", dataOra)
				.setParameter("dipendente", dipendente)
				.getResultList();
	}		
}
