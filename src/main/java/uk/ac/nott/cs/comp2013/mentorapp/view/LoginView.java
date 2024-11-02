package uk.ac.nott.cs.comp2013.mentorapp.view;

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

import static uk.ac.nott.cs.comp2013.mentorapp.view.ViewManager.*;
import static uk.ac.nott.cs.comp2013.mentorapp.view.ViewManager.ADMIN;

public class LoginView extends VBox implements ManagedView {

  private final LoginController controller;
  protected ObjectProperty<EventHandler<? super ViewChangeEvent>> onViewChange;
  private TextField txtUsername, txtPassword;
  private Button btnLogin;
  private Label lblMessage;

  public LoginView(LoginController controller) {
    this.controller = controller;
    this.onViewChange = new SimpleObjectProperty<>("onViewChange", null);
    buildView();
  }

  private void buildView() {
    txtUsername = new TextField();
    txtUsername.setPromptText("Enter Username");

    txtPassword = new TextField();
    //txtPassword = new PasswordField();
    txtPassword.setPromptText("Enter Password");

    btnLogin = new Button("Login");

    lblMessage = new Label();
    lblMessage.setStyle("-fx-text-fill: red;");

    btnLogin.setOnAction(e -> onLoginClickAction());

    clearMessageOnTextChange();
    setSpacing(10);
    setStyle("-fx-padding: 20;");

    getChildren().addAll(txtUsername, txtPassword, btnLogin, lblMessage);
  }

  private void onLoginClickAction() {
    boolean success = controller.onLoginClick(txtUsername.getText(), txtPassword.getText());
    if (success) {
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
  }


  private void switchToRoleView(User user) {
    String viewKey = switch (user.getRole()) {
      case MENTEE -> MENTEE;
      case MENTOR -> MENTOR;
      case ADMIN -> ADMIN;
    };
    var eh = onViewChange.get();
    if (eh != null) {
      onViewChange.get().handle(new ViewChangeEvent(viewKey));
    }
  }

  private void clearMessageOnTextChange() {
    txtUsername.textProperty().addListener((obs, oldText, newText) -> lblMessage.setText(""));
    txtPassword.textProperty().addListener((obs, oldText, newText) -> lblMessage.setText(""));
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