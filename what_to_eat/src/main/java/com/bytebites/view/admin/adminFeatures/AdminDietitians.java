package com.bytebites.view.admin.adminFeatures;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.bytebites.controller.AdminController;
import com.bytebites.dao.DietitianDao;
import com.bytebites.model.Dietitian;

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

public class AdminDietitians {

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

    private static final String AMBER =
            "#F59E0B";

    private static final String AMBER_DARK =
            "#3B2F16";

    private static final String RED =
            "#EF4444";

    private static final String RED_DARK =
            "#3B1720";



    // =====================================================
    // BACKEND
    // =====================================================

    private final AdminController adminController;

    private final DietitianDao dietitianDao;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;

    private final VBox dietitianListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<Dietitian> allDietitians;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> statusFilter;


    // =====================================================
    // STATISTICS
    // =====================================================

    private Label totalValue;

    private Label approvedValue;

    private Label pendingValue;

    private Label suspendedValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminDietitians() {

        adminController =
                new AdminController();

        dietitianDao =
                new DietitianDao();

        allDietitians =
                new ArrayList<>();

        root =
                new BorderPane();

        dietitianListBox =
                new VBox(15);

        buildPage();

        loadDietitians();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getDietitiansPage() {

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


        content
                .getChildren()
                .addAll(
                        createHeader(),
                        createStatistics(),
                        createFilters(),
                        createLoadingSection(),
                        dietitianListBox
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
                        "Dietitians"
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
                        "Approve applications and manage all Dietitian accounts."
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


        applyPrimaryStyle(
                refreshButton
        );


        refreshButton.setOnAction(
                event ->
                        loadDietitians()
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
                        applyPrimaryStyle(
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
    // STATISTICS
    // =====================================================

    private Node createStatistics() {

        HBox row =
                new HBox(14);


        totalValue =
                new Label("0");

        approvedValue =
                new Label("0");

        pendingValue =
                new Label("0");

        suspendedValue =
                new Label("0");


        VBox total =
                createStatCard(
                        "Total Dietitians",
                        totalValue,
                        PRIMARY_BLUE
                );


        VBox approved =
                createStatCard(
                        "Approved",
                        approvedValue,
                        GREEN
                );


        VBox pending =
                createStatCard(
                        "Pending",
                        pendingValue,
                        AMBER
                );


        VBox suspended =
                createStatCard(
                        "Suspended",
                        suspendedValue,
                        RED
                );


        for (
                VBox card :
                new VBox[]{
                        total,
                        approved,
                        pending,
                        suspended
                }
        ) {

            HBox.setHgrow(
                    card,
                    Priority.ALWAYS
            );

            card.setMaxWidth(
                    Double.MAX_VALUE
            );
        }


        row
                .getChildren()
                .addAll(
                        total,
                        approved,
                        pending,
                        suspended
                );


        return row;
    }


    private VBox createStatCard(
            String titleText,
            Label value,
            String color
    ) {

        VBox card =
                new VBox(7);


        card.setPadding(
                new Insets(
                        18
                )
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
                        + color
                        + ";"
                        +
                "-fx-font-size: 25px;"
                        +
                "-fx-font-weight: bold;"
        );


        card
                .getChildren()
                .addAll(
                        title,
                        value
                );


        return card;
    }


    // =====================================================
    // FILTERS
    // =====================================================

    private Node createFilters() {

        HBox filters =
                new HBox(12);


        filters.setAlignment(
                Pos.CENTER_LEFT
        );


        searchField =
                new TextField();


        searchField.setPromptText(
                "Search name, email, specialization, UID..."
        );


        searchField.setPrefWidth(
                390
        );


        searchField.setPrefHeight(
                42
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
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 9;"
                        +
                "-fx-background-radius: 9;"
                        +
                "-fx-padding: 0 13 0 13;"
        );


        statusFilter =
                new ComboBox<>();


        statusFilter
                .getItems()
                .addAll(
                        "All",
                        "Pending",
                        "Approved",
                        "Rejected",
                        "Suspended"
                );


        statusFilter.setValue(
                "All"
        );


        statusFilter.setPrefWidth(
                170
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
                        "Loading Dietitians..."
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
    // LOAD DIETITIANS
    // =====================================================

    private void loadDietitians() {

        setLoading(
                true,
                "Loading Dietitians..."
        );


        Task<List<Dietitian>> task =
                new Task<>() {

                    @Override
                    protected List<Dietitian> call() {

                        return adminController
                                .getAllDietitians();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<Dietitian> loaded =
                            task.getValue();


                    if (loaded == null) {

                        loaded =
                                new ArrayList<>();
                    }


                    allDietitians =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allDietitians.size()
                                    + " Dietitian(s) loaded."
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
                            "Unable to load Dietitians."
                    );


                    showError(
                            "Unable to load Dietitians from the database."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // FILTER
    // =====================================================

    private void applyFilters() {

        if (
                searchField == null
                        ||
                statusFilter == null
        ) {

            return;
        }


        String query =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String selectedStatus =
                statusFilter.getValue();


        List<Dietitian> result =
                new ArrayList<>();


        for (
                Dietitian dietitian :
                allDietitians
        ) {

            if (
                    dietitian == null
            ) {

                continue;
            }


            boolean searchMatches =
                    query.isEmpty()
                            ||
                    contains(
                            dietitian.getName(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getEmail(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getUid(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getWhatsappNumber(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getSpecialization(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getExperience(),
                            query
                    );


            if (!searchMatches) {

                continue;
            }


            boolean statusMatches =
                    selectedStatus == null
                            ||
                    selectedStatus.equalsIgnoreCase(
                            "All"
                    )
                            ||
                    (
                            dietitian.getStatus()
                                    != null
                                    &&
                            dietitian
                                    .getStatus()
                                    .equalsIgnoreCase(
                                            selectedStatus
                                    )
                    );


            if (statusMatches) {

                result.add(
                        dietitian
                );
            }
        }


        displayDietitians(
                result
        );
    }


    // =====================================================
    // DISPLAY
    // =====================================================

    private void displayDietitians(
            List<Dietitian> dietitians
    ) {

        dietitianListBox
                .getChildren()
                .clear();


        if (
                dietitians == null
                        ||
                dietitians.isEmpty()
        ) {

            dietitianListBox
                    .getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                Dietitian dietitian :
                dietitians
        ) {

            dietitianListBox
                    .getChildren()
                    .add(
                            createDietitianCard(
                                    dietitian
                            )
                    );
        }
    }


    // =====================================================
    // DIETITIAN CARD
    // =====================================================

    private VBox createDietitianCard(
            Dietitian dietitian
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
        // HEADER
        // =================================================

        HBox header =
                new HBox(12);


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox identity =
                new VBox(4);


        Label name =
                new Label(
                        value(
                                dietitian.getName(),
                                "Unnamed Dietitian"
                        )
                );


        name.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label professional =
                new Label(
                        value(
                                dietitian.getSpecialization(),
                                "Specialization not provided"
                        )
                                +
                        "  •  "
                                +
                        value(
                                dietitian.getExperience(),
                                "Experience not provided"
                        )
                );


        professional.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        identity
                .getChildren()
                .addAll(
                        name,
                        professional
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label statusBadge =
                createStatusBadge(
                        dietitian.getStatus()
                );


        header
                .getChildren()
                .addAll(
                        identity,
                        spacer,
                        statusBadge
                );


        Separator divider =
                new Separator();


        // =================================================
        // BASIC INFORMATION
        // =================================================

        HBox information =
                new HBox(25);


        information
                .getChildren()
                .addAll(
                        createInfo(
                                "EMAIL",
                                value(
                                        dietitian.getEmail(),
                                        "Not available"
                                )
                        ),

                        createInfo(
                                "WHATSAPP",
                                value(
                                        dietitian.getWhatsappNumber(),
                                        "Not provided"
                                )
                        ),

                        createInfo(
                                "ACTIVE CLIENTS",
                                String.valueOf(
                                        dietitian.getActiveUsers()
                                )
                        ),

                        createInfo(
                                "JOINED",
                                formatDate(
                                        dietitian.getCreatedAt()
                                )
                        ),

                        createInfo(
                                "LAST LOGIN",
                                formatDateTime(
                                        dietitian.getLastLoginAt()
                                )
                        )
                );


        // =================================================
        // LIVE PERFORMANCE
        // =================================================

        HBox performance =
                createPerformanceSection(
                        dietitian
                );


        // =================================================
        // ACTIONS
        // =================================================

        HBox actions =
                new HBox(9);


        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        Button detailsButton =
                createNeutralButton(
                        "View Details"
                );


        Button editButton =
                createNeutralButton(
                        "Edit Profile"
                );


        detailsButton.setOnAction(
                event ->
                        showDetails(
                                dietitian
                        )
        );


        editButton.setOnAction(
                event ->
                        showEditDialog(
                                dietitian
                        )
        );


        actions
                .getChildren()
                .addAll(
                        detailsButton,
                        editButton
                );


        addStatusActions(
                actions,
                dietitian
        );


        Button delete =
                createDangerButton(
                        "Delete"
                );


        delete.setOnAction(
                event ->
                        deleteDietitian(
                                dietitian
                        )
        );


        actions
                .getChildren()
                .add(
                        delete
                );


        card
                .getChildren()
                .addAll(
                        header,
                        divider,
                        information,
                        performance,
                        actions
                );


        return card;
    }


    // =====================================================
    // PERFORMANCE SECTION
    // =====================================================

    private HBox createPerformanceSection(
            Dietitian dietitian
    ) {

        HBox row =
                new HBox(18);


        row.setPadding(
                new Insets(
                        13
                )
        );


        row.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 10;"
        );


        Label subscribers =
                new Label(
                        "Subscribers: Loading..."
                );


        Label rating =
                new Label(
                        "Rating: Loading..."
                );


        Label revenue =
                new Label(
                        "Revenue: Loading..."
                );


        for (
                Label label :
                new Label[]{
                        subscribers,
                        rating,
                        revenue
                }
        ) {

            label.setStyle(
                    "-fx-text-fill: #CBD5E1;"
                            +
                    "-fx-font-size: 12px;"
                            +
                    "-fx-font-weight: bold;"
            );
        }


        row
                .getChildren()
                .addAll(
                        subscribers,
                        rating,
                        revenue
                );


        loadPerformance(
                dietitian.getUid(),
                subscribers,
                rating,
                revenue
        );


        return row;
    }


    // =====================================================
    // LOAD PERFORMANCE DATA
    // =====================================================

    private void loadPerformance(
            String uid,
            Label subscribersLabel,
            Label ratingLabel,
            Label revenueLabel
    ) {

        Task<DietitianPerformance> task =
                new Task<>() {

                    @Override
                    protected DietitianPerformance call() {

                        int subscriberCount =
                                dietitianDao
                                        .getSubscriberCount(
                                                uid
                                        );


                        double rating =
                                dietitianDao
                                        .getAverageRating(
                                                uid
                                        );


                        double revenue =
                                dietitianDao
                                        .getTotalRevenue(
                                                uid
                                        );


                        return new DietitianPerformance(
                                subscriberCount,
                                rating,
                                revenue
                        );
                    }
                };


        task.setOnSucceeded(
                event -> {

                    DietitianPerformance data =
                            task.getValue();


                    subscribersLabel.setText(
                            "Subscribers: "
                                    + data.subscribers
                    );


                    ratingLabel.setText(
                            "Rating: "
                                    + String.format(
                                            "%.1f / 5",
                                            data.rating
                                    )
                    );


                    revenueLabel.setText(
                            "Revenue: "
                                    + formatCurrency(
                                            data.revenue
                                    )
                    );
                }
        );


        task.setOnFailed(
                event -> {

                    subscribersLabel.setText(
                            "Subscribers: —"
                    );

                    ratingLabel.setText(
                            "Rating: —"
                    );

                    revenueLabel.setText(
                            "Revenue: —"
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // STATUS ACTIONS
    // =====================================================

    private void addStatusActions(
            HBox actions,
            Dietitian dietitian
    ) {

        String status =
                value(
                        dietitian.getStatus(),
                        "Pending"
                );


        if (
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            Button approve =
                    createSuccessButton(
                            "Approve"
                    );


            Button reject =
                    createDangerButton(
                            "Reject"
                    );


            approve.setOnAction(
                    event ->
                            approveDietitian(
                                    dietitian
                            )
            );


            reject.setOnAction(
                    event ->
                            rejectDietitian(
                                    dietitian
                            )
            );


            actions
                    .getChildren()
                    .addAll(
                            approve,
                            reject
                    );


        } else if (
                status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            Button suspend =
                    createWarningButton(
                            "Suspend"
                    );


            suspend.setOnAction(
                    event ->
                            suspendDietitian(
                                    dietitian
                            )
            );


            actions
                    .getChildren()
                    .add(
                            suspend
                    );


        } else if (
                status.equalsIgnoreCase(
                        "Suspended"
                )
        ) {

            Button activate =
                    createSuccessButton(
                            "Reactivate"
                    );


            activate.setOnAction(
                    event ->
                            activateDietitian(
                                    dietitian
                            )
            );


            actions
                    .getChildren()
                    .add(
                            activate
                    );


        } else if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            Button approve =
                    createSuccessButton(
                            "Approve"
                    );


            approve.setOnAction(
                    event ->
                            approveDietitian(
                                    dietitian
                            )
            );


            actions
                    .getChildren()
                    .add(
                            approve
                    );
        }
    }


    // =====================================================
    // APPROVE
    // =====================================================

    private void approveDietitian(
            Dietitian dietitian
    ) {

        if (
                !confirm(
                        "Approve Dietitian",
                        "Approve "
                                + value(
                                        dietitian.getName(),
                                        "this Dietitian"
                                )
                                + "?",
                        "The Dietitian will be allowed to access the Dietitian dashboard."
                )
        ) {

            return;
        }


        executeAction(
                "Approving Dietitian...",
                () ->
                        adminController
                                .approveDietitian(
                                        dietitian.getUid()
                                ),
                "Dietitian approved successfully.",
                "Unable to approve Dietitian."
        );
    }


    // =====================================================
    // REJECT
    // =====================================================

    private void rejectDietitian(
            Dietitian dietitian
    ) {

        if (
                !confirm(
                        "Reject Dietitian",
                        "Reject "
                                + value(
                                        dietitian.getName(),
                                        "this Dietitian"
                                )
                                + "?",
                        "The Dietitian will not be allowed to access the Dietitian dashboard."
                )
        ) {

            return;
        }


        executeAction(
                "Rejecting Dietitian...",
                () ->
                        adminController
                                .rejectDietitian(
                                        dietitian.getUid()
                                ),
                "Dietitian rejected successfully.",
                "Unable to reject Dietitian."
        );
    }


    // =====================================================
    // SUSPEND
    // =====================================================

    private void suspendDietitian(
            Dietitian dietitian
    ) {

        if (
                !confirm(
                        "Suspend Dietitian",
                        "Suspend "
                                + value(
                                        dietitian.getName(),
                                        "this Dietitian"
                                )
                                + "?",
                        "The account will lose access until an Admin reactivates it."
                )
        ) {

            return;
        }


        executeAction(
                "Suspending Dietitian...",
                () ->
                        adminController
                                .suspendDietitian(
                                        dietitian.getUid()
                                ),
                "Dietitian suspended successfully.",
                "Unable to suspend Dietitian."
        );
    }


    // =====================================================
    // ACTIVATE
    // =====================================================

    private void activateDietitian(
            Dietitian dietitian
    ) {

        if (
                !confirm(
                        "Reactivate Dietitian",
                        "Reactivate "
                                + value(
                                        dietitian.getName(),
                                        "this Dietitian"
                                )
                                + "?",
                        "The account will return to Approved status."
                )
        ) {

            return;
        }


        executeAction(
                "Reactivating Dietitian...",
                () ->
                        adminController
                                .activateDietitian(
                                        dietitian.getUid()
                                ),
                "Dietitian reactivated successfully.",
                "Unable to reactivate Dietitian."
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    private void deleteDietitian(
            Dietitian dietitian
    ) {

        if (
                !confirm(
                        "Delete Dietitian",
                        "Delete "
                                + value(
                                        dietitian.getName(),
                                        "this Dietitian"
                                )
                                + "?",
                        "This currently deletes the main Firestore Dietitian profile. This action cannot be undone."
                )
        ) {

            return;
        }


        executeAction(
                "Deleting Dietitian...",
                () ->
                        adminController
                                .deleteDietitian(
                                        dietitian.getUid()
                                ),
                "Dietitian profile deleted successfully.",
                "Unable to delete Dietitian."
        );
    }


    // =====================================================
    // EXECUTE ACTION
    // =====================================================

    private void executeAction(
            String loadingText,
            DietitianAction action,
            String successText,
            String failureText
    ) {

        setLoading(
                true,
                loadingText
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
                                successText
                        );

                        loadDietitians();

                    } else {

                        setLoading(
                                false,
                                failureText
                        );

                        showError(
                                failureText
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
                            failureText
                    );


                    showError(
                            failureText
                    );
                }
        );


        startTask(
                task
        );
    }


    @FunctionalInterface
    private interface DietitianAction {

        boolean execute();
    }


    // =====================================================
    // VIEW DETAILS
    // =====================================================

    private void showDetails(
            Dietitian dietitian
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Dietitian Details"
        );


        dialog.setHeaderText(
                value(
                        dietitian.getName(),
                        "Dietitian"
                )
        );


        ButtonType close =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog
                .getDialogPane()
                .getButtonTypes()
                .add(
                        close
                );


        VBox content =
                new VBox(12);


        content.setPadding(
                new Insets(
                        15
                )
        );


        content.setPrefWidth(
                560
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        content
                .getChildren()
                .addAll(
                        detailRow(
                                "Name",
                                dietitian.getName()
                        ),

                        detailRow(
                                "Email",
                                dietitian.getEmail()
                        ),

                        detailRow(
                                "UID",
                                dietitian.getUid()
                        ),

                        detailRow(
                                "Role",
                                value(
                                        dietitian.getRole(),
                                        "DIETITIAN"
                                )
                        ),

                        detailRow(
                                "Status",
                                dietitian.getStatus()
                        ),

                        detailRow(
                                "WhatsApp",
                                dietitian.getWhatsappNumber()
                        ),

                        detailRow(
                                "Specialization",
                                dietitian.getSpecialization()
                        ),

                        detailRow(
                                "Experience",
                                dietitian.getExperience()
                        ),

                        detailRow(
                                "Active Users",
                                String.valueOf(
                                        dietitian.getActiveUsers()
                                )
                        ),

                        detailRow(
                                "Created",
                                formatDateTime(
                                        dietitian.getCreatedAt()
                                )
                        ),

                        detailRow(
                                "Approved",
                                formatDateTime(
                                        dietitian.getApprovedAt()
                                )
                        ),

                        detailRow(
                                "Rejected",
                                formatDateTime(
                                        dietitian.getRejectedAt()
                                )
                        ),

                        detailRow(
                                "Suspended",
                                formatDateTime(
                                        dietitian.getSuspendedAt()
                                )
                        ),

                        detailRow(
                                "Last Login",
                                formatDateTime(
                                        dietitian.getLastLoginAt()
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
    // EDIT PROFILE
    // =====================================================

    private void showEditDialog(
            Dietitian dietitian
    ) {

        SameStageDialog<ButtonType> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Edit Dietitian"
        );


        dialog.setHeaderText(
                "Edit "
                        + value(
                                dietitian.getName(),
                                "Dietitian"
                        )
                + "'s profile"
        );


        ButtonType save =
                new ButtonType(
                        "Save Changes",
                        ButtonBar.ButtonData.OK_DONE
                );


        ButtonType cancel =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog
                .getDialogPane()
                .getButtonTypes()
                .addAll(
                        save,
                        cancel
                );


        TextField name =
                createField(
                        "Name",
                        dietitian.getName()
                );


        TextField email =
                createField(
                        "Email",
                        dietitian.getEmail()
                );


        email.setEditable(
                false
        );


        email.setDisable(
                true
        );


        TextField whatsapp =
                createField(
                        "WhatsApp",
                        dietitian.getWhatsappNumber()
                );


        TextField specialization =
                createField(
                        "Specialization",
                        dietitian.getSpecialization()
                );


        TextField experience =
                createField(
                        "Experience",
                        dietitian.getExperience()
                );


        VBox content =
                new VBox(10);


        content.setPadding(
                new Insets(
                        15
                )
        );


        content.setPrefWidth(
                480
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        content
                .getChildren()
                .addAll(
                        formLabel("Name"),
                        name,

                        formLabel("Email"),
                        email,

                        formLabel("WhatsApp"),
                        whatsapp,

                        formLabel("Specialization"),
                        specialization,

                        formLabel("Experience"),
                        experience
                );


        dialog
                .getDialogPane()
                .setContent(
                        content
                );


        Optional<ButtonType> result =
                dialog.showAndWait();


        if (
                result.isEmpty()
                        ||
                result.get() != save
        ) {

            return;
        }


        String newName =
                name.getText()
                        .trim();


        if (newName.isEmpty()) {

            showError(
                    "Dietitian name cannot be empty."
            );

            return;
        }


        updateProfile(
                dietitian,
                newName,
                whatsapp.getText(),
                specialization.getText(),
                experience.getText()
        );
    }


    // =====================================================
    // UPDATE PROFILE
    // =====================================================

    private void updateProfile(
            Dietitian dietitian,
            String name,
            String whatsapp,
            String specialization,
            String experience
    ) {

        setLoading(
                true,
                "Updating Dietitian profile..."
        );


        Task<Boolean> task =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return dietitianDao
                                .adminUpdateDietitianProfile(
                                        dietitian.getUid(),
                                        name,
                                        whatsapp,
                                        specialization,
                                        experience
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
                                "Dietitian profile updated successfully."
                        );

                        loadDietitians();

                    } else {

                        setLoading(
                                false,
                                "Unable to update profile."
                        );

                        showError(
                                "Unable to update Dietitian profile."
                        );
                    }
                }
        );


        task.setOnFailed(
                event -> {

                    setLoading(
                            false,
                            "Unable to update profile."
                    );

                    showError(
                            "An error occurred while updating the Dietitian."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // UPDATE STATISTICS
    // =====================================================

    private void updateStatistics() {

        int total =
                allDietitians.size();

        int approved =
                0;

        int pending =
                0;

        int suspended =
                0;


        for (
                Dietitian dietitian :
                allDietitians
        ) {

            if (
                    dietitian == null
                            ||
                    dietitian.getStatus()
                            == null
            ) {

                continue;
            }


            if (
                    dietitian
                            .getStatus()
                            .equalsIgnoreCase(
                                    "Approved"
                            )
            ) {

                approved++;

            } else if (
                    dietitian
                            .getStatus()
                            .equalsIgnoreCase(
                                    "Pending"
                            )
            ) {

                pending++;

            } else if (
                    dietitian
                            .getStatus()
                            .equalsIgnoreCase(
                                    "Suspended"
                            )
            ) {

                suspended++;
            }
        }


        totalValue.setText(
                String.valueOf(
                        total
                )
        );


        approvedValue.setText(
                String.valueOf(
                        approved
                )
        );


        pendingValue.setText(
                String.valueOf(
                        pending
                )
        );


        suspendedValue.setText(
                String.valueOf(
                        suspended
                )
        );
    }


    // =====================================================
    // STATUS BADGE
    // =====================================================

    private Label createStatusBadge(
            String status
    ) {

        String actual =
                value(
                        status,
                        "Pending"
                );


        String textColor;

        String background;


        if (
                actual.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            textColor =
                    GREEN;

            background =
                    GREEN_DARK;


        } else if (
                actual.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            textColor =
                    AMBER;

            background =
                    AMBER_DARK;


        } else if (
                actual.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            textColor =
                    RED;

            background =
                    RED_DARK;


        } else {

            textColor =
                    RED;

            background =
                    RED_DARK;
        }


        Label badge =
                new Label(
                        actual.toUpperCase()
                );


        badge.setPadding(
                new Insets(
                        5,
                        11,
                        5,
                        11
                )
        );


        badge.setStyle(
                "-fx-background-color: "
                        + background
                        + ";"
                        +
                "-fx-background-radius: 20;"
                        +
                "-fx-text-fill: "
                        + textColor
                        + ";"
                        +
                "-fx-font-size: 11px;"
                        +
                "-fx-font-weight: bold;"
        );


        return badge;
    }


    // =====================================================
    // INFO
    // =====================================================

    private VBox createInfo(
            String titleText,
            String valueText
    ) {

        VBox box =
                new VBox(4);


        box.setMinWidth(
                145
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
    // DETAIL ROW
    // =====================================================

    private Node detailRow(
            String titleText,
            String valueText
    ) {

        HBox row =
                new HBox(15);


        Label title =
                new Label(
                        titleText
                );


        title.setMinWidth(
                125
        );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-weight: bold;"
        );


        Label value =
                new Label(
                        value(
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
    // FORM
    // =====================================================

    private TextField createField(
            String prompt,
            String current
    ) {

        TextField field =
                new TextField();


        field.setPromptText(
                prompt
        );


        field.setText(
                value(
                        current,
                        ""
                )
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


    private Label formLabel(
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


    // =====================================================
    // BUTTONS
    // =====================================================

    private Button createNeutralButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
                        +
                "-fx-border-radius: 8;"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private Button createSuccessButton(
            String text
    ) {

        Button button =
                new Button(
                        text
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


        button.setStyle(
                "-fx-background-color: "
                        + AMBER_DARK
                        + ";"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: "
                        + AMBER
                        + ";"
                        +
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private Button createDangerButton(
            String text
    ) {

        Button button =
                new Button(
                        text
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
                "-fx-font-weight: bold;"
                        +
                "-fx-cursor: hand;"
        );


        return button;
    }


    private void applyPrimaryStyle(
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
                        "No Dietitians found"
                );


        title.setStyle(
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 17px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label description =
                new Label(
                        "Try changing the search or status filter."
                );


        description.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
        );


        box
                .getChildren()
                .addAll(
                        title,
                        description
                );


        return box;
    }


    // =====================================================
    // ALERTS
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


    private void showInformation(
            String text
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
                text
        );


        alert.showAndWait();
    }


    private void showError(
            String text
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
                text
        );


        alert.showAndWait();
    }


    // =====================================================
    // LOADING
    // =====================================================

    private void setLoading(
            boolean loading,
            String text
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
                            text
                    );


                    refreshButton.setDisable(
                            loading
                    );
                }
        );
    }


    // =====================================================
    // THREAD
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


    private String value(
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


    private String formatDate(
            long timestamp
    ) {

        if (
                timestamp <= 0
        ) {

            return "Not available";
        }


        return new SimpleDateFormat(
                "dd MMM yyyy"
        ).format(
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


        return new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a"
        ).format(
                new Date(
                        timestamp
                )
        );
    }


    private String formatCurrency(
            double amount
    ) {

        NumberFormat formatter =
                NumberFormat
                        .getCurrencyInstance(
                                new Locale(
                                        "en",
                                        "IN"
                                )
                        );


        return formatter.format(
                amount
        );
    }


    // =====================================================
    // PERFORMANCE OBJECT
    // =====================================================

    private static class DietitianPerformance {

        private final int subscribers;

        private final double rating;

        private final double revenue;


        private DietitianPerformance(
                int subscribers,
                double rating,
                double revenue
        ) {

            this.subscribers =
                    subscribers;

            this.rating =
                    rating;

            this.revenue =
                    revenue;
        }
    }
}