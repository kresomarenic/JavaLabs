package hr.java.vjezbe.iznimke;

/**
 * Predstavlja gresku u slucaju da postoji vise najmladih studenata
 * @author kmarenic
 *
 */
public class PostojiViseNajmladjihStudenataException extends RuntimeException {
	
	private static final long serialVersionUID = 4815319869172975408L;

	public PostojiViseNajmladjihStudenataException() {
		super("Postoji više najmlađih studenata!");
	}
	
	public PostojiViseNajmladjihStudenataException(String message) {
		super(message);
	}
	
	public PostojiViseNajmladjihStudenataException(Throwable cause) {
		super(cause);
	}
	
	public PostojiViseNajmladjihStudenataException(String message, Throwable cause) {
		super(message, cause);
	}

}
