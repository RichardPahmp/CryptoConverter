package crypto.messages;

import java.io.Serializable;

public class RegisterMessage implements Serializable {
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public RegisterMessage(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
}
