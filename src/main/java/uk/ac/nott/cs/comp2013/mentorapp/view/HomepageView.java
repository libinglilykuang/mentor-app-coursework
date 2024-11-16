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
        // CSS Style for background image
        this.setStyle(
                "-fx-background-image: url('/university.jpg');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Title
        Text titleText = new Text("Welcome to the Mentor App");
        titleText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: white;");
        titleText.setTextAlignment(TextAlignment.CENTER);

        Text titleLine1 = new Text("INSPIRE, LEAD, AND TRANSFORM");
        titleLine1.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-fill: white;");

        Text titleLine2 = new Text("GUIDE A NEW GENERATION");
        titleLine2.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-fill: white;");

        Text titleLine3 = new Text("FIND YOUR SUPPORT SYSTEM");
        titleLine3.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-fill: white;");

        Text subtitle = new Text("BECOME A BEACON FOR FUTURE TALENT.");
        subtitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: white;");
        subtitle.setTextAlignment(TextAlignment.LEFT);

        // Button
        Button verifyButton = new Button("Sign up");
        verifyButton.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 30px; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2); "+
                        "-fx-cursor: hand;"
        );
        verifyButton.setOnAction(event -> onVerifyIdentity.run());
        VBox.setMargin(verifyButton, new Insets(40, 0, 0, 0));

        // Layout
        VBox contentBox = new VBox(15, titleLine1, titleLine2,titleLine3, subtitle,verifyButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20, 0, 0, 50));

        // Add content to the StackPane
        this.getChildren().add(contentBox);

        playSlideInAnimation(contentBox);
    }

    private void playSlideInAnimation(VBox contentBox) {
        int delayIndex = 0;
        for (var node : contentBox.getChildren()) {
            // Apply slide-in animation to each child node
            slideInFromBottom(node, delayIndex * 0.2);
            delayIndex++;
        }
    }

    private void slideInFromBottom(javafx.scene.Node node, double delay) {
        // Set the node's initial state
        node.setTranslateY(50); // Start below its original position
        node.setOpacity(0); // Start fully transparent

        // Create a TranslateTransition for the sliding effect
        TranslateTransition slideUp = new TranslateTransition(Duration.seconds(1), node);
        slideUp.setFromY(50); // Start 50px below
        slideUp.setToY(0); // End at the original position

        // Create a FadeTransition for the fading effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), node);
        fadeIn.setFromValue(0); // Start fully transparent
        fadeIn.setToValue(1); // End fully visible

        // Combine the two transitions
        ParallelTransition animation = new ParallelTransition(slideUp, fadeIn);
        animation.setDelay(Duration.seconds(delay)); // Add a delay for staggered animation
        animation.play();
    }

}
