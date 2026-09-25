package com.bytebites.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.bytebites.config.ApiConfig;
import com.bytebites.dao.MealPlanDao;
import com.bytebites.dao.RecipeDao;
import com.bytebites.model.session.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CookNowController {

        private final MealPlanDao mealPlanDao;
        private final RecipeDao recipeDao;
        private String calories = "--";
        private String protein = "--";
        private String carbs = "--";
        private String recipeName = "";
        private String cookTime = "";
        private String lastRecipeJson = null;

        public CookNowController() {

                mealPlanDao = new MealPlanDao();
                recipeDao = new RecipeDao();
        }

        // =========================================================
        // GET TODAY'S MEALS
        // =========================================================

        public JsonObject getTodaysMeals(String userId) {

                JsonObject result = new JsonObject();

                try {

                        if (userId == null || userId.isBlank()) {

                                result.addProperty(
                                                "error",
                                                "User ID not found.");

                                return result;
                        }

                        System.out.println(
                                        "Loading today's meal plan for user: "
                                                        + userId);

                        String mealPlanJson = mealPlanDao.getMealPlan(userId);

                        System.out.println(
                                        "\n========== WEEKLY MEAL PLAN ==========\n");

                        System.out.println(mealPlanJson);

                        System.out.println(
                                        "\n======================================\n");
                        if (mealPlanJson == null ||
                                        mealPlanJson.isBlank()) {

                                result.addProperty(
                                                "error",
                                                "No weekly meal plan found. Please generate a weekly meal plan first.");

                                return result;
                        }

                        mealPlanJson = cleanJson(mealPlanJson);

                        JsonObject weeklyPlan = new Gson().fromJson(
                                        mealPlanJson,
                                        JsonObject.class);

                        String today = java.time.LocalDate.now()
                                        .getDayOfWeek()
                                        .toString();

                        System.out.println(
                                        "Today: " + today);

                        if (!weeklyPlan.has(today)) {

                                result.addProperty(
                                                "error",
                                                "No meal plan found for " + today);

                                return result;
                        }

                        JsonObject todayPlan = weeklyPlan.getAsJsonObject(today);

                        System.out.println("TODAY PLAN:");
                        System.out.println(todayPlan);

                        result.addProperty(
                                        "day",
                                        today);

                        result.addProperty(
                                        "breakfast",
                                        getValue(todayPlan, "breakfast"));

                        result.addProperty(
                                        "lunch",
                                        getValue(todayPlan, "lunch"));

                        result.addProperty(
                                        "dinner",
                                        getValue(todayPlan, "dinner"));

                        // =========================================
                        // GET NUTRITION FOR CURRENT MEAL
                        // =========================================

                        int hour = java.time.LocalTime.now().getHour();

                        String mealKey;

                        if (hour >= 5 && hour < 12) {

                                mealKey = "breakfast";

                        } else if (hour >= 12 && hour < 17) {

                                mealKey = "lunch";

                        } else {

                                mealKey = "dinner";
                        }

                        JsonObject mealObj = null;

                        if (todayPlan.has(mealKey)
                                        && todayPlan.get(mealKey).isJsonObject()) {

                                mealObj = todayPlan.getAsJsonObject(mealKey);
                        }

                        if (mealObj != null) {

                                result.addProperty(
                                                "calories",
                                                getNutritionValue(
                                                                mealObj,
                                                                "calories"));

                                result.addProperty(
                                                "protein",
                                                getNutritionValue(
                                                                mealObj,
                                                                "protein"));

                                result.addProperty(
                                                "carbs",
                                                getNutritionValue(
                                                                mealObj,
                                                                "carbs"));

                        } else {

                                result.addProperty("calories", "--");
                                result.addProperty("protein", "--");
                                result.addProperty("carbs", "--");
                        }

                        return result;

                } catch (Exception e) {

                        e.printStackTrace();

                        result.addProperty(
                                        "error",
                                        "Unable to load today's meal plan.");

                        return result;
                }
        }

        public List<JsonObject> getRecentMeals(
                        String userId,
                        int limit) {

                List<JsonObject> recentMeals = new ArrayList<>();

                try {

                        String mealPlanJson = mealPlanDao.getMealPlan(userId);

                        if (mealPlanJson == null ||
                                        mealPlanJson.isBlank()) {

                                return recentMeals;
                        }

                        mealPlanJson = cleanJson(mealPlanJson);

                        JsonObject weeklyPlan = new Gson().fromJson(
                                        mealPlanJson,
                                        JsonObject.class);

                        DayOfWeek today = LocalDate.now().getDayOfWeek();

                        String[] mealOrder = {
                                        "dinner",
                                        "lunch",
                                        "breakfast"
                        };

                        for (int dayOffset = 1; dayOffset < 7
                                        && recentMeals.size() < limit; dayOffset++) {

                                DayOfWeek day = today.minus(dayOffset);

                                String dayName = day.toString();

                                if (!weeklyPlan.has(dayName)) {
                                        continue;
                                }

                                JsonObject dayPlan = weeklyPlan.getAsJsonObject(
                                                dayName);

                                for (String mealType : mealOrder) {

                                        if (recentMeals.size() >= limit) {
                                                break;
                                        }

                                        if (!dayPlan.has(mealType)) {
                                                continue;
                                        }

                                        if (!dayPlan.get(mealType)
                                                        .isJsonObject()) {
                                                continue;
                                        }

                                        JsonObject mealObj = dayPlan.getAsJsonObject(
                                                        mealType);

                                        if (!mealObj.has("meal")) {
                                                continue;
                                        }

                                        JsonObject item = new JsonObject();

                                        item.addProperty(
                                                        "meal",
                                                        mealObj.get("meal")
                                                                        .getAsString());

                                        item.addProperty(
        "label",
        dayName.substring(0,1).toUpperCase()
                + dayName.substring(1).toLowerCase()
                + " "
                + mealType.substring(0,1).toUpperCase()
                + mealType.substring(1).toLowerCase());

                                        recentMeals.add(item);
                                }
                        }

                } catch (Exception e) {

                        e.printStackTrace();
                }

                return recentMeals;
        }
        // =========================================================
        // GENERATE RECIPE USING GEMINI
        // =========================================================

        public String generateRecipe(
                        String mealName,
                        int numberOfPeople) {

                try {

                        if (mealName == null ||
                                        mealName.isBlank()) {

                                return null;
                        }

                        System.out.println(
                                        "\n==================================");

                        System.out.println(
                                        "Generating Recipe");

                        System.out.println(
                                        "Meal: " + mealName);

                        System.out.println(
                                        "People: " + numberOfPeople);

                        System.out.println(
                                        "==================================");

                        String prompt = """
                                        You are an expert Indian home chef.

                                        Generate a complete recipe for:

                                        MEAL:
                                        %s

                                        NUMBER OF PEOPLE:
                                        %d

                                        IMPORTANT RULES:

                                        1. Generate recipe only for the meal provided.
                                        2. Use common Indian ingredients.
                                        3. Prefer Maharashtrian style where suitable.
                                        4. Scale ingredient quantities for the specified number of people.
                                        5. Give practical home-cooking instructions.
                                        6. Return ONLY valid JSON.
                                        7. Do NOT return markdown.
                                        8. Do NOT return code blocks.
                                        9. Do NOT return explanations before JSON.
                                        10. Do NOT return explanations after JSON.
                                        11. Do NOT return headings such as:
                                            "For Rice Ghavan:"
                                            "Ingredients:"
                                            "Recipe:"
                                        12. Output must begin with '{' and end with '}'.
                                        13. Every field must be present.
                                        14. Ingredients must be an array of JSON objects.
                                        15. Steps must be an array of strings.
                                        16. Tips must be an array of strings.

                                        Return EXACTLY this JSON structure:

                                        {
                                          "mealName": "",
                                          "preparationTime": "",
                                          "cookingTime": "",
                                          "totalTime": "",
                                          "caloriesPerServing": "",
                                          "proteinPerServing": "",
                                          "carbsPerServing": "",
                                          "ingredients": [
                                            {
                                              "name": "",
                                              "quantity": ""
                                            }
                                          ],
                                          "steps": [
                                            ""
                                          ],
                                          "tips": [
                                            ""
                                          ]
                                        }

                                        Example:

                                        {
                                          "mealName": "Rice Ghavan",
                                          "preparationTime": "10 min",
                                          "cookingTime": "15 min",
                                          "totalTime": "25 min",
                                          "caloriesPerServing": "220 kcal",
                                          "proteinPerServing": "4 g",
                                          "carbsPerServing": "42 g",
                                          "ingredients": [
                                            {
                                              "name": "Rice Flour",
                                              "quantity": "2 cups"
                                            },
                                            {
                                              "name": "Water",
                                              "quantity": "3 cups"
                                            }
                                          ],
                                          "steps": [
                                            "Mix rice flour and water.",
                                            "Prepare a smooth batter.",
                                            "Heat a pan.",
                                            "Pour batter and cook both sides."
                                          ],
                                          "tips": [
                                            "Use warm water for smoother batter.",
                                            "Cook on medium flame."
                                          ]
                                        }

                                        Return ONLY the JSON object.
                                        """.formatted(
                                        mealName,
                                        numberOfPeople);
                        return callAI(prompt);

                } catch (Exception e) {

                        System.out.println(
                                        "Recipe Generation Failed");

                        e.printStackTrace();

                        return null;
                }
        }

        // =========================================================
        // GEMINI API CALL
        // =========================================================

        private String callAI(String prompt)
                        throws IOException {

                OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(
                                                30,
                                                TimeUnit.SECONDS)
                                .writeTimeout(
                                                30,
                                                TimeUnit.SECONDS)
                                .readTimeout(
                                                120,
                                                TimeUnit.SECONDS)
                                .build();

                JsonObject body = new JsonObject();

                body.addProperty(
                                "model",
                                ApiConfig.MODEL);

                JsonArray messages = new JsonArray();

                JsonObject system = new JsonObject();

                system.addProperty(
                                "role",
                                "system");

                system.addProperty(
                                "content",
                                "Return only valid JSON.");

                JsonObject user = new JsonObject();

                user.addProperty(
                                "role",
                                "user");

                user.addProperty(
                                "content",
                                prompt);

                messages.add(system);
                messages.add(user);

                body.add(
                                "messages",
                                messages);

                body.addProperty(
                                "temperature",
                                0.2);

                RequestBody requestBody = RequestBody.create(
                                body.toString(),
                                MediaType.parse(
                                                "application/json"));

                Request request = new Request.Builder()
                                .url(
                                                ApiConfig.API_URL)
                                .addHeader(
                                                "Authorization",
                                                "Bearer "
                                                                + ApiConfig.API_KEY)
                                .addHeader(
                                                "Content-Type",
                                                "application/json")
                                .post(
                                                requestBody)
                                .build();

                try (
                                Response response = client.newCall(request)
                                                .execute()) {

                        if (response.body() == null) {

                                return null;
                        }

                        String json = response.body()
                                        .string();

                        System.out.println(
                                        "Groq Response:");

                        System.out.println(
                                        json);

                        if (!response.isSuccessful()) {

                                return null;
                        }

                        JsonObject root = new Gson().fromJson(
                                        json,
                                        JsonObject.class);

                        String recipe = root.getAsJsonArray(
                                        "choices")
                                        .get(0)
                                        .getAsJsonObject()
                                        .getAsJsonObject(
                                                        "message")
                                        .get("content")
                                        .getAsString();

                        String cleanedRecipe = cleanJson(recipe);
                        if (!cleanedRecipe.trim().startsWith("{")) {

                                System.out.println(
                                                "AI did not return JSON");

                                return null;
                        }

                        JsonObject recipeJson = new Gson().fromJson(
                                        cleanedRecipe,
                                        JsonObject.class);

                        if (recipeJson.has("caloriesPerServing")) {

                                calories = recipeJson.get(
                                                "caloriesPerServing")
                                                .getAsString();
                        }

                        if (recipeJson.has("proteinPerServing")) {

                                protein = recipeJson.get(
                                                "proteinPerServing")
                                                .getAsString();
                        }

                        if (recipeJson.has("carbsPerServing")) {

                                carbs = recipeJson.get(
                                                "carbsPerServing")
                                                .getAsString();
                        }

                        if (recipeJson.has("mealName")) {

                                recipeName = recipeJson.get(
                                                "mealName")
                                                .getAsString();
                        }

                        if (recipeJson.has("cookingTime")) {

                                cookTime = recipeJson.get(
                                                "cookingTime")
                                                .getAsString();
                        }
                        lastRecipeJson = cleanedRecipe;

                        recipeDao.saveRecipe(
                                        SessionManager.getUid(),
                                        cleanedRecipe);

                        System.out.println("Recipe Saved Successfully");

                        return cleanedRecipe;
                }
        }

        // =========================================================
        // GET JSON VALUE
        // =========================================================

        private String getValue(JsonObject object, String key) {

                if (!object.has(key) || object.get(key).isJsonNull()) {
                        return "Not available";
                }

                if (object.get(key).isJsonPrimitive()) {
                        return object.get(key).getAsString();
                }

                if (object.get(key).isJsonObject()) {

                        JsonObject mealObject = object.getAsJsonObject(key);

                        if (mealObject.has("meal")) {
                                return mealObject.get("meal").getAsString();
                        }
                }

                return "Not available";
        }

        private String getNutritionValue(
                        JsonObject mealObject,
                        String key) {

                if (mealObject.has(key)) {
                        return mealObject.get(key).getAsString();
                }

                return "--";
        }

        // =========================================================
        // CLEAN GEMINI JSON
        // =========================================================

        private String cleanJson(
                        String json) {

                if (json == null) {
                        return null;
                }

                json = json.trim();

                if (json.startsWith(
                                "```json")) {

                        json = json.substring(7);
                }

                else if (json.startsWith(
                                "```")) {

                        json = json.substring(3);
                }

                if (json.endsWith(
                                "```")) {

                        json = json.substring(
                                        0,
                                        json.length() - 3);
                }

                return json.trim();
        }

        public String loadSavedRecipe() {

                return recipeDao.getRecipe(
                                SessionManager.getUid());
        }

        public String getCalories() {

                return calories;
        }

        public String getProtein() {

                return protein;
        }

        public String getCarbs() {

                return carbs;
        }

        public String getRecipeName() {

                return recipeName;
        }

        public String getCookTime() {

                return cookTime;
        }

        public String getLastRecipeJson() {

                return lastRecipeJson;
        }

        public void saveRecipe(
                        String userId,
                        String recipeJson) {

                recipeDao.saveRecipe(
                                userId,
                                recipeJson);
        }

        public String getSavedRecipe(
                        String userId) {

                return recipeDao.getRecipe(
                                userId);
        }

        public String getNutritionTip(JsonObject todaysMeals) {

                if (todaysMeals == null) {
                        return "Generate a meal plan to receive nutrition tips.";
                }

                int protein = 0;

                try {
                        String proteinText = todaysMeals.get("protein")
                                        .getAsString()
                                        .replaceAll("[^0-9]", "");

                        protein = Integer.parseInt(proteinText);

                } catch (Exception e) {
                        return "Keep a balanced diet with protein, carbs and vegetables.";
                }

                if (protein < 40) {
                        return "Your protein intake looks low today. Consider adding eggs, paneer or lentils.";
                }

                if (protein > 80) {
                        return "Great protein intake today. Add fruits and vegetables for balance.";
                }

                return "Your nutrition looks balanced. Keep drinking enough water throughout the day.";
        }
}