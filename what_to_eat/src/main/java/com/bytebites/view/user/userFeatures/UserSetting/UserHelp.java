package com.bytebites.view.user.userFeatures.UserSetting;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class UserHelp {

    public VBox getHelp() {

        VBox helpPage =
                new VBox(15);

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
                        "Frequently Asked Questions"
                );

        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;"
        );

        // ============================
        // FAQ CONTAINER
        // ============================

        VBox faqBox =
                new VBox(12);

        // 1
        addQuestion(
                faqBox,
                "1. What is What To Eat?",
                "What To Eat? is a family-focused meal planning and nutrition management application."
        );

        // 2
        addQuestion(
                faqBox,
                "2. How do I create an account?",
                "Enter your required details on the registration page and create your account."
        );

        // 3
        addQuestion(
                faqBox,
                "3. How do I login?",
                "Enter your registered email and password on the login page and click Login."
        );

        // 4
        addQuestion(
                faqBox,
                "4. Can I manage my family members?",
                "Yes. You can add and manage family members from the Family section."
        );

        // 5
        addQuestion(
                faqBox,
                "5. Can I plan meals for my family?",
                "Yes. The application provides meal planning features for family food management."
        );

        // 6
        addQuestion(
                faqBox,
                "6. What is the Weekly Meal Planner?",
                "It helps you organize meals for different days of the week."
        );

        // 7
        addQuestion(
                faqBox,
                "7. Can I save my recipes?",
                "Yes. You can use the My Recipes section to manage your saved recipes."
        );

        // 8
        addQuestion(
                faqBox,
                "8. What is Pantry & Grocery?",
                "It helps you manage pantry items and grocery requirements."
        );

        // 9
        addQuestion(
                faqBox,
                "9. Can I connect with a dietitian?",
                "Yes. Users can browse available dietitians and request a consultation."
        );

        // 10
        addQuestion(
                faqBox,
                "10. How does dietitian consultation work?",
                "You can select a dietitian and request a consultation. The consultation status can then be managed through the application."
        );

        // 11
        addQuestion(
                faqBox,
                "11. Can I manage my profile?",
                "Yes. Your name and other available profile information can be managed from Settings > Profile."
        );

        // 12
        addQuestion(
                faqBox,
                "12. Can I update my name?",
                "Yes. Open Settings > Profile, edit your name and click Update Profile."
        );

        // 13
        addQuestion(
                faqBox,
                "13. Can I see my meal history?",
                "Yes. Settings > History provides a calendar-based interface for viewing past meal activity."
        );

        // 14
        addQuestion(
                faqBox,
                "14. Where can I see my payment history?",
                "Payment information can be viewed from Settings > Payments."
        );

        // 15
        addQuestion(
                faqBox,
                "15. Can I see my current dietitian plan?",
                "The Payments section is designed to display the current subscription plan and its expiry information."
        );

        // 16
        addQuestion(
                faqBox,
                "16. What is Social Feed?",
                "Social Feed allows users to interact with food and meal-related content within the application."
        );

        // 17
        addQuestion(
                faqBox,
                "17. Is my login information stored?",
                "Authentication is handled through Firebase Authentication, while application user information is stored using Firebase services."
        );

        // 18
        addQuestion(
                faqBox,
                "18. What should I do if something is not working?",
                "Check your internet connection and try again. If the problem continues, contact the project support team."
        );

        // 19
        addQuestion(
                faqBox,
                "19. How do I return to another section?",
                "Use the navigation menu available in the user dashboard to move between application sections."
        );

        // 20
        addQuestion(
                faqBox,
                "20. Where can I learn about the project team?",
                "Open Settings > About Us to view the project, team members, instructors and mentors."
        );

        // ============================
        // SCROLL
        // ============================

        ScrollPane scrollPane =
                new ScrollPane(faqBox);

        scrollPane.setFitToWidth(true);

        scrollPane.setPrefHeight(550);

        scrollPane.setStyle(
                "-fx-background: #0F172A;" +
                "-fx-background-color: #0F172A;"
        );

        helpPage.getChildren().addAll(
                title,
                description,
                scrollPane
        );

        return helpPage;
    }

    // ============================
    // ADD FAQ
    // ============================

    private void addQuestion(
            VBox container,
            String question,
            String answer) {

        VBox box =
                new VBox(8);

        box.setPadding(
                new Insets(15)
        );

        box.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-background-radius: 10;"
        );

        Label questionLabel =
                new Label(question);

        questionLabel.setWrapText(true);

        questionLabel.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label answerLabel =
                new Label(answer);

        answerLabel.setWrapText(true);

        answerLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );

        box.getChildren().addAll(
                questionLabel,
                answerLabel
        );

        container.getChildren().add(box);
    }
}