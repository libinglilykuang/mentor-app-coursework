package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportSession;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * View to display the sessions assigned to a mentor.
 */
public class AssignedMenteeView extends VBox {
    private final Mentor mentor;
    private final ListView<String> sessionListView = new ListView<>();

    public AssignedMenteeView(Mentor mentor) {
        this.mentor = mentor;
        buildView();

        // Add CSS styling when the scene changes
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/assigned-mentee-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the view components.
     */
    private void buildView() {
        HBox topContainer = createTopContainer(); // Top navigation bar
        Label titleLabel = createTitleLabel(); // Title of the page
        VBox sessionBox = createSessionBox(); // Section to display sessions
        getStyleClass().add("assigned-mentee-view");
        getChildren().addAll(topContainer, titleLabel, sessionBox);
    }

    /**
     * Creates the top navigation bar.
     */
    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer(); // Logo or header image
        Label mentorHomeLabel = createNavigationLabel("Home", "navigation-label-inactive");
        Label settingLabel = createNavigationLabel("Set Available Time", "navigation-label-inactive");
        Label viewMentorLabel = createNavigationLabel("View Assigned Session", "navigation-label-active");

        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        settingLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorAvailabilityView(mentor)));

        HBox labelContainer = new HBox(20, mentorHomeLabel, settingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileButton = createUserProfileButton(); // Profile navigation button
        HBox.setHgrow(profileButton, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileButton);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        return topContainer;
    }

    /**
     * Creates a container with the logo or header image.
     */
    private VBox createImageContainer() {
        URL imageUrl = getClass().getResource("/icon/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/icon/pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));
        return imageContainer;
    }

    /**
     * Creates a styled navigation label.
     */
    private Label createNavigationLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Creates the title label for the page.
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label("Assigned Mentee Sessions");
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    /**
     * Creates the session list display box.
     */
    private VBox createSessionBox() {
        Label sessionListLabel = new Label("Your Assigned Sessions:");
        sessionListLabel.getStyleClass().add("session-list-label");

        sessionListView.setPrefHeight(300);
        sessionListView.getStyleClass().add("session-list");
        populateSessionList(); // Populate the session list with mentor's sessions

        VBox sessionBox = new VBox(10, sessionListLabel, sessionListView);
        sessionBox.setPadding(new Insets(10));
        return sessionBox;
    }

    /**
     * Creates the user profile button with a username label.
     */
    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon(); // User icon
        Label usernameLabel = createUsernameLabel(mentor.getUsername()); // Username label
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.getStyleClass().add("profile-button");
        configureProfileButton(profileButton);
        return profileButton;
    }

    /**
     * Creates the user icon for the profile button.
     */
    private ImageView createUserIcon() {
        URL iconUrl = getClass().getResource("/icon/user_icon.png");
        if (iconUrl == null) {
            throw new IllegalArgumentException("User icon resource not found: " + "/icon/user_icon.png");
        }
        ImageView userIcon = new ImageView(new Image(iconUrl.toExternalForm()));
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);
        return userIcon;
    }

    /**
     * Creates the username label for the profile button.
     */
    private Label createUsernameLabel(String username) {
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("username-label");
        return usernameLabel;
    }

    /**
     * Configures the profile button to navigate to the edit profile view.
     */
    private void configureProfileButton(HBox profileButton) {
        profileButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene();
            stage.setScene(new Scene(new EditProfileView(new MentorController(mentor), currentScene, true), 800, 600));
        });
    }

    /**
     * Populates the session list with assigned sessions for the mentor.
     */
    private void populateSessionList() {
        List<SupportSession> sessions = mentor.getAssignedSessions(); // Fetch sessions
        if (sessions == null || sessions.isEmpty()) {
            sessionListView.getItems().add("No sessions assigned yet.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        for (SupportSession session : sessions) {
            String menteeName = session.getMentee().getUsername();
            String startDate = session.getStartDate().format(formatter);
            String endDate = session.getEndDate().format(formatter);
            String status = session.isSessionActive() ? "Active" : "Completed";

            String sessionInfo = String.format("Mentee: %s\nStart: %s | End: %s\nStatus: %s",
                    menteeName, startDate, endDate, status);
            sessionListView.getItems().add(sessionInfo);
        }
    }
}
