package com.example.myfoodapp.ui.orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.OrderHistoryAdapter;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.OrderModel;

import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private OrderHistoryAdapter adapter;
    private foodController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        controller = new foodController(requireContext());
        recyclerView = view.findViewById(R.id.order_history_rec);
        emptyView = view.findViewById(R.id.order_history_empty);

        adapter = new OrderHistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        loadOrders();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(requireContext(), R.string.order_requires_login, Toast.LENGTH_SHORT).show();
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        List<OrderModel> orders = controller.getOrdersByUser(userId);
        adapter.submitList(orders);
        boolean isEmpty = orders.isEmpty();
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
