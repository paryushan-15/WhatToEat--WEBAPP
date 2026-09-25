package com.bytebites.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.model.UserDietitianConsultationModel;

import com.google.api.core.ApiFuture;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import com.google.firebase.cloud.FirestoreClient;


public class UserDietitianConsultationDao {


    // =====================================================
    // COLLECTION NAME
    // =====================================================

    private static final String COLLECTION =
            "consultations";


    // =====================================================
    // CREATE CONSULTATION
    // =====================================================

    public String createConsultation(
            UserDietitianConsultationModel consultation
    ) throws Exception {


        Firestore db =
                FirestoreClient
                        .getFirestore();


        // CREATE NEW DOCUMENT ID

        DocumentReference documentReference =
                db.collection(
                        COLLECTION
                )
                .document();


        String consultationId =
                documentReference
                        .getId();


        // SET ID INSIDE MODEL

        consultation.setConsultationId(
                consultationId
        );


        // =================================================
        // FIREBASE MAP
        // =================================================

        Map<String, Object> data =
                new HashMap<>();


        data.put(
                "consultationId",
                consultationId
        );


        // USER

        data.put(
                "userId",
                consultation.getUserId()
        );

        data.put(
                "userName",
                consultation.getUserName()
        );


        // DIETITIAN

        data.put(
                "dietitianId",
                consultation.getDietitianId()
        );

        data.put(
                "dietitianName",
                consultation.getDietitianName()
        );


        // REQUESTED SLOT

        data.put(
                "requestedDate",
                consultation.getRequestedDate()
        );

        data.put(
                "requestedTime",
                consultation.getRequestedTime()
        );


        // FINAL SLOT
        // Initially null

        data.put(
                "confirmedDate",
                consultation.getConfirmedDate()
        );

        data.put(
                "confirmedTime",
                consultation.getConfirmedTime()
        );


        // REASON

        data.put(
                "reason",
                consultation.getReason()
        );


        // DIETITIAN MESSAGE
        // Initially null

        data.put(
                "dietitianMessage",
                consultation.getDietitianMessage()
        );


        // STATUS

        data.put(
                "status",
                consultation.getStatus() == null
                        ? "PENDING"
                        : consultation.getStatus()
        );


        // MEETING LINK
        // Initially null

        data.put(
                "meetingLink",
                consultation.getMeetingLink()
        );


        // TIMESTAMPS

        data.put(
                "createdAt",
                consultation.getCreatedAt()
        );

        data.put(
                "acceptedAt",
                consultation.getAcceptedAt()
        );

        data.put(
                "confirmedAt",
                consultation.getConfirmedAt()
        );

        data.put(
                "completedAt",
                consultation.getCompletedAt()
        );

        data.put(
                "cancelledAt",
                consultation.getCancelledAt()
        );


        // =================================================
        // SAVE TO FIREBASE
        // =================================================

        ApiFuture<WriteResult> future =
                documentReference.set(
                        data
                );


        future.get();


        System.out.println(
                "======================================"
        );

        System.out.println(
                "CONSULTATION REQUEST SAVED"
        );

        System.out.println(
                "Consultation ID: "
                        + consultationId
        );

        System.out.println(
                "User ID: "
                        + consultation.getUserId()
        );

        System.out.println(
                "Dietitian ID: "
                        + consultation.getDietitianId()
        );

        System.out.println(
                "Requested Date: "
                        + consultation.getRequestedDate()
        );

        System.out.println(
                "Requested Time: "
                        + consultation.getRequestedTime()
        );

        System.out.println(
                "Status: "
                        + consultation.getStatus()
        );

        System.out.println(
                "======================================"
        );


        return consultationId;
    }


