package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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
        HBox topContainer = createTopContainer();
        VBox headerBox = createHeaderBox();
        GridPane gridPane = createAvailabilityGrid();
        HBox buttonBox = createButtonBox();
        VBox belowBox = createBelowBox();
        VBox availabilityBox = createAvailabilityBox();
        getChildren().addAll(topContainer, headerBox, gridPane, buttonBox, belowBox, availabilityBox);
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f9f9f9;");
    }

    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        HBox labelContainer = createLabelContainer();
        HBox profileBox = createUserProfileButton();
        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileBox);
        styleTopContainer(topContainer, profileBox);
        return topContainer;
    }

    private VBox createImageContainer() {
        ImageView imageView = createImageView("/pairing_image.png", 170, 170);
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));
        return imageContainer;
    }

    private ImageView createImageView(String imagePath, double width, double height) {
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + imagePath);
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private HBox createLabelContainer() {
        Label mentorHomeLabel = createStyledLabel("Home", getInactiveStyle());
        Label signingLabel = createStyledLabel("Set Available Time", getActiveStyle());
        Label viewMentorLabel = createStyledLabel("View Assigned Session", getInactiveStyle());
        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        viewMentorLabel.setOnMouseClicked(e -> getScene().setRoot(new AssignedMenteeView(mentor)));
        HBox labelContainer = new HBox(20, mentorHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        return labelContainer;
    }

    private Label createStyledLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    private void styleTopContainer(HBox topContainer, HBox profileBox) {
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        topContainer.setStyle("-fx-background-color: white;");
        HBox.setHgrow(profileBox, Priority.ALWAYS);
    }

    private VBox createHeaderBox() {
        Label titleLabel = new Label("Set Your Availability");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label descriptionLabel = new Label("Choose dates and times when you will be available.");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        VBox headerBox = new VBox(5, titleLabel, descriptionLabel);
        headerBox.setAlignment(Pos.TOP_LEFT);
        headerBox.setStyle("-fx-background-color: #0278a4; -fx-padding: 15;");
        return headerBox;
    }

    private GridPane createAvailabilityGrid() {
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

        return gridPane;
    }

    private HBox createButtonBox() {
        Button saveButton = createStyledButton();
        saveButton.setOnAction(e -> saveAvailability());
        HBox buttonBox = new HBox(10, saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 30, 0));
        return buttonBox;
    }

    private VBox createBelowBox() {
        Label availabilityTitle = new Label("Your Selected Time Slots");
        availabilityTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox belowBox = new VBox(5, availabilityTitle);
        belowBox.setAlignment(Pos.TOP_LEFT);
        belowBox.setStyle("-fx-background-color: #0278a4; -fx-padding: 15;");
        belowBox.setPadding(new Insets(0, 0, 5, 0));
        return belowBox;
    }

    private VBox createAvailabilityBox() {
        availabilityLabel.setWrapText(true);
        VBox availabilityBox = new VBox(10, availabilityLabel);
        availabilityBox.setAlignment(Pos.TOP_LEFT);
        availabilityBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10px; -fx-padding: 15px;");
        availabilityBox.setPadding(new Insets(5));
        return availabilityBox;
    }

    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon();
        Label usernameLabel = createUsernameLabel(mentor.getUsername());
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        styleProfileButton(profileButton);
        attachProfileButtonAction(profileButton);
        return profileButton;
    }

    private ImageView createUserIcon() {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/user_icon.png"))));
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);
        return userIcon;
    }

    private Label createUsernameLabel(String username) {
        Label usernameLabel = new Label(username);
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");
        return usernameLabel;
    }

    private void styleProfileButton(HBox profileButton) {
        profileButton.setAlignment(Pos.CENTER_RIGHT);
        profileButton.setPadding(new Insets(5));
        profileButton.setStyle("-fx-cursor: hand;");
    }

    private void attachProfileButtonAction(HBox profileButton) {
        profileButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene();
            stage.setScene(new Scene(new EditProfileView(new MentorController(mentor), currentScene, true), 800, 600));
        });
    }

    private void saveAvailability() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTime = startTimePicker.getValue();
        String endTime = endTimePicker.getValue();
        if (startDate == null || endDate == null || startTime == null || endTime == null) {
            availabilityLabel.setText("You have not set any available time yet.");
            showSnackbar("Please fill in all fields.", false);
            return;
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));
        if (endDateTime.isBefore(startDateTime)) {
            availabilityLabel.setText("You have not set any available time yet.");
            showSnackbar("End time cannot be earlier than start time.", false);
            return;
        }
        String formattedStart = startDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        String formattedEnd = endDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        availabilityLabel.setText("From: " + formattedStart + "\n" + "To: " + formattedEnd);
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");
        showSnackbar("Availability set successfully.", true);
    }

    private void showSnackbar(String message, boolean success) {
        Label messageLabel = new Label(message);
        messageLabel.setStyle((success
                ? "-fx-background-color: #d4edda; -fx-text-fill: #155724;"
                : "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;")
                + "-fx-font-size: 14px; "
                + "-fx-padding: 10px 20px; "
                + "-fx-background-radius: 10px; "
                + "-fx-border-radius: 10px; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        Scene scene = getScene();
        if (scene == null) return;
        Pane root = (Pane) scene.getRoot();
        StackPane snackbarPane = new StackPane(messageLabel);
        snackbarPane.setAlignment(Pos.BOTTOM_CENTER);
        snackbarPane.setPadding(new Insets(0, 0, 20, 0));
        root.getChildren().add(snackbarPane);
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(event -> root.getChildren().remove(snackbarPane));
        delay.play();
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; " + "-fx-font-weight: bold; " + "-fx-text-fill: #333333;");
        return label;
    }

    private Button createStyledButton() {
        Button button = new Button("Save Availability");
        button.setStyle(getButtonStyle("#0b3d61"));
        button.setOnMouseEntered(e -> button.setStyle(getButtonStyle("#04253d")));
        button.setOnMouseExited(e -> button.setStyle(getButtonStyle("#0b3d61")));
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

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }
}
