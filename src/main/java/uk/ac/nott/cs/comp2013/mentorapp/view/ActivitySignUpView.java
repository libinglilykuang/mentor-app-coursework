package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import java.net.URL;
import java.util.Objects;

/**
 * The ActivitySignUpView class provides a user interface for mentees to browse and sign up for activities.
 * It features search and filter functionalities for easier activity selection and supports submitting activity requests.
 */
public class ActivitySignUpView extends VBox {
    private final MenteeController menteeController;
    private final ObservableList<SupportType> activityList;
    private final ListView<SupportType> listView;
    private final TextField searchField;

    /**
     * Constructor to initialize the ActivitySignUpView.
     *
     * @param controller The mentee controller to handle actions.
     */
    public ActivitySignUpView(MenteeController controller) {
        this.menteeController = controller;
        this.activityList = FXCollections.observableArrayList(SupportType.values());
        this.listView = new ListView<>(activityList);
        this.searchField = new TextField();
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/activity_signup.css")).toExternalForm());
        buildView(); // Builds the UI layout
    }

    /**
     * Builds the main layout of the view.
     */
    private void buildView() {
        HBox topContainer = createTopContainer();
        VBox headerBox = createHeaderBox();
        configureListView();
        Button submitButton = createSubmitButton();

        getStyleClass().add("root");
        getChildren().addAll(topContainer, headerBox, listView, submitButton);
    }

    /**
     * Creates the top navigation container for navigation buttons and user profile.
     *
     * @return A styled HBox containing navigation buttons and profile details.
     */
    private HBox createTopContainer() {
        ImageView imageView = createHeaderImage();
        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.TOP_LEFT);
        imageContainer.setPadding(new Insets(10));

        // Navigation labels
        Label signingLabel = createStyledLabel("Signing up for Activities", "active-label");
        Label viewMentorLabel = createStyledLabel("View Assigned Mentor", "inactive-label");
        Label menteeHomeLabel = createStyledLabel("Home", "inactive-label");

        // Add navigation actions
        menteeHomeLabel.setOnMouseClicked(e -> getScene().setRoot(new MenteeView(menteeController)));
        viewMentorLabel.setOnMouseClicked(e -> getScene().setRoot(new AssignedMentorView(menteeController)));

        HBox labelContainer = new HBox(20, menteeHomeLabel, signingLabel, viewMentorLabel);
        labelContainer.setAlignment(Pos.CENTER_LEFT);

        HBox profileBox = createUserProfileButton();
        HBox.setHgrow(profileBox, Priority.ALWAYS);

