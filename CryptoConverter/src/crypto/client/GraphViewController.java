package crypto.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import me.joshmcfarlin.CryptoCompareAPI.Historic;
import me.joshmcfarlin.CryptoCompareAPI.Historic.History;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;

public class GraphViewController implements Initializable{

	@FXML
	private LineChart<String, Number> chart;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Day");
		chart.setTitle("CryptoGraph");
		
		XYChart.Series series = new XYChart.Series();
		
		try {
			History history = Historic.getDay("BTC", "SEK", 100);
			for (History.Data data : history.data) {
				series.getData().add(new XYChart.Data(new java.util.Date(data.time).toString(), data.high));
			}
			chart.getData().add(series);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfCallsException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
