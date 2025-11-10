package com.example.myfoodapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.AdminOrdersAdapter;
import com.example.myfoodapp.controller.foodController;
import com.example.myfoodapp.models.OrderItemModel;
import com.example.myfoodapp.models.OrderModel;

import java.util.List;
import java.util.Locale;

public class ManageOrdersActivity extends AppCompatActivity implements AdminOrdersAdapter.OrderActionListener {

    private foodController controller;
    private AdminOrdersAdapter adapter;
    private String[] statuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.manage_orders);
        }

        controller = new foodController(this);
        statuses = new String[]{
                foodController.STATUS_PENDING,
                foodController.STATUS_RECEIVED,
                foodController.STATUS_COMPLETED,
                foodController.STATUS_CANCELLED
        };

        RecyclerView recyclerView = findViewById(R.id.orders_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminOrdersAdapter(this, statuses);
        recyclerView.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {
        adapter.submitList(controller.getAllOrders());
    }

    @Override
    public void onStatusChanged(OrderModel order, String newStatus) {
        controller.updateOrderStatus(order.getId(), newStatus);
        Toast.makeText(this, R.string.saved_success, Toast.LENGTH_SHORT).show();
        loadOrders();
    }

    @Override
    public void onViewItems(OrderModel order) {
        List<OrderItemModel> items = controller.getOrderItems(order.getId());
        if (items.isEmpty()) {
            Toast.makeText(this, R.string.cart_empty_state, Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (OrderItemModel item : items) {
            builder.append(String.format(Locale.getDefault(),
                    "%s x%d - $%.2f\n", item.getProductName(), item.getQuantity(), item.getPrice()));
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.order_items)
                .setMessage(builder.toString())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
