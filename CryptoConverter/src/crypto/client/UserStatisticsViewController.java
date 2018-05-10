package crypto.client;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class UserStatisticsViewController implements Initializable {
	
	@FXML
	private Label favoriteLabel;
	
	@FXML
	private Label numberLabel;
	
	@FXML
	private PieChart pieChart;
	
	@FXML
	private ChoiceBox<Choices> choiceBox;
	
	private MainController mainController;
	
	private Choices currentChoice;
	
	private HashMap<String, Integer> myDataMap, allDataMap;
	
	private enum Choices{
		myData,
		allData;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		choiceBox.setItems(FXCollections.observableArrayList(Choices.myData, Choices.allData));
		choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Choices>() {
			public void changed(ObservableValue obs, Choices value, Choices newValue) {
				currentChoice = newValue;
				updateData();
			}
		});
		choiceBox.getSelectionModel().select(0);
	}
	
	private void showData(HashMap<String, Integer> map) {
		if(map != null) {
			Platform.runLater(() -> {
				int totalSearches = 0;
				int biggestSum = 0;
				String biggestSymbol = "";
				for(String str : map.keySet()) {
					totalSearches += map.get(str);
					if(map.get(str) > biggestSum) {
						biggestSum = map.get(str);
						biggestSymbol = str;
					}
				}
				
				ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
				int showLimit = totalSearches / 10;
				int others = 0;
				
				for(String str : map.keySet()) {
					if(map.get(str) > showLimit) {
						pieChartData.add(createPieChartData(str, map.get(str)));
					} else {
						others += map.get(str);
					}
				}
				pieChartData.add(new PieChart.Data("Others", others));
				pieChart.setData(pieChartData);
				
				numberLabel.setText(totalSearches+"");
				favoriteLabel.setText(biggestSymbol);
				
			});
		}
	}
	
	private PieChart.Data createPieChartData(String name, int num) {
		PieChart.Data data = new PieChart.Data(name, num);
		return data;
	}
	
	private void updateData() {
		if(currentChoice == Choices.myData) {
			showData(myDataMap);
		} else if(currentChoice == Choices.allData) {
			showData(allDataMap);
		}
	}

	@FXML
	private void onRefresh() {
		if(currentChoice == Choices.myData) {
			mainController.requestUserData();
		} else if(currentChoice == Choices.allData) {
			mainController.requestAllUserData();
		}
	}
	
	public void putUserData(HashMap<String, Integer> map) {
		myDataMap = map;
		updateData();
	}
	
	public void putAllUserData(HashMap<String, Integer> map) {
		allDataMap = map;
		updateData();
	}
	
	public void setMainController(MainController controller) {
		this.mainController = controller;
		mainController.requestAllUserData();
		mainController.requestUserData();
	}
	
}
