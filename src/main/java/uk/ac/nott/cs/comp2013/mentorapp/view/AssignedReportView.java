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
        pairingLabel.setStyle(getInactiveStyle());
        viewReportLabel.setStyle(getActiveStyle());
        adminHomeLabel.setStyle(getInactiveStyle());
        adminHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new AdminView(adminController)));
        pairingLabel.setOnMouseClicked(e -> getScene().setRoot(new AdminPairingView(adminController)));

        HBox labelContainer = new HBox(20, adminHomeLabel,pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

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
                        setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
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

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }
}