package crypto.client;

import crypto.client.model.ClientConfig;
import crypto.client.model.Currency;
import crypto.util.ComboboxUtil;
import crypto.util.ConverterData;
import crypto.util.SearchUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;

/**
 * An HBox node Containing UI elements for converting a currency to a different
 * currency.
 *
 * @author Richard
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
	
	private Timeline timelineLeftField, timelineRightField;
	
	
	/**
	 * Initialize a ConverterPane with a list of currencies.
	 *
	 * @param list The list of currencies the user can choose from.
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
		selectCurrencyLeft(ClientConfig.DEFAULT_SYMBOL);
		selectCurrencyRight(ClientConfig.DEFAULT_SYMBOL);

		ComboboxUtil.setupCombobox(leftComboBox);
		ComboboxUtil.setupCombobox(rightComboBox);

		Image upArrowImage = new Image("file:files/images/UpArrow.png", 16, 16, true, true);
		Image downArrowImage = new Image("file:files/images/DownArrow.png", 16, 16, true, true);
		ImageView view = new ImageView(upArrowImage);
		upArrowButton.setGraphic(view);
		view = new ImageView(downArrowImage);
		downArrowButton.setGraphic(view);

		upArrowButton.setOnAction(e -> listener.moveUp(this));
		downArrowButton.setOnAction(e -> listener.moveDown(this));

		leftComboBox.valueProperty().addListener((obs, newV, oldV) -> listener.onChange());
		rightComboBox.valueProperty().addListener((obs, newV, oldV) -> listener.onChange());

		leftTextField.setText("0");
		rightTextField.setText("0");
		
		timelineLeftField = new Timeline();
		timelineRightField = new Timeline();
		
		KeyFrame keyFrameUpdateLeft = new KeyFrame(Duration.millis(750), e -> leftTextfieldAction());
		KeyFrame keyFrameUpdateRight = new KeyFrame(Duration.millis(750), e -> rightTextfieldAction());
		
		timelineLeftField.getKeyFrames().add(keyFrameUpdateLeft);
		timelineRightField.getKeyFrames().add(keyFrameUpdateRight);
		
		leftTextField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				leftTextfieldAction();
			} else {
				timelineLeftField.playFromStart();
			}
		});
		rightTextField.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				rightTextfieldAction();
			} else {
				timelineRightField.playFromStart();
			}
		});
	}

	/**
	 * Set the data to display in the ConverterPane
	 *
	 * @param data The data to display
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
	 * @param symbol The symbol of the currency to select.
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
	 * @param symbol The symbol of the currency to select.
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
		timelineLeftField.stop();
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
		timelineRightField.stop();
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

}
