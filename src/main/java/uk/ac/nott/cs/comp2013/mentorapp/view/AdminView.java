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
import java.util.Objects;

public class AdminView extends VBox {
    private final AdminController adminController;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        buildMainMenu();
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/admin-view.css")).toExternalForm());
            }
        });
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
        contentBox.getStyleClass().add("content-box");
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
        welcomeLabel.getStyleClass().add("welcome-label");
        return welcomeLabel;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(350);
        button.setOnAction(e -> action.run());
        return button;
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