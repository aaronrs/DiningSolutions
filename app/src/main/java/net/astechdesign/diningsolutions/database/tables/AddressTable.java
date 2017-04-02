package net.astechdesign.diningsolutions.database.tables;

import android.content.ContentValues;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.model.Address;

public class AddressTable extends CMSTable<Address> {

    private static String TABLE_NAME = "address";

    public static final String ADDRESS_NAME = "house_name";
    public static final String ADDRESS_LINE1 = "line1";
    public static final String ADDRESS_LINE2 = "line2";
    public static final String ADDRESS_TOWN = "town";
    public static final String ADDRESS_COUNTY = "county";
    public static final String ADDRESS_POSTCODE = "postcode";

    private static String CREATE_TABLE =
            ADDRESS_NAME + " TEXT, " +
            ADDRESS_LINE1 + " TEXT, " +
            ADDRESS_LINE2 + " TEXT, " +
            ADDRESS_TOWN + " TEXT, " +
            ADDRESS_COUNTY + " TEXT, " +
            ADDRESS_POSTCODE + " TEXT " +
                    "";

    protected AddressTable(DBHelper db) {
        super(TABLE_NAME, CREATE_TABLE);
    }

    @Override
    protected void insertDbValues(ContentValues values, Address address) {
        values.put(ADDRESS_NAME, address.name);
        values.put(ADDRESS_LINE1, address.line1);
        values.put(ADDRESS_LINE2, address.line2);
        values.put(ADDRESS_TOWN, address.town);
        values.put(ADDRESS_COUNTY, address.county);
        values.put(ADDRESS_POSTCODE, address.postcode);
    }
}
