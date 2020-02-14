package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {

	public VeleucilisteJave(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
		super(nazivUstanove, predmeti, profesori, studenti, ispiti);		
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaZavrsnogRada, Integer ocjenaObraneZavrsnogRada) {
		
		Integer zbrojOcjena = 0;
		for (Ispit ispit : ispiti) {
			zbrojOcjena += ispit.getOcjena();
		}
		
		BigDecimal prosjekOcjena = new BigDecimal(zbrojOcjena).divide(new BigDecimal(ispiti.length));
		BigDecimal ocjenaRada = new BigDecimal(ocjenaZavrsnogRada);
		BigDecimal ocjenaObraneRada = new BigDecimal(ocjenaObraneZavrsnogRada);
		
		// konačna ocjena = (2 * prosjek ocjena studenta + ocjena završnog rada + ocjena obrane završnog rada) / 4
		BigDecimal konacnaOcjena = new BigDecimal(2);
		konacnaOcjena = konacnaOcjena.multiply(prosjekOcjena);
		konacnaOcjena = konacnaOcjena.add(ocjenaRada);
		konacnaOcjena = konacnaOcjena.add(ocjenaObraneRada);
		konacnaOcjena = konacnaOcjena.divide(new BigDecimal(4));		
		
		return konacnaOcjena;
	}

	@Override
	public Student odrediNajuspjesnijegStudentaNaGodini(Integer godina) {
		
		Student najboljiStudent = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);
		
		for (int i = 0; i < super.getStudenti().length; i++) {
			Ispit[] ispitiStudenta = filtrirajIspitePoStudentu(super.getIspiti(), super.getStudenti()[i]);
			BigDecimal prosjek = odrediProsjekOcjenaNaIspitima(ispitiStudenta);
			
			if (prosjek.compareTo(najboljiProsjek) >= 0) {
				najboljiProsjek = prosjek;
				najboljiStudent = super.getStudenti()[i];
			}
		}	
		
		return najboljiStudent;
	}

}
