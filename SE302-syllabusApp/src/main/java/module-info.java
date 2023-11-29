module com.example.se302syllabusapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.se302syllabusapp to javafx.fxml;
    exports com.example.se302syllabusapp;
}