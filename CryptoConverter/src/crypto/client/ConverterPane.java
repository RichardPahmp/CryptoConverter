package crypto.client;

import java.io.IOException;
import java.util.ArrayList;

import crypto.client.model.Config;
import crypto.client.model.Currency;
import crypto.util.ConverterData;
import crypto.util.Pair;
import crypto.util.SearchUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;

/**
 * An HBox node Containing UI elements for converting a currency to a different
 * currency.
 * 
 * @author Richard
 *
 */
public class ConverterPane extends HBox {

	private ConverterPaneListener listener;

	@FXML
	private Button upArrowButton;
	
	@FXML
	private Button downArrowButton;
	
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

	/**
	 * Initialize a ConverterPane with a list of currencies.
	 * 
	 * @param list
	 *            The list of currencies the user can choose from.
	 */
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
		selectCurrencyLeft(Config.DEFAULT_SYMBOL);
		selectCurrencyRight(Config.DEFAULT_SYMBOL);
		

		
		
		leftComboBox.setCellFactory(createCellFactory());
		leftComboBox.setButtonCell(createListCell());
		rightComboBox.setCellFactory(createCellFactory());
		rightComboBox.setButtonCell(createListCell());
		
		Image upArrowImage = new Image("file:files/images/UpArrow.png", 16, 16, true, true);
		Image downArrowImage = new Image("file:files/images/DownArrow.png", 16, 16, true, true);
		ImageView view = new ImageView(upArrowImage);
		upArrowButton.setGraphic(view);
		view = new ImageView(downArrowImage);
		downArrowButton.setGraphic(view);
		
		upArrowButton.setOnAction(e -> listener.moveUp(this));
		downArrowButton.setOnAction(e -> listener.moveDown(this));
		
		new SearchUtil<Currency>(leftComboBox);
		new SearchUtil<Currency>(rightComboBox);
		
		leftComboBox.valueProperty().addListener((obs, newV, oldV) -> listener.onChange());
		rightComboBox.valueProperty().addListener((obs, newV, oldV) -> listener.onChange());

		leftTextField.setText("0");
		rightTextField.setText("0");
	}

	/**
	 * Set the data to display in the ConverterPane
	 * 
	 * @param data
	 *            The data to display
	 */
	public void setConverterData(ConverterData data) {
		selectCurrencyLeft(data.getLeftCurrency());
		selectCurrencyRight(data.getRightCurrency());
		datePicker.setValue(data.getDate());
		leftTextField.setText(data.getLeftSum() + "");
		rightTextField.setText(data.getRightSum() + "");
	}

	/**
	 * Change the current selection of the left combobox to the currency with the
	 * given symbol.
	 * 
	 * @param symbol
	 *            The symbol of the currency to select.
	 */
	public void selectCurrencyLeft(String symbol) {
		for (Currency currency : leftComboBox.getItems()) {
			if (currency.getSymbol().equals(symbol)) {
				leftComboBox.getSelectionModel().select(currency);
				break;
			}
		}
	}

	/**
	 * Change the current selection of the right combobox to the currency with the
	 * given symbol.
	 * 
	 * @param symbol
	 *            The symbol of the currency to select.
	 */
	public void selectCurrencyRight(String symbol) {
		for (Currency currency : rightComboBox.getItems()) {
			if (currency.getSymbol().equals(symbol)) {
				rightComboBox.getSelectionModel().select(currency);
				break;
			}
		}
	}

	/**
	 * Get a ConverterData object containing the data entered by the user.
	 * 
	 * @return ConverterData A class containing the selections made by the user.
	 */
	public ConverterData getConverterData() {
		double leftSum, rightSum;
		try {
			leftSum = Double.parseDouble(leftTextField.getText());
		} catch (NumberFormatException e) {
			leftSum = 0.0;
		}
		try {
			rightSum = Double.parseDouble(rightTextField.getText());
		} catch (NumberFormatException e) {
			rightSum = 0.0;
		}
		return new ConverterData(leftComboBox.getValue().getSymbol(), rightComboBox.getValue().getSymbol(),
				datePicker.getValue(), leftSum, rightSum);
	}

	public void addConverterPaneListener(ConverterPaneListener listener) {
		this.listener = listener;
	}

	public void removeConverterPaneListener() {
		this.listener = null;
	}

	@FXML
	private void closeButtonClicked() {
		listener.closeButtonClicked(this);
	}

	/**
	 * Called when the user presses enter when the left textfield is selected.
	 */
	@FXML
	private void leftTextfieldAction() {
		double sum;
		try {
			sum = Double.parseDouble(leftTextField.getText());
		} catch (NumberFormatException e) {
			// TODO: Show error. the input is not a number.
			System.out.println("Add error handling for not-a-number inputs");
			e.printStackTrace();
			return;
		}
		listener.leftTextfieldAction(this, leftComboBox.getValue(), rightComboBox.getValue(), sum,
				datePicker.getValue());
	}

	/**
	 * Called when the user presses enter when the right textfield is selected.
	 */
	@FXML
	private void rightTextfieldAction() {
		double sum;
		try {
			sum = Double.parseDouble(rightTextField.getText());
		} catch (NumberFormatException e) {
			// TODO: Show error. the input is not a number.
			System.out.println("Add error handling for not-a-number inputs");
			e.printStackTrace();
			return;
		}
		listener.rightTextfieldAction(this, rightComboBox.getValue(), leftComboBox.getValue(), sum,
				datePicker.getValue());
	}

	/**
	 * Set the text in the left textfield.
	 * 
	 * @param text
	 */
	public void setLeftTextfieldText(String text) {
		leftTextField.setText(text);

	}

	/**
	 * Set the text in the right textfield.
	 * 
	 * @param text
	 */
	public void setRightTextfieldText(String text) {
		rightTextField.setText(text);
	}
	
	private Callback<ListView<Currency>, ListCell<Currency>> createCellFactory() {
		return new Callback<ListView<Currency>, ListCell<Currency>>() {
			@Override
			public ListCell<Currency> call(ListView<Currency> arg0) {
				return new ListCell<Currency>() {
					@Override
					protected void updateItem(Currency item, boolean empty) {
						super.updateItem(item, empty);
						if(item != null && !empty) {
							setText(item.getCoinFullName());
						}
					}
				};
			}
		};
	}
	
	private ListCell<Currency> createListCell(){
		return new ListCell<Currency>() {
			protected void updateItem(Currency item, boolean empty) {
				super.updateItem(item, empty);
				if(item != null && !empty) {
					setText(item.getCoinName());
				}
			};
		};
	}
}
