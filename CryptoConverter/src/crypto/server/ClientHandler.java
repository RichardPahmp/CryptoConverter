package crypto.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.HashMap;

import crypto.messages.AllUserDataMessage;
import crypto.messages.LoginFailedMessage;
import crypto.messages.LoginMessage;
import crypto.messages.LoginSuccessfulMessage;
import crypto.messages.RegisterFailedMessage;
import crypto.messages.RegisterMessage;
import crypto.messages.RegisterSuccessfulMessage;
import crypto.messages.RequestAllUserDataMessage;
import crypto.messages.RequestUserDataMessage;
import crypto.messages.SearchMessage;
import crypto.messages.UserDataMessage;

/**
 * Serverside connection to a client.
 * 
 * @author Emil Ögge
 *
 */
public class ClientHandler extends Thread {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private DatabaseConnection db;

	private int id;

	public ClientHandler(ObjectInputStream ois, ObjectOutputStream oos, DatabaseConnection db) {
		this.ois = ois;
		this.oos = oos;
		this.db = db;
	}

	public void run() {
		Object obj = null;
		try {
			obj = ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			if (obj instanceof LoginMessage) {
				LoginMessage message = (LoginMessage) obj;
				String user = message.getUsername();
				String pass = message.getPassword();
				try {
					int n = db.verifyLogin(user, pass);
					if (n == -1) {
						LoginFailedMessage tempMess = new LoginFailedMessage();
						oos.writeObject(tempMess);
						oos.flush();
					}
				} catch (SQLException e) {
					LoginFailedMessage tempMess = new LoginFailedMessage();
					try {
						oos.writeObject(tempMess);
						oos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
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
					try {
						oos.writeObject(tempMess);
						oos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (obj instanceof RequestAllUserDataMessage) {
				try {
					HashMap<String, Integer> map = db.getAllSearches();
					AllUserDataMessage tempMess = new AllUserDataMessage(map);
					oos.writeObject(tempMess);
					oos.flush();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (obj instanceof RequestUserDataMessage) {
				try {
					HashMap<String, Integer> map = db.getSearches(id);
					UserDataMessage tempMess = new UserDataMessage(map);
					oos.writeObject(tempMess);
					oos.flush();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (obj instanceof SearchMessage) {
				String[] symbols = ((SearchMessage)obj).getSymbols();
				for(String str : symbols) {
					try {
						db.incrementSearchHistory(id, str);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
