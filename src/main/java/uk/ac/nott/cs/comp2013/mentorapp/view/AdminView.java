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

/**
 * The AdminView class represents the main interface for administrators.
 * Provides options for mentor-student pairing and viewing reports.
 */
public class AdminView extends VBox {
    private final AdminController adminController; // Controller for admin-related actions

    /**
     * Constructs the AdminView with a given AdminController.
     *
     * @param adminController the controller managing admin actions.
     */
    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        buildMainMenu();

        // Add stylesheet when the scene is set
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/admin-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the main menu layout for the admin view.
     */
    private void buildMainMenu() {
        VBox contentBox = createContentBox(); // Main content container
        ImageView logoImageView = createLogoImageView(); // Logo at the top
        Label welcomeLabel = createWelcomeLabel(); // Welcome message
        Button btnPairMentorStudent = createMenuButton("Pair Mentor and Student", this::showPairingInterface); // Pairing button
        Button btnViewReports = createMenuButton("View Assigned Report", this::showReportInterface); // Reports button

        // Add all components to the content box
        contentBox.getChildren().addAll(logoImageView, welcomeLabel, btnPairMentorStudent, btnViewReports);

        // Set layout properties
        getChildren().clear();
        setAlignment(Pos.CENTER);
        getChildren().add(contentBox);
    }

    /**
     * Creates the main content container.
     *
     * @return A VBox configured as the main content container.
     */
    private VBox createContentBox() {
        VBox contentBox = new VBox();
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setMaxWidth(400);
        contentBox.getStyleClass().add("content-box");
        return contentBox;
    }

    /**
     * Creates an ImageView for the logo.
     *
     * @return An ImageView displaying the university logo.
     */
    private ImageView createLogoImageView() {
        ImageView logoImageView = new ImageView(new Image("/university-logo.png"));
        logoImageView.setFitWidth(350);
        logoImageView.setFitHeight(350);
        logoImageView.setPreserveRatio(true);
        return logoImageView;
    }

    /**
     * Creates the welcome label for the admin interface.
     *
     * @return A styled Label with the welcome message.
     */
    private Label createWelcomeLabel() {
        Label welcomeLabel = new Label("Welcome, Admin!");
        welcomeLabel.getStyleClass().add("welcome-label");
        return welcomeLabel;
    }

    /**
     * Creates a styled button for the menu.
     *
     * @param text   The button text.
     * @param action The action to perform when the button is clicked.
     * @return A configured Button instance.
     */
    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        button.setPrefWidth(350);
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Navigates to the pairing interface.
     */
    private void showPairingInterface() {
        Scene pairingScene = new Scene(new AdminPairingView(adminController));
        Stage stage = (Stage) getScene().getWindow();
        Platform.runLater(() -> stage.setScene(pairingScene));
        stage.setMaximized(true);
    }

    /**
     * Navigates to the report interface.
     */
    private void showReportInterface() {
        getScene().setRoot(new AssignedReportView(adminController));
    }
}
