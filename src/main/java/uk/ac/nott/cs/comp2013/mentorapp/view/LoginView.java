package uk.ac.nott.cs.comp2013.mentorapp.view;

/*import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;

public class LoginView extends VBox implements ManagedView {

  private final LoginController controller;
  protected ObjectProperty<EventHandler<? super ViewChangeEvent>> onViewChange;
  private TextField txtUsername, txtPassword;
  private Button btnLogin;

  public LoginView(LoginController controller) {
    this.controller = controller;
    this.onViewChange = new SimpleObjectProperty<>("onViewChange", null);
    buildView();
  }

  private void buildView() {
    txtUsername = new TextField();
    txtPassword = new TextField();
    btnLogin = new Button("Login");
    btnLogin.setOnAction(e -> {
      boolean success = controller.onLoginClick(txtUsername.getText(), txtPassword.getText());
      if (success) {
        var eh = onViewChange.get();
        if (eh != null) {
          onViewChange.get().handle(new ViewChangeEvent(ViewManager.DUMMY));
        }
      }
    });

    getChildren().addAll(txtUsername, txtPassword, btnLogin);
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.UserRole;

public class LoginView extends VBox implements ManagedView {

  private final LoginController controller;
  protected ObjectProperty<EventHandler<? super ViewChangeEvent>> onViewChange;
  private TextField txtUsername;
  private PasswordField txtPassword;
  private Button btnLogin;
  private Label lblMessage;

  public LoginView(LoginController controller) {
    this.controller = controller;
    this.onViewChange = new SimpleObjectProperty<>("onViewChange", null);
    buildView();
  }

  private void buildView() {
    txtUsername = new TextField();
    txtUsername.setPromptText("Username");
    txtPassword = new PasswordField();
    txtPassword.setPromptText("Password");
    btnLogin = new Button("Login");
    lblMessage = new Label();

    btnLogin.setOnAction(e -> {
      boolean loginSuccess = controller.onLoginClick(txtUsername.getText(), txtPassword.getText());
      if (loginSuccess) {
        User user = controller.getCurrentUser();
        if (user != null) {
          switchToRoleView(user);
          lblMessage.setText("");
        } else {
          lblMessage.setText("User not found. Please try logging in again.");
        }
      } else {
        lblMessage.setText("Invalid username or password.");
      }
    });


    txtUsername.textProperty().addListener((obs, oldText, newText) -> lblMessage.setText(""));
    txtPassword.textProperty().addListener((obs, oldText, newText) -> lblMessage.setText(""));

    getChildren().addAll(txtUsername, txtPassword, btnLogin, lblMessage);
  }

  private void switchToRoleView(User user) {
    if (user.getRole() == UserRole.MENTEE) {
      onViewChange.get().handle(new ViewChangeEvent("MenteeView"));
    } else if (user.getRole() == UserRole.MENTOR) {
      onViewChange.get().handle(new ViewChangeEvent("MentorView"));
    } else if (user.getRole() == UserRole.ADMIN) {
      onViewChange.get().handle(new ViewChangeEvent("AdminView"));
    }
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
