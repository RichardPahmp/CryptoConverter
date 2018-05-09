package crypto.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CryptoServer {
	private DatabaseConnection connection;
	private ServerSocket serverSocket;

	public CryptoServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				new ConnectionHandler(socket).start();
			}
		} catch (Exception e) { }
	}

	private class ConnectionHandler extends Thread {
		private Socket socket;

		public ConnectionHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ClientHandler client = new ClientHandler(ois, oos, connection);
			    client.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		CryptoServer server = new CryptoServer(3280);
	}
}
