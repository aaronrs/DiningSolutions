package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.List;

public class CustomerRepo {

    public static CustomerRepo instance;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final CustomerTable customerTable;
    private final DBHelper dbHelper;

    private CustomerRepo(Context context) {
        this.mContext = context.getApplicationContext();
        dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        customerTable = dbHelper.getCustomerTable();
    }

    public static List<Customer> get(Context context) {
        return getInstance(context).get();
    }

    public static void addOrUpdate(Context context, Customer customer) {
        getInstance(context).addOrUpdate(customer);
    }

    private static CustomerRepo getInstance(Context context) {
        if (instance == null) {
            instance = new CustomerRepo(context);
        }
        return instance;
    }

    public List<Customer> get() {
        List<Customer> customerList = customerTable.get(mDatabase);
        if (customerList == null || customerList.isEmpty()) {
            initDb(mContext);
            customerList = customerTable.get(mDatabase);
        }
        return customerList;
    }

    private void addOrUpdate(Customer customer) {
        customerTable.addOrUpdate(mDatabase, customer);
    }

    private void initDb(Context context) {
        List<Customer> customerList = CustomerAssets.getCustomers(context);
        for (Customer customer: customerList) {
            customerTable.addOrUpdate(mDatabase, customer);
        }
    }
}
