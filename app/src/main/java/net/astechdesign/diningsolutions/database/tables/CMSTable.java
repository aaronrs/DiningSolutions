package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.Model;

import java.util.List;
import java.util.UUID;

public abstract class CMSTable<T extends Model> implements BaseColumns {

    private final String tableName;
    private final java.lang.String createTable;

    protected CMSTable(String tableName, String createTable) {
        this.tableName = tableName;
        this.createTable = createTable;
    }

    public void create(SQLiteDatabase db) {
        try {
            db.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void upgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    protected abstract ContentValues getInsertValues(T model);

    public void addOrUpdate(SQLiteDatabase mDatabase, T model) {
        if (model.getId() == -1) {
            add(mDatabase, model);
        } else {
            update(mDatabase, model);
        }
    }

    private void add(SQLiteDatabase mDatabase, T model) {
        ContentValues insertValues = getInsertValues(model);
        mDatabase.insert(tableName, null, insertValues);
    }

    private void update(SQLiteDatabase mDatabase, T model) {
        ContentValues insertValues = getInsertValues(model);
        mDatabase.update(tableName, insertValues, _ID + " = ?", new String[]{Integer.toString(model.getId())});
    }

    protected String[] id(int id) {
        return new String[]{Integer.toString(id)};
    }
}
