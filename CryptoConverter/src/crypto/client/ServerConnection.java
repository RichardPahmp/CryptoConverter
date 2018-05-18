package crypto.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import crypto.messages.LoginFailedMessage;
import crypto.messages.LoginMessage;
import crypto.messages.LoginSuccessfulMessage;
import crypto.messages.LogoutMessage;
import crypto.messages.NewTrackerMessage;
import crypto.messages.RegisterFailedMessage;
import crypto.messages.RegisterMessage;
import crypto.messages.RegisterSuccessfulMessage;
import crypto.messages.RequestUserDataMessage;
import crypto.messages.SearchMessage;
import crypto.messages.UserDataMessage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ServerConnection {
	
	private MainController mainController;
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream  oos;
	
	private boolean isAlive = false;
	
	public ServerConnection(MainController controller) {
		mainController = controller;
	}
	
	public boolean isConnected() {
		return isAlive;
	}
	
	public boolean connect() {
		if(!isAlive) {
			try {
				if(socket != null) {
					socket.close();
				}
				socket = new Socket("localhost", 3280);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				isAlive = true;
				new Thread(this::run).start();
				return true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
//				Alert alert = new Alert(AlertType.WARNING);
//				alert.setTitle("Failed to connect to the server.");
//				alert.setHeaderText("No connection could be made to the server");
//				alert.setContentText("Try again later");
//				alert.showAndWait();
			} 
		}
		return false;
	}
	
	public void disconnect() {
		if(isAlive) {
			LogoutMessage message = new LogoutMessage();
			try {
				oos.writeObject(message);
				oos.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				onDisconnect();
			}
		}
	}
	
	public void close() {
		try {
			if(socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isAlive = false;
	}
	
	public void login(String user, String pass) {
		LoginMessage message = new LoginMessage(user, pass);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			onDisconnect();
		}
	}
	
	public void register(String user, String pass) {
		RegisterMessage message = new RegisterMessage(user, pass);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			onDisconnect();
		}
	}
	
	public void newSearch(String[] symbols) {
		SearchMessage message = new SearchMessage(symbols);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			onDisconnect();
		}
	}
	
	public void requestUserData() {
		RequestUserDataMessage message = new RequestUserDataMessage();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			onDisconnect();
		}
	}
	
	private void onDisconnect() {
		mainController.onDisconnect();
		try {
			isAlive = false;
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void sendTracker(String symbol, String email, double limit) {
		NewTrackerMessage message = new NewTrackerMessage(symbol, email, limit);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void run() {
		while(isAlive) {
			Object obj = null;;
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				onDisconnect();
			}
			
			if(obj instanceof LoginSuccessfulMessage) {
				LoginSuccessfulMessage message = (LoginSuccessfulMessage)obj;
				mainController.onLoginSuccess(message.getUsername());
			} else if(obj instanceof LoginFailedMessage) {
				mainController.onLoginFailed();
			} else if(obj instanceof RegisterSuccessfulMessage) {
				mainController.onRegisterSuccess();
			} else if(obj instanceof RegisterFailedMessage){ 
				mainController.onRegisterFailed();
			} else if(obj instanceof UserDataMessage) {
				UserDataMessage message = (UserDataMessage)obj;
				mainController.onUserDataReceived(message.getMapMe(), message.getMapAll());
			}
		}
	}
	
}
