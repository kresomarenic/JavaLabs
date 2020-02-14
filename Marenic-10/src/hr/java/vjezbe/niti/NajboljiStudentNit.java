package hr.java.vjezbe.niti;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.Main;
import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Messages;

public class NajboljiStudentNit implements Runnable {
	
	private final static Logger log = LoggerFactory.getLogger(NajboljiStudentNit.class);
	
	public final static Integer TIME_INTERVAL = 3;

	@Override
	public void run() {
		
		try {
			List<Student> studenti = BazaPodataka.dohvatiStudentePremaKriterijima(null);
			
			
			Double najboljiProsjek = 0d;
			Student najboljiStudent = null;
				
			for (Student student : studenti) {
				
				List<Ispit> ispitiStudenta = BazaPodataka.dohvatiIspitePremaKriterijima(new Ispit(null, null, student, null, null));
				OptionalDouble prosjek = ispitiStudenta.stream().mapToDouble(i -> i.getOcjena()).average();
				
				if (prosjek.isPresent() && prosjek.getAsDouble() > najboljiProsjek) {
					najboljiStudent = student;
					najboljiProsjek = prosjek.getAsDouble();
				}
			}				
			
			if (najboljiStudent != null) {
				Main.setStageTitle(String.format("Najbolji student je: %s %s sa prosjekom: %.2f", najboljiStudent.getIme(), najboljiStudent.getPrezime(), najboljiProsjek));
			} else {
				Main.setStageTitle("Nema najboljeg studenta");
			}
			
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
		
	}

}
