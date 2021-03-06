package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.wrappers.CustomerCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerRepo {

    private static CustomerRepo repo;

    private final CustomerTable mCustomerTable;
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public static CustomerRepo get(Context context) {
        if (repo == null) {
            repo = new CustomerRepo(context.getApplicationContext());
        }
        return repo;
    }

    private CustomerRepo(Context context) {
        mContext = context;
        mDatabase = DBHelper.get(mContext).getWritableDatabase();
        mCustomerTable = CustomerTable.table();
    }

    public List<Customer> get() {
        Cursor cursor = mCustomerTable.get(mDatabase, CustomerTable.CUSTOMER_NAME);

        List<Customer> customerList = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            CustomerCursorWrapper customerCursorWrapper = new CustomerCursorWrapper(cursor);
            Customer customer = customerCursorWrapper.getCustomer();
            customerList.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customerList;
    }

    public UUID addOrUpdate(Customer customer) {
        return mCustomerTable.addOrUpdate(mDatabase, customer);
    }

    public Customer get(UUID customerId) {
        Cursor cursor = mCustomerTable.get(mDatabase, customerId);
        while (!cursor.isAfterLast()) {
            CustomerCursorWrapper customerCursorWrapper = new CustomerCursorWrapper(cursor);
            return customerCursorWrapper.getCustomer();
        }
        throw new RuntimeException("unable to find customer:" + customerId);
    }

    public void updateVisit(Customer customer, DSDDate date) {
        mCustomerTable.updateVisit(mDatabase, customer, date);
    }

    public void deleteCustomer(Customer customer) {
        mCustomerTable.delete(mDatabase, customer);
    }

    public void update(Customer customer, String field, String value) {
        mCustomerTable.update(mDatabase, customer, field, value);
    }

    public Customer create() {
        UUID id = mCustomerTable.addOrUpdate(mDatabase, Customer.create());
        return new CustomerCursorWrapper(mCustomerTable.get(mDatabase, id)).getCustomer();
    }
}
