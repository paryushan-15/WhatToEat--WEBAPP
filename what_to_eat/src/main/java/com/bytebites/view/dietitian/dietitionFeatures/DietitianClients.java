package com.bytebites.view.dietitian.dietitionFeatures;

import com.bytebites.dao.DietitianMealPlanDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;
import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.model.FamilyMember;
import com.bytebites.model.session.SessionManager;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DietitianClients {

        /*
         * =========================================================
         * MAIN FEATURE PANE
         * =========================================================
         *
         * This is the pane controlled by DietitianMainLayout.
         *
         * We NEVER create a new Stage or Scene here.
         * Every page is simply placed inside this pane.
         *
         */

        private BorderPane featurePane;
        // =========================================================
        // GET CLIENT PAGE
        // =========================================================

        public BorderPane getClients() {

                featurePane = new BorderPane();

                featurePane.setStyle(
                                "-fx-background-color: #0F172A;");

                showClientsPage();

                return featurePane;
        }

        public BorderPane getFamilyProfile(String familyName) {

                if (featurePane == null) {
                        featurePane = new BorderPane();
                        featurePane.setStyle("-fx-background-color: #0F172A;");
                }

                return featurePane;
        }

        // =========================================================
        // CLIENT LIST PAGE
        // =========================================================

        private void showClientsPage() {

                BorderPane mainPane = new BorderPane();

                mainPane.setPadding(
                                new Insets(25));

                mainPane.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // PAGE TITLE
                // =====================================================

                Label title = new Label(
                                "Clients");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;");

                Label subtitle = new Label(
                                "Manage your families and track their nutrition plans");

                subtitle.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                VBox titleBox = new VBox(
                                5,
                                title,
                                subtitle);

                // =====================================================
                // STATUS FILTER
                // =====================================================

                ComboBox<String> statusFilter = new ComboBox<>();

                statusFilter.getItems().addAll(
                                "Status: All",
                                "Active",
                                "Pending Review",
                                "Inactive");

                statusFilter.setValue(
                                "Status: All");

                statusFilter.setPrefWidth(140);
                statusFilter.setPrefHeight(38);

                statusFilter.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;");

                // =====================================================
                // GOAL FILTER
                // =====================================================

                ComboBox<String> goalFilter = new ComboBox<>();

                goalFilter.getItems().addAll(
                                "Goal: All",
                                "Weight Management",
                                "Sports Nutrition",
                                "Clinical / Diabetic");

                goalFilter.setValue(
                                "Goal: All");

                goalFilter.setPrefWidth(180);
                goalFilter.setPrefHeight(38);

                goalFilter.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;");

                // =====================================================
                // ADD CLIENT
                // =====================================================

                Button addClient = new Button("+  Add Client");

                addClient.setPrefWidth(125);
                addClient.setPrefHeight(38);

                addClient.setStyle(
                                "-fx-background-color: #1976D2;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                // =====================================================
                // HEADER
                // =====================================================

                HBox header = new HBox(15);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                HBox.setHgrow(
                                titleBox,
                                Priority.ALWAYS);

                header.getChildren().addAll(
                                titleBox,
                                statusFilter,
                                goalFilter,
                                addClient);

                // =====================================================
                // CLIENT CONTAINER
                // =====================================================

                VBox clientsContainer = new VBox(15);

                clientsContainer.setPadding(
                                new Insets(5, 2, 20, 2));

                // =====================================================
                // CLIENTS
                // =====================================================

                // LOAD REAL CLIENTS FROM FIREBASE

                DietitianDao dietitianDao = new DietitianDao();
                FamilyMemberDao familyMemberDao = new FamilyMemberDao();

                String dietitianUid = SessionManager.getUid();

                List<String> subscriberUserIds = dietitianDao.getSubscriberUserIds(dietitianUid);

                for (String userId : subscriberUserIds) {

                        List<FamilyMember> members = familyMemberDao.getFamilyMembersByUserId(userId);

                        if (members.isEmpty()) {
                                continue;
                        }

                        String familyName = members.get(0).getName() + " Family";

                        String memberCount = members.size() + " Members";

                        String goal = members.get(0).getGoal();

                        if (goal == null || goal.trim().isEmpty()) {
                                goal = "Nutrition";
                        }

                        VBox familyCard;

                        // GET SUBSCRIBER DETAILS
                        Map<String, Object> subscriber = dietitianDao.getSubscriberDetails(
                                        dietitianUid,
                                        userId);

                        String plan = "Family Plan";
                        String payment = "Not Set";
                        String expiry = "Not Set";

                        if (subscriber != null) {

                                Object planObject = subscriber.get("plan");

                                Object amountObject = subscriber.get("amount");

                                Object expiryObject = subscriber.get("planExpiry");

                                if (planObject != null) {
                                        plan = planObject.toString();
                                }

                                if (amountObject != null) {
                                        payment = "₹" + amountObject.toString();
                                }

                                if (expiryObject instanceof com.google.cloud.Timestamp) {
                                        com.google.cloud.Timestamp expiryTimestamp = (com.google.cloud.Timestamp) expiryObject;

                                        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(
                                                        "dd MMM yyyy");

                                        expiry = dateFormat.format(
                                                        expiryTimestamp.toDate());
                                }
                        }

                        familyCard = createClientCard(
                                        familyName,
                                        memberCount,
                                        goal,
                                        plan,
                                        payment,
                                        expiry,
                                        "Active",
                                        userId);

                        clientsContainer.getChildren().add(familyCard);
                }

                // =====================================================
                // SCROLL
                // =====================================================

                ScrollPane scrollPane = new ScrollPane(
                                clientsContainer);

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

                VBox content = new VBox(20);

                VBox.setVgrow(
                                scrollPane,
                                Priority.ALWAYS);

                content.getChildren().addAll(
                                header,
                                scrollPane);

                mainPane.setCenter(
                                content);

                featurePane.setCenter(
                                mainPane);
        }

        // =========================================================
        // CLIENT CARD
        // =========================================================

        private VBox createClientCard(
                        String familyName,
                        String members,
                        String goal,
                        String plan,
                        String payment,
                        String date,
                        String status,
                        String userId) {

                VBox card = new VBox(18);

                card.setPadding(
                                new Insets(20));

                card.setMaxWidth(
                                Double.MAX_VALUE);

                card.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-border-color: #1F2937;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                // =====================================================
                // FAMILY NAME
                // =====================================================

                Label familyLabel = new Label(
                                familyName);

                familyLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 19px;" +
                                                "-fx-font-weight: bold;");

                Label membersLabel = new Label(
                                members);

                membersLabel.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 13px;");

                VBox familyInfo = new VBox(
                                5,
                                familyLabel,
                                membersLabel);

                // =====================================================
                // EXPLORE BUTTON
                // =====================================================

                Button exploreButton = new Button(
                                "Explore Family  →");

                exploreButton.setPrefHeight(
                                34);

                exploreButton.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #60A5FA;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-border-color: #1976D2;" +
                                                "-fx-border-radius: 6;" +
                                                "-fx-background-radius: 6;" +
                                                "-fx-padding: 0 14 0 14;");

                /*
                 * Open family details inside SAME featurePane
                 */

                exploreButton.setOnAction(
                                event -> {


                                        showFamilyDetails(
                                                        familyName,
                                                        userId);
                                });

                Region spacer = new Region();

                HBox.setHgrow(
                                spacer,
                                Priority.ALWAYS);

                HBox topRow = new HBox(10);

                topRow.setAlignment(
                                Pos.CENTER_LEFT);

                topRow.getChildren().addAll(
                                familyInfo,
                                spacer,
                                exploreButton);

                // =====================================================
                // DIVIDER
                // =====================================================

                Region divider = new Region();

                divider.setPrefHeight(
                                1);

                divider.setMaxWidth(
                                Double.MAX_VALUE);

                divider.setStyle(
                                "-fx-background-color: #1F2937;");

                // =====================================================
                // PLAN
                // =====================================================

                Label planTitle = new Label(
                                "CURRENT PLAN");

                planTitle.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;");

                Label planName = new Label(
                                plan);

                planName.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;");

                VBox planBox = new VBox(
                                5,
                                planTitle,
                                planName);

                // =====================================================
                // EXPIRY
                // =====================================================

                // =====================================================
                // PAYMENT
                // =====================================================

                Label paymentTitle = new Label(
                                "PAYMENT");

                paymentTitle.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;");

                Label paymentLabel = new Label(
                                payment);

                paymentLabel.setStyle(
                                "-fx-text-fill: #22C55E;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;");

                VBox paymentBox = new VBox(
                                5,
                                paymentTitle,
                                paymentLabel);

                // =====================================================
                // EXPIRY
                // =====================================================

                Label expiryTitle = new Label(
                                "PLAN EXPIRY");

                expiryTitle.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 10px;" +
                                                "-fx-font-weight: bold;");

                Label expiryLabel = new Label(
                                date);

                expiryLabel.setStyle(
                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;");

                VBox expiryBox = new VBox(
                                5,
                                expiryTitle,
                                expiryLabel);

                HBox planRow = new HBox(60);

                planRow.setAlignment(
                                Pos.CENTER_LEFT);

                planRow.getChildren().addAll(
                                planBox,
                                paymentBox,
                                expiryBox);

                // =====================================================
                // STATUS
                // =====================================================

                Label statusLabel = new Label(
                                "●  " + status);

                if (status.equals("Active")) {

                        statusLabel.setStyle(
                                        "-fx-text-fill: #22C55E;" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-color: #163B2A;" +
                                                        "-fx-padding: 6 10 6 10;" +
                                                        "-fx-background-radius: 15;");

                } else {

                        statusLabel.setStyle(
                                        "-fx-text-fill: #F59E0B;" +
                                                        "-fx-font-size: 12px;" +
                                                        "-fx-font-weight: bold;" +
                                                        "-fx-background-color: #3B2F16;" +
                                                        "-fx-padding: 6 10 6 10;" +
                                                        "-fx-background-radius: 15;");
                }

                Region bottomSpacer = new Region();

                HBox.setHgrow(
                                bottomSpacer,
                                Priority.ALWAYS);

                HBox bottomRow = new HBox(
                                20,
                                bottomSpacer,
                                statusLabel);

                bottomRow.setAlignment(
                                Pos.CENTER_LEFT);

                card.getChildren().addAll(
                                topRow,
                                divider,
                                planRow,
                                bottomRow);

                return card;
        }

        // =========================================================
        // FAMILY DETAILS PAGE
        // =========================================================

        public void showFamilyDetails(
                        String familyName,
                        String userId) {

                BorderPane detailsPane = new BorderPane();

                detailsPane.setPadding(
                                new Insets(25));

                detailsPane.setStyle(
                                "-fx-background-color: #0F172A;");

                // =====================================================
                // HEADER
                // =====================================================

                Label familyTitle = new Label(
                                familyName);

                familyTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 28px;" +
                                                "-fx-font-weight: bold;");

                Button backButton = new Button(
                                "←  Back");

                backButton.setPrefHeight(
                                36);

                backButton.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-text-fill: #CBD5E1;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;" +
                                                "-fx-font-weight: bold;");

                backButton.setOnAction(
                                event -> showClientsPage());

                HBox header = new HBox(
                                15,
                                backButton,
                                familyTitle);

                header.setAlignment(
                                Pos.CENTER_LEFT);

                // =====================================================
                // CONTENT
                // =====================================================

                VBox content = new VBox(20);

                content.setPadding(
                                new Insets(20, 0, 20, 0));

                // =====================================================
                // FAMILY MEMBERS TITLE
                // =====================================================

                Label membersTitle = new Label(
                                "Family Members");

                membersTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // MEMBERS ROW
                // =====================================================

                HBox membersRow = new HBox(15);

                FamilyMemberDao familyMemberDao = new FamilyMemberDao();

                List<FamilyMember> familyMembers = familyMemberDao.getFamilyMembersByUserId(userId);

                for (FamilyMember member : familyMembers) {

                        String allergies = member.getAllergy();
                        String healthIssue = member.getHealthIssue();

                        if (allergies == null || allergies.trim().isEmpty()) {
                                allergies = "None";
                        }

                        if (healthIssue == null || healthIssue.trim().isEmpty()) {
                                healthIssue = "None";
                        }

                        membersRow.getChildren().add(
                                        createMemberCard(
                                                        member.getName(),
                                                        String.valueOf(member.getAge()),
                                                        String.valueOf(member.getHeight()),
                                                        String.valueOf(member.getWeight()),
                                                        allergies,
                                                        healthIssue));
                }

                // =====================================================
                // WEEKLY PLAN
                // =====================================================

                HBox weeklyTitleRow = new HBox();

                weeklyTitleRow.setAlignment(Pos.CENTER_LEFT);
                weeklyTitleRow.setPrefWidth(Double.MAX_VALUE);

                Label weeklyTitle = new Label("Weekly Plan");

                weeklyTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                Region weeklySpacer = new Region();
                HBox.setHgrow(weeklySpacer, Priority.ALWAYS);

                Button createPlanButton = new Button("+ Create Plan");
                System.out.println("CREATE PLAN BUTTON CREATED");

                createPlanButton.setPrefWidth(140);
                createPlanButton.setPrefHeight(42);
                createPlanButton.setStyle(
                                "-fx-background-color: #1976D2;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");
                createPlanButton.setOnAction(event -> {

                        DietitianWeeklyMealPlanner planner = new DietitianWeeklyMealPlanner(
                                        featurePane,
                                        familyName,
                                        userId,
                                        () -> showFamilyDetails(familyName, userId));

                        planner.showCreateWeeklyPlan();
                });

                weeklyTitleRow.getChildren().addAll(
                                weeklyTitle,
                                weeklySpacer,
                                createPlanButton);

                // =====================================================
                // WEEKLY PLAN PREVIEW
                // =====================================================

                VBox weeklyPlan = createWeeklyPlanPreview(userId);

                // =====================================================
                // NUTRITION ANALYSIS
                // =====================================================

                VBox nutritionCard = createNutritionCard(userId);
                ;

                // =====================================================
                // RATINGS + REVIEWS
                // =====================================================


                content.getChildren().addAll(
                                membersTitle,
                                membersRow,
                                weeklyTitleRow,
                                weeklyPlan,
                                nutritionCard);

                ScrollPane scrollPane = new ScrollPane(
                                content);

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

                detailsPane.setTop(
                                header);

                detailsPane.setCenter(
                                scrollPane);

                featurePane.setCenter(
                                detailsPane);
        }

        // =========================================================
        // MEMBER CARD
        // =========================================================

        private VBox createMemberCard(
                        String name,
                        String age,
                        String height,
                        String weight,
                        String allergies,
                        String healthIssue) {

                VBox card = new VBox(10);

                card.setPrefWidth(
                                270);

                card.setPadding(
                                new Insets(16));

                card.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-border-color: #1F2937;" +
                                                "-fx-border-radius: 9;" +
                                                "-fx-background-radius: 9;");

                Label nameLabel = new Label(name);

                nameLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 16px;" +
                                                "-fx-font-weight: bold;");

                Label info = new Label(
                                "Age: " + age +
                                                "\nHeight: " + height +
                                                "\nWeight: " + weight +
                                                "\nAllergies: " + allergies +
                                                "\nHealth Issue: " + healthIssue);

                info.setStyle(
                                "-fx-text-fill: #94A3B8;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-line-spacing: 4;");

                card.getChildren().addAll(
                                nameLabel,
                                info);

                return card;
        }

        // =========================================================
        // WEEKLY PLAN PREVIEW
        // =========================================================

        private VBox createWeeklyPlanPreview(String userId) {

                VBox card = new VBox(12);

                card.setPadding(new Insets(18));

                card.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-border-color: #1F2937;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                try {

                        DietitianMealPlanDao dao = new DietitianMealPlanDao();

                        DocumentSnapshot doc = dao.getPlan(userId);

                        if (!doc.exists()) {

                                card.getChildren().add(
                                                new Label("No meal plan created yet."));

                                return card;
                        }

                        String mealPlanJson = doc.getString("mealPlanJson");

                        if (mealPlanJson == null ||
                                        mealPlanJson.isBlank()) {

                                card.getChildren().add(
                                                new Label("No meal plan available."));

                                return card;
                        }

                        JsonObject root = new Gson().fromJson(
                                        mealPlanJson,
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

                                if (dayObj == null)
                                        continue;

                                String breakfast = dayObj.getAsJsonObject("breakfast")
                                                .get("meal")
                                                .getAsString();

                                String lunch = dayObj.getAsJsonObject("lunch")
                                                .get("meal")
                                                .getAsString();

                                String dinner = dayObj.getAsJsonObject("dinner")
                                                .get("meal")
                                                .getAsString();

                                card.getChildren().add(
                                                createPreviewDay(
                                                                day,
                                                                breakfast,
                                                                lunch,
                                                                dinner));
                        }

                } catch (Exception e) {

                        e.printStackTrace();

                        Label error = new Label("Failed to load meal plan");

                        error.setStyle("-fx-text-fill: red;");

                        card.getChildren().add(error);
                }

                return card;
        }

        // =========================================================
        // PREVIEW DAY
        // =========================================================

        private VBox createPreviewDay(
                        String day,
                        String breakfast,
                        String lunch,
                        String dinner) {

                VBox dayBox = new VBox(8);

                Label dayLabel = new Label(
                                day.toUpperCase());

                dayLabel.setStyle(
                                "-fx-text-fill: #60A5FA;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;");

                GridPane meals = new GridPane();

                meals.setHgap(10);
                meals.setMaxWidth(Double.MAX_VALUE);

                ColumnConstraints col = new ColumnConstraints();
                col.setPercentWidth(33.33);

                meals.getColumnConstraints().addAll(
                                col,
                                col,
                                col);

                meals.add(createMealPreview("Breakfast", breakfast), 0, 0);
                meals.add(createMealPreview("Lunch", lunch), 1, 0);
                meals.add(createMealPreview("Dinner", dinner), 2, 0);

                dayBox.getChildren().addAll(
                                dayLabel,
                                meals);

                return dayBox;
        }

        // =========================================================
        // MEAL PREVIEW
        // =========================================================

        private VBox createMealPreview(
                        String type,
                        String meal) {

                VBox box = new VBox(5);

                HBox.setHgrow(
                                box,
                                Priority.ALWAYS);

                box.setPadding(
                                new Insets(12));

                box.setStyle(
                                "-fx-background-color: #0F172A;" +
                                                "-fx-border-color: #334155;" +
                                                "-fx-border-radius: 7;" +
                                                "-fx-background-radius: 7;");
                HBox.setHgrow(box, Priority.ALWAYS);

                box.setMaxWidth(Double.MAX_VALUE);
                box.setMinWidth(0);
                box.setMinHeight(80);
                box.setMaxHeight(80);
                box.setAlignment(Pos.TOP_LEFT);

                Label typeLabel = new Label(
                                type.toUpperCase());

                typeLabel.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 9px;" +
                                                "-fx-font-weight: bold;");

                Label mealLabel = new Label(
                                meal);
                mealLabel.setWrapText(true);
                mealLabel.setMaxWidth(Double.MAX_VALUE);

                mealLabel.setStyle(
                                "-fx-text-fill: #E2E8F0;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;");

                box.getChildren().addAll(
                                typeLabel,
                                mealLabel);

                return box;
        }

        // =========================================================
        // NUTRITION CARD
        // =========================================================

        private VBox createNutritionCard(String userId) {

                VBox card = new VBox(15);

                card.setPadding(new Insets(18));

                card.setStyle(
                                "-fx-background-color: #121D35;" +
                                                "-fx-border-color: #1F2937;" +
                                                "-fx-border-radius: 10;" +
                                                "-fx-background-radius: 10;");

                Label title = new Label("Nutrition Analysis");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                double totalCalories = 0;
                double totalProtein = 0;
                double totalCarbs = 0;

                int mealCount = 0;

                try {

                        DietitianMealPlanDao dao = new DietitianMealPlanDao();

                        DocumentSnapshot doc = dao.getPlan(userId);

                        if (doc.exists()) {

                                String mealPlanJson = doc.getString("mealPlanJson");

                                JsonObject root = JsonParser.parseString(mealPlanJson)
                                                .getAsJsonObject();

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

                                        if (dayObj == null)
                                                continue;

                                        String[] meals = {
                                                        "breakfast",
                                                        "lunch",
                                                        "dinner"
                                        };

                                        for (String meal : meals) {

                                                JsonObject mealObj = dayObj.getAsJsonObject(meal);

                                                if (mealObj == null)
                                                        continue;

                                                totalCalories += extractNumber(
                                                                mealObj.get("calories").getAsString());

                                                totalProtein += extractNumber(
                                                                mealObj.get("protein").getAsString());

                                                totalCarbs += extractNumber(
                                                                mealObj.get("carbs").getAsString());

                                                mealCount++;
                                        }
                                }
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                }

                double avgCalories = mealCount == 0 ? 0 : totalCalories / mealCount;

                double avgProtein = mealCount == 0 ? 0 : totalProtein / mealCount;

                double avgCarbs = mealCount == 0 ? 0 : totalCarbs / mealCount;

                HBox stats = new HBox(50);

                stats.getChildren().addAll(

                                createNutritionStat(
                                                "AVG CALORIES",
                                                String.format("%.0f kcal", avgCalories),
                                                "#60A5FA"),

                                createNutritionStat(
                                                "AVG PROTEIN",
                                                String.format("%.1f g", avgProtein),
                                                "#22C55E"),

                                createNutritionStat(
                                                "AVG CARBS",
                                                String.format("%.1f g", avgCarbs),
                                                "#F59E0B"));

                card.getChildren().addAll(
                                title,
                                stats);

                return card;
        }

        private double extractNumber(String value) {

                if (value == null)
                        return 0;

                try {

                        return Double.parseDouble(
                                        value.replaceAll("[^0-9.]", ""));

                } catch (Exception e) {

                        return 0;
                }
        }
        // =========================================================
        // NUTRITION STAT
        // =========================================================

        private VBox createNutritionStat(
                        String title,
                        String value,
                        String color) {

                VBox box = new VBox(5);

                Label titleLabel = new Label(title);

                titleLabel.setStyle(
                                "-fx-text-fill: #64748B;" +
                                                "-fx-font-size: 9px;" +
                                                "-fx-font-weight: bold;");

                Label valueLabel = new Label(value);

                valueLabel.setStyle(
                                "-fx-text-fill: " +
                                                color + ";" +
                                                "-fx-font-size: 15px;" +
                                                "-fx-font-weight: bold;");

                box.getChildren().addAll(
                                titleLabel,
                                valueLabel);

                return box;
        }

        // =========================================================
        // REVIEWS CARD
        // =========================================================

      
}