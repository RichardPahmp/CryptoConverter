package crypto.client.model;

import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;

import java.io.Serializable;

public class Currency implements Serializable {
	
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
		return this.symbol;
	}
}
