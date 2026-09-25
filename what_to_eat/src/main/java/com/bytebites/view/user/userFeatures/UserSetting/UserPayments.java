package com.bytebites.view.user.userFeatures.UserSetting;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserPayments {

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
                        "View your dietitian subscription and payment history."
                );

        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        // ============================
        // CURRENT PLAN
        // ============================

        Label planTitle =
                new Label("Current Plan");

        planTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        VBox currentPlan =
                new VBox(10);

        currentPlan.setPadding(
                new Insets(20)
        );

        currentPlan.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        Label plan =
                new Label("No active payment plan");

        plan.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label expiry =
                new Label(
                        "Plan Expiry: Not available"
                );

        expiry.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        currentPlan.getChildren().addAll(
                plan,
                expiry
        );

        // ============================
        // PAYMENT HISTORY
        // ============================

        Label historyTitle =
                new Label("Payment History");

        historyTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        VBox historyBox =
                new VBox(10);

        historyBox.setPadding(
                new Insets(20)
        );

        historyBox.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        Label noPayments =
                new Label(
                        "No payment history available."
                );

        noPayments.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        historyBox.getChildren().add(
                noPayments
        );

        // ============================
        // PAYMENT INFORMATION
        // ============================

        Label information =
                new Label(
                        "Payment details will appear here after a dietitian subscription is completed."
                );

        information.setWrapText(true);

        information.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 13px;"
        );

        paymentsPage.getChildren().addAll(
                title,
                description,
                planTitle,
                currentPlan,
                historyTitle,
                historyBox,
                information
        );

        return paymentsPage;
    }
}