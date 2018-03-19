package crypto.client.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import crypto.client.DemoMain;
import crypto.client.model.Currency;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class DemoViewController implements Initializable{

	private DemoMain demoMain;
	
	@FXML
	private ComboBox<Currency> leftComboBox;
	
	@FXML
	private ComboBox<Currency> rightComboBox;
	
	@FXML
	private TextField leftTextField;
	
	@FXML
	private TextField rightTextField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	public void setDemoMain(DemoMain main) {
		this.demoMain = main;
		
		leftComboBox.getItems().setAll(demoMain.getCurrencies());
		rightComboBox.getItems().setAll(demoMain.getCurrencies());
		leftComboBox.getSelectionModel().select(0);
		rightComboBox.getSelectionModel().select(0);
	}
	
	@FXML
	private void leftFieldChanged() {
		Double result = Double.parseDouble(demoMain.exchange(leftComboBox.getValue().getSymbol(), rightComboBox.getValue().getSymbol()));
		Double numFrom = Double.parseDouble(leftTextField.getText());
		rightTextField.setText(String.valueOf(result * numFrom));
	}
	
	@FXML
	private void rightFieldChanged() {
		Double result = Double.parseDouble(demoMain.exchange(rightComboBox.getValue().getSymbol(), leftComboBox.getValue().getSymbol()));
		Double numFrom = Double.parseDouble(rightTextField.getText());
		leftTextField.setText(String.valueOf(result * numFrom));
	}
	
	
}
