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
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminFoodsAdapter extends RecyclerView.Adapter<AdminFoodsAdapter.ViewHolder> {

    public interface FoodActionListener {
        void onEdit(HomeVerModel model);
        void onDelete(HomeVerModel model);
    }

    private final List<HomeVerModel> items = new ArrayList<>();
    private final FoodActionListener listener;

    public AdminFoodsAdapter(FoodActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<HomeVerModel> data) {
        items.clear();
        if (data != null) {
            items.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeVerModel model = items.get(position);
        holder.image.setImageResource(model.getImage());
        holder.name.setText(model.getName());
        holder.price.setText(String.format(Locale.getDefault(), "%s", model.getPrice()));
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(model);
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(model);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView price;
        Button editButton;
        Button deleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.food_image);
            name = itemView.findViewById(R.id.food_name);
            price = itemView.findViewById(R.id.food_price);
            editButton = itemView.findViewById(R.id.btn_edit_food);
            deleteButton = itemView.findViewById(R.id.btn_delete_food);
        }
    }
}
