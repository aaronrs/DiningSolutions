package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Model;

import java.util.UUID;

public abstract class CMSTable<T extends Model> {

    String DB_DATE_FORMAT = "yyyy-MM-dd";
    String DB_TIME_FORMAT = "HH:mm:ss";

    public abstract void create(SQLiteDatabase db);
    public abstract void upgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    ContentValues getInsertValues(T model) {
        return getInsertValues(model.getId(), model);
    }

    protected abstract ContentValues getInsertValues(UUID id, T model);

    public void addOrUpdate(SQLiteDatabase mDatabase, T model) {
        if (model.getId() == null) {
            add(mDatabase, model);
        } else {
            update(mDatabase, model);
        }
    }

    private void add(SQLiteDatabase mDatabase, T model) {
        ContentValues insertValues = getInsertValues(UUID.randomUUID(), model);
        mDatabase.insert(getTableName(), null, insertValues);
    }

    private void update(SQLiteDatabase mDatabase, T model) {
        mDatabase.update(getTableName(), getInsertValues(model), "id = ?", new String[]{model.getId().toString()});
    }

    protected abstract String getTableName();

}
