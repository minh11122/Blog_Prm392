package com.example.myfoodapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myfoodapp.db";
    private static final int DATABASE_VERSION = 1;

    // ĐẶT TÊN TABLE
    public static final String TABLE_USERS = "users";

    // --- Bảng CATEGORIES (cho list ngang) ---
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COL_CAT_ID = "id"; // ID (Primary Key)
    public static final String COL_CAT_NAME = "name";
    public static final String COL_CAT_IMAGE = "image_resource_id";

    // --- Bảng PRODUCTS (cho list dọc) ---
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PROD_ID = "id"; // ID (Primary Key)
    public static final String COL_PROD_NAME = "name";
    public static final String COL_PROD_IMAGE = "image_resource_id";
    public static final String COL_PROD_TIMING = "timing";
    public static final String COL_PROD_RATING = "rating";
    public static final String COL_PROD_PRICE = "price";
    public static final String COL_PROD_CATEGORY_ID = "category_id";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Hàm thêm 1 Category
    private void addCategory(SQLiteDatabase db, String name, int imageResId) {
        ContentValues values = new ContentValues();
        values.put(COL_CAT_NAME, name);
        values.put(COL_CAT_IMAGE, imageResId);
        db.insert(TABLE_CATEGORIES, null, values);
    }

    // Hàm thêm 1 Product
    private void addProduct(SQLiteDatabase db, String name, int imageResId, String timing, String rating, String price, int categoryId) {
        ContentValues values = new ContentValues();
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_IMAGE, imageResId);
        values.put(COL_PROD_TIMING, timing);
        values.put(COL_PROD_RATING, rating);
        values.put(COL_PROD_PRICE, price);
        values.put(COL_PROD_CATEGORY_ID, categoryId);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    // Hàm để thêm toàn bộ dữ liệu ban đầu
    private void addInitialData(SQLiteDatabase db) {
        // Thêm Categories
        addCategory(db, "Pizza", R.drawable.pizza);
        addCategory(db, "Hamburger", R.drawable.hamburger);
        addCategory(db, "Fries", R.drawable.fried_potatoes);
        addCategory(db, "Ice Cream", R.drawable.ice_cream);
        addCategory(db, "Sandwich", R.drawable.sandwich);

        // Thêm Products - Chú ý các ID danh mục (1, 2, 3...)
        // Pizza (Category ID = 1)
        addProduct(db, "Pizaa 1", R.drawable.pizza1, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 2", R.drawable.pizza2, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 3", R.drawable.pizza3, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 4", R.drawable.pizza4, "10:00-23:00", "4.9", "Min-$34", 1);

        // Hamburger (Category ID = 2)
        addProduct(db, "Burrger 1", R.drawable.burger1, "10:00-23:00", "4.9", "Min-$34", 2);
        addProduct(db, "Burrger 2", R.drawable.burger2, "10:00-23:00", "4.9", "Min-$34", 2);
        addProduct(db, "Burrger 4", R.drawable.burger4, "10:00-23:00", "4.9", "Min-$34", 2);

        // Fries (Category ID = 3)
        addProduct(db, "Fries 1", R.drawable.fries1, "10:00-23:00", "4.9", "Min-$34", 3);
        addProduct(db, "Fries 2", R.drawable.fries2, "10:00-23:00", "4.9", "Min-$34", 3);
        addProduct(db, "Fries 3", R.drawable.fries3, "10:00-23:00", "4.9", "Min-$34", 3);

        // Ice Cream (Category ID = 4)
        addProduct(db, "Ice Cream 1", R.drawable.icecream1, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 2", R.drawable.icecream2, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 3", R.drawable.icecream3, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 4", R.drawable.icecream4, "10:00-23:00", "4.9", "Min-$34", 4);

        // Sandwich (Category ID = 5)
        addProduct(db, "Sandwich 1", R.drawable.sandwich1, "10:00-23:00", "4.9", "Min-$34", 5);
        addProduct(db, "Sandwich 2", R.drawable.sandwich2, "10:00-23:00", "4.9", "Min-$34", 5);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE USER TABLE
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "phone TEXT, " +
                "address TEXT)";
        db.execSQL(createUsersTable);

        String createTableCategories = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAT_NAME + " TEXT, " +
                COL_CAT_IMAGE + " INTEGER)";
        db.execSQL(createTableCategories);


        // Câu lệnh tạo bảng Products
        String createTableProducts = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_NAME + " TEXT, " +
                COL_PROD_IMAGE + " INTEGER, " +
                COL_PROD_TIMING + " TEXT, " +
                COL_PROD_RATING + " TEXT, " +
                COL_PROD_PRICE + " TEXT, " +
                COL_PROD_CATEGORY_ID + " INTEGER, " + // Khóa ngoại
                "is_favorite INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_PROD_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_CAT_ID + "))";
        db.execSQL(createTableProducts);
        addInitialData(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }
}
