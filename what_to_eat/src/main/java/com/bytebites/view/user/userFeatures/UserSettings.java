package com.bytebites.view.user.userFeatures;


import com.bytebites.view.user.userFeatures.UserSetting.UserAboutUs;
import com.bytebites.view.user.userFeatures.UserSetting.UserHelp;
import com.bytebites.view.user.userFeatures.UserSetting.UserHistory;
import com.bytebites.view.user.userFeatures.UserSetting.UserPayments;
import com.bytebites.view.user.userFeatures.UserSetting.UserProfile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class UserSettings {

    private BorderPane dashboardPane;

    private VBox contentPane;

    private Runnable openFamilyAction;

        public UserSettings(
                BorderPane dashboardPane,
                Runnable openFamilyAction
        ) {
        this.dashboardPane = dashboardPane;
        this.openFamilyAction = openFamilyAction;
        }

    public VBox getSettings() {

        // ============================
        // MAIN SETTINGS PAGE
        // ============================

        VBox settingsPage = new VBox(20);

        settingsPage.setPadding(new Insets(30));

        settingsPage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // TITLE
        // ============================

        Label title = new Label("Settings");

        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 32px;" +
                "-fx-font-weight: bold;"
        );

        // ============================
        // SETTINGS MENU
        // ============================

        VBox menu = new VBox(10);

        menu.setPrefWidth(220);

        // Profile button
        Button profileButton = new Button("👤   Profile");

        // History button
        Button historyButton = new Button("📅   History");

        // Payments button
        Button paymentsButton = new Button("💳   Payments");

        // Help button
        Button helpButton = new Button("❓   Help");

        // About Us button
        Button aboutUsButton = new Button("ⓘ   About Us");

        // Style all buttons
        styleMenuButton(profileButton);
        styleMenuButton(historyButton);
        styleMenuButton(paymentsButton);
        styleMenuButton(helpButton);
        styleMenuButton(aboutUsButton);

        menu.getChildren().addAll(
                profileButton,
                historyButton,
                paymentsButton,
                helpButton,
                aboutUsButton
        );

        // ============================
        // CONTENT AREA
        // ============================

        contentPane = new VBox();

        contentPane.setPadding(new Insets(10));

        contentPane.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // HORIZONTAL SETTINGS LAYOUT
        // ============================

        HBox mainContent = new HBox(30);

        mainContent.setAlignment(Pos.TOP_LEFT);

        mainContent.getChildren().addAll(
                menu,
                contentPane
        );

        // ============================
        // PROFILE BUTTON ACTION
        // ============================

        profileButton.setOnAction(e -> {

            UserProfile profile =
                    new UserProfile(
                                dashboardPane,
                                openFamilyAction
                        );

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    profile.getProfile()
            );
        });

        // ============================
        // HISTORY BUTTON ACTION
        // ============================

        historyButton.setOnAction(e -> {

            UserHistory history =
                    new UserHistory();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    history.getHistory()
            );
        });

        // ============================
        // PAYMENTS BUTTON ACTION
        // ============================

        paymentsButton.setOnAction(e -> {

            UserPayments payments =
                    new UserPayments();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    payments.getPayments()
            );
        });

        // ============================
        // HELP BUTTON ACTION
        // ============================

        helpButton.setOnAction(e -> {

            UserHelp help =
                    new UserHelp();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    help.getHelp()
            );
        });

        // ============================
        // ABOUT US BUTTON ACTION
        // ============================

        aboutUsButton.setOnAction(e -> {

            UserAboutUs aboutUs =
                    new UserAboutUs(dashboardPane);

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    aboutUs.getAboutUs()
            );
        });

        // ============================
        // DEFAULT PAGE = PROFILE
        // ============================

        UserProfile profile =
                new UserProfile(
                        dashboardPane,
                        openFamilyAction
                );

        contentPane.getChildren().add(
                profile.getProfile()
        );

        // ============================
        // ADD EVERYTHING
        // ============================

        settingsPage.getChildren().addAll(
                title,
                mainContent
        );

        return settingsPage;
    }

    // ============================
    // MENU BUTTON STYLE
    // ============================

    private void styleMenuButton(Button button) {

        button.setPrefWidth(220);
        button.setPrefHeight(50);

        button.setAlignment(Pos.CENTER_LEFT);

        button.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 15;"
        );
    }
}