package hr.java.vjezbe.view;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

public class IspitController {
	@FXML
	private ComboBox<Predmet> comboPredmet;
	@FXML
	private ComboBox<Integer> comboOcjena;
	@FXML
	private ComboBox<Student> comboStudent;
	@FXML
	private DatePicker datePickerDate;
	@FXML
	private ComboBox<Integer> comboHour;
	@FXML
	private ComboBox<Integer> comboMinute;
	@FXML
	private Button btnPretraga;
	@FXML
	private TableView<Ispit> tablePretraga;
	@FXML
	private TableColumn<Ispit, String> nazivColumn;
	@FXML
	private TableColumn<Ispit, String> studentColumn;
	@FXML
	private TableColumn<Ispit, Integer> ocjenaColumn;
	@FXML
	private TableColumn<Ispit, String> datumVrijemeColumn;
	
	private ObservableList<Ispit> ispiti;

	@FXML
	public void initialize() {
		
		datePickerDate.setEditable(false);
		comboHour.setEditable(false);
		comboMinute.setEditable(false);
		
		IntStream.rangeClosed(1,5).boxed().forEach(comboOcjena.getItems()::add);
		IntStream.rangeClosed(8,21).boxed().forEach(comboHour.getItems()::add);		
		IntStream.rangeClosed(0,59).boxed().forEach(comboMinute.getItems()::add);
		
		nazivColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						Predmet predmet = ispit.getValue().getPredmet();
						property.setValue(String.format("%s", predmet.getNaziv()));
						return property;
					}
				});
		
		ocjenaColumn.setCellValueFactory(new PropertyValueFactory<Ispit, Integer>("ocjena"));
		ocjenaColumn.setStyle("-fx-alignment: CENTER;");
		
		studentColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						Student student = ispit.getValue().getStudent();
						property.setValue(String.format("%s %s", student.getIme(), student.getPrezime()));
						return property;
					}
				});

		datumVrijemeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Ispit, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Ispit, String> ispit) {
						SimpleStringProperty property = new SimpleStringProperty();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_TIME_FORMAT);
						property.setValue(ispit.getValue().getDatumIVrijeme().format(formatter));
						return property;
					}
				});
		datumVrijemeColumn.setStyle("-fx-alignment: CENTER;");		
		
		tablePretraga.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		try {
			ispiti = FXCollections.observableList(BazaPodataka.dohvatiIspitePremaKriterijima(null));
			ispiti.sort(Comparator.comparing(Ispit::getId));
			tablePretraga.setItems(ispiti);
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}		
		
		initializeComboPredmet();
		//initializeComboStudent();
	}

	private void initializeComboPredmet() {
		
		ObservableList<Predmet> predmeti;
		try {
			predmeti = FXCollections.observableList(BazaPodataka.dohvatiPredmetePremaKriterijima(null));
			comboPredmet.setItems(predmeti);
		} catch (BazaPodatakaException e1) {
			Messages.showDBConnErrorMessage();
			e1.printStackTrace();
		}
		
		
		comboPredmet.setCellFactory(new Callback<ListView<Predmet>,ListCell<Predmet>>(){
			 
            @Override
            public ListCell<Predmet> call(ListView<Predmet> p) {
                 
                final ListCell<Predmet> cell = new ListCell<Predmet>(){
 
                    @Override
                    protected void updateItem(Predmet p, boolean bln) {
                        super.updateItem(p, bln);
                         
                        if(p != null){
                            setText(p.getNaziv());
                        }else{
                            setText(null);
                        }
                    }  
                };
                 
                return cell;
            }
        });
		
		comboPredmet.setOnAction(e -> populateComboStudent(e));		
	}
	
	private void populateComboStudent(ActionEvent e) {
		if (comboPredmet.getValue() != null) {
			Set<Student> upisaniStudenti = comboPredmet.getValue().getStudenti();
			List<Student> listaStudenata;
			try {
				if (upisaniStudenti != null) {
					listaStudenata = BazaPodataka.dohvatiStudentePremaKriterijima(null);				
					listaStudenata = listaStudenata.stream().filter(s -> upisaniStudenti.stream().map(Student::getId).collect(Collectors.toList()).contains(s.getId())).collect(Collectors.toList());
					ObservableList<Student> studenti = FXCollections.observableList(listaStudenata);
					comboStudent.setItems(studenti);
				}				
			} catch (BazaPodatakaException e1) {
				Messages.showDBConnErrorMessage();
				e1.printStackTrace();
			}
			
		}		
		
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
	
	public void pretrazi(ActionEvent event) {

		Ispit ispit = new Ispit();
		
		if (comboPredmet.getValue() != null) {
			ispit.setPredmet(comboPredmet.getValue());
			comboPredmet.setValue(null);
		}		
		if (comboStudent.getValue() != null) {
			ispit.setStudent(comboStudent.getValue());
			comboStudent.setValue(null);
		}
		if (comboOcjena.getValue() != null) {
			ispit.setOcjena(comboOcjena.getValue());
			comboOcjena.setValue(null);
		}
		if (datePickerDate.getValue() != null && comboHour.getValue() != null && comboMinute.getValue() != null) {
			LocalDateTime datumiVrijemeIspita = LocalDateTime.of(datePickerDate.getValue(), LocalTime.of(comboHour.getValue(), comboMinute.getValue()));
			ispit.setDatumIVrijeme(datumiVrijemeIspita);
			datePickerDate.setValue(null);
			comboHour.setValue(null);
			comboMinute.setValue(null);
		}				
		
		try {
			tablePretraga.setItems(FXCollections.observableList(BazaPodataka.dohvatiIspitePremaKriterijima(ispit)));
		} catch (BazaPodatakaException e) {
			Messages.showDBConnErrorMessage();
			e.printStackTrace();
		}
	}

//	public void pretrazi(ActionEvent event) {
//
//		List<Ispit> filteredList = ispiti
//				.filtered(i -> i.getPredmet().getNaziv().toLowerCase().contains(txtNaziv.getText().toLowerCase()))
//				.filtered(i -> String.format("%s %s", i.getStudent().getIme(), i.getStudent().getPrezime()).toLowerCase().contains(txtStudent.getText().toLowerCase()))
//				.filtered(i -> i.getOcjena().toString().contains(txtOcjena.getText()))							
//				.stream().collect(Collectors.toList());
//		
//		if (datePickerDate.getValue() != null) {
//			filteredList = filteredList.stream()
//					.filter(i -> i.getDatumIVrijeme()
//					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT))
//					.contains(datePickerDate.getValue()
//					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT)))).collect(Collectors.toList());
//			datePickerDate.setValue(null);
//		}
//		
//		if (comboHour.getValue() != null) {
//			filteredList = filteredList.stream()
//					.filter(i -> String.format("%02d", i.getDatumIVrijeme().getHour())
//							.contains(String.format("%02d", comboHour.getValue())))
//					.collect(Collectors.toList());
//			comboHour.setValue(null);
//		}
//		
//		if (comboMinute.getValue() != null) {
//			filteredList = filteredList.stream()
//					.filter(i -> String.format("%02d", i.getDatumIVrijeme().getMinute())
//							.contains(String.format("%02d", comboMinute.getValue())))
//					.collect(Collectors.toList());
//			comboMinute.setValue(null);
//		}		
//		
//		tablePretraga.setItems(FXCollections.observableList(filteredList));
//	}

}
