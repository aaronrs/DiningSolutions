package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class OrderItem extends Model {

    public final String name;
    public final double price;
    public final int quantity;
    public final String batch;
    public final DSDDate deliveryDate;

    public OrderItem(UUID id, String name, double price, int quantity, String batch, DSDDate deliveryDate) {
        super(id);
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.batch = batch;
        this.deliveryDate = deliveryDate;
    }

    public double cost() {
        return price * quantity;
    }
}
