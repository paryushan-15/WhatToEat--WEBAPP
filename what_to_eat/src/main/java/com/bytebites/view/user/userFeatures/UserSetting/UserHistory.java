package com.bytebites.view.user.userFeatures.UserSetting;

import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class UserHistory {

    public VBox getHistory() {

        VBox historyPage =
                new VBox(20);

        historyPage.setPadding(
                new Insets(20)
        );

        historyPage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // TITLE
        // ============================

        Label title =
                new Label("History");

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
                        "View your previous meal and food activities."
                );

        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        // ============================
        // CALENDAR
        // ============================

        Label calendarTitle =
                new Label("Select a Date");

        calendarTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        DatePicker datePicker =
                new DatePicker();

        datePicker.setPrefWidth(300);
        datePicker.setPrefHeight(40);

        // ============================
        // HISTORY RESULT
        // ============================

        VBox resultBox =
                new VBox(10);

        resultBox.setPadding(
                new Insets(20)
        );

        resultBox.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        Label resultTitle =
                new Label("Meal History");

        resultTitle.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label result =
                new Label(
                        "Select a date to view your meal history."
                );

        result.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        resultBox.getChildren().addAll(
                resultTitle,
                result
        );

        datePicker.setOnAction(e -> {

            if (datePicker.getValue() != null) {

                result.setText(
                        "Meal history for "
                                + datePicker.getValue()
                                + "\n\n"
                                + "No meal history available for this date."
                );
            }
        });

        // ============================
        // SCROLL
        // ============================

        ScrollPane scrollPane =
                new ScrollPane(resultBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: #0F172A;" +
                "-fx-background-color: #0F172A;"
        );

        historyPage.getChildren().addAll(
                title,
                description,
                calendarTitle,
                datePicker,
                scrollPane
        );

        return historyPage;
    }
}