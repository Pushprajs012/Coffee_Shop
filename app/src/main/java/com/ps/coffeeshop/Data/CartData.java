package com.ps.coffeeshop.Data;

public class CartData {
    private String name;
    private int price;
    private String size;
    private String image;
    private String description;
    private int quantity;



    private String product_id;

    public CartData() {
    }

    public CartData(String product_id,String name, int price, String size, String image, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.image = image;
        this.quantity = quantity;
        this.product_id=product_id;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
