package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.astechdesign.diningsolutions.database.DBHelper;
import net.astechdesign.diningsolutions.database.tables.OrderItemTable;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.util.List;

public class OrderItemRepo {

    private static OrderItemRepo repo;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;
    private final OrderItemTable orderItemTable;

    public static OrderItemRepo get(Context context) {
        if (repo == null) {
            repo = new OrderItemRepo(context.getApplicationContext());
        }
        return repo;
    }

    private OrderItemRepo(Context context) {
        mContext = context;
        mDatabase = new DBHelper(mContext).getWritableDatabase();
        orderItemTable = DBHelper.getOrderItemTable();
    }

    public List<OrderItem> getOrderItems(Order order) {
        return orderItemTable.getOrderItems(mDatabase, order);
    }
}
