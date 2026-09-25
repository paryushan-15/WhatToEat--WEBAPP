package com.bytebites.view.user.userFeatures;

import com.bytebites.controller.CookNowController;
import com.bytebites.dao.PantryGroceryDao;
import com.bytebites.model.session.SessionManager;
import com.google.gson.JsonObject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ListView;
import java.io.InputStream;
import java.util.List;

public class UserDashBoard {

        private BorderPane dashboardPane;
        private ListView<String> listView1;
        private CookNowController cookNowController;
        private String todayMeal;
        private String currentMealType;

        private final PantryGroceryDao pantryGroceryDao = new PantryGroceryDao();

        public UserDashBoard(
                        BorderPane dashboardPane,
                        ListView<String> listView1) {

                this.dashboardPane = dashboardPane;
                this.listView1 = listView1;

                cookNowController = new CookNowController();
        }

        public ScrollPane getDashboard() {

                // =====================================================
                // MAIN DASHBOARD CONTENT
                // =====================================================

                VBox dashboardBox = new VBox(20);

                dashboardBox.setPadding(
                                new Insets(10, 17, 25, 17));

                dashboardBox.setMinWidth(0);

                dashboardBox.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // GOOD MORNING HEADER
                // =====================================================

                VBox greetingBox = new VBox(4);

                greetingBox.setPadding(
                                new Insets(5, 0, 5, 0));

                String greetingText = getGreeting();

                Label greeting = new Label(
                                greetingText);

                greeting.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 24px;" +
                                                "-fx-font-weight: bold;");

                Label greetingSubtitle = new Label(
                                "Here's your family's meal plan for today.");

                greetingSubtitle.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                greetingBox.getChildren().addAll(
                                greeting,
                                greetingSubtitle);

                // =====================================================
                // GET TODAY'S MEAL FROM WEEKLY MEAL PLAN
                // =====================================================

                String userId = SessionManager.getUid();

                JsonObject todaysMeals = null;

                if (userId != null &&
                                !userId.isBlank()) {

                        todaysMeals = cookNowController
                                        .getTodaysMeals(userId);
                        System.out.println(todaysMeals);
                }

                if (todaysMeals != null &&
                                !todaysMeals.has("error")) {

                        currentMealType = getCurrentMealType();

                        todayMeal = getMealForCurrentTime(
                                        todaysMeals,
                                        currentMealType);

                        System.out.println(
                                        "Calories = "
                                                        + todaysMeals.get("calories").getAsString());

                        System.out.println(
                                        "Protein = "
                                                        + todaysMeals.get("protein").getAsString());

                        System.out.println(
                                        "Carbs = "
                                                        + todaysMeals.get("carbs").getAsString());

                } else {

                        currentMealType = "Meal";
                        todayMeal = "No meal planned";
                }
                // =====================================================
                // MAIN TOP AREA
                // =====================================================

                HBox mainArea = new HBox(25);

                mainArea.setAlignment(
                                Pos.TOP_LEFT);

                // =====================================================
                // LEFT SIDE
                // RECIPE + NUTRITION TIP
                // =====================================================

                VBox leftSide = new VBox(25);

                leftSide.setPrefWidth(820);
                leftSide.setMinWidth(820);
                leftSide.setMaxWidth(820);

                // =====================================================
                // RECIPE CARD
                // =====================================================

                VBox recipeCard = new VBox();

                recipeCard.setPrefWidth(820);
                recipeCard.setMinWidth(820);
                recipeCard.setMaxWidth(820);

                recipeCard.setPrefHeight(500);
                recipeCard.setMinHeight(500);
                recipeCard.setMaxHeight(500);

                recipeCard.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 12;");

                // =====================================================
                // FOOD IMAGE
                // =====================================================

                StackPane foodImage = new StackPane();

                foodImage.setPrefWidth(368);
                foodImage.setMinWidth(368);
                foodImage.setMaxWidth(368);

                foodImage.setPrefHeight(500);
                foodImage.setMinHeight(500);
                foodImage.setMaxHeight(500);

