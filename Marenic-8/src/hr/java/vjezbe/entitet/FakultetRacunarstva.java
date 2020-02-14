package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;

/**
 * Predstavlja entitet Fakultet računarstva koji je definiran Obrazovnom ustanovom i Diplomskim sušeljem
 * @see ObrazovnaUstanova
 * @see Diplomski
 * @author kmarenic
 *
 */
public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {
	
	private final static Logger log = LoggerFactory.getLogger(FakultetRacunarstva.class);

	/**
	 * Inicijalizira objekt FakultetRacunarstva
	 * @param nazivUstanove naziv ustanove
	 * @param predmeti polje predmeta
	 * @param profesori polje profesora
	 * @param studenti polje studenata
	 * @param ispiti polje ispita
	 */
	public FakultetRacunarstva(Long id, String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
		super(id, nazivUstanove, predmeti, profesori, studenti, ispiti);		
	}

	@Override
	public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, Integer ocjenaDiplomskogRada, Integer ocjenaObraneDiplomskogRada) {
		
		Integer zbrojOcjena = 0;		
		for (Ispit ispit : ispiti) {
			zbrojOcjena += ispit.getOcjena();
		}
		
		BigDecimal prosjekOcjena = new BigDecimal(zbrojOcjena).divide(new BigDecimal(ispiti.size()));
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
		
		for (Student student : super.getStudenti()) {
			List<Ispit> ispitiStudenta = filtrirajIspitePoStudentu(super.getIspiti(), student);
			Integer brojOcjenaIzvrstan = 0;
			for (Ispit ispit : ispitiStudenta) {
				if (ispit.getOcjena().equals(Ocjena.IZVRSTAN.getOcjena())) {
					brojOcjenaIzvrstan++;
				}
			}
			
			if (brojOcjenaIzvrstan.compareTo(maxBrojOcjenaIzvrstan) > 0) {
				maxBrojOcjenaIzvrstan = brojOcjenaIzvrstan;
				najboljiStudent = student;
			}
		}

		return najboljiStudent;
	}

	@Override
	public Student odrediStudentaZaRektorovuNagradu() {

		Student studentZaNagradu = null;
		BigDecimal najboljiProsjek = new BigDecimal(0);

		List<Student> najmladiStudenti = new ArrayList<Student>();

		for (Student student : super.getStudenti()) {
			List<Ispit> ispitiStudenta = filtrirajIspitePoStudentu(super.getIspiti(), student);			
			BigDecimal prosjek;
			try {
				if (!ispitiStudenta.isEmpty()) {
					prosjek = odrediProsjekOcjenaNaIspitima(ispitiStudenta);
					if (prosjek.compareTo(najboljiProsjek) > 0) {
						najboljiProsjek = prosjek;
						studentZaNagradu = student;
					} else if (prosjek.compareTo(najboljiProsjek) == 0) {
						LocalDate datumRodjenjaNajboljeg = studentZaNagradu.getDatumRodjenja();
						LocalDate datumRodjenjaTrenutnog = student.getDatumRodjenja();
						if (datumRodjenjaTrenutnog.compareTo(datumRodjenjaNajboljeg) < 0) {
							studentZaNagradu = student;
						}
						if (datumRodjenjaTrenutnog.compareTo(datumRodjenjaNajboljeg) == 0) {
							najmladiStudenti.add(student);
						}
					}
				} else {
					System.out.println("Student nije izašao na niti jedan ispit i nije moguuće odrediti prosjek ocjena.");
					throw new NemoguceOdreditiProsjekStudentaException("Student nije izašao na niti jedan ispit i nije moguuće odrediti prosjek ocjena.");
				}				
			} catch (NemoguceOdreditiProsjekStudentaException ex) {				
				log.error(ex.getMessage(), ex);
			}
		}

		if (!najmladiStudenti.isEmpty()) {
			StringBuilder message = new StringBuilder();
			for (Student student : najmladiStudenti) {
				message.append(String.format("%s %s%n", student.getIme(), student.getPrezime()));
			}
			throw new PostojiViseNajmladjihStudenataException(
					String.format("Postoji više najmlađih studeneta!%n%s", message.toString()));
		}

		return studentZaNagradu;
	}

	

}
