package com.bytebites.view.user.userFeatures.UserSetting;

import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.dao.UserDao;
import com.bytebites.model.FamilyMember;
import com.bytebites.model.User;
import com.bytebites.model.session.SessionManager;
import com.bytebites.view.user.userFeatures.UserFamilyProfile;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class UserProfile {

    private BorderPane dashboardPane;
    private Runnable openFamilyAction;

    public UserProfile(
                BorderPane dashboardPane,
                Runnable openFamilyAction
        ) {
        this.dashboardPane = dashboardPane;
        this.openFamilyAction = openFamilyAction;
        }

    public VBox getProfile() {

        VBox profilePage = new VBox(20);

        profilePage.setPadding(new Insets(20));

        profilePage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // ============================
        // TITLE
        // ============================

        Label title = new Label("Profile");

        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        // ============================
        // GET LOGGED-IN USER
        // ============================

        String uid = SessionManager.getUid();

        if (uid == null || uid.isEmpty()) {

            Label error = new Label(
                    "User session not found."
            );

            error.setStyle(
                    "-fx-text-fill: #EF4444;" +
                    "-fx-font-size: 16px;"
            );

            profilePage.getChildren().addAll(
                    title,
                    error
            );

            return profilePage;
        }

        UserDao userDao = new UserDao();

        User user = userDao.getUserByUid(uid);

        // ============================
        // PERSONAL INFORMATION
        // ============================

        Label personalTitle =
                new Label("Personal Information");

        personalTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        // NAME

        Label nameLabel = new Label("Name");

        nameLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;"
        );

        TextField nameField = new TextField();

        if (user != null && user.getName() != null) {
            nameField.setText(user.getName());
        }

        nameField.setPrefWidth(400);
        nameField.setPrefHeight(40);

        nameField.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #94A3B8;"
        );

        // EMAIL

        Label emailLabel = new Label("Email");

        emailLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;"
        );

        TextField emailField = new TextField();

        if (user != null && user.getEmail() != null) {
            emailField.setText(user.getEmail());
        }

        emailField.setEditable(false);

        emailField.setPrefWidth(400);
        emailField.setPrefHeight(40);

        emailField.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #94A3B8;"
        );

        // WHATSAPP NUMBER
Label whatsappLabel = new Label("WhatsApp Number");
whatsappLabel.setStyle(
        "-fx-text-fill: white;" +
        "-fx-font-size: 14px;"
);

TextField whatsappField = new TextField();

if (user != null && user.getWhatsappNumber() != null) {
    whatsappField.setText(user.getWhatsappNumber());
}

whatsappField.setPromptText("Enter WhatsApp Number");
whatsappField.setPrefWidth(400);
whatsappField.setPrefHeight(40);
whatsappField.setStyle(
        "-fx-background-color: #1E293B;" +
        "-fx-text-fill: white;" +
        "-fx-prompt-text-fill: #94A3B8;"
);

        // ============================
        // UPDATE PROFILE BUTTON
        // ============================

        Button updateButton =
                new Button("Update Profile");

        updateButton.setPrefWidth(180);
        updateButton.setPrefHeight(40);

        updateButton.setStyle(
                "-fx-background-color: #22C55E;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
        );

        Label message =
                new Label();

        message.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 14px;"
        );

        updateButton.setOnAction(e -> {

            String newName =
                    nameField.getText().trim();

         String whatsappNumber = whatsappField.getText().trim();

            if (newName.isEmpty()) {

                message.setText(
                        "Name cannot be empty."
                );

                message.setStyle(
                        "-fx-text-fill: #EF4444;" +
                        "-fx-font-size: 14px;"
                );

                return;
            }

            boolean updated =
                    userDao.updateUserName(
                            uid,
                            newName
                            
                    );

                boolean whatsappUpdated =
                    userDao.updateWhatsappNumber(
                            uid,
                            whatsappNumber
                    );

            if (updated && whatsappUpdated) {

                message.setText(
                        "Profile updated successfully."
                );

                message.setStyle(
                        "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 14px;"
                );

            } else {

                message.setText(
                        "Failed to update profile."
                );

                message.setStyle(
                        "-fx-text-fill: #EF4444;" +
                        "-fx-font-size: 14px;"
                );
            }
        });

        // ============================
        // FAMILY MEMBERS
        // ============================

        Label familyTitle =
                new Label("Family");

        familyTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );

        FamilyMemberDao familyMemberDao =
                new FamilyMemberDao();

        List<FamilyMember> familyMembers =
                familyMemberDao.getFamilyMembersByUserId(uid);

        int familyCount =
                familyMembers != null
                        ? familyMembers.size()
                        : 0;

        Label familyCountLabel =
                new Label(
                        "Family Members: " + familyCount
                );

        familyCountLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;"
        );

        // ============================
        // MANAGE FAMILY
        // ============================

        Button manageFamilyButton =
                new Button("Manage Family");

        manageFamilyButton.setPrefWidth(180);
        manageFamilyButton.setPrefHeight(40);

        manageFamilyButton.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;"
        );

        manageFamilyButton.setOnAction(e -> {

                if (openFamilyAction != null) {
                        openFamilyAction.run();
                        return;
                }

                UserFamilyProfile familyProfile =
                        new UserFamilyProfile();

                dashboardPane.setCenter(
                        familyProfile.getFamilyProfile()
                );
        });

        // ============================
        // ADD EVERYTHING
        // ============================

        VBox personalBox =
                new VBox(8);

       personalBox.getChildren().addAll(
        personalTitle,
        nameLabel,
        nameField,
        emailLabel,
        emailField,
        whatsappLabel,
        whatsappField,
        updateButton,
        message
);

        VBox familyBox =
                new VBox(10);

        familyBox.getChildren().addAll(
                familyTitle,
                familyCountLabel,
                manageFamilyButton
        );

        profilePage.getChildren().addAll(
                title,
                personalBox,
                familyBox
        );

        return profilePage;
    }
}