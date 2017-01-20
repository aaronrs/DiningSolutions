package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.Model;

public abstract class CMSTable<T extends Model> implements BaseColumns {

    public static String UUID_ID = "id";

    private static String CREATE = "CREATE TABLE %s (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UUID_ID + " TEXT," +
            " %s)";

    private final String tableName;
    private final String createTable;
    protected CMSTable(String tableName, String createTable) {
        this.tableName = tableName;
        this.createTable = createTable;
    }

    public final void create(SQLiteDatabase db) {
        try {
            db.execSQL(String.format(CREATE, tableName, createTable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void upgrade(int oldVersion, int newVersion){
    }

    protected abstract ContentValues getInsertValues(T model);

    public void addOrUpdate(SQLiteDatabase db, T model) {
        if (model.getId() == null) {
            add(db, model);
        } else {
            update(db, model);
        }
    }

    public void addOrUpdate(SQLiteDatabase db, Model parent, T model) {
        if (model.getId() == null) {
            add(db, parent, model);
        } else {
            update(db, parent, model);
        }
    }

    protected String[] selectionArgs(Object... values) {
        String[] output = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            output[i] = values[i].toString();
        }
        return output;
    }

    private void add(SQLiteDatabase db, T model) {
        ContentValues insertValues = getModelValues(model);
        db.insert(tableName, null, insertValues);
    }

    private void add(SQLiteDatabase db, Model parent, T model) {
        ContentValues insertValues = getModelValues(parent, model);
        db.insert(tableName, null, insertValues);
    }

    private void update(SQLiteDatabase db, T model) {
        ContentValues insertValues = getModelValues(model);
        db.update(tableName, insertValues, _ID + " = ?", new String[]{model.getId().toString()});
    }

    private void update(SQLiteDatabase db, Model parent, T model) {
        ContentValues insertValues = getModelValues(model);
        db.update(tableName, insertValues, _ID + " = ?", new String[]{model.getId().toString()});
    }

    private ContentValues getModelValues(T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put(UUID_ID, model.getDbId());
        return insertValues;
    }

    private ContentValues getModelValues(Model parent, T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put(UUID_ID, model.getDbId());
        insertValues.put(getParentIdColumn(), parent.getDbId());
        return insertValues;
    }

    protected abstract String getParentIdColumn();
}
