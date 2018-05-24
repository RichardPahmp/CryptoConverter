package crypto.client;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import crypto.client.model.ClientConfig;
import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.client.model.LiveCurrency;
import crypto.util.ComboboxUtil;
import crypto.util.LiveCurrencyData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

/**
 * Controller class for the LiveFeedView.
 *
 * @author Anto
 */

public class LivefeedViewController implements Initializable {

	@FXML
	private ComboBox<Currency> comboBox;

	@FXML
	private TableView<LiveCurrency> tableView;

	private MainController mainController;

	private static ObservableList<LiveCurrency> data = FXCollections
			.observableArrayList();

	private static TimerTask updateTable;
	private static Timer timer = new Timer("name");

	private NumberFormat priceFormat = new DecimalFormat("$###,###.##");
	private NumberFormat volumeFormat = new DecimalFormat("$###,###,###.###");
	private NumberFormat marketCapFormat = new DecimalFormat(
			"$###,###,###.###");
	private NumberFormat changeFormat = new DecimalFormat("#0.00");

	/**
	 * Initialise a tableview in a separate tab from the main view. Sets table
	 * values to respective values in the LiveCurrency-class. Creates a
	 * TimerTask that will update values in the LiveCurrency-objects, which in
	 * turn inhabit the TableView.
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TableColumn currencyName = new TableColumn("Currency");
		currencyName.setMinWidth(100);
		currencyName.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("name"));

		TableColumn currencyPrice = new TableColumn("Price");
		currencyPrice.setMinWidth(100);
		currencyPrice.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("price"));

		TableColumn currencyVolume24h = new TableColumn("Total Vol. 24H");
		currencyVolume24h.setMinWidth(150);
		currencyVolume24h.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("volume24h"));

		TableColumn currencyMarketCap = new TableColumn("Market Cap");
		currencyMarketCap.setMinWidth(150);
		currencyMarketCap.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("marketCap"));

		TableColumn currencyChange = new TableColumn("Change 24H");
		currencyChange.setMinWidth(100);
		currencyChange.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("liveChange"));

		comboBox.getItems().addAll(CurrencyList.getCurrencyList());
		for (Currency currency : comboBox.getItems()) {
			if (currency.getSymbol().equals(ClientConfig.DEFAULT_SYMBOL)) {
				comboBox.getSelectionModel().select(currency);
				break;
			}
		}
		tableView.getColumns().addAll(currencyName, currencyPrice,
				currencyVolume24h, currencyMarketCap, currencyChange);

		ComboboxUtil.setupCombobox(comboBox);

		loadLiveCurrencies();

		if (data != null) {
			updateTable = new TimerTask() {
				@Override
				public void run() {
					updateTable(data);
				}
			};
			long period = ClientConfig.LIVE_FEED_RATE;

			timer.scheduleAtFixedRate(updateTable, 0, period * 1000);
		}
	}

	/**
	 * Updates the TableView with a synchronized function that fetches
	 * information through the API, for all values in the tracked
	 * LiveCurrency-objects.
	 *
	 * @param list
	 *            = The list of LiveCurrency-Objects to be tracked.
	 */

	private void updateTable(List<LiveCurrency> list) {

		list = Collections.synchronizedList(new ArrayList<LiveCurrency>());

		for (LiveCurrency currency : data)
			if (!list.contains(currency))
				list.add(currency);

		synchronized (list) {
			for (LiveCurrency currency : list) {
				updateObjects(currency);
			}
		}
		tableView.setItems(data);
	}

	/**
	 * Stores Currency-objects derived from LiveCurrency-objects into a file.
	 */

