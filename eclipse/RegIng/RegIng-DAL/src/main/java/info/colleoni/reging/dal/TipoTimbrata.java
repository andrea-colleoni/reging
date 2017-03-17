package info.colleoni.reging.dal;

public enum TipoTimbrata {
	NonIdentificata,
	Entrata,
	InizioPranzo,
	FinePranzo,
	Interruzione,
	Ripresa,
	Uscita,
	Anomala;
	
	public static TipoTimbrata fromString(String param) {
        //String toUpper = param.toUpperCase();
        try {
            return valueOf(param);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }	
}
