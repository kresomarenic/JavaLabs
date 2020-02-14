package hr.java.vjezbe.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Messages {
	
	private final static Logger log = LoggerFactory.getLogger(Messages.class);	
	
	public static void showErrorMessage(List<String> errors) {
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
