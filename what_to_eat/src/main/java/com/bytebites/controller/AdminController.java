package com.bytebites.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.bytebites.dao.AdminDao;
import com.bytebites.model.Admin;
import com.bytebites.model.Dietitian;
import com.bytebites.model.UploadedRecipe;
import com.bytebites.model.User;
import com.bytebites.model.UserDietitianConsultationModel;

public class AdminController {

    // =====================================================
    // DAO
    // =====================================================

    private final AdminDao adminDao;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminController() {

        this.adminDao =
                new AdminDao();
    }


    // =====================================================
    // =====================================================
    //
    // ADMIN AUTHENTICATION
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ADMIN
    // =====================================================

    public Admin getAdmin(
            String uid
    ) {

        if (isBlank(uid)) {
            return null;
        }

        return adminDao.getAdminByUid(
                uid.trim()
        );
    }


    // =====================================================
    // CHECK IF UID IS ADMIN
    // =====================================================

    public boolean isAdmin(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.isAdmin(
                uid.trim()
        );
    }


    // =====================================================
    // CHECK ADMIN ACTIVE
    // =====================================================

    public boolean isAdminActive(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.isAdminActive(
                uid.trim()
        );
    }


    // =====================================================
    // CHECK ADMIN LOGIN ACCESS
    // =====================================================

    public boolean canAdminLogin(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.canAdminLogin(
                uid.trim()
        );
    }


    // =====================================================
    // RECORD ADMIN LOGIN
    // =====================================================

    public boolean recordAdminLogin(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.updateLastLogin(
                uid.trim()
        );
    }


    // =====================================================
    // UPDATE ADMIN PROFILE
    // =====================================================

    public boolean updateAdminProfile(
            String uid,
            String name,
            String whatsappNumber
    ) {

        if (isBlank(uid)) {

            System.out.println(
                    "Admin UID cannot be empty."
            );

            return false;
        }

        if (isBlank(name)) {

            System.out.println(
                    "Admin name cannot be empty."
            );

            return false;
        }

        return adminDao.updateAdminProfile(
                uid.trim(),
                name.trim(),
                safeTrim(whatsappNumber)
        );
    }


    // =====================================================
    // =====================================================
    //
    // DASHBOARD
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL DASHBOARD STATISTICS
    // =====================================================

    public Map<String, Object>
    getDashboardStatistics() {

        return adminDao
                .getDashboardStatistics();
    }


    // =====================================================
    // TOTAL USERS
    // =====================================================

    public int getTotalUsers() {

        return adminDao.getTotalUsers();
    }


    // =====================================================
    // TOTAL DIETITIANS
    // =====================================================

    public int getTotalDietitians() {

        return adminDao
                .getTotalDietitians();
    }


    // =====================================================
    // PENDING DIETITIANS
    // =====================================================

    public int getPendingDietitianCount() {

        return adminDao
                .getPendingDietitianCount();
    }


    // =====================================================
    // APPROVED DIETITIANS
    // =====================================================

    public int getApprovedDietitianCount() {

        return adminDao
                .getApprovedDietitianCount();
    }


    // =====================================================
    // TOTAL CONSULTATIONS
    // =====================================================

    public int getTotalConsultations() {

        return adminDao
                .getTotalConsultations();
    }


    // =====================================================
    // TOTAL SUBSCRIPTIONS
    // =====================================================

    public int getTotalSubscriptions() {

        return adminDao
                .getTotalSubscriptions();
    }


    // =====================================================
    // ACTIVE SUBSCRIPTIONS
    // =====================================================

    public int getActiveSubscriptions() {

        return adminDao
                .getActiveSubscriptions();
    }


    // =====================================================
    // TOTAL REVENUE
    // =====================================================

    public double getTotalRevenue() {

        return adminDao
                .getTotalSubscriptionRevenue();
    }


    // =====================================================
    // TOTAL COMMUNITY POSTS
    // =====================================================

    public int getTotalCommunityPosts() {

        return adminDao
                .getTotalCommunityPosts();
    }


    // =====================================================
    // TOTAL MEAL PLANS
    // =====================================================

