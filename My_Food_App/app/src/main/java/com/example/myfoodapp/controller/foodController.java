package com.example.myfoodapp.controller; // Package controller mới

import android.content.Context;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper; // Import DatabaseHelper
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;
import com.example.myfoodapp.models.OrderItemModel;
import com.example.myfoodapp.models.OrderModel;
import com.example.myfoodapp.models.UserModel;

import java.util.ArrayList;
import java.util.List;


public class foodController {

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_RECEIVED = "Đã nhận";
    public static final String STATUS_COMPLETED = "Hoàn thành";
    public static final String STATUS_CANCELLED = "Hủy";

    private final Context context;
    private final DatabaseHelper dbHelper;

    public foodController(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DatabaseHelper(this.context);
    }


    // Hàm lấy  Categories
    public ArrayList<HomeHorModel> getAllCategories() {
        ArrayList<HomeHorModel> list = new ArrayList<>();


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CATEGORIES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CAT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CAT_NAME));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CAT_IMAGE));

                list.add(new HomeHorModel(id, image, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Hàm lấy Products theo Category ID
    // Lấy Products theo Category ID
    public ArrayList<HomeVerModel> getProductsByCategory(int categoryId) {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COL_PROD_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_ID)); // lấy ID
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                HomeVerModel product = new HomeVerModel(id, image, name, timing, rating, price);
                product.setCategoryId(categoryId);
                int fav = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"));
                product.setFavorite(fav == 1);

                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // SEARCH Products
    public ArrayList<HomeVerModel> searchProducts(String query) {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COL_PROD_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%"};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                HomeVerModel product = new HomeVerModel(id, image, name, timing, rating, price);
                int fav = cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite"));
                product.setFavorite(fav == 1);
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_CATEGORY_ID));
                product.setCategoryId(categoryId);

                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<HomeVerModel> getAllProducts() {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COL_PROD_ID + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_CATEGORY_ID));

                HomeVerModel product = new HomeVerModel(id, image, name, timing, rating, price);
                product.setCategoryId(categoryId);
                product.setFavorite(cursor.getInt(cursor.getColumnIndexOrThrow("is_favorite")) == 1);
                list.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    // Lấy danh sách món ăn yêu thích
    public ArrayList<HomeVerModel> getFavoriteFoods() {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Lọc những món có is_favorite = 1
        String selection = "is_favorite = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS,
                null,
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_ID));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                HomeVerModel product = new HomeVerModel(id, image, name, timing, rating, price);
                product.setFavorite(true); // chắc chắn là món yêu thích
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_CATEGORY_ID));
                product.setCategoryId(categoryId);
                list.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    // Thêm vào class foodController
    public void updateFavorite(int productId, int isFavorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_favorite", isFavorite); // Cột is_favorite trong bảng sản phẩm

        String whereClause = DatabaseHelper.COL_PROD_ID + " = ?";
        String[] whereArgs = {String.valueOf(productId)};

        db.update(DatabaseHelper.TABLE_PRODUCTS, values, whereClause, whereArgs);
        db.close();
    }

    public long addCategory(String name, int imageResId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CAT_NAME, name);
        values.put(DatabaseHelper.COL_CAT_IMAGE, imageResId);
        long id = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        db.close();
        return id;
    }

    public boolean updateCategory(HomeHorModel model) {
        if (model == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_CAT_NAME, model.getName());
        values.put(DatabaseHelper.COL_CAT_IMAGE, model.getImage());
        int rows = db.update(DatabaseHelper.TABLE_CATEGORIES,
                values,
                DatabaseHelper.COL_CAT_ID + " = ?",
                new String[]{String.valueOf(model.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COL_PROD_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
        int rows = db.delete(DatabaseHelper.TABLE_CATEGORIES,
                DatabaseHelper.COL_CAT_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
        db.close();
        return rows > 0;
    }

    public long addProduct(HomeVerModel model) {
        if (model == null) return -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PROD_NAME, model.getName());
        values.put(DatabaseHelper.COL_PROD_IMAGE, model.getImage());
        values.put(DatabaseHelper.COL_PROD_TIMING, model.getTiming());
        values.put(DatabaseHelper.COL_PROD_RATING, model.getRating());
        values.put(DatabaseHelper.COL_PROD_PRICE, model.getPrice());
        values.put(DatabaseHelper.COL_PROD_CATEGORY_ID, model.getCategoryId());
        long id = db.insert(DatabaseHelper.TABLE_PRODUCTS, null, values);
        db.close();
        return id;
    }

    public boolean updateProduct(HomeVerModel model) {
        if (model == null) return false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_PROD_NAME, model.getName());
        values.put(DatabaseHelper.COL_PROD_IMAGE, model.getImage());
        values.put(DatabaseHelper.COL_PROD_TIMING, model.getTiming());
        values.put(DatabaseHelper.COL_PROD_RATING, model.getRating());
        values.put(DatabaseHelper.COL_PROD_PRICE, model.getPrice());
        values.put(DatabaseHelper.COL_PROD_CATEGORY_ID, model.getCategoryId());
        int rows = db.update(DatabaseHelper.TABLE_PRODUCTS,
                values,
                DatabaseHelper.COL_PROD_ID + " = ?",
                new String[]{String.valueOf(model.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_PRODUCTS,
                DatabaseHelper.COL_PROD_ID + " = ?",
                new String[]{String.valueOf(productId)});
        db.close();
        return rows > 0;
    }

    public long placeOrder(int userId) {
        ArrayList<CartModel> cartItems = getCartItems();
        if (cartItems.isEmpty()) {
            return -1;
        }

        double total = 0;
        for (CartModel item : cartItems) {
            total += item.getTotalPrice();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        long orderId = -1;
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseHelper.COL_ORDER_USER_ID, userId);
            orderValues.put(DatabaseHelper.COL_ORDER_TOTAL, total);
            orderValues.put(DatabaseHelper.COL_ORDER_STATUS, STATUS_PENDING);
            orderValues.put(DatabaseHelper.COL_ORDER_CREATED_AT, System.currentTimeMillis());
            orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, orderValues);

            for (CartModel item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(DatabaseHelper.COL_ORDER_ITEM_ORDER_ID, orderId);
                itemValues.put(DatabaseHelper.COL_ORDER_ITEM_PRODUCT_ID, item.getProductId());
                itemValues.put(DatabaseHelper.COL_ORDER_ITEM_NAME, item.getName());
                itemValues.put(DatabaseHelper.COL_ORDER_ITEM_PRICE, item.getUnitPrice());
                itemValues.put(DatabaseHelper.COL_ORDER_ITEM_QUANTITY, item.getQuantity());
                db.insert(DatabaseHelper.TABLE_ORDER_ITEMS, null, itemValues);
            }

            db.delete(DatabaseHelper.TABLE_CART, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return orderId;
    }

    public ArrayList<OrderModel> getOrdersByUser(int userId) {
        String selection = "o." + DatabaseHelper.COL_ORDER_USER_ID + " = ?";
        String[] args = {String.valueOf(userId)};
        return queryOrders(selection, args);
    }

    public ArrayList<OrderModel> getAllOrders() {
        return queryOrders(null, null);
    }

    private ArrayList<OrderModel> queryOrders(String selection, String[] args) {
        ArrayList<OrderModel> orders = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT o." + DatabaseHelper.COL_ORDER_ID + ", " +
                "o." + DatabaseHelper.COL_ORDER_USER_ID + ", " +
                "o." + DatabaseHelper.COL_ORDER_TOTAL + ", " +
                "o." + DatabaseHelper.COL_ORDER_STATUS + ", " +
                "o." + DatabaseHelper.COL_ORDER_CREATED_AT + ", " +
                "u.name AS user_name " +
                "FROM " + DatabaseHelper.TABLE_ORDERS + " o " +
                "LEFT JOIN " + DatabaseHelper.TABLE_USERS + " u ON o." + DatabaseHelper.COL_ORDER_USER_ID + " = u.id";

        if (selection != null) {
            query += " WHERE " + selection;
        }

        query += " ORDER BY o." + DatabaseHelper.COL_ORDER_ID + " DESC";

        Cursor cursor = db.rawQuery(query, args);
        if (cursor.moveToFirst()) {
            do {
                orders.add(mapOrder(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orders;
    }

    private OrderModel mapOrder(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID));
        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_USER_ID));
        double total = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_TOTAL));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_STATUS));
        long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_CREATED_AT));
        String userName = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
        return new OrderModel(id, userId, userName, total, status, createdAt);
    }

    public ArrayList<OrderItemModel> getOrderItems(int orderId) {
        ArrayList<OrderItemModel> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COL_ORDER_ITEM_ORDER_ID + " = ?";
        String[] args = {String.valueOf(orderId)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER_ITEMS,
                null,
                selection,
                args,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEM_QUANTITY));

                items.add(new OrderItemModel(id, orderId, productId, name, price, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public void updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ORDER_STATUS, status);
        db.update(DatabaseHelper.TABLE_ORDERS,
                values,
                DatabaseHelper.COL_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        db.close();
    }

    // ---------- CART ----------

    public void addProductToCart(HomeVerModel product) {
        if (product == null) return;

        double unitPrice = extractUnitPrice(product.getPrice());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        Cursor cursor = null;
        try {
            String selection = DatabaseHelper.COL_CART_PRODUCT_ID + " = ?";
            String[] selectionArgs = {String.valueOf(product.getId())};
            cursor = db.query(DatabaseHelper.TABLE_CART,
                    new String[]{DatabaseHelper.COL_CART_ID, DatabaseHelper.COL_CART_QUANTITY},
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            if (cursor.moveToFirst()) {
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID));
                int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_QUANTITY));

                ContentValues updateValues = new ContentValues();
                updateValues.put(DatabaseHelper.COL_CART_QUANTITY, currentQty + 1);

                db.update(DatabaseHelper.TABLE_CART,
                        updateValues,
                        DatabaseHelper.COL_CART_ID + " = ?",
                        new String[]{String.valueOf(cartId)});
            } else {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_CART_PRODUCT_ID, product.getId());
                values.put(DatabaseHelper.COL_CART_NAME, product.getName());
                values.put(DatabaseHelper.COL_CART_IMAGE, product.getImage());
                values.put(DatabaseHelper.COL_CART_RATING, product.getRating());
                values.put(DatabaseHelper.COL_CART_PRICE_TEXT, product.getPrice());
                values.put(DatabaseHelper.COL_CART_UNIT_PRICE, unitPrice);
                values.put(DatabaseHelper.COL_CART_QUANTITY, 1);

                db.insert(DatabaseHelper.TABLE_CART, null, values);
            }

            db.setTransactionSuccessful();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<CartModel> getCartItems() {
        ArrayList<CartModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_CART,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_NAME));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_IMAGE));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_RATING));
                String priceDisplay = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_PRICE_TEXT));
                double unitPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_UNIT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CART_QUANTITY));

                list.add(new CartModel(id, productId, image, name, priceDisplay, rating, quantity, unitPrice));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return list;
    }

    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART, null, null);
        db.close();
    }

    public void deleteCartItem(int cartItemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART,
                DatabaseHelper.COL_CART_ID + " = ?",
                new String[]{String.valueOf(cartItemId)});
        db.close();
    }

    private double extractUnitPrice(String priceText) {
        if (priceText == null) return 0;
        String sanitized = priceText.replaceAll("[^0-9.]", "");
        if (sanitized.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(sanitized);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
