package info.colleoni.reging.ws;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.colleoni.reging.dal.BancaOreRepository;
import info.colleoni.reging.dal.Dipendente;
import info.colleoni.reging.dal.DipendenteRepository;
import info.colleoni.reging.dal.GiornataRepository;
import info.colleoni.reging.dal.TipoOra;

@Path("bancaore")
public class RestBancaOre {
	
	final static Logger logger = LoggerFactory.getLogger(RestBancaOre.class);
	
	DipendenteRepository dr = new DipendenteRepository();
	GiornataRepository gr = new GiornataRepository();
	BancaOreRepository bor = new BancaOreRepository();

	@GET
	@Path("{codiceBadge}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Map<TipoOra, Float> situazioneRecuperoOreDipendente(@PathParam("codiceBadge") String codiceBadge) {
		Dipendente d = dr.byCodiceBadge(codiceBadge);
		if (d != null) {
			return d.getSaldiBancaOre();
		}
		return null;
	}
}
