package crypto.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author Richard Pahmp, Emil Ögge
 *
 */
public class CryptoServer extends Thread {
	private DatabaseConnection databaseConnection;
	private ServerSocket serverSocket;

	private ServerViewController viewController;

	private ArrayList<ClientHandler> clientHandlers;
	
	private int port;
	
	private boolean running = true;

	/**
	 * Create a server listening for connection on the given port.
	 * 
	 * @param port
	 */
	public CryptoServer(ServerViewController viewController, int port) {
		this.port = port;
		clientHandlers = new ArrayList<ClientHandler>();
		this.viewController = viewController;
	}

	public void run() {
		try {
			databaseConnection = new DatabaseConnection();
		} catch (SQLException e1) {
			viewController.log("Failed to connect to database. Press restart to try again.");
			e1.printStackTrace();
			return;
		}
		viewController.log("Database connection successful");
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			viewController.log("Failed to start listen server. Press restart to try again");
			e.printStackTrace();
			return;
		}
		viewController.log("Now listening on port: " + port);

		startListenLoop();
	}

	/**
	 * Start listening for new connections.
	 */
	private void startListenLoop() {
		try {
			while (running) {
				Socket socket = serverSocket.accept();
				viewController.log("New connection received from " + socket.getInetAddress());
				ClientHandler handler = new ClientHandler(viewController, socket, databaseConnection);
				clientHandlers.add(handler);
				handler.start();
			}
		} catch (IOException e) {
			viewController.log("Listen socket stopping.");
		}
	}
	
	/**
	 * Restarts the server and closes all clienthandler connections. 
	 */
	public void onRestart() {
		viewController.log("Restarting server.");
		
		running = false;
		
		for(ClientHandler handler : clientHandlers) {
			handler.close();
		}
		
		clientHandlers.clear();
		
		if(serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets the boolean running to false and stops the serverSocket.
	 */
	public void close() {
		running = false;
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
