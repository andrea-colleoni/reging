package info.colleoni.reging.ws;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.colleoni.reging.dal.Dipendente;
import info.colleoni.reging.dal.DipendenteRepository;
import info.colleoni.reging.dal.Ruolo;
import info.colleoni.reging.dal.RuoloRepository;

@Path("dipendente")
public class RestDipendenti {
	
	final static Logger logger = LoggerFactory.getLogger(RestDipendenti.class);
	
	DipendenteRepository br = new DipendenteRepository();
	RuoloRepository rr = new RuoloRepository();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dipendente> all() {
		try {
			return br.elenco();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@GET
	@Path("byCodiceBadge/{codiceBadge}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dipendente byCodiceBadge(@PathParam("codiceBadge") String codiceBadge) {
		logger.debug(String.format("byCodiceBadge => codiceBadge: %1$s", codiceBadge));
		
		try {
			return br.byCodiceBadge(codiceBadge);
		} catch (Exception ex) {
			logger.error("exception", ex);
			return null;
		}
	}	
	
	@GET
	@Path("byUsername/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dipendente byUsername(@PathParam("username") String username) {
		logger.debug(String.format("byUsername => username: %1$s", username));
		
		try {
			return br.byUsername(username);
		} catch (Exception ex) {
			logger.error("exception", ex);
			return null;
		}
	}	
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Dipendente login(@FormParam("username") String username,
			@FormParam("password") String password) {
		logger.debug(String.format("login => username: %1$s", username));
		
		try {
			if(br.login(username, password)){
				return br.byUsername(username);
			} else {
				return null;
			}
		} catch (Exception ex) {
			logger.error("exception", ex);
			return null;
		}
	}	
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dipendente byId(@PathParam("id") int id) {
		logger.debug(String.format("byId => badgeCode: %1$d", id));
		
		try {
			return br.byId(id);
		} catch (Exception ex) {
			logger.error("exception", ex);
			return null;
		}
	}	
	
	@POST
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public EsitoOperazione insert(String userDataJSON) {
		EsitoOperazione esito = new EsitoOperazione();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Dipendente b = mapper.readValue(userDataJSON, Dipendente.class);
			List<Ruolo> rs = rr.byNome("user");
			if (rs.size() > 0) {
				b.setRuolo(rs.get(0));
			}
			br.save(b);
			esito.setOk(true);
			esito.setMessaggio("Badge salvato correttamente");
		} catch (Exception ex) {
			ex.printStackTrace();
			esito.setOk(false);
			esito.setMessaggio(ex.getMessage());
		}
		return esito;
	}
}