    public int getTotalMealPlans() {

        return adminDao
                .getTotalMealPlans();
    }


    // =====================================================
    // TOTAL FAMILY MEMBERS
    // =====================================================

    public int getTotalFamilyMembers() {

        return adminDao
                .getTotalFamilyMembers();
    }


    // =====================================================
    // =====================================================
    //
    // USER MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL USERS
    // =====================================================

    public List<User> getAllUsers() {

        List<User> users =
                adminDao.getAllUsers();

        if (users == null) {

            return new ArrayList<>();
        }

        return users;
    }


    // =====================================================
    // GET USER
    // =====================================================

    public User getUser(
            String uid
    ) {

        if (isBlank(uid)) {
            return null;
        }

        return adminDao.getUserByUid(
                uid.trim()
        );
    }


    // =====================================================
    // SEARCH USERS
    // =====================================================

    public List<User> searchUsers(
            String searchText
    ) {

        List<User> users =
                getAllUsers();

        if (isBlank(searchText)) {

            return users;
        }

        String query =
                searchText
                        .trim()
                        .toLowerCase();

        List<User> filteredUsers =
                new ArrayList<>();


        for (User user : users) {

            if (user == null) {
                continue;
            }

            boolean matchesName =
                    containsIgnoreCase(
                            user.getName(),
                            query
                    );

            boolean matchesEmail =
                    containsIgnoreCase(
                            user.getEmail(),
                            query
                    );

            boolean matchesUid =
                    containsIgnoreCase(
                            user.getUid(),
                            query
                    );


            if (
                    matchesName
                            || matchesEmail
                            || matchesUid
            ) {

                filteredUsers.add(
                        user
                );
            }
        }

        return filteredUsers;
    }


    // =====================================================
    // SUSPEND USER
    // =====================================================

    public boolean suspendUser(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.suspendUser(
                uid.trim()
        );
    }


    // =====================================================
    // ACTIVATE USER
    // =====================================================

    public boolean activateUser(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.activateUser(
                uid.trim()
        );
    }


    // =====================================================
    // DELETE USER PROFILE
    // =====================================================

    public boolean deleteUser(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao.deleteUser(
                uid.trim()
        );
    }


    // =====================================================
    // =====================================================
    //
    // DIETITIAN MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL DIETITIANS
    // =====================================================

    public List<Dietitian>
    getAllDietitians() {

        List<Dietitian> dietitians =
                adminDao
                        .getAllDietitians();

        if (dietitians == null) {

            return new ArrayList<>();
        }

        return dietitians;
    }


    // =====================================================
    // GET DIETITIAN
    // =====================================================

