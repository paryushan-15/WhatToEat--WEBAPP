package com.bytebites.controller;

import com.bytebites.dao.DietitianMealPlanDao;
import com.google.cloud.firestore.DocumentSnapshot;

public class DietitianMealPlanController {

    private final DietitianMealPlanDao dao =
            new DietitianMealPlanDao();

    public void saveMealPlan(
            String userId,
            String weekStartDate,
            String mealPlanJson,
            String insight,
            String dietitianId)
            throws Exception {

        dao.savePlan(
                userId,
                weekStartDate,
                mealPlanJson,
                insight,
                dietitianId);
    }

    public DocumentSnapshot getMealPlan(
            String userId)
            throws Exception {

        return dao.getPlan(userId);
    }
}