package com.tolojanahary.Gcourrier;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class GCourrierApplication extends Application{

	public static ConfigurableApplicationContext context;

	@Value("${spring.application.ui.title:JavaFX application}")
	private String windowTitle;

	@Override
	public void init(){

		ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {
			ac.registerBean(Application.class, () -> GCourrierApplication.this);
			ac.registerBean(Parameters.class, this::getParameters);
			ac.registerBean(HostServices.class, this::getHostServices);
		};
		context = new SpringApplicationBuilder()
				.sources(GCourrierApplication.class)
				.initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(GCourrierApplication.class.getResource("fenetrecontrol.fxml"));
		fxmlLoader.setControllerFactory(context::getBean);
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle(windowTitle);
		stage.setMinWidth(1100);
		stage.setMinHeight(600);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		context.stop();
		Platform.exit();
	}
}