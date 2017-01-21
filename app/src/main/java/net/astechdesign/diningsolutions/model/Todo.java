package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Todo extends Model {

    public final DSDDate date;
    public final String title;
    public final String details;

    public Todo(UUID id, DSDDate date, String title, String details) {
        super(id);
        this.date = date;
        this.title = title;
        this.details = details;
    }
}
