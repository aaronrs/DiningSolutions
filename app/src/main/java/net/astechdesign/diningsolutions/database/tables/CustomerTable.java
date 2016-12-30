package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerTable extends CMSTable<Customer> {

    static final String TABLE_NAME = "customers";

    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_CURRENT = "current";
    public static final String CUSTOMER_CREATED = "created";
    public static final String CUSTOMER_REFERRAL = "referral";
    public static final String ADDRESS_NAME = "house_name";
    public static final String ADDRESS_LINE1 = "line1";
    public static final String ADDRESS_LINE2 = "line2";
    public static final String ADDRESS_TOWN = "town";
    public static final String ADDRESS_COUNTY = "county";
    public static final String ADDRESS_POSTCODE = "postcode";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            CUSTOMER_NAME + " INTEGER, " +
            CUSTOMER_EMAIL + " TEXT, " +
            CUSTOMER_PHONE + " TEXT, " +
            CUSTOMER_CURRENT + " INTEGER, " +
            CUSTOMER_CREATED + " TEXT, " +
            CUSTOMER_REFERRAL + " TEXT, " +
            ADDRESS_NAME + " TEXT, " +
            ADDRESS_LINE1 + " TEXT, " +
            ADDRESS_LINE2 + " TEXT, " +
            ADDRESS_TOWN + " TEXT, " +
            ADDRESS_COUNTY + " TEXT, " +
            ADDRESS_POSTCODE + " TEXT " +
            ")";

    public CustomerTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    protected ContentValues getInsertValues(Customer customer) {
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, customer.name);
        values.put(CUSTOMER_EMAIL, customer.email.address);
        values.put(CUSTOMER_PHONE, customer.phone.number);
        values.put(CUSTOMER_CURRENT, customer.current);
        values.put(CUSTOMER_CREATED, customer.created.dbFormat());
        values.put(CUSTOMER_REFERRAL, customer.referral);
        Address address = customer.address;
        values.put(ADDRESS_NAME, address.name);
        values.put(ADDRESS_LINE1, address.line1);
        values.put(ADDRESS_LINE2, address.line2);
        values.put(ADDRESS_TOWN, address.town);
        values.put(ADDRESS_COUNTY, address.county);
        values.put(ADDRESS_POSTCODE, address.postcode);
        return values;
    }

    public List<Customer> get(SQLiteDatabase mDatabase) {
        List<Customer> customerList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, CUSTOMER_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CustomerCursorWrapper customerCursorWrapper = new CustomerCursorWrapper(cursor);
            Customer customer = customerCursorWrapper.getCustomer();
            customerList.add(customer);
            cursor.moveToNext();
        }
        return customerList;
    }

    public void addOrUpdate(SQLiteDatabase mDatabase, Customer customer) {
        if (customer.id == -1) {
            add(mDatabase, customer);
        } else {
            update(mDatabase, customer);
        }
    }

    private void add(SQLiteDatabase mDatabase, Customer customer) {
        ContentValues insertValues = getInsertValues(customer);
        mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    private void update(SQLiteDatabase mDatabase, Customer customer) {
        mDatabase.update(TABLE_NAME, getInsertValues(customer), _ID + " = ?", new String[]{Integer.toString(customer.id)});
    }
}
