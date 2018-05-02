package crypto.client;

import java.io.IOException;

import crypto.client.model.Config;
import crypto.client.model.CurrencyList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main class for the JavaFX project.
 * @author Richard
 *
 */
public class MainController extends Application {

	private Stage primaryStage;
	private Stage settingsStage;
	private BorderPane rootLayout;
	private TabPane tabPane;
	
	private ConverterViewController converterController;
	private GraphViewController graphController;
	private RootLayoutController rootController;
	private SettingsViewController settingsController;

	/**
	 * Called when javaFX has initialized
	 */
	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle("CryptoConverter");
		primaryStage.setOnCloseRequest(this::onClose);

		initRootLayout();
	}

	/**
	 * load the different views and put them into the RootLayout.
	 */
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainController.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			rootController = loader.getController();
			rootController.setMainController(this);
			tabPane = new TabPane();
			rootLayout.setCenter(tabPane);

			//load the converter tab
			Tab conversionTab = new Tab("Conversion");
			conversionTab.setClosable(false);
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/ConverterView.fxml"));
			conversionTab.setContent(loader.load());
			converterController = loader.getController();
			converterController.setMainController(this);
			tabPane.getTabs().add(conversionTab);

			//load the graph tab
			Tab graphTab = new Tab("Graphs");
			graphTab.setClosable(false);
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/GraphView.fxml"));
			graphTab.setContent(loader.load());
			graphController = loader.getController();
			graphController.setMainController(this);
			tabPane.getTabs().add(graphTab);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void init() throws Exception {
		try {
			CurrencyList.loadCurrencyList();
			Config.loadFromDisk();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onClose(WindowEvent e) {
		if(settingsStage != null) {
			if(settingsStage.isShowing()) {
				settingsController.onClose();
			}
			settingsStage.close();
		}
		converterController.onClosing();
	}
	
	public void onSave() {
		converterController.saveData();
	}
	
	public void closeApp() {
		primaryStage.close();
	}
	
	public void openSettings() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/SettingsView.fxml"));
			
			Scene scene = new Scene(loader.load(), 600, 400);
			settingsController = loader.getController();
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(scene);
			stage.initOwner(primaryStage);
			stage.setOnCloseRequest(e -> settingsController.onClose());
			stage.show();
			settingsStage = stage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
