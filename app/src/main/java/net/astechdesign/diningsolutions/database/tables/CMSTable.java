package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Model;

import java.util.List;
import java.util.UUID;

public abstract class CMSTable<T extends Model> {

    public static final String ID = "id";

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
        if (model.getId() == null) {
            add(mDatabase, model);
        } else {
            update(mDatabase, model);
        }
    }

    private void add(SQLiteDatabase mDatabase, T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put("id", UUID.randomUUID().toString());
        mDatabase.insert(tableName, null, insertValues);
        addOrUpdateChild(mDatabase, model);
    }

    private void update(SQLiteDatabase mDatabase, T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put("id", model.getId().toString());
        mDatabase.update(tableName, insertValues, "id = ?", new String[]{model.getId().toString()});
        addOrUpdateChild(mDatabase, model);
    }

    protected void addOrUpdateChild(SQLiteDatabase mDatabase, T model) {
        List<Model> children = model.getChildren();
    }
}
