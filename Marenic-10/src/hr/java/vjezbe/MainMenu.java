package hr.java.vjezbe;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenu extends MenuBar {
	
	private MenuItem pretragaProfesora;
	private MenuItem pretragaStudenata;
	private MenuItem pretragaPredmeta;
	private MenuItem pretragaIspita;
	
	private MenuItem unosProfesora;
	private MenuItem unosStudenata;
	private MenuItem unosPredmeta;
	private MenuItem unosIspita;
	
	
	public MainMenu() {
		
		Menu menuProfesor = new Menu("Profesor");
		pretragaProfesora = new MenuItem("Pretraga");
		unosProfesora = new MenuItem("Unos");
		menuProfesor.getItems().add(pretragaProfesora);
		menuProfesor.getItems().add(unosProfesora);		
		
		Menu menuStudent = new Menu("Student");
		pretragaStudenata = new MenuItem("Pretraga");
		unosStudenata = new MenuItem("Unos");
		menuStudent.getItems().add(pretragaStudenata);
		menuStudent.getItems().add(unosStudenata);
		
		Menu menuPredmet = new Menu("Predmet");
		pretragaPredmeta = new MenuItem("Pretraga");
		unosPredmeta = new MenuItem("Unos");
		menuPredmet.getItems().add(pretragaPredmeta);
		menuPredmet.getItems().add(unosPredmeta);
		
		Menu menuIspit = new Menu("Ispit");
		pretragaIspita = new MenuItem("Pretraga");
		unosIspita = new MenuItem("Unos");
		menuIspit.getItems().add(pretragaIspita);
		menuIspit.getItems().add(unosIspita);
		
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


	public MenuItem getUnosProfesora() {
		return unosProfesora;
	}


	public MenuItem getUnosStudenata() {
		return unosStudenata;
	}


	public MenuItem getUnosPredmeta() {
		return unosPredmeta;
	}


	public MenuItem getUnosIspita() {
		return unosIspita;
	}
	
	


	

}
