package com.bytebites.view.admin.adminFeatures;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.bytebites.controller.AdminController;
import com.google.cloud.Timestamp;

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

public class AdminSubscriptions {

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

    private static final String PURPLE =
            "#A855F7";


    // =====================================================
    // CONTROLLER
    // =====================================================

    private final AdminController adminController;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;

    private final VBox subscriptionListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<Map<String, Object>> allSubscriptions;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> statusFilter;

    private ComboBox<String> paymentFilter;


    // =====================================================
    // STATISTICS
    // =====================================================

    private Label totalSubscriptionsValue;

    private Label activeSubscriptionsValue;

    private Label expiredSubscriptionsValue;

    private Label cancelledSubscriptionsValue;

    private Label revenueValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminSubscriptions() {

        adminController =
                new AdminController();


        root =
                new BorderPane();


        subscriptionListBox =
                new VBox(15);


        allSubscriptions =
                new ArrayList<>();


        buildPage();

        loadSubscriptions();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getSubscriptionsPage() {

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
                        createStatistics(),
                        createFilters(),
                        createLoadingSection(),
                        subscriptionListBox
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
                        "Subscriptions"
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
                        "Monitor User subscriptions, plans, payments and revenue."
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


        applyPrimaryStyle(
                refreshButton
        );


        refreshButton.setOnAction(
                event ->
                        loadSubscriptions()
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


        header.getChildren()
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


        totalSubscriptionsValue =
                new Label("0");

        activeSubscriptionsValue =
                new Label("0");

        expiredSubscriptionsValue =
                new Label("0");

        cancelledSubscriptionsValue =
                new Label("0");

        revenueValue =
                new Label("₹0");


        VBox total =
                createStatCard(
                        "Total Subscriptions",
                        totalSubscriptionsValue,
                        PRIMARY_BLUE
                );


        VBox active =
                createStatCard(
                        "Active",
                        activeSubscriptionsValue,
                        GREEN
                );


        VBox expired =
                createStatCard(
                        "Expired",
                        expiredSubscriptionsValue,
                        AMBER
                );


        VBox cancelled =
                createStatCard(
                        "Cancelled",
                        cancelledSubscriptionsValue,
                        RED
                );


        VBox revenue =
                createStatCard(
                        "Revenue",
                        revenueValue,
                        PURPLE
                );


        VBox[] cards = {
                total,
                active,
                expired,
                cancelled,
                revenue
        };


        for (
                VBox card :
                cards
        ) {

            HBox.setHgrow(
                    card,
                    Priority.ALWAYS
            );


            card.setMaxWidth(
                    Double.MAX_VALUE
            );
        }


        row.getChildren()
                .addAll(
                        cards
                );


        return row;
    }


    // =====================================================
    // STAT CARD
    // =====================================================

    private VBox createStatCard(
            String titleText,
            Label value,
            String accent
    ) {

        VBox card =
                new VBox(7);


        card.setPadding(
                new Insets(
                        17
                )
        );


        card.setMinHeight(
                92
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


        title.setWrapText(
                true
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


        value.setStyle(
                "-fx-text-fill: "
                        + accent
                        + ";"
                        +
                "-fx-font-size: 23px;"
                        +
                "-fx-font-weight: bold;"
        );


        card.getChildren()
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
                "Search User, Dietitian, plan, UID or payment ID..."
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


        // =================================================
        // SUBSCRIPTION STATUS FILTER
        // =================================================

        statusFilter =
                new ComboBox<>();


        statusFilter.getItems()
                .addAll(
                        "All",
                        "Active",
                        "Expired",
                        "Cancelled"
                );


        statusFilter.setValue(
                "All"
        );


        statusFilter.setPrefWidth(
                155
        );


        statusFilter.setPrefHeight(
                42
        );


        // =================================================
        // PAYMENT FILTER
        // =================================================

        paymentFilter =
                new ComboBox<>();


        paymentFilter.getItems()
                .addAll(
                        "All Payments",
                        "Paid",
                        "Pending",
                        "Failed"
                );


        paymentFilter.setValue(
                "All Payments"
        );


        paymentFilter.setPrefWidth(
                165
        );


        paymentFilter.setPrefHeight(
                42
        );


        String comboStyle =
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
                "-fx-background-radius: 9;";


        statusFilter.setStyle(
                comboStyle
        );


        paymentFilter.setStyle(
                comboStyle
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


        paymentFilter.setOnAction(
                event ->
                        applyFilters()
        );


        filters.getChildren()
                .addAll(
                        searchField,
                        statusFilter,
                        paymentFilter
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
                        "Loading subscriptions..."
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
    // LOAD SUBSCRIPTIONS
    // =====================================================

    private void loadSubscriptions() {

        setLoading(
                true,
                "Loading subscriptions..."
        );


        Task<List<Map<String, Object>>> task =
                new Task<>() {

                    @Override
                    protected List<Map<String, Object>> call() {

                        return adminController
                                .getAllSubscriptions();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<Map<String, Object>> loaded =
                            task.getValue();


                    if (
                            loaded == null
                    ) {

                        loaded =
                                new ArrayList<>();
                    }


                    allSubscriptions =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allSubscriptions.size()
                                    + " subscription(s) loaded."
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
                            "Unable to load subscriptions."
                    );


                    showError(
                            "Unable to load subscription data from Firestore."
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
                        ||
                paymentFilter == null
        ) {

            return;
        }


        String query =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String selectedStatus =
                statusFilter
                        .getValue();


        String selectedPayment =
                paymentFilter
                        .getValue();


        List<Map<String, Object>> filtered =
                new ArrayList<>();


        for (
                Map<String, Object> subscription :
                allSubscriptions
        ) {

            if (
                    subscription == null
            ) {

                continue;
            }


            boolean matchesSearch =
                    query.isEmpty()

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "userName"
                                    )
                            ),
                            query
                    )

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "userUid"
                                    )
                            ),
                            query
                    )

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "dietitianName"
                                    )
                            ),
                            query
                    )

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "dietitianUid"
                                    )
                            ),
                            query
                    )

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "plan"
                                    )
                            ),
                            query
                    )

                            ||

                    contains(
                            stringValue(
                                    subscription.get(
                                            "paymentLinkId"
                                    )
                            ),
                            query
                    );


            if (
                    !matchesSearch
            ) {

                continue;
            }


            String actualStatus =
                    getDisplayStatus(
                            subscription
                    );


            boolean matchesStatus =
                    selectedStatus == null

                            ||

                    selectedStatus.equalsIgnoreCase(
                            "All"
                    )

                            ||

                    actualStatus.equalsIgnoreCase(
                            selectedStatus
                    );


            if (
                    !matchesStatus
            ) {

                continue;
            }


            String paymentStatus =
                    stringValue(
                            subscription.get(
                                    "paymentStatus"
                            )
                    );


            boolean matchesPayment =
                    selectedPayment == null

                            ||

                    selectedPayment.equalsIgnoreCase(
                            "All Payments"
                    )

                            ||

                    paymentStatus.equalsIgnoreCase(
                            selectedPayment
                    );


            if (
                    matchesPayment
            ) {

                filtered.add(
                        subscription
                );
            }
        }


        displaySubscriptions(
                filtered
        );
    }


    // =====================================================
    // DISPLAY
    // =====================================================

    private void displaySubscriptions(
            List<Map<String, Object>> subscriptions
    ) {

        subscriptionListBox
                .getChildren()
                .clear();


        if (
                subscriptions == null
                        ||
                subscriptions.isEmpty()
        ) {

            subscriptionListBox
                    .getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                Map<String, Object> subscription :
                subscriptions
        ) {

            subscriptionListBox
                    .getChildren()
                    .add(
                            createSubscriptionCard(
                                    subscription
                            )
                    );
        }
    }


    // =====================================================
    // SUBSCRIPTION CARD
    // =====================================================

    private VBox createSubscriptionCard(
            Map<String, Object> subscription
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
                new VBox(5);


        String userName =
                value(
                        stringValue(
                                subscription.get(
                                        "userName"
                                )
                        ),
                        "Unknown User"
                );


        String dietitianName =
                value(
                        stringValue(
                                subscription.get(
                                        "dietitianName"
                                )
                        ),
                        "Unknown Dietitian"
                );


        Label name =
                new Label(
                        userName
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


        Label relation =
                new Label(
                        "Subscribed to "
                                + dietitianName
                );


        relation.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        identity.getChildren()
                .addAll(
                        name,
                        relation
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label badge =
                createStatusBadge(
                        getDisplayStatus(
                                subscription
                        )
                );


        header.getChildren()
                .addAll(
                        identity,
                        spacer,
                        badge
                );


        Separator divider =
                new Separator();


        // =================================================
        // PRIMARY DETAILS
        // =================================================

        HBox details =
                new HBox(25);


        String plan =
                value(
                        stringValue(
                                subscription.get(
                                        "plan"
                                )
                        ),
                        "Not specified"
                );


        double amount =
                numberValue(
                        subscription.get(
                                "amount"
                        )
                );


        String paymentStatus =
                value(
                        stringValue(
                                subscription.get(
                                        "paymentStatus"
                                )
                        ),
                        "Unknown"
                );


        details.getChildren()
                .addAll(
                        createInfo(
                                "PLAN",
                                plan
                        ),

                        createInfo(
                                "AMOUNT",
                                formatCurrency(
                                        amount
                                )
                        ),

                        createInfo(
                                "PAYMENT",
                                paymentStatus.toUpperCase()
                        ),

                        createInfo(
                                "SUBSCRIBED",
                                formatFirestoreDate(
                                        subscription.get(
                                                "subscribedAt"
                                        )
                                )
                        ),

                        createInfo(
                                "EXPIRY",
                                formatFirestoreDate(
                                        subscription.get(
                                                "planExpiry"
                                        )
                                )
                        )
                );


        // =================================================
        // IDS
        // =================================================

        HBox ids =
                new HBox(18);


        ids.setPadding(
                new Insets(
                        12
                )
        );


        ids.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 10;"
        );


        ids.getChildren()
                .addAll(
                        createCompactInfo(
                                "USER UID",
                                stringValue(
                                        subscription.get(
                                                "userUid"
                                        )
                                )
                        ),

                        createCompactInfo(
                                "DIETITIAN UID",
                                stringValue(
                                        subscription.get(
                                                "dietitianUid"
                                        )
                                )
                        ),

                        createCompactInfo(
                                "PAYMENT ID",
                                stringValue(
                                        subscription.get(
                                                "paymentLinkId"
                                        )
                                )
                        )
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


        detailsButton.setOnAction(
                event ->
                        showDetails(
                                subscription
                        )
        );


        actions.getChildren()
                .add(
                        detailsButton
                );


        String status =
                getDisplayStatus(
                        subscription
                );


        if (
                status.equalsIgnoreCase(
                        "Active"
                )
        ) {

            Button cancelButton =
                    createDangerButton(
                            "Cancel Subscription"
                    );


            cancelButton.setOnAction(
                    event ->
                            cancelSubscription(
                                    subscription
                            )
            );


            actions.getChildren()
                    .add(
                            cancelButton
                    );


        } else if (
                status.equalsIgnoreCase(
                        "Cancelled"
                )
        ) {

            Button activateButton =
                    createSuccessButton(
                            "Reactivate"
                    );


            activateButton.setOnAction(
                    event ->
                            activateSubscription(
                                    subscription
                            )
            );


            actions.getChildren()
                    .add(
                            activateButton
                    );
        }


        card.getChildren()
                .addAll(
                        header,
                        divider,
                        details,
                        ids,
                        actions
                );


        return card;
    }


    // =====================================================
    // CANCEL SUBSCRIPTION
    // =====================================================

    private void cancelSubscription(
            Map<String, Object> subscription
    ) {

        String dietitianUid =
                stringValue(
                        subscription.get(
                                "dietitianUid"
                        )
                );


        String subscriberDocumentId =
                getSubscriberDocumentId(
                        subscription
                );


        if (
                dietitianUid.isBlank()
                        ||
                subscriberDocumentId.isBlank()
        ) {

            showError(
                    "Subscription identifiers are missing."
            );

            return;
        }


        String userName =
                value(
                        stringValue(
                                subscription.get(
                                        "userName"
                                )
                        ),
                        "this user"
                );


        if (
                !confirm(
                        "Cancel Subscription",
                        "Cancel "
                                + userName
                                + "'s subscription?",
                        "The subscription will be marked as CANCELLED."
                )
        ) {

            return;
        }


        executeAction(
                "Cancelling subscription...",
                () ->
                        adminController
                                .cancelSubscription(
                                        dietitianUid,
                                        subscriberDocumentId
                                ),
                "Subscription cancelled successfully.",
                "Unable to cancel subscription."
        );
    }


    // =====================================================
    // ACTIVATE SUBSCRIPTION
    // =====================================================

    private void activateSubscription(
            Map<String, Object> subscription
    ) {

        String dietitianUid =
                stringValue(
                        subscription.get(
                                "dietitianUid"
                        )
                );


        String subscriberDocumentId =
                getSubscriberDocumentId(
                        subscription
                );


        if (
                dietitianUid.isBlank()
                        ||
                subscriberDocumentId.isBlank()
        ) {

            showError(
                    "Subscription identifiers are missing."
            );

            return;
        }


        if (
                !confirm(
                        "Reactivate Subscription",
                        "Reactivate this subscription?",
                        "The subscriptionStatus field will be changed back to ACTIVE."
                )
        ) {

            return;
        }


        executeAction(
                "Reactivating subscription...",
                () ->
                        adminController
                                .activateSubscription(
                                        dietitianUid,
                                        subscriberDocumentId
                                ),
                "Subscription reactivated successfully.",
                "Unable to reactivate subscription."
        );
    }


    // =====================================================
    // EXECUTE ACTION
    // =====================================================

    private void executeAction(
            String loadingText,
            SubscriptionAction action,
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


                        loadSubscriptions();

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
    private interface SubscriptionAction {

        boolean execute();
    }


    // =====================================================
    // DETAILS DIALOG
    // =====================================================

    private void showDetails(
            Map<String, Object> subscription
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Subscription Details"
        );


        dialog.setHeaderText(
                value(
                        stringValue(
                                subscription.get(
                                        "userName"
                                )
                        ),
                        "Subscription"
                )
        );


        ButtonType close =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog.getDialogPane()
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
                580
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        content.getChildren()
                .addAll(
                        detailRow(
                                "User Name",
                                stringValue(
                                        subscription.get(
                                                "userName"
                                        )
                                )
                        ),

                        detailRow(
                                "User UID",
                                stringValue(
                                        subscription.get(
                                                "userUid"
                                        )
                                )
                        ),

                        detailRow(
                                "Dietitian",
                                stringValue(
                                        subscription.get(
                                                "dietitianName"
                                        )
                                )
                        ),

                        detailRow(
                                "Dietitian UID",
                                stringValue(
                                        subscription.get(
                                                "dietitianUid"
                                        )
                                )
                        ),

                        detailRow(
                                "Plan",
                                stringValue(
                                        subscription.get(
                                                "plan"
                                        )
                                )
                        ),

                        detailRow(
                                "Amount",
                                formatCurrency(
                                        numberValue(
                                                subscription.get(
                                                        "amount"
                                                )
                                        )
                                )
                        ),

                        detailRow(
                                "Payment Status",
                                stringValue(
                                        subscription.get(
                                                "paymentStatus"
                                        )
                                )
                        ),

                        detailRow(
                                "Payment Link ID",
                                stringValue(
                                        subscription.get(
                                                "paymentLinkId"
                                        )
                                )
                        ),

                        detailRow(
                                "Subscription Status",
                                getDisplayStatus(
                                        subscription
                                )
                        ),

                        detailRow(
                                "Subscribed At",
                                formatFirestoreDateTime(
                                        subscription.get(
                                                "subscribedAt"
                                        )
                                )
                        ),

                        detailRow(
                                "Plan Expiry",
                                formatFirestoreDateTime(
                                        subscription.get(
                                                "planExpiry"
                                        )
                                )
                        ),

                        detailRow(
                                "Cancelled At",
                                formatFirestoreDateTime(
                                        subscription.get(
                                                "cancelledAt"
                                        )
                                )
                        ),

                        detailRow(
                                "Reactivated At",
                                formatFirestoreDateTime(
                                        subscription.get(
                                                "reactivatedAt"
                                        )
                                )
                        ),

                        detailRow(
                                "Subscriber Document",
                                getSubscriberDocumentId(
                                        subscription
                                )
                        )
                );


        dialog.getDialogPane()
                .setContent(
                        content
                );


        dialog.getDialogPane()
                .setStyle(
                        "-fx-background-color: "
                                + CARD_BACKGROUND
                                + ";"
                );


        dialog.showAndWait();
    }


    // =====================================================
    // STATISTICS
    // =====================================================

    private void updateStatistics() {

        int total =
                allSubscriptions.size();


        int active =
                0;


        int expired =
                0;


        int cancelled =
                0;


        double revenue =
                0;


        for (
                Map<String, Object> subscription :
                allSubscriptions
        ) {

            if (
                    subscription == null
            ) {

                continue;
            }


            String status =
                    getDisplayStatus(
                            subscription
                    );


            if (
                    status.equalsIgnoreCase(
                            "Active"
                    )
            ) {

                active++;

            } else if (
                    status.equalsIgnoreCase(
                            "Expired"
                    )
            ) {

                expired++;

            } else if (
                    status.equalsIgnoreCase(
                            "Cancelled"
                    )
            ) {

                cancelled++;
            }


            String paymentStatus =
                    stringValue(
                            subscription.get(
                                    "paymentStatus"
                            )
                    );


            if (
                    paymentStatus.equalsIgnoreCase(
                            "paid"
                    )
            ) {

                revenue +=
                        numberValue(
                                subscription.get(
                                        "amount"
                                )
                        );
            }
        }


        totalSubscriptionsValue.setText(
                String.valueOf(
                        total
                )
        );


        activeSubscriptionsValue.setText(
                String.valueOf(
                        active
                )
        );


        expiredSubscriptionsValue.setText(
                String.valueOf(
                        expired
                )
        );


        cancelledSubscriptionsValue.setText(
                String.valueOf(
                        cancelled
                )
        );


        revenueValue.setText(
                formatCurrency(
                        revenue
                )
        );
    }


    // =====================================================
    // DETERMINE DISPLAY STATUS
    // =====================================================

    private String getDisplayStatus(
            Map<String, Object> subscription
    ) {

        String subscriptionStatus =
                stringValue(
                        subscription.get(
                                "subscriptionStatus"
                        )
                );


        if (
                subscriptionStatus.equalsIgnoreCase(
                        "CANCELLED"
                )
        ) {

            return "Cancelled";
        }


        Object expiry =
                subscription.get(
                        "planExpiry"
                );


        long expiryMillis =
                timestampToMillis(
                        expiry
                );


        if (
                expiryMillis > 0
                        &&
                expiryMillis < System.currentTimeMillis()
        ) {

            return "Expired";
        }


        return "Active";
    }


    // =====================================================
    // STATUS BADGE
    // =====================================================

    private Label createStatusBadge(
            String status
    ) {

        String textColor;

        String background;


        if (
                status.equalsIgnoreCase(
                        "Active"
                )
        ) {

            textColor =
                    GREEN;

            background =
                    GREEN_DARK;


        } else if (
                status.equalsIgnoreCase(
                        "Expired"
                )
        ) {

            textColor =
                    AMBER;

            background =
                    AMBER_DARK;


        } else {

            textColor =
                    RED;

            background =
                    RED_DARK;
        }


        Label badge =
                new Label(
                        status.toUpperCase()
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
                135
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
                        value(
                                valueText,
                                "—"
                        )
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


        box.getChildren()
                .addAll(
                        title,
                        value
                );


        return box;
    }


    // =====================================================
    // COMPACT INFO
    // =====================================================

    private VBox createCompactInfo(
            String titleText,
            String valueText
    ) {

        VBox box =
                new VBox(3);


        HBox.setHgrow(
                box,
                Priority.ALWAYS
        );


        box.setMaxWidth(
                Double.MAX_VALUE
        );


        Label title =
                new Label(
                        titleText
                );


        title.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 9px;"
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
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 11px;"
        );


        box.getChildren()
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


        row.setAlignment(
                Pos.TOP_LEFT
        );


        Label title =
                new Label(
                        titleText
                );


        title.setMinWidth(
                145
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
                        value(
                                valueText,
                                "—"
                        )
                );


        value.setWrapText(
                true
        );


        value.setMaxWidth(
                400
        );


        value.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        row.getChildren()
                .addAll(
                        title,
                        value
                );


        return row;
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


        button.setPrefHeight(
                35
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
                "-fx-font-size: 11px;"
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


        button.setPrefHeight(
                35
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
                "-fx-font-size: 11px;"
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


        button.setPrefHeight(
                35
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
                "-fx-font-size: 11px;"
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
                "-fx-border-radius: 14;"
                        +
                "-fx-border-color: "
                        + BORDER
                        + ";"
        );


        Label title =
                new Label(
                        "No subscriptions found"
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
                        "Try changing your search or subscription filters."
                );


        message.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
        );


        box.getChildren()
                .addAll(
                        title,
                        message
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
    // TASK
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

    private String getSubscriberDocumentId(
            Map<String, Object> subscription
    ) {

        String id =
                stringValue(
                        subscription.get(
                                "subscriberDocumentId"
                        )
                );


        if (
                !id.isBlank()
        ) {

            return id;
        }


        /*
         * Your existing structure uses userUid as the
         * subscribers document ID, so this is a safe
         * fallback for older data.
         */

        return stringValue(
                subscription.get(
                        "userUid"
                )
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


    private String stringValue(
            Object object
    ) {

        if (
                object == null
        ) {

            return "";
        }


        return String.valueOf(
                object
        ).trim();
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


    private double numberValue(
            Object object
    ) {

        if (
                object instanceof Number
        ) {

            return ((Number) object)
                    .doubleValue();
        }


        if (
                object != null
        ) {

            try {

                return Double.parseDouble(
                        object.toString()
                );

            } catch (
                    NumberFormatException ignored
            ) {

            }
        }


        return 0;
    }


    // =====================================================
    // TIMESTAMP CONVERSION
    // =====================================================

    private long timestampToMillis(
            Object object
    ) {

        if (
                object == null
        ) {

            return 0;
        }


        if (
                object instanceof Timestamp
        ) {

            return ((Timestamp) object)
                    .toDate()
                    .getTime();
        }


        if (
                object instanceof Date
        ) {

            return ((Date) object)
                    .getTime();
        }


        if (
                object instanceof Number
        ) {

            return ((Number) object)
                    .longValue();
        }


        return 0;
    }


    // =====================================================
    // DATE FORMAT
    // =====================================================

    private String formatFirestoreDate(
            Object timestamp
    ) {

        long millis =
                timestampToMillis(
                        timestamp
                );


        if (
                millis <= 0
        ) {

            return "Not available";
        }


        return new SimpleDateFormat(
                "dd MMM yyyy"
        ).format(
                new Date(
                        millis
                )
        );
    }


    private String formatFirestoreDateTime(
            Object timestamp
    ) {

        long millis =
                timestampToMillis(
                        timestamp
                );


        if (
                millis <= 0
        ) {

            return "Not available";
        }


        return new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a"
        ).format(
                new Date(
                        millis
                )
        );
    }


    // =====================================================
    // CURRENCY
    // =====================================================

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
}