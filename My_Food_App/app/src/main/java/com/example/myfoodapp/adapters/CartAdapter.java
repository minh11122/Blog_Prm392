package com.example.myfoodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.models.CartModel;
import com.example.myfoodapp.R;

import java.util.ArrayList;
import java.util.List;

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel cartItem = list.get(position);
        holder.imageView.setImageResource(cartItem.getImages());
        holder.name.setText(cartItem.getName());
        holder.rating.setText(cartItem.getRating());
        holder.price.setText(cartItem.getPrice());
        holder.quantity.setText("x" + cartItem.getQuantity());
        holder.removeButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onRemove(cartItem);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name,rating,price,quantity;
        View removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            rating = itemView.findViewById(R.id.detailed_rating);
            price = itemView.findViewById(R.id.textView8);
            quantity = itemView.findViewById(R.id.detailed_quantity);
            removeButton = itemView.findViewById(R.id.cart_item_remove);
        }
    }
}
