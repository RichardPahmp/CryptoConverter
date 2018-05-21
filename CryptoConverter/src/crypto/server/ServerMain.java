package crypto.server;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ServerMain extends Application {

	private CryptoServer server;
	private ServerViewController viewController;
	private ServerConfig config;
	
	@Override
	public void start(Stage primaryStage) {
		config = new ServerConfig();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/ServerView.fxml"));
		primaryStage.setTitle("CryptoConverter Server");
		
		try {
			AnchorPane pane = loader.load();
			viewController = loader.getController();
			viewController.setOnRestart(this::restartServer);
			primaryStage.setOnCloseRequest(e -> {
				if(server != null) {
					server.close();
				}
			});
			
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if(viewController != null) {
			startServer();
		}
	}
	
	private void startServer() {
		int port = config.getPort();
		server = new CryptoServer(viewController, port);
		server.setName("Server thread");
		server.start();
	}
	
	private void restartServer() {
		server.onRestart();
		startServer();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
