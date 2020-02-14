package hr.java.vjezbe.entitet;

import java.util.Set;

/**
 * Predstavlja entitet Predmet koji je definiran šifrom, nazivom, brojem ECTS bodova, nositeljem (Profesorom) i poljem studenata koji ga pohađaju
 * @see Profesor
 * @see Student
 * @author kmarenic
 */
public class Predmet extends Entitet {
	
	public static final String INPUT_FILE_NAME = "predmeti.dat";
	public static final Integer NBR_OF_LINES_PER_RECORD = 6;
	
	public static final String INPUT_NAME = "predmeta";
	public static final Integer MIN_INPUT = 3;
	
	private String sifra;
	private String naziv;
	private Integer brojEctsBodova;
	private Profesor nositelj;
	private Set<Student> studenti;;
	
	public Predmet() {
		
	}
	
	/**
	 * Inicijalizira objekt Predmet
	 * @param sifra šifra predmeta
	 * @param naziv naziv predmeta
	 * @param brojEctsBodova broj ECTS bodova
	 * @param nositelj Profesor koji je nositelj predmeta
	 * @param brojStudenata broj studenata koji pohađaju predmet
	 */
	public Predmet(Long id, String sifra, String naziv, Integer brojEctsBodova, Profesor nositelj, Set<Student> studenti) {
		super(id);
		this.sifra = sifra;
		this.naziv = naziv;
		this.brojEctsBodova = brojEctsBodova;
		this.nositelj = nositelj;
		this.studenti = studenti;
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
	public Set<Student> getStudenti() {		
		return studenti;
	}

	/**
	 * Postavlja studente koji pohađaju predmet
	 * @param studenti polje studenata koji pohađaju predmet
	 * @see Student
	 */
	public void setStudenti(Set<Student> studenti) {
		this.studenti = studenti;
	}
	
	@Override
	public String toString() {
		return String.format("%s", this.getNaziv());
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brojEctsBodova == null) ? 0 : brojEctsBodova.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((nositelj == null) ? 0 : nositelj.hashCode());
		result = prime * result + ((sifra == null) ? 0 : sifra.hashCode());
		result = prime * result + ((studenti == null) ? 0 : studenti.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predmet other = (Predmet) obj;
		if (brojEctsBodova == null) {
			if (other.brojEctsBodova != null)
				return false;
		} else if (!brojEctsBodova.equals(other.brojEctsBodova))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (nositelj == null) {
			if (other.nositelj != null)
				return false;
		} else if (!nositelj.equals(other.nositelj))
			return false;
		if (sifra == null) {
			if (other.sifra != null)
				return false;
		} else if (!sifra.equals(other.sifra))
			return false;
		if (studenti == null) {
			if (other.studenti != null)
				return false;
		} else if (!studenti.equals(other.studenti))
			return false;
		return true;
	}
	
	
	
}
