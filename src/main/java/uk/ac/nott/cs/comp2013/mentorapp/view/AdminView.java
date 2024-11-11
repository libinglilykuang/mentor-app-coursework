package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;

public class AdminView extends VBox {
    private final AdminController adminController;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        buildMainMenu();
    }

    private void buildMainMenu() {
        VBox contentBox = new VBox();
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);

        ImageView logoImageView = new ImageView(new Image("/university-logo.png"));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);

        Label welcomeLabel = new Label("Welcome, Admin!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnPairMentorStudent = createStyledButton("Pair Mentor and Student", "#0b3d61", "#04253d");
        btnPairMentorStudent.setOnAction(e -> showPairingInterface());
        btnPairMentorStudent.setPrefWidth(350);

        Button btnViewReports = createStyledButton("View Assigned Report", "#0b3d61", "#04253d");
        btnViewReports.setOnAction(e -> showReportInterface());
        btnViewReports.setPrefWidth(350);

        btnPairMentorStudent.setOnAction(e -> showPairingInterface());
        btnViewReports.setOnAction(e -> showReportInterface());

        setSpacing(20);
        setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(logoImageView,welcomeLabel, btnPairMentorStudent, btnViewReports);

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

    private void showPairingInterface() {
        getChildren().clear();
        // Create the pairing view
        AdminPairingView pairingView = new AdminPairingView(adminController);

        // Set desired size for pairing window
        Platform.runLater(() -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.setWidth(900); // Set pairing window width
            stage.setHeight(700); // Set pairing window height
            stage.setMinWidth(800); // Set minimum width
            stage.setMinHeight(600); // Set minimum height
        });
        getChildren().add(pairingView);
    }


    private void showReportInterface() {
        getScene().setRoot(new AssignedReportView(adminController));
    }
}