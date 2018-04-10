package crypto.client;

import java.io.IOException;
import java.util.ArrayList;

import crypto.client.model.Currency;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;

public class ConverterPane extends HBox{
	
	private ArrayList<ConverterPaneListener> listeners;
	
	@FXML
	private Button closeButton;
	
	@FXML
	private ComboBox<Currency> leftComboBox;
	
	@FXML
	private ComboBox<Currency> rightComboBox;
	
	@FXML
	private TextField leftTextField;
	
	@FXML
	private TextField rightTextField;
	
	@FXML
	private DatePicker datePicker;
	
	
	public ConverterPane(ObservableList<Currency> list) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ConverterPane.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		leftComboBox.getItems().setAll(list);
		rightComboBox.getItems().setAll(list);
		leftComboBox.getSelectionModel().select(0);
		rightComboBox.getSelectionModel().select(0);
		
		listeners = new ArrayList<ConverterPaneListener>();
	}
	
	@FXML
	private void initialize() {
		
	}
	
	public void addConverterPaneListener(ConverterPaneListener listener) {
		listeners.add(listener);
	}
	
	public void removeConverterPaneListener(ConverterPaneListener listener) {
		listeners.remove(listener);
	}
	
	@FXML
	private void closeButtonClicked() {
		for(ConverterPaneListener listener : listeners) {
			listener.closeButtonClicked(this);
		}
	}
	
	@FXML
	private void leftTextfieldAction() {
		for(ConverterPaneListener listener : listeners) {
			listener.leftTextfieldAction(this, leftComboBox.getValue(), rightComboBox.getValue(), Double.parseDouble(leftTextField.getText()), datePicker.getValue());
		}
	}
	
	@FXML 
	private void rightTextfieldAction() {
		for(ConverterPaneListener listener : listeners) {
			listener.rightTextfieldAction(this, rightComboBox.getValue(), leftComboBox.getValue(), Double.parseDouble(rightTextField.getText()), datePicker.getValue());
		}
	}
	
	public void setLeftTextfieldText(String text) {
		leftTextField.setText(text);
		
	}
	
	public void setRightTextfieldText(String text) {
		rightTextField.setText(text);
	}
}
