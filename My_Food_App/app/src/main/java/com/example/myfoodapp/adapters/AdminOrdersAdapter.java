package com.example.myfoodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.OrderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder> {

    public interface OrderActionListener {
        void onStatusChanged(OrderModel order, String newStatus);
        void onViewItems(OrderModel order);
    }

    private final List<OrderModel> data = new ArrayList<>();
    private final OrderActionListener listener;
    private final String[] statuses;

    public AdminOrdersAdapter(OrderActionListener listener, String[] statuses) {
        this.listener = listener;
        this.statuses = statuses;
    }

    public void submitList(List<OrderModel> orders) {
        data.clear();
        if (orders != null) {
            data.addAll(orders);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = data.get(position);
        holder.orderId.setText(String.format(Locale.getDefault(), "Order #%d", order.getId()));
        String userName = order.getUserName() != null ? order.getUserName() :
                holder.itemView.getContext().getString(R.string.unknown_user);
        holder.orderUser.setText(userName);
        holder.orderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.statusSpinner.setAdapter(adapter);
        int index = Arrays.asList(statuses).indexOf(order.getStatus());
        holder.statusSpinner.setSelection(Math.max(index, 0), false);

        holder.statusSpinner.setOnItemSelectedListener(null);
        holder.statusSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(position1 -> {
            String selected = statuses[position1];
            if (!selected.equals(order.getStatus()) && listener != null) {
                listener.onStatusChanged(order, selected);
            }
        }));

        holder.viewItemsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewItems(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderUser;
        TextView orderTotal;
        Spinner statusSpinner;
        Button viewItemsButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.admin_order_id);
            orderUser = itemView.findViewById(R.id.admin_order_user);
            orderTotal = itemView.findViewById(R.id.admin_order_total);
            statusSpinner = itemView.findViewById(R.id.admin_order_status);
            viewItemsButton = itemView.findViewById(R.id.btn_view_items);
        }
    }

    private static class SimpleItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        private final OnItemSelected callback;

        SimpleItemSelectedListener(OnItemSelected callback) {
            this.callback = callback;
        }

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
            if (callback != null) {
                callback.onSelected(position);
            }
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
        }
    }

    private interface OnItemSelected {
        void onSelected(int position);
    }
}
