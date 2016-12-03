package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";

    private final List<CMSTable> tables = new ArrayList<>();
    private final OrderTable orderTable;
    private final ProductTable productTable;
    private final AddressTable addressTable;
    private final CustomerTable customerTable;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        orderTable = new OrderTable();
        productTable = new ProductTable();
        addressTable = new AddressTable();
        customerTable = new CustomerTable(addressTable);
        tables.add(orderTable);
        tables.add(productTable);
        tables.add(addressTable);
        tables.add(customerTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (CMSTable table : tables) {
            table.create(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (CMSTable table : tables) {
            table.upgrade(db, oldVersion, newVersion);
        }
    }

    public AddressTable getAddressTable() {
        return addressTable;
    }

    public CustomerTable getCustomerTable() {
        return customerTable;
    }
}