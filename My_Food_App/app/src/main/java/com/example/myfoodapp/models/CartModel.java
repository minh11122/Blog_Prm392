package com.example.myfoodapp.models;

public class CartModel {
    private final int id;
    private final int productId;
    private final int images;
    private final String name;
    private final String price;
    private final String rating;
    private final int quantity;
    private final double unitPrice;

    public CartModel(int id, int productId, int images, String name, String price, String rating, int quantity, double unitPrice) {
        this.id = id;
        this.productId = productId;
        this.images = images;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return unitPrice * quantity;
    }
}
