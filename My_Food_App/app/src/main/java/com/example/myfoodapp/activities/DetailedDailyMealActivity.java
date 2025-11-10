package com.example.myfoodapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.DetailedDailyAdapter;
import com.example.myfoodapp.database.DatabaseHelper;
import com.example.myfoodapp.models.DetailedDailyModel;

import java.util.ArrayList;
import java.util.List;

public class DetailedDailyMealActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DetailedDailyModel> detailedDailyModelList;
    DetailedDailyAdapter dailyAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_daily_meal);

        String type = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_img);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailedDailyModelList = new ArrayList<>();
        dailyAdapter = new DetailedDailyAdapter(detailedDailyModelList);
        recyclerView.setAdapter(dailyAdapter);

        // LẤY DỮ LIỆU TỪ DATABASE
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (type != null) {
            detailedDailyModelList.addAll(dbHelper.getDetailedMealsByType(type));
            dailyAdapter.notifyDataSetChanged();

            // Cập nhật ảnh nền theo loại
            switch (type.toLowerCase()) {
                case "breakfast":
                    imageView.setImageResource(R.drawable.breakfast);
                    break;
                case "lunch":
                    imageView.setImageResource(R.drawable.fav3);
                    break;
                case "dinner":
                    imageView.setImageResource(R.drawable.dinner);
                    break;
                case "sweets":
                    imageView.setImageResource(R.drawable.sweets);
                    break;
                case "coffe":
                    imageView.setImageResource(R.drawable.coffe);
                    break;
            }
        }

        // BẮT SỰ KIỆN ADD TO CART
        dailyAdapter.setOnAddToCartClickListener(item -> {
            dbHelper.addToCart(item);
            Toast.makeText(this, item.getName() + " đã thêm vào giỏ!", Toast.LENGTH_SHORT).show();
        });

        // Xử lý insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}