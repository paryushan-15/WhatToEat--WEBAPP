package com.bytebites.model;

public class Admin {

    // =====================================================
    // BASIC ADMIN INFORMATION
    // =====================================================

    private String uid;

    private String name;

    private String email;

    private String role;

    private String status;

    private String whatsappNumber;


    // =====================================================
    // ADMIN ACTIVITY INFORMATION
    // =====================================================

    private long createdAt;

    private long updatedAt;

    private long lastLoginAt;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // REQUIRED BY FIRESTORE
    // =====================================================

    public Admin() {

    }


    // =====================================================
    // MAIN CONSTRUCTOR
    // =====================================================

    public Admin(
            String uid,
            String name,
            String email
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = "ADMIN";

        this.status = "ACTIVE";

        this.createdAt = System.currentTimeMillis();

        this.updatedAt = System.currentTimeMillis();
    }


    // =====================================================
    // FULL CONSTRUCTOR
    // =====================================================

    public Admin(
            String uid,
            String name,
            String email,
            String role,
            String status,
            String whatsappNumber,
            long createdAt,
            long updatedAt,
            long lastLoginAt
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = role;

        this.status = status;

        this.whatsappNumber = whatsappNumber;

        this.createdAt = createdAt;

        this.updatedAt = updatedAt;

        this.lastLoginAt = lastLoginAt;
    }


    // =====================================================
    // UID
    // =====================================================

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }


    // =====================================================
    // NAME
    // =====================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }


    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }


    // =====================================================
    // ROLE
    // =====================================================

    public String getRole() {

        return role;
    }

    public void setRole(String role) {

        this.role = role;
    }


    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }


    // =====================================================
    // WHATSAPP NUMBER
    // =====================================================

    public String getWhatsappNumber() {

        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {

        this.whatsappNumber = whatsappNumber;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public long getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(long createdAt) {

        this.createdAt = createdAt;
    }


    // =====================================================
    // UPDATED AT
    // =====================================================

    public long getUpdatedAt() {

        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {

        this.updatedAt = updatedAt;
    }


    // =====================================================
    // LAST LOGIN AT
    // =====================================================

    public long getLastLoginAt() {

        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {

        this.lastLoginAt = lastLoginAt;
    }


    // =====================================================
    // CHECK IF ADMIN IS ACTIVE
    // =====================================================

    public boolean isActive() {

        return status != null
                && status.equalsIgnoreCase("ACTIVE");
    }


    // =====================================================
    // CHECK IF ADMIN ROLE IS VALID
    // =====================================================

    public boolean isAdmin() {

        return role != null
                && role.equalsIgnoreCase("ADMIN");
    }


    // =====================================================
    // UPDATE LAST LOGIN
    // =====================================================

    public void markLogin() {

        this.lastLoginAt = System.currentTimeMillis();

        this.updatedAt = System.currentTimeMillis();
    }


    // =====================================================
    // UPDATE MODIFICATION TIME
    // =====================================================

    public void markUpdated() {

        this.updatedAt = System.currentTimeMillis();
    }
}