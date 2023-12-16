package com.example.se302syllabusapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Controllers extends FileManager{

    public Controllers(SyllabusData syllabusData1, SyllabusData syllabusData2) {
        super(syllabusData1, syllabusData2);
    }

    public void create(SyllabusData syllabusData){
        

    }

    public void update(){

    }

    public void delete(){

    }

    public void fileExport(){

    }

    public void fileImport(){

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
