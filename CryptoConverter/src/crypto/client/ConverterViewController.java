package crypto.client;

import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.util.ConverterChoices;
import crypto.util.ConverterData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;

/**
 * The controller for the ConverterView.
 *
 * @author Richard
 */
public class ConverterViewController implements ConverterPaneListener {


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
			save.loadFromFile("files/save.dat");
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
	}

	/**
	 * Creates a ConverterPane and adds it to the view.
	 */
	@FXML
	private void addConverterPane() {
		ConverterPane pane = new ConverterPane(currencies);
		pane.addConverterPaneListener(this);
		vBox.getChildren().add(vBox.getChildren().size() - 1, pane);
	}

	/**
	 * Creates a ConverterPane and adds it to the view with the values in the given ConverterData.
	 *
	 * @param data The data to initialize the pane with.
	 */
	private void addConverterPane(ConverterData data) {
		ConverterPane pane = new ConverterPane(currencies);
		pane.addConverterPaneListener(this);
		pane.setConverterData(data);
		vBox.getChildren().add(vBox.getChildren().size() - 1, pane);
	}

	/**
	 * Callback for when the user presses the close button on a ConverterPane.
	 *
	 * @param pane The pane that called this listener.
	 */
	@Override
	public void closeButtonClicked(ConverterPane pane) {
		vBox.getChildren().remove(pane);
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
				pane.setRightTextfieldText(String.format("%.12f", coinValue * sum));
			} else {
				Map<String, Double> price = Historic.getPriceAtTime((int) date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC), from.getSymbol(), to.getSymbol());
				pane.setRightTextfieldText(price.get(to.getSymbol()).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				pane.setLeftTextfieldText(String.format("%.12f", coinValue * sum));
			} else {
				Map<String, Double> price = Historic.getPriceAtTime((int) date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC), from.getSymbol(), to.getSymbol());
				pane.setLeftTextfieldText(price.get(to.getSymbol()).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			save.saveToFile("files/save.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
