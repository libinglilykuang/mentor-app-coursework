/*package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.time.LocalDate;

public class MentorView extends VBox implements ManagedView {

    private User user; // The logged-in mentor
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button btnSetAvailability;
    private ListView<String> assignedSessions;

    // Property for the event handler
    private final ObjectProperty<EventHandler<? super ViewChangeEvent>> onViewChange;

    public MentorView(User user) {
        this.user = user;
        this.onViewChange = new SimpleObjectProperty<>();
        buildView();
    }

    private void buildView() {
        // Label for mentor view
        Label mentorLabel = new Label("Welcome, " + user.getUsername() + "!");

        // Date pickers for setting availability
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        btnSetAvailability = new Button("Set Availability");

        // ListView to display assigned sessions
        assignedSessions = new ListView<>();

        // Action for setting availability
        btnSetAvailability.setOnAction(e -> setAvailability());

        // Layout
        getChildren().addAll(
                mentorLabel,
                new Label("Start Availability:"),
                startDatePicker,
                new Label("End Availability:"),
                endDatePicker,
                btnSetAvailability,
                new Label("Assigned Sessions:"),
                assignedSessions
        );

        // Load assigned sessions
        loadAssignedSessions();
    }

    private void setAvailability() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        // Logic to set availability for the mentor (implement this according to your logic)
        if (start != null && end != null) {
            // Here you can call a method to save the availability in your model
            // For example: user.setStartAvailability(start.atStartOfDay());
            //              user.setEndAvailability(end.atStartOfDay());
            // You could also save this information in a database if necessary
            System.out.println("Availability set from " + start + " to " + end);
        } else {
            System.out.println("Please select both start and end dates.");
        }
    }

    private void loadAssignedSessions() {
        // Logic to load assigned sessions for the mentor (implement this according to your logic)
        // For example:
        // assignedSessions.getItems().add("Session 1");
        // assignedSessions.getItems().add("Session 2");
    }

    @Override
    public EventHandler<? super ViewChangeEvent> getOnViewChange() {
        return onViewChange.get();
    }

    @Override
    public void setOnViewChange(EventHandler<? super ViewChangeEvent> eventHandler) {
        onViewChange.set(eventHandler);
    }
}
*/