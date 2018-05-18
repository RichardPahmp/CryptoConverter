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
 * @author Emil �gge
 *
 */
public class ClientHandler extends Thread {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DatabaseConnection db;

	private boolean isAlive = false;
	
	private int id;

	/**
	 * Initialize a new ClientHandler.
	 * @param socket
	 * @param db
	 */
	public ClientHandler(Socket socket, DatabaseConnection db) {
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isAlive = true;
		this.db = db;
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
					try {
						HashMap<String, Integer> mapMe = db.getSearches(id);
						HashMap<String, Integer> mapAll = db.getAllSearches();
						UserDataMessage tempMess = new UserDataMessage(mapMe, mapAll);
						oos.writeObject(tempMess);
						oos.flush();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
				} else if (obj instanceof SearchMessage) {
					String[] symbols = ((SearchMessage) obj).getSymbols();
					for (String str : symbols) {
						try {
							db.incrementSearchHistory(id, str);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} 
			} catch (IOException e) {
				isAlive = false;
			} 
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