	@FXML
	public void storeLiveCurrencies() {

		LiveCurrencyData save = new LiveCurrencyData();

		for (LiveCurrency inCurrency : data) {
			Currency currency = inCurrency.getCurrency();
			if (data != null) {
				save.addData(currency);
			}
		}
		try {
			save.saveToFile("files/liveSave.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads Currency-objects into LiveCurrency-objects from a file.
	 */

	private void loadLiveCurrencies() {
		LiveCurrencyData save = new LiveCurrencyData();

		try {
			save.loadFromFile("files/liveSave.dat");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		Iterator<Currency> iterator = save.getIterator();
		if (iterator.hasNext()) {
			while (iterator.hasNext()) {
				addButton(iterator.next());
			}
		} else {
			return;
		}
	}

	/**
	 * Creates and instantiates a LiveCurrency-object from the chosen currency
	 * loaded from a file.
	 */

	private void addButton(Currency inCurrency) {
		Market.toSym convert = null;

		String[] usd = {"USD"};
		String[] toSym = {inCurrency.getSymbol()};

		try {
			convert = Market.getMultiFull(toSym, usd)
					.get(inCurrency.getSymbol()).get("USD");
		} catch (IOException | OutOfCallsException e) {
			e.printStackTrace();
		}

		String name = getName(inCurrency);
		String price = priceFormat.format(convert.price);
		String volume = volumeFormat.format(convert.totalVolume24HourTo);
		String marketCap = marketCapFormat.format(convert.marketCap);
		String liveChange = null;

		if (convert.change24Hour > 0) {
			liveChange = "+" + changeFormat.format(convert.changePct24Hour)
					+ "%";
		}

		LiveCurrency newCurrency = new LiveCurrency(name, price, volume,
				marketCap, liveChange, inCurrency);

		data.add(newCurrency);
	}

	/**
	 * Creates and instantiates LiveCurrency-object from the chosen currency one
	 * wants to track, and adds it to a list. Fetches information from the API
	 * and gives the new LiveCurrency-object those values.
	 */

	@FXML
	private void onAddButtonClick() {
		Currency currency = comboBox.getValue();

		Market.toSym convert = null;

		String[] usd = {"USD"};
		String[] toSym = {currency.getSymbol()};

		try {
			convert = Market.getMultiFull(toSym, usd).get(currency.getSymbol())
					.get("USD");
		} catch (IOException | OutOfCallsException | NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Invalid selection");
			alert.setHeaderText("The currency selected is invalid.");
			alert.setContentText(
					"Data for the selected currency could not be fetched.");
			alert.showAndWait();
		}

		String name = getName(currency);
		String price = priceFormat.format(convert.price);
		String volume = volumeFormat.format(convert.totalVolume24HourTo);
		String marketCap = marketCapFormat.format(convert.marketCap);
		String liveChange = changeFormat.format(convert.changePct24Hour) + "%";

		LiveCurrency newCurrency = new LiveCurrency(name, price, volume,
				marketCap, liveChange, currency);

		data.add(newCurrency);
	}

	/**
	 * Fetches information from the API about the current price, volume over 24
	 * hours, market cap and percentage change over 24 hours. and sets these
	 * values to the corresponding LiveCurrency values.
	 *
	 * @param inCurrency
	 *            = The LiveCurrency-object that is being tracked.
	 */

	private void updateObjects(LiveCurrency inCurrency) {
		Market.toSym convert = null;
		Currency currency = inCurrency.getCurrency();

		String[] usd = {"USD"};
		String[] toSym = {currency.getSymbol()};

		try {
			convert = Market.getMultiFull(toSym, usd).get(currency.getSymbol())
					.get("USD");
		} catch (IOException | OutOfCallsException e) {
			e.printStackTrace();
		}

		inCurrency.setPrice(priceFormat.format(convert.price));
		inCurrency
				.setVolume24h(volumeFormat.format(convert.totalVolume24HourTo));
		inCurrency.setMarketCap(marketCapFormat.format(convert.marketCap));
		inCurrency.setLiveChange(
				changeFormat.format(convert.changePct24Hour) + "%");
	}

	/**
	 * Removes the tracked currency from the TableView and notifies the user if
	 * no currency is selected.
	 */

	@FXML
	private void handleDeleteCurrency() {
		LiveCurrency currency = tableView.getSelectionModel().getSelectedItem();
		if (currency != null) {
			tableView.getItems().remove(currency);
			data.remove(currency);
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("No Selection");
			alert.setHeaderText("No currency Selected");
			alert.setContentText("Please select a currency in the table.");
			alert.showAndWait();
		}
	}

	/**
	 * Connects this controller to the MainController.
	 */

	void setMainController(MainController controller) {
		this.mainController = controller;
	}

	/**
	 * Returns the name of the currently tracked currency.
	 */

	private String getName(Currency currency) {
		return currency.getCoinFullName();
	}

	public void addCurrency(LiveCurrency currency) {
		data.add(currency);
	}

	/**
	 * Stops the timer in control of updating the values in the table.
	 */

	public void onClose() {
		timer.cancel();
	}
}
