package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.util.List;

public class AdminView extends VBox {
    private AdminController adminController;
    private AdminPairingView pairingView;
    private final ListView<String> menteeListView = new ListView<>();
    private final ListView<Mentor> mentorListView = new ListView<>();
    private final Label statusLabel = new Label();

    public AdminView(AdminController adminController) {
        this.adminController = adminController;
        Button pairButton = new Button("Pair Mentor and Mentee");
        pairButton.setOnAction(e -> pairSelectedMenteeAndMentor());

        //menteeListView.getItems().addAll(adminController.getAllUnpairedMentees());
        //mentorListView.getItems().addAll(adminController.getAvailableMentors());

        refreshLists();

        getChildren().addAll(new Label("Unpaired Mentees"), menteeListView,
                new Label("Available Mentors"), mentorListView,
                pairButton, statusLabel);
    }

    private void pairSelectedMenteeAndMentor() {
        String selectedMenteeInfo = menteeListView.getSelectionModel().getSelectedItem();
        Mentee selectedMentee = getMenteeFromInfo(selectedMenteeInfo);
        Mentor selectedMentor = mentorListView.getSelectionModel().getSelectedItem();

        if (selectedMentee != null && selectedMentor != null) {
            boolean success = adminController.pairMenteeWithMentor(selectedMentee.getUsername(), selectedMentor.getUsername());
            if (success) {
                statusLabel.setText("Paired " + selectedMentee.getUsername() + " with " + selectedMentor.getUsername());
                refreshLists();
            } else {
                statusLabel.setText("Failed to pair. Mentor already has a mentee.");
            }
        } else {
            statusLabel.setText("Please select both a mentee and a mentor.");
        }
    }

    private void refreshLists() {
        menteeListView.getItems().clear();
        List<Mentee> unpairedMentees = adminController.getAllUnpairedMentees();
        for (Mentee mentee : unpairedMentees) {
            String menteeInfo = mentee.getUsername() + " - Request: " + mentee.getSupportRequest();
            menteeListView.getItems().add(menteeInfo);
        }

        mentorListView.getItems().setAll(adminController.getAvailableMentors());
    }

    public void buildView() {
        getChildren().add(new Label("Welcome, Admin!"));

        Button btnPairMentorStudent = new Button("Pair Mentor and Student");

        getChildren().add(btnPairMentorStudent);

        btnPairMentorStudent.setOnAction(e -> {
            List<Mentee> unpairedMentees = adminController.getAllUnpairedMentees();
            List<Mentor> availableMentors = adminController.getAvailableMentors();
            pairingView.updateMenteeListView(unpairedMentees);
            pairingView.updateMentorListView(availableMentors);

            getChildren().clear();
            getChildren().add(pairingView);
        });
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
