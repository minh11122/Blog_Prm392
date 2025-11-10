package com.example.myfoodapp.models;

public class HomeVerModel {

    private int id;           // ID món ăn trong DB
    private int image;
    private String name;
    private String timing;
    private String rating;
    private String price;

    private boolean isFavorite = false; // trạng thái yêu thích

    // Constructor mặc định
    public HomeVerModel() {}

    // Constructor có id
    public HomeVerModel(int id, int image, String name, String timing, String rating, String price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.timing = timing;
        this.rating = rating;
        this.price = price;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getImage() { return image; }
    public void setImage(int image) { this.image = image; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTiming() { return timing; }
    public void setTiming(String timing) { this.timing = timing; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { this.isFavorite = favorite; }
}
