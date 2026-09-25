package com.bytebites.view.user.userFeatures.UserSetting;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UserAboutUs {

    // =====================================================
    // COLORS
    // =====================================================

    private static final String BG = "#0F172A";
    private static final String CARD = "#111F35";
    private static final String CARD_LIGHT = "#1E293B";

    private static final String GREEN = "#22C55E";
    private static final String GREEN_DARK = "#16A34A";

    private static final String TEXT = "#F8FAFC";
    private static final String SECONDARY = "#CBD5E1";
    private static final String BORDER = "#334155";


    // =====================================================
    // DASHBOARD
    // =====================================================

    private BorderPane dashboardPane;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public UserAboutUs(BorderPane dashboardPane) {

        this.dashboardPane = dashboardPane;
    }


    // =====================================================
    // MAIN ABOUT US
    // =====================================================

    public VBox getAboutUs() {

        // Outer container
        VBox root = new VBox();

        root.setFillWidth(true);

        root.setStyle(
                "-fx-background-color: " + BG + ";"
        );


        // =================================================
        // SCROLL PANE
        // =================================================

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: " + BG + ";" +
                "-fx-background: " + BG + ";"
        );


        // =================================================
        // CONTENT
        // =================================================

        VBox content = new VBox(18);

        content.setPadding(
                new Insets(25)
        );

        content.setFillWidth(true);

        content.setStyle(
                "-fx-background-color: " + BG + ";"
        );


        // =================================================
        // 1. HERO
        // =================================================

        HBox hero =
                createHeroSection();


        // =================================================
        // 2. WHAT IS WTE
        // =================================================

        VBox whatIsWTE =
                createWhatIsWTECard();


        // =================================================
        // 3. SHASHI SIR
        // =================================================

        VBox shashiCard =
                createShashiCard();


        // =================================================
        // 4. OUR TEAM
        // =================================================

        VBox teamSection =
                createTeamSection();


        // =================================================
        // 5. INSTRUCTORS + MENTORS
        // =================================================

        HBox mentorSection =
                new HBox(15);

        mentorSection.setFillHeight(true);

        VBox instructors =
                createPeopleCard(
                        "🎓",
                        "Our Instructors",
                        "For their constant guidance and support.",
                        new String[]{
                                "Sachin Sir",
                                "Pramod Sir",
                                "Akshay Sir"
                        }
                );

        VBox superMentors =
                createPeopleCard(
                        "★",
                        "Our Super Mentors",
                        "For their valuable support and motivation.",
                        new String[]{
                                "Shiv Sir",
                                "Subodh Sir"
                        }
                );

        mentorSection.getChildren().addAll(
                instructors,
                superMentors
        );

        HBox.setHgrow(
                instructors,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                superMentors,
                Priority.ALWAYS
        );


        // =================================================
        // 6. FINAL THANKS
        // =================================================

        VBox finalThanks =
                createFinalThanksSection();


        // =================================================
        // ADD ALL CONTENT
        // =================================================

        content.getChildren().addAll(
                hero,
                whatIsWTE,
                shashiCard,
                teamSection,
                mentorSection,
                finalThanks
        );


        // Put content inside ScrollPane
        scrollPane.setContent(content);


        // ScrollPane should occupy complete available area
        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS
        );

        root.getChildren().add(
                scrollPane
        );


        return root;
    }


    // =====================================================
    // HERO SECTION
    // =====================================================

    private HBox createHeroSection() {

        HBox hero =
                new HBox(25);

        hero.setPadding(
                new Insets(25)
        );

        hero.setAlignment(
                Pos.CENTER_LEFT
        );

        hero.setMinHeight(190);

        hero.setMaxWidth(
                Double.MAX_VALUE
        );

        hero.setStyle(
                "-fx-background-color: #052E2B;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #14532D;" +
                "-fx-border-radius: 16;"
        );


        // =================================================
        // LOGO
        // =================================================

        VBox logoBox =
                createLogoBox();


        // =================================================
        // TITLE AREA
        // =================================================

        VBox textBox =
                new VBox(10);

        textBox.setAlignment(
                Pos.CENTER_LEFT
        );


        // About Us title
        HBox titleBox =
                new HBox(0);

        Label about =
                new Label("About ");

        about.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 42px;" +
                "-fx-font-weight: bold;"
        );

        Label us =
                new Label("Us");

        us.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 42px;" +
                "-fx-font-weight: bold;"
        );

        titleBox.getChildren().addAll(
                about,
                us
        );


        // Tagline
        Label tagline =
                new Label(
                        "Good Food   ✦   Healthy Families   ✦   Happier Lives"
                );

        tagline.setWrapText(true);

        tagline.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );


        // Description
        Label description =
                new Label(
                        "A team of passionate learners building a solution\n" +
                        "for smarter meal planning and better nutrition."
                );

        description.setWrapText(true);

        description.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 15px;" +
                "-fx-line-spacing: 4px;"
        );

        textBox.getChildren().addAll(
                titleBox,
                tagline,
                description
        );


        // =================================================
        // SPACER
        // =================================================

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        // =================================================
        // RIGHT SIDE
        // =================================================

        VBox rightSide =
                new VBox(5);

        rightSide.setAlignment(
                Pos.CENTER
        );

        Label food =
                new Label("🥗");

        food.setStyle(
                "-fx-font-size: 65px;"
        );

        Label healthy =
                new Label(
                        "Healthy Food"
                );

        healthy.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );

        rightSide.getChildren().addAll(
                food,
                healthy
        );


        hero.getChildren().addAll(
                logoBox,
                textBox,
                spacer,
                rightSide
        );


        return hero;
    }


    // =====================================================
    // LOGO
    // =====================================================

    private VBox createLogoBox() {

        VBox box =
                new VBox(5);

        box.setAlignment(
                Pos.CENTER
        );

        ImageView logo =
                new ImageView();


        try {

            Image image =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/icon/image.png"
                                    )
                    );

            logo.setImage(
                    image
            );

            logo.setFitWidth(90);
            logo.setFitHeight(90);

            logo.setPreserveRatio(
                    true
            );


        } catch (Exception e) {

            Label fallback =
                    new Label("WTE?");

            fallback.setStyle(
                    "-fx-text-fill: " + GREEN + ";" +
                    "-fx-font-size: 25px;" +
                    "-fx-font-weight: bold;"
            );

            box.getChildren().add(
                    fallback
            );

            return box;
        }


        Label name =
                new Label(
                        "What To Eat ?"
                );

        name.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );


        box.getChildren().addAll(
                logo,
                name
        );


        return box;
    }


    // =====================================================
    // WHAT IS WTE
    // =====================================================

    private VBox createWhatIsWTECard() {

        VBox card =
                createCard();


        // Heading
        HBox heading =
                new HBox(12);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );


        Label icon =
                createCircleIcon("🌿");


        Label title =
                new Label(
                        "What is "
                );

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );


        Label wte =
                new Label(
                        "What To Eat?"
                );

        wte.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );


        heading.getChildren().addAll(
                icon,
                title,
                wte
        );


        // Description
        Label description =
                new Label(
                        "What To Eat? is a family-focused meal planning and " +
                        "nutrition management application designed to help users " +
                        "plan meals, manage family food preferences, discover recipes, " +
                        "manage pantry and grocery items, connect with dietitians and " +
                        "organize their food and nutrition activities in one place."
                );

        description.setWrapText(
                true
        );

        description.setMaxWidth(
                Double.MAX_VALUE
        );

        description.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 15px;" +
                "-fx-line-spacing: 4px;"
        );


        // =================================================
        // FEATURES
        // =================================================

        HBox features =
                new HBox(10);

        features.setFillHeight(
                true
        );


        VBox feature1 =
                createFeature(
                        "🍴",
                        "Plan Meals",
                        "For your family"
                );

        VBox feature2 =
                createFeature(
                        "📖",
                        "Discover Recipes",
                        "Healthy & tasty"
                );

        VBox feature3 =
                createFeature(
                        "👜",
                        "Manage Pantry",
                        "Keep track easily"
                );

        VBox feature4 =
                createFeature(
                        "👥",
                        "Connect with Dietitians",
                        "Expert guidance"
                );


        features.getChildren().addAll(
                feature1,
                feature2,
                feature3,
                feature4
        );


        HBox.setHgrow(
                feature1,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                feature2,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                feature3,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                feature4,
                Priority.ALWAYS
        );


        card.getChildren().addAll(
                heading,
                description,
                features
        );


        return card;
    }


    // =====================================================
    // FEATURE CARD
    // =====================================================

    private VBox createFeature(
            String iconText,
            String titleText,
            String subtitleText) {

        VBox box =
                new VBox(5);

        box.setPadding(
                new Insets(12)
        );

        box.setAlignment(
                Pos.CENTER
        );

        box.setMaxWidth(
                Double.MAX_VALUE
        );

        box.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 10;"
        );


        Label icon =
                new Label(
                        iconText
                );

        icon.setStyle(
                "-fx-font-size: 25px;"
        );


        Label title =
                new Label(
                        titleText
                );

        title.setWrapText(
                true
        );

        title.setAlignment(
                Pos.CENTER
        );

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        subtitleText
                );

        subtitle.setWrapText(
                true
        );

        subtitle.setAlignment(
                Pos.CENTER
        );

        subtitle.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 10px;"
        );


        box.getChildren().addAll(
                icon,
                title,
                subtitle
        );


        return box;
    }


    // =====================================================
    // SHASHI SIR
    // =====================================================

    private VBox createShashiCard() {

        VBox card =
                createCard();


        card.setStyle(
                "-fx-background-color: #052E2B;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + GREEN_DARK + ";" +
                "-fx-border-radius: 14;"
        );


        // Heading
        HBox heading =
                new HBox(12);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );


        Label icon =
                createCircleIcon(
                        "🎓"
                );


        Label title =
                new Label(
                        "Special Thanks"
                );

        title.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );


        heading.getChildren().addAll(
                icon,
                title
        );


        // =================================================
        // CONTENT
        // =================================================

        HBox content =
                new HBox(25);

        content.setAlignment(
                Pos.CENTER_LEFT
        );


        // Photo
        VBox photoBox =
                createShashiImage();


        // Information
        VBox information =
                new VBox(10);

        information.setAlignment(
                Pos.CENTER_LEFT
        );


        Label name =
                new Label(
                        "Shashi Sir (core2web)"
                );

        name.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );


        Label thanks =
                new Label(
                        "Thanks to Shashi Sir, core2web."
                );

        thanks.setWrapText(
                true
        );

        thanks.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 16px;"
        );


        Label support =
                new Label(
                        "Thank you for your guidance and support."
                );

        support.setWrapText(
                true
        );

        support.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 14px;"
        );


        information.getChildren().addAll(
                name,
                thanks,
                support
        );


        HBox.setHgrow(
                information,
                Priority.ALWAYS
        );


        content.getChildren().addAll(
                photoBox,
                information
        );


        card.getChildren().addAll(
                heading,
                content
        );


        return card;
    }


    // =====================================================
    // SHASHI PHOTO
    // =====================================================

    private VBox createShashiImage() {

        VBox box =
                new VBox();

        box.setAlignment(
                Pos.CENTER
        );

        box.setPrefWidth(
                150
        );


        StackPaneImage image =
                new StackPaneImage();


        try {

            Image imageFile =
                    new Image(
                            getClass()
                                    .getResourceAsStream(
                                            "/assets/images/images (1).jpg"
                                    )
                    );


            ImageView imageView =
                    new ImageView(
                            imageFile
                    );

            imageView.setFitWidth(
                    135
            );

            imageView.setFitHeight(
                    135
            );

            imageView.setPreserveRatio(
                    true
            );


            Circle clip =
                    new Circle(
                            67.5,
                            67.5,
                            67.5
                    );

            imageView.setClip(
                    clip
            );


            Circle border =
                    new Circle(
                            70
                    );

            border.setFill(
                    Color.TRANSPARENT
            );

            border.setStroke(
                    Color.web(
                            GREEN
                    )
            );

            border.setStrokeWidth(
                    3
            );


            image.getChildren().addAll(
                    imageView,
                    border
            );


        } catch (Exception e) {

            Circle circle =
                    new Circle(
                            70
                    );

            circle.setFill(
                    Color.web(
                            "#064E3B"
                    )
            );

            circle.setStroke(
                    Color.web(
                            GREEN
                    )
            );

            circle.setStrokeWidth(
                    3
            );


            Label fallback =
                    new Label(
                            "SS"
                    );

            fallback.setStyle(
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 30px;" +
                    "-fx-font-weight: bold;"
            );


            image.getChildren().addAll(
                    circle,
                    fallback
            );
        }


        box.getChildren().add(
                image
        );


        return box;
    }


    // =====================================================
    // CUSTOM STACKPANE
    // =====================================================

    private static class StackPaneImage
            extends javafx.scene.layout.StackPane {

        public StackPaneImage() {

            setPrefSize(
                    145,
                    145
            );

            setMinSize(
                    145,
                    145
            );
        }
    }


    // =====================================================
    // OUR TEAM
    // =====================================================

    private VBox createTeamSection() {

        VBox section =
                createCard();


        // Heading
        HBox heading =
                new HBox(12);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );


        Label icon =
                createCircleIcon(
                        "👥"
                );


        Label title =
                new Label(
                        "Our Team"
                );

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 25px;" +
                "-fx-font-weight: bold;"
        );


        heading.getChildren().addAll(
                icon,
                title
        );


        Label subtitle =
                new Label(
                        "Passionate learners working together to make healthy living easier."
                );

        subtitle.setWrapText(
                true
        );

        subtitle.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 14px;"
        );


        // =================================================
        // TEAM MEMBERS
        // =================================================

        HBox members =
                new HBox(12);

        members.setFillHeight(
                true
        );


        VBox sakshi =
                createTeamMember(
                        "S",
                        "Sakshi H Divekar",
                        "Lead"
                );

        VBox vaishnavi =
                createTeamMember(
                        "V",
                        "Vaishnavi N Kadam",
                        "Member"
                );

        VBox samiksha =
                createTeamMember(
                        "S",
                        "Samiksha A Kharabe",
                        "Member"
                );

        VBox paryushan =
                createTeamMember(
                        "P",
                        "Paryushan S Kamboj",
                        "Member"
                );


        members.getChildren().addAll(
                sakshi,
                vaishnavi,
                samiksha,
                paryushan
        );


        HBox.setHgrow(
                sakshi,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                vaishnavi,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                samiksha,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                paryushan,
                Priority.ALWAYS
        );


        section.getChildren().addAll(
                heading,
                subtitle,
                members
        );


        return section;
    }


    // =====================================================
    // TEAM MEMBER
    // =====================================================

    private VBox createTeamMember(
            String initial,
            String name,
            String role) {

        VBox card =
                new VBox(8);

        card.setPrefHeight(
                140
        );

        card.setMinHeight(
                140
        );

        card.setAlignment(
                Pos.CENTER
        );

        card.setPadding(
                new Insets(12)
        );

        card.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT + ";" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 10;"
        );


        // Avatar
        Circle circle =
                new Circle(
                        28
                );

        circle.setFill(
                Color.web(
                        "#A7F3D0"
                )
        );


        Label initialLabel =
                new Label(
                        initial
                );

        initialLabel.setStyle(
                "-fx-text-fill: #052E16;" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );


        javafx.scene.layout.StackPane avatar =
                new javafx.scene.layout.StackPane(
                        circle,
                        initialLabel
                );


        // Name
        Label nameLabel =
                new Label(
                        name
                );

        nameLabel.setWrapText(
                true
        );

        nameLabel.setAlignment(
                Pos.CENTER
        );

        nameLabel.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );


        // Role
        Label roleLabel =
                new Label(
                        role
                );

        roleLabel.setPadding(
                new Insets(
                        5,
                        20,
                        5,
                        20
                )
        );


        if (role.equals("Lead")) {

            roleLabel.setStyle(
                    "-fx-background-color: " + GREEN + ";" +
                    "-fx-text-fill: #052E16;" +
                    "-fx-background-radius: 15;" +
                    "-fx-font-weight: bold;"
            );

        } else {

            roleLabel.setStyle(
                    "-fx-background-color: #64748B;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 15;" +
                    "-fx-font-weight: bold;"
            );
        }


        card.getChildren().addAll(
                avatar,
                nameLabel,
                roleLabel
        );


        return card;
    }


    // =====================================================
    // INSTRUCTORS / SUPER MENTORS
    // =====================================================

    private VBox createPeopleCard(
            String iconText,
            String titleText,
            String subtitleText,
            String[] people) {

        VBox card =
                createCard();


        HBox heading =
                new HBox(12);

        heading.setAlignment(
                Pos.CENTER_LEFT
        );


        Label icon =
                createCircleIcon(
                        iconText
                );


        Label title =
                new Label(
                        titleText
                );

        title.setWrapText(
                true
        );

        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 21px;" +
                "-fx-font-weight: bold;"
        );


        heading.getChildren().addAll(
                icon,
                title
        );


        Label subtitle =
                new Label(
                        subtitleText
                );

        subtitle.setWrapText(
                true
        );

        subtitle.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 13px;"
        );


        VBox names =
                new VBox(8);


        for (String person : people) {

            Label name =
                    new Label(
                            person
                    );

            name.setMaxWidth(
                    Double.MAX_VALUE
            );

            name.setAlignment(
                    Pos.CENTER
            );

            name.setPadding(
                    new Insets(
                            8,
                            18,
                            8,
                            18
                    )
            );

            name.setStyle(
                    "-fx-background-color: " +
                            CARD_LIGHT + ";" +
                    "-fx-text-fill: " +
                            TEXT + ";" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20;" +
                    "-fx-border-color: " +
                            BORDER + ";" +
                    "-fx-border-radius: 20;"
            );


            names.getChildren().add(
                    name
            );
        }


        card.getChildren().addAll(
                heading,
                subtitle,
                names
        );


        return card;
    }


    // =====================================================
    // FINAL THANKS
    // =====================================================

    private VBox createFinalThanksSection() {

        VBox card =
                new VBox(10);

        card.setPadding(
                new Insets(22)
        );

        card.setAlignment(
                Pos.CENTER
        );

        card.setStyle(
                "-fx-background-color: #052E2B;" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #14532D;" +
                "-fx-border-radius: 14;"
        );


        Label title =
                new Label(
                        "♥  Thanks to all our mentors & TeamLeads"
                );

        title.setWrapText(
                true
        );

        title.setAlignment(
                Pos.CENTER
        );

        title.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;"
        );


        Label guidance =
                new Label(
                        "for their guidance and support."
                );

        guidance.setStyle(
                "-fx-text-fill: " + SECONDARY + ";" +
                "-fx-font-size: 14px;"
        );


        Label quote =
                new Label(
                        "“Good Food Builds Great Memories”"
                );

        quote.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-style: italic;"
        );


        Label ending =
                new Label(
                        "EAT   •   PLAN   •   COOK   •   GROW   •   TOGETHER"
                );

        ending.setStyle(
                "-fx-text-fill: " + GREEN + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;"
        );


        card.getChildren().addAll(
                title,
                guidance,
                quote,
                ending
        );


        return card;
    }


    // =====================================================
    // COMMON CARD
    // =====================================================

    private VBox createCard() {

        VBox card =
                new VBox(15);

        card.setPadding(
                new Insets(20)
        );

        card.setMaxWidth(
                Double.MAX_VALUE
        );

        card.setStyle(
                "-fx-background-color: " + CARD + ";" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 14;"
        );


        return card;
    }


    // =====================================================
    // CIRCLE ICON
    // =====================================================

    private Label createCircleIcon(
            String text) {

        Label icon =
                new Label(
                        text
                );

        icon.setMinSize(
                48,
                48
        );

        icon.setMaxSize(
                48,
                48
        );

        icon.setAlignment(
                Pos.CENTER
        );

        icon.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                "-fx-background-radius: 50%;" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-size: 20px;"
        );


        return icon;
    }
}