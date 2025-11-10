package com.example.myfoodapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;

public class FoodDAO {

    private DatabaseHelper dbHelper;

    public FoodDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Lấy danh sách món ăn yêu thích
    public ArrayList<HomeVerModel> getFavoriteFoods() {
        ArrayList<HomeVerModel> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PRODUCTS,
                null,
                "is_favorite=?",
                new String[]{"1"},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int imageRes = cursor.getInt(cursor.getColumnIndexOrThrow("image_resource_id"));
                String timing = cursor.getString(cursor.getColumnIndexOrThrow("timing"));
                String rating = cursor.getString(cursor.getColumnIndexOrThrow("rating"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));

                list.add(new HomeVerModel(id, imageRes, name, timing, rating, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Cập nhật món ăn yêu thích
    public void setFavorite(int productId, boolean favorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_favorite", favorite ? 1 : 0);
        db.update(DatabaseHelper.TABLE_PRODUCTS, values, "id=?", new String[]{String.valueOf(productId)});
    }
}
