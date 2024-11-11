package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

import java.net.URL;
import java.util.List;

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

        updateStyles(viewMentorLabel, menteeHomeLabel, signingLabel);

        // Set styles for labels
        menteeHomeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        menteeHomeLabel.setOnMouseClicked(e -> switchToMenteeHome(menteeHomeLabel,signingLabel,viewMentorLabel));

        // Set styles and actions for labels
        signingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");
        signingLabel.setOnMouseClicked(e -> switchToSigningView(menteeHomeLabel,signingLabel,viewMentorLabel));

        viewMentorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        viewMentorLabel.setOnMouseClicked(e -> openReportWindow(menteeHomeLabel,signingLabel,viewMentorLabel));

        // Label container
        HBox labelContainer = new HBox(20,  menteeHomeLabel,signingLabel,viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10, 10, 5, 10));
        setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Assigned Mentors");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox titleBox = createTitleBox("Past Activities");

        VBox activityCard = createActivityCard("CV Review", "Mentor123", "Mon - Fri, 10:00 AM - 4:00 PM");

        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);
        getChildren().addAll(topContainer,titleLabel, titleBox,activityCard);
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

    private void switchToMenteeHome(Label menteeHomeLabel, Label signingLabel, Label viewMentorLabel) {
        updateStyles(menteeHomeLabel, signingLabel, viewMentorLabel);
        Scene scene = getScene();
        if (scene != null) {
            scene.setRoot(new MenteeView(menteeController));
        }
    }

    private void switchToSigningView(Label menteeHomeLabel, Label signingLabel, Label viewMentorLabel) {
        updateStyles(signingLabel, menteeHomeLabel, viewMentorLabel);
        Scene scene = getScene();
        if (scene != null) {
            scene.setRoot(new ActivitySignUpView(menteeController));
        }
    }

    private void openReportWindow(Label menteeHomeLabel, Label signingLabel, Label viewMentorLabel) {
        updateStyles(viewMentorLabel, menteeHomeLabel, signingLabel);
        Scene scene = getScene();
        if (scene != null) {
            scene.setRoot(new AssignedMentorView(menteeController));
        }
    }

    private void updateStyles(Label activeLabel, Label... otherLabels) {
        activeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");
        for (Label label : otherLabels) {
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        }
    }


}
