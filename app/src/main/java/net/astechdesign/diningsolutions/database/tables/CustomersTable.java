package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import static android.provider.BaseColumns._ID;

public class CustomersTable implements CMSTable {

    private static final String TABLE_NAME = "customers";

    public static final String CONTACT_ID = "contact_id";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_ADDRESS = "address";
    public static final String CUSTOMER_POSTCODE = "postcode";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACT_ID + " INTEGER, " +
                CUSTOMER_NAME + " INTEGER, " +
                CUSTOMER_ADDRESS + " INTEGER, " +
                CUSTOMER_POSTCODE + " INTEGER, " +
                CUSTOMER_EMAIL + " INTEGER, " +
                CUSTOMER_PHONE + " INTEGER" +
                ")";
        db.execSQL(query);
        initialise(db);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void initialise(SQLiteDatabase db) {
        for (int i=1; i < 100; i++) {
            db.insert(TABLE_NAME, null,
                    getInsertValues(i,
                    String.format("Person %03d", i),
                    String.format("%d Street, Town", i),
                    String.format("AA%d %dAA", i,i),
                    String.format("person%03d@gmail.com", i,i),
                    String.format("07700000%03d", i)));
        }
    }

    private ContentValues getInsertValues(int contactId, String name, String address, String postcode, String email, String phone) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_ID, contactId);
        values.put(CUSTOMER_NAME, name);
        values.put(CUSTOMER_ADDRESS, address);
        values.put(CUSTOMER_POSTCODE, postcode);
        values.put(CUSTOMER_EMAIL, email);
        values.put(CUSTOMER_PHONE, phone);
        return values;
    }
}
