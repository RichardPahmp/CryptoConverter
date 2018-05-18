package crypto.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class UserStatisticsViewController implements Initializable {

	@FXML
	private Label labelMeCurrency;

	@FXML
	private Label labelAllCurrency;

	@FXML
	private Label labelMeSearches;

	@FXML
	private Label labelAllSearches;

	@FXML
	private PieChart pieChartMe;

	@FXML
	private PieChart pieChartAll;

	private MainController mainController;

	private Choices currentChoice;

	private HashMap<String, Integer> myDataMap, allDataMap;

	private enum Choices {
		myData, allData;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	/**
	 * Outputs the data in the myDataMap to the view
	 */
	private void showMeData() {
		int totalSearches = getSearchesSum(myDataMap);
		String biggestSymbol = getBiggestSymbol(myDataMap);

		int showLimit = totalSearches / 10;

		ObservableList<PieChart.Data> chartData = createChartData(myDataMap, showLimit);

		Platform.runLater(() -> {
			pieChartMe.setData(chartData);

			labelMeSearches.setText(totalSearches + "");
			labelMeCurrency.setText(biggestSymbol);
		});
	}

	/**
	 * outputs the data in the allDataMap to the view
	 */
	private void showAllData() {
		int totalSearches = getSearchesSum(allDataMap);
		String biggestSymbol = getBiggestSymbol(allDataMap);

		int showLimit = totalSearches / 10;

		ObservableList<PieChart.Data> chartData = createChartData(allDataMap, showLimit);

		Platform.runLater(() -> {
			pieChartAll.setData(chartData);

			labelAllSearches.setText(totalSearches + "");
			labelAllCurrency.setText(biggestSymbol);
		});
	}

	/**
	 * Creates a list of piechart data from the given map
	 * @param map The map to create data from
	 * @param othersLimit Any entry with less than these many entries is put into the "others" category
	 * @return A list of pieChart.Data
	 */
	private ObservableList<PieChart.Data> createChartData(HashMap<String, Integer> map, int othersLimit) {
		int others = 0;
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (String str : map.keySet()) {
			if (map.get(str) > othersLimit) {
				pieChartData.add(createPieChartData(str, map.get(str)));
			} else {
				others += map.get(str);
			}
		}
		pieChartData.add(new PieChart.Data("Others", others));
		return pieChartData;
	}

	/**
	 * Returns the total numbers of searches in the given map
	 * @param map
	 * @return
	 */
	private int getSearchesSum(HashMap<String, Integer> map) {
		int totalSearches = 0;
		for (String str : map.keySet()) {
			totalSearches += map.get(str);
		}
		//since 1 search logs 2 symbols there are twice as many symbols as there are searches
		return totalSearches / 2;
	}

	/**
	 * Returns the most popular symbol the given map.
	 * @param map
	 * @return
	 */
	private String getBiggestSymbol(HashMap<String, Integer> map) {
		int biggestSum = 0;
		String biggestSymbol = "";
		for (String str : map.keySet()) {
			if (map.get(str) > biggestSum) {
				biggestSum = map.get(str);
				biggestSymbol = str;
			}
		}
		return biggestSymbol;
	}

	/**
	 * Create a PieChart.Data with the given name and int
	 * @param name
	 * @param num
	 * @return
	 */
	private PieChart.Data createPieChartData(String name, int num) {
		PieChart.Data data = new PieChart.Data(name, num);
		return data;
	}

	@FXML
	private void onRefresh() {
		mainController.requestUserData();
	}

	/**
	 * Update the userdata maps and update the view.
	 * @param myMapData
	 * @param allMapData
	 */
	public void putUserData(HashMap<String, Integer> myMapData, HashMap<String, Integer> allMapData) {
		myDataMap = myMapData;
		allDataMap = allMapData;
		showMeData();
		showAllData();
	}

	public void setMainController(MainController controller) {
		this.mainController = controller;
		mainController.requestUserData();
	}

}
