package crypto.server.messages;

import java.util.HashMap;

public class AllUserDataMessage {

	private HashMap<String, Integer> map;
	
	public AllUserDataMessage(HashMap<String, Integer> map) {
		this.map = map;
	}
	
	public HashMap<String, Integer> getMap(){
		return map;
	}
	
}
