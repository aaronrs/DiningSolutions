package net.astechdesign.diningsolutions.repositories;

import net.astechdesign.diningsolutions.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoRepo {

    private static final TodoRepo instance = new TodoRepo();
    private List<Todo> todos = new ArrayList<>();

    public TodoRepo() {
        todos.add(new Todo());
    }

    public static Todo get(UUID uuid) {
        return instance.getTodo(uuid);
    }

    public static List<Todo> get() {
        return instance.getTodos();
    }

    private Todo getTodo(UUID uuid) {
        return new Todo();
    }

    public List<Todo> getTodos() {
        return todos;
    }
}
