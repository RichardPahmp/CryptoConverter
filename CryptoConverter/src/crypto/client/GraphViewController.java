package crypto.client;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import crypto.client.model.Currency;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class GraphViewController implements Initializable {

	@FXML
	private LineChart<String, Number> chart;
	
	@FXML
	private ComboBox<Currency> comboBox;
	
	@FXML
	private ListView<Currency> listView;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Value in USD");
		xAxis.setLabel("Day");
		chart.setTitle("CryptoGraph");
		chart.setCursor(Cursor.CROSSHAIR);
		chart.setCreateSymbols(false);

		XYChart.Series series = new XYChart.Series();

		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 15);
			long time = 0;
			for (int i = 0; i < 15; i++) {
				time = cal.getTimeInMillis();
				Double n = Historic.getDayAverage("BTC", "USD", time / 1000L);
				XYChart.Data data = new XYChart.Data(cal.getTime().toString(), n);
				data.setNode(new HoverNode(n));
				series.getData().add(data);
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
			}
			chart.getData().add(series);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}
	}

	private class HoverNode extends StackPane {
		public HoverNode(double value) {
			setPrefSize(10, 10);
			
			Label label = new Label("$" + value);
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
