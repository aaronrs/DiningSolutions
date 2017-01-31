package net.astechdesign.diningsolutions.model;

import java.util.List;
import java.util.UUID;

public class Task extends Model {

    private final UUID id;
    public final DSDDate date;
    public final String title;
    public final String description;
    public final UUID customerId;

    public Task(UUID id, DSDDate date, String title, String description, UUID customerId) {
        super(id);
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.customerId = customerId;
    }

    public Task(UUID id, DSDDate date, String title, String description) {
        this(id, date, title, description, null);
    }

    public static Task deliveryTask(Customer customer, List<OrderItem> orderItems) {
        String items = "";
        String delim = "";
        for (OrderItem item : orderItems) {
            items += delim;
            items += item.name;
            delim = ", ";
        }
        String description = String.format("%s : %s", customer.name, items);
        return new Task(null, orderItems.get(0).deliveryDate, "Delivery", description, customer.getId());
    }
}
