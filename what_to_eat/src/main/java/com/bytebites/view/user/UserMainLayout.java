package com.bytebites.view.user;

import java.util.function.Supplier;

import com.bytebites.controller.AuthController;
import com.bytebites.model.session.SessionManager;
import com.bytebites.view.common.LoadingOverlay;
import com.bytebites.view.login.MainLoginPage;
import com.bytebites.view.user.userFeatures.UserDashBoard;
import com.bytebites.view.user.userFeatures.UserDietitian;
import com.bytebites.view.user.userFeatures.UserFamilyProfile;
import com.bytebites.view.user.userFeatures.UserMyRecipes;
import com.bytebites.view.user.userFeatures.UserPantryGrocery;
import com.bytebites.view.user.userFeatures.UserSettings;
import com.bytebites.view.user.userFeatures.UserSocialFeed;
import com.bytebites.view.user.userFeatures.UserWeeklyMealPlanner;
import com.bytebites.view.user.userFeatures.UserWhatToCookToday;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * User shell aligned with AdminMainLayout.
 * Uses the same button navigation structure, hover/active states,
 * profile card, bottom actions and page-loading overlay.
 */
public class UserMainLayout {

    private static final String BACKGROUND = "#0F172A";
    private static final String SIDEBAR_BACKGROUND = "#121D35";
    private static final String CARD_BACKGROUND = "#1E293B";
    private static final String PRIMARY = "#22C55E";
    private static final String TEXT_PRIMARY = "#F8FAFC";
    private static final String TEXT_SECONDARY = "#94A3B8";
    private static final String BORDER = "#334155";
    private static final String DANGER = "#EF4444";

    private final AuthController authController = new AuthController();

    private BorderPane root;
    private BorderPane featurePane;
    private StackPane contentStack;
    private LoadingOverlay loadingOverlay;
    private VBox sidebar;
    private Scene scene;

    private Button activeButton;
    private Button dashboardButton;
    private Button cookTodayButton;
    private Button weeklyPlanButton;
    private Button pantryButton;
    private Button socialButton;
    private Button recipesButton;
    private Button familyButton;
    private Button dietitiansButton;
    private Button settingsButton;
    private Button logoutButton;

    // Compatibility bridge for UserDashBoard quick-action navigation.
    // It is not shown on screen anymore.
    private final ListView<String> dashboardNavigationBridge = new ListView<>();

    public Scene getUserMainLayout() {
        if (scene != null) {
            return scene;
        }

        root = new BorderPane();
        root.setStyle("-fx-background-color: " + BACKGROUND + ";");

        featurePane = new BorderPane();
        featurePane.setMinWidth(0);
        featurePane.setMinHeight(0);
        featurePane.setStyle("-fx-background-color: " + BACKGROUND + ";");

        loadingOverlay = new LoadingOverlay(PRIMARY);
        contentStack = new StackPane(featurePane, loadingOverlay);
        StackPane.setAlignment(loadingOverlay, Pos.CENTER);

        createSidebar();
        configureDashboardNavigationBridge();

        root.setLeft(sidebar);
        root.setCenter(contentStack);

        scene = new Scene(root, 1550, 830);
        openDashboard();
        return scene;
    }

    public BorderPane getFeaturePane() {
        return featurePane;
    }

