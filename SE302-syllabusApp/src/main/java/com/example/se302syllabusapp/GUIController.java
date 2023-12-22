package com.example.se302syllabusapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    public TextArea versionDescriptions;
    @FXML
    private VBox parentVBox;
    @FXML
    private VBox lo;
    @FXML
    private HBox parentHBox;
    private Stage popup;
    @FXML
    private TextField learningOutcomesField;



    @FXML
    private ComboBox<String> comboBox;



    FileManager fileManager;
    // Todo bunlari dosyala
    private Stage primaryStage;
    Controllers controllers;
    VersionController versionController;

    ArrayList<String> syllabusData;
    private int INDEX_FOR_DATA_PASSING;

    private int counterOfCheckBoxes;

    public GUIController(){
        syllabusData = new ArrayList<>(500);
        controllers = new Controllers();
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

        SyllabusData syllabusData;

        if (selectedFile != null) {
            setControllers(new Controllers(new SyllabusData(),new SyllabusData()));
            getControllers().setJsonFile(new File(selectedFile.getAbsolutePath()));
            syllabusData = getControllers().read();


            if (syllabusData != null) {

                FXMLLoader syllabusLoader = new FXMLLoader(getClass().getResource("SyllabusSheet.fxml"));
                ArrayList<String> data = syllabusData.getAttributes(new ArrayList<>());

                try {
                    Node syllabusSheet = syllabusLoader.load();

                    // This two line for allocating syllabus data only
                    BorderPane borderPane = (BorderPane) syllabusSheet;
                    ScrollPane scrollPane = (ScrollPane) borderPane.getChildren().get(1);

                    passValuesToSyllabusSheet(scrollPane, data);

                    if (!parentVBox.getChildren().isEmpty())
                        parentVBox.getChildren().remove(1);

                    parentVBox.getChildren().add(syllabusSheet);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }



    }

    public void addingSyllabus() {

        FXMLLoader learningOutcomesLoader = new FXMLLoader(getClass().getResource("LearningOutcomes.fxml"));

        try {
            Node learningOutcomesSheet = learningOutcomesLoader.load();

            if (!parentVBox.getChildren().isEmpty())
                parentVBox.getChildren().remove(1);

            parentVBox.getChildren().add(learningOutcomesSheet);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public void continueButtonClicked(ActionEvent event) {
        try {
            int numberOfLearningOutcomes = Integer.parseInt(learningOutcomesField.getText());

            try {
                if (!lo.getChildren().isEmpty())
                   lo.getChildren().remove(0);


                FXMLLoader syllabusLoader = new FXMLLoader(getClass().getResource("SyllabusSheet.fxml"));
                Node syllabusSheet = syllabusLoader.load();


                GUIController syllabusSheetController = syllabusLoader.getController();
                syllabusSheetController.handleNumberOfLearningOutcomes(numberOfLearningOutcomes);


                lo.getChildren().add(syllabusSheet);


            } catch (IOException e) {
                System.out.println("Error loading Syllabus Sheet:");
                e.printStackTrace();
                if (e.getCause() != null) {
                    Throwable cause = e.getCause();
                    System.out.println("Wrapped Exception:");
                    cause.printStackTrace();
                }
            }

        } catch (NumberFormatException e) {
            // Handle invalid input (non-numeric value)
            showAlert("Error", "Invalid Input", "Please enter a valid number for learning outcomes.");
        }
    }
    public void handleNumberOfLearningOutcomes(int numberOfLearningOutcomes) {

        System.out.println("Number of Learning Outcomes: " + numberOfLearningOutcomes);
    }



    public void compareVersions(VBox parentVBox) {
        if (popup != null) {
            System.out.println(popup.getTitle());
            popup.close();
        }
        FXMLLoader compareLoader = new FXMLLoader(getClass().getResource("ComparePage.fxml"));
        try {
            // version numbers and description will be added
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
        ChoiceBox<String> langChoiceBox = new ChoiceBox<>();
        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        ChoiceBox<String> versionChoiceBox1 = new ChoiceBox<>();
        ChoiceBox<String> versionChoiceBox2 = new ChoiceBox<>();

        VBox centerVBox = new VBox();
        AnchorPane centerAnchorPane = new AnchorPane();
        HBox topHBox = new HBox();
        AnchorPane topAnchorPane = new AnchorPane();
        Text courseText = new Text("Language:");

        langChoiceBox.getItems().addAll("en","tr");
        Label languageLabel = new Label();
        langChoiceBox.setOnAction(event -> {
            String selectedOption = langChoiceBox.getValue();
            languageLabel.setText( selectedOption);
            courseChoiceBox.getItems().clear();
            versionChoiceBox1.getItems().clear();
            versionChoiceBox2.getItems().clear();
            try {
                Files.newDirectoryStream(Paths.get("storage/" + selectedOption))
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            courseChoiceBox.getItems().add(fileName);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        courseText.setFont(new Font(14.0));
        courseText.setLayoutX(49);
        courseText.setLayoutY(55);
        courseText.setWrappingWidth(64.13671875);

        langChoiceBox.setLayoutX(135.0);
        langChoiceBox.setLayoutY(37.0);
        langChoiceBox.setPrefWidth(150);

        languageLabel.setLayoutX(344);
        languageLabel.setLayoutY(41);
        languageLabel.setPrefWidth(193);
        languageLabel.setPrefHeight(17);


        topAnchorPane.getChildren().addAll(courseText, langChoiceBox, languageLabel);
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

        Text text1 = new Text("Course: ");
        text1.setFont(new Font(14));
        text1.setLayoutX(51);
        text1.setLayoutY(46);
        text1.setWrappingWidth(88.13671875);

        courseChoiceBox.setLayoutX(135.0);
        courseChoiceBox.setLayoutY(37.0);
        courseChoiceBox.setPrefWidth(150);

        Label courseLabel = new Label();
        courseLabel.setLayoutX(344);
        courseLabel.setLayoutY(42);

        courseChoiceBox.setOnAction(event -> {
            String selectedOption = courseChoiceBox.getValue();
            courseLabel.setText( selectedOption);
            versionChoiceBox1.getItems().clear();
            versionChoiceBox2.getItems().clear();
            try {
                Files.newDirectoryStream(Paths.get("storage/" + langChoiceBox.getValue() + "/" + selectedOption))
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            versionChoiceBox1.getItems().add(fileName);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        anchorPane2.getChildren().setAll(text1,courseChoiceBox,courseLabel);
        hBox2.getChildren().setAll(anchorPane2);

//        2. hBox

        HBox hBox3 = new HBox();
        hBox3.setLayoutY(100);
        hBox3.prefHeight(100);
        hBox3.prefWidth(600);

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.prefHeight(100);
        anchorPane3.prefWidth(607);

        Text text2 = new Text("First chosen version: ");
        text2.setFont(new Font(14));
        text2.setLayoutX(51);
        text2.setLayoutY(46);
        text2.setWrappingWidth(88.13671875);

        versionChoiceBox1.setLayoutX(135.0);
        versionChoiceBox1.setLayoutY(37.0);
        versionChoiceBox1.setPrefWidth(150);

        Label versionLabel1 = new Label();
        versionLabel1.setLayoutX(344);
        versionLabel1.setLayoutY(42);

        versionChoiceBox1.setOnAction(event -> {
            String selectedOption = versionChoiceBox1.getValue();
            versionLabel1.setText( selectedOption);
            versionChoiceBox2.getItems().clear();
            versionChoiceBox2.getItems().addAll(versionChoiceBox1.getItems());
            versionChoiceBox2.getItems().remove(selectedOption);

        });

        anchorPane3.getChildren().setAll(text2,versionChoiceBox1, versionLabel1);
        hBox3.getChildren().setAll(anchorPane3);

        //        3. hBox

        HBox hBox4 = new HBox();
        hBox4.setLayoutY(180);
        hBox4.prefHeight(100);
        hBox4.prefWidth(600);

        AnchorPane anchorPane4 = new AnchorPane();
        anchorPane4.prefHeight(100);
        anchorPane4.prefWidth(607);

        Text text3 = new Text("Second chosen version: ");
        text3.setFont(new Font(14));
        text3.setLayoutX(51);
        text3.setLayoutY(46);
        text3.setWrappingWidth(90.13671875);

        versionChoiceBox2.setLayoutX(135.0);
        versionChoiceBox2.setLayoutY(37.0);
        versionChoiceBox2.setPrefWidth(150);

        Label versionLabel2 = new Label();
        versionLabel2.setLayoutX(344);
        versionLabel2.setLayoutY(42);

        versionChoiceBox2.setOnAction(event -> {
            String selectedOption = versionChoiceBox2.getValue();
            versionLabel2.setText( selectedOption);
        });

        anchorPane4.getChildren().setAll(text3,versionChoiceBox2, versionLabel2);
        hBox4.getChildren().setAll(anchorPane4);

        anchorPane.getChildren().setAll(hBox2,hBox3,hBox4);
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
        Controllers controllers1 = new Controllers();
        BorderPane borderPane = new BorderPane();

        // Merkez
        ChoiceBox<String> langChoiceBox = new ChoiceBox<>();
        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        ChoiceBox<String> versionChoiceBox = new ChoiceBox<>();

        VBox centerVBox = new VBox();
        AnchorPane centerAnchorPane = new AnchorPane();
        HBox topHBox = new HBox();
        AnchorPane topAnchorPane = new AnchorPane();
        Text courseText = new Text("Language:");

        langChoiceBox.getItems().addAll("en","tr");
        Label languageLabel = new Label();
        langChoiceBox.setOnAction(event -> {
            String selectedOption1 = langChoiceBox.getValue();
            languageLabel.setText( selectedOption1);
            courseChoiceBox.getItems().clear();
            try {
                Files.newDirectoryStream(Paths.get("storage/" + selectedOption1))
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            courseChoiceBox.getItems().add(fileName);
                            System.out.println(fileName);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        courseText.setFont(new Font(14.0));
        courseText.setLayoutX(49);
        courseText.setLayoutY(55);
        courseText.setWrappingWidth(65.13671875);

        langChoiceBox.setLayoutX(135.0);
        langChoiceBox.setLayoutY(37.0);
        langChoiceBox.setPrefWidth(150);

        languageLabel.setLayoutX(344);
        languageLabel.setLayoutY(41);
        languageLabel.setPrefWidth(193);
        languageLabel.setPrefHeight(17);


        topAnchorPane.getChildren().addAll(courseText, langChoiceBox, languageLabel);
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

        Text text1 = new Text("Course: ");
        text1.setFont(new Font(14));
        text1.setLayoutX(51);
        text1.setLayoutY(46);
        text1.setWrappingWidth(88.13671875);

        courseChoiceBox.setLayoutX(135.0);
        courseChoiceBox.setLayoutY(37.0);
        courseChoiceBox.setPrefWidth(150);

        Label courseLabel = new Label();
        courseLabel.setLayoutX(344);
        courseLabel.setLayoutY(42);

        courseChoiceBox.setOnAction(event -> {
            String selectedOption2 = courseChoiceBox.getValue();
            courseLabel.setText( selectedOption2);
            versionChoiceBox.getItems().clear();
            try {
                Files.newDirectoryStream(Paths.get("storage/" + langChoiceBox.getValue() + "/" + selectedOption2))
                        .forEach(path -> {
                            String fileName = path.getFileName().toString();
                            versionChoiceBox.getItems().add(fileName);
                            System.out.println(fileName);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        anchorPane2.getChildren().setAll(text1,courseChoiceBox,courseLabel);
        hBox2.getChildren().setAll(anchorPane2);

//        2. hBox

        HBox hBox3 = new HBox();
        hBox3.setLayoutY(149);
        hBox3.prefHeight(100);
        hBox3.prefWidth(600);

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.prefHeight(100);
        anchorPane3.prefWidth(607);

        Text text2 = new Text("Version: ");
        text2.setFont(new Font(14));
        text2.setLayoutX(51);
        text2.setLayoutY(46);
        text2.setWrappingWidth(88.13671875);

        versionChoiceBox.setLayoutX(135.0);
        versionChoiceBox.setLayoutY(37.0);
        versionChoiceBox.setPrefWidth(150);

        Label versionLabel = new Label();
        versionLabel.setLayoutX(344);
        versionLabel.setLayoutY(42);

        versionChoiceBox.setOnAction(event -> {
            String selectedOption3 = versionChoiceBox.getValue();
            versionLabel.setText( selectedOption3);
        });

        anchorPane3.getChildren().setAll(text2,versionChoiceBox, versionLabel);
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
            controllers1.delete("storage/" + langChoiceBox.getValue() + "/" +
                    courseChoiceBox.getValue() + "/" + versionChoiceBox.getValue(), courseChoiceBox.getValue());
            popup.close();
        });

        Scene scene = new Scene(borderPane, 600, 400);
        // Scene
        setPopup(new Stage());
        popup.initOwner(getPrimaryStage());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Delete Version");
        popup.setResizable(false);
        popup.setScene(scene);

        popup.showAndWait();
    }


    public void deleteSyllabus(){
        deletePopup();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void saveButtonFunctionality() {

        counterOfCheckBoxes = 0;

        filterInput(page1);
        filterInput(page2);
        filterInput(page3);
        filterInput(page4);
        filterInput(page5);

        if (counterOfCheckBoxes != 32){
            showAlert("Warning!", "Empty CheckBox", "Fill in the checkbox fields.");
            return;
        }


        String selectedValue = comboBox.getValue();
        if (selectedValue.equals("English")) {
            controllers.saveFromUserEntry(syllabusData, "en");
        }else if (selectedValue.equals("Turkish")){
            controllers.saveFromUserEntry(syllabusData, "tr");
        }


    }

    public void filterInput(Node node) {


        if (node instanceof TextField) {
            String textValues = ((TextInputControl) node).getText();
            syllabusData.add(textValues);



        } else if (node instanceof CheckBox) {
            String userData = "";
            if (((CheckBox) node).isSelected()) {
               userData = node.getUserData().toString();
                syllabusData.add(userData);
                counterOfCheckBoxes++;


            }


        } else {

            if (node instanceof Parent parent){
                for (Node child : parent.getChildrenUnmodifiable()) {
                    filterInput(child);


                }
            }
        }
    }

    public void passValuesToSyllabusSheet(Node node, ArrayList<String> syllabusData) {

        if (node instanceof TextField) {

            ((TextField) node).setText(syllabusData.get(INDEX_FOR_DATA_PASSING));
            INDEX_FOR_DATA_PASSING++;

        } else if (node instanceof CheckBox) {
            if (syllabusData.get(INDEX_FOR_DATA_PASSING).equals(node.getUserData())) {
                ((CheckBox) node).setSelected(true);
                INDEX_FOR_DATA_PASSING++;
            }
        }
        else {
            if (node instanceof Parent) {
                if (node instanceof ScrollPane) {
                    passValuesToSyllabusSheet(((ScrollPane) node).getContent(), syllabusData);
                }
                else {
                    for (Node child: ((Parent) node).getChildrenUnmodifiable()) {
                        passValuesToSyllabusSheet(child, syllabusData);
                    }
                }
            }
        }
    }

    public void onCheckBoxClicked(ActionEvent event) {

        Parent parent = ((AnchorPane) ((CheckBox) event.getSource()).getParent()).getParent();

        if (parent instanceof VBox || parent instanceof HBox || parent instanceof BorderPane) {
            Pane parentPane = (Pane) parent;

            for (Node child : parentPane.getChildren()) {
                if (child instanceof AnchorPane) {
                    for (Node checkBox : ((AnchorPane) child).getChildren()) {
                        if (checkBox instanceof CheckBox) {
                            ((CheckBox) checkBox).setSelected(event.getSource() == checkBox);
                        }
                    }
                }
            }
        }

    }

    public void exportPopup() {
        BorderPane borderPane = new BorderPane();

        // Merkez
        VBox centerVBox = new VBox();
        AnchorPane centerAnchorPane = new AnchorPane();
        HBox topHBox = new HBox();
        AnchorPane topAnchorPane = new AnchorPane();
        Text courseText = new Text("Language:");
        ChoiceBox<String> langChoiceBox = new ChoiceBox<>();
        langChoiceBox.getItems().addAll("en","tur");
        Label languageLabel = new Label();
        langChoiceBox.setOnAction(event -> {
            String selectedOption = langChoiceBox.getValue();
            languageLabel.setText( selectedOption);
        });

        courseText.setFont(new Font(14.0));
        courseText.setLayoutX(49);
        courseText.setLayoutY(55);
        courseText.setWrappingWidth(64.13671875);

        langChoiceBox.setLayoutX(135.0);
        langChoiceBox.setLayoutY(37.0);
        langChoiceBox.setPrefWidth(150);

        languageLabel.setLayoutX(344);
        languageLabel.setLayoutY(41);
        languageLabel.setPrefWidth(193);
        languageLabel.setPrefHeight(17);


        topAnchorPane.getChildren().addAll(courseText, langChoiceBox, languageLabel);
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

        Text text1 = new Text("Course: ");
        text1.setFont(new Font(14));
        text1.setLayoutX(51);
        text1.setLayoutY(46);
        text1.setWrappingWidth(88.13671875);

        ChoiceBox<String> courseChoiceBox = new ChoiceBox<>();
        courseChoiceBox.getItems().addAll("se302","ce323");
        courseChoiceBox.setLayoutX(135.0);
        courseChoiceBox.setLayoutY(37.0);
        courseChoiceBox.setPrefWidth(150);

        Label courseLabel = new Label();
        courseLabel.setLayoutX(344);
        courseLabel.setLayoutY(42);

        courseChoiceBox.setOnAction(event -> {
            String selectedOption = courseChoiceBox.getValue();
            courseLabel.setText( selectedOption);
        });

        anchorPane2.getChildren().setAll(text1,courseChoiceBox,courseLabel);
        hBox2.getChildren().setAll(anchorPane2);

//        2. hBox

        HBox hBox3 = new HBox();
        hBox3.setLayoutY(100);
        hBox3.prefHeight(100);
        hBox3.prefWidth(600);

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.prefHeight(100);
        anchorPane3.prefWidth(607);

        Text text2 = new Text("First chosen version: ");
        text2.setFont(new Font(14));
        text2.setLayoutX(51);
        text2.setLayoutY(46);
        text2.setWrappingWidth(88.13671875);

        ChoiceBox<String> versionChoiceBox1 = new ChoiceBox<>();
        versionChoiceBox1.getItems().setAll("V1","V2","V3");
        versionChoiceBox1.setLayoutX(135.0);
        versionChoiceBox1.setLayoutY(37.0);
        versionChoiceBox1.setPrefWidth(150);

        Label versionLabel1 = new Label();
        versionLabel1.setLayoutX(344);
        versionLabel1.setLayoutY(42);

        versionChoiceBox1.setOnAction(event -> {
            String selectedOption = versionChoiceBox1.getValue();
            versionLabel1.setText( selectedOption);
        });

        anchorPane3.getChildren().setAll(text2,versionChoiceBox1, versionLabel1);
        hBox3.getChildren().setAll(anchorPane3);

        //        3. hBox

        HBox hBox4 = new HBox();
        hBox4.setLayoutY(180);
        hBox4.prefHeight(100);
        hBox4.prefWidth(600);

        AnchorPane anchorPane4 = new AnchorPane();
        anchorPane4.prefHeight(100);
        anchorPane4.prefWidth(607);

        Text text3 = new Text("File Type: ");
        text3.setFont(new Font(14));
        text3.setLayoutX(51);
        text3.setLayoutY(46);
        text3.setWrappingWidth(90.13671875);

        ChoiceBox<String> versionChoiceBox2 = new ChoiceBox<>();
        versionChoiceBox2.getItems().setAll("JSON","HTML","WORD");
        versionChoiceBox2.setLayoutX(135.0);
        versionChoiceBox2.setLayoutY(37.0);
        versionChoiceBox2.setPrefWidth(150);

        Label versionLabel2 = new Label();
        versionLabel2.setLayoutX(344);
        versionLabel2.setLayoutY(42);

        versionChoiceBox2.setOnAction(event -> {
            String selectedOption = versionChoiceBox2.getValue();
            versionLabel2.setText( selectedOption);
        });

        anchorPane4.getChildren().setAll(text3,versionChoiceBox2, versionLabel2);
        hBox4.getChildren().setAll(anchorPane4);

        anchorPane.getChildren().setAll(hBox2,hBox3,hBox4);
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
        popup.setTitle("Export File");
        popup.setResizable(false);
        popup.setScene(scene);

        popup.showAndWait();

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