                foodImage.setStyle(
                                "-fx-background-color: #334155;" +
                                                "-fx-background-radius: 12 0 0 12;");

                ImageView mealImage = new ImageView();

                String imagePath;

                switch (currentMealType.toLowerCase()) {

                        case "breakfast":
                                imagePath = "/assets/images/breakfast.png";
                                break;

                        case "lunch":
                                imagePath = "/assets/images/lunch.png";
                                break;

                        case "dinner":
                                imagePath = "/assets/images/dinner.png";
                                break;

                        default:
                                imagePath = "/assets/images/breakfast.png";
                }

                InputStream imageStream = getClass().getResourceAsStream(imagePath);

                if (imageStream != null) {
                        mealImage.setImage(new Image(imageStream));
                } else {
                        System.err.println("Warning: Image resource not found: " + imagePath);
                }
                mealImage.setFitWidth(368);
                mealImage.setFitHeight(500);
                // mealImage.setPreserveRatio(true);

                foodImage.getChildren().add(mealImage);
                // =====================================================
                // RECIPE INFORMATION
                // =====================================================

                VBox recipeInfo = new VBox(15);

                recipeInfo.setPadding(
                                new Insets(20, 20, 20, 30));

                HBox.setHgrow(
                                recipeInfo,
                                Priority.ALWAYS);

                // =====================================================
                // AI RECOMMENDATION
                // =====================================================

                Label aiRecommendation = new Label(
                                "✦ AI Recommendation");

