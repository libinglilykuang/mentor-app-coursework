package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;

public class ActivitySignUpView extends VBox {
    private final MenteeController controller;
    private final ObservableList<SupportType> activityList;
    private final ListView<SupportType> listView;
    private final TextField searchField;

    public ActivitySignUpView(MenteeController controller, Stage stage) {
        this.controller = controller;
        this.activityList = FXCollections.observableArrayList(SupportType.values());
        this.listView = new ListView<>(activityList);
        this.searchField = new TextField();
        buildView(stage);
    }

    private void buildView(Stage stage) {
        Label label = new Label("Sign up for Activities");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox filterBox = new HBox(40);
        Label mentorLabel = new Label("Mentor");
        mentorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        ComboBox<String> adviserComboBox = new ComboBox<>();
        adviserComboBox.getItems().addAll("Any", "Adviser A", "Adviser B", "Adviser C");
        adviserComboBox.setValue("Any");
        adviserComboBox.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        adviserComboBox.setPrefWidth(200);

        VBox mentorBox = new VBox(10, mentorLabel, adviserComboBox);
        mentorBox.setAlignment(Pos.CENTER_LEFT);

        Label campusLabel = new Label("Campus");
        campusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        ComboBox<String> campusComboBox = new ComboBox<>();
        campusComboBox.getItems().addAll("Any", "Jubilee", "University Park");
        campusComboBox.setValue("Any");
        campusComboBox.setStyle("-fx-background-color: white; -fx-padding: 5px;");
        campusComboBox.setPrefWidth(200);

        VBox campusBox = new VBox(10, campusLabel, campusComboBox);
        campusBox.setAlignment(Pos.CENTER_LEFT);

        filterBox.getChildren().addAll(mentorBox, campusBox);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        VBox headerBox = new VBox(10, label, filterBox, searchField);
        headerBox.setStyle("-fx-background-color: #0278a4; -fx-padding: 15px;");
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.TOP_LEFT);

        searchField.setPromptText("Search Activities...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterActivities(newValue));
        searchField.setStyle("-fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label titleLabel = new Label("What would you like to talk about?");
        titleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");
        VBox titleBox = new VBox(titleLabel, searchField);
        titleBox.setStyle("-fx-background-color: #0056a1; -fx-padding: 10;");
        titleBox.setAlignment(Pos.CENTER_LEFT);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SupportType activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                } else {
                    setText(activity.name());
                }
            }
        });
        listView.setMinWidth(400); // Minimum width
        listView.setMinHeight(350); // Minimum height
        listView.setPrefWidth(500); // Preferred width
        listView.setPrefHeight(550); // Preferred height

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        submitButton.setOnMouseEntered(e -> submitButton.setStyle(
                "-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle(
                "-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));

        submitButton.setOnAction(e -> {
            SupportType selectedType = listView.getSelectionModel().getSelectedItem();
            if (selectedType != null) {
                controller.requestSupport(selectedType);
                System.out.println("Activity sign-up submitted: " + selectedType);
                showAlert("Activity Sign-up", "You have successfully signed up for: " + selectedType);
            } else {
                showAlert("Error", "Please select an activity before submitting.");
            }
        });

        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(headerBox, searchField,titleBox,listView,submitButton);
    }

    private void filterActivities(String filter) {
        if (filter == null || filter.isEmpty()) {
            listView.setItems(activityList);
        } else {
            ObservableList<SupportType> filteredList = FXCollections.observableArrayList();
            for (SupportType activity : activityList) {
                if (activity.name().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(activity);
                }
            }
            listView.setItems(filteredList);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private VBox createWhiteContent() {
        Label contentLabel = new Label("This is the white content area.");
        contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        VBox whiteContent = new VBox(contentLabel);
        whiteContent.setStyle("-fx-background-color: white; -fx-padding: 20;"); // 设置白色背景
        whiteContent.setAlignment(Pos.TOP_CENTER);
        whiteContent.setPrefHeight(400);

        return whiteContent;
    }
}
