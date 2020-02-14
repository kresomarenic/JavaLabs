package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;

public class KomisijskiIspit extends Ispit {
	
	public static final String INPUT_FILE_NAME = "komisijskiIspit.dat";
	public static final Integer NBR_OF_LINES_PER_RECORD = 6;
	
	private Profesor komisijskiClan1;
	private Profesor komisijskiClan2;
	private Profesor komisijskiClan3;
	
	public KomisijskiIspit(Long id, Predmet predmet, Student student, Integer ocjena, LocalDateTime datumIVrijeme,
			Profesor komisijskiClan1, Profesor komisijskiClan2, Profesor komisijskiClan3) {
		super(id, predmet, student, ocjena, datumIVrijeme);
		this.komisijskiClan1 = komisijskiClan1;
		this.komisijskiClan2 = komisijskiClan2;
		this.komisijskiClan3 = komisijskiClan3;
	}

	public Profesor getKomisijskiClan1() {
		return komisijskiClan1;
	}

	public void setKomisijskiClan1(Profesor komisijskiClan1) {
		this.komisijskiClan1 = komisijskiClan1;
	}

	public Profesor getKomisijskiClan2() {
		return komisijskiClan2;
	}

	public void setKomisijskiClan2(Profesor komisijskiClan2) {
		this.komisijskiClan2 = komisijskiClan2;
	}

	public Profesor getKomisijskiClan3() {
		return komisijskiClan3;
	}

	public void setKomisijskiClan3(Profesor komisijskiClan3) {
		this.komisijskiClan3 = komisijskiClan3;
	}
	
	
	
	
	
	

}
