package com.bytebites.view.dietitian.dietitionFeatures;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.model.FamilyMember;
import com.bytebites.model.session.SessionManager;
import com.bytebites.dao.DietitianConsultationDao;
import com.bytebites.model.UserDietitianConsultationModel;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import com.bytebites.view.common.SameStageDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DietitianDashBoard {

    // =========================================================
    // COLORS
    // =========================================================

    private final String BG = "#0F172A";
    private final String CARD = "#1E293B";
    private final String CARD_LIGHT = "#243247";
    private final String BORDER = "#334155";
    private final String WHITE = "#F8FAFC";
    private final String TEXT = "#CBD5E1";
    private final String MUTED = "#94A3B8";
    private final String BLUE = "#1976D2";
    private final String GREEN = "#22C55E";
    private final String ORANGE = "#F59E0B";

    // =========================================================
    // DAO
    // =========================================================

    private final DietitianDao dietitianDao =
            new DietitianDao();

    private final FamilyMemberDao familyMemberDao =
            new FamilyMemberDao();
    
    private final DietitianConsultationDao consultationDao =
        new DietitianConsultationDao();

    // =========================================================
    // CURRENT DIETITIAN
    // =========================================================

    private String dietitianUid;

    // =========================================================
    // MAIN DASHBOARD
    // =========================================================

    public ScrollPane getDashboard() {

        dietitianUid =
                SessionManager.getUid();

        // =====================================================
        // LOAD DATA
        // =====================================================

        List<ClientDashboardData> clients =
                loadSubscribedClients();

        int activeClientCount =
                clients.size();

        int approvedMealPlanCount =
                getApprovedMealPlanCount();

        double averageRating =
                getAverageRating();

        List<ConsultationDashboardData> todaysConsultations =
                getTodaysConsultations();

        // =====================================================
        // MAIN CONTAINER
        // =====================================================

        VBox dashboard =
                new VBox(24);

        dashboard.setPadding(
                new Insets(22, 24, 35, 24)
        );

        dashboard.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        dashboard.setFillWidth(true);

        // =====================================================
        // HEADER
        // =====================================================

        VBox header =
                createHeader();

        // =====================================================
        // STATISTICS
        // =====================================================

        HBox statistics =
                new HBox(16);

        statistics.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox activeClientsCard =
                statisticCard(
                        "♙",
                        "Active Clients",
                        String.valueOf(activeClientCount),
                        BLUE
                );

        VBox mealPlanCard =
                statisticCard(
                        "▦",
                        "Meal Plans",
                        String.valueOf(approvedMealPlanCount),
                        BLUE
                );

        VBox ratingCard =
                statisticCard(
                        "★",
                        "Ratings",
                        averageRating <= 0
                                ? "0.0"
                                : String.format("%.1f", averageRating),
                        ORANGE
                );

        VBox consultationCountCard =
                statisticCard(
                        "▣",
                        "Today's Consultations",
                        String.valueOf(todaysConsultations.size()),
                        GREEN
                );

        statistics.getChildren().addAll(
                activeClientsCard,
                mealPlanCard,
                ratingCard,
                consultationCountCard
        );

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        HBox mainContent =
                new HBox(18);

        mainContent.setAlignment(
                Pos.TOP_LEFT
        );

        VBox clientsAttention =
                createClientsAttentionCard(clients);

        VBox consultationsCard =
                createConsultationsCard(todaysConsultations);

        HBox.setHgrow(
                clientsAttention,
                Priority.ALWAYS
        );

        clientsAttention.setMaxWidth(
                Double.MAX_VALUE
        );

        consultationsCard.setPrefWidth(380);
        consultationsCard.setMinWidth(330);
        consultationsCard.setMaxWidth(430);

        mainContent.getChildren().addAll(
                clientsAttention,
                consultationsCard
        );

        dashboard.getChildren().addAll(
                header,
                statistics,
                mainContent
        );

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(dashboard);

        scrollPane.setFitToWidth(true);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setPannable(false);

        scrollPane.setStyle(
                "-fx-background-color: " + BG + ";" +
                "-fx-background: " + BG + ";" +
                "-fx-border-color: transparent;"
        );

        return scrollPane;
    }

    // =========================================================
    // HEADER
    // =========================================================

    private VBox createHeader() {

        VBox header =
                new VBox(5);

        String dietitianName =
                getLoggedInDietitianName();

        String greetingText;

        if (
                dietitianName == null ||
                dietitianName.trim().isEmpty()
        ) {

            greetingText =
                    "Good morning, Dietitian 👋";

        } else {

            greetingText =
                    "Good morning, Dietitian "
                            + dietitianName
                            + " 👋";
        }

        Label greeting =
                new Label(greetingText);

        greeting.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 27px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "Manage your clients, meal plans and consultations from one place."
                );

        subtitle.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 13px;"
        );

        header.getChildren().addAll(
                greeting,
                subtitle
        );

        return header;
    }

    // =========================================================
    // LOAD CLIENTS
    // =========================================================

    private List<ClientDashboardData>
            loadSubscribedClients() {

        List<ClientDashboardData> clients =
                new ArrayList<>();

        try {

            if (
                    dietitianUid == null ||
                    dietitianUid.trim().isEmpty()
            ) {

                return clients;
            }

            List<String> subscriberUserIds =
                    dietitianDao
                            .getSubscriberUserIds(
                                    dietitianUid
                            );

            if (subscriberUserIds == null) {

                return clients;
            }

            for (
                    String userId :
                    subscriberUserIds
            ) {

                List<FamilyMember> members =
                        familyMemberDao
                                .getFamilyMembersByUserId(
                                        userId
                                );

                if (
                        members == null ||
                        members.isEmpty()
                ) {

                    continue;
                }

                FamilyMember firstMember =
                        members.get(0);

                String familyName =
                        firstMember.getName()
                                + " Family";

                String goal =
                        firstMember.getGoal();

                if (
                        goal == null ||
                        goal.trim().isEmpty()
                ) {

                    goal =
                            "Nutrition";
                }

                String currentPlan =
                        getCurrentPlanName(
                                userId
                        );

                LocalDate expiry =
                        getCurrentPlanExpiry(
                                userId
                        );

                int adherence =
                        getClientAdherence(
                                userId
                        );

                ClientDashboardData client =
                        new ClientDashboardData(
                                userId,
                                familyName,
                                members.size(),
                                goal,
                                currentPlan,
                                expiry,
                                adherence
                        );

                clients.add(client);
            }

        } catch (Exception e) {

            System.err.println(
                    "Error loading dashboard clients:"
            );

            e.printStackTrace();
        }

        return clients;
    }

    // =========================================================
    // DIETITIAN NAME
    // =========================================================

    private String getLoggedInDietitianName() {

        return "";
    }

    // =========================================================
    // APPROVED MEAL PLANS
    // =========================================================

    private int getApprovedMealPlanCount() {

        return 0;
    }

    // =========================================================
    // AVERAGE RATING
    // =========================================================

    private double getAverageRating() {

        return 0.0;
    }

    // =========================================================
    // TODAY'S CONSULTATIONS
    // =========================================================

    private List<ConsultationDashboardData> getTodaysConsultations() {

                List<ConsultationDashboardData> result =
                        new ArrayList<>();

                List<UserDietitianConsultationModel> consultations =
                        consultationDao.getConsultationsByDietitianId(
                                dietitianUid
                        );

                LocalDate today = LocalDate.now();

                for (UserDietitianConsultationModel consultation : consultations) {

                        ConsultationDashboardData data =
                                convertConsultation(consultation);

                        if (data != null && today.equals(data.getDate())) {
                        result.add(data);
                        }
                }

                result.sort(
                        Comparator.comparing(
                                ConsultationDashboardData::getTime,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                );

                return result;
        }

    // =========================================================
    // CONSULTATIONS FOR MONTH
    // =========================================================

    private List<ConsultationDashboardData> getConsultationsForMonth(
                YearMonth month
        ) {

                List<ConsultationDashboardData> result =
                        new ArrayList<>();

                List<UserDietitianConsultationModel> consultations =
                        consultationDao.getConsultationsByDietitianId(
                                dietitianUid
                        );

                for (UserDietitianConsultationModel consultation : consultations) {

                        ConsultationDashboardData data =
                                convertConsultation(consultation);

                        if (data != null &&
                                data.getDate() != null &&
                                YearMonth.from(data.getDate()).equals(month)) {

                        result.add(data);
                        }
                }

                result.sort(
                        Comparator.comparing(
                                ConsultationDashboardData::getDate
                        ).thenComparing(
                                ConsultationDashboardData::getTime,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                );

                return result;
        }

        private ConsultationDashboardData convertConsultation(
                        UserDietitianConsultationModel consultation
                ) {

                if (consultation == null) {
                        return null;
                }

                String dateText =
                        firstNonBlank(
                                consultation.getConfirmedDate(),
                                consultation.getRequestedDate()
                        );

                String timeText =
                        firstNonBlank(
                                consultation.getConfirmedTime(),
                                consultation.getRequestedTime()
                        );

                LocalDate date = parseConsultationDate(dateText);

                if (date == null) {
                        return null;
                }

                LocalTime time = parseConsultationTime(timeText);

                String status =
                        consultation.getStatus() == null
                                ? ""
                                : consultation.getStatus().trim();

                // Past confirmed consultations are displayed as completed.
                if (date.isBefore(LocalDate.now()) &&
                        ("confirmed".equalsIgnoreCase(status) ||
                        "accepted".equalsIgnoreCase(status))) {

                        status = "completed";
                }

                String type =
                        consultation.getReason() == null ||
                        consultation.getReason().isBlank()
                                ? "Consultation"
                                : consultation.getReason();

                return new ConsultationDashboardData(
                        safeText(consultation.getUserName()),
                        date,
                        time,
                        type,
                        status
                );
                }

                private String firstNonBlank(
                        String first,
                        String second
                ) {

                if (first != null && !first.isBlank()) {
                        return first.trim();
                }

                return second == null ? "" : second.trim();
                }

                private LocalDate parseConsultationDate(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                String[] patterns = {
                        "yyyy-MM-dd",
                        "dd-MM-yyyy",
                        "dd/MM/yyyy",
                        "d/M/yyyy",
                        "dd MMM yyyy",
                        "d MMM yyyy"
                };

                for (String pattern : patterns) {
                        try {
                        return LocalDate.parse(
                                value.trim(),
                                DateTimeFormatter.ofPattern(pattern)
                        );
                        } catch (Exception ignored) {
                        }
                }

                return null;
                }

                private LocalTime parseConsultationTime(String value) {

                if (value == null || value.isBlank()) {
                        return null;
                }

                String cleaned = value.trim().toUpperCase();

                String[] patterns = {
                        "hh:mm a",
                        "h:mm a",
                        "HH:mm",
                        "H:mm"
                };

                for (String pattern : patterns) {
                        try {
                        return LocalTime.parse(
                                cleaned,
                                DateTimeFormatter.ofPattern(pattern)
                        );
                        } catch (Exception ignored) {
                        }
                }

                return null;
        }
    // =========================================================
    // CURRENT PLAN
    // =========================================================

    private String getCurrentPlanName(
            String userId
    ) {

        return "Family Plan";
    }

    // =========================================================
    // PLAN EXPIRY
    // =========================================================

    private LocalDate getCurrentPlanExpiry(
            String userId
    ) {

        return null;
    }

    // =========================================================
    // ADHERENCE
    // =========================================================

    private int getClientAdherence(
            String userId
    ) {

        return 0;
    }

    // =========================================================
    // STATISTIC CARD
    // =========================================================

    private VBox statisticCard(
            String icon,
            String title,
            String value,
            String accent
    ) {

        VBox card =
                new VBox(8);

        card.setPadding(
                new Insets(18)
        );

        card.setPrefHeight(110);
        card.setMinHeight(110);
        card.setMinWidth(160);

        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 11;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 11;"
        );

        HBox titleBox =
                new HBox(8);

        titleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        Label iconLabel =
                new Label(icon);

        iconLabel.setStyle(
                "-fx-text-fill: " + accent + ";" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Label titleLabel =
                new Label(title);

        titleLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 12px;"
        );

        titleBox.getChildren().addAll(
                iconLabel,
                titleLabel
        );

        Label valueLabel =
                new Label(value);

        valueLabel.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        card.getChildren().addAll(
                titleBox,
                valueLabel
        );

        HBox.setHgrow(
                card,
                Priority.ALWAYS
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        addHoverAnimation(
                card,
                1.025
        );

        return card;
    }

    // =========================================================
    // CLIENT ATTENTION
    // =========================================================

    private VBox createClientsAttentionCard(
            List<ClientDashboardData> clients
    ) {

        VBox card =
                new VBox();

        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 11;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 11;"
        );

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        18,
                        18,
                        15,
                        18
                )
        );

        VBox headingBox =
                new VBox(3);

        Label title =
                new Label(
                        "Clients Needing Attention"
                );

        title.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "Clients are prioritized by current weekly plan expiry"
                );

        subtitle.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 10px;"
        );

        headingBox.getChildren().addAll(
                title,
                subtitle
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Button viewAll =
                new Button(
                        "View All"
                );

        styleTextButton(
                viewAll
        );

        header.getChildren().addAll(
                headingBox,
                spacer,
                viewAll
        );

        card.getChildren().add(
                header
        );

        HBox tableHeader =
                createClientTableHeader();

        card.getChildren().add(
                tableHeader
        );

        List<ClientDashboardData> sorted =
                new ArrayList<>(
                        clients
                );

        sorted.sort(
                Comparator.comparing(
                        client -> {

                            LocalDate expiry =
                                    client.getPlanExpiry();

                            if (expiry == null) {

                                return LocalDate.MAX;
                            }

                            return expiry;
                        }
                )
        );

        if (sorted.isEmpty()) {

            VBox emptyBox =
                    new VBox(8);

            emptyBox.setAlignment(
                    Pos.CENTER
            );

            emptyBox.setPadding(
                    new Insets(40)
            );

            Label emptyIcon =
                    new Label("♙");

            emptyIcon.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 25px;"
            );

            Label emptyLabel =
                    new Label(
                            "No subscribed clients found."
                    );

            emptyLabel.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 12px;"
            );

            emptyBox.getChildren().addAll(
                    emptyIcon,
                    emptyLabel
            );

            card.getChildren().add(
                    emptyBox
            );

        } else {

            for (
                    ClientDashboardData client :
                    sorted
            ) {

                card.getChildren().add(
                        createClientAttentionRow(
                                client
                        )
                );
            }
        }

        addHoverAnimation(
                card,
                1.007
        );

        return card;
    }

    // =========================================================
    // CLIENT TABLE HEADER
    // =========================================================

    private HBox createClientTableHeader() {

        HBox row =
                new HBox();

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        10,
                        14,
                        10,
                        14
                )
        );

        row.setStyle(
                "-fx-background-color: #172033;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1 0 1 0;"
        );

        addHeaderCell(
                row,
                "CLIENT",
                170
        );

        addHeaderCell(
                row,
                "GOAL",
                135
        );

        addHeaderCell(
                row,
                "CURRENT PLAN",
                145
        );

        addHeaderCell(
                row,
                "PLAN EXPIRY",
                115
        );

        addHeaderCell(
                row,
                "PRIORITY",
                100
        );

        return row;
    }

    // =========================================================
    // CLIENT ROW
    // =========================================================

    private HBox createClientAttentionRow(
            ClientDashboardData client
    ) {

        HBox row =
                new HBox();

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(
                        13,
                        14,
                        13,
                        14
                )
        );

        row.setStyle(
                "-fx-border-color: #293548;" +
                "-fx-border-width: 0 0 1 0;"
        );

        HBox clientBox =
                new HBox(9);

        clientBox.setAlignment(
                Pos.CENTER_LEFT
        );

        Label avatar =
                new Label(
                        createInitials(
                                client.getFamilyName()
                        )
                );

        avatar.setMinSize(
                36,
                36
        );

        avatar.setMaxSize(
                36,
                36
        );

        avatar.setAlignment(
                Pos.CENTER
        );

        avatar.setStyle(
                "-fx-background-color: #233B60;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #93C5FD;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;"
        );

        VBox clientText =
                new VBox(2);

        Label familyName =
                new Label(
                        client.getFamilyName()
                );

        familyName.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Label members =
                new Label(
                        client.getMemberCount()
                                + (
                                client.getMemberCount() == 1
                                        ? " Member"
                                        : " Members"
                        )
                );

        members.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 9px;"
        );

        clientText.getChildren().addAll(
                familyName,
                members
        );

        clientBox.getChildren().addAll(
                avatar,
                clientText
        );

        clientBox.setPrefWidth(170);
        clientBox.setMinWidth(170);

        row.getChildren().add(
                clientBox
        );

        addNormalCell(
                row,
                safeText(client.getGoal()),
                135
        );

        addNormalCell(
                row,
                safeText(client.getCurrentPlan()),
                145
        );

        String expiryText;

        if (
                client.getPlanExpiry() == null
        ) {

            expiryText =
                    "Not Set";

        } else {

            expiryText =
                    client.getPlanExpiry()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "dd MMM yyyy"
                                            )
                            );
        }

        addNormalCell(
                row,
                expiryText,
                115
        );

        Label priority =
                createPriorityLabel(
                        client.getPlanExpiry()
                );

        VBox priorityBox =
                new VBox(
                        priority
                );

        priorityBox.setAlignment(
                Pos.CENTER_LEFT
        );

        priorityBox.setPrefWidth(100);
        priorityBox.setMinWidth(100);

        row.getChildren().add(
                priorityBox
        );

        addHoverAnimation(
                row,
                1.012
        );

        return row;
    }

    // =========================================================
    // PRIORITY
    // =========================================================

    private Label createPriorityLabel(
            LocalDate expiry
    ) {

        Label label =
                new Label();

        if (
                expiry == null
        ) {

            label.setText(
                    "No Plan"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#334155",
                            MUTED
                    )
            );

            return label;
        }

        long daysRemaining =
                ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        expiry
                );

        if (
                daysRemaining < 0
        ) {

            label.setText(
                    "Expired"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#7F1D1D",
                            "#FCA5A5"
                    )
            );

        } else if (
                daysRemaining == 0
        ) {

            label.setText(
                    "Today"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#7F1D1D",
                            "#FCA5A5"
                    )
            );

        } else if (
                daysRemaining <= 3
        ) {

            label.setText(
                    daysRemaining + " Days"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#7F1D1D",
                            "#FCA5A5"
                    )
            );

        } else if (
                daysRemaining <= 7
        ) {

            label.setText(
                    daysRemaining + " Days"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#78350F",
                            "#FBBF24"
                    )
            );

        } else {

            label.setText(
                    daysRemaining + " Days"
            );

            label.setStyle(
                    createPriorityStyle(
                            "#163B2A",
                            "#4ADE80"
                    )
            );
        }

        return label;
    }

    private String createPriorityStyle(
            String background,
            String foreground
    ) {

        return
                "-fx-background-color: " + background + ";" +
                "-fx-text-fill: " + foreground + ";" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 5 9 5 9;" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;";
    }

    // =========================================================
    // CONSULTATIONS CARD
    // =========================================================

    private VBox createConsultationsCard(
            List<ConsultationDashboardData>
                    consultations
    ) {

        VBox card =
                new VBox();

        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 11;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 11;"
        );

        HBox header =
                new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        18,
                        16,
                        15,
                        16
                )
        );

        VBox heading =
                new VBox(3);

        Label title =
                new Label(
                        "Today's Consultations"
                );

        title.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );

        Label currentDate =
                new Label(
                        LocalDate.now()
                                .format(
                                        DateTimeFormatter
                                                .ofPattern(
                                                        "EEEE, dd MMMM"
                                                )
                                )
                );

        currentDate.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 9px;"
        );

        heading.getChildren().addAll(
                title,
                currentDate
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        Label count =
                new Label(
                        String.valueOf(
                                consultations.size()
                        )
                );

        count.setMinSize(
                29,
                29
        );

        count.setAlignment(
                Pos.CENTER
        );

        count.setStyle(
                "-fx-background-color: #19375E;" +
                "-fx-background-radius: 20;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        header.getChildren().addAll(
                heading,
                spacer,
                count
        );

        VBox consultationList =
                new VBox(10);

        consultationList.setPadding(
                new Insets(
                        5,
                        12,
                        15,
                        12
                )
        );

        if (
                consultations.isEmpty()
        ) {

            VBox empty =
                    new VBox(8);

            empty.setAlignment(
                    Pos.CENTER
            );

            empty.setPadding(
                    new Insets(
                            40,
                            10,
                            40,
                            10
                    )
            );

            Label icon =
                    new Label("▣");

            icon.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 26px;"
            );

            Label emptyText =
                    new Label(
                            "No consultations booked today"
                    );

            emptyText.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 11px;"
            );

            empty.getChildren().addAll(
                    icon,
                    emptyText
            );

            consultationList
                    .getChildren()
                    .add(
                            empty
                    );

        } else {

            consultations.sort(
                    Comparator.comparing(
                            consultation ->
                                    consultation.getTime()
                                            == null
                                            ? LocalTime.MAX
                                            : consultation.getTime()
                    )
            );

            for (
                    ConsultationDashboardData data :
                    consultations
            ) {

                consultationList
                        .getChildren()
                        .add(
                                createConsultationItem(
                                        data
                                )
                        );
            }
        }

        // =====================================================
        // VIEW CONSULTATION CALENDAR
        // =====================================================

        Button calendar =
                new Button(
                        "View Consultation Calendar"
                );

        calendar.setMaxWidth(
                Double.MAX_VALUE
        );

        calendar.setOnAction(
                event ->
                        showConsultationCalendarDialog()
        );

        String normalStyle =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + BLUE + ";" +
                "-fx-font-size: 11px;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1 0 0 0;" +
                "-fx-padding: 13;";

        String hoverStyle =
                "-fx-background-color: #25364D;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-font-size: 11px;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1 0 0 0;" +
                "-fx-padding: 13;" +
                "-fx-cursor: hand;";

        calendar.setStyle(
                normalStyle
        );

        calendar.setOnMouseEntered(
                event ->
                        calendar.setStyle(
                                hoverStyle
                        )
        );

        calendar.setOnMouseExited(
                event ->
                        calendar.setStyle(
                                normalStyle
                        )
        );

        card.getChildren().addAll(
                header,
                consultationList,
                calendar
        );

        addHoverAnimation(
                card,
                1.009
        );

        return card;
    }

    // =========================================================
    // CONSULTATION ITEM
    // =========================================================

    private HBox createConsultationItem(
            ConsultationDashboardData data
    ) {

        HBox box =
                new HBox(12);

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        box.setPadding(
                new Insets(13)
        );

        box.setStyle(
                "-fx-background-color: " + CARD_LIGHT + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;"
        );

        VBox timeBox =
                new VBox(1);

        timeBox.setPrefWidth(60);
        timeBox.setMinWidth(60);

        timeBox.setAlignment(
                Pos.CENTER
        );

        String timeText =
                "--:--";

        String amPm =
                "";

        if (
                data.getTime() != null
        ) {

            timeText =
                    data.getTime()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "hh:mm"
                                            )
                            );

            amPm =
                    data.getTime()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "a"
                                            )
                            );
        }

        Label time =
                new Label(
                        timeText
                );

        time.setStyle(
                "-fx-text-fill: #93C5FD;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        Label period =
                new Label(
                        amPm
                );

        period.setStyle(
                "-fx-text-fill: " + BLUE + ";" +
                "-fx-font-size: 9px;"
        );

        timeBox.getChildren().addAll(
                time,
                period
        );

        Region line =
                new Region();

        line.setPrefWidth(2);
        line.setMinWidth(2);
        line.setPrefHeight(42);

        line.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                "-fx-background-radius: 2;"
        );

        VBox details =
                new VBox(4);

        Label clientName =
                new Label(
                        safeText(
                                data.getClientName()
                        )
                );

        clientName.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );

        Label type =
                new Label(
                        getConsultationIcon(
                                data.getType()
                        )
                                + " "
                                + safeText(
                                        data.getType()
                                )
                );

        type.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 9px;"
        );

        Label status = new Label(
                formatConsultationStatus(data.getStatus())
        );

        status.setStyle(
                getConsultationStatusStyle(data.getStatus())
        );

        details.getChildren().addAll(
                clientName,
                type,
                status
        );
        

        box.getChildren().addAll(
                timeBox,
                line,
                details
        );

        addHoverAnimation(
                box,
                1.03
        );

        return box;
    }

    private String formatConsultationStatus(String status) {

        if (status == null || status.isBlank()) {
                return "Unknown";
        }

        String cleaned = status.trim().toLowerCase();

        return Character.toUpperCase(cleaned.charAt(0)) +
                cleaned.substring(1);
        }

        private String getConsultationStatusStyle(String status) {

        String color = MUTED;

        if ("completed".equalsIgnoreCase(status)) {
                color = GREEN;
        } else if ("confirmed".equalsIgnoreCase(status) ||
                "accepted".equalsIgnoreCase(status)) {
                color = "#60A5FA";
        } else if ("requested".equalsIgnoreCase(status)) {
                color = ORANGE;
        } else if ("cancelled".equalsIgnoreCase(status) ||
                "rejected".equalsIgnoreCase(status)) {
                color = "#F87171";
        }

        return "-fx-text-fill: " + color + ";" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;";
    }

    // =========================================================
    // CONSULTATION CALENDAR DIALOG
    // =========================================================

    private void showConsultationCalendarDialog() {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();

        dialog.setTitle(
                "Consultation Calendar"
        );

        dialog.setHeaderText(
                null
        );

        DialogPane dialogPane =
                dialog.getDialogPane();

        dialogPane.setPrefWidth(
                830
        );

        dialogPane.setPrefHeight(
                650
        );

        dialogPane.setStyle(
                "-fx-background-color: " + BG + ";"
        );

        // =====================================================
        // CURRENT DISPLAYED MONTH
        // =====================================================

        final YearMonth[] displayedMonth = {
                YearMonth.now()
        };

        Label monthLabel =
                new Label();

        monthLabel.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle =
                new Label(
                        "Consultation bookings for each day"
                );

        subtitle.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 12px;"
        );

        // =====================================================
        // PREVIOUS MONTH
        // =====================================================

        Button previousMonth =
                new Button(
                        "‹"
                );

        previousMonth.setPrefSize(
                38,
                38
        );

        previousMonth.setStyle(
                "-fx-background-color: #121D35;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        // =====================================================
        // NEXT MONTH
        // =====================================================

        Button nextMonth =
                new Button(
                        "›"
                );

        nextMonth.setPrefSize(
                38,
                38
        );

        nextMonth.setStyle(
                "-fx-background-color: #121D35;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        VBox monthHeading =
                new VBox(
                        4,
                        monthLabel,
                        subtitle
                );

        HBox monthHeader =
                new HBox(
                        10,
                        monthHeading,
                        headerSpacer,
                        previousMonth,
                        nextMonth
                );

        monthHeader.setAlignment(
                Pos.CENTER_LEFT
        );

        // =====================================================
        // CALENDAR GRID HOLDER
        // =====================================================

        VBox calendarHolder =
                new VBox();

        calendarHolder.setFillWidth(
                true
        );

        // =====================================================
        // UPDATE CALENDAR
        // =====================================================

        Runnable updateCalendar =
                () -> {

                    YearMonth month =
                            displayedMonth[0];

                    monthLabel.setText(
                            month.format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "MMMM yyyy"
                                            )
                            )
                    );

                    GridPane calendarGrid =
                            createConsultationCalendarGrid(
                                    month
                            );

                    calendarHolder
                            .getChildren()
                            .setAll(
                                    calendarGrid
                            );
                };

        // =====================================================
        // PREVIOUS ACTION
        // =====================================================

        previousMonth.setOnAction(
                event -> {

                    displayedMonth[0] =
                            displayedMonth[0]
                                    .minusMonths(1);

                    updateCalendar.run();
                }
        );

        // =====================================================
        // NEXT ACTION
        // =====================================================

        nextMonth.setOnAction(
                event -> {

                    displayedMonth[0] =
                            displayedMonth[0]
                                    .plusMonths(1);

                    updateCalendar.run();
                }
        );

        updateCalendar.run();

        VBox content =
                new VBox(
                        18,
                        monthHeader,
                        calendarHolder
                );

        content.setPadding(
                new Insets(20)
        );

        dialogPane.setContent(
                content
        );

        // =====================================================
        // CLOSE BUTTON
        // =====================================================

        ButtonType closeButtonType =
                new ButtonType(
                        "Close",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialogPane
                .getButtonTypes()
                .add(
                        closeButtonType
                );

        Button closeButton =
                (Button)
                        dialogPane.lookupButton(
                                closeButtonType
                        );

        closeButton.setStyle(
                "-fx-background-color: " + BLUE + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 8 22 8 22;"
        );

        dialog.showAndWait();
    }

    // =========================================================
    // CALENDAR GRID
    // =========================================================

    private GridPane createConsultationCalendarGrid(
            YearMonth month
    ) {

        GridPane grid =
                new GridPane();

        grid.setHgap(
                8
        );

        grid.setVgap(
                8
        );

        grid.setPadding(
                new Insets(
                        5,
                        0,
                        5,
                        0
                )
        );

        // =====================================================
        // DAY HEADERS
        // =====================================================

        String[] dayNames = {
                "MON",
                "TUE",
                "WED",
                "THU",
                "FRI",
                "SAT",
                "SUN"
        };

        for (
                int column = 0;
                column < 7;
                column++
        ) {

            Label dayHeader =
                    new Label(
                            dayNames[column]
                    );

            dayHeader.setPrefWidth(
                    100
            );

            dayHeader.setAlignment(
                    Pos.CENTER
            );

            dayHeader.setStyle(
                    "-fx-text-fill: " + MUTED + ";" +
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;"
            );

            grid.add(
                    dayHeader,
                    column,
                    0
            );
        }

        // =====================================================
        // CONSULTATION COUNTS
        // =====================================================

        Map<LocalDate, Integer> consultationCounts =
                loadConsultationCountForMonth(
                        month
                );

        LocalDate firstDay =
                month.atDay(1);

        int startColumn =
                firstDay
                        .getDayOfWeek()
                        .getValue()
                        - 1;

        int column =
                startColumn;

        int row =
                1;

        for (
                int day = 1;
                day <= month.lengthOfMonth();
                day++
        ) {

            LocalDate date =
                    month.atDay(
                            day
                    );

            int count =
                    consultationCounts
                            .getOrDefault(
                                    date,
                                    0
                            );

            VBox dayCard =
                    createCalendarDayCard(
                            date,
                            count
                    );

            grid.add(
                    dayCard,
                    column,
                    row
            );

            column++;

            if (
                    column == 7
            ) {

                column = 0;
                row++;
            }
        }

        return grid;
    }

    // =========================================================
    // DAY CARD
    // =========================================================

    private VBox createCalendarDayCard(
            LocalDate date,
            int consultationCount
    ) {

        VBox card =
                new VBox(8);

        card.setPrefSize(
                100,
                82
        );

        card.setMinSize(
                100,
                82
        );

        card.setPadding(
                new Insets(9)
        );

        // =====================================================
        // DATE
        // =====================================================

        Label dateLabel =
                new Label(
                        String.valueOf(
                                date.getDayOfMonth()
                        )
                );

        dateLabel.setStyle(
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        card.getChildren().add(
                dateLabel
        );

        // =====================================================
        // CONSULTATION COUNT
        // =====================================================

        if (
                consultationCount > 0
        ) {

            String consultationText =
                    consultationCount == 1
                            ? "1 Consultation"
                            : consultationCount
                            + " Consultations";

            Label countLabel =
                    new Label(
                            consultationText
                    );

            countLabel.setWrapText(
                    true
            );

            countLabel.setStyle(
                    "-fx-text-fill: #60A5FA;" +
                    "-fx-font-size: 9px;" +
                    "-fx-font-weight: bold;"
            );

            card.getChildren().add(
                    countLabel
            );
        }

        // =====================================================
        // TODAY
        // =====================================================

        if (
                date.equals(
                        LocalDate.now()
                )
        ) {

            card.setStyle(
                    "-fx-background-color: #19375E;" +
                    "-fx-border-color: " + BLUE + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;"
            );

        } else if (
                consultationCount > 0
        ) {

            card.setStyle(
                    "-fx-background-color: #1E293B;" +
                    "-fx-border-color: #3B82F6;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;"
            );

        } else {

            card.setStyle(
                    "-fx-background-color: #121D35;" +
                    "-fx-border-color: #1F2937;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;"
            );
        }

        addHoverAnimation(
                card,
                1.05
        );

        if (consultationCount > 0) {

                card.setStyle(
                        card.getStyle() +
                        "-fx-cursor: hand;"
                );

                card.setOnMouseClicked(event ->
                        showConsultationsForDate(date)
                );
        }

        return card;
    }

        private void showConsultationsForDate(LocalDate selectedDate) {

                List<ConsultationDashboardData> consultations =
                        getConsultationsForMonth(
                                YearMonth.from(selectedDate)
                        );

                consultations.removeIf(data ->
                        data.getDate() == null ||
                        !selectedDate.equals(data.getDate())
                );

                SameStageDialog<Void> dialog =
                        new SameStageDialog<>();

                dialog.setTitle("Consultations");
                dialog.setHeaderText(null);

                DialogPane dialogPane =
                        dialog.getDialogPane();

                dialogPane.setPrefWidth(520);
                dialogPane.setPrefHeight(520);

                VBox content = new VBox(14);
                content.setPadding(new Insets(22));
                content.setStyle(
                        "-fx-background-color: " + BG + ";"
                );

                Label title = new Label(
                        "Consultations — " +
                        selectedDate.format(
                                DateTimeFormatter.ofPattern(
                                        "dd MMMM yyyy"
                                )
                        )
                );

                title.setStyle(
                        "-fx-text-fill: " + WHITE + ";" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;"
                );

                VBox consultationList = new VBox(10);

                if (consultations.isEmpty()) {

                        Label empty = new Label(
                                "No consultations on this date."
                        );

                        empty.setStyle(
                                "-fx-text-fill: " + MUTED + ";" +
                                "-fx-font-size: 13px;"
                        );

                        consultationList.getChildren().add(empty);

                } else {

                        consultations.sort(
                                Comparator.comparing(
                                        ConsultationDashboardData::getTime,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()
                                        )
                                )
                        );

                        for (ConsultationDashboardData consultation :
                                consultations) {

                        consultationList.getChildren().add(
                                createConsultationItem(
                                        consultation
                                )
                        );
                        }
                }

                ScrollPane scroll = new ScrollPane(
                        consultationList
                );

                scroll.setFitToWidth(true);
                scroll.setStyle(
                        "-fx-background: " + BG + ";" +
                        "-fx-background-color: " + BG + ";" +
                        "-fx-border-color: transparent;"
                );

                VBox.setVgrow(scroll, Priority.ALWAYS);

                content.getChildren().addAll(
                        title,
                        scroll
                );

                dialogPane.setContent(content);

                ButtonType closeType =
                        new ButtonType(
                                "Close",
                                ButtonBar.ButtonData.CANCEL_CLOSE
                        );

                dialogPane.getButtonTypes().add(closeType);

                Button closeButton =
                        (Button) dialogPane.lookupButton(closeType);

                closeButton.setStyle(
                        "-fx-background-color: " + BLUE + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 7;"
                );

                dialog.showAndWait();
        }

    // =========================================================
    // LOAD CONSULTATION COUNT
    // =========================================================

    private Map<LocalDate, Integer>
            loadConsultationCountForMonth(
                    YearMonth month
            ) {

        Map<LocalDate, Integer> counts =
                new HashMap<>();

        List<ConsultationDashboardData> consultations =
                getConsultationsForMonth(
                        month
                );

        for (
                ConsultationDashboardData consultation :
                consultations
        ) {

            LocalDate date =
                    consultation.getDate();

            if (
                    date == null
            ) {

                continue;
            }

            counts.put(
                    date,
                    counts.getOrDefault(
                            date,
                            0
                    ) + 1
            );
        }

        return counts;
    }

    // =========================================================
    // HEADER CELL
    // =========================================================

    private void addHeaderCell(
            HBox row,
            String text,
            double width
    ) {

        Label label =
                new Label(
                        text
                );

        label.setStyle(
                "-fx-text-fill: " + MUTED + ";" +
                "-fx-font-size: 9px;" +
                "-fx-font-weight: bold;"
        );

        VBox box =
                new VBox(
                        label
                );

        box.setPrefWidth(
                width
        );

        box.setMinWidth(
                width
        );

        row.getChildren().add(
                box
        );
    }

    // =========================================================
    // NORMAL CELL
    // =========================================================

    private void addNormalCell(
            HBox row,
            String text,
            double width
    ) {

        Label label =
                new Label(
                        text
                );

        label.setWrapText(
                true
        );

        label.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 10px;"
        );

        VBox box =
                new VBox(
                        label
                );

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        box.setPrefWidth(
                width
        );

        box.setMinWidth(
                width
        );

        row.getChildren().add(
                box
        );
    }

    // =========================================================
    // TEXT BUTTON
    // =========================================================

    private void styleTextButton(
            Button button
    ) {

        String normal =
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + BLUE + ";" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 7 10 7 10;";

        String hover =
                "-fx-background-color: #25364D;" +
                "-fx-text-fill: #60A5FA;" +
                "-fx-background-radius: 6;" +
                "-fx-font-size: 10px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 7 10 7 10;" +
                "-fx-cursor: hand;";

        button.setStyle(
                normal
        );

        button.setOnMouseEntered(
                event ->
                        button.setStyle(
                                hover
                        )
        );

        button.setOnMouseExited(
                event ->
                        button.setStyle(
                                normal
                        )
        );
    }

    // =========================================================
    // HOVER ANIMATION
    // =========================================================

    private void addHoverAnimation(
            Node node,
            double scale
    ) {

        node.setOnMouseEntered(
                event -> {

                    ScaleTransition transition =
                            new ScaleTransition(
                                    Duration.millis(150),
                                    node
                            );

                    transition.setToX(
                            scale
                    );

                    transition.setToY(
                            scale
                    );

                    transition.play();
                }
        );

        node.setOnMouseExited(
                event -> {

                    ScaleTransition transition =
                            new ScaleTransition(
                                    Duration.millis(150),
                                    node
                            );

                    transition.setToX(
                            1
                    );

                    transition.setToY(
                            1
                    );

                    transition.play();
                }
        );
    }

    // =========================================================
    // INITIALS
    // =========================================================

    private String createInitials(
            String name
    ) {

        if (
                name == null ||
                name.trim().isEmpty()
        ) {

            return "?";
        }

        String cleanName =
                name.replace(
                        "Family",
                        ""
                ).trim();

        String[] words =
                cleanName.split(
                        "\\s+"
                );

        if (
                words.length == 1
        ) {

            String first =
                    words[0];

            return first.substring(
                    0,
                    Math.min(
                            2,
                            first.length()
                    )
            ).toUpperCase();
        }

        return (
                words[0]
                        .substring(
                                0,
                                1
                        )
                        +
                        words[
                                words.length - 1
                        ].substring(
                                0,
                                1
                        )
        ).toUpperCase();
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(
            String text
    ) {

        if (
                text == null ||
                text.trim().isEmpty()
        ) {

            return "—";
        }

        return text;
    }

    // =========================================================
    // CONSULTATION ICON
    // =========================================================

    private String getConsultationIcon(
            String type
    ) {

        if (
                type == null
        ) {

            return "▣";
        }

        String lower =
                type.toLowerCase();

        if (
                lower.contains("video") ||
                lower.contains("online")
        ) {

            return "▣";
        }

        if (
                lower.contains("clinic") ||
                lower.contains("offline") ||
                lower.contains("physical")
        ) {

            return "◉";
        }

        if (
                lower.contains("phone") ||
                lower.contains("call")
        ) {

            return "☎";
        }

        return "▣";
    }

    // =========================================================
    // CLIENT DATA CLASS
    // =========================================================

    private static class ClientDashboardData {

        private final String userId;
        private final String familyName;
        private final int memberCount;
        private final String goal;
        private final String currentPlan;
        private final LocalDate planExpiry;
        private final int adherence;

        public ClientDashboardData(
                String userId,
                String familyName,
                int memberCount,
                String goal,
                String currentPlan,
                LocalDate planExpiry,
                int adherence
        ) {

            this.userId =
                    userId;

            this.familyName =
                    familyName;

            this.memberCount =
                    memberCount;

            this.goal =
                    goal;

            this.currentPlan =
                    currentPlan;

            this.planExpiry =
                    planExpiry;

            this.adherence =
                    adherence;
        }

        public String getUserId() {

            return userId;
        }

        public String getFamilyName() {

            return familyName;
        }

        public int getMemberCount() {

            return memberCount;
        }

        public String getGoal() {

            return goal;
        }

        public String getCurrentPlan() {

            return currentPlan;
        }

        public LocalDate getPlanExpiry() {

            return planExpiry;
        }

        public int getAdherence() {

            return adherence;
        }
    }

    // =========================================================
    // CONSULTATION DATA CLASS
    // =========================================================

        private static class ConsultationDashboardData {

                private final String clientName;
                private final LocalDate date;
                private final LocalTime time;
                private final String type;
                private final String status;

                public ConsultationDashboardData(
                        String clientName,
                        LocalDate date,
                        LocalTime time,
                        String type,
                        String status
                ) {
                        this.clientName = clientName;
                        this.date = date;
                        this.time = time;
                        this.type = type;
                        this.status = status;
                }

                public String getClientName() {
                        return clientName;
                }

                public LocalDate getDate() {
                        return date;
                }

                public LocalTime getTime() {
                        return time;
                }

                public String getType() {
                        return type;
                }

                public String getStatus() {
                        return status;
                }
        }
}