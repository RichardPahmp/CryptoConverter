package crypto.client.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConverterPane extends HBox{
	
	@FXML
	public Button closeButton;
	
	public ConverterPane() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ConverterPane.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void initialize() {
		
	}
	
}
