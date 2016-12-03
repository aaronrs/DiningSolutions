package net.astechdesign.diningsolutions.database.tables;

import android.database.Cursor;
import android.database.CursorWrapper;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.UUID;

public class CustomersCursorWrapper extends CursorWrapper {

    public CustomersCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Customer getCustomer() {
        UUID id = UUID.fromString(getString(getColumnIndex(CustomersTable.ID)));
        String name = getString(getColumnIndex(CustomersTable.CUSTOMER_NAME));
        String email = getString(getColumnIndex(CustomersTable.CUSTOMER_EMAIL));
        String phone = getString(getColumnIndex(CustomersTable.CUSTOMER_PHONE));
        boolean current = getInt(getColumnIndex(CustomersTable.CUSTOMER_CURRENT)) == 0;
        DSDDate created = new DSDDate(getString(getColumnIndex(CustomersTable.CUSTOMER_CREATED)));
        String referral = getString(getColumnIndex(CustomersTable.CUSTOMER_REFERRAL));
        return new Customer(id, name, email, phone, current, created, referral, null);
    }
}
