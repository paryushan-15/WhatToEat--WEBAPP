package com.bytebites.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import com.bytebites.dao.AdminDao;
import com.bytebites.dao.DietitianDao;
import com.bytebites.dao.UserDao;

import com.bytebites.model.Admin;
import com.bytebites.model.Dietitian;
import com.bytebites.model.User;

import com.bytebites.model.session.SessionManager;

public class AuthController {

    // =====================================================
    // FIREBASE WEB API KEY
    // =====================================================

    private static final String API_KEY =
            "AIzaSyCseLDxLbrl1iw3j3mw5ggQZXFc620hoPY";


    // =====================================================
    // FIREBASE AUTH URLS
    // =====================================================

    private static final String SIGN_UP_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=";

    private static final String SIGN_IN_URL =
            "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";


    // =====================================================
    // HTTP CLIENT
    // =====================================================

    private final HttpClient httpClient;


    // =====================================================
    // DAOS
    // =====================================================

    private final AdminDao adminDao;

    private final UserDao userDao;

    private final DietitianDao dietitianDao;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AuthController() {

        httpClient =
                HttpClient.newHttpClient();

        adminDao =
                new AdminDao();

        userDao =
                new UserDao();

        dietitianDao =
                new DietitianDao();
    }


    // =====================================================
    // =====================================================
    //
    // SIGN UP
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // FIREBASE SIGN UP
    //
    // Existing method preserved.
    // Returns Firebase UID.
    // =====================================================

    public String signUp(
            String email,
            String password
    ) {

        if (isBlank(email) ||
                isBlank(password)) {

            System.out.println(
                    "Email and password are required."
            );

            return null;
        }


        JSONObject payload =
                new JSONObject()
                        .put(
                                "email",
                                email.trim()
                        )
                        .put(
                                "password",
                                password
                        )
                        .put(
                                "returnSecureToken",
                                true
                        );


        try {

            URI uri =
                    URI.create(
                            SIGN_UP_URL
                                    + API_KEY
                    );


            HttpRequest request =
                    HttpRequest
                            .newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest
                                            .BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();


            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );


            if (
                    response.statusCode()
                            == 200
            ) {

                JSONObject json =
                        new JSONObject(
                                response.body()
                        );


                return json.getString(
                        "localId"
                );
            }


            printFirebaseError(
                    "SIGN UP",
                    response
            );


        } catch (Exception e) {

            System.out.println(
                    "Error while creating Firebase account."
            );

            e.printStackTrace();
        }


