package com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DietitianPayments {

    public VBox getPayments() {

        VBox paymentsPage =
                new VBox(20);

        paymentsPage.setPadding(
                new Insets(20)
        );

        paymentsPage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // TITLE
        // ============================

        Label title =
                new Label("Payments");

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
                        "View your payment and consultation earning details."
                );

        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        // ============================
        // PAYMENT INFORMATION
        // ============================

        VBox paymentBox =
                new VBox(15);

        paymentBox.setPadding(
                new Insets(20)
        );

        paymentBox.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        // ============================
        // TOTAL EARNINGS
        // ============================

        Label earningsTitle =
                new Label("Total Earnings");

        earningsTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label earnings =
                new Label("No payment information available.");

        earnings.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        // ============================
        // PAYMENT HISTORY
        // ============================

        Label historyTitle =
                new Label("Payment History");

        historyTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label history =
                new Label(
                        "Your payment history will appear here."
                );

        history.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        paymentBox.getChildren().addAll(
                earningsTitle,
                earnings,
                historyTitle,
                history
        );

        // ============================
        // SCROLL
        // ============================

        ScrollPane scrollPane =
                new ScrollPane(paymentBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: #0F172A;" +
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // ADD EVERYTHING
        // ============================

        paymentsPage.getChildren().addAll(
                title,
                description,
                scrollPane
        );

        return paymentsPage;
    }
}