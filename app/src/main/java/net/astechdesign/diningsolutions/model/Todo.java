package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Todo extends Model {

    public final String title;
    public final String details;

    public Todo(UUID id, String title, String details) {
        super(id);
        this.title = title;
        this.details = details;
    }
}
