package crypto.client;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Map;

import crypto.client.model.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class ConverterViewController implements ConverterPaneListener {

	@FXML
	private VBox vBox;

	private ObservableList<Currency> currencies = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		currencies.addAll(Currency.getCurrencyList());
		addConverterPane();
	}

	@FXML
	private void addConverterPane() {
		ConverterPane pane = new ConverterPane(currencies);
		pane.addConverterPaneListener(this);
		vBox.getChildren().add(vBox.getChildren().size() - 1, pane);
	}

	@Override
	public void closeButtonClicked(ConverterPane pane) {
		vBox.getChildren().remove(pane);
	}

	@Override
	public void leftTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date) {
		try {
			if (date == null) {
				Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
				pane.setRightTextfieldText(price.get(to.getSymbol()).toString());
			} else {
				Map<String, Double> price = Historic.getPriceAtTime(
						(int) date.toEpochSecond(LocalTime.now(), ZoneOffset.UTC), from.getSymbol(), to.getSymbol());
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

	@Override
	public void rightTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum, LocalDate date) {
		try {
			if (date == null) {
				Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
				pane.setLeftTextfieldText(price.get(to.getSymbol()).toString());
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
}
