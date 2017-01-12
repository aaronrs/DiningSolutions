package net.astechdesign.diningsolutions.repositories;

import android.content.Context;

import net.astechdesign.diningsolutions.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoRepo {

    private List<Todo> todos = new ArrayList<>();

    TodoRepo(Context context) {
        todos.add(new Todo());
    }

    public Todo get(UUID uuid) {
        return new Todo();
    }

    public List<Todo> get() {
        return todos;
    }
}
