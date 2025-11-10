package com.example.myfoodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.CartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public interface CartActionListener {
        void onRemove(CartModel cartModel);
    }

    private final List<CartModel> list;
    private final CartActionListener actionListener;

    public CartAdapter(List<CartModel> list, CartActionListener actionListener) {
        this.list = list != null ? list : new ArrayList<>();
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel item = list.get(position);

        holder.imageView.setImageResource(item.getImages());
        holder.name.setText(item.getName());
        holder.rating.setText(item.getRating());
        holder.quantity.setText("x" + item.getQuantity());

        // HIỂN THỊ TỔNG TIỀN = đơn giá × số lượng
        double totalPrice = item.getUnitPrice() * item.getQuantity();
        holder.price.setText(String.format(Locale.getDefault(), "$%.2f", totalPrice));

        // NÚT XÓA (ImageButton)
        holder.removeButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onRemove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<CartModel> newItems) {
        list.clear();
        if (newItems != null) {
            list.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    // VIEW HOLDER
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, rating, price, quantity;
        ImageButton removeButton; // ĐÚNG KIỂU

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            rating = itemView.findViewById(R.id.detailed_rating);
            price = itemView.findViewById(R.id.textView8); // TỔNG TIỀN
            quantity = itemView.findViewById(R.id.detailed_quantity);
            removeButton = itemView.findViewById(R.id.cart_item_remove); // ImageButton
        }
    }
}