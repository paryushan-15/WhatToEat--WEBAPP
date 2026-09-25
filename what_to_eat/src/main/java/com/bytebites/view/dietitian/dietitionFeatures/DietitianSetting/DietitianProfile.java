package com.bytebites.view.dietitian.dietitionFeatures.DietitianSetting;

import com.bytebites.dao.DietitianDao;
import com.bytebites.model.Dietitian;
import com.bytebites.model.session.SessionManager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DietitianProfile {

    private DietitianDao dietitianDao;

    public DietitianProfile() {
        dietitianDao = new DietitianDao();
    }

    // =====================================================
    // PROFILE PAGE
    // =====================================================

    public VBox getProfile() {

        String uid = SessionManager.getUid();

        // -------------------------------------------------
        // Check logged-in dietitian
        // -------------------------------------------------

        if (uid == null || uid.isEmpty()) {

            VBox errorPage = new VBox(20);

            errorPage.setPadding(
                    new Insets(35)
            );

            errorPage.setStyle(
                    "-fx-background-color: #0F172A;"
            );

            Label errorLabel = new Label(
                    "Dietitian session not found."
            );

            errorLabel.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;"
            );

            errorPage.getChildren().add(
                    errorLabel
            );

            return errorPage;
        }

        // -------------------------------------------------
        // Get current dietitian data from Firebase
        // -------------------------------------------------

        Dietitian dietitian =
                dietitianDao.getDietitianByUid(uid);

        // =================================================
        // MAIN PROFILE PAGE
        // =================================================

        VBox profilePage = new VBox(20);

        profilePage.setPadding(
                new Insets(20)
        );

        profilePage.setStyle(
                "-fx-background-color: #0F172A;"
        );

        // =================================================
        // PROFILE TITLE
        // =================================================

        Label profileTitle = new Label(
                "Profile"
        );

        profileTitle.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        // =================================================
        // NAME
        // =================================================

        Label nameLabel = new Label(
                "Name"
        );

        nameLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        TextField nameField = new TextField();

        nameField.setPrefWidth(400);

        nameField.setPromptText(
                "Dietitian Name"
        );

        if (dietitian != null &&
                dietitian.getName() != null) {

            nameField.setText(
                    dietitian.getName()
            );
        }

        styleTextField(nameField);

        // =================================================
        // EMAIL
        // =================================================

        Label emailLabel = new Label(
                "Email"
        );

        emailLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        TextField emailField = new TextField();

        emailField.setPrefWidth(400);

        emailField.setEditable(false);

        emailField.setPromptText(
                "email@example.com"
        );

        if (dietitian != null &&
                dietitian.getEmail() != null) {

            emailField.setText(
                    dietitian.getEmail()
            );
        }

        styleTextField(emailField);

        // =================================================
        // SPECIALIZATION
        // =================================================

        Label specializationLabel =
                new Label("Specialization");

        specializationLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        TextField specializationField =
                new TextField();

        specializationField.setPrefWidth(400);

        specializationField.setPromptText(
                "Clinical Nutrition"
        );

        if (dietitian != null &&
                dietitian.getSpecialization() != null) {

            specializationField.setText(
                    dietitian.getSpecialization()
            );
        }

        styleTextField(specializationField);

        // =================================================
        // EXPERIENCE
        // =================================================

        Label experienceLabel =
                new Label("Experience");

        experienceLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        TextField experienceField =
                new TextField();

        experienceField.setPrefWidth(400);

        experienceField.setPromptText(
                "5 years"
        );

        if (dietitian != null &&
                dietitian.getExperience() != null) {

            experienceField.setText(
                    dietitian.getExperience()
            );
        }

        styleTextField(experienceField);

        // =================================================
        // UPDATE PROFILE BUTTON
        // =================================================

        Button updateProfileButton =
                new Button("Update Profile");

        updateProfileButton.setStyle(
                "-fx-background-color: #1976D2;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 30;"
        );

        updateProfileButton.setOnAction(e -> {

            String name =
                    nameField.getText().trim();

            String specialization =
                    specializationField
                            .getText()
                            .trim();

            String experience =
                    experienceField
                            .getText()
                            .trim();

            if (name.isEmpty()) {

                System.out.println(
                        "Please enter name."
                );

                return;
            }

            if (specialization.isEmpty()) {

                System.out.println(
                        "Please enter specialization."
                );

                return;
            }

            if (experience.isEmpty()) {

                System.out.println(
                        "Please enter experience."
                );

                return;
            }

            dietitianDao.updateDietitianProfile(
                    uid,
                    name,
                    specialization,
                    experience
            );

            System.out.println(
                    "Profile Updated Successfully"
            );
        });

        // =================================================
        // WHATSAPP NUMBER
        // =================================================

        Label whatsappLabel =
                new Label("WhatsApp Number");

        whatsappLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;"
        );

        TextField whatsappField =
                new TextField();

        whatsappField.setPrefWidth(400);

        whatsappField.setPromptText(
                "Enter WhatsApp Number"
        );

        if (dietitian != null &&
                dietitian.getWhatsappNumber() != null) {

            whatsappField.setText(
                    dietitian.getWhatsappNumber()
            );
        }

        styleTextField(whatsappField);

        // =================================================
        // SAVE WHATSAPP BUTTON
        // =================================================

        Button saveWhatsappButton =
                new Button("Save");

        saveWhatsappButton.setStyle(
                "-fx-background-color: #1976D2;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 30;"
        );

        saveWhatsappButton.setOnAction(e -> {

            String number =
                    whatsappField.getText().trim();

            if (number.isEmpty()) {

                System.out.println(
                        "Please enter WhatsApp number."
                );

                return;
            }

            dietitianDao.updateWhatsappNumber(
                    uid,
                    number
            );

            System.out.println(
                    "WhatsApp Number Saved Successfully"
            );
        });

        // =================================================
        // ADD EVERYTHING
        // =================================================

        profilePage.getChildren().addAll(

                profileTitle,

                nameLabel,
                nameField,

                emailLabel,
                emailField,

                specializationLabel,
                specializationField,

                experienceLabel,
                experienceField,

                updateProfileButton,

                whatsappLabel,
                whatsappField,

                saveWhatsappButton
        );

        return profilePage;
    }

    // =====================================================
    // TEXT FIELD STYLE
    // =====================================================

    private void styleTextField(TextField field) {

        field.setStyle(
                "-fx-background-color: #1E293B;" +
                "-fx-text-fill: white;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;"
        );
    }
}