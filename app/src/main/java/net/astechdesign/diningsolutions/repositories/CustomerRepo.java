package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.database.tables.CustomersTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerRepo {

    public static CustomerRepo instance;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final CustomersTable customersTable;
    private final AddressTable addressTable;
    private final DBHelper dbHelper;

    private CustomerRepo(Context context) {
        this.mContext = context.getApplicationContext();
        dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        addressTable = dbHelper.getAddressTable();
        customersTable = dbHelper.getCustomersTable();
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
        List<Customer> customerList = customersTable.get(mDatabase);
        if (customerList == null || customerList.isEmpty()) {
            initDb(mContext);
            customerList = customersTable.get(mDatabase);
        }
        return customerList;
    }

    private void addOrUpdate(Customer customer) {
        customersTable.addOrUpdate(mDatabase, customer);
    }

    private void initDb(Context context) {
        List<Customer> customerList = CustomerAssets.getCustomers(context);
        for (Customer customer: customerList) {
            customersTable.addOrUpdate(mDatabase, customer);
        }
    }
}
