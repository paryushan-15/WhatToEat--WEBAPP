package com.bytebites.view.admin.adminFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.controller.AdminController;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminCommunity {

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
    // FIRESTORE COLLECTION
    // =====================================================

    private static final String COMMUNITY_COLLECTION =
            "uploadedRecipes";


    // =====================================================
    // BACKEND
    // =====================================================

    private final AdminController adminController;

    private final Firestore db;


    // =====================================================
    // ROOT
    // =====================================================

    private final BorderPane root;

    private final VBox postListBox;


    // =====================================================
    // DATA
    // =====================================================

    private List<CommunityPostItem> allPosts;


    // =====================================================
    // FILTERS
    // =====================================================

    private TextField searchField;

    private ComboBox<String> moderationFilter;


    // =====================================================
    // STATISTICS
    // =====================================================

    private Label totalPostsValue;

    private Label visiblePostsValue;

    private Label hiddenPostsValue;

    private Label totalLikesValue;

    private Label totalSavesValue;


    // =====================================================
    // LOADING
    // =====================================================

    private ProgressIndicator loadingIndicator;

    private Label loadingLabel;

    private Button refreshButton;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminCommunity() {

        adminController =
                new AdminController();


        db =
                FirebaseConfig.getFirestore();


        root =
                new BorderPane();


        postListBox =
                new VBox(15);


        allPosts =
                new ArrayList<>();


        buildPage();

        loadPosts();
    }


    // =====================================================
    // GET PAGE
    // =====================================================

    public BorderPane getCommunityPage() {

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
                        postListBox
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
                        "Community"
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
                        "Monitor and moderate recipes shared by the community."
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
                        loadPosts()
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


        totalPostsValue =
                new Label("0");

        visiblePostsValue =
                new Label("0");

        hiddenPostsValue =
                new Label("0");

        totalLikesValue =
                new Label("0");

        totalSavesValue =
                new Label("0");


        VBox total =
                createStatCard(
                        "Total Posts",
                        totalPostsValue,
                        PRIMARY_BLUE
                );


        VBox visible =
                createStatCard(
                        "Visible",
                        visiblePostsValue,
                        GREEN
                );


        VBox hidden =
                createStatCard(
                        "Hidden",
                        hiddenPostsValue,
                        RED
                );


        VBox likes =
                createStatCard(
                        "Total Likes",
                        totalLikesValue,
                        AMBER
                );


        VBox saves =
                createStatCard(
                        "Total Saves",
                        totalSavesValue,
                        PURPLE
                );


        VBox[] cards = {
                total,
                visible,
                hidden,
                likes,
                saves
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
                "Search author, dish, ingredients, recipe or Post ID..."
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


        moderationFilter =
                new ComboBox<>();


        moderationFilter.getItems()
                .addAll(
                        "All Posts",
                        "Visible",
                        "Hidden"
                );


        moderationFilter.setValue(
                "All Posts"
        );


        moderationFilter.setPrefWidth(
                160
        );


        moderationFilter.setPrefHeight(
                42
        );


        moderationFilter.setStyle(
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


        searchField.textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                applyFilters()
                );


        moderationFilter.setOnAction(
                event ->
                        applyFilters()
        );


        filters.getChildren()
                .addAll(
                        searchField,
                        moderationFilter
                );


        return filters;
    }


    // =====================================================
    // LOADING SECTION
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
                        "Loading community posts..."
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
    // LOAD POSTS
    // =====================================================

    private void loadPosts() {

        setLoading(
                true,
                "Loading community posts..."
        );


        Task<List<CommunityPostItem>> task =
                new Task<>() {

                    @Override
                    protected List<CommunityPostItem> call()
                            throws Exception {

                        List<CommunityPostItem> result =
                                new ArrayList<>();


                        QuerySnapshot snapshot =
                                db.collection(
                                        COMMUNITY_COLLECTION
                                )
                                        .get()
                                        .get();


                        for (
                                DocumentSnapshot document :
                                snapshot.getDocuments()
                        ) {

                            Map<String, Object> data =
                                    document.getData();


                            if (
                                    data == null
                            ) {

                                continue;
                            }


                            CommunityPostItem item =
                                    new CommunityPostItem(
                                            document.getId(),
                                            data
                                    );


                            result.add(
                                    item
                            );
                        }


                        return result;
                    }
                };


        task.setOnSucceeded(
                event -> {

                    List<CommunityPostItem> loaded =
                            task.getValue();


                    if (
                            loaded == null
                    ) {

                        loaded =
                                new ArrayList<>();
                    }


                    allPosts =
                            loaded;


                    updateStatistics();

                    applyFilters();


                    setLoading(
                            false,
                            allPosts.size()
                                    + " post(s) loaded."
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
                            "Unable to load community posts."
                    );


                    showError(
                            "Unable to load uploadedRecipes from Firestore."
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
                moderationFilter == null
        ) {

            return;
        }


        String query =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        String selectedModeration =
                moderationFilter
                        .getValue();


        List<CommunityPostItem> filtered =
                new ArrayList<>();


        for (
                CommunityPostItem item :
                allPosts
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
                            item.getPostId(),
                            query
                    )

                            ||

                    contains(
                            item.getUsername(),
                            query
                    )

                            ||

                    contains(
                            item.getDishName(),
                            query
                    )

                            ||

                    contains(
                            item.getIngredients(),
                            query
                    )

                            ||

                    contains(
                            item.getRecipe(),
                            query
                    );


            if (
                    !matchesSearch
            ) {

                continue;
            }


            // =================================================
            // MODERATION STATUS
            // =================================================

            String moderationStatus =
                    item.getModerationStatus();


            boolean matchesModeration =
                    selectedModeration == null

                            ||

                    selectedModeration.equalsIgnoreCase(
                            "All Posts"
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


        displayPosts(
                filtered
        );
    }


    // =====================================================
    // DISPLAY POSTS
    // =====================================================

    private void displayPosts(
            List<CommunityPostItem> posts
    ) {

        postListBox
                .getChildren()
                .clear();


        if (
                posts == null
                        ||
                posts.isEmpty()
        ) {

            postListBox.getChildren()
                    .add(
                            createEmptyState()
                    );

            return;
        }


        for (
                CommunityPostItem post :
                posts
        ) {

            postListBox.getChildren()
                    .add(
                            createPostCard(
                                    post
                            )
                    );
        }
    }


    // =====================================================
    // CREATE POST CARD
    // =====================================================

    private VBox createPostCard(
            CommunityPostItem post
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
                new HBox(15);


        header.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox identity =
                new VBox(4);


        Label dishName =
                new Label(
                        value(
                                post.getDishName(),
                                "Untitled Recipe"
                        )
                );


        dishName.setStyle(
                "-fx-text-fill: "
                        + TEXT_PRIMARY
                        + ";"
                        +
                "-fx-font-size: 18px;"
                        +
                "-fx-font-weight: bold;"
        );


        Label author =
                new Label(
                        "Posted by "
                                +
                        value(
                                post.getUsername(),
                                "Unknown User"
                        )
                                +
                        (
                                post.getTime()
                                        .isBlank()
                                        ? ""
                                        : "  •  "
                                        + post.getTime()
                        )
                );


        author.setStyle(
                "-fx-text-fill: "
                        + TEXT_SECONDARY
                        + ";"
                        +
                "-fx-font-size: 12px;"
        );


        identity.getChildren()
                .addAll(
                        dishName,
                        author
                );


        Region spacer =
                new Region();


        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        Label status =
                createModerationBadge(
                        post.getModerationStatus()
                );


        header.getChildren()
                .addAll(
                        identity,
                        spacer,
                        status
                );


        Separator separator =
                new Separator();


        // =================================================
        // CONTENT
        // =================================================

        HBox content =
                new HBox(18);


        content.setAlignment(
                Pos.TOP_LEFT
        );


        Node image =
                createImagePreview(
                        post
                );


        VBox recipeInformation =
                new VBox(12);


        HBox.setHgrow(
                recipeInformation,
                Priority.ALWAYS
        );


        recipeInformation.setMaxWidth(
                Double.MAX_VALUE
        );


        VBox ingredientBox =
                createTextSection(
                        "INGREDIENTS",
                        post.getIngredients(),
                        "No ingredients provided."
                );


        VBox recipeBox =
                createTextSection(
                        "RECIPE",
                        post.getRecipe(),
                        "No recipe instructions provided."
                );


        recipeInformation.getChildren()
                .addAll(
                        ingredientBox,
                        recipeBox
                );


        content.getChildren()
                .addAll(
                        image,
                        recipeInformation
                );


        // =================================================
        // ACTIVITY
        // =================================================

        HBox activity =
                new HBox(20);


        activity.setPadding(
                new Insets(
                        12
                )
        );


        activity.setStyle(
                "-fx-background-color: "
                        + CARD_BACKGROUND_ALT
                        + ";"
                        +
                "-fx-background-radius: 10;"
        );


        activity.getChildren()
                .addAll(
                        createCompactInfo(
                                "POST ID",
                                post.getPostId()
                        ),

                        createCompactInfo(
                                "LIKES",
                                String.valueOf(
                                        post.getLikeCount()
                                )
                        ),

                        createCompactInfo(
                                "SAVES",
                                String.valueOf(
                                        post.getSaveCount()
                                )
                        ),

                        createCompactInfo(
                                "MODERATION",
                                post.getModerationStatus()
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


        Button details =
                createNeutralButton(
                        "View Details"
                );


        details.setOnAction(
                event ->
                        showPostDetails(
                                post
                        )
        );


        actions.getChildren()
                .add(
                        details
                );


        if (
                post.isHidden()
        ) {

            Button restore =
                    createSuccessButton(
                            "Restore"
                    );


            restore.setOnAction(
                    event ->
                            restorePost(
                                    post
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
                            hidePost(
                                    post
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
                        deletePost(
                                post
                        )
        );


        actions.getChildren()
                .add(
                        delete
                );


        card.getChildren()
                .addAll(
                        header,
                        separator,
                        content,
                        activity,
                        actions
                );


        return card;
    }


    // =====================================================
    // IMAGE PREVIEW
    // =====================================================

    private Node createImagePreview(
            CommunityPostItem post
    ) {

        StackPane container =
                new StackPane();


        container.setPrefSize(
                180,
                150
        );


        container.setMinSize(
                180,
                150
        );


        container.setMaxSize(
                180,
                150
        );


        container.setStyle(
                "-fx-background-color: "
                        + BACKGROUND
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


        String imageUrl =
                post.getImageUrl();


        if (
                imageUrl == null
                        ||
                imageUrl.isBlank()
        ) {

            Label noImage =
                    new Label(
                            "No Image"
                    );


            noImage.setStyle(
                    "-fx-text-fill: #64748B;"
                            +
                    "-fx-font-size: 12px;"
            );


            container.getChildren()
                    .add(
                            noImage
                    );


            return container;
        }


        try {

            Image image =
                    new Image(
                            imageUrl,
                            true
                    );


            ImageView imageView =
                    new ImageView(
                            image
                    );


            imageView.setFitWidth(
                    180
            );


            imageView.setFitHeight(
                    150
            );


            imageView.setPreserveRatio(
                    true
            );


            imageView.setSmooth(
                    true
            );


            container.getChildren()
                    .add(
                            imageView
                    );


        } catch (
                Exception exception
        ) {

            Label error =
                    new Label(
                            "Image unavailable"
                    );


            error.setStyle(
                    "-fx-text-fill: #64748B;"
            );


            container.getChildren()
                    .add(
                            error
                    );
        }


        return container;
    }


    // =====================================================
    // TEXT SECTION
    // =====================================================

    private VBox createTextSection(
            String titleText,
            String valueText,
            String fallback
    ) {

        VBox box =
                new VBox(5);


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
                                fallback
                        )
                );


        value.setWrapText(
                true
        );


        value.setMaxWidth(
                Double.MAX_VALUE
        );


        value.setStyle(
                "-fx-text-fill: #CBD5E1;"
                        +
                "-fx-font-size: 12px;"
        );


        box.getChildren()
                .addAll(
                        title,
                        value
                );


        return box;
    }


    // =====================================================
    // HIDE POST
    // =====================================================

    private void hidePost(
            CommunityPostItem post
    ) {

        if (
                !confirm(
                        "Hide Community Post",
                        "Hide "
                                +
                        value(
                                post.getDishName(),
                                "this post"
                        )
                                +
                        "?",
                        "The post will remain in Firestore but will be marked as HIDDEN."
                )
        ) {

            return;
        }


        executeAction(
                "Hiding community post...",
                () ->
                        adminController
                                .hideCommunityPost(
                                        post.getPostId()
                                ),
                "Community post hidden successfully.",
                "Unable to hide community post."
        );
    }


    // =====================================================
    // RESTORE POST
    // =====================================================

    private void restorePost(
            CommunityPostItem post
    ) {

        if (
                !confirm(
                        "Restore Community Post",
                        "Restore "
                                +
                        value(
                                post.getDishName(),
                                "this post"
                        )
                                +
                        "?",
                        "The post will become visible again."
                )
        ) {

            return;
        }


        executeAction(
                "Restoring community post...",
                () ->
                        adminController
                                .restoreCommunityPost(
                                        post.getPostId()
                                ),
                "Community post restored successfully.",
                "Unable to restore community post."
        );
    }


    // =====================================================
    // DELETE POST
    // =====================================================

    private void deletePost(
            CommunityPostItem post
    ) {

        if (
                !confirm(
                        "Delete Community Post",
                        "Permanently delete "
                                +
                        value(
                                post.getDishName(),
                                "this post"
                        )
                                +
                        "?",
                        "This removes the uploadedRecipes document from Firestore and cannot be undone."
                )
        ) {

            return;
        }


        executeAction(
                "Deleting community post...",
                () ->
                        adminController
                                .deleteCommunityPost(
                                        post.getPostId()
                                ),
                "Community post deleted successfully.",
                "Unable to delete community post."
        );
    }


    // =====================================================
    // EXECUTE ACTION
    // =====================================================

    private void executeAction(
            String loadingText,
            CommunityAction action,
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


                        loadPosts();

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


    // =====================================================
    // ACTION INTERFACE
    // =====================================================

    @FunctionalInterface
    private interface CommunityAction {

        boolean execute();
    }


    // =====================================================
    // DETAILS DIALOG
    // =====================================================

    private void showPostDetails(
            CommunityPostItem post
    ) {

        SameStageDialog<Void> dialog =
                new SameStageDialog<>();


        dialog.setTitle(
                "Community Post Details"
        );


        dialog.setHeaderText(
                value(
                        post.getDishName(),
                        "Community Post"
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
                                "Post ID",
                                post.getPostId()
                        ),

                        detailRow(
                                "Username",
                                post.getUsername()
                        ),

                        detailRow(
                                "Posted Time",
                                post.getTime()
                        ),

                        detailRow(
                                "Dish Name",
                                post.getDishName()
                        ),

                        detailRow(
                                "Ingredients",
                                post.getIngredients()
                        ),

                        detailRow(
                                "Recipe",
                                post.getRecipe()
                        ),

                        detailRow(
                                "Image URL",
                                post.getImageUrl()
                        ),

                        detailRow(
                                "Likes",
                                String.valueOf(
                                        post.getLikeCount()
                                )
                        ),

                        detailRow(
                                "Saves",
                                String.valueOf(
                                        post.getSaveCount()
                                )
                        ),

                        detailRow(
                                "Moderation Status",
                                post.getModerationStatus()
                        ),

                        detailRow(
                                "Moderated At",
                                post.getModeratedAt()
                        )
                );


        ScrollPane scrollPane =
                new ScrollPane(
                        content
                );


        scrollPane.setFitToWidth(
                true
        );


        scrollPane.setPrefHeight(
                570
        );


        scrollPane.setStyle(
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
                        scrollPane
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
                allPosts.size();


        int visible =
                0;


        int hidden =
                0;


        int likes =
                0;


        int saves =
                0;


        for (
                CommunityPostItem item :
                allPosts
        ) {

            if (
                    item == null
            ) {

                continue;
            }


            if (
                    item.isHidden()
            ) {

                hidden++;

            } else {

                visible++;
            }


            likes +=
                    item.getLikeCount();


            saves +=
                    item.getSaveCount();
        }


        totalPostsValue.setText(
                String.valueOf(
                        total
                )
        );


        visiblePostsValue.setText(
                String.valueOf(
                        visible
                )
        );


        hiddenPostsValue.setText(
                String.valueOf(
                        hidden
                )
        );


        totalLikesValue.setText(
                String.valueOf(
                        likes
                )
        );


        totalSavesValue.setText(
                String.valueOf(
                        saves
                )
        );
    }


    // =====================================================
    // MODERATION BADGE
    // =====================================================

    private Label createModerationBadge(
            String status
    ) {

        boolean hidden =
                status.equalsIgnoreCase(
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
                430
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
                        "No community posts found"
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
                        "Try changing the search or moderation filter."
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


    // =====================================================
    // COMMUNITY POST ITEM
    // =====================================================

    private class CommunityPostItem {

        private final String documentId;

        private final Map<String, Object> data;


        private CommunityPostItem(
                String documentId,
                Map<String, Object> data
        ) {

            this.documentId =
                    documentId;


            this.data =
                    data;
        }


        private String getPostId() {

            String storedPostId =
                    stringValue(
                            data.get(
                                    "postId"
                            )
                    );


            if (
                    !storedPostId.isBlank()
            ) {

                return storedPostId;
            }


            return documentId;
        }


        private String getUsername() {

            return stringValue(
                    data.get(
                            "username"
                    )
            );
        }


        private String getTime() {

            return stringValue(
                    data.get(
                            "time"
                    )
            );
        }


        private String getDishName() {

            return stringValue(
                    data.get(
                            "dishName"
                    )
            );
        }


        private String getIngredients() {

            return stringValue(
                    data.get(
                            "ingredients"
                    )
            );
        }


        private String getRecipe() {

            return stringValue(
                    data.get(
                            "recipe"
                    )
            );
        }


        private String getImageUrl() {

            return stringValue(
                    data.get(
                            "imageUrl"
                    )
            );
        }


        private String getModerationStatus() {

            String status =
                    stringValue(
                            data.get(
                                    "moderationStatus"
                            )
                    );


            /*
             * Older community posts do not contain
             * moderationStatus.
             *
             * Those posts should remain visible.
             */

            if (
                    status.isBlank()
            ) {

                return "VISIBLE";
            }


            return status.toUpperCase();
        }


        private boolean isHidden() {

            return getModerationStatus()
                    .equalsIgnoreCase(
                            "HIDDEN"
                    );
        }


        private int getLikeCount() {

            Object likedBy =
                    data.get(
                            "likedBy"
                    );


            if (
                    likedBy instanceof List<?>
            ) {

                return ((List<?>) likedBy)
                        .size();
            }


            return 0;
        }


        private int getSaveCount() {

            Object savedBy =
                    data.get(
                            "savedBy"
                    );


            if (
                    savedBy instanceof List<?>
            ) {

                return ((List<?>) savedBy)
                        .size();
            }


            return 0;
        }


        private String getModeratedAt() {

            Object value =
                    data.get(
                            "moderatedAt"
                    );


            if (
                    value == null
            ) {

                return "Not available";
            }


            if (
                    value instanceof Timestamp
            ) {

                Timestamp timestamp =
                        (Timestamp) value;


                return timestamp
                        .toDate()
                        .toString();
            }


            return String.valueOf(
                    value
            );
        }
    }
}