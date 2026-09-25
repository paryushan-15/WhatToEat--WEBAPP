package com.bytebites.view.login;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen extends Application {

    private PauseTransition delay;

    @Override
    public void start(Stage stage) throws Exception {

        URL imageUrl = getClass().getResource(
                "/assets/images/splash.png"
        );

        // Open the login page immediately if the image is missing.
        if (imageUrl == null) {
            System.err.println("Splash image not found.");
            new MainLoginPage().start(stage);
            return;
        }

        Image image = new Image(imageUrl.toExternalForm());

        if (image.isError()) {
            System.err.println("Could not load splash image.");
            new MainLoginPage().start(stage);
            return;
        }

        ImageView imageView = new ImageView(image);
        imageView.setSmooth(true);

        StackPane root = new StackPane(imageView);
        root.setStyle("-fx-background-color: #0F172A;");

        Scene splashScene = new Scene(root, 1550, 830);

        // Resize the image with the window without stretching it.
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());

        stage.setTitle("WHAT TO EAT?");
        stage.setScene(splashScene);
        stage.show();

        // Show the image for five seconds, then open the login page.
        delay = new PauseTransition(Duration.seconds(5));

        delay.setOnFinished(event -> {
            try {
                new MainLoginPage().start(stage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        delay.play();
    }

    @Override
    public void stop() {
        if (delay != null) {
            delay.stop();
        }
    }
}