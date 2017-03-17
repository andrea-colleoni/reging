package info.colleoni.reging;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class RegistrazioneIngressi extends ResourceConfig {

	public RegistrazioneIngressi() {
		// Register resources and providers using package-scanning.
		packages("info.colleoni.reging.ws");

		// Register my custom provider - not needed if it's in my.package.
		register(CORSFilter.class);

		// Enable Tracing support.
		property(ServerProperties.TRACING, "ALL");
	}
}
