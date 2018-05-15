package crypto.messages;

public class NewTrackerMessage {
	
	private String symbol;
	private String email;
	private double limit;
	
	public NewTrackerMessage(String symbol, String email, double limit) {
		this.symbol = symbol;
		this.email = email;
		this.limit = limit;
	}
	
}