package info.colleoni.reging.ws;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.colleoni.reging.bl.RulesTimbrature;
import info.colleoni.reging.dal.CoppiaTimbrate;
import info.colleoni.reging.dal.CoppiaTimbrateRepository;
import info.colleoni.reging.dal.Dipendente;
import info.colleoni.reging.dal.DipendenteRepository;
import info.colleoni.reging.dal.GiornataRepository;
import info.colleoni.reging.dal.Timbrata;
import info.colleoni.reging.dal.TimbrataRepository;
import info.colleoni.reging.dal.TipoTimbrata;

@Path("timbrata")
public class RestTimbrata {

	final static Logger logger = LoggerFactory.getLogger(RestTimbrata.class);

	TimbrataRepository tr = new TimbrataRepository();
	GiornataRepository gr = new GiornataRepository();
	DipendenteRepository dr = new DipendenteRepository();
	CoppiaTimbrateRepository ctr = new CoppiaTimbrateRepository();
	
	RulesTimbrature rt = new RulesTimbrature();

	@POST
	@Path("log/{codiceBadge}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Timbrata registraTimbrata(@Context HttpHeaders hh, @PathParam("codiceBadge") String codiceBadge) {
		try {
			Timbrata t = new Timbrata();
			t.setCodiceBadge(codiceBadge);
			t.setDataOra(new Date());
			if (hh != null) {
				//t.setIp(hh.getRequestHeader("Remote_Addr").toArray(String[]));
			}
			t.setTipoTimbrata(TipoTimbrata.NonIdentificata);
			tr.save(t);
			if (t != null) {
				Dipendente d = dr.byCodiceBadge(t.getCodiceBadge());
				if (d != null) {
					// solo se il barcode letto è valido
					CoppiaTimbrate ct = rt.getCoppiaDisponibile(d, t.getDataOra());
					t.setTipoTimbrata(rt.getTipoTimbrataBasataSuOrari(d, t));
					if (ct != null && t.getTipoTimbrata() == TipoTimbrata.NonIdentificata) {
						t.setTipoTimbrata(rt.getTipoTimbrataDaCoppia(ct, t));
					}
					t.setNecessitaCausale(rt.getNecessitaCausale(ct.getGiornata(), t));
				}
			}
			return t;
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return null;
	}
	
	@POST
	@Path("assign/{idTimbrata}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
	@Produces(MediaType.APPLICATION_JSON)
	public void assegnaTimbrata(@Context HttpHeaders hh, @PathParam("idTimbrata") int idTimbrata,
			@FormParam("tipoTimbrata") TipoTimbrata tipoTimbrata,
			@FormParam("causale") String causale,
			@FormParam("dataOraTimbrata") String dataOraTimbrata) {
		try {
			SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			Timbrata t = tr.byId(idTimbrata);
			t.setTipoTimbrata(tipoTimbrata);
			t.setCausale(causale);
			tr.save(t);
			if (t != null) {
				Dipendente d = dr.byCodiceBadge(t.getCodiceBadge());
				if (d != null) {
					// solo se il barcode letto è valido
					try {
						Date dt = sdt.parse(dataOraTimbrata);
						DateTime dtt = new DateTime(dt);
						Duration dur = new Duration(new DateTime(t.getDataOra()), dtt);
						if (Math.abs(dur.getStandardMinutes()) > d.getTurno().getMinutiArrotondamentoEccesso()) {
							t.setModificataManualmente(true);
						}
					} catch (ParseException ex) {
						logger.warn("data inserita non corretta => " + dataOraTimbrata);
					}
					
					CoppiaTimbrate ct = rt.getCoppiaDisponibile(d, t.getDataOra());
					TipoTimbrata proposta = rt.getTipoTimbrataBasataSuOrari(d, t);
					if (ct != null && proposta == TipoTimbrata.NonIdentificata) {
						proposta = rt.getTipoTimbrataDaCoppia(ct, t);
					}
					if(proposta != TipoTimbrata.NonIdentificata && tipoTimbrata != proposta) {
						t.setModificataManualmente(true);
					}
					t.setFuoriOrarioTurno(rt.getFuoriOrario(t, ct.getGiornata()));
					tr.save(t);
					
					CoppiaTimbrate cts = ctr.byId(ct.getId());
					switch(t.getTipoTimbrata()) {
					case Entrata:
					case FinePranzo:
					case Ripresa:
						cts.setIngresso(t);
						break;
					case Uscita:
					case InizioPranzo:
					case Interruzione:
						cts.setUscita(t);
						break;
					default:
						break;
					}
					ctr.save(cts);
				}
				tr.save(t);
				
				// se ho un'uscita (fine giornata) devo registrare anche il recupero ore
				if (t.getTipoTimbrata() == TipoTimbrata.Uscita) {
					rt.calcolaEAggiornaRecuperoOreGiornata(d, t.getDataOra());
				}
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
	}
}
