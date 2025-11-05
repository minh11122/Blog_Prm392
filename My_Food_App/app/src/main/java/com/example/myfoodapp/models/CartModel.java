package com.example.myfoodapp.models;

public class CartModel {
    int images;
    String name;
    String price;
    String rating;

    public CartModel(int images, String name, String price, String rating) {
        this.images = images;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
