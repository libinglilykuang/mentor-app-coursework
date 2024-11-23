package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import java.util.Objects;
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

    this.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene != null) {
        newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/login-view.css")).toExternalForm());
      }
    });
  }

  private void buildView() {
    VBox logoContainer = createLogoContainer();
    Label lblWelcome = createStyledLabel(
            "Welcome to the University of Nottingham's CS Mentoring App",
            "login-welcome",
            500
    );
    Label lblTitle = createStyledLabel(
            "Login to Mentoring System",
            "login-title",
            400
    );
    VBox usernameBox = createInputBox("Username", txtUsername = new TextField());
    VBox passwordBox = createInputBox("Password", txtPassword = new PasswordField());

    btnLogin = createStyledButton("Login", "styled-button");
    btnLogin.setOnAction(e -> onLoginClickAction());
    lblMessage = new Label();
    lblMessage.getStyleClass().add("error-message");
    clearMessageOnTextChange();

    VBox container = new VBox(15, logoContainer, lblWelcome, lblTitle, usernameBox, passwordBox, btnLogin, lblMessage);
    container.setAlignment(Pos.CENTER);
    container.getStyleClass().add("unified-border");
    container.setMaxWidth(500);
    setSpacing(10);
    setAlignment(Pos.CENTER);
    getChildren().add(container);
  }

  private VBox createLogoContainer() {
    ImageView logoView = null;
    try {
      Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/university-logo.png")));
      logoView = new ImageView(logo);
      logoView.setFitWidth(350);
      logoView.setFitHeight(350);
      logoView.setPreserveRatio(true);
    } catch (NullPointerException e) {
      logoView = new ImageView();
    }
    VBox logoContainer = new VBox(logoView);
    logoContainer.setAlignment(Pos.CENTER);
    return logoContainer;
  }

  private Label createStyledLabel(String text, String styleClass, double maxWidth) {
    Label label = new Label(text);
    label.getStyleClass().add(styleClass);
    label.setWrapText(true);
    label.setMaxWidth(maxWidth);
    return label;
  }

  private VBox createInputBox(String labelText, TextField inputField) {
    Label label = new Label(labelText);
    label.getStyleClass().add("input-label");
    inputField.setMaxWidth(400);
    inputField.setMinHeight(40);
    VBox box = new VBox(5, label, inputField);
    box.setMaxWidth(400);
    return box;
  }

  private Button createStyledButton(String text, String styleClass) {
    Button button = new Button(text);
    button.getStyleClass().add(styleClass);
    button.setMaxWidth(400);
    button.setMinHeight(40);
    return button;
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
    return null;
  }

  @Override
  public void setOnViewChange(EventHandler<? super ViewChangeEvent> eventHandler) {
    onViewChange.set(eventHandler);
  }
}
