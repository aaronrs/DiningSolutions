package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.model.Model;

public abstract class CMSTable<T extends Model> implements BaseColumns {

    private final String tableName;
    private final java.lang.String createTable;

    protected CMSTable(String tableName, String createTable) {
        this.tableName = tableName;
        this.createTable = createTable;
    }

    public final void create(SQLiteDatabase db) {
        try {
            db.execSQL(createTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void upgrade(int oldVersion, int newVersion);

    protected abstract ContentValues getInsertValues(T model);

    public abstract void addOrUpdate(SQLiteDatabase db, T model);

    protected void addOrUpdateModel(SQLiteDatabase db, T model) {
        if (model.getId() == -1) {
            add(db, model);
        } else {
            update(db, model);
        }
    }

    private void add(SQLiteDatabase db, T model) {
        ContentValues insertValues = getInsertValues(model);
        db.insert(tableName, null, insertValues);
    }

    private void update(SQLiteDatabase db, T model) {
        ContentValues insertValues = getInsertValues(model);
        db.update(tableName, insertValues, _ID + " = ?", new String[]{Integer.toString(model.getId())});
    }

    protected String[] id(int id) {
        return new String[]{Integer.toString(id)};
    }
}
