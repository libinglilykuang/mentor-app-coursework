package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

public class IdentitySelectionView extends VBox{

    private final IdentitySelectionHandler selectionHandler;

    public IdentitySelectionView(IdentitySelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
        buildView();
    }

    private void buildView() {
        HBox topContainer = createTopContainer();
        VBox centerContainer = createCenterContainer();
        HBox bottomContainer = createBottomContainer();

        VBox container = new VBox(10, topContainer,centerContainer,bottomContainer);
        container.setAlignment(Pos.CENTER);
        setUnifiedBorderStyle(container);
        container.setMaxWidth(500);

        setSpacing(5);
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

    private HBox createTopContainer() {
        ImageView logoView = null;
        try {
            Image logo = new Image(getClass().getResourceAsStream("/university-logo.png"));
            logoView = new ImageView(logo);
            logoView.setFitWidth(200);
            logoView.setFitHeight(200);
            logoView.setPreserveRatio(true);
        } catch (NullPointerException e) {
            logoView = new ImageView();
        }

        Label title = new Label("Student Life");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #003366;");

        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER_RIGHT);
        titleContainer.setPadding(new Insets(0, 10, 0, 10));

        HBox topContainer = new HBox(20, logoView, titleContainer);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(10, 20, 0, 20));

        return topContainer;
    }

    private VBox createCenterContainer() {
        Label subtitle = new Label("Log in as a");
        subtitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #666666;");
        subtitle.setPadding(new Insets(5, 0, 10, 0));

        HBox subtitleContainer = new HBox(subtitle);
        subtitleContainer.setAlignment(Pos.CENTER_LEFT);
        subtitleContainer.setPadding(new Insets(0, 0, 10, 10));

        Button btnMentee = createStyledButton("Student");
        btnMentee.setPrefWidth(300);
        btnMentee.setMaxWidth(600);
        btnMentee.setMinWidth(300);

        Button btnMentor = createStyledButton("Mentor");
        btnMentor.setPrefWidth(300);
        btnMentor.setMaxWidth(600);
        btnMentor.setMinWidth(300);

        Button btnAdmin = createStyledButton("Admin");
        btnAdmin.setPrefWidth(300);
        btnAdmin.setMaxWidth(600);
        btnAdmin.setMinWidth(300);

        btnMentee.setOnAction(e -> selectionHandler.onIdentitySelected("MENTEE"));
        btnMentor.setOnAction(e -> selectionHandler.onIdentitySelected("MENTOR"));
        btnAdmin.setOnAction(e -> selectionHandler.onIdentitySelected("ADMIN"));

        VBox centerContainer = new VBox(10, subtitleContainer,btnMentee, btnMentor, btnAdmin);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(20, 40, 20, 40));
        return centerContainer;
    }

    private HBox createBottomContainer() {
        Hyperlink newUserLink = new Hyperlink("New User?");
        newUserLink.setStyle("-fx-font-size: 14px; -fx-text-fill: #003366;");
        newUserLink.setOnAction(e -> {
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Repository<User, String> menteeRepository = new HashMapRepository<>();
                    Mentee newMentee = new Mentee("", "", "");
                    MenteeController newMenteeController = new MenteeController(menteeRepository,newMentee);
                    stage.setScene(new Scene(new EditProfileView(newMenteeController, stage.getScene(), true), 600, 400));
        });
        Hyperlink havingProblemsLink = new Hyperlink("Having Problems?");
        havingProblemsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: #003366;");
        havingProblemsLink.setOnAction(e -> System.out.println("Having Problems clicked"));

        HBox bottomContainer = new HBox(20, newUserLink, havingProblemsLink);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(10, 0, 20, 0));
        return bottomContainer;

    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #004586; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-padding: 10 20; -fx-background-radius: 5px;");
        button.setMaxWidth(300);
        button.setMinHeight(40);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #003366; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-padding: 10 20; -fx-background-radius: 5px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #004586; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;" +
                "-fx-padding: 10 20; -fx-background-radius: 5px;"));
        return button;
    }

    @FunctionalInterface
    public interface IdentitySelectionHandler {
        void onIdentitySelected(String userType);
    }
}
