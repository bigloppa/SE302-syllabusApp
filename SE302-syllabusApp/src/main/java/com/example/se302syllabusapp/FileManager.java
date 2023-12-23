package com.example.se302syllabusapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.util.*;

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

    public SyllabusData read() {

        JSONParser parser = new JSONParser();

        SyllabusData root = new SyllabusData();

        int counter = 0;
        try {
            // [ syllabus ]
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(getJsonFile()));

            Object keyName = jsonObject.keySet();
            String[] name = keyName.toString().split("[\\[\\]]");

            root.setName(name[1]);

            Collection<JSONArray> value =  jsonObject.values();
            Object[] objList = value.toArray();

            // [ GI, WSRM, A, ECTS-WT, C-POM ]
            JSONArray sub1List = (JSONArray) objList[0];

            for (Object sub1ListElements : sub1List) {

                JSONObject forValuePassing = (JSONObject)  sub1ListElements;
                String keyName1 = forValuePassing.keySet().toString().split("[\\[\\]]")[1];

                SyllabusData subObject = new SyllabusData();
                subObject.setName(keyName1);

                Collection<JSONArray> value1 =  ((JSONObject) sub1ListElements).values();
                Object[] objList1 = value1.toArray();

                for (int i = 0; i < objList1.length; i++) {
                    // [ sub1, ... , sub1, ... , Participation, ... , Course-Hours, ..., sub1 ]

                    JSONArray sub2List = (JSONArray) objList1[i];
                    //System.out.println(sub2List);

                    for (Object sub2ListElements : sub2List) {

                        JSONObject forValuePassing2 = (JSONObject)  sub2ListElements;
                        String keyName3 = forValuePassing2.keySet().toString().split("[\\[\\]]")[1];

                        SyllabusData subObject2 = new SyllabusData();
                        subObject2.setName(keyName3);

                        Collection<JSONArray> value2 =  ((JSONObject) sub2ListElements).values();
                        Object[] objList2 = value2.toArray();

                        for (int j = 0; j < objList2.length; j++) {

                            JSONArray sub3List = (JSONArray) objList2[i];
                            SyllabusData subObject3 = new SyllabusData();

                            for (Object sub3ListElements : sub3List) {

                                JSONObject lastValue = (JSONObject) sub3ListElements;
                                String keyName4 = lastValue.keySet().toString().split("[\\[\\]]")[1];

                                SyllabusData subObject4 = new SyllabusData();
                                subObject4.setName(keyName4);
                                subObject4.setValue(lastValue.get(keyName4).toString());
                                subObject3.getChildren().add(subObject4);
                                counter++;
                            }
                            subObject2.getChildren().add(subObject3);
                        }
                        subObject.getChildren().add(subObject2);
                    }
                }
                root.getChildren().add(subObject);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(counter);
        return root;
    }

}
