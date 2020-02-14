package hr.java.vjezbe.glavna;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;

public class Glavna {
	
	private final static String DATE_FORMAT = "dd.MM.yyyy.";
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	

	public static void main(String[] args) {		
			
		Scanner input = null;
		Integer brojUnosa = 0;	
		
		try {
			input = new Scanner(System.in);
			
			brojUnosa = odaberiBrojUnosa(input, Profesor.NAME, Profesor.MIN_INPUT);
			Profesor[] profesori = new Profesor[brojUnosa];						
			for (int i = 0; i < brojUnosa; i++) {
				profesori[i] = unosProfesora(input, i, brojUnosa);
			}
						
			brojUnosa = odaberiBrojUnosa(input, Predmet.NAME, Predmet.MIN_INPUT);
			Predmet[] predmeti = new Predmet[brojUnosa];						
			for (int i = 0; i < brojUnosa; i++) {
				predmeti[i] = unosPredmeta(input, profesori, i, brojUnosa);
			}
			
			brojUnosa = odaberiBrojUnosa(input, Student.NAME, Student.MIN_INPUT);
			Student[] studenti = new Student[brojUnosa];						
			for (int i = 0; i < brojUnosa; i++) {
				studenti[i] = unosStudenta(input, i, brojUnosa);
			}
			
			brojUnosa = odaberiBrojUnosa(input, Ispit.NAME, Ispit.MIN_INPUT);
			Ispit[] ispiti = new Ispit[brojUnosa];						
			for (int i = 0; i < brojUnosa; i++) {
				ispiti[i] = unosIspita(input, predmeti, studenti, i, brojUnosa);
			}
			
			double[] ocjene = new double[ispiti.length];
			for (Ispit ispit : ispiti) {
				Integer i = 0;
				ocjene[0] = ispit.getOcjena();
				i++;
				
				if (ispit.getOcjena() == Ispit.OCJENA_ODLICAN) {
					System.out.printf("%nStudent %s %s iz predmeta \"%s\" ima ocjenu 5.", ispit.getStudent().getIme(), ispit.getStudent().getPrezime(), ispit.getPredmet().getNaziv());
				}
			}			
			
			for (Profesor profesor : profesori) {
				System.out.println(profesor.toString());
				System.out.println("Datum rođenja profesora: " + datumRodnjenjaFromJMBG(profesor.getJmbg()));
			}
			
			System.out.println("Standardna devijacija ocjena je: " + calculateStandardDeviation(ocjene));
			
		} catch (Exception e) {
			System.out.println("Ups...dogodila se pogreška prilikom izvođenja programa.");	
		} finally {
			if (input != null) {
				input.close();
			}
		}

	}

	private static String datumRodnjenjaFromJMBG(String jmbg) {
		Integer dan = Integer.parseInt(jmbg.substring(0, 2));
		Integer mjesec = Integer.parseInt(jmbg.substring(2, 4));
		String godina = jmbg.substring(4, 7);
		
		if (godina.substring(0, godina.length() - 2).equals("0")) {
			godina = "2" + godina;
		}
		else {
			godina = "1" + godina;
		}
		
		LocalDate datumRodjenja = LocalDate.of(Integer.parseInt(godina), mjesec, dan);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String datumRodjenjaString = datumRodjenja.format(formatter);
		
		
		
		return datumRodjenjaString;
	}

	private static Integer odaberiBrojUnosa(Scanner input, String nazivUnosa, Integer minimum) {	
		
		Integer brojUnosa = 0;		
		do {
			System.out.printf("Unesite broj %sa koji želite unijeti (min. %s): ", nazivUnosa, minimum);
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
	
	private static double calculateStandardDeviation(double numArray[]) {
		
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

}
