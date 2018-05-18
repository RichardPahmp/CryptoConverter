package crypto.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class CryptoServer {
	private DatabaseConnection databaseConnection;
	private ServerSocket serverSocket;

	/**
	 * Create a server listening for connection on the given port.
	 * @param port
	 */
	public CryptoServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			try {
				databaseConnection = new DatabaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start listening for new connections.
	 */
	public void start() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				new ClientHandler(socket, databaseConnection).start();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CryptoServer server = new CryptoServer(3280);
	}
}
