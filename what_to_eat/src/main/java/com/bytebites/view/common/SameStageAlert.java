package com.bytebites.view.common;

import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Same-stage replacement for JavaFX Alert. */
public final class SameStageAlert {

    public enum AlertType { CONFIRMATION, INFORMATION, ERROR, WARNING }

    private final AlertType type;
    private String titleText = "";
    private String headerText = "";
    private String contentText = "";
    private ButtonType result;
    private SameStageOverlayHost.OverlayHandle overlayHandle;
    private Object loopKey;
    private boolean nestedLoopRunning;

    private String primaryColor = "#F68F32";
    private String primaryTextColor = "white";

    public SameStageAlert(AlertType type) {
        this.type = type;
    }

    public void setTitle(String title) { this.titleText = title == null ? "" : title; }
    public void setHeaderText(String header) { this.headerText = header == null ? "" : header; }
    public void setContentText(String content) { this.contentText = content == null ? "" : content; }

    public void setPrimaryColor(String color) {
        if (color != null && !color.isBlank()) {
            this.primaryColor = color;
        }
    }

    public void setPrimaryTextColor(String color) {
        if (color != null && !color.isBlank()) {
            this.primaryTextColor = color;
        }
    }
    
    public Optional<ButtonType> showAndWait() {
        VBox card = new VBox(14);
        card.setPrefWidth(430);
        card.setMaxWidth(430);
        card.setPadding(new Insets(24));
        card.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-border-color: #334155;"
        );

        String heading = !headerText.isBlank() ? headerText : titleText;
        if (!heading.isBlank()) {
            Label header = new Label(heading);
            header.setWrapText(true);
            header.setStyle("-fx-text-fill: #F8FAFC; -fx-font-size: 19px; -fx-font-weight: bold;");
            card.getChildren().add(header);
        }
        if (!contentText.isBlank()) {
            Label content = new Label(contentText);
            content.setWrapText(true);
            content.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 13px;");
            card.getChildren().add(content);
        }

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if (type == AlertType.CONFIRMATION) {
            Button cancel = button("Cancel", false);
            cancel.setOnAction(e -> finish(ButtonType.CANCEL));
            actions.getChildren().add(cancel);
        }

        Button ok = button(type == AlertType.CONFIRMATION ? "Confirm" : "OK", true);
        ok.setOnAction(e -> finish(ButtonType.OK));
        actions.getChildren().add(ok);
        card.getChildren().add(actions);

        overlayHandle = SameStageOverlayHost.show(card, null);
        if (Platform.isFxApplicationThread()) {
            loopKey = new Object();
            nestedLoopRunning = true;
            Platform.enterNestedEventLoop(loopKey);
            nestedLoopRunning = false;
            loopKey = null;
        }
        return Optional.ofNullable(result);
    }

    private Button button(String text, boolean primary) {
        Button button = new Button(text);
        button.setStyle(
                (primary ? "-fx-background-color: " + primaryColor + "; -fx-text-fill: " + primaryTextColor + ";":
                        "-fx-background-color: transparent; -fx-text-fill: #CBD5E1; -fx-border-color: #475569;") +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-border-radius: 8;" +
                "-fx-padding: 9 16; -fx-cursor: hand;"
        );
        return button;
    }

    private void finish(ButtonType value) {
        result = value;
        if (overlayHandle != null) {
            overlayHandle.close();
            overlayHandle = null;
        }
        if (nestedLoopRunning && loopKey != null) {
            Platform.exitNestedEventLoop(loopKey, result);
        }
    }
}
