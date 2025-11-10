package com.example.myfoodapp.ui.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.models.HomeVerModel;
import com.example.myfoodapp.controller.FoodDAO;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {

    private RecyclerView favoriteRecycler;
    private LinearLayout emptyLayout;
    private HomeVerAdapter adapter;
    private FoodDAO foodDAO;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        favoriteRecycler = root.findViewById(R.id.favorite_recycler);
        emptyLayout = root.findViewById(R.id.empty_layout);

        favoriteRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        foodDAO = new FoodDAO(getContext());
        ArrayList<HomeVerModel> favoriteList = foodDAO.getFavoriteFoods(); // Bỏ comment & định nghĩa biến

        if (favoriteList != null && !favoriteList.isEmpty()) {
            adapter = new HomeVerAdapter(getContext(), favoriteList, foodDAO); // truyền DAO
            favoriteRecycler.setAdapter(adapter);
            emptyLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }

        return root;
    }
}

