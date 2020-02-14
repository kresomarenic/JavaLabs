package hr.java.vjezbe.niti;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Messages;

public class DatumRodjenjaNit implements Runnable {
	
	private final static Logger log = LoggerFactory.getLogger(DatumRodjenjaNit.class);
	
	public final static Integer TIME_INTERVAL = 10;

	@Override
	public void run() {
		
		try {
			List<Student> studentiKojiImajuRodjendan = BazaPodataka.dohvatiStudenteKojiImajuRodjendan(LocalDate.now());
			
			if (!studentiKojiImajuRodjendan.isEmpty()) {
				Messages.showInfoMessage("Čestitke", "Čestitajte rođendan slijedećim studentima:", studentiKojiImajuRodjendan.stream().map(s -> s.getIme() + " " + s.getPrezime()).collect(Collectors.toList()));
			}
			
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
		
	}

}
