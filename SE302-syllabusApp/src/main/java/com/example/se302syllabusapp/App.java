package com.example.se302syllabusapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("demo4.fxml"));
        Parent root = loader.load();

        // FXML controller has been set.
        GUIController controller = loader.getController();

        // The width and height can be changed.
        Scene scene = new Scene(root,1050,800);
        stage.setScene(scene);
        stage.show();

        FileManager fileManager = new FileManager();

        fileManager.read();

    }

    public static void main(String[] args) {
        launch();
    }
}