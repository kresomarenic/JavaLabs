package hr.java.vjezbe.iznimke;

/**
 * Predstavlja gresku u slucaju da nije moguce odrediti prosjek ocjena studenta
 * @author kmarenic
 *
 */
public class NemoguceOdreditiProsjekStudentaException extends Exception {	
	
	private static final long serialVersionUID = 2354675407061208204L;

	public NemoguceOdreditiProsjekStudentaException() {
		super("Nije moguće odrediti prosjek ocjena studenta!");
	}
	
	public NemoguceOdreditiProsjekStudentaException(String message) {
		super(message);
	}
	
	public NemoguceOdreditiProsjekStudentaException(Throwable cause) {
		super(cause);
	}
	
	public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
