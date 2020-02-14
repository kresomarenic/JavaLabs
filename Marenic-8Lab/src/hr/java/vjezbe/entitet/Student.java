package hr.java.vjezbe.entitet;

import java.time.LocalDate;

/**
 * Predstavlja entitet student koji je definiran Osobom, jmbag-om i datumom rođenja
 * @see Osoba 
 * @author kmarenic
 *
 */
public class Student extends Osoba {
	
	public static final String INPUT_FILE_NAME = "studenti.dat";
	public static final Integer NBR_OF_LINES_PER_RECORD = 6;
	
	public static final String INPUT_NAME = "studenata";
	public static final Integer MIN_INPUT = 2;	
	
	private String jmbag;
	private LocalDate datumRodjenja;
	private String adresa;
	
	
	/**
	 * Inicijalizira objekt Studenta
	 * @param ime ime studenta
	 * @param prezime prezime studenta
	 * @param jmbag JMBAG studenta
	 * @param datumRodjenja datum rođenja studenta
	 */
	public Student(Long id, String ime, String prezime, String jmbag, LocalDate datumRodjenja, String adresa) {
		super(id, ime, prezime);		
		this.jmbag = jmbag;
		this.datumRodjenja = datumRodjenja;
		this.adresa = adresa;
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
	
	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	@Override
	public String toString() {
		return String.format("%s %s", this.getIme(), this.getPrezime());
	}
	
}
