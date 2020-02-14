package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

public interface Visokoskolska {
	
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaZavrsnogRada, Integer ocjenaObraneZavrsnogRada);
	
	default public BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) {
		
		Integer zbrojOcjena = 0;
		Ispit[] polozeniIspiti = (ispiti);
		
		for (Ispit ispit : polozeniIspiti) {			
			zbrojOcjena += ispit.getOcjena();
		}
		return BigDecimal.valueOf(zbrojOcjena).divide(BigDecimal.valueOf(polozeniIspiti.length));
	}
	
	private Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti) {
		
		Ispit[] polozeniIspiti = new Ispit[ispiti.length];
		
		Integer i = 0;
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() > 1) {
				polozeniIspiti[i] = ispit;
				i++;
			}
		}
		return Arrays.copyOf(polozeniIspiti, i+1);
	}
	
	default public Ispit[] filtrirajIspitePoStudentu(Ispit[] ispiti, Student student) {
		
		Ispit[] ispitiStudenta = new Ispit[ispiti.length];
		
		Integer i = 0;
		for (Ispit ispit : ispiti) {
			if (ispit.getStudent().equals(student)) {
				ispitiStudenta[i] = ispit;
				i++;
			}
		}
		return Arrays.copyOf(ispitiStudenta, i);
	}

}
