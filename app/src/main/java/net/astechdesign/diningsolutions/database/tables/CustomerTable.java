package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;

import java.util.UUID;

public class CustomerTable extends CMSTable<Customer> {

    public static final String TABLE_NAME = "customers";

    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_CURRENT = "current";
    public static final String CUSTOMER_CREATED = "created";
    public static final String CUSTOMER_REFERRAL = "referral";
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_NAME = "house_name";
    public static final String ADDRESS_LINE1 = "line1";
    public static final String ADDRESS_LINE2 = "line2";
    public static final String ADDRESS_TOWN = "town";
    public static final String ADDRESS_COUNTY = "county";
    public static final String ADDRESS_POSTCODE = "postcode";

    private static String CREATE_TABLE =
            CUSTOMER_NAME + " INTEGER, " +
            CUSTOMER_EMAIL + " TEXT, " +
            CUSTOMER_PHONE + " TEXT, " +
            CUSTOMER_CURRENT + " INTEGER, " +
            CUSTOMER_CREATED + " TEXT, " +
            CUSTOMER_REFERRAL + " TEXT, " +
            ADDRESS_ID + " TEXT, " +
            ADDRESS_NAME + " TEXT, " +
            ADDRESS_LINE1 + " TEXT, " +
            ADDRESS_LINE2 + " TEXT, " +
            ADDRESS_TOWN + " TEXT, " +
            ADDRESS_COUNTY + " TEXT, " +
            ADDRESS_POSTCODE + " TEXT " +
            "";
    private static CustomerTable instance;

    public CustomerTable() {
        super(TABLE_NAME, CREATE_TABLE);
        instance = this;
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
        values.put(ADDRESS_ID, address.getDbId());
        values.put(ADDRESS_NAME, address.name);
        values.put(ADDRESS_LINE1, address.line1);
        values.put(ADDRESS_LINE2, address.line2);
        values.put(ADDRESS_TOWN, address.town);
        values.put(ADDRESS_COUNTY, address.county);
        values.put(ADDRESS_POSTCODE, address.postcode);
        return values;
    }

    public Cursor get(SQLiteDatabase db) {
        return db.query(TABLE_NAME, null, null, null, null, null, CUSTOMER_NAME);
    }

    public Cursor get(SQLiteDatabase db, UUID customerId) {
        return db.query(TABLE_NAME, null, UUID_ID + " = ?", new String[]{customerId.toString()}, null, null, null);
    }

    public Cursor get(SQLiteDatabase db, String name) {
        return db.query(TABLE_NAME, null, CUSTOMER_NAME + " = ?", new String[]{name}, null, null, CUSTOMER_NAME);
    }

    public static CustomerTable table() {
        return instance;
    }
}
