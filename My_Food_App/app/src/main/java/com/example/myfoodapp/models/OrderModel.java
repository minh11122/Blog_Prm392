package com.example.myfoodapp.models;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private int id;
    private int userId;
    private String userName;
    private double totalAmount;
    private String status;
    private long createdAt;
    private List<OrderItemModel> items = new ArrayList<>();

    public OrderModel() {
    }

    public OrderModel(int id, int userId, String userName, double totalAmount, String status, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items != null ? items : new ArrayList<>();
    }
}
