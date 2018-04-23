package crypto.client;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import crypto.client.model.Currency;
import crypto.client.model.CurrencyList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

/**
 * The controller for the GraphView.
 * @author Richard
 *
 */
public class GraphViewController implements Initializable {

	@FXML
	private LineChart<String, Number> chart;
	
	@FXML
	private ComboBox<Currency> comboBox;
	
	@FXML
	private ListView<ToggleButton> listView;
	
	@FXML
	private DatePicker datePickerFrom;
	
	@FXML
	private DatePicker datePickerTo;
	
	/**
	 * Called by javaFX when the view has finished loading.
	 * Initializes the graph.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Value in USD");
		xAxis.setLabel("Day");
		chart.setTitle("CryptoGraph");
		chart.setCursor(Cursor.CROSSHAIR);
		chart.setCreateSymbols(false);

		comboBox.getItems().addAll(CurrencyList.getCurrencyList());
		
		XYChart.Series series = new XYChart.Series();
		
		try {
			History history = Historic.getDay("BTC", "USD", 200);
			for(History.Data data : history.data) {
				Date time = new Date(data.time * 1000l);
				XYChart.Data chartData = new XYChart.Data(time.toString(), data.close);
				chartData.setNode(new HoverNode(data.close));
				series.getData().add(chartData);
			}
			chart.getData().add(series);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called when the Add-button is clicked
	 */
	@FXML
	private void onAddButtonClick() {
		Currency currency = comboBox.getValue();
		try {
			History history = Historic.getDay(currency.getSymbol(), "USD", 2000);
			ToggleButton button = new ToggleButton(currency.getCoinFullName());
			button.setUserData(new GraphButtonData(currency, history));
			button.setOnAction(e -> onToggleButtonAction((GraphButtonData)button.getUserData()));
			listView.getItems().add(button);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Called when a ToggleButton in the listview is pressed
	 * @param data The GraphButtonData held by the calling button
	 */
	private void onToggleButtonAction(GraphButtonData data) {
		System.out.println(data.getCurrency().toString());
	}
	
	/**
	 * An inner class holding the data for a currency. Used to bind a button a currency.
	 * @author Richard
	 *
	 */
	private class GraphButtonData {
		
		private Currency currency;
		private History history;
		
		public GraphButtonData(Currency currency, History history) {
			this.currency = currency;
			this.history = history;
		}
		
		public Currency getCurrency() {
			return currency;
		}
		
		public History getHistory() {
			return history;
		}
		
	}
	
	/**
	 * A Node for showing graph values when mousing over nodes in the graph.
	 * @author Richard
	 *
	 */
	private class HoverNode extends StackPane {
		public HoverNode(double value) {
			setPrefSize(10, 10);
			
			Label label = new Label(value+"");
			label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
			label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
			label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");
			
			setOnMouseEntered((e) -> {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			});
			
			setOnMouseExited((e) -> {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			});
		}
	}
}
