package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

	public FakultetRacunarstva(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenti, ispiti);		
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaDiplomskogRada, Integer ocjenaObraneDiplomskogRada) {
		
		Integer zbrojOcjena = 0;
		for (Ispit ispit : ispiti) {
			zbrojOcjena += ispit.getOcjena();
		}
		
		BigDecimal prosjekOcjena = new BigDecimal(zbrojOcjena).divide(new BigDecimal(ispiti.length));
		BigDecimal ocjenaRada = new BigDecimal(ocjenaDiplomskogRada);
		BigDecimal ocjenaObraneRada = new BigDecimal(ocjenaObraneDiplomskogRada);
		
		// konačna ocjena = (3 * prosjek ocjena studenta + ocjena diplomskog rada + ocjena obrane diplomskog rada) / 5 
		BigDecimal konacnaOcjena = new BigDecimal(3);
		konacnaOcjena = konacnaOcjena.multiply(prosjekOcjena);
		konacnaOcjena = konacnaOcjena.add(ocjenaRada);
		konacnaOcjena = konacnaOcjena.add(ocjenaObraneRada);
		konacnaOcjena = konacnaOcjena.divide(new BigDecimal(5));			
		
		return konacnaOcjena;
	}
	
	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(Integer godina) {

		Student najboljiStudent = null;
		Integer maxBrojOcjenaIzvrstan = 0;
		
		for (int i = 0; i < super.getStudenti().length; i++) {
			Ispit[] ispitiStudenta = filtrirajIspitePoStudentu(super.getIspiti(), super.getStudenti()[i]);
			Integer brojOcjenaIzvrstan = 0;
			for (Ispit ispit : ispitiStudenta) {
				if (ispit.getOcjena().equals(Ispit.OCJENA_ODLICAN)) {
					brojOcjenaIzvrstan++;
				}
			}
			
			if (brojOcjenaIzvrstan.compareTo(maxBrojOcjenaIzvrstan) > 0) {
				maxBrojOcjenaIzvrstan = brojOcjenaIzvrstan;
				najboljiStudent = super.getStudenti()[i];
			}
		}	
		
		return najboljiStudent;
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() {

		Student studentZaNagradu = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);		
		
		for (Student student : super.getStudenti()) {
			Ispit[] ispitiStudenta = filtrirajIspitePoStudentu(super.getIspiti(), student);
			BigDecimal prosjek = odrediProsjekOcjenaNaIspitima(ispitiStudenta);
						
			if (prosjek.compareTo(najboljiProsjek) > 0) {
				najboljiProsjek = prosjek;
				studentZaNagradu = student;
			} else if (prosjek.compareTo(najboljiProsjek) == 0) {
				LocalDate datumRodjenjaNajboljeg = studentZaNagradu.getDatumRodjenja();
				LocalDate datumRodjenjaTrenutnog = student.getDatumRodjenja();
				if (datumRodjenjaTrenutnog.compareTo(datumRodjenjaNajboljeg) < 0) {
					studentZaNagradu = student;
				} 
			}
		}	
		
		return studentZaNagradu;
	}

	

}
