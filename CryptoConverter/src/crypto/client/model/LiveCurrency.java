package crypto.client.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class which represents a currency the user wants to track in the LiveFeedView.
 *
 * @author Anto
 */

public class LiveCurrency {
	private SimpleStringProperty name;
	private SimpleStringProperty price;
	private SimpleStringProperty volume24h;
	private SimpleStringProperty marketCap;
	private SimpleStringProperty liveChange;
	private Currency currency;

	/**
	 * The object being updated in a table on the LiveFeedView, representing a currency the user wants to track.
	 * All Strings represent information about the currency fetched from the CryptoCompare API, and is updated
	 * at a regular interval set by the user in the Settings-view. The Currency-object defines what currency the
	 * Live-Currency is storing.
	 */

	public LiveCurrency(String name, String price, String volume24h,
						String marketCap, String liveChange, Currency currency) {
		this.name = new SimpleStringProperty(name);
		this.price = new SimpleStringProperty(price);
		this.volume24h = new SimpleStringProperty(volume24h);
		this.marketCap = new SimpleStringProperty(marketCap);
		this.liveChange = new SimpleStringProperty(liveChange);
		this.currency = currency;
	}

	/**
	 * Returns the currency stored in a LiveCurrency-object.
	 */

	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Returns the name of the currently tracked currency.
	 */

	public String getName() {
		return name.get();
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	/**
	 * Sets the name of the LiveCurrency-object.
	 *
	 * @param name Name of the currency being tracked.
	 */

	public void setName(String name) {
		this.name.set(name);
	}

	public String getPrice() {
		return price.get();
	}

	public SimpleStringProperty priceProperty() {
		return price;
	}

	/**
	 * Updates the current price of the tracked currency.
	 *
	 * @param price Current price fetched from the CryptoCompare API.
	 */
	public void setPrice(String price) {
		this.price.set(price);
	}

	public String getVolume24h() {
		return volume24h.get();
	}

	public SimpleStringProperty volume24hProperty() {
		return volume24h;
	}

	/**
	 * Updates the 24h worth of trading volume of the tracked currency.
	 *
	 * @param volume24h Current trading volume fetched from the CryptoCompare API.
	 */

	public void setVolume24h(String volume24h) {
		this.volume24h.set(volume24h);
	}

	public String getMarketCap() {
		return marketCap.get();
	}

	public SimpleStringProperty marketCapProperty() {
		return marketCap;
	}

	/**
	 * Updates the current market cap held by the tracked currency.
	 *
	 * @param marketCap Current market cap fetched from the CryptoCompare API.
	 */

	public void setMarketCap(String marketCap) {
		this.marketCap.set(marketCap);
	}

	public String getLiveChange() {
		return liveChange.get();
	}

	public SimpleStringProperty liveChangeProperty() {
		return liveChange;
	}

	/**
	 * Updates the percentage in price changed over 24 hours from the tracked currency.
	 *
	 * @param liveChange Percentage change in price fetched from the CryptoCompare API.
	 */

	public void setLiveChange(String liveChange) {
		this.liveChange.set(liveChange);
	}

}
