package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    ImageView logoView = null;
    try {
      Image logo = new Image(getClass().getResourceAsStream("/university-logo.png"));
      logoView = new ImageView(logo);
      logoView.setFitWidth(350);
      logoView.setFitHeight(350);
      logoView.setPreserveRatio(true);
    } catch (NullPointerException e) {
      logoView = new ImageView();
    }
    VBox logoContainer = new VBox(logoView);
    logoContainer.setAlignment(Pos.CENTER);

    Label lblWelcome = new Label("Welcome to the University of Nottingham's CS Mentoring App");
    lblWelcome.setStyle("-fx-text-fill: #4F4F4F; -fx-font-size: 14; -fx-font-weight: normal;-fx-font-weight: bold;");
    lblWelcome.setWrapText(true);
    lblWelcome.setMaxWidth(500);

    Label lblTitle = new Label("Login to Mentoring System");
    lblTitle.setStyle(
            "-fx-background-color: #003366;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;"
    );
    lblTitle.setMaxWidth(400);
    lblTitle.setMinHeight(25);
    lblTitle.setAlignment(Pos.BASELINE_LEFT);

    Label lblUsername = new Label("Username");
    lblUsername.setStyle("-fx-text-fill:#6C6C6C;-fx-font-size: 12; -fx-font-weight: bold;");
    txtUsername = new TextField();
    txtUsername.setMaxWidth(400);
    txtUsername.setMinHeight(40);
    VBox usernameBox = new VBox(5, lblUsername, txtUsername);
    usernameBox.setMaxWidth(400);

    Label lblPassword = new Label("Password");
    lblPassword.setStyle("-fx-text-fill:#6C6C6C;-fx-font-size: 12; -fx-font-weight: bold;");
    txtPassword = new PasswordField();
    txtPassword.setMaxWidth(400);
    txtPassword.setMinHeight(40);
    VBox passwordBox = new VBox(5, lblPassword, txtPassword);
    passwordBox.setMaxWidth(400);

    btnLogin = new Button("Login");
    btnLogin.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-size: 12;");
    btnLogin.setMaxWidth(400);
    btnLogin.setMinHeight(40);
    btnLogin.setOnAction(e -> onLoginClickAction());

    lblMessage = new Label();
    lblMessage.setStyle("-fx-text-fill: red;");

    btnLogin.setOnAction(e -> onLoginClickAction());

    clearMessageOnTextChange();

    VBox container = new VBox(15, logoContainer, lblWelcome, lblTitle, usernameBox, passwordBox, btnLogin, lblMessage);
    container.setAlignment(Pos.CENTER);
    setUnifiedBorderStyle(container);
    container.setMaxWidth(500);

    setSpacing(10);
    setAlignment(Pos.CENTER);
    setStyle("-fx-padding: 20; -fx-background-color: white;");
    getChildren().add(container);
  }

  private void setUnifiedBorderStyle(Region region) {
    region.setStyle(
            "-fx-background-color: #FFFFFF; " +
                    "-fx-border-color: #DDDDDD; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 10px; " +
                    "-fx-background-radius: 10px; " +
                    "-fx-padding: 20px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.5, 0, 4);"
    );
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