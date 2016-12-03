package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.AddressTable;
import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomersTable;
import net.astechdesign.diningsolutions.database.tables.OrdersTable;
import net.astechdesign.diningsolutions.database.tables.ProductsTable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";

    private final List<CMSTable> tables = new ArrayList<>();
    private final OrdersTable ordersTable;
    private final ProductsTable productsTable;
    private final AddressTable addressTable;
    private final CustomersTable customerTable;
    private CustomersTable customersTable;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ordersTable = new OrdersTable();
        productsTable = new ProductsTable();
        addressTable = new AddressTable();
        customerTable = new CustomersTable(addressTable);
        tables.add(ordersTable);
        tables.add(productsTable);
        tables.add(addressTable);
        tables.add(customerTable);
//        resetDB();
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

    public CustomersTable getCustomersTable() {
        return customersTable;
    }
}