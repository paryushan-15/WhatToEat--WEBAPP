package com.bytebites.view.dietitian.dietitionFeatures;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.controller.FollowController;
import com.bytebites.controller.ImageUploadController;
import com.bytebites.controller.RecipeController;
import com.bytebites.dao.UserDao;
import com.bytebites.model.UploadedRecipe;
import com.bytebites.model.User;
import com.bytebites.model.session.SessionManager;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.bytebites.view.common.SameStageWindow;

public class DietitianSocialFeed {

    private RecipeController recipeController =
            new RecipeController();

    // =====================================================
    // CLOUDINARY IMAGE UPLOAD CONTROLLER
    // =====================================================

    private ImageUploadController imageUploadController =
            new ImageUploadController();

    // =====================================================
    // STORE USER POSTS
    // =====================================================

    private VBox myPostsContainer =
            new VBox(15);

    private VBox feedPostsContainer =
            new VBox(15);

    private Label emptyMyPostsMessage;

    // =====================================================
    // STORE FOLLOWED USERS
    // =====================================================

    private Set<String> followedUsers =
            new HashSet<>();

    private Set<String> ownPostsShownInFeed =
            new HashSet<>();

    private boolean firstFeedLoad = true;

    private FollowController followController =
            new FollowController();

    private UserDao userDao =
            new UserDao();

   
    // CURRENT CENTER CONTENT
   
    private VBox currentContent;

    // =====================================================
    // REMEMBER DETAILS PAGE SOURCE
    // =====================================================

    private boolean detailsOpenedFromMyPosts = false;

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public VBox getSocialFeed() {

        VBox main =
                new VBox();

        main.setStyle(
                "-fx-background-color: #0F172A;");

        // =====================================================
        // TOP NAVIGATION
        // =====================================================

        HBox navigation =
                new HBox(10);

        navigation.setPadding(
                new Insets(20, 25, 10, 25));

        navigation.setAlignment(
                Pos.CENTER_LEFT);

        Button feedButton =
                createNavigationButton(
                        "Social Feed",
                        true);

        Button followingButton =
                createNavigationButton(
                        "Following",
                        false);

        Button myPostsButton =
                createNavigationButton(
                        "My Posts",
                        false);

        navigation.getChildren().addAll(
                feedButton,
                followingButton,
                myPostsButton);

        // LOAD FOLLOWED USERS
       
        loadFollowedUsers();

        // =====================================================
        // CENTER CONTENT
        // =====================================================

        VBox centerContent =
                new VBox();

        currentContent =
                centerContent;

        centerContent.setPadding(
                new Insets(10, 25, 30, 25));

        centerContent.getChildren().add(
                createFeedPage());

        VBox.setVgrow(
                centerContent,
                Priority.ALWAYS);

        // =====================================================
        // FEED BUTTON
        // =====================================================

        feedButton.setOnAction(event -> {

            centerContent
                    .getChildren()
                    .clear();

            centerContent
                    .getChildren()
                    .add(
                            createFeedPage());

            feedButton.setStyle(
                    navigationButtonStyle(true));

            followingButton.setStyle(
                    navigationButtonStyle(false));

            myPostsButton.setStyle(
                    navigationButtonStyle(false));
        });

        // =====================================================
        // FOLLOWING BUTTON
        // =====================================================

        followingButton.setOnAction(event -> {

            centerContent
                    .getChildren()
                    .clear();

            centerContent
                    .getChildren()
                    .add(
                            createFollowingPage());

            feedButton.setStyle(
                    navigationButtonStyle(false));

            followingButton.setStyle(
                    navigationButtonStyle(true));

            myPostsButton.setStyle(
                    navigationButtonStyle(false));
        });

        // =====================================================
        // MY POSTS BUTTON
        // =====================================================

        myPostsButton.setOnAction(event -> {

            centerContent
                    .getChildren()
                    .clear();

            centerContent
                    .getChildren()
                    .add(
                            createMyPostsPage());

            feedButton.setStyle(
                    navigationButtonStyle(false));

            followingButton.setStyle(
                    navigationButtonStyle(false));

            myPostsButton.setStyle(
                    navigationButtonStyle(true));
        });

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        main.getChildren().addAll(
                navigation,
                centerContent);

        return main;
    }

    // =====================================================
    // NAVIGATION BUTTON
    // =====================================================

    private Button createNavigationButton(
            String text,
            boolean selected) {

        Button button =
                new Button(text);

        button.setStyle(
                navigationButtonStyle(selected));

        button.setPrefHeight(
                38);

        button.setPadding(
                new Insets(8, 18, 8, 18));

        return button;
    }

    private String navigationButtonStyle(
            boolean selected) {

        if (selected) {

            return "-fx-background-color: #1976D2;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20;";
        }

        return "-fx-background-color: #1E293B;" +
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;";
    }

    // =====================================================
    // SOCIAL FEED PAGE
    // =====================================================

    private VBox createFeedPage() {

        VBox page =
                new VBox(18);

        page.setFillWidth(true);

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // TITLE ROW
        // =====================================================

        HBox titleRow =
                new HBox();

        titleRow.setAlignment(
                Pos.CENTER_LEFT);

        VBox titleBox =
                new VBox(5);

        Label title =
                new Label("Social Feed");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;");

        Label subtitle =
                new Label(
                        "Share healthy recipes, nutrition ideas and dietitian tips");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        titleBox.getChildren().addAll(
                title,
                subtitle);

        Region titleSpace =
                new Region();

        HBox.setHgrow(
                titleSpace,
                Priority.ALWAYS);

        Button createPostButton =
                new Button("+  Create Post");

        createPostButton.setPrefHeight(
                40);

        createPostButton.setPadding(
                new Insets(8, 18, 8, 18));

        createPostButton.setStyle(
                "-fx-background-color: #1976D2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 8;");

        createPostButton.setOnAction(event -> {

            showCreatePostWindow();

        });

        titleRow.getChildren().addAll(
                titleBox,
                titleSpace,
                createPostButton);

        // =====================================================
        // FEED POSTS
        // =====================================================

        feedPostsContainer =
                new VBox(15);

        loadAllFeedPosts();

        ScrollPane postScroll =
                createScrollPane(
                        feedPostsContainer);

        VBox.setVgrow(
                postScroll,
                Priority.ALWAYS);

        page.getChildren().addAll(
                titleRow,
                postScroll);

        return page;
    }

    // =====================================================
    // CREATE POST BOX
    // =====================================================

