package crypto.messages;

import java.io.Serializable;
import java.util.HashMap;

public class AllUserDataMessage implements Serializable {

	private HashMap<String, Integer> map;
	
	public AllUserDataMessage(HashMap<String, Integer> map) {
		this.map = map;
	}
	
	public HashMap<String, Integer> getMap(){
		return map;
	}
	
}
