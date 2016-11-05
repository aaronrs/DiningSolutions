package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Product implements Comparable {

    public final UUID id;
    private final String category;
    public String name;
    public double price;

    public Product(UUID id, String category, String name, double price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    @Override
    public int compareTo(Object another) {
        if (another instanceof Product) {
            return this.name.compareTo(((Product)another).name);
        }
        return 0;
    }

    public String setName(String name) {
        return this.name = name;
    }
}
