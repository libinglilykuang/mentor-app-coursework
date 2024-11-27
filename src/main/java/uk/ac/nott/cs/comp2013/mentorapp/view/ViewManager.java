package uk.ac.nott.cs.comp2013.mentorapp.view;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import javafx.scene.layout.Pane;

/**
 * Manages the scenes available in the application and controls which scene is current presented in
 * the stage.
 */
public class ViewManager {

  public static final String LOGIN = "login_view";
  public static final String MENTEE = "mentee_view";
  public static final String MENTOR = "mentor_view";
  public static final String ADMIN = "admin_view";
  public static final String IDENTITY_SELECTION = "identity_selection";
  private final Stage stage;
  private final Map<String, Parent> scenes = new HashMap<>();

  /**
   * Construct a new manager.
   *
   * @param stage the stage used to display this manager's scenes
   */
  public ViewManager(Stage stage) {
    this.stage = stage;
    Repository<User, String> menteeRepository = new HashMapRepository<>();
    Repository<User, String> mentorRepository = new HashMapRepository<>();
    Repository<User, String> userRepository = new HashMapRepository<>();
    LoginController loginController = new LoginController(userRepository);
    addView(IDENTITY_SELECTION, new IdentitySelectionView(this::handleIdentitySelection));
    addView(LOGIN, new LoginView(loginController));
    addView(MENTEE, new MenteeView(new MenteeController(menteeRepository, null)));
    addView(MENTOR, new MentorView(new MentorController(mentorRepository, null)));
    addView(ADMIN, new AdminView(new AdminController(userRepository)));
  }

  /**
   * Add a new view to the manager.
   *
   * @param key unique key to identify the scene
   * @param s   view
   */
  public void addView(String key, Parent s) {
    scenes.put(key, s);
    if (s instanceof ManagedView mv) {
      mv.setOnViewChange(e -> setStageView(e.getView()));
    }
  }

  /**
   * Set which view should be displayed on the stage. Will do nothing if the given key does not
   * exist.
   *
   * @param key key of scene to display
   */
  public void setStageView(String key) {
    if (!scenes.containsKey(key)) {
      System.out.println("View not found for key: " + key);
      return;
    }
    Parent view = scenes.get(key);
    if (view.getScene() != null) {
      view.getScene().setRoot(new Pane());
    }
    Scene currentScene = stage.getScene();
    if (currentScene == null) {
      currentScene = new Scene(view, stage.getWidth(), stage.getHeight());
      stage.setScene(currentScene);
    } else {
      currentScene.setRoot(view);
    }
    stage.setMaximized(true);
    System.out.println("Switched to view: " + key);
  }

  private void handleIdentitySelection(String userType) {
    switch (userType) {
      case "MENTEE" -> setStageView(MENTEE);
      case "MENTOR" -> setStageView(MENTOR);
      case "ADMIN" -> setStageView(ADMIN);
      default -> System.err.println("Invalid user type selected: " + userType);
    }
  }

}

