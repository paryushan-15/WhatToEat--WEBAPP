package com.bytebites.view.login;

import com.bytebites.view.admin.AdminMainLayout;
import com.bytebites.view.dietitian.DietitianMainLayout;
import com.bytebites.view.user.UserMainLayout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainLoginPage extends Application {

        public static Stage Mainstage;
        private Scene Mainscene;

        @Override
        public void start(Stage Mainstage) throws Exception {

                MainLoginPage.Mainstage = Mainstage;

                // LOGO

                Image logoImage = new Image("assets\\icon\\image.png");
                ImageView viewImage = new ImageView(logoImage);

                viewImage.setFitWidth(300);
                viewImage.setFitHeight(300);
                viewImage.setPreserveRatio(true);

                // TITLE

                Label welcomeLabel = new Label("Welcome to WTE?");
                welcomeLabel.setStyle(
                                "-fx-font-weight: bold;" +
                                                "-fx-font-size: 24px;");

                welcomeLabel.setTextFill(Color.WHITE);

                Label subtitle = new Label("Choose how you want to continue");
                subtitle.setStyle(
                                "-fx-font-size: 13px;");

                subtitle.setTextFill(Color.web("#B8C0D0"));

                VBox titlesubtitleBox = new VBox(8, welcomeLabel, subtitle);
                titlesubtitleBox.setAlignment(Pos.CENTER);

                // DECORATION

                Label leaf = new Label("❧");

                leaf.setStyle("-fx-font-size: 28px;" +
                                "-fx-text-fill: #22C55E;");

                Label line1 = new Label("━━━━━━");
                line1.setTextFill(Color.web("#22C55E"));

                Label line2 = new Label("━━━━━━");
                line2.setTextFill(Color.web("#22C55E"));

                HBox leafdecoration = new HBox(8, line1, leaf, line2);
                leafdecoration.setAlignment(Pos.CENTER);

                // USER CARD

                Label userIcon = new Label("👤");
                userIcon.setStyle("-fx-font-size: 40px;");

                Label userLabel = new Label("User");

                userLabel.setTextFill(Color.WHITE);
                userLabel.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;");

                Label userLogin = new Label("Login as User");

                userLogin.setTextFill(Color.WHITE);
                userLogin.setStyle("-fx-font-size: 15px;");

                Button userButton = new Button("Continue →");

                userButton.setPrefWidth(200);
                userButton.setPrefHeight(40);

                userButton.setStyle(
                                "-fx-background-color: #22C55E;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;");

                // USER -> SIGNUP

                userButton.setOnAction(e -> {

                        openSignupPage(
                                        "User",
                                        "#22C55E");
                });

                VBox userBox = new VBox(15, userIcon, userLabel, userLogin, userButton);

                userBox.setAlignment(Pos.CENTER);

                userBox.setPrefWidth(220);
                userBox.setPrefHeight(270);
                userBox.setPadding(new Insets(25));
                userBox.setStyle(
                                "-fx-background-color: #081227;" +
                                                "-fx-border-color: #22C55E;" +
                                                "-fx-border-width: 1.5px;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-background-radius: 15px;");

                // DIETITIAN CARD

                Label dietitianIcon = new Label("🥗");
                dietitianIcon.setStyle("-fx-font-size: 40px;");

                Label dietitianLabel = new Label("Dietitian");
                dietitianLabel.setTextFill(Color.WHITE);
                dietitianLabel.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;");

                Label dietitianLogin = new Label("Login as Dietitian");
                dietitianLogin.setTextFill(Color.WHITE);
                dietitianLogin.setStyle("-fx-font-size: 15px;");

                Button dietitianButton = new Button("Continue →");

                dietitianButton.setPrefWidth(200);
                dietitianButton.setPrefHeight(40);

                dietitianButton.setStyle(
                                "-fx-background-color: #1976D2;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;");

                // DIETITIAN -> SIGNUP

                dietitianButton.setOnAction(e -> {

                        openSignupPage("Dietitian", "#1976D2");
                });

                VBox dietitianBox = new VBox(15, dietitianIcon, dietitianLabel, dietitianLogin, dietitianButton);

                dietitianBox.setAlignment(Pos.CENTER);

                dietitianBox.setPrefWidth(220);
                dietitianBox.setPrefHeight(270);
                dietitianBox.setPadding(new Insets(25));
                dietitianBox.setStyle(
                                "-fx-background-color: #081227;" +
                                                "-fx-border-color: #1976D2;" +
                                                "-fx-border-width: 1.5px;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-background-radius: 15px;");

                // ADMIN CARD

                Label adminIcon = new Label("🛡");
                adminIcon.setStyle("-fx-font-size: 40px;");

                Label adminLabel = new Label("Admin");

                adminLabel.setTextFill(Color.WHITE);
                adminLabel.setStyle(
                                "-fx-font-size: 22px;" +
                                                "-fx-font-weight: bold;");

                Label adminLogin = new Label("Login as Admin");
                adminLogin.setTextFill(Color.WHITE);
                adminLogin.setStyle("-fx-font-size: 15px;");

                Button adminButton = new Button("Continue →");

                adminButton.setPrefWidth(200);
                adminButton.setPrefHeight(40);
                adminButton.setStyle(
                                "-fx-background-color: #F68F32;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;");

                // ADMIN -> SIGNUP
                adminButton.setOnAction(e -> {
                        openLoginPage("Admin", "#F68F32");
                });

                VBox adminBox = new VBox(15, adminIcon, adminLabel, adminLogin, adminButton);

                adminBox.setAlignment(Pos.CENTER);
                adminBox.setPrefWidth(220);
                adminBox.setPrefHeight(270);
                adminBox.setPadding(new Insets(25));
                adminBox.setStyle(
                                "-fx-background-color: #081227;" +
                                                "-fx-border-color: #F68F32;" +
                                                "-fx-border-width: 1.5px;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-background-radius: 15px;");

                // CARDS

                HBox loginCards = new HBox(15, userBox, dietitianBox, adminBox);
                loginCards.setAlignment(Pos.CENTER);

                // MAIN CARD
                VBox decorationBox = new VBox(12, viewImage, titlesubtitleBox, leafdecoration, loginCards);

                decorationBox.setAlignment(Pos.CENTER);
                decorationBox.setPadding(new Insets(30));
                decorationBox.setStyle(
                                "-fx-background-color: #081227;" +
                                                "-fx-border-color: #29332D;" +
                                                "-fx-border-width: 1.5px;" +
                                                "-fx-border-radius: 15px;" +
                                                "-fx-background-radius: 15px;");

                HBox finalfinalHBox = new HBox(decorationBox);

                finalfinalHBox.setStyle("-fx-background-color: #0F172A;");
                finalfinalHBox.setAlignment(Pos.CENTER);
                finalfinalHBox.setPadding(new Insets(30));

                // SCENE
                Mainscene = new Scene(finalfinalHBox, 1550, 830);
                Mainscene.setFill(Color.web("#0F172A"));

                Mainstage.setScene(Mainscene);
                Mainstage.show();
        }

        // OPEN SIGNUP PAGE
        public static void openSignupPage(String actor, String themeColor) {
                SignupPage signupPage = new SignupPage(actor, themeColor);
                Mainstage.setScene(signupPage.getUserSignupScene());
        }

        // OPEN LOGIN PAGE
        public static void openLoginPage(String actor, String themeColor) {
                LoginPage loginPage = new LoginPage(actor, themeColor);
                Mainstage.setScene(loginPage.getLoginScene());
        }

        // DASHBOARD NAVIGATION
        // =====================================================

        public static void navigateToDashboard(String actor) {
                switch (actor.toLowerCase()) {

                        case "user":
                                UserMainLayout userDashboard = new UserMainLayout();
                                Mainstage.setScene(userDashboard.getUserMainLayout());
                                break;

                        case "dietitian":

                                System.out.println("Opening Dietitian Dashboard");

                                DietitianMainLayout dietitionDashboard = new DietitianMainLayout();
                                Mainstage.setScene(dietitionDashboard.getDietitionMainLayout());
                                break;

                        case "admin":

                                System.out.println("Opening Admin Dashboard");

                                AdminMainLayout adminDashboard =
                                        new AdminMainLayout();

                                Scene adminScene =
                                        new Scene(
                                                adminDashboard.getLayout(),
                                                1550,
                                                830
                                        );

                                Mainstage.setScene(
                                        adminScene
                                );

                                break;

                        default:

                                System.out.println("Invalid actor: " + actor);
                }
        }

        public static void navigateToMainLoginPage() {

                MainLoginPage loginPage = new MainLoginPage();

                try {
                        loginPage.start(Mainstage);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
