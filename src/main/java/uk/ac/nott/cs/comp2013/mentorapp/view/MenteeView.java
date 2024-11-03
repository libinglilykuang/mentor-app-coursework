package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenteeView extends VBox {
    public MenteeView() {
        getChildren().add(new Label("Welcome, Mentee!"));

        Button btnViewActivities = new Button("Sign up for activities");

        Button btnAssignedSessions = new Button("View Assigned Sessions");

        getChildren().addAll(btnViewActivities, btnAssignedSessions);
    }
}
