package com.bytebites.view.common;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * Drop-in replacement for feature-level popup Stages.
 * It keeps the supplied content inside the currently visible application stage.
 */
public final class SameStageWindow {

    private static final List<SameStageWindow> ACTIVE_WINDOWS = new ArrayList<>();

    private Window owner;
    private Node content;
    private SameStageOverlayHost.OverlayHandle overlayHandle;
    private Object loopKey;
    private boolean nestedLoopRunning;

    public SameStageWindow() {}

    public void initModality(Modality modality) {
        // The in-stage overlay is modal by construction because it intercepts mouse input.
    }

    public void initOwner(Window owner) {
        this.owner = owner;
    }

    public Window getOwner() {
        return SameStageOverlayHost.resolveOwner(owner);
    }

    public void setTitle(String title) {
        // Titles are rendered by the overlay card itself; no second native window is created.
    }

    public void setScene(Scene scene) {
        if (scene == null) {
            content = null;
            return;
        }
        content = scene.getRoot();
        // Detach the content from the temporary Scene so it can join the real app scene graph.
        scene.setRoot(new Group());
    }

    public void show() {
        if (content == null || overlayHandle != null) return;
        overlayHandle = SameStageOverlayHost.show(content, owner);
        if (!ACTIVE_WINDOWS.contains(this)) {
            ACTIVE_WINDOWS.add(this);
        }
    }

    public void showAndWait() {
        show();
        if (!Platform.isFxApplicationThread() || overlayHandle == null) return;
        loopKey = new Object();
        nestedLoopRunning = true;
        Platform.enterNestedEventLoop(loopKey);
        nestedLoopRunning = false;
        loopKey = null;
    }

    public static void closeContaining(Node node) {
        for (SameStageWindow window : new ArrayList<>(ACTIVE_WINDOWS)) {
            if (isAncestorOrSelf(window.content, node)) {
                window.close();
                return;
            }
        }
        SameStageOverlayHost.closeContaining(node);
    }

    private static boolean isAncestorOrSelf(Node ancestor, Node node) {
        Node current = node;
        while (current != null) {
            if (current == ancestor) return true;
            current = current.getParent();
        }
        return false;
    }

    public void close() {
        if (overlayHandle != null) {
            overlayHandle.close();
            overlayHandle = null;
        }
        ACTIVE_WINDOWS.remove(this);
        if (nestedLoopRunning && loopKey != null) {
            Platform.exitNestedEventLoop(loopKey, null);
        }
    }
}
