package hr.java.vjezbe.view;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.Messages;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
		
		//profesori = FXCollections.observableList(Datoteke.ucitajDatotekuProfesora());
		try {
			profesori = FXCollections.observableList(BazaPodataka.dohvatiProfesorePremaKriterijima(null));
			profesori.sort(Comparator.comparing(Profesor::getId));
			tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			tablePretraga.setItems(profesori);
			setRowFactory();
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
	}
	
	private void setRowFactory() {
		tablePretraga.setRowFactory(new Callback<TableView<Profesor>, TableRow<Profesor>>() {  
	        @Override  
	        public TableRow<Profesor> call(TableView<Profesor> tableView) {  
	            final TableRow<Profesor> row = new TableRow<>();  
	            final ContextMenu contextMenu = new ContextMenu();  
	            final MenuItem removeMenuItem = new MenuItem("Obriši profesora");  
	            removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
	                @Override  
	                public void handle(ActionEvent event) {
	                	try {
	                		BazaPodataka.obrisiProfesora(row.getItem());
	                		profesori.remove(row.getItem());	                			
						} catch (BazaPodatakaException e) {
							Messages.showDBConnErrorMessage();
							e.printStackTrace();
						}                  
	                }  
	            });  
	            contextMenu.getItems().add(removeMenuItem);  
	           // Set context menu on row, but use a binding to make it only show for non-empty rows:  
	            row.contextMenuProperty().bind(  
	                    Bindings.when(row.emptyProperty())  
	                    .then((ContextMenu)null)  
	                    .otherwise(contextMenu)  
	            );  
	            return row ;  
	        }  
	    });  
		
	}
	
	public void pretrazi() {
		
		Profesor profesor = new Profesor();
		
		if (!txtSifra.getText().trim().equals("")) {
			profesor.setSifra(txtSifra.getText());
		}
		if (!txtPrezime.getText().trim().equals("")) {
			profesor.setPrezime(txtPrezime.getText());
		}
		if (!txtIme.getText().trim().equals("")) {
			profesor.setIme(txtIme.getText());		
		}
		if (!txtTitula.getText().trim().equals("")) {
			profesor.setTitula(txtTitula.getText());
		}
		
		try {
			tablePretraga.setItems(FXCollections.observableList(BazaPodataka.dohvatiProfesorePremaKriterijima(profesor)));
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
	}

}
