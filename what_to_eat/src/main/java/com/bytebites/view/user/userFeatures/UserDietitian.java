package com.bytebites.view.user.userFeatures;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import com.bytebites.view.common.SameStageWindow;
import java.awt.Desktop;
import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.UserDietitianConsultationDao;
import com.bytebites.model.Dietitian;
import com.bytebites.model.UserDietitianConsultationModel;
import com.bytebites.model.session.SessionManager;
import com.bytebites.service.RazorpayPaymentService;

public class UserDietitian {

    // THEME
   
    private static final String BG = "#0F172A";
    private static final String CARD = "#1E293B";
    private static final String CARD_LIGHT = "#243247";

    private static final String GREEN = "#22C55E";
    private static final String GREEN_DARK = "#16A34A";

    private static final String TEXT = "#F8FAFC";
    private static final String SECONDARY = "#94A3B8";

    private static final String BLUE = "#38BDF8";
    private static final String BORDER = "#334155";

    // DASHBOARD REFERENCE
    
    private BorderPane dashboardPane;
    private ContextMenu filterMenu;

    // REVIEW DATA
    
    private static class ReviewData {
        String userName;
        String reviewText;
        int stars;

        ReviewData(
                String userName,
                String reviewText,
                int stars) {
            this.userName = userName;
            this.reviewText = reviewText;
            this.stars = stars;
        }
    }

    // DIETITIAN DATA
    
    private static class DietitianData {

        String uid;
        String whatsappNumber;
        String email;
        String specialization;
        String experience;

        String name;
        String specialty;

        double rating;
        int reviews;
        int activeUsers;

        String description;
        String about;

        String[] specializations;
        String imagePath;

        boolean subscribed = false;
        List<ReviewData> reviewList = new ArrayList<>();
        DietitianData(
                
        String name,
        String specialty,
        String email,
        String specialization,
        String experience,
        String whatsappNumber,
        double rating,
        int reviews,
        int activeUsers,
        String description,
        String about,
        String[] specializations,
        String imagePath) { 
            this.name = name;
            this.specialty = specialty;
            this.email = email;
            this.specialization = specialization;
            this.experience = experience;
            this.whatsappNumber = whatsappNumber;
            this.rating = rating;
            this.reviews = reviews;
            this.activeUsers = activeUsers;
            this.description = description;
            this.about = about;
            this.specializations = specializations;
            this.imagePath = imagePath;
        }

        double getRating() {
            return rating;
        }
        int getActiveUsers() {
            return activeUsers;
        }
    }
    // VARIABLES
    
    private final List<DietitianData> allDietitians = new ArrayList<>();
    private VBox cardsContainer;
    private TextField searchField;
    private VBox mainPage;

    // CONSTRUCTORS
   
    // Oldructor
    // Ye Back button ko properly kaam karne ke liye hai
    public UserDietitian(BorderPane dashboardPane) {
        this.dashboardPane = dashboardPane;
        createDietitianData();
    }

    // CREATE DIETITIAN DATA
   
    private void createDietitianData() {

        DietitianDao dietitianDao = new DietitianDao();
        List<Dietitian> firebaseDietitians = dietitianDao.getApprovedDietitians();

        System.out.println("User Side - Dietitians Loaded: "+ firebaseDietitians.size());

        for (Dietitian dietitian : firebaseDietitians) {

        System.out.println(
                "Loading Dietitian: "
                + dietitian.getName()
        );

        DietitianData data = new DietitianData(
                dietitian.getName(),
                "Dietitian",
                dietitian.getEmail(),
                dietitian.getSpecialization(),
                dietitian.getExperience(),
                dietitian.getWhatsappNumber(),
                0.0,
                0,
                dietitian.getActiveUsers(),
                "Certified dietitian available for nutrition consultation.",
                "This dietitian is approved and available for consultation.",
                new String[]{
                        "Nutrition",
                        "Diet Planning"
                },
                "/assets/images/ProfileImage.jpg"
        );
        data.uid = dietitian.getUid();
        data.whatsappNumber = dietitian.getWhatsappNumber();

        String currentUserUid =
                SessionManager.getUid();

        if (currentUserUid != null &&
                !currentUserUid.isBlank() &&
                data.uid != null &&
                !data.uid.isBlank()) {

        try {

                data.subscribed =
                        dietitianDao.isUserSubscribed(
                                data.uid,
                                currentUserUid
                        );

        } catch (Exception ex) {

                ex.printStackTrace();
                data.subscribed = false;
        }
        }

        // LOAD REVIEWS FROM FIREBASE
        List<java.util.Map<String, Object>> firebaseReviews =
                dietitianDao.getReviews(dietitian.getUid());

        for (java.util.Map<String, Object> review : firebaseReviews) {

                String userName =
                        (String) review.get("userName");

                String reviewText =
                        (String) review.get("reviewText");

                Object starsObj =
                        review.get("stars");

                int stars = 0;

                if (starsObj instanceof Number) {
                        stars = ((Number) starsObj).intValue();
                }

                data.reviewList.add(
                        new ReviewData(
                                userName,
                                reviewText,
                                stars
                        )
                );
        }

                // UPDATE REVIEW COUNT
                data.reviews = data.reviewList.size();

                // CALCULATE RATING
                if (!data.reviewList.isEmpty()) {

                double totalStars = 0;

                for (ReviewData review : data.reviewList) {
                        totalStars += review.stars;
                }

                data.rating =
                        totalStars / data.reviewList.size();
                }

                allDietitians.add(data);
        }
    }
    

    // MAIN PAGE
   
    public VBox getDietitianPage() {
        mainPage = new VBox(20);
        mainPage.setPadding(new Insets(35));
        mainPage.setStyle("-fx-background-color: " + BG + ";");

        // TITLE
        
        Label title = new Label("Find a Dietitian");
        title.setStyle(
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 38px;" +
                "-fx-font-weight: bold;");
        Label subtitle = new Label( "Connect with certified professionals "+ "tailored to your family's needs.");
        subtitle.setStyle( "-fx-text-fill: " + SECONDARY + ";" + "-fx-font-size: 16px;");

        // SEARCH
        
        searchField = new TextField();
        searchField.setPromptText("Search by dietitian name...");
        searchField.setPrefHeight(44);
        searchField.setPrefWidth(365);
        searchField.setStyle(
                "-fx-background-color: #FFFFFF;" +
                "-fx-text-fill: #0F172A;" +
                "-fx-prompt-text-fill: #64748B;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;" +
                "-fx-border-color: " + BORDER + ";");

        searchField.textProperty()
                .addListener(
                        (obs, oldValue, newValue) ->
                                updateCards(newValue));

        // FILTER BUTTON
      
        Button filterButton = new Button("☷  Filters");
        filterButton.setPrefHeight(44);
        filterButton.setPrefWidth(120);
        filterButton.setStyle(
                "-fx-background-color: " + BG + ";" +
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;");

        filterButton.setOnAction(
                e -> showFilterMenu(filterButton));

        // SEARCH ROW
        
        HBox searchArea = new HBox(12);
        searchArea.setAlignment(Pos.CENTER_RIGHT);
        RegionSpacer spacer = new RegionSpacer();

        searchArea.getChildren().addAll(
                spacer,
                searchField,
                filterButton);

        // CARDS
       
        cardsContainer = new VBox(15);
        cardsContainer.setPadding(new Insets(5));
        updateCards("");

        // SCROLL
       
        ScrollPane scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background: " + BG + ";" +
                "-fx-background-color: " + BG + ";");

        VBox.setVgrow(
                scrollPane,
                Priority.ALWAYS);
        mainPage.getChildren().addAll(
                title,
                subtitle,
                searchArea,
                scrollPane);
        return mainPage;
    }

    // FILTER

        // FILTER DROPDOWN
      
