package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order extends Model {
    public UUID id;
    public UUID customerId;
    public final DSDDate created;
    public final List<OrderItem> orderItems;
    public String invoiceNumber;

    public Order(UUID id, UUID customerId, DSDDate created, String invoiceNumber, List<OrderItem> orderItems) {
        this.id = id;
        this.customerId = customerId;
        this.created = created;
        this.invoiceNumber = invoiceNumber;
        this.orderItems = orderItems;
    }

    public static Order create(UUID customerId) {
        return new Order(UUID.randomUUID(), customerId, new DSDDate(), "000123", new ArrayList<OrderItem>());
    }

    public int totalQuantity() {
        int quantity = 0;
        for (OrderItem item : orderItems) {
            quantity += item.quantity;
        }
        return quantity;
    }

    public double totalCost() {
        double cost = 0;
        for (OrderItem item : orderItems) {
            cost += item.price * item.quantity;
        }
        return cost;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public List<OrderItem> getChildren() {
        return orderItems;
    }
}
