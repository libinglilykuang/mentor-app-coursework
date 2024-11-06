package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;

public class MenteeView extends VBox {
    private final MenteeController controller;

    public MenteeView(MenteeController controller) {
        this.controller = controller;
        buildView();
    }

    private void buildView() {
        Label welcomeLabel = new Label("Welcome, Mentee!");
        getChildren().add(welcomeLabel);

        ComboBox<SupportType> supportTypeCombo = new ComboBox<>();
        supportTypeCombo.getItems().addAll(SupportType.values());
        supportTypeCombo.setPromptText("Select Support Type");

        Button requestSupportButton = new Button("Request Support");
        requestSupportButton.setOnAction(e -> {
            SupportType selectedType = supportTypeCombo.getValue();
            if (selectedType != null) {
                controller.requestSupport(selectedType);
                System.out.println("Support request submitted: " + selectedType);
            }
        });

        Button btnViewActivities = new Button("Sign up for activities");
        Button btnAssignedSessions = new Button("View Assigned Sessions");

        getChildren().addAll(supportTypeCombo, requestSupportButton, btnViewActivities, btnAssignedSessions);
    }
}
