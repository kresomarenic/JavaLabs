package hr.java.vjezbe.view;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.AppConstants;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.Messages;
import hr.java.vjezbe.util.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.DatePicker;

import javafx.scene.control.TableColumn;

public class StudentUnosController {
	@FXML
	private TextField txtIme;
	@FXML
	private TextField txtJmbag;
	@FXML
	private TextField txtPrezime;
	@FXML
	private DatePicker datePickerDatumRodjenja;
	@FXML
	private Button btnUnesi;
	@FXML
	private TableView<Student> tablePretraga;
	@FXML
	private TableColumn<Student, String> imeColumn;
	@FXML
	private TableColumn<Student, String> prezimeColumn;
	@FXML
	private TableColumn<Student, String> jmbagColumn;
	@FXML
	private TableColumn<Student, String> datumRodjenjaColumn;

	private ObservableList<Student> studenti;

	@FXML
	public void initialize() {

		imeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("ime"));
		prezimeColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("prezime"));
		jmbagColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("jmbag"));
		jmbagColumn.setStyle("-fx-alignment: CENTER;");
		
		datumRodjenjaColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> student) {
						SimpleStringProperty property = new SimpleStringProperty();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
						property.setValue(student.getValue().getDatumRodjenja().format(formatter));
						return property;
					}
				});
		datumRodjenjaColumn.setStyle("-fx-alignment: CENTER;");
		
		
		try {
			studenti = FXCollections.observableList(BazaPodataka.dohvatiStudentePremaKriterijima(null));
			studenti.sort(Comparator.comparing(Student::getId));
			tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			tablePretraga.setItems(studenti);			
			
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
		
		txtIme.requestFocus();		
		
	}

	public void unesi() {
		
		List<String> errors = validateFields();
		if (!errors.isEmpty()) {
			Messages.showInputErrorMessage(errors);
		} else {
			OptionalLong maxId = studenti.stream().mapToLong(student -> student.getId()).max();
			
			//String datumRodjenja = datePickerDatumRodjenja.getValue().format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));
			
			Student noviStudent = new Student(maxId.getAsLong() + 1, txtIme.getText(), txtPrezime.getText(), txtJmbag.getText(), datePickerDatumRodjenja.getValue());			
			try {
				BazaPodataka.spremiStudenta(noviStudent);
				studenti.add(noviStudent);
			} catch (BazaPodatakaException e) {
				Messages.showDBConnErrorMessage();
				e.printStackTrace();
			}			
			ponistiUnos();
		}		
	}


	private List<String> validateFields() {
		
		List<String> errors = new ArrayList<>();
		
		if (Validation.isNullOrEmpty(txtIme.getText())) {
			errors.add("Unos imena je obavezan");
		}
		if (Validation.isNullOrEmpty(txtPrezime.getText())) {
			errors.add("Unos prezimena je obavezan");
		}
		if (Validation.isNullOrEmpty(txtJmbag.getText())) {
			errors.add("Unos JMBAG-a je obavezan");
		} else {
			if (!Validation.isNumeric(txtJmbag.getText())) {
				errors.add("JMBAG se može sastojati samo od brojeva");
			}
			if (Validation.checkLength(txtJmbag.getText(), 8)) {
				errors.add("Duljina JMBAG-a mora biti 8 znamenki");
			}
		}
		if (Validation.isNullOrEmpty(txtJmbag.getText())) {
			errors.add("Unos JMBAG-a je obavezan");
		}
		if (studenti.stream().map(Student::getJmbag).collect(Collectors.toList()).contains(txtJmbag.getText().trim())) {
			errors.add("Student sa unesenom JMBAG-om već postoji");
		}	
		if (datePickerDatumRodjenja.getValue() == null) {
			errors.add("Unos datuma rođenja je obavezan");
		}		
		
		return errors;
	}
	
	private void ponistiUnos() {
		txtIme.clear();
		txtPrezime.clear();
		txtJmbag.clear();
		datePickerDatumRodjenja.setValue(null);
		
	}
	
}
