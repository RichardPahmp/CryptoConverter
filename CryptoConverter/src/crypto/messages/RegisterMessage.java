package crypto.messages;

public class RegisterMessage {
	
	public String username;
	public String password;
	
	public RegisterMessage(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
}
