package com.bytebites.view.user.userFeatures;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.bytebites.controller.AIController;
import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.DietitianMealPlanDao;
import com.bytebites.model.DietitianMealPlan;
import com.bytebites.model.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UserWeeklyMealPlanner {

        private VBox weekPlanner;
        private Button generatePlanTop;
        private HBox weekHeader;
        private HBox plannerArea;
        private Label weeklyInsightLabel;
        private AIController aiController = new AIController();
        private DietitianMealPlanDao dietitianMealPlanDao = new DietitianMealPlanDao();
        private DietitianDao dietitianDao = new DietitianDao();

        public ScrollPane getWeeklyPlanner() {

                // =====================================================
                // MAIN CONTENT
                // =====================================================

                VBox content = new VBox(20);

                content.setPadding(
                                new Insets(25, 28, 90, 28));

                content.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // HEADER
                // =====================================================

                HBox header = new HBox();

                header.setAlignment(
                                Pos.CENTER_LEFT);

                VBox titleBox = new VBox(4);

                Label title = new Label(
                                "Weekly Meal Planner");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 30px;" +
                                                "-fx-font-weight: bold;");

                Label subtitle = new Label(
                                "Plan your family's meals for the week");

                subtitle.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 14px;");

                titleBox.getChildren().addAll(
                                title,
                                subtitle);

                Region headerSpace = new Region();

                HBox.setHgrow(
                                headerSpace,
                                Priority.ALWAYS);

                generatePlanTop = new Button(
                                "✦  Generate Plan");

                String userId = SessionManager.getUid();

                generatePlanTop = new Button("✦ Generate Plan");

                generatePlanTop.setPrefHeight(42);

                if (dietitianMealPlanDao.hasDietitianMealPlan(userId)) {

                        generatePlanTop.setDisable(true);

                        generatePlanTop.setText("Dietitian Plan Active");

                        generatePlanTop.setStyle(
                                        "-fx-background-color: #64748B;" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-padding: 10 20 10 20;");

                } else {

                        generatePlanTop.setStyle(
                                        "-fx-background-color: #22C55E;" +
                                                        "-fx-text-fill: #0F172A;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-radius: 8;" +
                                                        "-fx-padding: 10 20 10 20;");
                }
                generatePlanTop.setOnAction(e -> {

                        generatePlanTop.setDisable(true);
                        generatePlanTop.setText("Generating...");

                        new Thread(() -> {

                                try {

                                        // Check if dietitian meal plan exists
                                        System.out.println(
                                                        "Has Dietitian Meal Plan = "
                                                                        + dietitianMealPlanDao
                                                                                        .hasDietitianMealPlan(userId));

                                        // AI fallback
                                        String mealPlan =
                                                aiController.generateWeeklyMealPlan(userId);

                                        String nutritionInsight = null;

                                        if (mealPlan != null && !mealPlan.isBlank()) {

                                        // generateWeeklyMealPlan already generates and saves the
                                        // nutrition insight, so first load that saved value.
                                        nutritionInsight =
                                                aiController.getSavedInsight(userId);

                                        // Fallback if Firestore does not return the saved insight.
                                        if (nutritionInsight == null ||
                                                nutritionInsight.isBlank()) {

                                                nutritionInsight =
                                                        aiController.generateWeeklyInsight(
                                                                mealPlan
                                                        );
                                        }
                                        }

                                        final String finalNutritionInsight =
                                                nutritionInsight;

                                        Platform.runLater(() -> {

                                        if (mealPlan != null &&
                                                !mealPlan.isBlank()) {

                                                updateMealPlanUI(mealPlan);

                                                if (weeklyInsightLabel != null) {

                                                if (finalNutritionInsight != null &&
                                                        !finalNutritionInsight.isBlank()) {

                                                        weeklyInsightLabel.setText(
                                                                finalNutritionInsight
                                                        );

                                                } else {

                                                        weeklyInsightLabel.setText(
                                                                "Your weekly plan has been generated. " +
                                                                "Include balanced protein, carbohydrates " +
                                                                "and vegetables throughout the week."
                                                        );
                                                }
                                                }

                                        } else {

                                                System.out.println(
                                                        "Meal plan was not generated."
                                                );
                                        }

                                        generatePlanTop.setDisable(false);
                                        generatePlanTop.setText("✦ Generate Plan");
                                        });

                                } catch (Exception ex) {

                                        ex.printStackTrace();

                                        Platform.runLater(() -> {

                                                generatePlanTop.setDisable(false);
                                                generatePlanTop.setText("✦ Generate Plan");
                                        });
                                }

                        }).start();
                });
                header.getChildren().addAll(
                                titleBox,
                                headerSpace,
                                generatePlanTop);

                // =====================================================
                // WEEK PLANNER
                // =====================================================

                weekPlanner = new VBox(15);

                // =====================================================
                // WEEK HEADER
                // =====================================================

                weekHeader = new HBox(10);

                weekHeader.setAlignment(
                                Pos.CENTER_LEFT);

                LocalDate today = LocalDate.now();

                LocalDate monday = today.with(DayOfWeek.MONDAY);

                String[] days = {
                                "MON", "TUE", "WED",
                                "THU", "FRI", "SAT", "SUN"
                };

                for (int i = 0; i < 7; i++) {

                        LocalDate date = monday.plusDays(i);

                        weekHeader.getChildren().add(

                                        createDayHeader(
                                                        days[i],
                                                        String.valueOf(
                                                                        date.getDayOfMonth())));
                }

                // =====================================================
                // MEAL COLUMNS
                // =====================================================

                plannerArea = new HBox(10);

                plannerArea.setAlignment(
                                Pos.TOP_LEFT);

                plannerArea.getChildren().addAll(

                                createDayColumn(
                                                "MONDAY",
                                                "Lemon Herb Chicken",
                                                "Greek Salad",
                                                "Rice & Vegetables"),

                                createDayColumn(
                                                "TUESDAY",
                                                "Oatmeal Bowl",
                                                "Grilled Chicken Wrap",
                                                "Pasta Primavera"),

                                createDayColumn(
                                                "WEDNESDAY",
                                                "Avocado Toast",
                                                "Quinoa Buddha Bowl",
                                                "Chicken Curry"),

                                createDayColumn(
                                                "THURSDAY",
                                                "Berry Pancakes",
                                                "Grilled Salmon",
                                                "Vegetable Soup"),

                                createDayColumn(
                                                "FRIDAY",
                                                "Scrambled Eggs",
                                                "Turkey Sandwich",
                                                "Beef Stir Fry"),

                                createDayColumn(
                                                "SATURDAY",
                                                "French Toast",
                                                "Chicken Salad",
                                                "Homemade Pizza"),

                                createDayColumn(
                                                "SUNDAY",
                                                "Pancakes",
                                                "Roasted Chicken",
                                                "Creamy Pasta"));

                weekPlanner.getChildren().addAll(

                                weekHeader,

                                plannerArea);

                // =====================================================
                // NUTRITION SUMMARY
                // =====================================================

                VBox nutritionSummary = createNutritionSummary();

                nutritionSummary.setPrefWidth(700);
                nutritionSummary.setMinWidth(700);
                nutritionSummary.setMaxWidth(700);

                // Load saved meal plan from Firebase

                if (dietitianMealPlanDao.hasDietitianMealPlan(userId)) {

                        DietitianMealPlan plan = dietitianMealPlanDao.getDietitianMealPlan(userId);

                        if (plan != null) {

                                updateMealPlanUI(
                                                plan.getMealPlanJson());

                                if (weeklyInsightLabel != null) {

                                        weeklyInsightLabel.setText(
                                                        plan.getInsight());
                                }
                        }

                } else {

                        String savedMealPlan = aiController.getSavedMealPlan(userId);

                        if (savedMealPlan != null &&
                                        !savedMealPlan.isEmpty()) {

                                updateMealPlanUI(savedMealPlan);

                                String insight = aiController.getSavedInsight(userId);

                                if (weeklyInsightLabel != null &&
                                                insight != null) {

                                        weeklyInsightLabel.setText(insight);
                                }
                        }
                }
                // =====================================================
                // SUMMARY AREA
                // NUTRITION + GROCERY SIDE BY SIDE
                // =====================================================

                HBox summaryArea = new HBox(18);

                summaryArea.setAlignment(
                                Pos.TOP_LEFT);

                summaryArea.getChildren().addAll(

                                nutritionSummary);

                // =====================================================
                // ADD EVERYTHING
                // =====================================================

                content.getChildren().addAll(

                                header,

                                weekPlanner,

                                summaryArea);

                // =====================================================
                // MAIN SCROLL PANE
                // =====================================================

                ScrollPane scrollPane = new ScrollPane();

                scrollPane.setContent(
                                content);

                scrollPane.setFitToWidth(
                                true);

                // =====================================================
                // HORIZONTAL SCROLL DISABLED
                // =====================================================

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                // =====================================================
                // VERTICAL SCROLL ENABLED
                // =====================================================

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setPannable(
                                true);

                scrollPane.setStyle(
                                "-fx-background-color: #0F172A;" +
                                                "-fx-background: #0F172A;");

                return scrollPane;
        }

        // =====================================================
        // DAY HEADER
        // =====================================================

        private VBox createDayHeader(

                        String day,

                        String date

        ) {

                VBox box = new VBox(4);

                box.setAlignment(
                                Pos.CENTER);

                box.setPrefWidth(
                                140);

                box.setMinWidth(
                                140);

                box.setMaxWidth(
                                140);

                box.setPadding(
                                new Insets(10, 5, 10, 5));

                box.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 8;");

                Label dayLabel = new Label(
                                day);

                dayLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 11px;" +
                                                "-fx-font-weight: bold;");

                Label dateLabel = new Label(
                                date);

                dateLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;");

                box.getChildren().addAll(

                                dayLabel,

                                dateLabel);

                return box;
        }

        // =====================================================
        // DAY COLUMN
        // =====================================================

        private VBox createDayColumn(

                        String day,

                        String breakfast,

                        String lunch,

                        String dinner

        ) {

                VBox column = new VBox(10);

                column.setPrefWidth(
                                140);

                column.setMinWidth(
                                140);

                column.setMaxWidth(
                                140);

                column.setPadding(
                                new Insets(12));

                column.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 10;");

                // =====================================================
                // DAY NAME
                // =====================================================

                Label dayLabel = new Label(
                                day);

                dayLabel.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // MEALS
                // =====================================================

                VBox breakfastCard = mealCard(

                                "BREAKFAST",

                                breakfast);

                VBox lunchCard = mealCard(

                                "LUNCH",

                                lunch);

                VBox dinnerCard = mealCard(

                                "DINNER",

                                dinner);

                // =====================================================
                // ADD MEALS
                // =====================================================

                column.getChildren().addAll(

                                dayLabel,

                                breakfastCard,

                                lunchCard,

                                dinnerCard);

                return column;
        }

        // =====================================================
        // MEAL CARD
        // =====================================================

        private VBox mealCard(

                        String mealType,

                        String mealName

        ) {

                VBox card = new VBox(5);

                card.setPadding(
                                new Insets(10));

                card.setStyle(
                                "-fx-background-color: #334155;" +
                                                "-fx-background-radius: 8;");

                Label type = new Label(
                                mealType);

                type.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 9px;" +
                                                "-fx-font-weight: bold;");

                Label name = new Label(
                                mealName);

                name.setWrapText(
                                true);

                name.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                card.getChildren().addAll(

                                type,

                                name);

                return card;
        }

        // =====================================================
        // NUTRITION SUMMARY
        // =====================================================

        private VBox createNutritionSummary() {

                VBox card = new VBox(15);

                card.setPrefWidth(700);
                card.setMinWidth(700);
                card.setMaxWidth(700);

                card.setPadding(
                                new Insets(18));

                card.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-background-radius: 10;");

                Label title = new Label(
                                "Nutrition Summary");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                HBox nutritionValues = new HBox(8);

                nutritionValues.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // TIP
                // =====================================================

                VBox tip = new VBox(5);

                tip.setPadding(
                                new Insets(12));

                tip.setStyle(
                                "-fx-background-color: #0F172A;" +
                                                "-fx-background-radius: 8;");

                Label tipTitle = new Label(
                                "💡  Nutrition Tip");

                tipTitle.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                weeklyInsightLabel = new Label(
                                "Generate a meal plan to receive AI weekly insights.");
                weeklyInsightLabel.setWrapText(true);

                weeklyInsightLabel.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 11px;");
                tip.getChildren().addAll(
                                tipTitle,
                                weeklyInsightLabel);

                // =====================================================
                // ADD ALL NUTRITION CONTENT
                // =====================================================

                card.getChildren().addAll(

                                title,

                                nutritionValues,

                                tip);

                return card;
        }

        // =====================================================
        // COST ITEM
        // =====================================================

        private void updateMealPlanUI(String mealPlan) {

                System.out.println("========== AI RESPONSE ==========");
                System.out.println(mealPlan);
                System.out.println("=================================");

                plannerArea.getChildren().clear();

                try {

                        JsonObject root = new Gson().fromJson(
                                        mealPlan,
                                        JsonObject.class);

                        String[] days = {
                                        "MONDAY",
                                        "TUESDAY",
                                        "WEDNESDAY",
                                        "THURSDAY",
                                        "FRIDAY",
                                        "SATURDAY",
                                        "SUNDAY"
                        };

                        for (String day : days) {

                                JsonObject dayData = root.getAsJsonObject(day);

                                if (dayData == null) {
                                        continue;
                                }

                                JsonObject breakfastObj = dayData.getAsJsonObject("breakfast");

                                JsonObject lunchObj = dayData.getAsJsonObject("lunch");

                                JsonObject dinnerObj = dayData.getAsJsonObject("dinner");

                                String breakfast = breakfastObj.get("meal").getAsString();

                                String lunch = lunchObj.get("meal").getAsString();

                                String dinner = dinnerObj.get("meal").getAsString();

                                plannerArea.getChildren().add(

                                                createDayColumn(
                                                                day,
                                                                breakfast,
                                                                lunch,
                                                                dinner));
                        }

                } catch (Exception e) {

                        System.out.println("Failed To Parse Meal Plan JSON");
                        e.printStackTrace();
                }
        }
}