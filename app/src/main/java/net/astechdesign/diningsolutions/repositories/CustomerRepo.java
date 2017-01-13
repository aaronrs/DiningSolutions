package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.List;

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
        mDatabase = new DBHelper(mContext).getWritableDatabase();
        mCustomerTable = DBHelper.getCustomerTable();
    }

    public List<Customer> get() {
        List<Customer> customerList = mCustomerTable.get(mDatabase);
        return customerList;
    }

    public void addOrUpdate(Customer customer) {
        mCustomerTable.addOrUpdate(mDatabase, customer);
    }
}
