package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";
    private static DBHelper instance;

    private final List<CMSTable<?>> tables = new ArrayList<>();
    private final OrderTable orderTable;
    private final ProductTable productTable;
    private final CustomerTable customerTable;
    private final OrderItemTable orderItemTable;

    public static DBHelper getDBHelper(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        productTable = new ProductTable();
        orderItemTable = new OrderItemTable();
        orderTable = new OrderTable(orderItemTable);
        customerTable = new CustomerTable(orderTable);
        tables.add(productTable);
        tables.add(customerTable);
        tables.add(orderTable);
        tables.add(orderItemTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (CMSTable<?> table : tables) {
            table.create(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (CMSTable<?> table : tables) {
            table.upgrade(db, oldVersion, newVersion);
        }
    }

    public CustomerTable getCustomerTable() {
        return customerTable;
    }

    public ProductTable getProductTable() {
        return productTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}