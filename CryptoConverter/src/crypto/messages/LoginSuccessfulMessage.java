package crypto.messages;

import java.io.Serializable;

/**
 * A message sent to the client when a login request was successful
 * @author Richard
 *
 */
public class LoginSuccessfulMessage implements Serializable{
	
	private String username;
	
	public LoginSuccessfulMessage(String user) {
		username = user;
	}
	
	public String getUsername() {
		return username;
	}
}
