package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.UUID;

public class CustomerCursorWrapper extends CursorWrapper {

    public CustomerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Customer getCustomer() {
        UUID id = UUID.fromString(getString(getColumnIndex(CustomerTable.ID)));
        String name = getString(getColumnIndex(CustomerTable.CUSTOMER_NAME));
        String email = getString(getColumnIndex(CustomerTable.CUSTOMER_EMAIL));
        String phone = getString(getColumnIndex(CustomerTable.CUSTOMER_PHONE));
        boolean current = getInt(getColumnIndex(CustomerTable.CUSTOMER_CURRENT)) == 0;
        DSDDate created = new DSDDate(getString(getColumnIndex(CustomerTable.CUSTOMER_CREATED)));
        String referral = getString(getColumnIndex(CustomerTable.CUSTOMER_REFERRAL));
        return new Customer(id, name, email, phone, current, created, referral, null);
    }
}
