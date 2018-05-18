package crypto.messages;

import java.io.Serializable;

/**
 * A login message sent when a client wants to log into the server.
 * @author Richard
 *
 */
public class LoginMessage implements Serializable{
	
	private String username;
	private String password;
	
	public LoginMessage(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
