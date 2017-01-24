package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Task extends Model {

    public final TaskType type;
    public final DSDDate date;
    public final DSDTime time;
    public final String customerName;
    public final String customerPhone;
    public final String title;
    public final String description;

    public Task(UUID id, String type, DSDDate date, DSDTime time, String customerName, String customerPhone, String title, String description) {
        super(id);
        this.type = TaskType.valueOf(type);
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }
}
