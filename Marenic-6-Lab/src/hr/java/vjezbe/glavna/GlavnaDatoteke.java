package hr.java.vjezbe.glavna;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import hr.java.vjezbe.entitet.SrednjaSkola;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.entitet.VeleucilisteJave;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.sortiranje.ObrazovnaUstanovaSorter;

public class GlavnaDatoteke {
	
	private final static Logger log = LoggerFactory.getLogger(Glavna.class);
	
	private static final String DATA_LOCATION = "dat/";
	
	private final static String DATE_FORMAT = "dd.MM.yyyy.";
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";
	
	private static List<Profesor> profesori;
	private static List<Student> studenti;
	private static List<SrednjaSkola> srednjeSkole;
	private static List<Predmet> predmeti;
	private static List<Ispit> ispiti;
	private static List<ObrazovnaUstanova> obrUstanove;
	
	public static void main(String[] args) {
		
		try (Scanner input = new Scanner(System.in)) {
			
			System.out.println("Učitavanje srednjih skola...");
			srednjeSkole = ucitajSrednjeSkole(SrednjaSkola.INPUT_FILE_NAME, SrednjaSkola.NBR_OF_LINES_PER_RECORD);
			
			System.out.println("Učitavanje profesora...");
			profesori = ucitajProfesore(Profesor.INPUT_FILE_NAME, Profesor.NBR_OF_LINES_PER_RECORD);
			
			System.out.println("Učitavanje studenata...");
			studenti = ucitajStudente(Student.INPUT_FILE_NAME, Student.NBR_OF_LINES_PER_RECORD);			
			
			System.out.println("Učitavanje predmeta...");
			predmeti = ucitajPredmete(Predmet.INPUT_FILE_NAME, Predmet.NBR_OF_LINES_PER_RECORD);			
			
			System.out.println("Učitavanje ispita...");
			ispiti = ucitajIspite(Ispit.INPUT_FILE_NAME, Ispit.NBR_OF_LINES_PER_RECORD);
			
			System.out.println("Učitavanje obrazovnih ustanova...");
			obrUstanove = ucitajObrUstanove(ObrazovnaUstanova.INPUT_FILE_NAME, ObrazovnaUstanova.NBR_OF_LINES_PER_RECORD);
			
//			for (ObrazovnaUstanova obrUstanova : obrUstanove) {
//				
//				ispisProfesoraSaPredmetima(obrUstanova.getPredmeti());				
//				ispisPredmetaSaStudentima(obrUstanova.getPredmeti());				
//				ispisSvihOdlicnihStudenata(obrUstanova.getIspiti());				
//				ispisKonacneOcjeneStudenata(input, obrUstanova);
//				
//				ispisNajboljegStudentaGodine(obrUstanova);
//				if (obrUstanova instanceof FakultetRacunarstva) {
//					ispisStudentaZaRektorovuNagradu(obrUstanova);
//				}				
//				
//				System.out.println();
//			}
//			
//			ispisObrazovnihUstanova();
			
			ispisSrednjeSkole();
			
		} catch (Exception e) {
			System.out.println("Ups...dogodila se pogreška prilikom izvođenja programa.");
			e.printStackTrace();
			log.error("Greška u izvršavanju glavnog programa!", e);
		}
		
	}


	private static void ispisSrednjeSkole() {
		
		Map<SrednjaSkola, Long> counters = studenti.stream()
			     .collect(Collectors.groupingBy(Student::getSrednjaSkola, Collectors.counting()));
		
		
		System.out.println("Škola koju je završilo najviše studenta je: " + counters.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey().getNaziv());
	

	}