        HBox topContainer = new HBox(20, imageContainer, labelContainer, profileBox);
        topContainer.getStyleClass().add("top-container");
        return topContainer;
    }

    /**
     * Creates the header image for the top section of the view.
     *
     * @return A styled ImageView displaying the header logo.
     */
    private ImageView createHeaderImage() {
        URL imageUrl = getClass().getResource("/pairing_image.png");
        if (imageUrl == null) {
            throw new IllegalArgumentException("Image resource not found: " + "/pairing_image.png");
        }
        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
        imageView.setFitWidth(170);
        imageView.setFitHeight(170);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    /**
     * Creates the header box containing the title, search field, and filters for activities.
     *
     * @return A VBox with the header content.
     */
    private VBox createHeaderBox() {
        Label titleLabel = createStyledLabel("Sign up for Activities", "header-title");
        HBox filterBox = createFilterBox();
        searchField.setPromptText("Search Activities..."); 
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterActivities(newValue));
        searchField.getStyleClass().add("search-field");

        VBox headerBox = new VBox(15, titleLabel, filterBox, searchField);
        headerBox.getStyleClass().add("header-box");
        return headerBox;
    }

    /**
     * Creates a styled label.
     */
    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Creates the user profile button in the header.
     */
    private HBox createUserProfileButton() {
        ImageView userIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/user_icon.png"))));
        userIcon.setFitWidth(30);
        userIcon.setPreserveRatio(true);

        Label usernameLabel = new Label(menteeController.getMentee().getUsername());
        usernameLabel.getStyleClass().add("username-label");

        HBox profileButton = new HBox(8, userIcon, usernameLabel);
        profileButton.setAlignment(Pos.CENTER_RIGHT);
        profileButton.setPadding(new Insets(5));
        profileButton.setStyle("-fx-cursor: hand;");
        profileButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) profileButton.getScene().getWindow();
            Scene currentScene = stage.getScene();
            stage.setScene(new Scene(new EditProfileView(menteeController, currentScene, false), 800, 600));
        });
        return profileButton;
    }

    /**
     * Creates filters for activity selection.
     */
    private HBox createFilterBox() {
        Label mentorLabel = new Label("Mentor:");
        mentorLabel.getStyleClass().add("mentor-label");

        ComboBox<String> adviserComboBox = new ComboBox<>();
        adviserComboBox.getItems().addAll("Any", "Alumni from the department", "Tech Industry Professional", "Second Year Student", "Third Year Student");
        adviserComboBox.setValue("Any");
        adviserComboBox.getStyleClass().add("combo-box");

        HBox mentorBox = new HBox(10, mentorLabel, adviserComboBox);
        mentorBox.setAlignment(Pos.CENTER_LEFT);
        HBox filterBox = new HBox(40, mentorBox);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        return filterBox;
    }

    /**
     * Configures the ListView for displaying activities.
     */
    private void configureListView() {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SupportType activity, boolean empty) {
                super.updateItem(activity, empty);
                if (empty || activity == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label activityTitle = new Label(activity.getDisplayName());
                    activityTitle.getStyleClass().add("activity-title");

                    Label activityDescription = new Label(activity.getDescription());
                    activityDescription.getStyleClass().add("activity-description");

                    VBox contentBox = new VBox(5, activityTitle, activityDescription);
                    contentBox.getStyleClass().add("list-cell-container");

                    setGraphic(contentBox);
                }
            }
        });
        listView.getStyleClass().add("list-view");
    }

    /**
     * Creates the submit button for signing up for an activity.
     *
     * @return A Button styled for submission.
     */
    private Button createSubmitButton() {
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("submit-button");
        submitButton.setOnAction(e -> {
            SupportType selectedType = listView.getSelectionModel().getSelectedItem();
            if (selectedType != null) {
                menteeController.requestSupport(selectedType); // Request support for selected activity
                showSnackbar("Successfully signed up for: " + selectedType.getDisplayName(), true);
            } else {
                showSnackbar("Error: Please select an activity before submitting.", false);
            }
        });
        return submitButton;
    }

    /**
     * Displays a snackbar message for feedback.
     *
     * @param message The message to display.
     * @param success True if the operation was successful, false otherwise.
     */
    private void showSnackbar(String message, boolean success) {
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add(success ? "snackbar-success" : "snackbar-error");

        Scene scene = getScene();
        if (scene == null) return;

        Pane root = (Pane) scene.getRoot();
        StackPane snackbarPane = new StackPane(messageLabel);
        snackbarPane.setAlignment(Pos.BOTTOM_CENTER);
        snackbarPane.setPadding(new Insets(20, 20, 50, 20));
        root.getChildren().add(snackbarPane);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> root.getChildren().remove(snackbarPane));
        delay.play();
    }

    /**
     * Filters the activities based on the search input.
     *
     * @param filter The filter string.
     */
    private void filterActivities(String filter) {
        if (filter == null || filter.isEmpty()) {
            listView.setItems(activityList); // Reset to full list
        } else {
            ObservableList<SupportType> filteredList = FXCollections.observableArrayList();
            for (SupportType activity : activityList) {
                if (activity.getDisplayName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredList.add(activity); // Add matching activities
                }
            }
            listView.setItems(filteredList);
        }
    }
}
