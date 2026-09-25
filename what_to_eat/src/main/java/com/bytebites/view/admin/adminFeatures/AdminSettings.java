package com.bytebites.view.admin.adminFeatures;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.bytebites.controller.AdminController;
import com.bytebites.model.Admin;
import com.bytebites.model.session.SessionManager;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;

import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminSettings {

    // =====================================================
    // COLORS
    // =====================================================

    private static final String BACKGROUND =
            "#0F172A";

    private static final String CARD_BACKGROUND =
            "#1E293B";

    private static final String CARD_BACKGROUND_ALT =
            "#121D35";

    private static final String PRIMARY_BLUE =
            "#F68F32";

    private static final String PRIMARY_BLUE_HOVER =
            "#E57D22";

    private static final String TEXT_PRIMARY =
            "#F8FAFC";

    private static final String TEXT_SECONDARY =
            "#94A3B8";

    private static final String BORDER =
            "#334155";

    private static final String GREEN =
            "#22C55E";

    private static final String GREEN_DARK =
            "#163B2A";

    private static final String RED =
            "#EF4444";

    private static final String RED_DARK =
            "#3B1720";


    // =====================================================
    // CONTROLLER
    // =====================================================

    private final AdminController adminController;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;


    // =====================================================
    // ADMIN DATA
    // =====================================================

    private Admin currentAdmin;


    // =====================================================
    // PROFILE UI
    // =====================================================

    private Label avatarLabel;

    private Label profileNameLabel;

    private Label profileEmailLabel;

    private Label profileStatusLabel;


    // =====================================================
    // FORM
    // =====================================================

    private TextField nameField;

    private TextField whatsappField;

    private TextField emailField;

    private TextField uidField;


    // =====================================================
    // ACCOUNT INFORMATION
    // =====================================================

    private Label roleValue;

    private Label statusValue;

    private Label createdAtValue;

    private Label updatedAtValue;

    private Label lastLoginValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button saveButton;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminSettings() {

        adminController =
                new AdminController();


        root =
                new BorderPane();


        buildPage();

        loadAdmin();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getSettingsPage() {

        return root;
    }


    // =====================================================
    // BUILD PAGE
    // =====================================================

    private void buildPage() {

        root.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        VBox content =
                new VBox(22);


        content.setPadding(
                new Insets(
                        28,
                        32,
                        40,
                        32
                )
        );


        content.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        content.getChildren()
                .addAll(
                        createHeader(),
                        createProfileCard(),
                        createEditProfileCard(),
                        createAccountInformationCard(),
                        createLoadingSection()
                );


        ScrollPane scrollPane =
                new ScrollPane(
                        content
                );


        scrollPane.setFitToWidth(
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


        root.setCenter(
                scrollPane
        );
    }


    // =====================================================
    // HEADER
    // =====================================================

    private Node createHeader() {

        HBox header =
                new HBox(20);


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox titleBox =
                new VBox(5);


        Label title =
                new Label(
                        "Settings"
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
                        "Manage your Admin profile and account information."
                );


        subtitle.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 14px;"
        );


        titleBox.getChildren()
                .addAll(
                        title,
                        subtitle
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        refreshButton =
                new Button(
                        "↻  Refresh"
                );


        refreshButton.setPrefHeight(
                40
        );


        refreshButton.setPadding(
                new Insets(
                        0,
                        18,
                        0,
                        18
                )
        );


        applyPrimaryButtonStyle(
                refreshButton
        );


        refreshButton.setOnAction(
                event ->
                        loadAdmin()
        );


        refreshButton.setOnMouseEntered(
                event ->
                        refreshButton.setStyle(
                                "-fx-background-color: "
                                        + PRIMARY_BLUE_HOVER
                                        + ";"
                                        +
                                "-fx-background-radius: 9;"
                                        +
                                "-fx-text-fill: white;"
                                        +
                                "-fx-font-size: 13px;"
                                        +
                                "-fx-font-weight: bold;"
                                        +
                                "-fx-cursor: hand;"
                        )
        );


        refreshButton.setOnMouseExited(
                event ->
                        applyPrimaryButtonStyle(
                                refreshButton
                        )
        );


        header.getChildren()
                .addAll(
                        titleBox,
                        spacer,
                        refreshButton
                );


        return header;
    }


    // =====================================================
    // PROFILE CARD
    // =====================================================

    private Node createProfileCard() {

        HBox card =
                new HBox(18);


        card.setAlignment(
                Pos.CENTER_LEFT
        );


        card.setPadding(
                new Insets(
                        22
                )
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 14;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 14;"
        );


        // =================================================
        // AVATAR
        // =================================================

        StackPane avatar =
                new StackPane();


        avatar.setPrefSize(
                72,
                72
        );


        avatar.setMinSize(
                72,
                72
        );


        avatar.setMaxSize(
                72,
                72
        );


        avatar.setStyle(
                "-fx-background-color: "
                        + PRIMARY_BLUE
                        + ";"
                        +
                "-fx-background-radius: 36;"
        );


        avatarLabel =
                new Label(
                        "A"
                );


        avatarLabel.setStyle(
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 28px;"
                        +
                "-fx-font-weight: bold;"
        );


        avatar.getChildren()
                .add(
                        avatarLabel
                );


        // =================================================
        // IDENTITY
        // =================================================

        VBox identity =
                new VBox(5);


        profileNameLabel =
                new Label(
                        "Admin"
                );


        profileNameLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 21px;"
                        +
                "-fx-font-weight: bold;"
        );


        profileEmailLabel =
                new Label(
                        "Loading..."
                );


        profileEmailLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 13px;"
        );


        identity.getChildren()
                .addAll(
                        profileNameLabel,
                        profileEmailLabel
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        profileStatusLabel =
                new Label(
                        "ACTIVE"
                );


        profileStatusLabel.setPadding(
                new Insets(
                        5,
                        11,
                        5,
                        11
                )
        );


        applyActiveBadgeStyle(
                profileStatusLabel
        );


        card.getChildren()
                .addAll(
                        avatar,
                        identity,
                        spacer,
                        profileStatusLabel
                );


        return card;
    }


    // =====================================================
    // EDIT PROFILE CARD
    // =====================================================

    private Node createEditProfileCard() {

        VBox card =
                new VBox(17);


        card.setPadding(
                new Insets(
                        22
                )
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 14;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 14;"
        );


        Label title =
                new Label(
                        "Admin Profile"
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        "Update the profile information used inside the Admin panel."
                );


        subtitle.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        Separator separator =
                new Separator();


        // =================================================
        // NAME
        // =================================================

        nameField =
                createTextField(
                        "Admin Name"
                );


        // =================================================
        // WHATSAPP
        // =================================================

        whatsappField =
                createTextField(
                        "WhatsApp Number"
                );


        // =================================================
        // EMAIL
        // =================================================

        emailField =
                createTextField(
                        "Email"
                );


        emailField.setEditable(
                false
        );


        emailField.setDisable(
                true
        );


        // =================================================
        // UID
        // =================================================

        uidField =
                createTextField(
                        "Admin UID"
                );


        uidField.setEditable(
                false
        );


        uidField.setDisable(
                true
        );


        HBox firstRow =
                new HBox(16);


        VBox nameBox =
                createFormGroup(
                        "Name",
                        nameField
                );


        VBox whatsappBox =
                createFormGroup(
                        "WhatsApp Number",
                        whatsappField
                );


        HBox.setHgrow(
                nameBox,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                whatsappBox,
                Priority.ALWAYS
        );


        nameBox.setMaxWidth(
                Double.MAX_VALUE
        );


        whatsappBox.setMaxWidth(
                Double.MAX_VALUE
        );


        firstRow.getChildren()
                .addAll(
                        nameBox,
                        whatsappBox
                );


        HBox secondRow =
                new HBox(16);


        VBox emailBox =
                createFormGroup(
                        "Email",
                        emailField
                );


        VBox uidBox =
                createFormGroup(
                        "Admin UID",
                        uidField
                );


        HBox.setHgrow(
                emailBox,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                uidBox,
                Priority.ALWAYS
        );


        emailBox.setMaxWidth(
                Double.MAX_VALUE
        );


        uidBox.setMaxWidth(
                Double.MAX_VALUE
        );


        secondRow.getChildren()
                .addAll(
                        emailBox,
                        uidBox
                );


        // =================================================
        // SAVE
        // =================================================

        HBox actions =
                new HBox();


        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        saveButton =
                new Button(
                        "Save Changes"
                );


        saveButton.setPrefHeight(
                40
        );


        saveButton.setPadding(
                new Insets(
                        0,
                        18,
                        0,
                        18
                )
        );


        applyPrimaryButtonStyle(
                saveButton
        );


        saveButton.setOnAction(
                event ->
                        saveProfile()
        );


        actions.getChildren()
                .add(
                        saveButton
                );


        card.getChildren()
                .addAll(
                        title,
                        subtitle,
                        separator,
                        firstRow,
                        secondRow,
                        actions
                );


        return card;
    }


    // =====================================================
    // ACCOUNT INFORMATION
    // =====================================================

    private Node createAccountInformationCard() {

        VBox card =
                new VBox(16);


        card.setPadding(
                new Insets(
                        22
                )
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 14;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 14;"
        );


        Label title =
                new Label(
                        "Account Information"
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        "System-level information for the currently logged-in Admin account."
                );


        subtitle.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        Separator separator =
                new Separator();


        roleValue =
                createAccountValue();


        statusValue =
                createAccountValue();


        createdAtValue =
                createAccountValue();


        updatedAtValue =
                createAccountValue();


        lastLoginValue =
                createAccountValue();


        HBox firstRow =
                new HBox(16);


        VBox role =
                createAccountInfoBox(
                        "ROLE",
                        roleValue
                );


        VBox status =
                createAccountInfoBox(
                        "STATUS",
                        statusValue
                );


        VBox created =
                createAccountInfoBox(
                        "ACCOUNT CREATED",
                        createdAtValue
                );


        HBox.setHgrow(
                role,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                status,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                created,
                Priority.ALWAYS
        );


        role.setMaxWidth(
                Double.MAX_VALUE
        );


        status.setMaxWidth(
                Double.MAX_VALUE
        );


        created.setMaxWidth(
                Double.MAX_VALUE
        );


        firstRow.getChildren()
                .addAll(
                        role,
                        status,
                        created
                );


        HBox secondRow =
                new HBox(16);


        VBox updated =
                createAccountInfoBox(
                        "LAST UPDATED",
                        updatedAtValue
                );


        VBox lastLogin =
                createAccountInfoBox(
                        "LAST LOGIN",
                        lastLoginValue
                );


        HBox.setHgrow(
                updated,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                lastLogin,
                Priority.ALWAYS
        );


        updated.setMaxWidth(
                Double.MAX_VALUE
        );


        lastLogin.setMaxWidth(
                Double.MAX_VALUE
        );


        secondRow.getChildren()
                .addAll(
                        updated,
                        lastLogin
                );


        card.getChildren()
                .addAll(
                        title,
                        subtitle,
                        separator,
                        firstRow,
                        secondRow
                );


        return card;
    }


    // =====================================================
    // LOADING
    // =====================================================

    private Node createLoadingSection() {

        HBox box =
                new HBox(10);


        box.setAlignment(
                Pos.CENTER_LEFT
        );


        loadingIndicator =
                new ProgressIndicator();


        loadingIndicator.setPrefSize(
                20,
                20
        );


        loadingIndicator.setMaxSize(
                20,
                20
        );


        loadingLabel =
                new Label(
                        "Loading Admin profile..."
                );


        loadingLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        box.getChildren()
                .addAll(
                        loadingIndicator,
                        loadingLabel
                );


        return box;
    }


    // =====================================================
    // LOAD ADMIN
    // =====================================================

    private void loadAdmin() {

        String uid =
                SessionManager.getUid();


        if (
                uid == null
                        ||
                uid.trim().isEmpty()
        ) {

            setLoading(
                    false,
                    "Admin session not found."
            );


            showError(
                    "No logged-in Admin session was found."
            );

            return;
        }


        setLoading(
                true,
                "Loading Admin profile..."
        );


        Task<Admin> task =
                new Task<>() {

                    @Override
                    protected Admin call() {

                        return adminController
                                .getAdmin(
                                        uid
                                );
                    }
                };


        task.setOnSucceeded(
                event -> {

                    Admin admin =
                            task.getValue();


                    if (
                            admin == null
                    ) {

                        setLoading(
                                false,
                                "Admin profile not found."
                        );


                        showError(
                                "The Admin document could not be found."
                        );

                        return;
                    }


                    currentAdmin =
                            admin;


                    populateAdmin(
                            admin
                    );


                    setLoading(
                            false,
                            "Admin profile loaded."
                    );
                }
        );


        task.setOnFailed(
                event -> {

                    if (
                            task.getException()
                                    != null
                    ) {

                        task.getException()
                                .printStackTrace();
                    }


                    setLoading(
                            false,
                            "Unable to load Admin profile."
                    );


                    showError(
                            "Unable to load the Admin profile from Firestore."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // POPULATE ADMIN
    // =====================================================

    private void populateAdmin(
            Admin admin
    ) {

        String name =
                value(
                        admin.getName(),
                        "Admin"
                );


        String email =
                value(
                        admin.getEmail(),
                        "Not available"
                );


        String status =
                value(
                        admin.getStatus(),
                        "ACTIVE"
                );


        String role =
                value(
                        admin.getRole(),
                        "ADMIN"
                );


        // =================================================
        // PROFILE CARD
        // =================================================

        profileNameLabel.setText(
                name
        );


        profileEmailLabel.setText(
                email
        );


        avatarLabel.setText(
                firstLetter(
                        name
                )
        );


        profileStatusLabel.setText(
                status.toUpperCase()
        );


        if (
                status.equalsIgnoreCase(
                        "ACTIVE"
                )
        ) {

            applyActiveBadgeStyle(
                    profileStatusLabel
            );

        } else {

            applySuspendedBadgeStyle(
                    profileStatusLabel
            );
        }


        // =================================================
        // FORM
        // =================================================

        nameField.setText(
                value(
                        admin.getName(),
                        ""
                )
        );


        whatsappField.setText(
                value(
                        admin.getWhatsappNumber(),
                        ""
                )
        );


        emailField.setText(
                value(
                        admin.getEmail(),
                        ""
                )
        );


        uidField.setText(
                value(
                        admin.getUid(),
                        ""
                )
        );


        // =================================================
        // ACCOUNT INFO
        // =================================================

        roleValue.setText(
                role
        );


        statusValue.setText(
                status
        );


        createdAtValue.setText(
                formatDateTime(
                        admin.getCreatedAt()
                )
        );


        updatedAtValue.setText(
                formatDateTime(
                        admin.getUpdatedAt()
                )
        );


        lastLoginValue.setText(
                formatDateTime(
                        admin.getLastLoginAt()
                )
        );
    }


    // =====================================================
    // SAVE PROFILE
    // =====================================================

    private void saveProfile() {

        if (
                currentAdmin == null
        ) {

            showError(
                    "Admin profile has not been loaded yet."
            );

            return;
        }


        String name =
                nameField
                        .getText()
                        .trim();


        String whatsapp =
                whatsappField
                        .getText()
                        .trim();


        if (
                name.isEmpty()
        ) {

            showError(
                    "Admin name cannot be empty."
            );

            return;
        }


        if (
                !isValidWhatsapp(
                        whatsapp
                )
        ) {

            showError(
                    "Enter a valid WhatsApp number or leave it empty."
            );

            return;
        }


        boolean confirmed =
                confirm(
                        "Save Admin Profile",
                        "Save these profile changes?",
                        "The Admin name and WhatsApp number will be updated."
                );


        if (
                !confirmed
        ) {

            return;
        }


        setLoading(
                true,
                "Updating Admin profile..."
        );


        Task<Boolean> task =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return adminController
                                .updateAdminProfile(
                                        currentAdmin.getUid(),
                                        name,
                                        whatsapp
                                );
                    }
                };


        task.setOnSucceeded(
                event -> {

                    if (
                            Boolean.TRUE.equals(
                                    task.getValue()
                            )
                    ) {

                        // =====================================
                        // UPDATE CURRENT SESSION NAME
                        // =====================================

                        SessionManager.setName(
                                name
                        );


                        showInformation(
                                "Admin profile updated successfully."
                        );


                        loadAdmin();

                    } else {

                        setLoading(
                                false,
                                "Unable to update Admin profile."
                        );


                        showError(
                                "Admin profile could not be updated."
                        );
                    }
                }
        );


        task.setOnFailed(
                event -> {

                    if (
                            task.getException()
                                    != null
                    ) {

                        task.getException()
                                .printStackTrace();
                    }


                    setLoading(
                            false,
                            "Unable to update Admin profile."
                    );


                    showError(
                            "An error occurred while updating the Admin profile."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // FORM GROUP
    // =====================================================

    private VBox createFormGroup(
            String titleText,
            TextField field
    ) {

        VBox box =
                new VBox(7);


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 11px;"
                        +
                "-fx-font-weight: bold;"
        );


        VBox.setVgrow(
                field,
                Priority.NEVER
        );


        field.setMaxWidth(
                Double.MAX_VALUE
        );


        box.getChildren()
                .addAll(
                        title,
                        field
                );


        return box;
    }


    // =====================================================
    // TEXT FIELD
    // =====================================================

    private TextField createTextField(
            String prompt
    ) {

        TextField field =
                new TextField();


        field.setPromptText(
                prompt
        );


        field.setPrefHeight(
                42
        );


        field.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-prompt-text-fill: #64748B;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 8;"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-padding: 0 12 0 12;"
        );


        return field;
    }


    // =====================================================
    // ACCOUNT BOX
    // =====================================================

    private VBox createAccountInfoBox(
            String titleText,
            Label value
    ) {

        VBox box =
                new VBox(6);


        box.setPadding(
                new Insets(
                        15
                )
        );


        box.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 10;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 10;"
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 10px;"
                        +
                "-fx-font-weight: bold;"
        );


        box.getChildren()
                .addAll(
                        title,
                        value
                );


        return box;
    }


    // =====================================================
    // ACCOUNT VALUE
    // =====================================================

    private Label createAccountValue() {

        Label label =
                new Label(
                        "—"
                );


        label.setWrapText(
                true
        );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 13px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    // =====================================================
    // BADGES
    // =====================================================

    private void applyActiveBadgeStyle(
            Label badge
    ) {

        badge.setStyle(
                "-fx-background-color: "
                        + GREEN_DARK
                        + ";"
                        +
                "-fx-background-radius: 20;"
                        +
                "-fx-text-fill: "
                        + GREEN
                        + ";"
                        +
                "-fx-font-size: 11px;"
                        +
                "-fx-font-weight: bold;"
        );
    }


    private void applySuspendedBadgeStyle(
            Label badge
    ) {

        badge.setStyle(
                "-fx-background-color: "
                        + RED_DARK
                        + ";"
                        +
                "-fx-background-radius: 20;"
                        +
                "-fx-text-fill: "
                        + RED
                        + ";"
                        +
                "-fx-font-size: 11px;"
                        +
                "-fx-font-weight: bold;"
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
                        + PRIMARY_BLUE
                        + ";"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 13px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );
    }


    // =====================================================
    // CONFIRMATION
    // =====================================================

    private boolean confirm(
            String title,
            String header,
            String content
    ) {

        SameStageAlert alert =
                new SameStageAlert(
                        SameStageAlert.AlertType.CONFIRMATION
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(
                header
        );


        alert.setContentText(
                content
        );


        Optional<ButtonType> result =
                alert.showAndWait();


        return result.isPresent()
                &&
                result.get()
                        == ButtonType.OK;
    }


    // =====================================================
    // INFORMATION
    // =====================================================

    private void showInformation(
            String message
    ) {

        SameStageAlert alert =
                new SameStageAlert(
                        SameStageAlert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Admin"
        );


        alert.setHeaderText(
                null
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            String message
    ) {

        SameStageAlert alert =
                new SameStageAlert(
                        SameStageAlert.AlertType.ERROR
                );


        alert.setTitle(
                "Admin"
        );


        alert.setHeaderText(
                "Something went wrong"
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =====================================================
    // LOADING STATE
    // =====================================================

    private void setLoading(
            boolean loading,
            String message
    ) {

        Platform.runLater(
                () -> {

                    loadingIndicator.setVisible(
                            loading
                    );


                    loadingIndicator.setManaged(
                            loading
                    );


                    loadingLabel.setText(
                            message
                    );


                    if (
                            saveButton != null
                    ) {

                        saveButton.setDisable(
                                loading
                        );
                    }


                    if (
                            refreshButton != null
                    ) {

                        refreshButton.setDisable(
                                loading
                        );
                    }
                }
        );
    }


    // =====================================================
    // START TASK
    // =====================================================

    private void startTask(
            Task<?> task
    ) {

        Thread thread =
                new Thread(
                        task
                );


        thread.setDaemon(
                true
        );


        thread.start();
    }


    // =====================================================
    // HELPERS
    // =====================================================

    private String value(
            String text,
            String fallback
    ) {

        if (
                text == null
                        ||
                text.trim().isEmpty()
        ) {

            return fallback;
        }


        return text.trim();
    }


    private String firstLetter(
            String text
    ) {

        if (
                text == null
                        ||
                text.trim().isEmpty()
        ) {

            return "A";
        }


        return text
                .trim()
                .substring(
                        0,
                        1
                )
                .toUpperCase();
    }


    // =====================================================
    // WHATSAPP VALIDATION
    // =====================================================

    private boolean isValidWhatsapp(
            String whatsapp
    ) {

        if (
                whatsapp == null
                        ||
                whatsapp.trim().isEmpty()
        ) {

            return true;
        }


        String cleaned =
                whatsapp
                        .replace(
                                " ",
                                ""
                        )
                        .replace(
                                "-",
                                ""
                        );


        return cleaned.matches(
                "\\+?[0-9]{7,15}"
        );
    }


    // =====================================================
    // DATE FORMAT
    // =====================================================

    private String formatDateTime(
            long timestamp
    ) {

        if (
                timestamp <= 0
        ) {

            return "Not available";
        }


        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a"
                );


        return formatter.format(
                new Date(
                        timestamp
                )
        );
    }
}