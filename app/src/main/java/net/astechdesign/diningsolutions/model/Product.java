package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product {

    public final UUID id;
    public final String name;
    public final String description;
    public final double price;
    public final String barcode;

    public Product(UUID id, String name, String description, double price, String barcode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }
}
