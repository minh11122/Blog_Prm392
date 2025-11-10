package com.example.myfoodapp.models;

public class RoleModel {
    private int id;
    private String name; // Ví dụ: "Admin", "Customer"

    public RoleModel() {
    }

    public RoleModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
