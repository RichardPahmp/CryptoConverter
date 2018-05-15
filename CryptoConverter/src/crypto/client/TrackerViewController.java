package crypto.client;

import crypto.client.model.Currency;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TrackerViewController {
	
	@FXML
	TextField limitTextfield;
	
	@FXML
	TextField emailTextfield;
	
	@FXML
	ComboBox<Currency> comboBox;
	
	private MainController mainController;
	
	
	@FXML
	private void onAdd() {
		try {
			Double.parseDouble(limitTextfield.toString());
			
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Invalid input");
			alert.setContentText("The value you enter need to be a valid number!");

			alert.showAndWait();
		}
		//mainController.sendTracker(symbol, email, limit);
	}
	
	@FXML
	private void onClose() {
		Stage stage =(Stage) comboBox.getScene().getWindow();
		stage.close();
	}
	
	public void setMainController(MainController controller) {
		mainController = controller;
	}
}
