package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.tables.TodoTable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";
    private static CustomerTable customerTable;
    private static OrderTable orderTable;
    private static OrderItemTable orderItemTable;
    private static TodoTable todoTable;
    private static ProductTable productTable;

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        if (customerTable == null) {
            customerTable = new CustomerTable();
            orderTable = new OrderTable();
            orderItemTable = new OrderItemTable();
            todoTable = new TodoTable();
            productTable = new ProductTable();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBLoader.create(db, productTable, customerTable, orderTable, orderItemTable, todoTable);
        DBLoader.load(mContext, db, productTable, customerTable, orderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static CustomerTable getCustomerTable() {
        return customerTable;
    }

    public static OrderItemTable getOrderItemTable() {
        return orderItemTable;
    }

    public static TodoTable getTodoTable() {
        return todoTable;
    }

    public static ProductTable getProductTable() {
        return productTable;
    }

    public static OrderTable getOrderTable() {
        return orderTable;
    }
}