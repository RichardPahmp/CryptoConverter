package crypto.server.messages;

import java.util.HashMap;

public class UserDataMessage {
	private HashMap<String, Integer> map;
	
	public UserDataMessage(HashMap<String, Integer> map) {
		this.map = map;
	}
	
	public HashMap<String, Integer> getMap(){
		return map;
	}
}
