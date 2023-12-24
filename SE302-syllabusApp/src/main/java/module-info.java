module com.example.se302syllabusapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires json.simple;
    requires org.glassfish.java.json;
    requires poi.ooxml;


    opens com.example.se302syllabusapp to javafx.fxml;
    exports com.example.se302syllabusapp;
}