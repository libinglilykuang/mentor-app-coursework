package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AdminPairingView extends VBox {
    private final AdminController adminController;
    private final ListView<String> menteeListView = new ListView<>();
    private final TableView<Mentor> mentorListView = new TableView<>();
    //private final ListView<String> mentorListView = new ListView<>();
    // For menteeListView
    private final ObservableList<String> menteeItems = FXCollections.observableArrayList();

    // For mentorListView
    private final ObservableList<Mentor> mentorItems = FXCollections.observableArrayList();


    private String lastSelectedMentee = null;
    private Mentor lastSelectedMentor = null;

    public AdminPairingView(AdminController adminController) {
        this.adminController = adminController;
        buildView();
    }

    private void buildView() {
        Image image = new Image(getClass().getResource("/pairing_image.png").toExternalForm());
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

        // Open the report window
        AssignedReportWindow reportWindow = new AssignedReportWindow(adminController);
        reportWindow.showAndWait();
    }

    private void setupMentorTableView() {
        // Create columns
        TableColumn<Mentor, String> nameColumn = new TableColumn<>("Mentor Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));

        TableColumn<Mentor, String> availabilityColumn = new TableColumn<>("Available Time");
        availabilityColumn.setCellValueFactory(cellData -> {
            Mentor mentor = cellData.getValue();
            String availability = formatAvailability(mentor.getStartAvailability(), mentor.getEndAvailability());
            return new SimpleStringProperty(availability);
        });

        // Add columns to table
        mentorListView.getColumns().addAll(nameColumn, availabilityColumn);

        // Adjust column widths
        nameColumn.setPrefWidth(150);
        availabilityColumn.setPrefWidth(300);

        // Allow single row selection
        mentorListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Set table size
        mentorListView.setPrefHeight(550);
        mentorListView.setPrefWidth(450);

        // Style table
        mentorListView.setStyle("-fx-border-color: #0b3d61; -fx-border-width: 1px;");
    }


    /*private void setupListViewSelection () {
            // For Mentee ListView
            menteeListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: white; -fx-text-fill: black;-fx-font-size: 14px");
                    } else {
                        setText(item);

                        // Maintain existing selection logic
                        if (menteeListView.getSelectionModel().getSelectedItem() != null
                                && menteeListView.getSelectionModel().getSelectedItem().equals(item)) {
                            setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;-fx-font-size: 14px");
                        } else {
                            // Alternate background color based on index
                            if (getIndex() % 2 == 0) {
                                setStyle("-fx-background-color: #e8f4fc; -fx-text-fill: black;-fx-font-size: 14px"); // Light blue
                            } else {
                                setStyle("-fx-background-color: white; -fx-text-fill: black;-fx-font-size: 14px"); // White
                            }
                        }
                    }
                }
            });

            // For Mentor ListView
            mentorListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Mentor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: white; -fx-text-fill: black;");
                    } else {
                        setText(item.toString());

                        // Maintain existing selection logic
                        if (mentorListView.getSelectionModel().getSelectedItem() != null
                                && mentorListView.getSelectionModel().getSelectedItem().equals(item)) {
                            setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;-fx-font-size: 14px");
                        } else {
                            // Alternate background color based on index
                            if (getIndex() % 2 == 0) {
                                setStyle("-fx-background-color: #e8f4fc; -fx-text-fill: black;-fx-font-size: 14px"); // Light blue
                            } else {
                                setStyle("-fx-background-color: white; -fx-text-fill: black;-fx-font-size: 14px"); // White
                            }
                        }
                    }
                }
            });

            menteeListView.setOnMouseClicked(event -> handleSelectionToggle(menteeListView, true));
            mentorListView.setOnMouseClicked(event -> handleSelectionToggle(mentorListView, false));
        }*/

    private void setupListViewSelection() {
        // Mentee ListView setup remains unchanged
        menteeListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px;");
                } else {
                    setText(item);
                    if (menteeListView.getSelectionModel().getSelectedItem() != null
                            && menteeListView.getSelectionModel().getSelectedItem().equals(item)) {
                        setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
                    } else {
                        if (getIndex() % 2 == 0) {
                            setStyle("-fx-background-color: #e8f4fc; -fx-text-fill: black; -fx-font-size: 14px;");
                        } else {
                            setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 14px;");
                        }
                    }
                }
            }
        });

        menteeListView.setOnMouseClicked(event -> {
            String selected = menteeListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals(lastSelectedMentee)) {
                menteeListView.getSelectionModel().clearSelection();
                lastSelectedMentee = null;
            } else {
                lastSelectedMentee = selected;
            }
            menteeListView.refresh();
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

        mentorListView.getColumns().clear();
        mentorListView.getColumns().addAll(nameColumn, availabilityColumn);

        // RowFactory for Mentor TableView
        mentorListView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Mentor item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(""); // Clear previous styles
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.equals(mentorListView.getSelectionModel().getSelectedItem())) {
                        setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
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
            if (selectedMentor == null || (lastSelectedMentor != null && selectedMentor.equals(lastSelectedMentor))) {
                mentorListView.getSelectionModel().clearSelection();
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selectedMentor;
            }
            mentorListView.refresh();
        });
    }


    /*private void handleSelectionToggle(ListView<?> listView, boolean isMenteeList) {
        if (isMenteeList) {
            String selected = menteeListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals(lastSelectedMentee)) {
                menteeListView.getSelectionModel().clearSelection();
                lastSelectedMentee = null;
            } else {
                lastSelectedMentee = selected;
            }
        } else {
            Mentor selected = mentorListView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.equals(lastSelectedMentor)) {
                mentorListView.getSelectionModel().clearSelection();
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selected;
            }
        }
        listView.refresh();
    }*/

    private void handleSelectionToggle(ListView<String> menteeListView, TableView<Mentor> mentorTableView, boolean isMenteeList) {
        if (isMenteeList) {
            String selected = menteeListView.getSelectionModel().getSelectedItem();
            if (selected != null && menteeListView.getSelectionModel().getSelectedIndex() == 0) {
                menteeListView.getSelectionModel().clearSelection();
            } else if (selected != null && selected.equals(lastSelectedMentee)) {
                menteeListView.getSelectionModel().clearSelection();
                lastSelectedMentee = null;
            } else {
                lastSelectedMentee = selected;
            }
            menteeListView.refresh();
        } else {
            Mentor selectedMentor = mentorTableView.getSelectionModel().getSelectedItem();
            if (selectedMentor == null || (lastSelectedMentor != null && selectedMentor.equals(lastSelectedMentor))) {
                mentorTableView.getSelectionModel().clearSelection();
                lastSelectedMentor = null;
            } else {
                lastSelectedMentor = selectedMentor;
            }
            mentorTableView.refresh();
        }
    }


    /*private void pairSelectedMenteeAndMentor() {
        Mentee selectedMentee = getMenteeFromInfo(menteeListView.getSelectionModel().getSelectedItem());
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        if (selectedMentee != null && selectedMentor != null) {
            boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            Alert alert = new Alert(AlertType.INFORMATION);
            if (success) {
                alert.setTitle("Pairing Successful");
                alert.setHeaderText("Pairing Completed");
                refreshLists();
            } else {
                alert.setTitle("Pairing Failed");
                alert.setHeaderText("Pairing Unsuccessful");
                alert.setContentText("Unable to pair. Mentor is already assigned.");
            }
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select both a mentee and a mentor.");
            alert.showAndWait();
        }
    }*/

    private void pairSelectedMenteeAndMentor() {
        // Get selected mentee
        String selectedMenteeInfo = menteeListView.getSelectionModel().getSelectedItem();
        Mentee selectedMentee = getMenteeFromInfo(selectedMenteeInfo);

        // Get selected mentor
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        if (selectedMentee != null && selectedMentor != null) {
            boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            Alert alert = new Alert(AlertType.INFORMATION);
            if (success) {
                alert.setTitle("Pairing Successful");
                alert.setHeaderText("Pairing Completed");
                refreshLists(); // Update the lists after successful pairing
            } else {
                alert.setTitle("Pairing Failed");
                alert.setHeaderText("Pairing Unsuccessful");
                alert.setContentText("Unable to pair. Mentor is already assigned.");
            }
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selection Error");
            alert.setHeaderText("Selection Error");
            alert.setContentText("Please select both a mentee and a mentor.");
            alert.showAndWait();
        }
    }

    private void refreshLists() {
        // 更新 menteeItems
        menteeItems.clear();
        List<Mentee> mentees = adminController.getAllUnpairedMentees();
        mentees.forEach(mentee -> menteeItems.add(mentee.getUsername() + " - Request: " + mentee.getSupportRequest()));
        menteeListView.setItems(menteeItems);

        // 更新 mentorItems
        mentorItems.clear();
        List<Mentor> mentors = adminController.getAvailableMentors();
        mentorItems.addAll(mentors);
        mentorListView.setItems(mentorItems);
    }

    /*private void refreshLists() {
        menteeListView.getItems().clear();
        adminController.getAllUnpairedMentees().forEach(mentee ->
                menteeListView.getItems().add(mentee.getUsername() + " - Request: " + mentee.getSupportRequest()));
        mentorListView.getItems().setAll(adminController.getAvailableMentors());
    }*/

    private String formatAvailability(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "Unavailable";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return start.format(formatter) + " - " + end.format(formatter);
    }

    private Mentee getMenteeFromInfo(String menteeInfo) {
        if (menteeInfo == null) return null;
        String menteeUsername = menteeInfo.split(" - ")[0];
        return adminController.getAllUnpairedMentees().stream()
                .filter(mentee -> mentee.getUsername().equals(menteeUsername))
                .findFirst()
                .orElse(null);
    }

    /*private Mentor getSelectedMentor() {
        String selectedRow = mentorListView.getSelectionModel().getSelectedItem();
        if (selectedRow == null || selectedRow.startsWith("Mentor Name")) {
            // Ignore if no selection or title row is selected
            return null;
        }
        // Extract the mentor name from the selected row
        String mentorName = selectedRow.split(" \\| ")[0]; // Assuming the format is "Mentor Name | Available Time"
        return adminController.findMentorByName(mentorName);
    }*/

}