package net.astechdesign.diningsolutions.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;

public class OrderRepo extends Repo {

    private static OrderRepo instance;

    private Context mContext;

    private final SQLiteDatabase mDatabase;
    private final OrderTable orderTable;
    private final OrderItemTable orderItemsTable;

    public OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        DBHelper dbHelper = DBHelper.getDBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
        orderTable = dbHelper.getOrderTable();
        orderItemsTable = dbHelper.getOrderItemsTable();
    }

    public List<Order> getOrders() {
        List<Order> orders = orderTable.getOrders(mDatabase);
        if (orders.isEmpty()) {
            initDb(mContext);
            orders = orderTable.getOrders(mDatabase);
        }
        return orders;
    }

    public List<Order> getOrders(Customer customer) {
        List<Order> orders = orderTable.getOrders(mDatabase, customer);
        if (orders.isEmpty()) {
            initDb(mContext);
            orders = orderTable.getOrders(mDatabase, customer);
        }
        return orders;
    }

    public static List<Order> get(Context context, Customer customer) {
        return getInstance(context).getOrders(customer);
    }

    private static OrderRepo getInstance(Context context) {
        if (instance == null) {
            instance = new OrderRepo(context);
        }
        return instance;
    }

    private void initDb(Context context) {
        List<Order> productList = OrderAssets.getOrders(context, CustomerRepo.get(context).get(0).id);
        for (Order order : productList) {
            ContentValues insertValues = orderTable.getInsertValues(order);
            mDatabase.insert(orderTable.TABLE_NAME, null, insertValues);
            for (OrderItem item : order.orderItems) {
                insertValues = orderItemsTable.getInsertValues(item);
                mDatabase.insert(orderItemsTable.TABLE_NAME, null, insertValues);
            }
        }
    }

    public List<OrderItem> getOrderItems(Order order) {
        return orderItemsTable.getOrderItems(mDatabase, order);
    }
}
