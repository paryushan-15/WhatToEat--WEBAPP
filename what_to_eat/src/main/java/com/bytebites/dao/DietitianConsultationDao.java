package com.bytebites.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytebites.model.UserDietitianConsultationModel;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;


public class DietitianConsultationDao {

    private static final String COLLECTION =
            "consultations";


    // =====================================================
    // GET ALL CONSULTATIONS FOR DIETITIAN
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByDietitianId(
            String dietitianId
    ) {

        List<UserDietitianConsultationModel> consultations =
                new ArrayList<>();

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "dietitianId",
                                    dietitianId
                            )
                            .get();

            List<QueryDocumentSnapshot> documents =
                    future.get().getDocuments();


            for (QueryDocumentSnapshot document : documents) {

                consultations.add(
                        convertDocumentToModel(document)
                );
            }


            // newest first
            consultations.sort(
                    (a, b) ->
                            Long.compare(
                                    b.getCreatedAt(),
                                    a.getCreatedAt()
                            )
            );


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return consultations;
    }


    // =====================================================
    // GET BY STATUS
    // =====================================================

    public List<UserDietitianConsultationModel>
    getConsultationsByStatus(
            String dietitianId,
            String status
    ) {

        List<UserDietitianConsultationModel> consultations =
                new ArrayList<>();

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "dietitianId",
                                    dietitianId
                            )
                            .whereEqualTo(
                                    "status",
                                    status
                            )
                            .get();


            for (
                    QueryDocumentSnapshot document :
                    future.get().getDocuments()
            ) {

                consultations.add(
                        convertDocumentToModel(document)
                );
            }


            consultations.sort(
                    (a, b) ->
                            Long.compare(
                                    b.getCreatedAt(),
                                    a.getCreatedAt()
                            )
            );


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return consultations;
    }


    // =====================================================
    // ACCEPT REQUEST
    // =====================================================

        public boolean acceptConsultation(
                        String consultationId,
                        String confirmedDate,
                        String confirmedTime,
                        String dietitianMessage,
                        String meetingLink
                ) {

                try {

                        Firestore db =
                                FirestoreClient.getFirestore();


                        Map<String, Object> updates =
                                new HashMap<>();


                        updates.put(
                                "confirmedDate",
                                confirmedDate
                        );

                        updates.put(
                                "confirmedTime",
                                confirmedTime
                        );

                        updates.put(
                                "dietitianMessage",
                                dietitianMessage
                        );

                        updates.put(
                                "meetingLink",
                                meetingLink
                        );

                        updates.put(
                                "status",
                                "DIETITIAN_ACCEPTED"
                        );

                        updates.put(
                                "acceptedAt",
                                System.currentTimeMillis()
                        );


                        db.collection(COLLECTION)
                                .document(
                                        consultationId
                                )
                                .update(
                                        updates
                                )
                                .get();


                        return true;


                } catch (Exception ex) {

                        ex.printStackTrace();

                        return false;
                }
        }

    // =====================================================
    // REJECT REQUEST
    // =====================================================

    public boolean rejectConsultation(
            String consultationId
    ) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();


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

            updates.put(
                    "cancelledBy",
                    "DIETITIAN"
            );


            db.collection(COLLECTION)
                    .document(consultationId)
                    .update(updates)
                    .get();


            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // MARK COMPLETED
    // =====================================================

    public boolean completeConsultation(
            String consultationId
    ) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();


            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    "COMPLETED"
            );

            updates.put(
                    "completedAt",
                    System.currentTimeMillis()
            );


            db.collection(COLLECTION)
                    .document(consultationId)
                    .update(updates)
                    .get();


            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // SAVE MEETING LINK
    // =====================================================

    public boolean updateMeetingLink(
            String consultationId,
            String meetingLink
    ) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();


            db.collection(COLLECTION)
                    .document(consultationId)
                    .update(
                            "meetingLink",
                            meetingLink
                    )
                    .get();


            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // DOCUMENT -> MODEL
    // =====================================================

    private UserDietitianConsultationModel
    convertDocumentToModel(
            DocumentSnapshot document
    ) {

        UserDietitianConsultationModel model =
                new UserDietitianConsultationModel();


        model.setConsultationId(
                getString(
                        document,
                        "consultationId"
                )
        );


        model.setUserId(
                getString(
                        document,
                        "userId"
                )
        );

        model.setUserName(
                getString(
                        document,
                        "userName"
                )
        );


        model.setDietitianId(
                getString(
                        document,
                        "dietitianId"
                )
        );

        model.setDietitianName(
                getString(
                        document,
                        "dietitianName"
                )
        );


        model.setRequestedDate(
                getString(
                        document,
                        "requestedDate"
                )
        );

        model.setRequestedTime(
                getString(
                        document,
                        "requestedTime"
                )
        );


        model.setConfirmedDate(
                getString(
                        document,
                        "confirmedDate"
                )
        );

        model.setConfirmedTime(
                getString(
                        document,
                        "confirmedTime"
                )
        );


        model.setReason(
                getString(
                        document,
                        "reason"
                )
        );

        model.setDietitianMessage(
                getString(
                        document,
                        "dietitianMessage"
                )
        );

        model.setStatus(
                getString(
                        document,
                        "status"
                )
        );

        model.setMeetingLink(
                getString(
                        document,
                        "meetingLink"
                )
        );


        model.setCreatedAt(
                getLong(
                        document,
                        "createdAt"
                )
        );

        model.setAcceptedAt(
                getLong(
                        document,
                        "acceptedAt"
                )
        );

        model.setConfirmedAt(
                getLong(
                        document,
                        "confirmedAt"
                )
        );

        model.setCompletedAt(
                getLong(
                        document,
                        "completedAt"
                )
        );

        model.setCancelledAt(
                getLong(
                        document,
                        "cancelledAt"
                )
        );


        return model;
    }


    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(field);

        return value == null
                ? null
                : String.valueOf(value);
    }


    private long getLong(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(field);

        if (value instanceof Number) {

            return ((Number) value)
                    .longValue();
        }

        return 0L;
    }
}