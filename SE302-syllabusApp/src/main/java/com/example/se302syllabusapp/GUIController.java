package com.example.se302syllabusapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    FileManager fileManager;
    FXMLLoader loader;
    Controllers controllers;
    VersionController versionController;



    public void handelImportEvent(ActionEvent event){

        // File Dialog here
        //getClass().getPackage().get
        File storagePath = new File("storage");
        if (!storagePath.exists()) {
            System.out.println("File does not exist!");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose JSON File");
        chooser.setInitialDirectory(storagePath);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File seletcetFile = chooser.showOpenDialog(new Popup());


        System.out.println(seletcetFile.getAbsolutePath());

        setFileManager(new FileManager());
        getFileManager().setJsonFile(new File(seletcetFile.getAbsolutePath()));

        SyllabusData syllabusData = getFileManager().read();

        System.out.println(syllabusData.name);

        //getFileManager().setJsonFile(new File(""));

        //SyllabusData syllabusData = getFileManager().read();

    }





    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public Controllers getControllers() {
        return controllers;
    }

    public void setControllers(Controllers controllers) {
        this.controllers = controllers;
    }

    public VersionController getVersionController() {
        return versionController;
    }

    public void setVersionController(VersionController versionController) {
        this.versionController = versionController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
