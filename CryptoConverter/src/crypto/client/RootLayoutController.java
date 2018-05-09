package crypto.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

/**
 * The controller for the root layout. Primarily controls the menu bar at the top of the application.
 * @author Richard
 *
 */
public class RootLayoutController{

	private MainController mainController;
	
	@FXML
	private HBox loginBox;
	
	@FXML
	private HBox logoutBox;
	
	@FXML
	private Label loginLabel;
	
	
	@FXML
	private void onLogin() {
		logoutBox.setVisible(true);
		loginBox.setVisible(false);
	}
	
	@FXML
	private void onLogout() {
		logoutBox.setVisible(false);
		loginBox.setVisible(true);
	}
	
	@FXML
	private void onRegister() {
		mainController.openRegister();
	}
	
	/**
	 * Tell the program to save any changes when the save menu item is clicked.
	 */
	@FXML
	private void handleSave() {
		mainController.onSave();
	}
	
	/**
	 * Tell the program that it's closing.
	 */
	@FXML
	private void handleClose() {
		mainController.closeApp();
	}
	
	@FXML
	private void handleSettings() {
		mainController.openSettings();
	}
	
	/**
	 * Opens a window showing information about the program when the About menu item is clicked.
	 */
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
