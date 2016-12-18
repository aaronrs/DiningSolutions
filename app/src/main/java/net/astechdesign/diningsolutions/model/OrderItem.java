package net.astechdesign.diningsolutions.model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderItem extends Model {
    public final UUID id;
    public final UUID orderId;
    public final String name;
    public final double price;
    public final int quantity;
    public final String batch;
    public final DSDDate deliveryDate;

    public OrderItem(UUID id, UUID orderId, String name, double price, int quantity, String batch, DSDDate deliveryDate) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.batch = batch;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public List<Model> getChildren() {
        return Collections.emptyList();
    }
}
