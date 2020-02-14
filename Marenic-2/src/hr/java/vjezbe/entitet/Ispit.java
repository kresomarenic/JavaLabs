package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Ispit implements Comparator<Ispit> {
	
	public static final String INPUT_NAME = "ispita";
	public static final Integer MIN_INPUT = 2;
	
	public static final Integer OCJENA_ODLICAN = 5;
	
	private Predmet predmet;
	private Student student;
	private Integer ocjena;
	private LocalDateTime datumIVrijeme;
	
	public Ispit() {
		
	}
	
	public Ispit(Predmet predmet, Student student, Integer ocjena, LocalDateTime datumIVrijeme) {
		super();
		this.predmet = predmet;
		this.student = student;
		this.ocjena = ocjena;
		this.datumIVrijeme = datumIVrijeme;
	}

	public Predmet getPredmet() {
		return predmet;
	}

	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Integer getOcjena() {
		return ocjena;
	}

	public void setOcjena(Integer ocjena) {
		this.ocjena = ocjena;
	}

	public LocalDateTime getDatumIVrijeme() {
		return datumIVrijeme;
	}

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
