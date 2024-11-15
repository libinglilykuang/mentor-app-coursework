package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MentorAvailabilityView extends VBox {
    private final Mentor mentor;
    private final DatePicker startDatePicker = new DatePicker();
    private final ComboBox<String> startTimePicker = new ComboBox<>();
    private final DatePicker endDatePicker = new DatePicker();
    private final ComboBox<String> endTimePicker = new ComboBox<>();
    private final Label availabilityLabel = new Label("You have not set any available time yet.");

    public MentorAvailabilityView(Mentor mentor) {
        this.mentor = mentor;
        buildView();
    }

    private void buildView() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: /pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label signingLabel = new Label("Set Available Time");
        Label viewMentorLabel = new Label("View Assigned Session");
        Label mentorHomeLabel = new Label("Home");

        mentorHomeLabel.setStyle(getInactiveStyle());
        signingLabel.setStyle(getActiveStyle());
        viewMentorLabel.setStyle(getInactiveStyle());

        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        viewMentorLabel.setOnMouseClicked(e->getScene().setRoot(new AssignedMenteeView(mentor)));

        // Navigation bar
        HBox labelContainer = new HBox(20, mentorHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = createUserProfileButton();
        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileBox);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(profileBox, Priority.ALWAYS);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        setStyle("-fx-background-color: white;");

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f9f9f9;");

        Label titleLabel = new Label("Set Your Availability");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label descriptionLabel = new Label("Choose dates and times when you will be available.");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill:white;");
        VBox headerBox = new VBox(5, titleLabel, descriptionLabel);
        headerBox.setAlignment(Pos.TOP_LEFT);
        headerBox.setStyle("-fx-background-color: #0278a4; -fx-padding: 15;");

        initializeTimePickers();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(15));
        gridPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10px; -fx-padding: 15px;");
        styleDatePicker(startDatePicker);
        styleDatePicker(endDatePicker);
        styleComboBox(startTimePicker);
        styleComboBox(endTimePicker);
        gridPane.add(createStyledLabel("Start Date:"), 0, 0);
        gridPane.add(startDatePicker, 1, 0);
        gridPane.add(createStyledLabel("Start Time:"), 2, 0);
        gridPane.add(startTimePicker, 3, 0);
        gridPane.add(createStyledLabel("End Date:"), 0, 1);
        gridPane.add(endDatePicker, 1, 1);
        gridPane.add(createStyledLabel("End Time:"), 2, 1);
        gridPane.add(endTimePicker, 3, 1);

        Button saveButton = createStyledButton("Save Availability", "#0b3d61", "#04253d");
        saveButton.setOnAction(e -> saveAvailability());
        HBox buttonBox = new HBox(10, saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 30, 0));

        Label availabilityTitle = new Label("Your Selected Time Slots");
        availabilityTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox belowBox = new VBox(5, availabilityTitle);
        belowBox.setAlignment(Pos.TOP_LEFT);
        belowBox.setStyle("-fx-background-color: #0278a4; -fx-padding: 15;");
        belowBox.setPadding(new Insets(0, 0, 5, 0));

        availabilityLabel.setWrapText(true);
        VBox availabilityBox = new VBox(10, availabilityLabel);
        availabilityBox.setAlignment(Pos.TOP_LEFT);
        availabilityBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10px; -fx-padding: 15px;");
        availabilityBox.setPadding(new Insets(5));

        getChildren().addAll(topContainer,headerBox, gridPane, buttonBox,belowBox,availabilityBox);
    }

    private HBox createUserProfileButton() {
        // User icon
        ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/user_icon.png"))); // Replace with your icon path
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);

        // Username label
        Label usernameLabel = new Label(mentor.getUsername());
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");

        // Combine user icon and username
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.setAlignment(Pos.CENTER_RIGHT);
        profileButton.setPadding(new Insets(5));
        profileButton.setStyle("-fx-cursor: hand;");

        // Add click event to navigate to EditProfileView
        profileButton.setOnMouseClicked(e -> {
            // Ensure the Scene and Window are accessed during the event
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene(); // Safely get the current scene
            stage.setScene(new Scene(new EditProfileView(new MentorController(mentor), currentScene,true), 800, 600));
        });

        return profileButton;
    }

    private void saveAvailability() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTime = startTimePicker.getValue();
        String endTime = endTimePicker.getValue();

        if (startDate == null || endDate == null || startTime == null || endTime == null) {
            availabilityLabel.setText("You have not set any available time yet.");
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));

        if (endDateTime.isBefore(startDateTime)) {
            availabilityLabel.setText("You have not set any available time yet.");
            showAlert("Error", "End time cannot be earlier than start time.");
            return;
        }

        String formattedStart = startDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        String formattedEnd = endDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        availabilityLabel.setText("From: " + formattedStart + "\n" + "To: " + formattedEnd);
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");

        showAlert("Success", "Availability set successfully.");
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-text-fill: #333333;");
        return label;
    }

    private Button createStyledButton(String text, String defaultColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle(getButtonStyle(defaultColor));

        button.setOnMouseEntered(e -> button.setStyle(getButtonStyle(hoverColor)));
        button.setOnMouseExited(e -> button.setStyle(getButtonStyle(defaultColor)));
        return button;
    }

    private String getButtonStyle(String backgroundColor) {
        return String.format(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-background-color: %s; " +
                        "-fx-cursor: hand;",
                backgroundColor
        );
    }

    private void styleDatePicker(DatePicker datePicker) {
        datePicker.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: #0b3d61; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px;");
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-border-color: #0b3d61; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px;");
    }

    private void initializeTimePickers() {
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d:00", i);
            startTimePicker.getItems().add(hour);
            endTimePicker.getItems().add(hour);
        }
        startTimePicker.setValue("09:00");
        endTimePicker.setValue("17:00");
    }

    private void switchToMentorView() {
        Stage stage = (Stage) getScene().getWindow();
        MentorController mentorController = new MentorController(mentor);
        stage.setScene(new Scene(new MentorView(mentorController), 400, 300));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }
}
