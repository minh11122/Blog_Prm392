package com.example.myfoodapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {
    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());

        if (check) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizaa 1", "10:00-23:00", "4.9", "Min-$34"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizaa 2", "10:00-23:00", "4.9", "Min-$34"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizaa 3", "10:00-23:00", "4.9", "Min-$34"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizaa 4", "10:00-23:00", "4.9", "Min-$34"));

            updateVerticalRec.callBack(position, homeVerModels);
            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                row_index = currentPosition;
                notifyDataSetChanged();

                ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                if (currentPosition == 0) {
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizaa 1", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizaa 2", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizaa 3", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizaa 4", "10:00-23:00", "4.9", "Min-$34"));
                } else if (currentPosition == 1) {
                    homeVerModels.add(new HomeVerModel(R.drawable.burger1, "Burrger 1", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger2, "Burrger 2", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger4, "Burrger 4", "10:00-23:00", "4.9", "Min-$34"));
                } else if (currentPosition == 2) {
                    homeVerModels.add(new HomeVerModel(R.drawable.fries1, "Fries 1", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries2, "Fries 2", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries3, "Fries 3", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries4, "Fries 4", "10:00-23:00", "4.9", "Min-$34"));
                } else if (currentPosition == 3) {
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream1, "Ice Cream 1", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream2, "Ice Cream 2", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream3, "Ice Cream 3", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream4, "Ice Cream 4", "10:00-23:00", "4.9", "Min-$34"));
                } else if (currentPosition == 4) {
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich1, "Sandwich 1", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich2, "Sandwich 2", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich3, "Sandwich 3", "10:00-23:00", "4.9", "Min-$34"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich4, "Sandwich 4", "10:00-23:00", "4.9", "Min-$34"));
                }

                updateVerticalRec.callBack(currentPosition, homeVerModels);
            }
        });

        if (select) {
            if (position == 0) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
                select = false;
            }
        } else {
            if (row_index == position) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
            } else {
                holder.cardView.setBackgroundResource(R.drawable.default_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}