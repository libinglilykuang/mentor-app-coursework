package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MentorView extends VBox {
    public MentorView() {
        getChildren().add(new Label("Welcome, Mentor!"));

        Button btnSetAvailability = new Button("Set Availability");

        Button btnAssignedSessions = new Button("View Assigned Sessions");

        getChildren().addAll(btnSetAvailability, btnAssignedSessions);
    }
}
