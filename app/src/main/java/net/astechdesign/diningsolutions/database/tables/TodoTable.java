package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Todo;

import java.util.UUID;

public class TodoTable  extends CMSTable<Todo>{

    private static final String TABLE_NAME = "todos";

    public static final String TODO_CONTENT = "content";
    public static final String TODO_DETAILS = "details";

    private static final String CREATE_TABLE =
            TODO_CONTENT + " TEXT," +
            TODO_DETAILS + " TEXT";

    public TodoTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected ContentValues getInsertValues(Todo model) {
        return null;
    }

    @Override
    protected String getParentIdColumn() {
        return null;
    }
}
