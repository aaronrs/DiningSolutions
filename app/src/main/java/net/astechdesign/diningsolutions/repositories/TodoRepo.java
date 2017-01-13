package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.TodoTable;
import net.astechdesign.diningsolutions.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TodoRepo {

    private static TodoRepo repo;
    private final TodoTable todoTable;
    private final SQLiteDatabase mDatabase;
    private final Context mContext;

    private List<Todo> todos = new ArrayList<>();

    public static TodoRepo get(Context context) {
        if (repo == null) {
            repo = new TodoRepo(context.getApplicationContext());
        }
        return repo;
    }

    public TodoRepo(Context context) {
        mContext = context;
        mDatabase = new DBHelper(mContext).getWritableDatabase();
        this.todoTable = DBHelper.getTodoTable();
    }

    public Todo get(UUID uuid) {
        return new Todo(0);
    }

    public List<Todo> get() {
        return todos;
    }
}
