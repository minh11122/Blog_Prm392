package com.example.myfoodapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.CartAdapter;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.CartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyCartFragment extends Fragment {

    private final List<CartModel> list = new ArrayList<>();
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private TextView totalAmountText;
    private TextView emptyCartText;
    private Button makeOrderButton;
    private foodController controller;

    public MyCartFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_cart, container, false);

        controller = new foodController(requireContext());

        recyclerView = view.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        totalAmountText = view.findViewById(R.id.total_amount);
        emptyCartText = view.findViewById(R.id.empty_cart_text);
        makeOrderButton = view.findViewById(R.id.buttonRegister);

        cartAdapter = new CartAdapter(list, cartItem -> {
            controller.deleteCartItem(cartItem.getId());
            Toast.makeText(requireContext(), R.string.cart_item_removed, Toast.LENGTH_SHORT).show();
            loadCartItems();
        });
        recyclerView.setAdapter(cartAdapter);

        makeOrderButton.setOnClickListener(v -> createOrder());

        loadCartItems();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartItems();
    }

    private void loadCartItems() {
        List<CartModel> cartItems = controller.getCartItems();
        cartAdapter.updateData(cartItems);
        updateTotal(cartItems);
        toggleEmptyState(cartItems.isEmpty());
    }

    private void updateTotal(List<CartModel> cartItems) {
        double total = 0;
        for (CartModel item : cartItems) {
            total += item.getTotalPrice(); // ĐÃ CÓ TRONG CartModel
        }

        if (totalAmountText != null) {
            totalAmountText.setText(String.format(Locale.getDefault(), "$%.2f", total));
        }
    }

    private void toggleEmptyState(boolean isEmpty) {
        if (recyclerView == null || emptyCartText == null) {
            return;
        }

        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyCartText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        if (makeOrderButton != null) {
            makeOrderButton.setEnabled(!isEmpty);
        }
    }

    private void createOrder() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(requireContext(), R.string.order_requires_login, Toast.LENGTH_SHORT).show();
            return;
        }

        long orderId = controller.placeOrder(userId);
        if (orderId > 0) {
            Toast.makeText(requireContext(), R.string.order_created_success, Toast.LENGTH_SHORT).show();
            loadCartItems();
        } else {
            Toast.makeText(requireContext(), R.string.order_created_fail, Toast.LENGTH_SHORT).show();
        }
    }
}
