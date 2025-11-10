package com.example.myfoodapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.AdminFoodsAdapter;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageFoodsActivity extends AppCompatActivity implements AdminFoodsAdapter.FoodActionListener {

    private foodController controller;
    private AdminFoodsAdapter adapter;
    private List<HomeHorModel> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_foods);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.manage_foods);
        }

        controller = new foodController(this);
        RecyclerView recyclerView = findViewById(R.id.foods_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminFoodsAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_food);
        fab.setOnClickListener(v -> showFoodDialog(null));

        loadData();
    }

    private void loadData() {
        categories = controller.getAllCategories();
        adapter.submitList(controller.getAllProducts());
    }

    @Override
    public void onEdit(HomeVerModel model) {
        showFoodDialog(model);
    }

    @Override
    public void onDelete(HomeVerModel model) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (controller.deleteProduct(model.getId())) {
                        Toast.makeText(this, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showFoodDialog(HomeVerModel product) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_food, null, false);
        EditText inputName = view.findViewById(R.id.input_food_name);
        EditText inputPrice = view.findViewById(R.id.input_food_price);
        EditText inputRating = view.findViewById(R.id.input_food_rating);
        EditText inputTiming = view.findViewById(R.id.input_food_timing);
        EditText inputImage = view.findViewById(R.id.input_food_image);
        Spinner categorySpinner = view.findViewById(R.id.input_food_category);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getCategoryNames());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        if (product != null) {
            inputName.setText(product.getName());
            inputPrice.setText(product.getPrice());
            inputRating.setText(product.getRating());
            inputTiming.setText(product.getTiming());
            inputImage.setText(getResources().getResourceEntryName(product.getImage()));
            setCategorySelection(categorySpinner, product.getCategoryId());
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(product == null ? R.string.add_food : R.string.edit_food)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String price = inputPrice.getText().toString().trim();
            String rating = inputRating.getText().toString().trim();
            String timing = inputTiming.getText().toString().trim();
            String imageName = inputImage.getText().toString().trim();
            int categoryId = getCategoryId(categorySpinner.getSelectedItemPosition());

            if (TextUtils.isEmpty(name) || categoryId == -1) {
                Toast.makeText(this, R.string.validation_required, Toast.LENGTH_SHORT).show();
                return;
            }

            int imageRes = resolveDrawable(imageName, product != null ? product.getImage() : R.drawable.pizza1);
            boolean success;

            if (product == null) {
                HomeVerModel newProduct = new HomeVerModel();
                newProduct.setName(name);
                newProduct.setPrice(price);
                newProduct.setRating(rating);
                newProduct.setTiming(timing);
                newProduct.setImage(imageRes);
                newProduct.setCategoryId(categoryId);
                success = controller.addProduct(newProduct) > 0;
            } else {
                product.setName(name);
                product.setPrice(price);
                product.setRating(rating);
                product.setTiming(timing);
                product.setImage(imageRes);
                product.setCategoryId(categoryId);
                success = controller.updateProduct(product);
            }

            if (success) {
                Toast.makeText(this, R.string.saved_success, Toast.LENGTH_SHORT).show();
                loadData();
                dialog.dismiss();
            } else {
                Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
            }
        }));
        dialog.show();
    }

    private List<String> getCategoryNames() {
        List<String> names = new ArrayList<>();
        for (HomeHorModel cat : categories) {
            names.add(cat.getName());
        }
        return names;
    }

    private void setCategorySelection(Spinner spinner, int categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == categoryId) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private int getCategoryId(int position) {
        if (position < 0 || position >= categories.size()) {
            return -1;
        }
        return categories.get(position).getId();
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
