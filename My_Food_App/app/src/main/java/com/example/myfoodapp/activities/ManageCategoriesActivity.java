package com.example.myfoodapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.AdminCategoriesAdapter;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.HomeHorModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ManageCategoriesActivity extends AppCompatActivity implements AdminCategoriesAdapter.CategoryActionListener {

    private foodController controller;
    private AdminCategoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.manage_categories);
        }

        controller = new foodController(this);
        RecyclerView recyclerView = findViewById(R.id.categories_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminCategoriesAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_category);
        fab.setOnClickListener(v -> showCategoryDialog(null));

        loadCategories();
    }

    private void loadCategories() {
        adapter.submitList(controller.getAllCategories());
    }

    @Override
    public void onEdit(HomeHorModel category) {
        showCategoryDialog(category);
    }

    @Override
    public void onDelete(HomeHorModel category) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (controller.deleteCategory(category.getId())) {
                        Toast.makeText(this, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                        loadCategories();
                    } else {
                        Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showCategoryDialog(HomeHorModel category) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category, null, false);
        EditText inputName = view.findViewById(R.id.input_category_name);
        EditText inputImage = view.findViewById(R.id.input_category_image);

        if (category != null) {
            inputName.setText(category.getName());
            inputImage.setText(getResources().getResourceEntryName(category.getImage()));
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(category == null ? R.string.add_category : R.string.edit_category)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String imageName = inputImage.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, R.string.validation_required, Toast.LENGTH_SHORT).show();
                return;
            }
            int imageRes = resolveDrawable(imageName, category != null ? category.getImage() : R.drawable.pizza1);
            boolean success;
            if (category == null) {
                long id = controller.addCategory(name, imageRes);
                success = id > 0;
            } else {
                category.setName(name);
                category.setImage(imageRes);
                success = controller.updateCategory(category);
            }
            if (success) {
                Toast.makeText(this, R.string.saved_success, Toast.LENGTH_SHORT).show();
                loadCategories();
                dialog.dismiss();
            } else {
                Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
            }
        }));
        dialog.show();
    }

    private int resolveDrawable(String name, int fallback) {
        if (TextUtils.isEmpty(name)) {
            return fallback;
        }
        int resId = getResources().getIdentifier(name, "drawable", getPackageName());
        if (resId == 0) {
            Toast.makeText(this, R.string.image_not_found, Toast.LENGTH_SHORT).show();
            return fallback;
        }
        return resId;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
