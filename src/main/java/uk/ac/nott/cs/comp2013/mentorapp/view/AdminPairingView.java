package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.RecommendationEngine;
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
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/pairing_image.png");
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
        configureListView(menteeListView, "mentee-list-view", 400, 450);
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
        TableColumn<Mentee, String> menteeNameColumn = createTableColumn("Mentee Name", 100, Mentee::getUsername);
        menteeListView.getColumns().add(menteeNameColumn);

        TableColumn<Mentee, String> requestTypeColumn = createTableColumn("Request Type", 325, mentee -> mentee.getSupportRequest() != null
                ? mentee.getSupportRequest().getDisplayName()
                : "No Request");
        menteeListView.getColumns().add(requestTypeColumn);

        TableColumn<Mentee, String> preferredMentorTypeColumn = createTableColumn("Preferred Mentor Type", 180, mentee -> {
            MentorType preferredType = mentee.getPreferredMentorType();
            return preferredType != null ? preferredType.getDescription() : "Not Set";
        });
        menteeListView.getColumns().add(preferredMentorTypeColumn);

        TableColumn<Mentee, String> menteeTypeColumn = createTableColumn("Mentee Type", 130, mentee -> {
            MenteeType menteeType = mentee.getMenteeType();
            return menteeType != null ? menteeType.getDescription() : "Not Set";
        });
        menteeListView.getColumns().add(menteeTypeColumn);

        TableColumn<Mentee, String> cvSummaryColumn = createCVSummaryColumn();
        menteeListView.getColumns().add(cvSummaryColumn);
    }

    /**
     * Creates a table column for displaying the mentee's CV summary with tooltips for full content.
     */
    private TableColumn<Mentee, String> createCVSummaryColumn() {
        TableColumn<Mentee, String> cvTextColumn = new TableColumn<>("CV Summary");
        cvTextColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvText()));
        cvTextColumn.setCellFactory(column -> createTooltipTableCell());
        return cvTextColumn;
    }

    /**
     * Creates a custom TableCell that displays a truncated summary of the content,
     * with a tooltip that shows the full content when hovered over.
     * The tooltip is configured to wrap text and have a maximum width for better readability.
     *
     * @param <T> The type of the items in the table.
     * @return A customized TableCell with a tooltip for displaying long text.
     */
    private <T> TableCell<T, String> createTooltipTableCell() {
        // Create a new TableCell instance with a tooltip for displaying long text.
        return new TableCell<>() {
            private final Tooltip tooltip = new Tooltip();  // Create a new Tooltip to display full text

            @Override
            protected void updateItem(String fullText, boolean empty) {
                super.updateItem(fullText, empty);  // Update the item in the TableCell

                // If the item is empty or contains null text, clear the text and tooltip
                if (empty || fullText == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    // If the full text is too long, show a truncated summary
                    String summary = fullText.length() > 30 ? fullText.substring(0, 30) + "..." : fullText;
                    setText(summary);

                    // Configure the tooltip with the full text
                    tooltip.setText(fullText);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(400);
                    tooltip.getStyleClass().add("custom-tooltip");
                    setTooltip(tooltip);
                }
            }
        };
    }

    /**
     * Sets up columns for the mentor table view.
     */
    private void setupMentorColumns() {
        mentorListView.getColumns().clear();
        TableColumn<Mentor, String> mentorNameColumn = createTableColumn("Mentor Name", Mentor::getUsername);
        mentorListView.getColumns().add(mentorNameColumn);

        TableColumn<Mentor, String> availableTimeColumn = createTableColumn("Available Time",
                mentor -> formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability()));
        mentorListView.getColumns().add(availableTimeColumn);

        TableColumn<Mentor, String> mentorTypeColumn = createTableColumn("Mentor Type", 180,
                mentor -> {
                    MentorType mentorType = mentor.getMentorType();
                    return mentorType != null ? mentorType.getDescription() : "Not Set";
                });
        mentorListView.getColumns().add(mentorTypeColumn);
        mentorListView.getColumns().add(createMentorStatusColumn());
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
        // Set a custom row factory for the mentee list view
        menteeListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentee item, boolean empty) {
                super.updateItem(item, empty); // Ensure the default behavior is executed
                if (empty || item == null) {
                    // If the row is empty or contains a null item, remove all custom styles
                    getStyleClass().removeAll("mentee-selected", "mentee-even-row", "mentee-odd-row");
                } else {
                    // Get the currently selected mentee from the selection model
                    Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
                    getStyleClass().removeAll("mentee-selected", "mentee-even-row", "mentee-odd-row");
                    // If the current row item is the selected mentee, highlight it as selected
                    if (item.equals(selectedMentee)) {
                        getStyleClass().add("mentee-selected");
                    } else {
                        // Otherwise, apply alternating row styles (even/odd rows)
                        getStyleClass().add(getIndex() % 2 == 0 ? "mentee-even-row" : "mentee-odd-row");
                    }
                }
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
                if (empty || item == null) {
                    getStyleClass().removeAll("mentor-selected", "mentor-recommended", "mentor-even-row", "mentor-odd-row");
                } else {
                    getStyleClass().removeAll("mentor-selected", "mentor-recommended", "mentor-even-row", "mentor-odd-row");
                    Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
                    if (item.equals(selectedMentor)) {
                        getStyleClass().add("mentor-selected");
                    } else if (recommendedMentors != null && recommendedMentors.contains(item)) {
                        getStyleClass().add("mentor-recommended");
                    }
                }
            }
        });
        mentorListView.setOnMouseClicked(event -> {
            Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
            if (selectedMentor != null && selectedMentor.equals(lastSelectedMentor)) {
                mentorListView.getSelectionModel().clearSelection();  // Deselect if same item clicked again
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selectedMentor;
            }
        });
    }

    /**
     * Handles selection of an item in a table view.
     *
     * @param tableView The table view.
     * @param selectionConsumer The action to perform on the selected item.
     * @param <T> The type of items in the table.
     */
    private <T> void handleSelection(TableView<T> tableView, java.util.function.Consumer<T> selectionConsumer) {
        T selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.equals(selectionConsumer)) {
            tableView.getSelectionModel().clearSelection();
            selectionConsumer.accept(null);
        } else {
            selectionConsumer.accept(selected);
        }
        tableView.refresh();
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
     * Handles the pairing of the selected mentee and mentor.
     * Displays a success or error message based on the result of the pairing operation.
     */
    private void pairSelectedMenteeAndMentor() {
        Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        // Check if both a mentee and mentor are selected
        if (selectedMentee == null || selectedMentor == null) {
            displaySnackbar(false, "Please select both a mentee and a mentor to proceed.");
            return;
        }

        // Attempt to pair the mentee with the selected mentor
        boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
        displaySnackbar(success, success
                ? "Successfully paired!"
                : "Pairing failed: Mentor \"" + selectedMentor.getUsername() + "\" is unavailable.");

        if (success) refreshLists();
    }

    /**
     * Displays a snackbar message to provide feedback on operations.
     */
    private void displaySnackbar(boolean success, String message) {
        showSnackbar(message, success);
    }

    /**
     * Shows a snackbar notification at the center of the scene.
     */
    private void showSnackbar(String message, boolean success) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add(success ? "snackbar-success" : "snackbar-error");
        Scene scene = this.getScene();
        if (scene == null) return;
        Pane root = (Pane) scene.getRoot();
        StackPane snackbarPane = new StackPane(messageLabel);
        snackbarPane.setAlignment(Pos.CENTER);
        root.getChildren().add(snackbarPane);
        PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(3));
        delay.setOnFinished(event -> root.getChildren().remove(snackbarPane));
        delay.play();
    }

    /**
     * Refreshes the mentee and mentor lists.
     */
    private void refreshLists() {
        refreshList(menteeItems, menteeListView, adminController.getAllUnpairedMentees());
        refreshList(mentorItems, mentorListView, adminController.getAvailableMentors());
    }

    /**
     * Refreshes a given list with new data.
     */
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