    private void createSidebar() {
        sidebar = new VBox();
        sidebar.setPrefWidth(260);
        sidebar.setMinWidth(260);
        sidebar.setMaxWidth(260);
        sidebar.setStyle(
                "-fx-background-color: " + SIDEBAR_BACKGROUND + ";" +
                "-fx-border-color: transparent " + BORDER + " transparent transparent;"
        );

        VBox brandBox = createBrandBox();
        VBox profile = createProfileBox();

        Label section = new Label("YOUR SPACE");
        section.setStyle(
                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );
        VBox.setMargin(section, new Insets(20, 20, 8, 22));

        VBox navigation = new VBox(5);
        navigation.setPadding(new Insets(0, 12, 0, 12));

        dashboardButton = createNavigationButton("▦", "Dashboard");
        cookTodayButton = createNavigationButton("🍴", "What To Cook Today");
        weeklyPlanButton = createNavigationButton("▦", "Weekly Meal Planner");
        pantryButton = createNavigationButton("▣", "Pantry");
        socialButton = createNavigationButton("♧", "Social Feed");
        recipesButton = createNavigationButton("▤", "My Recipes");
        familyButton = createNavigationButton("♟", "Family");
        dietitiansButton = createNavigationButton("♙", "Dietitians");

        navigation.getChildren().addAll(
                dashboardButton,
                cookTodayButton,
                weeklyPlanButton,
                pantryButton,
                socialButton,
                recipesButton,
                familyButton,
                dietitiansButton
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottom = new VBox(5);
        bottom.setPadding(new Insets(10, 12, 18, 12));
        settingsButton = createNavigationButton("⚙", "Settings");
        logoutButton = createLogoutButton();
        bottom.getChildren().addAll(settingsButton, logoutButton);

        sidebar.getChildren().addAll(brandBox, profile, section, navigation, spacer, bottom);
        setupNavigationActions();
    }

    private VBox createBrandBox() {
        VBox box = new VBox(3);
        box.setPadding(new Insets(25, 22, 22, 22));

        Label title = new Label("What To Eat?");
        title.setFont(Font.font("System", FontWeight.BOLD, 23));
        title.setStyle("-fx-text-fill: white;");

        Label role = new Label("FAMILY APP");
        role.setFont(Font.font("System", FontWeight.BOLD, 10));
        role.setStyle("-fx-text-fill: " + PRIMARY + "; -fx-letter-spacing: 1px;");

        box.getChildren().addAll(title, role);
        return box;
    }

    private VBox createProfileBox() {
        VBox outer = new VBox();
        outer.setPadding(new Insets(0, 14, 5, 14));

        HBox profile = new HBox(12);
        profile.setAlignment(Pos.CENTER_LEFT);
        profile.setPadding(new Insets(14));
        profile.setStyle(
                "-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-cursor: hand;"
        );

        String userName = safeName(SessionManager.getName(), "User");
        StackPane avatar = createAvatar(userName);

        VBox text = new VBox(2);
        Label name = new Label(userName);
        name.setMaxWidth(150);
        name.setStyle(
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );
        Label role = new Label("Family Account");
        role.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 11px;");
        text.getChildren().addAll(name, role);

        profile.getChildren().addAll(avatar, text);
        profile.setOnMouseEntered(e -> profile.setStyle(
                "-fx-background-color: #26354E;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + PRIMARY + ";" +
                "-fx-cursor: hand;"
        ));
        profile.setOnMouseExited(e -> profile.setStyle(
                "-fx-background-color: " + CARD_BACKGROUND + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-cursor: hand;"
        ));
        profile.setOnMouseClicked(e -> openSettings());

        outer.getChildren().add(profile);
        return outer;
    }

    private StackPane createAvatar(String name) {
        StackPane avatar = new StackPane();
        avatar.setPrefSize(42, 42);
        avatar.setMinSize(42, 42);
        avatar.setMaxSize(42, 42);
        avatar.setStyle("-fx-background-color: " + PRIMARY + "; -fx-background-radius: 50;");

        Label letter = new Label(name.substring(0, 1).toUpperCase());
        letter.setStyle("-fx-text-fill: #052E16; -fx-font-size: 17px; -fx-font-weight: bold;");
        avatar.getChildren().add(letter);
        return avatar;
    }

    private Button createNavigationButton(String icon, String text) {
        Button button = new Button(icon + "    " + text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(46);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));
        applyInactiveStyle(button);

        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(
                        "-fx-background-color: #1E293B;" +
                        "-fx-background-radius: 9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: 600;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: transparent;"
                );
            }
        });
        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                applyInactiveStyle(button);
            }
        });
        return button;
    }

    private void applyActiveStyle(Button button) {
        button.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;"
        );
    }

    private void applyInactiveStyle(Button button) {
        button.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 600;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;"
        );
    }

    private void selectButton(Button selected) {
        if (activeButton != null) {
            applyInactiveStyle(activeButton);
        }
        activeButton = selected;
        applyActiveStyle(selected);
    }

    private Button createLogoutButton() {
        Button button = new Button("↪    Logout");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(46);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0, 16, 0, 16));
        applyLogoutStyle(button, false);
        button.setOnMouseEntered(e -> applyLogoutStyle(button, true));
        button.setOnMouseExited(e -> applyLogoutStyle(button, false));
        return button;
    }

    private void applyLogoutStyle(Button button, boolean hover) {
        button.setStyle(
                "-fx-background-color: " + (hover ? "#3B1720" : "transparent") + ";" +
                "-fx-background-radius: 9;" +
                "-fx-text-fill: " + (hover ? "#F87171" : DANGER) + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;"
        );
    }

    private void setupNavigationActions() {
        dashboardButton.setOnAction(e -> openDashboard());
        cookTodayButton.setOnAction(e -> openCookToday());
        weeklyPlanButton.setOnAction(e -> openWeeklyPlan());
        pantryButton.setOnAction(e -> openPantry());
        socialButton.setOnAction(e -> openSocialFeed());
        recipesButton.setOnAction(e -> openRecipes());
        familyButton.setOnAction(e -> openFamily());
        dietitiansButton.setOnAction(e -> openDietitians());
        settingsButton.setOnAction(e -> openSettings());
        logoutButton.setOnAction(e -> logout());
    }

    private void configureDashboardNavigationBridge() {
        dashboardNavigationBridge.getItems().setAll(
                "▦   Dashboard",
                "🍴  What To Cook Today",
                "▦   Weekly Meal Planner",
                "▣   Pantry",
                "♧   Social Feed",
                "▤   My Recipes",
                "♟   Family",
                "♙   Dietitians"
        );
        dashboardNavigationBridge.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, item) -> {
            if (item == null) return;
            if (item.contains("What To Cook Today")) selectButton(cookTodayButton);
            else if (item.contains("Weekly Meal Planner")) selectButton(weeklyPlanButton);
            else if (item.contains("Pantry")) selectButton(pantryButton);
            else if (item.contains("Social Feed")) selectButton(socialButton);
            else if (item.contains("My Recipes")) selectButton(recipesButton);
            else if (item.contains("Family")) selectButton(familyButton);
            else if (item.contains("Dietitians")) selectButton(dietitiansButton);
            else if (item.contains("Dashboard")) selectButton(dashboardButton);
        });
    }

    public void openDashboard() {
        selectButton(dashboardButton);
        showPageWithLoader("Loading dashboard...", () -> new UserDashBoard(featurePane, dashboardNavigationBridge).getDashboard());
    }

    public void openCookToday() {
        selectButton(cookTodayButton);
        showPageWithLoader("Finding today's meals...", () -> new UserWhatToCookToday().getMealDetails());
    }

    public void openWeeklyPlan() {
        selectButton(weeklyPlanButton);
        showPageWithLoader("Loading weekly meal plan...", () -> new UserWeeklyMealPlanner().getWeeklyPlanner());
    }

    public void openPantry() {
        selectButton(pantryButton);
        showPageWithLoader("Loading pantry and grocery list...", () -> new UserPantryGrocery().getPantryGrocery());
    }

    public void openSocialFeed() {
        selectButton(socialButton);
        showPageWithLoader("Loading social feed...", () -> new UserSocialFeed().getSocialFeed());
    }

    public void openRecipes() {
        selectButton(recipesButton);
        showPageWithLoader("Loading recipes...", () -> new UserMyRecipes().getMyRecipes());
    }

    public void openFamily() {
        selectButton(familyButton);
        showPageWithLoader("Loading family profile...", () -> new UserFamilyProfile().getFamilyProfile());
    }

    public void openDietitians() {
        selectButton(dietitiansButton);
        showPageWithLoader("Loading dietitians...", () -> new UserDietitian(featurePane).getDietitianPage());
    }

    public void openSettings() {
        selectButton(settingsButton);
        showPageWithLoader("Loading settings...",() -> new UserSettings(featurePane,this::openFamily).getSettings());
    }

    public void showPage(Node page) {
        if (page != null) {
            featurePane.setCenter(page);
        }
    }

    private void showPageWithLoader(String message, Supplier<Node> pageSupplier) {
        loadingOverlay.show(message);
        setNavigationDisabled(true);

        // One short JavaFX pulse lets the overlay become visible before legacy
        // feature pages perform any synchronous Firestore/API work.
        PauseTransition pulse = new PauseTransition(Duration.millis(60));
        pulse.setOnFinished(e -> {
            try {
                Node page = pageSupplier.get();
                if (page != null) {
                    featurePane.setCenter(page);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                featurePane.setCenter(createErrorPage("Unable to load this page."));
            } finally {
                loadingOverlay.hide();
                setNavigationDisabled(false);
            }
        });
        pulse.play();
    }

    private Node createErrorPage(String message) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        Label title = new Label("Something went wrong");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        Label detail = new Label(message);
        detail.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 13px;");
        box.getChildren().addAll(title, detail);
        return box;
    }

    private void setNavigationDisabled(boolean disabled) {
        sidebar.setDisable(disabled);
    }

    private void logout() {
        SameStageAlert confirmation = new SameStageAlert(SameStageAlert.AlertType.CONFIRMATION);
        confirmation.setPrimaryColor(PRIMARY);
        confirmation.setPrimaryTextColor("#052E16");
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Logout from What To Eat?");
        confirmation.setContentText("Your current user session will be closed.");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                authController.logout();
                MainLoginPage.navigateToMainLoginPage();
            }
        });
    }

    private static String safeName(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
