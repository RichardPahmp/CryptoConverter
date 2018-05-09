package crypto.client;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
		
	}
	
	public void setMainController(MainController controller) {
		this.mainController = controller;
	}
	
}
