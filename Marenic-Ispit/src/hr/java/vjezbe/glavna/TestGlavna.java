package hr.java.vjezbe.glavna;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import hr.java.vjezbe.entitet.GenerickaOsoba;
import hr.java.vjezbe.entitet.Osoba;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.sortiranje.StudentSorter;

public class TestGlavna {

	public static void main(String[] args) {
		
//		List<Profesor> profesori = new ArrayList<Profesor>();
//		profesori.add(new Profesor("PR01", "Prof1ime", "Arof1prebergzim", "predavač"));		
//		profesori.add(new Profesor("PR02", "Prof2ime", "Prof2prezime", "predavač"));
//		profesori.add(new Profesor("PR03", "Prof3ime", "BergPrp", "predavač"));
//		profesori.add(new Profesor("PR04", "Prof4ime", "of4prezime", "predavač"));
//		profesori.add(new Profesor("PR05", "Prof5ime", "Prof5prezimeBerg", "predavač"));
//		
//		List<Student> studenti = new ArrayList<Student>();
//		studenti.add(new Student("Stud1ime", "Stud1prezime", "123456789", LocalDate.of(1984, 06, 24)));
//		studenti.add(new Student("Stud2ime", "Stud2prezime", "123456789", LocalDate.of(1984, 06, 24)));
//		studenti.add(new Student("Stud3ime", "Stud3prezime", "123456789", LocalDate.of(1984, 06, 24)));
//		studenti.add(new Student("Stud4ime", "Stud4prezime", "123456789", LocalDate.of(1984, 06, 24)));
//		studenti.add(new Student("Stud5ime", "Stud5prezime", "123456789", LocalDate.of(1984, 06, 24)));
//		studenti.add(new Student("Stud6ime", "Stud6prezime", "123456789", LocalDate.of(1984, 06, 24)));
		
		//ispisGenerickihOsoba(profesori, studenti);
		
		
		
		

		//ispisBergProf(profesori);
//		ispisSumeDuljina(profesori);
//		ispisProsjekaDuljine(profesori);
//		ispisNajkracegImenaIPrezimena(profesori);
//		ispisNajduljegImenaIPrezimena(profesori);
		
		
		
		
		
	}
	

	private static void ispisGenerickihOsoba(List<Profesor> profesori, List<Student> studenti) {

		GenerickaOsoba<Osoba> osobe = new GenerickaOsoba<Osoba>();
		profesori.stream().forEach(p -> osobe.dodajOsobu(p));
		studenti.stream().forEach(s -> osobe.dodajOsobu(s));
		
		osobe.dohvatiSveOsobw().stream().forEach(o -> System.out.println(o.getPrezime() + " " + o.getIme()));		
	}


	private static void ispisSumeDuljina(List<Profesor> profesori) {

		System.out.println("Suma duljina je: " + 
		profesori.stream()
			.map(p -> p.getIme() + p.getPrezime())
			.mapToInt(String::length)
			.sum()		
		);		
	}
	
	private static void ispisProsjekaDuljine(List<Profesor> profesori) {

		System.out.println("Prosjek duljine je: " + 
		profesori.stream()
			.map(p -> p.getIme() + p.getPrezime())
			.mapToInt(String::length)
			.average()
			.getAsDouble()
		);		
	}

	private static void ispisNajkracegImenaIPrezimena(List<Profesor> profesori) {

		System.out.println("Najkraće ime i prezime ima: " + 
				profesori.stream()
					.map(p -> p.getIme() + p.getPrezime())
					.min(Comparator.comparingInt(String::length))
					.get()
				);		
	}

	private static void ispisNajduljegImenaIPrezimena(List<Profesor> profesori) {	

		System.out.println("Najdulje ime i prezime ima: " + 
		profesori.stream()
			.map(p -> p.getIme() + p.getPrezime())
			.max(Comparator.comparingInt(String::length))
			.get()
		);
	}

	private static void ispisBergProf(List<Profesor> profesori) {		
		
		profesori.stream()
			.filter(p -> p.getPrezime().toLowerCase().contains("berg") && p.getPrezime().length() > 8)
			.sorted(Comparator.comparing(Profesor::getPrezime).reversed())
			.forEach(p -> System.out.println(p.getPrezime()));		
	}

	private static void ispisSaLambdom(List<Predmet> predmeti) {		
		
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

	private static void ispisPredmetaSaStudentima(List<Predmet> predmeti) {
		
		System.out.println();
		System.out.println("FOREACH:");
		
		Long startTime;
		Long endTime;
		
		startTime = System.currentTimeMillis();
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
		System.out.println("Vrijeme izvođenja foreach izraza je: " + (endTime - startTime) + " ms");
		
		System.out.println();
		System.out.println("LAMBDA:");		
		
		startTime = System.currentTimeMillis();
		// TODO zamjeniti ispis studenata po predmetima sa lambdom
		endTime = System.currentTimeMillis();
		System.out.println("Vrijeme izvođenja lambda izraza je: " + (endTime - startTime) + " ms");
		
		System.out.println();
		
	}
}



//List<Profesor> profesori = new ArrayList<Profesor>();
//profesori.add(new Profesor("PR01", "Prof1ime", "Prof1prezime", "predavač"));		
//profesori.add(new Profesor("PR02", "Prof2ime", "Prof2prezime", "predavač"));
//profesori.add(new Profesor("PR03", "Prof3ime", "Prof3prezime", "predavač"));
//profesori.add(new Profesor("PR04", "Prof4ime", "Prof4prezime", "predavač"));
//profesori.add(new Profesor("PR05", "Prof5ime", "Prof5prezime", "predavač"));

//List<Student> studenti = new ArrayList<Student>();
//studenti.add(new Student("Stud1ime", "Stud1prezime", "123456789", LocalDate.of(1984, 06, 24)));
//studenti.add(new Student("Stud2ime", "Stud2prezime", "123456789", LocalDate.of(1984, 06, 24)));
//studenti.add(new Student("Stud3ime", "Stud3prezime", "123456789", LocalDate.of(1984, 06, 24)));
//studenti.add(new Student("Stud4ime", "Stud4prezime", "123456789", LocalDate.of(1984, 06, 24)));
//studenti.add(new Student("Stud5ime", "Stud5prezime", "123456789", LocalDate.of(1984, 06, 24)));
//studenti.add(new Student("Stud6ime", "Stud6prezime", "123456789", LocalDate.of(1984, 06, 24)));



//List<Predmet> predmeti = new ArrayList<Predmet>();
//Set<Student> studentiPredmeta = new HashSet<Student>();
//studentiPredmeta.add(studenti.get(0));
//predmeti.add(new Predmet("PRE101", "Predmet1", 5, profesori.get(0), studentiPredmeta));
//studentiPredmeta = new HashSet<Student>();
//studentiPredmeta.add(studenti.get(1));
//studentiPredmeta.add(studenti.get(2));
//predmeti.add(new Predmet("PRE102", "Predmet2", 5, profesori.get(1), studentiPredmeta));
//studentiPredmeta = new HashSet<Student>();
//studentiPredmeta.add(studenti.get(3));
//studentiPredmeta.add(studenti.get(4));
//studentiPredmeta.add(studenti.get(5));
//predmeti.add(new Predmet("PRE103", "Predmet3", 5, profesori.get(2), studentiPredmeta));
//studentiPredmeta = new HashSet<Student>();		
//predmeti.add(new Predmet("PRE104", "Predmet4", 5, profesori.get(3), studentiPredmeta));
