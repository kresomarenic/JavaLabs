package hr.java.vjezbe.iznimke;

public class BazaPodatakaException extends Exception {

	private static final long serialVersionUID = 2257111157118760237L;

	public BazaPodatakaException() {
		super();
	}

	public BazaPodatakaException(String message, Throwable cause) {
		super(message, cause);
	}

	public BazaPodatakaException(String message) {
		super(message);
	}

	public BazaPodatakaException(Throwable cause) {
		super(cause);
	}

}
