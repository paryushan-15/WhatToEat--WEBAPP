package com.bytebites.model;

public class DietitianMealPlan {

    private String userId;
    private String weekStartDate;
    private String mealPlanJson;
    private String insight;
    private String generatedBy;

    public DietitianMealPlan() {
    }

    public DietitianMealPlan(
            String userId,
            String weekStartDate,
            String mealPlanJson,
            String insight,
            String generatedBy) {

        this.userId = userId;
        this.weekStartDate = weekStartDate;
        this.mealPlanJson = mealPlanJson;
        this.insight = insight;
        this.generatedBy = generatedBy;
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

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    
}