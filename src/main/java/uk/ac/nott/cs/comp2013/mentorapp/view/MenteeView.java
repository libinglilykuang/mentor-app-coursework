package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import java.util.Objects;

/**
 * Represents the main view for a mentee.
 * Provides options for signing up for activities, viewing assigned mentors, and editing profiles.
 */
public class MenteeView extends VBox {
    private final MenteeController controller;

    public MenteeView(MenteeController controller) {
        this.controller = controller;
        buildView();

        // Add CSS when the scene changes
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/mentee-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the UI for the mentee view.
     */
    private void buildView() {
        VBox contentBox = createContentBox(); // Container for all components
        ImageView logoImageView = createLogoImageView(); // University logo
        Label welcomeLabel = createLabel(); // Welcome label
        Button btnViewActivities = createButton("Sign up for Activities", e -> switchToActivitySignUp());
        Button btnAssignedSessions = createButton("View Assigned Mentor", e -> switchToAssignedMentorView());
        Button btnEditProfile = createButton("Edit Profile", e -> switchToEditProfileView());
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnViewActivities, btnAssignedSessions, btnEditProfile);
        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    /**
     * Creates a container for the view's content.
     *
     * @return the styled VBox container
     */
    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("content-box");
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(400);
        return contentBox;
    }

    /**
     * Creates an image view for the university logo.
     *
     * @return the logo ImageView
     */
    private ImageView createLogoImageView() {
        ImageView logoImageView = new ImageView(new Image("/university-logo.png"));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }

    /**
     * Creates a welcome label.
     *
     * @return the welcome Label
     */
    private Label createLabel() {
        Label label = new Label("Welcome, Mentee!");
        label.getStyleClass().add("label");
        return label;
    }

    /**
     * Creates a styled button with the given text and action.
     *
     * @param text   the button text
     * @param action the event handler for button clicks
     * @return the styled Button
     */
    private Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        button.setPrefWidth(350);
        button.setOnAction(action);
        return button;
    }

    /**
     * Switches to the activity sign-up view.
     */
    private void switchToActivitySignUp() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new ActivitySignUpView(controller), 400, 300));
    }

    /**
     * Switches to the assigned mentor view.
     */
    private void switchToAssignedMentorView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AssignedMentorView(controller), 800, 600));
    }

    /**
     * Switches to the edit profile view.
     */
    private void switchToEditProfileView() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
    }
}
