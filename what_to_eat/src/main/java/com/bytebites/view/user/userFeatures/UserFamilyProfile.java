package com.bytebites.view.user.userFeatures;

import java.io.File;
import java.util.List;

import com.bytebites.controller.FamilyController;
import com.bytebites.model.FamilyMember;
import com.bytebites.model.session.SessionManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import com.bytebites.view.common.SameStageDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

/**
 * Family Profile page
 */
public class UserFamilyProfile {

        private static final String BG_COLOR = "#0F172A";
        private static final String CARD_COLOR = "#1E293B";
        private static final String INPUT_COLOR = "#111827";
        private static final String BORDER_COLOR = "#334155";
        private static final String GREEN = "#22C55E";
        private static final String GREEN_DARK = "#16A34A";
        private static final String SECONDARY_TEXT = "#94A3B8";

        // =========================================================
        // MAIN VARIABLES
        // =========================================================

        private VBox membersContainer;
        private Label emptyMessage;
        private VBox mainContent;

        // =========================================================
        // CONSTRUCTOR
        // =========================================================

        public UserFamilyProfile() {

        }

        // MAIN FAMILY PROFILE PAGE

        public VBox getFamilyProfile() {

                mainContent = new VBox(15);

                mainContent.setPadding(
                
                new Insets(25));

                mainContent.setStyle(
                                "-fx-background-color: " + BG_COLOR + ";");

                HBox header = new HBox();

                header.setAlignment(Pos.CENTER_LEFT);

                VBox titleBox = new VBox(4);

                Label title = new Label("Family Profile");

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 30px;" +
                                                "-fx-font-weight: bold;");

                Label subtitle = new Label("Manage dietary requirements and preferences for your household.");

                subtitle.setStyle(
                                "-fx-text-fill: " + SECONDARY_TEXT + ";" +
                                                "-fx-font-size: 14px;");

                titleBox.getChildren().addAll(
                                title,
                                subtitle);

                HBox.setHgrow(titleBox, Priority.ALWAYS);

                // =====================================================
                // ADD MEMBER BUTTON
                // =====================================================

                Button addMemberButton = new Button("+ Add Member");

                addMemberButton.setPrefHeight(42);
                addMemberButton.setPrefWidth(145);

                addMemberButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;");

