package com.example.se302syllabusapp;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class GUIController implements Initializable {
    @FXML
    public Button compareButton;
    @FXML
    public BorderPane syllabusParent;
    public Button saveButton;
    @FXML
    public TextArea descriptionValue;
    @FXML
    public VBox page1;
    public VBox page2;
    public VBox page3;
    public VBox page4;
    public VBox page5;
    @FXML
    private VBox parentVBox;
    @FXML
    private HBox parentHBox;
    private Stage popup;



    FileManager fileManager;
    // Todo bunlari dosyala
    private Stage primaryStage;
    Controllers controllers;
    VersionController versionController;

    public GUIController(){
    }



    public void importSyllabus(){


        File storagePath = new File("storage");
        if (!storagePath.exists()) {
            System.out.println("Storage file does not exist!");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose JSON File");
        chooser.setInitialDirectory(storagePath);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        File selectedFile = chooser.showOpenDialog(new Popup());

        // Buralar deneme amacli yazildi kod calisiyor ama duzenlenmesi gerek
        SyllabusData syllabusData = new SyllabusData();

        if (selectedFile != null) {
            setControllers(new Controllers(new SyllabusData(),new SyllabusData()));
            getControllers().setJsonFile(new File(selectedFile.getAbsolutePath()));


            if (getControllers().getSyllabusData1() != null) {
                syllabusData = getControllers().getSyllabusData1();
            }
        }

        System.out.println(syllabusData.getName());


//        System.out.println(syllabusData.name);

        //getFileManager().setJsonFile(new File(""));

        //SyllabusData syllabusData = getFileManager().read();

    }

    public void addingSyllabus() {

        FXMLLoader syllabusLoader = new FXMLLoader(getClass().getResource("SyllabusSheet.fxml"));

        try {
            Node syllabusSheet = syllabusLoader.load();

            if (!parentVBox.getChildren().isEmpty())
                parentVBox.getChildren().remove(1);

            parentVBox.getChildren().add(syllabusSheet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void compareVersions(VBox parentVBox) {
        if (popup != null) {
            System.out.println(popup.getTitle());
            popup.close();
        }
        FXMLLoader compareLoader = new FXMLLoader(getClass().getResource("ComparePage.fxml"));
        try {
            Node syllabusSheet = compareLoader.load();
            System.out.println(parentVBox);

            if (!parentVBox.getChildren().isEmpty())
                parentVBox.getChildren().remove(1);

            parentVBox.getChildren().add(syllabusSheet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(parentVBox);


    }

    // TODO bunu dosyaya eklemeyı unutma
    public void compareVersionsPopup(){
        BorderPane borderPane = new BorderPane();

        // Merkez
        VBox centerVBox = new VBox();
        AnchorPane centerAnchorPane = new AnchorPane();
        HBox topHBox = new HBox();
        AnchorPane topAnchorPane = new AnchorPane();
        Text courseText = new Text("Course:");
        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        Label courseLabel = new Label("Label");

        courseText.setFont(new Font(14.0));
        courseText.setLayoutX(49);
        courseText.setLayoutY(55);
        courseText.setWrappingWidth(63.13671875);

        courseChoiceBox.setLayoutX(135.0);
        courseChoiceBox.setLayoutY(37.0);
        courseChoiceBox.setPrefWidth(150);

        courseLabel.setLayoutX(344);
        courseLabel.setLayoutY(41);
        courseLabel.setPrefWidth(193);
        courseLabel.setPrefHeight(17);


        topAnchorPane.getChildren().addAll(courseText, courseChoiceBox, courseLabel);
        topHBox.getChildren().add(topAnchorPane);
        centerAnchorPane.getChildren().add(topHBox);

        VBox vBox2 = new VBox();
        vBox2.setPrefHeight(297);
        vBox2.setPrefWidth(600);
        vBox2.setSpacing(20);

        AnchorPane anchorPane = new AnchorPane();

        HBox hBox2 = new HBox();
        hBox2.setLayoutY(25);
        hBox2.prefHeight(100);
        hBox2.prefWidth(600);

        AnchorPane anchorPane2 = new AnchorPane();
        anchorPane2.prefHeight(100);
        anchorPane2.prefWidth(607);

        Text text1 = new Text("Choose version");
        text1.setFont(new Font(14));
        text1.setLayoutX(51);
        text1.setLayoutY(46);
        text1.setWrappingWidth(88.13671875);

        Button button = new Button("Choose");
        button.setLayoutX(175);
        button.setLayoutY(38);
        button.prefHeight(25);
        button.setPrefWidth(71);
        button.setOnAction(event -> {
            importSyllabus();
        });

        Label label = new Label("Label");
        label.setLayoutX(344);
        label.setLayoutY(42);

        anchorPane2.getChildren().setAll(text1,button,label);
        hBox2.getChildren().setAll(anchorPane2);

//        2. hBox

        HBox hBox3 = new HBox();
        hBox3.setLayoutY(149);
        hBox3.prefHeight(100);
        hBox3.prefWidth(600);

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.prefHeight(100);
        anchorPane3.prefWidth(607);

        Text text2 = new Text("Choose version");
        text2.setFont(new Font(14));
        text2.setLayoutX(51);
        text2.setLayoutY(46);
        text2.setWrappingWidth(88.13671875);

        Button button1 = new Button("Choose");
        button1.setLayoutX(175);
        button1.setLayoutY(38);
        button1.prefHeight(25);
        button1.setPrefWidth(71);
        button1.setOnAction(event -> {
            importSyllabus();
        });

        Label label1 = new Label("Label");
        label1.setLayoutX(344);
        label1.setLayoutY(42);

        anchorPane3.getChildren().setAll(text2,button1,label1);
        hBox3.getChildren().setAll(anchorPane3);

        anchorPane.getChildren().setAll(hBox2,hBox3);
        vBox2.getChildren().setAll(anchorPane);

        centerVBox.getChildren().setAll(centerAnchorPane, vBox2);


//        centerVBox.getChildren().add(centerAnchorPane);

        // Alt
        AnchorPane bottomAnchorPane = new AnchorPane();
        bottomAnchorPane.setPrefHeight(23);
        bottomAnchorPane.setPrefWidth(600);

        Button compareButton = new Button("Button");
        compareButton.setLayoutX(511.0);
        compareButton.setLayoutY(-12.0);
        compareButton.setPrefHeight(25);
        compareButton.setPrefWidth(69);

        bottomAnchorPane.getChildren().add(compareButton);
        borderPane.setBottom(bottomAnchorPane);
        borderPane.setCenter(centerVBox);

        // Add your other components here
        compareButton.setOnAction(event -> {
            // Call another method when the button is clicked
            compareVersions(parentVBox);
            popup.close();
        });

        Scene scene = new Scene(borderPane, 600, 400);
        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Compare Versions");
        popup.setResizable(false);
        popup.setScene(scene);

        popup.showAndWait();
    }

    public void deletePopup(){
        FXMLLoader popDelete = new FXMLLoader(getClass().getResource("deletePage.fxml"));
        try {
            Parent comparePopup = popDelete.load();
            setPopup(new Stage());
            popup.initOwner(getPrimaryStage());
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Select Version");
            popup.setResizable(false);
            popup.setScene(new Scene(comparePopup));

            popup.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteSyllabus(){
        deletePopup();
    }

    public void saveButtonFunctionality() {

        String desc = ((TextArea) ((AnchorPane) syllabusParent.getChildren().get(0)).getChildren().get(1)).getText();
        long start = System.nanoTime();
        filterInput(page1);
        filterInput(page2);
        filterInput(page3);
        filterInput(page4);
        filterInput(page5);
        long end = System.nanoTime();
        System.out.println(end-start);
    }

    public void filterInput(Node node) {

        if (node instanceof TextField || node instanceof TextArea) {
            // Text values of TextAreas and TextFields.
            String textValues = ((TextInputControl) node).getText();
//            System.out.println("TextValues----------------");
//            System.out.println(textValues);
        } else if (node instanceof RadioButton) {
            System.out.println("RadioButtons----------------");
            if (((RadioButton) node).isSelected()) {
                System.out.println(((RadioButton) node).getText());
            }
        } else if (node instanceof CheckBox) {
            System.out.println("CheckBox----------------");
            if (((CheckBox) node).isSelected()) {
                System.out.println(((CheckBox) node).getUserData());
            }
        } else {
            if (node instanceof Parent parent) {
                // If the node is a Parent (e.g., VBox, HBox), recursively search its children
                for (Node child : parent.getChildrenUnmodifiable()) {
                    filterInput(child); // Recursive call for each child
                }
            }
        }
    }

    public void onCheckBoxClicked(ActionEvent event) {

        Parent parent = ((AnchorPane) ((CheckBox) event.getSource()).getParent()).getParent();
        if (parent instanceof VBox) {
            VBox parentVBox = (VBox) ((AnchorPane) ((CheckBox) event.getSource()).getParent()).getParent();

            for (Node child: parentVBox.getChildren()) {
                if (child instanceof AnchorPane) {
                    for (Node checkBox: ((AnchorPane) child).getChildren()) {
                        if (checkBox instanceof CheckBox) {
                            if (((CheckBox) checkBox).isSelected() && event.getSource() != checkBox) {
                                ((CheckBox) checkBox).setSelected(false);
                            }
                        }
                    }
                }
            }
        }
        else {
            HBox parentHBox = (HBox) ((AnchorPane) ((CheckBox) event.getSource()).getParent()).getParent();
            for (Node child: parentHBox.getChildren()) {
                if (child instanceof AnchorPane) {
                    for (Node checkBox: ((AnchorPane) child).getChildren()) {
                        if (checkBox instanceof CheckBox) {
                            if (((CheckBox) checkBox).isSelected() && event.getSource() != checkBox) {
                                ((CheckBox) checkBox).setSelected(false);
                            }
                        }
                    }
                }
            }
        }



    }




    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    // Todo bunlari dosyala
    public Stage getPopup() {
        return popup;
    }

    public void setPopup(Stage popup) {
        this.popup = popup;
    }

    public Controllers getControllers() {
        return controllers;
    }

    public void setControllers(Controllers controllers) {
        this.controllers = controllers;
    }

    public VersionController getVersionController() {
        return versionController;
    }

    public void setVersionController(VersionController versionController) {
        this.versionController = versionController;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
