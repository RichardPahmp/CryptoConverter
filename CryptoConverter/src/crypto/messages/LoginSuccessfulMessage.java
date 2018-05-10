package crypto.messages;

import java.io.Serializable;

public class LoginSuccessfulMessage implements Serializable{
	
	private String username;
	
	public LoginSuccessfulMessage(String user) {
		username = user;
	}
	
	public String getUsername() {
		return username;
	}
}
