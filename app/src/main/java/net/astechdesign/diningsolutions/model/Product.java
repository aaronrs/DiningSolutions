package net.astechdesign.diningsolutions.model;

public class Product extends Model {

    public final int id;
    public final String name;
    public final String description;
    public final double price;
    public final String barcode;
    private final int deleted;

    public Product(int id, String name, String description, double price, String barcode, int deleted) {
        super(id);
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted == 1;
    }
}
