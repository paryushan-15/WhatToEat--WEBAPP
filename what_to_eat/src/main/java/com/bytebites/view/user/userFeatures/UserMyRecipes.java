package com.bytebites.view.user.userFeatures;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import com.bytebites.view.common.SameStageAlert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.bytebites.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;

import com.bytebites.controller.RecipeController;
import com.bytebites.dao.UserDao;
import com.bytebites.model.UploadedRecipe;
import com.bytebites.model.User;
import com.bytebites.model.session.SessionManager;

public class UserMyRecipes {

    private RecipeController recipeController =
            new RecipeController();

    private UserDao userDao =
            new UserDao();

    // =====================================================
    // RECIPE CONTAINERS
    // =====================================================

    private VBox uploadedRecipesContainer =
            new VBox(15);

    private VBox savedRecipesContainer =
            new VBox(15);

    // =====================================================
    // CURRENT RECIPE CONTENT
    // =====================================================

    private VBox currentContent;

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public VBox getMyRecipes() {

        VBox main = new VBox(18);

        main.setPadding(
                new Insets(10, 25, 30, 25));

        main.setStyle(
                "-fx-background-color: #0F172A;");

        // =====================================================
        // HEADER
        // =====================================================

        HBox header = new HBox();

        header.setAlignment(
                Pos.CENTER_LEFT);

        // =====================================================
        // TITLE + SUBTITLE
        // =====================================================

        VBox titleBox = new VBox(5);

        Label title = new Label(
                "My Recipes");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;");

        Label subtitle = new Label(
                "Manage your uploaded and saved recipes");

        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 14px;");

        titleBox.getChildren().addAll(
                title,
                subtitle);

        // =====================================================
        // HEADER SPACE
        // =====================================================

        Region headerSpace = new Region();

        HBox.setHgrow(
                headerSpace,
                Priority.ALWAYS);

        header.getChildren().addAll(
                titleBox,
                headerSpace);

        // =====================================================
        // NAVIGATION BUTTONS
        // =====================================================

        HBox navigation = new HBox(10);

        navigation.setAlignment(
                Pos.CENTER_LEFT);

        Button uploadedButton =
                createNavigationButton(
                        "My Uploaded Recipes",
                        true);

        Button savedButton =
                createNavigationButton(
                        "My Saved Recipes",
                        false);

        navigation.getChildren().addAll(
                uploadedButton,
                savedButton);

        // =====================================================
        // CENTER CONTENT
        // =====================================================

        VBox centerContent = new VBox();

        currentContent =
                centerContent;

        VBox.setVgrow(
                centerContent,
                Priority.ALWAYS);

        centerContent.getChildren().add(
                createUploadedRecipesPage());

        // =====================================================
        // UPLOADED RECIPES BUTTON
        // =====================================================

        uploadedButton.setOnAction(event -> {

            centerContent
                    .getChildren()
                    .clear();

            centerContent
                    .getChildren()
                    .add(
                            createUploadedRecipesPage());

            uploadedButton.setStyle(
                    navigationButtonStyle(true));

            savedButton.setStyle(
                    navigationButtonStyle(false));
        });

        // =====================================================
        // SAVED RECIPES BUTTON
        // =====================================================

        savedButton.setOnAction(event -> {

            centerContent
                    .getChildren()
                    .clear();

            centerContent
                    .getChildren()
                    .add(
                            createSavedRecipesPage());

            uploadedButton.setStyle(
                    navigationButtonStyle(false));

            savedButton.setStyle(
                    navigationButtonStyle(true));
        });

        // =====================================================
        // ADD EVERYTHING
        // =====================================================

        main.getChildren().addAll(
                header,
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

        button.setPrefHeight(
                38);

        button.setPadding(
                new Insets(8, 18, 8, 18));

        button.setStyle(
                navigationButtonStyle(selected));

        return button;
    }

    // =====================================================
    // NAVIGATION BUTTON STYLE
    // =====================================================

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
// UPLOADED RECIPES PAGE
// =====================================================

private VBox createUploadedRecipesPage() {

    VBox page = new VBox(15);

    VBox.setVgrow(
            page,
            Priority.ALWAYS);

    // =====================================================
    // PAGE TITLE
    // =====================================================

    Label title = new Label(
            "My Uploaded Recipes");

    title.setStyle(
            "-fx-text-fill: white;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;");

    // =====================================================
    // DESCRIPTION
    // =====================================================

    Label description = new Label(
            "Recipes that you have shared on the Social Feed");

    description.setStyle(
            "-fx-text-fill: #94A3B8;" +
                    "-fx-font-size: 13px;");

    // =====================================================
    // CLEAR OLD RECIPES
    // =====================================================

    uploadedRecipesContainer
            .getChildren()
            .clear();

    // =====================================================
    // CURRENT LOGGED-IN USER UID
    // =====================================================

    String currentUid =
            SessionManager.getUid();

    boolean foundMyRecipe = false;

    // =====================================================
    // LOAD ALL UPLOADED RECIPES
    // =====================================================

    List<UploadedRecipe> uploadedRecipes =
            new ArrayList<>(
                    recipeController.getUploadedRecipes());

    // =====================================================
    // SORT LATEST FIRST
    // =====================================================

    uploadedRecipes.sort(
            Comparator.comparing(
                    this::parsePostTime)
                    .reversed());

    // =====================================================
    // SHOW CURRENT USER'S POSTS
    // =====================================================

    for (UploadedRecipe uploadedRecipe :
            uploadedRecipes) {

        if (uploadedRecipe == null) {
            continue;
        }

        String ownerUid =
                uploadedRecipe.getUsername();

        if (currentUid != null
                && currentUid.equals(ownerUid)) {

            foundMyRecipe = true;

            uploadedRecipesContainer
                    .getChildren()
                    .add(
                            createUploadedRecipeCard(
                                    uploadedRecipe));
        }
    }

    // =====================================================
    // NO RECIPES MESSAGE
    // =====================================================

    if (!foundMyRecipe) {

        Label emptyMessage =
                new Label(
                        "You haven't uploaded any recipes yet.");

        emptyMessage.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 14px;");

        uploadedRecipesContainer
                .getChildren()
                .add(
                        emptyMessage);
    }

    // =====================================================
    // SCROLL
    // =====================================================

    ScrollPane scroll =
            createScrollPane(
                    uploadedRecipesContainer);

    VBox.setVgrow(
            scroll,
            Priority.ALWAYS);

    // =====================================================
    // ADD CONTENT
    // =====================================================

    page.getChildren().addAll(
            title,
            description,
            scroll);

    return page;
}



    // =====================================================
    // UPLOADED RECIPE CARD
    // =====================================================

    // =====================================================
    // COMPACT RECIPE CARD
    // =====================================================
    //
    // Both My Uploaded Recipes and My Saved Recipes show:
    // Name, time, photo and dish name.
    // Full content opens when the photo is clicked.
    // =====================================================

   private VBox createUploadedRecipeCard(
        UploadedRecipe recipe) {

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
        avatar.setAlignment(Pos.CENTER);

        avatar.setStyle(
                "-fx-background-color: #334155;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-font-size: 17px;");

        VBox userInfo =
                new VBox(2);

        String ownerUid =
                recipe.getUsername();

        String ownerName =
                getRecipeOwnerName(ownerUid);

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            ownerName =
                    ownerName + " (You)";
        }

        HBox nameBadgeRow =
                createRecipeNameBadgeRow(
                        ownerName,
                        ownerUid);

        Label time =
                new Label(recipe.getTime());

        time.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 11px;");

        userInfo.getChildren().addAll(
                nameBadgeRow,
                time);

        userRow.getChildren().addAll(
                avatar,
                userInfo);

        card.getChildren().add(
                userRow);

        // =====================================================
        // IMAGE
        // =====================================================

        String imageUrl =
                recipe.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isEmpty()) {

            ImageView imageView =
                    new ImageView(
                            new Image(
                                    imageUrl,
                                    true));

            imageView.setFitWidth(500);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            imageView.setCursor(
                    javafx.scene.Cursor.HAND);

            imageView.setOnMouseClicked(event -> {

                showRecipeDetails(
                        recipe);
            });

            card.getChildren().add(
                    imageView);
        }

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dish =
                new Label(
                        recipe.getDishName());

