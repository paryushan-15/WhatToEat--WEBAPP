package com.bytebites.view.admin.adminFeatures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bytebites.controller.AdminController;
import com.bytebites.dao.UserDao;
import com.bytebites.model.User;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;

import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import com.bytebites.view.common.SameStageDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdminUsers {

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

    private static final String AMBER =
            "#F59E0B";


    // =====================================================
    // CONTROLLERS / DAO
    // =====================================================

    private final AdminController adminController;

    /*
     * UserDao is used for profile editing because
     * AdminController currently handles account actions,
     * while UserDao contains updateUserProfile().
     */
    private final UserDao userDao;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;


    // =====================================================
    // USER LIST
    // =====================================================

    private final VBox userListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<User> allUsers;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> statusFilter;


    // =====================================================
    // HEADER STATS
    // =====================================================

    private Label totalUsersLabel;

    private Label activeUsersLabel;

    private Label suspendedUsersLabel;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminUsers() {

        adminController =
                new AdminController();

        userDao =
                new UserDao();

        allUsers =
                new ArrayList<>();

        root =
                new BorderPane();

        userListBox =
                new VBox(14);

        buildPage();

        loadUsers();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getUsersPage() {

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


        // =================================================
        // HEADER
        // =================================================

        content
                .getChildren()
                .add(
                        createHeader()
                );


        // =================================================
        // STATS
        // =================================================

        content
                .getChildren()
                .add(
                        createStatsSection()
                );


        // =================================================
        // FILTERS
        // =================================================

        content
                .getChildren()
                .add(
                        createFilterSection()
                );


        // =================================================
        // LOADING
        // =================================================

        content
                .getChildren()
                .add(
                        createLoadingSection()
                );


        // =================================================
        // USER CARDS
        // =================================================

        userListBox.setFillWidth(
                true
        );


        content
                .getChildren()
                .add(
                        userListBox
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
                        "Users"
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
                        "Manage registered users, account status and profile information."
                );


        subtitle.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 14px;"
        );


        titleBox
                .getChildren()
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
                        loadUsers()
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


        header
                .getChildren()
                .addAll(
                        titleBox,
                        spacer,
                        refreshButton
                );


        return header;
    }


    // =====================================================
    // STATS SECTION
    // =====================================================

    private Node createStatsSection() {

        HBox stats =
                new HBox(15);


        totalUsersLabel =
                createStatValueLabel();


        activeUsersLabel =
                createStatValueLabel();


        suspendedUsersLabel =
                createStatValueLabel();


        VBox totalCard =
                createStatCard(
                        "Total Users",
                        totalUsersLabel,
                        "All registered users",
                        PRIMARY_BLUE
                );


        VBox activeCard =
                createStatCard(
                        "Active Users",
                        activeUsersLabel,
                        "Accounts with platform access",
                        GREEN
                );


        VBox suspendedCard =
                createStatCard(
                        "Suspended Users",
                        suspendedUsersLabel,
                        "Accounts blocked by Admin",
                        RED
                );


        HBox.setHgrow(
                totalCard,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                activeCard,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                suspendedCard,
                Priority.ALWAYS
        );


        totalCard.setMaxWidth(
                Double.MAX_VALUE
        );


        activeCard.setMaxWidth(
                Double.MAX_VALUE
        );


        suspendedCard.setMaxWidth(
                Double.MAX_VALUE
        );


        stats
                .getChildren()
                .addAll(
                        totalCard,
                        activeCard,
                        suspendedCard
                );


        return stats;
    }


    // =====================================================
    // STAT CARD
    // =====================================================

    private VBox createStatCard(
            String titleText,
            Label value,
            String descriptionText,
            String accent
    ) {

        VBox card =
                new VBox(7);


        card.setPadding(
                new Insets(
                        18
                )
        );


        card.setMinHeight(
                110
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-radius: 13;"
                        +
                "-fx-border-radius: 13;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
        );


        value.setStyle(
                "-fx-text-fill: "
                        + accent
                        + ";"
                        +
                "-fx-font-size: 25px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label description =
                new Label(
                        descriptionText
                );


        description.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 11px;"
        );


        card
                .getChildren()
                .addAll(
                        title,
                        value,
                        description
                );


        return card;
    }


    // =====================================================
    // FILTER SECTION
    // =====================================================

    private Node createFilterSection() {

        HBox filters =
                new HBox(12);


        filters.setAlignment(
                Pos.CENTER_LEFT
        );


        searchField =
                new TextField();


        searchField.setPromptText(
                "Search by name, email, UID or WhatsApp..."
        );


        searchField.setPrefHeight(
                42
        );


        searchField.setPrefWidth(
                380
        );


        searchField.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-text-fill: white;"
                        +
                "-fx-prompt-text-fill: #64748B;"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-border-radius: 9;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-padding: 0 13 0 13;"
        );


        statusFilter =
                new ComboBox<>();


        statusFilter
                .getItems()
                .addAll(
                        "All",
                        "Active",
                        "Suspended"
                );


        statusFilter.setValue(
                "All"
        );


        statusFilter.setPrefWidth(
                160
        );


        statusFilter.setPrefHeight(
                42
        );


        statusFilter.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 9;"
                        +
                "-fx-background-radius: 9;"
        );


        searchField
                .textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                applyFilters()
                );


        statusFilter.setOnAction(
                event ->
                        applyFilters()
        );


        filters
                .getChildren()
                .addAll(
                        searchField,
                        statusFilter
                );


        return filters;
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
                        "Loading users..."
                );


        loadingLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        box
                .getChildren()
                .addAll(
                        loadingIndicator,
                        loadingLabel
                );


        return box;
    }


    // =====================================================
    // LOAD USERS
    // =====================================================

    private void loadUsers() {

        setLoading(
                true,
                "Loading users..."
        );


        Task<List<User>> task =
                new Task<>() {

                    @Override
                    protected List<User> call() {

                        return adminController
                                .getAllUsers();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<User> loaded =
                            task.getValue();


                    if (loaded == null) {

                        loaded =
                                new ArrayList<>();
                    }


                    allUsers =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allUsers.size()
                                    + " user(s) loaded."
                    );
                }
        );


        task.setOnFailed(
                event -> {

                    Throwable error =
                            task.getException();


                    if (error != null) {

                        error.printStackTrace();
                    }


                    setLoading(
                            false,
                            "Unable to load users."
                    );


                    showError(
                            "Unable to load users from the database."
                    );
                }
        );


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
    // FILTER USERS
    // =====================================================

    private void applyFilters() {

        if (
                searchField == null
                        ||
                statusFilter == null
        ) {

            return;
        }


        String search =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String status =
                statusFilter
                        .getValue();


        List<User> filtered =
                new ArrayList<>();


        for (
                User user :
                allUsers
        ) {

            if (user == null) {

                continue;
            }


            // =================================================
            // SEARCH
            // =================================================

            boolean matchesSearch =
                    search.isEmpty()
                            ||
                    contains(
                            user.getName(),
                            search
                    )
                            ||
                    contains(
                            user.getEmail(),
                            search
                    )
                            ||
                    contains(
                            user.getUid(),
                            search
                    )
                            ||
                    contains(
                            user.getWhatsappNumber(),
                            search
                    );


            if (!matchesSearch) {

                continue;
            }


            // =================================================
            // STATUS
            // =================================================

            boolean matchesStatus =
                    status == null
                            ||
                    status.equalsIgnoreCase(
                            "All"
                    )
                            ||
                    (
                            status.equalsIgnoreCase(
                                    "Active"
                            )
                                    &&
                            user.isActive()
                    )
                            ||
                    (
                            status.equalsIgnoreCase(
                                    "Suspended"
                            )
                                    &&
                            user.isSuspended()
                    );


            if (
                    matchesStatus
            ) {

                filtered.add(
                        user
                );
            }
        }


        displayUsers(
                filtered
        );
    }


    // =====================================================
    // DISPLAY USERS
    // =====================================================

    private void displayUsers(
            List<User> users
    ) {

        userListBox
                .getChildren()
                .clear();


        if (
                users == null
                        ||
                users.isEmpty()
        ) {

            userListBox
                    .getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                User user :
                users
        ) {

            userListBox
                    .getChildren()
                    .add(
                            createUserCard(
                                    user
                            )
                    );
        }
    }


    // =====================================================
    // USER CARD
    // =====================================================

    private VBox createUserCard(
            User user
    ) {

        VBox card =
                new VBox(15);


        card.setPadding(
                new Insets(
                        20
                )
        );


        card.setMaxWidth(
                Double.MAX_VALUE
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


        // =================================================
        // TOP
        // =================================================

        HBox top =
                new HBox(12);


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox nameBox =
                new VBox(4);


        Label name =
                new Label(
                        valueOrFallback(
                                user.getName(),
                                "Unnamed User"
                        )
                );


        name.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 17px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label email =
                new Label(
                        valueOrFallback(
                                user.getEmail(),
                                "No email"
                        )
                );


        email.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        nameBox
                .getChildren()
                .addAll(
                        name,
                        email
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label status =
                createStatusBadge(
                        user
                );


        top
                .getChildren()
                .addAll(
                        nameBox,
                        spacer,
                        status
                );


        // =================================================
        // DIVIDER
        // =================================================

        Separator separator =
                new Separator();


        separator.setStyle(
                "-fx-background-color: "
                        + BORDER
                        + ";"
        );


        // =================================================
        // INFORMATION
        // =================================================

        HBox info =
                new HBox(25);


        info.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox uidInfo =
                createInfoField(
                        "USER UID",
                        valueOrFallback(
                                user.getUid(),
                                "—"
                        )
                );


        VBox whatsappInfo =
                createInfoField(
                        "WHATSAPP",
                        valueOrFallback(
                                user.getWhatsappNumber(),
                                "Not provided"
                        )
                );


        VBox joinedInfo =
                createInfoField(
                        "JOINED",
                        formatDate(
                                user.getCreatedAt()
                        )
                );


        VBox loginInfo =
                createInfoField(
                        "LAST LOGIN",
                        formatDateTime(
                                user.getLastLoginAt()
                        )
                );


        HBox.setHgrow(
                uidInfo,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                whatsappInfo,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                joinedInfo,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                loginInfo,
                Priority.ALWAYS
        );


        info
                .getChildren()
                .addAll(
                        uidInfo,
                        whatsappInfo,
                        joinedInfo,
                        loginInfo
                );


        // =================================================
        // BUTTONS
        // =================================================

        HBox actions =
                new HBox(10);


        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button viewButton =
                createSecondaryButton(
                        "View Details"
                );


        Button editButton =
                createSecondaryButton(
                        "Edit Profile"
                );


        Button statusButton;


        if (
                user.isSuspended()
        ) {

            statusButton =
                    createGreenButton(
                            "Activate"
                    );


            statusButton.setOnAction(
                    event ->
                            activateUser(
                                    user
                            )
            );

        } else {

            statusButton =
                    createWarningButton(
                            "Suspend"
                    );


            statusButton.setOnAction(
                    event ->
                            suspendUser(
                                    user
                            )
            );
        }


        Button deleteButton =
                createDeleteButton(
                        "Delete"
                );


        viewButton.setOnAction(
                event ->
                        showUserDetails(
                                user
                        )
        );


        editButton.setOnAction(
                event ->
                        showEditUserDialog(
                                user
                        )
        );


        deleteButton.setOnAction(
                event ->
                        deleteUser(
                                user
                        )
        );


        actions
                .getChildren()
                .addAll(
                        viewButton,
                        editButton,
                        statusButton,
                        deleteButton
                );


        card
                .getChildren()
                .addAll(
                        top,
                        separator,
                        info,
                        actions
                );


        return card;
    }


    // =====================================================
    // STATUS BADGE
    // =====================================================

    private Label createStatusBadge(
            User user
    ) {

        boolean suspended =
                user.isSuspended();


        Label badge =
                new Label(
                        suspended
                                ? "SUSPENDED"
                                : "ACTIVE"
                );


        badge.setPadding(
                new Insets(
                        5,
                        11,
                        5,
                        11
                )
        );


        if (suspended) {

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

        } else {

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


        return badge;
    }


    // =====================================================
    // INFORMATION FIELD
    // =====================================================

    private VBox createInfoField(
            String titleText,
            String valueText
    ) {

        VBox box =
                new VBox(4);


        box.setMinWidth(
                150
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


        Label value =
                new Label(
                        valueText
                );


        value.setWrapText(
                true
        );


        value.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: 600;"
        );


        box
                .getChildren()
                .addAll(
                        title,
                        value
                );


        return box;
    }


    // =====================================================
    // VIEW DETAILS
    // =====================================================

    private void showUserDetails(
            User user
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "User Details"
        );


        dialog.setHeaderText(
                valueOrFallback(
                        user.getName(),
                        "User"
                )
        );


        ButtonType closeButton =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog
                .getDialogPane()
                .getButtonTypes()
                .add(
                        closeButton
                );


        VBox content =
                new VBox(14);


        content.setPadding(
                new Insets(
                        15
                )
        );


        content.setPrefWidth(
                520
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        content
                .getChildren()
                .addAll(
                        createDetailRow(
                                "Name",
                                user.getName()
                        ),

                        createDetailRow(
                                "Email",
                                user.getEmail()
                        ),

                        createDetailRow(
                                "UID",
                                user.getUid()
                        ),

                        createDetailRow(
                                "Role",
                                valueOrFallback(
                                        user.getRole(),
                                        "USER"
                                )
                        ),

                        createDetailRow(
                                "Status",
                                user.isSuspended()
                                        ? "SUSPENDED"
                                        : "ACTIVE"
                        ),

                        createDetailRow(
                                "WhatsApp",
                                user.getWhatsappNumber()
                        ),

                        createDetailRow(
                                "Joined",
                                formatDateTime(
                                        user.getCreatedAt()
                                )
                        ),

                        createDetailRow(
                                "Last Updated",
                                formatDateTime(
                                        user.getUpdatedAt()
                                )
                        ),

                        createDetailRow(
                                "Last Login",
                                formatDateTime(
                                        user.getLastLoginAt()
                                )
                        )
                );


        dialog
                .getDialogPane()
                .setContent(
                        content
                );


        dialog
                .getDialogPane()
                .setStyle(
                        "-fx-background-color: "
                                + CARD_BACKGROUND
                                + ";"
                );


        dialog.showAndWait();
    }


    // =====================================================
    // DETAIL ROW
    // =====================================================

    private Node createDetailRow(
            String titleText,
            String valueText
    ) {

        HBox row =
                new HBox(15);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        Label title =
                new Label(
                        titleText
                );


        title.setMinWidth(
                120
        );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label value =
                new Label(
                        valueOrFallback(
                                valueText,
                                "—"
                        )
                );


        value.setWrapText(
                true
        );


        value.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 13px;"
        );


        row
                .getChildren()
                .addAll(
                        title,
                        value
                );


        return row;
    }


    // =====================================================
    // EDIT USER PROFILE
    // =====================================================

    private void showEditUserDialog(
            User user
    ) {

        SameStageDialog<ButtonType> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Edit User"
        );


        dialog.setHeaderText(
                "Edit "
                        + valueOrFallback(
                                user.getName(),
                                "User"
                        )
                + "'s profile"
        );


        ButtonType saveButton =
                new ButtonType(
                        "Save Changes",
                        ButtonBar.ButtonData.OK_DONE
                );


        ButtonType cancelButton =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog
                .getDialogPane()
                .getButtonTypes()
                .addAll(
                        saveButton,
                        cancelButton
                );


        TextField nameField =
                createDialogTextField(
                        "Name"
                );


        nameField.setText(
                valueOrFallback(
                        user.getName(),
                        ""
                )
        );


        TextField whatsappField =
                createDialogTextField(
                        "WhatsApp Number"
                );


        whatsappField.setText(
                valueOrFallback(
                        user.getWhatsappNumber(),
                        ""
                )
        );


        TextField emailField =
                createDialogTextField(
                        "Email"
                );


        emailField.setText(
                valueOrFallback(
                        user.getEmail(),
                        ""
                )
        );


        /*
         * Authentication email is not being changed here,
         * because changing Firebase Authentication email
         * requires a separate Admin Auth operation.
         */

        emailField.setEditable(
                false
        );


        emailField.setDisable(
                true
        );


        VBox fields =
                new VBox(12);


        fields.setPadding(
                new Insets(
                        15
                )
        );


        fields.setPrefWidth(
                450
        );


        fields.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        fields
                .getChildren()
                .addAll(
                        createFormLabel(
                                "Name"
                        ),

                        nameField,

                        createFormLabel(
                                "Email"
                        ),

                        emailField,

                        createFormLabel(
                                "WhatsApp Number"
                        ),

                        whatsappField
                );


        dialog
                .getDialogPane()
                .setContent(
                        fields
                );


        dialog
                .getDialogPane()
                .setStyle(
                        "-fx-background-color: "
                                + CARD_BACKGROUND
                                + ";"
                );


        Optional<ButtonType> result =
                dialog.showAndWait();


        if (
                result.isPresent()
                        &&
                result.get()
                        == saveButton
        ) {

            String name =
                    nameField
                            .getText()
                            .trim();


            String whatsapp =
                    whatsappField
                            .getText()
                            .trim();


            if (name.isEmpty()) {

                showError(
                        "User name cannot be empty."
                );

                return;
            }


            updateUserProfile(
                    user,
                    name,
                    whatsapp
            );
        }
    }


    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    private void updateUserProfile(
            User user,
            String name,
            String whatsapp
    ) {

        setLoading(
                true,
                "Updating user profile..."
        );


        Task<Boolean> task =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return userDao
                                .updateUserProfile(
                                        user.getUid(),
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

                        showInformation(
                                "User profile updated successfully."
                        );


                        loadUsers();

                    } else {

                        setLoading(
                                false,
                                "Unable to update user."
                        );


                        showError(
                                "User profile could not be updated."
                        );
                    }
                }
        );


        task.setOnFailed(
                event -> {

                    setLoading(
                            false,
                            "Unable to update user."
                    );


                    if (
                            task.getException()
                                    != null
                    ) {

                        task
                                .getException()
                                .printStackTrace();
                    }


                    showError(
                            "An error occurred while updating the user."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // SUSPEND USER
    // =====================================================

    private void suspendUser(
            User user
    ) {

        boolean confirmed =
                showConfirmation(
                        "Suspend User",
                        "Suspend "
                                + valueOrFallback(
                                        user.getName(),
                                        "this user"
                                )
                                + "?",
                        "The user will no longer be allowed to access the application."
                );


        if (!confirmed) {

            return;
        }


        executeAccountAction(
                "Suspending user...",
                () ->
                        adminController
                                .suspendUser(
                                        user.getUid()
                                ),
                "User suspended successfully.",
                "Unable to suspend user."
        );
    }


    // =====================================================
    // ACTIVATE USER
    // =====================================================

    private void activateUser(
            User user
    ) {

        boolean confirmed =
                showConfirmation(
                        "Activate User",
                        "Reactivate "
                                + valueOrFallback(
                                        user.getName(),
                                        "this user"
                                )
                                + "?",
                        "The user will regain access to the application."
                );


        if (!confirmed) {

            return;
        }


        executeAccountAction(
                "Activating user...",
                () ->
                        adminController
                                .activateUser(
                                        user.getUid()
                                ),
                "User activated successfully.",
                "Unable to activate user."
        );
    }


    // =====================================================
    // DELETE USER
    // =====================================================

    private void deleteUser(
            User user
    ) {

        boolean confirmed =
                showConfirmation(
                        "Delete User",
                        "Delete "
                                + valueOrFallback(
                                        user.getName(),
                                        "this user"
                                )
                                + "?",
                        "This currently deletes the user's Firestore profile. This action cannot be undone."
                );


        if (!confirmed) {

            return;
        }


        executeAccountAction(
                "Deleting user...",
                () ->
                        adminController
                                .deleteUser(
                                        user.getUid()
                                ),
                "User profile deleted successfully.",
                "Unable to delete user."
        );
    }


    // =====================================================
    // GENERIC ACCOUNT ACTION
    // =====================================================

    private void executeAccountAction(
            String loadingMessage,
            UserAction action,
            String successMessage,
            String failureMessage
    ) {

        setLoading(
                true,
                loadingMessage
        );


        Task<Boolean> task =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return action.execute();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    if (
                            Boolean.TRUE.equals(
                                    task.getValue()
                            )
                    ) {

                        showInformation(
                                successMessage
                        );


                        loadUsers();

                    } else {

                        setLoading(
                                false,
                                failureMessage
                        );


                        showError(
                                failureMessage
                        );
                    }
                }
        );


        task.setOnFailed(
                event -> {

                    setLoading(
                            false,
                            failureMessage
                    );


                    if (
                            task.getException()
                                    != null
                    ) {

                        task
                                .getException()
                                .printStackTrace();
                    }


                    showError(
                            failureMessage
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // USER ACTION INTERFACE
    // =====================================================

    @FunctionalInterface
    private interface UserAction {

        boolean execute();
    }


    // =====================================================
    // UPDATE STATISTICS
    // =====================================================

    private void updateStatistics() {

        int total =
                allUsers.size();


        int active =
                0;


        int suspended =
                0;


        for (
                User user :
                allUsers
        ) {

            if (user == null) {

                continue;
            }


            if (
                    user.isSuspended()
            ) {

                suspended++;

            } else {

                active++;
            }
        }


        totalUsersLabel.setText(
                String.valueOf(
                        total
                )
        );


        activeUsersLabel.setText(
                String.valueOf(
                        active
                )
        );


        suspendedUsersLabel.setText(
                String.valueOf(
                        suspended
                )
        );
    }


    // =====================================================
    // EMPTY STATE
    // =====================================================

    private Node createEmptyState() {

        VBox box =
                new VBox(8);


        box.setAlignment(
                Pos.CENTER
        );


        box.setPadding(
                new Insets(
                        45
                )
        );


        box.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
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
                        "No users found"
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 17px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label message =
                new Label(
                        "Try changing your search or status filter."
                );


        message.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
        );


        box
                .getChildren()
                .addAll(
                        title,
                        message
                );


        return box;
    }


    // =====================================================
    // BUTTON FACTORIES
    // =====================================================

    private Button createSecondaryButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                36
        );


        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );


        button.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-border-radius: 8;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private Button createWarningButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                36
        );


        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );


        button.setStyle(
                "-fx-background-color: #3B2F16;"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: "
                        + AMBER
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private Button createGreenButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                36
        );


        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );


        button.setStyle(
                "-fx-background-color: "
                        + GREEN_DARK
                        + ";"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: "
                        + GREEN
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private Button createDeleteButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                36
        );


        button.setPadding(
                new Insets(
                        0,
                        14,
                        0,
                        14
                )
        );


        button.setStyle(
                "-fx-background-color: "
                        + RED_DARK
                        + ";"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: "
                        + RED
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


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
    // FORM
    // =====================================================

    private Label createFormLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private TextField createDialogTextField(
            String prompt
    ) {

        TextField field =
                new TextField();


        field.setPromptText(
                prompt
        );


        field.setPrefHeight(
                40
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
    // ALERTS
    // =====================================================

    private boolean showConfirmation(
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
    // LOADING
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


                    refreshButton.setDisable(
                            loading
                    );
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

    private Label createStatValueLabel() {

        return new Label(
                "0"
        );
    }


    private boolean contains(
            String value,
            String query
    ) {

        if (
                value == null
                        ||
                query == null
        ) {

            return false;
        }


        return value
                .toLowerCase()
                .contains(
                        query
                );
    }


    private String valueOrFallback(
            String value,
            String fallback
    ) {

        if (
                value == null
                        ||
                value.trim().isEmpty()
        ) {

            return fallback;
        }


        return value.trim();
    }


    // =====================================================
    // DATE
    // =====================================================

    private String formatDate(
            long timestamp
    ) {

        if (
                timestamp <= 0
        ) {

            return "Not available";
        }


        SimpleDateFormat formatter =
                new SimpleDateFormat(
                        "dd MMM yyyy"
                );


        return formatter.format(
                new Date(
                        timestamp
                )
        );
    }


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