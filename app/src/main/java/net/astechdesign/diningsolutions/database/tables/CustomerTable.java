package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

public class CustomerTable extends CMSTable<Customer> {

    public static final String TABLE_NAME = "customers";

    public static final String CUSTOMER_NAME = "name";
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_PHONE = "phone";
    public static final String CUSTOMER_CURRENT = "current";
    public static final String CUSTOMER_CREATED = "created";
    public static final String CUSTOMER_REFERRAL = "referral";
    public static final String VISIT_DATE = "visit_date";
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_LINE = "address";
    public static final String ADDRESS_TOWN = "town";
    public static final String ADDRESS_COUNTY = "county";
    public static final String ADDRESS_POSTCODE = "postcode";

    private static String CREATE_TABLE =
            CUSTOMER_NAME + " INTEGER, " +
            CUSTOMER_EMAIL + " TEXT, " +
            CUSTOMER_PHONE + " TEXT, " +
            CUSTOMER_CURRENT + " INTEGER, " +
            CUSTOMER_CREATED + " INTEGER, " +
            CUSTOMER_REFERRAL + " TEXT, " +
            VISIT_DATE + " INTEGER, " +
            ADDRESS_ID + " TEXT, " +
            ADDRESS_LINE + " TEXT, " +
            ADDRESS_TOWN + " TEXT, " +
            ADDRESS_COUNTY + " TEXT, " +
            ADDRESS_POSTCODE + " TEXT " +
            "";
    private static CustomerTable instance;

    public CustomerTable() {
        super(TABLE_NAME, CREATE_TABLE);
        instance = this;
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.upgrade(db, oldVersion, newVersion);
        if (newVersion == 2) {
            String query = "ALTER TABLE %s ADD COLUMN %s TEXT" ;
            db.execSQL(String.format(query, TABLE_NAME, ADDRESS_LINE));
            query = "UPDATE %s SET ADDRESS = COALESCE(%s,'') || ' ' || COALESCE(%s,'') || ' ' || COALESCE(%s,'')";
            db.execSQL(String.format(query, TABLE_NAME, "house_name", "line1", "line2"));
        }
    }

    protected void insertDbValues(ContentValues values, Customer customer) {
        values.put(CUSTOMER_NAME, customer.name);
        values.put(CUSTOMER_EMAIL, customer.email.address);
        values.put(CUSTOMER_PHONE, customer.phone.number);
        values.put(CUSTOMER_CURRENT, customer.current);
        values.put(CUSTOMER_CREATED, customer.created.dbFormat());
        values.put(CUSTOMER_REFERRAL, customer.referral);
        if (customer.visit != null) {
            values.put(VISIT_DATE, customer.visit.dbFormat());
        }
        Address address = customer.address;
        values.put(ADDRESS_ID, address.getDbId());
        values.put(ADDRESS_LINE, address.line);
        values.put(ADDRESS_TOWN, address.town);
        values.put(ADDRESS_COUNTY, address.county);
        values.put(ADDRESS_POSTCODE, address.postcode);
    }

    public Cursor get(SQLiteDatabase db, String name, String orderby) {
        return db.query(TABLE_NAME, null, CUSTOMER_NAME + " = ?", new String[]{name}, null, null, CUSTOMER_NAME);
    }

    public static CustomerTable table() {
        return instance;
    }

    public void updateVisit(SQLiteDatabase db, Customer mCurrentCustomer, DSDDate date) {
        ContentValues values = new ContentValues();
        values.put(VISIT_DATE, date.dbFormat());
        db.update(TABLE_NAME, values, UUID_ID + " = ?", new String[]{mCurrentCustomer.getDbId()});
    }

    public void update(SQLiteDatabase db, Customer customer, String field, String value) {
        ContentValues values = new ContentValues();
        values.put(field, value);
        db.update(TABLE_NAME, values, UUID_ID + " = ?", new String[]{customer.getId().toString()});
    }
}
