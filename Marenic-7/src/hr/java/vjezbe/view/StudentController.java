package hr.java.vjezbe.view;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.util.AppConstants;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.DatePicker;

import javafx.scene.control.TableColumn;

public class StudentController {
	@FXML
	private TextField txtIme;
	@FXML
	private TextField txtJmbag;
	@FXML
	private TextField txtPrezime;
	@FXML
	private DatePicker datePickerDatumRodjenja;
	@FXML
	private Button btnPretraga;
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

		studenti = FXCollections.observableList(Datoteke.ucitajDatotekuStudenata());
		tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tablePretraga.setItems(studenti);
	}

	public void pretrazi(ActionEvent event) {
		
		List<Student> filteredList = studenti
				.filtered(s -> s.getIme().toLowerCase().contains(txtIme.getText().toLowerCase()))
				.filtered(s -> s.getPrezime().toLowerCase().contains(txtPrezime.getText().toLowerCase()))
				.filtered(s -> s.getJmbag().toLowerCase().contains(txtJmbag.getText().toLowerCase()))				
				.stream().collect(Collectors.toList());
		
		if (datePickerDatumRodjenja.getValue() != null) {
			filteredList = filteredList.stream()
					.filter(s -> s.getDatumRodjenja()
					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT))
					.contains(datePickerDatumRodjenja.getValue()
					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT)))).collect(Collectors.toList());
			datePickerDatumRodjenja.setValue(null);
		}

		tablePretraga.setItems(FXCollections.observableList(filteredList));
	}
}
