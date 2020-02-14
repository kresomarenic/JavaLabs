package hr.java.vjezbe.entitet;

import java.util.ArrayList;
import java.util.List;

public class GenerickaOsoba<T extends Osoba> {
	
	private List<T> osobe;

	public GenerickaOsoba() {
		super();
		this.osobe = new ArrayList<T>();
	}

	public GenerickaOsoba(List<T> osobe) {
		super();
		this.osobe = osobe;
	}
		
	public void dodajOsobu(T osoba) {
		this.osobe.add(osoba);
	}	
	
	public T dohvatiOsobu(Integer index) {
		return this.osobe.get(index);
	}
	
	public void dodajViseOsoba(List<T> osobe) {
		this.osobe.addAll(osobe);
	}
	
	public List<T> dohvatiSveOsobw() {
		return this.osobe;
	}
	
	
}