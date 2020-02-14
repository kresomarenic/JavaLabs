package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Predstavlja entitet Veleucilista Jave koji je definiran Obrazovnom ustanovom i Visokoskolskim suceljem
 * @see ObrazovnaUstanova
 * @see Visokoskolska
 * @author kmarenic
 *
 */
public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska {
	
	private final static Logger log = LoggerFactory.getLogger(VeleucilisteJave.class);

	/**
	 * Inicijalizira objekt VeleucilisteJave
	 * @param nazivUstanove naziv obrazovne ustanove
	 * @param predmeti polje predmeta
	 * @param profesori polje profesora
	 * @param studenti polje studenata
	 * @param ispiti polje ispita
	 */
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
			BigDecimal prosjek;
			try {
				prosjek = odrediProsjekOcjenaNaIspitima(ispitiStudenta);
				if (prosjek.compareTo(najboljiProsjek) >= 0) {
					najboljiProsjek = prosjek;
					najboljiStudent = super.getStudenti()[i];
				}
			} catch (NemoguceOdreditiProsjekStudentaException ex) {
				System.out.printf("Student %s %s zbog nedovoljne ocjene iz nekog od predmeta ima prosjek \"nedovoljan(1)\"%n", super.getStudenti()[i].getIme(), super.getStudenti()[i].getPrezime());
				log.error(ex.getMessage(), ex);
			}
			
			
		}	
		
		return najboljiStudent;
	}

}
