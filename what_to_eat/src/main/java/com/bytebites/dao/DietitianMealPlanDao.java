package com.bytebites.dao;

import java.util.HashMap;
import java.util.Map;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.DietitianMealPlan;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class DietitianMealPlanDao {

    private final Firestore db =
            FirebaseConfig.getFirestore();

    public void savePlan(
            String userId,
            String weekStartDate,
            String mealPlanJson,
            String insight,
            String dietitianId)
            throws Exception {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "weekStartDate",
                weekStartDate);

        data.put(
                "mealPlanJson",
                mealPlanJson);

        data.put(
                "insight",
                insight);

        data.put(
                "generatedBy",
                dietitianId);

        data.put(
                "source",
                "DIETITIAN");

        db.collection("mealPlans")
                .document(userId)
                .set(data)
                .get();
    }

    public DocumentSnapshot getPlan(
            String userId)
            throws Exception {

        return db.collection("mealPlans")
                .document(userId)
                .get()
                .get();
    }

    public boolean hasDietitianMealPlan(String userId) {
    try {
        DocumentSnapshot doc = db.collection("mealPlans")
                .document(userId)
                .get()
                .get();

        if (!doc.exists()) {
            return false;
        }

        String source = doc.getString("source");

        return "DIETITIAN".equalsIgnoreCase(source);

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public DietitianMealPlan getDietitianMealPlan(String userId) {
    try {
        DocumentSnapshot doc = db.collection("mealPlans")
                .document(userId)
                .get()
                .get();

        if (!doc.exists()) {
            return null;
        }

        String source = doc.getString("source");

        if (!"DIETITIAN".equalsIgnoreCase(source)) {
            return null;
        }

        DietitianMealPlan plan = new DietitianMealPlan();

        plan.setUserId(userId);
        plan.setWeekStartDate(doc.getString("weekStartDate"));
        plan.setMealPlanJson(doc.getString("mealPlanJson"));
        plan.setInsight(doc.getString("insight"));
        plan.setGeneratedBy(doc.getString("generatedBy"));

        return plan;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
    
}