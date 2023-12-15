package com.example.se302syllabusapp;

import javafx.fxml.FXMLLoader;

public class GUIController {
    FileManager fileManager;
    FXMLLoader loader;
    Controllers controllers;
    VersionController versionController;



    public void handelImportEvent(){

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
}
