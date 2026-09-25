package com.bytebites.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import com.bytebites.model.UserDietitianConsultationModel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.ConferenceSolutionKey;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.EntryPoint;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;


public class GoogleMeetService {


    // =====================================================
    // CONFIGURATION
    // =====================================================

    private static final String APPLICATION_NAME =
            "WTE Consultation";


    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();


    private static final String CREDENTIALS_FILE =
            "/credentials.json";


    private static final String TOKEN_DIRECTORY =
            "google-calendar-tokens";


    private static final String CALENDAR_ID =
            "primary";


    private static final ZoneId ZONE_ID =
            ZoneId.of(
                    "Asia/Kolkata"
            );


    // =====================================================
    // GET GOOGLE CREDENTIALS
    // =====================================================

    private Credential getCredentials(
            NetHttpTransport httpTransport
    ) throws Exception {


        InputStream inputStream =
                GoogleMeetService.class
                        .getResourceAsStream(
                                CREDENTIALS_FILE
                        );


        if (inputStream == null) {

            throw new IllegalStateException(
                    "credentials.json not found in src/main/resources."
            );
        }


        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY,
                        new InputStreamReader(
                                inputStream
                        )
                );


        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(

                        httpTransport,

                        JSON_FACTORY,

                        clientSecrets,

                        Collections.singletonList(
                                CalendarScopes.CALENDAR_EVENTS
                        )

                )
                        .setDataStoreFactory(

                                new FileDataStoreFactory(

                                        Paths.get(
                                                TOKEN_DIRECTORY
                                        ).toFile()

                                )

                        )
                        .setAccessType(
                                "offline"
                        )
                        .build();


        // =================================================
        // AUTO SELECT AVAILABLE LOCAL PORT
        // =================================================

        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setPort(
                                -1
                        )
                        .build();


