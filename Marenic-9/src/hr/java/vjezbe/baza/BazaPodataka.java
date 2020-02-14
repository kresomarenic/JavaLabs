package hr.java.vjezbe.baza;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.AppConstants;

public class BazaPodataka {

	private final static Logger log = LoggerFactory.getLogger(BazaPodataka.class);

	public static Connection spajanjeNaBazu() throws FileNotFoundException, IOException, SQLException {
		Properties prop = new Properties();
		prop.load(new FileReader(AppConstants.BAZA_PROPERTIES_FILE));
		String dbUrl = prop.getProperty("bazaPodataka.url");
		String dbUsername = prop.getProperty("bazaPodataka.username");
		String dbPassword = prop.getProperty("bazaPodataka.password");
		Connection veza = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		return veza;
	}

	public static List<Profesor> dohvatiProfesorePremaKriterijima(Profesor profesor) throws BazaPodatakaException {
		List<Profesor> profesori = new ArrayList<>();
		try (Connection conn = spajanjeNaBazu()) {
			StringBuilder queryString = new StringBuilder("SELECT * FROM PROFESOR WHERE 1 = 1");

			if (profesor != null) {
				if (profesor.getId() != null) {
					queryString.append(" AND ID = " + profesor.getId());
				}
				if (profesor.getSifra() != null) {
					queryString.append(" AND SIFRA LIKE '%" + profesor.getSifra() + "%'");
				}
				if (profesor.getIme() != null) {
					queryString.append(" AND IME LIKE '%" + profesor.getIme() + "%'");
				}
				if (profesor.getPrezime() != null) {
					queryString.append(" AND PREZIME LIKE '%" + profesor.getPrezime() + "%'");
				}
				if (profesor.getTitula() != null) {
					queryString.append(" AND TITULA LIKE '%" + profesor.getTitula() + "%'");
				}
				System.out.println(queryString.toString());	
			}
			
			Statement query = conn.createStatement();
			ResultSet resultSet = query.executeQuery(queryString.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				String sifra = resultSet.getString("sifra");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String titula = resultSet.getString("titula");
				Profesor noviProfesor = new Profesor(id, sifra, ime, prezime, titula);
				profesori.add(noviProfesor);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			ex.printStackTrace();
			throw new BazaPodatakaException(poruka, ex);
		}
		return profesori;
	}

	public static List<Student> dohvatiStudentePremaKriterijima(Student student) throws BazaPodatakaException {
		List<Student> studenti = new ArrayList<>();
		try (Connection conn = spajanjeNaBazu()) {
			StringBuilder queryString = new StringBuilder("SELECT * FROM STUDENT WHERE 1 = 1");

			if (student != null) {
				if (student.getId() != null) {
					queryString.append(" AND ID = " + student.getId());
				}
				if (student.getIme() != null) {
					queryString.append(" AND IME LIKE '%" + student.getIme() + "%'");
				}
				if (student.getPrezime() != null) {
					queryString.append(" AND PREZIME LIKE '%" + student.getPrezime() + "%'");
				}
				if (student.getJmbag() != null) {
					queryString.append(" AND JMBAG LIKE '%" + student.getJmbag() + "%'");
				}
				if (student.getDatumRodjenja() != null) {					
					queryString.append(" AND DATUM_RODJENJA = '" + student.getDatumRodjenja() + "'");
				}
				System.out.println(queryString.toString());				
			}
			
			Statement query = conn.createStatement();
			ResultSet resultSet = query.executeQuery(queryString.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");				
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String jmbag = resultSet.getString("jmbag");				
				LocalDate datumRodjenja = resultSet.getDate("datum_rodjenja").toLocalDate();				
				Student noviStudent = new Student(id, ime, prezime, jmbag, datumRodjenja);
				studenti.add(noviStudent);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			ex.printStackTrace();
			throw new BazaPodatakaException(poruka, ex);
		}
		return studenti;
	}

	public static List<Predmet> dohvatiPredmetePremaKriterijima(Predmet predmet) throws BazaPodatakaException {
		List<Predmet> predmeti = new ArrayList<>();
		try (Connection conn = spajanjeNaBazu()) {
			StringBuilder queryString = new StringBuilder("SELECT * FROM PREDMET WHERE 1 = 1");

			if (predmet != null) {
				if (predmet.getId() != null) {
					queryString.append(" AND ID = " + predmet.getId());
				}
				if (predmet.getSifra() != null) {
					queryString.append(" AND SIFRA LIKE '%" + predmet.getSifra() + "%'");
				}
				if (predmet.getNaziv() != null) {
					queryString.append(" AND NAZIV LIKE '%" + predmet.getNaziv() + "%'");
				}
				if (predmet.getBrojEctsBodova() != null) {
					queryString.append(" AND BROJ_ECTS_BODOVA = " + predmet.getBrojEctsBodova());
				}
				if (predmet.getNositelj() != null) {					
					queryString.append(" AND PROFESOR_ID = " + predmet.getNositelj().getId());
				}
				System.out.println(queryString.toString());				
			}
			
			Statement query = conn.createStatement();
			ResultSet resultSet = query.executeQuery(queryString.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");				
				String sifra = resultSet.getString("sifra");
				String naziv = resultSet.getString("naziv");
				Integer brojEctsBodova = resultSet.getInt("broj_ects_bodova");				
				Profesor nositelj = dohvatiProfesorePremaKriterijima(new Profesor(resultSet.getLong("profesor_id"), null, null, null, null)).get(0);				
				Predmet novipredmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj, dohvatiUpisaneStudente(id));
				predmeti.add(novipredmet);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			ex.printStackTrace();
			throw new BazaPodatakaException(poruka, ex);
		}
		return predmeti;
	}

	private static Set<Student> dohvatiUpisaneStudente(Long id) throws BazaPodatakaException {
		Set<Student> studenti = new HashSet<Student>();
		try (Connection conn = spajanjeNaBazu()) {
			StringBuilder queryString = new StringBuilder("SELECT * FROM PREDMET_STUDENT WHERE PREDMET_ID = " + id);
			
			Statement query = conn.createStatement();
			ResultSet resultSet = query.executeQuery(queryString.toString());
			while (resultSet.next()) {
				Long studentId = resultSet.getLong("student_id");				
				Student student = dohvatiStudentePremaKriterijima(new Student(studentId, null, null, null, null)).get(0);
				studenti.add(student);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			ex.printStackTrace();
			throw new BazaPodatakaException(poruka, ex);
		}
		return studenti;
	}

	public static List<Ispit> dohvatiIspitePremaKriterijima(Ispit ispit) throws BazaPodatakaException {
		List<Ispit> ispiti = new ArrayList<>();
		try (Connection conn = spajanjeNaBazu()) {
			StringBuilder queryString = new StringBuilder("SELECT * FROM ISPIT WHERE 1 = 1");

			if (ispit != null) {
				if (ispit.getId() != null) {
					queryString.append(" AND ID = " + ispit.getId());
				}
				if (ispit.getPredmet() != null) {
					queryString.append(" AND PREDMET_ID = " + ispit.getPredmet().getId());
				}
				if (ispit.getStudent() != null) {
					queryString.append(" AND STUDENT_ID = " + ispit.getStudent().getId());
				}
				if (ispit.getOcjena() != null) {
					queryString.append(" AND OCJENA = " + ispit.getOcjena());
				}
				if (ispit.getDatumIVrijeme() != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
					queryString.append(" AND DATUM_I_VRIJEME = '" + ispit.getDatumIVrijeme().format(formatter) + "'");
				}				
				System.out.println(queryString.toString());				
			}
			
			Statement query = conn.createStatement();
			ResultSet resultSet = query.executeQuery(queryString.toString());
			while (resultSet.next()) {
				Long id = resultSet.getLong("id");
				Predmet predmet = dohvatiPredmetePremaKriterijima(new Predmet(resultSet.getLong("predmet_id"), null, null, null, null, null)).get(0);
				Student student = dohvatiStudentePremaKriterijima(new Student(resultSet.getLong("student_id"), null, null, null, null)).get(0);
				Integer ocjena = resultSet.getInt("ocjena");
				LocalDateTime datumIVrijeme = resultSet.getTimestamp("datum_i_vrijeme").toLocalDateTime();								
				Ispit noviIspit = new Ispit(id, predmet, student, ocjena, datumIVrijeme);
				ispiti.add(noviIspit);
			}
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			ex.printStackTrace();
			throw new BazaPodatakaException(poruka, ex);
		}
		return ispiti;
	}

	public static void spremiProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, profesor.getIme());
			preparedStatement.setString(2, profesor.getPrezime());
			preparedStatement.setString(3, profesor.getSifra());
			preparedStatement.setString(4, profesor.getTitula());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiStudenta(Student student) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO STUDENT(ime, prezime, jmbag, datum_rodjenja) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, student.getIme());
			preparedStatement.setString(2, student.getPrezime());
			preparedStatement.setString(3, student.getJmbag());
			preparedStatement.setDate(4, Date.valueOf(student.getDatumRodjenja()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiPredmet(Predmet predmet) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO PREDMET (sifra, naziv, broj_ects_bodova, profesor_id) VALUES (?, ?, ?, ?)");
			preparedStatement.setString(1, predmet.getSifra());
			preparedStatement.setString(2, predmet.getNaziv());
			preparedStatement.setInt(3, predmet.getBrojEctsBodova());
			preparedStatement.setLong(4, predmet.getNositelj().getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void upisiStudenta(Predmet predmet, Student student) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO PREDMET_STUDENT (predmet_id, student_id) VALUES (?, ?)");
			preparedStatement.setLong(1, predmet.getId());
			preparedStatement.setLong(2, student.getId());			
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

	public static void spremiIspit(Ispit ispit) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("INSERT INTO ISPIT (predmet_id, student_id, ocjena, datum_i_vrijeme) VALUES (?, ?, ?, ?)");
			preparedStatement.setLong(1, ispit.getPredmet().getId());
			preparedStatement.setLong(2, ispit.getStudent().getId());
			preparedStatement.setInt(3, ispit.getOcjena());
			preparedStatement.setTimestamp(4, Timestamp.valueOf(ispit.getDatumIVrijeme()));
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiProfesora(Profesor profesor) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("DELETE FROM PROFESOR WHERE id =  ?");
			preparedStatement.setLong(1, profesor.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiStudenta(Student student) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("DELETE FROM STUDENT WHERE id =  ?");
			preparedStatement.setLong(1, student.getId());	
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiPredmet(Predmet predmet) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("DELETE FROM PREDMET WHERE id =  ?");
			preparedStatement.setLong(1, predmet.getId());	
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}
	
	public static void obrisiIspit(Ispit ispit) throws BazaPodatakaException {
		try (Connection conn = spajanjeNaBazu()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("DELETE FROM ISPIT WHERE id =  ?");
			preparedStatement.setLong(1, ispit.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException | IOException ex) {
			String poruka = "Došlo je do pogreške u radu s bazom podataka";
			log.error(poruka, ex);
			throw new BazaPodatakaException(poruka, ex);
		}
	}

}
