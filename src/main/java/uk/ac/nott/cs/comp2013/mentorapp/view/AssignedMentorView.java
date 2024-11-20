package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import java.net.URL;
import java.util.Objects;

public class AssignedMentorView extends VBox {
    private final MenteeController menteeController;

    public AssignedMentorView(MenteeController menteeController) {
        this.menteeController = menteeController;
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/assigned-mentor-view.css")).toExternalForm());
            }
        });
    }

    private void buildView() {
        HBox topContainer = createTopContainer();
        Label titleLabel = createTitleLabel();

        VBox pastActivitiesBox = createActivitySection(
                "Past Activities",
                createActivityCard("CV Review", "Mentor1", "Mon - Fri, 10:00 AM - 4:00 PM")
        );

        VBox futureActivitiesBox = createActivitySection(
                "Future Activities",
                createActivityCard("Career Choice: How to enter a specific career field", "Mentor2", "Mon - Wed, 2:00 PM - 5:00 PM")
        );

        VBox mainContainer = new VBox(30, pastActivitiesBox, futureActivitiesBox);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        getStyleClass().add("assigned-mentor-view");
        getChildren().addAll(topContainer, titleLabel, mainContainer);
    }

    private VBox createActivitySection(String title, Node... activities) {
        VBox sectionBox = new VBox(20);
        sectionBox.getChildren().addAll(createTitleBox(title));
        sectionBox.getChildren().addAll(activities);
        sectionBox.setPadding(new Insets(20));
        sectionBox.setSpacing(10);
        return sectionBox;
    }

    private HBox createTopContainer() {
        ImageView imageView = createImageView();
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label menteeHomeLabel = createNavigationLabel("Home", "navigation-label-inactive", e -> getScene().setRoot(new MenteeView(menteeController)));
        Label signingLabel = createNavigationLabel("Signing up for Activities", "navigation-label-inactive", e -> getScene().setRoot(new ActivitySignUpView(menteeController)));
        Label viewMentorLabel = createNavigationLabel("View Assigned Mentor", "navigation-label-active", null);

        HBox labelContainer = new HBox(20, menteeHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileButton = createUserProfileButton();
        HBox.setHgrow(profileButton, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileButton);
        topContainer.getStyleClass().add("top-container");

        return topContainer;
    }

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

    private Label createNavigationLabel(String text, String styleClass, EventHandler<MouseEvent> onClick) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        if (onClick != null) {
            label.setOnMouseClicked(onClick);
        }
        return label;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Assigned Mentors");
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon("/user_icon.png", 30);
        Label usernameLabel = createUsernameLabel(menteeController.getMentee().getUsername());
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.getStyleClass().add("profile-button");
        profileButton.setOnMouseClicked(event -> navigateToEditProfile(profileButton));
        return profileButton;
    }

    private ImageView createUserIcon(String iconPath, double size) {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        userIcon.setFitWidth(size);
        userIcon.setPreserveRatio(true);
        return userIcon;
    }

    private Label createUsernameLabel(String username) {
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("username-label");
        return usernameLabel;
    }

    private void navigateToEditProfile(HBox profileButton) {
        Scene currentScene = profileButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(new EditProfileView(menteeController, currentScene, true), 800, 600));
    }

    private HBox createTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        HBox titleBox = new HBox(titleLabel);
        titleBox.getStyleClass().add("section-title");
        return titleBox;
    }

    private VBox createActivityCard(String activityType, String mentorUsername, String availability) {
        Label activityTypeLabel = new Label(activityType);
        activityTypeLabel.getStyleClass().add("activity-type");

        Label mentorLabel = new Label("Mentor: " + mentorUsername);
        mentorLabel.getStyleClass().add("mentor-label");

        Label availabilityLabel = new Label("Available Time: " + availability);
        availabilityLabel.getStyleClass().add("availability-label");

        VBox cardContent = new VBox(10, activityTypeLabel, mentorLabel, availabilityLabel);
        VBox activityCard = new VBox(cardContent);
        activityCard.getStyleClass().add("activity-card");

        return activityCard;
    }
}