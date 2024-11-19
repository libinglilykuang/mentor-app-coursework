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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import java.net.URL;
import java.util.function.Function;

public class AssignedReportView extends VBox {
    private final AdminController adminController;
    private final TableView<Pair> reportTableView;

    public AssignedReportView(AdminController adminController) {
        this.adminController = adminController;
        this.reportTableView = new TableView<>();
        buildView();
    }

    private void buildView() {
        setStyle("-fx-background-color: white;");
        HBox topContainer = createTopContainer();
        setupTableView();
        refreshReport();
        styleReportTableView();
        VBox layout = new VBox(20, topContainer, reportTableView);
        layout.setPadding(new Insets(10));
        layout.setSpacing(20);
        getChildren().clear();
        getChildren().addAll(topContainer, reportTableView);
    }

    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        Label pairingLabel = createLabel("Pairing Mentors", false);
        Label viewReportLabel = createLabel("View Assigned Report", true);
        Label adminHomeLabel = createLabel("Home", false);
        adminHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new AdminView(adminController)));
        pairingLabel.setOnMouseClicked(e -> getScene().setRoot(new AdminPairingView(adminController)));
        HBox labelContainer = new HBox(20, adminHomeLabel, pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10));
        return topContainer;
    }

    private VBox createImageContainer() {
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
        return imageContainer;
    }

    private Label createLabel(String text, boolean isActive) {
        Label label = new Label(text);
        label.setStyle(isActive ? getActiveStyle() : getInactiveStyle());
        return label;
    }

    private void styleReportTableView() {
        reportTableView.setPrefWidth(Double.MAX_VALUE);
        reportTableView.setPrefHeight(700);
        reportTableView.setStyle(
                "-fx-border-color: #0b3d61; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );
        VBox.setVgrow(reportTableView, Priority.ALWAYS);
    }

    private void setupTableView() {
        TableColumn<Pair, String> mentorColumn = createTableColumn("Mentor", 200, Pair::getMentorName);
        TableColumn<Pair, String> menteeColumn = createTableColumn("Mentee", 200, Pair::getMenteeName);
        TableColumn<Pair, String> mentorTypeColumn = createTableColumn("Mentor Type", 250, pair -> {
            String mentorType = adminController.getMentorTypeByName(pair.getMentorName());
            return mentorType != null ? mentorType : "Not Set";
        });
        TableColumn<Pair, String> availabilityColumn = createTableColumn("Availability", 250, pair -> {
            String availability = adminController.getMentorAvailabilityByName(pair.getMentorName());
            return availability != null ? availability : "Unavailable";
        });
        TableColumn<Pair, String> cvColumn = createCVSummaryColumn();

        reportTableView.getColumns().add(mentorColumn);
        reportTableView.getColumns().add(menteeColumn);
        reportTableView.getColumns().add(mentorTypeColumn);
        reportTableView.getColumns().add(availabilityColumn);
        reportTableView.getColumns().add(cvColumn);
        reportTableView.setPlaceholder(new Label("No assigned reports to display."));
        reportTableView.getPlaceholder().setStyle("-fx-text-fill: gray; -fx-font-size: 16px; -fx-font-weight: bold;");
        reportTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            reportTableView.refresh();
        });
        setCustomCellFactory(mentorColumn);
        setCustomCellFactory(menteeColumn);
    }

    private TableColumn<Pair, String> createTableColumn(String title, double width, Function<Pair, String> valueFunction) {
        TableColumn<Pair, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(valueFunction.apply(data.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    private TableColumn<Pair, String> createCVSummaryColumn() {
        TableColumn<Pair, String> cvColumn = new TableColumn<>("CV Summary");
        cvColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCvText()));
        cvColumn.setCellFactory(column -> new TableCell<>() {
            private Tooltip tooltip;

            @Override
            protected void updateItem(String fullCvText, boolean empty) {
                super.updateItem(fullCvText, empty);
                if (empty || fullCvText == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String summary = fullCvText.length() > 100 ? fullCvText.substring(0, 100) + "..." : fullCvText;
                    setText(summary);
                    tooltip = createTooltip(fullCvText);
                    setOnMouseEntered(event -> setTooltip(tooltip));
                    setOnMouseExited(event -> setTooltip(null));
                }
            }
        });
        cvColumn.setPrefWidth(500);
        return cvColumn;
    }

    private Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(400);
        tooltip.setStyle("-fx-font-size: 14px;");
        tooltip.setShowDelay(javafx.util.Duration.ZERO);
        tooltip.setHideDelay(javafx.util.Duration.INDEFINITE);
        tooltip.setShowDuration(javafx.util.Duration.INDEFINITE);
        return tooltip;
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
            String mentor = pair.getKey().getUsername();
            String mentee = pair.getValue().getUsername();
            String mentorType = adminController.getMentorTypeByName(mentor);
            String availability = adminController.getMentorAvailabilityByName(mentor);
            String cvText = adminController.getMenteeCVByName(mentee);
            data.add(new Pair(mentor, mentee, mentorType, availability, cvText));
        });
        reportTableView.setItems(data);
    }

    public static class Pair {
        private final String mentorName;
        private final String menteeName;
        private final String mentorType;
        private final String availability;
        private final String cvText;
        public Pair(String mentorName, String menteeName, String mentorType, String availability, String cvText) {
            this.mentorName = mentorName;
            this.menteeName = menteeName;
            this.mentorType = mentorType;
            this.availability = availability;
            this.cvText = cvText;
        }
        public String getMentorName() {
            return mentorName;
        }
        public String getMenteeName() {
            return menteeName;
        }
        public String getCvText() {
            return cvText;
        }
    }

    private String getActiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
    }

    private String getInactiveStyle() {
        return "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";
    }
}