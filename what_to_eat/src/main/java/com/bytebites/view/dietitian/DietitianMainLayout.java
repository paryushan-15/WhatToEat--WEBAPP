package com.bytebites.view.dietitian;

import java.util.function.Supplier;

import com.bytebites.controller.AuthController;
import com.bytebites.model.session.SessionManager;
import com.bytebites.view.common.LoadingOverlay;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianClients;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianConsultation;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianDashBoard;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianMyRecipes;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianRevenue;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSettings;
import com.bytebites.view.dietitian.dietitionFeatures.DietitianSocialFeed;
import com.bytebites.view.login.MainLoginPage;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import com.bytebites.view.common.SameStageAlert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/** Dietitian shell using the same structure and mouse behavior as AdminMainLayout. */
public class DietitianMainLayout {

    private static final String BACKGROUND = "#0F172A";
    private static final String SIDEBAR_BACKGROUND = "#121D35";
    private static final String CARD_BACKGROUND = "#1E293B";
    private static final String PRIMARY = "#1976D2";
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
    private Button clientsButton;
    private Button weeklyPlansButton;
    private Button socialButton;
    private Button recipesButton;
    private Button consultationButton;
    private Button revenueButton;
    private Button settingsButton;
    private Button logoutButton;

    public Scene getDietitionMainLayout() {
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

        createSidebar();
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

        Label section = new Label("PRACTICE");
        section.setStyle(
                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );
        VBox.setMargin(section, new Insets(20, 20, 8, 22));

        VBox navigation = new VBox(5);
        navigation.setPadding(new Insets(0, 12, 0, 12));

        dashboardButton = createNavigationButton("▦", "Dashboard");
        clientsButton = createNavigationButton("♙", "Clients");
        weeklyPlansButton = createNavigationButton("🍴", "Weekly Meal Plans");
        socialButton = createNavigationButton("♧", "Social Feed");
        recipesButton = createNavigationButton("▤", "My Recipes");
        consultationButton = createNavigationButton("☏", "Consultation");
        revenueButton = createNavigationButton("₹", "Revenue");

        navigation.getChildren().addAll(
                dashboardButton,
                clientsButton,
                weeklyPlansButton,
                socialButton,
                recipesButton,
                consultationButton,
                revenueButton
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

        Label role = new Label("DIETITIAN PORTAL");
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
        applyProfileStyle(profile, false);

        String dietitianName = safeName(SessionManager.getName(), "Dietitian");

        StackPane avatar = new StackPane();
        avatar.setPrefSize(42, 42);
        avatar.setMinSize(42, 42);
        avatar.setMaxSize(42, 42);
        avatar.setStyle("-fx-background-color: " + PRIMARY + "; -fx-background-radius: 50;");
        Label letter = new Label(dietitianName.substring(0, 1).toUpperCase());
        letter.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");
        avatar.getChildren().add(letter);

        VBox text = new VBox(2);
        Label name = new Label(dietitianName);
        name.setMaxWidth(150);
        name.setStyle(
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );
        Label role = new Label("Dietitian");
        role.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 11px;");
        text.getChildren().addAll(name, role);

        profile.getChildren().addAll(avatar, text);
        profile.setOnMouseEntered(e -> applyProfileStyle(profile, true));
        profile.setOnMouseExited(e -> applyProfileStyle(profile, false));
        profile.setOnMouseClicked(e -> openSettings());

        outer.getChildren().add(profile);
        return outer;
    }

    private void applyProfileStyle(HBox profile, boolean hover) {
        profile.setStyle(
                "-fx-background-color: " + (hover ? "#26354E" : CARD_BACKGROUND) + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + (hover ? PRIMARY : BORDER) + ";" +
                "-fx-cursor: hand;"
        );
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
                "-fx-text-fill: white;" +
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

    /** Lets nested Dietitian screens keep the Clients nav item selected. */
    public void selectClientsNavigation() {
        selectButton(clientsButton);
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
        clientsButton.setOnAction(e -> openClients());
        weeklyPlansButton.setOnAction(e -> openWeeklyPlans());
        socialButton.setOnAction(e -> openSocialFeed());
        recipesButton.setOnAction(e -> openRecipes());
        consultationButton.setOnAction(e -> openConsultation());
        revenueButton.setOnAction(e -> openRevenue());
        settingsButton.setOnAction(e -> openSettings());
        logoutButton.setOnAction(e -> logout());
    }

    public void openDashboard() {
        selectButton(dashboardButton);
        showPageWithLoader("Loading dietitian dashboard...", () -> new DietitianDashBoard().getDashboard());
    }

    public void openClients() {
        selectButton(clientsButton);
        showPageWithLoader("Loading clients...", () -> new DietitianClients().getClients());
    }

    public void openWeeklyPlans() {
        selectButton(weeklyPlansButton);
        showPageWithLoader("Loading meal-plan workspace...", this::createMealPlanLandingPage);
    }

    private Node createMealPlanLandingPage() {
        VBox box = new VBox(14);
        box.setPadding(new Insets(35));
        box.setStyle("-fx-background-color: " + BACKGROUND + ";");

        Label title = new Label("Weekly Meal Plans");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        Label subtitle = new Label("Choose a client first, then open their family profile to create or edit the weekly plan.");
        subtitle.setWrapText(true);
        subtitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px;");

        Button chooseClient = new Button("Open Clients");
        chooseClient.setStyle(
                "-fx-background-color: " + PRIMARY + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-padding: 10 18;" +
                "-fx-cursor: hand;"
        );
        chooseClient.setOnAction(e -> openClients());
        box.getChildren().addAll(title, subtitle, chooseClient);
        return box;
    }

    public void openSocialFeed() {
        selectButton(socialButton);
        showPageWithLoader("Loading social feed...", () -> new DietitianSocialFeed().getSocialFeed());
    }

    public void openRecipes() {
        selectButton(recipesButton);
        showPageWithLoader("Loading recipes...", () -> new DietitianMyRecipes().getMyRecipes());
    }

    public void openConsultation() {
        selectButton(consultationButton);
        showPageWithLoader("Loading consultations...", () -> new DietitianConsultation().getConsultationPage());
    }

    public void openRevenue() {
        selectButton(revenueButton);
        showPageWithLoader("Loading revenue...", () -> new DietitianRevenue().getRevenuePage());
    }

    public void openSettings() {
        selectButton(settingsButton);
        showPageWithLoader("Loading settings...", () -> new DietitianSettings().getSettings());
    }

    public void showPage(Node page) {
        if (page != null) {
            featurePane.setCenter(page);
        }
    }

    private void showPageWithLoader(String message, Supplier<Node> pageSupplier) {
        loadingOverlay.show(message);
        sidebar.setDisable(true);

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
                sidebar.setDisable(false);
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

    private void logout() {
        SameStageAlert confirmation = new SameStageAlert(SameStageAlert.AlertType.CONFIRMATION);
        confirmation.setPrimaryColor(PRIMARY);
        confirmation.setPrimaryTextColor("white");
        confirmation.setTitle("Logout");
        confirmation.setHeaderText("Logout from Dietitian Portal?");
        confirmation.setContentText("Your current dietitian session will be closed.");
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
