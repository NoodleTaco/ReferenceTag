package com.noodle.reference_tag;

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
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		Parent root = fxmlLoader.load();

		primaryStage.setTitle("Reference Tag Application");
		primaryStage.setScene(new Scene(root));
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