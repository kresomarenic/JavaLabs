package hr.java.vjezbe.view;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class IspitController {
	@FXML
	private TextField txtNaziv;
	@FXML
	private TextField txtOcjena;
	@FXML
	private TextField txtStudent;
	@FXML
	private DatePicker datePickerDate;
	@FXML
	private ComboBox<Integer> comboHour;
	@FXML
	private ComboBox<Integer> comboMinute;
	@FXML
	private Button btnPretraga;
	
	@FXML
	private Button btnSortByDateAndTime;
	@FXML
	private ComboBox<Student> comboStudent;
	
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
	
	private List<Ispit> filteredList;

	@FXML
	public void initialize() {
		
		datePickerDate.setEditable(false);
		comboHour.setEditable(false);
		comboMinute.setEditable(false);
		
		IntStream.rangeClosed(0,23).boxed().forEach(comboHour.getItems()::add);		
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
		
		ispiti = FXCollections.observableList(Datoteke.ucitajDatotekuIspita());
		
		tablePretraga.setItems(ispiti);
		
		
		
		//ObservableList<Student> studenti = FXCollections.observableList(ispiti.stream().map(Ispit::getStudent).collect(Collectors.toList()));
		
		ObservableList<Student> studenti = FXCollections.observableList(Datoteke.ucitajDatotekuStudenata());
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

	public void pretrazi(ActionEvent event) {

		filteredList = ispiti
				.filtered(i -> i.getPredmet().getNaziv().toLowerCase().contains(txtNaziv.getText().toLowerCase()))
				.filtered(i -> String.format("%s %s", i.getStudent().getIme(), i.getStudent().getPrezime()).toLowerCase().contains(txtStudent.getText().toLowerCase()))
				.filtered(i -> i.getOcjena().toString().contains(txtOcjena.getText()))							
				.stream().collect(Collectors.toList());
		
		if (datePickerDate.getValue() != null) {
			filteredList = filteredList.stream()
					.filter(i -> i.getDatumIVrijeme()
					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT))
					.contains(datePickerDate.getValue()
					.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT)))).collect(Collectors.toList());
			datePickerDate.setValue(null);
		}
		
		if (comboHour.getValue() != null) {
			filteredList = filteredList.stream()
					.filter(i -> String.format("%02d", i.getDatumIVrijeme().getHour())
							.contains(String.format("%02d", comboHour.getValue())))
					.collect(Collectors.toList());
			comboHour.setValue(null);
		}
		
		if (comboMinute.getValue() != null) {
			filteredList = filteredList.stream()
					.filter(i -> String.format("%02d", i.getDatumIVrijeme().getMinute())
							.contains(String.format("%02d", comboMinute.getValue())))
					.collect(Collectors.toList());
			comboMinute.setValue(null);
		}
		
		if (comboStudent.getValue() != null) {
			filteredList = filteredList.stream()
					.filter(i -> i.getStudent().getId().equals(comboStudent.getValue().getId()))
					.collect(Collectors.toList());						
			comboStudent.setValue(null);
		}
		
		tablePretraga.setItems(FXCollections.observableList(filteredList));
	}
	
	public void sortirajPoDatumuIVremenu() {
		
		if (filteredList == null) {
			filteredList = new ArrayList<Ispit>();
			filteredList.addAll(ispiti);
		}
		
		filteredList.sort((d1,d2) -> d1.getDatumIVrijeme().compareTo(d2.getDatumIVrijeme()));
		tablePretraga.setItems(FXCollections.observableList(filteredList));
		
		
	}
	
	

}
