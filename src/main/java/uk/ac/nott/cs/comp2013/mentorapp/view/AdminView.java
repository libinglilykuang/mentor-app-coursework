package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        VBox contentBox = createContentBox();
        ImageView logoImageView = createLogoImageView();
        Label welcomeLabel = createWelcomeLabel();
        Button btnPairMentorStudent = createMenuButton("Pair Mentor and Student", this::showPairingInterface);
        Button btnViewReports = createMenuButton("View Assigned Report", this::showReportInterface);
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnPairMentorStudent, btnViewReports);

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
        ImageView logoImageView = new ImageView(new Image("/university-logo.png"));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }

    private Label createWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome, Admin!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return welcomeLabel;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(getButtonStyle("#0b3d61"));
        button.setPrefWidth(350);
        button.setOnMouseEntered(e -> button.setStyle(getButtonStyle("#04253d")));
        button.setOnMouseExited(e -> button.setStyle(getButtonStyle("#0b3d61")));
        button.setOnAction(e -> action.run());
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
        Scene pairingScene = new Scene(new AdminPairingView(adminController));
        Stage stage = (Stage) getScene().getWindow();
        Platform.runLater(() -> stage.setScene(pairingScene));
        stage.setMaximized(true);
    }

    private void showReportInterface() {
        getScene().setRoot(new AssignedReportView(adminController));
    }
}