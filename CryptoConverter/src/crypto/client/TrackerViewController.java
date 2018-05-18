package crypto.client;

import java.net.URL;
import java.util.ResourceBundle;

import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.util.SearchUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Tracker used to notify users when currencies reaches a specific amount.
 * 
 * @author Emil �gge
 *
 */
public class TrackerViewController implements Initializable {
	
	@FXML
	TextField limitTextfield;
	
	@FXML
	TextField emailTextfield;
	
	@FXML
	ComboBox<Currency> comboBox;
	
	private MainController mainController;	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBox.getItems().addAll(CurrencyList.getCurrencyList());
		comboBox.getSelectionModel().select(0);
		new SearchUtil(comboBox);
		limitTextfield.setPromptText("limit");
		emailTextfield.setPromptText("email");
	}

	@FXML
	private void onAdd() {
		try {
			double limit = Double.parseDouble(limitTextfield.toString());
			String email = emailTextfield.getText();
			mainController.sendTracker(comboBox.getValue().getSymbol(), email, limit);
			
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("Invalid input");
			alert.setContentText("The value you enter need to be a valid number!");

			alert.showAndWait();
		}
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
