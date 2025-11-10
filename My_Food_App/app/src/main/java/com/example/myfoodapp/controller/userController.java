package com.example.myfoodapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfoodapp.database.DatabaseHelper;
import com.example.myfoodapp.models.RoleModel;
import com.example.myfoodapp.models.UserModel;

import java.util.ArrayList;

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
    public boolean registerUser(UserModel user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE email = ?", new String[]{user.getEmail()});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // email đã tồn tại
        }

        if (user.getRole() == null) {
            RoleModel customerRole = new RoleModel(2, "Customer");
            user.setRole(customerRole);
        }

        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("phone", user.getPhone());
        values.put("address", user.getAddress());
        values.put("role_id", user.getRole().getId()); // lưu role_id vào DB

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        cursor.close();
        db.close();

        return result != -1;
    }

    // LOGIN
    public UserModel loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Lấy thêm role_id từ DB
        String query = "SELECT u.*, r.name AS role_name " +
                "FROM " + DatabaseHelper.TABLE_USERS + " u " +
                "LEFT JOIN roles r ON u.role_id = r.id " +
                "WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        UserModel user = null;
        if (cursor.moveToFirst()) {
            user = new UserModel();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));

            // Set role
            int roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id"));
            String roleName = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
            user.setRole(new RoleModel(roleId, roleName));
        }

        cursor.close();
        db.close();
        return user;
    }

    public ArrayList<UserModel> getAllUsers() {
        ArrayList<UserModel> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT u.*, r.name AS role_name FROM " + DatabaseHelper.TABLE_USERS + " u " +
                "LEFT JOIN " + DatabaseHelper.TABLE_ROLE + " r ON u.role_id = r.id";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
                int roleId = cursor.getInt(cursor.getColumnIndexOrThrow("role_id"));
                String roleName = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                user.setRole(new RoleModel(roleId, roleName));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DatabaseHelper.TABLE_USERS, "id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rows > 0;
    }

    public boolean updateUser(UserModel user) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        values.put("address", user.getAddress());
        values.put("role_id", user.getRole().getId());
        int rows = db.update(DatabaseHelper.TABLE_USERS, values, "id = ?", new String[]{String.valueOf(user.getId())});
        db.close();
        return rows > 0;
    }

    public ArrayList<RoleModel> getAllRoles() {
        ArrayList<RoleModel> roles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ROLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                roles.add(new RoleModel(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roles;
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
