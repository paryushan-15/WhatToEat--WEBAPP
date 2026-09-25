package com.bytebites.view.admin;

import com.bytebites.controller.AuthController;
import com.bytebites.model.session.SessionManager;
import com.bytebites.view.admin.adminFeatures.AdminCommunity;
import com.bytebites.view.admin.adminFeatures.AdminConsultations;
import com.bytebites.view.admin.adminFeatures.AdminDashboard;
import com.bytebites.view.admin.adminFeatures.AdminDietitians;
import com.bytebites.view.admin.adminFeatures.AdminReviews;
import com.bytebites.view.admin.adminFeatures.AdminSettings;
import com.bytebites.view.admin.adminFeatures.AdminSubscriptions;
import com.bytebites.view.admin.adminFeatures.AdminUsers;
import com.bytebites.view.login.MainLoginPage;
import com.bytebites.view.common.LoadingOverlay;

import java.util.function.Supplier;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;

import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class AdminMainLayout {

    // =====================================================
    // COLORS
    // =====================================================

    private static final String BACKGROUND =
            "#0F172A";

    private static final String SIDEBAR_BACKGROUND =
            "#121D35";

    private static final String CARD_BACKGROUND =
            "#1E293B";

    private static final String PRIMARY_ORANGE =
            "#F68F32";

    private static final String TEXT_PRIMARY =
            "#F8FAFC";

    private static final String TEXT_SECONDARY =
            "#94A3B8";

    private static final String BORDER =
            "#334155";

    private static final String DANGER =
            "#EF4444";



    // =====================================================
    // MAIN ROOT
    // =====================================================

    private final BorderPane root;


    // =====================================================
    // SHARED FEATURE PANE
    // =====================================================

    private final BorderPane featurePane;

    private final StackPane contentStack;

    private final LoadingOverlay loadingOverlay;


    // =====================================================
    // SIDEBAR
    // =====================================================

    private VBox sidebar;


    // =====================================================
    // ACTIVE NAVIGATION BUTTON
    // =====================================================

    private Button activeButton;


    // =====================================================
    // NAVIGATION BUTTONS
    // =====================================================

    private Button dashboardButton;

    private Button usersButton;

    private Button dietitiansButton;

    private Button consultationsButton;

    private Button subscriptionsButton;

    private Button reviewsButton;

    private Button communityButton;

    private Button settingsButton;

    private Button logoutButton;


    // =====================================================
    // AUTH
    // =====================================================

    private final AuthController authController;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminMainLayout() {

        authController =
                new AuthController();


        root =
                new BorderPane();


        root.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        featurePane =
                new BorderPane();


        featurePane.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        featurePane.setMinWidth(
                0
        );


        featurePane.setMinHeight(
                0
        );


        loadingOverlay =
                new LoadingOverlay(PRIMARY_ORANGE);


        contentStack =
                new StackPane(featurePane, loadingOverlay);


        createSidebar();


        root.setLeft(
                sidebar
        );


        root.setCenter(
                contentStack
        );


        // Default page
        openDashboard();
    }


    // =====================================================
    // GET MAIN LAYOUT
    // =====================================================

    public BorderPane getLayout() {

        return root;
    }


    // =====================================================
    // GET FEATURE PANE
    //
    // Other Admin feature classes can use the same pane.
    // =====================================================

    public BorderPane getFeaturePane() {

        return featurePane;
    }


    // =====================================================
    // =====================================================
    //
    // SIDEBAR
    //
    // =====================================================
    // =====================================================


    private void createSidebar() {

        sidebar =
                new VBox();


        sidebar.setPrefWidth(
                260
        );


        sidebar.setMinWidth(
                260
        );


        sidebar.setMaxWidth(
                260
        );


        sidebar.setStyle(
                "-fx-background-color: "
                        + SIDEBAR_BACKGROUND
                        + ";"
                        +
                "-fx-border-color: transparent "
                        + BORDER
                        + " transparent transparent;"
        );


        // =================================================
        // BRAND
        // =================================================

        VBox brandBox =
                createBrandBox();


        // =================================================
        // ADMIN PROFILE
        // =================================================

        VBox adminProfile =
                createAdminProfile();


        // =================================================
        // SECTION LABEL
        // =================================================

        Label managementLabel =
                new Label(
                        "MANAGEMENT"
                );


        managementLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 11px;"
                        +
                "-fx-font-weight: bold;"
        );


        VBox.setMargin(
                managementLabel,
                new Insets(
                        20,
                        20,
                        8,
                        22
                )
        );


        // =================================================
        // NAVIGATION
        // =================================================

        VBox navigation =
                new VBox(5);


        navigation.setPadding(
                new Insets(
                        0,
                        12,
                        0,
                        12
                )
        );


        dashboardButton =
                createNavigationButton(
                        "▦",
                        "Dashboard"
                );


        usersButton =
                createNavigationButton(
                        "👥",
                        "Users"
                );


        dietitiansButton =
                createNavigationButton(
                        "⚕",
                        "Dietitians"
                );


        consultationsButton =
                createNavigationButton(
                        "◫",
                        "Consultations"
                );


        subscriptionsButton =
                createNavigationButton(
                        "₹",
                        "Subscriptions"
                );


        reviewsButton =
                createNavigationButton(
                        "★",
                        "Ratings & Reviews"
                );


        communityButton =
                createNavigationButton(
                        "◉",
                        "Community"
                );


        navigation
                .getChildren()
                .addAll(
                        dashboardButton,
                        usersButton,
                        dietitiansButton,
                        consultationsButton,
                        subscriptionsButton,
                        reviewsButton,
                        communityButton
                );


        // =================================================
        // SPACER
        // =================================================

        Region spacer =
                new Region();


        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );


        // =================================================
        // BOTTOM NAVIGATION
        // =================================================

        VBox bottomNavigation =
                new VBox(5);


        bottomNavigation.setPadding(
                new Insets(
                        10,
                        12,
                        18,
                        12
                )
        );


        settingsButton =
                createNavigationButton(
                        "⚙",
                        "Settings"
                );


        logoutButton =
                createLogoutButton();


        bottomNavigation
                .getChildren()
                .addAll(
                        settingsButton,
                        logoutButton
                );


        sidebar
                .getChildren()
                .addAll(
                        brandBox,
                        adminProfile,
                        managementLabel,
                        navigation,
                        spacer,
                        bottomNavigation
                );


        setupNavigationActions();
    }


    // =====================================================
    // BRAND
    // =====================================================

    private VBox createBrandBox() {

        VBox box =
                new VBox(3);


        box.setPadding(
                new Insets(
                        25,
                        22,
                        22,
                        22
                )
        );


        Label title =
                new Label(
                        "What To Eat?"
                );


        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        23
                )
        );


        title.setStyle(
                "-fx-text-fill: white;"
        );


        Label adminLabel =
                new Label(
                        "ADMIN CONSOLE"
                );


        adminLabel.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        10
                )
        );


        adminLabel.setStyle(
                "-fx-text-fill: "
                        + PRIMARY_ORANGE
                        + ";"
                        +
                "-fx-letter-spacing: 1px;"
        );


        box
                .getChildren()
                .addAll(
                        title,
                        adminLabel
                );


        return box;
    }


    // =====================================================
    // ADMIN PROFILE
    // =====================================================

    private VBox createAdminProfile() {

        VBox outer =
                new VBox();


        outer.setPadding(
                new Insets(
                        0,
                        14,
                        5,
                        14
                )
        );


        HBox profile =
                new HBox(12);


        profile.setAlignment(
                Pos.CENTER_LEFT
        );


        profile.setPadding(
                new Insets(
                        14
                )
        );


        profile.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 12;"
                        +
                "-fx-border-radius: 12;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
        );


        // =================================================
        // AVATAR
        // =================================================

        StackPane avatar =
                new StackPane();


        avatar.setPrefSize(
                42,
                42
        );


        avatar.setMinSize(
                42,
                42
        );


        avatar.setMaxSize(
                42,
                42
        );


        avatar.setStyle(
                "-fx-background-color: "
                        + PRIMARY_ORANGE
                        + ";"
                        +
                "-fx-background-radius: 50;"
        );


        String adminName =
                SessionManager.getName();


        if (
                adminName == null
                        ||
                adminName.isBlank()
        ) {

            adminName =
                    "Admin";
        }


        String firstLetter =
                adminName
                        .substring(
                                0,
                                1
                        )
                        .toUpperCase();


        Label avatarText =
                new Label(
                        firstLetter
                );


        avatarText.setStyle(
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 17px;"
                        +
                "-fx-font-weight: bold;"
        );


        avatar
                .getChildren()
                .add(
                        avatarText
                );


        // =================================================
        // PROFILE TEXT
        // =================================================

        VBox profileText =
                new VBox(2);


        Label name =
                new Label(
                        adminName
                );


        name.setMaxWidth(
                150
        );


        name.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label role =
                new Label(
                        "Administrator"
                );


        role.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 11px;"
        );


        profileText
                .getChildren()
                .addAll(
                        name,
                        role
                );


        profile
                .getChildren()
                .addAll(
                        avatar,
                        profileText
                );


        outer
                .getChildren()
                .add(
                        profile
                );


        return outer;
    }


    // =====================================================
    // =====================================================
    //
    // NAVIGATION BUTTON
    //
    // =====================================================
    // =====================================================


    private Button createNavigationButton(
            String icon,
            String text
    ) {

        Button button =
                new Button(
                        icon
                                + "    "
                                + text
                );


        button.setMaxWidth(
                Double.MAX_VALUE
        );


        button.setPrefHeight(
                46
        );


        button.setAlignment(
                Pos.CENTER_LEFT
        );


        button.setPadding(
                new Insets(
                        0,
                        16,
                        0,
                        16
                )
        );


        applyInactiveStyle(
                button
        );


        button.setOnMouseEntered(
                event -> {

                    if (
                            button
                                    != activeButton
                    ) {

                        button.setStyle(
                                "-fx-background-color: #1E293B;"
                                        +
                                "-fx-background-radius: 9;"
                                        +
                                "-fx-text-fill: white;"
                                        +
                                "-fx-font-size: 14px;"
                                        +
                                "-fx-font-weight: 600;"
                                        +
                                "-fx-cursor: hand;"
                                        +
                                "-fx-border-color: transparent;"
                        );
                    }
                }
        );


        button.setOnMouseExited(
                event -> {

                    if (
                            button
                                    != activeButton
                    ) {

                        applyInactiveStyle(
                                button
                        );
                    }
                }
        );


        return button;
    }


    // =====================================================
    // ACTIVE STYLE
    // =====================================================

    private void applyActiveStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: "
                        + PRIMARY_ORANGE
                        + ";"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
                        +
                "-fx-border-color: transparent;"
        );
    }


    // =====================================================
    // INACTIVE STYLE
    // =====================================================

    private void applyInactiveStyle(
            Button button
    ) {

        button.setStyle(
                "-fx-background-color: transparent;"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: 600;"
                        +
                "-fx-cursor: hand;"
                        +
                "-fx-border-color: transparent;"
        );
    }


    // =====================================================
    // SELECT BUTTON
    // =====================================================

    private void selectButton(
            Button selectedButton
    ) {

        if (
                activeButton != null
        ) {

            applyInactiveStyle(
                    activeButton
            );
        }


        activeButton =
                selectedButton;


        applyActiveStyle(
                selectedButton
        );
    }


    // =====================================================
    // LOGOUT BUTTON
    // =====================================================

    private Button createLogoutButton() {

        Button button =
                new Button(
                        "↪    Logout"
                );


        button.setMaxWidth(
                Double.MAX_VALUE
        );


        button.setPrefHeight(
                46
        );


        button.setAlignment(
                Pos.CENTER_LEFT
        );


        button.setPadding(
                new Insets(
                        0,
                        16,
                        0,
                        16
                )
        );


        button.setStyle(
                "-fx-background-color: transparent;"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-text-fill: "
                        + DANGER
                        + ";"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
                        +
                "-fx-border-color: transparent;"
        );


        button.setOnMouseEntered(
                event ->
                        button.setStyle(
                                "-fx-background-color: #3B1720;"
                                        +
                                "-fx-background-radius: 9;"
                                        +
                                "-fx-text-fill: #F87171;"
                                        +
                                "-fx-font-size: 14px;"
                                        +
                                "-fx-font-weight: bold;"
                                        +
                                "-fx-cursor: hand;"
                                        +
                                "-fx-border-color: transparent;"
                        )
        );


        button.setOnMouseExited(
                event ->
                        button.setStyle(
                                "-fx-background-color: transparent;"
                                        +
                                "-fx-background-radius: 9;"
                                        +
                                "-fx-text-fill: "
                                        + DANGER
                                        + ";"
                                        +
                                "-fx-font-size: 14px;"
                                        +
                                "-fx-font-weight: bold;"
                                        +
                                "-fx-cursor: hand;"
                                        +
                                "-fx-border-color: transparent;"
                        )
        );

        return button;
    }


    // =====================================================
    // =====================================================
    //
    // NAVIGATION ACTIONS
    //
    // =====================================================
    // =====================================================


    private void setupNavigationActions() {

        dashboardButton.setOnAction(
                event ->
                        openDashboard()
        );


        usersButton.setOnAction(
                event ->
                        openUsers()
        );


        dietitiansButton.setOnAction(
                event ->
                        openDietitians()
        );


        consultationsButton.setOnAction(
                event ->
                        openConsultations()
        );


        subscriptionsButton.setOnAction(
                event ->
                        openSubscriptions()
        );


        reviewsButton.setOnAction(
                event ->
                        openReviews()
        );


        communityButton.setOnAction(
                event ->
                        openCommunity()
        );


        settingsButton.setOnAction(
                event ->
                        openSettings()
        );


        logoutButton.setOnAction(
                event ->
                        logout()
        );
    }


    // =====================================================
    // =====================================================
    //
    // FEATURE NAVIGATION
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // DASHBOARD
    // =====================================================

    public void openDashboard() {

        selectButton(
                dashboardButton
        );


        showPageWithLoader(
                "Loading dashboard...",
                () -> new AdminDashboard().getDashboard()
        );
    }


    // =====================================================
    // USERS
    // =====================================================

    public void openUsers() {

        selectButton(
                usersButton
        );


        showPageWithLoader(
                "Loading users...",
                () -> new AdminUsers().getUsersPage()
        );
    }


    // =====================================================
    // DIETITIANS
    // =====================================================

    public void openDietitians() {

        selectButton(
                dietitiansButton
        );


        showPageWithLoader(
                "Loading dietitians...",
                () -> new AdminDietitians().getDietitiansPage()
        );
    }


    // =====================================================
    // CONSULTATIONS
    // =====================================================

    public void openConsultations() {

        selectButton(
                consultationsButton
        );


        showPageWithLoader(
                "Loading consultations...",
                () -> new AdminConsultations().getConsultationsPage()
        );
    }


    // =====================================================
    // SUBSCRIPTIONS
    // =====================================================

    public void openSubscriptions() {

        selectButton(
                subscriptionsButton
        );


        showPageWithLoader(
                "Loading subscriptions...",
                () -> new AdminSubscriptions().getSubscriptionsPage()
        );
    }


    // =====================================================
    // RATINGS / REVIEWS
    // =====================================================

    public void openReviews() {

        selectButton(
                reviewsButton
        );


        showPageWithLoader(
                "Loading reviews...",
                () -> new AdminReviews().getReviewsPage()
        );
    }


    // =====================================================
    // COMMUNITY
    // =====================================================

    public void openCommunity() {

        selectButton(
                communityButton
        );


        showPageWithLoader(
                "Loading community...",
                () -> new AdminCommunity().getCommunityPage()
        );
    }


    // =====================================================
    // SETTINGS
    // =====================================================

    public void openSettings() {

        selectButton(
                settingsButton
        );


        showPageWithLoader(
                "Loading settings...",
                () -> new AdminSettings().getSettingsPage()
        );
    }


    // =====================================================
    // SHARED PAGE LOADER
    // =====================================================

    private void showPageWithLoader(
            String message,
            Supplier<Node> pageSupplier
    ) {

        loadingOverlay.show(message);
        sidebar.setDisable(true);

        PauseTransition pulse =
                new PauseTransition(
                        Duration.millis(60)
                );

        pulse.setOnFinished(
                event -> {
                    try {
                        Node page =
                                pageSupplier.get();

                        if (page != null) {
                            featurePane.setCenter(page);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        loadingOverlay.hide();
                        sidebar.setDisable(false);
                    }
                }
        );

        pulse.play();
    }


    // =====================================================
    // =====================================================
    //
    // PLACEHOLDER
    //
    // These placeholders allow AdminMainLayout to compile
    // before Files 10–17 exist.
    //
    // =====================================================
    // =====================================================


    private void showPlaceholderPage(
            String titleText,
            String subtitleText
    ) {

        VBox content =
                new VBox(12);


        content.setPadding(
                new Insets(
                        35
                )
        );


        content.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 28px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        subtitleText
                );


        subtitle.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 14px;"
        );


        VBox card =
                new VBox(8);


        card.setPadding(
                new Insets(
                        25
                )
        );


        card.setMaxWidth(
                700
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 14;"
                        +
                "-fx-border-radius: 14;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
        );


        Label comingSoon =
                new Label(
                        "This section is connected from the Admin shell."
                );


        comingSoon.setStyle(
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 15px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label description =
                new Label(
                        "The full functional page will replace this card in the next files."
                );


        description.setWrapText(
                true
        );


        description.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 13px;"
        );


        card
                .getChildren()
                .addAll(
                        comingSoon,
                        description
                );


        content
                .getChildren()
                .addAll(
                        title,
                        subtitle,
                        card
                );


        ScrollPane scrollPane =
                new ScrollPane(
                        content
                );


        scrollPane.setFitToWidth(
                true
        );


        scrollPane.setFitToHeight(
                true
        );


        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );


        scrollPane.setStyle(
                "-fx-background: "
                        + BACKGROUND
                        + ";"
                        +
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
                        +
                "-fx-border-color: transparent;"
        );


        featurePane.setCenter(
                scrollPane
        );
    }


    // =====================================================
    // =====================================================
    //
    // GENERIC PAGE LOADER
    //
    // Feature classes can also be pushed here directly.
    //
    // =====================================================
    // =====================================================


    public void showPage(
            Node page
    ) {

        if (page == null) {

            return;
        }


        featurePane.setCenter(
                page
        );
    }


    // =====================================================
    // =====================================================
    //
    // LOGOUT
    //
    // =====================================================
    // =====================================================

        private void logout() {

        SameStageAlert confirmation =
                new SameStageAlert(
                        SameStageAlert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Logout"
        );

        confirmation.setHeaderText(
                "Logout from Admin Console?"
        );

        confirmation.setContentText(
                "Your current Admin session will be closed."
        );

        confirmation
                .showAndWait()
                .ifPresent(
                        response -> {

                                if (
                                        response
                                                == javafx.scene.control.ButtonType.OK
                                ) {

                                // =========================================
                                // CLEAR AUTH / SESSION
                                // =========================================

                                authController.logout();


                                // =========================================
                                // RETURN TO MAIN LOGIN PAGE
                                // =========================================

                                MainLoginPage.navigateToMainLoginPage();
                                }
                        }
                );
        }

}