    public Dietitian getDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return null;
        }

        return adminDao
                .getDietitianByUid(
                        uid.trim()
                );
    }


    // =====================================================
    // SEARCH DIETITIANS
    // =====================================================

    public List<Dietitian>
    searchDietitians(
            String searchText
    ) {

        List<Dietitian> dietitians =
                getAllDietitians();

        if (isBlank(searchText)) {

            return dietitians;
        }

        String query =
                searchText
                        .trim()
                        .toLowerCase();

        List<Dietitian> result =
                new ArrayList<>();


        for (
                Dietitian dietitian :
                dietitians
        ) {

            if (dietitian == null) {
                continue;
            }

            boolean matchesName =
                    containsIgnoreCase(
                            dietitian.getName(),
                            query
                    );

            boolean matchesEmail =
                    containsIgnoreCase(
                            dietitian.getEmail(),
                            query
                    );

            boolean matchesUid =
                    containsIgnoreCase(
                            dietitian.getUid(),
                            query
                    );

            boolean matchesStatus =
                    containsIgnoreCase(
                            dietitian.getStatus(),
                            query
                    );


            if (
                    matchesName
                            || matchesEmail
                            || matchesUid
                            || matchesStatus
            ) {

                result.add(
                        dietitian
                );
            }
        }

        return result;
    }


    // =====================================================
    // FILTER DIETITIANS BY STATUS
    // =====================================================

    public List<Dietitian>
    getDietitiansByStatus(
            String status
    ) {

        if (isBlank(status) ||
                status.equalsIgnoreCase("All")) {

            return getAllDietitians();
        }

        List<Dietitian> result =
                new ArrayList<>();


        for (
                Dietitian dietitian :
                getAllDietitians()
        ) {

            if (dietitian == null ||
                    dietitian.getStatus() == null) {

                continue;
            }

            if (
                    dietitian
                            .getStatus()
                            .equalsIgnoreCase(
                                    status.trim()
                            )
            ) {

                result.add(
                        dietitian
                );
            }
        }

        return result;
    }


    // =====================================================
    // APPROVE DIETITIAN
    // =====================================================

    public boolean approveDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao
                .approveDietitian(
                        uid.trim()
                );
    }


    // =====================================================
    // REJECT DIETITIAN
    // =====================================================

    public boolean rejectDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao
                .rejectDietitian(
                        uid.trim()
                );
    }


    // =====================================================
    // SUSPEND DIETITIAN
    // =====================================================

    public boolean suspendDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao
                .suspendDietitian(
                        uid.trim()
                );
    }


    // =====================================================
    // ACTIVATE DIETITIAN
    // =====================================================

    public boolean activateDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao
                .activateDietitian(
                        uid.trim()
                );
    }


    // =====================================================
    // DELETE DIETITIAN PROFILE
    // =====================================================

    public boolean deleteDietitian(
            String uid
    ) {

        if (isBlank(uid)) {
            return false;
        }

        return adminDao
                .deleteDietitian(
                        uid.trim()
                );
    }


    // =====================================================
    // =====================================================
    //
    // CONSULTATION MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL CONSULTATIONS
    // =====================================================

    public List<UserDietitianConsultationModel>
    getAllConsultations() {

        List<UserDietitianConsultationModel>
                consultations =
                adminDao
                        .getAllConsultations();

        if (consultations == null) {

            return new ArrayList<>();
        }

        return consultations;
    }


    // =====================================================
    // FILTER CONSULTATIONS
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByStatus(
            String status
    ) {

        if (isBlank(status) ||
                status.equalsIgnoreCase(
                        "All"
                )) {

            return getAllConsultations();
        }

        return adminDao
                .getConsultationsByStatus(
                        status.trim()
                );
    }


    // =====================================================
    // SEARCH CONSULTATIONS
    // =====================================================

    public List<UserDietitianConsultationModel>
    searchConsultations(
            String searchText
    ) {

        List<UserDietitianConsultationModel>
                consultations =
                getAllConsultations();

        if (isBlank(searchText)) {

            return consultations;
        }

        String query =
                searchText
                        .trim()
                        .toLowerCase();

        List<UserDietitianConsultationModel>
                result =
                new ArrayList<>();


        for (
                UserDietitianConsultationModel consultation :
                consultations
        ) {

            if (consultation == null) {
                continue;
            }

            boolean matchesUser =
                    containsIgnoreCase(
                            consultation.getUserName(),
                            query
                    );

            boolean matchesDietitian =
                    containsIgnoreCase(
                            consultation.getDietitianName(),
                            query
                    );

            boolean matchesStatus =
                    containsIgnoreCase(
                            consultation.getStatus(),
                            query
                    );

            boolean matchesReason =
                    containsIgnoreCase(
                            consultation.getReason(),
                            query
                    );

            boolean matchesId =
                    containsIgnoreCase(
                            consultation
                                    .getConsultationId(),
                            query
                    );


            if (
                    matchesUser
                            || matchesDietitian
                            || matchesStatus
                            || matchesReason
                            || matchesId
            ) {

                result.add(
                        consultation
                );
            }
        }

        return result;
    }


    // =====================================================
    // UPDATE CONSULTATION STATUS
    // =====================================================

    public boolean updateConsultationStatus(
            String consultationId,
            String status
    ) {

        if (
                isBlank(consultationId)
                        || isBlank(status)
        ) {

            return false;
        }

        return adminDao
                .updateConsultationStatus(
                        consultationId.trim(),
                        status.trim()
                );
    }


    // =====================================================
    // CANCEL CONSULTATION
    // =====================================================

    public boolean cancelConsultation(
            String consultationId
    ) {

        if (isBlank(consultationId)) {
            return false;
        }

        return adminDao
                .cancelConsultation(
                        consultationId.trim()
                );
    }


    // =====================================================
    // DELETE CONSULTATION
    // =====================================================

    public boolean deleteConsultation(
            String consultationId
    ) {

        if (isBlank(consultationId)) {
            return false;
        }

        return adminDao
                .deleteConsultation(
                        consultationId.trim()
                );
    }


    // =====================================================
    // =====================================================
    //
    // SUBSCRIPTION MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL SUBSCRIPTIONS
    // =====================================================

    public List<Map<String, Object>>
    getAllSubscriptions() {

        List<Map<String, Object>>
                subscriptions =
                adminDao
                        .getAllSubscriptions();

        if (subscriptions == null) {

            return Collections.emptyList();
        }

        return subscriptions;
    }


    // =====================================================
    // GET DIETITIAN SUBSCRIPTIONS
    // =====================================================

    public List<Map<String, Object>>
    getSubscriptionsForDietitian(
            String dietitianUid
    ) {

        if (isBlank(dietitianUid)) {

            return Collections.emptyList();
        }

        return adminDao
                .getSubscriptionsForDietitian(
                        dietitianUid.trim()
                );
    }


    // =====================================================
    // CANCEL SUBSCRIPTION
    // =====================================================

    public boolean cancelSubscription(
            String dietitianUid,
            String userUid
    ) {

        if (
                isBlank(dietitianUid)
                        || isBlank(userUid)
        ) {

            return false;
        }

        return adminDao
                .cancelSubscription(
                        dietitianUid.trim(),
                        userUid.trim()
                );
    }


    // =====================================================
    // ACTIVATE SUBSCRIPTION
    // =====================================================

    public boolean activateSubscription(
            String dietitianUid,
            String userUid
    ) {

        if (
                isBlank(dietitianUid)
                        || isBlank(userUid)
        ) {

            return false;
        }

        return adminDao
                .activateSubscription(
                        dietitianUid.trim(),
                        userUid.trim()
                );
    }


    // =====================================================
    // =====================================================
    //
    // COMMUNITY MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL COMMUNITY POSTS
    // =====================================================

    public List<UploadedRecipe>
    getAllCommunityPosts() {

        List<UploadedRecipe> posts =
                adminDao
                        .getAllCommunityPosts();

        if (posts == null) {

            return new ArrayList<>();
        }

        return posts;
    }


    // =====================================================
    // HIDE COMMUNITY POST
    // =====================================================

    public boolean hideCommunityPost(
            String postId
    ) {

        if (isBlank(postId)) {
            return false;
        }

        return adminDao
                .hideCommunityPost(
                        postId.trim()
                );
    }


    // =====================================================
    // RESTORE COMMUNITY POST
    // =====================================================

    public boolean restoreCommunityPost(
            String postId
    ) {

        if (isBlank(postId)) {
            return false;
        }

        return adminDao
                .restoreCommunityPost(
                        postId.trim()
                );
    }


    // =====================================================
    // DELETE COMMUNITY POST
    // =====================================================

    public boolean deleteCommunityPost(
            String postId
    ) {

        if (isBlank(postId)) {
            return false;
        }

        return adminDao
                .deleteCommunityPost(
                        postId.trim()
                );
    }


    // =====================================================
    // =====================================================
    //
    // UTILITY METHODS
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
                || value.trim().isEmpty();
    }


    // =====================================================
    // SAFE TRIM
    // =====================================================

    private String safeTrim(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }


    // =====================================================
    // CASE INSENSITIVE SEARCH
    // =====================================================

    private boolean containsIgnoreCase(
            String value,
            String query
    ) {

        if (value == null ||
                query == null) {

            return false;
        }

        return value
                .toLowerCase()
                .contains(
                        query.toLowerCase()
                );
    }
}