package com.example.myfoodapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfoodapp.MainActivity;
import com.example.myfoodapp.R;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        findViewById(R.id.btn_manage_users).setOnClickListener(this);
        findViewById(R.id.btn_manage_orders).setOnClickListener(this);
        findViewById(R.id.btn_manage_categories).setOnClickListener(this);
        findViewById(R.id.btn_manage_foods).setOnClickListener(this);
        findViewById(R.id.btn_go_customer).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_manage_users) {
            startActivity(new Intent(this, ManageUsersActivity.class));
        } else if (id == R.id.btn_manage_orders) {
            startActivity(new Intent(this, ManageOrdersActivity.class));
        } else if (id == R.id.btn_manage_categories) {
            startActivity(new Intent(this, ManageCategoriesActivity.class));
        } else if (id == R.id.btn_manage_foods) {
            startActivity(new Intent(this, ManageFoodsActivity.class));
        } else if (id == R.id.btn_go_customer) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.btn_logout) {
            performLogout();
        }
    }

    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
