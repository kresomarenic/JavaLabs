package hr.java.vjezbe.iznimke;

/**
 * Predstavlja gresku u slucaju da nije uneseno ispravno prezime
 * @author kmarenic
 *
 */
public class NeispravnoPrezimeException extends Exception {	
	
	private static final long serialVersionUID = 6288619908377337917L;

	public NeispravnoPrezimeException() {
		super("Neispravno prezime!");
	}
	
	public NeispravnoPrezimeException(String message) {
		super(message);
	}
	
	public NeispravnoPrezimeException(Throwable cause) {
		super(cause);
	}
	
	public NeispravnoPrezimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
