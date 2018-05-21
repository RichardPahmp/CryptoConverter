package crypto.server;

import java.io.*;
import java.util.Properties;

public class ServerConfig {

	private int port;

	private static String FILE_PATH = "files/ServerConfig.properties";

	/**
	 * A container for the settings in the ServerConfig.properties file.
	 */
	public ServerConfig() {
		File file = new File(FILE_PATH);
		try {
			FileReader reader = new FileReader(file);
			Properties prop = new Properties();
			prop.load(reader);
			reader.close();
			port = Integer.parseInt(prop.getProperty("PORT"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public int getPort() {
		return port;
	}
}
