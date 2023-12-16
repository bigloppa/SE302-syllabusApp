package com.example.se302syllabusapp;


import com.google.gson.*;
import com.google.gson.stream.JsonToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    SyllabusData syllabusData1;
    SyllabusData syllabusData2;

    public FileManager(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        this.syllabusData1 = syllabusData1;
        this.syllabusData2 = syllabusData2;
    }

    public FileManager(){

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
    public void read() {

        // TODO: We have to figure it out how to pass paths of JSON and TXT files!
        String filepath = "storage/en/CE216/Deneme-JSON-V1.json";


        try {

            FileReader reader = new FileReader(filepath);


            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray syllabus = jsonObject.getAsJsonArray("syllabus");


            if (syllabus != null && !syllabus.isEmpty()) {
                for (JsonElement element : syllabus) {
                    JsonObject giObject = element.getAsJsonObject();
                    for (String key : giObject.keySet()) {
                        JsonArray Category = giObject.getAsJsonArray(key);

                        if (Category != null && !Category.isEmpty()) {
                            for (JsonElement subElement : Category) {
                                JsonObject subObject = subElement.getAsJsonObject();

                                for (String partKey : subObject.keySet()) {
                                    JsonElement value = subObject.get(partKey);
                                    JsonArray parts = subObject.getAsJsonArray(partKey);

                                    if (parts != null && !parts.isEmpty()) {
                                        for (JsonElement partElement : parts) {
                                            JsonObject partObject = partElement.getAsJsonObject();
                                            for (String partElementKey : partObject.keySet()) {
                                                String word = partObject.get(partElementKey).getAsString();
                                                System.out.println(word);

                                            }

                                        }
                                    }
                                }


                            }
                        }


                    }
                }
                // Close the reader
                reader.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

}
