package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Predstavlja interface za visokoškolske ustanove
 * @see VeleucilisteJave
 * @see FakultetRacunarstva
 * @author kmarenic
 *
 */
public interface Visokoskolska {
	
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Integer ocjenaZavrsnogRada, Integer ocjenaObraneZavrsnogRada);
	
	/**
	 * Izračunava prosjek ocjena na ispitima
	 * @param ispiti polje ispita za izračun prosjeka
	 * @return prosjekOcjena
	 * @throws NemoguceOdreditiProsjekStudentaException u slučaju da neki ispit ima ocjenu nedovoljan
	 * @see Ispit
	 * @see NemoguceOdreditiProsjekStudentaException
	 */
	default public BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispiti) throws NemoguceOdreditiProsjekStudentaException {
		
		Integer zbrojOcjena = 0;
		
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() >= Ocjena.DOVOLJAN.getOcjena()) {
				zbrojOcjena += ispit.getOcjena();
			} else {
				throw new NemoguceOdreditiProsjekStudentaException(String.format("Student %s %s iz predmeta %s ima ocjenu nedovoljan!", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv()));
			}			
		}
		return BigDecimal.valueOf(zbrojOcjena).divide(BigDecimal.valueOf(ispiti.size()));
	}
	
	/**
	 * Filtrira položene ispite
	 * @param ispiti polje svih ispita
	 * @return polje ispita koji su položeni (ocjena 1 - 5)
	 * @see Ispit
	 */
	default public List<Ispit> filtrirajPolozeneIspite(List<Ispit> ispiti) {
		
		List<Ispit> polozeniIspiti = new ArrayList<Ispit>();
		
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() > Ocjena.NEDOVOLJAN.getOcjena()) {
				polozeniIspiti.add(ispit);				
			}
		}
		return polozeniIspiti;
	}
	
	/**
	 * Filtrira ispite po studentu
	 * @param ispiti polje svih ispita
	 * @param student objekt tipa Student
	 * @return polje ispita kojima je pristupio zadani student
	 * @see Ispit
	 * @see Student
	 */
	default public List<Ispit> filtrirajIspitePoStudentu(List<Ispit> ispiti, Student student) {
		
		Long startTime;
		Long endTime;
		
		List<Ispit> ispitiStudenta = new ArrayList<Ispit>();
		
		startTime = System.currentTimeMillis();
		for (Ispit ispit : ispiti) {
			if (ispit.getStudent().equals(student)) {
				ispitiStudenta.add(ispit);
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja foreach petlje je: " + (endTime - startTime) + " ms");
		
		ispitiStudenta.clear();
		startTime = System.currentTimeMillis();
		ispitiStudenta = ispiti.stream().filter(i -> i.getStudent() == student).collect(Collectors.toList());
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja lambda izraza je: " + (endTime - startTime) + " ms");
		
		
		return ispitiStudenta;
	}

}
