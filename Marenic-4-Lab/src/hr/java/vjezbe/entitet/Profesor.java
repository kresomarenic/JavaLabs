package hr.java.vjezbe.entitet;

/**
 * Predstavlja entitet profesora koji je definiran Osobom, šifrom, titulom
 * @see Osoba 
 * @author kmarenic
 *
 */
public class Profesor extends Osoba {
	
	public static final String INPUT_NAME = "profesora";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;	
	private String titula;
	private String nazivMjesta;
	
	/**
	 * Inicijalizira objekt Profesor
	 * @param sifra šifra profesora
	 * @param ime ime profesora
	 * @param prezime prezime profesora
	 * @param titula titula profesora
	 * @param nazivMjesta naziv mjesta
	 */
	public Profesor(String sifra, String ime, String prezime, String titula, String nazivMjesta) {
		super(ime, prezime);
		this.sifra = sifra;		
		this.titula = titula;	
		this.nazivMjesta = nazivMjesta;
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

	public String getNazivMjesta() {
		return nazivMjesta;
	}

	public void setNazivMjesta(String nazivMjesta) {
		this.nazivMjesta = nazivMjesta;
	}

	@Override
	public String toString() {
		return nazivMjesta + " - " + getIme() + " " + getPrezime();
	}

	
	
	
	
	
}
