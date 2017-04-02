package net.astechdesign.diningsolutions.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Order extends Model {

    public String invoiceNumber;
    public final UUID customerId;
    public final DSDDate created;
    public List<OrderItem> orderItems = new ArrayList<>();

    public Order(UUID id, UUID customerId, DSDDate created, String invoiceNumber) {
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

    @Override
    public String toString() {
        return "Invoice No. " + invoiceNumber + " - " + (created != null ? created.getDisplayDate() : "");
    }

    public void addItem(Product product, double price, int quantity, String batch, DSDDate deliveryDate) {
        addItem(new OrderItem(null, product.name, price, quantity, batch, deliveryDate));
    }

    public double total() {
        double total = 0;
        for (OrderItem item: orderItems) {
            total += item.price * item.quantity;
        }
        return total;
    }

    public List<OrderItem> getDeliveryItems() {
        List<OrderItem> deliveryItems = new ArrayList<>();
        for (OrderItem item : orderItems ) {
            if (item.deliveryDate.futureDate()) {
                deliveryItems.add(item);
            }
        }
        return deliveryItems;
    }

    public static List<Order> emptyOrderList() {
        Order order = new Order(null, null, null, "No Orders");
        return Arrays.asList(new Order[]{order});
    }

    public static Order create(String invoiceNumber) {
        return new Order(null, null, DSDDate.create(), invoiceNumber);
    }
}