                aiRecommendation.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 15;" +
                                                "-fx-padding: 6 14 6 14;");

                // =====================================================
                // RECIPE NAME
                // =====================================================

                Label recipeName = new Label(
                                formatMealName(todayMeal));
                recipeName.setWrapText(true);

                recipeName.setMaxWidth(350);

                recipeName.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 23px;" +
                                                "-fx-font-weight: bold;");

                HBox recipeTitle = new HBox(15);

                recipeTitle.setAlignment(
                                Pos.TOP_LEFT);

                recipeTitle.getChildren().addAll(
                                recipeName);

                // =====================================================
                // DETAILS
                // =====================================================

                Label details = new Label(
                                "🍽 " + currentMealType
                                                + " • From today's meal plan");

                details.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                // =====================================================
                // NUTRITION SUMMARY
                // =====================================================

                Label nutritionTitle = new Label(
                                "NUTRITION SUMMARY");

                nutritionTitle.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                String calories = "--";
                String protein = "--";
                String carbs = "--";

                if (todaysMeals != null) {

                        calories = todaysMeals.has("calories")
                                        ? todaysMeals.get("calories").getAsString()
                                        : "--";

                        protein = todaysMeals.has("protein")
                                        ? todaysMeals.get("protein").getAsString()
                                        : "--";

                        carbs = todaysMeals.has("carbs")
                                        ? todaysMeals.get("carbs").getAsString()
                                        : "--";
                }
                HBox nutrition = new HBox(30);

                nutrition.getChildren().addAll(

                                nutritionItem(
                                                calories,
                                                "CALORIES"),

                                nutritionItem(
                                                protein,
                                                "PROTEIN"),

                                nutritionItem(
                                                carbs,
                                                "CARBS"));

                // =====================================================
                // COOK NOW BUTTON
                // =====================================================

                Button cookNow = new Button(
                                "Cook Now");

                cookNow.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-padding: 12 22 12 22;");

                cookNow.setOnAction(event -> {

                        UserWhatToCookToday mealDetails = new UserWhatToCookToday();

                        dashboardPane.setCenter(
                                        mealDetails.getMealDetails());

                        listView1.getSelectionModel().select(
                                        "🍴  What To Cook Today");
                });

                // =====================================================
                // FIT TO FAMILY BUTTON
                // =====================================================

                HBox buttons = new HBox(10);

                buttons.getChildren().addAll(
                                cookNow);

                // =====================================================
                // ADD RECIPE INFORMATION
                // =====================================================

                recipeInfo.getChildren().addAll(

                                aiRecommendation,

                                recipeTitle,

                                details,

                                nutritionTitle,

                                nutrition,

                                buttons);

                // =====================================================
                // RECIPE CONTENT
                // =====================================================

                HBox recipeContent = new HBox();

                recipeContent.setPrefHeight(500);
                recipeContent.setMinHeight(500);

                recipeContent.getChildren().addAll(
                                foodImage,
                                recipeInfo);

                recipeCard.getChildren().add(
                                recipeContent);

                // AI NUTRITION TIP

                VBox nutritionTip = new VBox(12);

                nutritionTip.setPrefWidth(1195);
                nutritionTip.setMinWidth(1195);
                nutritionTip.setMaxWidth(1195);

                nutritionTip.setPrefHeight(130);
                nutritionTip.setMinHeight(130);
                nutritionTip.setMaxHeight(130);

                nutritionTip.setPadding(
        new Insets(18, 25, 18, 25));

                nutritionTip.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 12;" +
                                                "-fx-border-color: #A855F7;" +
                                                "-fx-border-radius: 12;");

                Label tipTitle = new Label(
                                "💡  AI Nutrition Tip");

                tipTitle.setStyle(
                                "-fx-text-fill: #C084FC;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                String nutritionTipText = cookNowController.getNutritionTip(todaysMeals);
                Label tipText = new Label(
                                nutritionTipText);

                tipText.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;");

                Label explore = new Label(
                                "Explore recipes →");

                explore.setStyle(
                                "-fx-text-fill: #C084FC;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-cursor: hand;");
                explore.setOnMouseClicked(event -> {

                        UserWhatToCookToday mealDetails = new UserWhatToCookToday();

                        dashboardPane.setCenter(
                                        mealDetails.getMealDetails());

                        listView1.getSelectionModel().select(
                                        "🍴  What To Cook Today");
                });

                nutritionTip.getChildren().addAll(

                                tipTitle,

                                tipText,

                                explore);

                // =====================================================
                // ADD RECIPE + NUTRITION TO LEFT SIDE
                // =====================================================

                leftSide.getChildren().addAll(

                                recipeCard);

                // =====================================================
                // RIGHT SIDE
                // =====================================================

                VBox rightSide = new VBox(20);

                rightSide.setPrefWidth(350);
                rightSide.setMinWidth(350);
                rightSide.setMaxWidth(350);

                // =====================================================
                // 1. PLANNER CARD
                // =====================================================

                VBox planner = createCard();

                planner.setPrefWidth(350);
                planner.setMinWidth(350);
                planner.setMaxWidth(350);

                planner.setPrefHeight(213);
                planner.setMinHeight(213);
                planner.setMaxHeight(213);

                Label plannerTitle = createTitle(
                                "This Week Progress");

                System.out.println("Day = " +
                                java.time.LocalDate.now().getDayOfWeek());

                System.out.println("Completed Days = " +
                                java.time.LocalDate.now().getDayOfWeek().getValue());

                int completedDays = java.time.LocalDate.now()
                                .getDayOfWeek()
                                .getValue();

                Label plannerText = createSmallText(
                                completedDays + " of 7 days completed");

                // Track
                double totalWidth = 280;

                Pane progress = new Pane();
                progress.setPrefSize(totalWidth, 8);

                Region progressTrack = new Region();
                progressTrack.setPrefSize(totalWidth, 8);
                progressTrack.setStyle(
                                "-fx-background-color: #334155;" +
                                                "-fx-background-radius: 10;");

                Region progressFill = new Region();
                progressFill.setPrefSize(
                                totalWidth * completedDays / 7.0,
                                8);
                progressFill.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-background-radius: 10;");

                progress.getChildren().addAll(
                                progressTrack,
                                progressFill);

                Button viewPlan = new Button(
                                "View Full Plan");

                viewPlan.setMaxWidth(
                                Double.MAX_VALUE);

                viewPlan.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #4ADE80;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 6;" +
                                                "-fx-padding: 10;");

                viewPlan.setOnAction(event -> {

                        UserWeeklyMealPlanner weeklyPlanner = new UserWeeklyMealPlanner();

                        dashboardPane.setCenter(
                                        weeklyPlanner.getWeeklyPlanner());

                        listView1.getSelectionModel().select(
                                        "▦   Weekly Meal Planner");
                });

                planner.getChildren().addAll(

                                plannerTitle,

                                plannerText,

                                progress,

                                viewPlan);

                // =====================================================
                // 2. GROCERY CARD
                // =====================================================

                VBox grocery = createCard();

                grocery.setPrefWidth(350);
                grocery.setMinWidth(350);
                grocery.setMaxWidth(350);

                grocery.setPrefHeight(115);
                grocery.setMinHeight(115);
                grocery.setMaxHeight(115);

                Label groceryTitle = createSmallText(
                                "GROCERY LIST");

                String uid = SessionManager.getUid();

                int groceryItemCount = 0;

                if (uid != null && !uid.trim().isEmpty()) {
                        groceryItemCount = pantryGroceryDao.getGroceryItemCount(uid);
                }

                Label groceryCount = new Label(
                                groceryItemCount +
                                                (groceryItemCount == 1
                                                                ? " Item"
                                                                : " Items"));

                groceryCount.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;");

                grocery.getChildren().addAll(

                                groceryTitle,

                                groceryCount);

                // =====================================================
                // 4. QUICK ACTIONS
                // =====================================================

                VBox quickActions = createCard();

                quickActions.setPrefWidth(350);
                quickActions.setMinWidth(350);
                quickActions.setMaxWidth(350);

                quickActions.setPrefHeight(132);
                quickActions.setMinHeight(132);
                quickActions.setMaxHeight(132);

                quickActions.setSpacing(4);

                quickActions.setPadding(
                                new Insets(14, 18, 12, 18));

                Label quickTitle = createSmallText(
                                "QUICK ACTIONS");

                Button generatePlan = new Button(
                                "⟳  Generate Weekly Plan");

                generatePlan.setMaxWidth(
                                Double.MAX_VALUE);

                generatePlan.setAlignment(
                                Pos.CENTER_LEFT);

                generatePlan.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-padding: 10 8 10 8;");
                generatePlan.setOnAction(event -> {

                        UserWeeklyMealPlanner mealPlanner = new UserWeeklyMealPlanner();

                        dashboardPane.setCenter(
                                        mealPlanner.getWeeklyPlanner());

                        listView1.getSelectionModel()
                                        .select("▦   Weekly Meal Planner");
                });

                Button addPantry = new Button(
                                "＋  Add Pantry Items");

                addPantry.setMaxWidth(
                                Double.MAX_VALUE);

                addPantry.setAlignment(
                                Pos.CENTER_LEFT);

                addPantry.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-padding: 10 8 10 8;");
                addPantry.setOnAction(event -> {

                        UserPantryGrocery pantryPage = new UserPantryGrocery();

                        dashboardPane.setCenter(
                                        pantryPage.getPantryGrocery());

                        listView1.getSelectionModel()
                                        .select("▣   Pantry");
                });

                quickActions.getChildren().addAll(

                                quickTitle,

                                generatePlan,

                                addPantry);

                // =====================================================
                // ADD RIGHT SIDE CARDS
                // =====================================================

                rightSide.getChildren().addAll(

                                planner,

                                grocery,

                                quickActions);

                // =====================================================
                // ADD LEFT + RIGHT
                // =====================================================

                mainArea.getChildren().addAll(

                                leftSide,

                                rightSide);

                // =====================================================
                // FAVORITE RECIPES
                // =====================================================

                HBox favoriteRecipes = new HBox(15);

                favoriteRecipes.getChildren().addAll(

                                recipeSmallCard(
                                                "🥗",
                                                "Quinoa Buddha Bowl",
                                                "25 min • Easy",
                                                "92% Match"),

                                recipeSmallCard(
                                                "🍝",
                                                "Creamy Carbonara",
                                                "20 min • Easy",
                                                "85% Match"),

                                recipeSmallCard(
                                                "🐟",
                                                "Grilled Salmon",
                                                "30 min • Med",
                                                "96% Match"),

                                recipeSmallCard(
                                                "🍲",
                                                "Tofu Special",
                                                "15 min • Easy",
                                                "90% Match"),

                                recipeSmallCard(
                                                "🥩",
                                                "Grilled Steak",
                                                "35 min • Med",
                                                "94% Match"),

                                recipeSmallCard(
                                                "🥑",
                                                "Avocado Salad",
                                                "15 min • Easy",
                                                "91% Match"));

                // =====================================================
                // RECENTLY COOKED
                // =====================================================

                Label recentlyTitle = new Label(
                                "Recently Cooked");

                recentlyTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;");

                List<JsonObject> recentMeals = cookNowController.getRecentMeals(
                                SessionManager.getUid(),
                                5);

                HBox recentlyCooked = new HBox(15);

                if (recentMeals.isEmpty()) {

                        recentlyCooked.getChildren().add(

                                        recentCard(
                                                        "🍽",
                                                        "No Recent Meals",
                                                        "Generate a meal plan"));
                } else {

                        for (JsonObject meal : recentMeals) {

                                recentlyCooked.getChildren().add(

                                                recentCard(
                                                                "🍽",
                                                                meal.get("meal")
                                                                                .getAsString(),

                                                                meal.get("label")
                                                                                .getAsString()));
                        }
                }

                // =====================================================
                // RECENTLY COOKED HORIZONTAL SCROLL
                // ONLY RECENTLY COOKED SCROLL
                // =====================================================

                ScrollPane recentlyScroll = new ScrollPane();

                recentlyScroll.setContent(
                                recentlyCooked);

                // IMPORTANT:
                // ScrollPane takes only available dashboard width.

                recentlyScroll.setMinWidth(0);

                recentlyScroll.setMaxWidth(
                                Double.MAX_VALUE);

                recentlyScroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                recentlyScroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                recentlyScroll.setFitToHeight(
                                true);

                // IMPORTANT:
                // false = cards keep their original width.

                recentlyScroll.setFitToWidth(
                                false);

                recentlyScroll.setPannable(
                                true);

                recentlyScroll.setPrefHeight(
                                140);

                recentlyScroll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: #0F172A;");

                // =====================================================
                // ADD EVERYTHING TO DASHBOARD
                // =====================================================

                dashboardBox.getChildren().addAll(

                                // GOOD MORNING HEADER
                                greetingBox,

                                // MAIN DASHBOARD
                                mainArea,
                                nutritionTip,

                                // RECENTLY COOKED
                                recentlyTitle,

                                recentlyScroll);

                // =====================================================
                // MAIN VERTICAL SCROLL
                // =====================================================

                ScrollPane dashboardScroll = new ScrollPane();

                dashboardScroll.setContent(
                                dashboardBox);

                dashboardScroll.setFitToWidth(
                                true);

                // =====================================================
                // IMPORTANT
                // OUTER DASHBOARD MUST NOT HORIZONTALLY PAN
                // =====================================================

                dashboardScroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                dashboardScroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                // VERY IMPORTANT:
                // false prevents the whole dashboard from
                // moving horizontally when dragging.

                dashboardScroll.setPannable(
                                false);

                dashboardScroll.setStyle(
                                "-fx-background-color: #0F172A;" +
                                                "-fx-background: #0F172A;");

                return dashboardScroll;
        }

        // =====================================================
        // NUTRITION ITEM
        // =====================================================

        private VBox nutritionItem(
                        String value,
                        String name) {

                VBox box = new VBox(3);

                Label valueLabel = new Label(
                                value);

                valueLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                Label nameLabel = new Label(
                                name);

                nameLabel.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 10px;");

                box.getChildren().addAll(

                                valueLabel,

                                nameLabel);

                return box;
        }

        // =====================================================
        // COMMON CARD
        // =====================================================

        private VBox createCard() {

                VBox card = new VBox(10);

                card.setPadding(
                                new Insets(18));

                card.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 12;");

                return card;
        }

        // =====================================================
        // TITLE
        // =====================================================

        private Label createTitle(
                        String text) {

                Label label = new Label(
                                text);

                label.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                return label;
        }

        // =====================================================
        // SMALL TEXT
        // =====================================================

        private Label createSmallText(
                        String text) {

                Label label = new Label(
                                text);

                label.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                return label;
        }

        // =====================================================
        // FAVORITE RECIPE CARD
        // =====================================================

        private VBox recipeSmallCard(

                        String emoji,

                        String name,

                        String time,

                        String match

        ) {

                VBox card = new VBox(8);

                card.setPrefWidth(
                                190);

                card.setMinWidth(
                                190);

                card.setPadding(
                                new Insets(10));

                card.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 10;");

                // =====================================================
                // IMAGE
                // =====================================================

                StackPane image = new StackPane();

                image.setPrefHeight(
                                100);

                image.setMinHeight(
                                100);

                image.setStyle(
                                "-fx-background-color: #334155;" +
                                                "-fx-background-radius: 8;");

                Label food = new Label(
                                emoji);

                food.setStyle(
                                "-fx-font-size: 50px;");

                image.getChildren().add(
                                food);

                // =====================================================
                // RECIPE NAME
                // =====================================================

                Label recipeName = new Label(
                                name);

                recipeName.setWrapText(
                                true);

                recipeName.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // TIME
                // =====================================================

                Label recipeTime = new Label(
                                time);

                recipeTime.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 12px;");

                // =====================================================
                // MATCH
                // =====================================================

                Label recipeMatch = new Label(
                                match);

                recipeMatch.setStyle(
                                "-fx-text-fill: #4ADE80;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                card.getChildren().addAll(

                                image,

                                recipeName,

                                recipeTime,

                                recipeMatch);

                return card;
        }

        // =====================================================
        // RECENT CARD
        // =====================================================

        private VBox recentCard(

                        String emoji,

                        String name,

                        String time

        ) {

                VBox card = new VBox(5);

                card.setPrefWidth(
                                200);

                card.setMinWidth(
                                200);

                card.setPadding(
                                new Insets(12));

                card.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 10;");

                // =====================================================
                // FOOD
                // =====================================================

                Label food = new Label(
                                emoji);

                food.setStyle(
                                "-fx-font-size: 30px;");

                // =====================================================
                // TITLE
                // =====================================================

                Label title = new Label(
                                name);

                title.setWrapText(
                                true);

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // TIME
                // =====================================================

                Label cooked = new Label(
                                time);

                cooked.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 11px;");

                card.getChildren().addAll(

                                food,

                                title,

                                cooked);

                return card;
        }

        private String getGreeting() {

                int hour = java.time.LocalTime.now()
                                .getHour();

                if (hour >= 5 && hour < 12) {

                        return "Good morning!";

                } else if (hour >= 12 && hour < 17) {

                        return "Good afternoon!";

                } else {

                        return "Good evening!";
                }
        }

        private String getCurrentMealType() {

                int hour = java.time.LocalTime.now()
                                .getHour();

                if (hour >= 5 && hour < 12) {

                        return "Breakfast";

                } else if (hour >= 12 && hour < 17) {

                        return "Lunch";

                } else {

                        return "Dinner";
                }
        }

        private String getMealForCurrentTime(
                        JsonObject todaysMeals,
                        String mealType) {

                String key = mealType.toLowerCase();

                if (todaysMeals.has(key)
                                && !todaysMeals.get(key).isJsonNull()) {

                        return todaysMeals
                                        .get(key)
                                        .getAsString();
                }

                return "No meal planned";
        }

        private String formatMealName(
                        String meal) {

                if (meal == null ||
                                meal.isBlank()) {

                        return "No meal planned";
                }

                return meal;
        }
}