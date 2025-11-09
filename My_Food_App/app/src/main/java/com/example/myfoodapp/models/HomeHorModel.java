package com.example.myfoodapp.models;

import com.example.myfoodapp.adapters.HomeHorAdapter;

public class HomeHorModel  {

    int id;
    int image;
    String name;

    public HomeHorModel() {
    }

    public HomeHorModel(int id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
