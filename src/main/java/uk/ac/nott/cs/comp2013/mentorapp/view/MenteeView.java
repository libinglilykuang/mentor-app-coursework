/*package uk.ac.nott.cs.comp2013.mentorapp.view;

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

public class MenteeView extends VBox {
    private final MenteeController controller;

    public MenteeView(MenteeController controller) {
        this.controller = controller;
        buildView();
    }

    private void buildView() {
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView("/university-logo.png");
        Label welcomeLabel = createLabel("Welcome, Mentee!", "-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnViewActivities = createButton("Sign up for Activities", "#0b3d61", "#04253d", e -> switchToActivitySignUp());
        Button btnAssignedSessions = createButton("View Assigned Mentor", "#0b3d61", "#04253d", e -> switchToAssignedMentorView());
        Button btnEditProfile = createButton("Edit Profile", "#0b3d61", "#04253d", e -> switchToEditProfileView());
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnViewActivities, btnAssignedSessions, btnEditProfile);
        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(400);
        contentBox.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-border-color: #DDDDDD; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 15px; " +
                        "-fx-background-radius: 15px; " +
                        "-fx-padding: 40px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.5, 0, 4);"
        );
        return contentBox;
    }

    private ImageView createLogoImageView(String logoPath) {
        ImageView logoImageView = new ImageView(new Image(logoPath));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }

    private Label createLabel(String text, String style) {
        Label label = new Label(text);
        label.setStyle(style);
        return label;
    }

    private Button createButton(String text, String backgroundColor, String hoverColor, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"
        ));
        button.setPrefWidth(350);
        button.setOnAction(action);
        return button;
    }

    private void switchToActivitySignUp() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new ActivitySignUpView(controller), 400, 300));
    }

    private void switchToAssignedMentorView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AssignedMentorView(controller), 800, 600));
    }

    private void switchToEditProfileView() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
    }
}*/

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

public class MenteeView extends VBox {
    private final MenteeController controller;

    public MenteeView(MenteeController controller) {
        this.controller = controller;
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/mentee-view.css")).toExternalForm());
            }
        });
    }

    private void buildView() {
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView("/university-logo.png");
        Label welcomeLabel = createLabel("Welcome, Mentee!", "label");
        Button btnViewActivities = createButton("Sign up for Activities", "styled-button", e -> switchToActivitySignUp());
        Button btnAssignedSessions = createButton("View Assigned Mentor", "styled-button", e -> switchToAssignedMentorView());
        Button btnEditProfile = createButton("Edit Profile", "styled-button", e -> switchToEditProfileView());

        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnViewActivities, btnAssignedSessions, btnEditProfile);

        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.getStyleClass().add("content-box");
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(400);
        return contentBox;
    }

    private ImageView createLogoImageView(String logoPath) {
        ImageView logoImageView = new ImageView(new Image(logoPath));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private Button createButton(String text, String styleClass, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setPrefWidth(350);
        button.setOnAction(action);
        return button;
    }

    private void switchToActivitySignUp() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new ActivitySignUpView(controller), 400, 300));
    }

    private void switchToAssignedMentorView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AssignedMentorView(controller), 800, 600));
    }

    private void switchToEditProfileView() {
        Stage stage = (Stage) getScene().getWindow();
        Scene currentScene = stage.getScene();
        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
    }
}

