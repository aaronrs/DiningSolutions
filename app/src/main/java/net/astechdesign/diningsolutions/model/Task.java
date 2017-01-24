package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Task extends Model {

    public final TaskType type;
    public final DSDDate date;
    public final DSDTime time;
    public final Customer customer;
    public final String title;
    public final String description;

    public Task(UUID id, DSDDate date, DSDTime time, Customer customer, String title, String description, String type) {
        super(id);
        this.type = TaskType.valueOf(type);
        this.date = date;
        this.time = time;
        this.customer = customer;
        this.title = title;
        this.description = description;
    }
}
