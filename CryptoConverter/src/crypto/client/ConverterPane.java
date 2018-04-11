package crypto.client;

import java.io.IOException;
import java.util.ArrayList;

import crypto.client.model.Currency;
import crypto.util.ConverterData;
import crypto.util.Pair;
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
	
	public void setConverterData(ConverterData data) {
		selectCurrencyLeft(data.getLeftCurrency());
		selectCurrencyRight(data.getRightCurrency());
		datePicker.setValue(data.getDate());
		leftTextField.setText(data.getLeftSum() + "");
		rightTextField.setText(data.getRightSum() + "");
	}
	
	public void selectCurrencyLeft(String symbol) {
		for(Currency currency : leftComboBox.getItems()) {
			if(currency.getSymbol().equals(symbol)) {
				leftComboBox.getSelectionModel().select(currency);
				break;
			}
		}
	}

	public void selectCurrencyRight(String symbol) {
		for(Currency currency : rightComboBox.getItems()) {
			if(currency.getSymbol().equals(symbol)) {
				rightComboBox.getSelectionModel().select(currency);
				break;
			}
		}
	}
	
	public Pair<String, String> getCurrencyChoices(){
		return new Pair<String, String>(leftComboBox.getValue().getSymbol(), rightComboBox.getValue().getSymbol());
	}
	
	public ConverterData getConverterData() {
		return new ConverterData(leftComboBox.getValue().getSymbol(), rightComboBox.getValue().getSymbol(), datePicker.getValue(), Double.parseDouble(leftTextField.getText()), Double.parseDouble(rightTextField.getText()));
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
			double sum;
			try {
				sum = Double.parseDouble(leftTextField.getText());
			} catch (NumberFormatException e) {
				// TODO: Show error. the input is not a number.
				System.out.println("Add error handling for not-a-number inputs");
				e.printStackTrace();
				return;
			}
			listener.leftTextfieldAction(this, leftComboBox.getValue(), rightComboBox.getValue(), sum, datePicker.getValue());
		}
	}
	
	@FXML 
	private void rightTextfieldAction() {
		for(ConverterPaneListener listener : listeners) {
			double sum;
			try {
				sum = Double.parseDouble(rightTextField.getText());
			} catch (NumberFormatException e) {
				// TODO: Show error. the input is not a number.
				System.out.println("Add error handling for not-a-number inputs");
				e.printStackTrace();
				return;
			}
			listener.rightTextfieldAction(this, rightComboBox.getValue(), leftComboBox.getValue(), sum, datePicker.getValue());
		}
	}
	
	public void setLeftTextfieldText(String text) {
		leftTextField.setText(text);
		
	}
	
	public void setRightTextfieldText(String text) {
		rightTextField.setText(text);
	}
}
