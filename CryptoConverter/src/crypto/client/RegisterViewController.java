package crypto.client;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterViewController {

	@FXML
	private Label errorLabel;
	
	@FXML
	private TextField usernameTextfield;
	
	@FXML
	private PasswordField passwordTextfield;
	
	@FXML
	private PasswordField repeatTextfield;
	
	private MainController mainController;
	
	@FXML
	private void onRegister() {
		if(passwordTextfield.getText().equals(repeatTextfield.getText())) {
			String user = usernameTextfield.getText();
			String pass = passwordTextfield.getText();
			mainController.register(user, pass);
		}
	}
	
	public void registerSuccessful() {
		Platform.runLater(this::closeWindow);
	}
	
	private void closeWindow() {
		Stage stage = (Stage) usernameTextfield.getScene().getWindow();
		stage.close();
	}
	
	public void registerFailed() {
		errorLabel.setText("Registration failed");
	}
	
	public void setMainController(MainController controller) {
		this.mainController = controller;
	}
	
}
