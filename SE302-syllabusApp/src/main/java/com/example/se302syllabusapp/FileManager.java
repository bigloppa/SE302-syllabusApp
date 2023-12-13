package com.example.se302syllabusapp;

import java.io.File;
import org.json.simple.parser.*;


public class FileManager {
    SyllabusData syllabusData1;
    SyllabusData syllabusData2;

    public FileManager(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        this.syllabusData1 = syllabusData1;
        this.syllabusData2 = syllabusData2;
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


    // This function reads JSON files and translate into SyllabusData object.
    // Also, reads Text files that are related with JSON file, for descriptions.
    public SyllabusData read(){

        // TODO: We have to figure it out how to pass paths of JSON and TXT files!
        String filepath = "fake_path";
        // Take JSON file
        JSONParser parser = new JSONParser();

        return new SyllabusData();
    }
}
