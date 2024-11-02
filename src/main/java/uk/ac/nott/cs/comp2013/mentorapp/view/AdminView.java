package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AdminView extends VBox {
    public AdminView() {
        getChildren().add(new Label("Welcome, Admin!"));

        Button btnPairMentorStudent = new Button("Pair Mentor and Student");

        getChildren().add(btnPairMentorStudent);
    }
}
