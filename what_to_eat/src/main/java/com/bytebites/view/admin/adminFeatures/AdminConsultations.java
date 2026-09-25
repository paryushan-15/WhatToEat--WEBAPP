package com.bytebites.view.admin.adminFeatures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bytebites.controller.AdminController;
import com.bytebites.model.UserDietitianConsultationModel;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdminConsultations {

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

    private static final String PURPLE_DARK =
            "#2E1B47";


    // =====================================================
    // CONTROLLER
    // =====================================================

    private final AdminController adminController;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;

    private final VBox consultationListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<UserDietitianConsultationModel>
            allConsultations;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> statusFilter;


    // =====================================================
    // STATISTICS
    // =====================================================

    private Label totalValue;

    private Label pendingValue;

    private Label confirmedValue;

    private Label completedValue;

    private Label cancelledValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminConsultations() {

        adminController =
                new AdminController();


        root =
                new BorderPane();


        consultationListBox =
                new VBox(15);


        allConsultations =
                new ArrayList<>();


        buildPage();

        loadConsultations();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getConsultationsPage() {

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
                        consultationListBox
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
                        "Consultations"
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
                        "Monitor and manage all User-Dietitian consultation bookings."
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
                        loadConsultations()
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


        totalValue =
                new Label("0");

        pendingValue =
                new Label("0");

        confirmedValue =
                new Label("0");

        completedValue =
                new Label("0");

        cancelledValue =
                new Label("0");


        VBox total =
                createStatCard(
                        "Total",
                        totalValue,
                        PRIMARY_BLUE
                );


        VBox pending =
                createStatCard(
                        "Pending / Accepted",
                        pendingValue,
                        AMBER
                );


        VBox confirmed =
                createStatCard(
                        "Confirmed",
                        confirmedValue,
                        PURPLE
                );


        VBox completed =
                createStatCard(
                        "Completed",
                        completedValue,
                        GREEN
                );


        VBox cancelled =
                createStatCard(
                        "Cancelled",
                        cancelledValue,
                        RED
                );


        VBox[] cards = {
                total,
                pending,
                confirmed,
                completed,
                cancelled
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
                "-fx-font-size: 24px;"
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
                "Search User, Dietitian, Consultation ID, date or reason..."
        );


        searchField.setPrefWidth(
                430
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
                        "Dietitian Accepted",
                        "Confirmed",
                        "Completed",
                        "Cancelled"
                );


        statusFilter.setValue(
                "All"
        );


        statusFilter.setPrefWidth(
                205
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


        filters.getChildren()
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
                        "Loading consultations..."
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
    // LOAD CONSULTATIONS
    // =====================================================

    private void loadConsultations() {

        setLoading(
                true,
                "Loading consultations..."
        );


        Task<List<UserDietitianConsultationModel>> task =
                new Task<>() {

                    @Override
                    protected List<UserDietitianConsultationModel>
                    call() {

                        return adminController
                                .getAllConsultations();
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<UserDietitianConsultationModel> loaded =
                            task.getValue();


                    if (
                            loaded == null
                    ) {

                        loaded =
                                new ArrayList<>();
                    }


                    allConsultations =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allConsultations.size()
                                    + " consultation(s) loaded."
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
                            "Unable to load consultations."
                    );


                    showError(
                            "Unable to load consultations from the database."
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
                statusFilter
                        .getValue();


        List<UserDietitianConsultationModel> filtered =
                new ArrayList<>();


        for (
                UserDietitianConsultationModel consultation :
                allConsultations
        ) {

            if (
                    consultation == null
            ) {

                continue;
            }


            boolean searchMatches =
                    query.isEmpty()

                            ||

                    contains(
                            consultation.getConsultationId(),
                            query
                    )

                            ||

                    contains(
                            consultation.getUserName(),
                            query
                    )

                            ||

                    contains(
                            consultation.getUserId(),
                            query
                    )

                            ||

                    contains(
                            consultation.getDietitianName(),
                            query
                    )

                            ||

                    contains(
                            consultation.getDietitianId(),
                            query
                    )

                            ||

                    contains(
                            consultation.getRequestedDate(),
                            query
                    )

                            ||

                    contains(
                            consultation.getRequestedTime(),
                            query
                    )

                            ||

                    contains(
                            consultation.getConfirmedDate(),
                            query
                    )

                            ||

                    contains(
                            consultation.getReason(),
                            query
                    );


            if (
                    !searchMatches
            ) {

                continue;
            }


            if (
                    matchesStatus(
                            consultation,
                            selectedStatus
                    )
            ) {

                filtered.add(
                        consultation
                );
            }
        }


        displayConsultations(
                filtered
        );
    }


    // =====================================================
    // MATCH STATUS
    // =====================================================

    private boolean matchesStatus(
            UserDietitianConsultationModel consultation,
            String selectedStatus
    ) {

        if (
                selectedStatus == null
                        ||
                selectedStatus.equalsIgnoreCase(
                        "All"
                )
        ) {

            return true;
        }


        String status =
                value(
                        consultation.getStatus(),
                        "PENDING"
                );


        if (
                selectedStatus.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            return status.equalsIgnoreCase(
                    "PENDING"
            );
        }


        if (
                selectedStatus.equalsIgnoreCase(
                        "Dietitian Accepted"
                )
        ) {

            return status.equalsIgnoreCase(
                    "DIETITIAN_ACCEPTED"
            );
        }


        if (
                selectedStatus.equalsIgnoreCase(
                        "Confirmed"
                )
        ) {

            return status.equalsIgnoreCase(
                    "CONFIRMED"
            );
        }


        if (
                selectedStatus.equalsIgnoreCase(
                        "Completed"
                )
        ) {

            return status.equalsIgnoreCase(
                    "COMPLETED"
            );
        }


        if (
                selectedStatus.equalsIgnoreCase(
                        "Cancelled"
                )
        ) {

            return status.equalsIgnoreCase(
                    "CANCELLED"
            );
        }


        return true;
    }


    // =====================================================
    // DISPLAY
    // =====================================================

    private void displayConsultations(
            List<UserDietitianConsultationModel> consultations
    ) {

        consultationListBox
                .getChildren()
                .clear();


        if (
                consultations == null
                        ||
                consultations.isEmpty()
        ) {

            consultationListBox
                    .getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                UserDietitianConsultationModel consultation :
                consultations
        ) {

            consultationListBox
                    .getChildren()
                    .add(
                            createConsultationCard(
                                    consultation
                            )
                    );
        }
    }


    // =====================================================
    // CONSULTATION CARD
    // =====================================================

    private VBox createConsultationCard(
            UserDietitianConsultationModel consultation
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
                new HBox(15);


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox people =
                new VBox(5);


        Label title =
                new Label(
                        value(
                                consultation.getUserName(),
                                "Unknown User"
                        )
                                +
                        "  →  "
                                +
                        value(
                                consultation.getDietitianName(),
                                "Unknown Dietitian"
                        )
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


        Label consultationId =
                new Label(
                        "Consultation ID: "
                                +
                        value(
                                consultation.getConsultationId(),
                                "—"
                        )
                );


        consultationId.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 11px;"
        );


        people.getChildren()
                .addAll(
                        title,
                        consultationId
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label status =
                createStatusBadge(
                        consultation.getStatus()
                );


        top.getChildren()
                .addAll(
                        people,
                        spacer,
                        status
                );


        Separator separator =
                new Separator();


        // =================================================
        // DETAILS
        // =================================================

        HBox details =
                new HBox(24);


        details.getChildren()
                .addAll(
                        createInfo(
                                "REQUESTED DATE",
                                value(
                                        consultation.getRequestedDate(),
                                        "Not provided"
                                )
                        ),

                        createInfo(
                                "REQUESTED TIME",
                                value(
                                        consultation.getRequestedTime(),
                                        "Not provided"
                                )
                        ),

                        createInfo(
                                "CONFIRMED DATE",
                                value(
                                        consultation.getConfirmedDate(),
                                        "Not confirmed"
                                )
                        ),

                        createInfo(
                                "CONFIRMED TIME",
                                value(
                                        consultation.getConfirmedTime(),
                                        "Not confirmed"
                                )
                        ),

                        createInfo(
                                "CREATED",
                                formatDateTime(
                                        consultation.getCreatedAt()
                                )
                        )
                );


        // =================================================
        // REASON
        // =================================================

        VBox reasonBox =
                new VBox(5);


        Label reasonTitle =
                new Label(
                        "CONSULTATION REASON"
                );


        reasonTitle.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 10px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label reason =
                new Label(
                        value(
                                consultation.getReason(),
                                "No reason provided."
                        )
                );


        reason.setWrapText(
                true
        );


        reason.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 12px;"
        );


        reasonBox.getChildren()
                .addAll(
                        reasonTitle,
                        reason
                );


        // =================================================
        // MEETING INFO
        // =================================================

        HBox meetingRow =
                createMeetingRow(
                        consultation
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
                                consultation
                        )
        );


        Button statusButton =
                createNeutralButton(
                        "Change Status"
                );


        statusButton.setOnAction(
                event ->
                        showStatusDialog(
                                consultation
                        )
        );


        actions.getChildren()
                .addAll(
                        detailsButton,
                        statusButton
                );


        String currentStatus =
                value(
                        consultation.getStatus(),
                        "PENDING"
                );


        if (
                !currentStatus.equalsIgnoreCase(
                        "COMPLETED"
                )
                        &&
                !currentStatus.equalsIgnoreCase(
                        "CANCELLED"
                )
        ) {

            Button completeButton =
                    createSuccessButton(
                            "Mark Completed"
                    );


            completeButton.setOnAction(
                    event ->
                            completeConsultation(
                                    consultation
                            )
            );


            Button cancelButton =
                    createWarningButton(
                            "Cancel"
                    );


            cancelButton.setOnAction(
                    event ->
                            cancelConsultation(
                                    consultation
                            )
            );


            actions.getChildren()
                    .addAll(
                            completeButton,
                            cancelButton
                    );
        }


        Button deleteButton =
                createDangerButton(
                        "Delete"
                );


        deleteButton.setOnAction(
                event ->
                        deleteConsultation(
                                consultation
                        )
        );


        actions.getChildren()
                .add(
                        deleteButton
                );


        card.getChildren()
                .addAll(
                        top,
                        separator,
                        details,
                        reasonBox,
                        meetingRow,
                        actions
                );


        return card;
    }


    // =====================================================
    // MEETING ROW
    // =====================================================

    private HBox createMeetingRow(
            UserDietitianConsultationModel consultation
    ) {

        HBox row =
                new HBox(12);


        row.setAlignment(
                Pos.CENTER_LEFT
        );


        row.setPadding(
                new Insets(
                        12
                )
        );


        row.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 10;"
        );


        Label title =
                new Label(
                        "Meeting Link"
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


        String link =
                value(
                        consultation.getMeetingLink(),
                        ""
                );


        Label linkLabel =
                new Label(
                        link.isEmpty()
                                ? "Not available"
                                : link
                );


        linkLabel.setWrapText(
                true
        );


        linkLabel.setMaxWidth(
                500
        );


        linkLabel.setStyle(
                "-fx-text-fill: "
                        + (
                        link.isEmpty()
                                ? TEXT_SECONDARY
                                : PRIMARY_BLUE
                )
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        row.getChildren()
                .addAll(
                        title,
                        linkLabel,
                        spacer
                );


        if (
                !link.isEmpty()
        ) {

            Button copyButton =
                    createNeutralButton(
                            "Copy Link"
                    );


            copyButton.setOnAction(
                    event ->
                            copyToClipboard(
                                    link
                            )
            );


            row.getChildren()
                    .add(
                            copyButton
                    );
        }


        return row;
    }


    // =====================================================
    // VIEW DETAILS
    // =====================================================

    private void showDetails(
            UserDietitianConsultationModel consultation
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Consultation Details"
        );


        dialog.setHeaderText(
                value(
                        consultation.getUserName(),
                        "User"
                )
                        +
                " → "
                        +
                value(
                        consultation.getDietitianName(),
                        "Dietitian"
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
                620
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        content.getChildren()
                .addAll(
                        detailRow(
                                "Consultation ID",
                                consultation.getConsultationId()
                        ),

                        detailRow(
                                "User",
                                consultation.getUserName()
                        ),

                        detailRow(
                                "User UID",
                                consultation.getUserId()
                        ),

                        detailRow(
                                "Dietitian",
                                consultation.getDietitianName()
                        ),

                        detailRow(
                                "Dietitian UID",
                                consultation.getDietitianId()
                        ),

                        detailRow(
                                "Status",
                                consultation.getStatus()
                        ),

                        detailRow(
                                "Requested Date",
                                consultation.getRequestedDate()
                        ),

                        detailRow(
                                "Requested Time",
                                consultation.getRequestedTime()
                        ),

                        detailRow(
                                "Confirmed Date",
                                consultation.getConfirmedDate()
                        ),

                        detailRow(
                                "Confirmed Time",
                                consultation.getConfirmedTime()
                        ),

                        detailRow(
                                "Reason",
                                consultation.getReason()
                        ),

                        detailRow(
                                "Dietitian Message",
                                consultation.getDietitianMessage()
                        ),

                        detailRow(
                                "Meeting Link",
                                consultation.getMeetingLink()
                        ),

                        detailRow(
                                "Created At",
                                formatDateTime(
                                        consultation.getCreatedAt()
                                )
                        ),

                        detailRow(
                                "Accepted At",
                                formatDateTime(
                                        consultation.getAcceptedAt()
                                )
                        ),

                        detailRow(
                                "Confirmed At",
                                formatDateTime(
                                        consultation.getConfirmedAt()
                                )
                        ),

                        detailRow(
                                "Completed At",
                                formatDateTime(
                                        consultation.getCompletedAt()
                                )
                        ),

                        detailRow(
                                "Cancelled At",
                                formatDateTime(
                                        consultation.getCancelledAt()
                                )
                        )
                );


        ScrollPane scroll =
                new ScrollPane(
                        content
                );


        scroll.setFitToWidth(
                true
        );


        scroll.setPrefHeight(
                600
        );


        scroll.setStyle(
                "-fx-background: "
                        + CARD_BACKGROUND
                        + ";"
                        +
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        dialog.getDialogPane()
                .setContent(
                        scroll
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
    // CHANGE STATUS DIALOG
    // =====================================================

    private void showStatusDialog(
            UserDietitianConsultationModel consultation
    ) {

        SameStageDialog<ButtonType> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Update Consultation"
        );


        dialog.setHeaderText(
                "Change Consultation Status"
        );


        ButtonType updateButton =
                new ButtonType(
                        "Update",
                        ButtonBar.ButtonData.OK_DONE
                );


        ButtonType cancelButton =
                new ButtonType(
                        "Cancel",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );


        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        updateButton,
                        cancelButton
                );


        ComboBox<String> status =
                new ComboBox<>();


        status.getItems()
                .addAll(
                        "PENDING",
                        "DIETITIAN_ACCEPTED",
                        "CONFIRMED",
                        "COMPLETED",
                        "CANCELLED"
                );


        String currentStatus =
                value(
                        consultation.getStatus(),
                        "PENDING"
                );


        status.setValue(
                currentStatus
        );


        status.setPrefWidth(
                300
        );


        status.setPrefHeight(
                40
        );


        TextArea warning =
                new TextArea(
                        "Admin override changes the consultation status directly. "
                                +
                        "Use this only when a booking needs administrative correction."
                );


        warning.setEditable(
                false
        );


        warning.setWrapText(
                true
        );


        warning.setPrefRowCount(
                3
        );


        warning.setStyle(
                "-fx-control-inner-background: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
        );


        VBox content =
                new VBox(12);


        content.setPadding(
                new Insets(
                        15
                )
        );


        content.setPrefWidth(
                420
        );


        content.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND
                        + ";"
        );


        Label label =
                new Label(
                        "Status"
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-weight: bold;"
        );


        content.getChildren()
                .addAll(
                        label,
                        status,
                        warning
                );


        dialog.getDialogPane()
                .setContent(
                        content
                );


        Optional<ButtonType> result =
                dialog.showAndWait();


        if (
                result.isEmpty()
                        ||
                result.get()
                        != updateButton
        ) {

            return;
        }


        String newStatus =
                status.getValue();


        if (
                newStatus == null
        ) {

            return;
        }


        if (
                newStatus.equalsIgnoreCase(
                        currentStatus
                )
        ) {

            showInformation(
                    "The consultation already has this status."
            );

            return;
        }


        updateStatus(
                consultation,
                newStatus
        );
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private void updateStatus(
            UserDietitianConsultationModel consultation,
            String status
    ) {

        if (
                status.equalsIgnoreCase(
                        "CANCELLED"
                )
        ) {

            cancelConsultation(
                    consultation
            );

            return;
        }


        executeAction(
                "Updating consultation status...",
                () ->
                        adminController
                                .updateConsultationStatus(
                                        consultation.getConsultationId(),
                                        status
                                ),
                "Consultation status updated successfully.",
                "Unable to update consultation status."
        );
    }


    // =====================================================
    // COMPLETE
    // =====================================================

    private void completeConsultation(
            UserDietitianConsultationModel consultation
    ) {

        if (
                !confirm(
                        "Complete Consultation",
                        "Mark this consultation as completed?",
                        value(
                                consultation.getUserName(),
                                "User"
                        )
                                +
                        " and "
                                +
                        value(
                                consultation.getDietitianName(),
                                "Dietitian"
                        )
                                +
                        " will have this booking marked as completed."
                )
        ) {

            return;
        }


        executeAction(
                "Completing consultation...",
                () ->
                        adminController
                                .updateConsultationStatus(
                                        consultation.getConsultationId(),
                                        "COMPLETED"
                                ),
                "Consultation marked as completed.",
                "Unable to complete consultation."
        );
    }


    // =====================================================
    // CANCEL
    // =====================================================

    private void cancelConsultation(
            UserDietitianConsultationModel consultation
    ) {

        if (
                !confirm(
                        "Cancel Consultation",
                        "Cancel this consultation?",
                        "The booking will be changed to CANCELLED."
                )
        ) {

            return;
        }


        executeAction(
                "Cancelling consultation...",
                () ->
                        adminController
                                .cancelConsultation(
                                        consultation.getConsultationId()
                                ),
                "Consultation cancelled successfully.",
                "Unable to cancel consultation."
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    private void deleteConsultation(
            UserDietitianConsultationModel consultation
    ) {

        if (
                !confirm(
                        "Delete Consultation",
                        "Permanently delete this consultation?",
                        "This removes the consultation document from Firestore and cannot be undone."
                )
        ) {

            return;
        }


        executeAction(
                "Deleting consultation...",
                () ->
                        adminController
                                .deleteConsultation(
                                        consultation.getConsultationId()
                                ),
                "Consultation deleted successfully.",
                "Unable to delete consultation."
        );
    }


    // =====================================================
    // GENERIC DATABASE ACTION
    // =====================================================

    private void executeAction(
            String loadingText,
            ConsultationAction action,
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


                        loadConsultations();

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
    private interface ConsultationAction {

        boolean execute();
    }


    // =====================================================
    // STATISTICS
    // =====================================================

    private void updateStatistics() {

        int total =
                allConsultations.size();


        int pending =
                0;

        int confirmed =
                0;

        int completed =
                0;

        int cancelled =
                0;


        for (
                UserDietitianConsultationModel consultation :
                allConsultations
        ) {

            if (
                    consultation == null
            ) {

                continue;
            }


            String status =
                    value(
                            consultation.getStatus(),
                            "PENDING"
                    );


            if (
                    status.equalsIgnoreCase(
                            "PENDING"
                    )
                            ||
                    status.equalsIgnoreCase(
                            "DIETITIAN_ACCEPTED"
                    )
            ) {

                pending++;

            } else if (
                    status.equalsIgnoreCase(
                            "CONFIRMED"
                    )
            ) {

                confirmed++;

            } else if (
                    status.equalsIgnoreCase(
                            "COMPLETED"
                    )
            ) {

                completed++;

            } else if (
                    status.equalsIgnoreCase(
                            "CANCELLED"
                    )
            ) {

                cancelled++;
            }
        }


        totalValue.setText(
                String.valueOf(
                        total
                )
        );


        pendingValue.setText(
                String.valueOf(
                        pending
                )
        );


        confirmedValue.setText(
                String.valueOf(
                        confirmed
                )
        );


        completedValue.setText(
                String.valueOf(
                        completed
                )
        );


        cancelledValue.setText(
                String.valueOf(
                        cancelled
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
                        "PENDING"
                );


        String textColor;

        String background;


        if (
                actual.equalsIgnoreCase(
                        "PENDING"
                )
        ) {

            textColor =
                    AMBER;

            background =
                    AMBER_DARK;


        } else if (
                actual.equalsIgnoreCase(
                        "DIETITIAN_ACCEPTED"
                )
        ) {

            textColor =
                    PRIMARY_BLUE;

            background =
                    "#3A2617";


        } else if (
                actual.equalsIgnoreCase(
                        "CONFIRMED"
                )
        ) {

            textColor =
                    PURPLE;

            background =
                    PURPLE_DARK;


        } else if (
                actual.equalsIgnoreCase(
                        "COMPLETED"
                )
        ) {

            textColor =
                    GREEN;

            background =
                    GREEN_DARK;


        } else {

            textColor =
                    RED;

            background =
                    RED_DARK;
        }


        Label badge =
                new Label(
                        prettifyStatus(
                                actual
                        )
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
    // INFO FIELD
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
                140
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
                420
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


        button.setPadding(
                new Insets(
                        0,
                        13,
                        0,
                        13
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


    private Button createWarningButton(
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
                        + AMBER_DARK
                        + ";"
                        +
                "-fx-background-radius: 8;"
                        +
                "-fx-text-fill: "
                        + AMBER
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
                        "No consultations found"
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
                        "Try changing the search or status filter."
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
    // COPY LINK
    // =====================================================

    private void copyToClipboard(
            String text
    ) {

        if (
                text == null
                        ||
                text.isBlank()
        ) {

            return;
        }


        Clipboard clipboard =
                Clipboard.getSystemClipboard();


        ClipboardContent content =
                new ClipboardContent();


        content.putString(
                text
        );


        clipboard.setContent(
                content
        );


        showInformation(
                "Meeting link copied to clipboard."
        );
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
    // BACKGROUND TASK
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


    private String prettifyStatus(
            String status
    ) {

        if (
                status == null
                        ||
                status.isBlank()
        ) {

            return "PENDING";
        }


        if (
                status.equalsIgnoreCase(
                        "DIETITIAN_ACCEPTED"
                )
        ) {

            return "DIETITIAN ACCEPTED";
        }


        return status
                .replace(
                        "_",
                        " "
                )
                .toUpperCase();
    }


    // =====================================================
    // DATE
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