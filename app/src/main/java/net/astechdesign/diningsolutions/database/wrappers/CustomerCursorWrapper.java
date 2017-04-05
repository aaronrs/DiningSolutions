package net.astechdesign.diningsolutions.database.wrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
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
        UUID addressId = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        String line = getString(getColumnIndex(CustomerTable.ADDRESS_LINE));
        String town = getString(getColumnIndex(CustomerTable.ADDRESS_TOWN));
        String county = getString(getColumnIndex(CustomerTable.ADDRESS_COUNTY));
        String postcode = getString(getColumnIndex(CustomerTable.ADDRESS_POSTCODE));
        Address address = Address.create(addressId, line, town, county, postcode);

        UUID id = UUID.fromString(getString(getColumnIndex(CMSTable.UUID_ID)));
        String name = getString(getColumnIndex(CustomerTable.CUSTOMER_NAME));
        String email = getString(getColumnIndex(CustomerTable.CUSTOMER_EMAIL));
        String phone = getString(getColumnIndex(CustomerTable.CUSTOMER_PHONE));
        boolean current = getInt(getColumnIndex(CustomerTable.CUSTOMER_CURRENT)) == 0;
        DSDDate created = DSDDate.create(getLong(getColumnIndex(CustomerTable.CUSTOMER_CREATED)));
        String referral = getString(getColumnIndex(CustomerTable.CUSTOMER_REFERRAL));
        DSDDate visit = DSDDate.create(getLong(getColumnIndex(CustomerTable.VISIT_DATE)));
        return Customer.create(id, name, email, phone, current, created, referral, address, visit);
    }
}
