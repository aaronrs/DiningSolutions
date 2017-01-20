package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product extends Model {

    public final String name;
    public final String description;
    public final double price;
    public final String barcode;
    private final int deleted;

    public Product(UUID id, String name, String description, double price, String barcode, int deleted) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted == 1;
    }

    @Override
    public String toString() {
        return name;
    }
}
