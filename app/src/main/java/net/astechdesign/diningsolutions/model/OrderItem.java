package net.astechdesign.diningsolutions.model;

public class OrderItem extends Model {

    public final int orderId;
    public final String name;
    public final double price;
    public final int quantity;
    public final String batch;
    public final DSDDate deliveryDate;

    public OrderItem(int id, int orderId, String name, double price, int quantity, String batch, DSDDate deliveryDate) {
        super(id);
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.batch = batch;
        this.deliveryDate = deliveryDate;
    }
}
