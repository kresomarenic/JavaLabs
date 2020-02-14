package hr.java.vjezbe.view;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.util.AppConstants;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.Messages;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

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
		predmeti.sort(Comparator.comparing(Predmet::getId));
		tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tablePretraga.setItems(predmeti);
		
		setRowFactory();
	}
	
	private void setRowFactory() {
		tablePretraga.setRowFactory(new Callback<TableView<Predmet>, TableRow<Predmet>>() {  
	        @Override  
	        public TableRow<Predmet> call(TableView<Predmet> tableView) {  
	            final TableRow<Predmet> row = new TableRow<>();  
	            final ContextMenu contextMenu = new ContextMenu(); 
	            
	            final MenuItem prikaziStudenteMenuItem = new MenuItem("Prikaži upisane studente");  
	            prikaziStudenteMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
	                @Override  
	                public void handle(ActionEvent event) {
	                	List<String> upisaniStudenti = row.getItem().getStudenti().stream().map(s -> String.format("%s %s", s.getPrezime(), s.getIme())).collect(Collectors.toList());
	                	Messages.showInfoMessage("Popis studenata", "Studenti upisani na predmet \"" + row.getItem().getNaziv() + "\" su:", upisaniStudenti);
	                }  
	            });
	            
	            final MenuItem removeMenuItem = new MenuItem("Obriši predmet");  
	            removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
	                @Override  
	                public void handle(ActionEvent event) {
	                	predmeti.remove(row.getItem());
	                	Datoteke.spremiPredmete(predmeti);	                    
	                }  
	            });
	            
	            contextMenu.getItems().add(prikaziStudenteMenuItem);
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