        dish.setWrapText(true);

        dish.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        card.getChildren().add(
                dish);
        
         // =====================================================
        // EDIT + DELETE BUTTONS
        // =====================================================

        HBox actionRow =
                new HBox(10);

        actionRow.setAlignment(
                Pos.CENTER_RIGHT);

        // EDIT BUTTON
        Button editButton =
                new Button("✏ Edit");

        editButton.setPrefHeight(34);

        editButton.setPadding(
                new Insets(6, 14, 6, 14));

       editButton.setStyle(
        "-fx-background-color: #3B82F6;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 8 18;");
        editButton.setOnAction(event -> {

        currentContent
                .getChildren()
                .clear();

        currentContent
                .getChildren()
                .add(
                        showEditRecipePage(recipe));
        });

        // DELETE BUTTON
        Button deleteButton =
                new Button("🗑 Delete");

        deleteButton.setPrefHeight(34);

        deleteButton.setPadding(
                new Insets(6, 14, 6, 14));

        deleteButton.setStyle(
                "-fx-background-color: #7F1D1D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;");

        deleteButton.setOnAction(event -> {

        deleteRecipe(recipe);
        });

        actionRow.getChildren().addAll(
                editButton,
                deleteButton);

        card.getChildren().add(
                actionRow);      

        return card;
    }

    // =====================================================
    // SHOW FULL RECIPE DETAILS
    // =====================================================

    private void showRecipeDetails(
            UploadedRecipe recipe) {

        if (currentContent == null
                || recipe == null) {

            return;
        }

        currentContent
                .getChildren()
                .clear();

        currentContent
                .getChildren()
                .add(
                        createRecipeDetailsPage(
                                recipe));
    }

    // =====================================================
    // FULL RECIPE DETAILS PAGE
    // =====================================================

    private VBox createRecipeDetailsPage(
            UploadedRecipe recipe) {

        VBox page =
                new VBox(15);

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // BACK BUTTON
        // =====================================================

        Button backButton =
                new Button("←  Back to My Recipes");

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

            String currentUid =
                    SessionManager.getUid();

            if (currentUid != null
                    && currentUid.equals(
                            recipe.getUsername())) {

                currentContent
                        .getChildren()
                        .add(
                                createUploadedRecipesPage());

            } else {

                currentContent
                        .getChildren()
                        .add(
                                createSavedRecipesPage());
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
        // DETAILS CARD
        // =====================================================

        VBox card =
                new VBox(12);

        card.setPadding(
                new Insets(18));

        card.setStyle(
                "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 12;");

        String ownerUid =
                recipe.getUsername();

        String ownerName =
                getRecipeOwnerName(ownerUid);

        if (ownerUid != null
                && ownerUid.equals(
                        SessionManager.getUid())) {

            ownerName =
                    ownerName + " (You)";
        }

        HBox nameBadgeRow =
                createRecipeNameBadgeRow(
                        ownerName,
                        ownerUid);

        Label time =
                new Label(recipe.getTime());

        time.setStyle(
                "-fx-text-fill: #64748B;" +
                        "-fx-font-size: 11px;");

        card.getChildren().addAll(
                nameBadgeRow,
                time);

        // =====================================================
        // IMAGE
        // =====================================================

        String imageUrl =
                recipe.getImageUrl();

        if (imageUrl != null
                && !imageUrl.isEmpty()) {

            ImageView imageView =
                    new ImageView(
                            new Image(
                                    imageUrl,
                                    true));

            imageView.setFitWidth(500);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);

            card.getChildren().add(
                    imageView);
        }

        // =====================================================
        // DISH NAME
        // =====================================================

        Label dishTitle =
                new Label("Dish Name");

        dishTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label dish =
                new Label(
                        recipe.getDishName());

        dish.setWrapText(true);

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
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label ingredients =
                new Label(
                        recipe.getIngredients());

        ingredients.setWrapText(true);

        ingredients.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 15px;");

        // =====================================================
        // RECIPE
        // =====================================================

        Label recipeTitle =
                new Label("Recipe");

        recipeTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        Label recipeText =
                new Label(
                        recipe.getRecipe());

        recipeText.setWrapText(true);

        recipeText.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                        "-fx-font-size: 15px;");

        card.getChildren().addAll(
                dishTitle,
                dish,
                ingredientsTitle,
                ingredients,
                recipeTitle,
                recipeText);

        ScrollPane scroll =
                createScrollPane(
                        new VBox(card));

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        page.getChildren().addAll(
                backButton,
                title,
                scroll);

        return page;
    }

    private VBox createSavedRecipesPage() {

        VBox page = new VBox(15);

        VBox.setVgrow(
                page,
                Priority.ALWAYS);

        // =====================================================
        // PAGE TITLE
        // =====================================================

        Label title = new Label(
                "My Saved Recipes");

        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;");

        // =====================================================
        // DESCRIPTION
        // =====================================================

        Label description = new Label(
                "Recipes that you have saved from the Social Feed");

        description.setStyle(
                "-fx-text-fill: #94A3B8;" +
                        "-fx-font-size: 13px;");

        // =====================================================
        // CLEAR OLD SAVED RECIPES
        // =====================================================

        savedRecipesContainer
                .getChildren()
                .clear();

        // =====================================================
        // GET CURRENT LOGGED-IN USER UID
        // =====================================================

        String currentUid =
                SessionManager.getUid();

        boolean foundSavedRecipe = false;

        // =====================================================
        // CHECK LOGIN
        // =====================================================

        if (currentUid != null
                && !currentUid.isEmpty()) {

            // =================================================
            // LOAD SAVED RECIPES FROM FIRESTORE
            // =================================================

            List<UploadedRecipe> savedRecipes =
                    new ArrayList<>(
                            recipeController.getSavedRecipes(
                                    currentUid));

            // =================================================
            // SORT BY ORIGINAL POST TIME - LATEST FIRST
            // =================================================

            savedRecipes.sort(
                    Comparator.comparing(
                            this::parsePostTime)
                            .reversed());

            // =================================================
            // CREATE SAVED RECIPE CARDS
            // =================================================

            for (UploadedRecipe savedRecipe :
                    savedRecipes) {

                foundSavedRecipe = true;

               savedRecipesContainer
              .getChildren()
              .add(
                createSavedRecipeCard(
                        savedRecipe));
            }
        }

        // =====================================================
        // NO SAVED RECIPES MESSAGE
        // =====================================================

        if (!foundSavedRecipe) {

            Label emptyMessage =
                    new Label(
                            "You haven't saved any recipes yet.");

            emptyMessage.setStyle(
                    "-fx-text-fill: #64748B;" +
                            "-fx-font-size: 14px;");

            savedRecipesContainer
                    .getChildren()
                    .add(
                            emptyMessage);
        }

        // =====================================================
        // SCROLL
        // =====================================================

        ScrollPane scroll =
                createScrollPane(
                        savedRecipesContainer);

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS);

        // =====================================================
        // ADD CONTENT
        // =====================================================

        page.getChildren().addAll(
                title,
                description,
                scroll);

        return page;
    }

    // =====================================================
