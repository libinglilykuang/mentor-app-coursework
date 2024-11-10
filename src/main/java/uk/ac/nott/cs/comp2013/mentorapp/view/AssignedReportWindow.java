package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;

public class AssignedReportWindow extends Stage {

    private final AdminController adminController;
    private final ListView<String> reportListView;

    public AssignedReportWindow(AdminController adminController) {
        this.adminController = adminController;
        this.reportListView = new ListView<>();
        setupWindow();
    }

    private void setupWindow() {
        // Set window title
        setTitle("Mentor-Mentee Assignment Report");
        initModality(Modality.APPLICATION_MODAL); // Modal window to block the main interface

        // Title label
        Label titleLabel = new Label("Assigned Mentors and Mentees");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        // ListView for displaying the report
        reportListView.setPrefHeight(400);
        reportListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px; -fx-padding: 10px;");
        refreshReport(); // Populate data

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-font-weight: bold;");
        closeButton.setOnAction(e -> this.close()); // Close the window

        // Layout
        VBox layout = new VBox(20, titleLabel, reportListView, closeButton);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.setSpacing(20);

        // Scene setup
        Scene scene = new Scene(layout, 500, 500);
        setScene(scene);
    }

    private void refreshReport() {
        reportListView.getItems().clear();

        // Fetch assigned pairs from AdminController
        adminController.getAssignedPairs().forEach(pair -> {
            String mentor = pair.getKey().getUsername(); // Use getKey() for the Mentor
            String mentee = pair.getValue().getUsername(); // Use getValue() for the Mentee
            reportListView.getItems().add("Mentor: " + mentor + " - Mentee: " + mentee);
        });
    }

}
