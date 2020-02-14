package hr.java.vjezbe.view;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Messages;
import hr.java.vjezbe.util.Validation;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
	private ComboBox<Profesor> comboNositelj;
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
		
		try {
			predmeti = FXCollections.observableList(BazaPodataka.dohvatiPredmetePremaKriterijima(null));			
			predmeti.sort(Comparator.comparing(Predmet::getId));
			tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			tablePretraga.setItems(predmeti);
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}		
		setRowFactory();
		populateComboNositelj();
	}
	
	private void populateComboNositelj() {
		ObservableList<Profesor> profesori;
		try {
			profesori = FXCollections.observableList(BazaPodataka.dohvatiProfesorePremaKriterijima(null));
			comboNositelj.setItems(profesori);
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}		
		
		comboNositelj.setCellFactory(new Callback<ListView<Profesor>,ListCell<Profesor>>(){
			 
            @Override
            public ListCell<Profesor> call(ListView<Profesor> p) {
                 
                final ListCell<Profesor> cell = new ListCell<Profesor>(){
 
                    @Override
                    protected void updateItem(Profesor p, boolean bln) {
                        super.updateItem(p, bln);
                         
                        if(p != null){
                            setText(p.getIme() + " " + p.getPrezime());
                        } else{
                            setText(null);
                        }
                    }  
                };
                 
                return cell;
            }
        });
		
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
	                	try {
							BazaPodataka.obrisiPredmet(row.getItem());
							predmeti.remove(row.getItem());
						} catch (BazaPodatakaException e) {
							Messages.showDBConnErrorMessage();
							e.printStackTrace();
						}
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

		Predmet predmet = new Predmet();
		
		if (!txtSifra.getText().trim().equals("")) {
			predmet.setSifra(txtSifra.getText());
		}
		if (!txtNaziv.getText().trim().equals("")) {
			predmet.setNaziv(txtNaziv.getText());
		}		
		if (!txtBrojECTS.getText().trim().equals("") || Validation.isNumeric(txtBrojECTS.getText().trim())) {
			predmet.setBrojEctsBodova(Integer.parseInt(txtBrojECTS.getText()));
		}
		
		if (comboNositelj.getValue() != null) {
			predmet.setNositelj(comboNositelj.getValue());			
			comboNositelj.setValue(null);
		}
				
		try {
			tablePretraga.setItems(FXCollections.observableList(BazaPodataka.dohvatiPredmetePremaKriterijima(predmet)));
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
	}

//	// PRETRAGA PO UČITANOJ LISTI
//	public void pretrazi(ActionEvent event) {
//
//		List<Predmet> filteredList = predmeti
//				.filtered(p -> p.getSifra().toLowerCase().contains(txtSifra.getText().toLowerCase()))
//				.filtered(p -> p.getNaziv().toLowerCase().contains(txtNaziv.getText().toLowerCase()))
//				.filtered(p -> p.getBrojEctsBodova().toString().contains(txtBrojECTS.getText()))
//				.filtered(p -> String.format("%s %s", p.getNositelj().getIme(), p.getNositelj().getPrezime()).toLowerCase().contains(txtNositelj.getText().toLowerCase()))
//				.stream().collect(Collectors.toList());
//		
//		tablePretraga.setItems(FXCollections.observableList(filteredList));
//	}
	
}
