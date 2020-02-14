package hr.java.vjezbe.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;

public class Datoteke {
	
	private final static Logger log = LoggerFactory.getLogger(Datoteke.class);
	
	private static final String DATA_LOCATION = "dat/";
	
	public static List<Profesor> ucitajDatotekuProfesora() {
		
		List<Profesor> profesori = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + Profesor.INPUT_FILE_NAME);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += Profesor.NBR_OF_LINES_PER_RECORD) {
				Long id = Long.parseLong(inputData.get(i));
				String sifra = inputData.get(i + 1);
				String ime = inputData.get(i + 2);
				String prezime = inputData.get(i + 3);
				String titula = inputData.get(i + 4);
				profesori.add(new Profesor(id, sifra, ime, prezime, titula));
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke profesora");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return profesori;		
	}
	
	public static List<Student> ucitajDatotekuStudenata() {
		List<Student> studenti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + Student.INPUT_FILE_NAME);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += Student.NBR_OF_LINES_PER_RECORD) {
				Long id = Long.parseLong(inputData.get(i));				
				String ime = inputData.get(i + 1);
				String prezime = inputData.get(i + 2);
				LocalDate datumRodjenja = LocalDate.parse(inputData.get(i + 3), DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));
				String jmbag = inputData.get(i + 4);
				studenti.add(new Student(id, ime, prezime, jmbag, datumRodjenja));
				
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke studenata");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return studenti;
	}
	
	public static List<Predmet> ucitajDatotekuPredmeta() {
		List<Predmet> predmeti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + Predmet.INPUT_FILE_NAME);
		try {
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += Predmet.NBR_OF_LINES_PER_RECORD) {
				Long id = Long.parseLong(inputData.get(i));
				String sifra = inputData.get(i + 1);
				String naziv = inputData.get(i + 2);
				Integer brojECTSBodova = Integer.parseInt(inputData.get(i + 3));
				Long idNositelja = Long.parseLong(inputData.get(i + 4));
				List<Profesor> profesori = ucitajDatotekuProfesora();
				Profesor nositelj = profesori.stream().filter(p -> p.getId() == idNositelja).findAny().orElse(null);
				List<String> idStudenata = Arrays.asList(inputData.get(i + 5).split(" "));
				Set<Student> studentiNaPredmetu = new HashSet<>();
				List<Student> studenti = ucitajDatotekuStudenata();
				for (String idStudent : idStudenata) {
					studentiNaPredmetu.add(studenti.stream().filter(s -> s.getId() == Long.parseLong(idStudent)).findAny().orElse(null));					
				}
				
				predmeti.add(new Predmet(id, sifra, naziv, brojECTSBodova, nositelj, studentiNaPredmetu));					
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke predmeta");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return predmeti;
	}
	
	public static List<Ispit> ucitajDatotekuIspita() {
		List<Ispit> ispiti = new ArrayList<>();
		List<String> inputData = Collections.emptyList();
		Path inputFile = Path.of(DATA_LOCATION + Ispit.INPUT_FILE_NAME);
		try {			
			inputData = Files.readAllLines(inputFile);
			for (int i = 0; i < inputData.size(); i += Ispit.NBR_OF_LINES_PER_RECORD) {
				Long id = Long.parseLong(inputData.get(i));
				Long idPredmeta = Long.parseLong(inputData.get(i + 1));
				List<Predmet> predmeti = ucitajDatotekuPredmeta();
				Predmet predmet = predmeti.stream().filter(p -> p.getId() == idPredmeta).findAny().orElse(null);
				Long idStudenta = Long.parseLong(inputData.get(i + 2));
				List<Student> studenti = ucitajDatotekuStudenata();
				Student student = studenti.stream().filter(s -> s.getId() == idStudenta).findAny().orElse(null);
				Integer ocjena = Integer.parseInt(inputData.get(i + 3));
				LocalDateTime datumIspita = LocalDateTime.parse(inputData.get(i + 4), DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_FORMAT));
				
				ispiti.add(new Ispit(id, predmet, student, ocjena, datumIspita));				
			}
		} catch (IOException e) {
			System.out.println("Došlo je do pogreške kod čitanja datoteke");
			log.error("Greška u čitanju datoteke!", e);
		}
		
		return ispiti;
	}

}
