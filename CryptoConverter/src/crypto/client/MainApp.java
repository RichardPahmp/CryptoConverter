package crypto.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private TabPane tabPane;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("CryptoConverter");
		
		initRootLayout();
	}
	
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			tabPane = new TabPane();
			rootLayout.setCenter(tabPane);
			
			//load the converter tab
			Tab conversionTab = new Tab("Conversion");
			conversionTab.setClosable(false);
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/ConverterView.fxml"));
			conversionTab.setContent(loader.load());
			tabPane.getTabs().add(conversionTab);
			
			//load the graph tab
			Tab graphTab = new Tab("Graphs");
			graphTab.setClosable(false);
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/GraphView.fxml"));
			graphTab.setContent(loader.load());
			tabPane.getTabs().add(graphTab);
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}