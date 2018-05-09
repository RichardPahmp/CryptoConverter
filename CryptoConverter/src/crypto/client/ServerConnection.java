package crypto.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import crypto.messages.AllUserDataMessage;
import crypto.messages.LoginMessage;
import crypto.messages.LoginSuccessfulMessage;
import crypto.messages.RegisterFailedMessage;
import crypto.messages.RegisterMessage;
import crypto.messages.RegisterSuccessfulMessage;
import crypto.messages.RequestAllUserDataMessage;
import crypto.messages.RequestUserDataMessage;
import crypto.messages.UserDataMessage;

public class ServerConnection {
	
	private MainController mainController;
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream  oos;
	
	
	public ServerConnection(MainController controller) {
		mainController = controller;
	}
	
	public void connect() {
		try {
			socket = new Socket("localhost", 3280);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new Thread(this::run).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void login(String user, String pass) {
		if(socket == null) {
			connect();
		}
		LoginMessage message = new LoginMessage(user, pass);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void register(String user, String pass) {
		if(socket == null) {
			connect();
		}
		RegisterMessage message = new RegisterMessage(user, pass);
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void requestUserData() {
		RequestUserDataMessage message = new RequestUserDataMessage();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void requestAllUserData() {
		RequestAllUserDataMessage message = new RequestAllUserDataMessage();
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void run() {
		while(true) {
			Object obj = null;;
			try {
				obj = ois.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(obj == null) {
				continue;
			}
			
			if(obj instanceof LoginSuccessfulMessage) {
				mainController.onLoginSuccess();
			} else if(obj instanceof RegisterSuccessfulMessage) {
				mainController.onRegisterSuccess();
			} else if(obj instanceof RegisterFailedMessage){ 
				mainController.onRegisterFailed();
			} else if(obj instanceof UserDataMessage) {
				HashMap<String, Integer> map = ((UserDataMessage)obj).getMap();
				mainController.onUserDataReceived(map);
			} else if(obj instanceof AllUserDataMessage) {
				HashMap<String, Integer> map = ((UserDataMessage)obj).getMap();
				mainController.onAllUserDataReceived(map);
			}
		}
	}
	
}
