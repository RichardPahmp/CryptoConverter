package crypto.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import crypto.client.model.Currency;
import crypto.client.view.DemoViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DemoMain extends Application{

	private Stage primaryStage;
	private AnchorPane demoView;
	DemoViewController controller;
	
	private ObservableList<Currency> currencies = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
		readCurrencies("https://min-api.cryptocompare.com/data/all/coinlist");
		currencies.sort((o1, o2) -> o1.getCoinNameFull().compareTo(o2.getCoinNameFull()));
		currencies.add(0, new Currency("SEK", "Svensk krona", "Svensk krona (SEK)"));
		currencies.add(0, new Currency("EUR", "Euro", "European Euro (EURO)"));
		currencies.add(0, new Currency("USD", "American dollar", "American Dollar (USD)"));
		
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Crypto Demo");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DemoMain.class.getResource("view/DemoView.fxml"));
			demoView = (AnchorPane) loader.load();

			controller = loader.getController();
			controller.setDemoMain(this);

			Scene scene = new Scene(demoView);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readCurrencies(String url) {
		InputStream is;
		try {
			is = new URL(url).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(br);
			JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(jsonText);
				JSONObject jsonObj = (JSONObject)obj;
				JSONObject data = (JSONObject) jsonObj.get("Data");
				for(Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
					String key = (String) iterator.next();
					currencies.add(new Currency((JSONObject)data.get(key)));
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public ObservableList<Currency> getCurrencies(){
		return currencies;
	}
	
	public String exchange(String from, String to) {
		InputStream is;
		try {
			is = new URL("https://min-api.cryptocompare.com/data/price?fsym=" + from + "&tsyms=" + to).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			JSONParser parser = new JSONParser();
			String jsonText = readAll(br);
			try {
				JSONObject obj = (JSONObject) parser.parse(jsonText);
				return String.valueOf((Number)obj.get(to));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			
		}
		return to;
	}
}
