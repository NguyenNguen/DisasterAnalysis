package com.disaster.analysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Đảm bảo bạn đã có file main.fxml trong src/main/resources/fxml/
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Disaster Relief Radar");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}