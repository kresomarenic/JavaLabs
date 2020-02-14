package hr.java.vjezbe.glavna;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import hr.java.vjezbe.entitet.FakultetRacunarstva;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.ObrazovnaUstanova;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.VeleucilisteJave;

public class Glavna {
	
	private final static String DATE_FORMAT = "dd.MM.yyyy.";
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	

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
				
				
//				brojUnosa = odaberiBrojUnosa(input, Ispit.INPUT_NAME, Ispit.MIN_INPUT);
//				Ispit[] ispiti = new Ispit[brojUnosa];						
//				for (int j = 0; j < brojUnosa; j++) {
//					ispiti[j] = unosIspita(input, predmeti, studenti, j, brojUnosa);
//				}
				Ispit[] ispiti = new Ispit[0];
				Integer counter = 1;
				Boolean nastaviUnos = true;
				do {
					Ispit ispit = unosIspita(input, predmeti, studenti, counter);
					if (ispit == null) {
						nastaviUnos = false;
						break;
					}
					Arrays.copyOf(ispiti, counter);
					counter++;
				} while (nastaviUnos);
				
				
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
				
//				for (Profesor profesor : profesori) {
//					System.out.println(profesor.toString());
//					System.out.println("Datum rođenja profesora: " + datumRodnjenjaFromJMBG(profesor.getJmbg()));
//				}
				
//				System.out.println("Standardna devijacija ocjena je: " + calculateStandardDeviation(ocjene));
				System.out.println();
				
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
					System.out.printf("Unesite ocjenu završnog rada za studenta: %s %s: ", student.getIme(), student.getPrezime());
					Integer ocjenaRada = unosOcjene(input);
					System.out.printf("Unesite ocjenu obrane završnog rada za studenta: %s %s: ", student.getIme(), student.getPrezime());
					Integer ocjenaObraneRada = unosOcjene(input);
					
					BigDecimal konacnaOcjena = new BigDecimal(0);
					if (obrazovneUstanove[i] instanceof VeleucilisteJave) {						
						Ispit[] ispitiStudenta = ((VeleucilisteJave)obrazovneUstanove[i]).filtrirajIspitePoStudentu(ispiti, student);
						konacnaOcjena = ((VeleucilisteJave)obrazovneUstanove[i]).izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaRada, ocjenaObraneRada);
					} else if (obrazovneUstanove[i] instanceof FakultetRacunarstva) {
						Ispit[] ispitiStudenta = ((FakultetRacunarstva)obrazovneUstanove[i]).filtrirajIspitePoStudentu(ispiti, student);
						konacnaOcjena = ((FakultetRacunarstva)obrazovneUstanove[i]).izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaRada, ocjenaObraneRada);
					}					
					System.out.printf("Konačna ocjena studija studenta %s %s je %s%n", student.getIme(), student.getPrezime(), konacnaOcjena);
				}
				
				Integer godina = 2018;
				Student najboljiStudent = obrazovneUstanove[i].odrediNajuspjesnijegStudentaNaGodini(godina);
				System.out.printf("Najbolji student %s. godine je %s %s JMBAG: %s%n",godina, najboljiStudent.getIme(), najboljiStudent.getPrezime(), najboljiStudent.getJmbag());;
				
				
				if (obrazovneUstanove[i] instanceof FakultetRacunarstva) {					
					Student studentZaNagradu = ((FakultetRacunarstva)obrazovneUstanove[i]).odrediStudentaZaRektorovuNagradu();
					System.out.printf("Student koji je osvojio rektorovu nagradu je: %s %s JMBAG: %s%n", studentZaNagradu.getIme(), studentZaNagradu.getPrezime(), studentZaNagradu.getJmbag());
				}
			}
			
		} catch (Exception e) {
			System.out.println("Ups...dogodila se pogreška prilikom izvođenja programa.");	
		} finally {
			if (input != null) {
				input.close();
			}
		}

	}

	

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
	
	private static Student unosStudenta(Scanner input, Integer i, Integer ukupnoUnosa) {
		
		System.out.printf("Unesite studenta (%s/%s):\n", i+1, ukupnoUnosa);			
		System.out.print("Ime: ");
		String ime = input.nextLine();
		System.out.print("Prezime: ");
		String prezime = input.nextLine();		
		System.out.print("JMBAG: ");
		String jmbag = input.nextLine();		
		System.out.printf("Datum rođenja (%s): ", DATE_FORMAT);		
		LocalDate datumRodjenja = stringToDate(input, DATE_FORMAT);
		
		return new Student(ime, prezime, jmbag, datumRodjenja);		 
	}

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
	
	private static void ispisZaOdabir(Integer redniBroj, String tekstZaIspis) {
		System.out.printf("%s. %s%n", redniBroj + 1, tekstZaIspis);		
	}
	
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
	
	private static Integer unosBroja(Scanner input) {
		
		Integer inputNumber = null;
		boolean inputSuccess = false;
		do {
			try {
				inputNumber = input.nextInt();
				inputSuccess = true;
			} catch (InputMismatchException ex ) {
				System.out.println("Moguće je unijeti samo brojeve. Pokušajte ponovno.");
			} finally {
				input.nextLine();
			}			
		} while (!inputSuccess);
		
		return inputNumber;
	}
	
	private static LocalDate stringToDate(Scanner input, String dateFormat) {
		
		LocalDate date = null;
		Boolean conversionSuccess = false;
		do {
			String inputData = input.nextLine();
			try {
				date = LocalDate.parse(inputData, DateTimeFormatter.ofPattern(dateFormat));
				conversionSuccess = true;
			} catch (Exception e) {
				System.out.println("Neispravan format datuma. Pokušajte ponovno.");
			}
		} while (!conversionSuccess);
		
		return date;
	}
	
	private static LocalDateTime stringToDateTime(Scanner input, String dateFormat) {
		
		LocalDateTime date = null;
		Boolean conversionSuccess = false;
		do {
			String inputData = input.nextLine();
			try {
				date = LocalDateTime.parse(inputData, DateTimeFormatter.ofPattern(dateFormat));
				conversionSuccess = true;
			} catch (Exception e) {
				System.out.println("Neispravan format datuma. Pokušajte ponovno.");
			}
		} while (!conversionSuccess);
		
		return date;
	}
	
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
	
}
