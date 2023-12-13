package com.example.se302syllabusapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.json.Json;
import javax.json.stream.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class FileManager {
    SyllabusData syllabusData1;
    SyllabusData syllabusData2;
    //TODO Add this object to the document
    File jsonFile;

    //TODO Add this constructor to the document
    public FileManager() {
        syllabusData1 = new SyllabusData();
        syllabusData2 = new SyllabusData();
        jsonFile = null;
    }

    public FileManager(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        this.syllabusData1 = syllabusData1;
        this.syllabusData2 = syllabusData2;
    }

    //TODO Add this constructor to the document
    public FileManager(File jsonFile) {
        syllabusData1 = new SyllabusData();
        syllabusData2 = new SyllabusData();
        this.jsonFile = jsonFile;
    }

    public SyllabusData getSyllabusData1() {
        return syllabusData1;
    }

    public void setSyllabusData1(SyllabusData syllabusData1) {
        this.syllabusData1 = syllabusData1;
    }

    public SyllabusData getSyllabusData2() {
        return syllabusData2;
    }

    public void setSyllabusData2(SyllabusData syllabusData2) {
        this.syllabusData2 = syllabusData2;
    }

    public File getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(File jsonFile) {
        this.jsonFile = jsonFile;
    }


    // This function reads JSON files and translate into SyllabusData object.
    // Also, reads Text files that are related with JSON file, for descriptions.
    public SyllabusData read(){

        // TODO: We have to figure it out how to pass paths of JSON and TXT files!
        String filepath = "fake_path";
        // Take JSON file


        try {
            //JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("storage/en/CE216/Deneme-JSON-V1.json"));

            JsonParser parser1 = Json.createParser(new FileReader("storage/en/CE216/Deneme-JSON-V1.json"));

            SyllabusData root = new SyllabusData();
            //JSONArray root1 = (JSONArray) jsonObject.get("syllabus");
            JsonParser.Event event = parser1.next(); // START_OBJECT
            System.out.println(event);


            // Root's children of the JSON file
            //JSONArray sections = (JSONArray) jsonObject.get("syllabus");




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new SyllabusData();
    }


    public SyllabusData recursive(JSONObject object) {

        return null;
    }
}
