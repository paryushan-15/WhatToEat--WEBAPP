package com.bytebites.view.common;

import java.util.Optional;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * JavaFX Dialog-style API backed by a card overlay inside the existing stage.
 */
public class SameStageDialog<R> {

    private final DialogPane dialogPane = new DialogPane();
    private Function<ButtonType, R> resultConverter;
    private R result;
    private SameStageOverlayHost.OverlayHandle overlayHandle;
    private Object loopKey;
    private boolean nestedLoopRunning;
    private Window owner;
    private Modality modality = Modality.NONE;

    public SameStageDialog() {
        dialogPane.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-border-color: #334155;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;" +
                "-fx-padding: 8;"
        );
    }

    public void setTitle(String title) {
        // No native title bar; callers normally render their own heading in the content.
    }

    public void setHeaderText(String headerText) {
        dialogPane.setHeaderText(headerText);
    }

    public DialogPane getDialogPane() {
        return dialogPane;
    }

    public void setResultConverter(Function<ButtonType, R> converter) {
        this.resultConverter = converter;
    }

    public Window getOwner() {
        return SameStageOverlayHost.resolveOwner(owner);
    }

    public void initOwner(Window owner) {
        this.owner = owner;
    }

    public void initModality(Modality modality) {
        this.modality = modality == null ? Modality.NONE : modality;
    }

    public Modality getModality() {
        return modality;
    }

    public Optional<R> showAndWait() {
        result = null;
        attachButtons();
        overlayHandle = SameStageOverlayHost.show(dialogPane, owner);

        if (Platform.isFxApplicationThread()) {
            loopKey = new Object();
            nestedLoopRunning = true;
            Platform.enterNestedEventLoop(loopKey);
            nestedLoopRunning = false;
            loopKey = null;
        }
        return Optional.ofNullable(result);
    }

    public void close() {
        if (overlayHandle != null) {
            overlayHandle.close();
            overlayHandle = null;
        }
        if (nestedLoopRunning && loopKey != null) {
            Platform.exitNestedEventLoop(loopKey, result);
        }
    }

    @SuppressWarnings("unchecked")
    private void attachButtons() {
        for (ButtonType type : dialogPane.getButtonTypes()) {
            ButtonBase button = (ButtonBase) dialogPane.lookupButton(type);
            if (button == null) continue;
            button.addEventHandler(ActionEvent.ACTION, event -> {
                if (event.isConsumed()) return;
                if (resultConverter != null) {
                    result = resultConverter.apply(type);
                } else {
                    try {
                        result = (R) type;
                    } catch (ClassCastException ignored) {
                        result = null;
                    }
                }
                close();
            });
        }
    }
}
