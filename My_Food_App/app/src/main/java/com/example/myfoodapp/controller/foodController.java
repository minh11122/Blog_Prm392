package com.example.myfoodapp.controller; // Package controller mới

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper; // Import DatabaseHelper
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
    public ArrayList<HomeVerModel> getProductsByCategory(int categoryId) {
        ArrayList<HomeVerModel> list = new ArrayList<>();


        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseHelper.COL_PROD_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(categoryId) };

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                list.add(new HomeVerModel(image, name, timing, rating, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //Hàm SEARCH
    public ArrayList<HomeVerModel> searchProducts(String query) {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Câu lệnh SQL: "SELECT * FROM products WHERE name LIKE '%query%'"
        String selection = DatabaseHelper.COL_PROD_NAME + " LIKE ?";
        String[] selectionArgs = { "%" + query + "%" };

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_TIMING));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_RATING));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                list.add(new HomeVerModel(image, name, timing, rating, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}