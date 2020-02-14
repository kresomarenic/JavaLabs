package hr.java.vjezbe.entitet;

public class SrednjaSkola {
	
	public static final String INPUT_FILE_NAME = "srednjeSkole.txt";
	public static final Integer NBR_OF_LINES_PER_RECORD = 2;
	
	private Long id;
	private String naziv;
	
	public SrednjaSkola(Long id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
}