	private static List<Profesor> ucitajProfesore(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<Profesor> profesori = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));
				String sifra = inputData.get(i + 1);
				String ime = inputData.get(i + 2);
				String prezime = inputData.get(i + 3);
				String titula = inputData.get(i + 4);
				profesori.add(new Profesor(id, sifra, ime, prezime, titula));
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return profesori;
	}
	
	private static List<Student> ucitajStudente(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<Student> studenti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));				
				String ime = inputData.get(i + 1);
				String prezime = inputData.get(i + 2);
				LocalDate datumRodjenja = LocalDate.parse(inputData.get(i + 3), DateTimeFormatter.ofPattern(DATE_FORMAT));
				String jmbag = inputData.get(i + 4);				
				Long idSrednjeSkole = Long.parseLong(inputData.get(i + 5));				
				SrednjaSkola srednjaSkola = srednjeSkole.stream().filter(p -> p.getId() == idSrednjeSkole).findAny().orElse(null);				
				
				studenti.add(new Student(id, ime, prezime, jmbag, datumRodjenja, srednjaSkola));
				
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return studenti;
	}
	
	private static List<SrednjaSkola> ucitajSrednjeSkole(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<SrednjaSkola> srednjeSkole = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));				
				String naziv = inputData.get(i + 1);
				srednjeSkole.add(new SrednjaSkola(id, naziv));				
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return srednjeSkole;
	}
	
	private static List<Predmet> ucitajPredmete(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<Predmet> predmeti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));
				String sifra = inputData.get(i + 1);
				String naziv = inputData.get(i + 2);
				Integer brojECTSBodova = Integer.parseInt(inputData.get(i + 3));
				Long idNositelja = Long.parseLong(inputData.get(i + 4));
				Profesor nositelj = profesori.stream().filter(p -> p.getId() == idNositelja).findAny().orElse(null);
				List<String> idStudenata = Arrays.asList(inputData.get(i + 5).split(" "));
				Set<Student> studentiNaPredmetu = new HashSet<>();
				for (String idStudent : idStudenata) {
					studentiNaPredmetu.add(studenti.stream().filter(s -> s.getId() == Long.parseLong(idStudent)).findAny().orElse(null));					
				}
				
				predmeti.add(new Predmet(id, sifra, naziv, brojECTSBodova, nositelj, studentiNaPredmetu));					
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return predmeti;
	}
	
	private static List<Ispit> ucitajIspite(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<Ispit> ispiti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {			
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));
				Long idPredmeta = Long.parseLong(inputData.get(i + 1));
				Predmet predmet = predmeti.stream().filter(p -> p.getId() == idPredmeta).findAny().orElse(null);
				Long idStudenta = Long.parseLong(inputData.get(i + 2));
				Student student = studenti.stream().filter(s -> s.getId() == idStudenta).findAny().orElse(null);
				Integer ocjena = Integer.parseInt(inputData.get(i + 3));
				LocalDateTime datumIspita = LocalDateTime.parse(inputData.get(i + 4), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
				
				ispiti.add(new Ispit(id, predmet, student, ocjena, datumIspita));				
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return ispiti;
	}
	
	private static List<ObrazovnaUstanova> ucitajObrUstanove(String inputFileName, Integer nbrOfLinesPerRecord) {
		
		List<ObrazovnaUstanova> obrUstanove = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + inputFileName);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += nbrOfLinesPerRecord) {
				Long id = Long.parseLong(inputData.get(i));
				String tipUstanove = inputData.get(i + 1);
				String naziv = inputData.get(i + 2);
				
				List<String> idPredmeta = Arrays.asList(inputData.get(i + 3).split(" "));
				List<Predmet> predmetiUstanove = new ArrayList<>();
				for (String idPredmet : idPredmeta) {
					predmetiUstanove.add(predmeti.stream().filter(p -> p.getId() == Long.parseLong(idPredmet)).findAny().orElse(null));					
				}
				
				List<String> idProfesora = Arrays.asList(inputData.get(i + 4).split(" "));
				List<Profesor> profesoriUstanove = new ArrayList<>();
				for (String idProfesor : idProfesora) {
					profesoriUstanove.add(profesori.stream().filter(p -> p.getId() == Long.parseLong(idProfesor)).findAny().orElse(null));					
				}
				
				List<String> idStudenata = Arrays.asList(inputData.get(i + 5).split(" "));
				List<Student> studentiUstanove = new ArrayList<>();
				for (String idStudent : idStudenata) {
					studentiUstanove.add(studenti.stream().filter(s -> s.getId() == Long.parseLong(idStudent)).findAny().orElse(null));					
				}
				
				List<String> idIspita = Arrays.asList(inputData.get(i + 6).split(" "));
				List<Ispit> ispitiUstanove = new ArrayList<>();
				for (String idIspit : idIspita) {
					ispitiUstanove.add(ispiti.stream().filter(is -> is.getId() == Long.parseLong(idIspit)).findAny().orElse(null));					
				}
				
				switch (tipUstanove) {
				case ObrazovnaUstanova.TYPE_FAKULTET_RACUNARSTVA:
					obrUstanove.add(new FakultetRacunarstva(id, naziv, predmetiUstanove, profesoriUstanove, studentiUstanove, ispitiUstanove));
					break;
				case ObrazovnaUstanova.TYPE_VELEUCILISTE_JAVE:
					obrUstanove.add(new VeleucilisteJave(id, naziv, predmetiUstanove, profesoriUstanove, studentiUstanove, ispitiUstanove));
					break;
				}							
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return obrUstanove;
	}
	
	/**
	 * Ispisuje profesora sa svim predmeatima koje predaje
	 * @param predmeti lista predmeta
	 */
	private static void ispisProfesoraSaPredmetima(List<Predmet> predmeti) {
		System.out.println("\n-----------------\nISPIS PROFESORA SA PREDMETIMA: ");
		Map<Profesor, List<Predmet>> profesoriSaPredmetima = new HashMap<Profesor, List<Predmet>>(); 
		for (Predmet predmet : predmeti) {
			if (!profesoriSaPredmetima.containsKey(predmet.getNositelj())) {
				profesoriSaPredmetima.put(predmet.getNositelj(), new ArrayList<Predmet>());
			}					
			profesoriSaPredmetima.get(predmet.getNositelj()).add(predmet);
		}	
		
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
	 * @param predmeti
	 */
	private static void ispisPredmetaSaStudentima(List<Predmet> predmeti) {
		
		System.out.println("\n-----------------\nISPIS PREDMETA SA STUDENTIMA: ");		
		
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
	}
	
	/**
	 * Ispisuje sve studente koji su na ispitu ostvarili ocjenu izvrstan
	 * @param ispiti lista ispita
	 */
	private static void ispisSvihOdlicnihStudenata(List<Ispit> ispiti) {
		
		System.out.println("\n-----------------\nISPIS STUDENATA SA OCJENOM ODLIČAN:");			
		
		ispiti.stream()
			.filter(i -> i.getOcjena() == Ocjena.IZVRSTAN.getOcjena())
			.forEach(i -> System.out.printf("Student %s %s iz predmeta \"%s\" ima ocjenu 5.%n", i.getStudent().getIme(), i.getStudent().getPrezime(), i.getPredmet().getNaziv()));;  
	}
	
	
	/**
	 * Ispisuje konacne ocjene studenata nakon unosa zavrsnih ocjena
	 * @param input 
	 * @param input skener
	 * @param obrazovnaUstanova obrazovna ustanova
	 */
	private static void ispisKonacneOcjeneStudenata(Scanner input, ObrazovnaUstanova obrazovnaUstanova) {
		
		System.out.println("\n-----------------\nISPIS KONAČNIH OCJENA STUDENATA:");		
		
		for (Student student : obrazovnaUstanova.getStudenti()) {
			
			HashMap<String, Integer> zavrsneOcjene;
			BigDecimal konacnaOcjena = new BigDecimal(0);
			if (obrazovnaUstanova instanceof VeleucilisteJave) {	
				VeleucilisteJave ustanova = (VeleucilisteJave)obrazovnaUstanova;
				List<Ispit> ispitiStudenta = ustanova.filtrirajIspitePoStudentu(obrazovnaUstanova.getIspiti(), student);
				List<Ispit> polozeniIspiti = ustanova.filtrirajPolozeneIspite(ispitiStudenta);				
				if (ispitiStudenta.isEmpty()) {
					System.out.printf("Student %s %s nije položio niti jedan ispit i ne može izaći na završni ispit.%n", student.getIme(), student.getPrezime());
				} else if (ispitiStudenta.size() == polozeniIspiti.size()) {
					zavrsneOcjene = unosZavrsnihOcjena(input, student);
					konacnaOcjena = ustanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
				} else {
					System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
				}
			} else if (obrazovnaUstanova instanceof FakultetRacunarstva) {
				FakultetRacunarstva ustanova = (FakultetRacunarstva)obrazovnaUstanova;
				List<Ispit> ispitiStudenta = ustanova.filtrirajIspitePoStudentu(obrazovnaUstanova.getIspiti(), student);
				List<Ispit> polozeniIspiti = ustanova.filtrirajPolozeneIspite(ispitiStudenta);
				if (ispitiStudenta.isEmpty()) {
					System.out.printf("Student %s %s nije položio niti jedan ispit i ne može izaći na završni ispit.%n", student.getIme(), student.getPrezime());
				} else if (ispitiStudenta.size() == polozeniIspiti.size()) {
					zavrsneOcjene = unosZavrsnihOcjena(input, student);
					konacnaOcjena = ustanova.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, zavrsneOcjene.get("ocjenaRada"), zavrsneOcjene.get("ocjenaObraneRada"));
				} else {
					System.out.println("Student ima nedovoljnu ocjenu iz nekog predmeta i nije moguće pristupiti završnom ispitu.");
				}						
			}
			if (konacnaOcjena.compareTo(new BigDecimal(0)) > 0) {
				System.out.printf("Konačna ocjena studija studenta %s %s je %s%n", student.getIme(), student.getPrezime(), konacnaOcjena);
			}
			
		}		
	}
	
	/**
	 * Ispisuje najboljeg studenta na godini
	 * @param obrazovnaUstanova obrazovna ustanova
	 */
	private static void ispisNajboljegStudentaGodine(ObrazovnaUstanova obrazovnaUstanova) {
		
		System.out.println("Obrazovna ustanova: " + obrazovnaUstanova.getNazivUstanove());
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
		System.out.println("Obrazovna ustanova: " + ustanova.getNazivUstanove());
		try {
			studentZaNagradu = ((FakultetRacunarstva)ustanova).odrediStudentaZaRektorovuNagradu();
			System.out.printf("Student koji je osvojio rektorovu nagradu je: %s %s JMBAG: %s%n", studentZaNagradu.getIme(), studentZaNagradu.getPrezime(), studentZaNagradu.getJmbag());
		} catch (PostojiViseNajmladjihStudenataException ex) {
			System.out.println("Postoji više najmlađih studenata!");
			log.error(ex.getMessage(), ex);
			System.exit(0);
		}
	}
	


	private static void ispisObrazovnihUstanova() {

		System.out.println("\n-----------------\nSortirane obrazovne ustanove prema broju studenata:");		
		obrUstanove.stream().sorted(new ObrazovnaUstanovaSorter()).forEach(o -> System.out.printf("%s - broj studenata: %s%n", o.getNazivUstanove(), o.getStudenti().size()));		
	}


	
	/**
	 * Pokrece unos zavrsnih ocjena
	 * @param input 
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
	 * Validira unos ocjene
	 * Ocjena je ispravna ukoliko je u rasponu 1 - 5
	 * @param input 
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
			} catch (InputMismatchException ex) {
				System.out.println("Moguće je unijeti samo brojeve. Pokušajte ponovno.");
				log.error("Pogrešan unos broja!", ex);
			} finally {
				if (input.hasNextLine()) {
					input.nextLine();
				}
			} 		
		} while (!inputSuccess);
		
		return inputNumber;
	}
	
	
	
	
}
