package com.bytebites.dao;

import java.util.HashMap;
import java.util.Map;

import com.bytebites.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class MealPlanDao {

    private final Firestore db =
            FirebaseConfig.getFirestore();

    public void saveMealPlan(
            String userId,
            String weekStartDate,
            String mealPlanJson,
            String insight)
            throws Exception {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "weekStartDate",
                weekStartDate
        );

        data.put(
                "mealPlanJson",
                mealPlanJson
        );
        data.put(
        "insight",
        insight
);

        System.out.println(
                "Saving Meal Plan..."
        );

        System.out.println(
                "User ID: " + userId
        );

        System.out.println(
                "Week: " + weekStartDate
        );

        db.collection("mealPlans")
                .document(userId)
                .set(data)
                .get();

        System.out.println(
                "Saved Successfully"
        );
    }

    public String getMealPlan(
            String userId) {

        try {

            System.out.println(
                    "Fetching meal plan for user: "
                            + userId
            );

            DocumentSnapshot document =
                    db.collection("mealPlans")
                            .document(userId)
                            .get()
                            .get();

            System.out.println(
                    "Document Exists: "
                            + document.exists()
            );

            if (document.exists()) {

                String mealPlan =
                        document.getString(
                                "mealPlanJson"
                        );

                System.out.println(
                        "Meal Plan Loaded"
                );

                return mealPlan;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
    public String getInsight(String userId) {

    try {

        DocumentSnapshot document =
                db.collection("mealPlans")
                        .document(userId)
                        .get()
                        .get();

        if (document.exists()) {

            return document.getString("insight");
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return null;
}
}