        private void showFilterMenu(Button owner) {
                // If dropdown is already open, close it
                if (filterMenu != null &&
                        filterMenu.isShowing()) {
                        filterMenu.hide();
                        return;
                }
                filterMenu = new ContextMenu();

                // MAIN FILTER BOX

                VBox box = new VBox(12);
                box.setPadding( new Insets(15));
                box.setPrefWidth(270);
                box.setStyle(
                        "-fx-background-color: " + CARD + ";" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;");

                // HEADING
                
                Label heading = new Label("Priority");
                heading.setStyle(
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 19px;" +
                        "-fx-font-weight: bold;");

                // FILTER BUTTONS

                Button highRating = createFilterButton( "★  High Rating");
                Button highActive = createFilterButton( "●  High Active Users");
                Button clearFilter = createFilterButton( "↻  Clear Filter");

                // HIGH RATING
               
                highRating.setOnAction(e -> {
                        List<DietitianData> result =
                                allDietitians.stream()
                                        .sorted(
                                                Comparator.comparingDouble(
                                                        DietitianData::getRating
                                                ).reversed()
                                        )
                                        .collect(
                                                Collectors.toList()
                                        );
                        displayDietitians(result);
                        filterMenu.hide();});

                // HIGH ACTIVE USERS
               
                highActive.setOnAction(e -> {
                        List<DietitianData> result =
                                allDietitians.stream()
                                        .sorted(
                                                Comparator.comparingInt(
                                                        DietitianData::getActiveUsers
                                                ).reversed()
                                        )
                                        .collect(
                                                Collectors.toList()
                                        );
                        displayDietitians(result);
                        filterMenu.hide();});

                // CLEAR FILTER
              
                clearFilter.setOnAction(e -> {
                        updateCards(
                                searchField == null
                                        ? ""
                                        : searchField.getText());
                        filterMenu.hide();});
                box.getChildren().addAll(
                        heading,
                        highRating,
                        highActive,
                        clearFilter);

                // CUSTOM MENU ITEM
         
                CustomMenuItem menuItem = new CustomMenuItem(box);
                menuItem.setHideOnClick(false);
                filterMenu.getItems()
                        .add(menuItem);

                // DROPDOWN STYLE
               
                filterMenu.setStyle(
                        "-fx-background-color: " + CARD + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 0;");

                // SHOW BELOW FILTER BUTTON
               
                filterMenu.show(owner,Side.BOTTOM,0,5);
        }

    // FILTER BUTTON
   
    private Button createFilterButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42);
        button.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT + ";" +
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 14px;" +
                "-fx-alignment: CENTER_LEFT;" +
                "-fx-background-radius: 7;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 7;");
        return button;
    }

    // SEARCH
  

    private void updateCards(String searchText) {
        String text = searchText == null
                        ? ""
                        : searchText.trim().toLowerCase();
        final String finalSearchText = text;
        List<DietitianData> filtered =
                allDietitians.stream()
                        .filter(
                                d -> d.name
                                        .toLowerCase()
                                        .contains(
                                                finalSearchText))
                        .collect(
                                Collectors.toList());
        displayDietitians(filtered);
    }

    // DISPLAY CARDS
    
    // DISPLAY DIETITIANS
        private void displayDietitians(List<DietitianData> dietitians) {

                if (cardsContainer == null) {
                        return;
                }

                // Clear old cards
                cardsContainer.getChildren().clear();

                // If no dietitians are available
                if (dietitians == null || dietitians.isEmpty()) {

                        Label emptyLabel =
                                new Label(
                                        "No approved dietitians available."
                                );

                        emptyLabel.setStyle(
                                "-fx-text-fill: " + SECONDARY + ";" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;"
                        );

                        emptyLabel.setMaxWidth(
                                Double.MAX_VALUE
                        );

                        emptyLabel.setAlignment(
                                Pos.CENTER
                        );

                        cardsContainer.getChildren()
                                .add(emptyLabel);

                        return;
                }

                // =====================================================
                // SEPARATE SUBSCRIBED AND OTHER DIETITIANS
                // =====================================================

                List<DietitianData> subscribedDietitians =
                        dietitians.stream()
                                .filter(d -> d.subscribed)
                                .collect(Collectors.toList());

                List<DietitianData> otherDietitians =
                        dietitians.stream()
                                .filter(d -> !d.subscribed)
                                .collect(Collectors.toList());

                // =====================================================
                // MY DIETITIAN
                // =====================================================

                if (!subscribedDietitians.isEmpty()) {

                        Label myDietitianTitle =
                                new Label("My Dietitian");

                        myDietitianTitle.setStyle(
                                "-fx-text-fill: " + TEXT + ";" +
                                "-fx-font-size: 22px;" +
                                "-fx-font-weight: bold;"
                        );

                        FlowPane subscribedFlow =
                                new FlowPane();

                        subscribedFlow.setHgap(16);
                        subscribedFlow.setVgap(16);
                        subscribedFlow.setPrefWrapLength(1250);
                        subscribedFlow.setPadding(
                                new Insets(5)
                        );

                        for (DietitianData dietitian :
                                subscribedDietitians) {

                        subscribedFlow.getChildren().add(
                                createDietitianCard(dietitian)
                        );
                        }

                        cardsContainer.getChildren().addAll(
                                myDietitianTitle,
                                subscribedFlow
                        );

                        // =================================================
                        // MARGIN ABOVE LINE
                        // =================================================

                        Region topMargin = new Region();
                        topMargin.setPrefHeight(15);

                        // =================================================
                        // SEPARATOR LINE
                        // =================================================

                        Separator separator = new Separator();

                        separator.setMaxWidth(
                                Double.MAX_VALUE
                        );

                        // =================================================
                        // MARGIN BELOW LINE
                        // =================================================

                        Region bottomMargin = new Region();
                        bottomMargin.setPrefHeight(20);

                        cardsContainer.getChildren().addAll(
                                topMargin,
                                separator,
                                bottomMargin
                        );
                }

                // =====================================================
                // ALL DIETITIANS
                // =====================================================

                Label allDietitianTitle =
                        new Label("All Dietitians");

                allDietitianTitle.setStyle(
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
                );

                FlowPane allFlow =
                        new FlowPane();

                allFlow.setHgap(16);
                allFlow.setVgap(16);
                allFlow.setPrefWrapLength(1250);
                allFlow.setPadding(
                        new Insets(5)
                );

                for (DietitianData dietitian :
                        otherDietitians) {

                        allFlow.getChildren().add(
                                createDietitianCard(dietitian)
                        );
                }

                cardsContainer.getChildren().addAll(
                        allDietitianTitle,
                        allFlow
                );
        }

    // DIETITIAN CARD
  
    private VBox createDietitianCard(
            DietitianData dietitian) {

        VBox card = new VBox(10);
        card.setPrefWidth(300);
        card.setMinWidth(300);
        card.setMaxWidth(300);
        card.setPadding(new Insets(18));
        card.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 12;");

        // PHOTO
        StackPane photo =createProfileImage(dietitian);

        // NAME

        Button nameButton = new Button(dietitian.name);
        nameButton.setWrapText(true);
        nameButton.setMaxWidth(Double.MAX_VALUE);
        nameButton.setAlignment( Pos.CENTER_LEFT);
        nameButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 0;");

        // IMPORTANT
        // Name click -> Detail page

        nameButton.setOnAction(
                e -> openDietitianDetails(
                        dietitian));

        // SPECIALTY

        Label specialty = new Label(dietitian.specialty);
        specialty.setStyle(
                "-fx-text-fill: " +
                        SECONDARY + ";" +
                "-fx-font-size: 14px;");

        // RATING

        Label rating = new Label( "★ " +
                        dietitian.rating +
                        "   (" +
                        dietitian.reviews +
                        " reviews)");
        rating.setStyle(
                "-fx-text-fill: " +
                        GREEN + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;");

        // ACTIVE USERS

        Label activeUsers = new Label();
        updateActiveUserLabel(activeUsers, dietitian);

        // DESCRIPTION

        Label description = new Label( dietitian.description);
        description.setWrapText(true);
        description.setMaxWidth(260);
        description.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 13px;");

        // TAGS

        FlowPane tags = new FlowPane();
        tags.setHgap(6);
        tags.setVgap(6);
        for (String specialization :
                dietitian.specializations) {
            Label tag = new Label( specialization);
            tag.setStyle(
                    "-fx-background-color: #123D2A;" +
                    "-fx-text-fill: " +
                            GREEN + ";" +
                    "-fx-border-color: " +
                            GREEN_DARK + ";" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 5 8 5 8;" +
                    "-fx-font-size: 11px;");
            tags.getChildren()
                    .add(tag);
        }
        card.getChildren().addAll(photo,nameButton,specialty,rating,activeUsers,description,tags);
        return card;
    }

    // ACTIVE USER LABEL
    
    private void updateActiveUserLabel(
            Label label,
            DietitianData dietitian) {
        if (dietitian.subscribed) {
            label.setText(
                    "● " +
                    dietitian.activeUsers +
                    " active users   ✓ Subscribed");
        } else {
            label.setText(
                    "● " +
                    dietitian.activeUsers +
                    " active users");
        }
        label.setStyle(
                "-fx-text-fill: " +
                        BLUE + ";" +
                "-fx-font-size: 13px;");
    }

    // PROFILE IMAGE
    
    private StackPane createProfileImage(
            DietitianData dietitian) {
        StackPane holder = new StackPane();
        holder.setPrefSize(110, 110);
        holder.setMinSize(110, 110);
        holder.setMaxSize(110, 110);
        Circle background = new Circle(55);
        background.setFill(Color.web("#064E3B"));

        try {
            Image image = new Image(getClass().getResourceAsStream(dietitian.imagePath));

            if (!image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(105);
                imageView.setFitHeight(105);
                imageView.setPreserveRatio(true);
                Circle clip = new Circle( 52.5, 52.5, 52.5);
                imageView.setClip(clip);
                holder.getChildren()
                        .addAll( background, imageView);
                return holder;
            }
        } catch (Exception ignored) {
        }
        holder.getChildren()
                .add(background);
        return holder;
    }

    // DETAIL PAGE
    
        private void openDietitianDetails(DietitianData dietitian) {
                VBox page = new VBox(20);
                page.setPadding(new Insets(30));
                page.setStyle("-fx-background-color: " + BG + ";");

                // BACK BUTTON
                
                Button backButton = new Button("←  Back to Dietitians");
                backButton.setPrefHeight(42);
                backButton.setStyle(
                        "-fx-background-color: " +
                                CARD + ";" +
                        "-fx-text-fill: " +
                                TEXT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: " +
                                BORDER + ";" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;");

                // IMPORTANT FIX
                backButton.setOnAction(
                        e -> showListPage());

                // PROFILE HEADER
                
                HBox profileHeader = new HBox(25);
                profileHeader.setAlignment(Pos.CENTER_LEFT);

                StackPane profilePhoto = createLargeProfileImage( dietitian);
                VBox information = new VBox(8);
                Label name = new Label(dietitian.name);
                name.setStyle(
                        "-fx-text-fill: " +
                                TEXT + ";" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;");
                Label specialty = new Label(dietitian.specialty);
                specialty.setStyle(
                        "-fx-text-fill: " +
                                BLUE + ";" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;");

                Label rating = new Label("★ " + dietitian.rating + " / 5     " + dietitian.reviews + " reviews");
                rating.setStyle( "-fx-text-fill: " +GREEN + ";" + "-fx-font-size: 16px;" + "-fx-font-weight: bold;");

                Label active = new Label();
                updateDetailActiveLabel(active,dietitian);
                information.getChildren().addAll( name,specialty, rating,active );
                profileHeader.getChildren()
                        .addAll( profilePhoto, information );

                // =====================================================
                // ACTION BUTTONS
                // =====================================================

                Button subscribe = new Button();

                updateSubscribeButton(
                        subscribe,
                        dietitian
                );


                // MESSAGE BUTTON
                Button message =
                        new Button(
                                "✉  Message"
                        );


                // ADD REVIEW BUTTON
                Button addReview =
                        new Button(
                                "+  Add Review"
                        );


                // CONSULTATION BUTTON
                Button consultation =
                        new Button(
                                "▣  Consultation"
                        );


                // =====================================================
                // BUTTON STYLING
                // =====================================================

                styleActionButton(
                        message,
                        CARD,
                        TEXT
                );

                styleActionButton(
                        addReview,
                        CARD,
                        GREEN
                );

                styleActionButton(
                        consultation,
                        CARD,
                        TEXT
                );


                // =====================================================
                // SUBSCRIBE ACTION
                // =====================================================

                subscribe.setOnAction(e -> {

                if (dietitian.subscribed) {

                        showCustomMessage(
                                "Already Subscribed",
                                "You are already subscribed to "
                                        + dietitian.name
                                        + "."
                        );

                        return;
                }

                showSubscriptionPlans(
                        dietitian
                );
                });


                // =====================================================
                // MESSAGE ACTION
                // =====================================================

                message.setOnAction(e -> {

                try {

                        String whatsappNumber =
                                dietitian.whatsappNumber;


                        if (
                                whatsappNumber == null ||
                                whatsappNumber.trim().isEmpty()
                        ) {

                        showCustomMessage(
                                "WhatsApp Not Available",
                                "WhatsApp number is not available for "
                                        + dietitian.name
                                        + "."
                        );

                        return;
                        }


                        String cleanNumber =
                                whatsappNumber.replaceAll(
                                        "[^0-9]",
                                        ""
                                );


                        String url =
                                "https://wa.me/"
                                        + cleanNumber;


                        Desktop.getDesktop()
                                .browse(
                                        new URI(url)
                                );


                } catch (Exception ex) {

                        ex.printStackTrace();
                }
                });


                // =====================================================
                // ADD REVIEW ACTION
                // =====================================================

                addReview.setOnAction(
                        e -> showReviewDialog(
                                dietitian
                        )
                );


                // =====================================================
                // CONSULTATION ACTION
                // =====================================================

                consultation.setOnAction(e -> {

                        handleConsultationClick(
                                dietitian
                        );
                });


                // =====================================================
                // ACTION ROW
                // =====================================================

                HBox actions =
                        new HBox(12);

                actions.setAlignment(
                        Pos.CENTER_LEFT
                );

                actions.getChildren().addAll(
                        subscribe,
                        message,
                        addReview,
                        consultation
                );

                
                // ABOUT
        
                VBox aboutCard = createDetailCard( "About " + dietitian.name, dietitian.about );
                // DIETITIAN DETAILS

                VBox detailsCard = new VBox(12);
                detailsCard.setPadding(new Insets(20));
                detailsCard.setStyle(
                        "-fx-background-color: " + CARD + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 12;"
                );

                Label detailsTitle = new Label("Dietitian Details");
                detailsTitle.setStyle(
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
                );

                Label emailLabel = new Label(
                        "Email: " +
                        (dietitian.email != null ? dietitian.email : "Not Set")
                );



                Label experienceLabel = new Label(
                        "Experience: " +
                        (dietitian.experience != null
                                ? dietitian.experience
                                : "Not Set")
                );

                Label whatsappLabel = new Label(
                        "WhatsApp Number: " +
                        (dietitian.whatsappNumber != null
                                ? dietitian.whatsappNumber
                                : "Not Set")
                );

                for (Label label : new Label[]{
                        emailLabel,
                // specializationLabel,
                        experienceLabel,
                        whatsappLabel
                }) {
                label.setStyle(
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 16px;"
                );
                }

                detailsCard.getChildren().addAll(
                        detailsTitle,
                        emailLabel,
                // specializationLabel,
                        experienceLabel,
                        whatsappLabel
                );

                        // SPECIALIZATION
                        
                        VBox specializationCard = new VBox(15);
                        specializationCard.setPadding( new Insets(20) );
                        specializationCard.setStyle(
                                "-fx-background-color: " +
                                        CARD + ";" +
                                "-fx-background-radius: 12;" +
                                "-fx-border-color: " +
                                        BORDER + ";" +
                                "-fx-border-radius: 12;" );
                        Label specializationTitle = new Label("Specialization" );
                        specializationTitle.setStyle(
                                "-fx-text-fill: " +
                                        TEXT + ";" +
                                "-fx-font-size: 22px;" +
                                "-fx-font-weight: bold;" );
                        FlowPane specializationTags = new FlowPane();
                        specializationTags.setHgap(10);
                        specializationTags.setVgap(10);
                String specialization =
                        dietitian.specialization;

                if (specialization == null ||
                        specialization.trim().isEmpty()) {
                specialization = "Not Set";
                }

                Label tag = new Label(specialization);

                tag.setStyle(
                        "-fx-background-color: #123D2A;" +
                        "-fx-text-fill: " +
                                GREEN + ";" +
                        "-fx-border-color: " +
                                GREEN + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 7 12 7 12;"
                );

                specializationTags
                        .getChildren()
                        .add(tag);
                        specializationCard
                                .getChildren()
                                .addAll(
                                        specializationTitle,
                                        specializationTags );

                        // REVIEWS
                
                        VBox reviewsCard = createReviewsCard(dietitian );
                page.getChildren().addAll(
                        backButton,
                        profileHeader,
                        actions,
                        aboutCard,
                        detailsCard,
                        specializationCard,
                        reviewsCard
                );

                // SCROLL PANE
        
                ScrollPane scrollPane = new ScrollPane(page);
                scrollPane.setFitToWidth(true);
                scrollPane.setStyle(
                        "-fx-background-color: " +
                                BG + ";" +
                        "-fx-background: " +
                                BG + ";" );

                // SHOW DETAIL
                
                showDetailInExistingDashboard( scrollPane );
        }

        private void showConsultationRequestDialog(DietitianData dietitian) {

                SameStageWindow dialog =
                        new SameStageWindow();

                dialog.initModality(
                        Modality.APPLICATION_MODAL
                );

                dialog.setTitle(
                        "Request Consultation"
                );


                VBox box =
                        new VBox(15);

                box.setPadding(
                        new Insets(25)
                );

                box.setPrefWidth(
                        500
                );

                box.setStyle(
                        "-fx-background-color: "
                                + BG + ";" +
                        "-fx-border-color: "
                                + BORDER + ";"
                );


                // =====================================================
                // TITLE
                // =====================================================

                Label title =
                        new Label(
                                "Request Consultation"
                        );

                title.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
                );


                Label subtitle =
                        new Label(
                                "Request a consultation with "
                                        + dietitian.name
                        );

                subtitle.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";" +
                        "-fx-font-size: 14px;"
                );


                // =====================================================
                // DIETITIAN INFO CARD
                // =====================================================

                HBox dietitianCard =
                        new HBox(12);

                dietitianCard.setAlignment(
                        Pos.CENTER_LEFT
                );

                dietitianCard.setPadding(
                        new Insets(12)
                );

                dietitianCard.setStyle(
                        "-fx-background-color: "
                                + CARD + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: "
                                + BORDER + ";" +
                        "-fx-border-radius: 10;"
                );


                StackPane photo =
                        createSmallProfileImage(
                                dietitian
                        );


                VBox info =
                        new VBox(4);


                Label name =
                        new Label(
                                dietitian.name
                        );

                name.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
                );


                Label specialization =
                        new Label(
                                dietitian.specialization == null
                                        ||
                                dietitian.specialization.trim().isEmpty()

                                        ? "Nutrition"

                                        : dietitian.specialization
                        );

                specialization.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";" +
                        "-fx-font-size: 13px;"
                );


                info.getChildren().addAll(
                        name,
                        specialization
                );


                dietitianCard.getChildren().addAll(
                        photo,
                        info
                );


                // =====================================================
                // PREFERRED DATE
                // =====================================================

                Label dateLabel =
                        new Label(
                                "Preferred Date"
                        );

                dateLabel.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
                );


                javafx.scene.control.DatePicker datePicker =
                        new javafx.scene.control.DatePicker();

                datePicker.setValue(
                        java.time.LocalDate.now()
                                .plusDays(1)
                );

                datePicker.setMaxWidth(
                        Double.MAX_VALUE
                );

                datePicker.setPrefHeight(
                        42
                );


                // DON'T ALLOW PAST DATES
                datePicker.setDayCellFactory(
                        picker ->
                                new javafx.scene.control.DateCell() {

                                        @Override
                                        public void updateItem(
                                                java.time.LocalDate date,
                                                boolean empty
                                        ) {

                                        super.updateItem(
                                                date,
                                                empty
                                        );

                                        setDisable(
                                                empty
                                                        ||
                                                date.isBefore(
                                                        java.time.LocalDate.now()
                                                )
                                        );
                                        }
                                }
                );


                // =====================================================
                // PREFERRED TIME
                // =====================================================

                Label timeLabel =
                        new Label(
                                "Preferred Time"
                        );

                timeLabel.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
                );


                javafx.scene.control.ComboBox<String> timeBox =
                        new javafx.scene.control.ComboBox<>();


                timeBox.getItems().addAll(
                        "09:00 AM",
                        "09:30 AM",
                        "10:00 AM",
                        "10:30 AM",
                        "11:00 AM",
                        "11:30 AM",
                        "12:00 PM",
                        "12:30 PM",
                        "01:00 PM",
                        "01:30 PM",
                        "02:00 PM",
                        "02:30 PM",
                        "03:00 PM",
                        "03:30 PM",
                        "04:00 PM",
                        "04:30 PM",
                        "05:00 PM",
                        "05:30 PM",
                        "06:00 PM",
                        "06:30 PM",
                        "07:00 PM",
                        "07:30 PM",
                        "08:00 PM"
                );


                timeBox.setPromptText(
                        "Select preferred time"
                );

                timeBox.setMaxWidth(
                        Double.MAX_VALUE
                );

                timeBox.setPrefHeight(
                        42
                );


                // =====================================================
                // REASON
                // =====================================================

                Label reasonLabel =
                        new Label(
                                "Reason (Optional)"
                        );

                reasonLabel.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
                );


                TextArea reasonField =
                        new TextArea();

                reasonField.setPromptText(
                        "e.g. I need help with weight loss"
                );

                reasonField.setPrefHeight(
                        90
                );

                reasonField.setWrapText(
                        true
                );

                reasonField.setStyle(
                        "-fx-control-inner-background: "
                                + CARD + ";" +
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-prompt-text-fill: "
                                + SECONDARY + ";" +
                        "-fx-border-color: "
                                + BORDER + ";"
                );


                // =====================================================
                // BUTTONS
                // =====================================================

                Button cancel =
                        new Button(
                                "Cancel"
                        );

                cancel.setPrefWidth(
                        140
                );

                cancel.setPrefHeight(
                        42
                );

                cancel.setStyle(
                        "-fx-background-color: "
                                + CARD + ";" +
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-color: "
                                + BORDER + ";" +
                        "-fx-border-radius: 7;" +
                        "-fx-background-radius: 7;"
                );


                Button sendRequest =
                        new Button(
                                "Send Request"
                        );

                sendRequest.setPrefWidth(
                        160
                );

                sendRequest.setPrefHeight(
                        42
                );

                sendRequest.setStyle(
                        "-fx-background-color: "
                                + GREEN + ";" +
                        "-fx-text-fill: #052E16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 7;"
                );


                Region spacer =
                        new Region();

                HBox.setHgrow(
                        spacer,
                        Priority.ALWAYS
                );


                HBox buttonRow =
                        new HBox(
                                10,
                                cancel,
                                spacer,
                                sendRequest
                        );


                cancel.setOnAction(
                        e -> dialog.close()
                );


                // =====================================================
                // SEND CONSULTATION REQUEST
                // =====================================================

                sendRequest.setOnAction(e -> {

                        java.time.LocalDate selectedDate =
                                datePicker.getValue();

                        String selectedTime =
                                timeBox.getValue();


                        // =================================================
                        // VALIDATION
                        // =================================================

                        if (selectedDate == null) {

                        showCustomMessage(
                                "Date Required",
                                "Please select a preferred date."
                        );

                        return;
                        }


                        if (
                                selectedDate.isBefore(
                                        java.time.LocalDate.now()
                                )
                        ) {

                        showCustomMessage(
                                "Invalid Date",
                                "You cannot select a past date."
                        );

                        return;
                        }


                        if (
                                selectedTime == null ||
                                selectedTime.trim().isEmpty()
                        ) {

                        showCustomMessage(
                                "Time Required",
                                "Please select a preferred time."
                        );

                        return;
                        }


                        try {

                        String userId =
                                SessionManager.getUid();

                        String userName =
                                SessionManager.getName();


                        if (
                                userId == null ||
                                userId.trim().isEmpty()
                        ) {

                                showCustomMessage(
                                        "Session Error",
                                        "Logged-in user could not be identified."
                                );

                                return;
                        }


                        if (
                                dietitian.uid == null ||
                                dietitian.uid.trim().isEmpty()
                        ) {

                                showCustomMessage(
                                        "Dietitian Error",
                                        "Dietitian could not be identified."
                                );

                                return;
                        }


                        // =================================================
                        // CREATE CONSULTATION MODEL
                        // =================================================

                        UserDietitianConsultationModel consultation =
                                new UserDietitianConsultationModel();


                        consultation.setUserId(
                                userId
                        );

                        consultation.setUserName(
                                userName
                        );


                        consultation.setDietitianId(
                                dietitian.uid
                        );

                        consultation.setDietitianName(
                                dietitian.name
                        );


                        consultation.setRequestedDate(
                                selectedDate.toString()
                        );

                        consultation.setRequestedTime(
                                selectedTime
                        );


                        consultation.setReason(
                                reasonField
                                        .getText()
                                        .trim()
                        );


                        consultation.setStatus(
                                "PENDING"
                        );


                        consultation.setCreatedAt(
                                System.currentTimeMillis()
                        );


                        // These values will be updated later
                        // by the dietitian/user flow

                        consultation.setConfirmedDate(
                                null
                        );

                        consultation.setConfirmedTime(
                                null
                        );

                        consultation.setDietitianMessage(
                                null
                        );

                        consultation.setMeetingLink(
                                null
                        );

                        consultation.setAcceptedAt(
                                0L
                        );

                        consultation.setConfirmedAt(
                                0L
                        );

                        consultation.setCompletedAt(
                                0L
                        );

                        consultation.setCancelledAt(
                                0L
                        );


                        // =================================================
                        // DAO
                        // =================================================

                        UserDietitianConsultationDao consultationDao =
                                new UserDietitianConsultationDao();


                        // =================================================
                        // LOADING STATE
                        // =================================================

                        sendRequest.setDisable(
                                true
                        );

                        sendRequest.setText(
                                "Sending..."
                        );


                        // =================================================
                        // SAVE TO FIREBASE
                        // =================================================

                        String consultationId =
                                consultationDao.createConsultation(
                                        consultation
                                );


                        // =================================================
                        // SUCCESS
                        // =================================================

                        if (
                                consultationId != null &&
                                !consultationId.isBlank()
                        ) {

                                dialog.close();


                                showCustomMessage(
                                        "Consultation Requested",
                                        "Your consultation request has been sent to "
                                                + dietitian.name
                                                + ".\n\n"
                                                + "Date: "
                                                + selectedDate
                                                + "\n"
                                                + "Time: "
                                                + selectedTime
                                                + "\n"
                                                + "Status: Pending"
                                );

                        } else {

                                // =============================================
                                // FAILED
                                // =============================================

                                sendRequest.setDisable(
                                        false
                                );

                                sendRequest.setText(
                                        "Send Request"
                                );


                                showCustomMessage(
                                        "Request Failed",
                                        "Unable to create consultation request."
                                );
                        }


                        } catch (Exception ex) {

                        ex.printStackTrace();


                        sendRequest.setDisable(
                                false
                        );

                        sendRequest.setText(
                                "Send Request"
                        );


                        showCustomMessage(
                                "Consultation Error",
                                "Unable to send consultation request."
                        );
                        }
                });


                // =====================================================
                // ADD UI
                // =====================================================

                box.getChildren().addAll(
                        title,
                        subtitle,
                        dietitianCard,

                        dateLabel,
                        datePicker,

                        timeLabel,
                        timeBox,

                        reasonLabel,
                        reasonField,

                        buttonRow
                );


                Scene scene =
                        new Scene(
                                box
                        );


                dialog.setScene(
                        scene
                );

                dialog.showAndWait();
        }

        private void handleConsultationClick(
                        DietitianData dietitian
                ) {

                try {

                        String userId =
                                SessionManager.getUid();


                        if (
                                userId == null ||
                                userId.isBlank()
                        ) {

                        showCustomMessage(
                                "Session Error",
                                "Logged-in user could not be identified."
                        );

                        return;
                        }


                        UserDietitianConsultationDao dao =
                                new UserDietitianConsultationDao();


                        // =================================================
                        // LOAD USER'S CONSULTATIONS
                        // =================================================

                        java.util.List<UserDietitianConsultationModel> consultations =
                                dao.getConsultationsByUserId(
                                        userId
                                );


                        UserDietitianConsultationModel existingConsultation =
                                null;


                        // =================================================
                        // FIND NEWEST ACTIVE CONSULTATION
                        // FOR THIS DIETITIAN
                        // =================================================

                        for (
                                UserDietitianConsultationModel consultation :
                                consultations
                        ) {

                        if (
                                consultation.getDietitianId() != null
                                        &&
                                consultation.getDietitianId()
                                        .equals(dietitian.uid)
                        ) {

                                String status =
                                        consultation.getStatus();


                                // Ignore old consultations
                                if (
                                        "CANCELLED".equals(status)
                                                ||
                                        "COMPLETED".equals(status)
                                ) {

                                continue;
                                }


                                // Keep newest consultation only
                                if (
                                        existingConsultation == null
                                                ||
                                        consultation.getCreatedAt()
                                                > existingConsultation.getCreatedAt()
                                ) {

                                existingConsultation =
                                        consultation;
                                }
                        }
                        }


                        // =================================================
                        // NO ACTIVE CONSULTATION
                        // =================================================

                        if (
                                existingConsultation == null
                        ) {

                        showConsultationRequestDialog(
                                dietitian
                        );

                        return;
                        }


                        // =================================================
                        // DEBUG — KEEP TEMPORARILY
                        // =================================================

                        System.out.println(
                                "===================================="
                        );

                        System.out.println(
                                "ACTIVE CONSULTATION: "
                                        + existingConsultation.getConsultationId()
                        );

                        System.out.println(
                                "STATUS: "
                                        + existingConsultation.getStatus()
                        );

                        System.out.println(
                                "MEETING LINK: "
                                        + existingConsultation.getMeetingLink()
                        );

                        System.out.println(
                                "CREATED AT: "
                                        + existingConsultation.getCreatedAt()
                        );

                        System.out.println(
                                "===================================="
                        );


                        // =================================================
                        // CHECK STATUS
                        // =================================================

                        String status =
                                existingConsultation.getStatus();


                        if (
                                "PENDING".equals(status)
                        ) {

                        showPendingConsultationDialog(
                                existingConsultation
                        );

                        } else if (
                                "DIETITIAN_ACCEPTED".equals(status)
                        ) {

                        showAcceptedConsultationDialog(
                                existingConsultation
                        );

                        } else if (
                                "CONFIRMED".equals(status)
                        ) {

                        showConfirmedConsultationDialog(
                                existingConsultation
                        );

                        } else {

                        showConsultationRequestDialog(
                                dietitian
                        );
                        }


                } catch (Exception ex) {

                        ex.printStackTrace();

                        showCustomMessage(
                                "Consultation Error",
                                "Unable to check consultation status."
                        );
                }
        }

        private void showPendingConsultationDialog(
                        UserDietitianConsultationModel consultation
                ) {

                SameStageWindow dialog =
                        new SameStageWindow();

                dialog.initModality(
                        Modality.APPLICATION_MODAL
                );

                dialog.setTitle(
                        "Consultation Request"
                );


                VBox box =
                        new VBox(15);

                box.setPadding(
                        new Insets(25)
                );

                box.setPrefWidth(
                        450
                );

                box.setStyle(
                        "-fx-background-color: "
                                + BG + ";"
                );


                Label title =
                        new Label(
                                "Consultation Request Pending"
                        );

                title.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
                );


                Label dietitian =
                        new Label(
                                consultation.getDietitianName()
                        );

                dietitian.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
                );


                Label details =
                        new Label(
                                "Requested Date: "
                                        + consultation.getRequestedDate()
                                        + "\n"
                                        + "Requested Time: "
                                        + consultation.getRequestedTime()
                                        + "\n\n"
                                        + "Waiting for the dietitian to accept your request."
                        );

                details.setWrapText(
                        true
                );

                details.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";" +
                        "-fx-font-size: 14px;"
                );


                Button close =
                        new Button(
                                "Close"
                        );

                styleActionButton(
                        close,
                        CARD,
                        TEXT
                );

                close.setOnAction(
                        e -> dialog.close()
                );


                box.getChildren().addAll(
                        title,
                        dietitian,
                        details,
                        close
                );


                dialog.setScene(
                        new Scene(
                                box
                        )
                );

                dialog.showAndWait();
        }

        private void showAcceptedConsultationDialog(
                        UserDietitianConsultationModel consultation
                ) {

                SameStageWindow dialog =
                        new SameStageWindow();

                dialog.initModality(
                        Modality.APPLICATION_MODAL
                );

                dialog.setTitle(
                        "Consultation Accepted"
                );


                VBox box =
                        new VBox(15);

                box.setPadding(
                        new Insets(25)
                );

                box.setPrefWidth(
                        480
                );

                box.setStyle(
                        "-fx-background-color: "
                                + BG + ";"
                );


                Label title =
                        new Label(
                                "Dietitian Accepted Your Request"
                        );

                title.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
                );


                Label dietitian =
                        new Label(
                                consultation.getDietitianName()
                        );

                dietitian.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
                );


                Label confirmedDate =
                        new Label(
                                "Final Date: "
                                        + consultation.getConfirmedDate()
                        );

                confirmedDate.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";"
                );


                Label confirmedTime =
                        new Label(
                                "Final Time: "
                                        + consultation.getConfirmedTime()
                        );

                confirmedTime.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";"
                );


                String messageText =
                        consultation.getDietitianMessage();


                if (
                        messageText == null ||
                        messageText.isBlank()
                ) {

                        messageText =
                                "No message from dietitian.";
                }


                Label message =
                        new Label(
                                "Message: "
                                        + messageText
                        );

                message.setWrapText(
                        true
                );

                message.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";"
                );


                Label confirmationText =
                        new Label(
                                "Please confirm this consultation."
                        );

                confirmationText.setStyle(
                        "-fx-text-fill: "
                                + GREEN + ";" +
                        "-fx-font-weight: bold;"
                );


                Button cancel =
                        new Button(
                                "Cancel"
                        );


                Button confirm =
                        new Button(
                                "Confirm Consultation"
                        );


                styleActionButton(
                        cancel,
                        CARD,
                        TEXT
                );


                styleActionButton(
                        confirm,
                        GREEN,
                        "#052E16"
                );


                cancel.setOnAction(e -> {

                        try {

                        UserDietitianConsultationDao dao =
                                new UserDietitianConsultationDao();


                        boolean cancelled =
                                dao.cancelConsultation(
                                        consultation.getConsultationId()
                                );


                        if (cancelled) {

                                dialog.close();

                                showCustomMessage(
                                        "Consultation Cancelled",
                                        "Your consultation has been cancelled."
                                );

                        } else {

                                showCustomMessage(
                                        "Error",
                                        "Unable to cancel consultation."
                                );
                        }


                        } catch (Exception ex) {

                        ex.printStackTrace();

                        showCustomMessage(
                                "Error",
                                "Unable to cancel consultation."
                        );
                        }
                });


                confirm.setOnAction(e -> {

                        try {

                        UserDietitianConsultationDao dao =
                                new UserDietitianConsultationDao();


                        boolean confirmed =
                                dao.confirmConsultation(
                                        consultation.getConsultationId()
                                );


                        if (confirmed) {

                                consultation.setStatus(
                                        "CONFIRMED"
                                );


                                consultation.setConfirmedAt(
                                        System.currentTimeMillis()
                                );


                                dialog.close();


                                showCustomMessage(
                                        "Consultation Confirmed",
                                        "Your consultation with "
                                                + consultation.getDietitianName()
                                                + " has been confirmed."
                                );


                                showConfirmedConsultationDialog(
                                        consultation
                                );

                        } else {

                                showCustomMessage(
                                        "Error",
                                        "Unable to confirm consultation."
                                );
                        }


                        } catch (Exception ex) {

                        ex.printStackTrace();

                        showCustomMessage(
                                "Error",
                                "Unable to confirm consultation."
                        );
                        }
                });


                HBox buttons =
                        new HBox(
                                10,
                                cancel,
                                confirm
                        );

                buttons.setAlignment(
                        Pos.CENTER_RIGHT
                );


                box.getChildren().addAll(
                        title,
                        dietitian,
                        confirmedDate,
                        confirmedTime,
                        message,
                        confirmationText,
                        buttons
                );


                dialog.setScene(
                        new Scene(
                                box
                        )
                );

                dialog.showAndWait();
        }

        private void showConfirmedConsultationDialog(
                        UserDietitianConsultationModel consultation
                ) {

                SameStageWindow dialog =
                        new SameStageWindow();

                dialog.initModality(
                        Modality.APPLICATION_MODAL
                );

                dialog.setTitle(
                        "Confirmed Consultation"
                );


                VBox box =
                        new VBox(15);

                box.setPadding(
                        new Insets(25)
                );

                box.setPrefWidth(
                        480
                );

                box.setStyle(
                        "-fx-background-color: "
                                + BG + ";"
                );


                Label title =
                        new Label(
                                "Consultation Confirmed"
                        );

                title.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
                );


                Label dietitian =
                        new Label(
                                consultation.getDietitianName()
                        );

                dietitian.setStyle(
                        "-fx-text-fill: "
                                + TEXT + ";" +
                        "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
                );


                Label details =
                        new Label(
                                "Date: "
                                        + consultation.getConfirmedDate()
                                        + "\n"
                                        + "Time: "
                                        + consultation.getConfirmedTime()
                                        + "\n\n"
                                        + "Status: Confirmed"
                        );

                details.setStyle(
                        "-fx-text-fill: "
                                + SECONDARY + ";"
                );


                Button join =
                        new Button(
                                "Join Consultation"
                        );


                styleActionButton(
                        join,
                        GREEN,
                        "#052E16"
                );


                String meetingLink =
                        consultation.getMeetingLink();


                if (
                        meetingLink == null ||
                        meetingLink.isBlank()
                ) {

                        join.setDisable(
                                true
                        );

                        join.setText(
                                "Meeting Link Not Added Yet"
                        );

                } else {

                        join.setOnAction(e -> {

                        try {

                                Desktop.getDesktop()
                                        .browse(
                                                new URI(
                                                        meetingLink
                                                )
                                        );

                        } catch (Exception ex) {

                                ex.printStackTrace();

                                showCustomMessage(
                                        "Meeting Error",
                                        "Unable to open meeting link."
                                );
                        }
                        });
                }


                Button close =
                        new Button(
                                "Close"
                        );


                styleActionButton(
                        close,
                        CARD,
                        TEXT
                );


                close.setOnAction(
                        e -> dialog.close()
                );


                HBox buttons =
                        new HBox(
                                10,
                                close,
                                join
                        );

                buttons.setAlignment(
                        Pos.CENTER_RIGHT
                );


                box.getChildren().addAll(
                        title,
                        dietitian,
                        details,
                        buttons
                );


                dialog.setScene(
                        new Scene(
                                box
                        )
                );

                dialog.showAndWait();
        }

        

      // SUBSCRIPTION PLAN SELECTION

        private void showSubscriptionPlans(
                DietitianData dietitian) {

        try {

                String userUid = SessionManager.getUid();

                DietitianDao dietitianDao =
                        new DietitianDao();

                boolean alreadySubscribed =
                        dietitianDao.isUserSubscribed(
                                dietitian.uid,
                                userUid
                        );

                if (alreadySubscribed) {

                showCustomMessage(
                        "Already Subscribed",
                        "You are already subscribed to "
                                + dietitian.name
                                + "."
                );

                return;
                }

        } catch (Exception ex) {

                ex.printStackTrace();

                showCustomMessage(
                        "Error",
                        "Unable to check subscription status."
                );

                return;
        }

        SameStageWindow dialog = new SameStageWindow();

        dialog.initModality(
                Modality.APPLICATION_MODAL);

        dialog.setTitle(
                "Choose Subscription Plan");

        VBox box = new VBox(18);

        box.setAlignment(Pos.CENTER);

        box.setPadding(
                new Insets(25));

        box.setPrefWidth(500);

        box.setStyle(
                "-fx-background-color: " +
                        CARD +
                        ";" +
                "-fx-border-color: " +
                        BORDER +
                        ";");

        // TITLE

        Label title =
                new Label(
                        "Choose Subscription Plan");

        title.setStyle(
                "-fx-text-fill: " +
                        TEXT +
                        ";" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;");

        Label subtitle =
                new Label(
                        "Select a plan to continue with "
                                + dietitian.name);

        subtitle.setStyle(
                "-fx-text-fill: " +
                        SECONDARY +
                        ";" +
                "-fx-font-size: 14px;");

        // SELECTED PLAN

        Label selectedPlan =
                new Label(
                        "Please select a plan");

        selectedPlan.setStyle(
                "-fx-text-fill: " +
                        SECONDARY +
                        ";" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;");

        // WEEKLY PLAN

        Button weeklyPlan =
                new Button(
                        "Weekly Plan\n₹2,000 / week");

        weeklyPlan.setPrefWidth(210);

        weeklyPlan.setPrefHeight(85);

        weeklyPlan.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT +
                        ";" +
                "-fx-text-fill: " +
                        TEXT +
                        ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " +
                        BORDER +
                        ";" +
                "-fx-border-radius: 10;");

        // MONTHLY PLAN

        Button monthlyPlan =
                new Button(
                        "Monthly Plan\n₹7,000 / month");

        monthlyPlan.setPrefWidth(210);

        monthlyPlan.setPrefHeight(85);

        monthlyPlan.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT +
                        ";" +
                "-fx-text-fill: " +
                        TEXT +
                        ";" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 10;" +
                "-fx-border-color: " +
                        BORDER +
                        ";" +
                "-fx-border-radius: 10;");

        // PAY NOW BUTTON

        Button payNow =
                new Button("Pay Now");

        payNow.setPrefWidth(250);

        payNow.setPrefHeight(45);

        payNow.setDisable(true);

        payNow.setStyle(
                "-fx-background-color: " +
                        GREEN +
                        ";" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;");

        // BACK BUTTON
        Button backButton = new Button("←  Back");

        backButton.setPrefWidth(250);
        backButton.setPrefHeight(42);

        backButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-cursor: hand;"
        );

        backButton.setOnMouseEntered(e ->
                backButton.setStyle(
                        "-fx-background-color: " + CARD_LIGHT + ";" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: " + GREEN + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
                )
        );

        backButton.setOnMouseExited(e ->
                backButton.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-cursor: hand;"
                )
        );

        backButton.setOnAction(e -> dialog.close());

        // SELECTED PLAN HOLDER

        final String[] selectedPlanName =
                {null};

        final int[] selectedAmount =
                {0};

        // WEEKLY CLICK

        weeklyPlan.setOnAction(e -> {

                selectedPlanName[0] =
                        "Weekly Plan";

                selectedAmount[0] =
                        2000;

                selectedPlan.setText(
                        "Selected: Weekly Plan - ₹2,000");

                payNow.setDisable(false);

                weeklyPlan.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                        "-fx-text-fill: #052E16;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " +
                                GREEN_DARK +
                                ";" +
                        "-fx-border-radius: 10;");

                monthlyPlan.setStyle(
                        "-fx-background-color: " +
                                CARD_LIGHT +
                                ";" +
                        "-fx-text-fill: " +
                                TEXT +
                                ";" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " +
                                BORDER +
                                ";" +
                        "-fx-border-radius: 10;");
        });

        // MONTHLY CLICK

        monthlyPlan.setOnAction(e -> {

                selectedPlanName[0] =
                        "Monthly Plan";

                selectedAmount[0] =
                        7000;

                selectedPlan.setText(
                        "Selected: Monthly Plan - ₹7,000");

                payNow.setDisable(false);

                monthlyPlan.setStyle(
                        "-fx-background-color: " +
                                GREEN +
                                ";" +
                        "-fx-text-fill: #052E16;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " +
                                GREEN_DARK +
                                ";" +
                        "-fx-border-radius: 10;");

                weeklyPlan.setStyle(
                        "-fx-background-color: " +
                                CARD_LIGHT +
                                ";" +
                        "-fx-text-fill: " +
                                TEXT +
                                ";" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " +
                                BORDER +
                                ";" +
                        "-fx-border-radius: 10;");
        });

        // PAY NOW

        // PAY NOW

