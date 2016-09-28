package com.karman.mbazaar.model;

/**
 * Created by Admin on 19-09-2016.
 */
public class Product {
    int product_id;
    String name, description, price, rating, image_url;

    public Product (int product_id, String name, String description, String price, String rating, String image_url) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.image_url = image_url;
    }

    public int getProduct_id () {
        return product_id;
    }

    public void setProduct_id (int product_id) {
        this.product_id = product_id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public String getPrice () {
        return price;
    }

    public void setPrice (String price) {
        this.price = price;
    }

    public String getRating () {
        return rating;
    }

    public void setRating (String rating) {
        this.rating = rating;
    }

    public String getImage_url () {
        return image_url;
    }

    public void setImage_url (String image_url) {
        this.image_url = image_url;
    }
}
