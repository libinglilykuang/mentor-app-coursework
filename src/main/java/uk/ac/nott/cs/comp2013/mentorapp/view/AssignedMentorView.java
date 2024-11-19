package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
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

        Label menteeHomeLabel = createNavigationLabel("Home", getInactiveStyle(), e -> getScene().setRoot(new MenteeView(menteeController)));
        Label signingLabel = createNavigationLabel("Signing up for Activities", getInactiveStyle(), e -> getScene().setRoot(new ActivitySignUpView(menteeController)));
        Label viewMentorLabel = createNavigationLabel("View Assigned Mentor", getActiveStyle(), null);

        HBox labelContainer = new HBox(20, menteeHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileButton = createUserProfileButton();
        HBox.setHgrow(profileButton, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileButton);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        setStyle("-fx-background-color: white;");

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

    private Label createNavigationLabel(String text, String style, EventHandler<MouseEvent> onClick) {
        Label label = new Label(text);
        label.setStyle(style);
        if (onClick != null) {
            label.setOnMouseClicked(onClick);
        }
        return label;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Assigned Mentors");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        return titleLabel;
    }

    private HBox createUserProfileButton() {
        ImageView userIcon = createUserIcon("/user_icon.png", 30);
        Label usernameLabel = createUsernameLabel(menteeController.getMentee().getUsername());
        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        styleProfileButton(profileButton);
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
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold;");
        return usernameLabel;
    }

    private void styleProfileButton(HBox profileButton) {
        profileButton.setAlignment(Pos.CENTER_RIGHT);
        profileButton.setPadding(new Insets(5));
        profileButton.setStyle("-fx-cursor: hand;");
    }

    private void navigateToEditProfile(HBox profileButton) {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(menteeController, currentScene, true), 800, 600));
    }

    private HBox createTitleBox(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setStyle("-fx-background-color: #0056a1; -fx-padding: 10;");
        titleBox.setAlignment(Pos.CENTER_LEFT);
        return titleBox;
    }

    private VBox createActivityCard(String activityType, String mentorUsername, String availability) {
        Label activityTypeLabel = new Label(activityType);
        activityTypeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        Label mentorLabel = new Label("Mentor: " + mentorUsername);
        mentorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        Label availabilityLabel = new Label("Available Time: " + availability);
        availabilityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

        VBox cardContent = new VBox(10, activityTypeLabel, mentorLabel, availabilityLabel);
        cardContent.setPadding(new Insets(10));

        VBox activityCard = new VBox(cardContent);
        activityCard.setStyle("-fx-background-color: white; -fx-border-color: #dcdcdc; -fx-border-width: 1px; -fx-border-radius: 5px;");
        return activityCard;
    }

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }

}
