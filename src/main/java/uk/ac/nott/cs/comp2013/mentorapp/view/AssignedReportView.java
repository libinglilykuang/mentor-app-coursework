package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.ThreadPoolManager;

/**
 * View for displaying assigned mentor-mentee pairs and their details.
 */
public class AssignedReportView extends VBox {

    private final AdminController adminController;
    private final TableView<Pair> reportTableView;

    /**
     * Constructs an AssignedReportView with the given admin controller.
     *
     * @param adminController Controller for managing mentor-mentee relationships.
     */
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

    /**
     * Builds the view layout, including the table and navigation labels.
     */
    private void buildView() {
        HBox topContainer = createTopContainer();
        topContainer.setAlignment(Pos.CENTER_LEFT);
        setupTableView();
        HBox centeredTableContainer = new HBox(reportTableView);
        centeredTableContainer.setAlignment(Pos.CENTER);
        refreshReport();
        getStyleClass().add("assigned-report-view");
        getChildren().addAll(topContainer, centeredTableContainer);
    }

    /**
     * Creates the top container with navigation labels.
     */
    private HBox createTopContainer() {
        VBox imageContainer = createImageContainer();
        Label pairingLabel = createLabel("Pairing Mentors", "navigation-label-inactive", e -> getScene().setRoot(new AdminPairingView(adminController)));
        Label viewReportLabel = createLabel("View Assigned Report", "navigation-label-active", null);
        Label adminHomeLabel = createLabel("Home", "navigation-label-inactive", e -> getScene().setRoot(new AdminView(adminController)));

        HBox labelContainer = new HBox(20, adminHomeLabel, pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.getStyleClass().add("top-container");
        topContainer.setAlignment(Pos.CENTER_LEFT);
        return topContainer;
    }

    /**
     * Creates the image container for the top section.
     */
    private VBox createImageContainer() {
        URL imageUrl = getClass().getResource("/icon/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: /icon/pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.getStyleClass().add("image-container");

        return imageContainer;
    }

    /**
     * Creates a navigation label with a click event.
     *
     * @param text      The label text.
     * @param styleClass CSS class for styling.
     * @param onClick   Click event handler.
     */
    private Label createLabel(String text, String styleClass, javafx.event.EventHandler<javafx.scene.input.MouseEvent> onClick) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        if (onClick != null) {
            label.setOnMouseClicked(onClick);
        }
        return label;
    }

    /**
     * Configures the table view with columns for displaying mentor-mentee details.
     */
    private void setupTableView() {
        TableColumn<Pair, String> mentorColumn = createTableColumn("Mentor", 150, Pair::getMentorName);
        TableColumn<Pair, String> menteeColumn = createTableColumn("Mentee", 150, Pair::getMenteeName);
        TableColumn<Pair, String> mentorTypeColumn = createTableColumn("Mentor Type", 230, pair -> {
            String mentorType = adminController.getMentorTypeByName(pair.getMentorName());
            return mentorType != null ? mentorType : "Not Set";
        });
        TableColumn<Pair, String> availabilityColumn = createTableColumn("Availability", 300, pair -> {
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

        reportTableView.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                reportTableView.lookup(".column-header-background").setStyle("-fx-border-color: #0b3d61; -fx-border-width: 2px;");
            }
        });

        reportTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Pair item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setPrefHeight(40);
                    setStyle("-fx-border-color: #cccccc; " +
                            "-fx-border-width: 1px 0px; " +
                            "-fx-font-size: 14px;"+
                            "-fx-background-color: white;");
                }
            }
        });
    }

    /**
     * Creates a table column with the specified properties.
     *
     * @param title         The title of the column.
     * @param width         The preferred width of the column.
     * @param valueFunction Function to extract data for the column.
     */
    private TableColumn<Pair, String> createTableColumn(String title, double width, Function<Pair, String> valueFunction) {
        TableColumn<Pair, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(valueFunction.apply(data.getValue())));
        column.setPrefWidth(width);
        return column;
    }

    /**
     * Creates a table column for displaying CV summaries.
     */
    private TableColumn<Pair, String> createCVSummaryColumn() {
        TableColumn<Pair, String> cvColumn = new TableColumn<>("CV Summary");
        cvColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCvText()));
        cvColumn.setPrefWidth(500);
        return cvColumn;
    }

    /**
     * Refreshes the report table with the latest mentor-mentee assignment data.
     */
    private void refreshReport() {
        Task<ObservableList<Pair>> fetchReportTask = new Task<>() {
            @Override
            protected ObservableList<Pair> call() {
                ObservableList<Pair> data = FXCollections.observableArrayList();
                adminController.getAssignedPairs().forEach(pair -> {
                    String mentor = pair.getKey().getUsername();
                    String mentee = pair.getValue().getUsername();
                    String cvText = adminController.getMenteeCVByName(mentee);
                    data.add(new Pair(mentor, mentee, cvText));
                });
                return data;
            }
        };

        fetchReportTask.setOnSucceeded(event -> {
            ObservableList<Pair> data = fetchReportTask.getValue();
            reportTableView.setItems(data);
        });

        fetchReportTask.setOnFailed(event -> {
            Throwable exception = fetchReportTask.getException();
            exception.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to refresh report: " + exception.getMessage());
            alert.show();
        });

        ThreadPoolManager.getExecutor().submit(fetchReportTask);
    }

    /**
     * Represents a pair of mentor and mentee with associated data.
     */
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
