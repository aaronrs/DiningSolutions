package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.CustomersTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerRepo {

    public static CustomerRepo instance;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final CustomersTable customersTable;

    private List<Customer> mCustomers;
    private Map<UUID, Customer> mCustomerMap;

    private CustomerRepo(Context context) {
        this.mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
        customersTable = new CustomersTable();
    }

    public static List<Customer> get(Context context) {
        return getInstance(context).getCustomers();
    }

    public static Customer get(Context context, UUID id) {
        return getInstance(context).getCustomer(id);
    }

    private static CustomerRepo getInstance(Context context) {
        if (instance == null) {
            instance = new CustomerRepo(context);
        }
        return instance;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = customersTable.get();
        if (customers == null || customers.isEmpty()) {
            initDb(mContext);
            customers = customersTable.get(mDatabase);
        }
        return customers;
    }

    public Customer getCustomer(UUID id) {
        return mCustomerMap.get(id);
    }


    private void initDb(Context context) {
        List<Customer> customerList = CustomerAssets.getCustomers(context);
        for (Customer customer: customerList) {
            customersTable.addOrUpdate(mDatabase, customer);
        }
    }

    private String[][] testCustomers = {
    };
}
