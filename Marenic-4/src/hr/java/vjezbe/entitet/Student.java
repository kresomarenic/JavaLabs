package hr.java.vjezbe.entitet;

import java.time.LocalDate;

/**
 * Predstavlja entitet student koji je definiran Osobom, jmbag-om i datumom rođenja
 * @see Osoba 
 * @author kmarenic
 *
 */
public class Student extends Osoba {
	
	public static final String INPUT_NAME = "studenata";
	public static final Integer MIN_INPUT = 2;	
	
	private String jmbag;
	private LocalDate datumRodjenja;
	
	
	/**
	 * Inicijalizira objekt Studenta
	 * @param ime ime studenta
	 * @param prezime prezime studenta
	 * @param jmbag JMBAG studenta
	 * @param datumRodjenja datum rođenja studenta
	 */
	public Student(String ime, String prezime, String jmbag, LocalDate datumRodjenja) {
		super(ime, prezime);		
		this.jmbag = jmbag;
		this.datumRodjenja = datumRodjenja;
	}
	
	/**
	 * Dohvaća JMBAG studenta
	 * @return jmbag studenta
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Postavlja JMBAG studenta
	 * @param jmbag jmbag studenta
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Dohvaća datum rođenja studenta
	 * @return datumRodjenja studenta
	 */
	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	/**
	 * Postavlja datum rođenja studenta
	 * @param datumRodjenja datum rođenja studenta
	 */
	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	
}
