package hr.java.vjezbe.util;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class Messages {
	
	private final static Logger log = LoggerFactory.getLogger(Messages.class);
	
	public static void showDBConnErrorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greška");		
		alert.setContentText("Došlo je do greške prilikom rada sa bazom podataka");		
		alert.showAndWait();
    }
	
	public static void showInputErrorMessage(List<String> errors) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greška");
		alert.setHeaderText("Molim ispravite slijedeće greške:");		
		errors.stream().forEach(e -> alert.setContentText(alert.getContentText() + "\u2022 " + e + "\n"));		
		alert.showAndWait();
    }
	
	public static void showInfoMessage(String title, String headerText, List<String> messages) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);		
		messages.stream().forEach(e -> alert.setContentText(alert.getContentText() + "\u2022 " + e + "\n"));		
		alert.showAndWait();
    }
	
	

}
