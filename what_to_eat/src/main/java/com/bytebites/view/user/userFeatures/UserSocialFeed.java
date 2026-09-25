package com.bytebites.view.user.userFeatures;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import com.bytebites.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Cursor;
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
import javafx.stage.Modality;
import com.bytebites.view.common.SameStageWindow;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.bytebites.controller.RecipeController;
import com.bytebites.controller.ImageUploadController;
import com.bytebites.controller.FollowController;
import com.bytebites.dao.UserDao;
import com.bytebites.model.UploadedRecipe;
import com.bytebites.model.session.SessionManager;

public class UserSocialFeed {

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

    private VBox myPostsContainer = new VBox(15);

    private VBox feedPostsContainer = new VBox(15);

    private Label emptyMyPostsMessage;

    // =====================================================
    // STORE FOLLOWED USERS
    // =====================================================

    private Set<String> followedUsers = new HashSet<>();

    // Own posts are allowed to appear in Social Feed only once
    // after they are newly created. Afterwards they remain only in My Posts.
    private Set<String> ownPostsShownInFeed = new HashSet<>();
    private boolean firstFeedLoad = true;
    private String newlyCreatedPostId = null;

    private FollowController followController =
            new FollowController();

    private UserDao userDao =
            new UserDao();

    // =====================================================
    // CURRENT CENTER CONTENT
    // =====================================================

    private VBox currentContent;

    // =====================================================
    // REMEMBER DETAILS PAGE SOURCE
    // =====================================================