    private VBox createPostBox() {

        VBox box =
                new VBox(10);

        box.setPadding(
                new Insets(18));

        box.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        Label title =
                new Label("Create a Post");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;");

        Label dishNameLabel =
                new Label("Dish Name");

        dishNameLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextField dishName =
                new TextField();

        dishName.setPromptText(
                "Enter Dish Name");

        dishName.setPrefHeight(
                42);

        dishName.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        Label ingredientsLabel =
                new Label("Ingredients");

        ingredientsLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextArea ingredients =
                new TextArea();

        ingredients.setPromptText(
                "Enter Ingredients");

        ingredients.setWrapText(
                true);

        ingredients.setPrefRowCount(
                3);

        ingredients.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-background-color: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        Label recipeLabel =
                new Label("Recipe");

        recipeLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextArea recipe =
                new TextArea();

        recipe.setPromptText(
                "Write Recipe");

        recipe.setWrapText(
                true);

        recipe.setPrefRowCount(
                8);

        recipe.setPrefHeight(
                180);

        recipe.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-background-color: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        ImageView selectedImage =
                new ImageView();

        selectedImage.setFitWidth(
                150);

        selectedImage.setFitHeight(
                100);

        selectedImage.setPreserveRatio(
                true);

        selectedImage.setVisible(
                false);

        selectedImage.setManaged(
                false);

        final File[] selectedImageFile =
                new File[1];

        Button imageButton =
                new Button("📷 Add Photo");

        imageButton.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;");

        imageButton.setOnAction(event -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select Recipe Image");

            fileChooser
                    .getExtensionFilters()
                    .add(
                            new FileChooser.ExtensionFilter(
                                    "Image Files",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg"));

            File file =
                    fileChooser.showOpenDialog(
                            null);

            if (file != null) {

                selectedImageFile[0] =
                        file;

                Image image =
                        new Image(
                                file.toURI()
                                        .toString());

                selectedImage.setImage(
                        image);

                selectedImage.setVisible(
                        true);

                selectedImage.setManaged(
                        true);
            }
        });

        Button backButton = new Button("←  Back");

        backButton.setPrefWidth(90);

        backButton.setStyle(
                "-fx-background-color: #334155;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnAction(event -> {
                if (box.getScene() != null) {
                        SameStageWindow.closeContaining(box);
                }
        });

        Button postButton =
                new Button("Post");

        postButton.setPrefWidth(
                90);

        postButton.setStyle(
                "-fx-background-color: #1976D2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        HBox buttonRow =
                new HBox(10);

        buttonRow.setAlignment(
                Pos.CENTER_LEFT);

        Region buttonSpace =
                new Region();

        HBox.setHgrow(
                buttonSpace,
                Priority.ALWAYS);

        buttonRow.getChildren().addAll(
                backButton,
                imageButton,
                buttonSpace,
                postButton);

        postButton.setOnAction(event -> {

            String dish =
                    dishName.getText().trim();

            String ingredientText =
                    ingredients.getText().trim();

            String recipeText =
                    recipe.getText().trim();

            String currentUid =
                    SessionManager.getUid();

            if (currentUid == null
                    || currentUid.isEmpty()) {

                showErrorMessage(
                        box,
                        "User session not found. Please login again.");

                return;
            }

            if (dish.isEmpty()
                    || ingredientText.isEmpty()
                    || recipeText.isEmpty()) {

                showErrorMessage(
                        box,
                        "Please enter Dish Name, Ingredients and Recipe.");

                return;
            }

            String imageUrl =
                    null;

            if (selectedImageFile[0] != null) {

                postButton.setDisable(
                        true);

                imageUrl =
                        imageUploadController
                                .imageUpload(
                                        selectedImageFile[0]);

                postButton.setDisable(
                        false);

                if (imageUrl == null
                        || imageUrl.isEmpty()) {

                    showErrorMessage(
                            box,
                            "Image upload failed. Please try again.");

                    return;
                }
            }

            String currentTime =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd MMM yyyy, hh:mm a"));

            if (emptyMyPostsMessage != null
                    && myPostsContainer
                            .getChildren()
                            .contains(
                                    emptyMyPostsMessage)) {

                myPostsContainer
                        .getChildren()
                        .remove(
                                emptyMyPostsMessage);
            }

            recipeController.addUploadedRecipe(
                    currentUid,
                    currentTime,
                    dish,
                    ingredientText,
                    recipeText,
                    imageUrl);

            loadAllFeedPosts();

            dishName.clear();

            ingredients.clear();

            recipe.clear();

            selectedImage.setImage(
                    null);

            selectedImage.setVisible(
                    false);

            selectedImage.setManaged(
                    false);

            selectedImageFile[0] =
                    null;

            if (box.getScene() != null) {
                SameStageWindow.closeContaining(box);
            }
        });

        box.getChildren().addAll(
                title,
                dishNameLabel,
                dishName,
                ingredientsLabel,
                ingredients,
                recipeLabel,
                recipe,
                selectedImage,
                buttonRow);

        return box;
    }

    // =====================================================
    // ERROR MESSAGE
    // =====================================================

    private void showErrorMessage(
            VBox box,
            String message) {

        for (javafx.scene.Node node :
                box.getChildren()) {

            if (node instanceof Label
                    && ((Label) node)
                            .getText()
                            .equals(message)) {

                return;
            }
        }

        Label errorMessage =
                new Label(message);

        errorMessage.setStyle(
                "-fx-text-fill: #EF4444;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;");

        box.getChildren().add(
                errorMessage);
    }

    // =====================================================
    // MY POSTS COMPACT CARD
    // =====================================================

    private VBox createMyPostCard(
            UploadedRecipe uploadedRecipe,
            Image image) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        String ownerUid =
                uploadedRecipe.getUsername();

        String displayUsername =
                getDisplayName(ownerUid);

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            displayUsername =
                    displayUsername + " (You)";
        }

        // =====================================================
        // USER ROW
        // =====================================================

        HBox userRow =
                new HBox(10);

        userRow.setAlignment(
                Pos.CENTER_LEFT);

        Label avatar =
                new Label("👤");

        avatar.setPrefWidth(38);
        avatar.setPrefHeight(38);
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        HBox nameBadgeRow =
                new HBox(5);

        nameBadgeRow.setAlignment(
                Pos.CENTER_LEFT);

        Label name =
                new Label(
                        displayUsername);

        name.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        nameBadgeRow.getChildren().add(
                name);

