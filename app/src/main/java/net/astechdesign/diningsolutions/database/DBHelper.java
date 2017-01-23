package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.tables.TaskTable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";
    private static CustomerTable customerTable;
    private static OrderTable orderTable;
    private static OrderItemTable orderItemTable;
    private static TaskTable taskTable;
    private static ProductTable productTable;
    private static DBHelper dbHelper;

    private Context mContext;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        if (customerTable == null) {
            customerTable = new CustomerTable();
            orderTable = new OrderTable();
            orderItemTable = new OrderItemTable();
            taskTable = new TaskTable();
            productTable = new ProductTable();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBLoader.create(db, productTable, customerTable, orderTable, orderItemTable);
        DBLoader.load(mContext, db, productTable, customerTable, orderTable, orderItemTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static DBHelper get(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    public static CustomerTable getCustomerTable() {
        return customerTable;
    }

    public static OrderItemTable getOrderItemTable() {
        return orderItemTable;
    }

    public static TaskTable getTaskTable() {
        return taskTable;
    }

    public static ProductTable getProductTable() {
        return productTable;
    }

    public static OrderTable getOrderTable() {
        return orderTable;
    }
}