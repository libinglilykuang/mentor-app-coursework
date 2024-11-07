package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;

public class AdminView extends VBox {
    private final AdminController adminController;
    private final AdminPairingView pairingView;

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        this.pairingView = new AdminPairingView(adminController);

        buildMainMenu();
    }

    private void buildMainMenu() {
        getChildren().clear();

        Label welcomeLabel = new Label("Welcome, Admin!");
        Button btnPairMentorStudent = new Button("Pair Mentor and Student");
        Button btnViewReports = new Button("View Assigned Report");

        btnPairMentorStudent.setOnAction(e -> showPairingInterface());
        btnViewReports.setOnAction(e -> showReportInterface());

        getChildren().addAll(welcomeLabel, btnPairMentorStudent, btnViewReports);
    }

    private void showPairingInterface() {
        getChildren().clear();

        if (pairingView.lookup("#backButton") == null) {
            Button backButton = new Button("Return to Main Menu");
            backButton.setId("backButton");
            styleBackButton(backButton);

            backButton.setOnAction(e -> buildMainMenu());
            pairingView.getChildren().add(backButton);
        }

        getChildren().add(pairingView);
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-background-color: #ff5722; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #e64a19; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #ff5722; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10px 20px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"));
    }

    private void showReportInterface() {
        getChildren().clear();
        Label reportLabel = new Label("Assigned Report View");
        Button backButton = new Button("Return to Main Menu");
        backButton.setOnAction(e -> buildMainMenu());

        getChildren().addAll(reportLabel, backButton);
    }
}