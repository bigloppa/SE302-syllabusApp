package com.example.se302syllabusapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
        JSONParser parser = new JSONParser();

        SyllabusData root = new SyllabusData();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("storage/en/CE456/V1/CE456.json"));

            Object keyName = jsonObject.keySet();
            String[] name = keyName.toString().split("[\\[\\]]");

            root.setName(name[1]);

            JSONArray children = (JSONArray) jsonObject.get(name[1]);

            for(Object sub: children) {

                SyllabusData subObject = new SyllabusData();
                // GI , WSRM , A ...
                JSONObject jObj = (JSONObject) sub;

                String subObjectName = jObj.keySet().toString().split("[\\[\\]]")[1];

                subObject.setName(subObjectName);

                JSONArray jA1 = (JSONArray) jObj.get(subObjectName);

                for (Object sub1: jA1) {
                    // GI , WSRM , A ... bunların içindeki objeler
                    JSONObject jObj1 = (JSONObject) sub1;

                    if (jObj1.isEmpty())
                        continue;

                    SyllabusData subObject1 = new SyllabusData();

                    String subObjectName1 = jObj1.keySet().toString().split("[\\[\\]]")[1];

                    subObject1.setName(subObjectName1);

                    try {
                        JSONArray sub2 = (JSONArray) jObj1.get(subObjectName1);

                        for (Object sub2Elements: sub2) {

                            JSONObject sub3 = (JSONObject) sub2Elements;

                            SyllabusData subObject2 = new SyllabusData();
                            if (sub3.keySet().toString().split("[\\[\\]]")[1].isEmpty())
                                subObject2.setName(sub3.keySet().toString().split("[\\[\\]]")[1]);



                            for (Object kName: sub3.keySet().toArray()) {
                                SyllabusData subObject3 = new SyllabusData();
                                subObject3.setName(kName.toString());
                                subObject3.setValue(sub3.get(kName.toString()).toString());
                                subObject2.getChildren().add(subObject3);

                            }



                            subObject1.getChildren().add(subObject2);

                        }

                    }catch (Exception e) {
                        // Özel bir durum Total için bir alt çocuğu olmayanlar için
                        subObject1.setValue(jObj1.get(subObjectName1).toString());

                    }
                    subObject.getChildren().add(subObject1);

                }

                // Burada bir alt sub objeclerin ekelemelerini yapıyorum
                root.getChildren().add(subObject);

            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return root;
    }


    public SyllabusData recursive(JSONObject object) {

        return null;
    }
}
