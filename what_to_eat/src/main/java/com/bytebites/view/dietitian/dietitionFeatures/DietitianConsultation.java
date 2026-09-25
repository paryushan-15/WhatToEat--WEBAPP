package com.bytebites.view.dietitian.dietitionFeatures;

import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bytebites.dao.DietitianConsultationDao;
import com.bytebites.model.UserDietitianConsultationModel;
import com.bytebites.model.session.SessionManager;
import com.bytebites.service.GoogleMeetService;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.Modality;
import com.bytebites.view.common.SameStageWindow;


public class DietitianConsultation {


    // =====================================================
    // THEME
    // =====================================================

    private static final String BG =
            "#0F172A";

    private static final String CARD =
            "#1E293B";

    private static final String CARD_LIGHT =
            "#243247";

    private static final String TEXT =
            "#F8FAFC";

    private static final String SECONDARY =
            "#94A3B8";

    private static final String BORDER =
            "#334155";

    private static final String BLUE =
            "#1976D2";

    private static final String GREEN =
            "#22C55E";

    private static final String RED =
            "#EF4444";

    private static final String AMBER =
            "#F59E0B";


    // =====================================================
    // DAO
    // =====================================================

    private final DietitianConsultationDao consultationDao =
            new DietitianConsultationDao();


    // =====================================================
    // UI
    // =====================================================

    private VBox requestsContainer;

    private Button pendingButton;
    private Button upcomingButton;
    private Button completedButton;
    private Button cancelledButton;

    private String currentTab =
            "PENDING";


    // =====================================================
    // MAIN PAGE
    // =====================================================

    public ScrollPane getConsultationPage() {


        VBox main =
                new VBox(20);

        main.setPadding(
                new Insets(
                        30
                )
        );

        main.setStyle(
                "-fx-background-color: "
                        + BG + ";"
        );


        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Consultation Requests"
                );

