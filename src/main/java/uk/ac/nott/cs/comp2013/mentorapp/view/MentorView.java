package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

public class MentorView extends VBox {
    private final MentorController controller;

    public MentorView(MentorController controller) {
        this.controller = controller;
        buildView();
    }

    private void buildView() {
        VBox contentBox = new VBox();
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);

        // Add a logo image at the top
        ImageView logoImageView = new ImageView(new Image("/university-logo.png"));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);

        Label welcomeLabel = new Label("Welcome, Mentor!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        getChildren().add(welcomeLabel);

        Mentor mentor = new Mentor("mentor123", "password123");
        // Create buttons for Mentor actions
        Button btnSetAvailability = createStyledButton("Set Availability", "#0b3d61", "#04253d");
        btnSetAvailability.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(new Scene(new MentorAvailabilityView(mentor), 400, 300));
        });
        btnSetAvailability.setPrefWidth(350);

        Button btnAssignedSessions = createStyledButton("View Assigned Sessions", "#0b3d61", "#04253d");
        btnAssignedSessions.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(new Scene(new AssignedMenteeView(mentor), 800, 600));
        });
        btnAssignedSessions.setPrefWidth(350);

        // Add all components to the content box
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnSetAvailability, btnAssignedSessions);

        // Style the content box
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

        // Clear existing children and add the new content box
        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    private Button createStyledButton(String text, String defaultColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle(getButtonStyle(defaultColor));

        button.setOnMouseEntered(e -> button.setStyle(getButtonStyle(hoverColor)));
        button.setOnMouseExited(e -> button.setStyle(getButtonStyle(defaultColor)));

        button.setPrefWidth(200);
        return button;
    }

    private String getButtonStyle(String backgroundColor) {
        return String.format(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-background-color: %s; " +
                        "-fx-cursor: hand;",
                backgroundColor
        );
    }
}
