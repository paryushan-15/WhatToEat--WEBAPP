package com.bytebites.view.login;

import com.bytebites.controller.AuthController;

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

public class LoginPage {

    // =====================================================
    // FIELDS
    // =====================================================

    private Scene loginScene;

    private final String actor;

    private final String themeColor;

    private final AuthController authController;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public LoginPage(
            String actor,
            String themeColor
    ) {

        this.actor =
                actor;

        this.themeColor =
                themeColor;

        this.authController =
                new AuthController();
    }


    // =====================================================
    // GET LOGIN SCENE
    // =====================================================

    public Scene getLoginScene() {

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
                -60
        );


        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Welcome Back"
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
                        "Login as "
                                + actor
                                + " to continue your journey."
                );


        subtitle.setTextFill(
                Color.WHITE
        );


        subtitle.setStyle(
                "-fx-font-size: 13px;"
        );


        VBox descriptionBox =
                new VBox(
                        title,
                        subtitle
                );


        descriptionBox.setSpacing(
                15
        );


        descriptionBox.setAlignment(
                Pos.CENTER
        );


        descriptionBox.setTranslateY(
                -60
        );


        // =================================================
        // EMAIL
        // =================================================

        Label emailLabel =
                new Label(
                        "Email Address"
                );


        emailLabel.setTextFill(
                Color.WHITE
        );


        emailLabel.setStyle(
                "-fx-font-weight: bold;"
                        +
                "-fx-font-size: 15px;"
        );


        TextField emailTextField =
                new TextField();


        emailTextField.setPromptText(
                "Enter your email"
        );


        applyInputStyle(
                emailTextField
        );


        emailTextField.setPrefWidth(
                380
        );


        emailTextField.setPrefHeight(
                40
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
                new Label(
                        "Password"
                );


        passwordLabel.setTextFill(
                Color.WHITE
        );


        passwordLabel.setStyle(
                "-fx-font-weight: bold;"
                        +
                "-fx-font-size: 15px;"
        );


        PasswordField passwordField =
                new PasswordField();


        passwordField.setPromptText(
                "Enter your password"
        );


        applyInputStyle(
                passwordField
        );


        passwordField.setPrefWidth(
                380
        );


        passwordField.setPrefHeight(
                40
        );


        Label forgetLabel =
                new Label(
                        "Forgot Password?"
                );


        forgetLabel.setTextFill(
                Color.web(
                        themeColor
                )
        );


        forgetLabel.setStyle(
                "-fx-font-size: 12px;"
                        +
                "-fx-cursor: hand;"
        );


        HBox passwordHeader =
                new HBox();


        passwordHeader.setAlignment(
                Pos.CENTER_LEFT
        );


        passwordHeader.getChildren()
                .add(
                        passwordLabel
                );


        HBox spacer =
                new HBox();


        HBox.setHgrow(
                spacer,
                javafx.scene.layout.Priority.ALWAYS
        );


        passwordHeader.getChildren()
                .addAll(
                        spacer,
                        forgetLabel
                );


        VBox passwordBox =
                new VBox(
                        7,
                        passwordHeader,
                        passwordField
                );


        passwordBox.setAlignment(
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
        // LOGIN BUTTON
        // =================================================

        Button loginButton =
                new Button(
                        "Log in"
                );


        loginButton.setPrefWidth(
                380
        );


        loginButton.setPrefHeight(
                42
        );


        applyLoginButtonStyle(
                loginButton
        );


        // =================================================
        // ENTER KEY LOGIN
        // =================================================

        passwordField.setOnAction(
                event ->
                        loginButton.fire()
        );


        // =================================================
        // LOGIN ACTION
        // =================================================

        loginButton.setOnAction(
                event -> {

                    String email =
                            emailTextField
                                    .getText()
                                    .trim();


                    String password =
                            passwordField
                                    .getText();


                    // =========================================
                    // BASIC VALIDATION
                    // =========================================

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
                            password == null
                                    ||
                            password.isEmpty()
                    ) {

                        showError(
                                messageLabel,
                                "Please enter your password."
                        );

                        return;
                    }


                    // =========================================
                    // DISABLE BUTTON DURING LOGIN
                    // =========================================

                    loginButton.setDisable(
                            true
                    );


                    loginButton.setText(
                            "Logging in..."
                    );


                    messageLabel.setText(
                            ""
                    );


                    /*
                     * IMPORTANT:
                     *
                     * AuthController.login() now handles:
                     *
                     * Firebase Authentication
                     *          +
                     * Firestore role lookup
                     *          +
                     * account status
                     *          +
                     * SessionManager
                     *
                     * LoginPage should NOT manually query
                     * Firestore anymore.
                     */

                    String result =
                            authController.login(
                                    email,
                                    password
                            );


                    loginButton.setDisable(
                            false
                    );


                    loginButton.setText(
                            "Log in"
                    );


                    if (
                            result == null
                    ) {

                        showError(
                                messageLabel,
                                "Unable to login."
                        );

                        return;
                    }


                    handleLoginResult(
                            result,
                            messageLabel
                    );
                }
        );


        // =================================================
        // SIGN UP LINK
        // =================================================

        HBox loginBox =
                new HBox();


        loginBox.setAlignment(
                Pos.CENTER
        );


        /*
         * Admin accounts cannot be created through
         * the normal signup flow.
         */

        if (
                !actor.equalsIgnoreCase(
                        "Admin"
                )
        ) {

            Label alreadyLabel =
                    new Label(
                            "Don't have an account? "
                    );


            alreadyLabel.setTextFill(
                    Color.WHITE
            );


            Label signupLabel =
                    new Label(
                            "Sign up"
                    );


            signupLabel.setTextFill(
                    Color.web(
                            themeColor
                    )
            );


            signupLabel.setStyle(
                    "-fx-font-weight: bold;"
                            +
                    "-fx-cursor: hand;"
            );


            signupLabel.setOnMouseClicked(
                    event -> {

                        MainLoginPage.openSignupPage(
                                actor,
                                themeColor
                        );
                    }
            );


            loginBox.getChildren()
                    .addAll(
                            alreadyLabel,
                            signupLabel
                    );
        }


        // =================================================
        // BACK
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
                event ->
                        MainLoginPage
                                .navigateToMainLoginPage()
        );


        // =================================================
        // FORM
        // =================================================

        VBox form =
                new VBox(
                        22,
                        emailBox,
                        passwordBox,
                        loginButton,
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
                        50
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

        loginScene =
                new Scene(
                        root,
                        1550,
                        830
                );


        loginScene.setFill(
                Color.web(
                        "#0F172A"
                )
        );


        return loginScene;
    }


    // =====================================================
    // HANDLE LOGIN RESULT
    // =====================================================

    private void handleLoginResult(
            String result,
            Label messageLabel
    ) {

        switch (
                result.toUpperCase()
        ) {

            // =================================================
            // ADMIN SUCCESS
            // =================================================

            case "ADMIN":

                /*
                 * Prevent an Admin account from logging
                 * through User/Dietitian selection.
                 */

                if (
                        !actor.equalsIgnoreCase(
                                "Admin"
                        )
                ) {

                    authController.logout();


                    showError(
                            messageLabel,
                            "This account is registered as Admin. "
                                    +
                            "Please use Admin Login."
                    );

                    return;
                }


                showSuccess(
                        messageLabel,
                        "Admin login successful."
                );


                MainLoginPage.navigateToDashboard(
                        "Admin"
                );


                break;


            // =================================================
            // USER SUCCESS
            // =================================================

            case "USER":

                if (
                        !actor.equalsIgnoreCase(
                                "User"
                        )
                ) {

                    authController.logout();


                    showError(
                            messageLabel,
                            "This account is registered as User. "
                                    +
                            "Please use User Login."
                    );

                    return;
                }


                showSuccess(
                        messageLabel,
                        "Login successful."
                );


                MainLoginPage.navigateToDashboard(
                        "User"
                );


                break;


            // =================================================
            // DIETITIAN SUCCESS
            // =================================================

            case "DIETITIAN":

                if (
                        !actor.equalsIgnoreCase(
                                "Dietitian"
                        )
                ) {

                    authController.logout();


                    showError(
                            messageLabel,
                            "This account is registered as Dietitian. "
                                    +
                            "Please use Dietitian Login."
                    );

                    return;
                }


                showSuccess(
                        messageLabel,
                        "Login successful."
                );


                MainLoginPage.navigateToDashboard(
                        "Dietitian"
                );


                break;


            // =================================================
            // PENDING DIETITIAN
            // =================================================

            case "PENDING_DIETITIAN":

                authController.logout();


                showWarning(
                        messageLabel,
                        "Your Dietitian account is waiting for Admin approval."
                );


                break;


            // =================================================
            // REJECTED DIETITIAN
            // =================================================

            case "REJECTED_DIETITIAN":

                authController.logout();


                showError(
                        messageLabel,
                        "Your Dietitian account has been rejected by the Admin."
                );


                break;


            // =================================================
            // SUSPENDED DIETITIAN
            // =================================================

            case "SUSPENDED_DIETITIAN":

                authController.logout();


                showError(
                        messageLabel,
                        "Your Dietitian account has been suspended."
                );


                break;


            // =================================================
            // SUSPENDED USER
            // =================================================

            case "SUSPENDED_USER":

                authController.logout();


                showError(
                        messageLabel,
                        "Your User account has been suspended."
                );


                break;


            // =================================================
            // SUSPENDED ADMIN
            // =================================================

            case "SUSPENDED_ADMIN":

                authController.logout();


                showError(
                        messageLabel,
                        "Your Admin account has been suspended."
                );


                break;


            // =================================================
            // DIETITIAN NOT APPROVED
            // =================================================

            case "DIETITIAN_NOT_APPROVED":

                authController.logout();


                showWarning(
                        messageLabel,
                        "Your Dietitian account has not been approved yet."
                );


                break;


            // =================================================
            // INVALID CREDENTIALS
            // =================================================

            case "INVALID_CREDENTIALS":

                authController.logout();


                showError(
                        messageLabel,
                        "Invalid email or password."
                );


                break;


            // =================================================
            // UNKNOWN ROLE
            // =================================================

            case "UNKNOWN_ROLE":

                authController.logout();


                showError(
                        messageLabel,
                        "Account exists, but no valid WTE role was found."
                );


                break;


            // =================================================
            // DEFAULT
            // =================================================

            default:

                authController.logout();


                showError(
                        messageLabel,
                        "Login failed: "
                                + result
                );


                break;
        }
    }


    // =====================================================
    // INPUT STYLE
    // =====================================================

    private void applyInputStyle(
            TextField field
    ) {

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
    // LOGIN BUTTON STYLE
    // =====================================================

    private void applyLoginButtonStyle(
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