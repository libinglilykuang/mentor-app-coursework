package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        viewReportLabel.setOnMouseClicked(e -> openReportWindow(adminHomeLabel, pairingLabel, viewReportLabel));

        // Label container
        HBox labelContainer = new HBox(20, adminHomeLabel,pairingLabel, viewReportLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox topContainer = new HBox(20, imageContainer, labelContainer);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10));
        setStyle("-fx-background-color: white;");

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

        // Adjust the size of menteeListView
        menteeListView.setMinWidth(300); // Minimum width
        menteeListView.setMinHeight(450); // Minimum height
        menteeListView.setPrefWidth(350); // Preferred width
        menteeListView.setPrefHeight(550); // Preferred height

        // Adjust the size of mentorListView
        mentorListView.setMinWidth(300); // Minimum width
        mentorListView.setMinHeight(450); // Minimum height
        mentorListView.setPrefWidth(350); // Preferred width
        mentorListView.setPrefHeight(550); // Preferred height

        menteeListView.setMaxWidth(Double.MAX_VALUE);
        menteeListView.setMaxHeight(Double.MAX_VALUE);
        menteeListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        mentorListView.setMaxWidth(Double.MAX_VALUE);
        mentorListView.setMaxHeight(Double.MAX_VALUE);
        mentorListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 1);");

        HBox listsContainer = new HBox(20);
        listsContainer.setPrefWidth(Double.MAX_VALUE);

        HBox.setHgrow(menteeListView, Priority.ALWAYS);
        HBox.setHgrow(mentorListView, Priority.ALWAYS);

        Label menteeLabel = new Label("Unpaired Mentees");
        menteeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        Label mentorLabel = new Label("Available Mentors");
        mentorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");

        VBox menteeContainer = new VBox(10, menteeLabel, menteeListView);
        menteeContainer.setPadding(new Insets(10));
        menteeContainer.setAlignment(Pos.TOP_CENTER);

        VBox mentorContainer = new VBox(10, mentorLabel, mentorListView);
        mentorContainer.setPadding(new Insets(10));
        mentorContainer.setAlignment(Pos.TOP_CENTER);

        menteeContainer.setPadding(new Insets(10, 10, 10, 10));
        mentorContainer.setPadding(new Insets(10, 10, 10, 10));
        listsContainer.setSpacing(30);

        HBox.setHgrow(menteeContainer, Priority.ALWAYS);
        HBox.setHgrow(mentorContainer, Priority.ALWAYS);

        listsContainer.getChildren().addAll(menteeContainer, mentorContainer);
        setupListViewSelection();

        refreshLists();

        HBox buttonContainer = new HBox(pairButton);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefHeight(80);

        getChildren().addAll(topContainer, listsContainer, buttonContainer);
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
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

    private void openReportWindow(Label adminHomeLabel, Label pairingLabel, Label viewReportLabel) {
        // Update styles
        adminHomeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        pairingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #0b3d61; -fx-font-weight: bold; -fx-underline: false; -fx-cursor: hand;");
        viewReportLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff5722; -fx-font-weight: bold; -fx-underline: true; -fx-cursor: hand;");

        getScene().setRoot(new AssignedReportView(adminController));
    }

    private void setupListViewSelection() {
        TableColumn<Mentee, String> usernameColumn = new TableColumn<>("Mentee Name");
        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<Mentee, String> requestTypeColumn = new TableColumn<>("Request Type");
        requestTypeColumn.setCellValueFactory(cellData -> {
            Mentee mentee = cellData.getValue();
            String activityType = mentee.getSupportRequest() != null
                    ? mentee.getSupportRequest().getDisplayName()
                    : "No Request";
            return new SimpleStringProperty(activityType);
        });

        TableColumn<Mentee, String> preferredMentorTypeColumn = new TableColumn<>("Preferred Mentor Type");
        preferredMentorTypeColumn.setCellValueFactory(cellData -> {
            Mentee mentee = cellData.getValue();
            MentorType preferredType = mentee.getPreferredMentorType();
            return new SimpleStringProperty(preferredType != null ? preferredType.getDescription() : "Not Set");
        });
        preferredMentorTypeColumn.setPrefWidth(200);


        // Add columns to menteeListView
        menteeListView.getColumns().clear();
        menteeListView.getColumns().add(usernameColumn);
        menteeListView.getColumns().add(requestTypeColumn);
        menteeListView.getColumns().add(preferredMentorTypeColumn);


        // RowFactory for Mentee TableView to alternate row colors
        menteeListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
                    if (item.equals(selectedMentee)) {
                        setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        // Apply alternating row colors
                        if (getIndex() % 2 == 0) {
                            setStyle("-fx-background-color: #e8f4fc; -fx-text-fill: black;");
                        } else {
                            setStyle("-fx-background-color: white; -fx-text-fill: black;");
                        }
                    }
                }
            }
        });

        // Mouse click event for menteeListView
        menteeListView.setOnMouseClicked(event -> {
            Mentee selected = menteeListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals(lastSelectedMentee)) {
                menteeListView.getSelectionModel().clearSelection();
                lastSelectedMentee = null;
            } else {
                lastSelectedMentee = selected;
            }
            menteeListView.refresh(); // Refresh menteeListView only
        });

        // Mentor TableView setup
        TableColumn<Mentor, String> nameColumn = new TableColumn<>("Mentor Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<Mentor, String> availabilityColumn = new TableColumn<>("Available Time");
        availabilityColumn.setCellValueFactory(cellData -> {
            Mentor mentor = cellData.getValue();
            String availability = formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability());
            return new SimpleStringProperty(availability);
        });

        TableColumn<Mentor, String> mentorTypeColumn = new TableColumn<>("Mentor Type");
        mentorTypeColumn.setCellValueFactory(cellData -> {
            Mentor mentor = cellData.getValue();
            MentorType mentorType = mentor.getMentorType();
            return new SimpleStringProperty(mentorType != null ? mentorType.getDescription() : "Not Set");
        });
        mentorTypeColumn.setPrefWidth(200);

        mentorListView.getColumns().clear();
        mentorListView.getColumns().add(nameColumn);
        mentorListView.getColumns().add(availabilityColumn);
        mentorListView.getColumns().add(mentorTypeColumn);

        // RowFactory for Mentor TableView
        mentorListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(""); // Reset style for empty rows
                } else {
                    Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
                    if (item.equals(selectedMentor)) {
                        setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
                    } else {
                        // Apply alternating row colors
                        if (getIndex() % 2 == 0) {
                            setStyle("-fx-background-color: #e8f4fc; -fx-text-fill: black;");
                        } else {
                            setStyle("-fx-background-color: white; -fx-text-fill: black;");
                        }
                    }
                }
            }
        });

        mentorListView.setOnMouseClicked(event -> {
            Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();
            if (selectedMentor == null || selectedMentor.equals(lastSelectedMentor)) {
                mentorListView.getSelectionModel().clearSelection();
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selectedMentor;
            }
            mentorListView.refresh();
            menteeListView.refresh();
        });
    }

    private void pairSelectedMenteeAndMentor() {
        // Get selected mentee
        Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();

        // Get selected mentor
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        // Check if both a mentee and mentor are selected
        if (selectedMentee != null && selectedMentor != null) {
            // Perform the pairing operation
            boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            Alert alert = new Alert(AlertType.INFORMATION);

            if (success) {
                alert.setTitle("Pairing Successful");
                alert.setHeaderText("Pairing Completed");
                alert.setContentText("Mentee " + selectedMentee.getUsername() + " has been paired with Mentor " + selectedMentor.getUsername() + ".");

                // Update the lists after successful pairing
                refreshLists();
            } else {
                alert.setTitle("Pairing Failed");
                alert.setHeaderText("Pairing Unsuccessful");
                alert.setContentText("Unable to pair. Mentor " + selectedMentor.getUsername() + " is already assigned.");
            }
            alert.showAndWait();
        } else {
            // Show an error alert if either mentee or mentor is not selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select both a mentee and a mentor to proceed with pairing.");
            alert.showAndWait();
        }
    }

    private void refreshLists() {
        menteeItems.clear();
        List<Mentee> mentees = adminController.getAllUnpairedMentees();
        menteeItems.addAll(mentees);
        menteeListView.setItems(menteeItems);

        mentorItems.clear();
        List<Mentor> mentors = adminController.getAvailableMentors();
        mentorItems.addAll(mentors);
        mentorListView.setItems(mentorItems);
    }

    private String formatAvailability(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "Unavailable";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }
}