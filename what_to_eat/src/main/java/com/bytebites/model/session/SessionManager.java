package com.bytebites.model.session;

public class SessionManager {

    private static String uid;

    private static String role;

    private static String name;

    // =====================================================
    // UID
    // =====================================================

    public static void setUid(String uid) {

        SessionManager.uid = uid;
    }

    public static String getUid() {

        return uid;
    }

    // =====================================================
    // ROLE
    // =====================================================

    public static void setRole(String role) {

        SessionManager.role = role;
    }

    public static String getRole() {

        return role;
    }

    // =====================================================
    // NAME
    // =====================================================

    public static void setName(String name) {

        SessionManager.name = name;
    }

    public static String getName() {

        return name;
    }

    // =====================================================
    // CLEAR SESSION
    // =====================================================

    public static void clearSession() {

        uid = null;

        role = null;

        name = null;
    }
}