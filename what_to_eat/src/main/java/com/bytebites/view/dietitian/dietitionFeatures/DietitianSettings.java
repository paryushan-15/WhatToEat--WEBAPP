package com.bytebites.view.dietitian.dietitionFeatures;

import com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting.DietitianAboutUs;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting.DietitianHelp;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting.DietitianHistory;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting.DietitianPayments;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting.DietitianProfile;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DietitianSettings {

    private VBox contentPane;

    public DietitianSettings() {
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


        // ============================
        // STYLE ALL BUTTONS
        // ============================

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

            DietitianProfile profile =
                    new DietitianProfile();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    profile.getProfile()
            );
        });


        // ============================
        // HISTORY BUTTON ACTION
        // ============================

        historyButton.setOnAction(e -> {

            DietitianHistory history =
                    new DietitianHistory();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    history.getHistory()
            );
        });


        // ============================
        // PAYMENTS BUTTON ACTION
        // ============================

        paymentsButton.setOnAction(e -> {

            DietitianPayments payments =
                    new DietitianPayments();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    payments.getPayments()
            );
        });


        // ============================
        // HELP BUTTON ACTION
        // ============================

        helpButton.setOnAction(e -> {

            DietitianHelp help =
                    new DietitianHelp();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    help.getHelp()
            );
        });


        // ============================
        // ABOUT US BUTTON ACTION
        // ============================

        aboutUsButton.setOnAction(e -> {

            DietitianAboutUs aboutUs =
                    new DietitianAboutUs();

            contentPane.getChildren().clear();

            contentPane.getChildren().add(
                    aboutUs.getAboutUs()
            );
        });


        // ============================
        // DEFAULT PAGE = PROFILE
        // ============================

        DietitianProfile profile =
                new DietitianProfile();

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