package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.wrappers.CustomerCursorWrapper;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;

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
        Cursor cursor = mCustomerTable.get(mDatabase);
        List<Customer> customerList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CustomerCursorWrapper customerCursorWrapper = new CustomerCursorWrapper(cursor);
            Customer customer = customerCursorWrapper.getCustomer();
            customerList.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customerList;
    }

    public void addOrUpdate(Customer customer) {
        mCustomerTable.addOrUpdate(mDatabase, customer);
    }

    public Customer get(UUID customerId) {
        Cursor cursor = mCustomerTable.get(mDatabase, customerId);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CustomerCursorWrapper customerCursorWrapper = new CustomerCursorWrapper(cursor);
            return customerCursorWrapper.getCustomer();
        }
        throw new RuntimeException("unable to find customer:" + customerId);
    }
}
