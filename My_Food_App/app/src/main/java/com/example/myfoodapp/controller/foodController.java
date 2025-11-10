package com.example.myfoodapp.controller; // Package controller mới

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper; // Import DatabaseHelper
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;


public class foodController {

    private DatabaseHelper dbHelper;

    public foodController(Context context) {
        this.dbHelper = new DatabaseHelper(context);
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
                // Kiểm tra cột is_favorite nếu muốn đánh dấu
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
