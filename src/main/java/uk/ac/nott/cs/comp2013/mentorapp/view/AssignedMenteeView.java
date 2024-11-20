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

public class AssignedMenteeView extends VBox {
    private final Mentor mentor;
    private final ListView<String> sessionListView = new ListView<>();

    public AssignedMenteeView(Mentor mentor) {
        this.mentor = mentor;
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/assigned-mentee-view.css")).toExternalForm());
            }
        });
    }

    private void buildView() {
        HBox topContainer = createTopContainer();
        Label titleLabel = createTitleLabel();
        VBox sessionBox = createSessionBox();
        getStyleClass().add("assigned-mentee-view");
        getChildren().addAll(topContainer, titleLabel, sessionBox);
    }

    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        Label mentorHomeLabel = createNavigationLabel("Home", "navigation-label-inactive");
        Label settingLabel = createNavigationLabel("Set Available Time", "navigation-label-inactive");
        Label viewMentorLabel = createNavigationLabel("View Assigned Session", "navigation-label-active");

        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        settingLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorAvailabilityView(mentor)));
        viewMentorLabel.setOnMouseClicked(e -> getScene().setRoot(new AssignedMenteeView(mentor)));

        HBox labelContainer = new HBox(20, mentorHomeLabel, settingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        HBox profileBox = createUserProfileButton();
        HBox.setHgrow(profileBox, Priority.ALWAYS);
        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileBox);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        return topContainer;
    }

    private VBox createImageContainer() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/pairing_image.png");
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

    private Label createNavigationLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Assigned Mentee Sessions");
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    private VBox createSessionBox() {
        Label sessionListLabel = new Label("Your Assigned Sessions:");
        sessionListLabel.getStyleClass().add("session-list-label");

        sessionListView.setPrefHeight(300);
        sessionListView.getStyleClass().add("session-list");
        populateSessionList();

        VBox sessionBox = new VBox(10, sessionListLabel, sessionListView);
        sessionBox.setPadding(new Insets(10));
        return sessionBox;
    }

    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon();
        Label usernameLabel = createUsernameLabel(mentor.getUsername());
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.getStyleClass().add("profile-button");
        configureProfileButton(profileButton);
        return profileButton;
    }

    private ImageView createUserIcon() {
        URL iconUrl = getClass().getResource("/user_icon.png");
        if (iconUrl == null) {
            throw new IllegalArgumentException("User icon resource not found: " + "/user_icon.png");
        }
        ImageView userIcon = new ImageView(new Image(iconUrl.toExternalForm()));
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);
        return userIcon;
    }

    private Label createUsernameLabel(String username) {
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("username-label");
        return usernameLabel;
    }

    private void configureProfileButton(HBox profileButton) {
        profileButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene();
            stage.setScene(new Scene(new EditProfileView(new MentorController(mentor), currentScene, true), 800, 600));
        });
    }

    private void populateSessionList() {
        List<SupportSession> sessions = mentor.getAssignedSessions(); // Assume mentor has a method to fetch sessions
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
