package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import net.astechdesign.diningsolutions.model.Model;

import java.util.UUID;

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

    protected abstract void insertDbValues(ContentValues values, T model);

    public UUID addOrUpdate(SQLiteDatabase db, T model) {
        if (model.getId() == null) {
            return add(db, model);
        }
        update(db, model);
        return model.getId();
    }

    public Cursor get(SQLiteDatabase db, String orderBy) {
        Cursor cursor = db.query(tableName, null, DELETED + " != 1", null, null, null, orderBy);
        cursor.moveToFirst();
        return cursor;
    }

    public UUID addOrUpdate(SQLiteDatabase db, Model parent, T model) {
        if (model.getId() == null) {
            return add(db, parent, model);
        }
        update(db, parent, model);
        return model.getId();
    }

    private UUID add(SQLiteDatabase db, T model) {
        ContentValues insertValues = getModelValues(model);
        db.insert(tableName, null, insertValues);
        return UUID.fromString((String)insertValues.get(UUID_ID));
    }

    private UUID add(SQLiteDatabase db, Model parent, T model) {
        ContentValues insertValues = getModelValues(parent, model);
        db.insert(tableName, null, insertValues);
        return UUID.fromString((String)insertValues.get(UUID_ID));
    }

    private UUID update(SQLiteDatabase db, T model) {
        ContentValues insertValues = getModelValues(model);
        db.update(tableName, insertValues, UUID_ID + " = ?", new String[]{model.getDbId()});
        return model.getId();
    }

    private UUID update(SQLiteDatabase db, Model parent, T model) {
        ContentValues insertValues = getModelValues(model);
        db.update(tableName, insertValues, UUID_ID + " = ?", new String[]{model.getDbId()});
        return model.getId();
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
        ContentValues insertValues = new ContentValues();
        insertDbValues(insertValues, model);
        insertValues.put(UUID_ID, model.getId() == null ? UUID.randomUUID().toString() : model.getDbId());
        return insertValues;
    }

    private ContentValues getModelValues(Model parent, T model) {
        ContentValues insertValues = getModelValues(model);
        insertValues.put(PARENT_ID, parent.getDbId());
        return insertValues;
    }

    public Cursor get(SQLiteDatabase db, UUID id) {
        Cursor query = db.query(tableName, null, UUID_ID + " = ?", new String[]{id.toString()}, null, null, null);
        query.moveToFirst();
        return query;
    }

    public enum TableType {
        CUSTOMER,
        ORDER,
        ORDERITEM,
        PRODUCT,
        TASK
    }
}
