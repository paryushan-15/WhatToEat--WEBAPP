package com.bytebites.model;

public class User {

    // =====================================================
    // BASIC USER INFORMATION
    // =====================================================

    private String uid;

    private String name;

    private String email;

    private String role;

    private String whatsappNumber;


    // =====================================================
    // ACCOUNT CONTROL
    // =====================================================

    private String status;


    // =====================================================
    // ACTIVITY INFORMATION
    // =====================================================

    private long createdAt;

    private long updatedAt;

    private long lastLoginAt;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // REQUIRED BY FIRESTORE
    // =====================================================

    public User() {

    }


    // =====================================================
    // BASIC CONSTRUCTOR
    // =====================================================

    public User(
            String uid,
            String name,
            String email
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = "USER";

        this.status = "ACTIVE";

        this.createdAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // FULL CONSTRUCTOR
    // =====================================================

    public User(
            String uid,
            String name,
            String email,
            String role,
            String whatsappNumber,
            String status,
            long createdAt,
            long updatedAt,
            long lastLoginAt
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = role;

        this.whatsappNumber =
                whatsappNumber;

        this.status =
                status;

        this.createdAt =
                createdAt;

        this.updatedAt =
                updatedAt;

        this.lastLoginAt =
                lastLoginAt;
    }


    // =====================================================
    // UID
    // =====================================================

    public String getUid() {

        return uid;
    }

    public void setUid(
            String uid
    ) {

        this.uid = uid;
    }


    // =====================================================
    // NAME
    // =====================================================

    public String getName() {

        return name;
    }

    public void setName(
            String name
    ) {

        this.name = name;
    }


    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {

        return email;
    }

    public void setEmail(
            String email
    ) {

        this.email = email;
    }


    // =====================================================
    // ROLE
    // =====================================================

    public String getRole() {

        return role;
    }

    public void setRole(
            String role
    ) {

        this.role = role;
    }


    // =====================================================
    // WHATSAPP NUMBER
    // =====================================================

    public String getWhatsappNumber() {

        return whatsappNumber;
    }

    public void setWhatsappNumber(
            String whatsappNumber
    ) {

        this.whatsappNumber =
                whatsappNumber;
    }


    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {

        return status;
    }

    public void setStatus(
            String status
    ) {

        this.status = status;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public long getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(
            long createdAt
    ) {

        this.createdAt = createdAt;
    }


    // =====================================================
    // UPDATED AT
    // =====================================================

    public long getUpdatedAt() {

        return updatedAt;
    }

    public void setUpdatedAt(
            long updatedAt
    ) {

        this.updatedAt = updatedAt;
    }


    // =====================================================
    // LAST LOGIN
    // =====================================================

    public long getLastLoginAt() {

        return lastLoginAt;
    }

    public void setLastLoginAt(
            long lastLoginAt
    ) {

        this.lastLoginAt =
                lastLoginAt;
    }


    // =====================================================
    // ACCOUNT STATE HELPERS
    // =====================================================

    public boolean isActive() {

        /*
         * Backward compatibility:
         * old User documents may not have "status".
         */

        if (
                status == null
                        ||
                status.trim().isEmpty()
        ) {

            return true;
        }

        return status.equalsIgnoreCase(
                "ACTIVE"
        );
    }


    public boolean isSuspended() {

        return status != null
                &&
                status.equalsIgnoreCase(
                        "SUSPENDED"
                );
    }


    // =====================================================
    // ROLE CHECK
    // =====================================================

    public boolean isUser() {

        /*
         * Older User documents may not contain role.
         */

        if (
                role == null
                        ||
                role.trim().isEmpty()
        ) {

            return true;
        }

        return role.equalsIgnoreCase(
                "USER"
        );
    }


    // =====================================================
    // MARK LOGIN
    // =====================================================

    public void markLogin() {

        this.lastLoginAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // MARK UPDATE
    // =====================================================

    public void markUpdated() {

        this.updatedAt =
                System.currentTimeMillis();
    }
}