package hr.java.vjezbe.entitet;

import java.util.List;

/**
 * Predstavlja apstraktni entitet obrazovne ustanove koji je definiran nazivom i poljima predmeta, profesora, studenta i ispita
 * @see Predmet
 * @see Profesor
 * @see Student
 * @see Ispit
 * @author kmarenic
 */
public abstract class ObrazovnaUstanova {
	
	public static final String INPUT_NAME = "obrazovnih ustanova";
	public static final Integer MIN_INPUT = 2;
	
	private String nazivUstanove;
	private List<Predmet> predmeti;
	private List<Profesor> profesori;
	private List<Student> studenti;
	private List<Ispit> ispiti;
	
	/**
	 * Inicijalizira objekt obrazovne ustanove
	 * @param nazivUstanove naziv obrazovne ustanove
	 * @param predmeti - polje predmeta
	 * @param profesori - polje profesora
	 * @param studenti - polje studenata
	 * @param ispiti - polje ispita
	 */
	public ObrazovnaUstanova(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
		super();
		this.nazivUstanove = nazivUstanove;
		this.predmeti = predmeti;
		this.profesori = profesori;
		this.studenti = studenti;
		this.ispiti = ispiti;
	}

	/**
	 * Dohvaća naziv obrazovne ustanove
	 * @return naziv obrazovne ustanove
	 */
	public String getNazivUstanove() {
		return nazivUstanove;
	}

	/**
	 * Postavlja naziv obrazovne ustanove
	 * @param nazivUstanove naziv obrazovne ustanove
	 */
	public void setNazivUstanove(String nazivUstanove) {
		this.nazivUstanove = nazivUstanove;
	}

	/**
	 * Dohvaća predmete obrazovne ustanove
	 * @return polje predmeta obrazovne ustanove
	 * @see Predmet
	 */
	public List<Predmet> getPredmeti() {
		return predmeti;
	}

	/**
	 * Postavlja predmete obrazovne ustanove
	 * @param predmeti polje predmeta obrazovne ustanove
	 * @see Predmet
	 */
	public void setPredmeti(List<Predmet> predmeti) {
		this.predmeti = predmeti;
	}

	/**
	 * Dohvaća profesore obrazovne ustanove
	 * @return polje profesora obrazovne ustanove
	 * @see Profesor
	 */
	public List<Profesor> getProfesori() {
		return profesori;
	}

	/**
	 * Postavlja profesore obrazovne ustanove
	 * @param profesori polje profesora obrazovne ustanove
	 * @see Profesor
	 */
	public void setProfesori(List<Profesor> profesori) {
		this.profesori = profesori;
	}

	/**
	 * Dohvaća studente obrazovne ustanove
	 * @return polje studenata obrazovne ustanove
	 * @see Student
	 */
	public List<Student> getStudenti() {
		return studenti;
	}

	/**
	 * Postavlja studente obrazovne ustanove
	 * @param studenti polje studenata obrazovne ustanove
	 * @see Student
	 */
	public void setStudenti(List<Student> studenti) {
		this.studenti = studenti;
	}

	/**
	 * Dohvaća ispite obrazovne ustanove
	 * @return polje ispita obrazovne ustanove
	 * @see Ispit
	 */
	public List<Ispit> getIspiti() {
		return ispiti;
	}

	/**
	 * Postavlja ispite obrazovne ustanove
	 * @param ispiti polje ispita obrazovne ustanove
	 * @see Ispit
	 */
	public void setIspiti(List<Ispit> ispiti) {
		this.ispiti = ispiti;
	}
	
	/**
	 * Određuje najoljeg studenta na godini
	 * @param godina godina studija
	 * @return Student
	 */
	public abstract Student odrediNajuspjesnijegStudentaNaGodini(Integer godina);

}
