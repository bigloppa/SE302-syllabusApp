package com.example.se302syllabusapp;

import java.util.ArrayList;

public class SyllabusData {
    String name;
    String value;
    ArrayList<SyllabusData> children;

    public SyllabusData() {
        this.name = "";
        this.value = "";
        this.children = new ArrayList<>();
    }

    public SyllabusData(String name, String value, ArrayList<SyllabusData> children) {
        this.name = name;
        this.value = value;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<SyllabusData> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SyllabusData> children) {
        this.children = children;
    }
}








