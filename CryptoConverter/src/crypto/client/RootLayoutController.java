package crypto.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * The controller for the root layout. Primarily controls the menu bar at the top of the application.
 * @author Richard
 *
 */
public class RootLayoutController{
	
	private static String LOGIN_STRING = "Enter your username and password to login or register a new account.";
	private static String LOGGED_IN_STRING = "You are logged in as: ";
	private static String NO_CONNECTION_STRING = "Could connect to the server, please try again later.";

	private MainController mainController;
	
	@FXML
	private HBox loginBox;
	
	@FXML
	private HBox logoutBox;
	
	@FXML
	private HBox retryBox;
	
	@FXML
	private Label loginLabel;
	
	@FXML
	private TextField usernameTextfield;
	
	@FXML
	private PasswordField passwordField;
	
	public void showLogin() {
		logoutBox.setVisible(false);
		loginBox.setVisible(true);
		retryBox.setVisible(false);
	}
	
	public void showLogout() {
		logoutBox.setVisible(true);
		loginBox.setVisible(false);
		retryBox.setVisible(false);
	}
	
	public void showRetry() {
		logoutBox.setVisible(false);
		loginBox.setVisible(false);
		retryBox.setVisible(true);
	}
	
	@FXML
	private void onRetry() {
		mainController.tryConnect();
	}
	
	@FXML
	private void onLogin() {
		mainController.login(usernameTextfield.getText(), passwordField.getText());
	}
	
	@FXML
	private void onLogout() {
		Platform.runLater(() -> loginLabel.setText(LOGIN_STRING));
		mainController.logout();
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
	
	public void setConnectionAvailible() {
		showLogin();
		Platform.runLater(() -> loginLabel.setText(LOGIN_STRING));
	}

	public void setLoggedInAs(String username) {
		Platform.runLater(() -> loginLabel.setText(LOGGED_IN_STRING + username));
	}
	
	public void setNoConnection() {
		showRetry();
		Platform.runLater(() -> loginLabel.setText(NO_CONNECTION_STRING));
	}
	
}
