package crypto.client.model;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class CurrencyImages {
	
	private static final String API_URL = "https://www.cryptocompare.com";
	
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	
	public static boolean contains(String symbol) {
		return imageMap.containsKey(symbol);
	}
	
	/**
	 * Loads a new image and saves it for later use.
	 * @param symbol The symbol of the coin
	 * @param imageURL The imageURL of the coin
	 */
	public static void load(String symbol, String imageURL) {
		if(imageURL == null) {
			return;
		}
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
	
	/**
	 * Gets an image from the saved images map. If it's not in the map it's loaded from the API.
	 * @param symbol
	 * @param imageURL
	 * @return
	 */
	public static Image get(String symbol, String imageURL) {
		if(!imageMap.containsKey(symbol)) {
			CurrencyImages.load(symbol, imageURL);
		}
		return imageMap.get(symbol);
	}
	
	/**
	 * Get an image from the saved images map. Will return null if the image is not loaded.
	 * @param symbol
	 * @return
	 */
	public static Image get(String symbol) {
		return imageMap.get(symbol);
	}
	
}
