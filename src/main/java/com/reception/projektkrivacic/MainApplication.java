package com.reception.projektkrivacic;

import com.reception.projektkrivacic.utils.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MainApplication extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(getClass().getResource("/styles/nord-dark.css").toExternalForm());
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Aplikacija za upravljanje recepcijom hotela!");
        stage.getIcons().add(new Image("https://cdn.iconscout.com/icon/free/png-256/hotel-157-902099.png"));
        stage.setScene(scene);
        stage.show();
        logger.info("Application started");
    }
    public static Stage getMainStage()
    {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}