package crypto.client;

import java.io.IOException;
import java.util.Map;

import crypto.client.model.Currency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Market;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class ConverterViewController implements ConverterPaneListener{
	
	@FXML
	private VBox vBox;
	
	private ObservableList<Currency> currencies = FXCollections.observableArrayList();
	
	@FXML
	private void initialize(){
		try {
			Coins.CoinList coinList = Coins.getCoinList();
			for(CoinEntry coin : coinList.coins.values()) {
				currencies.add(new Currency(coin));
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OutOfCallsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//currencies.sort((o1, o2) -> o1.getCoinFullName().compareTo(o2.getCoinFullName()));
		currencies.add(0, new Currency("SEK", "Svensk krona", "Svensk krona (SEK)"));
		currencies.add(0, new Currency("EUR", "Euro", "European Euro (EURO)"));
		currencies.add(0, new Currency("USD", "American dollar", "American Dollar (USD)"));
	}
	
	@FXML
	private void addConverterPane() {
		ConverterPane pane = new ConverterPane(currencies);
		pane.addConverterPaneListener(this);
		vBox.getChildren().add(pane);
	}

	@Override
	public void closeButtonClicked(ConverterPane pane) {
		vBox.getChildren().remove(pane);
	}

	@Override
	public void leftTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum) {
		try {
			Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
			pane.setRightTextfieldText(price.get(to.getSymbol()).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void rightTextfieldAction(ConverterPane pane, Currency from, Currency to, double sum) {
		try {
			Map<String, Double> price = Market.getPrice(from.getSymbol(), to.getSymbol());
			pane.setLeftTextfieldText(price.get(to.getSymbol()).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
