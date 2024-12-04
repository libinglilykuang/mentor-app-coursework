package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import java.util.Objects;

/**
 * Represents the main view for administrators.
 * Displays two interactive images that navigate to different views when clicked.
 */
public class AdminView extends VBox {
    private final AdminController adminController;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        buildView();

        // Add CSS dynamically when the scene changes
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/admin-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the UI for the admin view.
     */
    private void buildView() {
        VBox layout = new VBox();

        // Create the top navigation bar
        HBox navBar = createNavBar();

        // Add main content (example with images)
        HBox contentBox = new HBox();
        contentBox.setSpacing(100);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(100));

        ImageView pairImage = createInteractiveImage("/icon/pair_icon.png", this::switchToPairingView);
        ImageView reportImage = createInteractiveImage("/icon/report_icon.png", this::switchToReportsView);
        contentBox.getChildren().addAll(pairImage, reportImage);

        // Add navigation bar and main content to layout
        layout.getChildren().addAll(navBar, contentBox);

        getChildren().clear();
        getChildren().add(layout);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox();
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(10));
        navBar.setSpacing(20);
        navBar.setStyle("-fx-background-color: #0B1D32;");

        // Title on the left
        Label titleLabel = new Label("Mentor App");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Icons on the right
        HBox iconBox = new HBox();
        iconBox.setSpacing(50);
        iconBox.setAlignment(Pos.CENTER_RIGHT);
        iconBox.setPadding(new Insets(0, 20, 0, 0));

        ImageView homeIcon = createSmallIcon("/icon/home_icon.png", this::switchToAdminView, 34, 34);
        ImageView logoutIcon = createSmallIcon("/icon/logout_icon.png", this::logout, 34, 34);

        iconBox.getChildren().addAll(homeIcon, logoutIcon);

        // Space between title and icons
        navBar.getChildren().addAll(titleLabel, iconBox);
        HBox.setHgrow(iconBox, javafx.scene.layout.Priority.ALWAYS);

        return navBar;
    }
    private ImageView createSmallIcon(String imagePath, Runnable action, double width, double height) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-cursor: hand;");
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
        }

        imageView.setOnMouseClicked(e -> action.run());
        return imageView;
    }

    private void switchToAdminView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AdminView(adminController), 800, 600));
    }

    private void logout() {
            Stage stage = (Stage) getScene().getWindow();
            stage.close();

    }
    /**
     * Creates an interactive image with a click action.
     *
     * @param imagePath the path to the image file
     * @param action    the click action
     * @return an ImageView configured for interaction
     */
    private ImageView createInteractiveImage(String imagePath, Runnable action) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(280); // Image width
            imageView.setFitHeight(280); // Image height
            imageView.setPreserveRatio(true); // Maintain aspect ratio
            imageView.setStyle("-fx-cursor: hand;"); // Set cursor to hand
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
        }

        // Wrap Runnable as a MouseEvent handler
        imageView.setOnMouseClicked(e -> action.run());
        return imageView;
    }


    /**
     * Switches to the mentor-student pairing view.
     */
    private void switchToPairingView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AdminPairingView(adminController), 800, 600));
    }

    /**
     * Switches to the assigned reports view.
     */
    private void switchToReportsView() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(new Scene(new AssignedReportView(adminController), 800, 600));
    }
}
