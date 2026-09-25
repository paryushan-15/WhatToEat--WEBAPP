package com.bytebites.controller;

import java.util.List;

import com.bytebites.dao.SocialRecipeDao;
import com.bytebites.model.UploadedRecipe;

public class RecipeController {

    private SocialRecipeDao recipeDao =
            new SocialRecipeDao();

    // =====================================================
    // ADD / SAVE RECIPE
    // =====================================================

   public String addUploadedRecipe(
        String username,
        String time,
        String dishName,
        String ingredients,
        String recipe,
        String imageUrl) {

    UploadedRecipe uploadedRecipe =
            new UploadedRecipe(
                    username,
                    time,
                    dishName,
                    ingredients,
                    recipe,
                    imageUrl);

    return recipeDao.saveRecipe(uploadedRecipe);
}

    // =====================================================
    // GET / FETCH ALL RECIPES
    // =====================================================

    public List<UploadedRecipe> getUploadedRecipes() {

        return recipeDao.getRecipes();
    }

    // =====================================================
    // LIKE POST
    // =====================================================

    public void likeRecipe(
            String postId,
            String userUid) {

        recipeDao.likeRecipe(
                postId,
                userUid
        );
    }

    // =====================================================
    // UNLIKE POST
    // =====================================================

    public void unlikeRecipe(
            String postId,
            String userUid) {

        recipeDao.unlikeRecipe(
                postId,
                userUid
        );
    }

    // =====================================================
    // SAVE POST
    // =====================================================

    public void saveRecipeForUser(
            String postId,
            String userUid) {

        recipeDao.saveRecipeForUser(
                postId,
                userUid
        );
    }

    // =====================================================
    // UNSAVE POST
    // =====================================================

    public void unsaveRecipeForUser(
            String postId,
            String userUid) {

        recipeDao.unsaveRecipeForUser(
                postId,
                userUid
        );
    }

    // =====================================================
// GET MY SAVED RECIPES
// =====================================================

public List<UploadedRecipe> getSavedRecipes(
        String userUid) {

    return recipeDao.getSavedRecipes(
            userUid
    );
}

// =====================================================
// UPDATE RECIPE
// =====================================================

public void updateRecipe(
        String postId,
        String dishName,
        String ingredients,
        String recipe,
        String imageUrl) {

    recipeDao.updateRecipe(
            postId,
            dishName,
            ingredients,
            recipe,
            imageUrl
    );
}

// =====================================================
// DELETE RECIPE
// =====================================================

public void deleteRecipe(
        String postId) {

    recipeDao.deleteRecipe(
            postId
    );
}

}