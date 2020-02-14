package hr.java.vjezbe.entitet;

/**
 * Predstavlja abstraktni entitet osobe
 * Entitet je definiran imenom i prezimenom
 * @author kmarenic
 *
 */
public abstract class Osoba {
	
	private String ime;
	private String prezime;	
	
	/**
	 * Inicijalizira ime i prezime osobe
	 * @param ime ime osobe
	 * @param prezime prezime osobe
	 */
	public Osoba(String ime, String prezime) {
		super();
		this.ime = ime;
		this.prezime = prezime;
	}
	
	/**
	 * Dohvaća ime osobe
	 * @return ime osobe
	 */
	public String getIme() {
		return ime;
	}
	
	/**
	 * Postavlja ime osobe
	 * @param ime ime osobe
	 */
	public void setIme(String ime) {
		this.ime = ime;
	}
	
	/**
	 * Dohvaća prezime osobe
	 * @return prezime osobe
	 */
	public String getPrezime() {
		return prezime;
	}
	
	/**
	 * Postavlja prezime osobe
	 * @param prezime prezime osobe
	 */
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

}
