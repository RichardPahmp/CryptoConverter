package crypto.messages;

import java.io.Serializable;
import java.util.HashMap;

/**
 * An update containing user statistics data.
 * @author Richard
 *
 */
public class UserDataMessage implements Serializable {
	private HashMap<String, Integer> mapAll;
	private HashMap<String, Integer> mapMe;
	
	public UserDataMessage(HashMap<String, Integer> mapMe, HashMap<String, Integer> mapAll) {
		this.mapMe = mapMe;
		this.mapAll = mapAll;
	}
	
	public HashMap<String, Integer> getMapAll() {
		return mapAll;
	}
	public HashMap<String, Integer> getMapMe() {
		return mapMe;
	}
	
}
