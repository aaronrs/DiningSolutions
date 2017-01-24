package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.TaskTable;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.DSDTime;
import net.astechdesign.diningsolutions.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskRepo {

    private static TaskRepo repo;
    private final TaskTable taskTable;
    private final SQLiteDatabase mDatabase;
    private final Context mContext;

    private List<Task> tasks = new ArrayList<>();

    public static TaskRepo get(Context context) {
        if (repo == null) {
            repo = new TaskRepo(context.getApplicationContext());
        }
        return repo;
    }

    public TaskRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        this.taskTable = DBHelper.getTaskTable();
    }

    public Task get(UUID id) {
        return new Task(id, new DSDDate(), new DSDTime(), null, "Test", "Data", "Visit");
    }

    public List<Task> get() {
        return tasks;
    }
}
