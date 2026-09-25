package com.bytebites.dao;

import com.bytebites.config.FirebaseConfig;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PantryGroceryDao {

    private final Firestore db = FirebaseConfig.getFirestore();

    // =====================================================
    // ADD PANTRY ITEM
    // =====================================================

    public String addPantryItem(
            String uid,
            String itemName,
            String quantity
    ) {

        try {

            Map<String, Object> item = new HashMap<>();

            item.put("itemName", itemName);
            item.put("quantity", quantity);

            var documentReference = db
                    .collection("Users")
                    .document(uid)
                    .collection("pantryItems")
                    .document();

            documentReference
                    .set(item)
                    .get();

            System.out.println(
                    "Pantry item saved successfully"
            );

            return documentReference.getId();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET PANTRY ITEMS
    // =====================================================

    public List<Map<String, Object>> getPantryItems(
            String uid
    ) {

        List<Map<String, Object>> items =
                new ArrayList<>();

        try {

            List<QueryDocumentSnapshot> documents =
                    db.collection("Users")
                      .document(uid)
                      .collection("pantryItems")
                      .get()
                      .get()
                      .getDocuments();

            for (QueryDocumentSnapshot document :
                    documents) {

                Map<String, Object> item =
                        new HashMap<>(
                                document.getData()
                        );

                item.put(
                        "id",
                        document.getId()
                );

                items.add(item);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return items;
    }

    // =====================================================
    // UPDATE PANTRY ITEM
    // =====================================================

    public boolean updatePantryItem(
            String uid,
            String itemId,
            String newQuantity
    ) {

        try {

            db.collection("Users")
              .document(uid)
              .collection("pantryItems")
              .document(itemId)
              .update(
                      "quantity",
                      newQuantity
              )
              .get();

            System.out.println(
                    "Pantry quantity updated"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE PANTRY ITEM
    // =====================================================

    public boolean deletePantryItem(
            String uid,
            String itemId
    ) {

        try {

            db.collection("Users")
              .document(uid)
              .collection("pantryItems")
              .document(itemId)
              .delete()
              .get();

            System.out.println(
                    "Pantry item deleted"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // ADD GROCERY ITEM
    // =====================================================

    public String addGroceryItem(
            String uid,
            String itemName,
            String quantity
    ) {

        try {

            Map<String, Object> item =
                    new HashMap<>();

            item.put("itemName", itemName);
            item.put("quantity", quantity);

            var documentReference = db
                    .collection("Users")
                    .document(uid)
                    .collection("groceryItems")
                    .document();

            documentReference
                    .set(item)
                    .get();

            System.out.println(
                    "Grocery item saved successfully"
            );

            return documentReference.getId();

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET GROCERY ITEMS
    // =====================================================

    public List<Map<String, Object>> getGroceryItems(
            String uid
    ) {

        List<Map<String, Object>> items =
                new ArrayList<>();

        try {

            List<QueryDocumentSnapshot> documents =
                    db.collection("Users")
                      .document(uid)
                      .collection("groceryItems")
                      .get()
                      .get()
                      .getDocuments();

            for (QueryDocumentSnapshot document :
                    documents) {

                Map<String, Object> item =
                        new HashMap<>(
                                document.getData()
                        );

                item.put(
                        "id",
                        document.getId()
                );

                items.add(item);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return items;
    }

    // =====================================================
    // UPDATE GROCERY ITEM
    // =====================================================

    public boolean updateGroceryItem(
            String uid,
            String itemId,
            String newQuantity
    ) {

        try {

            db.collection("Users")
              .document(uid)
              .collection("groceryItems")
              .document(itemId)
              .update(
                      "quantity",
                      newQuantity
              )
              .get();

            System.out.println(
                    "Grocery quantity updated"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE GROCERY ITEM
    // =====================================================

    public boolean deleteGroceryItem(
            String uid,
            String itemId
    ) {

        try {

            db.collection("Users")
              .document(uid)
              .collection("groceryItems")
              .document(itemId)
              .delete()
              .get();

            System.out.println(
                    "Grocery item deleted"
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
// GET GROCERY ITEM COUNT
// =====================================================

public int getGroceryItemCount(String uid) {

    if (uid == null || uid.trim().isEmpty()) {
        return 0;
    }

    try {

        return db.collection("Users")
                .document(uid)
                .collection("groceryItems")
                .get()
                .get()
                .size();

    } catch (Exception e) {

        e.printStackTrace();

        return 0;
    }
}

}