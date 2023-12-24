package com.example.se302syllabusapp;

import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import static java.io.File.separator;


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
                writer.write("<div><b>" + key + ":</b></div>\n");
                processJson(jsonObject.get(key), writer);
            }
        } else if (obj instanceof JSONArray) {
            // If JSON array, process its elements
            JSONArray jsonArray = (JSONArray) obj;
            for (Object element : jsonArray) {
                writer.write("<div>\n");
                processJson(element, writer);
                writer.write("</div>\n");
            }
        } else {
            // If JSON primitive, display its value
            writer.write("<div>" + obj.toString() + "</div>\n");
        }
    }
    public void exportJsonToDocx(String jsonFilePath, String docxFilePath) {
        try {

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonFilePath));

            Map<String, String> jsonData = parseJsonToMap(obj);


            exportToDocx(jsonData, docxFilePath);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void exportToDocx(Map<String, String> jsonData, String outputFile) {
        XWPFDocument document = new XWPFDocument();
        for (Map.Entry<String, String> entry : jsonData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String[] keyParts = key.split("\\.");
            String lastKeyPart = keyParts[keyParts.length - 1];

            System.out.println("LastKey"+lastKeyPart);



            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setBold(true);
            run.setText(lastKeyPart +": ");
            run.setColor("FF0000");
            run = paragraph.createRun();
            run.setText(value);
        }
        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            document.write(out);
            System.out.println("DOCX file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void fileExport2(String path, String type, String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(type + " Dosyaları", "*." + type));
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                Path source = Paths.get(path);
                Path destination = Paths.get(selectedFile.getPath());
                String fileExtension = "";

                if ("json".equals(type)) {
                    fileExtension = ".json";
                    destination = Paths.get(destination.toString() + fileExtension);
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } else if ("html".equals(type)) {
                    fileExtension = ".html";
                    destination = Paths.get(destination.toString() + fileExtension);
                    convertJsonToHtml(source.toString(), destination.toString());
                    System.out.println("Conversion completed successfully.");
                } else if ("docx".equals(type)) {
                    fileExtension = ".docx";
                    destination = Paths.get(destination.toString() + fileExtension);
                    exportJsonToDocx(source.toString(), destination.toString());
                    System.out.println("Export to DOCX completed successfully.");
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private static Map<String, String> parseJsonToMap(Object obj) {
        Map<String, String> resultMap = new HashMap<>();
        parseJsonObject(obj, resultMap, "");
        return resultMap;
    }

    private static void parseJsonObject(Object obj, Map<String, String> resultMap, String currentKey) {
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;

            // Yeni eklenen kod bloğu başlangıcı
            List<String> sortedKeys = new ArrayList<>(jsonObject.keySet());
            Collections.sort(sortedKeys);
            for (String key : sortedKeys) {
                String newKey = currentKey.isEmpty() ? key.toString() : currentKey + "." + key;
                parseJsonObject(jsonObject.get(key), resultMap, newKey);
            }
            // Yeni eklenen kod bloğu sonu

        } else if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                String newKey = currentKey + "[" + i + "]";
                parseJsonObject(jsonArray.get(i), resultMap, newKey);
            }
        } else {
            resultMap.put(currentKey, obj.toString());
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

    public void saveDescription(String description, String filePath){
        try(FileWriter fileWriter = new FileWriter(new File(filePath))){
            fileWriter.write(description);

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public void saveFromUserEntry(ArrayList<String>syllabusData, String language, boolean isEditLastVersionSelected,String description) {

        String lecture = syllabusData.get(1).trim();
        int version = createDir(language,lecture, isEditLastVersionSelected);
        String filepath = "storage/" + language+ "/"+ lecture+ "/V"+ --version+"/"+ lecture+".json";
        String filepathDes = "storage/" + language+ "/"+ lecture+ "/V"+ --version+"/"+ lecture+".txt";

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
