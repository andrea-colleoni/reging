package info.colleoni.reging.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EsitoOperazione implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3578884117102086694L;
	
	private boolean ok;
	private String messaggio;
	
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
}
