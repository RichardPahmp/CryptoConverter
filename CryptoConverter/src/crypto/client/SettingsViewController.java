package crypto.client;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import crypto.client.model.Config;
import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SettingsViewController implements Initializable {
	
	@FXML
	private ComboBox<Currency> comboBox;
	
	@FXML
	private Slider slider;
	
	@FXML
	private Label sliderLabel;
	
	private boolean unsavedChanges = false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		comboBox.getItems().setAll(CurrencyList.getCurrencyList());
		for(Currency currency : comboBox.getItems()) {
			if(currency.getSymbol().equals(Config.DEFAULT_SYMBOL)) {
				comboBox.getSelectionModel().select(currency);
				break;
			}
		}
		
		slider.setValue(Config.LIVE_FEED_RATE);
		slider.setMin(5);
		slider.setMax(300);
		slider.setBlockIncrement(10);
		slider.setSnapToTicks(true);
		
		sliderLabel.setText((int)slider.getValue() + "");
		
		comboBox.selectionModelProperty().addListener((obs, oldV, newV) -> onComboBoxChange());
		slider.valueProperty().addListener((obs, oldV, newV) -> onSliderChange());
	}

	@FXML
	private void onSave() {
		saveChanges();
		Config.saveToDisk();
		closeWindow();
	}
	
	@FXML
	private void onCancel() {
		onClose();
		closeWindow();
	}
	
	private void closeWindow() {
		Stage stage =(Stage) comboBox.getScene().getWindow();
		stage.close();
	}
	
	private void onComboBoxChange() {
		unsavedChanges = true;
	}
	
	private void onSliderChange() {
		sliderLabel.setText((int)slider.getValue() + "");
		unsavedChanges = true;
	}
	
	private void saveChanges() {
		Config.LIVE_FEED_RATE = (int)slider.getValue();
		Config.DEFAULT_SYMBOL = comboBox.getValue().getSymbol();
		unsavedChanges = false;
	}
	
	public void onClose() {
		if(unsavedChanges) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Unsaved Changes");
			alert.setHeaderText("You have unsaved settings.");
			alert.setContentText("Do you wish to save?");
			
			ButtonType buttonTypeYes = new ButtonType("Yes");
			ButtonType buttonTypeNo = new ButtonType("No");
			
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == buttonTypeYes) {
				saveChanges();
			}
		}
	}
	
}
