package crypto.client.model;

import java.io.IOException;
import java.util.ArrayList;

import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class CurrencyList {

	private static CoinList coinList;
	private static ArrayList<Currency> currencyList;
	
	/**
	 * Returns a CoinList containing all availible Coins from the CryptoCompareAPI.
	 * @return CoinList A list of CoinEntry objects.
	 */
	public static CoinList getCoinList() {
		if(coinList == null) {
			try {
				coinList = Coins.getCoinList();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfCallsException e) {
				e.printStackTrace();
			}
		}
		return coinList;
	}
	
	/**
	 * Get a list of all availible CryptoCurrencies in a list of Currency objects.
	 * @return ArrayList<Currency> An arraylist of currency objects.
	 */
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
