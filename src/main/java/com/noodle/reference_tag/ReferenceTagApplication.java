package com.noodle.reference_tag;

import com.noodle.reference_tag.config.StageInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ReferenceTagApplication extends Application {

	private ConfigurableApplicationContext springContext;

	/**
	 * Called by the launch() method
	 * Initializes the Spring Context
	 */
	@Override
	public void init() {
		springContext = SpringApplication.run(ReferenceTagMain.class);
	}

	/**
	 * Called in the launch sequence after init
	 * Loads the initial fxml file through fxml loader using spring injection, then sets the scene on stage and displays
	 * @param primaryStage
	 * @throws Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set the primary stage in StageInitializer
		springContext.getBean(StageInitializer.class).setPrimaryStage(primaryStage);

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		Parent root = fxmlLoader.load();

		primaryStage.setTitle("Reference Tag Application");
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() {
		springContext.close();
	}

	public static void main(String[] args) {
		launch(args);
	}
}