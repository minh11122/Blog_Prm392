package com.example.myfoodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.DetailedDailyModel;

import java.util.List;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {

    private final List<DetailedDailyModel> list;
    private OnAddToCartClickListener addToCartListener;

    // Interface để gửi sự kiện ra Activity
    public interface OnAddToCartClickListener {
        void onAddToCart(DetailedDailyModel item);
    }

    public DetailedDailyAdapter(List<DetailedDailyModel> list) {
        this.list = list;
    }

    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.addToCartListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailed_daily_meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailedDailyModel item = list.get(position);

        holder.imageView.setImageResource(item.getImage());
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.rating.setText(item.getRating());
        holder.price.setText("$" + item.getPrice());
        holder.timing.setText(item.getTiming());

        // XỬ LÝ NÚT ADD TO CART
        holder.addToCartButton.setOnClickListener(v -> {
            if (addToCartListener != null) {
                addToCartListener.onAddToCart(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // VIEW HOLDER
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price, description, timing, rating;
        Button addToCartButton; // NÚT ADD

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);        // Tên món
            price = itemView.findViewById(R.id.detailed_price);      // Giá
            description = itemView.findViewById(R.id.detailed_des);
            rating = itemView.findViewById(R.id.detailed_rating);
            timing = itemView.findViewById(R.id.detailed_timing);
            addToCartButton = itemView.findViewById(R.id.btn_add_to_cart); // NÚT
        }
    }
}