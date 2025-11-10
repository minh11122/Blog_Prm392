package com.example.myfoodapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.ViewHolder> {

    public interface UserActionListener {
        void onEdit(UserModel user);
        void onDelete(UserModel user);
    }

    private final List<UserModel> data = new ArrayList<>();
    private final UserActionListener listener;

    public AdminUsersAdapter(UserActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<UserModel> users) {
        data.clear();
        if (users != null) {
            data.addAll(users);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = data.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole() != null ? user.getRole().getName() : "");

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(user);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView role;
        Button editButton;
        Button deleteButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            role = itemView.findViewById(R.id.user_role);
            editButton = itemView.findViewById(R.id.btn_edit_user);
            deleteButton = itemView.findViewById(R.id.btn_delete_user);
        }
    }
}
