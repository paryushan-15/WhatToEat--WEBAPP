package com.bytebites.view.admin.adminFeatures;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import com.bytebites.controller.AdminController;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminDashboard {

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

    private static final String AMBER =
            "#F59E0B";

    private static final String RED =
            "#EF4444";

    private static final String PURPLE =
            "#A855F7";


    // =====================================================
    // CONTROLLER
    // =====================================================

    private final AdminController adminController;


    // =====================================================
    // ROOT
    // =====================================================

    private ScrollPane dashboardScrollPane;

    private VBox dashboardContent;


    // =====================================================
    // MAIN STAT LABELS
    // =====================================================

    private Label totalUsersValue;

    private Label totalDietitiansValue;

    private Label pendingDietitiansValue;

    private Label consultationsValue;

    private Label activeSubscriptionsValue;

    private Label revenueValue;


    // =====================================================
    // SECONDARY STAT LABELS
    // =====================================================

    private Label mealPlansValue;

    private Label familyMembersValue;

    private Label communityPostsValue;

    private Label totalSubscriptionsValue;


    // =====================================================
    // DIETITIAN STATUS LABELS
    // =====================================================

    private Label approvedDietitiansValue;

    private Label pendingDietitiansStatusValue;

    private Label rejectedDietitiansValue;

    private Label suspendedDietitiansValue;


    // =====================================================
    // PLATFORM HEALTH LABELS
    // =====================================================

    private Label userStatusLabel;

    private Label dietitianStatusLabel;

    private Label subscriptionStatusLabel;

    private Label revenueStatusLabel;


    // =====================================================
    // REFRESH
    // =====================================================

    private Button refreshButton;

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminDashboard() {

        adminController =
                new AdminController();

        buildDashboard();

        loadDashboardData();
    }


    // =====================================================
    // GET DASHBOARD
    // =====================================================

    public ScrollPane getDashboard() {

        return dashboardScrollPane;
    }


    // =====================================================
    // BUILD DASHBOARD
    // =====================================================

    private void buildDashboard() {

        dashboardContent =
                new VBox(24);


        dashboardContent.setPadding(
                new Insets(
                        28,
                        32,
                        40,
                        32
                )
        );


        dashboardContent.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
                        + ";"
        );


        dashboardContent.setMinWidth(
                0
        );


        // =================================================
        // HEADER
        // =================================================

        HBox header =
                createHeader();


        // =================================================
        // MAIN STATS
        // =================================================

        GridPane mainStats =
                createMainStatistics();


        // =================================================
        // SECONDARY STATS
        // =================================================

        GridPane secondaryStats =
                createSecondaryStatistics();


        // =================================================
        // LOWER SECTION
        // =================================================

        HBox lowerSection =
                createLowerSection();


        // =================================================
        // LOADING
        // =================================================

        HBox loadingBox =
                createLoadingBox();


        dashboardContent
                .getChildren()
                .addAll(
                        header,
                        loadingBox,
                        mainStats,
                        secondaryStats,
                        lowerSection
                );


        dashboardScrollPane =
                new ScrollPane(
                        dashboardContent
                );


        dashboardScrollPane.setFitToWidth(
                true
        );


        dashboardScrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );


        dashboardScrollPane.setStyle(
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
    }


    // =====================================================
    // HEADER
    // =====================================================

    private HBox createHeader() {

        HBox header =
                new HBox(20);


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox titleBox =
                new VBox(5);


        Label title =
                new Label(
                        "Admin Dashboard"
                );


        title.setFont(
                Font.font(
                        "System",
                        FontWeight.BOLD,
                        28
                )
        );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
        );


        Label subtitle =
                new Label(
                        "Monitor the entire What To Eat? platform."
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


        applyRefreshButtonStyle();


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
                        applyRefreshButtonStyle()
        );


        refreshButton.setOnAction(
                event ->
                        loadDashboardData()
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


    private void applyRefreshButtonStyle() {

        refreshButton.setStyle(
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
    // LOADING BOX
    // =====================================================

    private HBox createLoadingBox() {

        HBox box =
                new HBox(10);


        box.setAlignment(
                Pos.CENTER_LEFT
        );


        loadingIndicator =
                new ProgressIndicator();


        loadingIndicator.setPrefSize(
                22,
                22
        );


        loadingIndicator.setMaxSize(
                22,
                22
        );


        loadingLabel =
                new Label(
                        "Loading platform data..."
                );


        loadingLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 13px;"
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
    // MAIN STATISTICS
    // =====================================================

    private GridPane createMainStatistics() {

        GridPane grid =
                new GridPane();


        grid.setHgap(
                16
        );


        grid.setVgap(
                16
        );


        // =================================================
        // VALUES
        // =================================================

        totalUsersValue =
                createStatValueLabel();

        totalDietitiansValue =
                createStatValueLabel();

        pendingDietitiansValue =
                createStatValueLabel();

        consultationsValue =
                createStatValueLabel();

        activeSubscriptionsValue =
                createStatValueLabel();

        revenueValue =
                createStatValueLabel();


        VBox usersCard =
                createStatCard(
                        "👥",
                        "Total Users",
                        totalUsersValue,
                        "Registered platform users",
                        PRIMARY_BLUE
                );


        VBox dietitiansCard =
                createStatCard(
                        "⚕",
                        "Dietitians",
                        totalDietitiansValue,
                        "All registered Dietitians",
                        GREEN
                );


        VBox pendingCard =
                createStatCard(
                        "⌛",
                        "Pending Approval",
                        pendingDietitiansValue,
                        "Dietitians awaiting review",
                        AMBER
                );


        VBox consultationCard =
                createStatCard(
                        "◫",
                        "Consultations",
                        consultationsValue,
                        "Total consultation requests",
                        PURPLE
                );


        VBox subscriptionCard =
                createStatCard(
                        "✓",
                        "Active Subscriptions",
                        activeSubscriptionsValue,
                        "Currently active subscriptions",
                        GREEN
                );


        VBox revenueCard =
                createStatCard(
                        "₹",
                        "Platform Revenue",
                        revenueValue,
                        "Paid subscription revenue",
                        PRIMARY_BLUE
                );


        configureThreeColumnGrid(
                grid
        );


        grid.add(
                usersCard,
                0,
                0
        );

        grid.add(
                dietitiansCard,
                1,
                0
        );

        grid.add(
                pendingCard,
                2,
                0
        );


        grid.add(
                consultationCard,
                0,
                1
        );

        grid.add(
                subscriptionCard,
                1,
                1
        );

        grid.add(
                revenueCard,
                2,
                1
        );


        return grid;
    }


    // =====================================================
    // SECONDARY STATS
    // =====================================================

    private GridPane createSecondaryStatistics() {

        GridPane grid =
                new GridPane();


        grid.setHgap(
                14
        );


        grid.setVgap(
                14
        );


        mealPlansValue =
                createSmallValueLabel();

        familyMembersValue =
                createSmallValueLabel();

        communityPostsValue =
                createSmallValueLabel();

        totalSubscriptionsValue =
                createSmallValueLabel();


        VBox mealPlans =
                createSmallCard(
                        "Meal Plans",
                        mealPlansValue
                );


        VBox familyMembers =
                createSmallCard(
                        "Family Members",
                        familyMembersValue
                );


        VBox community =
                createSmallCard(
                        "Community Posts",
                        communityPostsValue
                );


        VBox subscriptions =
                createSmallCard(
                        "Total Subscriptions",
                        totalSubscriptionsValue
                );


        configureFourColumnGrid(
                grid
        );


        grid.add(
                mealPlans,
                0,
                0
        );

        grid.add(
                familyMembers,
                1,
                0
        );

        grid.add(
                community,
                2,
                0
        );

        grid.add(
                subscriptions,
                3,
                0
        );


        return grid;
    }


    // =====================================================
    // LOWER SECTION
    // =====================================================

    private HBox createLowerSection() {

        HBox lower =
                new HBox(18);


        lower.setAlignment(
                Pos.TOP_LEFT
        );


        VBox dietitianStatusCard =
                createDietitianStatusCard();


        VBox platformHealthCard =
                createPlatformHealthCard();


        HBox.setHgrow(
                dietitianStatusCard,
                Priority.ALWAYS
        );


        HBox.setHgrow(
                platformHealthCard,
                Priority.ALWAYS
        );


        dietitianStatusCard.setMaxWidth(
                Double.MAX_VALUE
        );


        platformHealthCard.setMaxWidth(
                Double.MAX_VALUE
        );


        lower
                .getChildren()
                .addAll(
                        dietitianStatusCard,
                        platformHealthCard
                );


        return lower;
    }


    // =====================================================
    // DIETITIAN STATUS CARD
    // =====================================================

    private VBox createDietitianStatusCard() {

        VBox card =
                createSectionCard();


        Label title =
                createSectionTitle(
                        "Dietitian Status"
                );


        Label subtitle =
                createSectionSubtitle(
                        "Current Dietitian account distribution"
                );


        approvedDietitiansValue =
                createStatusNumber();

        pendingDietitiansStatusValue =
                createStatusNumber();

        rejectedDietitiansValue =
                createStatusNumber();

        suspendedDietitiansValue =
                createStatusNumber();


        HBox approved =
                createStatusRow(
                        "●",
                        "Approved",
                        approvedDietitiansValue,
                        GREEN
                );


        HBox pending =
                createStatusRow(
                        "●",
                        "Pending",
                        pendingDietitiansStatusValue,
                        AMBER
                );


        HBox rejected =
                createStatusRow(
                        "●",
                        "Rejected",
                        rejectedDietitiansValue,
                        RED
                );


        HBox suspended =
                createStatusRow(
                        "●",
                        "Suspended",
                        suspendedDietitiansValue,
                        RED
                );


        card
                .getChildren()
                .addAll(
                        title,
                        subtitle,
                        createDivider(),
                        approved,
                        pending,
                        rejected,
                        suspended
                );


        return card;
    }


    // =====================================================
    // PLATFORM HEALTH
    // =====================================================

    private VBox createPlatformHealthCard() {

        VBox card =
                createSectionCard();


        Label title =
                createSectionTitle(
                        "Platform Overview"
                );


        Label subtitle =
                createSectionSubtitle(
                        "Live overview based on database activity"
                );


        userStatusLabel =
                createHealthValue();

        dietitianStatusLabel =
                createHealthValue();

        subscriptionStatusLabel =
                createHealthValue();

        revenueStatusLabel =
                createHealthValue();


        HBox users =
                createHealthRow(
                        "Users",
                        userStatusLabel
                );


        HBox dietitians =
                createHealthRow(
                        "Approved Dietitians",
                        dietitianStatusLabel
                );


        HBox subscriptions =
                createHealthRow(
                        "Active Subscriptions",
                        subscriptionStatusLabel
                );


        HBox revenue =
                createHealthRow(
                        "Revenue Generated",
                        revenueStatusLabel
                );


        card
                .getChildren()
                .addAll(
                        title,
                        subtitle,
                        createDivider(),
                        users,
                        dietitians,
                        subscriptions,
                        revenue
                );


        return card;
    }


    // =====================================================
    // MAIN STAT CARD
    // =====================================================

    private VBox createStatCard(
            String iconText,
            String titleText,
            Label valueLabel,
            String subtitleText,
            String accentColor
    ) {

        VBox card =
                new VBox(10);


        card.setPadding(
                new Insets(
                        19
                )
        );


        card.setMinHeight(
                155
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


        HBox top =
                new HBox();


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        StackPane icon =
                new StackPane();


        icon.setPrefSize(
                40,
                40
        );


        icon.setMinSize(
                40,
                40
        );


        icon.setMaxSize(
                40,
                40
        );


        icon.setStyle(
                "-fx-background-color: "
                        + accentColor
                        + "22;"
                        +
                "-fx-background-radius: 10;"
        );


        Label iconLabel =
                new Label(
                        iconText
                );


        iconLabel.setStyle(
                "-fx-text-fill: "
                        + accentColor
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        icon
                .getChildren()
                .add(
                        iconLabel
                );


        top
                .getChildren()
                .add(
                        icon
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
                "-fx-font-size: 13px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        subtitleText
                );


        subtitle.setWrapText(
                true
        );


        subtitle.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 11px;"
        );


        card
                .getChildren()
                .addAll(
                        top,
                        title,
                        valueLabel,
                        subtitle
                );


        return card;
    }


    // =====================================================
    // SMALL CARD
    // =====================================================

    private VBox createSmallCard(
            String titleText,
            Label valueLabel
    ) {

        VBox card =
                new VBox(8);


        card.setPadding(
                new Insets(
                        18
                )
        );


        card.setMaxWidth(
                Double.MAX_VALUE
        );


        card.setMinHeight(
                100
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
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


        card
                .getChildren()
                .addAll(
                        title,
                        valueLabel
                );


        return card;
    }


    // =====================================================
    // SECTION CARD
    // =====================================================

    private VBox createSectionCard() {

        VBox card =
                new VBox(13);


        card.setPadding(
                new Insets(
                        22
                )
        );


        card.setMinWidth(
                300
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


        return card;
    }


    // =====================================================
    // STATUS ROW
    // =====================================================

    private HBox createStatusRow(
            String bullet,
            String titleText,
            Label value,
            String color
    ) {

        HBox row =
                new HBox(10);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        row.setPadding(
                new Insets(
                        7,
                        0,
                        7,
                        0
                )
        );


        Label dot =
                new Label(
                        bullet
                );


        dot.setStyle(
                "-fx-text-fill: "
                        + color
                        + ";"
                        +
                "-fx-font-size: 13px;"
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 13px;"
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        row
                .getChildren()
                .addAll(
                        dot,
                        title,
                        spacer,
                        value
                );


        return row;
    }


    // =====================================================
    // HEALTH ROW
    // =====================================================

    private HBox createHealthRow(
            String titleText,
            Label value
    ) {

        HBox row =
                new HBox(10);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        row.setPadding(
                new Insets(
                        7,
                        0,
                        7,
                        0
                )
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 13px;"
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        row
                .getChildren()
                .addAll(
                        title,
                        spacer,
                        value
                );


        return row;
    }


    // =====================================================
    // LABEL FACTORIES
    // =====================================================

    private Label createStatValueLabel() {

        Label label =
                new Label(
                        "—"
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 27px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createSmallValueLabel() {

        Label label =
                new Label(
                        "—"
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 23px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createStatusNumber() {

        Label label =
                new Label(
                        "—"
                );


        label.setStyle(
                "-fx-text-fill: white;"
                        +
                "-fx-font-size: 14px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createHealthValue() {

        Label label =
                new Label(
                        "—"
                );


        label.setStyle(
                "-fx-text-fill: "
                        + PRIMARY_BLUE
                        + ";"
                        +
                "-fx-font-size: 13px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createSectionTitle(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createSectionSubtitle(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setWrapText(
                true
        );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        return label;
    }


    // =====================================================
    // DIVIDER
    // =====================================================

    private Region createDivider() {

        Region divider =
                new Region();


        divider.setPrefHeight(
                1
        );


        divider.setMaxHeight(
                1
        );


        divider.setStyle(
                "-fx-background-color: "
                        + BORDER
                        + ";"
        );


        return divider;
    }


    // =====================================================
    // GRID CONFIGURATION
    // =====================================================

    private void configureThreeColumnGrid(
            GridPane grid
    ) {

        for (
                int i = 0;
                i < 3;
                i++
        ) {

            javafx.scene.layout.ColumnConstraints column =
                    new javafx.scene.layout.ColumnConstraints();


            column.setPercentWidth(
                    33.333
            );


            column.setHgrow(
                    Priority.ALWAYS
            );


            column.setFillWidth(
                    true
            );


            grid
                    .getColumnConstraints()
                    .add(
                            column
                    );
        }
    }


    private void configureFourColumnGrid(
            GridPane grid
    ) {

        for (
                int i = 0;
                i < 4;
                i++
        ) {

            javafx.scene.layout.ColumnConstraints column =
                    new javafx.scene.layout.ColumnConstraints();


            column.setPercentWidth(
                    25
            );


            column.setHgrow(
                    Priority.ALWAYS
            );


            column.setFillWidth(
                    true
            );


            grid
                    .getColumnConstraints()
                    .add(
                            column
                    );
        }
    }


    // =====================================================
    // =====================================================
    //
    // DATABASE LOADING
    //
    // =====================================================
    // =====================================================

    private void loadDashboardData() {

        setLoading(
                true,
                "Loading platform data..."
        );


        Task<Map<String, Object>> task =
                new Task<>() {

                    @Override
                    protected Map<String, Object> call() {

                        return adminController
                                .getDashboardStatistics();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    Map<String, Object> statistics =
                            task.getValue();


                    updateDashboard(
                            statistics
                    );


                    setLoading(
                            false,
                            "Dashboard updated successfully."
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
                            "Unable to load dashboard data."
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
    // UPDATE DASHBOARD
    // =====================================================

    private void updateDashboard(
            Map<String, Object> data
    ) {

        if (data == null) {

            setLoading(
                    false,
                    "No dashboard data received."
            );

            return;
        }


        int totalUsers =
                getInt(
                        data,
                        "totalUsers"
                );


        int totalDietitians =
                getInt(
                        data,
                        "totalDietitians"
                );


        int approvedDietitians =
                getInt(
                        data,
                        "approvedDietitians"
                );


        int pendingDietitians =
                getInt(
                        data,
                        "pendingDietitians"
                );


        int rejectedDietitians =
                getInt(
                        data,
                        "rejectedDietitians"
                );


        int suspendedDietitians =
                getInt(
                        data,
                        "suspendedDietitians"
                );


        int totalConsultations =
                getInt(
                        data,
                        "totalConsultations"
                );


        int totalMealPlans =
                getInt(
                        data,
                        "totalMealPlans"
                );


        int totalFamilyMembers =
                getInt(
                        data,
                        "totalFamilyMembers"
                );


        int communityPosts =
                getInt(
                        data,
                        "communityPosts"
                );


        int totalSubscriptions =
                getInt(
                        data,
                        "totalSubscriptions"
                );


        int activeSubscriptions =
                getInt(
                        data,
                        "activeSubscriptions"
                );


        double totalRevenue =
                getDouble(
                        data,
                        "totalRevenue"
                );


        // =================================================
        // MAIN STATS
        // =================================================

        totalUsersValue.setText(
                formatNumber(
                        totalUsers
                )
        );


        totalDietitiansValue.setText(
                formatNumber(
                        totalDietitians
                )
        );


        pendingDietitiansStatusValue.setText(
                formatNumber(
                        pendingDietitians
                )
        );


        consultationsValue.setText(
                formatNumber(
                        totalConsultations
                )
        );


        activeSubscriptionsValue.setText(
                formatNumber(
                        activeSubscriptions
                )
        );


        revenueValue.setText(
                formatCurrency(
                        totalRevenue
                )
        );


        // =================================================
        // SECONDARY
        // =================================================

        mealPlansValue.setText(
                formatNumber(
                        totalMealPlans
                )
        );


        familyMembersValue.setText(
                formatNumber(
                        totalFamilyMembers
                )
        );


        communityPostsValue.setText(
                formatNumber(
                        communityPosts
                )
        );


        totalSubscriptionsValue.setText(
                formatNumber(
                        totalSubscriptions
                )
        );


        // =================================================
        // DIETITIAN STATUS
        // =================================================

        approvedDietitiansValue.setText(
                formatNumber(
                        approvedDietitians
                )
        );


        /*
         * Pending label is the same Label used
         * by the main pending card.
         *
         * For the status list we need another
         * independent Label.
         */

        rejectedDietitiansValue.setText(
                formatNumber(
                        rejectedDietitians
                )
        );


        suspendedDietitiansValue.setText(
                formatNumber(
                        suspendedDietitians
                )
        );


        // =================================================
        // PLATFORM OVERVIEW
        // =================================================

        userStatusLabel.setText(
                formatNumber(
                        totalUsers
                )
                        + " registered"
        );


        dietitianStatusLabel.setText(
                formatNumber(
                        approvedDietitians
                )
                        + " approved"
        );


        subscriptionStatusLabel.setText(
                formatNumber(
                        activeSubscriptions
                )
                        + " active"
        );


        revenueStatusLabel.setText(
                formatCurrency(
                        totalRevenue
                )
        );
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


                    refreshButton.setDisable(
                            loading
                    );
                }
        );
    }


    // =====================================================
    // =====================================================
    //
    // DATA HELPERS
    //
    // =====================================================
    // =====================================================

    private int getInt(
            Map<String, Object> data,
            String key
    ) {

        Object value =
                data.get(
                        key
                );


        if (
                value instanceof Number
        ) {

            return ((Number) value)
                    .intValue();
        }


        return 0;
    }


    private double getDouble(
            Map<String, Object> data,
            String key
    ) {

        Object value =
                data.get(
                        key
                );


        if (
                value instanceof Number
        ) {

            return ((Number) value)
                    .doubleValue();
        }


        return 0.0;
    }


    // =====================================================
    // NUMBER FORMAT
    // =====================================================

    private String formatNumber(
            int value
    ) {

        return NumberFormat
                .getIntegerInstance(
                        Locale.ENGLISH
                )
                .format(
                        value
                );
    }


    // =====================================================
    // INDIAN CURRENCY FORMAT
    // =====================================================

    private String formatCurrency(
            double amount
    ) {

        /*
         * Using new Locale("en", "IN")
         * instead of Locale.of(...)
         *
         * because your project is Java 17.
         */

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
}