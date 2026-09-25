package com.bytebites.dao;

import java.util.ArrayList;
import java.util.List;

import com.bytebites.config.FirebaseConfig;
import com.bytebites.model.UploadedRecipe;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class SocialRecipeDao {

    private Firestore db =
            FirebaseConfig.getFirestore();

   // =====================================================
        // SAVE RECIPE
        // =====================================================

        public String saveRecipe(UploadedRecipe recipe) {

        try {

                // Create a new Firestore document
                DocumentReference document =
                        db.collection("uploadedRecipes")
                                .document();

                // Store the generated document ID
                String postId = document.getId();

                recipe.setPostId(postId);

                // Save recipe
                document.set(recipe).get();

                System.out.println(
                        "Recipe saved successfully!");

                // Return generated post ID
                return postId;

        } catch (Exception e) {

                e.printStackTrace();

                return null;
        }
        }

    // =====================================================
    // FETCH ALL RECIPES
    // =====================================================

    public List<UploadedRecipe> getRecipes() {

        List<UploadedRecipe> recipes =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    db.collection("uploadedRecipes")
                            .get();

            QuerySnapshot querySnapshot =
                    future.get();

            for (
                    DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                UploadedRecipe recipe =
                        document.toObject(
                                UploadedRecipe.class);

                if (recipe != null) {

                    // For old posts which don't have postId
                    if (recipe.getPostId() == null) {

                        recipe.setPostId(
                                document.getId());
                    }

                    recipes.add(recipe);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return recipes;
    }

    // =====================================================
    // LIKE POST
    // =====================================================

    public void likeRecipe(
            String postId,
            String userUid) {

        try {

            db.collection("uploadedRecipes")
                    .document(postId)
                    .update(
                            "likedBy",
                            FieldValue.arrayUnion(userUid))
                    .get();

            System.out.println(
                    "Post liked!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // UNLIKE POST
    // =====================================================

    public void unlikeRecipe(
            String postId,
            String userUid) {

        try {

            db.collection("uploadedRecipes")
                    .document(postId)
                    .update(
                            "likedBy",
                            FieldValue.arrayRemove(userUid))
                    .get();

            System.out.println(
                    "Post unliked!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // SAVE POST
    // =====================================================

    public void saveRecipeForUser(
            String postId,
            String userUid) {

        try {

            db.collection("uploadedRecipes")
                    .document(postId)
                    .update(
                            "savedBy",
                            FieldValue.arrayUnion(userUid))
                    .get();

            System.out.println(
                    "Recipe saved!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // UNSAVE POST
    // =====================================================

    public void unsaveRecipeForUser(
            String postId,
            String userUid) {

        try {

            db.collection("uploadedRecipes")
                    .document(postId)
                    .update(
                            "savedBy",
                            FieldValue.arrayRemove(userUid))
                    .get();

            System.out.println(
                    "Recipe removed from saved recipes!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

// =====================================================
// GET MY SAVED RECIPES
// =====================================================

public List<UploadedRecipe> getSavedRecipes(
        String userUid) {

    List<UploadedRecipe> savedRecipes =
            new ArrayList<>();

    try {

        ApiFuture<QuerySnapshot> future =
                db.collection("uploadedRecipes")
                        .whereArrayContains(
                                "savedBy",
                                userUid)
                        .get();

        QuerySnapshot querySnapshot =
                future.get();

        for (
                DocumentSnapshot document :
                querySnapshot.getDocuments()) {

            UploadedRecipe recipe =
                    document.toObject(
                            UploadedRecipe.class);

            if (recipe != null) {

                if (recipe.getPostId() == null) {

                    recipe.setPostId(
                            document.getId());
                }

                savedRecipes.add(recipe);
            }
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return savedRecipes;
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

    try {

        db.collection("uploadedRecipes")
                .document(postId)
                .update(
                        "dishName", dishName,
                        "ingredients", ingredients,
                        "recipe", recipe,
                        "imageUrl", imageUrl)
                .get();

        System.out.println(
                "Recipe updated successfully!");

    } catch (Exception e) {

        e.printStackTrace();
    }
}

// =====================================================
// DELETE RECIPE
// =====================================================

public void deleteRecipe(
        String postId) {

    try {

        db.collection("uploadedRecipes")
                .document(postId)
                .delete()
                .get();

        System.out.println(
                "Recipe deleted successfully!");

    } catch (Exception e) {

        e.printStackTrace();
    }
}

}