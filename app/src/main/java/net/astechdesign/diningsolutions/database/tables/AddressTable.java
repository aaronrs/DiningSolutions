package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressTable extends CMSTable<Address> {

    private static String TABLE_NAME = "address";

    public static final String CUSTOMER_ID = "customer_id";
    public static final String ADDRESS_NAME = "house_name";
    public static final String ADDRESS_LINE1 = "line1";
    public static final String ADDRESS_LINE2 = "line2";
    public static final String ADDRESS_TOWN = "town";
    public static final String ADDRESS_COUNTY = "county";
    public static final String ADDRESS_POSTCODE = "postcode";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            CUSTOMER_ID + " TEXT, " +
            ADDRESS_NAME + " TEXT, " +
            ADDRESS_LINE1 + " TEXT, " +
            ADDRESS_LINE2 + " TEXT, " +
            ADDRESS_TOWN + " TEXT, " +
            ADDRESS_COUNTY + " TEXT, " +
            ADDRESS_POSTCODE + " TEXT " +
            ")";

    protected AddressTable() {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    protected ContentValues getInsertValues(Address address) {
        ContentValues values = new ContentValues();
        values.put(ADDRESS_NAME, address.name);
        values.put(ADDRESS_LINE1, address.line1);
        values.put(ADDRESS_LINE2, address.line2);
        values.put(ADDRESS_TOWN, address.town);
        values.put(ADDRESS_COUNTY, address.county);
        values.put(ADDRESS_POSTCODE, address.postcode);
        return values;
    }

    public List<Address> get(SQLiteDatabase mDatabase) {
        List<Address> addressList = new ArrayList<>();
        Cursor cursor = mDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AddressCursorWrapper addressCursorWrapper = new AddressCursorWrapper(cursor);
            addressList.add(addressCursorWrapper.getAddress());
            cursor.moveToNext();
        }
        return addressList;
    }

    public Address get(SQLiteDatabase mDatabase, UUID customerId) {
        Cursor cursor = mDatabase.query(TABLE_NAME, null, CUSTOMER_ID + " = ?", new String[]{customerId.toString()}, null, null, null);
        cursor.moveToFirst();
        AddressCursorWrapper addressCursorWrapper = new AddressCursorWrapper(cursor);
        return addressCursorWrapper.getAddress();
    }

    public void add(SQLiteDatabase mDatabase, Address address) {
        ContentValues insertValues = getInsertValues(address);
        mDatabase.insert(TABLE_NAME, null, insertValues);
    }

    public void update(SQLiteDatabase mDatabase, UUID customerId, Address address) {
        mDatabase.update(TABLE_NAME, getInsertValues(address), _ID + " = ?", new String[]{Integer.toString(address.id)});
    }
}
