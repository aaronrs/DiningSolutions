package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.UUID;

public class CustomerCursorWrapper extends CursorWrapper {

    public CustomerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Customer getCustomer() {
        int addressId = getInt(getColumnIndex(AddressTable._ID));
        String houseName = getString(getColumnIndex(AddressTable.ADDRESS_NAME));
        String line1 = getString(getColumnIndex(AddressTable.ADDRESS_LINE1));
        String line2 = getString(getColumnIndex(AddressTable.ADDRESS_LINE2));
        String town = getString(getColumnIndex(AddressTable.ADDRESS_TOWN));
        String county = getString(getColumnIndex(AddressTable.ADDRESS_COUNTY));
        String postcode = getString(getColumnIndex(AddressTable.ADDRESS_POSTCODE));
        Address address = new Address(addressId, houseName, line1, line2, town, county, postcode);

        int id = getInt(getColumnIndex(CustomerTable._ID));
        String name = getString(getColumnIndex(CustomerTable.CUSTOMER_NAME));
        String email = getString(getColumnIndex(CustomerTable.CUSTOMER_EMAIL));
        String phone = getString(getColumnIndex(CustomerTable.CUSTOMER_PHONE));
        boolean current = getInt(getColumnIndex(CustomerTable.CUSTOMER_CURRENT)) == 0;
        DSDDate created = new DSDDate(getString(getColumnIndex(CustomerTable.CUSTOMER_CREATED)));
        String referral = getString(getColumnIndex(CustomerTable.CUSTOMER_REFERRAL));
        return new Customer(id, name, email, phone, current, created, referral, address);
    }
}
