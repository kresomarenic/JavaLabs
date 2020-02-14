package hr.java.vjezbe.glavna;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.NeispravanDatumException;
import hr.java.vjezbe.iznimke.NeispravnoPrezimeException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;

/**
 * Pokrece program
 * @author kmarenic
 *
 */
public class Glavna {
	
	private final static Logger log = LoggerFactory.getLogger(Glavna.class);
	
	private final static String DATE_FORMAT = "dd.MM.yyyy.";
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	

	/**
	 * Pokrece program
	 * @param args set ulaznih parametara za pocetak izvodjenja programa
	 */
	public static void main(String[] args) {		
			
		Scanner input = null;
		Integer brojUnosa = 0;	
		
		try {
			input = new Scanner(System.in);
			
			brojUnosa = odaberiBrojUnosa(input, ObrazovnaUstanova.INPUT_NAME, ObrazovnaUstanova.MIN_INPUT);
			ObrazovnaUstanova[] obrazovneUstanove = new ObrazovnaUstanova[brojUnosa];
			
			for (int i = 0; i < obrazovneUstanove.length; i++) {
				
				System.out.printf("Unesite podatke za obrazovnu ustanovu (%s/%s): %n", i+1, obrazovneUstanove.length);
								
				brojUnosa = odaberiBrojUnosa(input, Profesor.INPUT_NAME, Profesor.MIN_INPUT);
				Profesor[] profesori = new Profesor[brojUnosa];						
				for (int j = 0; j < brojUnosa; j++) {
					profesori[j] = unosProfesora(input, j, brojUnosa);
				}
							
				brojUnosa = odaberiBrojUnosa(input, Predmet.INPUT_NAME, Predmet.MIN_INPUT);
				Predmet[] predmeti = new Predmet[brojUnosa];						
				for (int j = 0; j < brojUnosa; j++) {
					predmeti[j] = unosPredmeta(input, profesori, j, brojUnosa);
				}
				

				brojUnosa = odaberiBrojUnosa(input, Student.INPUT_NAME, Student.MIN_INPUT);
				Student[] studenti = new Student[brojUnosa];						
				for (int j = 0; j < brojUnosa; j++) {
					studenti[j] = unosStudenta(input, j, brojUnosa);
				}
				
				
				brojUnosa = odaberiBrojUnosa(input, Ispit.INPUT_NAME, Ispit.MIN_INPUT);
				Ispit[] ispiti = new Ispit[brojUnosa];						
				for (int j = 0; j < brojUnosa; j++) {
					ispiti[j] = unosIspita(input, predmeti, studenti, j, brojUnosa);
				}
//				Ispit[] ispiti = new Ispit[0];
//				Integer counter = 1;
//				Boolean nastaviUnos = true;
//				do {
//					Ispit ispit = unosIspita(input, predmeti, studenti, counter);
//					if (ispit == null) {
//						nastaviUnos = false;
//						break;
//					}
//					Arrays.copyOf(ispiti, counter);
//					counter++;
//				} while (nastaviUnos);
				
				
				Arrays.sort(ispiti, new Ispit());
				System.out.println(Arrays.toString(ispiti));
				 
				double[] ocjene = new double[ispiti.length];
				for (Ispit ispit : ispiti) {
					Integer j = 0;
					ocjene[0] = ispit.getOcjena();
					j++;
					
					if (ispit.getOcjena() == Ispit.OCJENA_ODLICAN) {
						System.out.printf("%nStudent %s %s iz predmeta \"%s\" ima ocjenu 5.", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
					}
				}
				
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
				
				switch (odabir) {
				case 1:
					obrazovneUstanove[i] = new VeleucilisteJave(nazivUstanove, predmeti, profesori, studenti, ispiti);
					break;
				case 2:
					obrazovneUstanove[i] = new FakultetRacunarstva(nazivUstanove, predmeti, profesori, studenti, ispiti);
					break;				
				}				
				
				for (Student student : studenti) {
					
					HashMap<String, Integer> zavrsneOcjene;
					BigDecimal konacnaOcjena = new BigDecimal(0);
					if (obrazovneUstanove[i] instanceof VeleucilisteJave) {	
						VeleucilisteJave obrazovnaUstanova = (VeleucilisteJave)obrazovneUstanove[i];
						Ispit[] ispitiStudenta = obrazovnaUstanova.filtrirajIspitePoStudentu(ispiti, student);
						Ispit[] polozeniIspiti = obrazovnaUstanova.filtrirajPolozeneIspite(ispitiStudenta);
						if (ispitiStudenta.length == polozeniIspiti.length) {
							zavrsneOcjene = unosZavrsnihOcjena(input, student);
							konacnaOcjena = obrazovnaUstanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
						} else {
							System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
						}
					} else if (obrazovneUstanove[i] instanceof FakultetRacunarstva) {
						FakultetRacunarstva obrazovnaUstanova = (FakultetRacunarstva)obrazovneUstanove[i];
						Ispit[] ispitiStudenta = obrazovnaUstanova.filtrirajIspitePoStudentu(ispiti, student);
						Ispit[] polozeniIspiti = obrazovnaUstanova.filtrirajPolozeneIspite(ispitiStudenta);
						if (ispitiStudenta.length == polozeniIspiti.length) {
							zavrsneOcjene = unosZavrsnihOcjena(input, student);
							konacnaOcjena = obrazovnaUstanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
						} else {
							System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
						}						
					}					
					System.out.printf("Konačna ocjena studija studenta %s %s je %s%n", student.getIme(), student.getPrezime(), konacnaOcjena);
				}
				
				Integer godina = 2018;
				Student najboljiStudent = obrazovneUstanove[i].odrediNajuspjesnijegStudentaNaGodini(godina);
				System.out.printf("Najbolji student %s. godine je %s %s JMBAG: %s%n",godina, najboljiStudent.getIme(), najboljiStudent.getPrezime(), najboljiStudent.getJmbag());;
				
				
				if (obrazovneUstanove[i] instanceof FakultetRacunarstva) {
					Student studentZaNagradu;
					try {
						studentZaNagradu = ((FakultetRacunarstva)obrazovneUstanove[i]).odrediStudentaZaRektorovuNagradu();
						System.out.printf("Student koji je osvojio rektorovu nagradu je: %s %s JMBAG: %s%n", studentZaNagradu.getIme(), studentZaNagradu.getPrezime(), studentZaNagradu.getJmbag());
					} catch (PostojiViseNajmladjihStudenataException ex) {
						System.out.println("Postoji više najmlađih studenata!");
						log.error(ex.getMessage(), ex);
						System.exit(0);
					}					
				}
			}
			
		} catch (Exception e) {
			System.out.println("Ups...dogodila se pogreška prilikom izvođenja programa.");
			log.error("Greška u izvršavanju glavnog programa!", e);
		} finally {
			if (input != null) {
				input.close();
			}
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
		System.out.print("OIB: ");
		String oib = input.nextLine();
		System.out.print("JMBG: ");
		String jmbg = input.nextLine();
		System.out.print("Titula iza imena: ");
		String titulaIzaImena = input.nextLine();
		
		return new Profesor(sifra, ime, prezime, titula, oib, jmbg, titulaIzaImena);		 
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
		String prezime = unosPrezimena(input);;		
		System.out.print("JMBAG: ");
		String jmbag = input.nextLine();		
		System.out.printf("Datum rođenja (%s): ", DATE_FORMAT);		
		LocalDate datumRodjenja = stringToDate(input, DATE_FORMAT);
		
		return new Student(ime, prezime, jmbag, datumRodjenja);		 
	}

	/**
	 * Unosi objekt tipa Predmet i kreira ga nakon uspješnog unosa
	 * @param input Scanner
	 * @param profesori polje profesora za odabir nositelja predmeta
	 * @param i redni broj koji se trenutno unosi
	 * @param ukupnoUnosa maksimalni broj unosa
	 * @return objekt tipa Predmet
	 */
	private static Predmet unosPredmeta(Scanner input, Profesor[] profesori, int i, Integer ukupnoUnosa) {
		
		System.out.format("Unesite predmet (%s/%s):%n", i+1, ukupnoUnosa);			
		System.out.print("Šifra: ");
		String sifra = input.nextLine();
		System.out.print("Naziv: ");
		String ime = input.nextLine();		
		System.out.print("Broj ECTS bodova: ");
		Integer ectsBodovi = unosBroja(input);
		
		System.out.println("Odaberite nositelja: ");		
		for (int j = 0; j < profesori.length; j++) {
			String imePrezime = String.format("%s %s", profesori[j].getIme(), profesori[j].getPrezime());
			ispisZaOdabir(j, imePrezime);
		}
		Profesor profesor = profesori[odabir(input, profesori.length) - 1];
		
		System.out.print("Unesite broj studenata koji pohađaju predmet: ");
		Integer brojStudenata = unosBroja(input);
		
		return new Predmet(sifra, ime, ectsBodovi, profesor, brojStudenata);
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
	private static Ispit unosIspita(Scanner input, Predmet[] predmeti, Student[] studenti, int i, Integer ukupnoUnosa) {
		
		System.out.printf("Unesite ispit (%s/%s):%n", i+1, ukupnoUnosa);			
		
		System.out.println("Odaberite predmet: ");		
		for (int j = 0; j < predmeti.length; j++) {
			String tekstZaIspis = String.format("%s", predmeti[j].getNaziv());
			ispisZaOdabir(j, tekstZaIspis);
		}
		Predmet predmet = predmeti[odabir(input, predmeti.length) - 1];
		
		System.out.println("Odaberite studenta: ");		
		for (int j = 0; j < studenti.length; j++) {
			String imePrezime = String.format("%s %s", studenti[j].getIme(), studenti[j].getPrezime());
			ispisZaOdabir(j, imePrezime);
		}
		Student student = studenti[odabir(input, studenti.length) - 1];
		
		System.out.printf("Datum održavanja (%s): ", DATE_TIME_FORMAT);		
		LocalDateTime datumVrijemeIspita = stringToDateTime(input, DATE_TIME_FORMAT);		
		System.out.print("Ocjena (1-5): ");
		Integer ocjena = unosOcjene(input);
		
		return new Ispit(predmet, student, ocjena, datumVrijemeIspita);
	}
	
	/**
	 * Unosi objekt tipa Ispit i kreira ga nakon uspješnog unosa
	 * @param input Scanner
	 * @param predmeti polje predmeta za odabir ispita
	 * @param studenti polje studenata za odabir studenta koji pristupa ispitu
	 * @param i redni broj koji se trenutno unosi
	 * @return objekt tipa Ispit
	 */
	private static Ispit unosIspita(Scanner input, Predmet[] predmeti, Student[] studenti, int i) {
		
		System.out.printf("Unesite ispit (%s.):%n", i);			
		
		System.out.println("Odaberite predmet: ");		
		for (int j = 0; j < predmeti.length; j++) {
			String tekstZaIspis = String.format("%s", predmeti[j].getNaziv());
			ispisZaOdabir(j, tekstZaIspis);
		}		
		Predmet predmet = predmeti[odabir(input, predmeti.length) - 1];
		if (predmet == null) {
			return null;
		}		
		
		System.out.println("Odaberite studenta: ");		
		for (int j = 0; j < studenti.length; j++) {
			String imePrezime = String.format("%s %s", studenti[j].getIme(), studenti[j].getPrezime());
			ispisZaOdabir(j, imePrezime);
		}
		Student student = studenti[odabir(input, studenti.length) - 1];
		
		System.out.printf("Datum održavanja (%s): ", DATE_TIME_FORMAT);		
		LocalDateTime datumVrijemeIspita = stringToDateTime(input, DATE_TIME_FORMAT);		
		System.out.print("Ocjena (1-5): ");
		Integer ocjena = unosOcjene(input);
		
		return new Ispit(predmet, student, ocjena, datumVrijemeIspita);
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
			if (ocjena < 1 || ocjena > 5) {
				System.out.println("Ocjena mora biti između 1 i 5. Pokušajte ponovno.");
				inputSuccess = false;
			}
		} while (!inputSuccess);
		
		return ocjena;
	}
	
	/**
	 * Validira unos prezimena
	 * Prezime moze sadrzavati samo velika slova, mala slova i razmak
	 * @param input
	 * @return uneseno prezime
	 */
	private static String unosPrezimena(Scanner input) {
		
		String prezime = "";
		boolean inputSuccess = false;
		
		do {
			prezime = input.nextLine();			
			try {				
				if (!prezime.matches("[A-Za-z ]+")) {					
					throw new NeispravnoPrezimeException("Prezime može sadržavati samo velika slova, mala slova i razmak. Pokušajte ponovno");
				}
				inputSuccess = true;
			} catch (NeispravnoPrezimeException ex) {
				System.out.println(ex.getMessage());
				log.error(ex.getMessage(), ex);					
			}
		} while (!inputSuccess);
		
		return prezime;
	}
	
	
	
	
	
	

}