// SAVED RECIPE CARD
// =====================================================

private VBox createSavedRecipeCard(
        UploadedRecipe recipe) {

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
    avatar.setAlignment(Pos.CENTER);

    avatar.setStyle(
            "-fx-background-color: #334155;" +
                    "-fx-background-radius: 50%;" +
                    "-fx-font-size: 17px;");

    VBox userInfo =
            new VBox(2);

    // =====================================================
    // GET POST OWNER UID
    // =====================================================

    String ownerUid =
            recipe.getUsername();

    String ownerName =
            getRecipeOwnerName(ownerUid);

    // =====================================================
    // NAME + DIETITIAN BADGE
    // =====================================================

    HBox nameBadgeRow =
            createRecipeNameBadgeRow(
                    ownerName,
                    ownerUid);

    // =====================================================
    // POST TIME
    // =====================================================

    Label time =
            new Label(
                    recipe.getTime());

    time.setStyle(
            "-fx-text-fill: #64748B;" +
                    "-fx-font-size: 11px;");

    userInfo.getChildren().addAll(
            nameBadgeRow,
            time);

    userRow.getChildren().addAll(
            avatar,
            userInfo);

    card.getChildren().add(
            userRow);

    // =====================================================
    // IMAGE
    // =====================================================

    String imageUrl =
            recipe.getImageUrl();

    if (imageUrl != null
            && !imageUrl.isEmpty()) {

        ImageView imageView =
                new ImageView(
                        new Image(
                                imageUrl,
                                true));

        imageView.setFitWidth(500);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        imageView.setCursor(
                javafx.scene.Cursor.HAND);

        // Click image → Recipe Details
        imageView.setOnMouseClicked(event -> {

            showRecipeDetails(recipe);
        });

        card.getChildren().add(
                imageView);
    }

    // =====================================================
    // DISH NAME
    // =====================================================

    Label dish =
            new Label(
                    recipe.getDishName());

    dish.setWrapText(true);

    dish.setStyle(
            "-fx-text-fill: white;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;");

    card.getChildren().add(
            dish);

    // =====================================================
    // UNSAVE BUTTON
    // =====================================================

    HBox actionRow =
            new HBox();

    actionRow.setAlignment(
            Pos.CENTER_RIGHT);

    Button unsaveButton =
            new Button("🔖 Unsave");

    unsaveButton.setPrefHeight(34);

    unsaveButton.setPadding(
            new Insets(6, 14, 6, 14));

    unsaveButton.setStyle(
            "-fx-background-color: #334155;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 8;");

    unsaveButton.setOnAction(event -> {

        String currentUid =
                SessionManager.getUid();

        if (currentUid == null
                || currentUid.isEmpty()) {

            return;
        }

        // Remove saved recipe from Firestore
        recipeController.unsaveRecipeForUser(
                recipe.getPostId(),
                currentUid);

        // Remove card from current UI
        savedRecipesContainer
                .getChildren()
                .remove(card);

        // Show empty message if no saved recipes remain
        if (savedRecipesContainer
                .getChildren()
                .isEmpty()) {

            Label emptyMessage =
                    new Label(
                            "You haven't saved any recipes yet.");

            emptyMessage.setStyle(
                    "-fx-text-fill: #64748B;" +
                            "-fx-font-size: 14px;");

            savedRecipesContainer
                    .getChildren()
                    .add(
                            emptyMessage);
        }
    });

    actionRow.getChildren().add(
            unsaveButton);

    card.getChildren().add(
            actionRow);

    return card;
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
    // GET RECIPE OWNER NAME
    // =====================================================
    //
    // Dietitian name is read from:
    // dietitians/{uid}/name
    //
    // Normal user name is read from:
    // Users/{uid}/name
    // =====================================================

    private String getRecipeOwnerName(String uid) {

        if (uid == null || uid.isEmpty()) {
            return "User";
        }

        // Current logged-in user's name is already available
        // in SessionManager.
        if (uid.equals(SessionManager.getUid())
                && SessionManager.getName() != null
                && !SessionManager.getName().isEmpty()) {

            return SessionManager.getName();
        }

        try {

            // First check Dietitian collection.
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

            // Then check normal Users collection.
            User owner =
                    userDao.getUserByUid(uid);

            if (owner != null
                    && owner.getName() != null
                    && !owner.getName().isEmpty()) {

                return owner.getName();
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to get recipe owner name for UID: "
                            + uid);

            e.printStackTrace();
        }

        return "User";
    }

    // =====================================================
    // CREATE NAME + VERIFIED DIETITIAN BADGE
    // =====================================================

    private HBox createRecipeNameBadgeRow(
            String ownerName,
            String ownerUid) {

        HBox row =
                new HBox(5);

        row.setAlignment(
                Pos.CENTER_LEFT);

        Label username =
                new Label(ownerName);

        username.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;");

        row.getChildren().add(
                username);

        // Show badge only for Dietitians.
        if (isDietitian(ownerUid)) {

            try {

                java.io.InputStream badgeStream =
                        getClass().getResourceAsStream(
                                "/assets/icon/badge.png");

                if (badgeStream != null) {

                    Image badgeImage =
                            new Image(badgeStream);

                    ImageView badge =
                            new ImageView(badgeImage);

                    badge.setFitWidth(24);
                    badge.setFitHeight(24);
                    badge.setPreserveRatio(true);

                    row.getChildren().add(
                            badge);

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

        return row;
    }

    // =====================================================
    // CHECK WHETHER OWNER IS A DIETITIAN
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
    // COMMON SCROLL PANE
    // =====================================================

    private ScrollPane createScrollPane(
            VBox content) {

        ScrollPane scroll = new ScrollPane();

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

    page.setStyle(
            "-fx-background-color: #0F172A;");

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
                    "-fx-font-size: 24px;" +
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

    ingredientsArea.setWrapText(true);

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

    recipeArea.setWrapText(true);

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
    // DISPLAY ONLY
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
        imageView.setPreserveRatio(true);
    }

    // =====================================================
    // BUTTON ROW
    // =====================================================

    HBox buttonRow =
            new HBox(10);

    buttonRow.setAlignment(
            Pos.CENTER_RIGHT);

    // =====================================================
    // UPDATE BUTTON
    // =====================================================

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

    // =====================================================
    // CANCEL BUTTON
    // =====================================================

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

        String newDishName =
                dishNameField.getText().trim();

        String newIngredients =
                ingredientsArea.getText().trim();

        String newRecipe =
                recipeArea.getText().trim();

        // -------------------------------------------------
        // VALIDATION
        // -------------------------------------------------

        if (newDishName.isEmpty()
                || newIngredients.isEmpty()
                || newRecipe.isEmpty()) {

            SameStageAlert alert =
                    new SameStageAlert(
                            SameStageAlert.AlertType.WARNING);

            alert.setTitle(
                    "Missing Information");

            alert.setHeaderText(
                    "Please fill all fields.");

            alert.setContentText(
                    "Dish Name, Ingredients and Recipe are required.");

            alert.showAndWait();

            return;
        }

        // -------------------------------------------------
        // UPDATE FIRESTORE
        // -------------------------------------------------

        recipeController.updateRecipe(
                recipe.getPostId(),
                newDishName,
                newIngredients,
                newRecipe,
                recipe.getImageUrl());

        // -------------------------------------------------
        // UPDATE LOCAL OBJECT
        // -------------------------------------------------

        recipe.setDishName(
                newDishName);

        recipe.setIngredients(
                newIngredients);

        recipe.setRecipe(
                newRecipe);

        // -------------------------------------------------
        // GO BACK TO MY UPLOADED RECIPES
        // -------------------------------------------------

        currentContent
                .getChildren()
                .clear();

        currentContent
                .getChildren()
                .add(
                        createUploadedRecipesPage());
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
                        createUploadedRecipesPage());
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

    // =====================================================
    // SHOW EXISTING IMAGE
    // =====================================================

    if (imageView != null) {

        page.getChildren().add(
                imageView);
    }

    // =====================================================
    // ADD BUTTONS ONCE
    // =====================================================

    page.getChildren().add(
            buttonRow);

    // =====================================================
    // MAKE PAGE SCROLLABLE
    // =====================================================

    ScrollPane scrollPane =
            new ScrollPane(page);

    scrollPane.setFitToWidth(
            true);

    scrollPane.setPannable(
            true);

    scrollPane.setHbarPolicy(
            ScrollPane.ScrollBarPolicy.NEVER);

    scrollPane.setVbarPolicy(
            ScrollPane.ScrollBarPolicy.AS_NEEDED);

    scrollPane.setStyle(
            "-fx-background-color: #0F172A;" +
                    "-fx-background: #0F172A;");

    return scrollPane;
}


// =====================================================
// DELETE RECIPE
// =====================================================

private void deleteRecipe(
        UploadedRecipe recipe) {

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

                    // Refresh My Uploaded Recipes
                    if (currentContent != null) {

                        currentContent
                                .getChildren()
                                .clear();

                        currentContent
                                .getChildren()
                                .add(
                                        createUploadedRecipesPage());
                    }
                }
            });
}
}