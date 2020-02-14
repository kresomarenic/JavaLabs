package hr.java.vjezbe.entitet;

public enum Ocjena {
	
	NEDOVOLJAN (1, "Nedovoljan"),
	DOVOLJAN (2, "Dovoljan"),
	DOBAR (3, "Dobar"),
	VRLO_DOBAR (4, "Vrlo dobar"),
	IZVRSTAN (5, "Izvrstan");
	
	private Integer ocjena;
	private String nazivOcjene;
	
	private Ocjena(Integer ocjena, String nazivOcjene) {
		this.ocjena = ocjena;
		this.nazivOcjene = nazivOcjene;
	}

	public Integer getOcjena() {
		return ocjena;
	}

	public String getNazivOcjene() {
		return nazivOcjene;
	}
	
	
	
	

}
