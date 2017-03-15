package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product extends Model {

    public final String name;
    public final String description;
    public final double price;
    public final String barcode;

    private Product(UUID id, String name, String description, double price, String barcode) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Product create(String name, double price) {
        return create(null, name, null, price, null);
    }

    public static Product create(String name, String description, double price, String barcode) {
        return new Product(null, name, description, price, barcode);
    }

    public static Product create(UUID id, String name, String description, double price, String barcode) {
        return new Product(id, name, description, price, barcode);
    }
}
