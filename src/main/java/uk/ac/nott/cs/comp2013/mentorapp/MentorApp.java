package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Administrator;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;

import java.time.LocalDateTime;

public class MentorApp extends Application {
  private HashMapRepository<User, String> repo;

  public static void main(String[] args) {
    Application.launch(args);
  }

  private void createRepository() {
    repo = new HashMapRepository<>();
    repo.insert(new Mentee("mentee1", "1"));
    repo.insert(new Mentee("mentee2", "2","Review CV"));
    repo.insert(new Mentor("mentor1", "3"));
    repo.insert(new Mentor("mentor2", "3",LocalDateTime.of(2024, 11, 5, 9, 0),
            LocalDateTime.of(2024, 11, 25, 17, 0)));
    repo.insert(new Mentor("mentor3", "3",LocalDateTime.of(2025, 11, 5, 9, 0),
            LocalDateTime.of(2025, 11, 25, 17, 0)));
    repo.insert(new Administrator("admin1", "4"));
    //return repo;
  }

  private LoginView createLoginView() {
    //LoginController controller = new LoginController(createRepository());
    //LoginView loginView = new LoginView(controller);
    LoginController controller = new LoginController(repo);
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
    createRepository();
    ViewManager sm = new ViewManager(stage);

    sm.addView(ViewManager.LOGIN, createLoginView());

    Mentee mentee = (Mentee) repo.selectById("mentee1").orElse(null);
    MenteeController menteeController = new MenteeController(mentee);
    menteeController.requestSupport(SupportType.CV_REVIEW);
    MenteeView menteeView = new MenteeView(menteeController);
    sm.addView(ViewManager.MENTEE, menteeView);

    //sm.addView(ViewManager.MENTEE, new MenteeView());
    sm.addView(ViewManager.MENTOR, new MentorView());
    //sm.addView(ViewManager.ADMIN, new AdminView());
    AdminController adminController = new AdminController(repo);
    sm.addView(ViewManager.ADMIN, new AdminView(adminController));

    sm.setStageView(ViewManager.LOGIN);
    stage.setTitle("Mentor App");
    stage.show();
  }
}
