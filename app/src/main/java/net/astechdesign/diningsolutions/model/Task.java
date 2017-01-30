package net.astechdesign.diningsolutions.model;

import java.util.List;
import java.util.UUID;

public class Task extends Model {

    public final DSDDate date;
    public final String title;
    public final String description;

    public Task(UUID id, DSDDate date, String title, String description) {
        super(id);
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public static Task deliveryTask(Customer customer, List<OrderItem> items) {
        String text = "%s of %s - Phone: %s";
         String description = String.format(text, customer.name, customer.address.toString(), customer.phone.number);
        return new Task(null, items.get(0).deliveryDate, "Delivery", description);
    }
}
