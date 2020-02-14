package hr.java.vjezbe.entitet;

import java.util.ArrayList;
import java.util.List;

public class Sveuciliste<T extends ObrazovnaUstanova> {
	
	private List<T> obrazovneUstanove;

	public Sveuciliste() {
		super();
		this.obrazovneUstanove = new ArrayList<T>();
	}

	public Sveuciliste(List<T> obrazovneUstanove) {
		super();
		this.obrazovneUstanove = obrazovneUstanove;
	}
	
	
	public void dodajObrazovnuUstanovu(T obrazovnaUstanova) {
		this.obrazovneUstanove.add(obrazovnaUstanova);
	}
	
	
	public T dohvatiObrazovnuUstanovu(Integer indeks) {
		return this.obrazovneUstanove.get(indeks);
	}
}
