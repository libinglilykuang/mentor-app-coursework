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
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

/**
 * The MentorAvailabilityView class provides a user interface for mentors
 * to set their available time slots for mentoring sessions.
 */
public class MentorAvailabilityView extends VBox {
    private final Mentor mentor;
    private final DatePicker startDatePicker = new DatePicker();
    private final ComboBox<String> endTimePicker = new ComboBox<>();
    private final ComboBox<String> startTimePicker = new ComboBox<>();
    private final DatePicker endDatePicker = new DatePicker();

    private final Label availabilityLabel = new Label("You have not set any available time yet.");

    /**
     * Constructs the MentorAvailabilityView for the given mentor.
     *
     * @param mentor The mentor whose availability is being managed.
     */
    public MentorAvailabilityView(Mentor mentor) {
        this.mentor = mentor;
        buildView();
        addCSSListener();
    }

    /**
     * Builds the layout of the view.
     */
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
    }

    /**
     * Adds a listener to dynamically load CSS when the scene is set.
     */
    private void addCSSListener() {
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(
                        Objects.requireNonNull(
                                getClass().getResource("/styles/mentor-availability-view.css")
                        ).toExternalForm()
                );
            }
        });
    }

    /**
     * Creates the top navigation bar.
     *
     * @return HBox with navigation elements.
     */
    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        HBox labelContainer = createLabelContainer();
        HBox profileBox = createUserProfileButton();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topContainer = new HBox(20, imageContainer, labelContainer, spacer, profileBox);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        topContainer.setStyle("-fx-background-color: white;");
        return topContainer;
    }

    /**
     * Creates a container for the mentor's profile button.
     *
     * @return HBox with the profile button.
     */
    private HBox createUserProfileButton() {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/user_icon.png"))));
        userIcon.getStyleClass().add("profile-icon");
        Label usernameLabel = new Label(mentor.getUsername());
        usernameLabel.getStyleClass().add("profile-label");
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.getStyleClass().add("profile-button");
        profileButton.setAlignment(Pos.CENTER);
        profileButton.setOnMouseClicked(e -> navigateToEditProfile());
        return profileButton;
    }

    /**
     * Navigates to the edit profile view.
     */
    private void navigateToEditProfile() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(new MentorController(mentor), currentScene, true), 800, 600));
    }

    /**
     * Creates the mentor logo container.
     */
    private VBox createImageContainer() {
        ImageView imageView = createImageView();
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));
        return imageContainer;
    }

    /**
     * Creates the mentor logo.
     */
    private ImageView createImageView() {
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

    /**
     * Creates the navigation labels.
     */
    private HBox createLabelContainer() {
        Label mentorHomeLabel = createStyledLabel("Home", "inactive-label");
        Label signingLabel = createStyledLabel("Set Available Time", "active-label");
        Label viewMentorLabel = createStyledLabel("View Assigned Session", "inactive-label");

        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        viewMentorLabel.setOnMouseClicked(e -> getScene().setRoot(new AssignedMenteeView(mentor)));

        HBox labelContainer = new HBox(20, mentorHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        return labelContainer;
    }

    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Creates the header section with instructions.
     */
    private VBox createHeaderBox() {
        Label titleLabel = new Label("Set Your Availability");
        titleLabel.getStyleClass().add("header-title");
        Label descriptionLabel = new Label("Choose dates and times when you will be available.");
        descriptionLabel.getStyleClass().add("header-description");

        VBox headerBox = new VBox(5, titleLabel, descriptionLabel);
        headerBox.setAlignment(Pos.TOP_LEFT);
        headerBox.getStyleClass().add("header-box");
        return headerBox;
    }

    /**
     * Creates the form for selecting availability.
     */
    private GridPane createAvailabilityGrid() {
        initializeTimePickers();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.getStyleClass().add("availability-grid");

        gridPane.add(new Label("Start Date:"), 0, 0);
        gridPane.add(startDatePicker, 1, 0);
        gridPane.add(new Label("Start Time:"), 2, 0);
        gridPane.add(startTimePicker, 3, 0);
        gridPane.add(new Label("End Date:"), 0, 1);
        gridPane.add(endDatePicker, 1, 1);
        gridPane.add(new Label("End Time:"), 2, 1);
        gridPane.add(endTimePicker, 3, 1);

        return gridPane;
    }

    /**
     * Creates the button container for saving availability.
     */
    private HBox createButtonBox() {
        Button saveButton = new Button("Save Availability");
        saveButton.getStyleClass().add("save-button");
        saveButton.setOnAction(e -> saveAvailability());
        HBox buttonBox = new HBox(saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private VBox createBelowBox() {
        Label availabilityTitle = new Label("Your Selected Time Slots");
        availabilityTitle.getStyleClass().add("below-box-title");
        VBox belowBox = new VBox(5, availabilityTitle);
        belowBox.setAlignment(Pos.TOP_LEFT);
        belowBox.getStyleClass().add("below-box");
        return belowBox;
    }

    private VBox createAvailabilityBox() {
        availabilityLabel.getStyleClass().add("availability-label");
        VBox availabilityBox = new VBox(availabilityLabel);
        availabilityBox.getStyleClass().add("availability-box");
        return availabilityBox;
    }

    /**
     * Handles saving the availability settings.
     * Validates the inputs and displays feedback to the user.
     */
    private void saveAvailability() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String startTime = startTimePicker.getValue();
        String endTime = endTimePicker.getValue();
        if (startDate == null || endDate == null || startTime == null || endTime == null) {
            showSnackbar("Please fill in all fields.", false);
            return;
        }
        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTime));
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTime));
        if (endDateTime.isBefore(startDateTime)) {
            showSnackbar("End time cannot be earlier than start time.", false);
            return;
        }
        String formattedStart = startDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        String formattedEnd = endDateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        availabilityLabel.setText("From: " + formattedStart + "\n" + "To: " + formattedEnd);
        showSnackbar("Availability set successfully.", true);
    }

    /**
     * Displays a snackbar notification with feedback.
     *
     * @param message The message to display.
     * @param success Whether the operation was successful.
     */
    private void showSnackbar(String message, boolean success) {
        Label snackbar = new Label(message);
        snackbar.getStyleClass().add(success ? "snackbar-success" : "snackbar-error");
        Scene scene = getScene();
        if (scene == null) return;
        StackPane snackbarPane = new StackPane(snackbar);
        snackbarPane.setAlignment(Pos.BOTTOM_CENTER);
        Pane root = (Pane) scene.getRoot();
        root.getChildren().add(snackbarPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> root.getChildren().remove(snackbarPane));
        delay.play();
    }

    /**
     * Initializes time pickers with hourly intervals.
     */
    private void initializeTimePickers() {
        for (int i = 0; i < 24; i++) {
            String time = String.format("%02d:00", i);
            startTimePicker.getItems().add(time);
            endTimePicker.getItems().add(time);
        }
        startTimePicker.setValue("09:00");
        endTimePicker.setValue("17:00");
    }
}
