package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;

import java.util.Locale;
import java.util.Objects;

/**
 * MentorView represents the main interface for a mentor, providing navigation options to set availability,
 * view assigned sessions, and edit the profile.
 */
public class MentorView extends VBox {
    private final MentorController controller;

    /**
     * Constructor for MentorView.
     *
     * @param controller The MentorController handling mentor-specific actions.
     */
    public MentorView(MentorController controller) {
        this.controller = controller;
        Locale.setDefault(Locale.ENGLISH); // Ensure locale is consistent across views
        buildView(); // Build the UI
        addCSSListener(); // Add CSS listener for dynamic styling
    }

    /**
     * Builds the UI components for the mentor view.
     */
    private void buildView() {
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView();
        Label welcomeLabel = createWelcomeLabel(controller.getMentor().getUsername());
        Button btnSetAvailability = createNavigationButton("Set Availability", e -> navigateTo(new MentorAvailabilityView(controller.getMentor()), 400, 300));
        Button btnAssignedSessions = createNavigationButton("View Assigned Sessions", e -> navigateTo(new AssignedMenteeView(controller.getMentor()), 800, 600));
        Button btnEditProfile = createNavigationButton("Edit Profile", e -> navigateToEditProfile());

        // Add components to content box
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnSetAvailability, btnAssignedSessions, btnEditProfile);
        getChildren().clear();
        setAlignment(Pos.CENTER); // Center-align the view
        getChildren().add(contentBox);
    }

    /**
     * Adds a listener to dynamically load CSS styling when the scene changes.
     */
    private void addCSSListener() {
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/mentor-view.css")).toExternalForm()
                );
            }
        });
    }

    /**
     * Creates a container for all content.
     */
    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("content-box"); // Apply CSS styling
        return contentBox;
    }

    /**
     * Creates the university logo image view.
     */
    private ImageView createLogoImageView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/university-logo.png"))));
        imageView.getStyleClass().add("logo-image");
        return imageView;
    }

    /**
     * Creates a welcome label for the mentor.
     *
     * @param username The username of the mentor.
     */
    private Label createWelcomeLabel(String username) {
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.getStyleClass().add("welcome-label"); // Apply CSS styling
        return welcomeLabel;
    }

    /**
     * Creates a navigation button.
     *
     * @param text   The button text.
     * @param action The action to perform when the button is clicked.
     */
    private Button createNavigationButton(String text, javafx.event.EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("navigation-button"); // Apply CSS styling
        button.setOnAction(action);
        return button;
    }

    /**
     * Navigates to a new view.
     *
     * @param view   The target view to navigate to.
     * @param width  The width of the new scene.
     * @param height The height of the new scene.
     */
    private void navigateTo(Parent view, double width, double height) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(view, width, height));
    }

    /**
     * Navigates to the Edit Profile view.
     */
    private void navigateToEditProfile() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
    }
}
