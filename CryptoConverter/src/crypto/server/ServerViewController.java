package crypto.server;


import crypto.util.Handler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * 
 * @author Richard
 *
 */
public class ServerViewController {
	
	private Handler restartHandler;
	
	@FXML
	private TextArea textArea;
	
	/**
	 * Log a message to the serverview.
	 * @param str
	 */
	public void log(String str) {
		textArea.appendText(str + "\n");
	}
	
	/**
	 * sets a handler for when the restart button is pressed.
	 * @param h
	 */
	public void setOnRestart(Handler h) {
		this.restartHandler = h;
	}
	
	@FXML
	private void onRestart() {
		if(restartHandler != null) {
			restartHandler.handle();
		}
	}
	
	/**
	 * Clear the text in the view when the clear button is pressed.
	 */
	@FXML
	private void onClear() {
		textArea.clear();
	}
	
}
