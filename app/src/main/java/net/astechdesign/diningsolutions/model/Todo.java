package net.astechdesign.diningsolutions.model;

import java.util.UUID;

public class Todo extends Model {
    public String content = "Content";
    public String details = "Visit Joe Blogss and take Turkey Supremes";
    public UUID id = UUID.randomUUID();

    public Todo(int id) {
        super(id);
    }
}
