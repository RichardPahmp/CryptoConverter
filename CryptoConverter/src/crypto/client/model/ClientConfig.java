package crypto.client.model;

import java.io.*;
import java.util.Properties;

public class ClientConfig {

	public static String DEFAULT_SYMBOL;
	public static int LIVE_FEED_RATE;
	public static String STYLE_PATH;
	
	public static int PORT;
	public static String IP_ADDRESS;
	
	private static String FILE_PATH = "files/ClientSettings.properties";

	/**
	 * Saves the current settings to a savefile
	 */
	public static void saveToDisk() {
		File file = new File(FILE_PATH);
		try {
			FileWriter writer = new FileWriter(file);
			Properties prop = new Properties();
			prop.setProperty("DEFAULT_SYMBOL", DEFAULT_SYMBOL);
			prop.setProperty("LIVE_FEED_RATE", LIVE_FEED_RATE+"");
			prop.setProperty("STYLE", STYLE_PATH);
			prop.store(writer, "Client settings");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads settings from a savefile.
	 */
	public static void loadFromDisk() {
		File file = new File(FILE_PATH);
		try {
			FileReader reader = new FileReader(file);
			Properties prop = new Properties();
			prop.load(reader);
			reader.close();
			DEFAULT_SYMBOL = prop.getProperty("DEFAULT_SYMBOL");
			LIVE_FEED_RATE = Integer.parseInt(prop.getProperty("LIVE_FEED_RATE"));
			PORT = Integer.parseInt(prop.getProperty("PORT"));
			IP_ADDRESS = prop.getProperty("IP_ADDRESS");
			STYLE_PATH = prop.getProperty("STYLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
