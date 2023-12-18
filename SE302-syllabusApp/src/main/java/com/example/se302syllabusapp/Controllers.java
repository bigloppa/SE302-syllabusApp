package com.example.se302syllabusapp;

import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class Controllers extends FileManager{

    public Controllers(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        super(syllabusData1, syllabusData2);
    }
    public Controllers(){

    }

    public void createWithOuterFile(String filepath){

        ArrayList<File> files = new ArrayList<>();

        Save(files,filepath);

    }

    // TODO: 18.12.2023 it needs to be tested 
    public void update(String filePath){
        JSONObject courseObject = new JSONObject();
        JSONObject subObject = new JSONObject();
        JSONArray syllabus = new JSONArray();
        delete(filePath);
        for(SyllabusData syllabusData: syllabusData1.getChildren()){
            for (SyllabusData syllabusData3 : syllabusData.getChildren()){
                for (SyllabusData syllabusData4 : syllabusData3.getChildren()){
                    
                    courseObject.put(syllabusData4.getName(), syllabusData4.getValue());
                    
                }
                
                subObject.put(syllabusData3.getName(),courseObject);
                
            }

            syllabus.add(subObject);
            
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(syllabus.toJSONString());
            System.out.println("JSON data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    public void delete(String filePath){
        File file = new File(filePath);


        if (file.delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete file!");
        }
    }

    public void fileExport(File file, Stage primaryStage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");


        File selectedFile = fileChooser.showSaveDialog(primaryStage);

        if (selectedFile != null) {
            try {
                Path source = Paths.get(file.getPath());
                Path destination = Paths.get(selectedFile.getPath());


                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void fileImport(String language,String lecture){


        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose JSON File");

        chooser.setInitialDirectory(new File("C:\\"));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File selectedFile = chooser.showOpenDialog(new Popup());

        String filepath = "storage/";
        filepath+= language;
        checkDir(filepath);

        filepath+= ("/"+ lecture);
        checkDir(filepath);


        int counter = 1;
        boolean flag = true;
        while(flag){
            flag = checkDir(filepath + "/V" +counter );
            counter++;

        }

        filepath += ("/V"+--counter+"/"+ selectedFile.getName());


        Path source = Paths.get(selectedFile.getPath());
        Path destination = Paths.get(filepath);


        try {

            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {

        }


    }

    public boolean checkDir(String storagePath){
        File storage = new File(storagePath);
        boolean flag = true;
        if (!storage.exists()) {
            flag = false;
            if (storage.mkdir()) {
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        return flag;

    }




    public void Save(ArrayList<File> files,String filePath){
        for (File file: files){
            String sourceFilePath = file.getPath();


            Path sourcePath = Paths.get(sourceFilePath);
            Path destinationPath = Paths.get(filePath);


            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File copied successfully to: " + filePath);
            } catch (IOException e) {
                System.out.println("An error occurred while copying the file: " + e.getMessage());
            }
        }
    }
}
