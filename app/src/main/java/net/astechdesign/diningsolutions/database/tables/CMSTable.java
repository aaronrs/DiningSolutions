package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.Model;

public abstract class CMSTable<T extends Model> implements BaseColumns {

    public static String UUID_ID = "id";
    public static final String DELETED = "deleted";
    public static final String PARENT_ID = "parent_id";

    private static String CREATE = "CREATE TABLE %s (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            UUID_ID + " TEXT," +
            PARENT_ID + " TEXT," +
            DELETED + " INTEGER DEFAULT 0," +
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

    protected abstract ContentValues getInsertValues(T model);

    public void addOrUpdate(SQLiteDatabase db, T model) {
        if (model.getId() == null) {
            add(db, model);
        } else {
            update(db, model);
        }
    }

    public Cursor get(SQLiteDatabase db, String orderBy) {
        return db.query(tableName, null, DELETED + " != 1", null, null, null, orderBy);
    }

    public void addOrUpdate(SQLiteDatabase db, Model parent, T model) {
        if (model.getId() == null) {
            add(db, parent, model);
        } else {
            update(db, parent, model);
        }
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
        db.update(tableName, insertValues, UUID_ID + " = ?", new String[]{model.getDbId()});
    }

    private void update(SQLiteDatabase db, Model parent, T model) {
        ContentValues insertValues = getModelValues(model);
        db.update(tableName, insertValues, UUID_ID + " = ?", new String[]{model.getDbId()});
    }

    public void delete(SQLiteDatabase db, T model) {
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        db.update(tableName, values, UUID_ID + " = ?", new String[]{model.getDbId()});
    }

    public void delete(SQLiteDatabase db, String parentId, T model) {
        ContentValues values = new ContentValues();
        values.put(DELETED, 1);
        db.update(tableName, values, PARENT_ID + " = ? AND " + UUID_ID + " = ?", new String[]{parentId, model.getDbId()});
    }

    private ContentValues getModelValues(T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put(UUID_ID, model.getDbId());
        return insertValues;
    }

    private ContentValues getModelValues(Model parent, T model) {
        ContentValues insertValues = getInsertValues(model);
        insertValues.put(UUID_ID, model.getDbId());
        insertValues.put(PARENT_ID, parent.getDbId());
        return insertValues;
    }

    public enum TableType {
        CUSTOMER,
        ORDER,
        ORDERITEM,
        PRODUCT,
        TASK
    }
}
