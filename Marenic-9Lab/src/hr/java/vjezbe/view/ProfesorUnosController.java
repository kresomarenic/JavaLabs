package hr.java.vjezbe.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.Messages;
import hr.java.vjezbe.util.Validation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

public class ProfesorUnosController {
	
	@FXML
	private TextField txtSifra;
	
	@FXML
	private TextField txtTitula;
	
	@FXML
	private TextField txtIme;
	
	@FXML
	private TextField txtPrezime;
	
	@FXML
	private Button btnUnesi;
	
	@FXML
	private TableView<Profesor> tablePretraga;
	
	@FXML
	private TableColumn<Profesor, String> sifraProfesoraColumn;
	
	@FXML
	private TableColumn<Profesor, String> prezimeProfesoraColumn;
	
	@FXML
	private TableColumn<Profesor, String> imeProfesoraColumn;
	
	@FXML
	private TableColumn<Profesor, String> titulaProfesoraColumn;
	
	private ObservableList<Profesor> profesori;
	
	
	@FXML
	public void initialize() {
		
		sifraProfesoraColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("sifra"));
		sifraProfesoraColumn.setStyle("-fx-alignment: CENTER;");
		
		prezimeProfesoraColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("prezime"));
		imeProfesoraColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("ime"));
		titulaProfesoraColumn.setCellValueFactory(new PropertyValueFactory<Profesor, String>("titula"));	
		
		try {
			profesori = FXCollections.observableList(BazaPodataka.dohvatiProfesorePremaKriterijima(null));
			profesori.sort(Comparator.comparing(Profesor::getId));
			tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			tablePretraga.setItems(profesori);
							
			txtSifra.setEditable(false);
			OptionalLong zadnjaSifra = profesori.stream().mapToLong(profesor -> Long.parseLong(profesor.getSifra().substring(2))).max();
			txtSifra.setText("PF" + String.format("%03d", zadnjaSifra.getAsLong() + 1));
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
	}

	public void unesi() {
		
		List<String> errors = validateFields();
		if (!errors.isEmpty()) {
			Messages.showInputErrorMessage(errors);
		} else {
			OptionalLong maxId = profesori.stream().mapToLong(profesor -> profesor.getId()).max();
			Profesor noviProfesor = new Profesor(maxId.getAsLong() + 1, txtSifra.getText(), txtIme.getText(), txtPrezime.getText(), txtTitula.getText());
			
			try {
				profesori.add(noviProfesor);
				BazaPodataka.spremiProfesora(noviProfesor);
			} catch (BazaPodatakaException e) {
				Messages.showDBConnErrorMessage();
				profesori.remove(noviProfesor);
				e.printStackTrace();
			}
			
			ponistiUnos();			
		}		
	}


	private List<String> validateFields() {
		
		List<String> errors = new ArrayList<>();
		
		if (Validation.isNullOrEmpty(txtSifra.getText())) {
			errors.add("Unos šifre je obavezan");
		}		
		if (profesori.stream().map(Profesor::getSifra).collect(Collectors.toList()).contains(txtSifra.getText().trim())) {
			errors.add("Profesor sa unesenom šifrom već postoji");
		}		
		if (Validation.isNullOrEmpty(txtIme.getText())) {
			errors.add("Unos imena je obavezan");
		}
		if (Validation.isNullOrEmpty(txtPrezime.getText())) {
			errors.add("Unos prezimena je obavezan");
		}
		if (Validation.isNullOrEmpty(txtTitula.getText())) {
			errors.add("Unos titule je obavezan");
		}
		return errors;
	}
	
	private void ponistiUnos() {
		txtSifra.clear();
		txtIme.clear();
		txtPrezime.clear();
		txtTitula.clear();
		
	}
	
	

}
