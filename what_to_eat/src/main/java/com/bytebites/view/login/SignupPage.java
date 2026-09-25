package com.bytebites.view.login;

import com.bytebites.controller.AuthController;
import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.UserDao;
import com.bytebites.model.Dietitian;
import com.bytebites.model.User;
import com.bytebites.model.session.SessionManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SignupPage {

    // =====================================================
    // FIELDS
    // =====================================================

    private Scene userSignupScene;

    private final String actor;

    private final String themeColor;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public SignupPage(
            String actor,
            String themeColor
    ) {

        this.actor =
                actor;

        this.themeColor =
                themeColor;
    }


    // =====================================================
    // GET SIGNUP SCENE
    // =====================================================

    public Scene getUserSignupScene() {

        // =================================================
        // SECURITY
        // =================================================

        /*
         * Admin accounts must never be created through
         * the normal application signup page.
         */

        if (
                actor.equalsIgnoreCase(
                        "Admin"
                )
        ) {

            return new LoginPage(
                    "Admin",
                    themeColor
            ).getLoginScene();
        }


        // =================================================
        // LOGO
        // =================================================

        Image logoImage =
                new Image(
                        "assets\\icon\\image.png"
                );


        ImageView viewImage =
                new ImageView(
                        logoImage
                );


        viewImage.setStyle(
                "-fx-padding: 10px;"
        );


        viewImage.setFitWidth(
                250
        );


        viewImage.setFitHeight(
                250
        );


        viewImage.setPreserveRatio(
                true
        );


        viewImage.setTranslateY(
                -40
        );


        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Create "
                                + actor
                                + " Account"
                );


        title.setTextFill(
                Color.WHITE
        );


        title.setStyle(
                "-fx-font-weight: bold;"
                        +
                "-fx-font-size: 24px;"
        );


        Label subtitle =
                new Label(
                        actor.equalsIgnoreCase(
                                "Dietitian"
                        )
                                ?
                        "Create your Dietitian account and wait for Admin approval."
                                :
                        "Join WTE? for a healthier, happier you."
                );


        subtitle.setTextFill(
                Color.WHITE
        );


        subtitle.setStyle(
                "-fx-font-size: 13px;"
        );


        VBox descriptionBox =
                new VBox(
                        15,
                        title,
                        subtitle
                );


        descriptionBox.setAlignment(
                Pos.CENTER
        );


        descriptionBox.setTranslateY(
                -40
        );


        // =================================================
        // FULL NAME
        // =================================================

        Label nameLabel =
                createFieldLabel(
                        "Full Name"
                );


        TextField nameTextField =
                new TextField();


        nameTextField.setPromptText(
                "Enter your full name"
        );


        applyTextFieldStyle(
                nameTextField
        );


        VBox nameBox =
                new VBox(
                        7,
                        nameLabel,
                        nameTextField
                );


        nameBox.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // EMAIL
        // =================================================

        Label emailLabel =
                createFieldLabel(
                        "Email Address"
                );


        TextField emailTextField =
                new TextField();


        emailTextField.setPromptText(
                "Enter your email"
        );


        applyTextFieldStyle(
                emailTextField
        );


        VBox emailBox =
                new VBox(
                        7,
                        emailLabel,
                        emailTextField
                );


        emailBox.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // PASSWORD
        // =================================================

        Label passwordLabel =
                createFieldLabel(
                        "Password"
                );


        PasswordField passwordField =
                new PasswordField();


        passwordField.setPromptText(
                "Enter your password"
        );


        applyTextFieldStyle(
                passwordField
        );


        VBox passwordBox =
                new VBox(
                        7,
                        passwordLabel,
                        passwordField
                );


        passwordBox.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // CONFIRM PASSWORD
        // =================================================

        Label confirmPasswordLabel =
                createFieldLabel(
                        "Confirm Password"
                );


        PasswordField confirmPasswordField =
                new PasswordField();


        confirmPasswordField.setPromptText(
                "Confirm your password"
        );


        applyTextFieldStyle(
                confirmPasswordField
        );


        VBox confirmPasswordBox =
                new VBox(
                        7,
                        confirmPasswordLabel,
                        confirmPasswordField
                );


        confirmPasswordBox.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // MESSAGE
        // =================================================

        Label messageLabel =
                new Label();


        messageLabel.setWrapText(
                true
        );


        messageLabel.setMaxWidth(
                380
        );


        messageLabel.setAlignment(
                Pos.CENTER
        );


        messageLabel.setStyle(
                "-fx-font-size: 13px;"
                        +
                "-fx-font-weight: bold;"
        );


        // =================================================
        // SIGNUP BUTTON
        // =================================================

        Button joinButton =
                new Button(
                        "Join WTE?"
                );


        joinButton.setPrefWidth(
                380
        );


        joinButton.setPrefHeight(
                42
        );


        applyPrimaryButtonStyle(
                joinButton
        );


        // =================================================
        // SIGNUP ACTION
        // =================================================

        joinButton.setOnAction(
                event -> {

                    String name =
                            nameTextField
                                    .getText()
                                    .trim();


                    String email =
                            emailTextField
                                    .getText()
                                    .trim();


                    String enteredPassword =
                            passwordField
                                    .getText();


                    String confirmPassword =
                            confirmPasswordField
                                    .getText();


                    // =========================================
                    // VALIDATION
                    // =========================================

                    if (
                            name.isEmpty()
                    ) {

                        showError(
                                messageLabel,
                                "Please enter your full name."
                        );

                        return;
                    }


                    if (
                            email.isEmpty()
                    ) {

                        showError(
                                messageLabel,
                                "Please enter your email address."
                        );

                        return;
                    }


                    if (
                            !isValidEmail(
                                    email
                            )
                    ) {

                        showError(
                                messageLabel,
                                "Please enter a valid email address."
                        );

                        return;
                    }


                    if (
                            enteredPassword == null
                                    ||
                            enteredPassword.isEmpty()
                    ) {

                        showError(
                                messageLabel,
                                "Please enter a password."
                        );

                        return;
                    }


                    if (
                            enteredPassword.length()
                                    < 6
                    ) {

                        showError(
                                messageLabel,
                                "Password must contain at least 6 characters."
                        );

                        return;
                    }


                    if (
                            !enteredPassword.equals(
                                    confirmPassword
                            )
                    ) {

                        showError(
                                messageLabel,
                                "Passwords do not match."
                        );

                        return;
                    }


                    // =========================================
                    // PREVENT ADMIN SIGNUP
                    // =========================================

                    if (
                            actor.equalsIgnoreCase(
                                    "Admin"
                            )
                    ) {

                        showError(
                                messageLabel,
                                "Admin accounts cannot be created here."
                        );

                        return;
                    }


                    // =========================================
                    // DISABLE BUTTON
                    // =========================================

                    joinButton.setDisable(
                            true
                    );


                    joinButton.setText(
                            "Creating Account..."
                    );


                    AuthController controller =
                            new AuthController();


                    // =========================================
                    // FIREBASE AUTHENTICATION SIGNUP
                    // =========================================

                    String uid =
                            controller.signUp(
                                    email,
                                    enteredPassword
                            );


                    if (
                            uid == null
                    ) {

                        joinButton.setDisable(
                                false
                        );


                        joinButton.setText(
                                "Join WTE?"
                        );


                        showError(
                                messageLabel,
                                "Unable to create account. "
                                        +
                                "The email may already be registered."
                        );

                        return;
                    }


                    // =========================================
                    // USER SIGNUP
                    // =========================================

                    if (
                            actor.equalsIgnoreCase(
                                    "User"
                            )
                    ) {

                        User user =
                                new User(
                                        uid,
                                        name,
                                        email
                                );


                        UserDao userDao =
                                new UserDao();


                        boolean saved =
                                userDao.saveUser(
                                        user
                                );


                        if (
                                !saved
                        ) {

                            joinButton.setDisable(
                                    false
                            );


                            joinButton.setText(
                                    "Join WTE?"
                            );


                            showError(
                                    messageLabel,
                                    "Authentication account was created, "
                                            +
                                    "but the User profile could not be saved."
                            );

                            return;
                        }


                        // =====================================
                        // CREATE USER SESSION
                        // =====================================

                        SessionManager.setUid(
                                uid
                        );


                        SessionManager.setRole(
                                "USER"
                        );


                        SessionManager.setName(
                                name
                        );


                        showSuccess(
                                messageLabel,
                                "Account created successfully."
                        );


                        // =====================================
                        // USER CAN ENTER DASHBOARD
                        // =====================================

                        MainLoginPage
                                .navigateToDashboard(
                                        "User"
                                );


                        return;
                    }


                    // =========================================
                    // DIETITIAN SIGNUP
                    // =========================================

                    if (
                            actor.equalsIgnoreCase(
                                    "Dietitian"
                            )
                    ) {

                        /*
                         * A newly created Dietitian is PENDING.
                         *
                         * They MUST NOT be allowed into the
                         * Dietitian Dashboard until an Admin
                         * approves the account.
                         */

                        Dietitian dietitian =
                                new Dietitian(
                                        uid,
                                        name,
                                        email,
                                        "DIETITIAN",
                                        "Pending"
                                );


                        DietitianDao dietitianDao =
                                new DietitianDao();


                        boolean saved =
                                dietitianDao
                                        .saveDietitian(
                                                dietitian
                                        );


                        if (
                                !saved
                        ) {

                            joinButton.setDisable(
                                    false
                            );


                            joinButton.setText(
                                    "Join WTE?"
                            );


                            showError(
                                    messageLabel,
                                    "Authentication account was created, "
                                            +
                                    "but the Dietitian profile could not be saved."
                            );

                            return;
                        }


                        /*
                         * Do NOT create a logged-in application
                         * session for a pending Dietitian.
                         */

                        SessionManager.clearSession();


                        showWarning(
                                messageLabel,
                                "Account created successfully. "
                                        +
                                "Your Dietitian account is waiting for Admin approval."
                        );


                        joinButton.setDisable(
                                false
                        );


                        joinButton.setText(
                                "Account Created"
                        );


                        /*
                         * The account now exists in:
                         *
                         * dietitians/{uid}
                         *
                         * with:
                         *
                         * status = Pending
                         *
                         * The Admin can approve it from
                         * Admin -> Dietitians.
                         */


                        return;
                    }


                    // =========================================
                    // UNKNOWN ACTOR
                    // =========================================

                    SessionManager.clearSession();


                    joinButton.setDisable(
                            false
                    );


                    joinButton.setText(
                            "Join WTE?"
                    );


                    showError(
                            messageLabel,
                            "Invalid account type."
                    );
                }
        );


        // =================================================
        // LOGIN LINK
        // =================================================

        Label alreadyLabel =
                new Label(
                        "Already have an account? "
                );


        alreadyLabel.setTextFill(
                Color.WHITE
        );


        Label loginLabel =
                new Label(
                        "Log in"
                );


        loginLabel.setTextFill(
                Color.web(
                        themeColor
                )
        );


        loginLabel.setStyle(
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        HBox loginBox =
                new HBox(
                        alreadyLabel,
                        loginLabel
                );


        loginBox.setAlignment(
                Pos.CENTER
        );


        loginLabel.setOnMouseClicked(
                event -> {

                    MainLoginPage.openLoginPage(
                            actor,
                            themeColor
                    );
                }
        );


        // =================================================
        // BACK TO ROLE SELECTION
        // =================================================

        Label backLabel =
                new Label(
                        "< Back to account selection"
                );


        backLabel.setTextFill(
                Color.web(
                        "#94A3B8"
                )
        );


        backLabel.setStyle(
                "-fx-font-size: 12px;"
                        +
                "-fx-cursor: hand;"
        );


        backLabel.setOnMouseClicked(
                event -> {

                    MainLoginPage
                            .navigateToMainLoginPage();
                }
        );


        // =================================================
        // FORM
        // =================================================

        VBox form =
                new VBox(
                        20,
                        nameBox,
                        emailBox,
                        passwordBox,
                        confirmPasswordBox,
                        joinButton,
                        loginBox,
                        messageLabel,
                        backLabel
                );


        form.setPrefWidth(
                380
        );


        form.setMaxWidth(
                380
        );


        form.setAlignment(
                Pos.CENTER
        );


        // =================================================
        // CARD
        // =================================================

        VBox card =
                new VBox(
                        viewImage,
                        descriptionBox,
                        form
                );


        card.setAlignment(
                Pos.CENTER
        );


        card.setPadding(
                new Insets(
                        20
                )
        );


        card.setPrefWidth(
                500
        );


        card.setStyle(
                "-fx-background-color: #081227;"
                        +
                "-fx-border-color: "
                        + themeColor
                        + ";"
                        +
                "-fx-border-width: 1.5px;"
                        +
                "-fx-border-radius: 8px;"
                        +
                "-fx-background-radius: 8px;"
        );


        // =================================================
        // ROOT
        // =================================================

        HBox root =
                new HBox(
                        card
                );


        root.setStyle(
                "-fx-background-color: #0F172A;"
        );


        root.setAlignment(
                Pos.CENTER
        );


        root.setPadding(
                new Insets(
                        50
                )
        );


        // =================================================
        // SCENE
        // =================================================

        userSignupScene =
                new Scene(
                        root,
                        1550,
                        830
                );


        userSignupScene.setFill(
                Color.web(
                        "#0F172A"
                )
        );


        return userSignupScene;
    }


    // =====================================================
    // FIELD LABEL
    // =====================================================

    private Label createFieldLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setTextFill(
                Color.WHITE
        );


        label.setStyle(
                "-fx-font-weight: bold;"
                        +
                "-fx-font-size: 15px;"
        );


        return label;
    }


    // =====================================================
    // TEXT FIELD STYLE
    // =====================================================

    private void applyTextFieldStyle(
            TextField field
    ) {

        field.setPrefWidth(
                380
        );


        field.setPrefHeight(
                40
        );


        field.setStyle(
                "-fx-background-color: #0F172A;"
                        +
                "-fx-border-color: #555555;"
                        +
                "-fx-border-width: 1px;"
                        +
                "-fx-border-radius: 5px;"
                        +
                "-fx-background-radius: 5px;"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-prompt-text-fill: #64748B;"
                        +
                "-fx-padding: 0 10 0 10;"
        );
    }


    // =====================================================
    // PRIMARY BUTTON STYLE
    // =====================================================

    private void applyPrimaryButtonStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: "
                        + themeColor
                        + ";"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-background-radius: 5px;"
                        +
                "-fx-border-radius: 5px;"
                        +
                "-fx-cursor: hand;"
        );
    }


    // =====================================================
    // EMAIL VALIDATION
    // =====================================================

    private boolean isValidEmail(
            String email
    ) {

        if (
                email == null
        ) {

            return false;
        }


        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }


    // =====================================================
    // SUCCESS MESSAGE
    // =====================================================

    private void showSuccess(
            Label label,
            String message
    ) {

        label.setTextFill(
                Color.web(
                        "#22C55E"
                )
        );


        label.setText(
                message
        );
    }


    // =====================================================
    // WARNING MESSAGE
    // =====================================================

    private void showWarning(
            Label label,
            String message
    ) {

        label.setTextFill(
                Color.web(
                        "#F59E0B"
                )
        );


        label.setText(
                message
        );
    }


    // =====================================================
    // ERROR MESSAGE
    // =====================================================

    private void showError(
            Label label,
            String message
    ) {

        label.setTextFill(
                Color.web(
                        "#EF4444"
                )
        );


        label.setText(
                message
        );
    }
}