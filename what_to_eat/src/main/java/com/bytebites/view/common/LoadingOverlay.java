package com.bytebites.view.common;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Reusable full-page loading overlay used by the User, Dietitian and Admin shells.
 * The overlay blocks mouse input while a page/action is being prepared.
 */
public class LoadingOverlay extends StackPane {

    private static final String BACKGROUND = "rgba(15, 23, 42, 0.88)";

    private final ProgressIndicator indicator;
    private final Label message;

    public LoadingOverlay(String accentColor) {
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: " + BACKGROUND + ";");
        setVisible(false);
        setManaged(false);
        setMouseTransparent(false);
        setPickOnBounds(true);

        indicator = new ProgressIndicator();
        indicator.setPrefSize(52, 52);
        indicator.setMaxSize(52, 52);
        indicator.setStyle("-fx-progress-color: " + accentColor + ";");

        message = new Label("Loading...");
        message.setStyle(
                "-fx-text-fill: #E2E8F0;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        VBox content = new VBox(12, indicator, message);
        content.setAlignment(Pos.CENTER);
        getChildren().add(content);
    }

    public void show(String text) {
        message.setText(text == null || text.isBlank() ? "Loading..." : text);
        setManaged(true);
        setVisible(true);
        toFront();
    }

    public void hide() {
        setVisible(false);
        setManaged(false);
    }
}
