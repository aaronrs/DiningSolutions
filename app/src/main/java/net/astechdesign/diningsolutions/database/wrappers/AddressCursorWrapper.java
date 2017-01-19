package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Product;

import java.util.UUID;

public class AddressCursorWrapper extends CursorWrapper {

    public AddressCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Address getAddress() {
        int id = getInt(getColumnIndex(AddressTable._ID));
        String name = getString(getColumnIndex(AddressTable.ADDRESS_NAME));
        String line1 = getString(getColumnIndex(AddressTable.ADDRESS_LINE1));
        String line2 = getString(getColumnIndex(AddressTable.ADDRESS_LINE2));
        String town = getString(getColumnIndex(AddressTable.ADDRESS_TOWN));
        String county = getString(getColumnIndex(AddressTable.ADDRESS_COUNTY));
        String postcode = getString(getColumnIndex(AddressTable.ADDRESS_POSTCODE));
        return new Address(id, name, line1, line2, town, county, postcode);
    }
}
