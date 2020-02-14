package hr.java.vjezbe;

import java.io.IOException;

import hr.java.vjezbe.niti.DatumRodjenjaNit;
import hr.java.vjezbe.niti.NajboljiStudentNit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private static Stage primaryStage;
	private BorderPane rootLayout;
	private MainMenu mainMenu;

	
	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle("JAVA VJEŽBA 9");

		try {
			mainMenu = new MainMenu();
			mainMenu.getPretragaProfesora().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaStudenata().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaPredmeta().setOnAction(e -> prikaziPretragu(e));
			mainMenu.getPretragaIspita().setOnAction(e -> prikaziPretragu(e));
			
			mainMenu.getUnosProfesora().setOnAction(e -> prikaziUnos(e));
			mainMenu.getUnosStudenata().setOnAction(e -> prikaziUnos(e));
			mainMenu.getUnosPredmeta().setOnAction(e -> prikaziUnos(e));
			mainMenu.getUnosIspita().setOnAction(e -> prikaziUnos(e));

			rootLayout = new BorderPane();
			rootLayout.setTop(mainMenu);

			Scene scene = new Scene(rootLayout, 1000, 700);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//pokreniPrikazRodjendanaNit();
		pokreniNajboljiStudentNit();
		
		
	}

	private void pokreniPrikazRodjendanaNit() {
		Timeline prikazRodjendana = new Timeline(new KeyFrame(Duration.seconds(DatumRodjenjaNit.TIME_INTERVAL), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new DatumRodjenjaNit());
				
			}
			
		}));
		prikazRodjendana.setCycleCount(Timeline.INDEFINITE);
		prikazRodjendana.play();
		
	}
	
	private void pokreniNajboljiStudentNit() {
		Timeline najboljiStudent = new Timeline(new KeyFrame(Duration.seconds(NajboljiStudentNit.TIME_INTERVAL), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.runLater(new NajboljiStudentNit());
				
			}
			
		}));
		najboljiStudent.setCycleCount(Timeline.INDEFINITE);
		najboljiStudent.play();
		
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
	
	private void prikaziUnos(ActionEvent event) {
		
		String viewName = ((MenuItem)event.getSource()).getParentMenu().getText();
		
		try {
			AnchorPane searchLayout = new AnchorPane();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/" + viewName + "Unos.fxml"));
			searchLayout = (AnchorPane) loader.load();
			rootLayout.setCenter(searchLayout);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setStageTitle(String title) {
		primaryStage.setTitle(title);
	}
	
	



}
