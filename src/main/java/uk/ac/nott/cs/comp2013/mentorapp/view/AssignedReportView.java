package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import java.net.URL;

public class AssignedReportView extends VBox {
    private final AdminController adminController;
    private final TableView<Pair> reportTableView;

    public AssignedReportView(AdminController adminController) {
        this.adminController = adminController;
        this.reportTableView = new TableView<>();
        buildView();
    }

    private void buildView() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: /pairing_image.png");
        }
        Image image = new Image(imageUrl.toExternalForm());

        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label pairingLabel = new Label("Pairing Mentors");
        Label viewReportLabel = new Label("View Assigned Report");
        Label adminHomeLabel = new Label("Home");

        // Set styles for labels
        adminHomeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        adminHomeLabel.setOnMouseClicked(e -> switchToAdminHome(adminHomeLabel, pairingLabel, viewReportLabel));

        // Set styles and actions for labels
        pairingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");
        pairingLabel.setOnMouseClicked(e -> switchToPairingView(adminHomeLabel, pairingLabel, viewReportLabel));

        viewReportLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");

        // Label container
        HBox labelContainer = new HBox(20, adminHomeLabel,pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        updateStyles(viewReportLabel, adminHomeLabel, pairingLabel);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10));

        setupTableView();
        refreshReport();

        reportTableView.setPrefWidth(500);
        reportTableView.setPrefHeight(700);

        VBox layout = new VBox(20,topContainer, reportTableView);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.setSpacing(20);

        getChildren().addAll(topContainer, reportTableView);

    }

    private void updateStyles(Label activeLabel, Label... otherLabels) {
        activeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");

        for (Label label : otherLabels) {
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        }
    }

    private void switchToPairingView(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        // Update styles
        adminHomeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        pairingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");
        viewReportLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");

        // Logic to refresh or focus on the pairing view
        System.out.println("Switched to Pairing Interface");
    }

    private void switchToAdminHome(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        // Update styles
        adminHomeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");
        pairingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        viewReportLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");

        // Logic to navigate to AdminView
        System.out.println("Navigating to Admin Home...");
        // Notify the main controller or parent layout to switch views
        getScene().setRoot(new AdminView(adminController)); // Replace the current view with AdminView
    }

    private void setupTableView() {
        // Mentor column
        TableColumn<Pair, String> mentorColumn = new TableColumn<>("Mentor");
        mentorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMentorName()));
        mentorColumn.setPrefWidth(250);

        // Mentee column
        TableColumn<Pair, String> menteeColumn = new TableColumn<>("Mentee");
        menteeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMenteeName()));
        menteeColumn.setPrefWidth(250);

        // Add columns to TableView
        reportTableView.getColumns().add(mentorColumn);
        reportTableView.getColumns().add(menteeColumn);

        reportTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            reportTableView.refresh();
        });

        setCustomCellFactory(mentorColumn);
        setCustomCellFactory(menteeColumn);
    }

    private void setCustomCellFactory(TableColumn<Pair, String> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Pair selectedPair = reportTableView.getSelectionModel().getSelectedItem();
                    Pair currentPair = getTableRow().getItem();

                    if (currentPair != null && currentPair.equals(selectedPair)) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void refreshReport() {
        ObservableList<Pair> data = FXCollections.observableArrayList();

        adminController.getAssignedPairs().forEach(pair -> {
            String mentor = pair.getKey().getUsername(); // Use getKey() for the Mentor
            String mentee = pair.getValue().getUsername(); // Use getValue() for the Mentee
            data.add(new Pair(mentor, mentee));
        });

        reportTableView.setItems(data);
    }

    // Helper class for TableView
    public static class Pair {
        private final String mentorName;
        private final String menteeName;

        public Pair(String mentorName, String menteeName) {
            this.mentorName = mentorName;
            this.menteeName = menteeName;
        }

        public String getMentorName() {
            return mentorName;
        }

        public String getMenteeName() {
            return menteeName;
        }
    }
}