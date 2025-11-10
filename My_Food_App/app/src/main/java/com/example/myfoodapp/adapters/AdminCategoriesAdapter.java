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
import com.example.myfoodapp.models.HomeHorModel;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.ViewHolder> {

    public interface CategoryActionListener {
        void onEdit(HomeHorModel category);
        void onDelete(HomeHorModel category);
    }

    private final List<HomeHorModel> categories = new ArrayList<>();
    private final CategoryActionListener listener;

    public AdminCategoriesAdapter(CategoryActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<HomeHorModel> data) {
        categories.clear();
        if (data != null) {
            categories.addAll(data);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeHorModel category = categories.get(position);
        holder.name.setText(category.getName());
        holder.image.setImageResource(category.getImage());
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(category);
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        Button editButton;
        Button deleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_image);
            name = itemView.findViewById(R.id.category_name);
            editButton = itemView.findViewById(R.id.btn_edit_category);
            deleteButton = itemView.findViewById(R.id.btn_delete_category);
        }
    }
}
