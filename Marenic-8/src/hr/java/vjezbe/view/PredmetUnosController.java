package hr.java.vjezbe.view;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.util.AppConstants;
import hr.java.vjezbe.util.Datoteke;
import hr.java.vjezbe.util.Messages;
import hr.java.vjezbe.util.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class PredmetUnosController {
	@FXML
	private TextField txtSifra;
	@FXML
	private TextField txtBrojECTS;
	@FXML
	private TextField txtNaziv;
	@FXML
	private ComboBox<Profesor> comboNositelj;
	@FXML
	private ComboBox<Student> comboStudent;
	@FXML
	private Button btnUnesi;
	@FXML
	private Button btnUpisiStudenta;
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
		
		
		tablePretraga.setOnMouseClicked((MouseEvent event) -> {
		    if (event.getButton().equals(MouseButton.PRIMARY)) {
		        int index = tablePretraga.getSelectionModel().getSelectedIndex();
		        Predmet predmet = tablePretraga.getItems().get(index);
		        populateComboStudent(predmet.getStudenti());
		    }
		});
				
		txtSifra.setEditable(false);
		txtSifra.setText("PR" + String.format("%03d", getLastCode() + 1));
		
		txtNaziv.requestFocus();
		
		populateComboNositelj();
		
	}

	
	private void populateComboNositelj() {
		ObservableList<Profesor> profesori = FXCollections.observableList(Datoteke.ucitajDatotekuProfesora());
		comboNositelj.setItems(profesori);
		
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


	private void populateComboStudent(Set<Student> upisaniStudenti) {
		
		List<Student> listaStudenata = Datoteke.ucitajDatotekuStudenata();	
		listaStudenata = listaStudenata.stream().filter(s -> !upisaniStudenti.stream().map(Student::getId).collect(Collectors.toList()).contains(s.getId())).collect(Collectors.toList());
		
		ObservableList<Student> studenti = FXCollections.observableList(listaStudenata);
		comboStudent.setItems(studenti);
		
		comboStudent.setCellFactory(new Callback<ListView<Student>,ListCell<Student>>(){
			 
            @Override
            public ListCell<Student> call(ListView<Student> p) {
                 
                final ListCell<Student> cell = new ListCell<Student>(){
 
                    @Override
                    protected void updateItem(Student s, boolean bln) {
                        super.updateItem(s, bln);
                         
                        if(s != null){
                            setText(s.getIme() + " " + s.getPrezime());
                        }else{
                            setText(null);
                        }
                    }  
                };
                 
                return cell;
            }
        });
		
	}


	public void unesi() {
		
		List<String> errors = validateFields();
		if (!errors.isEmpty()) {
			Messages.showErrorMessage(errors);
		} else {
			OptionalLong maxId = predmeti.stream().mapToLong(predmet -> predmet.getId()).max();
			
			// TODO dodati mogućnost upisa studenta na predmet 
			Predmet noviPredmet = new Predmet(maxId.getAsLong() + 1, txtSifra.getText(), txtNaziv.getText(), Integer.parseInt(txtBrojECTS.getText()), comboNositelj.getValue(), new HashSet<Student>());
			predmeti.add(noviPredmet);			
			Datoteke.spremiPredmete(predmeti);
			ponistiUnos();
		}		
	}


	private List<String> validateFields() {
		
		List<String> errors = new ArrayList<>();
		
		if (Validation.isNullOrEmpty(txtSifra.getText())) {
			errors.add("Unos šifre je obavezan");
		} else {
			if (predmeti.stream().map(Predmet::getSifra).collect(Collectors.toList()).contains(txtSifra.getText().trim())) {
				errors.add("Predmet sa unesenom šifrom već postoji");
			}	
		}
		if (Validation.isNullOrEmpty(txtNaziv.getText())) {
			errors.add("Unos naziva predmeta je obavezan");
		}
		if (Validation.isNullOrEmpty(txtBrojECTS.getText())) {
			errors.add("Unos broja ECTS bodova je obavezan");
		} else {
			if (!Validation.isNumeric(txtBrojECTS.getText())) {
				errors.add("ECTS bodovi se mogu sastojati samo od brojeva");
			}			
		}
		if (comboNositelj.getValue() == null) {
			errors.add("Odabir nositelja predmeta je obavezan");
		}
		
		return errors;
	}
	
	private void ponistiUnos() {
		txtSifra.setText("PR" + String.format("%03d", getLastCode() + 1));;
		txtNaziv.clear();
		txtBrojECTS.clear();
		comboNositelj.setValue(null);
		
	}
	
	private Long getLastCode() {
		return predmeti.stream().mapToLong(predmet -> Long.parseLong(predmet.getSifra().substring(2))).max().getAsLong();
	}
	
	public void upisiStudenta() {
		
		int index = tablePretraga.getSelectionModel().getSelectedIndex();
        Predmet predmet = tablePretraga.getItems().get(index);
        predmeti.remove(predmet);
        predmet.getStudenti().add(comboStudent.getValue());
        predmeti.add(predmet);
        predmeti.sort(Comparator.comparing(Predmet::getId));
		//tablePretraga.setItems(predmeti);
        Datoteke.spremiPredmete(predmeti); 
        
        populateComboStudent(predmet.getStudenti());
		
	}
	
}
