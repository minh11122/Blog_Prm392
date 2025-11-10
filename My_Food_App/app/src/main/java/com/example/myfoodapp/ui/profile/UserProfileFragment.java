package com.example.myfoodapp.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfoodapp.R;
import com.example.myfoodapp.controller.userController;
import com.example.myfoodapp.models.UserModel;

public class UserProfileFragment extends Fragment {

    private ImageView avatar;
    private TextView nameView;
    private TextView emailView;
    private TextView roleView;
    private TextView emptyState;
    private EditText phoneInput;
    private EditText addressInput;
    private Button updateButton;
    private userController controller;
    private int userId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        controller = new userController(requireContext());
        avatar = view.findViewById(R.id.profile_avatar);
        nameView = view.findViewById(R.id.profile_name);
        emailView = view.findViewById(R.id.profile_email);
        roleView = view.findViewById(R.id.profile_role);
        emptyState = view.findViewById(R.id.profile_empty_state);
        phoneInput = view.findViewById(R.id.input_phone);
        addressInput = view.findViewById(R.id.input_address);
        updateButton = view.findViewById(R.id.button_update_profile);
        updateButton.setOnClickListener(v -> updateProfile());
        loadProfile();
        return view;
    }

    private void loadProfile() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            showEmptyState(true);
            return;
        }
        UserModel user = controller.getUserById(userId);
        if (user == null) {
            showEmptyState(true);
            return;
        }
        showEmptyState(false);
        nameView.setText(user.getName());
        emailView.setText(user.getEmail());
        roleView.setText(user.getRole() != null ? user.getRole().getName() : "");
        phoneInput.setText(user.getPhone());
        addressInput.setText(user.getAddress());
    }

    private void showEmptyState(boolean isEmpty) {
        int visibility = isEmpty ? View.GONE : View.VISIBLE;
        avatar.setVisibility(visibility);
        nameView.setVisibility(visibility);
        emailView.setVisibility(visibility);
        roleView.setVisibility(visibility);
        phoneInput.setVisibility(visibility);
        addressInput.setVisibility(visibility);
        updateButton.setVisibility(visibility);
        emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void updateProfile() {
        if (userId == -1) {
            Toast.makeText(requireContext(), R.string.profile_not_logged_in, Toast.LENGTH_SHORT).show();
            return;
        }
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(address)) {
            Toast.makeText(requireContext(), R.string.validation_required, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean success = controller.updateUserContact(userId, phone, address);
        if (success) {
            Toast.makeText(requireContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), R.string.profile_update_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
