package com.bytebites.view.user.userFeatures;

import com.bytebites.controller.CookNowController;
import com.bytebites.model.session.SessionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserWhatToCookToday {

        private final CookNowController cookNowController;
        private final String userId;

        public UserWhatToCookToday() {

                cookNowController = new CookNowController();

                userId = SessionManager.getUid();
        }

        public UserWhatToCookToday(
                        String userId) {

                cookNowController = new CookNowController();

                this.userId = userId;
        }

        // =========================================================
        // MAIN PAGE
        // =========================================================

        public VBox getMealDetails() {

                VBox main = new VBox(20);

                main.setPadding(
                                new Insets(25));

                main.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // HEADER
                // =====================================================

                Label pageTitle = new Label(
                                "What To Cook Today");

                pageTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // USER CHECK
                // =====================================================

                if (userId == null ||
                                userId.isBlank()) {

                        Label error = createErrorLabel(
                                        "Unable to identify the logged-in user.");

                        main.getChildren().addAll(
                                        pageTitle,
                                        error);

                        return main;
                }

                // =====================================================
                // LOAD TODAY'S MEALS
                // =====================================================

                JsonObject todaysMeals = cookNowController
                                .getTodaysMeals(
                                                userId);

                if (todaysMeals.has("error")) {

                        Label error = createErrorLabel(
                                        todaysMeals
                                                        .get("error")
                                                        .getAsString());

                        main.getChildren().addAll(
                                        pageTitle,
                                        error);

                        return main;
                }

                String day = todaysMeals
                                .get("day")
                                .getAsString();

                String breakfast = getMealValue(
                                todaysMeals,
                                "breakfast");

                String lunch = getMealValue(
                                todaysMeals,
                                "lunch");

                String dinner = getMealValue(
                                todaysMeals,
                                "dinner");

                // =====================================================
                // DAY LABEL
                // =====================================================

                Label dayLabel = new Label(
                                "Today's Meal Plan • "
                                                + day);

                dayLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // TOP SECTION
                // =====================================================

                HBox topSection = new HBox(25);

                topSection.setAlignment(
                                Pos.TOP_LEFT);

                // =====================================================
                // IMAGE
                // =====================================================

                VBox imageBox = new VBox();

                imageBox.setPrefWidth(
                                420);

                imageBox.setPrefHeight(
                                300);

                imageBox.setAlignment(
                                Pos.CENTER);

                imageBox.setStyle(
                                "-fx-background-color: #334155;" +
                                                "-fx-background-radius: 12;");

                Image image = new Image(
                                "assets\\images\\foodimage.jpg");

                ImageView imageView = new ImageView(
                                image);

                imageView.setFitWidth(
                                400);

                imageView.setFitHeight(
                                280);

                imageView.setPreserveRatio(
                                true);

                imageBox.getChildren().add(
                                imageView);

                // =====================================================
                // TODAY'S MEALS
                // =====================================================

                VBox mealInfo = new VBox(12);

                HBox.setHgrow(
                                mealInfo,
                                Priority.ALWAYS);

                Label mealHeading = new Label(
                                "Today's Meals");

                mealHeading.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 26px;" +
                                                "-fx-font-weight: bold;");

                Label breakfastLabel = createMealLabel(
                                "🌅 Breakfast",
                                breakfast);

                Label lunchLabel = createMealLabel(
                                "☀ Lunch",
                                lunch);

                Label dinnerLabel = createMealLabel(
                                "🌙 Dinner",
                                dinner);

                mealInfo.getChildren().addAll(
                                mealHeading,
                                breakfastLabel,
                                lunchLabel,
                                dinnerLabel);

                topSection.getChildren().addAll(
                                imageBox,
                                mealInfo);

                // =====================================================
                // MEAL SELECTION
                // =====================================================

                VBox mealSelectionBox = new VBox(10);

                Label selectTitle = new Label(
                                "SELECT MEAL TO COOK");

                selectTitle.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                ComboBox<String> mealDropdown = new ComboBox<>();

                mealDropdown.getItems().addAll(
                                "Breakfast - " + breakfast,
                                "Lunch - " + lunch,
                                "Dinner - " + dinner);

                mealDropdown.setValue(
                                "Breakfast - " + breakfast);

                mealDropdown.setPrefWidth(
                                500);

                mealDropdown.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-text-fill: white;");

                // =====================================================
                // CLOSED DROPDOWN APPEARANCE
                // =====================================================

                mealDropdown.setButtonCell(
                                new ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(item);

                                                        setTextFill(
                                                                        Color.WHITE);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;" +
                                                                                        "-fx-text-fill: white;" +
                                                                                        "-fx-font-size: 13px;");
                                                }
                                        }
                                });

                // =====================================================
                // DROPDOWN POPUP APPEARANCE
                // =====================================================

                mealDropdown.setCellFactory(
                                list -> new ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;");

                                                } else {

                                                        setText(item);

                                                        setTextFill(
                                                                        Color.WHITE);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;" +
                                                                                        "-fx-text-fill: white;" +
                                                                                        "-fx-font-size: 13px;");
                                                }
                                        }
                                });
                // =====================================================
                // NUMBER OF PEOPLE
                // =====================================================

                Label peopleLabel = new Label(
                                "NUMBER OF PEOPLE");

                peopleLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;");

                ComboBox<String> peopleDropdown = new ComboBox<>();

                peopleDropdown.getItems().addAll(
                                "1",
                                "2",
                                "3",
                                "4",
                                "5",
                                "6",
                                "7",
                                "8",
                                "9",
                                "10");

                peopleDropdown.setValue(
                                "1");

                peopleDropdown.setPrefWidth(
                                250);

                peopleDropdown.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-border-color: #22C55E;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-background-radius: 8;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-text-fill: white;");

                // =============================================
                // CLOSED COMBOBOX
                // =============================================

                peopleDropdown.setButtonCell(
                                new ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                } else {

                                                        setText(item);

                                                        setTextFill(
                                                                        Color.WHITE);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;" +
                                                                                        "-fx-text-fill: white;" +
                                                                                        "-fx-font-size: 13px;");
                                                }
                                        }
                                });

                // =============================================
                // POPUP ITEMS
                // =============================================

                peopleDropdown.setCellFactory(
                                list -> new ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;");

                                                } else {

                                                        setText(item);

                                                        setTextFill(
                                                                        Color.WHITE);

                                                        if (isSelected()) {

                                                                setStyle(
                                                                                "-fx-background-color: #22C55E;" +
                                                                                                "-fx-text-fill: #0F172A;"
                                                                                                +
                                                                                                "-fx-font-weight: bold;");

                                                        } else {

                                                                setStyle(
                                                                                "-fx-background-color: #1E293B;" +
                                                                                                "-fx-text-fill: white;"
                                                                                                +
                                                                                                "-fx-font-size: 13px;");
                                                        }
                                                }
                                        }
                                });
                // =====================================================
                // COOK THIS BUTTON
                // =====================================================

                Button cookButton = new Button(
                                "🍳  Cook This");

                cookButton.setPrefWidth(
                                180);

                cookButton.setPrefHeight(
                                45);

                cookButton.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                // =====================================================
                // INGREDIENTS
                // =====================================================

                VBox ingredientsBox = new VBox(10);

                Label ingredientsTitle = new Label(
                                "INGREDIENTS");

                ingredientsTitle.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                ListView<String> ingredientsList = new ListView<>();

                ingredientsList.setPrefHeight(
                                200);

                ingredientsList.setStyle(
                                "-fx-background-color: #1E293B;" +
                                                "-fx-control-inner-background: #1E293B;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 8;" +
                                                "-fx-background-radius: 8;");

                ingredientsList.getItems().add(
                                "Click \"Cook This\" to generate ingredients.");

                ingredientsList.setCellFactory(
                                list -> new ListCell<String>() {

                                        @Override
                                        protected void updateItem(
                                                        String item,
                                                        boolean empty) {

                                                super.updateItem(
                                                                item,
                                                                empty);

                                                if (empty ||
                                                                item == null) {

                                                        setText(null);
                                                        setGraphic(null);

                                                } else {

                                                        Label label = new Label(
                                                                        item);

                                                        label.setWrapText(
                                                                        true);

                                                        label.setStyle(
                                                                        "-fx-text-fill: #CBD5E1;" +
                                                                                        "-fx-font-size: 14px;");

                                                        setGraphic(
                                                                        label);

                                                        setStyle(
                                                                        "-fx-background-color: #1E293B;");
                                                }
                                        }
                                });

                ingredientsBox.getChildren().addAll(
                                ingredientsTitle,
                                ingredientsList);

                // =====================================================
                // RECIPE SECTION
                // =====================================================

                VBox recipeBox = new VBox(12);

                Label recipeTitle = new Label(
                                "RECIPE");

                recipeTitle.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                Label recipe = new Label(
                                "Select a meal and click \"Cook This\" " +
                                                "to generate the recipe using AI.");

                Label timeLabel = new Label();
                String savedRecipe = cookNowController.loadSavedRecipe();
                recipe.setWrapText(
                                true);

                recipe.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;");

                recipeBox.getChildren().addAll(
                                recipeTitle,
                                recipe);

                if (savedRecipe != null &&
                                !savedRecipe.isBlank()) {

                        displayRecipe(
                                        savedRecipe,
                                        recipe,
                                        ingredientsList,
                                        timeLabel);
                }
                // =====================================================
                // NUTRITION / TIME
                // =====================================================

                timeLabel.setWrapText(
                                true);

                timeLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 14px;");

                recipeBox.getChildren().add(
                                timeLabel);

                // =====================================================
                // COOK BUTTON ACTION
                // =====================================================

                cookButton.setOnAction(
                                event -> {

                                        String selected = mealDropdown.getValue();

                                        if (selected == null ||
                                                        selected.isBlank()) {
                                                return;
                                        }

                                        String mealName = extractMealName(
                                                        selected);

                                        int people = Integer.parseInt(
                                                        peopleDropdown
                                                                        .getValue());

                                        // Disable button while AI works
                                        cookButton.setDisable(
                                                        true);

                                        cookButton.setText(
                                                        "⏳ Generating...");

                                        recipe.setText(
                                                        "AI is preparing your recipe...");

                                        ingredientsList
                                                        .getItems()
                                                        .clear();

                                        ingredientsList
                                                        .getItems()
                                                        .add(
                                                                        "Generating ingredients...");

                                        timeLabel.setText("");

                                        Thread recipeThread = new Thread(
                                                        () -> {

                                                                String result = cookNowController
                                                                                .generateRecipe(
                                                                                                mealName,
                                                                                                people);

                                                                Platform.runLater(
                                                                                () -> {

                                                                                        cookButton
                                                                                                        .setDisable(
                                                                                                                        false);

                                                                                        cookButton
                                                                                                        .setText(
                                                                                                                        "🍳  Cook This");

                                                                                        if (result == null ||
                                                                                                        result
                                                                                                                        .isBlank()) {

                                                                                                recipe.setText(
                                                                                                                "Unable to generate recipe. Please try again.");

                                                                                                ingredientsList
                                                                                                                .getItems()
                                                                                                                .clear();

                                                                                                ingredientsList
                                                                                                                .getItems()
                                                                                                                .add(
                                                                                                                                "Recipe generation failed.");

                                                                                                return;
                                                                                        }

                                                                                       displayRecipe(
        result,
        recipe,
        ingredientsList,
        timeLabel);
                                                                                });
                                                        });

                                        recipeThread.setDaemon(
                                                        true);

                                        recipeThread.start();
                                });

                // =====================================================
                // SELECTION AREA
                // =====================================================

                HBox cookControls = new HBox(20);

                cookControls.setAlignment(
                                Pos.CENTER_LEFT);

                VBox peopleBox = new VBox(5);

                peopleBox.getChildren().addAll(
                                peopleLabel,
                                peopleDropdown);

                cookControls.getChildren().addAll(
                                peopleBox,
                                cookButton);

                mealSelectionBox.getChildren().addAll(
                                selectTitle,
                                mealDropdown,
                                cookControls);

                // =====================================================
                // CONTENT
                // =====================================================

                VBox content = new VBox(20);

                content.getChildren().addAll(
                                topSection,
                                mealSelectionBox,
                                ingredientsBox,
                                recipeBox);

                // =====================================================
                // SCROLL
                // =====================================================

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

                VBox.setVgrow(
                                scroll,
                                Priority.ALWAYS);

                main.getChildren().addAll(
                                pageTitle,
                                dayLabel,
                                scroll);

                return main;
        }

        // =========================================================
        // DISPLAY AI RECIPE
        // =========================================================

        private void displayRecipe(
                        String json,
                        Label recipeLabel,
                        ListView<String> ingredientsList,
                        Label timeLabel) {

                try {

                        System.out.println("========== GEMINI RESPONSE ==========");
                        System.out.println(json);
                        System.out.println("=====================================");
                        JsonObject recipe = new com.google.gson.Gson()
                                        .fromJson(
                                                        json,
                                                        JsonObject.class);

                        // =================================================
                        // MEAL NAME
                        // =================================================

                        String mealName = getJsonValue(
                                        recipe,
                                        "mealName");

                        // =================================================
                        // TIME + NUTRITION
                        // =================================================

                        String preparation = getJsonValue(
                                        recipe,
                                        "preparationTime");

                        String cooking = getJsonValue(
                                        recipe,
                                        "cookingTime");

                        String total = getJsonValue(
                                        recipe,
                                        "totalTime");

                        timeLabel.setText(
                                        "⏱ Preparation: "
                                                        + preparation
                                                        + "    "
                                                        + "Cooking: "
                                                        + cooking
                                                        + "    "
                                                        + "Total: "
                                                        + total);

                        // =================================================
                        // INGREDIENTS
                        // =================================================

                        ingredientsList
                                        .getItems()
                                        .clear();

                        if (recipe.has(
                                        "ingredients")) {

                                JsonArray ingredients = recipe.getAsJsonArray(
                                                "ingredients");

                                for (int i = 0; i < ingredients.size(); i++) {

                                        JsonObject ingredient = ingredients
                                                        .get(i)
                                                        .getAsJsonObject();

                                        String name = getJsonValue(
                                                        ingredient,
                                                        "name");

                                        String quantity = getJsonValue(
                                                        ingredient,
                                                        "quantity");

                                        ingredientsList
                                                        .getItems()
                                                        .add(
                                                                        name
                                                                                        + "  |  "
                                                                                        + quantity);
                                }
                        }

                        // =================================================
                        // STEPS
                        // =================================================

                        StringBuilder instructions = new StringBuilder();

                        instructions.append(
                                        mealName);

                        instructions.append(
                                        "\n\n");

                        instructions.append(
                                        "STEP-BY-STEP INSTRUCTIONS\n\n");

                        if (recipe.has(
                                        "steps")) {

                                JsonArray steps = recipe.getAsJsonArray(
                                                "steps");

                                for (int i = 0; i < steps.size(); i++) {

                                        instructions
                                                        .append(
                                                                        (i + 1)
                                                                                        + ". ");

                                        instructions
                                                        .append(
                                                                        steps
                                                                                        .get(i)
                                                                                        .getAsString());

                                        instructions
                                                        .append(
                                                                        "\n\n");
                                }
                        }

                        // =================================================
                        // TIPS
                        // =================================================

                        instructions.append(
                                        "COOKING TIPS\n\n");

                        if (recipe.has(
                                        "tips")) {

                                JsonArray tips = recipe.getAsJsonArray(
                                                "tips");

                                for (int i = 0; i < tips.size(); i++) {

                                        instructions
                                                        .append(
                                                                        "• ");

                                        instructions
                                                        .append(
                                                                        tips
                                                                                        .get(i)
                                                                                        .getAsString());

                                        instructions
                                                        .append(
                                                                        "\n");
                                }
                        }

                        recipeLabel.setText(
                                        instructions.toString());

                } catch (Exception e) {

                        e.printStackTrace();

                        recipeLabel.setText(
                                        "Recipe was generated, but could not be displayed correctly.");
                }
        }

        // =========================================================
        // GET JSON VALUE
        // =========================================================

        private String getJsonValue(
                        JsonObject object,
                        String key) {

                if (object.has(key)
                                &&
                                !object.get(key).isJsonNull()) {

                        return object
                                        .get(key)
                                        .getAsString();
                }

                return "-";
        }

        // =========================================================
        // GET TODAY'S MEAL VALUE
        // =========================================================

        private String getMealValue(
                        JsonObject meals,
                        String type) {

                if (meals.has(type)
                                &&
                                !meals.get(type).isJsonNull()) {

                        return meals
                                        .get(type)
                                        .getAsString();
                }

                return "Not available";
        }

        // =========================================================
        // EXTRACT MEAL NAME
        // =========================================================

        private String extractMealName(
                        String selected) {

                int index = selected.indexOf(" - ");

                if (index >= 0) {

                        return selected.substring(
                                        index + 3).trim();
                }

                return selected;
        }

        // =========================================================
        // CREATE MEAL LABEL
        // =========================================================

        private Label createMealLabel(
                        String title,
                        String meal) {

                Label label = new Label(
                                title
                                                + "\n"
                                                + meal);

                label.setWrapText(
                                true);

                label.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-padding: 8 0 8 0;");

                return label;
        }

        // =========================================================
        // ERROR LABEL
        // =========================================================

        private Label createErrorLabel(
                        String message) {

                Label label = new Label(
                                message);

                label.setWrapText(
                                true);

                label.setStyle(
                                "-fx-text-fill: #EF4444;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;");

                return label;
        }

        // =========================================================
        // NUTRITION ITEM
        // =========================================================

        
}