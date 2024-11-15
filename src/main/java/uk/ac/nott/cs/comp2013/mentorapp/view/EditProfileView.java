package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;

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
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #f5f5f5;");

        Label lblTitle = new Label("Profile");
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label lblUsername = createStyledLabel("Username:");
        TextField txtUsername = createStyledTextField(getUsername(), "Enter your username");
        VBox usernameBox = createCenteredBox(lblUsername, txtUsername);

        Label lblPassword = createStyledLabel("Password:");
        TextField txtPassword = createStyledTextField(getPassword(), "Enter your password");
        VBox passwordBox = createCenteredBox(lblPassword, txtPassword);

        Label lblRealName = createStyledLabel("Real Name:");
        TextField txtRealName = createStyledTextField(getRealName(), "Real Name (optional)");
        VBox realNameBox = createCenteredBox(lblRealName, txtRealName);

        // Grade selection only for mentee
        VBox gradeBox = null;
        if (menteeController != null) {
            Label lblGrade = createStyledLabel("Select Your Grade:");
            ComboBox<MentorType> gradeComboBox = new ComboBox<>();
            gradeComboBox.getItems().addAll(MentorType.values());
            gradeComboBox.setStyle("-fx-font-size: 12px; -fx-border-color: #DDDDDD; -fx-border-radius: 5px; -fx-padding: 5px;");
            gradeComboBox.setPromptText("Choose your grade");
            gradeComboBox.setMaxWidth(400);

            MentorType currentGrade = menteeController.getMentee().getPreferredMentorType();
            if (currentGrade != null) {
                gradeComboBox.setValue(currentGrade);
            }

            gradeBox = createCenteredBox(lblGrade, gradeComboBox);
            gradeComboBox.setOnAction(e -> menteeController.getMentee().setPreferredMentorType(gradeComboBox.getValue()));
        }

        Button btnSave = new Button("Save");
        btnSave.setStyle("-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        btnSave.setOnAction(e -> {
            String username = txtUsername.getText() != null ? txtUsername.getText().trim() : "";
            String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";
            String realName = txtRealName.getText() != null ? txtRealName.getText().trim() : "";

            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and Password cannot be empty.");
                return;
            }

            if (!isPasswordValid(password)) {
                showError("Password must be at least 6 characters long and cannot contain ',', '\\n', '\\r\\n', or spaces.");
                return;
            }

            if (realName.isEmpty()) {
                realName = "";
            }

            // Update mentee or mentor data
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
            }
            else {
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

            Stage stage = (Stage) getScene().getWindow();
            if (previousScene != null) {
                stage.setScene(previousScene);
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setStyle("-fx-background-color: #0b3d61; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        btnCancel.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            if (previousScene != null) {
                stage.setScene(previousScene);
            }
        });

        HBox buttonBox = new HBox(20, btnSave, btnCancel);
        buttonBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(20, lblTitle, usernameBox, passwordBox, realNameBox);

        if (gradeBox != null) {
            container.getChildren().add(gradeBox);
        }
        container.getChildren().add(buttonBox);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(500);

        setUnifiedBorderStyle(container);

        getChildren().add(container);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
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
        label.setStyle("-fx-text-fill: #6C6C6C; -fx-font-size: 12px; -fx-font-weight: bold;");
        return label;
    }

    private TextField createStyledTextField(String initialValue, String promptText) {
        TextField textField = new TextField(initialValue != null ? initialValue : "");
        textField.setPromptText(promptText);
        textField.setMaxWidth(400);
        textField.setMinHeight(40);
        textField.setStyle("-fx-font-size: 12px; -fx-border-color: #DDDDDD; -fx-border-radius: 5px; -fx-padding: 10px;");
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


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
