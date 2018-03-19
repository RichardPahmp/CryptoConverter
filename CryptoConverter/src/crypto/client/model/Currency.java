package crypto.client.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Currency {
	
	private String symbol;
	private String ImageUrl;
	private String coinName;
	private String coinNameFull;
	
	public Currency(JSONObject obj) {
		this.symbol = (String) obj.get("Symbol");
		this.coinName = (String) obj.get("CoinName");
		this.coinNameFull = (String) obj.get("FullName");
		this.ImageUrl = (String) obj.get("ImageUrl");
	}
	
	public Currency(String symbol, String coinName, String coinNameFull) {
		this.symbol = symbol;
		this.coinName = coinName;
		this.coinNameFull = coinNameFull;
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

	public String getCoinNameFull() {
		return coinNameFull;
	}
	
	public String toString() {
		return this.coinNameFull;
	}
}
