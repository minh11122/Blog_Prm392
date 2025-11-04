package com.example.myfoodapp.models;

public class DailyMealModel {
    int image;
    String Name;
    String discount;

    String description;
    String type;


    public DailyMealModel(int image, String name, String discount, String description, String type) {
        this.image = image;
        Name = name;
        this.discount = discount;
        this.description = description;
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
