package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import javafx.event.ActionEvent;
import java.util.Objects;

public class EditProfileView extends VBox {
    private final MenteeController menteeController;
    private final MentorController mentorController;
    private final Scene previousScene;
    private final boolean isNewUser;

    public EditProfileView(MenteeController menteeController, Scene previousScene, boolean isNewUser) {
        this.menteeController = menteeController;
        this.mentorController = null;
        this.previousScene = previousScene;
        this.isNewUser = isNewUser;
        buildView();
    }

    public EditProfileView(MentorController mentorController, Scene previousScene, boolean isNewUser) {
        this.menteeController = null;
        this.mentorController = mentorController;
        this.previousScene = previousScene;
        this.isNewUser = isNewUser;
        buildView();
    }

    private void buildView() {
        setupContainerStyle();
        Label lblTitle = createStyledLabel("Profile");
        VBox usernameBox = createInputBox("Username:", getUsername(), "Enter your username");
        VBox passwordBox = createInputBox("Password:", getPassword(), "Enter your password");
        VBox realNameBox = createInputBox("Real Name:", getRealName(), "Real Name (optional)");
        VBox gradeBox = setupGradeBox();
        HBox buttonBox = createButtonBox(
                createButton("Save", e -> handleSave(usernameBox, passwordBox, realNameBox, gradeBox)),
                createButton("Cancel", e -> handleCancel())
        );
        VBox container = new VBox(20, lblTitle, usernameBox, passwordBox, realNameBox);
        if (gradeBox != null) container.getChildren().add(gradeBox);
        container.getChildren().add(buttonBox);
        styleContainer(container);
        getChildren().add(container);

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/edit-profile-view.css")).toExternalForm());
            }
        });
    }

    private void setupContainerStyle() {
        getStyleClass().add("edit-profile-view");
    }

    /**
     * Creates a labeled input box.
     *
     * @param label The label text.
     * @param value The initial value for the text field.
     * @param placeholder The placeholder text.
     * @return A VBox containing the label and text field.
     */
    private VBox createInputBox(String label, String value, String placeholder) {
        Label lbl = createStyledLabel(label);
        TextField txtField = createStyledTextField(value, placeholder);
        return createCenteredBox(lbl, txtField);
    }

    /**
     * Creates a grade selection box for mentees.
     */
    private VBox setupGradeBox() {
        if (menteeController == null) return null;
        Label lblGrade = createStyledLabel("Select Your Grade:");
        ComboBox<MentorType> gradeComboBox = new ComboBox<>();
        gradeComboBox.getItems().addAll(MentorType.values());
        gradeComboBox.setStyle("-fx-font-size: 12px; -fx-border-color: #DDDDDD; -fx-border-radius: 5px; -fx-padding: 5px;");
        gradeComboBox.setPromptText("Choose your grade");
        gradeComboBox.setMaxWidth(400);
        MentorType currentGrade = menteeController.getMentee().getPreferredMentorType();
        if (currentGrade != null) gradeComboBox.setValue(currentGrade);
        gradeComboBox.setOnAction(e -> menteeController.getMentee().setPreferredMentorType(gradeComboBox.getValue()));
        return createCenteredBox(lblGrade, gradeComboBox);
    }

    /**
     * Creates a button box containing Save and Cancel buttons.
     */
    private HBox createButtonBox(Button btnSave, Button btnCancel) {
        HBox buttonBox = new HBox(20, btnSave, btnCancel);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    private Button createButton(String text, EventHandler<ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        button.setOnAction(eventHandler);
        return button;
    }

    /**
     * Handles the save button action.
     */
    private void handleSave(VBox usernameBox, VBox passwordBox, VBox realNameBox, VBox gradeBox) {
        TextField txtUsername = (TextField) usernameBox.getChildren().get(1);
        TextField txtPassword = (TextField) passwordBox.getChildren().get(1);
        TextField txtRealName = (TextField) realNameBox.getChildren().get(1);
        String username = txtUsername.getText() != null ? txtUsername.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";
        String realName = txtRealName.getText() != null ? txtRealName.getText().trim() : "";
        if (!validateInputs(username, password, realName)) return;
        updateUserData(username, password, realName);
        Stage stage = (Stage) getScene().getWindow();
        if (previousScene != null) stage.setScene(previousScene);
    }

    /**
     * Validates the input fields.
     */
    private boolean validateInputs(String username, String password, String realName) {
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and Password cannot be empty.");
            return false;
        }
        if (!isPasswordValid(password)) {
            showError("Password must be at least 6 characters long and cannot contain ',', '\\n', '\\r\\n', or spaces.");
            return false;
        }
        return true;
    }

    /**
     * Updates the user data based on input.
     */
    private void updateUserData(String username, String password, String realName) {
        if (isNewUser) {
            if (menteeController != null) {
                boolean success = menteeController.getRepository().insert(new Mentee(username, password, realName)) != null;
                if (!success) {
                    showError("A user with this username already exists.");
                    return;
                }
            } else if (mentorController != null) {
                boolean success = mentorController.getRepository().insert(new Mentor(username, password, realName)) != null;
                if (!success) {
                    showError("A user with this username already exists.");
                    return;
                }
            }
            showSuccess("New user created successfully!");
        } else {
            if (menteeController != null) {
                menteeController.getMentee().setUsername(username);
                menteeController.getMentee().setPassword(password);
                menteeController.getMentee().setRealName(realName);
            } else if (mentorController != null) {
                mentorController.getMentor().setUsername(username);
                mentorController.getMentor().setPassword(password);
                mentorController.getMentor().setRealName(realName);
            }
            showSuccess("Profile updated successfully!");
        }
    }

    /**
     * Handles the cancel button action.
     */
    private void handleCancel() {
        Stage stage = (Stage) getScene().getWindow();
        if (previousScene != null) stage.setScene(previousScene);
    }

    private void styleContainer(VBox container) {
        container.getStyleClass().add("profile-container");
    }

    /**
     * Displays a success alert.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an error alert.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getUsername() {
        if (menteeController != null) {
            return menteeController.getMentee().getUsername();
        } else if (mentorController != null) {
            return mentorController.getMentor().getUsername();
        }
        return "";
    }

    private String getPassword() {
        if (menteeController != null) {
            return menteeController.getMentee().getPassword();
        } else if (mentorController != null) {
            return mentorController.getMentor().getPassword();
        }
        return "";
    }

    private String getRealName() {
        if (menteeController != null) {
            return menteeController.getMentee().getRealName();
        } else if (mentorController != null) {
            return mentorController.getMentor().getRealName();
        }
        return "";
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("input-label");
        return label;
    }

    private TextField createStyledTextField(String initialValue, String promptText) {
        TextField textField = new TextField(initialValue != null ? initialValue : "");
        textField.setPromptText(promptText);
        textField.getStyleClass().add("text-field");
        return textField;
    }

    private VBox createCenteredBox(Label label, Node field) {
        VBox box = new VBox(5, label, field);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }
        if (password.contains(",") || password.contains("\n") || password.contains("\r\n") || password.contains(" ")) {
            return false;
        }
        return true;
    }
}

