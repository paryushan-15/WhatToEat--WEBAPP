package com.bytebites.model;

public class FamilyMember {
    private String memberId;
    private String userId;

    private String name;
    private String gender;
    private int age;

    private double weight;
    private double height;
    private double bmi;

    private String allergy;
    private String healthIssue;
    private String goal;

    private String photoUrl;

    public FamilyMember() {

    }

    public FamilyMember(
        String memberId,
        String userId,
        String name,
        String gender,
        int age,
        double weight,
        double height,
        double bmi,
        String allergy,
        String healthIssue,
        String goal,
        String photoUrl
) {
    this.memberId = memberId;
    this.userId = userId;
    this.name = name;
    this.gender = gender;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.bmi = bmi;
    this.allergy = allergy;
    this.healthIssue = healthIssue;
    this.goal = goal;
    this.photoUrl = photoUrl;
}

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getHealthIssue() {
        return healthIssue;
    }

    public void setHealthIssue(String healthIssue) {
        this.healthIssue = healthIssue;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    
}
