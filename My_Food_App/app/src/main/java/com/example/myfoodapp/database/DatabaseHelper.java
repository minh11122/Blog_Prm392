package com.example.myfoodapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.models.DailyMealModel;
import com.example.myfoodapp.models.DetailedDailyModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myfoodapp.db";
    private static final int DATABASE_VERSION = 4; // Đã thêm bảng mới → tăng version

    // --- BẢNG USERS & ROLES ---
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ROLE = "roles";

    // --- BẢNG CATEGORIES ---
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COL_CAT_ID = "id";
    public static final String COL_CAT_NAME = "name";
    public static final String COL_CAT_IMAGE = "image_resource_id";

    // --- BẢNG PRODUCTS ---
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PROD_ID = "id";
    public static final String COL_PROD_NAME = "name";
    public static final String COL_PROD_IMAGE = "image_resource_id";
    public static final String COL_PROD_TIMING = "timing";
    public static final String COL_PROD_RATING = "rating";
    public static final String COL_PROD_PRICE = "price";
    public static final String COL_PROD_CATEGORY_ID = "category_id";

    // --- BẢNG CART ---
    public static final String TABLE_CART = "cart_items";
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_PRODUCT_ID = "product_id";
    public static final String COL_CART_NAME = "name";
    public static final String COL_CART_IMAGE = "image_resource_id";
    public static final String COL_CART_RATING = "rating";
    public static final String COL_CART_PRICE_TEXT = "price_display";
    public static final String COL_CART_UNIT_PRICE = "unit_price";
    public static final String COL_CART_QUANTITY = "quantity";

    // --- Orders ---
    public static final String TABLE_ORDERS = "orders";
    public static final String COL_ORDER_ID = "id";
    public static final String COL_ORDER_USER_ID = "user_id";
    public static final String COL_ORDER_TOTAL = "total_amount";
    public static final String COL_ORDER_STATUS = "status";
    public static final String COL_ORDER_CREATED_AT = "created_at";

    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COL_ORDER_ITEM_ID = "id";
    public static final String COL_ORDER_ITEM_ORDER_ID = "order_id";
    public static final String COL_ORDER_ITEM_PRODUCT_ID = "product_id";
    public static final String COL_ORDER_ITEM_NAME = "product_name";
    public static final String COL_ORDER_ITEM_PRICE = "price";
    public static final String COL_ORDER_ITEM_QUANTITY = "quantity";

    // --- BẢNG DAILY MEALS ---
    public static final String TABLE_DAILY_MEALS = "daily_meals";
    public static final String COL_DAILY_ID = "id";
    public static final String COL_DAILY_NAME = "name";
    public static final String COL_DAILY_IMAGE = "image_resource_id";
    public static final String COL_DAILY_DISCOUNT = "discount";
    public static final String COL_DAILY_DESCRIPTION = "description";
    public static final String COL_DAILY_TYPE = "type";

    // --- BẢNG DETAILED DAILY MEALS ---
    public static final String TABLE_DETAILED_DAILY = "detailed_daily_meals";
    public static final String COL_DET_ID = "id";
    public static final String COL_DET_NAME = "name";
    public static final String COL_DET_IMAGE = "image_resource_id";
    public static final String COL_DET_DESCRIPTION = "description";
    public static final String COL_DET_RATING = "rating";
    public static final String COL_DET_PRICE = "price";
    public static final String COL_DET_TIMING = "timing";
    public static final String COL_DET_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // === HÀM THÊM DỮ LIỆU ===
    private void addCategory(SQLiteDatabase db, String name, int imageResId) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CAT_NAME, name);
        cv.put(COL_CAT_IMAGE, imageResId);
        db.insert(TABLE_CATEGORIES, null, cv);
    }

    private void addProduct(SQLiteDatabase db, String name, int imageResId, String timing, String rating, String price, int categoryId) {
        ContentValues cv = new ContentValues();
        cv.put(COL_PROD_NAME, name);
        cv.put(COL_PROD_IMAGE, imageResId);
        cv.put(COL_PROD_TIMING, timing);
        cv.put(COL_PROD_RATING, rating);
        cv.put(COL_PROD_PRICE, price);
        cv.put(COL_PROD_CATEGORY_ID, categoryId);
        db.insert(TABLE_PRODUCTS, null, cv);
    }

    private void addDailyMeal(SQLiteDatabase db, String name, int imageResId, String discount, String description, String type) {
        ContentValues cv = new ContentValues();
        cv.put(COL_DAILY_NAME, name);
        cv.put(COL_DAILY_IMAGE, imageResId);
        cv.put(COL_DAILY_DISCOUNT, discount);
        cv.put(COL_DAILY_DESCRIPTION, description);
        cv.put(COL_DAILY_TYPE, type);
        db.insert(TABLE_DAILY_MEALS, null, cv);
    }

    private void addDetailedDailyMeal(SQLiteDatabase db, String name, int imageResId, String description,
                                      String rating, String price, String timing, String type) {
        ContentValues cv = new ContentValues();
        cv.put(COL_DET_NAME, name);
        cv.put(COL_DET_IMAGE, imageResId);
        cv.put(COL_DET_DESCRIPTION, description);
        cv.put(COL_DET_RATING, rating);
        cv.put(COL_DET_PRICE, price);
        cv.put(COL_DET_TIMING, timing);
        cv.put(COL_DET_TYPE, type);
        db.insert(TABLE_DETAILED_DAILY, null, cv);
    }

    // === DỮ LIỆU MẪU ===
    private void addInitialData(SQLiteDatabase db) {
        // Categories
        addCategory(db, "Pizza", R.drawable.pizza);
        addCategory(db, "Hamburger", R.drawable.hamburger);
        addCategory(db, "Fries", R.drawable.fried_potatoes);
        addCategory(db, "Ice Cream", R.drawable.ice_cream);
        addCategory(db, "Sandwich", R.drawable.sandwich);

        // Products
        addProduct(db, "Pizaa 1", R.drawable.pizza1, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 2", R.drawable.pizza2, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 3", R.drawable.pizza3, "10:00-23:00", "4.9", "Min-$34", 1);
        addProduct(db, "Pizaa 4", R.drawable.pizza4, "10:00-23:00", "4.9", "Min-$34", 1);

        addProduct(db, "Burrger 1", R.drawable.burger1, "10:00-23:00", "4.9", "Min-$34", 2);
        addProduct(db, "Burrger 2", R.drawable.burger2, "10:00-23:00", "4.9", "Min-$34", 2);
        addProduct(db, "Burrger 4", R.drawable.burger4, "10:00-23:00", "4.9", "Min-$34甜", 2);

        addProduct(db, "Fries 1", R.drawable.fries1, "10:00-23:00", "4.9", "Min-$34", 3);
        addProduct(db, "Fries 2", R.drawable.fries2, "10:00-23:00", "4.9", "Min-$34", 3);
        addProduct(db, "Fries 3", R.drawable.fries3, "10:00-23:00", "4.9", "Min-$34", 3);

        addProduct(db, "Ice Cream 1", R.drawable.icecream1, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 2", R.drawable.icecream2, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 3", R.drawable.icecream3, "10:00-23:00", "4.9", "Min-$34", 4);
        addProduct(db, "Ice Cream 4", R.drawable.icecream4, "10:00-23:00", "4.9", "Min-$34", 4);

        addProduct(db, "Sandwich 1", R.drawable.sandwich1, "10:00-23:00", "4.9", "Min-$34", 5);
        addProduct(db, "Sandwich 2", R.drawable.sandwich2, "10:00-23:00", "4.9", "Min-$34", 5);

        // Daily Meals
        addDailyMeal(db, "Breakfast", R.drawable.breakfast, "30% OFF", "Delicious morning meals", "breakfast");
        addDailyMeal(db, "Lunch", R.drawable.fav3, "20% OFF", "Fresh and healthy lunch", "lunch");
        addDailyMeal(db, "Dinner", R.drawable.dinner, "50% OFF", "Hearty dinner options", "dinner");
        addDailyMeal(db, "Sweets", R.drawable.icecream4, "39% OFF", "Sweet treats for you", "sweets");
        addDailyMeal(db, "Coffe", R.drawable.coffe, "20% OFF", "Premium coffee blends", "coffe");

        // Detailed Daily Meals
        // Breakfast
        addDetailedDailyMeal(db, "Pancake Stack", R.drawable.fav1, "Fluffy pancakes with syrup", "4.4", "40", "10:00 - 09:00", "breakfast");
        addDetailedDailyMeal(db, "Eggs Benedict", R.drawable.fav2, "Poached eggs on toast", "4.4", "40", "10:00 - 09:00", "breakfast");
        addDetailedDailyMeal(db, "Avocado Toast", R.drawable.fav3, "Healthy & delicious", "4.4", "40", "10:00 - 09:00", "breakfast");

        // Sweets
        addDetailedDailyMeal(db, "Chocolate Cake", R.drawable.s1, "Rich and moist", "4.4", "40", "10am to 9pm", "sweets");
        addDetailedDailyMeal(db, "Ice Cream Sundae", R.drawable.s2, "Topped with nuts", "4.4", "40", "10am to 9pm", "sweets");
        addDetailedDailyMeal(db, "Tiramisu", R.drawable.s3, "Classic Italian dessert", "4.4", "40", "10am to 9pm", "sweets");

        // Lunch
        addDetailedDailyMeal(db, "Grilled Chicken", R.drawable.lunch1, "Served with salad", "4.4", "40", "10am to 9pm", "lunch");
        addDetailedDailyMeal(db, "Pasta Salad", R.drawable.lunch2, "Fresh veggies & pasta", "4.4", "40", "10am to 9pm", "lunch");
        addDetailedDailyMeal(db, "Burger Combo", R.drawable.lunch3, "With fries & drink", "4.4", "40", "10am to 9pm", "lunch");

        // Dinner
        addDetailedDailyMeal(db, "Steak Dinner", R.drawable.dinner1, "Premium cut", "4.4", "40", "10am to 9pm", "dinner");
        addDetailedDailyMeal(db, "Salmon Fillet", R.drawable.dinner2, "Grilled to perfection", "4.4", "40", "10am to 9pm", "dinner");
        addDetailedDailyMeal(db, "Pasta Carbonara", R.drawable.dinner3, "Creamy & rich", "4.4", "40", "10am to 9pm", "dinner");

        // Coffe
        addDetailedDailyMeal(db, "Latte", R.drawable.c1, "Smooth and creamy", "4.4", "40", "10am to 9pm", "coffe");
        addDetailedDailyMeal(db, "Cappuccino", R.drawable.c2, "Espresso with foam", "4.4", "40", "10am to 9pm", "coffe");
        addDetailedDailyMeal(db, "Mocha", R.drawable.c3, "Coffee + chocolate", "4.4", "40", "10am to 9pm", "coffe");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Roles
        db.execSQL("CREATE TABLE " + TABLE_ROLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE NOT NULL)");
        db.insert(TABLE_ROLE, null, cv("id", 1, "name", "Admin"));
        db.insert(TABLE_ROLE, null, cv("id", 2, "name", "Customer"));

        // Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, password TEXT, phone TEXT, address TEXT, role_id INTEGER, FOREIGN KEY(role_id) REFERENCES " + TABLE_ROLE + "(id))");
        db.insert(TABLE_USERS, null, cv("name", "Admin", "email", "admin@gmail.com", "password", "123456", "phone", "0123456789", "address", "Admin Address", "role_id", 1));

        // Categories
        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" + COL_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CAT_NAME + " TEXT, " + COL_CAT_IMAGE + " INTEGER)");

        // Products
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_NAME + " TEXT, " +
                COL_PROD_IMAGE + " INTEGER, " +
                COL_PROD_TIMING + " TEXT, " +
                COL_PROD_RATING + " TEXT, " +
                COL_PROD_PRICE + " TEXT, " +
                COL_PROD_CATEGORY_ID + " INTEGER, " +
                "is_favorite INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COL_PROD_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_CAT_ID + "))");

        // Cart
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_PRODUCT_ID + " INTEGER UNIQUE, " +
                COL_CART_NAME + " TEXT, " +
                COL_CART_IMAGE + " INTEGER, " +
                COL_CART_RATING + " TEXT, " +
                COL_CART_PRICE_TEXT + " TEXT, " +
                COL_CART_UNIT_PRICE + " REAL, " +
                COL_CART_QUANTITY + " INTEGER DEFAULT 1)");

        // Daily Meals
        db.execSQL("CREATE TABLE " + TABLE_DAILY_MEALS + " (" +
                COL_DAILY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DAILY_NAME + " TEXT, " +
                COL_DAILY_IMAGE + " INTEGER, " +
                COL_DAILY_DISCOUNT + " TEXT, " +
                COL_DAILY_DESCRIPTION + " TEXT, " +
                COL_DAILY_TYPE + " TEXT)");

        // Detailed Daily Meals
        db.execSQL("CREATE TABLE " + TABLE_DETAILED_DAILY + " (" +
                COL_DET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DET_NAME + " TEXT, " +
                COL_DET_IMAGE + " INTEGER, " +
                COL_DET_DESCRIPTION + " TEXT, " +
                COL_DET_RATING + " TEXT, " +
                COL_DET_PRICE + " TEXT, " +
                COL_DET_TIMING + " TEXT, " +
                COL_DET_TYPE + " TEXT)");

        // Bảng Orders
        String createOrdersTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_USER_ID + " INTEGER, " +
                COL_ORDER_TOTAL + " REAL, " +
                COL_ORDER_STATUS + " TEXT, " +
                COL_ORDER_CREATED_AT + " INTEGER, " +
                "FOREIGN KEY(" + COL_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(id))";
        db.execSQL(createOrdersTable);

        // Bảng Order items
        String createOrderItemsTable = "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ITEM_ORDER_ID + " INTEGER, " +
                COL_ORDER_ITEM_PRODUCT_ID + " INTEGER, " +
                COL_ORDER_ITEM_NAME + " TEXT, " +
                COL_ORDER_ITEM_PRICE + " REAL, " +
                COL_ORDER_ITEM_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COL_ORDER_ID + "))";
        db.execSQL(createOrderItemsTable);

        addInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_MEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILED_DAILY); // ĐÃ THÊM
        onCreate(db);
    }

    // === HÀM LẤY DỮ LIỆU ===
    public List<DailyMealModel> getAllDailyMeals() {
        List<DailyMealModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DAILY_MEALS, null);
        if (c.moveToFirst()) {
            do {
                list.add(new DailyMealModel(
                        c.getInt(c.getColumnIndexOrThrow(COL_DAILY_IMAGE)),
                        c.getString(c.getColumnIndexOrThrow(COL_DAILY_NAME)),
                        c.getString(c.getColumnIndexOrThrow(COL_DAILY_DISCOUNT)),
                        c.getString(c.getColumnIndexOrThrow(COL_DAILY_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COL_DAILY_TYPE))
                ));
            } while (c.moveToNext());
        }
        c.close(); db.close();
        return list;
    }

    public List<DetailedDailyModel> getDetailedMealsByType(String type) {
        List<DetailedDailyModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_DET_TYPE + " = ?";
        String[] args = { type.toLowerCase() };
        Cursor c = db.query(TABLE_DETAILED_DAILY, null, selection, args, null, null, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow(COL_DET_ID)); // LẤY ID
                int image = c.getInt(c.getColumnIndexOrThrow(COL_DET_IMAGE));
                String name = c.getString(c.getColumnIndexOrThrow(COL_DET_NAME));
                String desc = c.getString(c.getColumnIndexOrThrow(COL_DET_DESCRIPTION));
                String rating = c.getString(c.getColumnIndexOrThrow(COL_DET_RATING));
                String price = c.getString(c.getColumnIndexOrThrow(COL_DET_PRICE));
                String timing = c.getString(c.getColumnIndexOrThrow(COL_DET_TIMING));

                list.add(new DetailedDailyModel(id, image, name, desc, rating, price, timing));
            } while (c.moveToNext());
        }
        c.close(); db.close();
        return list;
    }

    // Helper để tạo ContentValues nhanh
    private ContentValues cv(Object... pairs) {
        ContentValues cv = new ContentValues();
        for (int i = 0; i < pairs.length; i += 2) {
            Object value = pairs[i + 1];
            if (value instanceof String) cv.put((String) pairs[i], (String) value);
            else if (value instanceof Integer) cv.put((String) pairs[i], (Integer) value);
        }
        return cv;
    }



    public void addToCart(DetailedDailyModel item) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COL_CART_PRODUCT_ID + " = ?";
        String[] args = { String.valueOf(item.getId()) };

        Cursor cursor = db.query(TABLE_CART, null, selection, args, null, null, null);
        if (cursor.getCount() > 0) {
            // ĐÃ CÓ → TĂNG SỐ LƯỢNG
            cursor.moveToFirst();
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_QUANTITY));
            ContentValues cv = new ContentValues();
            cv.put(COL_CART_QUANTITY, currentQty + 1);
            db.update(TABLE_CART, cv, selection, args);
        } else {
            // CHƯA CÓ → THÊM MỚI
            ContentValues cv = new ContentValues();
            cv.put(COL_CART_PRODUCT_ID, item.getId());
            cv.put(COL_CART_NAME, item.getName());
            cv.put(COL_CART_IMAGE, item.getImage());
            cv.put(COL_CART_RATING, item.getRating());
            cv.put(COL_CART_PRICE_TEXT, item.getPrice());
            cv.put(COL_CART_UNIT_PRICE, parsePrice(item.getPrice()));
            cv.put(COL_CART_QUANTITY, 1);
            db.insert(TABLE_CART, null, cv);
        }
        cursor.close();
        db.close();
    }

    // === LẤY TẤT CẢ MÓN TRONG GIỎ ===
    public List<CartModel> getAllCartItems() {
        List<CartModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndexOrThrow(COL_CART_ID));
                int productId = c.getInt(c.getColumnIndexOrThrow(COL_CART_PRODUCT_ID));
                int image = c.getInt(c.getColumnIndexOrThrow(COL_CART_IMAGE));
                String name = c.getString(c.getColumnIndexOrThrow(COL_CART_NAME));
                String priceText = c.getString(c.getColumnIndexOrThrow(COL_CART_PRICE_TEXT));
                String rating = c.getString(c.getColumnIndexOrThrow(COL_CART_RATING));
                int quantity = c.getInt(c.getColumnIndexOrThrow(COL_CART_QUANTITY));
                double unitPrice = c.getDouble(c.getColumnIndexOrThrow(COL_CART_UNIT_PRICE));

                list.add(new CartModel(id, productId, image, name, priceText, rating, quantity, unitPrice));
            } while (c.moveToNext());
        }
        c.close(); db.close();
        return list;
    }

    // === XÓA MÓN KHỎI GIỎ ===
    public void removeFromCart(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // === CẬP NHẬT SỐ LƯỢNG ===
    public void updateCartQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeFromCart(productId);
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CART_QUANTITY, newQuantity);
        db.update(TABLE_CART, cv, COL_CART_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // === HỖ TRỢ: CHUYỂN GIÁ TIỀN TỪ CHUỖI SANG SỐ ===
    private double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
}