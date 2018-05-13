package crypto.client;

import crypto.client.model.Config;
import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import crypto.util.SearchUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

	private static ObservableList<LiveCurrency> data = FXCollections.observableArrayList();

	private static TimerTask updateTable;
	private static Timer timer = new Timer("name");

	/**
	 * Initialise a tableview in a separate tab from the main view. Sets table values to respective values in the
	 * LiveCurrency-class.
	 * Creates a TimerTask that will update values in the LiveCurrency-objects, which in turn inhabit the TableView.
	 * <p>
	 * TODO: Fix so all objects share one timer.
	 */

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TableColumn currencyName = new TableColumn("Currency");
		currencyName.setMinWidth(100);
		currencyName.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("name")
		);

		TableColumn currencyPrice = new TableColumn("Price");
		currencyPrice.setMinWidth(100);
		currencyPrice.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("price")
		);

		TableColumn currencyVolume24h = new TableColumn("Total Vol. 24H");
		currencyVolume24h.setMinWidth(150);
		currencyVolume24h.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("volume24h")
		);

		TableColumn currencyMarketCap = new TableColumn("Market Cap");
		currencyMarketCap.setMinWidth(100);
		currencyMarketCap.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("marketCap")
		);

		TableColumn currencyChange = new TableColumn("Change 24H");
		currencyChange.setMinWidth(100);
		currencyChange.setCellValueFactory(
				new PropertyValueFactory<LiveCurrency, String>("liveChange")
		);


		comboBox.getItems().addAll(CurrencyList.getCurrencyList());
		for (Currency currency : comboBox.getItems()) {
			if (currency.getSymbol().equals(Config.DEFAULT_SYMBOL)) {
				comboBox.getSelectionModel().select(currency);
				break;
			}
		}
		tableView.getColumns().addAll(currencyName, currencyPrice, currencyVolume24h, currencyMarketCap, currencyChange);

		new SearchUtil<>(comboBox);

		if (data != null) {
			updateTable = new TimerTask() {
				@Override
				public void run() {
					updateTable(data);
				}
			};
			long delay = Config.LIVE_FEED_RATE;
			long period = Config.LIVE_FEED_RATE;

			timer.scheduleAtFixedRate(updateTable, delay * 1000, period * 1000);
		}
	}

	/**
	 * Creates and instantiates LiveCurrency-object from the chosen currency one wants to track, and adds it to a list.
	 */

	@FXML
	private void onAddButtonClick() {
		Currency currency = comboBox.getValue();

		String name = getName(currency);
		String price = getPrice(currency);
		String volume = getVolume(currency);
		String marketCap = getMarketCap(currency);
		String change = getChange(currency);

		LiveCurrency newCurrency = new LiveCurrency(name, price, volume, marketCap, change, currency);

		data.add(newCurrency);
	}

	/**
	 * Updates the TableView with a synchronized function that fetches information through the API,
	 * for all values in the tracked LiveCurrency-objects.
	 *
	 * @param list = The list of LiveCurrency-Objects to be tracked.
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
	 * Recursively executes all methods that fetch information through the API,
	 * and sets these values to the corresponding LiveCurrency values.
	 *
	 * @param currency = The LiveCurrency-object that is being tracked.
	 */

	private void updateObjects(LiveCurrency currency) {
		currency.setName(getName(currency.getCurrency()));
		currency.setPrice(getPrice(currency.getCurrency()));
		currency.setVolume24h(getVolume(currency.getCurrency()));
		currency.setMarketCap(getMarketCap(currency.getCurrency()));
		currency.setLiveChange(getChange(currency.getCurrency()));
	}

    /**
     * Removes the tracked currency from the TableView and notifies the user if no currency is selected.
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
     *  Returns the name of the currently tracked currency.
     */

	private String getName(Currency currency) {
		return currency.getCoinFullName();
	}

    /**
     * Return the current price of the tracked currency.
     */

	private String getPrice(Currency currency) {
		double convert = 0;
		NumberFormat formatter = new DecimalFormat("$###,###.##");

		try {
			convert = Market.getPrice(currency.getSymbol(), "USD").get("USD");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}

		String price = formatter.format(convert);

		return price;
	}

    /**
     * Returns the trading volume from a 24 hour-period of the tracked currency.
     */

	private String getVolume(Currency currency) {
		String[] usd = {"USD"};
		String[] btc = {"BTC"};
		double convert = 0;
		NumberFormat formatter = new DecimalFormat("$###,###,###.###");


		try {
			convert = Market.getMultiFull(btc, usd).get("BTC").get("USD").totalVolume24Hour;
			convert *= Market.getPrice(currency.getSymbol(), "USD").get("USD");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}

		return formatter.format(convert);

	}

    /**
     * Returns the current market cap of the tracked currency.
     */

	private String getMarketCap(Currency currency) {
		String[] usd = {"USD"};
		String[] btc = {"BTC"};
		double convert = 0;
		NumberFormat formatter = new DecimalFormat("$###,###,###.###");

		try {
			convert = Market.getMultiFull(btc, usd).get("BTC").get("USD").marketCap;
			convert *= Market.getPrice(currency.getSymbol(), "USD").get("USD");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}
		return formatter.format(convert);
	}

    /**
     *  Returns a percentage change over a 24 hour-period of the tracked currency.
     */

	private String getChange(Currency currency) {
		double currentPrice;
		double comparePrice;
		double priceDifference = 0;
		NumberFormat formatter = new DecimalFormat("#0.00");

		try {
			currentPrice = Market.getPrice(currency.getSymbol(), "USD").get("USD");
			comparePrice = Historic.getHour(currency.getSymbol(), "USD", 24).data.get(0).close;

			priceDifference = ((currentPrice - comparePrice) / comparePrice) * 100;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			System.out.println(e.getMessage());
		}
		if (priceDifference > 9.99 || priceDifference < -9.99)
			formatter = new DecimalFormat("#00.00");
		if (priceDifference > 99.99 || priceDifference < -99.99)
			formatter = new DecimalFormat("#000.00");
		if (priceDifference > 0)
			return " + " + formatter.format(priceDifference) + " %";
		else
			return formatter.format(priceDifference) + " %";
	}

    /**
     * Class which represents a currency the user wants to track in the LiveFeedView.
     * @author Anto
     */

	public class LiveCurrency {
		private SimpleStringProperty name;
		private SimpleStringProperty price;
		private SimpleStringProperty volume24h;
		private SimpleStringProperty marketCap;
		private SimpleStringProperty liveChange;
		private Currency currency;

		LiveCurrency(String name, String price, String volume24h,
					 String marketCap, String liveChange, Currency currency) {
			this.name = new SimpleStringProperty(name);
			this.price = new SimpleStringProperty(price);
			this.volume24h = new SimpleStringProperty(volume24h);
			this.marketCap = new SimpleStringProperty(marketCap);
			this.liveChange = new SimpleStringProperty(liveChange);
			this.currency = currency;
		}

		public Currency getCurrency() {
			return currency;
		}


		public String getName() {
			return name.get();
		}

		public SimpleStringProperty nameProperty() {
			return name;
		}

		void setName(String name) {
			this.name.set(name);
		}

		public String getPrice() {
			return price.get();
		}

		public SimpleStringProperty priceProperty() {
			return price;
		}

		void setPrice(String price) {
			this.price.set(price);
		}

		public String getVolume24h() {
			return volume24h.get();
		}

		public SimpleStringProperty volume24hProperty() {
			return volume24h;
		}

		void setVolume24h(String volume24h) {
			this.volume24h.set(volume24h);
		}

		public String getMarketCap() {
			return marketCap.get();
		}

		public SimpleStringProperty marketCapProperty() {
			return marketCap;
		}

		void setMarketCap(String marketCap) {
			this.marketCap.set(marketCap);
		}

		public String getLiveChange() {
			return liveChange.get();
		}

		public SimpleStringProperty liveChangeProperty() {
			return liveChange;
		}

		void setLiveChange(String liveChange) {
			this.liveChange.set(liveChange);
		}
	}
}

