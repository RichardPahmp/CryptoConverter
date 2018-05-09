package crypto.messages;

public class SearchMessage {
	
	private String[] symbols;
	
	public SearchMessage(String symbol) {
		symbols = new String[1];
		symbols[0] = symbol;
	}
	
	public SearchMessage(String symbol1, String symbol2) {
		symbols = new String[2];
		symbols[0] = symbol1;
		symbols[1] = symbol2;
	}
	
	public String[] getSymbols() {
		return symbols;
	}
	
}
