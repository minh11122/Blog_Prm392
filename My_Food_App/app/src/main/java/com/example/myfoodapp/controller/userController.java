package com.example.myfoodapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper;
import com.example.myfoodapp.models.userModel;

public class userController {

    private final DatabaseHelper dbHelper;

    public userController(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // HELPER: GEN PASS
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // REGISTER
    public boolean registerUser(userModel user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE email = ?", new String[]{user.getEmail()});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // email đã tồn tại
        }

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("phone", user.getPhone());
        values.put("address", user.getAddress());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        cursor.close();
        db.close();

        return result != -1;
    }

    // LOGIN
    public userModel loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        userModel user = null;
        if (cursor.moveToFirst()) {
            user = new userModel();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
        }

        cursor.close();
        db.close();
        return user;
    }

    // RESET PASSWORD
    public String resetPassword(String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE email = ?", new String[]{email});
        if (cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return null;
        }

        String newPassword = generateRandomPassword(8);

        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        db.update(DatabaseHelper.TABLE_USERS, values, "email = ?", new String[]{email});

        cursor.close();
        db.close();

        // Gửi email thông báo mật khẩu mới
        new Thread(() -> {
            String subject = "Khôi phục mật khẩu - MyFoodApp";
            String body = "Xin chào,\n\nMật khẩu mới của bạn là: " + newPassword +
                    "\n\nVui lòng đăng nhập và thay đổi lại mật khẩu ngay sau khi đăng nhập.\n\nCảm ơn bạn đã sử dụng MyFoodApp!";
            com.example.myfoodapp.utils.MailSender.sendEmail(email, subject, body);
        }).start();

        return newPassword;
    }
}
