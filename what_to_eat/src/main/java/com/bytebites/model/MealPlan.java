package com.bytebites.model;

public class MealPlan {

    private String userId;
    private String weekStartDate;
    private String mealPlanJson;

    public MealPlan() {
    }

    public MealPlan(
            String userId,
            String weekStartDate,
            String mealPlanJson) {

        this.userId = userId;
        this.weekStartDate = weekStartDate;
        this.mealPlanJson = mealPlanJson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(String weekStartDate) {
        this.weekStartDate = weekStartDate;
    }

    public String getMealPlanJson() {
        return mealPlanJson;
    }

    public void setMealPlanJson(String mealPlanJson) {
        this.mealPlanJson = mealPlanJson;
    }
}