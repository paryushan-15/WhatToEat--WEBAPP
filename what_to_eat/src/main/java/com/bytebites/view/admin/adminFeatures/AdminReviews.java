package com.bytebites.view.admin.adminFeatures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.google.cloud.Timestamp;

public class AdminReviews {

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
    // DAO
    // =====================================================

    private final DietitianDao dietitianDao;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;

    private final VBox reviewListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<AdminReviewItem> allReviews;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> ratingFilter;

    private ComboBox<String> moderationFilter;


    // =====================================================
    // STATISTICS
    // =====================================================

    private Label totalReviewsValue;

    private Label visibleReviewsValue;

    private Label hiddenReviewsValue;

    private Label averageRatingValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminReviews() {

        dietitianDao =
                new DietitianDao();


        root =
                new BorderPane();


        reviewListBox =
                new VBox(15);


        allReviews =
                new ArrayList<>();


        buildPage();

        loadReviews();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getReviewsPage() {

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
                        reviewListBox
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
                        "Ratings & Reviews"
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
                        "Monitor and moderate all User reviews submitted to Dietitians."
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
                        loadReviews()
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


        totalReviewsValue =
                new Label("0");

        visibleReviewsValue =
                new Label("0");

        hiddenReviewsValue =
                new Label("0");

        averageRatingValue =
                new Label("0.0 / 5");


        VBox total =
                createStatCard(
                        "Total Reviews",
                        totalReviewsValue,
                        PRIMARY_BLUE
                );


        VBox visible =
                createStatCard(
                        "Visible",
                        visibleReviewsValue,
                        GREEN
                );


        VBox hidden =
                createStatCard(
                        "Hidden",
                        hiddenReviewsValue,
                        RED
                );


        VBox average =
                createStatCard(
                        "Average Rating",
                        averageRatingValue,
                        AMBER
                );


        VBox[] cards = {
                total,
                visible,
                hidden,
                average
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
                        18
                )
        );


        card.setMinHeight(
                100
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
                "Search User, Dietitian, review text or UID..."
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
        // RATING FILTER
        // =================================================

        ratingFilter =
                new ComboBox<>();


        ratingFilter.getItems()
                .addAll(
                        "All Ratings",
                        "5 Stars",
                        "4 Stars",
                        "3 Stars",
                        "2 Stars",
                        "1 Star"
                );


        ratingFilter.setValue(
                "All Ratings"
        );


        ratingFilter.setPrefWidth(
                150
        );


        ratingFilter.setPrefHeight(
                42
        );


        // =================================================
        // MODERATION FILTER
        // =================================================

        moderationFilter =
                new ComboBox<>();


        moderationFilter.getItems()
                .addAll(
                        "All Reviews",
                        "Visible",
                        "Hidden"
                );


        moderationFilter.setValue(
                "All Reviews"
        );


        moderationFilter.setPrefWidth(
                155
        );


        moderationFilter.setPrefHeight(
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


        ratingFilter.setStyle(
                comboStyle
        );


        moderationFilter.setStyle(
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


        ratingFilter.setOnAction(
                event ->
                        applyFilters()
        );


        moderationFilter.setOnAction(
                event ->
                        applyFilters()
        );


        filters.getChildren()
                .addAll(
                        searchField,
                        ratingFilter,
                        moderationFilter
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
                        "Loading reviews..."
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
    // LOAD ALL REVIEWS
    // =====================================================

    private void loadReviews() {

        setLoading(
                true,
                "Loading reviews..."
        );


        Task<List<AdminReviewItem>> task =
                new Task<>() {

                    @Override
                    protected List<AdminReviewItem> call() {

                        List<AdminReviewItem> result =
                                new ArrayList<>();


                        List<Dietitian> dietitians =
                                dietitianDao
                                        .getAllDietitians();


                        if (
                                dietitians == null
                        ) {

                            return result;
                        }


                        for (
                                Dietitian dietitian :
                                dietitians
                        ) {

                            if (
                                    dietitian == null
                                            ||
                                    dietitian.getUid()
                                            == null
                            ) {

                                continue;
                            }


                            List<Map<String, Object>> reviews =
                                    dietitianDao
                                            .getReviews(
                                                    dietitian.getUid()
                                            );


                            if (
                                    reviews == null
                            ) {

                                continue;
                            }


                            for (
                                    Map<String, Object> review :
                                    reviews
                            ) {

                                if (
                                        review == null
                                ) {

                                    continue;
                                }


                                result.add(
                                        new AdminReviewItem(
                                                dietitian.getUid(),
                                                dietitian.getName(),
                                                review
                                        )
                                );
                            }
                        }


                        return result;
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<AdminReviewItem> loaded =
                            task.getValue();


                    if (
                            loaded == null
                    ) {

                        loaded =
                                new ArrayList<>();
                    }


                    allReviews =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allReviews.size()
                                    + " review(s) loaded."
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
                            "Unable to load reviews."
                    );


                    showError(
                            "Unable to load ratings and reviews from Firestore."
                    );
                }
        );


        startTask(
                task
        );
    }


    // =====================================================
    // APPLY FILTERS
    // =====================================================

    private void applyFilters() {

        if (
                searchField == null
                        ||
                ratingFilter == null
                        ||
                moderationFilter == null
        ) {

            return;
        }


        String query =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String selectedRating =
                ratingFilter
                        .getValue();


        String selectedModeration =
                moderationFilter
                        .getValue();


        List<AdminReviewItem> filtered =
                new ArrayList<>();


        for (
                AdminReviewItem item :
                allReviews
        ) {

            if (
                    item == null
            ) {

                continue;
            }


            // =================================================
            // SEARCH
            // =================================================

            boolean matchesSearch =
                    query.isEmpty()

                            ||

                    contains(
                            item.dietitianName,
                            query
                    )

                            ||

                    contains(
                            item.dietitianUid,
                            query
                    )

                            ||

                    contains(
                            item.getUserName(),
                            query
                    )

                            ||

                    contains(
                            item.getUserUid(),
                            query
                    )

                            ||

                    contains(
                            item.getReviewText(),
                            query
                    );


            if (
                    !matchesSearch
            ) {

                continue;
            }


            // =================================================
            // RATING
            // =================================================

            boolean matchesRating =
                    selectedRating == null
                            ||
                    selectedRating.equalsIgnoreCase(
                            "All Ratings"
                    );


            if (
                    !matchesRating
            ) {

                int expected =
                        ratingFromFilter(
                                selectedRating
                        );


                matchesRating =
                        item.getRating()
                                == expected;
            }


            if (
                    !matchesRating
            ) {

                continue;
            }


            // =================================================
            // MODERATION
            // =================================================

            String moderationStatus =
                    item.getModerationStatus();


            boolean matchesModeration =
                    selectedModeration == null
                            ||
                    selectedModeration.equalsIgnoreCase(
                            "All Reviews"
                    )

                            ||

                    (
                            selectedModeration.equalsIgnoreCase(
                                    "Visible"
                            )
                                    &&
                            !moderationStatus.equalsIgnoreCase(
                                    "HIDDEN"
                            )
                    )

                            ||

                    (
                            selectedModeration.equalsIgnoreCase(
                                    "Hidden"
                            )
                                    &&
                            moderationStatus.equalsIgnoreCase(
                                    "HIDDEN"
                            )
                    );


            if (
                    matchesModeration
            ) {

                filtered.add(
                        item
                );
            }
        }


        displayReviews(
                filtered
        );
    }


    // =====================================================
    // DISPLAY
    // =====================================================

    private void displayReviews(
            List<AdminReviewItem> reviews
    ) {

        reviewListBox
                .getChildren()
                .clear();


        if (
                reviews == null
                        ||
                reviews.isEmpty()
        ) {

            reviewListBox
                    .getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                AdminReviewItem review :
                reviews
        ) {

            reviewListBox
                    .getChildren()
                    .add(
                            createReviewCard(
                                    review
                            )
                    );
        }
    }


    // =====================================================
    // REVIEW CARD
    // =====================================================

    private VBox createReviewCard(
            AdminReviewItem item
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
                new HBox(14);


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox identity =
                new VBox(5);


        Label user =
                new Label(
                        value(
                                item.getUserName(),
                                "Unknown User"
                        )
                );


        user.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 17px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label dietitian =
                new Label(
                        "Review for "
                                +
                        value(
                                item.dietitianName,
                                "Unknown Dietitian"
                        )
                );


        dietitian.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        identity.getChildren()
                .addAll(
                        user,
                        dietitian
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label moderationBadge =
                createModerationBadge(
                        item.getModerationStatus()
                );


        header.getChildren()
                .addAll(
                        identity,
                        spacer,
                        moderationBadge
                );


        Separator divider =
                new Separator();


        // =================================================
        // RATING
        // =================================================

        HBox ratingRow =
                new HBox(10);


        ratingRow.setAlignment(
                Pos.CENTER_LEFT
        );


        Label stars =
                new Label(
                        buildStars(
                                item.getRating()
                        )
                );


        stars.setStyle(
                "-fx-text-fill: "
                        + AMBER
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label numeric =
                new Label(
                        item.getRating()
                                + " / 5"
                );


        numeric.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        Region ratingSpacer =
                new Region();


        HBox.setHgrow(
                ratingSpacer,
                Priority.ALWAYS
        );


        Label date =
                new Label(
                        formatFirestoreDateTime(
                                item.review.get(
                                        "createdAt"
                                )
                        )
                );


        date.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 11px;"
        );


        ratingRow.getChildren()
                .addAll(
                        stars,
                        numeric,
                        ratingSpacer,
                        date
                );


        // =================================================
        // REVIEW TEXT
        // =================================================

        VBox reviewBox =
                new VBox(5);


        Label reviewTitle =
                new Label(
                        "REVIEW"
                );


        reviewTitle.setStyle(
                "-fx-text-fill: #64748B;"
                        +
                "-fx-font-size: 10px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label reviewText =
                new Label(
                        value(
                                item.getReviewText(),
                                "No written review."
                        )
                );


        reviewText.setWrapText(
                true
        );


        reviewText.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 13px;"
        );


        reviewBox.getChildren()
                .addAll(
                        reviewTitle,
                        reviewText
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
                                item.getUserUid()
                        ),

                        createCompactInfo(
                                "DIETITIAN UID",
                                item.dietitianUid
                        ),

                        createCompactInfo(
                                "REVIEW ID",
                                item.getReviewId()
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
                                item
                        )
        );


        actions.getChildren()
                .add(
                        detailsButton
                );


        if (
                item.getModerationStatus()
                        .equalsIgnoreCase(
                                "HIDDEN"
                        )
        ) {

            Button restore =
                    createSuccessButton(
                            "Restore"
                    );


            restore.setOnAction(
                    event ->
                            restoreReview(
                                    item
                            )
            );


            actions.getChildren()
                    .add(
                            restore
                    );

        } else {

            Button hide =
                    createWarningButton(
                            "Hide"
                    );


            hide.setOnAction(
                    event ->
                            hideReview(
                                    item
                            )
            );


            actions.getChildren()
                    .add(
                            hide
                    );
        }


        Button delete =
                createDangerButton(
                        "Delete"
                );


        delete.setOnAction(
                event ->
                        deleteReview(
                                item
                        )
        );


        actions.getChildren()
                .add(
                        delete
                );


        card.getChildren()
                .addAll(
                        header,
                        divider,
                        ratingRow,
                        reviewBox,
                        ids,
                        actions
                );


        return card;
    }


    // =====================================================
    // HIDE REVIEW
    // =====================================================

    private void hideReview(
            AdminReviewItem item
    ) {

        if (
                !confirm(
                        "Hide Review",
                        "Hide this review?",
                        "The review will remain in Firestore but will be marked as HIDDEN."
                )
        ) {

            return;
        }


        executeAction(
                "Hiding review...",
                () ->
                        dietitianDao
                                .hideReview(
                                        item.dietitianUid,
                                        item.getReviewId()
                                ),
                "Review hidden successfully.",
                "Unable to hide review."
        );
    }


    // =====================================================
    // RESTORE REVIEW
    // =====================================================

    private void restoreReview(
            AdminReviewItem item
    ) {

        if (
                !confirm(
                        "Restore Review",
                        "Restore this review?",
                        "The review will become visible again."
                )
        ) {

            return;
        }


        executeAction(
                "Restoring review...",
                () ->
                        dietitianDao
                                .restoreReview(
                                        item.dietitianUid,
                                        item.getReviewId()
                                ),
                "Review restored successfully.",
                "Unable to restore review."
        );
    }


    // =====================================================
    // DELETE REVIEW
    // =====================================================

    private void deleteReview(
            AdminReviewItem item
    ) {

        if (
                !confirm(
                        "Delete Review",
                        "Permanently delete this review?",
                        "This removes the review document from Firestore and cannot be undone."
                )
        ) {

            return;
        }


        executeAction(
                "Deleting review...",
                () ->
                        dietitianDao
                                .deleteReview(
                                        item.dietitianUid,
                                        item.getReviewId()
                                ),
                "Review deleted successfully.",
                "Unable to delete review."
        );
    }


    // =====================================================
    // EXECUTE ACTION
    // =====================================================

    private void executeAction(
            String loadingText,
            ReviewAction action,
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


                        loadReviews();

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
    private interface ReviewAction {

        boolean execute();
    }


    // =====================================================
    // DETAILS
    // =====================================================

    private void showDetails(
            AdminReviewItem item
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Review Details"
        );


        dialog.setHeaderText(
                value(
                        item.getUserName(),
                        "User"
                )
                        +
                " → "
                        +
                value(
                        item.dietitianName,
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
                                "Review ID",
                                item.getReviewId()
                        ),

                        detailRow(
                                "User Name",
                                item.getUserName()
                        ),

                        detailRow(
                                "User UID",
                                item.getUserUid()
                        ),

                        detailRow(
                                "Dietitian",
                                item.dietitianName
                        ),

                        detailRow(
                                "Dietitian UID",
                                item.dietitianUid
                        ),

                        detailRow(
                                "Rating",
                                item.getRating()
                                        + " / 5"
                        ),

                        detailRow(
                                "Review",
                                item.getReviewText()
                        ),

                        detailRow(
                                "Moderation Status",
                                item.getModerationStatus()
                        ),

                        detailRow(
                                "Created At",
                                formatFirestoreDateTime(
                                        item.review.get(
                                                "createdAt"
                                        )
                                )
                        ),

                        detailRow(
                                "Moderated At",
                                formatFirestoreDateTime(
                                        item.review.get(
                                                "moderatedAt"
                                        )
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
    // UPDATE STATISTICS
    // =====================================================

    private void updateStatistics() {

        int total =
                allReviews.size();


        int visible =
                0;


        int hidden =
                0;


        int visibleRatingTotal =
                0;


        int visibleRatingCount =
                0;


        for (
                AdminReviewItem item :
                allReviews
        ) {

            if (
                    item == null
            ) {

                continue;
            }


            if (
                    item.getModerationStatus()
                            .equalsIgnoreCase(
                                    "HIDDEN"
                            )
            ) {

                hidden++;

            } else {

                visible++;


                int rating =
                        item.getRating();


                if (
                        rating >= 1
                                &&
                        rating <= 5
                ) {

                    visibleRatingTotal +=
                            rating;

                    visibleRatingCount++;
                }
            }
        }


        double average =
                visibleRatingCount == 0
                        ? 0
                        : (
                        (double)
                                visibleRatingTotal
                                /
                                visibleRatingCount
                );


        totalReviewsValue.setText(
                String.valueOf(
                        total
                )
        );


        visibleReviewsValue.setText(
                String.valueOf(
                        visible
                )
        );


        hiddenReviewsValue.setText(
                String.valueOf(
                        hidden
                )
        );


        averageRatingValue.setText(
                String.format(
                        "%.1f / 5",
                        average
                )
        );
    }


    // =====================================================
    // MODERATION BADGE
    // =====================================================

    private Label createModerationBadge(
            String moderationStatus
    ) {

        boolean hidden =
                moderationStatus
                        .equalsIgnoreCase(
                                "HIDDEN"
                        );


        Label badge =
                new Label(
                        hidden
                                ? "HIDDEN"
                                : "VISIBLE"
                );


        badge.setPadding(
                new Insets(
                        5,
                        11,
                        5,
                        11
                )
        );


        if (
                hidden
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
    // STAR DISPLAY
    // =====================================================

    private String buildStars(
            int rating
    ) {

        StringBuilder builder =
                new StringBuilder();


        for (
                int i = 1;
                i <= 5;
                i++
        ) {

            if (
                    i <= rating
            ) {

                builder.append(
                        "★"
                );

            } else {

                builder.append(
                        "☆"
                );
            }
        }


        return builder.toString();
    }


    // =====================================================
    // FILTER RATING
    // =====================================================

    private int ratingFromFilter(
            String filter
    ) {

        if (
                filter == null
        ) {

            return 0;
        }


        if (
                filter.startsWith(
                        "5"
                )
        ) {

            return 5;
        }


        if (
                filter.startsWith(
                        "4"
                )
        ) {

            return 4;
        }


        if (
                filter.startsWith(
                        "3"
                )
        ) {

            return 3;
        }


        if (
                filter.startsWith(
                        "2"
                )
        ) {

            return 2;
        }


        if (
                filter.startsWith(
                        "1"
                )
        ) {

            return 1;
        }


        return 0;
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
                        "No reviews found"
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
                        "Try changing the search, rating or moderation filter."
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


    private int intValue(
            Object object
    ) {

        if (
                object instanceof Number
        ) {

            return ((Number) object)
                    .intValue();
        }


        if (
                object != null
        ) {

            try {

                return Integer.parseInt(
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
    // FIRESTORE TIME
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


    private String formatFirestoreDateTime(
            Object object
    ) {

        long timestamp =
                timestampToMillis(
                        object
                );


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


    // =====================================================
    // ADMIN REVIEW ITEM
    // =====================================================

    private class AdminReviewItem {

        private final String dietitianUid;

        private final String dietitianName;

        private final Map<String, Object> review;


        private AdminReviewItem(
                String dietitianUid,
                String dietitianName,
                Map<String, Object> review
        ) {

            this.dietitianUid =
                    dietitianUid;


            this.dietitianName =
                    dietitianName;


            this.review =
                    review;
        }


        private String getReviewId() {

            return stringValue(
                    review.get(
                            "reviewId"
                    )
            );
        }


        private String getUserUid() {

            return stringValue(
                    review.get(
                            "userUid"
                    )
            );
        }


        private String getUserName() {

            return stringValue(
                    review.get(
                            "userName"
                    )
            );
        }


        private int getRating() {

            return intValue(
                    review.get(
                            "rating"
                    )
            );
        }


        private String getReviewText() {

            return stringValue(
                    review.get(
                            "review"
                    )
            );
        }


        private String getModerationStatus() {

            String status =
                    stringValue(
                            review.get(
                                    "moderationStatus"
                            )
                    );


            /*
             * Old reviews did not originally contain
             * moderationStatus, so they are treated
             * as visible.
             */

            if (
                    status.isBlank()
            ) {

                return "VISIBLE";
            }


            return status;
        }
    }
}