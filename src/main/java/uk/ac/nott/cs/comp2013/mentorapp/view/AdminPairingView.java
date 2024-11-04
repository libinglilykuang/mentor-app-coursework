package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import java.util.List;

public class AdminPairingView extends VBox {
    private ListView<Mentee> menteeListView;
    private ListView<Mentor> mentorListView;
    private Button pairButton;
    private Text statusText;
    private AdminController adminController;

    public AdminPairingView(AdminController adminController) {
        this.adminController = adminController;
        buildView();
    }

    private void buildView() {
        Label menteeLabel = new Label("Mentee List");
        menteeListView = new ListView<>();

        Label mentorLabel = new Label("Mentor List");
        mentorListView = new ListView<>();

        pairButton = new Button("Pair Mentor with Mentee");
        statusText = new Text();

        VBox menteeBox = new VBox(menteeLabel, menteeListView);
        VBox mentorBox = new VBox(mentorLabel, mentorListView);
        HBox listsContainer = new HBox(menteeBox, pairButton, mentorBox);
        listsContainer.setSpacing(10);

        pairButton.setOnAction(e -> handlePairingAction());

        getChildren().addAll(new Label("Pairing Interface"), listsContainer, statusText);
    }

    private void handlePairingAction() {
        Mentee selectedMentee = menteeListView.getSelectionModel().getSelectedItem();
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        if (selectedMentee != null && selectedMentor != null) {
            boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            if (success) {
                statusText.setText("Successfully paired " + selectedMentee.getUsername() + " with " + selectedMentor.getUsername());
                menteeListView.getItems().remove(selectedMentee);

                List<Mentor> availableMentors = adminController.getAvailableMentors();
                mentorListView.getItems().setAll(availableMentors);
            } else {
                statusText.setText("Failed to pair. Mentor already has a mentee.");
            }
        } else {
            statusText.setText("Please select both a mentee and a mentor.");
        }
    }

    public void updateMenteeListView(List<Mentee> mentees) {
        menteeListView.getItems().setAll(mentees);
    }

    public void updateMentorListView(List<Mentor> mentors) {
        mentorListView.getItems().setAll(mentors);
    }
}