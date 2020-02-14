package hr.java.vjezbe.entitet;

public abstract class ObrazovnaUstanova {
	
	public static final String INPUT_NAME = "obrazovnih ustanova";
	public static final Integer MIN_INPUT = 2;
	
	private String nazivUstanove;
	private Predmet[] predmeti;
	private Profesor[] profesori;
	private Student[] studenti;
	private Ispit[] ispiti;
	
	public ObrazovnaUstanova(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
		super();
		this.nazivUstanove = nazivUstanove;
		this.predmeti = predmeti;
		this.profesori = profesori;
		this.studenti = studenti;
		this.ispiti = ispiti;
	}

	public String getNazivUstanove() {
		return nazivUstanove;
	}

	public void setNazivUstanove(String nazivUstanove) {
		this.nazivUstanove = nazivUstanove;
	}

	public Predmet[] getPredmeti() {
		return predmeti;
	}

	public void setPredmeti(Predmet[] predmeti) {
		this.predmeti = predmeti;
	}

	public Profesor[] getProfesori() {
		return profesori;
	}

	public void setProfesori(Profesor[] profesori) {
		this.profesori = profesori;
	}

	public Student[] getStudenti() {
		return studenti;
	}

	public void setStudenti(Student[] studenti) {
		this.studenti = studenti;
	}

	public Ispit[] getIspiti() {
		return ispiti;
	}

	public void setIspiti(Ispit[] ispiti) {
		this.ispiti = ispiti;
	}
	
	public abstract Student odrediNajuspjesnijegStudentaNaGodini(Integer godina);

}
