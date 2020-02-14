package hr.java.vjezbe.glavna;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Ocjena;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.Sveuciliste;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.NeispravanDatumException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.sortiranje.StudentSorter;

public class Glavna {
	
	private final static Logger log = LoggerFactory.getLogger(Glavna.class);
	
	private final static String DATE_FORMAT = "dd.MM.yyyy.";
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	
	
	public static void main(String[] args) {
		
		Scanner input = null;
		
		try {
			
			input = new Scanner(System.in);				
			
			Sveuciliste<ObrazovnaUstanova> sveuciliste = new Sveuciliste<ObrazovnaUstanova>(new ArrayList<ObrazovnaUstanova>());
			
			for (int i = 0; i < 2; i++) {
				System.out.printf("Unesite podatke za obrazovnu ustanovu (%s/%s): %n", i+1, 2);
				
				List<Profesor> profesori = new ArrayList<Profesor>();						
				for (int j = 0; j < Profesor.MIN_INPUT; j++) {
					profesori.add(unosProfesora(input, j, Profesor.MIN_INPUT));
				}				
				
				List<Predmet> predmeti = new ArrayList<Predmet>();
				Map<Profesor, List<Predmet>> profesoriSaPredmetima = new HashMap<Profesor, List<Predmet>>(); 
				for (int j = 0; j < Predmet.MIN_INPUT; j++) {
					Predmet predmet = unosPredmeta(input, profesori, j, Predmet.MIN_INPUT);
					predmeti.add(predmet);
					if (!profesoriSaPredmetima.containsKey(predmet.getNositelj())) {
						profesoriSaPredmetima.put(predmet.getNositelj(), new ArrayList<Predmet>());
					}					
					profesoriSaPredmetima.get(predmet.getNositelj()).add(predmet);
				}				
				
				ispisProfesoraSaPredmetima(profesoriSaPredmetima);				
				
				List<Student> studenti = new ArrayList<Student>();
				for (int j = 0; j < Student.MIN_INPUT; j++) {
					studenti.add(unosStudenta(input, j, Student.MIN_INPUT));
				}
				
				List<Ispit> ispiti = new ArrayList<Ispit>();					
				for (int j = 0; j < Ispit.MIN_INPUT; j++) {
					ispiti.add(unosIspita(input, predmeti, studenti, j, Ispit.MIN_INPUT));
				}
				
				ispisPredmetaSaStudentima(predmeti);
				
				ispisSvihOdlicnihStudenata(ispiti);
				
				ObrazovnaUstanova obrazovnaUstanova = unosObrazovneUstanove(input, predmeti, profesori, studenti, ispiti);
				
				ispisKonacneOcjeneStudenata(input, obrazovnaUstanova, studenti, ispiti);
				
				ispisNajboljegStudentaGodine(obrazovnaUstanova);
				
				if (obrazovnaUstanova instanceof FakultetRacunarstva) {
					ispisStudentaZaRektorovuNagradu(obrazovnaUstanova);
				}
				
				sveuciliste.dodajObrazovnuUstanovu(obrazovnaUstanova);
			}
			
			ispisObrazovnihUstanovaSortiranihPremaBrojuStudenata(sveuciliste);
			
			
			
		}
		catch (Exception e) {
			System.out.println("Ups...dogodila se pogreška prilikom izvođenja programa.");
			log.error("Greška u izvršavanju glavnog programa!", e);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
	
	
	
	
	


	// METODE ZA UNOS OBJEKATA (Profesor, Predmet, Student, Ispit, Obrazovna ustanova)

	/**
	 * Unosi objekt tipa Profesor i kreira ga nakon uspješnog unosa
	 * @param input Scanner
	 * @param i redni broj koji se trenutno unosi
	 * @param ukupnoUnosa maksimalni broj unosa
	 * @return objekt tipa Profesor
	 */
	private static Profesor unosProfesora(Scanner input, Integer i, Integer ukupnoUnosa) {
		
		System.out.printf("Unesite profesora (%s/%s):%n", i+1, ukupnoUnosa);			
		System.out.print("Šifra: ");
		String sifra = input.nextLine();	
		System.out.print("Ime: ");
		String ime = input.nextLine();
		System.out.print("Prezime: ");
		String prezime = input.nextLine();
		System.out.print("Titula: ");
		String titula = input.nextLine();
		
		return new Profesor(sifra, ime, prezime, titula);		 
	}
	
	private static Predmet unosPredmeta(Scanner input, List<Profesor> profesori,  int i, Integer ukupnoUnosa) {
		
		System.out.format("Unesite predmet (%s/%s):%n", i+1, ukupnoUnosa);			
		System.out.print("Šifra: ");
		String sifra = input.nextLine();
		System.out.print("Naziv: ");
		String ime = input.nextLine();		
		System.out.print("Broj ECTS bodova: ");
		Integer ectsBodovi = unosBroja(input);
		
		System.out.println("Odaberite profesora: ");
		for (Profesor profesor : profesori) {
			String imePrezime = String.format("%s %s", profesor.getIme(), profesor.getPrezime());
			ispisZaOdabir(profesori.indexOf(profesor), imePrezime);
		}
		Profesor profesor = profesori.get(odabir(input, profesori.size()) - 1);
		Set<Student> studenti = new HashSet<Student>();
				
		return new Predmet(sifra, ime, ectsBodovi, profesor, studenti);
	}
	
	/**
	 * Unosi objekt tipa Student i kreira ga nakon uspješnog unosa
	 * @param input Scanner	 
	 * @param ukupnoUnosa maksimalni broj unosa
	 * @return objekt tipa Student
	 */
	private static Student unosStudenta(Scanner input, Integer i, Integer ukupnoUnosa) {
		
		System.out.printf("Unesite studenta (%s/%s):\n", i+1, ukupnoUnosa);			
		System.out.print("Ime: ");
		String ime = input.nextLine();
		System.out.print("Prezime: ");
		String prezime = input.nextLine();
		System.out.printf("Datum rođenja (%s): ", DATE_FORMAT);		
		LocalDate datumRodjenja = stringToDate(input, DATE_FORMAT);
		System.out.print("JMBAG: ");
		String jmbag = input.nextLine();
		
		return new Student(ime, prezime, jmbag, datumRodjenja);		 
	}
	
	/**
	 * Unosi objekt tipa Ispit i kreira ga nakon uspješnog unosa
	 * @param input Scanner
	 * @param predmeti polje predmeta za odabir ispita
	 * @param studenti polje studenata za odabir studenta koji pristupa ispitu
	 * @param i redni broj koji se trenutno unosi
	 * @param ukupnoUnosa maksimalni broj unosa
	 * @return objekt tipa Ispit
	 */
	private static Ispit unosIspita(Scanner input, List<Predmet> predmeti, List<Student> studenti, int i, Integer ukupnoUnosa) {
		
		System.out.printf("Unesite ispitni rok (%s/%s):%n", i+1, ukupnoUnosa);			
		
		System.out.println("Odaberite predmet: ");
		for (Predmet predmet : predmeti) {
			String tekstZaIspis = String.format("%s", predmet.getNaziv());
			ispisZaOdabir(predmeti.indexOf(predmet), tekstZaIspis);
		}		
		Predmet predmet = predmeti.get(odabir(input, predmeti.size()) - 1);
		
		System.out.println("Odaberite studenta: ");
		for (Student student : studenti) {
			String imePrezime = String.format("%s %s", student.getIme(), student.getPrezime());
			ispisZaOdabir(studenti.indexOf(student), imePrezime);
		}
		Student student = studenti.get(odabir(input, studenti.size()) - 1);
		predmet.getStudenti().add(student);
		
		System.out.print("Ocjena (1-5): ");
		Integer ocjena = unosOcjene(input);
		System.out.printf("Datum održavanja (%s): ", DATE_TIME_FORMAT);		
		LocalDateTime datumVrijemeIspita = stringToDateTime(input, DATE_TIME_FORMAT);		
		
		
		return new Ispit(predmet, student, ocjena, datumVrijemeIspita);
	}
	
	/**
	 * Zahtjeva odabir obrazovne ustanove
	 * @param input skener
	 * @param predmeti lista predmeta
	 * @param profesori lista profesora
	 * @param studenti lista studenata
	 * @param ispiti lista ispita
	 * @return
	 */
	private static ObrazovnaUstanova unosObrazovneUstanove(Scanner input, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
		
		System.out.println("Odaberite obrazovnu ustanovu za navedene podatke koju želite unijeti: ");
		System.out.println("1. Veleučilište Jave");
		System.out.println("2. Fakultet računarstva");
		
		boolean inputOK = false;
		Integer odabir = 0;
		do {
			odabir = unosBroja(input);
			if (odabir >= 1 && odabir <= 2) {
				inputOK = true;
			}					
		} while (!inputOK);
		
		System.out.print("Unesite naziv ustanove: ");
		String nazivUstanove = input.nextLine();
		
		ObrazovnaUstanova obrazovnaUstanova = null;
		switch (odabir) {
		case 1:
			obrazovnaUstanova = new VeleucilisteJave(nazivUstanove, predmeti, profesori, studenti, ispiti);
			break;
		case 2:
			obrazovnaUstanova = new FakultetRacunarstva(nazivUstanove, predmeti, profesori, studenti, ispiti);
			break;				
		}				
		return obrazovnaUstanova;
	}
	
	// METODE ZA ISPIS IZ MAIN METODE
	
	/**
	 * Ispisuje profesora sa svim predmeatima koje predaje
	 * @param profesoriSaPredmetima 
	 * @param profesori lista profesora
	 * @param predmeti lista predmeta
	 */
	private static void ispisProfesoraSaPredmetima(Map<Profesor, List<Predmet>> profesoriSaPredmetima) {
		System.out.println();
		for (Profesor profesor : profesoriSaPredmetima.keySet()) {
			System.out.printf("Profesor %s %s predaje slijedeće predmete:%n", profesor.getIme(), profesor.getPrezime());
			Integer count = 1;
			for (Predmet predmet : profesoriSaPredmetima.get(profesor)) {
				System.out.printf("%s. %s%n", count, predmet.getNaziv());
				count++;
			}
		}
	}
	
	
	/**
	 * Ispisuje sve predmete i studente koji su upisani u njih
	 * Studenti su sortirani po prezimenu, a nakon toga po imenu
	 * @param predmeti
	 */
	private static void ispisPredmetaSaStudentima(List<Predmet> predmeti) {
		
		Long startTime;
		Long endTime;
		
		startTime = System.currentTimeMillis();
		
		System.out.println();
		for (Predmet predmet : predmeti) {
			if (predmet.getStudenti().size() > 0) {
				System.out.printf("Studenti upisani na predmet %s su:%n", predmet.getNaziv());
				Integer count = 1;
				List<Student> studenti = predmet.getStudenti().stream().collect(Collectors.toList());
				Collections.sort(studenti, new StudentSorter());
				for (Student student : studenti) {
					System.out.printf("%s. %s %s%n", count, student.getPrezime(), student.getIme());
				}
			} else {
				System.out.printf("Nema studenata upisanih na predmet: %s%n", predmet.getNaziv());
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja foreach petlje je: " + (endTime - startTime) + " ms");
		
		startTime = System.currentTimeMillis();
		Stream<Predmet> predmetiBezStud = predmeti.stream().filter(p -> p.getStudenti().isEmpty());		
		Stream<Predmet> predmetiSaStud = predmeti.stream().filter(p -> !p.getStudenti().isEmpty());
		
		predmetiSaStud
		.forEach(p -> System.out.printf("Studenti upisani na predmet %s su:%n%s%n", p.getNaziv(),
				p.getStudenti().stream().map(s -> s.getPrezime() + " " + s.getIme()).collect(Collectors.joining("\n"))
				));
		
		predmetiBezStud
		.forEach(
				p -> System.out.printf("Nema studenata upisanih na predmet %s", p.getNaziv())
				);
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja lambda izraza je: " + (endTime - startTime) + " ms");
		
		
		System.out.println();
		
	}
	
	/**
	 * Ispisuje sve studente koji su na ispiti ostvarili ocjenu izvrstan
	 * @param ispiti lista ispita
	 */
	private static void ispisSvihOdlicnihStudenata(List<Ispit> ispiti) {
		
		Long startTime;
		Long endTime;
		
		startTime = System.currentTimeMillis();
		System.out.println();
		for (Ispit ispit : ispiti) {
			if (ispit.getOcjena() == Ocjena.IZVRSTAN.getOcjena()) {
				System.out.printf("Student %s %s iz predmeta \"%s\" ima ocjenu 5.", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
			}
		}
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja foreach petlje je: " + (endTime - startTime) + " ms");
		
		startTime = System.currentTimeMillis();
		ispiti.stream().filter(i -> i.getOcjena() == Ocjena.IZVRSTAN.getOcjena()).forEach(i -> System.out.printf("Student %s %s iz predmeta \"%s\" ima ocjenu 5.", i.getStudent().getIme(), i.getStudent().getPrezime(), i.getPredmet().getNaziv()));;  
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja lambda izraza je: " + (endTime - startTime) + " ms");
		
	
	}
	
	
	/**
	 * Ispisuje konacne ocjene studenata nakon unosa zavrsnih ocjena
	 * @param input skener
	 * @param obrazovnaUstanova obrazovna ustanova
	 * @param studenti lista studenata
	 * @param ispiti lista ispita
	 */
	private static void ispisKonacneOcjeneStudenata(Scanner input, ObrazovnaUstanova obrazovnaUstanova, List<Student> studenti, List<Ispit> ispiti) {
		
		for (Student student : studenti) {
			
			HashMap<String, Integer> zavrsneOcjene;
			BigDecimal konacnaOcjena = new BigDecimal(0);
			if (obrazovnaUstanova instanceof VeleucilisteJave) {	
				VeleucilisteJave ustanova = (VeleucilisteJave)obrazovnaUstanova;
				List<Ispit> ispitiStudenta = ustanova.filtrirajIspitePoStudentu(ispiti, student);
				List<Ispit> polozeniIspiti = ustanova.filtrirajPolozeneIspite(ispitiStudenta);
				if (ispitiStudenta.size() == polozeniIspiti.size()) {
					zavrsneOcjene = unosZavrsnihOcjena(input, student);
					konacnaOcjena = ustanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
				} else {
					System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
				}
			} else if (obrazovnaUstanova instanceof FakultetRacunarstva) {
				FakultetRacunarstva ustanova = (FakultetRacunarstva)obrazovnaUstanova;
				List<Ispit> ispitiStudenta = ustanova.filtrirajIspitePoStudentu(ispiti, student);
				List<Ispit> polozeniIspiti = ustanova.filtrirajPolozeneIspite(ispitiStudenta);
				if (ispitiStudenta.size() == polozeniIspiti.size()) {
					zavrsneOcjene = unosZavrsnihOcjena(input, student);
					konacnaOcjena = ustanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
				} else {
					System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
				}						
			}					
			System.out.printf("Konačna ocjena studija studenta %s %s je %s%n", student.getIme(), student.getPrezime(), konacnaOcjena);
		}
		
	}
	
	/**
	 * Pokrece unos zavrsnih ocjena
	 * @param input Scanner
	 * @param student objekt tipa Student 
	 * @return ocjenaRada i ocjenaObraneRada u HashMap-u
	 * @see Student
	 */
	private static HashMap<String, Integer> unosZavrsnihOcjena(Scanner input, Student student) {
		
		HashMap<String, Integer> zavrsneOcjene = new HashMap<String, Integer>();
		
		System.out.printf("Unesite ocjenu završnog rada za studenta: %s %s: ", student.getIme(), student.getPrezime());
		zavrsneOcjene.put("ocjenaRada", unosOcjene(input));
		
		System.out.printf("Unesite ocjenu obrane završnog rada za studenta: %s %s: ", student.getIme(), student.getPrezime());
		zavrsneOcjene.put("ocjenaObraneRada", unosOcjene(input));
		
		return zavrsneOcjene;
	}


	/**
	 * Ispisuje najboljeg studenta na godini
	 * @param obrazovnaUstanova obrazovna ustanova
	 */
	private static void ispisNajboljegStudentaGodine(ObrazovnaUstanova obrazovnaUstanova) {
		
		Integer godina = LocalDate.now().getYear();
		Student najboljiStudent = obrazovnaUstanova.odrediNajuspjesnijegStudentaNaGodini(godina);
		System.out.printf("Najbolji student %s. godine je %s %s JMBAG: %s%n",godina, najboljiStudent.getIme(), najboljiStudent.getPrezime(), najboljiStudent.getJmbag());;
				
	}

	/**
	 * Ispisuje studenta za dodjelu rektorove nagrade
	 * @param ustanova obrazovna ustanova
	 */
	private static void ispisStudentaZaRektorovuNagradu(ObrazovnaUstanova ustanova) {
		Student studentZaNagradu;
		try {
			studentZaNagradu = ((FakultetRacunarstva)ustanova).odrediStudentaZaRektorovuNagradu();
			System.out.printf("Student koji je osvojio rektorovu nagradu je: %s %s JMBAG: %s%n", studentZaNagradu.getIme(), studentZaNagradu.getPrezime(), studentZaNagradu.getJmbag());
		} catch (PostojiViseNajmladjihStudenataException ex) {
			System.out.println("Postoji više najmlađih studenata!");
			log.error(ex.getMessage(), ex);
			System.exit(0);
		}
	}
	
	
	private static void ispisObrazovnihUstanovaSortiranihPremaBrojuStudenata(
			Sveuciliste<ObrazovnaUstanova> sveuciliste) {		
		
//		List<ObrazovnaUstanova> ustanove = sveuciliste.getSveucilista();		
//		
//		ustanove.stream().sorted(Comparator.comparing(u -> countStudents(ObrazovnaUstanova::getStudenti)))			
//			.forEach(o -> System.out.println(o.getNazivUstanove() + " - " + o.getStudenti().size()));
		
	}

	private Integer countStudents(ObrazovnaUstanova ustanova) {
		return ustanova.getStudenti().size();
	}

	
	// POMOĆNE METODE - VALIDACIJE I SL.
	
	/**
	 * Postavlja broj željenih unosa određenog objekta
	 * @param input Scanner
	 * @param nazivUnosa naziv objekta za koji se traži broj unosa
	 * @param minimum minimalna dozvoljena količina
	 * @return broj željenih unosa
	 */
	private static Integer odaberiBrojUnosa(Scanner input, String nazivUnosa, Integer minimum) {	
		
		Integer brojUnosa = 0;		
		do {
			System.out.printf("Unesite broj %s koji želite unijeti (min. %s): ", nazivUnosa, minimum);
			brojUnosa = unosBroja(input);
			if (brojUnosa < minimum) {
				System.out.format("Minimalni broj %sa za unos je %s! Pokušajte ponovno.%n", nazivUnosa, minimum);					
			}			
		} while (brojUnosa < minimum);
		
		return brojUnosa;
	}
	
	/**
	 * Validira unos broja prilikom korištenja Scanner naredbe nextInt()
	 * @param input
	 * @return uneseni broj
	 */
	private static Integer unosBroja(Scanner input) {
		
		Integer inputNumber = null;
		boolean inputSuccess = false;
		do {
			try {
				inputNumber = input.nextInt();
				inputSuccess = true;
			} catch (InputMismatchException ex ) {
				System.out.println("Moguće je unijeti samo brojeve. Pokušajte ponovno.");
				log.error("Pogrešan unos broja!", ex);
			} finally {
				input.nextLine();
			}			
		} while (!inputSuccess);
		
		return inputNumber;
	}
	
	/**
	 * Ispisuje u konzolu redni broj i poslani tekst
	 * Koristi se za ispis prilikom odabira objekta
	 * @param redniBroj
	 * @param tekstZaIspis
	 */
	private static void ispisZaOdabir(Integer redniBroj, String tekstZaIspis) {
		System.out.printf("%s. %s%n", redniBroj + 1, tekstZaIspis);		
	}
	
	/**
	 * Validira odabir nakon ispisane liste ponuđenih mogućnosti
	 * @param input
	 * @param maximum maksimalni broj koji je moguće odabrati
	 * @return
	 */
	private static Integer odabir(Scanner input, Integer maximum) {
		
		Integer odabir = 0;
		do {
			System.out.format("Odabir: ");
			odabir = unosBroja(input);
			if (odabir < 1 || odabir > maximum) {
				System.out.println("Pogrešan odabir. Pokušajte ponovno!");					
			}			
		} while (odabir < 1 || odabir > maximum);
		
		return odabir;
	}
	
	/**
	 * Pretvara uneseni text u LocalDate format
	 * @param input
	 * @param dateFormat očekivani format datuma
	 * @return datum
	 */
	private static LocalDate stringToDate(Scanner input, String dateFormat) {
		
		LocalDate date = null;
		Boolean conversionSuccess = false;
		do {
			String inputData = input.nextLine();			
			try {
				date = LocalDate.parse(inputData, DateTimeFormatter.ofPattern(dateFormat));				
				conversionSuccess = true;
			} catch (DateTimeParseException ex) {
				System.out.println("Neispravan format datuma. Pokušajte ponovno.");
				log.error("Pogreška u pretvorbi datuma!", ex);
			} 
		} while (!conversionSuccess);
		
		return date;
	}
	
	/**
	 * Pretvara uneseni text u LocalDateTime format
	 * @param input
	 * @param dateFormat očekivani format datuma i vremena
	 * @return datum i vrijeme
	 */
	private static LocalDateTime stringToDateTime(Scanner input, String dateFormat) {
		
		LocalDateTime date = null;
		Boolean conversionSuccess = false;
		do {
			String inputData = input.nextLine();
			try {
				date = LocalDateTime.parse(inputData, DateTimeFormatter.ofPattern(dateFormat));
				long daysBetween = ChronoUnit.DAYS.between(date, LocalDateTime.now());
				if (daysBetween > 14) {
					throw new NeispravanDatumException("Datum ne smije biti veći od dva tjedna u prošlosti. Pokušajte ponovno.");
				}
				conversionSuccess = true;
			} catch (DateTimeParseException ex) {
				System.out.println("Neispravan format datuma. Pokušajte ponovno.");
				log.error("Pogreška u pretvorbi datuma i vremena!", ex);
			} catch (NeispravanDatumException ex) {
				System.out.println(ex.getMessage());
				log.error(ex.getMessage(), ex);
			}
		} while (!conversionSuccess);
		
		return date;
	}
	
	/**
	 * Validira unos ocjene
	 * Ocjena je ispravna ukoliko je u rasponu 1 - 5
	 * @param input
	 * @return unesena ocjena
	 */
	private static Integer unosOcjene(Scanner input) {
		
		Integer ocjena = 0;
		boolean inputSuccess = true;
		
		do {
			ocjena = unosBroja(input);			
			if (ocjena < Ocjena.NEDOVOLJAN.getOcjena() || ocjena > Ocjena.IZVRSTAN.getOcjena()) {
				System.out.println("Ocjena mora biti između 1 i 5. Pokušajte ponovno.");
				inputSuccess = false;
			}
		} while (!inputSuccess);
		
		return ocjena;
	}
}
