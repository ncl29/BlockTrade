package com.example.blocktradefinal;

import java.util.Objects;

public class Product {

    private String name;
    private String location;
    private String price;
    private String owner;
    private String category;
    private String tradeType;
    private String imageUriString;
    private int imageResId;
    private String productId;

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

    public Product() {
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return name.equals(product.name) && location.equals(product.location)
                && price.equals(product.price) && owner.equals(product.owner)
                && category.equals(product.category) && tradeType.equals(product.tradeType)
                && imageUriString.equals(product.imageUriString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, price, owner, category, tradeType, imageUriString);
    }

}