                addMemberButton.setOnMouseEntered(e -> addMemberButton.setStyle(
                                "-fx-background-color: " + GREEN_DARK + ";" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;"));

                addMemberButton.setOnMouseExited(e -> addMemberButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 8;"));

                header.getChildren().addAll(
                                titleBox,
                                addMemberButton);

                emptyMessage = new Label(
                                "No family members yet. Click \"+ Add Member\" to add your first family member.");

                emptyMessage.setStyle(
                                "-fx-text-fill: " + SECONDARY_TEXT + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-padding: 10 0 10 0;");

                membersContainer = new VBox(15);
                membersContainer.setPadding(
                                new Insets(5, 5, 20, 5));

                // SCROLL PANE

                ScrollPane memberScroll = new ScrollPane();

                memberScroll.setContent(membersContainer);
                memberScroll.setFitToWidth(true);
                memberScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                memberScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                memberScroll.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-background: transparent;" +
                                                "-fx-border-color: transparent;");

                VBox.setVgrow(memberScroll, Priority.ALWAYS);

                // ADD MEMBER ACTION

                addMemberButton.setOnAction(
                                e -> showAddMemberDialog());

                // ADD EVERYTHING

                mainContent.getChildren().addAll(header, emptyMessage, memberScroll);
                loadFamilyMembers();
                return mainContent;
        }

        // ADD MEMBER DIALOG

        private void showAddMemberDialog() {

                SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

dialog.setTitle("Add Family Member");
dialog.setHeaderText(null);

if (mainContent != null && mainContent.getScene() != null) {
    dialog.initOwner(mainContent.getScene().getWindow());
    dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
}

                VBox form = new VBox(12);
                form.setPadding(new Insets(20));
                form.setStyle("-fx-background-color: " + BG_COLOR + ";");
                // TITLE
                Label formTitle = new Label("Enter Family Member Details");
                formTitle.setMinHeight(26);
                formTitle.setPrefHeight(26);
                formTitle.setMaxHeight(26);
                formTitle.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                Label formSubtitle = new Label("Enter all required information to create the profile.");

                formSubtitle.setStyle(
                                "-fx-text-fill: " + SECONDARY_TEXT + ";" +
                                                "-fx-font-size: 12px;");

                // NAME

                Label nameLabel = createFieldLabel("Name");

                TextField nameField = createTextField("Enter name");

                // GENDER

                Label genderLabel = createFieldLabel("Gender");

                ComboBox<String> genderBox = createComboBox();

                genderBox.getItems().addAll(
                                "Male",
                                "Female",
                                "Prefer not to say");

                genderBox.setPromptText("Select gender");

                // AGE

                Label ageLabel = createFieldLabel("Age");

                TextField ageField = createTextField("Enter age");
                // WEIGHT

                Label weightLabel = createFieldLabel("Weight (kg)");

                TextField weightField = createTextField("Enter weight");

                // HEIGHT

                Label heightLabel = createFieldLabel("Height (cm)");

                TextField heightField = createTextField("Enter height");

                // BMI

                Label bmiTitle = createFieldLabel("BMI");

                Label bmiLabel = new Label("BMI will be calculated automatically");

                bmiLabel.setStyle(
                                "-fx-text-fill: " + GREEN + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;");

                Runnable calculateBMI = () -> {

                        try {

                                double weight = Double.parseDouble(weightField
                                                .getText()
                                                .trim());

                                double heightCm = Double.parseDouble(
                                                heightField
                                                                .getText()
                                                                .trim());

                                if (weight > 0 && heightCm > 0) {

                                        double heightMeter = heightCm / 100.0;

                                        double bmi = weight / (heightMeter * heightMeter);

                                        bmiLabel.setText(String.format("BMI: %.1f", bmi));

                                } else {

                                        bmiLabel.setText("BMI will be calculated automatically");
                                }

                        } catch (NumberFormatException ex) {

                                bmiLabel.setText("BMI will be calculated automatically");
                        }
                };

                weightField.textProperty().addListener((obs, oldValue, newValue) -> calculateBMI.run());

                heightField.textProperty().addListener(
                                (obs, oldValue, newValue) -> calculateBMI.run());

                // ALLERGY

                Label allergyLabel = createFieldLabel("Allergy");

                TextField allergyField = createTextField("Example: Peanuts, Milk, Nuts");

                // Multiple allergies can be entered
                // Example:Peanuts, Milk, Nuts

                // HEALTH ISSUE

                Label healthLabel = createFieldLabel("Health Issue");

                TextField healthField = createTextField("Example: Diabetes, Thyroid");

                // Multiple health issues can be entered
                // Example: Diabetes, Thyroid

                // GOAL

                Label goalLabel = createFieldLabel("Goal");

                ComboBox<String> goalBox = createComboBox();

                goalBox.getItems().addAll(
                                "Weight Loss",
                                "Weight Gain",
                                "Muscle Gain",
                                "Nothing");

                goalBox.setPromptText("Select goal");

                // PROFILE PHOTO

                Label photoLabel = createFieldLabel("Profile Photo (Optional)");
                HBox photoRow = new HBox(10);

                photoRow.setAlignment(Pos.CENTER_LEFT);

                Button choosePhotoButton = new Button("Choose Photo");

                choosePhotoButton.setStyle(
                                "-fx-background-color: " + BORDER_COLOR + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                Label photoNameLabel = new Label("No photo selected");

                photoNameLabel.setStyle("-fx-text-fill: " + SECONDARY_TEXT + ";" + "-fx-font-size: 12px;");

                final String[] selectedPhotoPath = { null };

                // PHOTO FILE CHOOSER

                choosePhotoButton.setOnAction(e -> {

                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Choose Profile Photo");
                        fileChooser.getExtensionFilters()
                                        .add(
                                                        new FileChooser.ExtensionFilter(
                                                                        "Image Files",
                                                                        "*.png",
                                                                        "*.jpg",
                                                                        "*.jpeg"));

                        File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());

                        if (selectedFile != null) {

                                selectedPhotoPath[0] = selectedFile.getAbsolutePath();

                                photoNameLabel.setText(selectedFile.getName());

                                photoNameLabel.setStyle(
                                                "-fx-text-fill: " + GREEN + ";" +
                                                                "-fx-font-size: 12px;");
                        }
                });

                photoRow.getChildren().addAll(
                                choosePhotoButton,
                                photoNameLabel);

                // ADD FORM COMPONENTS

                form.getChildren().addAll(
                                formTitle,
                                formSubtitle,

                                nameLabel,
                                nameField,

                                genderLabel,
                                genderBox,

                                ageLabel,
                                ageField,

                                weightLabel,
                                weightField,

                                heightLabel,
                                heightField,

                                bmiTitle,
                                bmiLabel,

                                allergyLabel,
                                allergyField,

                                healthLabel,
                                healthField,

                                goalLabel,
                                goalBox,

                                photoLabel,
                                photoRow);

                // SCROLL INSIDE DIALOG

                ScrollPane formScroll = new ScrollPane(form);
                formScroll.setFitToWidth(true);
                formScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                // Keep the viewport dimensions stable. An AS_NEEDED scrollbar can
                // repeatedly appear/disappear while the overlay is laid out,
                // which makes the title and dialog buttons flicker.
                formScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
                formScroll.setMinWidth(460);
                formScroll.setPrefWidth(460);
                formScroll.setMaxWidth(460);
                formScroll.setMinHeight(540);
                formScroll.setPrefHeight(540);
                formScroll.setMaxHeight(540);
                formScroll.setVvalue(0);
                formScroll.setStyle(
                                "-fx-background-color: " + BG_COLOR + ";" +
                                                "-fx-background: " + BG_COLOR + ";" +
                                                "-fx-border-color: transparent;");

                // DIALOG PANE

                dialog.getDialogPane().setContent(formScroll);
                dialog.getDialogPane().setMinWidth(500);
                dialog.getDialogPane().setPrefWidth(500);
                dialog.getDialogPane().setMaxWidth(500);
                dialog.getDialogPane().setMinHeight(650);
                dialog.getDialogPane().setPrefHeight(650);
                dialog.getDialogPane().setMaxHeight(650);
                dialog.getDialogPane()
                                .setStyle(
                                                "-fx-background-color: " +
                                                                BG_COLOR + ";");

                // BUTTONS

                ButtonType submitButtonType = new ButtonType("Submit",
                                javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButtonType = new ButtonType("Cancel",
                                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

                dialog.getDialogPane()
                                .getButtonTypes()
                                .addAll(
                                                submitButtonType,
                                                cancelButtonType);

                Button submitButton = (Button) dialog.getDialogPane()
                                .lookupButton(
                                                submitButtonType);

                Button cancelButton = (Button) dialog.getDialogPane()
                                .lookupButton(
                                                cancelButtonType);

                submitButton.setStyle(
                                "-fx-background-color: " + GREEN + ";" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                cancelButton.setStyle(
                                "-fx-background-color: " + BORDER_COLOR + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                // VALIDATION

                Runnable validateForm = () -> {

                        boolean valid = !nameField.getText()
                                        .trim()
                                        .isEmpty()

                                        && genderBox.getValue() != null

                                        && !ageField.getText()
                                                        .trim()
                                                        .isEmpty()

                                        && !weightField.getText()
                                                        .trim()
                                                        .isEmpty()

                                        && !heightField.getText()
                                                        .trim()
                                                        .isEmpty()

                                        && !allergyField.getText()
                                                        .trim()
                                                        .isEmpty()

                                        && !healthField.getText()
                                                        .trim()
                                                        .isEmpty()

                                        && goalBox.getValue() != null;

                        submitButton.setDisable(
                                        !valid);
                };

                nameField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                ageField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                weightField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                heightField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                allergyField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                healthField.textProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                genderBox.valueProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                goalBox.valueProperty().addListener(
                                (obs, oldValue, newValue) -> validateForm.run());

                // Submit initially disabled
                submitButton.setDisable(true);

                // SHOW DIALOG

                dialog.showAndWait().ifPresent(result -> {
                        if (result == submitButtonType) {

                                System.out.println("Submit Clicked");

                                System.out.println("Session UID = " + SessionManager.getUid());

                                FamilyMember member = new FamilyMember();

                                member.setMemberId(java.util.UUID.randomUUID().toString());
                                member.setUserId(SessionManager.getUid());
                                member.setName(nameField.getText().trim());
                                member.setGender(genderBox.getValue());
                                member.setAge(Integer.parseInt(ageField.getText().trim()));

                                member.setWeight(Double.parseDouble(weightField.getText().trim()));

                                member.setHeight(Double.parseDouble(heightField.getText().trim()));

                                double heightMeter = member.getHeight() / 100.0;

                                member.setBmi(member.getWeight() / (heightMeter * heightMeter));

                                member.setAllergy(allergyField.getText().trim());

                                member.setHealthIssue(healthField.getText().trim());

                                member.setGoal(goalBox.getValue());

                                member.setPhotoUrl(
                                                selectedPhotoPath[0] == null
                                                                ? ""
                                                                : selectedPhotoPath[0]);

                                FamilyController controller = new FamilyController();

                                /*
                                 * System.out.println("UserId = " + member.getUserId());
                                 * System.out.println("MemberId = " + member.getMemberId());
                                 * System.out.println("Name = " + member.getName());
                                 * System.out.println("Gender = " + member.getGender());
                                 * System.out.println("Age = " + member.getAge());
                                 * System.out.println("Weight = " + member.getWeight());
                                 * System.out.println("Height = " + member.getHeight());
                                 * System.out.println("BMI = " + member.getBmi());
                                 * System.out.println("Allergy = " + member.getAllergy());
                                 * System.out.println("Health = " + member.getHealthIssue());
                                 * System.out.println("Goal = " + member.getGoal());
                                 * System.out.println("Photo = " + member.getPhotoUrl());
                                 */

                                boolean success = controller.saveFamilyMember(member);

                                if (success) {

                                        addMemberCard(
                                                        member.getMemberId(),
                                                        nameField.getText().trim(),
                                                        genderBox.getValue(),
                                                        ageField.getText().trim(),
                                                        weightField.getText().trim(),
                                                        heightField.getText().trim(),
                                                        bmiLabel.getText(),
                                                        allergyField.getText().trim(),
                                                        healthField.getText().trim(),
                                                        goalBox.getValue(),
                                                        selectedPhotoPath[0]);
                                }
                        }
                });
        }
        // ADD MEMBER CARD

        private void addMemberCard(
                        String memberId,
                        String name,
                        String gender,
                        String age,
                        String weight,
                        String height,
                        String bmi,
                        String allergy,
                        String healthIssue,
                        String goal,
                        String photoPath) {

                VBox memberCard = createMemberCard(
                                memberId,
                                name,
                                gender,
                                age,
                                weight,
                                height,
                                bmi,
                                allergy,
                                healthIssue,
                                goal,
                                photoPath);

                membersContainer.getChildren()
                                .add(memberCard);

                emptyMessage.setVisible(false);
                emptyMessage.setManaged(false);
        }

        // CREATE MEMBER CARD

        private VBox createMemberCard(
                        String memberId,
                        String name,
                        String gender,
                        String age,
                        String weight,
                        String height,
                        String bmi,
                        String allergy,
                        String healthIssue,
                        String goal,
                        String photoPath) {

                VBox card = new VBox(10);

                card.setPadding(new Insets(18));
                card.setMaxWidth(Double.MAX_VALUE);
                card.setStyle(
                                "-fx-background-color: " +
                                                CARD_COLOR + ";" +

                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +

                                                "-fx-border-width: 1px;" +

                                                "-fx-border-radius: 10px;" +

                                                "-fx-background-radius: 10px;");

                // TOP ROW

                HBox topRow = new HBox(15);

                topRow.setAlignment(Pos.CENTER_LEFT);

                // PROFILE PHOTO

                StackPane profilePhoto = createProfilePhoto(name, photoPath);
                topRow.getChildren().add(profilePhoto);

                // NAME + DETAILS

                VBox informationBox = new VBox(7);
                HBox.setHgrow(informationBox, Priority.ALWAYS);
                Label nameLabel = new Label(name);
                nameLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 20px;" +
                                                "-fx-font-weight: bold;");

                informationBox.getChildren().add(nameLabel);

                // BASIC INFORMATION

                Label basicInfo = new Label(
                                "Gender: " +
                                                gender +
                                                "    Age: " +
                                                age);

                basicInfo.setStyle(
                                "-fx-text-fill: " +
                                                SECONDARY_TEXT + ";" +
                                                "-fx-font-size: 13px;");

                informationBox.getChildren()
                                .add(basicInfo);

                // BODY INFORMATION

                Label bodyInfo = new Label(
                                "Weight: " +
                                                weight +
                                                " kg    Height: " +
                                                height +
                                                " cm    " +
                                                bmi);

                bodyInfo.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 14px;");

                informationBox.getChildren()
                                .add(bodyInfo);

                // ALLERGY

                Label allergyInfo = new Label(
                                "Allergy: " +
                                                allergy);

                allergyInfo.setStyle(
                                "-fx-text-fill: #EF4444;" +
                                                "-fx-font-size: 13px;");

                informationBox.getChildren()
                                .add(allergyInfo);

                // HEALTH ISSUE

                Label healthInfo = new Label("Health Issue: " + healthIssue);

                healthInfo.setStyle(
                                "-fx-text-fill: #FACC15;" +
                                                "-fx-font-size: 13px;");

                informationBox.getChildren().add(healthInfo);

                // GOAL

                Label goalInfo = new Label("Goal: " + goal);

                goalInfo.setStyle(
                                "-fx-text-fill: " +
                                                GREEN + ";" +
                                                "-fx-font-size: 14px;" +
                                                "-fx-font-weight: bold;");

                informationBox.getChildren()
                                .add(goalInfo);

                // BUTTONS

                HBox buttonBox = new HBox(8);

                buttonBox.setAlignment(Pos.TOP_RIGHT);

                Button editButton = new Button("✎ Edit");

                editButton.setMinWidth(82);
                editButton.setPrefWidth(82);
                editButton.setMaxWidth(82);
                editButton.setMinHeight(34);
                editButton.setPrefHeight(34);
                editButton.setMaxHeight(34);
                editButton.setFocusTraversable(false);

                editButton.setStyle(
                                "-fx-background-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                Button deleteButton = new Button("🗑 Delete");

                deleteButton.setStyle(
                                "-fx-background-color: #7F1D1D;" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 7;");

                buttonBox.getChildren()
                                .addAll(
                                                editButton,
                                                deleteButton);
                // TOP ROW

                topRow.getChildren()
                                .addAll(
                                                informationBox,
                                                buttonBox);

                // DELETE
                deleteButton.setOnAction(e -> {

                        FamilyController controller = new FamilyController();

                        boolean deleted = controller.deleteFamilyMember(memberId);

                        if (deleted) {

                                membersContainer.getChildren()
                                                .remove(card);

                                if (membersContainer.getChildren().isEmpty()) {

                                        emptyMessage.setVisible(true);
                                        emptyMessage.setManaged(true);
                                }
                        }
                });

                // EDIT

                editButton.setOnAction(e -> {
                        showEditMemberDialog(

                                        card,
                                        nameLabel,
                                        basicInfo,
                                        bodyInfo,
                                        allergyInfo,
                                        healthInfo,
                                        goalInfo);
                });

                // ADD TOP ROW

                card.getChildren().add(topRow);
                return card;
        }

        // PROFILE PHOTO

        private StackPane createProfilePhoto(String name, String photoPath) {

                StackPane container = new StackPane();

                container.setPrefSize(90, 90);

                Circle background = new Circle(45);

                background.setFill(javafx.scene.paint.Color.web("#334155"));

                container.getChildren()
                                .add(background);

                // =====================================================
                // IF PHOTO EXISTS
                // =====================================================

                if (photoPath != null &&
                                !photoPath.trim().isEmpty()) {

                        try {

                                File imageFile = new File(photoPath);

                                if (imageFile.exists()) {

                                        Image image = new Image(
                                                        imageFile
                                                                        .toURI()
                                                                        .toString(),
                                                        90,
                                                        90,
                                                        true,
                                                        true);

                                        ImageView imageView = new ImageView(image);

                                        imageView.setFitWidth(90);
                                        imageView.setFitHeight(90);

                                        imageView.setPreserveRatio(
                                                        true);

                                        Circle clip = new Circle(45, 45, 45);

                                        imageView.setClip(clip);

                                        container.getChildren()
                                                        .add(imageView);

                                        return container;
                                }

                        } catch (Exception ex) {

                                System.out.println(
                                                "Unable to load profile photo.");
                        }
                }

                // =====================================================
                // DEFAULT INITIAL
                // =====================================================

                String initial = "?";

                if (name != null &&
                                !name.trim().isEmpty()) {

                        initial = name.trim()
                                        .substring(0, 1)
                                        .toUpperCase();
                }

                Label initialLabel = new Label(initial);

                initialLabel.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 30px;" +
                                                "-fx-font-weight: bold;");

                container.getChildren()
                                .add(initialLabel);

                return container;
        }

        // =========================================================
        // EDIT MEMBER DIALOG
        // =========================================================

        private void showEditMemberDialog(

                        VBox card,

                        Label nameLabel,

                        Label basicInfo,

                        Label bodyInfo,

                        Label allergyInfo,

                        Label healthInfo,

                        Label goalInfo) {

                SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

dialog.setTitle(
        "Edit Family Member");

dialog.setHeaderText(null);

if (mainContent != null && mainContent.getScene() != null) {
    dialog.initOwner(mainContent.getScene().getWindow());
    dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
}

                VBox form = new VBox(12);

                form.setPadding(
                                new Insets(20));

                form.setStyle(
                                "-fx-background-color: " +
                                                BG_COLOR + ";");

                Label title = new Label(
                                "Edit Family Member Details");

                title.setMinHeight(26);
                title.setPrefHeight(26);
                title.setMaxHeight(26);

                title.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 18px;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // NAME
                // =====================================================

                TextField nameField = createTextField("Name");

                nameField.setText(
                                nameLabel.getText());

                // =====================================================
                // GENDER
                // =====================================================

                ComboBox<String> genderBox = createComboBox();

                genderBox.getItems().addAll(
                                "Male",
                                "Female",
                                "Prefer not to say");

                String currentBasic = basicInfo.getText();

                if (currentBasic.contains("Male")) {

                        genderBox.setValue(
                                        "Male");

                } else if (currentBasic.contains("Female")) {

                        genderBox.setValue(
                                        "Female");

                } else {

                        genderBox.setValue(
                                        "Prefer not to say");
                }

                // =====================================================
                // AGE
                // =====================================================

                TextField ageField = createTextField("Age");

                ageField.setText(
                                extractValue(
                                                currentBasic,
                                                "Age:"));

                // =====================================================
                // WEIGHT
                // =====================================================

                TextField weightField = createTextField(
                                "Weight");

                // =====================================================
                // HEIGHT
                // =====================================================

                TextField heightField = createTextField(
                                "Height");

                String currentBody = bodyInfo.getText();

                weightField.setText(
                                extractNumberBefore(
                                                currentBody,
                                                "kg"));

                heightField.setText(
                                extractNumberBefore(
                                                currentBody,
                                                "cm"));

                // =====================================================
                // BMI
                // =====================================================

                Label bmiLabel = new Label(
                                "BMI will be calculated automatically");

                bmiLabel.setStyle(
                                "-fx-text-fill: " +
                                                GREEN + ";" +
                                                "-fx-font-weight: bold;");

                Runnable calculateBMI = () -> {

                        try {

                                double weight = Double.parseDouble(
                                                weightField
                                                                .getText()
                                                                .trim());

                                double height = Double.parseDouble(
                                                heightField
                                                                .getText()
                                                                .trim());

                                double heightMeter = height / 100.0;

                                double bmi = weight /
                                                (heightMeter *
                                                                heightMeter);

                                bmiLabel.setText(
                                                String.format(
                                                                "BMI: %.1f",
                                                                bmi));

                        } catch (Exception ex) {

                                bmiLabel.setText(
                                                "BMI will be calculated automatically");
                        }
                };

                weightField.textProperty()
                                .addListener(
                                                (obs, oldV, newV) -> calculateBMI.run());

                heightField.textProperty()
                                .addListener(
                                                (obs, oldV, newV) -> calculateBMI.run());

                calculateBMI.run();

                // =====================================================
                // ALLERGY
                // =====================================================

                TextField allergyField = createTextField(
                                "Allergy");

                allergyField.setText(
                                allergyInfo.getText()
                                                .replace(
                                                                "Allergy: ",
                                                                ""));

                // =====================================================
                // HEALTH
                // =====================================================

                TextField healthField = createTextField(
                                "Health Issue");

                healthField.setText(
                                healthInfo.getText()
                                                .replace(
                                                                "Health Issue: ",
                                                                ""));

                // =====================================================
                // GOAL
                // =====================================================

                ComboBox<String> goalBox = createComboBox();

                goalBox.getItems().addAll(
                                "Weight Loss",
                                "Weight Gain",
                                "Muscle Gain",
                                "Nothing");

                goalBox.setValue(
                                goalInfo.getText()
                                                .replace(
                                                                "Goal: ",
                                                                ""));

                // =====================================================
                // FORM
                // =====================================================

                form.getChildren().addAll(

                                title,

                                createFieldLabel("Name"),
                                nameField,

                                createFieldLabel("Gender"),
                                genderBox,

                                createFieldLabel("Age"),
                                ageField,

                                createFieldLabel("Weight (kg)"),
                                weightField,

                                createFieldLabel("Height (cm)"),
                                heightField,

                                createFieldLabel("BMI"),
                                bmiLabel,

                                createFieldLabel("Allergy"),
                                allergyField,

                                createFieldLabel("Health Issue"),
                                healthField,

                                createFieldLabel("Goal"),
                                goalBox);

                // =====================================================
                // SCROLL
                // =====================================================

                ScrollPane scroll = new ScrollPane(form);

                scroll.setFitToWidth(true);

                // Always reserve scrollbar space so the edit form does not
                // oscillate between two viewport widths during layout.
                scroll.setVbarPolicy(
                                ScrollPane.ScrollBarPolicy.ALWAYS);
                scroll.setMinWidth(460);
                scroll.setPrefWidth(460);
                scroll.setMaxWidth(460);
                scroll.setMinHeight(540);
                scroll.setPrefHeight(540);
                scroll.setMaxHeight(540);

                scroll.setHbarPolicy(
                                ScrollPane.ScrollBarPolicy.NEVER);

                scroll.setStyle(
                                "-fx-background-color: " +
                                                BG_COLOR + ";" +

                                                "-fx-background: " +
                                                BG_COLOR + ";" +

                                                "-fx-border-color: transparent;");

                dialog.getDialogPane()
                                .setContent(scroll);

                dialog.getDialogPane().setMinWidth(500);
                dialog.getDialogPane().setPrefWidth(500);
                dialog.getDialogPane().setMaxWidth(500);
                dialog.getDialogPane().setMinHeight(650);
                dialog.getDialogPane().setPrefHeight(650);
                dialog.getDialogPane().setMaxHeight(650);

                dialog.getDialogPane()
                                .setStyle(
                                                "-fx-background-color: " +
                                                                BG_COLOR + ";");

                // =====================================================
                // BUTTONS
                // =====================================================

                ButtonType saveType = new ButtonType(
                                "Save",
                                javafx.scene.control.ButtonBar.ButtonData.OK_DONE);

                ButtonType cancelType = new ButtonType(
                                "Cancel",
                                javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);

                dialog.getDialogPane()
                                .getButtonTypes()
                                .addAll(
                                                saveType,
                                                cancelType);

                Button saveButton = (Button) dialog.getDialogPane()
                                .lookupButton(
                                                saveType);

                Button cancelButton = (Button) dialog.getDialogPane()
                                .lookupButton(
                                                cancelType);

                saveButton.setStyle(
                                "-fx-background-color: " +
                                                GREEN + ";" +
                                                "-fx-text-fill: #0F172A;" +
                                                "-fx-font-weight: bold;");

                cancelButton.setStyle(
                                "-fx-background-color: " +
                                                BORDER_COLOR + ";" +
                                                "-fx-text-fill: white;" +
                                                "-fx-font-weight: bold;");

                // =====================================================
                // VALIDATION
                // =====================================================

                Runnable validate = () -> {

                        boolean valid =

                                        !nameField.getText()
                                                        .trim()
                                                        .isEmpty()

                                                        && genderBox.getValue() != null

                                                        && !ageField.getText()
                                                                        .trim()
                                                                        .isEmpty()

                                                        && !weightField.getText()
                                                                        .trim()
                                                                        .isEmpty()

                                                        && !heightField.getText()
                                                                        .trim()
                                                                        .isEmpty()

                                                        && !allergyField.getText()
                                                                        .trim()
                                                                        .isEmpty()

                                                        && !healthField.getText()
                                                                        .trim()
                                                                        .isEmpty()

                                                        && goalBox.getValue() != null;

                        saveButton.setDisable(
                                        !valid);
                };

                nameField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                ageField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                weightField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                heightField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                allergyField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                healthField.textProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                genderBox.valueProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                goalBox.valueProperty().addListener(
                                (obs, oldV, newV) -> validate.run());

                validate.run();

                // =====================================================
                // SAVE
                // =====================================================

                dialog.showAndWait()
                                .ifPresent(result -> {

                                        if (result == saveType) {
                                                System.out.println("Submit Clicked");

                                                nameLabel.setText(
                                                                nameField.getText()
                                                                                .trim());

                                                basicInfo.setText(
                                                                "Gender: " +
                                                                                genderBox.getValue() +
                                                                                "    Age: " +
                                                                                ageField.getText()
                                                                                                .trim());

                                                bodyInfo.setText(
                                                                "Weight: " +
                                                                                weightField.getText()
                                                                                                .trim()
                                                                                +
                                                                                " kg    Height: " +
                                                                                heightField.getText()
                                                                                                .trim()
                                                                                +
                                                                                " cm    " +
                                                                                bmiLabel.getText());

                                                allergyInfo.setText(
                                                                "Allergy: " +
                                                                                allergyField.getText()
                                                                                                .trim());

                                                healthInfo.setText(
                                                                "Health Issue: " +
                                                                                healthField.getText()
                                                                                                .trim());

                                                goalInfo.setText(
                                                                "Goal: " +
                                                                                goalBox.getValue());
                                        }
                                });
        }

        // =========================================================
        // FIELD LABEL
        // =========================================================

        private Label createFieldLabel(
                        String text) {

                Label label = new Label(text);

                label.setStyle(
                                "-fx-text-fill: white;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;");

                return label;
        }

        // =========================================================
        // TEXT FIELD
        // =========================================================

        private TextField createTextField(
                        String prompt) {

                TextField field = new TextField();

                field.setPromptText(
                                prompt);

                field.setPrefHeight(38);

                field.setStyle(
                                "-fx-background-color: " +
                                                INPUT_COLOR + ";" +

                                                "-fx-text-fill: white;" +

                                                "-fx-prompt-text-fill: #64748B;" +

                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +

                                                "-fx-border-width: 1px;" +

                                                "-fx-border-radius: 7px;" +

                                                "-fx-background-radius: 7px;");

                return field;
        }

        // COMBO BOX

        private ComboBox<String> createComboBox() {
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.setPrefHeight(38);

                comboBox.setMaxWidth(Double.MAX_VALUE);

                comboBox.setStyle(
                                "-fx-background-color: " +
                                                INPUT_COLOR + ";" +

                                                "-fx-text-fill: white;" +

                                                "-fx-border-color: " +
                                                BORDER_COLOR + ";" +

                                                "-fx-border-radius: 7px;" +

                                                "-fx-background-radius: 7px;" +

                                                "-fx-mark-color: white;");

                // =====================================================
                // SELECTED VALUE
                // =====================================================

                comboBox.setButtonCell(createDarkComboCell());

                // =====================================================
                // DROPDOWN LIST
                // =====================================================

                comboBox.setCellFactory(listView -> createDarkComboCell());

                return comboBox;
        }

        // =========================================================
        // DARK COMBO BOX CELL
        // =========================================================

        private ListCell<String> createDarkComboCell() {

                ListCell<String> cell = new ListCell<String>() {

                        @Override
                        protected void updateItem(
                                        String item,
                                        boolean empty) {

                                super.updateItem(
                                                item,
                                                empty);

                                if (empty || item == null) {
                                        setText(null);

                                } else {

                                        setText(item);
                                }

                                setStyle(
                                                "-fx-background-color: " +
                                                                CARD_COLOR + ";" +

                                                                "-fx-text-fill: white;" +

                                                                "-fx-font-size: 13px;");
                        }
                };

                // Hover effect
                cell.setOnMouseEntered(e -> cell.setStyle(
                                "-fx-background-color: " +
                                                GREEN_DARK + ";" +

                                                "-fx-text-fill: white;" +

                                                "-fx-font-size: 13px;"));

                cell.setOnMouseExited(e -> cell.setStyle(
                                "-fx-background-color: " +
                                                CARD_COLOR + ";" +

                                                "-fx-text-fill: white;" +

                                                "-fx-font-size: 13px;"));

                return cell;
        }

        // =========================================================
        // EXTRACT VALUE
        // =========================================================

        private String extractValue(
                        String text,
                        String key) {

                try {

                        int index = text.indexOf(key);

                        if (index == -1) {
                                return "";
                        }

                        String value = text.substring(
                                        index + key.length()).trim();

                        return value;

                } catch (Exception e) {

                        return "";
                }
        }

        // =========================================================
        // EXTRACT NUMBER
        // =========================================================

        private String extractNumberBefore(
                        String text,
                        String unit) {

                try {

                        int index = text.indexOf(unit);

                        if (index == -1) {
                                return "";
                        }

                        String before = text.substring(
                                        0,
                                        index).trim();

                        String[] parts = before.split(":");

                        String value = parts[parts.length - 1].trim();

                        return value;

                } catch (Exception e) {

                        return "";
                }
        }

        private void loadFamilyMembers() {

                FamilyController controller = new FamilyController();

                List<FamilyMember> members = controller.getFamilyMembersByUserId(
                                SessionManager.getUid());

                for (FamilyMember member : members) {

                        addMemberCard(
                                        member.getMemberId(), // NEW PARAMETER
                                        member.getName(),
                                        member.getGender(),
                                        String.valueOf(member.getAge()),
                                        String.valueOf(member.getWeight()),
                                        String.valueOf(member.getHeight()),
                                        String.valueOf(member.getBmi()),
                                        member.getAllergy(),
                                        member.getHealthIssue(),
                                        member.getGoal(),
                                        member.getPhotoUrl());
                }
        }
}
