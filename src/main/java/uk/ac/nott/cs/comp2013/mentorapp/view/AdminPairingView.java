package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javafx.stage.Modality;
import javafx.stage.Stage;

import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.RecommendationEngine;
import uk.ac.nott.cs.comp2013.mentorapp.model.ThreadPoolManager;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MenteeType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

/**
 * The AdminPairingView class provides a graphical user interface for administrators
 * to manage and pair mentees with mentors. The class features mentor recommendations,
 * real-time updates, and intuitive navigation for managing mentor-mentee assignments.
 */
public class AdminPairingView extends VBox {
    private final AdminController adminController;
    private final TableView<Mentee> menteeListView = new TableView<>();
    private final TableView<Mentor> mentorListView = new TableView<>();
    private final ObservableList<Mentee> menteeItems = FXCollections.observableArrayList();
    private final ObservableList<Mentor> mentorItems = FXCollections.observableArrayList();
    private Mentee lastSelectedMentee = null;
    private Mentor lastSelectedMentor = null;
    private List<Mentor> recommendedMentors = new ArrayList<>();

    /**
     * Constructor for AdminPairingView.
     *
     * @param adminController the controller handling mentor-mentee pairings
     */
    public AdminPairingView(AdminController adminController) {
        this.adminController = adminController;
        buildView();
        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/admin_pairing.css")).toExternalForm());
            }
        });
        setupMenteeSelectionListener();
        refreshLists();
    }

    /**
     * Builds the UI layout for the pairing view.
     */
    private void buildView() {
        VBox topContainerWithSeparator = createTopContainer();
        HBox listsContainer = createListsContainer();
        HBox buttonContainer = createButtonContainer();
        getChildren().addAll(topContainerWithSeparator, listsContainer, buttonContainer);
        setSpacing(5);
        setAlignment(Pos.TOP_CENTER);
        setupListViewSelection();// Set up column definitions and row factories for both tables
        refreshLists();// Refresh the lists of mentees and mentors
    }

    /**
     * Sets up a listener to update mentor recommendations when a mentee is selected.
     */
    private void setupMenteeSelectionListener() {
        menteeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateMentorRecommendations(newSelection); // Update mentor recommendations when a mentee is selected
        });
    }

    /**
     * Updates the mentor recommendations for the selected mentee.
     *
     * @param selectedMentee the selected mentee
     */
    private void updateMentorRecommendations(Mentee selectedMentee) {
        if (selectedMentee == null) {
            return; // Return if no mentee is selected
        }
        // Get a list of recommended mentors based on the mentee's preferences and availability
        recommendedMentors = RecommendationEngine.recommendMentors(selectedMentee, mentorItems);
        mentorListView.getSelectionModel().clearSelection();
        mentorListView.refresh(); // Refresh the mentor list view
    }

    /**
     * Creates the top navigation bar with header and navigation links.
     *
     * @return A VBox containing the top navigation bar.
     */
    private VBox createTopContainer() {
        ImageView imageView = createImageView();
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.getStyleClass().add("image-container");

        Label adminHomeLabel = createLabel("Home", "navigation-label-inactive");
        Label pairingLabel = createLabel("Pairing Mentors", "navigation-label-active");
        Label viewReportLabel = createLabel("View Assigned Report", "navigation-label-inactive");

        adminHomeLabel.setOnMouseClicked(e -> switchToAdminHome(adminHomeLabel, pairingLabel, viewReportLabel));
        pairingLabel.setOnMouseClicked(e -> switchToPairingView(adminHomeLabel, pairingLabel, viewReportLabel));
        viewReportLabel.setOnMouseClicked(e -> openReportWindow(adminHomeLabel, pairingLabel, viewReportLabel));

        HBox labelContainer = new HBox(20, adminHomeLabel, pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);
        labelContainer.getStyleClass().add("label-container");

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.getStyleClass().add("top-container");

        Separator separator = new Separator();
        separator.getStyleClass().add("top-separator");

        VBox topContainerWithSeparator = new VBox(topContainer, separator);
        topContainerWithSeparator.getStyleClass().add("top-container-with-separator");

        return topContainerWithSeparator;
    }

    /**
     * Creates a styled label for navigation and titles.
     *
     * @param text       The text to display on the label.
     * @param styleClass The CSS style class to apply to the label.
     * @return A styled Label object.
     */
    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Creates an image view for the header.
     *
     * @return An ImageView object displaying the header image.
     */
    private ImageView createImageView() {
        URL imageUrl = getClass().getResource("/icon/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/icon/pairing_image.png");
        }
        Image image = new Image(imageUrl.toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates the button container for pairing actions.
     */
    private HBox createButtonContainer() {
        Button pairButton = new Button("Pair Mentor and Mentee");
        pairButton.getStyleClass().add("pair-button");
        pairButton.setOnAction(e -> pairSelectedMenteeAndMentor());
        HBox buttonContainer = new HBox(pairButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefHeight(80);
        return buttonContainer;
    }

    /**
     * Creates the container for mentee and mentor lists.
     */
    private HBox createListsContainer() {
        configureMenteeListView();
        configureMentorListView();
        Label menteeLabel = new Label("Unpaired Mentees");
        menteeLabel.getStyleClass().add("list-label");
        Label mentorLabel = new Label("All Mentors");
        mentorLabel.getStyleClass().add("list-label");
        VBox menteeContainer = new VBox(10, menteeLabel, menteeListView);
        menteeContainer.getStyleClass().add("mentee-container");
        menteeContainer.setAlignment(Pos.TOP_CENTER);

        VBox mentorContainer = new VBox(10, mentorLabel, mentorListView);
        mentorContainer.getStyleClass().add("mentor-container");
        mentorContainer.setAlignment(Pos.TOP_CENTER);
        HBox listsContainer = new HBox(20, menteeContainer, mentorContainer);
        listsContainer.getStyleClass().add("lists-container");
        listsContainer.setPrefWidth(Double.MAX_VALUE);
        HBox.setHgrow(menteeContainer, Priority.ALWAYS);
        HBox.setHgrow(mentorContainer, Priority.ALWAYS);
        return listsContainer;
    }

    /**
     * Configures the mentee list view.
     */
    private void configureMenteeListView() {
        configureListView(menteeListView, "mentee-list-view", 250, 250);
        menteeListView.setColumnResizePolicy((param) -> true);
    }

    /**
     * Configures the mentor list view.
     */
    private void configureMentorListView() {
        configureListView(mentorListView, "mentor-list-view", 200, 250);
    }

    /**
     * Generic configuration for list views.
     */
    private void configureListView(TableView<?> tableView, String styleClass, double minWidth, double prefWidth) {
        tableView.getStyleClass().add(styleClass);
        tableView.setMinWidth(minWidth);
        tableView.setPrefWidth(prefWidth);
        tableView.setMaxWidth(Double.MAX_VALUE);

    }

    private void switchToPairingView(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "pairing");
        System.out.println("Switched to Pairing Interface");
    }

    /**
     * Switches to the admin home view.
     */
    private void switchToAdminHome(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "home");
        getScene().setRoot(new AdminView(adminController));
    }

    /**
     * Switches to the report view.
     */
    private void openReportWindow(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "report");
        getScene().setRoot(new AssignedReportView(adminController));
    }

    private void updateLabelStyles(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel, String activeView) {
        String activeStyleClass = "active-label";
        String inactiveStyleClass = "inactive-label";

        applyLabelStyle(adminHomeLabel, activeView.equals("home"), activeStyleClass, inactiveStyleClass);
        applyLabelStyle(pairingLabel, activeView.equals("pairing"), activeStyleClass, inactiveStyleClass);
        applyLabelStyle(viewReportLabel, activeView.equals("report"), activeStyleClass, inactiveStyleClass);
    }

    private void applyLabelStyle(Label label, boolean isActive, String activeStyleClass, String inactiveStyleClass) {
        label.getStyleClass().removeAll(activeStyleClass, inactiveStyleClass);
        label.getStyleClass().add(isActive ? activeStyleClass : inactiveStyleClass);
    }
    private void setupListViewSelection() {
        setupMenteeColumns();
        setupMentorColumns();
        setupMenteeRowFactory();
        setupMentorRowFactory();
    }

    /**
     * Sets up columns for the mentor list.
     */
    private void setupMenteeColumns() {
        menteeListView.getColumns().clear();
        TableColumn<Mentee, String> menteeNameColumn = createTableColumn("Mentee Name", 130, Mentee::getUsername);
        menteeListView.getColumns().add(menteeNameColumn);

        // Modified column for Mentee Type with custom conversion logic
        TableColumn<Mentee, String> menteeTypeColumn = createTableColumn("Mentee Level", 100, mentee -> {
            MenteeType menteeType = mentee.getMenteeType();
            if (menteeType != null) {
                return switch (menteeType) {
                    case FIRST_YEAR_STUDENT -> "Y1";
                    case SECOND_YEAR_STUDENT -> "Y2";
                    case THIRD_YEAR_STUDENT -> "Y3";
                };
            }
            return "Not Set";
        });
        menteeListView.getColumns().add(menteeTypeColumn);

        TableColumn<Mentee, String> requestTypeColumn = createTableColumn("Request Type", 260, mentee -> mentee.getSupportRequest() != null
                ? mentee.getSupportRequest().getDisplayName()
                : "No Request");
        menteeListView.getColumns().add(requestTypeColumn);

        TableColumn<Mentee, String> preferredMentorTypeColumn = createTableColumn("Preferred Type", 120, mentee -> {
            MentorType preferredType = mentee.getPreferredMentorType();
            if (preferredType == null) {
                return "Not Set";
            }
            // Map MentorType to the desired display value
            return switch (preferredType) {
                case ALUMNI -> "Alumni";
                case TECH_PROFESSIONAL -> "Professional";
                case SECOND_YEAR_STUDENT -> "Y2 Student";
                case THIRD_YEAR_STUDENT -> "Y3 Student";
            };
        });
        menteeListView.getColumns().add(preferredMentorTypeColumn);

        TableColumn<Mentee, String> cvModalColumn = createCVModalColumn();
        cvModalColumn.getStyleClass().add("cv-column");
        menteeListView.getColumns().add(cvModalColumn);

    }

    private TableColumn<Mentee, String> createCVModalColumn() {
        TableColumn<Mentee, String> cvColumn = new TableColumn<>("CV");

        cvColumn.setCellFactory(column -> new TableCell<>() {
            private final Label cvLabel = new Label("CV");

            {
                cvLabel.getStyleClass().add("cv-label");
                cvLabel.setOnMouseClicked(event -> showCVModal(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cvLabel);
                }
            }
        });

        return cvColumn;
    }

    /**
     * Displays a modal dialog to show the full CV content of the selected mentee.
     *
     * @param mentee The selected Mentee object whose CV content will be displayed.
     */
    private void showCVModal(Mentee mentee) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);

        VBox modalContent = new VBox(15);
        modalContent.setAlignment(Pos.CENTER);
        modalContent.setPadding(new Insets(20));

        /*Label cvLabel = new Label("CV Content:");
        cvLabel.getStyleClass().add("cv-modal-label");

        TextArea cvTextArea = new TextArea(mentee.getCvText());
        cvTextArea.setEditable(false);
        cvTextArea.setWrapText(true);
        cvTextArea.setPrefSize(400, 300);
        cvTextArea.getStyleClass().add("cv-modal-textarea");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> modalStage.close());
        closeButton.getStyleClass().add("cv-modal-button");

        modalContent.getChildren().addAll(cvLabel, cvTextArea, closeButton);
*/
        Label cvLabel = new Label("CV Content:");
        cvLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 10px 0;");

        TextArea cvTextArea = new TextArea(mentee.getCvText());
        cvTextArea.setEditable(false);
        cvTextArea.setWrapText(true);
        cvTextArea.setPrefSize(400, 300);
        cvTextArea.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-radius: 5px;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> modalStage.close());
        closeButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #0b3d61; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-cursor: hand;");
        closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #04253d; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-cursor: hand;"));
        closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #0b3d61; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-cursor: hand;"));

        modalContent.getChildren().addAll(cvLabel, cvTextArea, closeButton);
        modalContent.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-spacing: 15px; -fx-alignment: center;");

        Scene modalScene = new Scene(modalContent);
        modalStage.setScene(modalScene);
        modalStage.setTitle("CV Details");
        modalStage.setResizable(false);
        modalStage.show();
    }

    /**
     * Sets up columns for the mentor table view.
     */
    private void setupMentorColumns() {
        mentorListView.getColumns().clear();

        TableColumn<Mentor, String> mentorNameColumn = createTableColumn("Mentor Name", Mentor::getUsername);
        mentorListView.getColumns().add(mentorNameColumn);

        TableColumn<Mentor, String> availableTimeColumn = createTableColumn("Available Time", 320,
                mentor -> formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability()));
        availableTimeColumn.setSortType(TableColumn.SortType.ASCENDING);
        availableTimeColumn.setComparator((time1, time2) -> {
            LocalDateTime start1 = parseAvailabilityStart(time1);
            LocalDateTime start2 = parseAvailabilityStart(time2);
            return Comparator.nullsLast(LocalDateTime::compareTo).compare(start1, start2);
        });

        mentorListView.getColumns().add(availableTimeColumn);

        TableColumn<Mentor, String> mentorTypeColumn = createTableColumn("Mentor Type", 140,
                mentor -> {
                    MentorType mentorType = mentor.getMentorType();
                    if (mentorType == null) {
                        return "Not Set";
                    }
                    return switch (mentorType) {
                        case ALUMNI -> "Alumni";
                        case TECH_PROFESSIONAL -> "Professional";
                        case SECOND_YEAR_STUDENT -> "Y2 Student";
                        case THIRD_YEAR_STUDENT -> "Y3 Student";
                    };
                });

        mentorListView.getColumns().add(mentorTypeColumn);
        mentorListView.getColumns().add(createMentorStatusColumn());

        mentorListView.setSortPolicy(tableView -> {
            ObservableList<TableColumn<Mentor, ?>> sortOrder = tableView.getSortOrder();
            if (!sortOrder.isEmpty()) {
                sortOrder.get(0).setSortType(TableColumn.SortType.ASCENDING);
            }
            return true;
        });
        mentorListView.getSortOrder().add(availableTimeColumn);
        mentorListView.sort();
    }


    private LocalDateTime parseAvailabilityStart(String availability) {
        if (availability == null || availability.equals("Unavailable")) {
            return null;
        }
        String[] parts = availability.split(" - ");
        if (parts.length > 0) {
            try {
                return LocalDateTime.parse(parts[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Creates a column for displaying the mentor's status (e.g., Available, Paired, Expired).
     */
    private TableColumn<Mentor, String> createMentorStatusColumn() {
        TableColumn<Mentor, String> statusColumn = new TableColumn<>("Status");

        // Set the cell value factory to extract the mentor's status and display it as a string
        statusColumn.setCellValueFactory(cellData -> {
            Mentor mentor = cellData.getValue();
            String status = determineMentorStatus(mentor);
            return new SimpleStringProperty(status);
        });

        // Set the cell factory to provide custom rendering for the status cell
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(createStatusGraphic(status)); // Set a graphic representation
            }
        });
        return statusColumn; // Return the status column for inclusion in the TableView
    }

    /**
     * Determines the status of a mentor based on availability and pairing state.
     */
    private String determineMentorStatus(Mentor mentor) {
        if (mentor.getEndAvailability() != null && mentor.getEndAvailability().isBefore(LocalDateTime.now())) {
            return "Expired";
        }
        return mentor.isAvailable() ? "Available" : "Paired";
    }

    /**
     * Creates a graphical representation of the mentor's status.
     */
    private HBox createStatusGraphic(String status) {
        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().add("status-label");
        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER);
        switch (status) {
            case "Available" -> statusBox.getStyleClass().add("status-available");
            case "Paired" -> statusBox.getStyleClass().add("status-paired");
            case "Expired" -> statusBox.getStyleClass().add("status-expired");
            default -> statusBox.getStyleClass().add("status-default");
        }
        return statusBox;
    }

    /**
     * Configures the row factory for the mentee table, adding styles based on selection state.
     */
    private void setupMenteeRowFactory() {
        menteeListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentee item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("hovered-row", "mentee-selected", "mentee-even-row", "mentee-odd-row");

                if (empty || item == null) {
                    return;
                }
                Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
                if (item.equals(selectedMentee)) {
                    getStyleClass().add("mentee-selected");
                } else {
                    getStyleClass().add(getIndex() % 2 == 0 ? "mentee-even-row" : "mentee-odd-row");
                }
                setOnMouseEntered(event -> {
                    if (!getStyleClass().contains("mentee-selected")) {
                        getStyleClass().add("hovered-row");
                    }
                });
                setOnMouseExited(event -> {
                    getStyleClass().remove("hovered-row");
                    if (!getStyleClass().contains("mentee-selected")) {
                        getStyleClass().add(getIndex() % 2 == 0 ? "mentee-even-row" : "mentee-odd-row");
                    }
                });
            }
        });

        menteeListView.setOnMouseClicked(event -> {
            Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
            if (selectedMentee != null && selectedMentee.equals(lastSelectedMentee)) {
                menteeListView.getSelectionModel().clearSelection();  // Deselect if same item clicked again
                lastSelectedMentee = null;
            } else {
                lastSelectedMentee = selectedMentee;
            }
        });
    }

    /**
     * Configures the row factory for the mentor table, adding styles based on selection and recommendation.
     */
    private void setupMentorRowFactory() {
        mentorListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentor item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("mentor-row", "mentor-selected", "mentor-recommended");
                if (empty || item == null) {
                    setCursor(Cursor.DEFAULT);
                    return;
                }

                setOnMouseEntered(event -> setCursor(Cursor.HAND));
                setOnMouseExited(event -> setCursor(Cursor.DEFAULT));

                Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
                if (item.equals(selectedMentor)) {
                    getStyleClass().add("mentor-selected");
                } else if (recommendedMentors != null && recommendedMentors.contains(item)) {
                    getStyleClass().add("mentor-recommended");
                } else {
                    getStyleClass().add("mentor-row");
                }
            }
        });
        mentorListView.setOnMouseClicked(event -> {
            Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
            if (selectedMentor != null && selectedMentor.equals(lastSelectedMentor)) {
                mentorListView.getSelectionModel().clearSelection();
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selectedMentor;
            }
        });
    }

    /**
     * Creates a table column with a specified title and data extractor function.
     *
     * @param title         The title of the column.
     * @param valueExtractor A function to extract the data for each cell in the column.
     * @param <T>           The type of the objects in the table.
     * @return The configured table column.
     */
    private <T> TableColumn<T, String> createTableColumn(String title, java.util.function.Function<T, String> valueExtractor) {
        return createTableColumn(title, -1, valueExtractor);
    }

    /**
     * Creates a table column with a specified title, preferred width, and data extractor function.
     *
     * @param title         The title of the column.
     * @param prefWidth     The preferred width of the column. Use -1 if no specific width is required.
     * @param valueExtractor A function to extract the data for each cell in the column.
     * @param <T>           The type of the objects in the table.
     * @return The configured table column.
     */
    private <T> TableColumn<T, String> createTableColumn(String title, double prefWidth, java.util.function.Function<T, String> valueExtractor) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(valueExtractor.apply(cellData.getValue())));
        if (prefWidth > 0) column.setPrefWidth(prefWidth);
        return column;
    }

    /**
     * Pairs the selected mentee and mentor.
     * This method uses a background task to perform the pairing logic
     * and updates the UI upon task completion or failure.
     */
    private void pairSelectedMenteeAndMentor() {
        // Get the selected mentee and mentor from the respective table views
        Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
        if (selectedMentee == null || selectedMentor == null) {
            displaySnackbar(false, "Please select both a mentee and a mentor to proceed.");
            return;
        }

        // Create a background task for pairing logic
        Task<Boolean> pairingTask = new Task<>() {
            @Override
            protected Boolean call() {
                // Perform the pairing operation in the background
                return adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            }
        };
        // On successful completion of the task, update the UI
        pairingTask.setOnSucceeded(event -> {
            boolean success = pairingTask.getValue();
            Platform.runLater(() -> {
                displaySnackbar(success, success ? "Successfully paired!"
                        : "Pairing failed: Mentor \"" + selectedMentor.getUsername() + "\" is unavailable.");
                if (success) {
                    refreshLists(); // Refresh the mentee and mentor lists
                }
            });
        });
        // Handle any errors or failures during task execution
        pairingTask.setOnFailed(event -> Platform.runLater(() -> {
            displaySnackbar(false, "An error occurred during pairing.");
        }));
        // Submit the task to the thread pool for execution
        ThreadPoolManager.getExecutor().submit(pairingTask);
    }


    /**
     * Displays a snackbar message to provide feedback on operations.
     */
    private void displaySnackbar(boolean success, String message) {
        showSnackbar(message, success);
    }

    /**
     * Displays a snackbar message with success or error styling.
     * The snackbar is displayed in the center of the scene and automatically removed after 3 seconds.
     *
     * @param message The message to display in the snackbar.
     * @param success Whether the snackbar should use the success or error style.
     */
    private void showSnackbar(String message, boolean success) {
        Platform.runLater(() -> {
            // Create a label with the message and apply the appropriate style
            Label messageLabel = new Label(message);
            messageLabel.getStyleClass().add(success ? "snackbar-success" : "snackbar-error");

            // Get the current scene of the view
            Scene scene = this.getScene();
            if (scene == null) return; // Return if the scene is not available
            // Get the root pane of the scene
            Pane root = (Pane) scene.getRoot();
            // Check if a snackbar already exists, and remove it if present
            StackPane existingSnackbar = (StackPane) root.lookup("#snackbarPane");
            if (existingSnackbar != null) {
                root.getChildren().remove(existingSnackbar);
            }
            // Create a new snackbar and add it to the root pane
            StackPane snackbarPane = new StackPane(messageLabel);
            snackbarPane.setId("snackbarPane"); // Set an ID for later lookup
            snackbarPane.setAlignment(Pos.CENTER);
            snackbarPane.getStyleClass().add("snackbar-pane");
            root.getChildren().add(snackbarPane);

            // Schedule the snackbar to be removed after 3 seconds
            PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(event -> root.getChildren().remove(snackbarPane));
            delay.play();
        });
    }

    /**
     * Refreshes the mentee and mentor lists.
     */
    private void refreshLists() {
        Task<Void> refreshTask = new Task<>() {
            @Override
            protected Void call() {
                List<Mentee> mentees = adminController.getAllUnpairedMentees();
                List<Mentor> mentors = adminController.getAvailableMentors();
                Platform.runLater(() -> {
                    refreshList(menteeItems, menteeListView, mentees);
                    refreshList(mentorItems, mentorListView, mentors);
                });
                return null;
            }
        };
        new Thread(refreshTask).start();
    }

    private <T> void refreshList(ObservableList<T> items, TableView<T> tableView, List<T> newData) {
        items.clear();
        items.addAll(newData);
        tableView.setItems(items);
    }
    /**
     * Formats availability times for mentors.
     */
    private String formatAvailability(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "Unavailable";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter) + " - " + end.format(formatter); // Return formatted availability times
    }
}