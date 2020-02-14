package hr.java.vjezbe;

import java.io.IOException;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private MainMenu mainMenu;

	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("JAVA VJEŽBA 7");

		try {
			mainMenu = new MainMenu();
			mainMenu.getPretragaProfesora().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaStudenata().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaPredmeta().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaIspita().setOnAction(e -> prikaziPretragu(e));

			rootLayout = new BorderPane();
			rootLayout.setTop(mainMenu);

			Scene scene = new Scene(rootLayout, 1000, 700);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prikaziPretragu(ActionEvent event) {
		
		String viewName = ((MenuItem)event.getSource()).getParentMenu().getText();
		
		try {
			AnchorPane searchLayout = new AnchorPane();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/" + viewName + ".fxml"));
			searchLayout = (AnchorPane) loader.load();
			rootLayout.setCenter(searchLayout);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	



}