    private boolean detailsOpenedFromMyPosts = false;

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public VBox getSocialFeed() {

        VBox main = new VBox();

        main.setStyle(
                "-fx-background-color: #0F172A;");

        // =====================================================
        // TOP NAVIGATION
        // =====================================================

        HBox navigation = new HBox(10);

        navigation.setPadding(
                new Insets(20, 25, 10, 25));

        navigation.setAlignment(
                Pos.CENTER_LEFT);

        Button feedButton = createNavigationButton(
                "Social Feed",
                true);

        Button followingButton = createNavigationButton(
                "Following",
                false);

        Button myPostsButton = createNavigationButton(
                "My Posts",
                false);

        navigation.getChildren().addAll(
                feedButton,
                followingButton,
                myPostsButton);

        // =====================================================
        // LOAD FOLLOWED USERS
        // =====================================================

        loadFollowedUsers();

        // =====================================================
        // CENTER CONTENT
        // =====================================================

        VBox centerContent = new VBox();

        currentContent = centerContent;

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

            centerContent.getChildren().clear();

            centerContent.getChildren().add(
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

            centerContent.getChildren().clear();

            centerContent.getChildren().add(
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

            centerContent.getChildren().clear();

            centerContent.getChildren().add(
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

        Button button = new Button(text);

        button.setStyle(
                navigationButtonStyle(selected));

        button.setPrefHeight(38);

        button.setPadding(
                new Insets(8, 18, 8, 18));

        return button;
    }

    private String navigationButtonStyle(
            boolean selected) {

        if (selected) {

            return "-fx-background-color: #22C55E;" +
                    "-fx-text-fill: #0F172A;" +
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

        VBox page = new VBox(18);

        page.setFillWidth(true);

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // TITLE ROW
        // =====================================================

        HBox titleRow = new HBox();

        titleRow.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // TITLE + SUBTITLE
        // =====================================================

        VBox titleBox = new VBox(5);

        Label title = new Label(
                "Social Feed");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;");

        Label subtitle = new Label(
                "Share meals, recipes and ideas with your family");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        titleBox.getChildren().addAll(
                title,
                subtitle);

        // =====================================================
        // SPACE
        // =====================================================

        Region titleSpace = new Region();

        HBox.setHgrow(
                titleSpace,
                Priority.ALWAYS);

        // =====================================================
        // CREATE POST BUTTON
        // =====================================================

        Button createPostButton = new Button(
                "+  Create Post");

        createPostButton.setPrefHeight(40);

        createPostButton.setPadding(
                new Insets(8, 18, 8, 18));

        createPostButton.setStyle(
                "-fx-background-color: #22C55E;" +
                        "-fx-text-fill: #0F172A;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-background-radius: 8;");

        // =====================================================
        // CREATE POST BUTTON ACTION
        // =====================================================

       createPostButton.setOnAction(event -> {

    showCreatePostWindow(createPostButton);

});
        // =====================================================
        // ADD TITLE ROW
        // =====================================================

        titleRow.getChildren().addAll(
                titleBox,
                titleSpace,
                createPostButton);

        // =====================================================
        // FEED POSTS
        // =====================================================

        feedPostsContainer = new VBox(15);

        // Load posts from Firestore so every logged-in user
        // can see posts created by other users.
        loadAllFeedPosts();

        // =====================================================
        // FEED SCROLL
        // =====================================================

        ScrollPane postScroll = createScrollPane(
                feedPostsContainer);

        VBox.setVgrow(
                postScroll,
                Priority.ALWAYS);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        page.getChildren().addAll(
                titleRow,
                postScroll);

        return page;
    }

    // =====================================================
    // CREATE POST BOX
    // =====================================================

    private VBox createPostBox() {

        VBox box = new VBox(10);

        box.setPadding(
                new Insets(18));

        box.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        // =====================================================
        // TITLE
        // =====================================================

        Label title = new Label(
                "Create a Post");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // DISH NAME LABEL
        // =====================================================

        Label dishNameLabel = new Label(
                "Dish Name");

        dishNameLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // DISH NAME
        // =====================================================

        TextField dishName = new TextField();

        dishName.setPromptText(
                "Enter Dish Name");

        dishName.setPrefHeight(42);

        dishName.setStyle(
                "-fx-control-inner-background: #0F172A;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #64748B;" +
                        "-fx-border-color: #334155;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;");

        // =====================================================
        // INGREDIENTS LABEL
        // =====================================================

        Label ingredientsLabel = new Label(
                "Ingredients");

        ingredientsLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // INGREDIENTS
        // =====================================================

        TextArea ingredients = new TextArea();

        ingredients.setPromptText(
                "Enter Ingredients");

        ingredients.setWrapText(
                true);

        ingredients.setPrefRowCount(3);

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

        // =====================================================
        // RECIPE LABEL
        // =====================================================

        Label recipeLabel = new Label(
                "Recipe");

        recipeLabel.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // RECIPE
        // =====================================================

        TextArea recipe = new TextArea();

        recipe.setPromptText(
                "Write Recipe");

        recipe.setWrapText(
                true);

        recipe.setPrefRowCount(8);

        recipe.setPrefHeight(180);

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

        // =====================================================
        // IMAGE PREVIEW
        // =====================================================

        ImageView selectedImage = new ImageView();

        selectedImage.setFitWidth(150);

        selectedImage.setFitHeight(100);

        selectedImage.setPreserveRatio(
                true);

        selectedImage.setVisible(
                false);

        selectedImage.setManaged(
                false);

        // =====================================================
        // SELECTED IMAGE FILE
        // =====================================================

        final File[] selectedImageFile =
                new File[1];

        // =====================================================
        // IMAGE BUTTON
        // =====================================================

        Button imageButton = new Button(
                "📷 Add Photo");

        imageButton.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;");

        imageButton.setOnAction(event -> {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Select Recipe Image");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Image Files",
                            "*.png",
                            "*.jpg",
                            "*.jpeg"));

           File file =
        fileChooser.showOpenDialog(
                imageButton
                        .getScene()
                        .getWindow());

            if (file != null) {

                // Store selected file
                selectedImageFile[0] =
                        file;

                // Show image preview
                Image image =
                        new Image(
                                file.toURI().toString());

                selectedImage.setImage(
                        image);

                selectedImage.setVisible(
                        true);

                selectedImage.setManaged(
                        true);
            }
        });
       
        // =====================================================
        // BACK BUTTON
        // =====================================================

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
        
        
        // =====================================================
        // POST BUTTON
        // =====================================================

        Button postButton = new Button(
                "Post");

        postButton.setPrefWidth(90);

        postButton.setStyle(
                "-fx-background-color: #22C55E;" +
                        "-fx-text-fill: #0F172A;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        // =====================================================
        // BUTTON ROW
        // =====================================================

        HBox buttonRow = new HBox(10);

        buttonRow.setAlignment(
                Pos.CENTER_LEFT);

        Region buttonSpace = new Region();

        HBox.setHgrow(
                buttonSpace,
                Priority.ALWAYS);

        buttonRow.getChildren().addAll(
                backButton,
                imageButton,
                buttonSpace,
                postButton);

        // =====================================================
        // POST ACTION
        // =====================================================

        postButton.setOnAction(event -> {

            String dish =
                    dishName.getText().trim();

            String ingredientText =
                    ingredients.getText().trim();

            String recipeText =
                    recipe.getText().trim();

            Image image =
                    selectedImage.getImage();

            // =================================================
            // CHECK CURRENT LOGIN
            // =================================================

            String currentUid =
                    SessionManager.getUid();

            if (currentUid == null
                    || currentUid.isEmpty()) {

                showErrorMessage(
                        box,
                        "User session not found. Please login again.");

                return;
            }

            // =================================================
            // VALIDATION
            // =================================================

            if (dish.isEmpty()
                    || ingredientText.isEmpty()
                    || recipeText.isEmpty()) {

                showErrorMessage(
                        box,
                        "Please enter Dish Name, Ingredients and Recipe.");

                return;
            }

            // =================================================
            // UPLOAD IMAGE TO CLOUDINARY
            // =================================================

            String imageUrl = null;

            if (selectedImageFile[0] != null) {

                postButton.setDisable(
                        true);

                imageUrl =
                        imageUploadController.imageUpload(
                                selectedImageFile[0]);

                postButton.setDisable(
                        false);

                // =================================================
                // CHECK IMAGE UPLOAD
                // =================================================

                if (imageUrl == null
                        || imageUrl.isEmpty()) {

                    showErrorMessage(
                            box,
                            "Image upload failed. Please try again.");

                    return;
                }
            }

            // =================================================
            // CREATE USER POST
            // =================================================

           String currentTime =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "dd MMM yyyy, hh:mm a"));

            // The recipe is saved first so Firestore can generate
            // the unique postId. The feed is refreshed after saving.

            // =================================================
            // REMOVE EMPTY MESSAGE
            // =================================================

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

            // =================================================
            // SAVE RECIPE TO FIRESTORE
            // =================================================

                newlyCreatedPostId =
                        recipeController.addUploadedRecipe(
                                currentUid,
                                currentTime,
                                dish,
                                ingredientText,
                                recipeText,
                                imageUrl);

            // Reload the feed so the newly saved post has its
            // Firestore postId and can immediately use Like/Save.
            loadAllFeedPosts();

            // =================================================
            // CLEAR INPUT
            // =================================================

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

            // =================================================
            // CLOSE CREATE POST WINDOW
            // =================================================

            if (box.getScene() != null) {
                SameStageWindow.closeContaining(box);
            }
        });

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

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
    // POST CARD
    // =====================================================

    // =====================================================
    // MY POSTS COMPACT CARD
    // =====================================================
    //
    // My Posts shows only:
    // 1. User name + (You)
    // 2. Post image
    // 3. Dish name
    //
    // Full ingredients and recipe open when the image is clicked.
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

        // =====================================================
        // SHOW "(You)"
        // =====================================================

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
                createNameBadgeRow(
                        displayUsername,
                        ownerUid);

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

        // =====================================================
        // POST IMAGE
        // =====================================================

        card.getChildren().add(
                userRow);

        if (image != null) {

            ImageView imageView =
                    new ImageView(image);

            imageView.setFitWidth(500);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            // Make image clickable
            imageView.setCursor(
                    Cursor.HAND);

            // Open full recipe details
            imageView.setOnMouseClicked(event -> {

                detailsOpenedFromMyPosts = true;

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

        dishName.setWrapText(true);

        dishName.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");


        // =====================================================
        // ACTION BUTTONS
        // =====================================================

        HBox actionRow =
                new HBox(10);

        actionRow.setAlignment(
                Pos.CENTER_RIGHT);


        // =====================================================
        // EDIT BUTTON
        // =====================================================

        Button editButton =
                new Button("Edit");

        editButton.setStyle(
                "-fx-background-color: #3B82F6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 18;");

        editButton.setOnAction(event -> {

        if (currentContent != null) {

                currentContent.getChildren().clear();

                currentContent.getChildren().add(
                        showEditRecipePage(
                                uploadedRecipe));
        }
        });


        // =====================================================
        // DELETE BUTTON
        // =====================================================

        Button deleteButton =
                new Button("Delete");

        deleteButton.setStyle(
                "-fx-background-color: #EF4444;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 18;");

        deleteButton.setOnAction(event -> {

        deleteRecipe(
                uploadedRecipe);
        });


        // =====================================================
        // ADD BUTTONS
        // =====================================================

        actionRow.getChildren().addAll(
                editButton,
                deleteButton);


        // =====================================================
        // ADD DISH NAME + BUTTONS TO CARD
        // =====================================================

        card.getChildren().addAll(
                dishName,
                actionRow);

        return card;
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
            List<String> savedBy
    ) {

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

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

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        // =====================================================
        // SHOW "(You)" FOR CURRENT USER'S OWN POST
        // =====================================================

        String displayUsername =
                username;

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            displayUsername =
                    username + " (You)";
        }

        HBox nameBadgeRow =
                createNameBadgeRow(
                        displayUsername,
                        ownerUid);

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

        // Don't show Follow button on your own post
        if (SessionManager.getUid() != null
                && SessionManager.getUid().equals(ownerUid)) {

            followButton.setVisible(
                    false);

            followButton.setManaged(
                    false);

        } else {

            boolean alreadyFollowing =
                    ownerUid != null
                            && followedUsers.contains(ownerUid);

            if (alreadyFollowing) {

                followButton.setText(
                        "Following");

                followButton.setStyle(
                        "-fx-background-color: #334155;" +
                                "-fx-text-fill: #22C55E;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");

            } else {

                followButton.setText(
                        "Follow");

                followButton.setStyle(
                        "-fx-background-color: #22C55E;" +
                                "-fx-text-fill: #0F172A;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");
            }

            // =================================================
            // FOLLOW / UNFOLLOW ACTION
            // =================================================

            followButton.setOnAction(event -> {

                String currentUserUid =
                        SessionManager.getUid();

                if (currentUserUid == null
                        || currentUserUid.isEmpty()
                        || ownerUid == null
                        || ownerUid.isEmpty()) {

                    return;
                }

                if (!followedUsers.contains(ownerUid)) {

                    followController.followUser(
                            currentUserUid,
                            ownerUid);

                    followedUsers.add(ownerUid);

                    followButton.setText(
                            "Following");

                    followButton.setStyle(
                            "-fx-background-color: #334155;" +
                                    "-fx-text-fill: #22C55E;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-radius: 8;");

                } else {

                    followController.unfollowUser(
                            currentUserUid,
                            ownerUid);

                    followedUsers.remove(ownerUid);

                    followButton.setText(
                            "Follow");

                    followButton.setStyle(
                            "-fx-background-color: #22C55E;" +
                                    "-fx-text-fill: #0F172A;" +
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
        // IMAGE VIEW
        // =====================================================

        ImageView imageView = null;

        if (image != null) {

            imageView =
                    new ImageView(image);

            imageView.setFitWidth(500);

            imageView.setFitHeight(300);

            imageView.setPreserveRatio(
                    true);
        }

        // =====================================================
        // DISH NAME LABEL
        // =====================================================

        Label dishNameLabel =
                new Label("Dish Name");

        dishNameLabel.setStyle(
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dish =
                new Label(dishName);

        dish.setWrapText(
                true);

        dish.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // INGREDIENTS TITLE
        // =====================================================

        Label ingredientsTitle =
                new Label("Ingredients");

        ingredientsTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // INGREDIENTS TEXT
        // =====================================================

        Label ingredientsText =
                new Label(ingredients);

        ingredientsText.setWrapText(
                true);

        ingredientsText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 15px;");

        // =====================================================
        // RECIPE TITLE
        // =====================================================

        Label recipeTitle =
                new Label("Recipe");

        recipeTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // RECIPE TEXT
        // =====================================================

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
                        && likedBy != null
                        && likedBy.contains(currentUid);

        boolean alreadySaved =
                currentUid != null
                        && savedBy != null
                        && savedBy.contains(currentUid);

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
                            "-fx-text-fill: #22C55E;");

        } else {

            like.setText(
                    "♡  Like");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        int initialLikeCount =
                likedBy == null
                        ? 0
                        : likedBy.size();

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
                            "-fx-text-fill: #22C55E;");

        } else {

            save.setText(
                    "🔖  Save");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        // =================================================
        // LIKE ACTION
        // =================================================

        like.setOnAction(event -> {

            if (currentUid == null
                    || currentUid.isEmpty()
                    || postId == null
                    || postId.isEmpty()) {

                return;
            }

            if (likedBy.contains(currentUid)) {

                recipeController.unlikeRecipe(
                        postId,
                        currentUid);

                likedBy.remove(currentUid);

                like.setText(
                        "♡  Like");

                like.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #CBD5E1;");

            } else {

                recipeController.likeRecipe(
                        postId,
                        currentUid);

                likedBy.add(currentUid);

                like.setText(
                        "♥  Liked");

                like.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #22C55E;");
            }

            int count =
                    likedBy.size();

            likeCount.setText(
                    count +
                            (count == 1
                                    ? " Like"
                                    : " Likes"));
        });

        // =================================================
        // SAVE ACTION
        // =================================================

        save.setOnAction(event -> {

            if (currentUid == null
                    || currentUid.isEmpty()
                    || postId == null
                    || postId.isEmpty()) {

                return;
            }

            if (savedBy.contains(currentUid)) {

                recipeController.unsaveRecipeForUser(
                        postId,
                        currentUid);

                savedBy.remove(currentUid);

                save.setText(
                        "🔖  Save");

                save.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #CBD5E1;");

            } else {

                recipeController.saveRecipeForUser(
                        postId,
                        currentUid);

                savedBy.add(currentUid);

                save.setText(
                        "🔖  Saved");

                save.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #22C55E;");
            }
        });

        actions.getChildren().addAll(
                like,
                likeCount,
                save);

        // =====================================================
        // ADD EVERYTHING TO CARD
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

        avatar.setPrefWidth(38);
        avatar.setPrefHeight(38);
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        // =====================================================
        // SHOW "(You)" FOR CURRENT USER'S OWN POST
        // =====================================================

        String displayUsername =
                getDisplayName(ownerUid);

        if (currentUid != null
                && currentUid.equals(ownerUid)) {

            displayUsername =
                    displayUsername + " (You)";
        }

        HBox nameBadgeRow =
                createNameBadgeRow(
                        displayUsername,
                        ownerUid);

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
        // FOLLOW BUTTON - VISIBLE OUTSIDE THE POST
        // =====================================================

        Button followButton =
                new Button();

        if (currentUid != null
                && currentUid.equals(ownerUid)) {

            followButton.setVisible(false);
            followButton.setManaged(false);

        } else {

            boolean alreadyFollowing =
                    ownerUid != null
                            && followedUsers.contains(ownerUid);

            if (alreadyFollowing) {

                followButton.setText(
                        "Following");

                followButton.setStyle(
                        "-fx-background-color: #334155;" +
                                "-fx-text-fill: #22C55E;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");

            } else {

                followButton.setText(
                        "Follow");

                followButton.setStyle(
                        "-fx-background-color: #22C55E;" +
                                "-fx-text-fill: #0F172A;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 8;");
            }

            followButton.setOnAction(event -> {

                String loggedInUid =
                        SessionManager.getUid();

                if (loggedInUid == null
                        || loggedInUid.isEmpty()
                        || ownerUid == null
                        || ownerUid.isEmpty()) {

                    return;
                }

                if (!followedUsers.contains(ownerUid)) {

                    followController.followUser(
                            loggedInUid,
                            ownerUid);

                    followedUsers.add(ownerUid);

                    followButton.setText(
                            "Following");

                    followButton.setStyle(
                            "-fx-background-color: #334155;" +
                                    "-fx-text-fill: #22C55E;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-background-radius: 8;");

                } else {

                    followController.unfollowUser(
                            loggedInUid,
                            ownerUid);

                    followedUsers.remove(ownerUid);

                    followButton.setText(
                            "Follow");

                    followButton.setStyle(
                            "-fx-background-color: #22C55E;" +
                                    "-fx-text-fill: #0F172A;" +
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

            imageView.setFitWidth(500);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            // Make the image look clickable
            imageView.setCursor(
                    Cursor.HAND);

            // Open complete recipe when image is clicked
            imageView.setOnMouseClicked(event -> {

                detailsOpenedFromMyPosts = false;

                showPostDetails(
                        uploadedRecipe);
            });

            card.getChildren().add(
                    imageView);
        }

        // =====================================================
        // DISH NAME - VISIBLE OUTSIDE THE POST
        // =====================================================

        Label dishNameLabel =
                new Label(
                        uploadedRecipe.getDishName());

        dishNameLabel.setWrapText(true);

        dishNameLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        card.getChildren().add(
                dishNameLabel);

        // =====================================================
        // LIKE / SAVE ACTIONS - VISIBLE OUTSIDE THE POST
        // =====================================================

        HBox actions =
                new HBox(10);

        boolean alreadyLiked =
                currentUid != null
                        && uploadedRecipe.getLikedBy() != null
                        && uploadedRecipe.getLikedBy()
                                .contains(currentUid);

        boolean alreadySaved =
                currentUid != null
                        && uploadedRecipe.getSavedBy() != null
                        && uploadedRecipe.getSavedBy()
                                .contains(currentUid);

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

            like.setText("♥  Liked");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #22C55E;");

        } else {

            like.setText("♡  Like");

            like.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        int initialLikeCount =
                uploadedRecipe.getLikedBy() == null
                        ? 0
                        : uploadedRecipe.getLikedBy().size();

        likeCount.setText(
                initialLikeCount +
                        (initialLikeCount == 1
                                ? " Like"
                                : " Likes"));

        if (alreadySaved) {

            save.setText("🔖  Saved");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #22C55E;");

        } else {

            save.setText("🔖  Save");

            save.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #CBD5E1;");
        }

        // =====================================================
        // LIKE ACTION
        // =====================================================

        like.setOnAction(event -> {

            if (currentUid == null
                    || currentUid.isEmpty()
                    || uploadedRecipe.getPostId() == null
                    || uploadedRecipe.getPostId().isEmpty()) {

                return;
            }

            List<String> likedBy =
                    uploadedRecipe.getLikedBy();

            if (likedBy.contains(currentUid)) {

                recipeController.unlikeRecipe(
                        uploadedRecipe.getPostId(),
                        currentUid);

                likedBy.remove(currentUid);

                like.setText("♡  Like");

                like.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #CBD5E1;");

            } else {

                recipeController.likeRecipe(
                        uploadedRecipe.getPostId(),
                        currentUid);

                likedBy.add(currentUid);

                like.setText("♥  Liked");

                like.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #22C55E;");
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

        save.setOnAction(event -> {

            if (currentUid == null
                    || currentUid.isEmpty()
                    || uploadedRecipe.getPostId() == null
                    || uploadedRecipe.getPostId().isEmpty()) {

                return;
            }

            List<String> savedBy =
                    uploadedRecipe.getSavedBy();

            if (savedBy.contains(currentUid)) {

                recipeController.unsaveRecipeForUser(
                        uploadedRecipe.getPostId(),
                        currentUid);

                savedBy.remove(currentUid);

                save.setText("🔖  Save");

                save.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #CBD5E1;");

            } else {

                recipeController.saveRecipeForUser(
                        uploadedRecipe.getPostId(),
                        currentUid);

                savedBy.add(currentUid);

                save.setText("🔖  Saved");

                save.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #22C55E;");
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
                new Insets(10, 0, 20, 0));

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // BACK BUTTON
        // =====================================================

        // =====================================================
        // BACK BUTTON
        // =====================================================

        String backButtonText =
                detailsOpenedFromMyPosts
                        ? "←  Back to My Posts"
                        : "←  Back to Social Feed";

        Button backButton =
                new Button(backButtonText);

        backButton.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        backButton.setPadding(
                new Insets(8, 14, 8, 14));

        backButton.setOnAction(event -> {

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
                new Label("Recipe Details");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // FULL POST
        // =====================================================

        Image image = null;

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
                                uploadedRecipe.getUsername()),
                        uploadedRecipe.getUsername(),
                        uploadedRecipe.getPostId(),
                        uploadedRecipe.getTime(),
                        uploadedRecipe.getDishName(),
                        uploadedRecipe.getIngredients(),
                        uploadedRecipe.getRecipe(),
                        image,
                        uploadedRecipe.getLikedBy(),
                        uploadedRecipe.getSavedBy());

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
// LOAD ALL SOCIAL FEED POSTS
// =====================================================

private void loadAllFeedPosts() {

    if (feedPostsContainer == null) {
        return;
    }

    // =================================================
    // CLEAR OLD FEED
    // =================================================

    feedPostsContainer.getChildren().clear();

    // =================================================
    // GET ALL UPLOADED RECIPES
    // =================================================

    List<UploadedRecipe> uploadedRecipes =
            recipeController.getUploadedRecipes();

    // =================================================
    // SORT POSTS - LATEST FIRST
    // =================================================

    List<UploadedRecipe> sortedRecipes =
            new ArrayList<>(uploadedRecipes);

    sortedRecipes.sort(
            Comparator.comparing(
                    this::parsePostTime)
                    .reversed());

    // =================================================
    // CURRENT LOGGED-IN USER
    // =================================================

    String currentUid =
            SessionManager.getUid();

    // =================================================
    // FIRST FEED LOAD
    // =================================================
    //
    // On the first load, all OLD posts created by the
    // current user are marked as already shown.
    //
    // Therefore old own posts will NOT appear in Feed.
    //
    // =================================================

    if (firstFeedLoad) {

        for (UploadedRecipe uploadedRecipe :
                sortedRecipes) {

            String postId =
                    uploadedRecipe.getPostId();

            String ownerUid =
                    uploadedRecipe.getUsername();

            if (currentUid != null
                    && currentUid.equals(ownerUid)
                    && postId != null
                    && !postId.isEmpty()) {

                ownPostsShownInFeed.add(postId);
            }
        }

        firstFeedLoad = false;
    }

    // =================================================
    // DISPLAY POSTS
    // =================================================

    for (UploadedRecipe uploadedRecipe :
            sortedRecipes) {

        String postId =
                uploadedRecipe.getPostId();

        String ownerUid =
                uploadedRecipe.getUsername();

        // =================================================
        // CURRENT USER'S POST
        // =================================================

        if (currentUid != null
                && currentUid.equals(ownerUid)) {

            // -------------------------------------------------
            // If this is the newly created post, allow it to
            // appear ONCE in Social Feed.
            // -------------------------------------------------

            if (postId != null
                    && postId.equals(
                            newlyCreatedPostId)) {

                Image image = null;

                String imageUrl =
                        uploadedRecipe.getImageUrl();

                if (imageUrl != null
                        && !imageUrl.isEmpty()) {

                    image = new Image(
                            imageUrl,
                            true);
                }

                feedPostsContainer.getChildren().add(
                        createFeedPostCard(
                                uploadedRecipe,
                                image));

                // Mark it as shown
                ownPostsShownInFeed.add(
                        postId);

                // Clear the new-post ID so it cannot
                // appear again on the next reload.
                newlyCreatedPostId = null;

                continue;
            }

            // -------------------------------------------------
            // OLD / ALREADY SHOWN OWN POST
            // -------------------------------------------------

            if (postId != null
                    && !postId.isEmpty()
                    && ownPostsShownInFeed.contains(
                            postId)) {

                continue;
            }

            // -------------------------------------------------
            // Safety: mark own post as shown
            // -------------------------------------------------

            if (postId != null
                    && !postId.isEmpty()) {

                ownPostsShownInFeed.add(
                        postId);
            }

            continue;
        }

        // =================================================
        // OTHER USERS' POSTS
        // =================================================

        Image image = null;

        String imageUrl =
                uploadedRecipe.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isEmpty()) {

            image = new Image(
                    imageUrl,
                    true);
        }

        feedPostsContainer.getChildren().add(
                createFeedPostCard(
                        uploadedRecipe,
                        image));
    }
}
    // =====================================================
    // PARSE POST TIME FOR SORTING
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
    // CREATE NAME + VERIFIED DIETITIAN BADGE
    // =====================================================
    //
    // Badge is shown ONLY when the post owner exists in
    // the "dietitians" collection.
    // Normal users do not get the badge.
    // =====================================================

    private HBox createNameBadgeRow(
            String displayUsername,
            String ownerUid) {

        HBox nameBadgeRow =
                new HBox(5);

        nameBadgeRow.setAlignment(
                Pos.CENTER_LEFT);

        Label name =
                new Label(displayUsername);

        name.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        nameBadgeRow.getChildren().add(
                name);

        // Check whether the post owner is a Dietitian.
        if (isDietitian(ownerUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass().getResourceAsStream(
                                "/assets/icon/badge.png");

                // Do not crash Social Feed if badge file
                // is missing from the classpath.
                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(badgeStream);

                    ImageView dietitianBadge =
                            new ImageView(badgeImage);

                    dietitianBadge.setFitWidth(24);
                    dietitianBadge.setFitHeight(24);
                    dietitianBadge.setPreserveRatio(true);

                    nameBadgeRow.getChildren().add(
                            dietitianBadge);

                } else {

                    System.out.println(
                            "Dietitian badge not found: "
                                    + "/assets/icon/badge.png");
                }

            } catch (Exception e) {

                System.out.println(
                        "Unable to load Dietitian badge.");
                e.printStackTrace();
            }
        }

        return nameBadgeRow;
    }

    // =====================================================
    // CHECK WHETHER POST OWNER IS A DIETITIAN
    // =====================================================
    //
    // Dietitians are stored in:
    // dietitians/{uid}
    //
    // Therefore, if the UID document exists in the
    // "dietitians" collection, the post owner is a Dietitian.
    // =====================================================

    private boolean isDietitian(String uid) {

        if (uid == null || uid.isEmpty()) {
            return false;
        }

        try {

            DocumentSnapshot dietitianDocument =
                    FirebaseConfig.getFirestore()
                            .collection("dietitians")
                            .document(uid)
                            .get()
                            .get();

            return dietitianDocument.exists();

        } catch (Exception e) {

            System.out.println(
                    "Unable to check Dietitian for UID: "
                            + uid);

            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // GET USER / DIETITIAN DISPLAY NAME
    // =====================================================
    //
    // Dietitian name is stored in:
    // dietitians/{uid}/name
    //
    // Normal user name is stored in:
    // Users/{uid}/name
    // =====================================================

    private String getDisplayName(String uid) {

        if (uid == null || uid.isEmpty()) {
            return "User";
        }

        // =================================================
        // CURRENT LOGGED-IN USER
        // =================================================

        // Use the name already loaded during login.
        if (uid.equals(SessionManager.getUid())
                && SessionManager.getName() != null
                && !SessionManager.getName().isEmpty()) {

            return SessionManager.getName();
        }

        try {

            // =================================================
            // FIRST CHECK DIETITIANS COLLECTION
            // =================================================

            DocumentSnapshot dietitianDocument =
                    FirebaseConfig.getFirestore()
                            .collection("dietitians")
                            .document(uid)
                            .get()
                            .get();

            if (dietitianDocument.exists()) {

                String name =
                        dietitianDocument.getString("name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

            // =================================================
            // THEN CHECK USERS COLLECTION
            // =================================================

            DocumentSnapshot userDocument =
                    FirebaseConfig.getFirestore()
                            .collection("Users")
                            .document(uid)
                            .get()
                            .get();

            if (userDocument.exists()) {

                String name =
                        userDocument.getString("name");

                if (name != null
                        && !name.isEmpty()) {

                    return name;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to get display name for UID: "
                            + uid);

            e.printStackTrace();
        }

        return "User";
    }

    // =====================================================
    // MY POSTS PAGE
    // =====================================================

    private VBox createMyPostsPage() {

        VBox page =
                new VBox(18);

        page.setPadding(
                new Insets(10, 0, 20, 0));

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // TITLE
        // =====================================================

        Label title =
                new Label("My Posts");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;");

        Label subtitle =
                new Label("Posts uploaded by you");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        // =====================================================
        // MY POSTS CONTAINER
        // =====================================================

        myPostsContainer.setSpacing(
                15);

        // =====================================================
        // CLEAR OLD POSTS
        // =====================================================

        myPostsContainer
                .getChildren()
                .clear();

        // =====================================================
        // GET CURRENT USER UID
        // =====================================================

        String currentUid =
                SessionManager.getUid();

        boolean foundMyPost = false;

        // =====================================================
        // LOAD POSTS FROM FIRESTORE
        // =====================================================

        List<UploadedRecipe> uploadedRecipes =
                new ArrayList<>(
                        recipeController.getUploadedRecipes());

        // =====================================================
        // SORT MY POSTS - LATEST FIRST
        // =====================================================

        uploadedRecipes.sort(
                Comparator.comparing(
                        this::parsePostTime)
                        .reversed());

        // =====================================================
        // CREATE MY POSTS FROM FIRESTORE DATA
        // =====================================================

        for (UploadedRecipe uploadedRecipe :
                uploadedRecipes) {

            // =================================================
            // ONLY SHOW POSTS OF CURRENT LOGGED-IN USER
            // =================================================

            if (currentUid != null
                    && currentUid.equals(
                            uploadedRecipe.getUsername())) {

                foundMyPost = true;

                Image image = null;

                String imageUrl =
                        uploadedRecipe.getImageUrl();

                // =================================================
                // LOAD IMAGE FROM CLOUDINARY
                // =================================================

                if (imageUrl != null
                        && !imageUrl.isEmpty()) {

                    image =
                            new Image(
                                    imageUrl,
                                    true);
                }

                // =================================================
                // CREATE POST CARD
                // =================================================

                VBox myPost =
                        createMyPostCard(
                                uploadedRecipe,
                                image);

                // =================================================
                // ADD POST
                // =================================================

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

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        page.getChildren().addAll(
                title,
                subtitle,
                scroll);

        return page;
    }

    // =====================================================
    // CREATE POST WINDOW
    // =====================================================

   private void showCreatePostWindow(Button createPostButton) {

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

// Main WTE window ko owner banana
if (createPostButton.getScene() != null) {

    stage.initOwner(
            createPostButton
                    .getScene()
                    .getWindow()
    );

    stage.initModality(
            Modality.WINDOW_MODAL
    );
}

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
                new Insets(10, 0, 20, 0));

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
                new Label("People you are following");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        // =====================================================
        // FOLLOWING MEMBERS
        // =====================================================

        VBox members =
                new VBox(10);

        // =====================================================
        // LOAD FOLLOWING USERS FROM FIRESTORE
        // =====================================================

        String currentUid =
                SessionManager.getUid();

        boolean foundFollowing = false;

        if (currentUid != null
                && !currentUid.isEmpty()) {

            loadFollowedUsers();

            for (String followingUid :
                    followedUsers) {

                // Get the correct display name from either
                // dietitians/{uid} or Users/{uid}.
                // Dietitians may not have a document in Users,
                // so userDao.getUserByUid() alone can return null.
                String name = getDisplayName(followingUid);

                members.getChildren().add(
                        followingMember(
                                name,
                                followingUid));

                foundFollowing = true;
            }
        }

        // =====================================================
        // NO FOLLOWING MESSAGE
        // =====================================================

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

        // =====================================================
        // FOLLOWING SCROLL
        // =====================================================

        ScrollPane scroll =
                createScrollPane(
                        members);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

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
            String followingUid
    ) {

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

        avatar.setPrefWidth(42);

        avatar.setPrefHeight(42);

        avatar.setAlignment(
                Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;");

        VBox info =
                new VBox(3);

        // Use the same verified Dietitian badge used in Social Feed.
        // Normal users will show only their name.
        HBox nameBadgeRow =
                createNameBadgeRow(
                        name,
                        followingUid);

        Label descriptionLabel =
                new Label("Following on ByteBites");

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
                        "-fx-text-fill: #22C55E;" +
                        "-fx-background-radius: 8;");

        // =====================================================
        // UNFOLLOW FROM FOLLOWING PAGE
        // =====================================================

        following.setOnAction(event -> {

            String currentUid =
                    SessionManager.getUid();

            if (currentUid == null
                    || currentUid.isEmpty()
                    || followingUid == null
                    || followingUid.isEmpty()) {

                return;
            }

            followController.unfollowUser(
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
                followController.getFollowingUsers(
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
    // =====================================================
    // EDIT RECIPE PAGE
   // =====================================================

private ScrollPane showEditRecipePage(
        UploadedRecipe recipe) {
    VBox page =
            new VBox(15);

    page.setPadding(
            new Insets(20));

    VBox.setVgrow(
            page,
            Priority.ALWAYS);

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

    dishNameField.setPromptText(
            "Enter dish name");

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

    ingredientsArea.setPromptText(
            "Enter ingredients");

    ingredientsArea.setPrefRowCount(6);

    ingredientsArea.setWrapText(
            true);

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

    recipeArea.setPromptText(
            "Enter recipe");

    recipeArea.setPrefRowCount(8);

    recipeArea.setWrapText(
            true);

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
    // EXISTING IMAGE - DISPLAY ONLY
    // =====================================================

    ImageView imageView =
            null;

    String imageUrl =
            recipe.getImageUrl();

    if (imageUrl != null
            && !imageUrl.isEmpty()) {

        imageView =
                new ImageView(
                        new Image(
                                imageUrl,
                                true));

        imageView.setFitWidth(500);

        imageView.setFitHeight(300);

        imageView.setPreserveRatio(
                true);
    }

    // =====================================================
    // BUTTONS
    // =====================================================

    HBox buttonRow =
            new HBox(10);

    buttonRow.setAlignment(
            Pos.CENTER_LEFT);

    Button updateButton =
            new Button("Update Recipe");

    updateButton.setPrefHeight(34);

    updateButton.setPadding(
            new Insets(8, 16, 8, 16));

    updateButton.setStyle(
            "-fx-background-color: #22C55E;" +
                    "-fx-text-fill: #0F172A;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;");

    Button cancelButton =
            new Button("Cancel");

    cancelButton.setPrefHeight(34);

    cancelButton.setPadding(
            new Insets(8, 16, 8, 16));

    cancelButton.setStyle(
            "-fx-background-color: #334155;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;");

    // =====================================================
    // UPDATE ACTION
    // =====================================================

    updateButton.setOnAction(event -> {

        String updatedDishName =
                dishNameField.getText().trim();

        String updatedIngredients =
                ingredientsArea.getText().trim();

        String updatedRecipe =
                recipeArea.getText().trim();

        // =================================================
        // VALIDATION
        // =================================================

        if (updatedDishName.isEmpty()
                || updatedIngredients.isEmpty()
                || updatedRecipe.isEmpty()) {

            showErrorMessage(
                    page,
                    "Please enter Dish Name, Ingredients and Recipe.");

            return;
        }

        // =================================================
        // UPDATE RECIPE
        // =================================================
        //
        // IMPORTANT:
        // Original image URL is used.
        // User cannot change the image.
        //

        recipeController.updateRecipe(
                recipe.getPostId(),
                updatedDishName,
                updatedIngredients,
                updatedRecipe,
                recipe.getImageUrl());

        // =================================================
        // GO BACK TO MY POSTS
        // =================================================

        currentContent
                .getChildren()
                .clear();

        currentContent
                .getChildren()
                .add(
                        createMyPostsPage());
    });

        // =====================================================
        // CANCEL ACTION
        // =====================================================

        cancelButton.setOnAction(event -> {

                currentContent
                        .getChildren()
                        .clear();

                currentContent
                        .getChildren()
                        .add(
                                createMyPostsPage());
        });

        buttonRow.getChildren().addAll(
                updateButton,
                cancelButton);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        page.getChildren().addAll(
                title,
                dishNameLabel,
                dishNameField,
                ingredientsLabel,
                ingredientsArea,
                recipeLabel,
                recipeArea);

        // Add original image only for viewing
        if (imageView != null) {

                page.getChildren().add(
                        imageView);
        }

        page.getChildren().add(
                buttonRow);

      
// =====================================================
// MAKE EDIT PAGE SCROLLABLE
// =====================================================

ScrollPane scroll =
        new ScrollPane(page);

scroll.setFitToWidth(true);

scroll.setPannable(true);

scroll.setHbarPolicy(
        ScrollPane.ScrollBarPolicy.NEVER);

scroll.setVbarPolicy(
        ScrollPane.ScrollBarPolicy.AS_NEEDED);

scroll.setStyle(
        "-fx-background-color: #0F172A;" +
        "-fx-background: #0F172A;");

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

    // Delete recipe from Firestore
    recipeController.deleteRecipe(
            recipe.getPostId());

    // Refresh My Posts
    currentContent
            .getChildren()
            .clear();

    currentContent
            .getChildren()
            .add(
                    createMyPostsPage());
}
}