package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
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

    public AdminPairingView(AdminController adminController) {
        this.adminController = adminController;
        buildView();
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

    private VBox createTopContainer() {
        ImageView imageView = createImageView("/pairing_image.png");
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label adminHomeLabel = createLabel("Home", false);
        Label pairingLabel = createLabel("Pairing Mentors", true);
        Label viewReportLabel = createLabel("View Assigned Report", false);

        adminHomeLabel.setOnMouseClicked(e -> switchToAdminHome(adminHomeLabel, pairingLabel, viewReportLabel));
        pairingLabel.setOnMouseClicked(e -> switchToPairingView(adminHomeLabel, pairingLabel, viewReportLabel));
        viewReportLabel.setOnMouseClicked(e -> openReportWindow(adminHomeLabel, pairingLabel, viewReportLabel));

        HBox labelContainer = new HBox(20, adminHomeLabel, pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10));
        setStyle("-fx-background-color: white;");

        Separator separator = new Separator();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #cccccc;");

        VBox topContainerWithSeparator = new VBox(topContainer, separator);
        topContainerWithSeparator.setSpacing(0);

        return topContainerWithSeparator;
    }

    private ImageView createImageView(String imagePath) {
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + imagePath);
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
        pairButton.setStyle("-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        pairButton.setOnMouseEntered(e -> pairButton.setStyle(
                "-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        pairButton.setOnMouseExited(e -> pairButton.setStyle(
                "-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                        "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;"));

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
        menteeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        Label mentorLabel = new Label("All Mentors");
        mentorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        VBox menteeContainer = new VBox(10, menteeLabel, menteeListView);
        menteeContainer.setPadding(new Insets(10));
        menteeContainer.setAlignment(Pos.TOP_CENTER);

        VBox mentorContainer = new VBox(10, mentorLabel, mentorListView);
        mentorContainer.setPadding(new Insets(10));
        mentorContainer.setAlignment(Pos.TOP_CENTER);

        HBox listsContainer = new HBox(20, menteeContainer, mentorContainer);
        listsContainer.setPrefWidth(Double.MAX_VALUE);
        listsContainer.setSpacing(5);

        HBox.setHgrow(menteeContainer, Priority.ALWAYS);
        HBox.setHgrow(mentorContainer, Priority.ALWAYS);

        return listsContainer;
    }

    private void configureMenteeListView() {
        menteeListView.setMinWidth(400);
        menteeListView.setMinHeight(450);
        menteeListView.setPrefWidth(450);
        menteeListView.setPrefHeight(550);
        menteeListView.setMaxWidth(Double.MAX_VALUE);
        menteeListView.setMaxHeight(Double.MAX_VALUE);
        menteeListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");
    }

    private void configureMentorListView() {
        mentorListView.setMinWidth(200);
        mentorListView.setMinHeight(450);
        mentorListView.setPrefWidth(250);
        mentorListView.setPrefHeight(550);
        mentorListView.setMaxWidth(Double.MAX_VALUE);
        mentorListView.setMaxHeight(Double.MAX_VALUE);
        mentorListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");
    }

    private Label createLabel(String text, boolean isActive) {
        Label label = new Label(text);
        label.setStyle(isActive ? "-fx-font-size: 16px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;"
                : "-fx-font-size: 16px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        return label;
    }

    private void switchToPairingView(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "pairing");
        System.out.println("Switched to Pairing Interface");
    }

    private void switchToAdminHome(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "home");
        getScene().setRoot(new AdminView(adminController)); // Replace the current view with AdminView
    }

    private void openReportWindow(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        updateLabelStyles(adminHomeLabel, pairingLabel, viewReportLabel, "report");
        getScene().setRoot(new AssignedReportView(adminController));
    }

    private void updateLabelStyles(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel, String activeView) {
        String activeStyle = "-fx-font-size: 14px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;";
        String inactiveStyle = "-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;";

        adminHomeLabel.setStyle(activeView.equals("home") ? activeStyle : inactiveStyle);
        pairingLabel.setStyle(activeView.equals("pairing") ? activeStyle : inactiveStyle);
        viewReportLabel.setStyle(activeView.equals("report") ? activeStyle : inactiveStyle);
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
                createTableColumn("Mentee Name", 100, mentee -> mentee.getUsername()),
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
        cvTextColumn.setCellFactory(column -> new TableCell<>() {
            private Tooltip tooltip;

            @Override
            protected void updateItem(String fullCvText, boolean empty) {
                super.updateItem(fullCvText, empty);
                if (empty || fullCvText == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String summary = fullCvText.length() > 30 ? fullCvText.substring(0, 30) + "..." : fullCvText;
                    setText(summary);

                    tooltip = new Tooltip(fullCvText);
                    tooltip.setWrapText(true);
                    tooltip.setMaxWidth(400);
                    tooltip.setStyle("-fx-font-size: 14px;");

                    setOnMouseEntered(event -> setTooltip(tooltip));
                    setOnMouseExited(event -> setTooltip(null));
                }
            }
        });
        return cvTextColumn;
    }

    private void setupMentorColumns() {
        mentorListView.getColumns().clear();

        mentorListView.getColumns().addAll(
                createTableColumn("Mentor Name", mentor -> mentor.getUsername()),
                createTableColumn("Available Time", mentor -> {
                    return formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability());
                }),
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
        statusLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        String backgroundColor = switch (status) {
            case "Available" -> "#d4edda";
            case "Paired" -> "#f5c6cb";
            case "Expired" -> "#fff3cd";
            default -> "#ffffff";
        };

        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setStyle("-fx-background-color: " + backgroundColor + "; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 5px;");

        return statusBox;
    }

    private void setupMenteeRowFactory() {
        menteeListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
                    if (item.equals(selectedMentee)) {
                        setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        setStyle(getIndex() % 2 == 0
                                ? "-fx-background-color: #e8f4fc; -fx-text-fill: black; -fx-padding: 5px;"
                                : "-fx-background-color: white; -fx-text-fill: black; -fx-padding: 5px;");
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
                    setStyle("");
                } else {
                    Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
                    if (item.equals(selectedMentor)) {
                        setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        setStyle(getIndex() % 2 == 0
                                ? "-fx-background-color: #e8f4fc; -fx-text-fill: black;"
                                : "-fx-background-color: white; -fx-text-fill: black;");
                    }
                }
            }
        });

        mentorListView.setOnMouseClicked(event -> handleSelection(mentorListView, selected -> lastSelectedMentor = selected));
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
        messageLabel.setStyle((success
                ? "-fx-background-color: #d4edda; -fx-text-fill: #155724;"
                : "-fx-background-color: #f8d7da; -fx-text-fill: #721c24;"
        ) +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-background-radius: 5px; " +
                "-fx-border-radius: 5px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.3, 0, 2);");

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