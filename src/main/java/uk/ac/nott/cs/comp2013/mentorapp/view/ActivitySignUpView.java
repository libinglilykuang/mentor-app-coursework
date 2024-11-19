package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import java.net.URL;
import java.util.Objects;

public class ActivitySignUpView extends VBox {
    private final MenteeController menteeController;
    private final ObservableList<SupportType> activityList;
    private final ListView<SupportType> listView;
    private final TextField searchField;

    public ActivitySignUpView(MenteeController controller) {
        this.menteeController = controller;
        this.activityList = FXCollections.observableArrayList(SupportType.values());
        this.listView = new ListView<>(activityList);
        this.searchField = new TextField();
        buildView();
    }

    private void buildView() {
        HBox topContainer = createTopContainer();
        VBox headerBox = createHeaderBox();
        configureListView();
        Button submitButton = createSubmitButton();

        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(topContainer, headerBox, listView, submitButton);
    }

    private HBox createTopContainer() {
        ImageView imageView = createHeaderImage();
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label signingLabel = createStyledLabel("Signing up for Activities", getActiveStyle());
        Label viewMentorLabel = createStyledLabel("View Assigned Mentor", getInactiveStyle());
        Label menteeHomeLabel = createStyledLabel("Home", getInactiveStyle());
        menteeHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MenteeView(menteeController)));
        viewMentorLabel.setOnMouseClicked(e -> getScene().setRoot(new AssignedMentorView(menteeController)));
        HBox labelContainer = new HBox(20, menteeHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = createUserProfileButton();
        HBox.setHgrow(profileBox, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileBox);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        topContainer.setStyle("-fx-background-color: white;");
        return topContainer;
    }

    private ImageView createHeaderImage() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private VBox createHeaderBox() {
        Label titleLabel = createStyledLabel("Sign up for Activities",
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox filterBox = createFilterBox();
        searchField.setPromptText("Search Activities...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterActivities(newValue));
        searchField.setStyle("-fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        VBox headerBox = new VBox(15, titleLabel, filterBox, searchField);
        headerBox.setStyle("-fx-background-color:#0278a4; -fx-padding: 20;");
        headerBox.setAlignment(Pos.TOP_LEFT);
        return headerBox;
    }

    private Label createStyledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    private HBox createUserProfileButton() {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/user_icon.png")))); // Replace with your icon path
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);

        Label usernameLabel = new Label(menteeController.getMentee().getUsername());
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");

        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.setAlignment(Pos.CENTER_RIGHT);
        profileButton.setPadding(new Insets(5));
        profileButton.setStyle("-fx-cursor: hand;");
        profileButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene();
            stage.setScene(new Scene(new EditProfileView(menteeController, currentScene,false), 800, 600));
        });
        return profileButton;
    }

    private HBox createFilterBox() {
        Label mentorLabel = new Label("Mentor:");
        mentorLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        ComboBox<String> adviserComboBox = new ComboBox<>();
        adviserComboBox.getItems().addAll(
                "Any",
                "Alumni from the department",
                "Tech Industry Professional",
                "Second Year Student",
                "Third Year Student"
        );
        adviserComboBox.setValue("Any");
        adviserComboBox.setStyle("-fx-background-color: white;");
        adviserComboBox.setPrefWidth(200);
        HBox mentorBox = new HBox(10, mentorLabel, adviserComboBox);
        mentorBox.setAlignment(Pos.CENTER_LEFT);
        HBox filterBox = new HBox(40, mentorBox);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        return filterBox;
    }

    private void configureListView() {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SupportType activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    Label activityTitle = new Label(activity.getDisplayName());
                    activityTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                    Label activityDescription = new Label(activity.getDescription());
                    activityDescription.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");

                    VBox contentBox = new VBox(5, activityTitle, activityDescription);
                    contentBox.setPadding(new Insets(10, 10, 10, 10));
                    contentBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

                    setGraphic(contentBox);
                }
            }
        });
        listView.setStyle("-fx-background-color: white; -fx-font-size: 14px;-fx-border-color: #dcdcdc; -fx-border-width: 1px;");
        listView.setMinWidth(400);
        listView.setPrefHeight(380);
    }

    private Button createSubmitButton() {
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #0b3d61; -fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setMinWidth(100); // Minimum button width
        submitButton.setMinHeight(50);
        submitButton.setOnAction(e -> {
            SupportType selectedType = listView.getSelectionModel().getSelectedItem();
            if (selectedType != null) {
                menteeController.requestSupport(selectedType);
                showSnackbar("Successfully signed up for: " + selectedType.getDisplayName(), true);
            }else {
                showSnackbar("Error: Please select an activity before submitting.", false);
            }
        });
        return submitButton;
    }

    private void showSnackbar(String message, boolean success) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle((success
                ? "-fx-background-color: #d4edda; -fx-text-fill: #155724;" // Success styling
                : "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;" // Error styling
        ) +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.3, 0, 2);");

        Scene scene = this.getScene();
        if (scene == null) return;

        Pane root = (Pane) scene.getRoot();
        StackPane snackbarPane = new StackPane(messageLabel);
        snackbarPane.setAlignment(Pos.BOTTOM_CENTER); // Position at the bottom center
        snackbarPane.setPadding(new Insets(20, 20, 50, 20)); // Adjust padding for better visibility
        root.getChildren().add(snackbarPane);

        // Auto-hide after 3 seconds
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(event -> root.getChildren().remove(snackbarPane));
        delay.play();
    }


    private void filterActivities(String filter) {
        if (filter == null || filter.isEmpty()) {
            listView.setItems(activityList);
        } else {
            ObservableList<SupportType> filteredList = FXCollections.observableArrayList();
            for (SupportType activity : activityList) {
                if (activity.getDisplayName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(activity);
                }
            }
            listView.setItems(filteredList);
        }
    }

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }

}