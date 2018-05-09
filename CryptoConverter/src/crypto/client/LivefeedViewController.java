package crypto.client;

import java.net.URL;
import java.util.ResourceBundle;

import crypto.client.model.Currency;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class LivefeedViewController implements Initializable{
	
	@FXML
	private ComboBox<Currency> comboBox;
	
	@FXML
	private TableView<String> tableView;
	
	private MainController mainController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	@FXML
	private void onAddButtonClick() {
		
	}
	
	public void setMainController(MainController controller) {
		this.mainController = controller;
	}
}
