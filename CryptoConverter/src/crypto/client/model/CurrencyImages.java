package crypto.client.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javafx.scene.image.Image;

public class CurrencyImages {
	
	private static final String API_URL = "https://www.cryptocompare.com";
	
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	
	public static boolean contains(String symbol) {
		return imageMap.containsKey(symbol);
	}
	
	public static void load(String symbol, String imageURL) {
		try {
			URL url = new URL(API_URL + imageURL);
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			Image image = new Image(is, 30, 30, true, true);
			imageMap.put(symbol, image);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Image get(String symbol, String imageURL) {
		if(!imageMap.containsKey(symbol)) {
			CurrencyImages.load(symbol, imageURL);
		}
		return imageMap.get(symbol);
	}
	
	public static Image get(String symbol) {
		return imageMap.get(symbol);
	}
	
}
