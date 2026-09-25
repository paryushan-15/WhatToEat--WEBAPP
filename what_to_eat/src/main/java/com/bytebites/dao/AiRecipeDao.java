package com.bytebites.dao;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AiRecipeDao {


    public void saveRecipe(
            String userId,
            String recipeJson) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "recipeJson",
                    recipeJson);

            db.collection("savedRecipes")
                    .document(userId)
                    .set(data);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getRecipe(
            String userId) {

        try {

            Firestore db =
                    FirestoreClient.getFirestore();

            ApiFuture<DocumentSnapshot> future =
                    db.collection("savedRecipes")
                            .document(userId)
                            .get();

            DocumentSnapshot document =
                    future.get();

            if (document.exists()) {

                return document.getString(
                        "recipeJson");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public String getLatestRecipeName(String userId) {

    String recipeJson = getRecipe(userId);

    if(recipeJson == null || recipeJson.isBlank()) {
        return null;
    }

    JsonObject recipe =
            new Gson().fromJson(recipeJson, JsonObject.class);

    return recipe.get("mealName").getAsString();
}
}