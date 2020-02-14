package hr.java.vjezbe;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenu extends MenuBar {
	
	private MenuItem pretragaProfesora;
	private MenuItem pretragaStudenata;
	private MenuItem pretragaPredmeta;
	private MenuItem pretragaIspita;
	
	
	public MainMenu() {
		
		Menu menuProfesor = new Menu("Profesor");
		pretragaProfesora = new MenuItem("Pretraga");
		menuProfesor.getItems().add(pretragaProfesora);
		
		
		Menu menuStudent = new Menu("Student");
		pretragaStudenata = new MenuItem("Pretraga");
		menuStudent.getItems().add(pretragaStudenata);
		
		Menu menuPredmet = new Menu("Predmet");
		pretragaPredmeta = new MenuItem("Pretraga");
		menuPredmet.getItems().add(pretragaPredmeta);
		
		Menu menuIspit = new Menu("Ispit");
		pretragaIspita = new MenuItem("Pretraga");
		menuIspit.getItems().add(pretragaIspita);
		
		getMenus().addAll(menuProfesor, menuStudent, menuPredmet, menuIspit);
		
	}


	public MenuItem getPretragaProfesora() {
		return pretragaProfesora;
	}


	public MenuItem getPretragaStudenata() {
		return pretragaStudenata;
	}


	public MenuItem getPretragaPredmeta() {
		return pretragaPredmeta;
	}


	public MenuItem getPretragaIspita() {
		return pretragaIspita;
	}
	
	


	

}