        return null;
    }


    // =====================================================
    // =====================================================
    //
    // SIGN IN
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // FIREBASE SIGN IN
    //
    // Existing method preserved.
    // Returns Firebase UID.
    // =====================================================

    public String signIn(
            String email,
            String password
    ) {

        if (isBlank(email) ||
                isBlank(password)) {

            System.out.println(
                    "Email and password are required."
            );

            return null;
        }


        JSONObject payload =
                new JSONObject()
                        .put(
                                "email",
                                email.trim()
                        )
                        .put(
                                "password",
                                password
                        )
                        .put(
                                "returnSecureToken",
                                true
                        );


        try {

            URI uri =
                    URI.create(
                            SIGN_IN_URL
                                    + API_KEY
                    );


            HttpRequest request =
                    HttpRequest
                            .newBuilder()
                            .uri(uri)
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest
                                            .BodyPublishers
                                            .ofString(
                                                    payload.toString()
                                            )
                            )
                            .build();


            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse
                                    .BodyHandlers
                                    .ofString()
                    );


            if (
                    response.statusCode()
                            == 200
            ) {

                JSONObject json =
                        new JSONObject(
                                response.body()
                        );


                return json.getString(
                        "localId"
                );
            }


            printFirebaseError(
                    "SIGN IN",
                    response
            );


        } catch (Exception e) {

            System.out.println(
                    "Error while signing in."
            );

            e.printStackTrace();
        }


        return null;
    }


    // =====================================================
    // =====================================================
    //
    // COMPLETE LOGIN
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // LOGIN AND CREATE SESSION
    //
    // Recommended method for MainLoginPage.
    //
    // Returns:
    //
    // ADMIN
    // DIETITIAN
    // USER
    // PENDING_DIETITIAN
    // REJECTED_DIETITIAN
    // SUSPENDED_DIETITIAN
    // SUSPENDED_ADMIN
    // UNKNOWN_ROLE
    // INVALID_CREDENTIALS
    //
    // =====================================================

    public String login(
            String email,
            String password
    ) {

        // Always clear old session first.
        SessionManager.clearSession();


        // Authenticate against Firebase.
        String uid =
                signIn(
                        email,
                        password
                );


        if (uid == null) {

            return "INVALID_CREDENTIALS";
        }


        // =================================================
        // 1. CHECK ADMIN FIRST
        // =================================================

        Admin admin =
                adminDao
                        .getAdminByUid(
                                uid
                        );


        if (admin != null) {

            return createAdminSession(
                    admin
            );
        }


        // =================================================
        // 2. CHECK DIETITIAN
        // =================================================

        Dietitian dietitian =
                dietitianDao
                        .getDietitianByUid(
                                uid
                        );


        if (dietitian != null) {

            return createDietitianSession(
                    dietitian
            );
        }


        // =================================================
        // 3. CHECK USER
        // =================================================

        User user =
                userDao
                        .getUserByUid(
                                uid
                        );


        if (user != null) {

            return createUserSession(
                    user
            );
        }


        // Firebase Authentication account exists,
        // but no profile document exists in any role
        // collection.
        return "UNKNOWN_ROLE";
    }


    // =====================================================
    // =====================================================
    //
    // ROLE RESOLUTION
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ROLE FROM UID
    // =====================================================

    public String getRoleByUid(
            String uid
    ) {

        if (isBlank(uid)) {
            return null;
        }


        // =================================================
        // ADMIN
        // =================================================

        Admin admin =
                adminDao
                        .getAdminByUid(
                                uid.trim()
                        );


        if (admin != null) {

            return "ADMIN";
        }


        // =================================================
        // DIETITIAN
        // =================================================

        Dietitian dietitian =
                dietitianDao
                        .getDietitianByUid(
                                uid.trim()
                        );


        if (dietitian != null) {

            return "DIETITIAN";
        }


        // =================================================
        // USER
        // =================================================

        User user =
                userDao
                        .getUserByUid(
                                uid.trim()
                        );


        if (user != null) {

            return "USER";
        }


        return null;
    }


    // =====================================================
    // CHECK ADMIN UID
    // =====================================================

    public boolean isAdmin(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }


        return adminDao
                .isAdmin(
                        uid.trim()
                );
    }


    // =====================================================
    // CHECK DIETITIAN UID
    // =====================================================

    public boolean isDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }


        return dietitianDao
                .getDietitianByUid(
                        uid.trim()
                ) != null;
    }


    // =====================================================
    // CHECK USER UID
    // =====================================================

    public boolean isUser(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }


        return userDao
                .getUserByUid(
                        uid.trim()
                ) != null;
    }


    // =====================================================
    // =====================================================
    //
    // SESSION CREATION
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // CREATE ADMIN SESSION
    // =====================================================

    private String createAdminSession(
            Admin admin
    ) {

        if (admin == null) {

            return "UNKNOWN_ROLE";
        }


        String role =
                admin.getRole();


        if (
                role == null
                        ||
                !role.equalsIgnoreCase(
                        "ADMIN"
                )
        ) {

            return "UNKNOWN_ROLE";
        }


        String status =
                admin.getStatus();


        // Old admin documents may not have status.
        if (
                status != null
                        &&
                !status.trim().isEmpty()
                        &&
                !status.equalsIgnoreCase(
                        "ACTIVE"
                )
        ) {

            return "SUSPENDED_ADMIN";
        }


        SessionManager.setUid(
                admin.getUid()
        );

        SessionManager.setRole(
                "ADMIN"
        );

        SessionManager.setName(
                safeName(
                        admin.getName(),
                        "Admin"
                )
        );


        adminDao.updateLastLogin(
                admin.getUid()
        );


        System.out.println(
                "Admin login successful: "
                        + admin.getName()
        );


        return "ADMIN";
    }


    // =====================================================
    // CREATE DIETITIAN SESSION
    // =====================================================

    private String createDietitianSession(
            Dietitian dietitian
    ) {

        if (dietitian == null) {

            return "UNKNOWN_ROLE";
        }


        String status =
                dietitian.getStatus();


        // =================================================
        // PENDING
        // =================================================

        if (
                status != null
                        &&
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            return "PENDING_DIETITIAN";
        }


        // =================================================
        // REJECTED
        // =================================================

        if (
                status != null
                        &&
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            return "REJECTED_DIETITIAN";
        }


        // =================================================
        // SUSPENDED
        // =================================================

        if (
                status != null
                        &&
                status.equalsIgnoreCase(
                        "Suspended"
                )
        ) {

            return "SUSPENDED_DIETITIAN";
        }


        /*
         * Existing approved Dietitians in your project use:
         *
         * Approved
         *
         * We accept null/blank too for backward compatibility
         * with any older documents.
         */
        if (
                status != null
                        &&
                !status.trim().isEmpty()
                        &&
                !status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            return "DIETITIAN_NOT_APPROVED";
        }


        SessionManager.setUid(
                dietitian.getUid()
        );

        SessionManager.setRole(
                "DIETITIAN"
        );

        SessionManager.setName(
                safeName(
                        dietitian.getName(),
                        "Dietitian"
                )
        );

        dietitianDao.updateLastLogin(
                dietitian.getUid()
        );

        System.out.println(
                "Dietitian login successful: "
                        + dietitian.getName()
        );


        return "DIETITIAN";
    }


    // =====================================================
    // CREATE USER SESSION
    // =====================================================

    private String createUserSession(User user) {

        if (user == null) {

            return "UNKNOWN_ROLE";
        }


        // =====================================================
        // CHECK USER ACCOUNT STATUS
        // =====================================================

        String status =
                user.getStatus();


        /*
        * Older Users may not have a status field.
        * Treat them as ACTIVE.
        */
        if (
                status != null
                        &&
                !status.trim().isEmpty()
                        &&
                status.equalsIgnoreCase(
                        "SUSPENDED"
                )
        ) {

            return "SUSPENDED_USER";
        }


        // =====================================================
        // CREATE USER SESSION
        // =====================================================

        SessionManager.setUid(
                user.getUid()
        );

        SessionManager.setRole(
                "USER"
        );

        SessionManager.setName(
                safeName(
                        user.getName(),
                        "User"
                )
        );

        

        // =====================================================
        // RECORD LAST LOGIN
        // =====================================================

        
        userDao.updateLastLogin(
                user.getUid()
        );


        System.out.println(
                "User login successful: "
                        + user.getName()
        );


        return "USER";
    }


    // =====================================================
    // =====================================================
    //
    // LOGOUT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // LOGOUT CURRENT USER
    // =====================================================

    public void logout() {

        SessionManager.clearSession();

        System.out.println(
                "Session cleared successfully."
        );
    }


    // =====================================================
    // CHECK IF SOMEONE IS LOGGED IN
    // =====================================================

    public boolean isLoggedIn() {

        return SessionManager.getUid() != null
                &&
                SessionManager.getRole() != null;
    }


    // =====================================================
    // GET CURRENT LOGGED IN UID
    // =====================================================

    public String getCurrentUid() {

        return SessionManager.getUid();
    }


    // =====================================================
    // GET CURRENT LOGGED IN ROLE
    // =====================================================

    public String getCurrentRole() {

        return SessionManager.getRole();
    }


    // =====================================================
    // GET CURRENT LOGGED IN NAME
    // =====================================================

    public String getCurrentName() {

        return SessionManager.getName();
    }


    // =====================================================
    // =====================================================
    //
    // ERROR HANDLING
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // FIREBASE ERROR PRINTING
    // =====================================================

    private void printFirebaseError(
            String operation,
            HttpResponse<String> response
    ) {

        System.out.println(
                "Firebase "
                        + operation
                        + " failed."
        );

        System.out.println(
                "Status Code: "
                        + response.statusCode()
        );


        try {

            JSONObject responseJson =
                    new JSONObject(
                            response.body()
                    );


            if (responseJson.has("error")) {

                JSONObject error =
                        responseJson
                                .getJSONObject(
                                        "error"
                                );


                String message =
                        error.optString(
                                "message",
                                "Unknown Firebase error"
                        );


                System.out.println(
                        "Firebase Error: "
                                + message
                );


                return;
            }

        } catch (Exception ignored) {

        }


        System.out.println(
                response.body()
        );
    }


    // =====================================================
    // =====================================================
    //
    // UTILITIES
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // CHECK BLANK
    // =====================================================

    private boolean isBlank(
            String value
    ) {

        return value == null
                ||
                value.trim().isEmpty();
    }


    // =====================================================
    // SAFE NAME
    // =====================================================

    private String safeName(
            String name,
            String fallback
    ) {

        if (
                name == null
                        ||
                name.trim().isEmpty()
        ) {

            return fallback;
        }


        return name.trim();
    }
}