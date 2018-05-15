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

    public LiveCurrency(String name, String price, String volume24h,
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

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getVolume24h() {
        return volume24h.get();
    }

    public SimpleStringProperty volume24hProperty() {
        return volume24h;
    }

    public void setVolume24h(String volume24h) {
        this.volume24h.set(volume24h);
    }

    public String getMarketCap() {
        return marketCap.get();
    }

    public SimpleStringProperty marketCapProperty() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap.set(marketCap);
    }

    public String getLiveChange() {
        return liveChange.get();
    }

    public SimpleStringProperty liveChangeProperty() {
        return liveChange;
    }

    public void setLiveChange(String liveChange) {
        this.liveChange.set(liveChange);
    }

}
