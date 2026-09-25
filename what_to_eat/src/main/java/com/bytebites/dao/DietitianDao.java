package com.bytebites.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.Dietitian;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class DietitianDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore db;


    // =====================================================
    // COLLECTION NAMES
    // =====================================================

    private static final String DIETITIAN_COLLECTION =
            "dietitians";

    private static final String SUBSCRIBERS_COLLECTION =
            "subscribers";

    private static final String REVIEWS_COLLECTION =
            "reviews";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DietitianDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =====================================================
    // =====================================================
    //
    // DIETITIAN BASIC OPERATIONS
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // SAVE DIETITIAN
    // =====================================================

    public boolean saveDietitian(
            Dietitian dietitian
    ) {

        if (
                dietitian == null
                        ||
                isBlank(
                        dietitian.getUid()
                )
        ) {

            return false;
        }


        try {

            // Default role
            if (
                    isBlank(
                            dietitian.getRole()
                    )
            ) {

                dietitian.setRole(
                        "DIETITIAN"
                );
            }


            // Newly registered Dietitians require approval
            if (
                    isBlank(
                            dietitian.getStatus()
                    )
            ) {

                dietitian.setStatus(
                        "Pending"
                );
            }


            if (
                    dietitian.getCreatedAt()
                            <= 0
            ) {

                dietitian.setCreatedAt(
                        System.currentTimeMillis()
                );
            }


            dietitian.setUpdatedAt(
                    System.currentTimeMillis()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitian.getUid()
                    )
                    .set(
                            dietitian
                    )
                    .get();


            System.out.println(
                    "Dietitian Saved Successfully"
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Unable to save Dietitian."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET DIETITIAN BY UID
    // =====================================================

    public Dietitian getDietitianByUid(
            String uid
    ) {

        if (isBlank(uid)) {

            return null;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    uid.trim()
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                return null;
            }


            Dietitian dietitian =
                    document.toObject(
                            Dietitian.class
                    );


            if (dietitian != null) {

                /*
                 * Backward compatibility with existing
                 * Firestore Dietitian documents.
                 */

                if (
                        isBlank(
                                dietitian.getUid()
                        )
                ) {

                    dietitian.setUid(
                            document.getId()
                    );
                }


                if (
                        isBlank(
                                dietitian.getRole()
                        )
                ) {

                    dietitian.setRole(
                            "DIETITIAN"
                    );
                }


                if (
                        isBlank(
                                dietitian.getStatus()
                        )
                ) {

                    dietitian.setStatus(
                            "Pending"
                    );
                }
            }


            return dietitian;

        } catch (Exception e) {

            System.out.println(
                    "Unable to load Dietitian: "
                            + uid
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // GET ALL DIETITIANS
    // =====================================================

    public List<Dietitian>
    getAllDietitians() {

        List<Dietitian> list =
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


                    if (dietitian == null) {

                        continue;
                    }


                    if (
                            isBlank(
                                    dietitian.getUid()
                            )
                    ) {

                        dietitian.setUid(
                                document.getId()
                        );
                    }


                    if (
                            isBlank(
                                    dietitian.getRole()
                            )
                    ) {

                        dietitian.setRole(
                                "DIETITIAN"
                        );
                    }


                    if (
                            isBlank(
                                    dietitian.getStatus()
                            )
                    ) {

                        dietitian.setStatus(
                                "Pending"
                        );
                    }


                    list.add(
                            dietitian
                    );

                } catch (Exception e) {

                    System.out.println(
                            "Unable to read Dietitian: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to load Dietitians."
            );

            e.printStackTrace();
        }


        return list;
    }


    // =====================================================
    // =====================================================
    //
    // DIETITIAN APPROVAL / ADMIN CONTROL
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // GET PENDING DIETITIANS
    // =====================================================

    public List<Dietitian>
    getPendingDietitians() {

        return getDietitiansByStatus(
                "Pending"
        );
    }


    // =====================================================
    // GET APPROVED DIETITIANS
    //
    // Existing method preserved.
    // =====================================================

    public List<Dietitian>
    getApprovedDietitians() {

        return getDietitiansByStatus(
                "Approved"
        );
    }


    // =====================================================
    // GET REJECTED DIETITIANS
    // =====================================================

    public List<Dietitian>
    getRejectedDietitians() {

        return getDietitiansByStatus(
                "Rejected"
        );
    }


    // =====================================================
    // GET SUSPENDED DIETITIANS
    // =====================================================

    public List<Dietitian>
    getSuspendedDietitians() {

        return getDietitiansByStatus(
                "Suspended"
        );
    }


    // =====================================================
    // FILTER BY STATUS
    // =====================================================

    public List<Dietitian>
    getDietitiansByStatus(
            String requiredStatus
    ) {

        List<Dietitian> result =
                new ArrayList<>();


        if (isBlank(requiredStatus)) {

            return result;
        }


        for (
                Dietitian dietitian :
                getAllDietitians()
        ) {

            if (
                    dietitian != null
                            &&
                    dietitian.getStatus()
                            != null
                            &&
                    dietitian
                            .getStatus()
                            .trim()
                            .equalsIgnoreCase(
                                    requiredStatus.trim()
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
    //
    // Existing method preserved.
    // =====================================================

    public boolean approveDietitian(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Approved"
            );

            updates.put(
                    "approvedAt",
                    now
            );

            updates.put(
                    "rejectedAt",
                    0L
            );

            updates.put(
                    "suspendedAt",
                    0L
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Dietitian Approved"
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Unable to approve Dietitian."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // REJECT DIETITIAN
    //
    // Existing method preserved.
    // =====================================================

    public boolean rejectDietitian(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Rejected"
            );

            updates.put(
                    "rejectedAt",
                    now
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Dietitian Rejected"
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "Unable to reject Dietitian."
            );

            e.printStackTrace();

            return false;
        }
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


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Suspended"
            );

            updates.put(
                    "suspendedAt",
                    now
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Dietitian Suspended"
            );


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // REACTIVATE DIETITIAN
    // =====================================================

    public boolean activateDietitian(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Approved"
            );

            updates.put(
                    "suspendedAt",
                    0L
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Dietitian Reactivated"
            );


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE STATUS
    // =====================================================

    public boolean updateDietitianStatus(
            String uid,
            String status
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(status)
        ) {

            return false;
        }


        if (
                status.equalsIgnoreCase(
                        "Approved"
                )
        ) {

            return approveDietitian(
                    uid
            );
        }


        if (
                status.equalsIgnoreCase(
                        "Rejected"
                )
        ) {

            return rejectDietitian(
                    uid
            );
        }


        if (
                status.equalsIgnoreCase(
                        "Suspended"
                )
        ) {

            return suspendDietitian(
                    uid
            );
        }


        if (
                status.equalsIgnoreCase(
                        "Pending"
                )
        ) {

            try {

                Map<String, Object> updates =
                        new HashMap<>();


                updates.put(
                        "status",
                        "Pending"
                );

                updates.put(
                        "updatedAt",
                        System.currentTimeMillis()
                );


                db.collection(
                                DIETITIAN_COLLECTION
                        )
                        .document(
                                uid.trim()
                        )
                        .update(
                                updates
                        )
                        .get();


                return true;

            } catch (Exception e) {

                e.printStackTrace();

                return false;
            }
        }


        return false;
    }


    // =====================================================
    // =====================================================
    //
    // PROFILE MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // UPDATE DIETITIAN PROFILE
    //
    // Existing method preserved.
    // =====================================================

    public boolean updateDietitianProfile(
            String uid,
            String name,
            String specialization,
            String experience
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(name)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    name.trim()
            );

            updates.put(
                    "specialization",
                    safeTrim(
                            specialization
                    )
            );

            updates.put(
                    "experience",
                    safeTrim(
                            experience
                    )
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            System.out.println(
                    "Dietitian Profile Updated Successfully"
            );


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE WHATSAPP NUMBER
    //
    // Existing method preserved.
    // =====================================================

    public boolean updateWhatsappNumber(
            String uid,
            String whatsappNumber
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "whatsappNumber",
                    safeTrim(
                            whatsappNumber
                    )
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // ADMIN UPDATE COMPLETE PROFILE
    // =====================================================

    public boolean adminUpdateDietitianProfile(
            String uid,
            String name,
            String whatsappNumber,
            String specialization,
            String experience
    ) {

        if (
                isBlank(uid)
                        ||
                isBlank(name)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    name.trim()
            );

            updates.put(
                    "whatsappNumber",
                    safeTrim(
                            whatsappNumber
                    )
            );

            updates.put(
                    "specialization",
                    safeTrim(
                            specialization
                    )
            );

            updates.put(
                    "experience",
                    safeTrim(
                            experience
                    )
            );

            updates.put(
                    "updatedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // UPDATE LAST LOGIN
    // =====================================================

    public boolean updateLastLogin(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            long now =
                    System.currentTimeMillis();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "lastLoginAt",
                    now
            );

            updates.put(
                    "updatedAt",
                    now
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .update(
                            updates
                    )
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
    // SEARCH
    //
    // =====================================================
    // =====================================================


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


            if (
                    contains(
                            dietitian.getName(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getEmail(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getUid(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getWhatsappNumber(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getSpecialization(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getExperience(),
                            query
                    )
                            ||
                    contains(
                            dietitian.getStatus(),
                            query
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
    // =====================================================
    //
    // ACTIVE USER / SUBSCRIBER MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // INCREMENT ACTIVE USERS
    //
    // Existing method preserved.
    // =====================================================

    public boolean incrementActiveUsers(
            String dietitianUid
    ) {

        if (isBlank(dietitianUid)) {

            return false;
        }


        try {

            DocumentReference dietitianRef =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            );


            DocumentSnapshot document =
                    dietitianRef
                            .get()
                            .get();


            Long currentUsers =
                    document.getLong(
                            "activeUsers"
                    );


            if (currentUsers == null) {

                currentUsers = 0L;
            }


            dietitianRef
                    .update(
                            "activeUsers",
                            currentUsers + 1
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // DECREMENT ACTIVE USERS
    // =====================================================

    public boolean decrementActiveUsers(
            String dietitianUid
    ) {

        if (isBlank(dietitianUid)) {

            return false;
        }


        try {

            DocumentReference dietitianRef =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            );


            DocumentSnapshot document =
                    dietitianRef
                            .get()
                            .get();


            Long currentUsers =
                    document.getLong(
                            "activeUsers"
                    );


            if (currentUsers == null) {

                currentUsers = 0L;
            }


            long newValue =
                    Math.max(
                            0,
                            currentUsers - 1
                    );


            dietitianRef
                    .update(
                            "activeUsers",
                            newValue
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET ACTIVE USERS
    // =====================================================

    public int getActiveUserCount(
            String dietitianUid
    ) {

        Dietitian dietitian =
                getDietitianByUid(
                        dietitianUid
                );


        if (dietitian == null) {

            return 0;
        }


        return dietitian.getActiveUsers();
    }


    // =====================================================
    // CHECK USER SUBSCRIPTION
    //
    // Existing method preserved.
    // =====================================================

    public boolean isUserSubscribed(
            String dietitianUid,
            String userUid
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(userUid)
        ) {

            return false;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    SUBSCRIBERS_COLLECTION
                            )
                            .document(
                                    userUid.trim()
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                return false;
            }


            String paymentStatus =
                    document.getString(
                            "paymentStatus"
                    );


            String subscriptionStatus =
                    document.getString(
                            "subscriptionStatus"
                    );


            boolean paid =
                    paymentStatus == null
                            ||
                    paymentStatus.equalsIgnoreCase(
                            "paid"
                    );


            boolean active =
                    subscriptionStatus == null
                            ||
                    !subscriptionStatus.equalsIgnoreCase(
                            "CANCELLED"
                    );


            Timestamp expiry =
                    document.getTimestamp(
                            "planExpiry"
                    );


            boolean notExpired =
                    expiry == null
                            ||
                    expiry.compareTo(
                            Timestamp.now()
                    ) > 0;


            return paid
                    && active
                    && notExpired;


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // SAVE SUBSCRIBER
    //
    // Existing method preserved.
    // =====================================================

    public boolean saveSubscriber(
            String dietitianUid,
            String userUid,
            String userName,
            String plan,
            int amount,
            String paymentLinkId
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(userUid)
        ) {

            return false;
        }


        try {

            DocumentReference subscriberRef =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    SUBSCRIBERS_COLLECTION
                            )
                            .document(
                                    userUid.trim()
                            );


            Map<String, Object> subscriberData =
                    new HashMap<>();


            subscriberData.put(
                    "userUid",
                    userUid
            );

            subscriberData.put(
                    "userName",
                    safeTrim(
                            userName
                    )
            );

            subscriberData.put(
                    "plan",
                    safeTrim(
                            plan
                    )
            );

            subscriberData.put(
                    "amount",
                    amount
            );

            subscriberData.put(
                    "paymentStatus",
                    "paid"
            );

            subscriberData.put(
                    "subscriptionStatus",
                    "ACTIVE"
            );

            subscriberData.put(
                    "paymentLinkId",
                    safeTrim(
                            paymentLinkId
                    )
            );


            // =================================================
            // SUBSCRIPTION START
            // =================================================

            Timestamp subscribedAt =
                    Timestamp.now();


            subscriberData.put(
                    "subscribedAt",
                    subscribedAt
            );


            // =================================================
            // PLAN EXPIRY
            // =================================================

            Calendar calendar =
                    Calendar.getInstance();


            calendar.setTime(
                    subscribedAt.toDate()
            );


            if (
                    "Weekly Plan"
                            .equalsIgnoreCase(
                                    plan
                            )
            ) {

                calendar.add(
                        Calendar.DAY_OF_MONTH,
                        7
                );

            } else if (
                    "Monthly Plan"
                            .equalsIgnoreCase(
                                    plan
                            )
            ) {

                calendar.add(
                        Calendar.DAY_OF_MONTH,
                        30
                );

            } else {

                /*
                 * Safe fallback for any future plan
                 * where duration hasn't been defined.
                 */
                calendar.add(
                        Calendar.DAY_OF_MONTH,
                        30
                );
            }


            Timestamp planExpiry =
                    Timestamp.of(
                            calendar.getTime()
                    );


            subscriberData.put(
                    "planExpiry",
                    planExpiry
            );


            subscriberRef
                    .set(
                            subscriberData
                    )
                    .get();


            System.out.println(
                    "Subscriber Saved Successfully"
            );

            System.out.println(
                    "Subscription Plan: "
                            + plan
            );

            System.out.println(
                    "Plan Expiry: "
                            + planExpiry
            );


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET SUBSCRIBER USER IDS
    //
    // Existing method preserved.
    // =====================================================

    public List<String>
    getSubscriberUserIds(
            String dietitianUid
    ) {

        List<String> userIds =
                new ArrayList<>();


        if (isBlank(dietitianUid)) {

            return userIds;
        }


        try {

            CollectionReference subscribers =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    SUBSCRIBERS_COLLECTION
                            );


            ApiFuture<QuerySnapshot> future =
                    subscribers.get();


            for (
                    DocumentSnapshot document :
                    future.get()
                            .getDocuments()
            ) {

                userIds.add(
                        document.getId()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return userIds;
    }


    // =====================================================
    // GET SUBSCRIBER DETAILS
    //
    // Existing method preserved.
    // =====================================================

    public Map<String, Object>
    getSubscriberDetails(
            String dietitianUid,
            String userUid
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(userUid)
        ) {

            return null;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    SUBSCRIBERS_COLLECTION
                            )
                            .document(
                                    userUid.trim()
                            )
                            .get()
                            .get();


            if (document.exists()) {

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


                return data;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }


    // =====================================================
    // GET ALL SUBSCRIBERS
    // =====================================================

    public List<Map<String, Object>>
    getSubscribers(
            String dietitianUid
    ) {

        List<Map<String, Object>> list =
                new ArrayList<>();


        if (isBlank(dietitianUid)) {

            return list;
        }


        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    SUBSCRIBERS_COLLECTION
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


                list.add(
                        data
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return list;
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
                        ||
                isBlank(userUid)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "subscriptionStatus",
                    "CANCELLED"
            );

            updates.put(
                    "cancelledAt",
                    Timestamp.now()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid.trim()
                    )
                    .collection(
                            SUBSCRIBERS_COLLECTION
                    )
                    .document(
                            userUid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // REACTIVATE SUBSCRIPTION
    // =====================================================

    public boolean activateSubscription(
            String dietitianUid,
            String userUid
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(userUid)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "subscriptionStatus",
                    "ACTIVE"
            );

            updates.put(
                    "reactivatedAt",
                    Timestamp.now()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid.trim()
                    )
                    .collection(
                            SUBSCRIBERS_COLLECTION
                    )
                    .document(
                            userUid.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // IS SUBSCRIBED TO ANY DIETITIAN
    //
    // Existing method preserved.
    // =====================================================

    public boolean isSubscribedToAnyDietitian(
            String userUid
    ) {

        if (isBlank(userUid)) {

            return false;
        }


        try {

            QuerySnapshot dietitians =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .get()
                            .get();


            for (
                    DocumentSnapshot dietitian :
                    dietitians.getDocuments()
            ) {

                if (
                        isUserSubscribed(
                                dietitian.getId(),
                                userUid
                        )
                ) {

                    return true;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return false;
    }


    // =====================================================
    // =====================================================
    //
    // REVIEW MANAGEMENT
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // SAVE REVIEW
    //
    // Existing method preserved.
    // =====================================================

    public boolean saveReview(
            String dietitianUid,
            String userUid,
            String userName,
            int rating,
            String review
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(userUid)
        ) {

            return false;
        }


        if (
                rating < 1
                        ||
                rating > 5
        ) {

            return false;
        }


        try {

            DocumentReference reviewRef =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    REVIEWS_COLLECTION
                            )
                            .document();


            Map<String, Object> reviewData =
                    new HashMap<>();


            reviewData.put(
                    "reviewId",
                    reviewRef.getId()
            );

            reviewData.put(
                    "userUid",
                    userUid
            );

            reviewData.put(
                    "userName",
                    safeTrim(
                            userName
                    )
            );

            reviewData.put(
                    "rating",
                    rating
            );

            reviewData.put(
                    "review",
                    safeTrim(
                            review
                    )
            );

            reviewData.put(
                    "createdAt",
                    Timestamp.now()
            );

            /*
             * Used later by AdminReviews.
             */
            reviewData.put(
                    "moderationStatus",
                    "VISIBLE"
            );


            reviewRef
                    .set(
                            reviewData
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // GET REVIEWS
    //
    // Existing method preserved.
    // =====================================================

    public List<Map<String, Object>>
    getReviews(
            String dietitianUid
    ) {

        List<Map<String, Object>> reviews =
                new ArrayList<>();


        if (isBlank(dietitianUid)) {

            return reviews;
        }


        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianUid.trim()
                            )
                            .collection(
                                    REVIEWS_COLLECTION
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
                        "reviewId",
                        document.getId()
                );

                data.put(
                        "dietitianUid",
                        dietitianUid
                );


                reviews.add(
                        data
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return reviews;
    }


    // =====================================================
    // GET REVIEW FOR USER
    //
    // Existing method preserved.
    // =====================================================

    public Map<String, Object>
    getReviewForUser(
            String dietitianId,
            String userUid
    ) {

        if (
                isBlank(dietitianId)
                        ||
                isBlank(userUid)
        ) {

            return null;
        }


        try {

            QuerySnapshot snapshot =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    dietitianId.trim()
                            )
                            .collection(
                                    REVIEWS_COLLECTION
                            )
                            .whereEqualTo(
                                    "userUid",
                                    userUid.trim()
                            )
                            .get()
                            .get();


            if (
                    !snapshot.isEmpty()
            ) {

                DocumentSnapshot document =
                        snapshot
                                .getDocuments()
                                .get(0);


                Map<String, Object> data =
                        new HashMap<>(
                                document.getData()
                        );


                data.put(
                        "reviewId",
                        document.getId()
                );


                return data;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return null;
    }


    // =====================================================
    // GET AVERAGE RATING
    // =====================================================

    public double getAverageRating(
            String dietitianUid
    ) {

        List<Map<String, Object>> reviews =
                getReviews(
                        dietitianUid
                );


        if (reviews.isEmpty()) {

            return 0.0;
        }


        double total = 0;

        int ratingCount = 0;


        for (
                Map<String, Object> review :
                reviews
        ) {

            String moderationStatus =
                    stringValue(
                            review.get(
                                    "moderationStatus"
                            )
                    );


            /*
             * Hidden reviews don't contribute to
             * the public average.
             */
            if (
                    "HIDDEN"
                            .equalsIgnoreCase(
                                    moderationStatus
                            )
            ) {

                continue;
            }


            Object ratingValue =
                    review.get(
                            "rating"
                    );


            if (
                    ratingValue
                            instanceof Number
            ) {

                total +=
                        ((Number) ratingValue)
                                .doubleValue();

                ratingCount++;
            }
        }


        if (ratingCount == 0) {

            return 0.0;
        }


        return total
                / ratingCount;
    }


    // =====================================================
    // HIDE REVIEW
    // =====================================================

    public boolean hideReview(
            String dietitianUid,
            String reviewId
    ) {

        return updateReviewModerationStatus(
                dietitianUid,
                reviewId,
                "HIDDEN"
        );
    }


    // =====================================================
    // RESTORE REVIEW
    // =====================================================

    public boolean restoreReview(
            String dietitianUid,
            String reviewId
    ) {

        return updateReviewModerationStatus(
                dietitianUid,
                reviewId,
                "VISIBLE"
        );
    }


    // =====================================================
    // UPDATE REVIEW MODERATION STATUS
    // =====================================================

    private boolean updateReviewModerationStatus(
            String dietitianUid,
            String reviewId,
            String status
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(reviewId)
        ) {

            return false;
        }


        try {

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "moderationStatus",
                    status
            );

            updates.put(
                    "moderatedAt",
                    Timestamp.now()
            );


            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid.trim()
                    )
                    .collection(
                            REVIEWS_COLLECTION
                    )
                    .document(
                            reviewId.trim()
                    )
                    .update(
                            updates
                    )
                    .get();


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // DELETE REVIEW
    // =====================================================

    public boolean deleteReview(
            String dietitianUid,
            String reviewId
    ) {

        if (
                isBlank(dietitianUid)
                        ||
                isBlank(reviewId)
        ) {

            return false;
        }


        try {

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            dietitianUid.trim()
                    )
                    .collection(
                            REVIEWS_COLLECTION
                    )
                    .document(
                            reviewId.trim()
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
    // ADMIN STATISTICS
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // TOTAL DIETITIAN COUNT
    // =====================================================

    public int getTotalDietitianCount() {

        return getAllDietitians().size();
    }


    // =====================================================
    // PENDING COUNT
    // =====================================================

    public int getPendingDietitianCount() {

        return getPendingDietitians()
                .size();
    }


    // =====================================================
    // APPROVED COUNT
    // =====================================================

    public int getApprovedDietitianCount() {

        return getApprovedDietitians()
                .size();
    }


    // =====================================================
    // SUSPENDED COUNT
    // =====================================================

    public int getSuspendedDietitianCount() {

        return getSuspendedDietitians()
                .size();
    }


    // =====================================================
    // REJECTED COUNT
    // =====================================================

    public int getRejectedDietitianCount() {

        return getRejectedDietitians()
                .size();
    }


    // =====================================================
    // SUBSCRIBER COUNT
    // =====================================================

    public int getSubscriberCount(
            String dietitianUid
    ) {

        return getSubscribers(
                dietitianUid
        ).size();
    }


    // =====================================================
    // TOTAL REVENUE FOR DIETITIAN
    // =====================================================

    public double getTotalRevenue(
            String dietitianUid
    ) {

        double revenue = 0;


        for (
                Map<String, Object> subscriber :
                getSubscribers(
                        dietitianUid
                )
        ) {

            String paymentStatus =
                    stringValue(
                            subscriber.get(
                                    "paymentStatus"
                            )
                    );


            if (
                    !"paid".equalsIgnoreCase(
                            paymentStatus
                    )
            ) {

                continue;
            }


            Object amount =
                    subscriber.get(
                            "amount"
                    );


            if (
                    amount instanceof Number
            ) {

                revenue +=
                        ((Number) amount)
                                .doubleValue();
            }
        }


        return revenue;
    }


    // =====================================================
    // =====================================================
    //
    // DIETITIAN DELETION
    //
    // =====================================================
    // =====================================================


    // =====================================================
    // DELETE DIETITIAN PROFILE
    //
    // IMPORTANT:
    // This deletes only the main dietitian document.
    //
    // We will build full Admin account cleanup later,
    // because Firestore does NOT automatically delete
    // subscriber/review subcollections.
    // =====================================================

    public boolean deleteDietitian(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            db.collection(
                            DIETITIAN_COLLECTION
                    )
                    .document(
                            uid.trim()
                    )
                    .delete()
                    .get();


            System.out.println(
                    "Dietitian profile deleted: "
                            + uid
            );


            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // CHECK DIETITIAN EXISTS
    // =====================================================

    public boolean dietitianExists(
            String uid
    ) {

        if (isBlank(uid)) {

            return false;
        }


        try {

            DocumentSnapshot document =
                    db.collection(
                                    DIETITIAN_COLLECTION
                            )
                            .document(
                                    uid.trim()
                            )
                            .get()
                            .get();


            return document.exists();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // =====================================================
    //
    // UTILITY METHODS
    //
    // =====================================================
    // =====================================================


    private boolean isBlank(
            String value
    ) {

        return value == null
                ||
                value.trim().isEmpty();
    }


    private String safeTrim(
            String value
    ) {

        if (value == null) {

            return "";
        }


        return value.trim();
    }


    private boolean contains(
            String value,
            String query
    ) {

        if (
                value == null
                        ||
                query == null
        ) {

            return false;
        }


        return value
                .toLowerCase()
                .contains(
                        query
                );
    }


    private String stringValue(
            Object value
    ) {

        if (value == null) {

            return "";
        }


        return String.valueOf(
                value
        );
    }
}