package net.astechdesign.diningsolutions.database.tables;

import android.database.sqlite.SQLiteDatabase;

public interface CMSTable {

    String DB_DATE_FORMAT = "yyyy-MM-dd";
    String DB_TIME_FORMAT = "HH:mm:ss";

    void create(SQLiteDatabase db);
    void upgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
