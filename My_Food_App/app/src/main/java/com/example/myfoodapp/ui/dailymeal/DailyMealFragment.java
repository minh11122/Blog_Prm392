package com.example.myfoodapp.ui.dailymeal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.DailyMealAdapter;
import com.example.myfoodapp.models.DailyMealModel;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {

    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModels;
    DailyMealAdapter dailyMealAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.daily_meal_fragment, container, false);

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dailyMealModels = new ArrayList<>();
        dailyMealModels.add(new DailyMealModel(R.drawable.breakfast, "Breakfast","30% OFF", "Description Description","breakfast"));
        dailyMealModels.add(new DailyMealModel(R.drawable.fav3, "Lunch","20% OFF", "Description Description","lunch"));
        dailyMealModels.add(new DailyMealModel(R.drawable.dinner, "Dinner","50% OFF", "Description Description","dinner"));
        dailyMealModels.add(new DailyMealModel(R.drawable.icecream4, "Sweets","39% OFF", "Description Description","sweets"));
        dailyMealModels.add(new DailyMealModel(R.drawable.coffe, "Coffe","20% OFF", "Description Description","coffe"));

        dailyMealAdapter = new DailyMealAdapter(getContext(), dailyMealModels);
        recyclerView.setAdapter(dailyMealAdapter);
        dailyMealAdapter.notifyDataSetChanged();

        return root;
    }
}