        if (isDietitian(ownerUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass()
                                .getResourceAsStream(
                                        "/assets/icon/badge.png");

                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(
                                    badgeStream);

                    ImageView dietitianBadge =
                            new ImageView(
                                    badgeImage);

                    dietitianBadge.setFitWidth(
                            24);

                    dietitianBadge.setFitHeight(
                            24);

                    dietitianBadge.setPreserveRatio(
                            true);

                    nameBadgeRow
                            .getChildren()
                            .add(
                                    dietitianBadge);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Label postTime =
                new Label(
                        uploadedRecipe.getTime());

        postTime.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 11px;");

        userInfo.getChildren().addAll(
                nameBadgeRow,
                postTime);

        userRow.getChildren().addAll(
                avatar,
                userInfo);

        card.getChildren().add(
                userRow);

        // =====================================================
        // POST IMAGE
        // =====================================================

        if (image != null) {

            ImageView imageView =
                    new ImageView(image);

            imageView.setFitWidth(
                    500);

            imageView.setFitHeight(
                    300);

            imageView.setPreserveRatio(
                    true);

            imageView.setCursor(
                    Cursor.HAND);

            imageView.setOnMouseClicked(
                    event -> {

                        detailsOpenedFromMyPosts =
                                true;

                        showPostDetails(
                                uploadedRecipe);
                    });

            card.getChildren().add(
                    imageView);
        }

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dishName =
                new Label(
                        uploadedRecipe.getDishName());

        dishName.setWrapText(
                true);

        dishName.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        card.getChildren().add(
                dishName);

        // =====================================================
        // EDIT / DELETE
        // ONLY FOR OWN POST
        // =====================================================

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            HBox actionRow =
                    new HBox(10);

            actionRow.setAlignment(
                    Pos.CENTER_RIGHT);

            // =================================================
            // EDIT BUTTON
            // =================================================

            Button editButton =
                    new Button("✏ Edit");

            editButton.setPrefHeight(
                    34);

            editButton.setPadding(
                    new Insets(
                            6,
                            14,
                            6,
                            14));

            editButton.setStyle(
                    "-fx-background-color: #1976D2;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;");

            editButton.setOnAction(
                    event -> {

                        currentContent
                                .getChildren()
                                .clear();

                        currentContent
                                .getChildren()
                                .add(
                                        showEditRecipePage(
                                                uploadedRecipe));
                    });

            // =================================================
            // DELETE BUTTON
            // =================================================

            Button deleteButton =
                    new Button("🗑 Delete");

            deleteButton.setPrefHeight(
                    34);

            deleteButton.setPadding(
                    new Insets(
                            6,
                            14,
                            6,
                            14));

            deleteButton.setStyle(
                    "-fx-background-color: #7F1D1D;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;");

            deleteButton.setOnAction(
                    event -> {

                        deleteRecipe(
                                uploadedRecipe);
                    });

            actionRow.getChildren().addAll(
                    editButton,
                    deleteButton);

            card.getChildren().add(
                    actionRow);
        }

        return card;
    }

    // =====================================================
    // EDIT RECIPE PAGE
    // =====================================================

    private ScrollPane showEditRecipePage(
            UploadedRecipe recipe) {

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(20));

        content.setStyle(
                "-fx-background-color: #0F172A;");

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("Edit Recipe");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dishNameLabel =
                new Label("Dish Name");

        dishNameLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextField dishNameField =
                new TextField(
                        recipe.getDishName());

        dishNameField.setPrefHeight(
                42);

        dishNameField.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        // =====================================================
        // INGREDIENTS
        // =====================================================

        Label ingredientsLabel =
                new Label("Ingredients");

        ingredientsLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextArea ingredientsArea =
                new TextArea(
                        recipe.getIngredients());

        ingredientsArea.setWrapText(
                true);

        ingredientsArea.setPrefRowCount(
                5);

        ingredientsArea.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-background-color: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        // =====================================================
        // RECIPE
        // =====================================================

        Label recipeLabel =
                new Label("Recipe");

        recipeLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        TextArea recipeArea =
                new TextArea(
                        recipe.getRecipe());

        recipeArea.setWrapText(
                true);

        recipeArea.setPrefRowCount(
                10);

        recipeArea.setPrefHeight(
                220);

        recipeArea.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-background-color: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        // =====================================================
        // EXISTING IMAGE
        // NOT EDITABLE
        // =====================================================

        Label imageLabel =
                new Label("Recipe Image");

        imageLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        ImageView imageView =
                new ImageView();

