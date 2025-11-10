package com.example.myfoodapp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeHorAdapter;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.adapters.UpdateVerticalRec;
import com.example.myfoodapp.controller.foodController;

import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;

public class FirstFragment extends Fragment implements UpdateVerticalRec {

    RecyclerView horRecyclerView, verRecyclerView;
    HomeHorAdapter horAdapter;
    HomeVerAdapter verAdapter;
    foodController foodController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        horRecyclerView = view.findViewById(R.id.featured_hor_rec);
        verRecyclerView = view.findViewById(R.id.featured_ver_rec);

        horRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        verRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

       foodController = new foodController(getContext());

        // Lấy danh sách category
        ArrayList<HomeHorModel> categories = foodController.getAllCategories();
        horAdapter = new HomeHorAdapter(this, getActivity(), categories);
        horRecyclerView.setAdapter(horAdapter);

        // Lấy danh sách sản phẩm của category đầu tiên
        if (!categories.isEmpty()) {
            ArrayList<HomeVerModel> products = foodController.getProductsByCategory(categories.get(0).getId());
            verAdapter = new HomeVerAdapter(getContext(), products, foodController);
            verRecyclerView.setAdapter(verAdapter);
        }

        return view;
    }

    @Override
    public void callBack(int position, ArrayList<HomeVerModel> list) {
        verAdapter = new HomeVerAdapter(getContext(), list, foodController); // <--- Thêm controller
        verRecyclerView.setAdapter(verAdapter);
    }
}

