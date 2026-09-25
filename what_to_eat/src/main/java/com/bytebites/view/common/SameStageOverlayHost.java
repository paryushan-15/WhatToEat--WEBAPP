package com.bytebites.view.common;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

final class SameStageOverlayHost {

    private static final String HOST_ID = "wte-same-stage-overlay-host";

    private SameStageOverlayHost() {}

    static OverlayHandle show(Node content, Window preferredOwner) {
        Scene scene = resolveScene(preferredOwner);
        if (scene == null) {
            throw new IllegalStateException("No active What To Eat scene is available for the overlay.");
        }

        StackPane host;
        Parent currentRoot = scene.getRoot();
        if (currentRoot instanceof StackPane && HOST_ID.equals(currentRoot.getId())) {
            host = (StackPane) currentRoot;
        } else {
            host = new StackPane();
            host.setId(HOST_ID);
            host.getChildren().add(currentRoot);
            scene.setRoot(host);
        }

        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("wte-modal-overlay-layer");
        overlay.setPickOnBounds(true);
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(2, 6, 23, 0.72);");
        overlay.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getTarget() == overlay) {
                event.consume();
            }
        });

        if (content instanceof Region) {
            Region region = (Region) content;
            region.setMaxWidth(Region.USE_PREF_SIZE);
            region.setMaxHeight(Region.USE_PREF_SIZE);
        }

        overlay.getChildren().add(content);
        host.getChildren().add(overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);

        return new OverlayHandle(host, overlay);
    }


    static void closeContaining(Node node) {
        Node current = node;
        while (current != null) {
            if (current instanceof StackPane && current.getStyleClass().contains("wte-modal-overlay-layer")) {
                Parent parent = current.getParent();
                if (parent instanceof StackPane) {
                    ((StackPane) parent).getChildren().remove(current);
                }
                return;
            }
            current = current.getParent();
        }
    }

    static Window resolveOwner(Window preferredOwner) {
        if (preferredOwner != null && preferredOwner.isShowing()) {
            return preferredOwner;
        }
        for (Window window : Window.getWindows()) {
            if (window.isShowing() && window.isFocused()) {
                return window;
            }
        }
        for (Window window : Window.getWindows()) {
            if (window.isShowing()) {
                return window;
            }
        }
        return null;
    }

    private static Scene resolveScene(Window preferredOwner) {
        Window window = resolveOwner(preferredOwner);
        return window == null ? null : window.getScene();
    }

    static final class OverlayHandle {
        private final StackPane host;
        private final StackPane overlay;
        private boolean closed;

        OverlayHandle(StackPane host, StackPane overlay) {
            this.host = host;
            this.overlay = overlay;
        }

        void close() {
            if (closed) return;
            closed = true;
            host.getChildren().remove(overlay);
        }
    }
}
