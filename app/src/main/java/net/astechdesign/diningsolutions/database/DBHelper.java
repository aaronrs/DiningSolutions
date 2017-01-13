package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.tables.TodoTable;
import net.astechdesign.diningsolutions.model.Product;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.ArrayList;
import java.util.List;

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
            customerTable = new CustomerTable(mContext);
            orderTable = new OrderTable(mContext);
            orderItemTable = new OrderItemTable(mContext);
            todoTable = new TodoTable(mContext);
            productTable = new ProductTable(mContext);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        customerTable.create(db);
        orderTable.create(db);
        orderItemTable.create(db);
        todoTable.create(db);
        productTable.create(db);
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