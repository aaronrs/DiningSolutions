package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product {

    public final UUID id;
    private final String category;
    private String name;
    private double price;

    public Product(UUID id, String category, String name, double price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setPrice(String price) {
        this.price = Double.parseDouble(price);
    }

    public double getPrice() {
        return price;
    }
}
