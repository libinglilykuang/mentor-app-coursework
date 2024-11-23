package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

public class AssignedReportView extends VBox {
    private final AdminController adminController;
    private final TableView<Pair> reportTableView;

    public AssignedReportView(AdminController adminController) {
        this.adminController = adminController;
        this.reportTableView = new TableView<>();
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/assigned-report-view.css")).toExternalForm());
            }
        });
    }

    private void buildView() {
        HBox topContainer = createTopContainer();
        setupTableView();
        refreshReport();

        getStyleClass().add("assigned-report-view");
        getChildren().addAll(topContainer, reportTableView);
    }

    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        Label pairingLabel = createLabel("Pairing Mentors", "navigation-label-inactive", e -> getScene().setRoot(new AdminPairingView(adminController)));
        Label viewReportLabel = createLabel("View Assigned Report", "navigation-label-active", null);
        Label adminHomeLabel = createLabel("Home", "navigation-label-inactive", e -> getScene().setRoot(new AdminView(adminController)));

        HBox labelContainer = new HBox(20, adminHomeLabel, pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.getStyleClass().add("top-container");

        return topContainer;
    }

    private VBox createImageContainer() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: /pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.getStyleClass().add("image-container");

        return imageContainer;
    }

    private Label createLabel(String text, String styleClass, javafx.event.EventHandler<javafx.scene.input.MouseEvent> onClick) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        if (onClick != null) {
            label.setOnMouseClicked(onClick);
        }
        return label;
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
        reportTableView.getStyleClass().add("report-table-view");

        VBox.setVgrow(reportTableView, Priority.ALWAYS);
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
        cvColumn.setPrefWidth(500);
        return cvColumn;
    }

    private void refreshReport() {
        ObservableList<Pair> data = FXCollections.observableArrayList();
        adminController.getAssignedPairs().forEach(pair -> {
            String mentor = pair.getKey().getUsername();
            String mentee = pair.getValue().getUsername();
            String cvText = adminController.getMenteeCVByName(mentee);
            data.add(new Pair(mentor, mentee, cvText));
        });
        reportTableView.setItems(data);
    }

    public static class Pair {
        private final String mentorName;
        private final String menteeName;
        private final String cvText;

        public Pair(String mentorName, String menteeName, String cvText) {
            this.mentorName = mentorName;
            this.menteeName = menteeName;
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
}