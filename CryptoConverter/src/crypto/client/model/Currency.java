package crypto.client.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.joshmcfarlin.CryptoCompareAPI.Coins.CoinList.CoinEntry;

public class Currency {
	
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
}