        title.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 32px;" +
                "-fx-font-weight: bold;"
        );


        Label subtitle =
                new Label(
                        "Manage consultation requests and upcoming appointments."
                );

        subtitle.setStyle(
                "-fx-text-fill: "
                        + SECONDARY + ";" +
                "-fx-font-size: 14px;"
        );


        // =================================================
        // TABS
        // =================================================

        pendingButton =
                createTabButton(
                        "Pending"
                );

        upcomingButton =
                createTabButton(
                        "Upcoming"
                );

        completedButton =
                createTabButton(
                        "Completed"
                );

        cancelledButton =
                createTabButton(
                        "Cancelled"
                );


        HBox tabs =
                new HBox(
                        10,
                        pendingButton,
                        upcomingButton,
                        completedButton,
                        cancelledButton
                );


        // =================================================
        // CONTAINER
        // =================================================

        requestsContainer =
                new VBox(15);


        // =================================================
        // TAB ACTIONS
        // =================================================

        pendingButton.setOnAction(e -> {

            currentTab =
                    "PENDING";

            updateTabStyles();

            loadConsultations();
        });


        upcomingButton.setOnAction(e -> {

            currentTab =
                    "UPCOMING";

            updateTabStyles();

            loadConsultations();
        });


        completedButton.setOnAction(e -> {

            currentTab =
                    "COMPLETED";

            updateTabStyles();

            loadConsultations();
        });


        cancelledButton.setOnAction(e -> {

            currentTab =
                    "CANCELLED";

            updateTabStyles();

            loadConsultations();
        });


        main.getChildren().addAll(
                title,
                subtitle,
                tabs,
                new Separator(),
                requestsContainer
        );


        ScrollPane scrollPane =
                new ScrollPane(
                        main
                );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setStyle(
                "-fx-background: "
                        + BG + ";" +
                "-fx-background-color: "
                        + BG + ";"
        );


        updateTabStyles();

        loadConsultations();


        return scrollPane;
    }


    // =====================================================
    // LOAD CONSULTATIONS
    // =====================================================

    private void loadConsultations() {

        requestsContainer
                .getChildren()
                .clear();


        Label loading =
                new Label(
                        "Loading consultations..."
                );

        loading.setStyle(
                "-fx-text-fill: "
                        + SECONDARY + ";" +
                "-fx-font-size: 15px;"
        );


        requestsContainer
                .getChildren()
                .add(
                        loading
                );


        String dietitianId =
                SessionManager.getUid();


        if (
                dietitianId == null ||
                dietitianId.isBlank()
        ) {

            showEmpty(
                    "Dietitian session not found."
            );

            return;
        }


        // Firestore .get() blocks, so don't run it
        // on the JavaFX application thread.

        Task<List<UserDietitianConsultationModel>> task =
                new Task<>() {

                    @Override
                    protected List<UserDietitianConsultationModel> call() {

                        return consultationDao
                                .getConsultationsByDietitianId(
                                        dietitianId
                                );
                    }
                };


        task.setOnSucceeded(e -> {

            List<UserDietitianConsultationModel> all =
                    task.getValue();


            List<UserDietitianConsultationModel> filtered =
                    filterForCurrentTab(
                            all
                    );


            displayConsultations(
                    filtered
            );
        });


        task.setOnFailed(e -> {

            task.getException()
                    .printStackTrace();

            showEmpty(
                    "Unable to load consultation requests."
            );
        });


        Thread thread =
                new Thread(
                        task
                );

        thread.setDaemon(
                true
        );

        thread.start();
    }


    // =====================================================
    // FILTER
    // =====================================================

    private List<UserDietitianConsultationModel>
    filterForCurrentTab(
            List<UserDietitianConsultationModel> all
    ) {

        List<UserDietitianConsultationModel> result =
                new ArrayList<>();


        if (all == null) {
            return result;
        }


        for (
                UserDietitianConsultationModel consultation :
                all
        ) {

            String status =
                    consultation.getStatus();


            if (status == null) {
                continue;
            }


            switch (currentTab) {

                case "PENDING":

                    if (
                            status.equals(
                                    "PENDING"
                            )
                    ) {

                        result.add(
                                consultation
                        );
                    }

                    break;


                case "UPCOMING":

                    if (
                            status.equals(
                                    "DIETITIAN_ACCEPTED"
                            )
                                    ||
                            status.equals(
                                    "CONFIRMED"
                            )
                    ) {

                        result.add(
                                consultation
                        );
                    }

                    break;


                case "COMPLETED":

                    if (
                            status.equals(
                                    "COMPLETED"
                            )
                    ) {

                        result.add(
                                consultation
                        );
                    }

                    break;


                case "CANCELLED":

                    if (
                            status.equals(
                                    "CANCELLED"
                            )
                    ) {

                        result.add(
                                consultation
                        );
                    }

                    break;
            }
        }


        return result;
    }


    // =====================================================
    // DISPLAY
    // =====================================================

    private void displayConsultations(
            List<UserDietitianConsultationModel> consultations
    ) {

        requestsContainer
                .getChildren()
                .clear();


        if (
                consultations == null ||
                consultations.isEmpty()
        ) {

            showEmpty(
                    getEmptyMessage()
            );

            return;
        }


        for (
                UserDietitianConsultationModel consultation :
                consultations
        ) {

            requestsContainer
                    .getChildren()
                    .add(
                            createConsultationCard(
                                    consultation
                            )
                    );
        }
    }


    // =====================================================
    // CARD
    // =====================================================

    private VBox createConsultationCard(
            UserDietitianConsultationModel consultation
    ) {

        VBox card =
                new VBox(
                        12
                );


        card.setPadding(
                new Insets(
                        20
                )
        );


        card.setStyle(
                "-fx-background-color: "
                        + CARD + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: "
                        + BORDER + ";" +
                "-fx-border-radius: 12;"
        );


        // =================================================
        // TOP
        // =================================================

        Label userName =
                new Label(
                        safe(
                                consultation.getUserName(),
                                "User"
                        )
                );


        userName.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );


        Label status =
                createStatusLabel(
                        consultation.getStatus()
                );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        HBox top =
                new HBox(
                        10,
                        userName,
                        spacer,
                        status
                );


        top.setAlignment(
                Pos.CENTER_LEFT
        );


        // =================================================
        // REQUESTED DATE
        // =================================================

        Label date =
                createInfoLabel(
                        "Requested Date",
                        safe(
                                consultation.getRequestedDate(),
                                "Not available"
                        )
                );


        Label time =
                createInfoLabel(
                        "Requested Time",
                        safe(
                                consultation.getRequestedTime(),
                                "Not available"
                        )
                );


        HBox requestedInfo =
                new HBox(
                        35,
                        date,
                        time
                );


        // =================================================
        // REASON
        // =================================================

        String reasonText =
                consultation.getReason();


        if (
                reasonText == null ||
                reasonText.isBlank()
        ) {

            reasonText =
                    "No reason provided.";
        }


        Label reason =
                new Label(
                        "Reason: "
                                + reasonText
                );


        reason.setWrapText(
                true
        );


        reason.setStyle(
                "-fx-text-fill: #CBD5E1;" +
                "-fx-font-size: 14px;"
        );


        card.getChildren()
                .addAll(
                        top,
                        requestedInfo,
                        reason
                );


        // =================================================
        // STATUS-SPECIFIC UI
        // =================================================

        String consultationStatus =
                consultation.getStatus();


        if (
                "PENDING".equals(
                        consultationStatus
                )
        ) {

            createPendingActions(
                    card,
                    consultation
            );
        }


        else if (
                "DIETITIAN_ACCEPTED".equals(
                        consultationStatus
                )
        ) {

            createAwaitingUserConfirmationSection(
                    card,
                    consultation
            );
        }


        else if (
                "CONFIRMED".equals(
                        consultationStatus
                )
        ) {

            createConfirmedSection(
                    card,
                    consultation
            );
        }


        return card;
    }


    // =====================================================
    // PENDING ACTIONS
    // =====================================================

    private void createPendingActions(
            VBox card,
            UserDietitianConsultationModel consultation
    ) {

        Button accept =
                new Button(
                        "Accept"
                );


        accept.setStyle(
                actionStyle(
                        GREEN,
                        "#052E16"
                )
        );


        Button reject =
                new Button(
                        "Reject"
                );


        reject.setStyle(
                actionStyle(
                        RED,
                        "#FFFFFF"
                )
        );


        accept.setOnAction(
                e -> showAcceptDialog(
                        consultation
                )
        );


        reject.setOnAction(
                e -> rejectConsultation(
                        consultation
                )
        );


        HBox actions =
                new HBox(
                        10,
                        accept,
                        reject
                );


        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        card.getChildren()
                .add(
                        actions
                );
    }


    // =====================================================
    // ACCEPT DIALOG
    // =====================================================

    private void showAcceptDialog(
            UserDietitianConsultationModel consultation
    ) {

        SameStageWindow dialog =
                new SameStageWindow();


        dialog.initModality(
                Modality.APPLICATION_MODAL
        );


        dialog.setTitle(
                "Confirm Consultation"
        );


        VBox box =
                new VBox(
                        15
                );


        box.setPadding(
                new Insets(
                        25
                )
        );


        box.setPrefWidth(
                480
        );


        box.setStyle(
                "-fx-background-color: "
                        + BG + ";"
        );


        // =================================================
        // TITLE
        // =================================================

        Label title =
                new Label(
                        "Confirm Consultation"
                );


        title.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
        );


        Label user =
                new Label(
                        safe(
                                consultation.getUserName(),
                                "User"
                        )
                );


        user.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );


        Label requested =
                new Label(
                        "Requested: "
                                + safe(
                                        consultation.getRequestedDate(),
                                        "-"
                                )
                                + " at "
                                + safe(
                                        consultation.getRequestedTime(),
                                        "-"
                                )
                );


        requested.setStyle(
                "-fx-text-fill: "
                        + SECONDARY + ";"
        );


        // =================================================
        // DATE
        // =================================================

        Label dateLabel =
                fieldLabel(
                        "Select Date"
                );


        DatePicker datePicker =
                new DatePicker();


        try {

            datePicker.setValue(
                    LocalDate.parse(
                            consultation.getRequestedDate()
                    )
            );

        } catch (Exception ex) {

            datePicker.setValue(
                    LocalDate.now()
                            .plusDays(1)
            );
        }


        datePicker.setMaxWidth(
                Double.MAX_VALUE
        );


        datePicker.setDayCellFactory(
                picker ->
                        new DateCell() {

                            @Override
                            public void updateItem(
                                    LocalDate date,
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
                                                LocalDate.now()
                                        )
                                );
                            }
                        }
        );


        // =================================================
        // TIME
        // =================================================

        Label timeLabel =
                fieldLabel(
                        "Select Time"
                );


        ComboBox<String> timeBox =
                createTimeComboBox();


        if (
                consultation.getRequestedTime() != null
        ) {

            timeBox.setValue(
                    consultation.getRequestedTime()
            );
        }


        // =================================================
        // MESSAGE
        // =================================================

        Label messageLabel =
                fieldLabel(
                        "Add Message (Optional)"
                );


        TextArea message =
                new TextArea();


        message.setPromptText(
                "Add a note for the user..."
        );


        message.setPrefHeight(
                80
        );


        message.setWrapText(
                true
        );


        // =================================================
        // BUTTONS
        // =================================================

        Button cancel =
                new Button(
                        "Cancel"
                );


        cancel.setStyle(
                actionStyle(
                        CARD_LIGHT,
                        TEXT
                )
        );


        Button confirm =
                new Button(
                        "Confirm"
                );


        confirm.setStyle(
                actionStyle(
                        BLUE,
                        "#FFFFFF"
                )
        );


        cancel.setOnAction(
                                e -> dialog.close()
                        );


        confirm.setOnAction(e -> {

                LocalDate selectedDate =
                        datePicker.getValue();


                String selectedTime =
                        timeBox.getValue();


                // =================================================
                // VALIDATE DATE
                // =================================================

                if (selectedDate == null) {

                        showMessage(
                                "Date Required",
                                "Please select the final consultation date."
                        );

                        return;
                }


                // =================================================
                // VALIDATE TIME
                // =================================================

                if (
                        selectedTime == null ||
                        selectedTime.isBlank()
                ) {

                        showMessage(
                                "Time Required",
                                "Please select the final consultation time."
                        );

                        return;
                }


                // =================================================
                // GET DIETITIAN MESSAGE
                // =================================================

                String dietitianMessage =
                        message
                                .getText()
                                .trim();


                // =================================================
                // DISABLE BUTTON WHILE GOOGLE MEET IS CREATED
                // =================================================

                confirm.setDisable(
                        true
                );


                confirm.setText(
                        "Creating Google Meet..."
                );


                // =================================================
                // CREATE GOOGLE MEET + SAVE TO FIRESTORE
                // =================================================

                Task<String> task =
                        new Task<>() {

                                @Override
                                protected String call()
                                        throws Exception {


                                // =========================================
                                // 1. CREATE GOOGLE MEET
                                // =========================================

                                GoogleMeetService meetService =
                                        new GoogleMeetService();


                                String meetingLink =
                                        meetService.createGoogleMeet(
                                                consultation,
                                                selectedDate,
                                                selectedTime
                                        );


                                if (
                                        meetingLink == null ||
                                        meetingLink.isBlank()
                                ) {

                                        throw new Exception(
                                                "Google Meet link was not generated."
                                        );
                                }


                                // =========================================
                                // 2. SAVE ACCEPTANCE + LINK TO FIRESTORE
                                // =========================================

                                boolean accepted =
                                        consultationDao
                                                .acceptConsultation(

                                                        consultation
                                                                .getConsultationId(),

                                                        selectedDate
                                                                .toString(),

                                                        selectedTime,

                                                        dietitianMessage,

                                                        meetingLink
                                                );


                                if (!accepted) {

                                        throw new Exception(
                                                "Google Meet was created, "
                                                        + "but consultation could not "
                                                        + "be updated in Firestore."
                                        );
                                }


                                return meetingLink;
                                }
                        };


                // =================================================
                // SUCCESS
                // =================================================

                task.setOnSucceeded(event -> {

                        String meetingLink =
                                task.getValue();


                        // Update local object

                        consultation.setConfirmedDate(
                                selectedDate.toString()
                        );


                        consultation.setConfirmedTime(
                                selectedTime
                        );


                        consultation.setDietitianMessage(
                                dietitianMessage
                        );


                        consultation.setMeetingLink(
                                meetingLink
                        );


                        consultation.setStatus(
                                "DIETITIAN_ACCEPTED"
                        );


                        // Close dialog

                        dialog.close();


                        // Move to upcoming

                        currentTab =
                                "UPCOMING";


                        updateTabStyles();

                        loadConsultations();


                        Platform.runLater(() -> {

                        showMessage(
                                "Consultation Accepted",

                                "The consultation with "
                                        + consultation.getUserName()
                                        + " has been accepted.\n\n"

                                        + "Date: "
                                        + selectedDate

                                        + "\nTime: "
                                        + selectedTime

                                        + "\n\nGoogle Meet created automatically:\n"

                                        + meetingLink

                                        + "\n\nWaiting for the user to confirm."
                        );
                        });
                });


                // =================================================
                // FAILURE
                // =================================================

                task.setOnFailed(event -> {

                        Throwable error =
                                task.getException();


                        if (error != null) {

                        error.printStackTrace();
                        }


                        confirm.setDisable(
                                false
                        );


                        confirm.setText(
                                "Confirm"
                        );


                        showMessage(
                                "Meeting Creation Failed",

                                "Unable to create the Google Meet link.\n\n"

                                        + (
                                                error == null
                                                        ? "Unknown error."
                                                        : error.getMessage()
                                        )
                        );
                });


                // =================================================
                // START BACKGROUND TASK
                // =================================================

                Thread thread =
                        new Thread(
                                task
                        );


                thread.setDaemon(
                        true
                );


                thread.start();
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
                user,
                requested,

                dateLabel,
                datePicker,

                timeLabel,
                timeBox,

                messageLabel,
                message,

                buttons
        );


        dialog.setScene(
                new Scene(
                        box
                )
        );


        dialog.showAndWait();
    }


    // =====================================================
    // REJECT
    // =====================================================

    private void rejectConsultation(
            UserDietitianConsultationModel consultation
    ) {

        Task<Boolean> task =
                new Task<>() {

                    @Override
                    protected Boolean call() {

                        return consultationDao
                                .rejectConsultation(
                                        consultation
                                                .getConsultationId()
                                );
                    }
                };


        task.setOnSucceeded(e -> {

            if (
                    Boolean.TRUE.equals(
                            task.getValue()
                    )
            ) {

                showMessage(
                        "Request Rejected",
                        "The consultation request has been rejected."
                );


                loadConsultations();

            } else {

                showMessage(
                        "Error",
                        "Unable to reject the consultation request."
                );
            }
        });


        Thread thread =
                new Thread(
                        task
                );

        thread.setDaemon(
                true
        );

        thread.start();
    }


    // =====================================================
    // AWAITING USER CONFIRMATION
    // =====================================================

    private void createAwaitingUserConfirmationSection(
            VBox card,
            UserDietitianConsultationModel consultation
    ) {

        Separator separator =
                new Separator();


        Label finalDate =
                createInfoLabel(
                        "Confirmed Date",
                        safe(
                                consultation.getConfirmedDate(),
                                "-"
                        )
                );


        Label finalTime =
                createInfoLabel(
                        "Confirmed Time",
                        safe(
                                consultation.getConfirmedTime(),
                                "-"
                        )
                );


        HBox slot =
                new HBox(
                        35,
                        finalDate,
                        finalTime
                );


        Label waiting =
                new Label(
                        "Waiting for user to confirm..."
                );


        waiting.setStyle(
                "-fx-text-fill: "
                        + AMBER + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;"
        );


        card.getChildren()
                .addAll(
                        separator,
                        slot,
                        waiting
                );
    }


    // =====================================================
    // CONFIRMED SECTION
    // =====================================================

    private void createConfirmedSection(
            VBox card,
            UserDietitianConsultationModel consultation
    ) {

        Separator separator =
                new Separator();


        Label date =
                createInfoLabel(
                        "Date",
                        safe(
                                consultation.getConfirmedDate(),
                                "-"
                        )
                );


        Label time =
                createInfoLabel(
                        "Time",
                        safe(
                                consultation.getConfirmedTime(),
                                "-"
                        )
                );


        HBox slot =
                new HBox(
                        35,
                        date,
                        time
                );


        Button join =
                new Button(
                        "Join Consultation"
                );


        join.setStyle(
                actionStyle(
                        GREEN,
                        "#052E16"
                )
        );


        join.setOnAction(e -> {

                String meetingLink =
                        consultation.getMeetingLink();


                // =================================================
                // LINK ALREADY EXISTS
                // =================================================

                if (
                        meetingLink != null &&
                        !meetingLink.isBlank()
                ) {

                        openMeetingLink(
                                meetingLink
                        );

                        return;
                }


                // =================================================
                // NO LINK -> GENERATE AUTOMATICALLY
                // =================================================

                generateMissingMeetingLink(
                        consultation,
                        join
                );
        });


        Button complete =
                new Button(
                        "Mark Completed"
                );


        complete.setStyle(
                actionStyle(
                        BLUE,
                        "#FFFFFF"
                )
        );


        complete.setOnAction(e -> {

            Task<Boolean> task =
                    new Task<>() {

                        @Override
                        protected Boolean call() {

                            return consultationDao
                                    .completeConsultation(
                                            consultation
                                                    .getConsultationId()
                                    );
                        }
                    };


            task.setOnSucceeded(event -> {

                if (
                        Boolean.TRUE.equals(
                                task.getValue()
                        )
                ) {

                    currentTab =
                            "COMPLETED";


                    updateTabStyles();

                    loadConsultations();

                } else {

                    showMessage(
                            "Error",
                            "Unable to mark consultation as completed."
                    );
                }
            });


            Thread thread =
                    new Thread(
                            task
                    );

            thread.setDaemon(
                    true
            );

            thread.start();
        });


        HBox actions =
                new HBox(
                        10,
                        join,
                        complete
                );


        actions.setAlignment(
                Pos.CENTER_RIGHT
        );


        card.getChildren()
                .addAll(
                        separator,
                        slot,
                        actions
                );
    }


    // =====================================================
    // MEETING LINK DIALOG
    // =====================================================

         // =====================================================
        // AUTO-GENERATE MISSING GOOGLE MEET LINK
        // =====================================================

        private void generateMissingMeetingLink(
                        UserDietitianConsultationModel consultation,
                        Button joinButton
                ) {

                // =================================================
                // VALIDATE DATE
                // =================================================

                String confirmedDate =
                        consultation.getConfirmedDate();


                if (
                        confirmedDate == null ||
                        confirmedDate.isBlank()
                ) {

                        showMessage(
                                "Meeting Error",
                                "Confirmed consultation date is missing."
                        );

                        return;
                }


                // =================================================
                // VALIDATE TIME
                // =================================================

                String confirmedTime =
                        consultation.getConfirmedTime();


                if (
                        confirmedTime == null ||
                        confirmedTime.isBlank()
                ) {

                        showMessage(
                                "Meeting Error",
                                "Confirmed consultation time is missing."
                        );

                        return;
                }


                // =================================================
                // PARSE DATE
                // =================================================

                LocalDate date;


                try {

                        date =
                                LocalDate.parse(
                                        confirmedDate
                                );

                } catch (Exception ex) {

                        showMessage(
                                "Meeting Error",
                                "Invalid consultation date."
                        );

                        return;
                }


                // =================================================
                // UPDATE BUTTON
                // =================================================

                joinButton.setDisable(
                        true
                );


                joinButton.setText(
                        "Creating Meet..."
                );


                // =================================================
                // CREATE GOOGLE MEET
                // =================================================

                Task<String> task =
                        new Task<>() {

                                @Override
                                protected String call()
                                        throws Exception {


                                GoogleMeetService meetService =
                                        new GoogleMeetService();


                                // =========================================
                                // CREATE GOOGLE MEET
                                // =========================================

                                String meetingLink =
                                        meetService.createGoogleMeet(

                                                consultation,

                                                date,

                                                confirmedTime
                                        );


                                if (
                                        meetingLink == null ||
                                        meetingLink.isBlank()
                                ) {

                                        throw new Exception(
                                                "Google Meet link could not be generated."
                                        );
                                }


                                // =========================================
                                // SAVE LINK TO FIRESTORE
                                // =========================================

                                boolean saved =
                                        consultationDao
                                                .updateMeetingLink(

                                                        consultation
                                                                .getConsultationId(),

                                                        meetingLink
                                                );


                                if (!saved) {

                                        throw new Exception(
                                                "Google Meet was created, but the link could not be saved."
                                        );
                                }


                                return meetingLink;
                                }
                        };


                // =================================================
                // SUCCESS
                // =================================================

                task.setOnSucceeded(event -> {

                        String meetingLink =
                                task.getValue();


                        // Store locally so next click doesn't recreate it

                        consultation.setMeetingLink(
                                meetingLink
                        );


                        joinButton.setDisable(
                                false
                        );


                        joinButton.setText(
                                "Join Consultation"
                        );


                        System.out.println(
                                "===================================="
                        );

                        System.out.println(
                                "GOOGLE MEET CREATED AUTOMATICALLY"
                        );

                        System.out.println(
                                "Consultation ID: "
                                        + consultation.getConsultationId()
                        );

                        System.out.println(
                                "Meeting Link: "
                                        + meetingLink
                        );

                        System.out.println(
                                "===================================="
                        );


                        // Open the automatically-created meeting

                        openMeetingLink(
                                meetingLink
                        );
                });


                // =================================================
                // FAILURE
                // =================================================

                task.setOnFailed(event -> {

                        joinButton.setDisable(
                                false
                        );


                        joinButton.setText(
                                "Join Consultation"
                        );


                        Throwable error =
                                task.getException();


                        if (
                                error != null
                        ) {

                        error.printStackTrace();
                        }


                        showMessage(
                                "Google Meet Error",

                                error == null

                                        ? "Unable to generate the Google Meet link."

                                        : error.getMessage()
                        );
                });


                // =================================================
                // BACKGROUND THREAD
                // =================================================

                Thread thread =
                        new Thread(
                                task
                        );


                thread.setDaemon(
                        true
                );


                thread.start();
        }


    // =====================================================
    // OPEN MEETING
    // =====================================================

    private void openMeetingLink(
            String link
    ) {

        try {

            if (
                    !Desktop.isDesktopSupported()
            ) {

                showMessage(
                        "Browser Error",
                        "Opening browser links is not supported."
                );

                return;
            }


            Desktop.getDesktop()
                    .browse(
                            new URI(
                                    link
                            )
                    );


        } catch (Exception ex) {

            ex.printStackTrace();


            showMessage(
                    "Meeting Error",
                    "Unable to open the consultation link."
            );
        }
    }


    // =====================================================
    // STATUS LABEL
    // =====================================================

    private Label createStatusLabel(
            String status
    ) {

        String text;
        String color;


        if (
                "PENDING".equals(
                        status
                )
        ) {

            text =
                    "PENDING";

            color =
                    AMBER;

        } else if (
                "DIETITIAN_ACCEPTED".equals(
                        status
                )
        ) {

            text =
                    "AWAITING USER CONFIRMATION";

            color =
                    AMBER;

        } else if (
                "CONFIRMED".equals(
                        status
                )
        ) {

            text =
                    "CONFIRMED";

            color =
                    GREEN;

        } else if (
                "COMPLETED".equals(
                        status
                )
        ) {

            text =
                    "COMPLETED";

            color =
                    BLUE;

        } else {

            text =
                    "CANCELLED";

            color =
                    RED;
        }


        Label label =
                new Label(
                        text
                );


        label.setStyle(
                "-fx-background-color: "
                        + color + "22;" +
                "-fx-text-fill: "
                        + color + ";" +
                "-fx-padding: 6 10 6 10;" +
                "-fx-background-radius: 15;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    // =====================================================
    // INFO LABEL
    // =====================================================

    private Label createInfoLabel(
            String heading,
            String value
    ) {

        Label label =
                new Label(
                        heading
                                + "\n"
                                + value
                );


        label.setStyle(
                "-fx-text-fill: "
                        + SECONDARY + ";" +
                "-fx-font-size: 13px;"
        );


        return label;
    }


    // =====================================================
    // TAB BUTTON
    // =====================================================

    private Button createTabButton(
            String text
    ) {

        Button button =
                new Button(
                        text
                );


        button.setPrefHeight(
                40
        );


        button.setPrefWidth(
                130
        );


        return button;
    }


    private void updateTabStyles() {

        styleTab(
                pendingButton,
                currentTab.equals(
                        "PENDING"
                )
        );

        styleTab(
                upcomingButton,
                currentTab.equals(
                        "UPCOMING"
                )
        );

        styleTab(
                completedButton,
                currentTab.equals(
                        "COMPLETED"
                )
        );

        styleTab(
                cancelledButton,
                currentTab.equals(
                        "CANCELLED"
                )
        );
    }


    private void styleTab(
            Button button,
            boolean selected
    ) {

        if (selected) {

            button.setStyle(
                    "-fx-background-color: "
                            + BLUE + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 7;"
            );

        } else {

            button.setStyle(
                    "-fx-background-color: "
                            + CARD + ";" +
                    "-fx-text-fill: "
                            + SECONDARY + ";" +
                    "-fx-border-color: "
                            + BORDER + ";" +
                    "-fx-border-radius: 7;" +
                    "-fx-background-radius: 7;"
            );
        }
    }


    // =====================================================
    // TIME COMBO
    // =====================================================

    private ComboBox<String>
    createTimeComboBox() {

        ComboBox<String> timeBox =
                new ComboBox<>();


        timeBox.getItems()
                .addAll(
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
                "Select time"
        );


        timeBox.setMaxWidth(
                Double.MAX_VALUE
        );


        return timeBox;
    }


    // =====================================================
    // FIELD LABEL
    // =====================================================

    private Label fieldLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );


        label.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );


        return label;
    }


    // =====================================================
    // BUTTON STYLE
    // =====================================================

    private String actionStyle(
            String background,
            String text
    ) {

        return "-fx-background-color: "
                + background + ";" +
                "-fx-text-fill: "
                + text + ";" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 9 18 9 18;" +
                "-fx-background-radius: 7;";
    }


    // =====================================================
    // EMPTY STATE
    // =====================================================

    private void showEmpty(
            String text
    ) {

        if (
                requestsContainer == null
        ) {
            return;
        }


        requestsContainer
                .getChildren()
                .clear();


        Label label =
                new Label(
                        text
                );


        label.setMaxWidth(
                Double.MAX_VALUE
        );


        label.setAlignment(
                Pos.CENTER
        );


        label.setPadding(
                new Insets(
                        50
                )
        );


        label.setStyle(
                "-fx-text-fill: "
                        + SECONDARY + ";" +
                "-fx-font-size: 16px;"
        );


        requestsContainer
                .getChildren()
                .add(
                        label
                );
    }


    private String getEmptyMessage() {

        switch (currentTab) {

            case "PENDING":
                return "No pending consultation requests.";

            case "UPCOMING":
                return "No upcoming consultations.";

            case "COMPLETED":
                return "No completed consultations.";

            case "CANCELLED":
                return "No cancelled consultations.";

            default:
                return "No consultations found.";
        }
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private String safe(
            String value,
            String fallback
    ) {

        return value == null ||
                value.isBlank()

                ? fallback

                : value;
    }


    // =====================================================
    // MESSAGE
    // =====================================================

    private void showMessage(
            String title,
            String message
    ) {

        SameStageWindow dialog =
                new SameStageWindow();


        dialog.initModality(
                Modality.APPLICATION_MODAL
        );


        VBox box =
                new VBox(
                        18
                );


        box.setAlignment(
                Pos.CENTER
        );


        box.setPadding(
                new Insets(
                        25
                )
        );


        box.setPrefWidth(
                400
        );


        box.setStyle(
                "-fx-background-color: "
                        + CARD + ";"
        );


        Label titleLabel =
                new Label(
                        title
                );


        titleLabel.setStyle(
                "-fx-text-fill: "
                        + TEXT + ";" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;"
        );


        Label messageLabel =
                new Label(
                        message
                );


        messageLabel.setWrapText(
                true
        );


        messageLabel.setStyle(
                "-fx-text-fill: #CBD5E1;"
        );


        Button ok =
                new Button(
                        "OK"
                );


        ok.setStyle(
                actionStyle(
                        BLUE,
                        "#FFFFFF"
                )
        );


        ok.setOnAction(
                e -> dialog.close()
        );


        box.getChildren()
                .addAll(
                        titleLabel,
                        messageLabel,
                        ok
                );


        dialog.setScene(
                new Scene(
                        box
                )
        );


        dialog.showAndWait();
    }
}