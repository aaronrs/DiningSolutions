package net.astechdesign.diningsolutions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.astechdesign.diningsolutions.database.tables.CMSTable;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.database.tables.ProductTable;
import net.astechdesign.diningsolutions.database.tables.TaskTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static net.astechdesign.diningsolutions.database.tables.CMSTable.TableType.*;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "orders";

    private static Map<CMSTable.TableType, CMSTable> tables;

    private static DBHelper dbHelper;

    private Context mContext;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        if (tables == null) {
            tables  = new HashMap<>();
            tables.put(CUSTOMER, new CustomerTable());
            tables.put(ORDER, new OrderTable());
            tables.put(ORDERITEM, new OrderItemTable());
            tables.put(TASK, new TaskTable());
            tables.put(PRODUCT, new ProductTable());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (CMSTable table : tables.values()) {
            table.create(db);
        }
        DBLoader.load(mContext, db);
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

    public static OrderTable getOrderTable() {
        return (OrderTable) tables.get(ORDER);
    }

    public static OrderItemTable getOrderItemTable() {
        return (OrderItemTable) tables.get(ORDERITEM);
    }

    public static TaskTable getTaskTable() {
        return (TaskTable) tables.get(TASK);
    }

    public static ProductTable getProductTable() {
        return (ProductTable) tables.get(PRODUCT);
    }

    public static Collection<CMSTable> getTables() {
        return tables.values();
    }
}