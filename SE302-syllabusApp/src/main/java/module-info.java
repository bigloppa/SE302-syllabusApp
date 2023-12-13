module com.example.se302syllabusapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires org.glassfish.java.json;


    opens com.example.se302syllabusapp to javafx.fxml;
    exports com.example.se302syllabusapp;
}