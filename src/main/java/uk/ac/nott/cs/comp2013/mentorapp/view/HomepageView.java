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

public class HomepageView extends StackPane {
    private final Runnable onVerifyIdentity;

    public HomepageView(Runnable onVerifyIdentity) {
        this.onVerifyIdentity = onVerifyIdentity;
        buildView();
    }

    private void buildView() {
        setBackgroundStyle();
        VBox contentBox = createContentBox();
        this.getChildren().add(contentBox);
        playSlideInAnimation(contentBox);
    }

    private void setBackgroundStyle() {
        this.setStyle(
                "-fx-background-image: url('/university.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );
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
        titleLine.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-fill: white;");
        return titleLine;
    }

    private Text createSubtitle() {
        Text subtitle = new Text("BECOME A BEACON FOR FUTURE TALENT.");
        subtitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: white;");
        subtitle.setTextAlignment(TextAlignment.LEFT);
        return subtitle;
    }

    private Button createVerifyButton() {
        Button verifyButton = new Button("Sign up");
        verifyButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2); " +
                        "-fx-cursor: hand;"
        );
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
