package hr.java.vjezbe.niti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Messages;

public class NajviseIspitaNit implements Runnable {
	
	private final static Logger log = LoggerFactory.getLogger(NajviseIspitaNit.class);
	
	public final static Integer TIME_INTERVAL = 10;

	@Override
	public void run() {
		
		try {
			List<Student> studenti = BazaPodataka.dohvatiStudentePremaKriterijima(null);
			
			List<Student> studentiSNajviseIspita = new ArrayList<Student>();
			Integer maxIspita = 0;			
			Map<Student, Integer> mapStudentAndIspit = new HashMap<Student, Integer>();
				
			for (Student student : studenti) {
				
				Integer brojIspita = BazaPodataka.dohvatiIspitePremaKriterijima(new Ispit(null, null, student, null, null)).size();				
				mapStudentAndIspit.put(student, brojIspita);
				
				if (brojIspita > maxIspita) {
					maxIspita = brojIspita;
				}			
			}
			
			Integer najveciBrojIspita = maxIspita;
			studentiSNajviseIspita = mapStudentAndIspit.entrySet().stream().filter(entry -> entry.getValue().equals(najveciBrojIspita)).map(e -> e.getKey()).collect(Collectors.toList());
			
			
			if (!studentiSNajviseIspita.isEmpty()) {
				Messages.showInfoMessage("Ambiciozni studenti", "Slijedeći studenti su prijavili najviše ispita:", studentiSNajviseIspita.stream().map(s -> s.getIme() + " " + s.getPrezime()).collect(Collectors.toList()));
			}
			
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
		
	}

}
