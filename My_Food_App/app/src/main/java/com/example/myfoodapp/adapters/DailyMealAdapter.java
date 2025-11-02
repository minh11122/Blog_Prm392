package com.example.myfoodapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.DailyMealModel;

import java.util.List;

public class DailyMealAdapter extends RecyclerView.Adapter<DailyMealAdapter.ViewHolder> {

    Context context;
    List<DailyMealModel> list;

    public DailyMealAdapter(Context context, List<DailyMealModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyMealAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyMealModel meal = list.get(position);

        holder.imageView.setImageResource(meal.getImage());
        holder.name.setText(meal.getName());
        holder.discount.setText(meal.getDiscount());
        holder.description.setText(meal.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, discount, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView1);
            name = itemView.findViewById(R.id.textView9);
            description = itemView.findViewById(R.id.textView10);
            discount = itemView.findViewById(R.id.discount);
        }
    }
}
