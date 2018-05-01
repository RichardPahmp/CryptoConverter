package crypto.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import crypto.client.model.Currency;
import crypto.client.model.CurrencyImages;
import crypto.client.model.CurrencyList;
import crypto.util.TimeUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private CategoryAxis xAxis;
	
	@FXML
	private NumberAxis yAxis;
	
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
		yAxis.setLabel("Value in USD");
		xAxis.setLabel("Day");
		chart.setTitle("CryptoGraph");
		chart.setCursor(Cursor.CROSSHAIR);
		chart.setCreateSymbols(false);
		
		comboBox.getItems().addAll(CurrencyList.getCurrencyList());
		
		datePickerFrom.setValue(LocalDate.now().minusMonths(1));
		datePickerTo.setValue(LocalDate.now());
		datePickerFrom.setOnAction(e -> onDateChanged());
		datePickerTo.setOnAction(e -> onDateChanged());
		
		
		/*XYChart.Series series = new XYChart.Series();
		
		try {
			History history = Historic.getDay("BTC", "USD", 200);
			for(History.Data data : history.data) {
				LocalDate date = TimeUtil.timestampToDate(data.time * 1000L);
				XYChart.Data chartData = new XYChart.Data(date.toString(), data.open);
				//chartData.setNode(new HoverNode(data.close));
				series.getData().add(chartData);
				
			}
			chart.getData().add(series);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Called when the Add-button is clicked
	 */
	@FXML
	private void onAddButtonClick() {
		Currency currency = comboBox.getValue();
		try {
			Image image = CurrencyImages.get(currency.getSymbol(), currency.getImageUrl());
			History history = Historic.getDayAllData(currency.getSymbol(), "USD");
			ToggleButton button = new ToggleButton(currency.getCoinFullName(), new ImageView(image));
			button.setMaxWidth(listView.getWidth());
			button.setAlignment(Pos.BASELINE_LEFT);
			button.textOverrunProperty().setValue(OverrunStyle.WORD_ELLIPSIS);
			button.setUserData(new GraphButtonData(currency, history));
			button.setOnAction(e -> onToggleButtonAction((GraphButtonData)button.getUserData(), button.isSelected()));
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
	private void onToggleButtonAction(GraphButtonData data, boolean isSelected) {
		if(isSelected) {
			XYChart.Series series = createSeries(data.currency.getSymbol(), data.history);
//			series.setName(data.currency.getSymbol());
//			series.getData().add(createFilteredGraphData(data.history, datePickerFrom.getValue(), datePickerTo.getValue()));
			data.series = series;
			chart.getData().add(series);
		} else {
			chart.getData().remove(data.series);
		}
	}
	
	/**
	 * Run when a the date in a datepicker is changed
	 */
	private void onDateChanged() {
		chart.getData().clear();
		for(ToggleButton toggleButton : listView.getItems()) {
			if(toggleButton.isSelected()) {
				GraphButtonData data = (GraphButtonData)toggleButton.getUserData();
				XYChart.Series series = createSeries(data.currency.getSymbol(), data.history);
				data.series = series;
				chart.getData().add(series);
			}
		}
	}
	
	private XYChart.Series createSeries(String name, History history){
		XYChart.Series series = new XYChart.Series();
		series.setName(name);
		series.getData().addAll(createFilteredGraphData(history, datePickerFrom.getValue(), datePickerTo.getValue()));
		return series;
	}
	
	/**
	 * Takes a History object and two dates. Returns a list of XYChart.Data objects between filtered by the dates.
	 */
	private ObservableList<XYChart.Data> createFilteredGraphData(History history, LocalDate fromDate, LocalDate toDate) {
		ObservableList<XYChart.Data> list = FXCollections.observableArrayList();
		for(History.Data data : history.data) {
			LocalDate dataTime = TimeUtil.timestampToDate(data.time * 1000L);
			if(dataTime.compareTo(fromDate) > 0 && dataTime.compareTo(toDate) < 0) {
				if(data.open != 0) {
					XYChart.Data newData = new XYChart.Data(dataTime.toString(), data.open);
					newData.setNode(null);
					list.add(newData);
				}
			}
		}
		return list;
	}

	/**
	 * An inner class holding the data and history for a currency. Used to bind a button a currency.
	 * @author Richard
	 *
	 */
	private class GraphButtonData {
		
		private Currency currency;
		private History history;
		private XYChart.Series series;
		
		public GraphButtonData(Currency currency, History history) {
			this.currency = currency;
			this.history = history;
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
