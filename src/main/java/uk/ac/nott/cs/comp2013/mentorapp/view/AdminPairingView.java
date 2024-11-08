package uk.ac.nott.cs.comp2013.mentorapp.view;

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


public class AdminPairingView extends VBox {
    private final AdminController adminController;
    private final ListView<String> menteeListView = new ListView<>();
    private final ListView<Mentor> mentorListView = new ListView<>();
    private String lastSelectedMentee = null;
    private Mentor lastSelectedMentor = null;

    public AdminPairingView(AdminController adminController) {
        this.adminController = adminController;
        buildView();
    }

    private void buildView() {
        Image image = new Image(getClass().getResource("/pairing_image.png").toExternalForm());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        Label pairingLabel = new Label("Pairing Interface");
        pairingLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0b3d61;");
        pairingLabel.setAlignment(Pos.CENTER);

        HBox topContainer = new HBox(20, imageContainer, pairingLabel);
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.setPadding(new Insets(10));

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

        /*HBox pairingLabelContainer = new HBox(pairingLabel);
        pairingLabelContainer.setAlignment(Pos.CENTER);
        pairingLabelContainer.setPadding(new Insets(10));*/

        getChildren().addAll(topContainer, listsContainer, buttonContainer);
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);
    }

    private void setupListViewSelection() {
        menteeListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: white; -fx-text-fill: black;");
                } else {
                    setText(item);
                    if (menteeListView.getSelectionModel().getSelectedItem() != null
                            && menteeListView.getSelectionModel().getSelectedItem().equals(item)) {
                        setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: white; -fx-text-fill: black;");
                    }
                }
            }
        });

        mentorListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Mentor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: white; -fx-text-fill: black;");
                } else {
                    setText(item.toString());
                    if (mentorListView.getSelectionModel().getSelectedItem() != null
                            && mentorListView.getSelectionModel().getSelectedItem().equals(item)) {
                        setStyle("-fx-background-color: #04253d; -fx-text-fill: white; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-background-color: white; -fx-text-fill: black;");
                    }
                }
            }
        });

        menteeListView.setOnMouseClicked(event -> handleSelectionToggle(menteeListView, true));
        mentorListView.setOnMouseClicked(event -> handleSelectionToggle(mentorListView, false));
    }

    private void handleSelectionToggle(ListView<?> listView, boolean isMenteeList) {
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
    }

    private void pairSelectedMenteeAndMentor() {
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
    }

    private void refreshLists() {
        menteeListView.getItems().clear();
        adminController.getAllUnpairedMentees().forEach(mentee ->
                menteeListView.getItems().add(mentee.getUsername() + " - Request: " + mentee.getSupportRequest()));
        mentorListView.getItems().setAll(adminController.getAvailableMentors());
    }

    private Mentee getMenteeFromInfo(String menteeInfo) {
        if (menteeInfo == null) return null;
        String menteeUsername = menteeInfo.split(" - ")[0];
        return adminController.getAllUnpairedMentees().stream()
                .filter(mentee -> mentee.getUsername().equals(menteeUsername))
                .findFirst()
                .orElse(null);
    }
}