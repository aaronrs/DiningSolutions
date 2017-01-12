package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;

import java.util.List;

public class CustomerRepo {

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final CustomerTable customerTable;
    private final DBHelper dbHelper;

    CustomerRepo(Context context) {
        this.mContext = context.getApplicationContext();
        dbHelper = DBHelper.getDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        customerTable = dbHelper.getCustomerTable();
    }

    public List<Customer> get() {
        List<Customer> customerList = customerTable.get(mDatabase);
        return customerList;
    }

    public void addOrUpdate(Customer customer) {
        customerTable.addOrUpdate(mDatabase, customer);
    }
}
