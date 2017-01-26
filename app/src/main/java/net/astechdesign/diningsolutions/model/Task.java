package net.astechdesign.diningsolutions.model;

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
}
