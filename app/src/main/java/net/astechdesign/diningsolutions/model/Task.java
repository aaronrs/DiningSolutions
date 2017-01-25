package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Task extends Model {

    public final DSDDate date;
    public final DSDTime time;
    public final String title;
    public final String description;

    public Task(UUID id, DSDDate date, DSDTime time, String title, String description) {
        super(id);
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
    }
}
