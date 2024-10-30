package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.view.LoginView;
import uk.ac.nott.cs.comp2013.mentorapp.view.ViewManager;

public class MentorApp extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  private LoginView createLoginView() {
    LoginController controller = new LoginController(new HashMapRepository<>());
    return new LoginView(controller);
  }

  @Override
  public void start(Stage stage) throws Exception {
    ViewManager sm = new ViewManager(stage);

    sm.addView(ViewManager.LOGIN, createLoginView());

    sm.setStageView(ViewManager.LOGIN);
    stage.show();
  }
}
