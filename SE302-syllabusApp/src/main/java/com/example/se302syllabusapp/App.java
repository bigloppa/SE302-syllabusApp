package com.example.se302syllabusapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        createFolders();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/se302syllabusapp/Demo4.fxml"));
        Parent root = loader.load();

        // FXML controller has been set.
        GUIController controller = loader.getController();
        Controllers controllers = new Controllers();


        controller.setPrimaryStage(stage);


        // The width and height can be changed.
        Scene scene = new Scene(root,1400,750);
        File file = new File("desktop.png");

        Image image = new Image(new FileInputStream(file.getAbsolutePath()));
        stage.getIcons().add(image);
        stage.setTitle("SyllabusApp");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void createFolders() {
        String storageFolderPath = "storage";
        Path storagePath = Paths.get(storageFolderPath);

        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
                System.out.println("Storage folder created successfully.");

                String enFolderPath = storageFolderPath + "/en";
                Path enPath = Paths.get(enFolderPath);
                Files.createDirectories(enPath);
                System.out.println("en folder created successfully.");

                String trFolderPath = storageFolderPath + "/tr";
                Path trPath = Paths.get(trFolderPath);
                Files.createDirectories(trPath);
                System.out.println("tr folder created successfully.");

            } catch (Exception e) {
                System.err.println("Error creating folders: " + e.getMessage());
            }
        } else {
            System.out.println("Storage folder already exists.");
        }
    }

}