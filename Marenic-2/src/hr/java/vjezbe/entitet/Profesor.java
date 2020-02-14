package hr.java.vjezbe.entitet;

public class Profesor extends Osoba {
	
	public static final String INPUT_NAME = "profesora";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;	
	private String titula;
	private String oib;
	private String jmbg;
	private String titulaIzaImena;
	
	
	public Profesor(String sifra, String ime, String prezime, String titula, String oib, String jmbg,
			String titulaIzaImena) {
		super(ime, prezime);
		this.sifra = sifra;		
		this.titula = titula;
		this.oib = oib;
		this.jmbg = jmbg;
		this.titulaIzaImena = titulaIzaImena;
	}
	
	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}
	
	public String getTitula() {
		return titula;
	}

	public void setTitula(String titula) {
		this.titula = titula;
	}

	public String getOib() {
		return oib;
	}

	public void setOib(String oib) {
		this.oib = oib;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getTitulaIzaImena() {
		return titulaIzaImena;
	}

	public void setTitulaIzaImena(String titulaIzaImena) {
		this.titulaIzaImena = titulaIzaImena;
	}

	@Override
	public String toString() {
		return "Profesor [sifra=" + sifra + ", ime=" + super.getIme() + ", prezime=" + super.getPrezime() + ", titula=" + titula + ", oib="
				+ oib + ", jmbg=" + jmbg + ", titulaIzaImena=" + titulaIzaImena + "]";
	}
	
	
	
	
	
	
	

}
