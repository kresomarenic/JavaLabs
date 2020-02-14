package hr.java.vjezbe.entitet;

/**
 * Predstavlja entitet Predmet koji je definiran šifrom, nazivom, brojem ECTS bodova, nositeljem (Profesorom) i poljem studenata koji ga pohađaju
 * @see Profesor
 * @see Student
 * @author kmarenic
 */
public class Predmet {
	
	public static final String INPUT_NAME = "predmeta";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;
	private String naziv;
	private Integer brojEctsBodova;
	private Profesor nositelj;
	private Student[] studenti;
	
	
	
	/**
	 * Inicijalizira objekt Predmet
	 * @param sifra šifra predmeta
	 * @param naziv naziv predmeta
	 * @param brojEctsBodova broj ECTS bodova
	 * @param nositelj Profesor koji je nositelj predmeta
	 * @param brojStudenata broj studenata koji pohađaju predmet
	 */
	public Predmet(String sifra, String naziv, Integer brojEctsBodova, Profesor nositelj, int brojStudenata) {
		super();
		this.sifra = sifra;
		this.naziv = naziv;
		this.brojEctsBodova = brojEctsBodova;
		this.nositelj = nositelj;
		this.studenti = new Student[brojStudenata];
	}

	
	/**
	 * Dohvaća šifru predmeta
	 * @return sifra predmeta
	 */
	public String getSifra() {
		return sifra;
	}

	/**
	 * Postavlja šifru predmeta
	 * @param sifra šifra predemta
	 */
	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	/**
	 * Dohvaća naziv predmeta
	 * @return naziv predmeta
	 */
	public String getNaziv() {
		return naziv;
	}

	/**
	 * Postavlja naziv predmeta
	 * @param naziv naziv predemta
	 */
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	/**
	 * Dohvaća broj ECTS bodova predmeta
	 * @return broj ECTS bodova
	 */
	public Integer getBrojEctsBodova() {
		return brojEctsBodova;
	}

	/**
	 * Postavlja broj ECTS bodova predmeta
	 * @param brojEctsBodova broj ECTS bodova predmeta
	 */
	public void setBrojEctsBodova(Integer brojEctsBodova) {
		this.brojEctsBodova = brojEctsBodova;
	}

	/**
	 * Dohvaća nositelja predmeta
	 * @return Profesor
	 * @see Profesor
	 */
	public Profesor getNositelj() {
		return nositelj;
	}

	/**
	 * Postavlja nositelja predmeta
	 * @param nositelj nostitelj predmeta
	 * @see Profesor
	 */
	public void setNositelj(Profesor nositelj) {
		this.nositelj = nositelj;
	}
	
	/**
	 * Dohvaća studente koji pohađaju predmet
	 * @return polje studenata
	 * @see Student
	 */
	public Student[] getStudenti() {
		return studenti;
	}

	/**
	 * Postavlja studente koji pohađaju predmet
	 * @param studenti polje studenata koji pohađaju predmet
	 * @see Student
	 */
	public void setStudenti(Student[] studenti) {
		this.studenti = studenti;
	}
	
}
