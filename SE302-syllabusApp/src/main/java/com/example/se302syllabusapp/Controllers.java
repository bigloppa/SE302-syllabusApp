package com.example.se302syllabusapp;

import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Locale;


public class Controllers extends FileManager{

    public Controllers(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        super(syllabusData1, syllabusData2);
    }
    public Controllers(){

    }

    public void createWithOuterFile(String filepath){

        ArrayList<File> files = new ArrayList<>();



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




    public int createDir(String language,String lecture){


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



        return counter;





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

    public void saveFromUserEntry(ArrayList<String>syllabusData, String language){
        String lecture = syllabusData.get(1).trim();
        int version = createDir(language,lecture);
        String filepath = "storage/" + language+ "/"+ lecture+ "/V"+ --version+"/"+ lecture+".json";


        int counter = 0;
        JSONParser parser = new JSONParser();
        SyllabusData root = new SyllabusData();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("base/base.json"));

            Object keyName = jsonObject.keySet();
            String[] name = keyName.toString().split("[\\[\\]]");

            root.setName(name[1]);

            JSONArray children = (JSONArray) jsonObject.get(name[1]);

            for(Object sub: children) {
                JSONObject jObj = (JSONObject) sub;
                String subObjectName = jObj.keySet().toString().split("[\\[\\]]")[1];
                JSONArray jA1 = (JSONArray) jObj.get(subObjectName);

                for (Object sub1: jA1) {
                    JSONObject jObj1 = (JSONObject) sub1;

                    if (jObj1.isEmpty()) {
                        continue;
                    }

                    String subObjectName1 = jObj1.keySet().toString().split("[\\[\\]]")[1];

                    try {
                        JSONArray sub2 = (JSONArray) jObj1.get(subObjectName1);

                        for (Object sub2Elements: sub2) {

                            JSONObject sub3 = (JSONObject) sub2Elements;


                            for (Object key: sub3.keySet()) {
                                sub3.put(key,syllabusData.get(counter));
                                counter++;
                            }


                        }

                    }catch (Exception e) {

                    }

                }

            }



            try (FileWriter file = new FileWriter(filepath)) {

                file.write(indentJson(jsonObject.toJSONString()));
                System.out.println("Data successfully written");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }



    }

    private static String indentJson(String jsonString) {
        StringBuilder indented = new StringBuilder();
        int indentLevel = 0;
        for (char c : jsonString.toCharArray()) {
            if (c == '{' || c == '[') {
                indented.append(c).append("\n").append(getIndentString(++indentLevel));
            } else if (c == '}' || c == ']') {
                indented.append("\n").append(getIndentString(--indentLevel)).append(c);
            } else if (c == ',') {
                indented.append(c).append("\n").append(getIndentString(indentLevel));
            } else {
                indented.append(c);
            }
        }
        return indented.toString();
    }

    private static String getIndentString(int indentLevel) {
        return "    ".repeat(Math.max(0, indentLevel));
    }

    public void save(File file,String filePath){

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