    // =====================================================
    // GET ALL CONSULTATIONS OF CURRENT USER
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByUserId(
            String userId
    ) {

        List<UserDietitianConsultationModel> consultations =
                new ArrayList<>();


        try {

            Firestore db =
                    FirestoreClient
                            .getFirestore();


            ApiFuture<QuerySnapshot> future =
                    db.collection(
                            COLLECTION
                    )
                    .whereEqualTo(
                            "userId",
                            userId
                    )
                    .get();


            List<QueryDocumentSnapshot> documents =
                    future.get()
                            .getDocuments();


            for (
                    QueryDocumentSnapshot document :
                    documents
            ) {

                UserDietitianConsultationModel consultation =
                        convertDocumentToModel(
                                document
                        );


                consultations.add(
                        consultation
                );
            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        consultations.sort(
                (a, b) ->
                        Long.compare(
                                b.getCreatedAt(),
                                a.getCreatedAt()
                        )
        );

        return consultations;
    }


    // =====================================================
    // GET USER CONSULTATIONS BY STATUS
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByUserAndStatus(
            String userId,
            String status
    ) {

        List<UserDietitianConsultationModel> consultations =
                new ArrayList<>();


        try {

            Firestore db =
                    FirestoreClient
                            .getFirestore();


            ApiFuture<QuerySnapshot> future =
                    db.collection(
                            COLLECTION
                    )
                    .whereEqualTo(
                            "userId",
                            userId
                    )
                    .whereEqualTo(
                            "status",
                            status
                    )
                    .get();


            List<QueryDocumentSnapshot> documents =
                    future.get()
                            .getDocuments();


            for (
                    QueryDocumentSnapshot document :
                    documents
            ) {

                consultations.add(
                        convertDocumentToModel(
                                document
                        )
                );
            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }


        return consultations;
    }


    // =====================================================
    // GET SINGLE CONSULTATION
    // =====================================================

    public UserDietitianConsultationModel
    getConsultationById(
            String consultationId
    ) {

        try {

            Firestore db =
                    FirestoreClient
                            .getFirestore();


            DocumentSnapshot document =
                    db.collection(
                            COLLECTION
                    )
                    .document(
                            consultationId
                    )
                    .get()
                    .get();


            if (!document.exists()) {

                return null;
            }


            return convertDocumentToModel(
                    document
            );


        } catch (Exception ex) {

            ex.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // USER CONFIRMS CONSULTATION
    // =====================================================

    public boolean confirmConsultation(
            String consultationId
    ) {

        try {

            Firestore db =
                    FirestoreClient
                            .getFirestore();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "CONFIRMED"
            );

            updates.put(
                    "confirmedAt",
                    System.currentTimeMillis()
            );


            db.collection(
                    COLLECTION
            )
            .document(
                    consultationId
            )
            .update(
                    updates
            )
            .get();


            System.out.println(
                    "Consultation confirmed: "
                            + consultationId
            );


            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // USER CANCELS CONSULTATION
    // =====================================================

    public boolean cancelConsultation(
            String consultationId
    ) {

        try {

            Firestore db =
                    FirestoreClient
                            .getFirestore();


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "CANCELLED"
            );

            updates.put(
                    "cancelledAt",
                    System.currentTimeMillis()
            );


            db.collection(
                    COLLECTION
            )
            .document(
                    consultationId
            )
            .update(
                    updates
            )
            .get();


            System.out.println(
                    "Consultation cancelled: "
                            + consultationId
            );


            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // CONVERT FIREBASE DOCUMENT TO MODEL
    // =====================================================

    private UserDietitianConsultationModel
    convertDocumentToModel(
            DocumentSnapshot document
    ) {

        UserDietitianConsultationModel consultation =
                new UserDietitianConsultationModel();


        consultation.setConsultationId(
                getString(
                        document,
                        "consultationId"
                )
        );


        consultation.setUserId(
                getString(
                        document,
                        "userId"
                )
        );

        consultation.setUserName(
                getString(
                        document,
                        "userName"
                )
        );


        consultation.setDietitianId(
                getString(
                        document,
                        "dietitianId"
                )
        );

        consultation.setDietitianName(
                getString(
                        document,
                        "dietitianName"
                )
        );


        consultation.setRequestedDate(
                getString(
                        document,
                        "requestedDate"
                )
        );

        consultation.setRequestedTime(
                getString(
                        document,
                        "requestedTime"
                )
        );


        consultation.setConfirmedDate(
                getString(
                        document,
                        "confirmedDate"
                )
        );

        consultation.setConfirmedTime(
                getString(
                        document,
                        "confirmedTime"
                )
        );


        consultation.setReason(
                getString(
                        document,
                        "reason"
                )
        );


        consultation.setDietitianMessage(
                getString(
                        document,
                        "dietitianMessage"
                )
        );


        consultation.setStatus(
                getString(
                        document,
                        "status"
                )
        );


        consultation.setMeetingLink(
                getString(
                        document,
                        "meetingLink"
                )
        );


        consultation.setCreatedAt(
                getLong(
                        document,
                        "createdAt"
                )
        );

        consultation.setAcceptedAt(
                getLong(
                        document,
                        "acceptedAt"
                )
        );

        consultation.setConfirmedAt(
                getLong(
                        document,
                        "confirmedAt"
                )
        );

        consultation.setCompletedAt(
                getLong(
                        document,
                        "completedAt"
                )
        );

        consultation.setCancelledAt(
                getLong(
                        document,
                        "cancelledAt"
                )
        );


        return consultation;
    }


    // =====================================================
    // SAFE STRING
    // =====================================================

    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(
                        field
                );


        if (value == null) {

            return null;
        }


        return String.valueOf(
                value
        );
    }


    // =====================================================
    // SAFE LONG
    // =====================================================

    private long getLong(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(
                        field
                );


        if (value instanceof Number) {

            return ((Number) value)
                    .longValue();
        }


        return 0L;
    }
}