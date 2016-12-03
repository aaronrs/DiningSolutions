package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomersTable implements CMSTable {

    private static final String TABLE_NAME = "customers";

    public static final String ID = "id";
    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_CURRENT = "current";
    public static final String CUSTOMER_CREATED = "created";
    public static final String CUSTOMER_REFERRAL = "referral";
    private AddressTable mAddressTable;

    public CustomersTable(AddressTable addressTable) {
        mAddressTable = addressTable;
    }

    @Override
    public void create(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER, " +
                CUSTOMER_NAME + " INTEGER, " +
                CUSTOMER_EMAIL + " TEXT, " +
                CUSTOMER_PHONE + " TEXT, " +
                CUSTOMER_CURRENT + " INTEGER, " +
                CUSTOMER_CREATED + " TEXT, " +
                CUSTOMER_REFERRAL + " TEXT " +
                ")";
        db.execSQL(query);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static ContentValues getInsertValues(Customer customer) {
        return getInsertValues(customer.id, customer);
    }

    private static ContentValues getInsertValues(UUID contactId, Customer customer) {
        ContentValues values = new ContentValues();
        values.put(ID, contactId.toString());
        values.put(CUSTOMER_NAME, customer.name);
        values.put(CUSTOMER_EMAIL, customer.email.address);
        values.put(CUSTOMER_PHONE, customer.phone.number);
        values.put(CUSTOMER_CURRENT, customer.current);
        values.put(CUSTOMER_CREATED, customer.created.toString());
        values.put(CUSTOMER_REFERRAL, customer.referral);
        return values;
    }

    public List<Customer> get(SQLiteDatabase mDatabase) {
        List<Customer> customerList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, CUSTOMER_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CustomersCursorWrapper customersCursorWrapper = new CustomersCursorWrapper(cursor);
            customerList.add(customersCursorWrapper.getCustomer());
            cursor.moveToNext();
        }
        return customerList;
    }

    public void addOrUpdate(SQLiteDatabase mDatabase, Customer customer) {

    }
}
