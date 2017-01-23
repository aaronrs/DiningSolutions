package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;

import net.astechdesign.diningsolutions.model.Task;

public class TaskTable extends CMSTable<Task>{

    private static final String TABLE_NAME = "todos";

    public static final String TODO_CONTENT = "content";
    public static final String TODO_DETAILS = "details";

    private static final String CREATE_TABLE =
            TODO_CONTENT + " TEXT," +
            TODO_DETAILS + " TEXT";

    public TaskTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected ContentValues getInsertValues(Task model) {
        return null;
    }

    @Override
    protected String getParentIdColumn() {
        return null;
    }
}
