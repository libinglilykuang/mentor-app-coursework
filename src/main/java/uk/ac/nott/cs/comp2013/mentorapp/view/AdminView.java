package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public AdminView() {
        HashMapRepository<User, String> userRepository = new HashMapRepository<>();
        userRepository.insert(new Mentee("mentee1", "password1"));
        userRepository.insert(new Mentor("mentor1", "password2"));
        adminController = new AdminController(userRepository);

        pairingView = new AdminPairingView(adminController);
        buildView();
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
}
