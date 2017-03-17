package info.colleoni.reging.dal;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmSingleton {
	
	static final String PERSISTENCE_UNIT_NAME = "RegIng";

	private EntityManagerFactory emFactory;
	
	public static EmSingleton instance;
	
	private EmSingleton() {
		emFactory = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
	}
	
	public static EmSingleton getInstance() {
		if (instance == null)
			instance = new EmSingleton();
		return instance;
	}
	
	public EntityManagerFactory getEmFactory() {
		return emFactory;
	}
}
