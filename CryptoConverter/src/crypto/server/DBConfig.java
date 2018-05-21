package crypto.server;

import java.io.*;
import java.util.Properties;

public class DBConfig {

	private String username;
	private String password;
	private String url;
	private String databaseName;
	private int port;

	private static String FILE_PATH = "files/DatabaseConfig.properties";

	/**
	 * A container for the settings in the ServerConfig.properties file.
	 */
	public DBConfig() {
		File file = new File(FILE_PATH);
		try {
			FileReader reader = new FileReader(file);
			Properties prop = new Properties();
			prop.load(reader);
			reader.close();
			username = prop.getProperty("DatabaseUsername");
			password = prop.getProperty("DatabasePassword");
			url = prop.getProperty("DatabaseURL");
			databaseName = prop.getProperty("DatabaseName");
			port = Integer.parseInt(prop.getProperty("DatabasePort"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public int getPort() {
		return port;
	}
}

