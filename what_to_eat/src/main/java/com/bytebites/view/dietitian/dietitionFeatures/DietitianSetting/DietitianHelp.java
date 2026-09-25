package com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DietitianHelp {

    public VBox getHelp() {

        VBox helpPage =
                new VBox(20);

        helpPage.setPadding(
                new Insets(20)
        );

        helpPage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // TITLE
        // ============================

        Label title =
                new Label("Help");

        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        // ============================
        // DESCRIPTION
        // ============================

        Label description =
                new Label(
                        "Get help and information about using the dietitian features."
                );

        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        // ============================
        // HELP CONTENT
        // ============================

        VBox helpBox =
                new VBox(15);

        helpBox.setPadding(
                new Insets(20)
        );

        helpBox.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        // ============================
        // PROFILE
        // ============================

        Label profileTitle =
                new Label("Profile");

        profileTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label profileText =
                new Label(
                        "You can update your name, specialization, " +
                        "experience and WhatsApp number from Profile."
                );

        profileText.setWrapText(true);

        profileText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        // ============================
        // HISTORY
        // ============================

        Label historyTitle =
                new Label("History");

        historyTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label historyText =
                new Label(
                        "Use History to check your previous " +
                        "consultation and client activities."
                );

        historyText.setWrapText(true);

        historyText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        // ============================
        // PAYMENTS
        // ============================

        Label paymentsTitle =
                new Label("Payments");

        paymentsTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label paymentsText =
                new Label(
                        "Use Payments to view your payment " +
                        "and consultation earning information."
                );

        paymentsText.setWrapText(true);

        paymentsText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        // ============================
        // SUPPORT
        // ============================

        Label supportTitle =
                new Label("Need More Help?");

        supportTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label supportText =
                new Label(
                        "If you face any problem while using the application, " +
                        "please contact the application administrator."
                );

        supportText.setWrapText(true);

        supportText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        helpBox.getChildren().addAll(
                profileTitle,
                profileText,

                historyTitle,
                historyText,

                paymentsTitle,
                paymentsText,

                supportTitle,
                supportText
        );

        // ============================
        // SCROLL
        // ============================

        ScrollPane scrollPane =
                new ScrollPane(helpBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: #0F172A;" +
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // ADD EVERYTHING
        // ============================

        helpPage.getChildren().addAll(
                title,
                description,
                scrollPane
        );

        return helpPage;
    }
}