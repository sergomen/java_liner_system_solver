package main;

import controllers.PlotsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

		try {
			loader.setLocation(getClass().getResource("../views/plots.fxml"));
			rootLayout = loader.load();
		} catch (Exception e) {
			rootLayout = loader.load(getClass().getResourceAsStream("/views/plots.fxml"));
		}

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

	public static void log(String die_klass, String message) {
		System.out.println(String.format("%1$s %2$s", die_klass, message));
	}

	public static void err(String die_klass, String message) {
		System.err.println(String.format("%1$s %2$s", die_klass, message));
	}

}
