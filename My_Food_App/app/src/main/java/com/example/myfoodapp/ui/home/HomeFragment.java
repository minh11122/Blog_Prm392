package com.example.myfoodapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Hãy chắc chắn bạn import đúng DatabaseHelper
import com.example.myfoodapp.controller.foodController;// Hoặc package đúng của bạn
import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.HomeHorAdapter;
import com.example.myfoodapp.adapters.HomeVerAdapter;
import com.example.myfoodapp.adapters.UpdateVerticalRec;
import com.example.myfoodapp.databinding.FragmentHomeBinding;
import com.example.myfoodapp.models.HomeHorModel;
import com.example.myfoodapp.models.HomeVerModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements UpdateVerticalRec {

    RecyclerView homeHorizontalRec, homeVerticalRec;

    //////////////////////HorizontalRec//////////////////////
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter; // <-- BẠN ĐÃ THIẾU DÒNG KHAI BÁO NÀY

    //////////////////////VerticalRec//////////////////////
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;

    foodController foodController;

    EditText searchBox;
    // Biến để lưu ID của category đầu tiên
    private int defaultCategoryId = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false) ;

        foodController = new foodController(getContext());

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        searchBox = root.findViewById(R.id.editText);

        // 1. Lấy data list ngang từ DB
        homeHorModelList = foodController.getAllCategories();

        // 2. Tạo và gắn Adapter cho list ngang
        homeHorAdapter = new HomeHorAdapter(this, getActivity(), homeHorModelList);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));



        // --- PHẦN CODE LIST DỌC  ---
        if (homeHorModelList != null && !homeHorModelList.isEmpty()) {
            defaultCategoryId = homeHorModelList.get(0).getId();
            homeVerModelList = foodController.getProductsByCategory(defaultCategoryId);
        } else {
            homeVerModelList = new ArrayList<>();
        }

        homeVerAdapter = new HomeVerAdapter(getActivity(), homeVerModelList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));


        setupSearch();
        return root;
    }

    @Override
    public void callBack(int position, ArrayList<HomeVerModel> list) {

        homeVerAdapter = new HomeVerAdapter(getContext(), list);
        homeVerAdapter.notifyDataSetChanged();
        homeVerticalRec.setAdapter(homeVerAdapter);
    }

    private void setupSearch() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                if (query.isEmpty()) {
                    // Nếu search rỗng, tải lại list mặc định
                    ArrayList<HomeVerModel> defaultList = foodController.getProductsByCategory(defaultCategoryId);
                    callBack(-1, defaultList); // Dùng -1 để báo đây ko phải click
                } else {
                    // Nếu có chữ, gọi controller để tìm
                    ArrayList<HomeVerModel> searchResults = foodController.searchProducts(query);
                    callBack(-1, searchResults);
                }
            }
        });
    }
}