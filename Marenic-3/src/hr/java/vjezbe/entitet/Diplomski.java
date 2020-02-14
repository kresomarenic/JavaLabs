package hr.java.vjezbe.entitet;

/**
 * Predstavlja sučelje Diplomski koje nasljeđuje sučelje Visokoskolska
 * @see Visokoskolska
 * @author kmarenic
 *
 */
public interface Diplomski extends Visokoskolska {
	
	/**
	 * Određuje studenta za rektorovu nagradu
	 * @return Student
	 */
	public Student odrediStudentaZaRektorovuNagradu();

}
