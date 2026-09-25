package com.bytebites.model;

import java.util.ArrayList;
import java.util.List;

public class UploadedRecipe {

    private String postId;
    private String username;
    private String time;
    private String dishName;
    private String ingredients;
    private String recipe;
    private String imageUrl;

    private List<String> likedBy;
    private List<String> savedBy;

    // =====================================================
    // EMPTY CONSTRUCTOR
    // Required for Firestore
    // =====================================================

    public UploadedRecipe() {

        likedBy = new ArrayList<>();
        savedBy = new ArrayList<>();
    }

    // =====================================================
    // PARAMETERIZED CONSTRUCTOR
    // =====================================================

    public UploadedRecipe(
            String username,
            String time,
            String dishName,
            String ingredients,
            String recipe,
            String imageUrl) {

        this.username = username;
        this.time = time;
        this.dishName = dishName;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.imageUrl = imageUrl;

        this.likedBy = new ArrayList<>();
        this.savedBy = new ArrayList<>();
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getPostId() {
        return postId;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getDishName() {
        return dishName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getLikedBy() {
        if (likedBy == null) {
            likedBy = new ArrayList<>();
        }

        return likedBy;
    }

    public List<String> getSavedBy() {
        if (savedBy == null) {
            savedBy = new ArrayList<>();
        }

        return savedBy;
    }

    // =====================================================
    // SETTERS
    // Required for Firestore
    // =====================================================

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }

    public void setSavedBy(List<String> savedBy) {
        this.savedBy = savedBy;
    }
}