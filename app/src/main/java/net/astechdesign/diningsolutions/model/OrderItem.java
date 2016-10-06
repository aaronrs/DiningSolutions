package net.astechdesign.diningsolutions.model;

public class OrderItem {
    public final String name;
    public final double price;
    public final int quantity;
    public final String batch;

    public OrderItem(String name, double price, int quantity, String batch) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.batch = batch;
    }

    public double getTotal() {
        return 0;
    }

    public static OrderItem create(String name, double price, int quantity, String batch) {
        return new OrderItem(name, price, quantity, batch);
    }
}
