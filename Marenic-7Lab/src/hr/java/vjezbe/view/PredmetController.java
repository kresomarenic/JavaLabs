package hr.java.vjezbe.view;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.util.AppConstants;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class PredmetController {
	@FXML
	private TextField txtSifra;
	@FXML
	private TextField txtBrojECTS;
	@FXML
	private TextField txtNaziv;
	@FXML
	private TextField txtNositelj;
	@FXML
	private Button btnPretraga;
	@FXML
	private TableView<Predmet> tablePretraga;
	@FXML
	private TableColumn<Predmet, String> sifraColumn;
	@FXML
	private TableColumn<Predmet, String> nazivColumn;
	@FXML
	private TableColumn<Predmet, Integer> brojECTSColumn;
	@FXML
	private TableColumn<Predmet, String> nositeljColumn;	

	private ObservableList<Predmet> predmeti;

	@FXML
	public void initialize() {
		
		sifraColumn.setCellValueFactory(new PropertyValueFactory<Predmet, String>("sifra"));
		sifraColumn.setStyle("-fx-alignment: CENTER;");
		
		nazivColumn.setCellValueFactory(new PropertyValueFactory<Predmet, String>("naziv"));
		brojECTSColumn.setCellValueFactory(new PropertyValueFactory<Predmet, Integer>("brojEctsBodova"));
		brojECTSColumn.setStyle("-fx-alignment: CENTER;");
		
		nositeljColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Predmet, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Predmet, String> predmet) {
						SimpleStringProperty property = new SimpleStringProperty();
						Profesor nositelj = predmet.getValue().getNositelj();
						property.setValue(String.format("%s %s", nositelj.getIme(), nositelj.getPrezime()));
						return property;
					}
				});

		predmeti = FXCollections.observableList(Datoteke.ucitajDatotekuPredmeta());
		tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tablePretraga.setItems(predmeti);
	}

	public void pretrazi(ActionEvent event) {

		List<Predmet> filteredList = predmeti
				.filtered(p -> p.getSifra().toLowerCase().contains(txtSifra.getText().toLowerCase()))
				.filtered(p -> p.getNaziv().toLowerCase().contains(txtNaziv.getText().toLowerCase()))
				.filtered(p -> p.getBrojEctsBodova().toString().contains(txtBrojECTS.getText()))
				.filtered(p -> String.format("%s %s", p.getNositelj().getIme(), p.getNositelj().getPrezime()).toLowerCase().contains(txtNositelj.getText().toLowerCase()))
				.stream().collect(Collectors.toList());
		
		tablePretraga.setItems(FXCollections.observableList(filteredList));
	}
	
}
