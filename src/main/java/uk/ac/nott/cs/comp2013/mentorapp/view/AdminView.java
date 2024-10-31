/*package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

public class AdminView extends VBox implements ManagedView {

    private ComboBox<Mentor> cbMentors;
    private Button btnAssignMentor;
    private final AdminController controller;
    private EventHandler<? super ViewChangeEvent> onViewChange;

    public AdminView(AdminController controller) {
        this.controller = controller;
        cbMentors = new ComboBox<>();
        btnAssignMentor = new Button("Assign Mentor");


        btnAssignMentor.setOnAction(event -> assignMentor());


        buildView();
    }

    private void buildView() {

        getChildren().addAll(cbMentors, btnAssignMentor);

    }

    private void assignMentor() {

        Mentor selectedMentor = cbMentors.getValue();
        if (selectedMentor != null) {

            controller.assignMentor(selectedMentor);
        }
    }

    @Override
    public EventHandler<? super ViewChangeEvent> getOnViewChange() {
        return onViewChange;
    }

    @Override
    public void setOnViewChange(EventHandler<? super ViewChangeEvent> eventHandler) {
        this.onViewChange = eventHandler;
    }
}
*/