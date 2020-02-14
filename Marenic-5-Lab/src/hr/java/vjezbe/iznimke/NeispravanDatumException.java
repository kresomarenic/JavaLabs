package hr.java.vjezbe.iznimke;

/**
 * Predstavlja gresku u slucaju da je unesen krivi format datuma
 * @author kmarenic
 *
 */
public class NeispravanDatumException extends RuntimeException {	
	
	private static final long serialVersionUID = 6288619908377337917L;

	public NeispravanDatumException() {
		super("Neispravan datum!");
	}
	
	public NeispravanDatumException(String message) {
		super(message);
	}
	
	public NeispravanDatumException(Throwable cause) {
		super(cause);
	}
	
	public NeispravanDatumException(String message, Throwable cause) {
		super(message, cause);
	}

}
