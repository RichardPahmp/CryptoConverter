package crypto.client;

import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.util.ConverterChoices;
import crypto.util.ConverterData;
import crypto.util.TimeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * The controller for the ConverterView.
 *
 * @author Richard
 */
public class ConverterViewController implements ConverterPaneListener {

	private MainController mainController;
	
	private boolean unsavedChanges;

	private static DecimalFormat df = new DecimalFormat("#.####");

	@FXML
	private VBox vBox;

	private ObservableList<Currency> currencies = FXCollections.observableArrayList();

	/**
	 * The method run by the JavaFX loader when the view has finished loading.
	 * Loads ConverterData from disk and adds it to the view.
	 */
	@FXML
	private void initialize() {
		currencies.addAll(CurrencyList.getCurrencyList());

		ConverterChoices save = new ConverterChoices();
		try {
			save.loadFromFile("CryptoConverter/files/save.dat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<ConverterData> iterator = save.getIterator();
		if (!iterator.hasNext()) {
			addConverterPane();
		}
		while (iterator.hasNext()) {
			addConverterPane(iterator.next());
		}
		
		unsavedChanges = false;
	}
	
	/**
	 * Creates a ConverterPane and adds it to the view.
	 */
	@FXML
	private void addConverterPane() {
		ConverterPane pane = createConverterPane();
		vBox.getChildren().add(vBox.getChildren().size() - 1, pane);
		unsavedChanges = true;
	}

	/**
	 * Creates a ConverterPane and adds it to the view with the values in the given ConverterData.
	 *
	 * @param data The data to initialize the pane with.
	 */
	private void addConverterPane(ConverterData data) {
		ConverterPane pane = createConverterPane();
		pane.setConverterData(data);
		vBox.getChildren().add(vBox.getChildren().size() - 1, pane);
		unsavedChanges = true;
	}
	
	private ConverterPane createConverterPane() {
		ConverterPane pane = new ConverterPane(currencies);
		pane.addConverterPaneListener(this);
		return pane;
	}

	/**
	 * Callback for when the user presses the close button on a ConverterPane.
	 *
	 * @param pane The pane that called this listener.
	 */
	@Override
	public void closeButtonClicked(ConverterPane pane) {
		vBox.getChildren().remove(pane);
		unsavedChanges = true;
	}

	/**
	 * Callback for when the user presses enter while the left textfield is selected.
	 *
	 * @param pane The pane that called this listener.
	 * @param from The currency to convert from.
	 * @param to   The currency to convert to.
	 * @param sum  The sum to convert to a different currency.
	 * @param date The conversion will be done with the currency values at the given date.
	 */
	@Override
	public void leftTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date) {

		try {
			if (date == null) {
				Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
				double coinValue = price.get(to.getSymbol());
				pane.setRightTextfieldText(df.format(coinValue * sum));
			} else {
				Map<String, Double> price = Historic.getPriceAtTime((int)(TimeUtil.dateToTimestamp(date) / 1000), from.getSymbol(), to.getSymbol());
				double coinValue = price.get(to.getSymbol());
				pane.setRightTextfieldText(df.format(coinValue * sum));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unsavedChanges = true;
		mainController.onSearch(new String[]{from.getSymbol(), to.getSymbol()});
	}

	/**
	 * Callback for when the user presses enter while the right textfield is selected.
	 *
	 * @param pane The pane that called this listener.
	 * @param from The currency to convert from.
	 * @param to   The currency to convert to.
	 * @param sum  The sum to convert to a different currency.
	 * @param date The conversion will be done with the currency values at the given date.
	 */
	@Override
	public void rightTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date) {

		try {
			if (date == null) {
				Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
				double coinValue = price.get(to.getSymbol());
				pane.setLeftTextfieldText(df.format(coinValue * sum));
			} else {
				Map<String, Double> price = Historic.getPriceAtTime((int)(TimeUtil.dateToTimestamp(date) / 1000), from.getSymbol(), to.getSymbol());
				double coinValue = price.get(to.getSymbol());
				pane.setLeftTextfieldText(df.format(coinValue * sum));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		unsavedChanges = true;
		mainController.onSearch(new String[]{from.getSymbol(), to.getSymbol()});
	}

	/**
	 * Get the ConverterData from all ConverterPanes and save them to the disk.
	 */
	@FXML
	public void saveData() {
		ConverterChoices save = new ConverterChoices();
		for (Node node : vBox.getChildren()) {

			if (node instanceof ConverterPane) {
				save.addData(((ConverterPane) node).getConverterData());
			}
		}
		try {
			save.saveToFile("CryptoConverter/files/save.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		unsavedChanges = false;
	}
	
	/**
	 * Sets the MainController of this controller.
	 * @param main
	 */
	public void setMainController(MainController main) {
		this.mainController = main;
	}
	
	public void onClosing() {
		if(unsavedChanges) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Unsaved Changes");
			alert.setHeaderText("You have unsaved changes.");
			alert.setContentText("Do you wish to save?");
			
			ButtonType buttonTypeYes = new ButtonType("Yes");
			ButtonType buttonTypeNo = new ButtonType("No");
			
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == buttonTypeYes) {
				saveData();
			} 
		}
	}
	
	@Override
	public void onChange() {
		unsavedChanges = true;
	}

	@Override
	public void moveUp(ConverterPane pane) {
		int index = vBox.getChildren().indexOf(pane);
		if(index != 0) {
			vBox.getChildren().remove(index);
			vBox.getChildren().add(index - 1, pane);
		}
	}

	@Override
	public void moveDown(ConverterPane pane) {
		int index = vBox.getChildren().indexOf(pane);
		if(index != vBox.getChildren().size() - 2) {
			vBox.getChildren().remove(index);
			vBox.getChildren().add(index + 1, pane);
		}
	}
}