        return new AuthorizationCodeInstalledApp(
                flow,
                receiver
        )
                .authorize(
                        "dietitian"
                );
    }


    // =====================================================
    // GET GOOGLE CALENDAR SERVICE
    // =====================================================

    private Calendar getCalendarService()
            throws Exception {


        NetHttpTransport httpTransport =
                GoogleNetHttpTransport
                        .newTrustedTransport();


        Credential credential =
                getCredentials(
                        httpTransport
                );


        return new Calendar.Builder(

                httpTransport,

                JSON_FACTORY,

                credential

        )
                .setApplicationName(
                        APPLICATION_NAME
                )
                .build();
    }


    // =====================================================
    // CREATE GOOGLE MEET
    // =====================================================

    public String createGoogleMeet(
            UserDietitianConsultationModel consultation,
            LocalDate consultationDate,
            String consultationTime
    ) throws Exception {


        // =================================================
        // VALIDATION
        // =================================================

        if (consultation == null) {

            throw new IllegalArgumentException(
                    "Consultation data is missing."
            );
        }


        if (consultationDate == null) {

            throw new IllegalArgumentException(
                    "Consultation date is missing."
            );
        }


        if (
                consultationTime == null ||
                consultationTime.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Consultation time is missing."
            );
        }


        // =================================================
        // GOOGLE CALENDAR SERVICE
        // =================================================

        Calendar service =
                getCalendarService();


        // =================================================
        // PARSE TIME
        // =================================================

        LocalTime startTime =
                parseConsultationTime(
                        consultationTime
                );


        LocalDateTime startDateTime =
                LocalDateTime.of(
                        consultationDate,
                        startTime
                );


        LocalDateTime endDateTime =
                startDateTime.plusMinutes(
                        30
                );


        // =================================================
        // CREATE CALENDAR EVENT
        // =================================================

        Event event =
                new Event();


        String userName =
                consultation.getUserName();


        if (
                userName == null ||
                userName.isBlank()
        ) {

            userName =
                    "User";
        }


        event.setSummary(
                "WTE Consultation - "
                        + userName
        );


        // =================================================
        // DESCRIPTION
        // =================================================

        String description =
                "WTE Dietitian Consultation";


        if (
                consultation.getReason() != null &&
                !consultation.getReason().isBlank()
        ) {

            description +=
                    "\nReason: "
                            + consultation.getReason();
        }


        event.setDescription(
                description
        );


        // =================================================
        // START DATE / TIME
        // =================================================

        DateTime googleStart =
                new DateTime(

                        java.util.Date.from(

                                startDateTime
                                        .atZone(
                                                ZONE_ID
                                        )
                                        .toInstant()

                        )

                );


        EventDateTime start =
                new EventDateTime()
                        .setDateTime(
                                googleStart
                        )
                        .setTimeZone(
                                ZONE_ID.getId()
                        );


        // =================================================
        // END DATE / TIME
        // =================================================

        DateTime googleEnd =
                new DateTime(

                        java.util.Date.from(

                                endDateTime
                                        .atZone(
                                                ZONE_ID
                                        )
                                        .toInstant()

                        )

                );


        EventDateTime end =
                new EventDateTime()
                        .setDateTime(
                                googleEnd
                        )
                        .setTimeZone(
                                ZONE_ID.getId()
                        );


        event.setStart(
                start
        );


        event.setEnd(
                end
        );


        // =================================================
        // GOOGLE MEET CONFIGURATION
        // =================================================

        ConferenceSolutionKey solutionKey =
                new ConferenceSolutionKey()
                        .setType(
                                "hangoutsMeet"
                        );


        CreateConferenceRequest createRequest =
                new CreateConferenceRequest()
                        .setRequestId(
                                UUID.randomUUID()
                                        .toString()
                        )
                        .setConferenceSolutionKey(
                                solutionKey
                        );


        ConferenceData conferenceData =
                new ConferenceData()
                        .setCreateRequest(
                                createRequest
                        );


        event.setConferenceData(
                conferenceData
        );


        // =================================================
        // INSERT EVENT
        // =================================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "CREATING GOOGLE CALENDAR EVENT"
        );

        System.out.println(
                "User: "
                        + userName
        );

        System.out.println(
                "Date: "
                        + consultationDate
        );

        System.out.println(
                "Time: "
                        + consultationTime
        );

        System.out.println(
                "Parsed Time: "
                        + startTime
        );

        System.out.println(
                "======================================"
        );


        Event createdEvent =
                service.events()
                        .insert(
                                CALENDAR_ID,
                                event
                        )
                        .setConferenceDataVersion(
                                1
                        )
                        .execute();


        if (createdEvent == null) {

            throw new IllegalStateException(
                    "Google Calendar event could not be created."
            );
        }


        System.out.println(
                "Calendar Event Created: "
                        + createdEvent.getId()
        );


        // =================================================
        // EXTRACT MEET LINK
        // =================================================

        String meetingLink =
                extractMeetingLink(
                        createdEvent
                );


        // =================================================
        // GOOGLE MAY CREATE CONFERENCE ASYNCHRONOUSLY
        // =================================================

        if (
                meetingLink == null ||
                meetingLink.isBlank()
        ) {

            meetingLink =
                    waitForMeetingLink(
                            service,
                            createdEvent.getId()
                    );
        }


        // =================================================
        // FINAL VALIDATION
        // =================================================

        if (
                meetingLink == null ||
                meetingLink.isBlank()
        ) {

            throw new IllegalStateException(
                    "Google Meet link could not be generated."
            );
        }


        System.out.println(
                "======================================"
        );

        System.out.println(
                "GOOGLE MEET CREATED SUCCESSFULLY"
        );

        System.out.println(
                "Meeting Link: "
                        + meetingLink
        );

        System.out.println(
                "======================================"
        );


        return meetingLink;
    }


    // =====================================================
    // PARSE CONSULTATION TIME
    // =====================================================

    private LocalTime parseConsultationTime(
            String consultationTime
    ) {


        if (
                consultationTime == null ||
                consultationTime.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Consultation time is missing."
            );
        }


        String normalizedTime =
                consultationTime
                        .trim()
                        .toUpperCase(
                                Locale.ENGLISH
                        );


        normalizedTime =
                normalizedTime.replaceAll(
                        "\\s+",
                        " "
                );


        // Example:
        // "10:00 AM"
        //
        // parts[0] = "10:00"
        // parts[1] = "AM"

        String[] parts =
                normalizedTime.split(
                        " "
                );


        if (
                parts.length != 2
        ) {

            throw new IllegalArgumentException(
                    "Invalid consultation time: "
                            + consultationTime
            );
        }


        String clockPart =
                parts[0];


        String period =
                parts[1];


        // =================================================
        // SPLIT HOUR / MINUTE
        // =================================================

        String[] clockParts =
                clockPart.split(
                        ":"
                );


        if (
                clockParts.length != 2
        ) {

            throw new IllegalArgumentException(
                    "Invalid consultation time: "
                            + consultationTime
            );
        }


        int hour;


        int minute;


        try {

            hour =
                    Integer.parseInt(
                            clockParts[0]
                    );


            minute =
                    Integer.parseInt(
                            clockParts[1]
                    );


        } catch (NumberFormatException ex) {

            throw new IllegalArgumentException(
                    "Invalid consultation time: "
                            + consultationTime,
                    ex
            );
        }


        // =================================================
        // VALIDATE HOUR
        // =================================================

        if (
                hour < 1 ||
                hour > 12
        ) {

            throw new IllegalArgumentException(
                    "Invalid consultation hour: "
                            + hour
            );
        }


        // =================================================
        // VALIDATE MINUTE
        // =================================================

        if (
                minute < 0 ||
                minute > 59
        ) {

            throw new IllegalArgumentException(
                    "Invalid consultation minute: "
                            + minute
            );
        }


        // =================================================
        // CONVERT 12-HOUR -> 24-HOUR
        // =================================================

        if (
                "AM".equals(
                        period
                )
        ) {

            // 12:00 AM -> 00:00

            if (
                    hour == 12
            ) {

                hour =
                        0;
            }


        } else if (
                "PM".equals(
                        period
                )
        ) {

            // 01:00 PM -> 13:00
            // 12:00 PM remains 12:00

            if (
                    hour != 12
            ) {

                hour +=
                        12;
            }


        } else {

            throw new IllegalArgumentException(
                    "Invalid AM/PM value: "
                            + period
            );
        }


        return LocalTime.of(
                hour,
                minute
        );
    }


    // =====================================================
    // WAIT FOR GOOGLE MEET LINK
    // =====================================================

    private String waitForMeetingLink(
            Calendar service,
            String eventId
    ) throws Exception {


        for (
                int i = 0;
                i < 15;
                i++
        ) {


            Thread.sleep(
                    700
            );


            Event event =
                    service.events()
                            .get(
                                    CALENDAR_ID,
                                    eventId
                            )
                            .execute();


            String link =
                    extractMeetingLink(
                            event
                    );


            if (
                    link != null &&
                    !link.isBlank()
            ) {

                return link;
            }
        }


        return null;
    }


    // =====================================================
    // EXTRACT GOOGLE MEET LINK
    // =====================================================

    private String extractMeetingLink(
            Event event
    ) {


        if (
                event == null
        ) {

            return null;
        }


        // =================================================
        // TRY HANGOUT LINK FIRST
        // =================================================

        if (
                event.getHangoutLink() != null &&
                !event.getHangoutLink().isBlank()
        ) {

            return event.getHangoutLink();
        }


        // =================================================
        // TRY CONFERENCE DATA
        // =================================================

        if (
                event.getConferenceData() == null ||
                event.getConferenceData()
                        .getEntryPoints() == null
        ) {

            return null;
        }


        for (
                EntryPoint entryPoint :
                event.getConferenceData()
                        .getEntryPoints()
        ) {


            if (
                    "video".equalsIgnoreCase(
                            entryPoint.getEntryPointType()
                    )
            ) {

                return entryPoint.getUri();
            }
        }


        return null;
    }
}