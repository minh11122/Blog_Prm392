package com.example.myfoodapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodapp.R;
import com.example.myfoodapp.adapters.AdminUsersAdapter;
import com.example.myfoodapp.controller.userController;
import com.example.myfoodapp.models.RoleModel;
import com.example.myfoodapp.models.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity implements AdminUsersAdapter.UserActionListener {

    private userController controller;
    private AdminUsersAdapter adapter;
    private List<RoleModel> roles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.manage_users);
        }

        controller = new userController(this);
        RecyclerView recyclerView = findViewById(R.id.users_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminUsersAdapter(this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_user);
        fab.setOnClickListener(v -> showUserDialog(null));

        loadRoles();
        loadUsers();
    }

    private void loadRoles() {
        roles = controller.getAllRoles();
    }

    private void loadUsers() {
        adapter.submitList(controller.getAllUsers());
    }

    @Override
    public void onEdit(UserModel user) {
        showUserDialog(user);
    }

    @Override
    public void onDelete(UserModel user) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (controller.deleteUser(user.getId())) {
                        Toast.makeText(this, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showUserDialog(@Nullable UserModel user) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_admin_user, null, false);
        EditText inputName = dialogView.findViewById(R.id.input_user_name);
        EditText inputEmail = dialogView.findViewById(R.id.input_user_email);
        EditText inputPassword = dialogView.findViewById(R.id.input_user_password);
        EditText inputPhone = dialogView.findViewById(R.id.input_user_phone);
        EditText inputAddress = dialogView.findViewById(R.id.input_user_address);
        Spinner roleSpinner = dialogView.findViewById(R.id.input_user_role);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getRoleNames());
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        if (user != null) {
            inputName.setText(user.getName());
            inputEmail.setText(user.getEmail());
            inputPhone.setText(user.getPhone());
            inputAddress.setText(user.getAddress());
            inputPassword.setVisibility(View.GONE);
            setSpinnerSelection(roleSpinner, user.getRole());
        } else {
            inputPassword.setVisibility(View.VISIBLE);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(user == null ? R.string.add_user : R.string.edit_user)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();
            String address = inputAddress.getText().toString().trim();
            RoleModel role = getSelectedRole(roleSpinner.getSelectedItemPosition());

            if (name.isEmpty() || email.isEmpty() || role == null) {
                Toast.makeText(this, R.string.validation_required, Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success;
            if (user == null) {
                String password = inputPassword.getText().toString().trim();
                if (password.length() < 6) {
                    Toast.makeText(this, R.string.validation_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                UserModel newUser = new UserModel();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setPhone(phone);
                newUser.setAddress(address);
                newUser.setRole(role);
                success = controller.registerUser(newUser);
            } else {
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                user.setRole(role);
                success = controller.updateUser(user);
            }

            if (success) {
                Toast.makeText(this, R.string.saved_success, Toast.LENGTH_SHORT).show();
                loadUsers();
                dialog.dismiss();
            } else {
                Toast.makeText(this, R.string.action_failed, Toast.LENGTH_SHORT).show();
            }
        }));
        dialog.show();
    }

    private List<String> getRoleNames() {
        List<String> names = new ArrayList<>();
        for (RoleModel role : roles) {
            names.add(role.getName());
        }
        return names;
    }

    private void setSpinnerSelection(Spinner spinner, @Nullable RoleModel role) {
        if (role == null) return;
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getId() == role.getId()) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private RoleModel getSelectedRole(int position) {
        if (position < 0 || position >= roles.size()) {
            return null;
        }
        return roles.get(position);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
