package com.bytebites.view.dietitian.dietitionFeatures;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import com.bytebites.controller.AIController;
import com.bytebites.dao.DietitianMealPlanDao;
import com.bytebites.model.session.SessionManager;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javafx.concurrent.Task;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import com.bytebites.view.common.SameStageDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DietitianWeeklyMealPlanner {

        private BorderPane featurePane;

        private String currentFamilyName;

        private String currentFamilyUserId;

        private Runnable backAction;

        private Map<String, Label> mealLabels = new HashMap<>();

        private Map<String, JsonObject> mealDataMap = new HashMap<>();

        public DietitianWeeklyMealPlanner(
                        BorderPane featurePane,
                        String currentFamilyName,
                        String currentFamilyUserId,
                        Runnable backAction) {
                this.featurePane = featurePane;
                this.currentFamilyName = currentFamilyName;
                this.currentFamilyUserId = currentFamilyUserId;
                this.backAction = backAction;
        }

        // =========================================================
        // CREATE WEEKLY PLAN PAGE
        // =========================================================

        public void showCreateWeeklyPlan() {

                BorderPane createPane = new BorderPane();

                createPane.setStyle(
                                "-fx-background-color: #0F172A;");

                createPane.setPadding(
                                new Insets(20, 25, 20, 25));

                // =====================================================
                // TOP HEADER
                // =====================================================

                Button backButton = new Button(
                                "←  Back");

                backButton.setPrefWidth(
                                85);

                backButton.setPrefHeight(
                                38);

                backButton.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;");

                backButton.setOnAction(
                                event -> backAction.run());

                Label title = new Label(
                                currentFamilyName +
                                                "  -  Create Weekly Plan");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 27px;" +
                                                "-fx-font-weight: bold;");

                Region headerSpacer = new Region();

                HBox.setHgrow(
                                headerSpacer,
                                Priority.ALWAYS);

                Label weekLabel = new Label(
                                "Week: 17 - 23 Aug 2026");

                weekLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                HBox topHeader = new HBox(
                                20);

                topHeader.setAlignment(
                                Pos.CENTER_LEFT);

                topHeader.getChildren().addAll(
                                backButton,
                                title,
                                headerSpacer,
                                weekLabel);

                // =====================================================
                // PAGE TITLE
                // =====================================================

                Label weeklyPlanTitle = new Label(
                                "WEEKLY PLAN");

                weeklyPlanTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 21px;" +
                                                "-fx-font-weight: bold;");

                Label weeklySubtitle = new Label(
                                "Create and customize meals for the entire family");

                weeklySubtitle.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 13px;");

                VBox pageTitle = new VBox(
                                5,
                                weeklyPlanTitle,
                                weeklySubtitle);

                pageTitle.setPadding(
                                new Insets(18, 0, 5, 0));

                // =====================================================
                // WEEKLY DAYS
                // =====================================================

                VBox daysContainer = new VBox(16);

                daysContainer.setPadding(
                                new Insets(5, 5, 20, 5));

                daysContainer.getChildren().addAll(

                                createEditableDay(
                                                "MONDAY",
                                                "Vegetable Poha",
                                                "Dal + Roti + Salad",
                                                "Paneer Bhurji + Roti"),

                                createEditableDay(
                                                "TUESDAY",
                                                "Oats + Banana",
                                                "Rajma Rice + Salad",
                                                "Vegetable Khichdi"),

                                createEditableDay(
                                                "WEDNESDAY",
                                                "Moong Dal Chilla",
                                                "Dal Tadka + Roti",
                                                "Paneer + Vegetables"),

                                createEditableDay(
                                                "THURSDAY",
                                                "Upma + Curd",
                                                "Chole + Roti + Salad",
                                                "Mixed Vegetable Pulao"),

                                createEditableDay(
                                                "FRIDAY",
                                                "Besan Chilla",
                                                "Dal + Rice + Salad",
                                                "Palak Paneer + Roti"),

                                createEditableDay(
                                                "SATURDAY",
                                                "Idli + Sambar",
                                                "Vegetable Biryani",
                                                "Dal Khichdi + Curd"),

                                createEditableDay(
                                                "SUNDAY",
                                                "Stuffed Paratha + Curd",
                                                "Paneer Rice Bowl",
                                                "Vegetable Soup + Roti"));

                // =====================================================
                // SCROLL PANE
                // =====================================================

                ScrollPane scrollPane = new ScrollPane(
                                daysContainer);

                scrollPane.setFitToWidth(
                                true);

                scrollPane.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scrollPane.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.AS_NEEDED);

                scrollPane.setStyle(
                                "-fx-background: #0F172A;" +
                                                "-fx-background-color: #0F172A;" +
                                                "-fx-border-color: transparent;");

                // =====================================================
                // BOTTOM BUTTONS
                // =====================================================

                Button generateAI = new Button(
                                "✦  Generate using AI");

                generateAI.setPrefWidth(
                                235);

                generateAI.setPrefHeight(
                                48);

                generateAI.setStyle(
                                "-fx-background-color: #1976D2;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                generateAI.setOnAction(event -> {

                        generateAI.setDisable(true);
                        generateAI.setText("Generating...");

                        Task<String> task = new Task<>() {

                                @Override
                                protected String call() throws Exception {

                                        AIController aiController = new AIController();

                                        return aiController
                                                        .generateMealPlanForDietitian(
                                                                        currentFamilyUserId);
                                }
                        };

                        task.setOnSucceeded(e -> {

                                String aiPlan = task.getValue();

                                if (aiPlan != null &&
                                                !aiPlan.isBlank()) {

                                        populateMealPlan(aiPlan);
                                }

                                generateAI.setDisable(false);
                                generateAI.setText("✦ Generate using AI");
                        });

                        task.setOnFailed(e -> {

                                task.getException()
                                                .printStackTrace();

                                generateAI.setDisable(false);
                                generateAI.setText("✦ Generate using AI");
                        });

                        new Thread(task).start();
                });
                Button savePlan = new Button(
                                "Save Weekly Plan");

                savePlan.setOnAction(event -> {

                        try {

                                String mealPlanJson = buildMealPlanJson();

                                System.out.println(mealPlanJson);

                                DietitianMealPlanDao dao = new DietitianMealPlanDao();

                                System.out.println("Saving plan for family UID: " + currentFamilyUserId);
                                System.out.println("Logged in dietitian UID: " + SessionManager.getUid());

                                dao.savePlan(
                                                currentFamilyUserId,
                                                LocalDate.now().toString(),
                                                mealPlanJson,
                                                "Created by Dietitian",
                                                SessionManager.getUid()

                                );

                                savePlan.setText("✓  Saved");
                                savePlan.setDisable(true);
                                savePlan.setOpacity(1.0);

                                savePlan.setStyle(
                                        "-fx-background-color: #1565C0;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-weight: bold;" +
                                        "-fx-background-radius: 8;" +
                                        "-fx-padding: 10 20 10 20;"
                                );

                                System.out.println(
                                                "Weekly plan saved successfully.");

                        } catch (Exception e) {

                                e.printStackTrace();
                                savePlan.setText("Save Plan");
                                savePlan.setDisable(false);
                        }
                });

                savePlan.setPrefWidth(
                                185);

                savePlan.setPrefHeight(
                                48);

                savePlan.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                HBox bottomButtons = new HBox(
                                12,
                                generateAI,
                                savePlan);

                bottomButtons.setAlignment(
                                Pos.CENTER);

                bottomButtons.setPadding(
                                new Insets(12, 0, 0, 0));

                // =====================================================
                // CENTER CONTENT
                // =====================================================

                VBox centerContent = new VBox(
                                5,
                                pageTitle,
                                scrollPane);

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                createPane.setTop(
                                topHeader);

                createPane.setCenter(
                                centerContent);

                createPane.setBottom(
                                bottomButtons);

                // =====================================================
                // PUT CREATE PAGE INTO SAME FEATURE PANE
                // =====================================================

                featurePane.setCenter(createPane);

                // load previously saved plan
                loadExistingPlan();
        }
        // =========================================================
        // EDITABLE DAY
        // =========================================================

        private VBox createEditableMeal(
                        String day,
                        String mealType,
                        String mealText) {

                VBox mealCard = new VBox(12);
                mealCard.setPrefHeight(115);
                mealCard.setMinHeight(115);
                mealCard.setMaxHeight(115);
                mealCard.setPadding(new Insets(16));

                mealCard.setPrefWidth(380);
                mealCard.setMinWidth(380);
                mealCard.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(mealCard, Priority.ALWAYS);

                mealCard.setStyle(
                                "-fx-background-color: #0B1730;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                Label mealTypeLabel = new Label(mealType);

                mealTypeLabel.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                Label mealName = new Label(mealText);

                mealName.setWrapText(true);
                mealName.setMaxWidth(Double.MAX_VALUE);
                mealName.setMaxHeight(42); // roughly 2 lines

                mealName.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                mealLabels.put(
                                day + "_" + mealType,
                                mealName);

                mealName.setMaxWidth(Double.MAX_VALUE);

                Button editButton = new Button("Edit");

                editButton.setOnAction(e -> {

                        String updatedMeal = showMealEditDialog(
                                        mealType,
                                        mealName.getText());

                        if (updatedMeal != null &&
                                        !updatedMeal.isBlank()) {

                                mealName.setText(updatedMeal);

                                String key = day + "_" + mealType;

                                JsonObject mealObj = mealDataMap.get(key);

                                if (mealObj == null) {

                                        mealObj = new JsonObject();

                                        mealObj.addProperty(
                                                        "calories",
                                                        "");

                                        mealObj.addProperty(
                                                        "protein",
                                                        "");

                                        mealObj.addProperty(
                                                        "carbs",
                                                        "");
                                }

                                mealObj.addProperty(
                                                "meal",
                                                updatedMeal);

                                mealDataMap.put(
                                                key,
                                                mealObj);
                        }
                });

                editButton.setPrefWidth(72);
                editButton.setPrefHeight(30);

                editButton.setStyle(
                                "-fx-background-color: #2D7FEA;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                BorderPane mealRow = new BorderPane();

                mealRow.setCenter(mealName);
                mealRow.setRight(editButton);

                BorderPane.setMargin(editButton, new Insets(0, 0, 0, 12));

                mealName.setMaxWidth(Double.MAX_VALUE);

                BorderPane.setAlignment(
                                editButton,
                                Pos.TOP_RIGHT);

                mealCard.getChildren().addAll(
                                mealTypeLabel,
                                mealRow);
                return mealCard;
        }

        private VBox createEditableDay(
                        String day,
                        String breakfast,
                        String lunch,
                        String dinner) {

                Label dayLabel = new Label(day);

                dayLabel.setStyle(
                                "-fx-text-fill: #60A5FA;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;");

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                HBox header = new HBox(
                                dayLabel,
                                spacer);

                VBox breakfastCard = createEditableMeal(day, "BREAKFAST", breakfast);

                VBox lunchCard = createEditableMeal(day, "LUNCH", lunch);

                VBox dinnerCard = createEditableMeal(day, "DINNER", dinner);

                HBox.setHgrow(breakfastCard, Priority.ALWAYS);
                HBox.setHgrow(lunchCard, Priority.ALWAYS);
                HBox.setHgrow(dinnerCard, Priority.ALWAYS);

                HBox mealsRow = new HBox(
                                18,
                                breakfastCard,
                                lunchCard,
                                dinnerCard);

                VBox dayCard = new VBox(
                                16,
                                header,
                                mealsRow);

                dayCard.setPadding(
                                new Insets(18));

                dayCard.setStyle(
                                "-fx-background-color: #0B1730;" +
                                                "-fx-border-color: #1E293B;" +
                                                "-fx-border-radius: 12;" +
                                                "-fx-background-radius: 12;");

                return dayCard;
        }

        // =========================================================
        // EDIT MEAL DIALOG
        // =========================================================

        // =========================================================
        // EDIT MEAL DIALOG
        // =========================================================

        private String showMealEditDialog(
                        String mealType,
                        String currentMeal) {

                SameStageDialog<String> dialog = new SameStageDialog<>();

                dialog.setTitle("Edit Meal");
                dialog.setHeaderText(null);

                // =====================================================
                // DIALOG PANE
                // =====================================================

                DialogPane dialogPane = dialog.getDialogPane();

                dialogPane.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // HEADER
                // =====================================================

                Label title = new Label(
                                "Edit " + mealType);

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                Label subtitle = new Label(
                                "Update the meal for your weekly family plan");

                subtitle.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 12px;");

                VBox header = new VBox(
                                5,
                                title,
                                subtitle);

                header.setPadding(
                                new Insets(5, 5, 10, 5));

                // =====================================================
                // TEXT FIELD
                // =====================================================

                TextField mealField = new TextField();

                mealField.setText(
                                currentMeal);

                mealField.setPrefWidth(
                                380);

                mealField.setPrefHeight(
                                42);

                mealField.setPromptText(
                                "Enter meal name...");

                mealField.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: #E2E8F0;" +
                                                "-fx-prompt-text-fill: #64748B;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 10 12 10 12;" +
                                                "-fx-font-size: 13px;");

                // =====================================================
                // DISH LABEL
                // =====================================================

                Label dishLabel = new Label(
                                "DISH NAME");

                dishLabel.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // CONTENT
                // =====================================================

                VBox content = new VBox(
                                10,
                                dishLabel,
                                mealField);

                content.setPadding(
                                new Insets(10, 5, 15, 5));

                VBox dialogContent = new VBox(
                                5,
                                header,
                                content);

                dialogPane.setContent(
                                dialogContent);

                // =====================================================
                // BUTTONS
                // =====================================================

                ButtonType saveButton = new ButtonType(
                                "Save",
                                ButtonBar.ButtonData.OK_DONE);

                ButtonType cancelButton = new ButtonType(
                                "Cancel",
                                ButtonBar.ButtonData.CANCEL_CLOSE);

                dialogPane.getButtonTypes().addAll(
                                saveButton,
                                cancelButton);

                // =====================================================
                // STYLE DIALOG BUTTONS
                // =====================================================

                Button save = (Button) dialogPane.lookupButton(
                                saveButton);

                save.setStyle(
                                "-fx-background-color: #1976D2;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-padding: 8 20 8 20;");

                Button cancel = (Button) dialogPane.lookupButton(
                                cancelButton);

                cancel.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-padding: 8 20 8 20;");

                // =====================================================
                // RESULT
                // =====================================================

                dialog.setResultConverter(
                                button -> {

                                        if (button == saveButton) {

                                                return mealField
                                                                .getText()
                                                                .trim();
                                        }

                                        return null;
                                });

                // =====================================================
                // SHOW DIALOG
                // =====================================================

                return dialog
                                .showAndWait()
                                .orElse(null);
        }

        // =========================================================
        // AI MEAL GENERATION
        // =========================================================

        private String generateMealUsingAI(
                        String mealType,
                        String currentMeal) {

                System.out.println(
                                "AI generating replacement meal...");

                System.out.println(
                                "Meal Type: " + mealType);

                System.out.println(
                                "Current Meal: " + currentMeal);

                /*
                 * =====================================================
                 * TEMPORARY AI RESPONSE
                 * =====================================================
                 *
                 * Replace this section later with your actual
                 * AI/API service.
                 *
                 */

                switch (mealType) {

                        case "BREAKFAST":

                                return "Vegetable Upma + Curd";

                        case "LUNCH":

                                return "Dal Tadka + Brown Rice + Salad";

                        case "DINNER":

                                return "Paneer Tikka + Roti + Vegetables";

                        default:

                                return currentMeal;
                }
        }

        private String buildMealPlanJson() {

                JsonObject root = new JsonObject();

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

                        JsonObject dayObj = new JsonObject();

                        JsonObject breakfast = mealDataMap.get(day + "_BREAKFAST");

                        JsonObject lunch = mealDataMap.get(day + "_LUNCH");

                        JsonObject dinner = mealDataMap.get(day + "_DINNER");

                        if (breakfast != null) {
                                dayObj.add("breakfast", breakfast);
                        }

                        if (lunch != null) {
                                dayObj.add("lunch", lunch);
                        }

                        if (dinner != null) {
                                dayObj.add("dinner", dinner);
                        }

                        root.add(day, dayObj);
                }

                return root.toString();
        }

        private void populateMealPlan(String aiPlan) {

                try {

                        JsonObject root = new Gson().fromJson(
                                        aiPlan,
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

                                JsonObject dayObj = root.getAsJsonObject(day);

                                if (dayObj == null) {
                                        continue;
                                }

                                JsonObject breakfast = dayObj.getAsJsonObject("breakfast");

                                JsonObject lunch = dayObj.getAsJsonObject("lunch");

                                JsonObject dinner = dayObj.getAsJsonObject("dinner");

                                if (breakfast != null) {

                                        mealLabels.get(day + "_BREAKFAST")
                                                        .setText(
                                                                        breakfast.get("meal")
                                                                                        .getAsString());

                                        mealDataMap.put(
                                                        day + "_BREAKFAST",
                                                        breakfast);
                                }
                                if (lunch != null) {

                                        mealLabels.get(day + "_LUNCH")
                                                        .setText(
                                                                        lunch.get("meal")
                                                                                        .getAsString());

                                        mealDataMap.put(
                                                        day + "_LUNCH",
                                                        lunch);
                                }
                                if (dinner != null) {

                                        mealLabels.get(day + "_DINNER")
                                                        .setText(
                                                                        dinner.get("meal")
                                                                                        .getAsString());

                                        mealDataMap.put(
                                                        day + "_DINNER",
                                                        dinner);
                                }

                        }

                        System.out.println(
                                        "Dietitian meal plan loaded.");

                } catch (Exception e) {

                        e.printStackTrace();
                }
        }

        private void loadExistingPlan() {

                try {

                        DietitianMealPlanDao dao = new DietitianMealPlanDao();

                        DocumentSnapshot doc = dao.getPlan(currentFamilyUserId);
                        System.out.println("CURRENT FAMILY = " + currentFamilyUserId);

                        if (!doc.exists()) {
                                return;
                        }

                        String mealPlanJson = doc.getString("mealPlanJson");

                        if (mealPlanJson == null ||
                                        mealPlanJson.isBlank()) {
                                return;
                        }

                        populateMealPlan(mealPlanJson);

                        System.out.println("Existing meal plan loaded.");

                } catch (Exception e) {

                        e.printStackTrace();
                }
        }

}
