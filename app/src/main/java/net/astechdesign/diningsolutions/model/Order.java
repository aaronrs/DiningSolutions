package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.List;

public class Order extends Model {

    public int customerId;
    public String invoiceNumber;
    public final DSDDate created;
    public List<OrderItem> orderItems;

    public Order(int id, int customerId, DSDDate created, String invoiceNumber) {
        super(id);
        this.customerId = customerId;
        this.created = created;
        this.invoiceNumber = invoiceNumber;
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

    public void addItem(OrderItem orderItem) {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        orderItems.add(orderItem);
    }


}
