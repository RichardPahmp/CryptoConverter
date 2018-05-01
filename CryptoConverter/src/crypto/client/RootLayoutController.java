package crypto.client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * The controller for the root layout. Primarily controls the menu bar at the top of the application.
 * @author Richard
 *
 */
public class RootLayoutController {

	private MainController mainController;
	
	@FXML
	private void handleSave() {
		mainController.onSave();
	}
	
	@FXML
	private void handleClose() {
		
	}
	
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("CryptoConverter");
		alert.setHeaderText("Made by group 19: ");
		alert.setContentText("Richard Pahmp\nAntoine Rebelo\nRazmus Rosén\nEmil Ögge");
		alert.showAndWait();
	}
	
	/**
	 * Sets the MainController of this controller.
	 * @param main
	 */
	public void setMainController(MainController main) {
		this.mainController = main;
	}
	
}
