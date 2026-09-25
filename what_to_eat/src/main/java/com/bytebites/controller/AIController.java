package com.bytebites.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bytebites.config.ApiConfig;
import com.bytebites.dao.FamilyMemberDao;
import com.bytebites.dao.MealPlanDao;
import com.bytebites.model.FamilyMember;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIController {

        private FamilyMemberDao familyDao = new FamilyMemberDao();

        private MealPlanDao mealPlanDao = new MealPlanDao();

        public String generateWeeklyMealPlan(String userId) {

                try {

                        System.out.println("\n==================================");
                        System.out.println("Starting Meal Plan Generation");
                        System.out.println("User ID : " + userId);
                        System.out.println("==================================");

                        List<FamilyMember> members = familyDao.getFamilyMembersByUserId(userId);

                        System.out.println(
                                        "Family Members Found : "
                                                        + members.size());

                        if (members.isEmpty()) {

                                System.out.println(
                                                "No family members found.");

                                return "No family members found.";
                        }

                        StringBuilder familyInfo = new StringBuilder();

                        familyInfo.append(
                                        "Generate a Weekly Meal Plan for the entire family.\n\n");

                        familyInfo.append(
                                        "Family Members:\n");

                        for (FamilyMember member : members) {

                                familyInfo.append(
                                                "Name: ").append(member.getName())
                                                .append(", Age: ").append(member.getAge())
                                                .append(", Gender: ").append(member.getGender())
                                                .append(", BMI: ").append(member.getBmi())
                                                .append(", Goal: ").append(member.getGoal())
                                                .append(", Health Issue: ").append(member.getHealthIssue())
                                                .append(", Allergy: ").append(member.getAllergy())
                                                .append("\n");
                        }

                        familyInfo.append(
                                        """

                                                        Generate a complete 7-day Maharashtrian family meal plan
                                                        suitable for all the family members listed above.

                                                        IMPORTANT REQUIREMENTS:

                                                        1. Generate meals for exactly these seven days:
                                                           - MONDAY
                                                           - TUESDAY
                                                           - WEDNESDAY
                                                           - THURSDAY
                                                           - FRIDAY
                                                           - SATURDAY
                                                           - SUNDAY

                                                        2. For every day, generate exactly:
                                                           - breakfast
                                                           - lunch
                                                           - dinner

                                                        3. The meal plan must be mostly vegetarian:
                                                           - At least 18 out of the 21 meals must be vegetarian.
                                                           - Prefer dal, usal, sprouts, vegetables, paneer,
                                                             curd, buttermilk and other vegetarian protein sources.
                                                           - If dietary preference is uncertain, choose a vegetarian meal.
                                                           - Do not include beef, pork or seafood.
                                                           - A maximum of three meals may contain egg or chicken,
                                                             but only when suitable for every family member.
                                                           - Do not include egg or chicken when any listed allergy,
                                                             health condition or dietary restriction makes it unsuitable.

                                                        4. Prefer relatable Maharashtrian and Indian home-cooked meals.

                                                        Use breakfast dishes such as:
                                                           - Kanda poha
                                                           - Batata poha
                                                           - Vegetable upma
                                                           - Thalipeeth with curd
                                                           - Sabudana khichdi
                                                           - Ghavan with chutney
                                                           - Amboli
                                                           - Moong dal chilla
                                                           - Vegetable paratha
                                                           - Idli-sambar
                                                           - Misal with limited farsan
                                                           - Vegetable daliya
                                                           - Sprouts chaat

                                                        Use lunch and dinner combinations such as:
                                                           - Chapati, seasonal bhaji, varan and rice
                                                           - Pithla-bhakri with koshimbir
                                                           - Zunka-bhakri with salad
                                                           - Bharli vangi with chapati
                                                           - Matki usal with chapati
                                                           - Moong usal with bhakri
                                                           - Amti, rice and vegetable
                                                           - Masale bhaat with raita
                                                           - Vegetable khichdi with kadhi
                                                           - Palak paneer with chapati
                                                           - Shev bhaji with chapati
                                                           - Vegetable pulao with raita
                                                           - Dal-rice with koshimbir
                                                           - Varan-bhaat with vegetable
                                                           - Usal-pav with limited oil
                                                           - Bhakri with pithla and salad

                                                        5. These examples are guidance only.
                                                           Generate a varied plan instead of copying the same dishes
                                                           repeatedly.

                                                        6. Meals must be realistic for an ordinary Maharashtrian family
                                                           to prepare at home.

                                                        7. Use common and affordable ingredients generally available
                                                           in Maharashtra.

                                                        8. Avoid foreign, luxurious, highly processed or
                                                           difficult-to-find foods.

                                                        9. Consider every family member's:
                                                           - age
                                                           - gender
                                                           - BMI
                                                           - health goal
                                                           - health issue
                                                           - allergy

                                                        10. Never include any ingredient that conflicts with the allergy
                                                            or health condition of even one family member.

                                                        11. Adjust meals to support the family's health goals.
                                                            For example:
                                                            - For weight loss, prefer high-fibre and moderate-calorie meals.
                                                            - For weight gain, provide nutritious calorie-dense meals.
                                                            - For diabetes, reduce sugar and refined carbohydrates.
                                                            - For high blood pressure, limit salt and processed food.
                                                            - For high cholesterol, limit fried food and saturated fat.

                                                        12. Keep fried, sugary and highly processed foods limited.

                                                        13. Every meal should be balanced and should contain an
                                                            appropriate combination of:
                                                            - vegetables or fruit
                                                            - protein
                                                            - carbohydrates
                                                            - fibre

                                                        14. Avoid repeating the same main dish during the week.
                                                            Do not repeat the exact same breakfast, lunch or dinner.

                                                        15. Use clear and familiar meal names.
                                                            Mention the complete meal combination rather than only
                                                            writing generic names such as "healthy breakfast" or
                                                            "vegetable meal."

                                                        16. For every individual meal, provide realistic approximate:
                                                            - calories
                                                            - protein
                                                            - carbs

                                                        17. Nutrition values must belong only to that individual meal,
                                                            not to the entire day.

                                                        18. Every breakfast, lunch and dinner object must contain:
                                                            - meal
                                                            - calories
                                                            - protein
                                                            - carbs

                                                        19. Use exactly this JSON structure:

                                                        {
                                                          "MONDAY": {
                                                            "breakfast": {
                                                              "meal": "Kanda Poha with Sprouts",
                                                              "calories": "320 kcal",
                                                              "protein": "10 g",
                                                              "carbs": "52 g"
                                                            },
                                                            "lunch": {
                                                              "meal": "Chapati, Matki Usal, Koshimbir and Buttermilk",
                                                              "calories": "610 kcal",
                                                              "protein": "22 g",
                                                              "carbs": "82 g"
                                                            },
                                                            "dinner": {
                                                              "meal": "Vegetable Khichdi with Kadhi",
                                                              "calories": "540 kcal",
                                                              "protein": "18 g",
                                                              "carbs": "76 g"
                                                            }
                                                          },
                                                          "TUESDAY": {
                                                            "breakfast": {
                                                              "meal": "Thalipeeth with Curd",
                                                              "calories": "350 kcal",
                                                              "protein": "12 g",
                                                              "carbs": "48 g"
                                                            },
                                                            "lunch": {
                                                              "meal": "Jowar Bhakri, Bharli Vangi and Koshimbir",
                                                              "calories": "590 kcal",
                                                              "protein": "17 g",
                                                              "carbs": "79 g"
                                                            },
                                                            "dinner": {
                                                              "meal": "Chapati, Moong Usal and Salad",
                                                              "calories": "520 kcal",
                                                              "protein": "21 g",
                                                              "carbs": "68 g"
                                                            }
                                                          }
                                                        }

                                                        20. The final JSON must contain all seven days from MONDAY
                                                            through SUNDAY.

                                                        21. Return only one valid JSON object.

                                                        22. Do not include:
                                                            - markdown
                                                            - code blocks
                                                            - explanations
                                                            - introductory text
                                                            - concluding text
                                                            - a separate nutrition object
                                                            - text outside the JSON object
                                                        """);

                        System.out.println(
                                        "Prompt Created Successfully");

                        System.out.println(
                                        "Calling API...");

                        String mealPlan = callAI(familyInfo.toString());
                        if (mealPlan == null || mealPlan.isBlank()) {

                                System.out.println(
                                                "Meal Plan was not generated by Gemini.");

                                return null;
                        }

                        System.out.println(
                                        "Meal Plan Successfully Received");

                        String weekStartDate = java.time.LocalDate.now()
                                        .with(java.time.DayOfWeek.MONDAY)
                                        .toString();

                        // Save to Firebase
                        String insight = generateWeeklyInsight(mealPlan);

                        mealPlanDao.saveMealPlan(
                                        userId,
                                        weekStartDate,
                                        mealPlan,
                                        insight);
                        System.out.println(
                                        "Meal Plan Saved To Firebase");

                        return mealPlan;

                } catch (Exception e) {

                        System.out.println(
                                        "Meal Plan Generation Failed");

                        e.printStackTrace();

                        return null;
                }
        }

        private String callAI(String prompt) throws IOException {

                OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(30, TimeUnit.SECONDS)
                                .writeTimeout(30, TimeUnit.SECONDS)
                                .readTimeout(300, TimeUnit.SECONDS)
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
                                MediaType.parse("application/json"));

                Request request = new Request.Builder()
                                .url(ApiConfig.API_URL)
                                .addHeader(
                                                "Authorization",
                                                "Bearer " + ApiConfig.API_KEY)
                                .addHeader(
                                                "Content-Type",
                                                "application/json")
                                .post(requestBody)
                                .build();

                System.out.println("Sending Request AI...");

                long start = System.currentTimeMillis();

                try (Response response = client.newCall(request).execute()) {

                        long end = System.currentTimeMillis();

                        System.out.println(
                                        "Response Time : " + (end - start) + " ms");

                        System.out.println(
                                        "HTTP Status : " + response.code());

                        if (response.body() == null) {
                                System.out.println("Response Body Is Null");
                                return null;
                        }

                        String json = response.body().string();

                        System.out.println("JSON Received");

                        if (!response.isSuccessful()) {

                                System.out.println("AI Error Response:");
                                System.out.println(json);

                                return null;
                        }

                        JsonObject root = new Gson().fromJson(
                                        json,
                                        JsonObject.class);

                        if (!root.has("choices")
                                        || root.getAsJsonArray("choices").isEmpty()) {

                                System.out.println("No choices returned.");

                                System.out.println(json);

                                return null;
                        }

                        String mealPlan = root.getAsJsonArray("choices")
                                        .get(0)
                                        .getAsJsonObject()
                                        .getAsJsonObject("message")
                                        .get("content")
                                        .getAsString();

                        System.out.println(
                                        "Meal Plan Extracted Successfully");
                        System.out.println("\n========== GEMINI RESPONSE ==========");
                        System.out.println(mealPlan);
                        System.out.println("=====================================\n");

                        return mealPlan;
                }
        }

        public String getSavedMealPlan(String userId) {

                return mealPlanDao.getMealPlan(userId);
        }

        public String getSavedInsight(String userId) {

                return mealPlanDao.getInsight(userId);
        }

        public String generateWeeklyInsight(String mealPlan) {

                try {

                        String prompt = """
                                        Analyze this weekly meal plan.

                                        Give a short 2-3 sentence nutrition summary.

                                        Mention:

                                        - protein balance
                                        - carbs balance
                                        - healthy eating suggestion

                                        Return plain text only.

                                        Meal Plan:

                                        """ + mealPlan;

                        return callAI(prompt);

                } catch (Exception e) {

                        e.printStackTrace();
                        return "Unable to generate insights.";
                }
        }

        public String generateMealPlanForDietitian(
                        String familyUserId) {

                try {

                        List<FamilyMember> members = familyDao.getFamilyMembersByUserId(
                                        familyUserId);

                        if (members.isEmpty()) {
                                return null;
                        }

                        StringBuilder familyInfo = new StringBuilder();

                        familyInfo.append(
                                        "Generate a Weekly Meal Plan for the entire family.\n\n");

                        familyInfo.append(
                                        "Family Members:\n");

                        for (FamilyMember member : members) {

                                familyInfo.append(
                                                "Name: ").append(member.getName())
                                                .append(", Age: ").append(member.getAge())
                                                .append(", Gender: ").append(member.getGender())
                                                .append(", BMI: ").append(member.getBmi())
                                                .append(", Goal: ").append(member.getGoal())
                                                .append(", Health Issue: ").append(member.getHealthIssue())
                                                .append(", Allergy: ").append(member.getAllergy())
                                                .append("\n");
                        }

                        familyInfo.append(
                                       """
Generate a complete 7-day Maharashtrian family meal plan suitable
for all the family members listed above.

IMPORTANT REQUIREMENTS:

1. Generate meals for exactly these seven days:
   - MONDAY
   - TUESDAY
   - WEDNESDAY
   - THURSDAY
   - FRIDAY
   - SATURDAY
   - SUNDAY

2. For every day generate:
   - breakfast
   - lunch
   - dinner

3. The plan must be mostly vegetarian:
   - At least 18 of the 21 meals must be vegetarian.
   - Prefer dal, usal, sprouts, vegetables, paneer, curd,
     buttermilk and other vegetarian protein sources.
   - If dietary preference is uncertain, choose vegetarian.
   - Do not include beef, pork or seafood.
   - A maximum of three meals may contain egg or chicken.
   - Use egg or chicken only when suitable for every family member.
   - If any allergy or health condition makes it unsuitable,
     generate a vegetarian meal instead.

4. Prefer relatable Maharashtrian and Indian home-cooked meals.

Suitable breakfast options include:
   - Kanda poha
   - Batata poha
   - Vegetable upma
   - Thalipeeth with curd
   - Sabudana khichdi
   - Ghavan with chutney
   - Amboli
   - Moong dal chilla
   - Vegetable paratha
   - Idli-sambar
   - Misal with limited farsan
   - Vegetable daliya
   - Sprouts chaat

Suitable lunch and dinner combinations include:
   - Chapati, seasonal bhaji, varan and rice
   - Pithla-bhakri with koshimbir
   - Zunka-bhakri with salad
   - Bharli vangi with chapati
   - Matki usal with chapati
   - Moong usal with bhakri
   - Amti, rice and vegetable
   - Masale bhaat with raita
   - Vegetable khichdi with kadhi
   - Palak paneer with chapati
   - Shev bhaji with chapati
   - Vegetable pulao with raita
   - Dal-rice with koshimbir
   - Varan-bhaat with vegetable
   - Usal-pav prepared with limited oil
   - Bhakri with pithla and salad

5. These examples are only guidance. Generate a varied plan and
   do not copy or repeat the same meals frequently.

6. Meals must be realistic for an ordinary Maharashtrian family
   to prepare at home.

7. Use common and affordable ingredients available in Maharashtra.

8. Avoid foreign, luxurious, highly processed and
   difficult-to-find foods.

9. Consider every family member's:
   - age
   - gender
   - BMI
   - health goal
   - health issue
   - allergy

10. Never include an ingredient that conflicts with the allergy
    or health condition of any family member.

11. Adjust meals according to health requirements:
    - For weight loss, prefer high-fibre and moderate-calorie meals.
    - For weight gain, use nutritious calorie-dense meals.
    - For diabetes, reduce sugar and refined carbohydrates.
    - For high blood pressure, limit salt and processed food.
    - For high cholesterol, limit fried food and saturated fat.

12. Every meal should contain an appropriate balance of:
    - vegetables or fruit
    - protein
    - carbohydrates
    - fibre

13. Keep fried, sugary and highly processed foods limited.

14. Do not repeat the same main dish during the week.

15. Use clear and familiar meal names. Mention the complete meal
    combination instead of generic names such as
    "healthy breakfast" or "vegetable meal."

16. For every meal provide realistic approximate:
    - calories
    - protein
    - carbs

17. Nutrition values must belong only to that individual meal,
    not to the entire day.

18. Every breakfast, lunch and dinner object must contain:
    - meal
    - calories
    - protein
    - carbs

19. Use exactly this JSON structure:

{
  "MONDAY": {
    "breakfast": {
      "meal": "Kanda Poha with Sprouts",
      "calories": "320 kcal",
      "protein": "10 g",
      "carbs": "52 g"
    },
    "lunch": {
      "meal": "Chapati, Matki Usal, Koshimbir and Buttermilk",
      "calories": "610 kcal",
      "protein": "22 g",
      "carbs": "82 g"
    },
    "dinner": {
      "meal": "Vegetable Khichdi with Kadhi",
      "calories": "540 kcal",
      "protein": "18 g",
      "carbs": "76 g"
    }
  },
  "TUESDAY": {
    "breakfast": {
      "meal": "Thalipeeth with Curd",
      "calories": "350 kcal",
      "protein": "12 g",
      "carbs": "48 g"
    },
    "lunch": {
      "meal": "Jowar Bhakri, Bharli Vangi and Koshimbir",
      "calories": "590 kcal",
      "protein": "17 g",
      "carbs": "79 g"
    },
    "dinner": {
      "meal": "Chapati, Moong Usal and Salad",
      "calories": "520 kcal",
      "protein": "21 g",
      "carbs": "68 g"
    }
  }
}

20. Follow the same structure for WEDNESDAY, THURSDAY, FRIDAY,
    SATURDAY and SUNDAY.

21. The final JSON must contain all seven days from MONDAY
    through SUNDAY.

22. Return only one valid JSON object.

23. Do not include:
    - markdown
    - code blocks
    - explanations
    - introductory text
    - concluding text
    - a separate nutrition object
    - any text outside the JSON object
""");

                        return callAI(
                                        familyInfo.toString());

                } catch (Exception e) {

                        e.printStackTrace();

                        return null;
                }
        }

}