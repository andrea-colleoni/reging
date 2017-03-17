package info.colleoni.reging.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.colleoni.reging.bl.GiornataDenormalizzata;
import info.colleoni.reging.dal.DipendenteRepository;
import info.colleoni.reging.dal.Giornata;
import info.colleoni.reging.dal.GiornataRepository;

@Path("giornata")
public class RestGiornata {
	
	final static Logger logger = LoggerFactory.getLogger(RestGiornata.class);

	GiornataRepository gr = new GiornataRepository();
	DipendenteRepository br = new DipendenteRepository();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<GiornataDenormalizzata> all(@DefaultValue("false") @QueryParam("today") boolean today) {
		List<GiornataDenormalizzata> _return = new ArrayList<GiornataDenormalizzata>();
		try {
			if (today) {
				for(Giornata g : gr.today())
					_return.add(new GiornataDenormalizzata(g));
			} else {
				for(Giornata g : gr.elenco())
					_return.add(new GiornataDenormalizzata(g));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _return;		
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Giornata byId(@PathParam("id") long id) {
		Giornata _return = null;
		try {
			logger.debug(String.format("byId => id: %1$d", id));
			_return = gr.byKey(id);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _return;		
	}	
	
	@GET
	@Path("dipendente")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Giornata> all(@QueryParam("month") String month,
			@QueryParam("badge") String badge) {
		List<Giornata> _return = null;
		try {
			logger.debug(String.format("all => badge: %1$s, month: %2$s", badge, month));
			_return = gr.byBadgeAndMonth(badge, month);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _return;		
	}
}
