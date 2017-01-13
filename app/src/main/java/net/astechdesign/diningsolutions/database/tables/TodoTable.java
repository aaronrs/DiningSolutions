package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Todo;

public class TodoTable  extends CMSTable<Todo>{

    private static final String TABLE_NAME = "todos";
    private static final String CREATE_TABLE = "";

    public TodoTable(Context mContext) {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    public void initDb(SQLiteDatabase db) {
    }

    @Override
    public void upgrade(int oldVersion, int newVersion) {

    }

    @Override
    protected ContentValues getInsertValues(Todo model) {
        return null;
    }
}
