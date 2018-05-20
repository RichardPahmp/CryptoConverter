package crypto.client;

import crypto.client.model.Config;
import crypto.client.model.CurrencyList;
import crypto.util.LiveCurrencyData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

/**
 * The main class for the JavaFX project.
 * @author Richard
 *
 */
public class MainController extends Application {

	private Stage primaryStage;
	private Stage settingsStage;
	
	private Scene primaryScene;
	private Scene settingsScene;
	
	private ConverterViewController converterController;
	private GraphViewController graphController;
	private RootLayoutController rootController;
	private SettingsViewController settingsController;
	private RegisterViewController registerController;
	private LivefeedViewController livefeedController;
	private UserStatisticsViewController userStatsController;
	
	private ServerConnection serverConnection;
	
	private TabPane tabPane;
	private Tab userDataTab;

	/**
	 * Called when javaFX has initialized
	 */
	@Override
	public void start(Stage stage) {
		primaryStage = stage;
		primaryStage.setTitle("CryptoConverter");
		primaryStage.setOnCloseRequest(this::onClose);

		initRootLayout();
		
		serverConnection = new ServerConnection(this);
		
		new Thread(this::tryConnect).start();
	}

	/**
	 * load the different views and put them into the RootLayout.
	 */
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainController.class.getResource("view/RootLayout.fxml"));
			BorderPane rootLayout = (BorderPane) loader.load();
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
			
			//load the livefeed tab
			Tab liveTab = new Tab("Live");
			liveTab.setClosable(false);
			loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/LivefeedView.fxml"));
			liveTab.setContent(loader.load());
			livefeedController = loader.getController();
			livefeedController.setMainController(this);
			tabPane.getTabs().add(liveTab);

			primaryScene = new Scene(rootLayout);
			primaryStage.setScene(primaryScene);
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
	
	private void addUserDataTab() {
		userDataTab = new Tab("User data");
		userDataTab.setClosable(false);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/UserStatisticsView.fxml"));
		try {
			userDataTab.setContent(loader.load());
			userStatsController = loader.getController();
			userStatsController.setMainController(this);
			Platform.runLater(() -> tabPane.getTabs().add(userDataTab));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onClose(WindowEvent e) {
		serverConnection.close();
		livefeedController.onClose();
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
		serverConnection.close();
		primaryStage.close();
	}
	
	public void changeStyle(String css) {
		primaryScene.getStylesheets().clear();
		primaryScene.getStylesheets().add(css);
		settingsScene.getStylesheets().clear();
		settingsScene.getStylesheets().add(css);
	}
	
	public void tryConnect() {
		if(serverConnection.connect()) {
			rootController.setLoginAvailible();
		} else {
			rootController.setNoConnection();
		}
	}
	
	public void onLoginSuccess(String username) {
		rootController.setLoggedInAs(username);
		addUserDataTab();
	}
	
	public void onLoginFailed() {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Failed to login");
			alert.setHeaderText("Failed to login");
			alert.setContentText("Wrong username, password or both");
			alert.showAndWait();
		});
	}
	
	public void onDisconnect() {
		rootController.setNoConnection();
		if(userDataTab != null) {
			Platform.runLater(() -> tabPane.getTabs().remove(userDataTab));
		}
	}
	
	public void onRegisterSuccess() {
		registerController.registerSuccessful();
	}
	
	public void onRegisterFailed() {
		registerController.registerFailed();
	}
	
	public void register(String username, String password) {
		serverConnection.connect();
		serverConnection.register(username, password);
	}
	
	public void login(String username, String password) {
		serverConnection.connect();
		serverConnection.login(username, password);
	}
	
	public void logout() {
		serverConnection.logout();
	}
	
	public void onLogout() {
		rootController.setLoginAvailible();
	}
	
	public void onSearch(String[] symbols) {
		if(serverConnection.isConnected()) {
			serverConnection.newSearch(symbols);
		}
	}
	
	public void requestUserData(){
		serverConnection.requestUserData();
	}
	
	public void onUserDataReceived(HashMap<String, Integer> mapMe, HashMap<String, Integer> mapAll) {
		userStatsController.putUserData(mapMe, mapAll);
	}
	
	public void openSettings() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/SettingsView.fxml"));
			
			settingsScene = new Scene(loader.load(), 600, 400);
			settingsController = loader.getController();
			settingsController.setMainController(this);
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(settingsScene);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.setOnCloseRequest(e -> settingsController.onClose());
			stage.show();
			settingsStage = stage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openRegister() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/RegisterView.fxml"));
			
			Scene scene = new Scene(loader.load(), 300, 300);
			registerController = loader.getController();
			registerController.setMainController(this);
			Stage stage = new Stage();
			stage.setTitle("Register");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setScene(scene);
			stage.initOwner(primaryStage);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
