package com.bytebites.view.user.userFeatures;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.bytebites.dao.PantryGroceryDao;
import com.bytebites.model.session.SessionManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import com.bytebites.view.common.SameStageDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class UserPantryGrocery {

    private final PantryGroceryDao dao = new PantryGroceryDao();

    public VBox getPantryGrocery() {

        VBox mainBox = new VBox(15);
        mainBox.setPadding(new Insets(5));
        mainBox.setStyle("-fx-background-color: #0F172A;");

        Label title = new Label("🧺  Pantry & Grocery");
        title.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Manage your items and turn them into delicious recipes"
        );
        subtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 13px;"
        );

        VBox titleBox = new VBox(4, title, subtitle);

        // =====================================================
        // PANTRY BOX
        // =====================================================

        VBox pantryBox = new VBox(10);
        pantryBox.setPadding(new Insets(15));
        pantryBox.setStyle(
                "-fx-background-color: #162033;" +
                "-fx-border-color: #22C55E;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;"
        );

        Label pantryTitle = new Label("🫙  PANTRY");
        pantryTitle.setStyle(
                "-fx-text-fill: #22C55E;" +
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;"
        );

        Label pantrySubtitle = new Label(
                "Items you already have at home."
        );
        pantrySubtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 12px;"
        );

        HBox pantrySearchRow = new HBox(8);

        TextField pantrySearch = new TextField();
        pantrySearch.setPromptText("Search pantry items...");
        pantrySearch.setPrefHeight(36);
        pantrySearch.setStyle(
                "-fx-background-color: #0F172A;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #64748B;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;"
        );

        Button pantryAdd = new Button("+ Add Item");
        pantryAdd.setPrefHeight(36);
        pantryAdd.setStyle(
                "-fx-background-color: #22C55E;" +
                "-fx-text-fill: #0F172A;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;"
        );

        pantrySearchRow.getChildren().addAll(pantrySearch, pantryAdd);
        HBox.setHgrow(pantrySearch, Priority.ALWAYS);

        VBox pantryItems = new VBox(5);

        Label pantryTotal = new Label("Total Items: 0");
        pantryTotal.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 12px;"
        );

        loadPantryItems(pantryItems, pantryTotal);

        pantryAdd.setOnAction(e ->
                showAddPantryItemDialog(pantryItems, pantryTotal)
        );

        pantryBox.getChildren().addAll(
                pantryTitle,
                pantrySubtitle,
                pantrySearchRow,
                pantryItems,
                pantryTotal
        );

        // =====================================================
        // GROCERY BOX
        // =====================================================

        VBox groceryBox = new VBox(10);
        groceryBox.setPadding(new Insets(15));
        groceryBox.setStyle(
                "-fx-background-color: #162033;" +
                "-fx-border-color: #F68F32;" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 10px;" +
                "-fx-background-radius: 10px;"
        );

        Label groceryTitle = new Label("🛒  GROCERY LIST");
        groceryTitle.setStyle(
                "-fx-text-fill: #F68F32;" +
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;"
        );

        Region groceryTitleSpacer = new Region();
        HBox.setHgrow(groceryTitleSpacer, Priority.ALWAYS);

        Button orderBlinkit = new Button("🛍  Order On Blinkit");
        orderBlinkit.setPrefHeight(36);
        orderBlinkit.setStyle(
                "-fx-background-color: #1976D2;" +
                "-fx-text-fill: #0F172A;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;"
        );

        orderBlinkit.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(
                        new URI("https://blinkit.com/")
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox groceryTitleRow = new HBox(
                10,
                groceryTitle,
                groceryTitleSpacer,
                orderBlinkit
        );
        groceryTitleRow.setAlignment(Pos.CENTER_LEFT);

        Label grocerySubtitle = new Label("Items you need to buy.");
        grocerySubtitle.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 12px;"
        );

        HBox grocerySearchRow = new HBox(8);

        TextField grocerySearch = new TextField();
        grocerySearch.setPromptText("Search grocery items...");
        grocerySearch.setPrefHeight(36);
        grocerySearch.setStyle(
                "-fx-background-color: #0F172A;" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #64748B;" +
                "-fx-border-color: #334155;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;"
        );

        Button groceryAdd = new Button("+ Add Item");
        groceryAdd.setPrefHeight(36);
        groceryAdd.setStyle(
                "-fx-background-color: #F68F32;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 7;"
        );

        grocerySearchRow.getChildren().addAll(
                grocerySearch,
                groceryAdd
        );
        HBox.setHgrow(grocerySearch, Priority.ALWAYS);

        VBox groceryItems = new VBox(5);

        Label groceryTotal = new Label("Total Items: 0");
        groceryTotal.setStyle(
                "-fx-text-fill: #94A3B8;" +
                "-fx-font-size: 12px;"
        );

        loadGroceryItems(groceryItems, groceryTotal);

        groceryAdd.setOnAction(e ->
                showAddGroceryItemDialog(groceryItems, groceryTotal)
        );

        groceryBox.getChildren().addAll(
                groceryTitleRow,
                grocerySubtitle,
                grocerySearchRow,
                groceryItems,
                groceryTotal
        );

        // =====================================================
        // SEARCH
        // =====================================================

        pantrySearch.textProperty().addListener((obs, oldValue, newValue) -> {
            filterItems(pantryItems, newValue);
        });

        grocerySearch.textProperty().addListener((obs, oldValue, newValue) -> {
            filterItems(groceryItems, newValue);
        });

        HBox cards = new HBox(15, pantryBox, groceryBox);

        HBox.setHgrow(pantryBox, Priority.ALWAYS);
        HBox.setHgrow(groceryBox, Priority.ALWAYS);

        mainBox.getChildren().addAll(titleBox, cards);

        ScrollPane scrollPane = new ScrollPane(mainBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background-color: #0F172A;" +
                "-fx-background: #0F172A;"
        );

        VBox scrollContainer = new VBox(scrollPane);
        scrollContainer.setFillWidth(true);
        scrollContainer.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return scrollContainer;
    }

    // =====================================================
    // LOAD PANTRY ITEMS FROM FIREBASE
    // =====================================================

    private void loadPantryItems(
            VBox pantryItems,
            Label pantryTotal
    ) {

        String uid = SessionManager.getUid();

        if (uid == null || uid.trim().isEmpty()) {
            pantryTotal.setText("Total Items: 0");
            return;
        }

        List<Map<String, Object>> items = dao.getPantryItems(uid);

        for (Map<String, Object> item : items) {

            String id = String.valueOf(item.get("id"));
            String name = String.valueOf(item.get("itemName"));
            String quantity = String.valueOf(item.get("quantity"));

            pantryItems.getChildren().add(
                    createPantryRow(
                            id,
                            name,
                            quantity,
                            pantryItems,
                            pantryTotal
                    )
            );
        }

        updateTotal(pantryItems, pantryTotal);
    }

    // =====================================================
    // LOAD GROCERY ITEMS FROM FIREBASE
    // =====================================================

    private void loadGroceryItems(
            VBox groceryItems,
            Label groceryTotal
    ) {

        String uid = SessionManager.getUid();

        if (uid == null || uid.trim().isEmpty()) {
            groceryTotal.setText("Total Items: 0");
            return;
        }

        List<Map<String, Object>> items = dao.getGroceryItems(uid);

        for (Map<String, Object> item : items) {

            String id = String.valueOf(item.get("id"));
            String name = String.valueOf(item.get("itemName"));
            String quantity = String.valueOf(item.get("quantity"));

            groceryItems.getChildren().add(
                    createGroceryRow(
                            id,
                            name,
                            quantity,
                            groceryItems,
                            groceryTotal
                    )
            );
        }

        updateTotal(groceryItems, groceryTotal);
    }

    // =====================================================
    // PANTRY ROW
    // =====================================================

    private HBox createPantryRow(
            String itemId,
            String item,
            String quantity,
            VBox parent,
            Label totalLabel
    ) {

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(7));
        row.setStyle(
                "-fx-background-color: #101A2D;" +
                "-fx-background-radius: 5;"
        );

        CheckBox checkBox = new CheckBox();
        checkBox.setTextFill(Color.WHITE);
        checkBox.setStyle("-fx-font-size: 15px;");

        Label itemLabel = new Label("📦  " + item);
        itemLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label quantityLabel = new Label(quantity);
        quantityLabel.setStyle(
                "-fx-text-fill: #fefbfb;" +
                "-fx-font-size: 13px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label edit = new Label("✎");
        edit.setStyle(
                "-fx-text-fill: #fefbfb;" +
                "-fx-font-size: 18px;" +
                "-fx-cursor: hand;"
        );

       edit.setOnMouseClicked(e ->
        editGroceryQuantity(
                itemId,
                item,
                quantityLabel,
                parent
        )
);
        Label delete = new Label("🗑");
        delete.setStyle(
                "-fx-text-fill: #ff0808;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;"
        );

        delete.setOnMouseClicked(e -> {

            String uid = SessionManager.getUid();

            if (uid == null || uid.trim().isEmpty()) {
                return;
            }

            if (dao.deletePantryItem(uid, itemId)) {
                parent.getChildren().remove(row);
                updateTotal(parent, totalLabel);
            }
        });

        row.getChildren().addAll(
                checkBox,
                itemLabel,
                spacer,
                quantityLabel,
                edit,
                delete
        );

        return row;
    }

    // =====================================================
    // EDIT PANTRY QUANTITY
    // =====================================================

   private void editPantryQuantity(
        String itemId,
        String item,
        Label quantityLabel,
        VBox parent
) {

    SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

    if (parent.getScene() != null) {
        dialog.initOwner(parent.getScene().getWindow());
    }

        dialog.setTitle("Edit Quantity");
        dialog.setHeaderText("Change quantity for " + item);

        Label quantityTitle = new Label("Quantity");
        quantityTitle.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField quantityField = new TextField(
                quantityLabel.getText()
        );
        quantityField.setPromptText("Example: 4 kg");
        quantityField.setPrefWidth(250);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(
                quantityTitle,
                quantityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                String newQuantity =
                        quantityField.getText().trim();

                if (!newQuantity.isEmpty()) {

                    String uid = SessionManager.getUid();

                    if (uid != null &&
                            dao.updatePantryItem(
                                    uid,
                                    itemId,
                                    newQuantity
                            )) {

                        quantityLabel.setText(newQuantity);
                    }
                }
            }
        });
    }

    // =====================================================
    // ADD PANTRY ITEM
    // =====================================================

    private void showAddPantryItemDialog(
            VBox pantryItems,
            Label pantryTotal
    ) {

       SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

if (pantryItems.getScene() != null) {
    dialog.initOwner(pantryItems.getScene().getWindow());
}

dialog.setTitle("Add Pantry Item");

        dialog.setTitle("Add Pantry Item");
        dialog.setHeaderText(
                "Add a new item to your Pantry"
        );

        Label itemLabel = new Label("Item Name");
        itemLabel.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField itemField = new TextField();
        itemField.setPromptText("Enter item name");
        itemField.setPrefWidth(280);

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField quantityField = new TextField();
        quantityField.setPromptText("Example: 2 kg");
        quantityField.setPrefWidth(280);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(
                itemLabel,
                itemField,
                quantityLabel,
                quantityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                String item = itemField.getText().trim();
                String quantity = quantityField.getText().trim();

                if (item.isEmpty()) {
                    return;
                }

                if (quantity.isEmpty()) {
                    quantity = "1";
                }

                String uid = SessionManager.getUid();

                if (uid == null || uid.trim().isEmpty()) {
                    return;
                }

                String itemId =
                        dao.addPantryItem(
                                uid,
                                item,
                                quantity
                        );

                if (itemId != null) {

                    pantryItems.getChildren().add(
                            createPantryRow(
                                    itemId,
                                    item,
                                    quantity,
                                    pantryItems,
                                    pantryTotal
                            )
                    );

                    updateTotal(
                            pantryItems,
                            pantryTotal
                    );
                }
            }
        });
    }

    // =====================================================
    // GROCERY ROW
    // =====================================================

    private HBox createGroceryRow(
            String itemId,
            String item,
            String quantity,
            VBox parent,
            Label totalLabel
    ) {

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(7));
        row.setStyle(
                "-fx-background-color: #101A2D;" +
                "-fx-background-radius: 5;"
        );

        CheckBox checkBox = new CheckBox();

        Label itemLabel = new Label("📦  " + item);
        itemLabel.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;"
        );

        Label quantityLabel = new Label(quantity);
        quantityLabel.setStyle(
                "-fx-text-fill: #fefbfb;" +
                "-fx-font-size: 13px;"
        );

        Label status = new Label("To Buy");
        status.setStyle(
                "-fx-background-color: #5a2b39;" +
                "-fx-text-fill: #db6969;" +
                "-fx-padding: 4 8;" +
                "-fx-background-radius: 5;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label edit = new Label("✎");
        edit.setStyle(
                "-fx-text-fill: #fefbfb;" +
                "-fx-font-size: 18px;" +
                "-fx-cursor: hand;"
        );

       edit.setOnMouseClicked(e ->
        editPantryQuantity(
                itemId,
                item,
                quantityLabel,
                parent
        )
);

        Label delete = new Label("🗑");
        delete.setStyle(
                "-fx-text-fill: #ff0808;" +
                "-fx-font-size: 20px;" +
                "-fx-cursor: hand;"
        );

        delete.setOnMouseClicked(e -> {

            String uid = SessionManager.getUid();

            if (uid == null || uid.trim().isEmpty()) {
                return;
            }

            if (dao.deleteGroceryItem(uid, itemId)) {
                parent.getChildren().remove(row);
                updateTotal(parent, totalLabel);
            }
        });

        row.getChildren().addAll(
                checkBox,
                itemLabel,
                spacer,
                quantityLabel,
                status,
                edit,
                delete
        );

        return row;
    }

    // =====================================================
    // EDIT GROCERY QUANTITY
    // =====================================================

    private void editGroceryQuantity(
        String itemId,
        String item,
        Label quantityLabel,
        VBox parent
) {

    SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

    if (parent.getScene() != null) {
        dialog.initOwner(parent.getScene().getWindow());
    }

        dialog.setTitle("Edit Quantity");
        dialog.setHeaderText("Change quantity for " + item);

        Label quantityTitle = new Label("Quantity");
        quantityTitle.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField quantityField = new TextField(
                quantityLabel.getText()
        );
        quantityField.setPromptText("Example: 4 kg");
        quantityField.setPrefWidth(250);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(
                quantityTitle,
                quantityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                String newQuantity =
                        quantityField.getText().trim();

                if (!newQuantity.isEmpty()) {

                    String uid = SessionManager.getUid();

                    if (uid != null &&
                            dao.updateGroceryItem(
                                    uid,
                                    itemId,
                                    newQuantity
                            )) {

                        quantityLabel.setText(newQuantity);
                    }
                }
            }
        });
    }

    // =====================================================
    // ADD GROCERY ITEM
    // =====================================================

    private void showAddGroceryItemDialog(
            VBox groceryItems,
            Label groceryTotal
    ) {

       SameStageDialog<ButtonType> dialog = new SameStageDialog<>();

if (groceryItems.getScene() != null) {
    dialog.initOwner(groceryItems.getScene().getWindow());
}

dialog.setTitle("Add Grocery Item");
        dialog.setTitle("Add Grocery Item");
        dialog.setHeaderText(
                "Add a new item to your Grocery List"
        );

        Label itemLabel = new Label("Item Name");
        itemLabel.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField itemField = new TextField();
        itemField.setPromptText("Enter item name");
        itemField.setPrefWidth(280);

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setStyle(
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #0F172A;"
        );

        TextField quantityField = new TextField();
        quantityField.setPromptText("Example: 2 kg");
        quantityField.setPrefWidth(280);

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(
                itemLabel,
                itemField,
                quantityLabel,
                quantityField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK,
                ButtonType.CANCEL
        );

        dialog.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {

                String item = itemField.getText().trim();
                String quantity = quantityField.getText().trim();

                if (item.isEmpty()) {
                    return;
                }

                if (quantity.isEmpty()) {
                    quantity = "1";
                }

                String uid = SessionManager.getUid();

                if (uid == null || uid.trim().isEmpty()) {
                    return;
                }

                String itemId =
                        dao.addGroceryItem(
                                uid,
                                item,
                                quantity
                        );

                if (itemId != null) {

                    groceryItems.getChildren().add(
                            createGroceryRow(
                                    itemId,
                                    item,
                                    quantity,
                                    groceryItems,
                                    groceryTotal
                            )
                    );

                    updateTotal(
                            groceryItems,
                            groceryTotal
                    );
                }
            }
        });
    }

    // =====================================================
    // TOTAL
    // =====================================================

    private void updateTotal(
            VBox items,
            Label totalLabel
    ) {
        totalLabel.setText(
                "Total Items: " +
                items.getChildren().size()
        );
    }

    // =====================================================
    // SEARCH FILTER
    // =====================================================

    private void filterItems(
            VBox items,
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText.trim().toLowerCase();

        for (javafx.scene.Node node : items.getChildren()) {

            if (!(node instanceof HBox)) {
                continue;
            }

            HBox row = (HBox) node;

            boolean found = false;

            for (javafx.scene.Node child :
                    row.getChildren()) {

                if (child instanceof Label) {

                    String text =
                            ((Label) child)
                                    .getText()
                                    .toLowerCase();

                    if (text.contains(search)) {
                        found = true;
                        break;
                    }
                }
            }

            row.setVisible(found);
            row.setManaged(found);
        }
    }
}
