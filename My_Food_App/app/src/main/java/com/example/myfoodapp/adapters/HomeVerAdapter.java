package com.example.myfoodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.HomeVerModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    private BottomSheetDialog bottomSheetDialog;
    private Context context;
    private ArrayList<HomeVerModel> list;
    private foodController foodController;

    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list, foodController controller) {
        this.context = context;
        this.list = list;
        this.foodController = controller;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeVerModel item = list.get(position);

        holder.imageView.setImageResource(item.getImage());
        holder.name.setText(item.getName());
        holder.timing.setText(item.getTiming());
        holder.rating.setText(item.getRating());
        holder.price.setText(item.getPrice());

        // Set trạng thái tim theo database
        holder.isFavorite = item.isFavorite();
        holder.ivFavorite.setImageResource(holder.isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_border);

        // Xử lý click item để mở BottomSheet
        holder.itemView.setOnClickListener(v -> {
            bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);

            sheetView.findViewById(R.id.add_to_cart).setOnClickListener(v1 -> {
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            });

            ImageView bottomImg = sheetView.findViewById(R.id.bottom_img);
            TextView bottomName = sheetView.findViewById(R.id.bottom_name);
            TextView bottomPrice = sheetView.findViewById(R.id.bottom_price);
            TextView bottomRating = sheetView.findViewById(R.id.bottom_rating);

            bottomName.setText(item.getName());
            bottomPrice.setText(item.getPrice());
            bottomRating.setText(item.getRating());
            bottomImg.setImageResource(item.getImage());

            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();
        });

        // Xử lý click tim
        holder.ivFavorite.setOnClickListener(v -> {
            holder.isFavorite = !holder.isFavorite;
            holder.ivFavorite.setImageResource(holder.isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_border);

            // Cập nhật database thông qua foodController
            foodController.updateFavorite(item.getId(), holder.isFavorite ? 1 : 0);
            item.setFavorite(holder.isFavorite);

            Toast.makeText(context,
                    holder.isFavorite ? "Đã thêm vào yêu thích ❤️" : "Đã bỏ khỏi yêu thích 🤍",
                    Toast.LENGTH_SHORT).show();

            // Hiệu ứng tim
            holder.ivFavorite.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(150)
                    .withEndAction(() ->
                            holder.ivFavorite.animate().scaleX(1f).scaleY(1f).setDuration(100))
                    .start();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivFavorite;
        TextView name, timing, rating, price;
        boolean isFavorite = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timing);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
        }
    }
}
