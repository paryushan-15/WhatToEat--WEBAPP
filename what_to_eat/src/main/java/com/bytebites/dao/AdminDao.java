package com.bytebites.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.Admin;
import com.bytebites.model.Dietitian;
import com.bytebites.model.UploadedRecipe;
import com.bytebites.model.User;
import com.bytebites.model.UserDietitianConsultationModel;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class AdminDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore db;


    // =====================================================
    // COLLECTION NAMES
    // =====================================================

    private static final String ADMIN_COLLECTION =
            "Admins";

    private static final String USER_COLLECTION =
            "Users";

    private static final String DIETITIAN_COLLECTION =
            "dietitians";

    private static final String CONSULTATION_COLLECTION =
            "consultations";

    private static final String MEAL_PLAN_COLLECTION =
            "mealPlans";

    private static final String FAMILY_MEMBER_COLLECTION =
            "familyMembers";

    private static final String COMMUNITY_COLLECTION =
            "uploadedRecipes";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public AdminDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =====================================================
    // SAVE ADMIN
    // =====================================================

    public boolean saveAdmin(Admin admin) {

        if (admin == null ||
                admin.getUid() == null ||
                admin.getUid().trim().isEmpty()) {

            return false;
        }

        try {

            db.collection(ADMIN_COLLECTION)
                    .document(admin.getUid())
                    .set(admin)
                    .get();

            System.out.println(
                    "Admin saved successfully"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while saving admin"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET ADMIN BY UID
    // =====================================================

    public Admin getAdminByUid(String uid) {

        if (uid == null || uid.trim().isEmpty()) {
            return null;
        }

        try {

            DocumentSnapshot document =
                    db.collection(ADMIN_COLLECTION)
                            .document(uid)
                            .get()
                            .get();

            if (document.exists()) {

                return document.toObject(
                        Admin.class
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =====================================================
    // CHECK IF UID BELONGS TO ADMIN
    // =====================================================

    public boolean isAdmin(String uid) {

        Admin admin =
                getAdminByUid(uid);

        if (admin == null) {
            return false;
        }

        return admin.getRole() != null
                && admin.getRole()
                .trim()
                .equalsIgnoreCase("ADMIN");
    }


    // =====================================================
    // CHECK ADMIN ACTIVE
    // =====================================================

    public boolean isAdminActive(String uid) {

        Admin admin =
                getAdminByUid(uid);

        if (admin == null) {
            return false;
        }

        String status =
                admin.getStatus();

        /*
         * This also keeps old Admin documents working
         * if they were created before "status" existed.
         */
        if (status == null ||
                status.trim().isEmpty()) {

            return true;
        }

        return status.trim()
                .equalsIgnoreCase("ACTIVE");
    }


    // =====================================================
    // VALID ADMIN LOGIN
    // =====================================================

    public boolean canAdminLogin(String uid) {

        return isAdmin(uid)
                && isAdminActive(uid);
    }


    // =====================================================
    // UPDATE ADMIN LAST LOGIN
    // =====================================================

    public boolean updateLastLogin(
            String uid
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            long now =
                    System.currentTimeMillis();

            updates.put(
                    "lastLoginAt",
                    now
            );

            updates.put(
                    "updatedAt",
                    now
            );

            db.collection(ADMIN_COLLECTION)
                    .document(uid)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE ADMIN PROFILE
    // =====================================================

    public boolean updateAdminProfile(
            String uid,
            String name,
            String whatsappNumber
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "name",
                    name
            );

            updates.put(
                    "whatsappNumber",
                    whatsappNumber
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );

            db.collection(ADMIN_COLLECTION)
                    .document(uid)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE ADMIN STATUS
    // =====================================================

    public boolean updateAdminStatus(
            String uid,
            String status
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    status
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );

            db.collection(ADMIN_COLLECTION)
                    .document(uid)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // =====================================================
    //
    // DASHBOARD COUNTS
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // TOTAL USERS
    // =====================================================

    public int getTotalUsers() {

        return getCollectionCount(
                USER_COLLECTION
        );
    }


    // =====================================================
    // TOTAL DIETITIANS
    // =====================================================

    public int getTotalDietitians() {

        return getCollectionCount(
                DIETITIAN_COLLECTION
        );
    }


    // =====================================================
    // TOTAL CONSULTATIONS
    // =====================================================

    public int getTotalConsultations() {

        return getCollectionCount(
                CONSULTATION_COLLECTION
        );
    }


    // =====================================================
    // TOTAL MEAL PLANS
    // =====================================================

    public int getTotalMealPlans() {

        return getCollectionCount(
                MEAL_PLAN_COLLECTION
        );
    }


    // =====================================================
    // TOTAL FAMILY MEMBERS
    // =====================================================

    public int getTotalFamilyMembers() {

        return getCollectionCount(
                FAMILY_MEMBER_COLLECTION
        );
    }


    // =====================================================
    // TOTAL COMMUNITY POSTS
    // =====================================================

    public int getTotalCommunityPosts() {

        return getCollectionCount(
                COMMUNITY_COLLECTION
        );
    }


    // =====================================================
    // GENERIC COLLECTION COUNT
    // =====================================================

    private int getCollectionCount(
            String collectionName
    ) {

        try {

            QuerySnapshot snapshot =
                    db.collection(collectionName)
                            .get()
                            .get();

            return snapshot.size();

        } catch (Exception e) {

            e.printStackTrace();

            return 0;
        }
    }


    // =====================================================
    // PENDING DIETITIANS
    // =====================================================

    public int getPendingDietitianCount() {

        return getDietitianStatusCount(
                "Pending"
        );
    }


    // =====================================================
    // APPROVED DIETITIANS
    // =====================================================

    public int getApprovedDietitianCount() {

        return getDietitianStatusCount(
                "Approved"
        );
    }


    // =====================================================
    // REJECTED DIETITIANS
    // =====================================================

    public int getRejectedDietitianCount() {

        return getDietitianStatusCount(
                "Rejected"
        );
    }


    // =====================================================
    // SUSPENDED DIETITIANS
    // =====================================================

    public int getSuspendedDietitianCount() {

        return getDietitianStatusCount(
                "Suspended"
        );
    }


    // =====================================================
    // COUNT DIETITIAN BY STATUS
    // =====================================================

    private int getDietitianStatusCount(
            String requiredStatus
    ) {

        int count = 0;

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                String status =
                        document.getString(
                                "status"
                        );

                if (status != null &&
                        status.trim()
                                .equalsIgnoreCase(
                                        requiredStatus
                                )) {

                    count++;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return count;
    }


    // =====================================================
    // TOTAL SUBSCRIPTIONS
    // =====================================================

    public int getTotalSubscriptions() {

        int count = 0;

        try {

            QuerySnapshot dietitianSnapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot dietitian :
                    dietitianSnapshot.getDocuments()
            ) {

                QuerySnapshot subscribers =
                        dietitian
                                .getReference()
                                .collection(
                                        "subscribers"
                                )
                                .get()
                                .get();

                count += subscribers.size();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return count;
    }


    // =====================================================
    // ACTIVE SUBSCRIPTIONS
    // =====================================================

    public int getActiveSubscriptions() {

        int count = 0;

        try {

            Timestamp now =
                    Timestamp.now();

            QuerySnapshot dietitianSnapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot dietitian :
                    dietitianSnapshot.getDocuments()
            ) {

                QuerySnapshot subscribers =
                        dietitian
                                .getReference()
                                .collection(
                                        "subscribers"
                                )
                                .get()
                                .get();

                for (
                        QueryDocumentSnapshot subscriber :
                        subscribers.getDocuments()
                ) {

                    String paymentStatus =
                            subscriber.getString(
                                    "paymentStatus"
                            );

                    Timestamp expiry =
                            subscriber.getTimestamp(
                                    "planExpiry"
                            );

                    boolean paid =
                            paymentStatus != null
                                    && paymentStatus
                                    .equalsIgnoreCase(
                                            "paid"
                                    );

                    boolean notExpired =
                            expiry == null
                                    || expiry.compareTo(now) > 0;

                    if (paid && notExpired) {

                        count++;
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return count;
    }


    // =====================================================
    // TOTAL SUBSCRIPTION REVENUE
    // =====================================================

    public double getTotalSubscriptionRevenue() {

        double revenue = 0;

        try {

            QuerySnapshot dietitianSnapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot dietitian :
                    dietitianSnapshot.getDocuments()
            ) {

                QuerySnapshot subscribers =
                        dietitian
                                .getReference()
                                .collection(
                                        "subscribers"
                                )
                                .get()
                                .get();

                for (
                        QueryDocumentSnapshot subscriber :
                        subscribers.getDocuments()
                ) {

                    String paymentStatus =
                            subscriber.getString(
                                    "paymentStatus"
                            );

                    if (paymentStatus == null ||
                            !paymentStatus
                                    .equalsIgnoreCase(
                                            "paid"
                                    )) {

                        continue;
                    }

                    Number amount =
                            (Number) subscriber.get(
                                    "amount"
                            );

                    if (amount != null) {

                        revenue +=
                                amount.doubleValue();
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return revenue;
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
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    future.get()
                            .getDocuments()
            ) {

                try {

                    User user =
                            document.toObject(
                                    User.class
                            );

                    if (user != null) {

                        users.add(user);
                    }

                } catch (Exception ex) {

                    System.out.println(
                            "Unable to read user: "
                                    + document.getId()
                    );

                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return users;
    }


    // =====================================================
    // GET USER BY UID
    // =====================================================

    public User getUserByUid(
            String uid
    ) {

        try {

            DocumentSnapshot document =
                    db.collection(
                                    USER_COLLECTION
                            )
                            .document(uid)
                            .get()
                            .get();

            if (document.exists()) {

                return document.toObject(
                        User.class
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =====================================================
    // UPDATE USER STATUS
    // =====================================================

    public boolean updateUserStatus(
            String uid,
            String status
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    status
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );

            db.collection(USER_COLLECTION)
                    .document(uid)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // SUSPEND USER
    // =====================================================

    public boolean suspendUser(
            String uid
    ) {

        return updateUserStatus(
                uid,
                "SUSPENDED"
        );
    }


    // =====================================================
    // ACTIVATE USER
    // =====================================================

    public boolean activateUser(
            String uid
    ) {

        return updateUserStatus(
                uid,
                "ACTIVE"
        );
    }


    // =====================================================
    // DELETE USER FIRESTORE PROFILE
    // =====================================================

    public boolean deleteUser(
            String uid
    ) {

        try {

            db.collection(USER_COLLECTION)
                    .document(uid)
                    .delete()
                    .get();

            System.out.println(
                    "User deleted successfully: "
                            + uid
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
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

    public List<Dietitian> getAllDietitians() {

        List<Dietitian> dietitians =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                try {

                    Dietitian dietitian =
                            document.toObject(
                                    Dietitian.class
                            );

                    if (dietitian != null) {

                        dietitians.add(
                                dietitian
                        );
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Unable to read dietitian "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return dietitians;
    }


    // =====================================================
    // GET DIETITIAN BY UID
    // =====================================================

    public Dietitian getDietitianByUid(
            String uid
    ) {

        try {

            DocumentSnapshot document =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(uid)
                            .get()
                            .get();

            if (document.exists()) {

                return document.toObject(
                        Dietitian.class
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // =====================================================
    // UPDATE DIETITIAN STATUS
    // =====================================================

    public boolean updateDietitianStatus(
            String uid,
            String status
    ) {

        try {

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(uid)
                    .update(
                            "status",
                            status
                    )
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // APPROVE DIETITIAN
    // =====================================================

    public boolean approveDietitian(
            String uid
    ) {

        return updateDietitianStatus(
                uid,
                "Approved"
        );
    }


    // =====================================================
    // REJECT DIETITIAN
    // =====================================================

    public boolean rejectDietitian(
            String uid
    ) {

        return updateDietitianStatus(
                uid,
                "Rejected"
        );
    }


    // =====================================================
    // SUSPEND DIETITIAN
    // =====================================================

    public boolean suspendDietitian(
            String uid
    ) {

        return updateDietitianStatus(
                uid,
                "Suspended"
        );
    }


    // =====================================================
    // ACTIVATE DIETITIAN
    // =====================================================

    public boolean activateDietitian(
            String uid
    ) {

        return updateDietitianStatus(
                uid,
                "Approved"
        );
    }


    // =====================================================
    // DELETE DIETITIAN PROFILE
    // =====================================================

    public boolean deleteDietitian(
            String uid
    ) {

        try {

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(uid)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
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
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    CONSULTATION_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                try {

                    UserDietitianConsultationModel consultation =
                            document.toObject(
                                    UserDietitianConsultationModel.class
                            );

                    if (consultation != null) {

                        consultations.add(
                                consultation
                        );
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Unable to read consultation: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }


            // Newest consultation first
            consultations.sort(
                    (a, b) ->
                            Long.compare(
                                    b.getCreatedAt(),
                                    a.getCreatedAt()
                            )
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return consultations;
    }


    // =====================================================
    // GET CONSULTATIONS BY STATUS
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByStatus(
            String requiredStatus
    ) {

        List<UserDietitianConsultationModel>
                result =
                new ArrayList<>();

        for (
                UserDietitianConsultationModel consultation :
                getAllConsultations()
        ) {

            if (consultation.getStatus() == null) {
                continue;
            }

            if (consultation
                    .getStatus()
                    .trim()
                    .equalsIgnoreCase(
                            requiredStatus
                    )) {

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

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    status
            );

            /*
             * Current consultation model already contains
             * these timestamp fields.
             */
            if ("COMPLETED".equalsIgnoreCase(status)) {

                updates.put(
                        "completedAt",
                        System.currentTimeMillis()
                );

            } else if (
                    "CANCELLED"
                            .equalsIgnoreCase(status)
            ) {

                updates.put(
                        "cancelledAt",
                        System.currentTimeMillis()
                );
            }

            db.collection(
                            CONSULTATION_COLLECTION
                    )
                    .document(
                            consultationId
                    )
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // ADMIN CANCEL CONSULTATION
    // =====================================================

    public boolean cancelConsultation(
            String consultationId
    ) {

        return updateConsultationStatus(
                consultationId,
                "CANCELLED"
        );
    }


    // =====================================================
    // DELETE CONSULTATION
    // =====================================================

    public boolean deleteConsultation(
            String consultationId
    ) {

        try {

            db.collection(
                            CONSULTATION_COLLECTION
                    )
                    .document(
                            consultationId
                    )
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
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
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    COMMUNITY_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                try {

                    UploadedRecipe recipe =
                            document.toObject(
                                    UploadedRecipe.class
                            );

                    if (recipe != null) {

                        posts.add(recipe);
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Unable to read community post: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return posts;
    }


    // =====================================================
    // HIDE COMMUNITY POST
    // =====================================================

    public boolean hideCommunityPost(
            String postId
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "moderationStatus",
                    "HIDDEN"
            );

            updates.put(
                    "moderatedAt",
                    System.currentTimeMillis()
            );

            db.collection(
                            COMMUNITY_COLLECTION
                    )
                    .document(postId)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // RESTORE COMMUNITY POST
    // =====================================================

    public boolean restoreCommunityPost(
            String postId
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "moderationStatus",
                    "VISIBLE"
            );

            updates.put(
                    "moderatedAt",
                    System.currentTimeMillis()
            );

            db.collection(
                            COMMUNITY_COLLECTION
                    )
                    .document(postId)
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // DELETE COMMUNITY POST
    // =====================================================

    public boolean deleteCommunityPost(
            String postId
    ) {

        try {

            db.collection(
                            COMMUNITY_COLLECTION
                    )
                    .document(postId)
                    .delete()
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
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
    //
    // Using Map because your current project does not yet
    // have a Subscription model.
    // =====================================================

    public List<Map<String, Object>>
    getAllSubscriptions() {

        List<Map<String, Object>>
                subscriptions =
                new ArrayList<>();

        try {

            QuerySnapshot dietitianSnapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot dietitian :
                    dietitianSnapshot.getDocuments()
            ) {

                String dietitianUid =
                        dietitian.getId();

                String dietitianName =
                        dietitian.getString(
                                "name"
                        );


                QuerySnapshot subscriberSnapshot =
                        dietitian
                                .getReference()
                                .collection(
                                        "subscribers"
                                )
                                .get()
                                .get();


                for (
                        QueryDocumentSnapshot subscriber :
                        subscriberSnapshot.getDocuments()
                ) {

                    Map<String, Object> data =
                            new HashMap<>(
                                    subscriber.getData()
                            );

                    data.put(
                            "subscriberDocumentId",
                            subscriber.getId()
                    );

                    data.put(
                            "dietitianUid",
                            dietitianUid
                    );

                    data.put(
                            "dietitianName",
                            dietitianName
                    );

                    subscriptions.add(
                            data
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return subscriptions;
    }


    // =====================================================
    // GET SUBSCRIPTIONS FOR DIETITIAN
    // =====================================================

    public List<Map<String, Object>>
    getSubscriptionsForDietitian(
            String dietitianUid
    ) {

        List<Map<String, Object>>
                subscriptions =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid
                            )
                            .collection(
                                    "subscribers"
                            )
                            .get()
                            .get();

            for (
                    QueryDocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                Map<String, Object> data =
                        new HashMap<>(
                                document.getData()
                        );

                data.put(
                        "subscriberDocumentId",
                        document.getId()
                );

                data.put(
                        "dietitianUid",
                        dietitianUid
                );

                subscriptions.add(
                        data
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return subscriptions;
    }


    // =====================================================
    // CANCEL / DISABLE SUBSCRIPTION
    // =====================================================

    public boolean cancelSubscription(
            String dietitianUid,
            String userUid
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "subscriptionStatus",
                    "CANCELLED"
            );

            updates.put(
                    "cancelledAt",
                    System.currentTimeMillis()
            );

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid
                    )
                    .collection(
                            "subscribers"
                    )
                    .document(
                            userUid
                    )
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // ACTIVATE SUBSCRIPTION
    // =====================================================

    public boolean activateSubscription(
            String dietitianUid,
            String userUid
    ) {

        try {

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "subscriptionStatus",
                    "ACTIVE"
            );

            updates.put(
                    "reactivatedAt",
                    System.currentTimeMillis()
            );

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid
                    )
                    .collection(
                            "subscribers"
                    )
                    .document(
                            userUid
                    )
                    .update(updates)
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // =====================================================
    //
    // DASHBOARD DATA
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET ALL DASHBOARD STATS
    //
    // This makes AdminDashboard easier.
    // One call gives all important numbers.
    // =====================================================

    public Map<String, Object>
    getDashboardStatistics() {

        Map<String, Object> statistics =
                new HashMap<>();


        statistics.put(
                "totalUsers",
                getTotalUsers()
        );

        statistics.put(
                "totalDietitians",
                getTotalDietitians()
        );

        statistics.put(
                "approvedDietitians",
                getApprovedDietitianCount()
        );

        statistics.put(
                "pendingDietitians",
                getPendingDietitianCount()
        );

        statistics.put(
                "rejectedDietitians",
                getRejectedDietitianCount()
        );

        statistics.put(
                "suspendedDietitians",
                getSuspendedDietitianCount()
        );

        statistics.put(
                "totalConsultations",
                getTotalConsultations()
        );

        statistics.put(
                "totalMealPlans",
                getTotalMealPlans()
        );

        statistics.put(
                "totalFamilyMembers",
                getTotalFamilyMembers()
        );

        statistics.put(
                "communityPosts",
                getTotalCommunityPosts()
        );

        statistics.put(
                "totalSubscriptions",
                getTotalSubscriptions()
        );

        statistics.put(
                "activeSubscriptions",
                getActiveSubscriptions()
        );

        statistics.put(
                "totalRevenue",
                getTotalSubscriptionRevenue()
        );


        return statistics;
    }
}