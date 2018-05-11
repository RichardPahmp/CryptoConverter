package crypto.client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import crypto.client.model.Config;
import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.util.SearchUtil;
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
	private ComboBox<Currency> currencyComboBox;
	
	@FXML
	private ComboBox<StyleListItem> styleComboBox;
	
	@FXML
	private Slider slider;
	
	@FXML
	private Label sliderLabel;
	
	private boolean unsavedChanges = false;
	
	private MainController mainController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currencyComboBox.getItems().setAll(CurrencyList.getCurrencyList());
		for(Currency currency : currencyComboBox.getItems()) {
			if(currency.getSymbol().equals(Config.DEFAULT_SYMBOL)) {
				currencyComboBox.getSelectionModel().select(currency);
				break;
			}
		}
		
		populateStyleComboBox();
		
		slider.setValue(Config.LIVE_FEED_RATE);
		slider.setMin(5);
		slider.setMax(300);
		slider.setBlockIncrement(10);
		slider.setSnapToTicks(true);
		
		sliderLabel.setText((int)slider.getValue() + "");
		
		currencyComboBox.selectionModelProperty().addListener((obs, oldV, newV) -> onComboBoxChange());
		slider.valueProperty().addListener((obs, oldV, newV) -> onSliderChange());

		new SearchUtil<>(currencyComboBox);
	}
	
	private void populateStyleComboBox() {
		File folder = new File("files/css");
		File[] fileList = folder.listFiles();
		for(File file : fileList) {
			try {
				StyleListItem item = new StyleListItem(file.getName(), file.toURI().toURL().toExternalForm());
				styleComboBox.getItems().add(item);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		styleComboBox.getSelectionModel().select(0);
	}
	
	@FXML
	private void onApply() {
		applyChanges();
		Config.saveToDisk();
	}

	@FXML
	private void onSave() {
		applyChanges();
		Config.saveToDisk();
		closeWindow();
	}
	
	@FXML
	private void onCancel() {
		onClose();
		closeWindow();
	}
	
	private void closeWindow() {
		Stage stage =(Stage) currencyComboBox.getScene().getWindow();
		stage.close();
	}
	
	private void onComboBoxChange() {
		unsavedChanges = true;
	}
	
	private void onSliderChange() {
		sliderLabel.setText((int)slider.getValue() + "");
		unsavedChanges = true;
	}
	
	private void applyChanges() {
		StyleListItem item = styleComboBox.getValue();
		if(item != null) {
			mainController.changeStyle(item.urlString);
		}
		
		Config.LIVE_FEED_RATE = (int)slider.getValue();
		Config.DEFAULT_SYMBOL = currencyComboBox.getValue().getSymbol();
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
				applyChanges();
			}
		}
	}
	
	public void setMainController(MainController controller) {
		this.mainController = controller;
	}
	
	private class StyleListItem {
		private String name;
		private String urlString;
		
		public StyleListItem(String name, String URL) {
			this.name = name;
			this.urlString = URL;
		}
		
		public String toString() {
			return name;
		}
	}
	
}
