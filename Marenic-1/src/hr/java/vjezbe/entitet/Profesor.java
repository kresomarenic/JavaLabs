package hr.java.vjezbe.entitet;


public class Profesor {
	
	public static final String NAME = "profesor";
	public static final Integer MIN_INPUT = 2;
	
	private String sifra;
	private String ime;
	private String prezime;
	private String titula;
	private String oib;
	private String jmbg;
	private String titulaIzaImena;
	
	
	public Profesor(String sifra, String ime, String prezime, String titula, String oib, String jmbg,
			String titulaIzaImena) {
		super();
		this.sifra = sifra;
		this.ime = ime;
		this.prezime = prezime;
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

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
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
		return "Profesor [sifra=" + sifra + ", ime=" + ime + ", prezime=" + prezime + ", titula=" + titula + ", oib="
				+ oib + ", jmbg=" + jmbg + ", titulaIzaImena=" + titulaIzaImena + "]";
	}
	
	
	
	
	
	
	

}
