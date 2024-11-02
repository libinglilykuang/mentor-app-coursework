package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Administrator;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;

public class MentorApp extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  private HashMapRepository<User, String> createRepository() {
    HashMapRepository<User, String> repo = new HashMapRepository<>();
    repo.insert(new Mentee("mentee1", "password1"));
    repo.insert(new Mentor("mentor1", "password2"));
    repo.insert(new Administrator("admin1", "password3"));
    return repo;
  }

  private LoginView createLoginView() {
    LoginController controller = new LoginController(createRepository());
    LoginView loginView = new LoginView(controller);

    loginView.setOnViewChange(event -> {
      User currentUser = controller.getCurrentUser();
      if (currentUser != null) {
        String viewKey = switch (currentUser.getRole()) {
          case MENTEE -> ViewManager.MENTEE;
          case MENTOR -> ViewManager.MENTOR;
          case ADMIN -> ViewManager.ADMIN;
        };
        ViewManager viewManager = (ViewManager) event.getSource();
        viewManager.setStageView(viewKey);
      }
    });

    return loginView;
  }

  @Override
  public void start(Stage stage) throws Exception {
    ViewManager sm = new ViewManager(stage);

    sm.addView(ViewManager.LOGIN, createLoginView());
    sm.addView(ViewManager.MENTEE, new MenteeView());
    sm.addView(ViewManager.MENTOR, new MentorView());
    sm.addView(ViewManager.ADMIN, new AdminView());

    sm.setStageView(ViewManager.LOGIN);
    stage.setTitle("Mentor App");
    stage.show();
  }
}
