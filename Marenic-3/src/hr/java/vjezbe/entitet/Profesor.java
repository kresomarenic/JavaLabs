package hr.java.vjezbe.entitet;

/**
 * Predstavlja entitet profesora koji je definiran Osobom, šifrom, titulom, oib-om, jmbg-om i titulom iza imena
 * @see Osoba 
 * @author kmarenic
 *
 */
public class Profesor extends Osoba {
	
	public static final String INPUT_NAME = "profesora";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;	
	private String titula;
	private String oib;
	private String jmbg;
	private String titulaIzaImena;
	
	
	/**
	 * Inicijalizira objekt Profesor
	 * @param sifra šifra profesora
	 * @param ime ime profesora
	 * @param prezime prezime profesora
	 * @param titula titula profesora
	 * @param oib OIB profesora
	 * @param jmbg JMBG profesora
	 * @param titulaIzaImena titula iza imena profesora
	 */
	public Profesor(String sifra, String ime, String prezime, String titula, String oib, String jmbg,
			String titulaIzaImena) {
		super(ime, prezime);
		this.sifra = sifra;		
		this.titula = titula;
		this.oib = oib;
		this.jmbg = jmbg;
		this.titulaIzaImena = titulaIzaImena;
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

	/**
	 * Dohvaća OIB profesora
	 * @return oib profesora
	 */
	public String getOib() {
		return oib;
	}

	/**
	 * Postavlja OIB profesora
	 * @param oib oib profesora
	 */
	public void setOib(String oib) {
		this.oib = oib;
	}

	/**
	 * Dohvaća JMBG profesora
	 * @return jmbg profesora
	 */
	public String getJmbg() {
		return jmbg;
	}

	/**
	 * Postavlja JMBG profesora
	 * @param jmbg jmbg profesora
	 */
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	/**
	 * Dohvaća titulu iza imena profesora
	 * @return titulaIzaImena profesora
	 */
	public String getTitulaIzaImena() {
		return titulaIzaImena;
	}

	/**
	 * Postavlja titulu iza imena profesora
	 * @param titulaIzaImena titula iza imena profesora
	 */
	public void setTitulaIzaImena(String titulaIzaImena) {
		this.titulaIzaImena = titulaIzaImena;
	}
	
	
	@Override
	public String toString() {
		return "Profesor [sifra=" + sifra + ", ime=" + super.getIme() + ", prezime=" + super.getPrezime() + ", titula=" + titula + ", oib="
				+ oib + ", jmbg=" + jmbg + ", titulaIzaImena=" + titulaIzaImena + "]";
	}
	
	
	
	
	
	
	

}
