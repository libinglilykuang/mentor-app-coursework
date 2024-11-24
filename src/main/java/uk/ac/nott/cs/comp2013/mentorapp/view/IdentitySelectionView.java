package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Objects;

import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

/**
 * The IdentitySelectionView class provides the user interface for selecting
 * an identity (Student, Mentor, or Admin) when logging in.
 *
 * This view includes:
 * - A top bar with the university logo and title.
 * - Center buttons for identity selection.
 * - Bottom hyperlinks for new user creation or support.
 */
public class IdentitySelectionView extends VBox {
    private final IdentitySelectionHandler selectionHandler;

    /**
     * Constructs the IdentitySelectionView with a given handler.
     *
     * @param selectionHandler The handler for identity selection actions.
     */
    public IdentitySelectionView(IdentitySelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/identity-selection-view.css")).toExternalForm());
            }
        });
    }

    /**
     * Builds the main layout of the view.
     */
    private void buildView() {
        HBox topContainer = createTopContainer();
        VBox centerContainer = createCenterContainer();
        HBox bottomContainer = createBottomContainer();
        VBox container = new VBox(10, topContainer, centerContainer, bottomContainer);
        container.getStyleClass().add("identity-selection-view");
        setUnifiedBorderStyle(container);
        container.setMaxWidth(500);
        setSpacing(5);
        setAlignment(Pos.CENTER);
        getChildren().add(container);
    }

    /**
     * Sets a unified border style for a given region.
     */
    private void setUnifiedBorderStyle(Region region) {
        region.getStyleClass().add("unified-border");
    }

    /**
     * Creates the top bar with the university logo and title.
     */
    private HBox createTopContainer() {
        ImageView logoView = createLogoView();

        Label title = new Label("Student Life");
        title.getStyleClass().add("title");
        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER_RIGHT);
        titleContainer.setPadding(new Insets(0, 10, 0, 10));

        HBox topContainer = new HBox(20, logoView, titleContainer);
        topContainer.getStyleClass().add("top-container");

        return topContainer;
    }

    /**
     * Creates the university logo view.
     */
    private ImageView createLogoView() {
        ImageView logoView = null;
        try {
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/university-logo.png")));
            logoView = new ImageView(logo);
            logoView.setFitWidth(200);
            logoView.setFitHeight(200);
            logoView.setPreserveRatio(true);
        } catch (NullPointerException e) {
            logoView = new ImageView();
        }
        return logoView;
    }

    /**
     * Creates the center section with identity selection buttons.
     */
    private VBox createCenterContainer() {
        Label subtitle = new Label("Log in as a");
        subtitle.getStyleClass().add("subtitle");

        Button btnMentee = createStyledButton("Student");
        Button btnMentor = createStyledButton("Mentor");
        Button btnAdmin = createStyledButton("Admin");

        btnMentee.setOnAction(e -> selectionHandler.onIdentitySelected("MENTEE"));
        btnMentor.setOnAction(e -> selectionHandler.onIdentitySelected("MENTOR"));
        btnAdmin.setOnAction(e -> selectionHandler.onIdentitySelected("ADMIN"));

        VBox centerContainer = new VBox(10, subtitle, btnMentee, btnMentor, btnAdmin);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(20, 40, 20, 40));
        return centerContainer;
    }

    /**
     * Creates the bottom section with hyperlinks for new users and support.
     */
    private HBox createBottomContainer() {
        Hyperlink newUserLink = createHyperlink("New User?", e -> handleNewUserLink());
        Hyperlink havingProblemsLink = createHyperlink("Having Problems?", e -> handleHavingProblemsLink());

        HBox bottomContainer = new HBox(20, newUserLink, havingProblemsLink);
        bottomContainer.getStyleClass().add("bottom-container");

        return bottomContainer;
    }

    /**
     * Creates a styled hyperlink with an action.
     */
    private Hyperlink createHyperlink(String text, EventHandler<ActionEvent> action) {
        Hyperlink hyperlink = new Hyperlink(text);
        hyperlink.getStyleClass().add("hyperlink");
        hyperlink.setOnAction(action);
        return hyperlink;
    }

    /**
     * Creates a styled button for identity selection.
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        button.setMaxWidth(300);
        button.setMinHeight(40);
        return button;
    }

    /**
     * Handles the "New User" hyperlink click action.
     */
    private void handleNewUserLink() {
        Stage stage = (Stage) this.getScene().getWindow();
        Repository<User, String> menteeRepository = new HashMapRepository<>();
        Mentee newMentee = new Mentee("", "", "");
        MenteeController newMenteeController = new MenteeController(menteeRepository, newMentee);
        stage.setScene(new Scene(new EditProfileView(newMenteeController, stage.getScene(), true), 600, 400));
    }

    /**
     * Handles the "Having Problems" hyperlink click action.
     */
    private void handleHavingProblemsLink() {
        System.out.println("Having Problems clicked");
    }

    /**
     * Interface for handling identity selection.
     */
    @FunctionalInterface
    public interface IdentitySelectionHandler {
        void onIdentitySelected(String userType);
    }
}
