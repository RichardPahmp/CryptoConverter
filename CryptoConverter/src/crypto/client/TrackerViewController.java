package crypto.client;

import crypto.client.model.Currency;
import javafx.fxml.FXML;
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
		//mainController.sendTracker(symbol, email, limit);
	}
	
	@FXML
	private void onClose() {
		
	}
	
	public void setMainController(MainController controller) {
		mainController = controller;
	}
}
