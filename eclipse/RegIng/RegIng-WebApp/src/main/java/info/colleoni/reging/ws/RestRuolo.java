package info.colleoni.reging.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import info.colleoni.reging.dal.Ruolo;
import info.colleoni.reging.dal.RuoloRepository;

@Path("ruolo")
public class RestRuolo {
	
	RuoloRepository rr = new RuoloRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ruolo> all() {
		try {
			return rr.elenco();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ruolo byId(@PathParam("id") int id) {
		try {
			return rr.byKey(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insert(Ruolo r) {
		try {
			rr.save(r);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	

}
