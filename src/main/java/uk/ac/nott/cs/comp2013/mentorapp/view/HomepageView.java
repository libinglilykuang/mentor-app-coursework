package uk.ac.nott.cs.comp2013.mentorapp.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.Objects;

public class HomepageView extends StackPane {
    private final Runnable onVerifyIdentity;

    public HomepageView(Runnable onVerifyIdentity) {
        this.onVerifyIdentity = onVerifyIdentity;
        buildView();

        this.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/homepage-view.css")).toExternalForm());
            }
        });
    }

    private void buildView() {
        setBackgroundStyle();
        VBox contentBox = createContentBox();
        this.getChildren().add(contentBox);
        playSlideInAnimation(contentBox);
    }

    private void setBackgroundStyle() {
        getStyleClass().add("homepage-view");
    }

    private VBox createContentBox() {
        VBox contentBox = new VBox(15,
                createTitleLine("INSPIRE, LEAD, AND TRANSFORM"),
                createTitleLine("GUIDE A NEW GENERATION"),
                createTitleLine("FIND YOUR SUPPORT SYSTEM"),
                createSubtitle(),
                createVerifyButton()
        );
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 50));
        return contentBox;
    }

    private Text createTitleLine(String text) {
        Text titleLine = new Text(text);
        titleLine.getStyleClass().add("title-line");
        return titleLine;
    }

    private Text createSubtitle() {
        Text subtitle = new Text("BECOME A BEACON FOR FUTURE TALENT.");
        subtitle.getStyleClass().add("subtitle");
        subtitle.setTextAlignment(TextAlignment.LEFT);
        return subtitle;
    }

    private Button createVerifyButton() {
        Button verifyButton = new Button("Sign up");
        verifyButton.getStyleClass().add("verify-button");
        verifyButton.setOnAction(event -> onVerifyIdentity.run());
        VBox.setMargin(verifyButton, new Insets(40, 0, 0, 0));
        return verifyButton;
    }

    private void playSlideInAnimation(VBox contentBox) {
        int delayIndex = 0;
        for (var node : contentBox.getChildren()) {
            slideInFromBottom(node, delayIndex * 0.2);
            delayIndex++;
        }
    }

    private void slideInFromBottom(javafx.scene.Node node, double delay) {
        node.setTranslateY(50);
        node.setOpacity(0);
        TranslateTransition slideUp = new TranslateTransition(Duration.seconds(1), node);
        slideUp.setFromY(50);
        slideUp.setToY(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        ParallelTransition animation = new ParallelTransition(slideUp, fadeIn);
        animation.setDelay(Duration.seconds(delay));
        animation.play();
    }
}
