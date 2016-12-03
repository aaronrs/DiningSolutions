package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.database.tables.OrderTable;
import net.astechdesign.diningsolutions.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderRepo {

    public static OrderRepo repo;
    private Context mContext;

    private SQLiteDatabase mDatabase;
    private OrderTable orderTable;
    private final OrderItemTable orderItemTable;

    public static OrderRepo get(Context context) {
        if (repo == null) {
            repo = new OrderRepo(context);
        }
        return repo;
    }

    private OrderRepo(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DBHelper(context).getWritableDatabase();
        orderTable = new OrderTable();
        orderItemTable = new OrderItemTable();
    }

    public List<Order> getmOrders() {
//        mDatabase.rawQueryWithFactory();
        List<Order> mOrders = new ArrayList<>();
        return mOrders;
    }

    public void addOrder(Order order) {
        orderTable.addOrder(mDatabase, order);
    }

    public Order getOrder(UUID id) {
        return orderTable.getOrder(id);
    }

    public static Order get(FragmentActivity activity, UUID serializable) {
        return get(activity).getOrder(serializable);
    }
}
