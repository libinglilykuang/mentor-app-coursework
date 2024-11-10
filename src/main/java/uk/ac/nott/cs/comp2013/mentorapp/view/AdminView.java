package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
        AssignedReportWindow reportWindow = new AssignedReportWindow(adminController);
        reportWindow.showAndWait();
    }
}