package crypto.client.model;

import java.io.IOException;
import java.util.ArrayList;

import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList;
import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class Currency {
	
	private String symbol;
	private String ImageUrl;
	private String coinName;
	private String coinFullName;
	
	/**
	 * Initializes a Currency with the given values.
	 * @param symbol
	 * @param coinName
	 * @param coinNameFull
	 */
	public Currency(String symbol, String coinName, String coinNameFull) {
		this.symbol = symbol;
		this.coinName = coinName;
		this.coinFullName = coinNameFull;
	}
	
	/**
	 * Initializes a Currency with the values in the given CoinEntry.
	 * @param coin
	 */
	public Currency(CoinEntry coin) {
		this.symbol = coin.symbol;
		this.coinName = coin.symbol;
		this.coinFullName = coin.fullName;
		this.ImageUrl = coin.imageUrl;
	}
	
	/**
	 * Initializes an empty Currency.
	 */
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
}
