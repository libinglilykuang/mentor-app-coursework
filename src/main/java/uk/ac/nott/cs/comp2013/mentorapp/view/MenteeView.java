/*package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.Activity;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.util.List;

public class MenteeView extends VBox implements ManagedView {

    private final User user;
    private final MenteeController controller;
    private ListView<Activity> activityList;
    private Button btnSignUp;
    private Label lblMessage;

    // 用于处理视图变化事件
    protected ObjectProperty<EventHandler<? super ViewChangeEvent>> onViewChange;

    public MenteeView(User user) {
        this.user = user;
        this.controller = new MenteeController();
        this.onViewChange = new SimpleObjectProperty<>("onViewChange", null);
        buildView();
        loadActivities();
    }

    private void buildView() {
        activityList = new ListView<>();
        btnSignUp = new Button("Sign Up for Selected Activity");
        lblMessage = new Label();

        btnSignUp.setOnAction(e -> {
            Activity selectedActivity = activityList.getSelectionModel().getSelectedItem();
            if (selectedActivity != null) {
                boolean success = controller.signUpForActivity(user, selectedActivity);
                if (success) {
                    lblMessage.setText("Successfully signed up for " + selectedActivity.getName());
                } else {
                    lblMessage.setText("Failed to sign up for activity. Please try again.");
                }
            } else {
                lblMessage.setText("Please select an activity to sign up.");
            }
        });

        getChildren().addAll(new Label("Available Activities:"), activityList, btnSignUp, lblMessage);
    }

    private void loadActivities() {
        List<Activity> activities = controller.getAvailableActivities();
        activityList.getItems().addAll(activities);
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