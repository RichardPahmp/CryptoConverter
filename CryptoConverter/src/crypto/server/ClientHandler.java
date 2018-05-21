package crypto.server;

import crypto.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Serverside connection to a client.
 * 
 * @author Emil �gge, Richard Pahmp
 *
 */
public class ClientHandler extends Thread {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DatabaseConnection db;
	private ServerViewController viewController;

	private boolean isAlive = false;
	
	private boolean loggedIn = false;
	private int id;
	private String username = "not_logged_in";

	/**
	 * Initialize a new ClientHandler.
	 * @param socket
	 * @param db
	 */
	public ClientHandler(ServerViewController viewController, Socket socket, DatabaseConnection db) {
		this.viewController = viewController;
		this.socket = socket;
		this.db = db;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		isAlive = true;
		
	}
	
	/**
	 * Stops the clienthandler thread.
	 */
	public void close() {
		isAlive = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Listen for new objects from the inputstream and handle the messages.
	 */
	public void run() {
		while (isAlive) {
			try {

				Object obj = null;
				try {
					obj = ois.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					socket.close();
					isAlive = false;
				}

				if (obj instanceof LoginMessage) {
					viewController.log("Login request received from " + socket.getInetAddress());
					if(loggedIn) continue;
					LoginMessage message = (LoginMessage) obj;
					String user = message.getUsername();
					String pass = message.getPassword();
					try {
						int tempID = db.verifyLogin(user, pass);
						if (tempID == -1) {
							LoginFailedMessage tempMess = new LoginFailedMessage();
							oos.writeObject(tempMess);
							oos.flush();
						} else {
							this.id = tempID;
							username = message.getUsername();
							loggedIn = true;
							viewController.log(socket.getInetAddress() + " logged in as " + username);
							LoginSuccessfulMessage tempMess = new LoginSuccessfulMessage(user);
							oos.writeObject(tempMess);
							oos.flush();
						}
					} catch (SQLException e) {
						LoginFailedMessage tempMess = new LoginFailedMessage();
						oos.writeObject(tempMess);
						oos.flush();
					}
				} else if (obj instanceof RegisterMessage) {
					viewController.log("Register request received from " + username);
					if(loggedIn) continue;
					RegisterMessage message = (RegisterMessage) obj;
					try {
						db.createNewUser(message.getUsername(), message.getPassword());
						RegisterSuccessfulMessage tempMess = new RegisterSuccessfulMessage();
						oos.writeObject(tempMess);
						oos.flush();
					} catch (SQLException e) {
						RegisterFailedMessage tempMess = new RegisterFailedMessage();
						oos.writeObject(tempMess);
						oos.flush();
					}
				} else if (obj instanceof RequestUserDataMessage) {
					viewController.log("userdata request received from " + username);
					if(!loggedIn) continue;
					try {
						HashMap<String, Integer> mapMe = db.getSearches(id);
						HashMap<String, Integer> mapAll = db.getAllSearches();
						UserDataMessage tempMess = new UserDataMessage(mapMe, mapAll);
						oos.writeObject(tempMess);
						oos.flush();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else if (obj instanceof SearchMessage) {
					if(!loggedIn) continue;
					viewController.log("New search logged by " + username);
					String[] symbols = ((SearchMessage) obj).getSymbols();
					for (String str : symbols) {
						try {
							db.incrementSearchHistory(id, str);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else if (obj instanceof LogoutMessage) {
					viewController.log(username + " has logged out");
					loggedIn = false;
					username = "not_logged_in";
					LogoutMessage message = new LogoutMessage();
					oos.writeObject(message);
					oos.flush();
				}
			} catch (IOException e) {
				isAlive = false;
			} 
		}
		try {
			viewController.log("Closing connection to " + username + "(" + socket.getInetAddress() + ")");
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
