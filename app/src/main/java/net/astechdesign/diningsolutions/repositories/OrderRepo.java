package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemsTable;
import net.astechdesign.diningsolutions.database.tables.OrdersTable;
import net.astechdesign.diningsolutions.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepo {

    public static OrderRepo repo;
    private Context mContext;

    private SQLiteDatabase mDatabase;
    private OrdersTable ordersTable;
    private final OrderItemsTable orderItemsTable;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
        ordersTable = new OrdersTable();
        orderItemsTable = new OrderItemsTable();
    }

    public List<Order> getmOrders() {
//        mDatabase.rawQueryWithFactory();
        List<Order> mOrders = new ArrayList<>();
        return mOrders;
    }

    public void addOrder(Order order) {
        ordersTable.addOrder(mDatabase, order);
    }

    public Order getOrder(UUID id) {
        return ordersTable.getOrder(id);
    }

    public static Order get(FragmentActivity activity, UUID serializable) {
        return get(activity).getOrder(serializable);
    }
}
