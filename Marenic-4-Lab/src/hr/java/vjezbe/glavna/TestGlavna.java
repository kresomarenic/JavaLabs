package hr.java.vjezbe.glavna;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.sortiranje.ProfesorSorter;

public class TestGlavna {
	
	private final static Logger log = LoggerFactory.getLogger(TestGlavna.class);
	
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	
	
	
	public static void main(String[] args) {
		
		List<Profesor> profesori = new ArrayList<Profesor>();
		profesori.add(new Profesor("PR1234", "Alen", "Grozdić", "predavač", "Velika Gorica"));
		profesori.add(new Profesor("PR1235", "Božo", "Grozdić", "predavač", "Zagreb"));
		profesori.add(new Profesor("PR1236", "Pero", "Perić", "predavač", "Zagreb"));
		profesori.add(new Profesor("PR1237", "Ivo", "Ivić", "predavač", "Velika Gorica"));
			
		ispisProfesoraPoPrezimenuPaImenu(profesori);
		ispisProfesoraPoMjestuIPrezimenu(profesori);
			
			
				
	}
	
	
	private static void ispisProfesoraPoPrezimenuPaImenu(List<Profesor> profesori) {
		profesori.stream().sorted(new ProfesorSorter()).forEach(p -> System.out.println(p.getPrezime() + " " + p.getIme()));
		profesori.stream().sorted(Comparator.comparing(Profesor::getPrezime).thenComparing(Profesor::getIme)).forEach(p -> System.out.println(p.getPrezime() + " " + p.getIme()));
	}
	
	private static void ispisProfesoraPoMjestuIPrezimenu(List<Profesor> profesori) {
		System.out.println(profesori.stream().min(Comparator.comparing(Profesor::getNazivMjesta).thenComparing(Profesor::getPrezime)).get());
		System.out.println(profesori.stream().max(Comparator.comparing(Profesor::getNazivMjesta).thenComparing(Profesor::getPrezime)).get());			
	}
	

	
	
	
	
}
