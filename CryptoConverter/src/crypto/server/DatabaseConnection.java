package crypto.server;

import java.sql.*;
import java.util.HashMap;
import java.util.Properties;

public class DatabaseConnection {

	private static String dbName;
	private static String URL;
	
	private static String USERS_TABLE = "USERS";
	private static String SEARCHES_TABLE = "SEARCHES";
	
	private Connection connection;
	
	private PreparedStatement createUserStatement;
	private PreparedStatement incrementSearchesStatement;
	private PreparedStatement insertSearchesStatement;
	private PreparedStatement getSearchesStatement;
	private PreparedStatement getAllSearchesStatement;
	private PreparedStatement searchesRowExists;
	private PreparedStatement verifyLoginStatement;
	
	/**
	 * Create a new DatabaseConnection
	 * @throws SQLException
	 */
	public DatabaseConnection() throws SQLException {
		DBConfig config = new DBConfig();
		dbName = config.getDatabaseName();
		URL = "jdbc:mysql://" + config.getUrl() + ":" + config.getPort() + "/" + dbName + "?useSSL=false";
		
		Properties props = new Properties();
		props.put("user", config.getUsername());
		props.put("password", config.getPassword());
		DriverManager.setLoginTimeout(5);
		connection = DriverManager.getConnection(URL, props);
		connection.setAutoCommit(false);
		
		createUserStatement = connection.prepareStatement("insert into " + dbName + "." + USERS_TABLE + " (USERNAME, PASSWORD) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
		verifyLoginStatement = connection.prepareStatement("SELECT ID, PASSWORD FROM " + dbName + "." + USERS_TABLE + " WHERE (USERNAME = ?)");
		incrementSearchesStatement = connection.prepareStatement("UPDATE " + dbName + "." + SEARCHES_TABLE + " SET SEARCHES = SEARCHES + 1 WHERE (ID = ? AND SYMBOL = ?)");
		insertSearchesStatement = connection.prepareStatement("INSERT INTO " + dbName + "." + SEARCHES_TABLE + " (ID, SYMBOL) VALUES (?, ?)");
		getSearchesStatement = connection.prepareStatement("SELECT SYMBOL, SEARCHES FROM " + dbName + "." + SEARCHES_TABLE + " WHERE id = ?");
		getAllSearchesStatement = connection.prepareStatement("SELECT SYMBOL, SEARCHES FROM " + dbName + "." + SEARCHES_TABLE);
		searchesRowExists = connection.prepareStatement("SELECT SEARCHES FROM " + dbName + "." + SEARCHES_TABLE + " WHERE (ID = ? AND SYMBOL = ?)");
	}
	
	/**
	 * Creates a new user in the database.
	 * @param username
	 * @param password
	 * @return The id of the created user. returns -1 if no id was returned from the database.
	 * @throws SQLException
	 */
	public int createNewUser(String username, String password) throws SQLException {
		createUserStatement.setString(1, username);
		createUserStatement.setString(2, password);
		createUserStatement.executeUpdate();
		connection.commit();
		ResultSet generatedKeys = createUserStatement.getGeneratedKeys();
		if(generatedKeys.next()) {
			return generatedKeys.getInt(1);
		}
		return -1;
	}
	
	/**
	 * Returns if the given user and password combo exists in the database.
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public int verifyLogin(String username, String password) throws SQLException {
		verifyLoginStatement.setString(1, username);
		ResultSet results = verifyLoginStatement.executeQuery();
		if(results.next()) {
			int id = results.getInt(1);
			String pass = results.getString(2);
			if(pass.equals(password)) {
				return id;
			}
		}
		return -1;
	}
	
	/**
	 * Create a new row in the SEARCHES table with the given parameters.
	 * @param id
	 * @param symbol
	 * @throws SQLException
	 */
	private void insertSearchHistory(int id, String symbol) throws SQLException {
		insertSearchesStatement.setInt(1, id);
		insertSearchesStatement.setString(2, symbol);
		insertSearchesStatement.executeUpdate();
		connection.commit();
	}
	
	/**
	 * Increments the number of times the user has searched for the given symbol
	 * @param id The ID of the user
	 * @param symbol The symbol that was searched for.
	 * @throws SQLException 
	 */
	public void incrementSearchHistory(int id, String symbol) throws SQLException {
		searchesRowExists.setInt(1, id);
		searchesRowExists.setString(2, symbol);
		ResultSet results = searchesRowExists.executeQuery();
		if(results.next()) {
			incrementSearchesStatement.setInt(1, id);
			incrementSearchesStatement.setString(2, symbol);
			incrementSearchesStatement.executeUpdate();
			connection.commit();
		} else {
			insertSearchHistory(id, symbol);
		}
	}
	
	/**
	 * Returns a HashMap where the values are currency symbols and the value is how many searches the user has made for that symbol.
	 * @param id The id for the user.
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, Integer> getSearches(int id) throws SQLException {
		getSearchesStatement.setInt(1, id);
		ResultSet results = getSearchesStatement.executeQuery();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while(results.next()) {
			String symbol = results.getString(1);
			int searches = results.getInt(2);
			map.put(symbol, searches);
		}
		return map;
	}
	
	/**
	 * Returns a HashMap where the values are currency symbols and the value is how many searches have been made for that symbol.
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, Integer> getAllSearches() throws SQLException{
		ResultSet results = getAllSearchesStatement.executeQuery();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while(results.next()) {
			String symbol = results.getString(1);
			int searches = results.getInt(2);
			if(map.containsKey(symbol)) {
				int oldSearches = map.get(symbol);
				map.put(symbol, oldSearches + searches);
			} else {
				map.put(symbol, searches);
			}
		}
		return map;
	}
}
