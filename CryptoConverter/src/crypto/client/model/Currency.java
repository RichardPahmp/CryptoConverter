package crypto.client.model;

import java.io.IOException;
import java.util.ArrayList;

import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class Currency {
	
	private static CoinList coinList;
	private static ArrayList<Currency> currencyList;
	
	private String symbol;
	private String ImageUrl;
	private String coinName;
	private String coinFullName;
	
	public Currency(String symbol, String coinName, String coinNameFull) {
		this.symbol = symbol;
		this.coinName = coinName;
		this.coinFullName = coinNameFull;
	}
	
	public Currency(CoinEntry coin) {
		this.symbol = coin.symbol;
		this.coinName = coin.symbol;
		this.coinFullName = coin.fullName;
		this.ImageUrl = coin.url;
	}
	
	public Currency() {
		
	}
	
	public String getSymbol() {
		return symbol;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public String getCoinName() {
		return coinName;
	}

	public String getCoinFullName() {
		return coinFullName;
	}
	
	public String toString() {
		return this.coinFullName;
	}
	
	public static CoinList getCoinList() {
		if(Currency.coinList == null) {
			try {
				Currency.coinList = Coins.getCoinList();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfCallsException e) {
				e.printStackTrace();
			}
		}
		return coinList;
	}
	
	public static ArrayList<Currency> getCurrencyList(){
		if(coinList == null) {
			getCoinList();
		}
		if(currencyList == null) {
			currencyList = new ArrayList<Currency>();
		}
		if(currencyList.size() <= 0) {
			for(CoinEntry coin : coinList.coins.values()) {
				currencyList.add(new Currency(coin));
			}
			currencyList.sort((o1, o2) -> o1.getCoinFullName().compareTo(o2.getCoinFullName()));
			currencyList.add(0, new Currency("SEK", "Svensk krona", "Svensk krona (SEK)"));
			currencyList.add(0, new Currency("EUR", "Euro", "European Euro (EURO)"));
			currencyList.add(0, new Currency("USD", "American dollar", "American Dollar (USD)"));
		}
		return currencyList;
	}
}
