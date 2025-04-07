package com.example.blocktradefinal;

public class Product {

    private String name;
    private String location;
    private String price;
    private String owner;
    private String category;
    private String tradeType;
    private String imageUriString;
    private int imageResId;

    // Constructor
    public Product(String name, String location, String price, String owner, String category, String tradeType, String imageUriString, int imageResId) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.owner = owner;
        this.category = category;
        this.tradeType = tradeType;
        this.imageUriString = imageUriString;
        this.imageResId = imageResId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getImageUriString() {
        return imageUriString;
    }

    public void setImageUriString(String imageUriString) {
        this.imageUriString = imageUriString;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}