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

public class AssignedMenteeView extends VBox {
    private final Mentor mentor;
    private final ListView<String> sessionListView = new ListView<>();

    public AssignedMenteeView(Mentor mentor) {
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

        Label settingLabel = new Label("Set Available Time");
        Label viewMentorLabel = new Label("View Assigned Session");
        Label mentorHomeLabel = new Label("Home");

        mentorHomeLabel.setStyle(getInactiveStyle());
        settingLabel.setStyle(getInactiveStyle());
        viewMentorLabel.setStyle(getActiveStyle());

        MentorController mentorController = new MentorController(mentor);
        mentorHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MentorView(mentorController)));
        settingLabel.setOnMouseClicked(e->getScene().setRoot(new MentorAvailabilityView(mentor)));
        viewMentorLabel.setOnMouseClicked(e->getScene().setRoot(new AssignedMenteeView(mentor)));

        // Navigation bar
        HBox labelContainer = new HBox(20, mentorHomeLabel, settingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = createUserProfileButton();
        HBox.setHgrow(profileBox, Priority.ALWAYS);
        HBox topContainer = new HBox(20, imageContainer, labelContainer,profileBox );
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        setStyle("-fx-background-color: white;");

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        setStyle("-fx-background-color: #f9f9f9;");

        // Page Title
        Label titleLabel = new Label("Assigned Mentee Sessions");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        // Mentor Info
        Label mentorInfoLabel = new Label("Mentor: " + mentor.getUsername());
        mentorInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

        // Session List
        Label sessionListLabel = new Label("Your Assigned Sessions:");
        sessionListLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333; -fx-font-weight: bold;");

        sessionListView.setPrefHeight(300);
        sessionListView.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 5px; -fx-padding: 10px;");
        populateSessionList(); // Populate the session list with mentor's sessions

        // Layout
        VBox mentorInfoBox = new VBox(10, mentorInfoLabel);
        VBox sessionBox = new VBox(10, sessionListLabel, sessionListView);

        mentorInfoBox.setPadding(new Insets(10));
        sessionBox.setPadding(new Insets(10));

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(topContainer,titleLabel, mentorInfoBox, sessionBox);
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

    private Label createStyledButton(String text, Runnable onClickAction) {
        Label button = new Label(text);
        button.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-underline: true; -fx-cursor: hand;");
        button.setOnMouseClicked(e -> onClickAction.run());
        return button;
    }

    private void switchToHomePage() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new MentorView(new MentorController(mentor)), 800, 600));
    }

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }
}
