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
import java.net.URL;
import java.util.Objects;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;

/**
 * View for displaying a mentee's assigned mentors and their activities.
 */
public class AssignedMentorView extends VBox {
    private final MenteeController menteeController;

    /**
     * Constructor to initialize the Assigned Mentor View.
     *
     * @param menteeController Controller for managing mentee actions.
     */
    public AssignedMentorView(MenteeController menteeController) {
        this.menteeController = menteeController;
        buildView();

        // Add stylesheet dynamically when the scene is set
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/assigned-mentor-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the main layout of the view.
     */
    private void buildView() {
        HBox topContainer = createTopContainer(); // Top navigation bar
        Label titleLabel = createTitleLabel(); // Main title

        // Create sections for past and future activities
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

        // Add all components to the view
        getStyleClass().add("assigned-mentor-view");
        getChildren().addAll(topContainer, titleLabel, mainContainer);
    }

    /**
     * Creates a section for activities with a title and a list of activity cards.
     *
     * @param title Title of the section.
     * @param activities List of activity cards.
     * @return A VBox containing the section.
     */
    private VBox createActivitySection(String title, Node... activities) {
        VBox sectionBox = new VBox(20);
        sectionBox.getChildren().addAll(createTitleBox(title));
        sectionBox.getChildren().addAll(activities);
        sectionBox.setPadding(new Insets(20));
        sectionBox.setSpacing(10);
        return sectionBox;
    }

    /**
     * Creates the top navigation bar.
     */
    private HBox createTopContainer() {
        ImageView imageView = createImageView(); // Logo
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        // Navigation labels
        Label menteeHomeLabel = createNavigationLabel("Home", "navigation-label-inactive", e -> getScene().setRoot(new MenteeView(menteeController)));
        Label signingLabel = createNavigationLabel("Signing up for Activities", "navigation-label-inactive", e -> getScene().setRoot(new ActivitySignUpView(menteeController)));
        Label viewMentorLabel = createNavigationLabel("View Assigned Mentor", "navigation-label-active", null);

        HBox labelContainer = new HBox(20, menteeHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileButton = createUserProfileButton(); // Profile button
        HBox.setHgrow(profileButton, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileButton);
        topContainer.getStyleClass().add("top-container");

        return topContainer;
    }

    /**
     * Creates an ImageView for the logo.
     */
    private ImageView createImageView() {
        URL imageUrl = getClass().getResource("/icon/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/icon/pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates a navigation label with a click handler.
     */
    private Label createNavigationLabel(String text, String styleClass, EventHandler<MouseEvent> onClick) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        if (onClick != null) {
            label.setOnMouseClicked(onClick);
        }
        return label;
    }

    /**
     * Creates the title label for the view.
     */
    private Label createTitleLabel() {
        Label titleLabel = new Label("Assigned Mentors");
        titleLabel.getStyleClass().add("title-label");
        return titleLabel;
    }

    /**
     * Creates a user profile button with an icon and username.
     */
    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon("/icon/user_icon.png", 30);
        Label usernameLabel = createUsernameLabel(menteeController.getMentee().getUsername());
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.getStyleClass().add("profile-button");
        profileButton.setOnMouseClicked(event -> navigateToEditProfile(profileButton));
        return profileButton;
    }

    /**
     * Creates a user icon ImageView.
     */
    private ImageView createUserIcon(String iconPath, double size) {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        userIcon.setFitWidth(size);
        userIcon.setPreserveRatio(true);
        return userIcon;
    }

    /**
     * Creates a label for displaying the username.
     */
    private Label createUsernameLabel(String username) {
        Label usernameLabel = new Label(username);
        usernameLabel.getStyleClass().add("username-label");
        return usernameLabel;
    }

    /**
     * Navigates to the Edit Profile view when the profile button is clicked.
     */
    private void navigateToEditProfile(HBox profileButton) {
        Scene currentScene = profileButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(new EditProfileView(menteeController, currentScene, true), 800, 600));
    }

    /**
     * Creates a title box for a section.
     */
    private HBox createTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("section-title");

        HBox titleBox = new HBox(titleLabel);
        titleBox.getStyleClass().add("section-title");
        return titleBox;
    }

    /**
     * Creates an activity card with details about the activity.
     *
     * @param activityType The type of the activity.
     * @param mentorUsername The mentor assigned to the activity.
     * @param availability The time availability for the activity.
     * @return A VBox representing the activity card.
     */
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
