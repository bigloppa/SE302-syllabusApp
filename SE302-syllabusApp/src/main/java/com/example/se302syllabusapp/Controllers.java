package com.example.se302syllabusapp;

import javafx.stage.FileChooser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Collection;


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
    public void update(String filePath, String course){
        JSONObject courseObject = new JSONObject();
        JSONObject subObject = new JSONObject();
        JSONArray syllabus = new JSONArray();
        delete(filePath, course);
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

    public void delete(String filePath , String course){
        File file = new File(filePath);
        File insideFile = new File(filePath + "/" + course + ".json");
        if (insideFile.delete()){
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete file!");
        }

        if (file.delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete file!");
        }
    }

    public void fileExport(String path, String type, String name){

        // diğer dosya türleri oluşturulduğunda düzenleme yapılması gerekiyor

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(type + " Dosyaları", "*." + type));


        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                Path source = Paths.get(path);
                Path destination = Paths.get(selectedFile.getPath());


                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public int createDir(String language,String lecture,boolean isEditLastVersionSelected){

        String filepath = "storage/";
        filepath+= language;
        checkDir(filepath);

        filepath+= ("/"+ lecture);
        checkDir(filepath);

        int counter = 0;
        if (!isEditLastVersionSelected) {
            counter = 1;
            boolean flag = true;
            while(flag){
                flag = checkDir(filepath + "/V" +counter );
                counter++;

            }
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

    public void saveFromUserEntry(ArrayList<String>syllabusData, String language, boolean isEditLastVersionSelected) {

        String lecture = syllabusData.get(1).trim();
        int version = createDir(language,lecture, isEditLastVersionSelected);
        String filepath = "storage/" + language+ "/"+ lecture+ "/V"+ --version+"/"+ lecture+".json";

        if (isEditLastVersionSelected) {
            delete(filepath, lecture);
        }

        JSONParser parser = new JSONParser();
        int counter = 0;
        try {
            // [ syllabus ]
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("base/base.json"));


            Collection<JSONArray> value =  jsonObject.values();
            Object[] objList = value.toArray();

            // [ GI, WSRM, A, ECTS-WT, C-POM ]
            JSONArray sub1List = (JSONArray) objList[0];

            for (Object sub1ListElements : sub1List) {


                Collection<JSONArray> value1 =  ((JSONObject) sub1ListElements).values();
                Object[] objList1 = value1.toArray();

                for (int i = 0; i < objList1.length; i++) {
                    // [ sub1, ... , sub1, ... , Participation, ... , Course-Hours, ..., sub1 ]

                    JSONArray sub2List = (JSONArray) objList1[i];

                    for (Object sub2ListElements : sub2List) {

                        Collection<JSONArray> value2 =  ((JSONObject) sub2ListElements).values();
                        Object[] objList2 = value2.toArray();

                        for (int j = 0; j < objList2.length; j++) {

                            JSONArray sub3List = (JSONArray) objList2[i];

                            for (Object sub3ListElements : sub3List) {

                                JSONObject lastValue = (JSONObject) sub3ListElements;
                                String keyName = lastValue.keySet().toString().split("[\\[\\]]")[1];

                                lastValue.put(keyName,syllabusData.get(counter));
                                counter++;
                            }
                        }
                    }
                }
            }

            try (FileWriter file = new FileWriter(filepath)) {
                file.write(indentJson(jsonObject.toJSONString()));
                System.out.println("Data successfully written");
            } catch (IOException e) {
                throw new RuntimeException(e);
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