payNow.setOnAction(e -> {

    if (selectedPlanName[0] == null) {

        showCustomMessage(
                "Select Plan",
                "Please select a subscription plan first."
        );

        return;
    }

    payNow.setDisable(true);
    payNow.setText("Opening Payment...");

    System.out.println("=================================");
    System.out.println("PAY NOW CLICKED");
    System.out.println("Plan: " + selectedPlanName[0]);
    System.out.println("Amount: ₹" + selectedAmount[0]);
    System.out.println("User: " + SessionManager.getName());
    System.out.println("=================================");

    try {

        // CREATE RAZORPAY SERVICE
        RazorpayPaymentService razorpay =
                new RazorpayPaymentService();

        System.out.println(
                "RazorpayPaymentService created."
        );

        // CREATE PAYMENT LINK
        String paymentLink =
                razorpay.createPaymentLink(
                        selectedAmount[0],

                        "WhatToEat "
                                + selectedPlanName[0]
                                + " Subscription",

                        SessionManager.getName(),

                        "test@example.com",

                        "9876543210"
                );

        // GET PAYMENT LINK ID
        String paymentLinkId =
                razorpay.getLastPaymentLinkId();

        System.out.println(
                "Razorpay Payment Link: "
                        + paymentLink
        );

        System.out.println(
                "Payment Link ID: "
                        + paymentLinkId
        );

        // CHECK PAYMENT LINK
        if (paymentLink == null ||
                paymentLink.trim().isEmpty()) {

            throw new Exception(
                    "Razorpay returned an empty payment link."
            );
        }

        // CHECK DESKTOP SUPPORT
        if (!Desktop.isDesktopSupported()) {

            throw new Exception(
                    "Desktop browser is not supported."
            );
        }

        Desktop desktop =
                Desktop.getDesktop();

        if (!desktop.isSupported(
                Desktop.Action.BROWSE)) {

            throw new Exception(
                    "Browser opening is not supported."
            );
        }

        // OPEN RAZORPAY
        System.out.println(
                "Opening Razorpay in browser..."
        );

        desktop.browse(
                new URI(paymentLink)
        );

        System.out.println(
                "Razorpay browser open command sent."
        );

        // CLOSE PLAN DIALOG
        dialog.close();


        // =================================================
        // CHECK PAYMENT STATUS IN BACKGROUND
        // =================================================

        Thread paymentChecker =
                new Thread(() -> {

                    try {

                        // Check for maximum 5 minutes
                        for (int i = 0; i < 100; i++) {

                            // Wait 3 seconds
                            Thread.sleep(3000);

                            boolean paid =
                                    razorpay.isPaymentCompleted(
                                            paymentLinkId
                                    );

                            System.out.println(
                                    "Payment Status Check "
                                            + (i + 1)
                                            + ": "
                                            + paid
                            );


                            // =====================================
                            // PAYMENT SUCCESSFUL
                            // =====================================

                            if (paid) {

                                System.out.println(
                                        "Payment Successful!"
                                );

                                String userUid =
                                        SessionManager.getUid();


                                // SAVE SUBSCRIPTION
                                DietitianDao dietitianDao =
                                        new DietitianDao();

                                dietitianDao.saveSubscriber(
                                        dietitian.uid,
                                        userUid,
                                        SessionManager.getName(),
                                        selectedPlanName[0],
                                        selectedAmount[0],
                                        paymentLinkId
                                );


                                // INCREASE ACTIVE USERS
                                dietitianDao.incrementActiveUsers(
                                        dietitian.uid
                                );


                                // UPDATE JAVA FX UI
                                Platform.runLater(() -> {

                                    // IMPORTANT
                                    // Change subscription status
                                    dietitian.subscribed = true;

                                    // Increase local active users
                                    dietitian.activeUsers++;


                                    // SHOW SUCCESS POPUP
                                    showCustomMessage(
                                            "Payment Successful",
                                            "Your "
                                                    + selectedPlanName[0]
                                                    + " subscription with "
                                                    + dietitian.name
                                                    + " is now active."
                                    );


                                    // REFRESH DETAIL PAGE
                                    // This recreates the Subscribe button
                                    // and it will now show ✓ Subscribed
                                    openDietitianDetails(
                                            dietitian
                                    );

                                });

                                return;
                            }
                        }


                        // =====================================
                        // PAYMENT NOT COMPLETED
                        // =====================================

                        Platform.runLater(() -> {

                            showCustomMessage(
                                    "Payment Pending",
                                    "Payment was not completed."
                                            + "\nPlease complete the Razorpay payment."
                            );

                        });


                    } catch (Exception ex) {

                        ex.printStackTrace();

                        Platform.runLater(() -> {

                            showCustomMessage(
                                    "Payment Error",
                                    "Unable to verify the payment."
                            );

                        });

                    }

                });


        // RUN PAYMENT CHECKER IN BACKGROUND
        paymentChecker.setDaemon(true);
        paymentChecker.start();


    } catch (Exception ex) {

        System.out.println(
                "========== RAZORPAY ERROR =========="
        );

        ex.printStackTrace();

        System.out.println(
                "===================================="
        );

        payNow.setDisable(false);
        payNow.setText("Pay Now");

        showCustomMessage(
                "Razorpay Error",
                "Unable to open Razorpay payment page.\n"
                        + "Please check the Eclipse Console."
        );
    }
});

        // PLAN ROW

        HBox plans =
                new HBox(15);

        plans.setAlignment(
                Pos.CENTER);

        plans.getChildren().addAll(
                weeklyPlan,
                monthlyPlan);

        box.getChildren().addAll(
                title,
                subtitle,
                plans,
                selectedPlan,
                payNow,
                backButton
        );

        Scene scene =
                new Scene(box);

        dialog.setScene(scene);

        dialog.showAndWait();
        }

    // SUBSCRIBE BUTTON
    
    private void updateSubscribeButton(
            Button button,
            DietitianData dietitian) {
        if (dietitian.subscribed) {
            button.setText("✓  Subscribed");
            button.setDisable(true);
            button.setOpacity(1.0);
            styleActionButton(
                    button,
                    GREEN_DARK,
                    TEXT);
        } else {
            button.setText("♟  Subscribe");
            button.setDisable(false);
            button.setOpacity(1.0);
            styleActionButton(
                    button,
                    GREEN,
                    "#052E16");
        }
    }
    // DETAIL ACTIVE USER
    private void updateDetailActiveLabel(
            Label label,
            DietitianData dietitian) {
        if (dietitian.subscribed) {
            label.setText(
                    "● " +
                    dietitian.activeUsers +
                    " Active Users    ✓ Subscribed" );
        } else {
            label.setText(
                    "● " +
                    dietitian.activeUsers +
                    " Active Users" );
        }
        label.setStyle(
                "-fx-text-fill: " +
                        BLUE + ";" +
                "-fx-font-size: 15px;" );
    }

    // LARGE IMAGE
    
    private StackPane createLargeProfileImage(
            DietitianData dietitian) {
        StackPane holder = new StackPane();
        holder.setPrefSize(160, 160 );
        Circle background = new Circle(80);
        background.setFill(Color.web("#064E3B") );

        try {

            Image image = new Image(
                            getClass()
                                    .getResourceAsStream( dietitian.imagePath));

            if (!image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth( 155 );
                imageView.setFitHeight( 155 );
                imageView.setPreserveRatio( true );
                Circle clip = new Circle( 77.5, 77.5, 77.5 );
                imageView.setClip( clip );
                holder.getChildren()
                        .addAll( background, imageView );
                return holder;
            }

        } catch (Exception ignored) {
        }

        holder.getChildren()
                .add(background);

        return holder;
    }

    // DETAIL CARD
   
    private VBox createDetailCard(
            String headingText,
            String contentText) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(20) );
        card.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 12;" );

        Label heading = new Label( headingText );
        heading.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" );

        Label content = new Label( contentText );
        content.setWrapText(true );
        content.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 15px;" );
        card.getChildren()
                .addAll(heading, content );

        return card;
    }

    // REVIEWS CARD
   
    private VBox createReviewsCard(
            DietitianData dietitian) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 12;" );

        Label title = new Label("Reviews     ★ " +
                        dietitian.rating +
                        " / 5     " +
                        dietitian.reviews +
                        " reviews" );
        title.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" );

        card.getChildren() .add(title);
        card.getChildren() .add(new Separator());

        for (ReviewData review :
                dietitian.reviewList) {

            VBox reviewBox = createSingleReview(
                            review.userName,
                            review.reviewText,
                            review.stars );

            card.getChildren()
                    .add(reviewBox);

            card.getChildren()
                    .add(new Separator());
        }

        return card;
    }

    // SINGLE REVIEW
    
    private VBox createSingleReview(
            String user,
            String review,
            int stars) {

        VBox box = new VBox(5);

        Label userLabel = new Label("👤 " + user );
        userLabel.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" );
        Label rating = new Label( "★".repeat(stars) );
        rating.setStyle(
                "-fx-text-fill: " +
                        GREEN + ";" +
                "-fx-font-size: 14px;" );

        Label text = new Label( review);
        text.setWrapText(true );
        text.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;" );

        box.getChildren()
                .addAll(userLabel, rating, text );
        return box;
    }

    // ADD REVIEW
    
    private void showReviewDialog(
            DietitianData dietitian) {

        SameStageWindow dialog = new SameStageWindow();

        dialog.initModality(
                Modality.APPLICATION_MODAL );
        dialog.setTitle( "Add Review" );
        VBox box = new VBox(15);
        box.setPadding(new Insets(25));
        box.setPrefWidth(430 );
        box.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-border-color: " +
                        BORDER + ";" );

        Label title =new Label("Review " + dietitian.name );
        title.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" );

        TextArea reviewField = new TextArea();
        reviewField.setPromptText("Write your review..." );
        reviewField.setPrefHeight(120 );
        reviewField.setWrapText(true );
        reviewField.setStyle(
                "-fx-control-inner-background: " +
                        BG + ";" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: " +
                        SECONDARY + ";" +
                "-fx-border-color: " +
                        BORDER + ";" );

        Label ratingTitle = new Label("Your Rating" );
        ratingTitle.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-weight: bold;" );

        HBox stars = new HBox(5);
        int[] selectedRating = {5};
        for (int i = 1; i <= 5; i++) {
            final int starValue = i;
            Button star = new Button("★");
            star.setPrefWidth( 45 );
            star.setStyle(
                    "-fx-background-color: " +
                            CARD_LIGHT + ";" +
                    "-fx-text-fill: " +
                            GREEN + ";" +
                    "-fx-font-size: 18px;" +
                    "-fx-background-radius: 6;" );

            star.setOnAction(e -> {
                selectedRating[0] = starValue;
                for (
                        javafx.scene.Node node :
                        stars.getChildren()
                ) {

                    if (node instanceof Button) {
                        Button b = (Button) node;
                        b.setStyle(
                                "-fx-background-color: " +
                                        CARD_LIGHT + ";" +
                                "-fx-text-fill: " +
                                        GREEN + ";" +
                                "-fx-font-size: 18px;" );
                    }
                }

                for (int j = 0;
                     j < starValue;
                     j++) {

                    Button b = (Button)
                                    stars.getChildren()
                                            .get(j);
                    b.setStyle(
                            "-fx-background-color: " +
                                    GREEN + ";" +
                            "-fx-text-fill: #052E16;" +
                            "-fx-font-size: 18px;" );
                }
            });

            stars.getChildren()
                    .add(star);
        }

        Button submit = new Button("Submit Review" );
        submit.setMaxWidth( Double.MAX_VALUE );
        submit.setPrefHeight(42);
        submit.setStyle(
                "-fx-background-color: " +
                        GREEN + ";" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-weight: bold;" );
        submit.setOnAction(e -> {
            String reviewText =
                    reviewField
                            .getText()
                            .trim();

            if (reviewText.isEmpty()) {
                showCustomMessage(
                        "Review",
                        "Please write a review first." );
                return;
            }

           // SAVE REVIEW TO FIREBASE

String userUid = SessionManager.getUid();

DietitianDao dietitianDao = new DietitianDao();

dietitianDao.saveReview(
        dietitian.uid,
        userUid,
        "You",
        selectedRating[0],
        reviewText
);
// UPDATE CURRENT PAGE

dietitian.reviewList.add(
        new ReviewData(
                "You",
                reviewText,
                selectedRating[0]
        )
);

dietitian.reviews++;
            showCustomMessage(
                    "Review Added",
                    "Your review has been added successfully." );
            dialog.close();

            // Refresh detail page
            openDietitianDetails(dietitian );
        });

        box.getChildren()
                .addAll( title, ratingTitle, stars, reviewField, submit );
        Scene scene = new Scene(box);
        dialog.setScene( scene );
        dialog.showAndWait();
    }

    // WHATSAPP STYLE CHAT
    
    private void openChat(
            DietitianData dietitian) {

        BorderPane chatRoot =  new BorderPane();
        chatRoot.setStyle( "-fx-background-color: " + BG + ";" );

        // HEADER
       
        HBox header = new HBox(12);
        header.setPadding(new Insets(12) );
        header.setAlignment( Pos.CENTER_LEFT );
        header.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-border-color: " +
                        BORDER + ";" );

        Button backButton = new Button("←  Back");
        backButton.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT + ";" +
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-border-radius: 7;" );

        StackPane smallPhoto =createSmallProfileImage( dietitian );

        VBox headerText = new VBox(3);
        Label name = new Label( dietitian.name );
        name.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;" );

        Label status = new Label( "● Online" );
        status.setStyle(
                "-fx-text-fill: " +
                        GREEN + ";" +
                "-fx-font-size: 12px;" );
        headerText.getChildren()
                .addAll( name, status );
        header.getChildren()
                .addAll( backButton, smallPhoto, headerText );

        // CHAT MESSAGES
      
        VBox messages = new VBox(10);
        messages.setPadding( new Insets(15) );
        messages.setStyle("-fx-background-color: " + BG + ";" );

        addReceivedMessage(
                messages,
                "Hello! How can I help you with your nutrition today?" );
        addReceivedMessage(
                messages,
                "Feel free to tell me about your goal." );

        ScrollPane chatScroll = new ScrollPane( messages );
        chatScroll.setFitToWidth(true );
        chatScroll.setStyle(
                "-fx-background-color: " +
                        BG + ";" +
                "-fx-background: " +
                        BG + ";" );

        // INPUT
        
        HBox inputArea = new HBox(8);
        inputArea.setPadding(new Insets(10) );
        inputArea.setStyle("-fx-background-color: " +CARD + ";" );
        TextField messageField = new TextField();
        messageField.setPromptText( "Type a message..." );
        messageField.setPrefHeight( 42 );
        messageField.setStyle(
                "-fx-background-color: " +
                        BG + ";" +
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-prompt-text-fill: " +
                        SECONDARY + ";" +
                "-fx-border-color: " +
                        BORDER + ";" +
                "-fx-background-radius: 20;" +
                "-fx-border-radius: 20;" +
                "-fx-padding: 0 15 0 15;" );

        HBox.setHgrow(messageField, Priority.ALWAYS );
        Button send = new Button("➤");
        send.setPrefSize( 45, 42 );
        send.setStyle(
                "-fx-background-color: " +
                        GREEN + ";" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 22;");

        send.setOnAction(e -> {
            String messageText = messageField
                            .getText()
                            .trim();
            if (messageText.isEmpty()) {
                return;
            }

            addSentMessage(messages,messageText );
            messageField.clear();
            chatScroll.setVvalue(1.0);
        });
        messageField.setOnAction(
                e -> send.fire() );

        inputArea.getChildren()
                .addAll( messageField, send );
        chatRoot.setTop(header);
        chatRoot.setCenter(chatScroll);
        chatRoot.setBottom(inputArea);

        // BACK TO DIETITIAN DETAIL PAGE
      
        backButton.setOnAction(
                e -> openDietitianDetails(dietitian));

        // SHOW CHAT IN EXISTING DASHBOARD
       
        if (dashboardPane != null) {
            dashboardPane.setCenter(chatRoot);
        }
    }

    // RECEIVED MESSAGE
    
    private void addReceivedMessage(
            VBox messages,
            String text) {

        HBox row = new HBox();
        row.setAlignment( Pos.CENTER_LEFT );
        Label bubble = new Label( text );
        bubble.setWrapText(true);
        bubble.setMaxWidth(330 );
        bubble.setStyle(
                "-fx-background-color: " +
                        CARD_LIGHT + ";" +
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-padding: 10 14 10 14;" +
                "-fx-background-radius: 12 12 12 3;" +
                "-fx-font-size: 14px;" );
        row.getChildren()
                .add(bubble);
        messages.getChildren()
                .add(row);
    }

    // SENT MESSAGE
    
    private void addSentMessage(
            VBox messages,
            String text) {

        HBox row =new HBox();
        row.setAlignment(Pos.CENTER_RIGHT);
        Label bubble = new Label(text );
        bubble.setWrapText( true );
        bubble.setMaxWidth(330);
        bubble.setStyle(
                "-fx-background-color: " +
                        GREEN + ";" +
                "-fx-text-fill: #052E16;" +
                "-fx-padding: 10 14 10 14;" +
                "-fx-background-radius: 12 12 3 12;" +
                "-fx-font-size: 14px;" );
        row.getChildren() .add(bubble);
        messages.getChildren() .add(row);
    }

    // SMALL PROFILE
    
    private StackPane createSmallProfileImage(
            DietitianData dietitian) {
        StackPane holder = new StackPane();
        holder.setPrefSize( 45,45 );
        Circle background = new Circle(22.5);
        background.setFill( Color.web("#064E3B") );
        try {

            Image image = new Image(getClass().getResourceAsStream( dietitian.imagePath ) );
            if (!image.isError()) {
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth( 43 );
                imageView.setFitHeight(43);
                imageView.setPreserveRatio( true );
                Circle clip = new Circle( 21.5, 21.5, 21.5 );
                imageView.setClip(clip );

                holder.getChildren()
                        .addAll( background, imageView );
                return holder;
            }

        } catch (Exception ignored) {
        }
        holder.getChildren()
                .add(background);
        return holder;
    }

    // BUTTON STYLE
    
    private void styleActionButton(
            Button button,
            String background,
            String textColor) {
        button.setPrefWidth(175);
        button.setPrefHeight(45);
        button.setStyle(
                "-fx-background-color: " +
                        background + ";" +
                "-fx-text-fill: " +
                        textColor + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: " +
                        BORDER + ";" );
    }

    // CUSTOM MESSAGE
    
    private void showCustomMessage(
            String titleText,
            String messageText) {
        SameStageWindow dialog = new SameStageWindow();
        dialog.initModality(Modality.APPLICATION_MODAL );
        dialog.setTitle( titleText );
        VBox box = new VBox(18);
        box.setAlignment(Pos.CENTER );
        box.setPadding(new Insets(25) );
        box.setPrefWidth( 380);
        box.setStyle(
                "-fx-background-color: " +
                        CARD + ";" +
                "-fx-border-color: " +
                        BORDER + ";");

        Label title =new Label(titleText );
        title.setStyle(
                "-fx-text-fill: " +
                        TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" );
        Label message = new Label( messageText );
        message.setWrapText(true);
        message.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;" );
        Button ok = new Button( "OK" );
        ok.setPrefWidth( 100 );
        ok.setStyle(
                "-fx-background-color: " +
                        GREEN + ";" +
                "-fx-text-fill: #052E16;" +
                "-fx-font-weight: bold;" );
        ok.setOnAction(
                e -> dialog.close() );

        box.getChildren()
                .addAll( title, message, ok );
        Scene scene = new Scene( box );
        dialog.setScene( scene );
        dialog.showAndWait();
    }

    // SHOW DETAIL PAGE
    
    private void showDetailInExistingDashboard(
            ScrollPane detailPage) {

        // NEW FIX
        // Direct dashboardPane reference use hoga

        if (dashboardPane == null) {

            // Fallback agar constructor without BorderPane use hua
            if (cardsContainer != null &&
                    cardsContainer.getScene() != null) {
                javafx.scene.Node root =
                        cardsContainer
                                .getScene()
                                .getRoot();
                if (root instanceof BorderPane) {
                    BorderPane borderPane = (BorderPane) root;
                    borderPane.setCenter(
                            detailPage );
                }
            }
            return;
        }
        dashboardPane.setCenter( detailPage );
    }

    // BACK TO DIETITIANS
    
    private void showListPage() {

        // MAIN FIX

        if (dashboardPane == null) {
            return;
        }

        // Center mein list page wapas set karo

        dashboardPane.setCenter( mainPage );

        // Cards refresh karo

        updateCards( searchField == null  ? "" : searchField.getText() );
    }

    // SPACER
    
    private static class RegionSpacer
            extends javafx.scene.layout.Region {
        RegionSpacer() {
            HBox.setHgrow( this, Priority.ALWAYS );
        }
    }
}
