package hr.java.vjezbe.entitet;

/**
 * Predstavlja entitet profesora koji je definiran Osobom, šifrom, titulom
 * @see Osoba 
 * @author kmarenic
 *
 */
public class Profesor extends Osoba {
	
	public static final String INPUT_FILE_NAME = "profesori.dat";
	public static final Integer NBR_OF_LINES_PER_RECORD = 5;
	
	public static final String INPUT_NAME = "profesora";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;	
	private String titula;
	
	public Profesor() {
				
	}
	
	/**
	 * Inicijalizira objekt Profesor
	 * @param sifra šifra profesora
	 * @param ime ime profesora
	 * @param prezime prezime profesora
	 * @param titula titula profesora
	 */
	public Profesor(Long id, String sifra, String ime, String prezime, String titula) {
		super(id, ime, prezime);
		this.sifra = sifra;		
		this.titula = titula;		
	}
	
	/**
	 * Dohvaća šifru profesora
	 * @return sifra profesora
	 */
	public String getSifra() {
		return sifra;
	}

	/**
	 * Postavlja šifru profesora
	 * @param sifra šifra profesora
	 */
	public void setSifra(String sifra) {
		this.sifra = sifra;
	}
	
	/**
	 * Dohvaća titulu
	 * @return titula
	 */
	public String getTitula() {
		return titula;
	}

	/**
	 * Postavlja titula profesora
	 * @param titula titula profesora
	 */
	public void setTitula(String titula) {
		this.titula = titula;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", this.getIme(), this.getPrezime());
	}
	
}
