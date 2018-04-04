package crypto.client.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ConverterViewController {
	
	@FXML
	private VBox vBox;
	
	@FXML
	private void initialize(){
		
	}
	
	@FXML
	private void addConverterPane() {
		ConverterPane pane = new ConverterPane();
		pane.closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> vBox.getChildren().remove(pane));
		vBox.getChildren().add(pane);
	}
}
