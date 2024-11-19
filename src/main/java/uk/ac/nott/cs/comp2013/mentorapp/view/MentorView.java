package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Objects;

public class MentorView extends VBox {
    private final MentorController controller;

    public MentorView(MentorController controller) {
        this.controller = controller;
        buildView();
    }

    private void buildView() {
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView();
        Label welcomeLabel = createWelcomeLabel(controller.getMentor().getUsername());
        Button btnSetAvailability = createNavigationButton(
                "Set Availability",
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        navigateTo(new MentorAvailabilityView(controller.getMentor()), 400, 300);
                    }
                }
        );
        Button btnAssignedSessions = createNavigationButton(
                "View Assigned Sessions",
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        navigateTo(new AssignedMenteeView(controller.getMentor()), 800, 600);
                    }
                }
        );
        Button btnEditProfile = createNavigationButton(
                "Edit Profile",
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Stage stage = (Stage) getScene().getWindow();
                        Scene currentScene = stage.getScene();
                        stage.setScene(new Scene(new EditProfileView(controller, currentScene, false), 800, 600));
                    }
                }
        );

        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnSetAvailability, btnAssignedSessions, btnEditProfile);
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

    private ImageView createLogoImageView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/university-logo.png"))));
        imageView.setFitWidth(350);
        imageView.setFitHeight(350);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private Label createWelcomeLabel(String username) {
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return welcomeLabel;
    }

    private Button createNavigationButton(String text, EventHandler<ActionEvent> action) {
        Button button = createStyledButton(text);
        button.setPrefWidth(350);
        button.setOnAction(action);
        return button;
    }

    private void navigateTo(Parent view, double width, double height) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(view, width, height));
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(getButtonStyle("#0b3d61"));

        button.setOnMouseEntered(e -> button.setStyle(getButtonStyle("#04253d")));
        button.setOnMouseExited(e -> button.setStyle(getButtonStyle("#0b3d61")));

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
