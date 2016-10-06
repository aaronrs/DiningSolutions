package net.astechdesign.diningsolutions.model;

public class Product {
    private final Long id;
    private final String category;
    public final String name;

    public Product(Long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }
}
