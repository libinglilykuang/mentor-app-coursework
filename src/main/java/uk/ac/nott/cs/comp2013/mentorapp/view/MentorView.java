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

public class MentorView extends VBox {
    private final MentorController controller;

    public MentorView(MentorController controller) {
        this.controller = controller;
        Locale.setDefault(Locale.ENGLISH);
        buildView();
        addCSSListener();
    }

    private void buildView() {
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView();
        Label welcomeLabel = createWelcomeLabel(controller.getMentor().getUsername());
        Button btnSetAvailability = createNavigationButton("Set Availability", e -> navigateTo(new MentorAvailabilityView(controller.getMentor()), 400, 300));
        Button btnAssignedSessions = createNavigationButton("View Assigned Sessions", e -> navigateTo(new AssignedMenteeView(controller.getMentor()), 800, 600));
        Button btnEditProfile = createNavigationButton("Edit Profile", e -> navigateToEditProfile());

        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnSetAvailability, btnAssignedSessions, btnEditProfile);
        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    private void addCSSListener() {
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource("/styles/mentor-view.css")).toExternalForm()
                );
            }
        });
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("content-box");
        return contentBox;
    }

    private ImageView createLogoImageView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/university-logo.png"))));
        imageView.getStyleClass().add("logo-image");
        return imageView;
    }

    private Label createWelcomeLabel(String username) {
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.getStyleClass().add("welcome-label");
        return welcomeLabel;
    }

    private Button createNavigationButton(String text, javafx.event.EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add("navigation-button");
        button.setOnAction(action);
        return button;
    }

    private void navigateTo(Parent view, double width, double height) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(view, width, height));
    }

    private void navigateToEditProfile() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
    }
}
