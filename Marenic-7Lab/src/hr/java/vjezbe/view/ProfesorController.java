package hr.java.vjezbe.view;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

public class ProfesorController {
	
	@FXML
	private TextField txtSifra;
	
	@FXML
	private TextField txtTitula;
	
	@FXML
	private TextField txtIme;
	
	@FXML
	private TextField txtPrezime;
	
	@FXML
	private Button btnPretraga;
	
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
		
		profesori = FXCollections.observableList(Datoteke.ucitajDatotekuProfesora());
		tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tablePretraga.setItems(profesori);		
	}
	
	
	public void pretrazi() {
	
		List<Profesor> filteredList = profesori
				.filtered(p -> p.getSifra().toLowerCase().contains(txtSifra.getText().toLowerCase()))
				.filtered(p -> p.getPrezime().toLowerCase().contains(txtPrezime.getText().toLowerCase()))
				.filtered(p -> p.getIme().toLowerCase().contains(txtIme.getText().toLowerCase()))
				.filtered(p -> p.getTitula().toLowerCase().contains(txtTitula.getText().toLowerCase()))
				.stream().collect(Collectors.toList());
		
		tablePretraga.setItems(FXCollections.observableList(filteredList));
	}

}
