package com.bytebites.view.dietitian.dietitionFeatures;

import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.model.FamilyMember;
import com.bytebites.model.session.SessionManager;
import com.google.cloud.Timestamp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DietitianRevenue {

    // =========================================================
    // COLORS
    // =========================================================

    private static final String PAGE_BG =
            "#0F172A";

    private static final String CARD_BG =
            "#121D35";

    private static final String CARD_BG_LIGHT =
            "#1E293B";

    private static final String BLUE =
            "#1976D2";

    private static final String BLUE_LIGHT =
            "#60A5FA";

    private static final String GREEN =
            "#22C55E";

    private static final String GREEN_LIGHT =
            "#4ADE80";

    private static final String AMBER =
            "#F59E0B";

    private static final String RED =
            "#EF4444";

    private static final String WHITE =
            "#F8FAFC";

    private static final String MUTED =
            "#94A3B8";

    private static final String BORDER =
            "#334155";


    // =========================================================
    // DAO
    // =========================================================

    private final DietitianDao dietitianDao =
            new DietitianDao();

    private final FamilyMemberDao familyMemberDao =
            new FamilyMemberDao();


    // =========================================================
    // CURRENT DATA
    // =========================================================

    private String dietitianUid;

    private final List<RevenueRecord> revenueRecords =
            new ArrayList<>();


    // =========================================================
    // UI
    // =========================================================

    private VBox chartContainer;

    private VBox customerRevenueContainer;

    private VBox recentTransactionsContainer;

    private VBox sourceContainer;

    private VBox insightContainer;


    private Label totalRevenueValue;

    private Label periodRevenueValue;

    private Label activeClientsValue;

    private Label averageRevenueValue;

    private Label periodRevenueTitle;


    private ComboBox<String> periodFilter;


    private Button monthlyButton;

    private Button yearlyButton;

    private Button customerButton;


    private String selectedView =
            "Monthly";


    // =========================================================
    // MAIN REVENUE PAGE
    // =========================================================

    public ScrollPane getRevenuePage() {

        // =====================================================
        // CURRENT LOGGED IN DIETITIAN
        // =====================================================

        dietitianUid =
                SessionManager.getUid();


        // =====================================================
        // READ FIREBASE
        // =====================================================

        loadRevenueFromFirebase();


        // =====================================================
        // ROOT
        // =====================================================

        VBox mainContent =
                new VBox(22);

        mainContent.setPadding(
                new Insets(
                        18,
                        22,
                        35,
                        22
                )
        );

        mainContent.setStyle(
                "-fx-background-color: "
                        + PAGE_BG + ";"
        );


        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                createHeader();


        // =====================================================
        // STAT CARDS
        // =====================================================

        HBox summaryCards =
                createSummaryCards();


        // =====================================================
        // GRAPH SWITCHER
        // =====================================================

        HBox graphHeader =
                createRevenueViewHeader();


        // =====================================================
        // GRAPH CONTAINER
        // =====================================================

        chartContainer =
                new VBox();

        chartContainer.setFillWidth(
                true
        );


        // =====================================================
        // SOURCE + INSIGHT
        // =====================================================

        sourceContainer =
                new VBox();

        insightContainer =
                new VBox();

        sourceContainer.setMaxWidth(
                Double.MAX_VALUE
        );

        insightContainer.setMaxWidth(
                Double.MAX_VALUE
        );

        HBox.setHgrow(
                sourceContainer,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                insightContainer,
                Priority.ALWAYS
        );


        HBox analyticsRow =
                new HBox(
                        18,
                        sourceContainer,
                        insightContainer
                );


        // =====================================================
        // CUSTOMER REVENUE
        // =====================================================

        customerRevenueContainer =
                new VBox(12);


        // =====================================================
        // TRANSACTIONS
        // =====================================================

        VBox transactionSection =
                new VBox(12);


        Label transactionTitle =
                createSectionTitle(
                        "Recent Subscription Payments"
                );


        Label transactionSubtitle =
                createMutedLabel(
                        "Revenue received from your subscribed families"
                );


        recentTransactionsContainer =
                new VBox(10);


        transactionSection
                .getChildren()
                .addAll(
                        transactionTitle,
                        transactionSubtitle,
                        recentTransactionsContainer
                );


        // =====================================================
        // ADD
        // =====================================================

        mainContent
                .getChildren()
                .addAll(

                        header,

                        summaryCards,

                        graphHeader,

                        chartContainer,

                        analyticsRow,

                        customerRevenueContainer,

                        transactionSection
                );


        // =====================================================
        // REFRESH ALL DATA
        // =====================================================

        refreshDashboard();


        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane(
                        mainContent
                );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(

                "-fx-background: "
                        + PAGE_BG + ";" +

                "-fx-background-color: "
                        + PAGE_BG + ";" +

                "-fx-border-color: transparent;"
        );

        return scrollPane;
    }


    // =========================================================
    // LOAD REAL REVENUE FROM FIREBASE
    // =========================================================

    private void loadRevenueFromFirebase() {

        revenueRecords.clear();


        if (
                dietitianUid == null ||
                dietitianUid.trim().isEmpty()
        ) {

            System.err.println(
                    "Revenue: No logged-in dietitian UID."
            );

            return;
        }


        try {

            // =================================================
            // SAME METHOD ALREADY USED BY DietitianClients
            // =================================================

            List<String> subscriberIds =
                    dietitianDao
                            .getSubscriberUserIds(
                                    dietitianUid
                            );


            if (subscriberIds == null) {
                return;
            }


            for (String userId : subscriberIds) {

                // =============================================
                // GET SUBSCRIPTION DATA
                // =============================================

                Map<String, Object> subscriber =
                        dietitianDao
                                .getSubscriberDetails(
                                        dietitianUid,
                                        userId
                                );


                if (subscriber == null) {
                    continue;
                }


                // =============================================
                // FAMILY NAME
                // =============================================

                String familyName =
                        getFamilyName(
                                userId
                        );


                // =============================================
                // PLAN
                // =============================================

                String plan =
                        readString(
                                subscriber,
                                "plan",
                                "Family Plan"
                        );


                // =============================================
                // AMOUNT
                // =============================================

                double amount =
                        readDouble(
                                subscriber.get(
                                        "amount"
                                )
                        );


                // =============================================
                // EXPIRY
                // =============================================

                LocalDate planExpiry =
                        readDate(
                                subscriber.get(
                                        "planExpiry"
                                )
                        );


                // =============================================
                // PAYMENT / SUBSCRIPTION DATE
                // =============================================
                //
                // Supports several names so your existing
                // Firebase data can work without depending on
                // one exact spelling.
                //
                // Recommended final field:
                //
                // paymentDate
                //
                // =============================================

                LocalDate paymentDate =
                        firstValidDate(

                                subscriber.get(
                                        "paymentDate"
                                ),

                                subscriber.get(
                                        "subscribedAt"
                                ),

                                subscriber.get(
                                        "subscriptionDate"
                                ),

                                subscriber.get(
                                        "createdAt"
                                )
                        );


                // =============================================
                // STATUS
                // =============================================

                String status =
                        readString(
                                subscriber,
                                "status",
                                ""
                        );


                boolean active =
                        determineActiveStatus(
                                status,
                                planExpiry
                        );


                // =============================================
                // ONLY REAL SUBSCRIBERS
                // =============================================

                RevenueRecord record =
                        new RevenueRecord(

                                userId,

                                familyName,

                                plan,

                                amount,

                                paymentDate,

                                planExpiry,

                                active
                        );


                revenueRecords.add(
                        record
                );
            }


        } catch (Exception e) {

            System.err.println(
                    "Error loading revenue from Firebase:"
            );

            e.printStackTrace();
        }
    }


    // =========================================================
    // FAMILY NAME
    // =========================================================

    private String getFamilyName(
            String userId
    ) {

        try {

            List<FamilyMember> members =
                    familyMemberDao
                            .getFamilyMembersByUserId(
                                    userId
                            );


            if (
                    members != null &&
                    !members.isEmpty()
            ) {

                String name =
                        members.get(0)
                                .getName();


                if (
                        name != null &&
                        !name.trim().isEmpty()
                ) {

                    return name
                            + " Family";
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Could not load family name for "
                            + userId
            );
        }


        return "Client";
    }


    // =========================================================
    // ACTIVE SUBSCRIPTION
    // =========================================================

    private boolean determineActiveStatus(
            String status,
            LocalDate expiry
    ) {

        if (
                status != null &&
                !status.trim().isEmpty()
        ) {

            if (
                    status.equalsIgnoreCase(
                            "cancelled"
                    ) ||
                    status.equalsIgnoreCase(
                            "inactive"
                    ) ||
                    status.equalsIgnoreCase(
                            "expired"
                    )
            ) {

                return false;
            }


            if (
                    status.equalsIgnoreCase(
                            "active"
                    ) ||
                    status.equalsIgnoreCase(
                            "paid"
                    )
            ) {

                return true;
            }
        }


        /*
         * Your existing Clients page currently assumes
         * returned subscribers are active.
         *
         * If an expiry exists, use it to determine status.
         */

        if (expiry != null) {

            return !expiry.isBefore(
                    LocalDate.now()
            );
        }


        /*
         * getSubscriberUserIds() already represents
         * subscribers, so if no explicit inactive status
         * exists we treat the record as subscribed.
         */

        return true;
    }


    // =========================================================
    // HEADER
    // =========================================================

    private HBox createHeader() {

        HBox header =
                new HBox(15);

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox titleBox =
                new VBox(5);


        Label title =
                new Label(
                        "Revenue"
                );

        title.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 28px;" +

                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        "Track your real subscription earnings and monetization performance"
                );

        subtitle.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

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


        periodFilter =
                new ComboBox<>();


        periodFilter
                .getItems()
                .addAll(

                        "This Month",

                        "Last Month",

                        "Last 3 Months",

                        "This Year",

                        "All Time"
                );


        periodFilter.setValue(
                "This Month"
        );


        periodFilter.setPrefWidth(
                165
        );


        periodFilter.setStyle(

                "-fx-background-color: "
                        + CARD_BG_LIGHT + ";" +

                "-fx-text-fill: white;" +

                "-fx-background-radius: 8;" +

                "-fx-border-radius: 8;" +

                "-fx-border-color: "
                        + BORDER + ";" +

                "-fx-padding: 3;"
        );


        periodFilter.setOnAction(
                event ->
                        refreshDashboard()
        );


        header.getChildren()
                .addAll(

                        titleBox,

                        spacer,

                        periodFilter
                );


        return header;
    }


    // =========================================================
    // SUMMARY CARDS
    // =========================================================

    private HBox createSummaryCards() {

        HBox row =
                new HBox(15);


        VBox totalCard =
                createSummaryCard(

                        "Total Revenue",

                        "₹0",

                        "Subscription earnings",

                        GREEN
                );


        totalRevenueValue =
                getValueLabel(
                        totalCard
                );


        VBox periodCard =
                createSummaryCard(

                        "This Month",

                        "₹0",

                        "Selected period",

                        GREEN
                );


        periodRevenueTitle =
                getTitleLabel(
                        periodCard
                );


        periodRevenueValue =
                getValueLabel(
                        periodCard
                );


        VBox clientsCard =
                createSummaryCard(

                        "Active Subscribers",

                        "0",

                        "Currently subscribed",

                        BLUE_LIGHT
                );


        activeClientsValue =
                getValueLabel(
                        clientsCard
                );


        VBox averageCard =
                createSummaryCard(

                        "Avg. Revenue / Client",

                        "₹0",

                        "Average subscription value",

                        GREEN
                );


        averageRevenueValue =
                getValueLabel(
                        averageCard
                );


        for (
                VBox card :
                List.of(
                        totalCard,
                        periodCard,
                        clientsCard,
                        averageCard
                )
        ) {

            card.setMaxWidth(
                    Double.MAX_VALUE
            );

            HBox.setHgrow(
                    card,
                    Priority.ALWAYS
            );
        }


        row.getChildren()
                .addAll(

                        totalCard,

                        periodCard,

                        clientsCard,

                        averageCard
                );


        return row;
    }


    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private VBox createSummaryCard(

            String title,

            String value,

            String description,

            String accent
    ) {

        VBox card =
                new VBox(8);


        card.setPadding(
                new Insets(18)
        );


        card.setPrefHeight(
                135
        );


        card.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 12;" +

                "-fx-border-radius: 12;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 13px;"
        );


        Label valueLabel =
                new Label(
                        value
                );


        valueLabel.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 25px;" +

                "-fx-font-weight: bold;"
        );


        Label descriptionLabel =
                new Label(
                        description
                );


        descriptionLabel.setStyle(

                "-fx-text-fill: "
                        + accent + ";" +

                "-fx-font-size: 12px;" +

                "-fx-font-weight: bold;"
        );


        card.getChildren()
                .addAll(

                        titleLabel,

                        valueLabel,

                        descriptionLabel
                );


        return card;
    }


    // =========================================================
    // GRAPH SWITCHER
    // =========================================================

    private HBox createRevenueViewHeader() {

        HBox row =
                new HBox(10);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        Label title =
                createSectionTitle(
                        "Revenue Overview"
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        monthlyButton =
                createViewButton(
                        "Monthly"
                );


        yearlyButton =
                createViewButton(
                        "Yearly"
                );


        customerButton =
                createViewButton(
                        "Customer-wise"
                );


        monthlyButton.setOnAction(
                event -> {

                    selectedView =
                            "Monthly";

                    updateViewButtons();

                    refreshChart();
                }
        );


        yearlyButton.setOnAction(
                event -> {

                    selectedView =
                            "Yearly";

                    updateViewButtons();

                    refreshChart();
                }
        );


        customerButton.setOnAction(
                event -> {

                    selectedView =
                            "Customer-wise";

                    updateViewButtons();

                    refreshChart();
                }
        );


        updateViewButtons();


        row.getChildren()
                .addAll(

                        title,

                        spacer,

                        monthlyButton,

                        yearlyButton,

                        customerButton
                );


        return row;
    }


    // =========================================================
    // BUTTON
    // =========================================================

    private Button createViewButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                35
        );


        return button;
    }


    private void updateViewButtons() {

        styleViewButton(

                monthlyButton,

                selectedView.equals(
                        "Monthly"
                )
        );


        styleViewButton(

                yearlyButton,

                selectedView.equals(
                        "Yearly"
                )
        );


        styleViewButton(

                customerButton,

                selectedView.equals(
                        "Customer-wise"
                )
        );
    }


    private void styleViewButton(

            Button button,

            boolean selected
    ) {

        if (button == null) {
            return;
        }


        if (selected) {

            button.setStyle(

                    "-fx-background-color: "
                            + BLUE + ";" +

                    "-fx-text-fill: white;" +

                    "-fx-font-weight: bold;" +

                    "-fx-background-radius: 7;" +

                    "-fx-padding: 7 14;" +

                    "-fx-cursor: hand;"
            );

        } else {

            button.setStyle(

                    "-fx-background-color: "
                            + CARD_BG_LIGHT + ";" +

                    "-fx-text-fill: "
                            + MUTED + ";" +

                    "-fx-border-color: "
                            + BORDER + ";" +

                    "-fx-border-radius: 7;" +

                    "-fx-background-radius: 7;" +

                    "-fx-padding: 7 14;" +

                    "-fx-cursor: hand;"
            );
        }
    }


    // =========================================================
    // REFRESH DASHBOARD
    // =========================================================

    private void refreshDashboard() {

        if (
                periodFilter == null ||
                totalRevenueValue == null
        ) {

            return;
        }


        // =====================================================
        // ACTIVE REVENUE
        // =====================================================

        List<RevenueRecord> activeRecords =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .filter(
                                record ->
                                        record.getAmount() > 0
                        )
                        .toList();


        // =====================================================
        // TOTAL REVENUE
        // =====================================================
        //
        // With your current DB this represents the total value
        // of the subscription records currently stored.
        //
        // Once payment history is stored separately this will
        // become true lifetime revenue.
        //
        // =====================================================

        double totalRevenue =
                activeRecords
                        .stream()
                        .mapToDouble(
                                RevenueRecord::getAmount
                        )
                        .sum();


        totalRevenueValue.setText(
                formatCurrency(
                        totalRevenue
                )
        );


        // =====================================================
        // ACTIVE CLIENTS
        // =====================================================

        long activeClients =
                activeRecords
                        .stream()
                        .map(
                                RevenueRecord::getUserId
                        )
                        .distinct()
                        .count();


        activeClientsValue.setText(
                String.valueOf(
                        activeClients
                )
        );


        // =====================================================
        // FILTERED REVENUE
        // =====================================================

        List<RevenueRecord> periodRecords =
                getFilteredRecords();


        double periodRevenue =
                periodRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .mapToDouble(
                                RevenueRecord::getAmount
                        )
                        .sum();


        periodRevenueTitle.setText(
                periodFilter.getValue()
        );


        periodRevenueValue.setText(
                formatCurrency(
                        periodRevenue
                )
        );


        // =====================================================
        // AVERAGE
        // =====================================================

        double average =
                activeClients == 0
                        ? 0
                        : totalRevenue
                        / activeClients;


        averageRevenueValue.setText(
                formatCurrency(
                        average
                )
        );


        // =====================================================
        // CHILD SECTIONS
        // =====================================================

        refreshChart();

        refreshSources();

        refreshInsight();

        refreshCustomerRevenue();

        refreshTransactions();
    }


    // =========================================================
    // PERIOD FILTER
    // =========================================================

    private List<RevenueRecord>
    getFilteredRecords() {

        String period =
                periodFilter.getValue();


        LocalDate today =
                LocalDate.now();


        /*
         * A record without a payment/subscription timestamp
         * cannot be assigned to a month.
         *
         * For All Time we still include it.
         */

        if (
                period.equals(
                        "All Time"
                )
        ) {

            return revenueRecords
                    .stream()
                    .filter(
                            RevenueRecord::isActive
                    )
                    .toList();
        }


        return revenueRecords
                .stream()
                .filter(
                        RevenueRecord::isActive
                )
                .filter(
                        record ->
                                record.getPaymentDate()
                                        != null
                )
                .filter(record -> {

                    LocalDate date =
                            record.getPaymentDate();


                    switch (period) {

                        case "This Month":

                            return date.getYear()
                                    == today.getYear()

                                    &&

                                    date.getMonth()
                                            == today.getMonth();


                        case "Last Month":

                            LocalDate previousMonth =
                                    today.minusMonths(
                                            1
                                    );


                            return date.getYear()
                                    == previousMonth
                                    .getYear()

                                    &&

                                    date.getMonth()
                                            == previousMonth
                                            .getMonth();


                        case "Last 3 Months":

                            LocalDate start =
                                    today
                                            .minusMonths(2)
                                            .withDayOfMonth(1);


                            return !date.isBefore(
                                    start
                            )

                                    &&

                                    !date.isAfter(
                                            today
                                    );


                        case "This Year":

                            return date.getYear()
                                    == today.getYear();


                        default:

                            return true;
                    }
                })
                .toList();
    }


    // =========================================================
    // GRAPH REFRESH
    // =========================================================

    private void refreshChart() {

        if (chartContainer == null) {
            return;
        }


        chartContainer
                .getChildren()
                .clear();


        switch (selectedView) {

            case "Yearly":

                chartContainer
                        .getChildren()
                        .add(
                                createYearlyChart()
                        );

                break;


            case "Customer-wise":

                chartContainer
                        .getChildren()
                        .add(
                                createCustomerChart()
                        );

                break;


            default:

                chartContainer
                        .getChildren()
                        .add(
                                createMonthlyChart()
                        );
        }
    }


    // =========================================================
    // MONTHLY GRAPH
    // =========================================================

    private VBox createMonthlyChart() {

        CategoryAxis xAxis =
                new CategoryAxis();


        NumberAxis yAxis =
                new NumberAxis();


        yAxis.setLabel(
                "Revenue (₹)"
        );


        LineChart<String, Number> chart =
                new LineChart<>(
                        xAxis,
                        yAxis
                );


        chart.setLegendVisible(
                false
        );


        chart.setAnimated(
                false
        );


        chart.setCreateSymbols(
                true
        );


        chart.setPrefHeight(
                350
        );


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        int year =
                LocalDate
                        .now()
                        .getYear();


        for (
                int month = 1;
                month <= 12;
                month++
        ) {

            final int m =
                    month;


            double revenue =
                    revenueRecords
                            .stream()
                            .filter(
                                    RevenueRecord::isActive
                            )
                            .filter(
                                    record ->
                                            record.getPaymentDate()
                                                    != null
                            )
                            .filter(record ->

                                    record
                                            .getPaymentDate()
                                            .getYear()
                                            == year

                                            &&

                                            record
                                                    .getPaymentDate()
                                                    .getMonthValue()
                                                    == m
                            )
                            .mapToDouble(
                                    RevenueRecord::getAmount
                            )
                            .sum();


            String monthName =
                    java.time.Month
                            .of(
                                    month
                            )
                            .getDisplayName(

                                    TextStyle.SHORT,

                                    Locale.ENGLISH
                            );


            XYChart.Data<String, Number> data =
                    new XYChart.Data<>(
                            monthName,
                            revenue
                    );


            series
                    .getData()
                    .add(
                            data
                    );
        }


        chart
                .getData()
                .add(
                        series
                );


        installTooltips(
                series
        );


        styleChart(
                chart
        );


        return createChartCard(

                "Monthly Revenue",

                hasAnyPaymentDates()
                        ? "Real subscription revenue by month"
                        : "Add paymentDate to subscription records to enable historical monthly revenue",

                chart
        );
    }


    // =========================================================
    // YEARLY GRAPH
    // =========================================================

    private VBox createYearlyChart() {

        CategoryAxis xAxis =
                new CategoryAxis();


        NumberAxis yAxis =
                new NumberAxis();


        yAxis.setLabel(
                "Revenue (₹)"
        );


        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );


        chart.setLegendVisible(
                false
        );


        chart.setAnimated(
                false
        );


        chart.setPrefHeight(
                350
        );


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        Map<Integer, Double> yearlyRevenue =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .filter(
                                record ->
                                        record.getPaymentDate()
                                                != null
                        )
                        .collect(

                                Collectors.groupingBy(

                                        record ->
                                                record
                                                        .getPaymentDate()
                                                        .getYear(),

                                        TreeMap::new,

                                        Collectors
                                                .summingDouble(
                                                        RevenueRecord
                                                                ::getAmount
                                                )
                                )
                        );


        yearlyRevenue.forEach(

                (year, amount) ->

                        series
                                .getData()
                                .add(

                                        new XYChart.Data<>(

                                                String.valueOf(
                                                        year
                                                ),

                                                amount
                                        )
                                )
        );


        chart
                .getData()
                .add(
                        series
                );


        installTooltips(
                series
        );


        styleChart(
                chart
        );


        return createChartCard(

                "Yearly Revenue",

                hasAnyPaymentDates()
                        ? "Subscription earnings across years"
                        : "Historical revenue requires a paymentDate timestamp",

                chart
        );
    }


    // =========================================================
    // CUSTOMER GRAPH
    // =========================================================

    private VBox createCustomerChart() {

        CategoryAxis xAxis =
                new CategoryAxis();


        NumberAxis yAxis =
                new NumberAxis();


        yAxis.setLabel(
                "Revenue (₹)"
        );


        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );


        chart.setLegendVisible(
                false
        );


        chart.setAnimated(
                false
        );


        chart.setPrefHeight(
                350
        );


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        Map<String, Double> customerRevenue =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .filter(
                                record ->
                                        record.getAmount()
                                                > 0
                        )
                        .collect(

                                Collectors.groupingBy(

                                        RevenueRecord
                                                ::getFamilyName,

                                        Collectors
                                                .summingDouble(
                                                        RevenueRecord
                                                                ::getAmount
                                                )
                                )
                        );


        customerRevenue
                .entrySet()
                .stream()
                .sorted(

                        Map.Entry
                                .<String, Double>
                                comparingByValue()
                                .reversed()
                )
                .forEach(entry -> {

                    series
                            .getData()
                            .add(

                                    new XYChart.Data<>(

                                            entry.getKey(),

                                            entry.getValue()
                                    )
                            );
                });


        chart
                .getData()
                .add(
                        series
                );


        installTooltips(
                series
        );


        styleChart(
                chart
        );


        return createChartCard(

                "Customer-wise Revenue",

                "Current subscription revenue by family",

                chart
        );
    }


    // =========================================================
    // TOOLTIP
    // =========================================================

    private void installTooltips(
            XYChart.Series<String, Number> series
    ) {

        for (
                XYChart.Data<String, Number> data :
                series.getData()
        ) {

            data.nodeProperty()
                    .addListener(
                            (
                                    observable,
                                    oldNode,
                                    newNode
                            ) -> {

                                if (newNode != null) {

                                    Tooltip.install(

                                            newNode,

                                            new Tooltip(

                                                    data
                                                            .getXValue()

                                                            + "\n"

                                                            + formatCurrency(

                                                            data
                                                                    .getYValue()
                                                                    .doubleValue()
                                                    )
                                            )
                                    );
                                }
                            }
                    );
        }
    }


    // =========================================================
    // CHART CARD
    // =========================================================

    private VBox createChartCard(

            String titleText,

            String subtitleText,

            Node chart
    ) {

        VBox card =
                new VBox(10);


        card.setPadding(
                new Insets(18)
        );


        card.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 12;" +

                "-fx-border-radius: 12;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 17px;" +

                "-fx-font-weight: bold;"
        );


        Label subtitle =
                createMutedLabel(
                        subtitleText
                );


        card.getChildren()
                .addAll(

                        title,

                        subtitle,

                        chart
                );


        return card;
    }


    // =========================================================
    // STYLE CHART
    // =========================================================

    private void styleChart(
            javafx.scene.chart.Chart chart
    ) {

        chart.setStyle(
                "-fx-background-color: transparent;"
        );


        chart.applyCss();


        chart.lookupAll(
                ".chart-plot-background"
        ).forEach(

                node ->
                        node.setStyle(
                                "-fx-background-color: transparent;"
                        )
        );


        chart.lookupAll(
                ".axis"
        ).forEach(

                node ->
                        node.setStyle(

                                "-fx-tick-label-fill: "
                                        + MUTED + ";" +

                                "-fx-text-fill: "
                                        + MUTED + ";"
                        )
        );


        chart.lookupAll(
                ".chart-series-line"
        ).forEach(

                node ->
                        node.setStyle(

                                "-fx-stroke: "
                                        + BLUE + ";" +

                                "-fx-stroke-width: 3px;"
                        )
        );


        chart.lookupAll(
                ".default-color0.chart-bar"
        ).forEach(

                node ->
                        node.setStyle(

                                "-fx-bar-fill: "
                                        + BLUE + ";"
                        )
        );
    }


    // =========================================================
    // REVENUE SOURCES
    // =========================================================

    private void refreshSources() {

        sourceContainer
                .getChildren()
                .clear();


        VBox card =
                new VBox(14);


        card.setPadding(
                new Insets(20)
        );


        card.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 12;" +

                "-fx-border-radius: 12;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        card.getChildren()
                .add(
                        createSectionTitle(
                                "Revenue by Plan"
                        )
                );


        Map<String, Double> sources =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .filter(
                                record ->
                                        record.getAmount()
                                                > 0
                        )
                        .collect(

                                Collectors.groupingBy(

                                        RevenueRecord::getPlan,

                                        Collectors
                                                .summingDouble(
                                                        RevenueRecord
                                                                ::getAmount
                                                )
                                )
                        );


        double total =
                sources
                        .values()
                        .stream()
                        .mapToDouble(
                                Double::doubleValue
                        )
                        .sum();


        if (sources.isEmpty()) {

            card.getChildren()
                    .add(
                            createEmptyLabel(
                                    "No paid subscriptions found."
                            )
                    );

        } else {

            sources
                    .entrySet()
                    .stream()
                    .sorted(

                            Map.Entry
                                    .<String, Double>
                                    comparingByValue()
                                    .reversed()
                    )
                    .forEach(entry -> {

                        double percentage =
                                total <= 0
                                        ? 0
                                        : entry.getValue()
                                        / total;


                        card.getChildren()
                                .add(

                                        createSourceRow(

                                                entry.getKey(),

                                                entry.getValue(),

                                                percentage
                                        )
                                );
                    });
        }


        sourceContainer
                .getChildren()
                .add(
                        card
                );
    }


    private VBox createSourceRow(

            String plan,

            double amount,

            double percentage
    ) {

        VBox box =
                new VBox(7);


        HBox row =
                new HBox(10);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        Label planLabel =
                new Label(
                        plan
                );


        planLabel.setStyle(
                "-fx-text-fill: "
                        + WHITE + ";"
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label amountLabel =
                new Label(
                        formatCurrency(
                                amount
                        )
                );


        amountLabel.setStyle(

                "-fx-text-fill: "
                        + GREEN_LIGHT + ";" +

                "-fx-font-weight: bold;"
        );


        Label percentageLabel =
                new Label(

                        String.format(

                                "%.0f%%",

                                percentage
                                        * 100
                        )
                );


        percentageLabel.setStyle(
                "-fx-text-fill: "
                        + MUTED + ";"
        );


        row.getChildren()
                .addAll(

                        planLabel,

                        spacer,

                        amountLabel,

                        percentageLabel
                );


        ProgressBar bar =
                new ProgressBar(
                        percentage
                );


        bar.setMaxWidth(
                Double.MAX_VALUE
        );


        bar.setStyle(
                "-fx-accent: "
                        + BLUE + ";"
        );


        box.getChildren()
                .addAll(
                        row,
                        bar
                );


        return box;
    }


    // =========================================================
    // INSIGHT
    // =========================================================

    private void refreshInsight() {

        insightContainer
                .getChildren()
                .clear();


        VBox card =
                new VBox(14);


        card.setPadding(
                new Insets(20)
        );


        card.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 12;" +

                "-fx-border-radius: 12;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        Label title =
                createSectionTitle(
                        "Revenue Insight"
                );


        List<RevenueRecord> active =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .toList();


        if (active.isEmpty()) {

            Label noRevenue =
                    createMutedLabel(
                            "You do not currently have any paid subscribers."
                    );


            card.getChildren()
                    .addAll(
                            title,
                            noRevenue
                    );


            insightContainer
                    .getChildren()
                    .add(
                            card
                    );


            return;
        }


        RevenueRecord highest =
                active
                        .stream()
                        .max(
                                Comparator.comparingDouble(
                                        RevenueRecord::getAmount
                                )
                        )
                        .orElse(null);


        Map<String, Double> byPlan =
                active
                        .stream()
                        .collect(

                                Collectors.groupingBy(

                                        RevenueRecord::getPlan,

                                        Collectors
                                                .summingDouble(
                                                        RevenueRecord
                                                                ::getAmount
                                                )
                                )
                        );


        String bestPlan =
                byPlan
                        .entrySet()
                        .stream()
                        .max(
                                Map.Entry.comparingByValue()
                        )
                        .map(
                                Map.Entry::getKey
                        )
                        .orElse(
                                "No plan"
                        );


        Label mainInsight =
                new Label(

                        "Your highest-value subscription is "
                                +

                                (
                                        highest == null
                                                ? "-"
                                                : highest
                                                .getFamilyName()
                                )

                                +

                                " at "

                                +

                                (
                                        highest == null
                                                ? "₹0"
                                                : formatCurrency(
                                                highest
                                                        .getAmount()
                                        )
                                )
                );


        mainInsight.setWrapText(
                true
        );


        mainInsight.setStyle(

                "-fx-text-fill: "
                        + GREEN + ";" +

                "-fx-font-size: 14px;" +

                "-fx-font-weight: bold;"
        );


        Label secondInsight =
                new Label(

                        bestPlan

                                +

                                " currently contributes the most subscription revenue."
                );


        secondInsight.setWrapText(
                true
        );


        secondInsight.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 13px;"
        );


        Label note =
                new Label();


        if (!hasAnyPaymentDates()) {

            note.setText(

                    "Tip: add paymentDate to the subscriber document. "
                            +

                            "That will enable accurate monthly and yearly "
                            +

                            "monetization analytics."
            );


            note.setStyle(

                    "-fx-text-fill: "
                            + AMBER + ";" +

                    "-fx-font-size: 12px;"
            );

        } else {

            note.setText(
                    "Your revenue graph is using real subscription payment dates."
            );


            note.setStyle(

                    "-fx-text-fill: "
                            + BLUE_LIGHT + ";" +

                    "-fx-font-size: 12px;"
            );
        }


        note.setWrapText(
                true
        );


        card.getChildren()
                .addAll(

                        title,

                        mainInsight,

                        secondInsight,

                        note
                );


        insightContainer
                .getChildren()
                .add(
                        card
                );
    }


    // =========================================================
    // CUSTOMER CARDS
    // =========================================================

    private void refreshCustomerRevenue() {

        customerRevenueContainer
                .getChildren()
                .clear();


        customerRevenueContainer
                .getChildren()
                .addAll(

                        createSectionTitle(
                                "Customer-wise Revenue"
                        ),

                        createMutedLabel(
                                "Revenue generated from every subscribed family"
                        )
                );


        List<RevenueRecord> sorted =
                revenueRecords
                        .stream()
                        .filter(
                                RevenueRecord::isActive
                        )
                        .filter(
                                record ->
                                        record.getAmount()
                                                > 0
                        )
                        .sorted(

                                Comparator
                                        .comparingDouble(
                                                RevenueRecord::getAmount
                                        )
                                        .reversed()
                        )
                        .toList();


        double total =
                sorted
                        .stream()
                        .mapToDouble(
                                RevenueRecord::getAmount
                        )
                        .sum();


        if (sorted.isEmpty()) {

            customerRevenueContainer
                    .getChildren()
                    .add(

                            createEmptyLabel(
                                    "No subscribed clients found."
                            )
                    );


            return;
        }


        for (RevenueRecord record : sorted) {

            double contribution =
                    total <= 0
                            ? 0
                            : record.getAmount()
                            / total
                            * 100;


            customerRevenueContainer
                    .getChildren()
                    .add(

                            createCustomerCard(

                                    record,

                                    contribution
                            )
                    );
        }
    }


    private HBox createCustomerCard(

            RevenueRecord record,

            double contribution
    ) {

        HBox card =
                new HBox(15);


        card.setAlignment(
                Pos.CENTER_LEFT
        );


        card.setPadding(
                new Insets(
                        16,
                        20,
                        16,
                        20
                )
        );


        card.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 10;" +

                "-fx-border-radius: 10;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        VBox familyBox =
                new VBox(4);


        Label family =
                new Label(
                        record.getFamilyName()
                );


        family.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 15px;" +

                "-fx-font-weight: bold;"
        );


        Label plan =
                new Label(
                        record.getPlan()
                );


        plan.setStyle(
                "-fx-text-fill: "
                        + MUTED + ";"
        );


        familyBox
                .getChildren()
                .addAll(
                        family,
                        plan
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        VBox expiryBox =
                createSmallValueBox(

                        "PLAN EXPIRY",

                        record.getExpiry() == null
                                ? "Not Set"
                                : record
                                .getExpiry()
                                .format(
                                        DateTimeFormatter
                                                .ofPattern(
                                                        "dd MMM yyyy"
                                                )
                                )
                );


        VBox contributionBox =
                createSmallValueBox(

                        "CONTRIBUTION",

                        String.format(
                                "%.0f%%",
                                contribution
                        )
                );


        VBox revenueBox =
                createSmallValueBox(

                        "REVENUE",

                        formatCurrency(
                                record.getAmount()
                        )
                );


        card.getChildren()
                .addAll(

                        familyBox,

                        spacer,

                        expiryBox,

                        contributionBox,

                        revenueBox
                );


        return card;
    }


    private VBox createSmallValueBox(

            String title,

            String value
    ) {

        VBox box =
                new VBox(4);


        box.setAlignment(
                Pos.CENTER_RIGHT
        );


        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 10px;"
        );


        Label valueLabel =
                new Label(
                        value
                );


        valueLabel.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 13px;" +

                "-fx-font-weight: bold;"
        );


        box.getChildren()
                .addAll(

                        titleLabel,

                        valueLabel
                );


        return box;
    }


    // =========================================================
    // TRANSACTIONS
    // =========================================================

    private void refreshTransactions() {

        recentTransactionsContainer
                .getChildren()
                .clear();


        List<RevenueRecord> sorted =
                revenueRecords
                        .stream()
                        .filter(
                                record ->
                                        record.getAmount()
                                                > 0
                        )
                        .sorted(

                                Comparator.comparing(

                                        record ->

                                                record.getPaymentDate()
                                                        == null

                                                        ? LocalDate.MIN

                                                        : record.getPaymentDate(),

                                        Comparator.reverseOrder()
                                )
                        )
                        .toList();


        if (sorted.isEmpty()) {

            recentTransactionsContainer
                    .getChildren()
                    .add(

                            createEmptyLabel(
                                    "No subscription payments found."
                            )
                    );


            return;
        }


        for (RevenueRecord record : sorted) {

            recentTransactionsContainer
                    .getChildren()
                    .add(

                            createTransaction(
                                    record
                            )
                    );
        }
    }


    private HBox createTransaction(
            RevenueRecord record
    ) {

        HBox row =
                new HBox(15);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        row.setPadding(
                new Insets(
                        14,
                        18,
                        14,
                        18
                )
        );


        row.setStyle(

                "-fx-background-color: "
                        + CARD_BG + ";" +

                "-fx-background-radius: 9;" +

                "-fx-border-radius: 9;" +

                "-fx-border-color: "
                        + BORDER + ";"
        );


        VBox clientBox =
                new VBox(4);


        Label clientLabel =
                new Label(
                        record.getFamilyName()
                );


        clientLabel.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-weight: bold;" +

                "-fx-font-size: 14px;"
        );


        Label serviceLabel =
                new Label(
                        record.getPlan()
                );


        serviceLabel.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 12px;"
        );


        clientBox.getChildren()
                .addAll(
                        clientLabel,
                        serviceLabel
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        String dateText =
                record.getPaymentDate()
                        == null

                        ? "Date not stored"

                        : record
                        .getPaymentDate()
                        .format(

                                DateTimeFormatter
                                        .ofPattern(
                                                "dd MMM yyyy"
                                        )
                        );


        Label date =
                new Label(
                        dateText
                );


        date.setStyle(
                "-fx-text-fill: "
                        + MUTED + ";"
        );


        Label status =
                new Label(

                        record.isActive()
                                ? "ACTIVE"
                                : "EXPIRED"
                );


        if (record.isActive()) {

            status.setStyle(

                    "-fx-text-fill: "
                            + GREEN + ";" +

                    "-fx-background-color: #163B2A;" +

                    "-fx-background-radius: 15;" +

                    "-fx-padding: 5 10;" +

                    "-fx-font-size: 10px;" +

                    "-fx-font-weight: bold;"
            );

        } else {

            status.setStyle(

                    "-fx-text-fill: "
                            + RED + ";" +

                    "-fx-background-color: #3F1D25;" +

                    "-fx-background-radius: 15;" +

                    "-fx-padding: 5 10;" +

                    "-fx-font-size: 10px;" +

                    "-fx-font-weight: bold;"
            );
        }


        Label amount =
                new Label(

                        "+ "
                                + formatCurrency(
                                record.getAmount()
                        )
                );


        amount.setStyle(

                "-fx-text-fill: "
                        + GREEN_LIGHT + ";" +

                "-fx-font-size: 15px;" +

                "-fx-font-weight: bold;"
        );


        amount.setMinWidth(
                100
        );


        amount.setAlignment(
                Pos.CENTER_RIGHT
        );


        row.getChildren()
                .addAll(

                        clientBox,

                        spacer,

                        date,

                        status,

                        amount
                );


        return row;
    }


    // =========================================================
    // FIREBASE OBJECT -> STRING
    // =========================================================

    private String readString(

            Map<String, Object> data,

            String field,

            String fallback
    ) {

        Object value =
                data.get(
                        field
                );


        if (value == null) {
            return fallback;
        }


        String text =
                value
                        .toString()
                        .trim();


        return text.isEmpty()
                ? fallback
                : text;
    }


    // =========================================================
    // FIREBASE OBJECT -> DOUBLE
    // =========================================================

    private double readDouble(
            Object value
    ) {

        if (value == null) {
            return 0;
        }


        if (value instanceof Number) {

            return ((Number) value)
                    .doubleValue();
        }


        try {

            String text =
                    value
                            .toString()
                            .replace(
                                    "₹",
                                    ""
                            )
                            .replace(
                                    ",",
                                    ""
                            )
                            .trim();


            return Double.parseDouble(
                    text
            );

        } catch (Exception e) {

            return 0;
        }
    }


    // =========================================================
    // FIREBASE DATE
    // =========================================================

    private LocalDate readDate(
            Object object
    ) {

        if (object == null) {
            return null;
        }


        try {

            // =============================================
            // FIRESTORE SERVER TIMESTAMP
            // =============================================

            if (object instanceof Timestamp) {

                Timestamp timestamp =
                        (Timestamp) object;


                return timestamp
                        .toDate()
                        .toInstant()
                        .atZone(
                                ZoneId.systemDefault()
                        )
                        .toLocalDate();
            }


            // =============================================
            // JAVA DATE
            // =============================================

            if (object instanceof Date) {

                return ((Date) object)
                        .toInstant()
                        .atZone(
                                ZoneId.systemDefault()
                        )
                        .toLocalDate();
            }


            // =============================================
            // INSTANT
            // =============================================

            if (object instanceof Instant) {

                return ((Instant) object)
                        .atZone(
                                ZoneId.systemDefault()
                        )
                        .toLocalDate();
            }


            // =============================================
            // LOCAL DATE
            // =============================================

            if (object instanceof LocalDate) {

                return (LocalDate) object;
            }


            // =============================================
            // STRING
            // =============================================

            String text =
                    object
                            .toString()
                            .trim();


            if (text.isEmpty()) {
                return null;
            }


            try {

                return LocalDate.parse(
                        text
                );

            } catch (Exception ignored) {
            }


            try {

                return LocalDate.parse(

                        text,

                        DateTimeFormatter.ofPattern(
                                "dd MMM yyyy",
                                Locale.ENGLISH
                        )
                );

            } catch (Exception ignored) {
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }


    private LocalDate firstValidDate(
            Object... values
    ) {

        for (Object value : values) {

            LocalDate date =
                    readDate(
                            value
                    );


            if (date != null) {
                return date;
            }
        }


        return null;
    }


    // =========================================================
    // DOES DB CONTAIN PAYMENT DATE?
    // =========================================================

    private boolean hasAnyPaymentDates() {

        return revenueRecords
                .stream()
                .anyMatch(

                        record ->
                                record.getPaymentDate()
                                        != null
                );
    }


    // =========================================================
    // CURRENCY
    // =========================================================

    private String formatCurrency(
            double amount
    ) {

        return String.format(

                new Locale(
                        "en",
                        "IN"
                ),

                "₹%,.0f",

                amount
        );
    }


    // =========================================================
    // LABELS
    // =========================================================

    private Label createSectionTitle(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(

                "-fx-text-fill: "
                        + WHITE + ";" +

                "-fx-font-size: 19px;" +

                "-fx-font-weight: bold;"
        );


        return label;
    }


    private Label createMutedLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 12px;"
        );


        return label;
    }


    private Label createEmptyLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(

                "-fx-text-fill: "
                        + MUTED + ";" +

                "-fx-font-size: 13px;" +

                "-fx-padding: 15 0 10 0;"
        );


        return label;
    }


    private Label getTitleLabel(
            VBox card
    ) {

        return (Label)
                card
                        .getChildren()
                        .get(0);
    }


    private Label getValueLabel(
            VBox card
    ) {

        return (Label)
                card
                        .getChildren()
                        .get(1);
    }


    // =========================================================
    // INTERNAL REVENUE MODEL
    // =========================================================

    private static class RevenueRecord {

        private final String userId;

        private final String familyName;

        private final String plan;

        private final double amount;

        private final LocalDate paymentDate;

        private final LocalDate expiry;

        private final boolean active;


        private RevenueRecord(

                String userId,

                String familyName,

                String plan,

                double amount,

                LocalDate paymentDate,

                LocalDate expiry,

                boolean active
        ) {

            this.userId =
                    userId;

            this.familyName =
                    familyName;

            this.plan =
                    plan;

            this.amount =
                    amount;

            this.paymentDate =
                    paymentDate;

            this.expiry =
                    expiry;

            this.active =
                    active;
        }


        public String getUserId() {
            return userId;
        }


        public String getFamilyName() {
            return familyName;
        }


        public String getPlan() {
            return plan;
        }


        public double getAmount() {
            return amount;
        }


        public LocalDate getPaymentDate() {
            return paymentDate;
        }


        public LocalDate getExpiry() {
            return expiry;
        }


        public boolean isActive() {
            return active;
        }
    }
}