        String imageUrl =
                recipe.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isEmpty()) {

            imageView.setImage(
                    new Image(
                            imageUrl,
                            true));

            imageView.setFitWidth(
                    300);

            imageView.setFitHeight(
                    200);

            imageView.setPreserveRatio(
                    true);
        }

        Label imageInfo =
                new Label(
                        "Existing image cannot be changed.");

        imageInfo.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 12px;");

        // =====================================================
        // BUTTON ROW
        // =====================================================

        HBox buttonRow =
                new HBox(10);

        buttonRow.setAlignment(
                Pos.CENTER_RIGHT);

        // =====================================================
        // CANCEL
        // =====================================================

        Button cancelButton =
                new Button("Cancel");

        cancelButton.setPrefHeight(
                36);

        cancelButton.setPadding(
                new Insets(
                        7,
                        16,
                        7,
                        16));

        cancelButton.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        cancelButton.setOnAction(
                event -> {

                    currentContent
                            .getChildren()
                            .clear();

                    currentContent
                            .getChildren()
                            .add(
                                    createMyPostsPage());
                });

        // =====================================================
        // UPDATE
        // =====================================================

        Button updateButton =
                new Button("Update");

        updateButton.setPrefHeight(
                36);

        updateButton.setPadding(
                new Insets(
                        7,
                        16,
                        7,
                        16));

        updateButton.setStyle(
                "-fx-background-color: #1976D2;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        updateButton.setOnAction(
                event -> {

                    String dish =
                            dishNameField
                                    .getText()
                                    .trim();

                    String ingredients =
                            ingredientsArea
                                    .getText()
                                    .trim();

                    String recipeText =
                            recipeArea
                                    .getText()
                                    .trim();

                    // =================================================
                    // VALIDATION
                    // =================================================

                    if (dish.isEmpty()
                            || ingredients.isEmpty()
                            || recipeText.isEmpty()) {

                        SameStageAlert alert =
                                new SameStageAlert(
                                        SameStageAlert.AlertType.WARNING);

                        alert.setTitle(
                                "Invalid Input");

                        alert.setHeaderText(
                                "Please fill all fields.");

                        alert.setContentText(
                                "Dish Name, Ingredients and Recipe are required.");

                        alert.showAndWait();

                        return;
                    }

                    // =================================================
                    // UPDATE
                    // =================================================
                    //
                    // IMPORTANT:
                    // Original image URL is preserved.
                    //

                    recipeController.updateRecipe(
                            recipe.getPostId(),
                            dish,
                            ingredients,
                            recipeText,
                            recipe.getImageUrl());

                    // =================================================
                    // REFRESH MY POSTS
                    // =================================================

                    currentContent
                            .getChildren()
                            .clear();

                    currentContent
                            .getChildren()
                            .add(
                                    createMyPostsPage());
                });

        buttonRow.getChildren().addAll(
                cancelButton,
                updateButton);

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        content.getChildren().addAll(
                title,
                dishNameLabel,
                dishNameField,
                ingredientsLabel,
                ingredientsArea,
                recipeLabel,
                recipeArea,
                imageLabel,
                imageView,
                imageInfo,
                buttonRow);

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scroll =
                createScrollPane(
                        content);

        scroll.setFitToWidth(
                true);

        scroll.setPannable(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        return scroll;
    }

    // =====================================================
    // DELETE RECIPE
    // =====================================================

    private void deleteRecipe(
            UploadedRecipe recipe) {

        if (recipe == null
                || recipe.getPostId() == null
                || recipe.getPostId().isEmpty()) {

            return;
        }

        SameStageAlert confirmation =
                new SameStageAlert(
                        SameStageAlert.AlertType.CONFIRMATION);

        confirmation.setTitle(
                "Delete Recipe");

        confirmation.setHeaderText(
                "Delete this recipe?");

        confirmation.setContentText(
                "This action cannot be undone.");

        confirmation.showAndWait()
                .ifPresent(response -> {

                    if (response ==
                            javafx.scene.control.ButtonType.OK) {

                        recipeController.deleteRecipe(
                                recipe.getPostId());

                        if (currentContent != null) {

                            currentContent
                                    .getChildren()
                                    .clear();

                            currentContent
                                    .getChildren()
                                    .add(
                                            createMyPostsPage());
                        }
                    }
                });
    }

    // =====================================================
    // FULL POST CARD
    // =====================================================

    private VBox createPostCard(
            String username,
            String ownerUid,
            String postId,
            String time,
            String dishName,
            String ingredients,
            String recipe,
            Image image,
            List<String> likedBy,
            List<String> savedBy) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        // IMPORTANT:
        // Do not reassign likedBy/savedBy.
        // This keeps them effectively final for lambdas.

        final List<String> safeLikedBy =
                likedBy == null
                        ? new ArrayList<>()
                        : likedBy;

        final List<String> safeSavedBy =
                savedBy == null
                        ? new ArrayList<>()
                        : savedBy;

        // =====================================================
        // USER ROW
        // =====================================================

        HBox userRow =
                new HBox(10);

        userRow.setAlignment(
                Pos.CENTER_LEFT);

        Label avatar =
                new Label("👤");

        avatar.setPrefWidth(
                38);

        avatar.setPrefHeight(
                38);

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        String displayUsername =
                username;

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            displayUsername =
                    username + " (You)";
        }

        HBox nameBadgeRow =
                new HBox(5);

        nameBadgeRow.setAlignment(
                Pos.CENTER_LEFT);

        Label name =
                new Label(
                        displayUsername);

        name.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        nameBadgeRow.getChildren().add(
                name);

        if (isDietitian(ownerUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass()
                                .getResourceAsStream(
                                        "/assets/icon/badge.png");

                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(
                                    badgeStream);

                    ImageView dietitianBadge =
                            new ImageView(
                                    badgeImage);

                    dietitianBadge.setFitWidth(
                            24);

                    dietitianBadge.setFitHeight(
                            24);

                    dietitianBadge.setPreserveRatio(
                            true);

                    nameBadgeRow
                            .getChildren()
                            .add(
                                    dietitianBadge);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Label postTime =
                new Label(time);

        postTime.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 11px;");

        userInfo.getChildren().addAll(
                nameBadgeRow,
                postTime);

        // =====================================================
        // FOLLOW BUTTON
        // =====================================================

        Region userSpace =
                new Region();

        HBox.setHgrow(
                userSpace,
                Priority.ALWAYS);

        Button followButton =
                new Button();

        if (SessionManager.getUid() != null
                && SessionManager.getUid()
                        .equals(ownerUid)) {

            followButton.setVisible(
                    false);

            followButton.setManaged(
                    false);

        } else {

            boolean alreadyFollowing =
                    ownerUid != null
                            && followedUsers
                                    .contains(ownerUid);

            if (alreadyFollowing) {

                followButton.setText(
                        "Following");

                followButton.setStyle(
                        "-fx-background-color: #334155;" +
                                "-fx-text-fill: #1976D2;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");

            } else {

                followButton.setText(
                        "Follow");

                followButton.setStyle(
                        "-fx-background-color: #1976D2;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");
            }

            followButton.setOnAction(
                    event -> {

                        String currentUserUid =
                                SessionManager
                                        .getUid();

                        if (currentUserUid == null
                                || currentUserUid.isEmpty()
                                || ownerUid == null
                                || ownerUid.isEmpty()) {

                            return;
                        }

                        if (!followedUsers
                                .contains(ownerUid)) {

                            followController
                                    .followUser(
                                            currentUserUid,
                                            ownerUid);

                            followedUsers.add(
                                    ownerUid);

                            followButton.setText(
                                    "Following");

                            followButton.setStyle(
                                    "-fx-background-color: #334155;" +
                                            "-fx-text-fill: #1976D2;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-background-radius: 8;");

                        } else {

                            followController
                                    .unfollowUser(
                                            currentUserUid,
                                            ownerUid);

                            followedUsers.remove(
                                    ownerUid);

                            followButton.setText(
                                    "Follow");

                            followButton.setStyle(
                                    "-fx-background-color: #1976D2;" +
                                            "-fx-text-fill: white;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-background-radius: 8;");
                        }
                    });
        }

        userRow.getChildren().addAll(
                avatar,
                userInfo,
                userSpace,
                followButton);

        // =====================================================
        // IMAGE
        // =====================================================

        ImageView imageView =
                null;

        if (image != null) {

            imageView =
                    new ImageView(image);

            imageView.setFitWidth(
                    500);

            imageView.setFitHeight(
                    300);

            imageView.setPreserveRatio(
                    true);
        }

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dishNameLabel =
                new Label("Dish Name");

        dishNameLabel.setStyle(
                "-fx-text-fill: #1976D2;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label dish =
                new Label(dishName);

        dish.setWrapText(
                true);

        dish.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // INGREDIENTS
        // =====================================================

        Label ingredientsTitle =
                new Label("Ingredients");

        ingredientsTitle.setStyle(
                "-fx-text-fill: #1976D2;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label ingredientsText =
                new Label(ingredients);

        ingredientsText.setWrapText(
                true);

        ingredientsText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 15px;");

        // =====================================================
        // RECIPE
        // =====================================================

        Label recipeTitle =
                new Label("Recipe");

        recipeTitle.setStyle(
                "-fx-text-fill: #1976D2;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label recipeText =
                new Label(recipe);

        recipeText.setWrapText(
                true);

        recipeText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 15px;");

        // =====================================================
        // LIKE / SAVE
        // =====================================================

        HBox actions =
                new HBox(10);

        String currentUid =
                SessionManager.getUid();

        boolean alreadyLiked =
                currentUid != null
                        && safeLikedBy.contains(
                                currentUid);

        boolean alreadySaved =
                currentUid != null
                        && safeSavedBy.contains(
                                currentUid);

        Button like =
                new Button();

        Label likeCount =
                new Label();

        likeCount.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 12px;");

        Button save =
                new Button();

        if (alreadyLiked) {

            like.setText(
                    "♥  Liked");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #1976D2;");

        } else {

            like.setText(
                    "♡  Like");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        int initialLikeCount =
                safeLikedBy.size();

        likeCount.setText(
                initialLikeCount +
                        (initialLikeCount == 1
                                ? " Like"
                                : " Likes"));

        if (alreadySaved) {

            save.setText(
                    "🔖  Saved");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #1976D2;");

        } else {

            save.setText(
                    "🔖  Save");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        // =====================================================
        // LIKE ACTION
        // =====================================================

        like.setOnAction(
                event -> {

                    if (currentUid == null
                            || currentUid.isEmpty()
                            || postId == null
                            || postId.isEmpty()) {

                        return;
                    }

                    if (safeLikedBy.contains(
                            currentUid)) {

                        recipeController
                                .unlikeRecipe(
                                        postId,
                                        currentUid);

                        safeLikedBy.remove(
                                currentUid);

                        like.setText(
                                "♡  Like");

                        like.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #CBD5E1;");

                    } else {

                        recipeController
                                .likeRecipe(
                                        postId,
                                        currentUid);

                        safeLikedBy.add(
                                currentUid);

                        like.setText(
                                "♥  Liked");

                        like.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #1976D2;");
                    }

                    int count =
                            safeLikedBy.size();

                    likeCount.setText(
                            count +
                                    (count == 1
                                            ? " Like"
                                            : " Likes"));
                });

        // =====================================================
        // SAVE ACTION
        // =====================================================

        save.setOnAction(
                event -> {

                    if (currentUid == null
                            || currentUid.isEmpty()
                            || postId == null
                            || postId.isEmpty()) {

                        return;
                    }

                    if (safeSavedBy.contains(
                            currentUid)) {

                        recipeController
                                .unsaveRecipeForUser(
                                        postId,
                                        currentUid);

                        safeSavedBy.remove(
                                currentUid);

                        save.setText(
                                "🔖  Save");

                        save.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #CBD5E1;");

                    } else {

                        recipeController
                                .saveRecipeForUser(
                                        postId,
                                        currentUid);

                        safeSavedBy.add(
                                currentUid);

                        save.setText(
                                "🔖  Saved");

                        save.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #1976D2;");
                    }
                });

        actions.getChildren().addAll(
                like,
                likeCount,
                save);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        card.getChildren().add(
                userRow);

        if (imageView != null) {

            card.getChildren().add(
                    imageView);
        }

        card.getChildren().addAll(
                dishNameLabel,
                dish,
                ingredientsTitle,
                ingredientsText,
                recipeTitle,
                recipeText,
                actions);

        return card;
    }

    // =====================================================
    // COMPACT SOCIAL FEED POST CARD
    // =====================================================

    private VBox createFeedPostCard(
            UploadedRecipe uploadedRecipe,
            Image image) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        String ownerUid =
                uploadedRecipe.getUsername();

        String currentUid =
                SessionManager.getUid();

        // =====================================================
        // USER ROW
        // =====================================================

        HBox userRow =
                new HBox(10);

        userRow.setAlignment(
                Pos.CENTER_LEFT);

        Label avatar =
                new Label("👤");

        avatar.setPrefWidth(
                38);

        avatar.setPrefHeight(
                38);

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        String displayUsername =
                getDisplayName(ownerUid);

        if (currentUid != null
                && currentUid.equals(
                        ownerUid)) {

            displayUsername =
                    displayUsername + " (You)";
        }

        HBox nameBadgeRow =
                new HBox(5);

        nameBadgeRow.setAlignment(
                Pos.CENTER_LEFT);

        Label name =
                new Label(
                        displayUsername);

        name.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        nameBadgeRow.getChildren().add(
                name);

        if (isDietitian(ownerUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass()
                                .getResourceAsStream(
                                        "/assets/icon/badge.png");

                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(
                                    badgeStream);

                    ImageView dietitianBadge =
                            new ImageView(
                                    badgeImage);

                    dietitianBadge.setFitWidth(
                            24);

                    dietitianBadge.setFitHeight(
                            24);

                    dietitianBadge.setPreserveRatio(
                            true);

                    nameBadgeRow
                            .getChildren()
                            .add(
                                    dietitianBadge);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Label postTime =
                new Label(
                        uploadedRecipe.getTime());

        postTime.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 11px;");

        userInfo.getChildren().addAll(
                nameBadgeRow,
                postTime);

        Region userSpace =
                new Region();

        HBox.setHgrow(
                userSpace,
                Priority.ALWAYS);

        // =====================================================
        // FOLLOW BUTTON
        // =====================================================

        Button followButton =
                new Button();

        if (currentUid != null
                && currentUid.equals(
                        ownerUid)) {

            followButton.setVisible(
                    false);

            followButton.setManaged(
                    false);

        } else {

            boolean alreadyFollowing =
                    ownerUid != null
                            && followedUsers
                                    .contains(
                                            ownerUid);

            if (alreadyFollowing) {

                followButton.setText(
                        "Following");

                followButton.setStyle(
                        "-fx-background-color: #334155;" +
                                "-fx-text-fill: #1976D2;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");

            } else {

                followButton.setText(
                        "Follow");

                followButton.setStyle(
                        "-fx-background-color: #1976D2;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");
            }

            followButton.setOnAction(
                    event -> {

                        String loggedInUid =
                                SessionManager
                                        .getUid();

                        if (loggedInUid == null
                                || loggedInUid.isEmpty()
                                || ownerUid == null
                                || ownerUid.isEmpty()) {

                            return;
                        }

                        if (!followedUsers
                                .contains(
                                        ownerUid)) {

                            followController
                                    .followUser(
                                            loggedInUid,
                                            ownerUid);

                            followedUsers.add(
                                    ownerUid);

                            followButton.setText(
                                    "Following");

                            followButton.setStyle(
                                    "-fx-background-color: #334155;" +
                                            "-fx-text-fill: #1976D2;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-background-radius: 8;");

                        } else {

                            followController
                                    .unfollowUser(
                                            loggedInUid,
                                            ownerUid);

                            followedUsers.remove(
                                    ownerUid);

                            followButton.setText(
                                    "Follow");

                            followButton.setStyle(
                                    "-fx-background-color: #1976D2;" +
                                            "-fx-text-fill: white;" +
                                            "-fx-font-weight: bold;" +
                                            "-fx-background-radius: 8;");
                        }
                    });
        }

        userRow.getChildren().addAll(
                avatar,
                userInfo,
                userSpace,
                followButton);

        card.getChildren().add(
                userRow);

        // =====================================================
        // POST IMAGE
        // =====================================================

        if (image != null) {

            ImageView imageView =
                    new ImageView(image);

            imageView.setFitWidth(
                    500);

            imageView.setFitHeight(
                    300);

            imageView.setPreserveRatio(
                    true);

            imageView.setCursor(
                    Cursor.HAND);

            imageView.setOnMouseClicked(
                    event -> {

                        detailsOpenedFromMyPosts =
                                false;

                        showPostDetails(
                                uploadedRecipe);
                    });

            card.getChildren().add(
                    imageView);
        }

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dishNameLabel =
                new Label(
                        uploadedRecipe
                                .getDishName());

        dishNameLabel.setWrapText(
                true);

        dishNameLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        card.getChildren().add(
                dishNameLabel);

        // =====================================================
        // LIKE / SAVE
        // =====================================================

        HBox actions =
                new HBox(10);

        /*
         * IMPORTANT FIX:
         * These variables are declared once and never reassigned.
         * Therefore they are effectively final and can safely be
         * used inside lambda expressions.
         */

        final List<String> likedBy =
                uploadedRecipe.getLikedBy() == null
                        ? new ArrayList<>()
                        : uploadedRecipe.getLikedBy();

        final List<String> savedBy =
                uploadedRecipe.getSavedBy() == null
                        ? new ArrayList<>()
                        : uploadedRecipe.getSavedBy();

        boolean alreadyLiked =
                currentUid != null
                        && likedBy.contains(
                                currentUid);

        boolean alreadySaved =
                currentUid != null
                        && savedBy.contains(
                                currentUid);

        Button like =
                new Button();

        Label likeCount =
                new Label();

        likeCount.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 12px;");

        Button save =
                new Button();

        if (alreadyLiked) {

            like.setText(
                    "♥  Liked");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #1976D2;");

        } else {

            like.setText(
                    "♡  Like");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        int initialLikeCount =
                likedBy.size();

        likeCount.setText(
                initialLikeCount +
                        (initialLikeCount == 1
                                ? " Like"
                                : " Likes"));

        if (alreadySaved) {

            save.setText(
                    "🔖  Saved");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #1976D2;");

        } else {

            save.setText(
                    "🔖  Save");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        // =====================================================
        // LIKE ACTION
        // =====================================================

        like.setOnAction(
                event -> {

                    if (currentUid == null
                            || currentUid.isEmpty()
                            || uploadedRecipe
                                    .getPostId() == null
                            || uploadedRecipe
                                    .getPostId()
                                    .isEmpty()) {

                        return;
                    }

                    if (likedBy.contains(
                            currentUid)) {

                        recipeController
                                .unlikeRecipe(
                                        uploadedRecipe
                                                .getPostId(),
                                        currentUid);

                        likedBy.remove(
                                currentUid);

                        like.setText(
                                "♡  Like");

                        like.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #CBD5E1;");

                    } else {

                        recipeController
                                .likeRecipe(
                                        uploadedRecipe
                                                .getPostId(),
                                        currentUid);

                        likedBy.add(
                                currentUid);

                        like.setText(
                                "♥  Liked");

                        like.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #1976D2;");
                    }

                    int count =
                            likedBy.size();

                    likeCount.setText(
                            count +
                                    (count == 1
                                            ? " Like"
                                            : " Likes"));
                });

        // =====================================================
        // SAVE ACTION
        // =====================================================

        save.setOnAction(
                event -> {

                    if (currentUid == null
                            || currentUid.isEmpty()
                            || uploadedRecipe
                                    .getPostId() == null
                            || uploadedRecipe
                                    .getPostId()
                                    .isEmpty()) {

                        return;
                    }

                    if (savedBy.contains(
                            currentUid)) {

                        recipeController
                                .unsaveRecipeForUser(
                                        uploadedRecipe
                                                .getPostId(),
                                        currentUid);

                        savedBy.remove(
                                currentUid);

                        save.setText(
                                "🔖  Save");

                        save.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #CBD5E1;");

                    } else {

                        recipeController
                                .saveRecipeForUser(
                                        uploadedRecipe
                                                .getPostId(),
                                        currentUid);

                        savedBy.add(
                                currentUid);

                        save.setText(
                                "🔖  Saved");

                        save.setStyle(
                                "-fx-background-color: transparent;" +
                                        "-fx-text-fill: #1976D2;");
                    }
                });

        actions.getChildren().addAll(
                like,
                likeCount,
                save);

        card.getChildren().add(
                actions);

        return card;
    }

    // =====================================================
    // SHOW FULL POST DETAILS
    // =====================================================

    private void showPostDetails(
            UploadedRecipe uploadedRecipe) {

        if (currentContent == null
                || uploadedRecipe == null) {

            return;
        }

        currentContent
                .getChildren()
                .clear();

        currentContent
                .getChildren()
                .add(
                        createPostDetailsPage(
                                uploadedRecipe));
    }

    // =====================================================
    // FULL POST DETAILS PAGE
    // =====================================================

    private VBox createPostDetailsPage(
            UploadedRecipe uploadedRecipe) {

        VBox page =
                new VBox(15);

        page.setPadding(
                new Insets(
                        10,
                        0,
                        20,
                        0));

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // BACK BUTTON
        // =====================================================

        String backButtonText =
                detailsOpenedFromMyPosts
                        ? "←  Back to My Posts"
                        : "←  Back to Social Feed";

        Button backButton =
                new Button(
                        backButtonText);

        backButton.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        backButton.setPadding(
                new Insets(
                        8,
                        14,
                        8,
                        14));

        backButton.setOnAction(
                event -> {

                    currentContent
                            .getChildren()
                            .clear();

                    if (detailsOpenedFromMyPosts) {

                        currentContent
                                .getChildren()
                                .add(
                                        createMyPostsPage());

                    } else {

                        currentContent
                                .getChildren()
                                .add(
                                        createFeedPage());
                    }
                });

        // =====================================================
        // PAGE TITLE
        // =====================================================

        Label title =
                new Label(
                        "Recipe Details");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // FULL POST
        // =====================================================

        Image image =
                null;

        String imageUrl =
                uploadedRecipe.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isEmpty()) {

            image =
                    new Image(
                            imageUrl,
                            true);
        }

        VBox fullPost =
                createPostCard(
                        getDisplayName(
                                uploadedRecipe
                                        .getUsername()),
                        uploadedRecipe
                                .getUsername(),
                        uploadedRecipe
                                .getPostId(),
                        uploadedRecipe
                                .getTime(),
                        uploadedRecipe
                                .getDishName(),
                        uploadedRecipe
                                .getIngredients(),
                        uploadedRecipe
                                .getRecipe(),
                        image,
                        uploadedRecipe
                                .getLikedBy(),
                        uploadedRecipe
                                .getSavedBy());

        ScrollPane scroll =
                createScrollPane(
                        new VBox(fullPost));

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        page.getChildren().addAll(
                backButton,
                title,
                scroll);

        return page;
    }

    // =====================================================
    // LOAD ALL POSTS INTO SOCIAL FEED
    // =====================================================

    private void loadAllFeedPosts() {

        if (feedPostsContainer == null) {
            return;
        }

        feedPostsContainer
                .getChildren()
                .clear();

        List<UploadedRecipe> uploadedRecipes =
                recipeController
                        .getUploadedRecipes();

        List<UploadedRecipe> sortedRecipes =
                new ArrayList<>(
                        uploadedRecipes);

        sortedRecipes.sort(
                Comparator.comparing(
                        this::parsePostTime)
                        .reversed());

        String currentUid =
                SessionManager.getUid();

        // =====================================================
        // FIRST FEED LOAD
        // =====================================================

        if (firstFeedLoad) {

            for (UploadedRecipe uploadedRecipe :
                    sortedRecipes) {

                if (currentUid != null
                        && currentUid.equals(
                                uploadedRecipe
                                        .getUsername())
                        && uploadedRecipe
                                .getPostId() != null
                        && !uploadedRecipe
                                .getPostId()
                                .isEmpty()) {

                    ownPostsShownInFeed.add(
                            uploadedRecipe
                                    .getPostId());
                }
            }

            firstFeedLoad =
                    false;
        }

        // =====================================================
        // CREATE FEED POSTS
        // =====================================================

        for (UploadedRecipe uploadedRecipe :
                sortedRecipes) {

            if (currentUid != null
                    && currentUid.equals(
                            uploadedRecipe
                                    .getUsername())) {

                String postId =
                        uploadedRecipe
                                .getPostId();

                if (postId != null
                        && !postId.isEmpty()
                        && ownPostsShownInFeed
                                .contains(
                                        postId)) {

                    continue;
                }

                if (postId != null
                        && !postId.isEmpty()) {

                    ownPostsShownInFeed.add(
                            postId);
                }
            }

            Image image =
                    null;

            String imageUrl =
                    uploadedRecipe
                            .getImageUrl();

            if (imageUrl != null
                    && !imageUrl.isEmpty()) {

                image =
                        new Image(
                                imageUrl,
                                true);
            }

            feedPostsContainer
                    .getChildren()
                    .add(
                            createFeedPostCard(
                                    uploadedRecipe,
                                    image));
        }
    }

    // =====================================================
    // PARSE POST TIME
    // =====================================================

    private LocalDateTime parsePostTime(
            UploadedRecipe recipe) {

        if (recipe == null
                || recipe.getTime() == null
                || recipe.getTime().isEmpty()) {

            return LocalDateTime.MIN;
        }

        try {

            return LocalDateTime.parse(
                    recipe.getTime(),
                    DateTimeFormatter.ofPattern(
                            "dd MMM yyyy, hh:mm a"));

        } catch (Exception e) {

            return LocalDateTime.MIN;
        }
    }

    // =====================================================
    // GET FOLLOWING USER DISPLAY NAME
    // =====================================================

    private String getFollowingDisplayName(
            String uid) {

        if (uid == null
                || uid.isEmpty()) {

            return "User";
        }

        try {

            DocumentSnapshot dietitianDocument =
                    FirebaseConfig
                            .getFirestore()
                            .collection(
                                    "dietitians")
                            .document(uid)
                            .get()
                            .get();

            if (dietitianDocument
                    .exists()) {

                String name =
                        dietitianDocument
                                .getString(
                                        "name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

            DocumentSnapshot userDocument =
                    FirebaseConfig
                            .getFirestore()
                            .collection(
                                    "Users")
                            .document(uid)
                            .get()
                            .get();

            if (userDocument.exists()) {

                String name =
                        userDocument
                                .getString(
                                        "name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

            User followedUser =
                    userDao.getUserByUid(
                            uid);

            if (followedUser != null
                    && followedUser.getName() != null
                    && !followedUser
                            .getName()
                            .isEmpty()) {

                return followedUser
                        .getName();
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to get following user name for UID: "
                            + uid);

            e.printStackTrace();
        }

        return "User";
    }

    // =====================================================
    // GET USER DISPLAY NAME
    // =====================================================

    private String getDisplayName(
            String uid) {

        if (uid == null
                || uid.isEmpty()) {

            return "User";
        }

        if (uid.equals(
                SessionManager.getUid())
                && SessionManager.getName() != null
                && !SessionManager
                        .getName()
                        .isEmpty()) {

            return SessionManager
                    .getName();
        }

        try {

            DocumentSnapshot dietitianDocument =
                    FirebaseConfig
                            .getFirestore()
                            .collection(
                                    "dietitians")
                            .document(uid)
                            .get()
                            .get();

            if (dietitianDocument
                    .exists()) {

                String name =
                        dietitianDocument
                                .getString(
                                        "name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

            DocumentSnapshot userDocument =
                    FirebaseConfig
                            .getFirestore()
                            .collection(
                                    "Users")
                            .document(uid)
                            .get()
                            .get();

            if (userDocument.exists()) {

                String name =
                        userDocument
                                .getString(
                                        "name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "User";
    }

    // =====================================================
    // CHECK WHETHER POST OWNER IS DIETITIAN
    // =====================================================

    private boolean isDietitian(
            String uid) {

        if (uid == null
                || uid.isEmpty()) {

            return false;
        }

        if (uid.equals(
                SessionManager.getUid())
                && SessionManager.getRole() != null) {

            return "Dietitian"
                    .equalsIgnoreCase(
                            SessionManager
                                    .getRole());
        }

        try {

            DocumentSnapshot userDocument =
                    FirebaseConfig
                            .getFirestore()
                            .collection(
                                    "Users")
                            .document(uid)
                            .get()
                            .get();

            if (userDocument.exists()) {

                String role =
                        userDocument
                                .getString(
                                        "role");

                return role != null
                        && "Dietitian"
                                .equalsIgnoreCase(
                                        role);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // MY POSTS PAGE
    // =====================================================

    private VBox createMyPostsPage() {

        VBox page =
                new VBox(18);

        page.setPadding(
                new Insets(
                        10,
                        0,
                        20,
                        0));

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        Label title =
                new Label("My Posts");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        Label subtitle =
                new Label(
                        "Recipes and nutrition posts uploaded by you");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        myPostsContainer.setSpacing(
                15);

        myPostsContainer
                .getChildren()
                .clear();

        String currentUid =
                SessionManager.getUid();

        boolean foundMyPost =
                false;

        List<UploadedRecipe> uploadedRecipes =
                new ArrayList<>(
                        recipeController
                                .getUploadedRecipes());

        uploadedRecipes.sort(
                Comparator.comparing(
                        this::parsePostTime)
                        .reversed());

        for (UploadedRecipe uploadedRecipe :
                uploadedRecipes) {

            // =================================================
            // ONLY CURRENT USER'S POSTS
            // =================================================

            if (currentUid != null
                    && currentUid.equals(
                            uploadedRecipe
                                    .getUsername())) {

                foundMyPost =
                        true;

                Image image =
                        null;

                String imageUrl =
                        uploadedRecipe
                                .getImageUrl();

                if (imageUrl != null
                        && !imageUrl.isEmpty()) {

                    image =
                            new Image(
                                    imageUrl,
                                    true);
                }

                VBox myPost =
                        createMyPostCard(
                                uploadedRecipe,
                                image);

                myPostsContainer
                        .getChildren()
                        .add(
                                myPost);
            }
        }

        // =====================================================
        // NO POSTS MESSAGE
        // =====================================================

        if (!foundMyPost) {

            emptyMyPostsMessage =
                    new Label(
                            "You haven't created any posts yet.");

            emptyMyPostsMessage.setStyle(
                    "-fx-text-fill: #64748B;" +
                            "-fx-font-size: 14px;");

            myPostsContainer
                    .getChildren()
                    .add(
                            emptyMyPostsMessage);
        }

        // =====================================================
        // MY POSTS SCROLL
        // =====================================================

        ScrollPane scroll =
                createScrollPane(
                        myPostsContainer);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        page.getChildren().addAll(
                title,
                subtitle,
                scroll);

        return page;
    }

    // =====================================================
    // CREATE POST WINDOW
    // =====================================================

    private void showCreatePostWindow() {

        VBox postBox =
                createPostBox();

        ScrollPane scroll =
                createScrollPane(
                        postBox);

        scroll.setFitToWidth(
                true);

        VBox container =
                new VBox();

        container.setPadding(
                new Insets(20));

        container.setStyle(
                "-fx-background-color: #0F172A;");

        container.getChildren().add(
                scroll);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        SameStageWindow stage =
                new SameStageWindow();

        stage.setTitle(
                "Create Post");

        Scene scene =
                new Scene(
                        container,
                        650,
                        700);

        stage.setScene(
                scene);

        stage.show();
    }

    // =====================================================
    // FOLLOWING PAGE
    // =====================================================

    private VBox createFollowingPage() {

        VBox page =
                new VBox(15);

        page.setPadding(
                new Insets(
                        10,
                        0,
                        20,
                        0));

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        Label title =
                new Label("Following");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        Label subtitle =
                new Label(
                        "People you are following");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        VBox members =
                new VBox(10);

        String currentUid =
                SessionManager.getUid();

        boolean foundFollowing =
                false;

        if (currentUid != null
                && !currentUid.isEmpty()) {

            loadFollowedUsers();

            for (String followingUid :
                    followedUsers) {

                String name =
                        getFollowingDisplayName(
                                followingUid);

                members.getChildren().add(
                        followingMember(
                                name,
                                followingUid));

                foundFollowing =
                        true;
            }
        }

        if (!foundFollowing) {

            Label noFollowing =
                    new Label(
                            "You are not following anyone yet.");

            noFollowing.setStyle(
                    "-fx-text-fill: #64748B;" +
                            "-fx-font-size: 14px;");

            members.getChildren().add(
                    noFollowing);
        }

        ScrollPane scroll =
                createScrollPane(
                        members);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        page.getChildren().addAll(
                title,
                subtitle,
                scroll);

        return page;
    }

    // =====================================================
    // FOLLOWING MEMBER
    // =====================================================

    private HBox followingMember(
            String name,
            String followingUid) {

        HBox member =
                new HBox(12);

        member.setAlignment(
                Pos.CENTER_LEFT);

        member.setPadding(
                new Insets(15));

        member.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 10;");

        Label avatar =
                new Label("👤");

        avatar.setPrefWidth(
                42);

        avatar.setPrefHeight(
                42);

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;");

        VBox info =
                new VBox(3);

        HBox nameBadgeRow =
                new HBox(5);

        nameBadgeRow.setAlignment(
                Pos.CENTER_LEFT);

        Label nameLabel =
                new Label(name);

        nameLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;");

        nameBadgeRow.getChildren().add(
                nameLabel);

        if (isDietitian(followingUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass()
                                .getResourceAsStream(
                                        "/assets/icon/badge.png");

                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(
                                    badgeStream);

                    ImageView dietitianBadge =
                            new ImageView(
                                    badgeImage);

                    dietitianBadge.setFitWidth(
                            24);

                    dietitianBadge.setFitHeight(
                            24);

                    dietitianBadge.setPreserveRatio(
                            true);

                    nameBadgeRow
                            .getChildren()
                            .add(
                                    dietitianBadge);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        Label descriptionLabel =
                new Label(
                        "Following on ByteBites");

        descriptionLabel.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 12px;");

        info.getChildren().addAll(
                nameBadgeRow,
                descriptionLabel);

        Region space =
                new Region();

        HBox.setHgrow(
                space,
                Priority.ALWAYS);

        Button following =
                new Button("Following");

        following.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: #1976D2;" +
                        "-fx-background-radius: 8;");

        following.setOnAction(
                event -> {

                    String currentUid =
                            SessionManager
                                    .getUid();

                    if (currentUid == null
                            || currentUid.isEmpty()
                            || followingUid == null
                            || followingUid.isEmpty()) {

                        return;
                    }

                    followController
                            .unfollowUser(
                                    currentUid,
                                    followingUid);

                    followedUsers.remove(
                            followingUid);

                    member.setVisible(
                            false);

                    member.setManaged(
                            false);
                });

        member.getChildren().addAll(
                avatar,
                info,
                space,
                following);

        return member;
    }

    // =====================================================
    // LOAD FOLLOWED USERS
    // =====================================================

    private void loadFollowedUsers() {

        String currentUid =
                SessionManager.getUid();

        followedUsers.clear();

        if (currentUid == null
                || currentUid.isEmpty()) {

            return;
        }

        List<String> followingUids =
                followController
                        .getFollowingUsers(
                                currentUid);

        if (followingUids != null) {

            followedUsers.addAll(
                    followingUids);
        }
    }

    // =====================================================
    // COMMON SCROLL PANE
    // =====================================================

    private ScrollPane createScrollPane(
            VBox content) {

        ScrollPane scroll =
                new ScrollPane();

        scroll.setContent(
                content);

        scroll.setFitToWidth(
                true);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER);

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setPannable(
                true);

        scroll.setStyle(
                "-fx-background-color: #0F172A;" +
                        "-fx-background: #0F172A;");

        return scroll;
    }
}