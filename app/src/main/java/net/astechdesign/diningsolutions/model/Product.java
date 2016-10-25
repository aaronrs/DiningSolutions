package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product {

    public final UUID id;
    private final String category;
    public final String name;
    public final double price;

    public Product(UUID id, String category, String name, double price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
    }
}
