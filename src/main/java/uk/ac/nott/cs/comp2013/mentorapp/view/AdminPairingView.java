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
import uk.ac.nott.cs.comp2013.mentorapp.model.logic.RecommendationEngine;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MenteeType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

public class AdminPairingView extends VBox {
    private final AdminController adminController;
    private final TableView<Mentee> menteeListView = new TableView<>();
    private final TableView<Mentor> mentorListView = new TableView<>();
    private final ObservableList<Mentee> menteeItems = FXCollections.observableArrayList();
    private final ObservableList<Mentor> mentorItems = FXCollections.observableArrayList();
    private Mentee lastSelectedMentee = null;
    private Mentor lastSelectedMentor = null;
    private List<Mentor> recommendedMentors = new ArrayList<>();


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

    private void buildView() {
        VBox topContainerWithSeparator = createTopContainer();
        HBox listsContainer = createListsContainer();
        HBox buttonContainer = createButtonContainer();
        getChildren().addAll(topContainerWithSeparator, listsContainer, buttonContainer);
        setSpacing(5);
        setAlignment(Pos.TOP_CENTER);
        setupListViewSelection();
        refreshLists();
    }

    private void setupMenteeSelectionListener() {
        menteeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateMentorRecommendations(newSelection);
        });
    }

    private void updateMentorRecommendations(Mentee selectedMentee) {
        if (selectedMentee == null) {
            return;
        }
        recommendedMentors = RecommendationEngine.recommendMentors(selectedMentee, mentorItems);
        mentorListView.getSelectionModel().clearSelection();
        mentorListView.refresh();
    }

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

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

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

    private HBox createButtonContainer() {
        Button pairButton = new Button("Pair Mentor and Mentee");
        pairButton.getStyleClass().add("pair-button");
        pairButton.setOnAction(e -> pairSelectedMenteeAndMentor());
        HBox buttonContainer = new HBox(pairButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefHeight(80);
        return buttonContainer;
    }

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

    private void configureMenteeListView() {
        configureListView(menteeListView, "mentee-list-view", 400, 450);
    }

    private void configureMentorListView() {
        configureListView(mentorListView, "mentor-list-view", 200, 250);
    }

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

    private void switchToAdminHome(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "home");
        getScene().setRoot(new AdminView(adminController));
    }

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

    private void setupMenteeColumns() {
        menteeListView.getColumns().clear();

        menteeListView.getColumns().addAll(
                createTableColumn("Mentee Name", 100, Mentee::getUsername),
                createTableColumn("Request Type", 325, mentee -> {
                    return mentee.getSupportRequest() != null
                            ? mentee.getSupportRequest().getDisplayName()
                            : "No Request";
                }),
                createTableColumn("Preferred Mentor Type", 180, mentee -> {
                    MentorType preferredType = mentee.getPreferredMentorType();
                    return preferredType != null ? preferredType.getDescription() : "Not Set";
                }),
                createTableColumn("Mentee Type", 130, mentee -> {
                    MenteeType menteeType = mentee.getMenteeType();
                    return menteeType != null ? menteeType.getDescription() : "Not Set";
                }),
                createCVSummaryColumn()
        );
    }

    private TableColumn<Mentee, String> createCVSummaryColumn() {
        TableColumn<Mentee, String> cvTextColumn = new TableColumn<>("CV Summary");
        cvTextColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCvText()));
        cvTextColumn.setCellFactory(column -> createTooltipTableCell());
        return cvTextColumn;
    }

    private <T> TableCell<T, String> createTooltipTableCell() {
        return new TableCell<>() {
            private final Tooltip tooltip = new Tooltip();
            @Override
            protected void updateItem(String fullText, boolean empty) {
                super.updateItem(fullText, empty);
                if (empty || fullText == null) {
                    setText(null);
                    setTooltip(null);
                } else {String summary = fullText.length() > 30 ? fullText.substring(0, 30) + "..." : fullText;
                    setText(summary);
                    tooltip.setText(fullText);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(400);
                    tooltip.getStyleClass().add("custom-tooltip");
                    setTooltip(tooltip);
                }
            }
        };
    }

    private void setupMentorColumns() {
        mentorListView.getColumns().clear();
        mentorListView.getColumns().addAll(
                createTableColumn("Mentor Name", Mentor::getUsername),
                createTableColumn("Available Time", mentor -> formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability())),
                createTableColumn("Mentor Type", 180, mentor -> {
                    MentorType mentorType = mentor.getMentorType();
                    return mentorType != null ? mentorType.getDescription() : "Not Set";
                }),
                createMentorStatusColumn()
        );
    }

    private TableColumn<Mentor, String> createMentorStatusColumn() {
        TableColumn<Mentor, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> {
            Mentor mentor = cellData.getValue();
            String status = determineMentorStatus(mentor);
            return new SimpleStringProperty(status);
        });

        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(createStatusGraphic(status));
            }
        });

        return statusColumn;
    }

    private String determineMentorStatus(Mentor mentor) {
        if (mentor.getEndAvailability() != null && mentor.getEndAvailability().isBefore(LocalDateTime.now())) {
            return "Expired";
        }
        return mentor.isAvailable() ? "Available" : "Paired";
    }

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

    private void setupMenteeRowFactory() {
        menteeListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    getStyleClass().removeAll("mentee-selected", "mentee-even-row", "mentee-odd-row");
                } else {
                    Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
                    getStyleClass().removeAll("mentee-selected", "mentee-even-row", "mentee-odd-row");
                    if (item.equals(selectedMentee)) {
                        getStyleClass().add("mentee-selected");
                    } else {
                        getStyleClass().add(getIndex() % 2 == 0 ? "mentee-even-row" : "mentee-odd-row");
                    }
                }
            }
        });

        menteeListView.setOnMouseClicked(event -> handleSelection(menteeListView, selected -> lastSelectedMentee = selected));
    }

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
            mentorListView.refresh();
            handleSelection(mentorListView, selected -> lastSelectedMentor = selected);
        });
    }

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

    private <T> TableColumn<T, String> createTableColumn(String title, java.util.function.Function<T, String> valueExtractor) {
        return createTableColumn(title, -1, valueExtractor);
    }

    private <T> TableColumn<T, String> createTableColumn(String title, double prefWidth, java.util.function.Function<T, String> valueExtractor) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(valueExtractor.apply(cellData.getValue())));
        if (prefWidth > 0) column.setPrefWidth(prefWidth);
        return column;
    }

    private void pairSelectedMenteeAndMentor() {
        Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        if (selectedMentee == null || selectedMentor == null) {
            displaySnackbar(false, "Please select both a mentee and a mentor to proceed.");
            return;
        }

        boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
        displaySnackbar(success, success
                ? "Successfully paired!"
                : "Pairing failed: Mentor \"" + selectedMentor.getUsername() + "\" is unavailable.");

        if (success) refreshLists();
    }

    private void displaySnackbar(boolean success, String message) {
        showSnackbar(message, success);
    }

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

    private void refreshLists() {
        refreshList(menteeItems, menteeListView, adminController.getAllUnpairedMentees());
        refreshList(mentorItems, mentorListView, adminController.getAvailableMentors());
    }

    private <T> void refreshList(ObservableList<T> items, TableView<T> tableView, List<T> newData) {
        items.clear();
        items.addAll(newData);
        tableView.setItems(items);
    }

    private String formatAvailability(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "Unavailable";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}