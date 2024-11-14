package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import java.net.URL;

public class AssignedMentorView extends VBox {
    private final MenteeController menteeController;

    public AssignedMentorView(MenteeController menteeController) {
        this.menteeController = menteeController;
        buildView();
    }

    private void buildView() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: /pairing_image.png");
        }
        Image image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label signingLabel = new Label("Signing up for Activities");
        Label viewMentorLabel = new Label("View Assigned Mentor");
        Label menteeHomeLabel = new Label("Home");
        menteeHomeLabel.setStyle(getInactiveStyle());
        signingLabel.setStyle(getInactiveStyle());
        viewMentorLabel.setStyle(getActiveStyle());
        menteeHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MenteeView(menteeController)));
        signingLabel.setOnMouseClicked(e -> getScene().setRoot(new ActivitySignUpView(menteeController)));

        // Label container
        HBox labelContainer = new HBox(20,  menteeHomeLabel,signingLabel,viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        // Add profile button
        HBox profileButton = createUserProfileButton();

        HBox topContainer = new HBox(20, imageContainer, labelContainer,profileButton);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(profileButton, Priority.ALWAYS);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Assigned Mentors");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox pastActivitiesBox = new VBox(20,
                createTitleBox("Past Activities"),
                createActivityCard("CV Review", "Mentor1", "Mon - Fri, 10:00 AM - 4:00 PM")
        );
        pastActivitiesBox.setPadding(new Insets(20));
        pastActivitiesBox.setSpacing(10);

        VBox futureActivitiesBox = new VBox(20,
                createTitleBox("Future Activities"),
                createActivityCard("Career Choice: How to enter a specific career field", "Mentor2", "Mon - Wed, 2:00 PM - 5:00 PM")
        );
        futureActivitiesBox.setPadding(new Insets(20));
        futureActivitiesBox.setSpacing(10);

        VBox mainContainer = new VBox(30, pastActivitiesBox, futureActivitiesBox);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(topContainer,titleLabel, mainContainer);
    }

    private HBox createUserProfileButton() {
        // User icon
        ImageView userIcon = new ImageView(new Image(getClass().getResourceAsStream("/user_icon.png"))); // Replace with your icon path
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);

        // Username label
        Label usernameLabel = new Label(menteeController.getMentee().getUsername());
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
            stage.setScene(new Scene(new EditProfileView(menteeController, currentScene), 800, 600));
        });

        return profileButton;
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
