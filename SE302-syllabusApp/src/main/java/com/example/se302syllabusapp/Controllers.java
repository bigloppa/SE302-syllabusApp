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


    public void delete(String filePath , String course){
        GUIController guiController = new GUIController();
        File file = new File(filePath);
        File insideFileJson = new File(filePath + "/" + course + ".json");
        File insideFileTxt = new File(filePath + "/" + course + ".txt");
        if (insideFileJson.delete() & insideFileTxt.delete()){
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("Failed to delete file!");
        }

        if (file.delete()) {
            System.out.println("File deleted successfully.");
            guiController.showAlert("Deleting File", "File deleted successfully." );
        } else {
            System.out.println("Failed to delete file!");
        }
    }

    public void fileExport(String path, String type, String name){
        GUIController guiController = new GUIController();
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
                guiController.showAlert("Exporting File" , "The file successfully exported.");

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private static void convertJsonToHtml(String inputFile, String outputFile) throws IOException, ParseException {
        // Parse JSON file using JSON.simple
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(inputFile));

        // Generate HTML and write to file
        generateHtml(obj, outputFile);
    }

    private static void generateHtml(Object obj, String outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Start HTML document
            writer.write("<html>\n<head>\n<title>HTML Version</title>\n</head>\n<body>\n");

            // Recursively process JSON and convert to HTML
            processJson(obj, writer);

            // End HTML document
            writer.write("</body>\n</html>");
        }
    }

    private static void processJson(Object obj, FileWriter writer) throws IOException {
        if (obj instanceof JSONObject) {
            // If JSON object, process its fields
            JSONObject jsonObject = (JSONObject) obj;
            for (Object key : jsonObject.keySet()) {
                writer.write("<div><b>" + key + ":</b></div>\t");
                processJson(jsonObject.get(key), writer);
            }
        } else if (obj instanceof JSONArray) {
            // If JSON array, process its elements as unordered lists
            JSONArray jsonArray = (JSONArray) obj;
            writer.write("<ul>\n");
            for (Object element : jsonArray) {
                writer.write("<li>\n");
                processJson(element, writer);
                writer.write("</li>\n");
            }
            writer.write("</ul>\n");
        } else {
            // If JSON primitive, display its value
            writer.write("<div>" + obj.toString() + "</div>\n");
        }
    }

    public void fileExport2(String path, String type, String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(type + " Dosyaları", "*." + type));

        File selectedFile = fileChooser.showSaveDialog(null);

        // If a file is selected, copy the content
        if (selectedFile != null) {
            try {
                // Get the source and destination paths
                Path source = Paths.get(path);
                Path destination = Paths.get(selectedFile.getPath());

                // Choose file extension based on the selected type
                String fileExtension = "";
                if ("json".equals(type)) {
                    fileExtension = ".json";
                    // Copy the file using Files.copy
                    destination = Paths.get(destination.toString() + fileExtension);
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);



                } else if ("html".equals(type)) {
                    fileExtension = ".html";
                    destination = Paths.get(destination.toString() + fileExtension);
                    convertJsonToHtml(source.toString(), destination.toString());
                    System.out.println("Conversion completed successfully.");

                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }




    public int createDir(String language,String lecture,boolean isEditLastVersionSelected){
        checkDir("storage",false);
        String filepath = "storage/";
        filepath+= language;
        checkDir(filepath,false);

        filepath+= ("/"+ lecture);
        checkDir(filepath,false);

        int counter = 1;


        boolean flag = true;
        while(flag) {
            flag = checkDir(filepath + "/V" + counter,isEditLastVersionSelected);
            counter++;
        }

        if (isEditLastVersionSelected) {
            return --counter;
        }else {
            return counter;
        }
    }

    public boolean checkDir(String storagePath,boolean isEditLastVersionSelected){
        File storage = new File(storagePath);
        boolean flag = true;
        if (!storage.exists()) {
            flag = false;
            if (!isEditLastVersionSelected) {
                storage.mkdir();
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        return flag;

    }

    public void saveDescription(String description, String filePath){
        try(FileWriter fileWriter = new FileWriter(new File(filePath))){
            fileWriter.write(description);

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void saveFromUserEntry(ArrayList<String>syllabusData, String language, boolean isEditLastVersionSelected,String description, String versionForEdit) {
        GUIController guiController = new GUIController();

        String lecture = syllabusData.get(1).trim();
        int version = createDir(language,lecture, isEditLastVersionSelected);
        String filepath = "storage/" + language+ "/" + lecture+ "/V"+ --version +"/" + lecture+".json";
        String filepathDes = "storage/" + language+ "/" + lecture+ "/V" + version+"/" + lecture+".txt";

        if (isEditLastVersionSelected ) {
            filepath = "storage/" + language+ "/" + lecture+ "/"+ versionForEdit +"/" + lecture+".json";
            filepathDes = "storage/" + language+ "/" + lecture+ "/" + versionForEdit+"/" + lecture+".txt";
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
                guiController.showAlert("Adding Data", "Data successfully written");
                System.out.println("Data successfully written");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        saveDescription(description,filepathDes);
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
