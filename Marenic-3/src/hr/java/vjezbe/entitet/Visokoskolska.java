package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.Arrays;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Predstavlja interface za visokoškolske ustanove
 * @see VeleucilisteJave
 * @see FakultetRacunarstva
 * @author kmarenic
 *
 */
public interface Visokoskolska {
	
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, Integer ocjenaZavrsnogRada, Integer ocjenaObraneZavrsnogRada);
	
	/**
	 * Izračunava prosjek ocjena na ispitima
	 * @param ispiti polje ispita za izračun prosjeka
	 * @return prosjekOcjena
	 * @throws NemoguceOdreditiProsjekStudentaException u slučaju da neki ispit ima ocjenu nedovoljan
	 * @see Ispit
	 * @see NemoguceOdreditiProsjekStudentaException
	 */
	default public BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) throws NemoguceOdreditiProsjekStudentaException {
		
		Integer zbrojOcjena = 0;
		
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() >= 2) {
				zbrojOcjena += ispit.getOcjena();
			} else {
				throw new NemoguceOdreditiProsjekStudentaException(String.format("Student %s %s iz predmeta %s ima ocjenu nedovoljan!", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv()));
			}			
		}
		return BigDecimal.valueOf(zbrojOcjena).divide(BigDecimal.valueOf(ispiti.length));
	}
	
	/**
	 * Filtrira položene ispite
	 * @param ispiti polje svih ispita
	 * @return polje ispita koji su položeni (ocjena 1 - 5)
	 * @see Ispit
	 */
	default public Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti) {
		
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
	
	/**
	 * Filtrira ispite po studentu
	 * @param ispiti polje svih ispita
	 * @param student objekt tipa Student
	 * @return polje ispita kojima je pristupio zadani student
	 * @see Ispit
	 * @see Student
	 */
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
