package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.controllers.PlotsController;

import java.io.IOException;

public class Main extends Application {

	private Stage mainStage;
	private GridPane rootLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainStage = primaryStage;
		loadMainStage();
		mainStage.show();
	}

	private void loadMainStage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("views/plots.fxml"));

		rootLayout = loader.load();

		Scene scene = new Scene(rootLayout);

		mainStage.setTitle("Equation solver");
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();

		PlotsController firstController = loader.getController();
		firstController.initialize(mainStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void log(String klass, String message) {
		System.out.println(String.format("%1$s %2$s", klass, message));
	}

	public static void err(String klass, String message) {
		System.err.println(String.format("%1$s %2$s", klass, message));
	}

}
