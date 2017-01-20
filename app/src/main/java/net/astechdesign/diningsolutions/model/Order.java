package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order extends Model {

    public String invoiceNumber;
    public final DSDDate created;
    public List<OrderItem> orderItems = new ArrayList<>();

    public Order(UUID id, DSDDate created, String invoiceNumber) {
        super(id);
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

    @Override
    public String toString() {
        return "Invoice No. " + invoiceNumber + " - " + created.toString();
    }

    public void addItem(Product product, double price, int quantity, String batch, DSDDate deliveryDate) {
        addItem(new OrderItem(null, product.name, price, quantity, batch, deliveryDate));
    }
}
