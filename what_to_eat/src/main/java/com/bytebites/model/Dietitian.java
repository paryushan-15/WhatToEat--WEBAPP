package com.bytebites.model;

public class Dietitian {

    // =====================================================
    // BASIC INFORMATION
    // =====================================================

    private String uid;

    private String name;

    private String email;

    private String role;

    private String status;

    private String whatsappNumber;


    // =====================================================
    // PROFESSIONAL INFORMATION
    // =====================================================

    private String specialization;

    private String experience;


    // =====================================================
    // PLATFORM INFORMATION
    // =====================================================

    private int activeUsers;


    // =====================================================
    // ADMIN / ACCOUNT ACTIVITY
    // =====================================================

    private long createdAt;

    private long updatedAt;

    private long approvedAt;

    private long rejectedAt;

    private long suspendedAt;

    private long lastLoginAt;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // REQUIRED BY FIRESTORE
    // =====================================================

    public Dietitian() {

    }


    // =====================================================
    // EXISTING CONSTRUCTOR
    //
    // Kept compatible with your current code.
    // =====================================================

    public Dietitian(
            String uid,
            String name,
            String email,
            String role,
            String status
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = role;

        this.status = status;

        this.createdAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // RECOMMENDED CONSTRUCTOR FOR NEW DIETITIAN SIGNUP
    // =====================================================

    public Dietitian(
            String uid,
            String name,
            String email,
            String whatsappNumber,
            String specialization,
            String experience
    ) {

        this.uid = uid;

        this.name = name;

        this.email = email;

        this.role = "DIETITIAN";

        this.status = "Pending";

        this.whatsappNumber =
                whatsappNumber;

        this.specialization =
                specialization;

        this.experience =
                experience;

        this.activeUsers = 0;

        this.createdAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
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
    // SPECIALIZATION
    // =====================================================

    public String getSpecialization() {

        return specialization;
    }

    public void setSpecialization(
            String specialization
    ) {

        this.specialization =
                specialization;
    }


    // =====================================================
    // EXPERIENCE
    // =====================================================

    public String getExperience() {

        return experience;
    }

    public void setExperience(
            String experience
    ) {

        this.experience =
                experience;
    }


    // =====================================================
    // ACTIVE USERS
    // =====================================================

    public int getActiveUsers() {

        return activeUsers;
    }

    public void setActiveUsers(
            int activeUsers
    ) {

        this.activeUsers =
                activeUsers;
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
    // APPROVED AT
    // =====================================================

    public long getApprovedAt() {

        return approvedAt;
    }

    public void setApprovedAt(
            long approvedAt
    ) {

        this.approvedAt = approvedAt;
    }


    // =====================================================
    // REJECTED AT
    // =====================================================

    public long getRejectedAt() {

        return rejectedAt;
    }

    public void setRejectedAt(
            long rejectedAt
    ) {

        this.rejectedAt = rejectedAt;
    }


    // =====================================================
    // SUSPENDED AT
    // =====================================================

    public long getSuspendedAt() {

        return suspendedAt;
    }

    public void setSuspendedAt(
            long suspendedAt
    ) {

        this.suspendedAt = suspendedAt;
    }


    // =====================================================
    // LAST LOGIN AT
    // =====================================================

    public long getLastLoginAt() {

        return lastLoginAt;
    }

    public void setLastLoginAt(
            long lastLoginAt
    ) {

        this.lastLoginAt = lastLoginAt;
    }


    // =====================================================
    // STATUS HELPERS
    // =====================================================

    public boolean isPending() {

        return status != null
                &&
                status.equalsIgnoreCase(
                        "Pending"
                );
    }


    public boolean isApproved() {

        return status != null
                &&
                status.equalsIgnoreCase(
                        "Approved"
                );
    }


    public boolean isRejected() {

        return status != null
                &&
                status.equalsIgnoreCase(
                        "Rejected"
                );
    }


    public boolean isSuspended() {

        return status != null
                &&
                status.equalsIgnoreCase(
                        "Suspended"
                );
    }


    // =====================================================
    // ROLE CHECK
    // =====================================================

    public boolean isDietitian() {

        /*
         * Backward compatibility:
         * Older Dietitian documents may not have role.
         */

        if (
                role == null
                        ||
                role.trim().isEmpty()
        ) {

            return true;
        }


        return role.equalsIgnoreCase(
                "DIETITIAN"
        );
    }


    // =====================================================
    // ADMIN ACTION HELPERS
    // =====================================================

    public void markApproved() {

        this.status =
                "Approved";

        this.approvedAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    public void markRejected() {

        this.status =
                "Rejected";

        this.rejectedAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    public void markSuspended() {

        this.status =
                "Suspended";

        this.suspendedAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    public void markReactivated() {

        this.status =
                "Approved";

        this.suspendedAt = 0;

        this.updatedAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // LOGIN ACTIVITY
    // =====================================================

    public void markLogin() {

        this.lastLoginAt =
                System.currentTimeMillis();

        this.updatedAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // UPDATE ACTIVITY
    // =====================================================

    public void markUpdated() {

        this.updatedAt =
                System.currentTimeMillis();
    }
}