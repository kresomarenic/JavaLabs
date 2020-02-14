package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Predstavlja entitet Ispit koji je definiran Predmetom, Studentom, ocjenomm datum i vremenom
 * @see Predmet
 * @see Student
 * @author kmarenic
 */
public class Ispit extends Entitet implements Comparator<Ispit> {
	
	public static final String INPUT_FILE_NAME = "ispiti.dat";
	public static final Integer NBR_OF_LINES_PER_RECORD = 5;
	
	public static final String INPUT_NAME = "ispita";
	public static final Integer MIN_INPUT = 2;
	
	private Predmet predmet;
	private Student student;
	private Integer ocjena;
	private LocalDateTime datumIVrijeme;
	
	/**
	 * Inicijalizira objekt Ispit
	 * @param predmet predmet ispita
	 * @param student student ispita
	 * @param ocjena ocjena ispita
	 * @param datumIVrijeme datum i vrijeme ispita
	 */
	public Ispit(Long id, Predmet predmet, Student student, Integer ocjena, LocalDateTime datumIVrijeme) {
		super(id);
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
	}

	/**
	 * Dohvaća Predmet ispita
	 * @return Predmet predmet ispita
	 * @see Predmet
	 */
	public Predmet getPredmet() {
		return predmet;
	}

	/**
	 * Postavlja Predmet ispita
	 * @param predmet objekt Predmet
	 * @see Predmet
	 */
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	/**
	 * Dohvaća Studenta ispita
	 * @return Student student ispita
	 * @see Student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Postavlja Studenta ispita
	 * @param student objekt Student
	 * @see Student
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * Dohvaća ocjenu ispita
	 * @return ocjena ispita
	 * @see Student
	 */
	public Integer getOcjena() {
		return ocjena;
	}

	/**
	 * Postavlja ocjenu ispita
	 * @param ocjena ocjena ispita
	 */
	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}

	/**
	 * Dohvaća datum i vrijeme ispita u formatu dd.MM.yyyy. HH:ss
	 * @return datum i vrijeme ispita	 *
	 */
	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}
	
	/**
	 * Postavlja datum i vrijeme ispita u formatu dd.MM.yyyy. HH:ss
	 * @param datumIVrijeme datum i vrijeme ispita
	 */
	public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
		this.datumIVrijeme = datumIVrijeme;
	}

	@Override
	public String toString() {
		return predmet.getNaziv() + " - " + student.getPrezime() + "\n";
	}

	@Override
	public int compare(Ispit i1, Ispit i2) {

		int difference = i1.getPredmet().getNaziv().compareTo(i2.getPredmet().getNaziv());
		if (difference == 0) {
			difference = i1.getStudent().getPrezime().compareTo(i2.getStudent().getPrezime());
		}
		return difference;

	}
	
	

}
